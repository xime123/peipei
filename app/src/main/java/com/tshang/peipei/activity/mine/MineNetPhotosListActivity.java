package com.tshang.peipei.activity.mine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.ImageDetailActivity;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.jobs.ReUploadPhotosJob;
import com.tshang.peipei.model.biz.user.UserAlbumBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackDeleteAlbumPic;
import com.tshang.peipei.model.bizcallback.BizCallBackGetAlbumPhotoList;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfo;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfoList;
import com.tshang.peipei.storage.database.entity.PhotoDatabaseEntity;
import com.tshang.peipei.storage.database.operate.PhotoOperate;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.view.HeaderGridView;
import com.tshang.peipei.view.PullToRefreshHeaderGridView;

public class MineNetPhotosListActivity extends BaseActivity implements OnItemClickListener, BizCallBackGetAlbumPhotoList, BizCallBackDeleteAlbumPic {

	private static final int GETALBUM_PHOTOS = 1;
	private final int DELETE_ALBUMPIC = 3;
	private boolean isRefresh = true;

	private PullToRefreshHeaderGridView mPhotoGridView;
	private LinearLayout mLinUploadStaus;
	private TextView mTxtUploadStaus;
	private ProgressBar mProgressPercent;
	private LinearLayout mLinCancelUpload;
	private LinearLayout mLinReupload;

	private MineNetPhotosListAdapter mPhotosAdapter;
	private LinearLayout mPhotoManage, mPhotoDelete;

	private PhotoOperate mPhotoOperate;
	//相册ID
	private int mAlbumId;
	private static final int LOADCOUNT = 15;
	private int startLoadPosition = -1;
	private boolean isSelf = false;
	private int whosId = 0;
	private int photoCount = 0;//相片张数
	private int sex = 0;

	private HashMap<String, PhotoInfo> photoMap = new HashMap<String, PhotoInfo>();
	private GoGirlUserInfo mUserEntity = null;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAlbumId = getIntent().getIntExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, -1);
		whosId = getIntent().getIntExtra("viewpeopleuid", 0);
		sex = getIntent().getIntExtra("viewpeoplesex", Gender.FEMALE.getValue());
		if (mUserEntity != null && mUserEntity.uid.intValue() == whosId) {//说明是自己查看
			isSelf = true;
		}
		initUI();
		setListener();
		BaseUtils.showDialog(this, R.string.loading);
		getNetPhotos(startLoadPosition, LOADCOUNT);

		if (BAApplication.mLocalUserInfo != null) {
			mPhotoOperate = PhotoOperate.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "");
			//正在上传的照片
			boolean isHaveUploading = mPhotoOperate.getPhotoStatus(BAConstants.UploadStatus.UPLOADING.getValue(), mAlbumId);
			if (isHaveUploading) {
				mLinUploadStaus.setVisibility(View.VISIBLE);
				mLinReupload.setVisibility(View.GONE);
				mTxtUploadStaus.setText("正在上传,进度获取中...");
			}
			//上传失败的照片
			PhotoDatabaseEntity photo = mPhotoOperate.getPhotoList(BAConstants.UploadStatus.FAILURE.getValue(), mAlbumId);
			if (null != photo) {
				mLinReupload.setVisibility(View.VISIBLE);
				mLinUploadStaus.setVisibility(View.VISIBLE);
				String[] images = photo.getImageKeys().split(";");
				mTxtUploadStaus.setText("部分上传失败!,已成功上传" + (photo.getTotal() - images.length) + "张");
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public void onEventMainThread(NoticeEvent event) {

		//开始上传
		if (event.getFlag() == NoticeEvent.NOTICE7) {
			mLinUploadStaus.setVisibility(View.VISIBLE);
			if (!isNetworkConnected(this)) {
				mLinReupload.setVisibility(View.GONE);
				mTxtUploadStaus.setText("上传失败");
				mLinReupload.setVisibility(View.VISIBLE);
			} else {
				mLinReupload.setVisibility(View.GONE);
				mTxtUploadStaus.setText("准备上传,请稍侯...");
			}
		}

		//中止上传
		if (event.getFlag() == NoticeEvent.NOTICE8) {
			BaseUtils.showTost(this, "用户已取消上传");
			mLinUploadStaus.setVisibility(View.GONE);
		}
		//已经存在上传任务
		if (event.getFlag() == NoticeEvent.NOTICE6) {
			BaseUtils.showTost(this, "正在上传,请稍侯");
		}

		//上传进度
		if (event.getFlag() == NoticeEvent.NOTICE5) {
			int total = event.getNum();
			int needUpload = event.getNum2();
			int now = total - needUpload;
			int charmnum = event.getNum3();
			BaseLog.i("vactor_log", "size:" + needUpload + "total:" + total + "per:" + now);
			mProgressPercent.setMax(total);
			mProgressPercent.setProgress(now);

			mTxtUploadStaus.setText("正在上传" + now + "/" + total);

			if (event.getRetcode() == 0) {
				if (needUpload == 0) {
					if (BAApplication.mLocalUserInfo != null) {
						//						MobclickAgent.onEvent(this, "zhaopianrenshu", BAApplication.mUserEntity.getUid() + "");
						if (BAApplication.mLocalUserInfo.sex.intValue() == Gender.FEMALE.getValue()) {
//							MobclickAgent.onEvent(this, "zhaopiancishu");
						} else {
//							MobclickAgent.onEvent(this, "NanXingShangChuanZhaoPianCiShu");
						}
					}
					if (charmnum > 0) {
						BaseUtils.showTost(this, "发布成功!魅力值增加" + charmnum);
					} else {
						BaseUtils.showTost(this, "上传成功!");
					}
					sendNoticeEvent(NoticeEvent.NOTICE26);
					mLinUploadStaus.setVisibility(View.GONE);
				}
				//重新查询
				setRefreshData();
			}
			if (event.getRetcode() != 0) {
				BaseUtils.showTost(this, "上传失败!");
				mLinReupload.setVisibility(View.VISIBLE);
				mLinUploadStaus.setVisibility(View.VISIBLE);
				mTxtUploadStaus.setText("部分上传失败!,已成功上传" + (int) now + "张");
				if (sex == Gender.FEMALE.getValue()) {
					sendNoticeEvent(NoticeEvent.NOTICE26);
				}
			}
			mTitle.setText(albumName + "(" + (photoCount + (int) now) + "张)");
		}

	}

	private void getNetPhotos(int start, int num) {
		new UserAlbumBiz().getAlbumPhotoList(this, whosId, mAlbumId, start, num, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		int retCode = -1;
		switch (msg.what) {
		case GETALBUM_PHOTOS:
			mPhotoGridView.onRefreshComplete();
			if (msg.arg1 == 0) {
				if (msg.arg2 > 0) {
					((ImageView) findViewById(R.id.iv_photo_manage)).setImageResource(R.drawable.album_icon_manage);
					((TextView) findViewById(R.id.tv_photo_manage)).setTextColor(getResources().getColor(R.color.peach));
					findViewById(R.id.photolist_photo_manage).setOnClickListener(this);
				}
				mTitle.setText(albumName + "(" + msg.arg2 + "张)");
				PhotoInfoList list = (PhotoInfoList) msg.obj;
				if (null != list && !list.isEmpty()) {

					//					PhotoInfo photo = (PhotoInfo) list.get(0);
					if (isRefresh) {
						mPhotosAdapter.clearList();
						if (list.size() == LOADCOUNT) {
							mPhotoGridView.setMode(Mode.BOTH);
						}
					}
					if (list.size() < LOADCOUNT) {
						mPhotoGridView.setMode(Mode.PULL_FROM_START);//如果加载更多小于15条数据，就不能够继续上拉了
					}
					Collections.reverse(list);
					mPhotosAdapter.appendToList(list);
				} else {
					mPhotoGridView.setMode(Mode.PULL_FROM_START);
				}
			}

			break;

		case DELETE_ALBUMPIC:
			retCode = msg.arg1;
			if (retCode == 0) {
				int picId = msg.arg2;
				mPhotosAdapter.removeItem(picId);
				sendNoticeEvent(NoticeEvent.NOTICE26);
			}
			break;

		default:
			break;
		}

	}

	private String albumName = "";//相册名字

	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		if (sex == Gender.FEMALE.getValue()) {
			mBackText.setText(R.string.photo_album);
		} else {
			mBackText.setText(R.string.private_page);
		}
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		photoCount = getIntent().getIntExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMCOUNT, 0);
		albumName = getIntent().getStringExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMNAME);
		mTitle.setText(albumName + "(" + photoCount + "张)");
		mLinUploadStaus = (LinearLayout) findViewById(R.id.net_photolist_ll_upload);
		mTxtUploadStaus = (TextView) findViewById(R.id.net_photolist_tv_uplod_status);
		mLinReupload = (LinearLayout) findViewById(R.id.net_photolist_ll_reupload);

		mProgressPercent = (ProgressBar) findViewById(R.id.net_photolist_pb);

		mPhotoGridView = (PullToRefreshHeaderGridView) findViewById(R.id.photolist_gvw);

		mPhotosAdapter = new MineNetPhotosListAdapter(this);
		mPhotoGridView.setAdapter(mPhotosAdapter);
		mPhotoGridView.setMode(Mode.PULL_FROM_START);
		mPhotoManage = (LinearLayout) findViewById(R.id.photo_album_manager_ll);
		mPhotoDelete = (LinearLayout) findViewById(R.id.photo_album_delete_ll);

		findViewById(R.id.photolist_photo_delete).setOnClickListener(this);
		findViewById(R.id.photolist_photo_cancel).setOnClickListener(this);
		findViewById(R.id.photolist_photo_upload).setOnClickListener(this);

		mLinCancelUpload = (LinearLayout) findViewById(R.id.net_photolist_lin_cancel);
		LinearLayout mLinBottom = (LinearLayout) findViewById(R.id.photolist_photo_bottom);
		if (isSelf) {
			mLinBottom.setVisibility(View.VISIBLE);
		} else {
			mLinBottom.setVisibility(View.GONE);
		}

	}

	private void setListener() {

		mPhotoGridView.setOnItemClickListener(this);
		mLinReupload.setOnClickListener(this);
		mLinCancelUpload.setOnClickListener(this);
		mPhotoGridView.setOnRefreshListener(new PullToRefreshListener());
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.photolist_photo_upload:
			mProgressPercent.setProgress(0);
			Intent intent = new Intent(this, MineDirctorySdcPhotosListActivity.class);
			intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, mAlbumId);
			startActivity(intent);
			overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			break;
		case R.id.photolist_photo_manage:
			clickManage();
			break;
		case R.id.photolist_photo_delete:
			clickDelete();
			break;
		case R.id.photolist_photo_cancel:
			clickCancel();
			break;
		case R.id.net_photolist_ll_reupload:
			BAApplication.getInstance().getJobManager().addJobInBackground(new ReUploadPhotosJob(this, mAlbumId));
			break;
		case R.id.net_photolist_lin_cancel:
			sendNoticeEvent(NoticeEvent.NOTICE4);

			if (mPhotoOperate != null)
				mPhotoOperate.deletePhoto(mAlbumId);
			mPhotoOperate = null;

			mLinUploadStaus.setVisibility(View.GONE);

			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mPhotosAdapter.isManage()) {

			PhotoInfo info = (PhotoInfo) parent.getAdapter().getItem(position);
			if (photoMap.containsKey(info.id.intValue() + "")) {
				photoMap.remove(info.id.intValue() + "");
			} else {
				photoMap.put(info.id.intValue() + "", info);
			}
			mPhotosAdapter.freshAdapterByChecked(photoMap);
		} else {
			List<PhotoInfo> mPhotoList = mPhotosAdapter.getList();
			if (mPhotoList != null && !mPhotoList.isEmpty()) {
				Bundle bundle = new Bundle();
				ArrayList<String> list = new ArrayList<String>();
				for (PhotoInfo photoInfo2 : mPhotoList) {
					list.add(new String(photoInfo2.key));
				}
				bundle.putInt(ImageDetailActivity.POSITION, position);
				bundle.putStringArrayList(ImageDetailActivity.EXTRA_IMAGE, list);
				BaseUtils.openActivity(this, ImageDetailActivity.class, bundle);
			}
		}

	}

	//上拉刷新 ,下拉加载更多刷新 
	class PullToRefreshListener implements OnRefreshListener2<HeaderGridView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
			setRefreshData();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
			isRefresh = false;
			startLoadPosition = startLoadPosition - LOADCOUNT;
			getNetPhotos(startLoadPosition, LOADCOUNT);
		}

	}

	/**
	 * 刷新数据源
	 * @author Jeff
	 *
	 */
	private void setRefreshData() {
		isRefresh = true;
		startLoadPosition = -1;
		mPhotosAdapter.clearList();
		getNetPhotos(startLoadPosition, LOADCOUNT);
	}

	private void clickManage() {
		setBottomTab(true);
		mPhotosAdapter.freshAdapter();
	}

	private void clickDelete() {
		if (photoMap.isEmpty()) {
			BaseUtils.showTost(this, R.string.str_please_select_photo);
			return;
		}
		deleteAlbumPic();
		setBottomTab(false);

		mPhotosAdapter.notifyDataSetChanged();
	}

	public void deleteAlbumPic() {
		UserAlbumBiz albumBiz = new UserAlbumBiz();
		if (BAApplication.mLocalUserInfo != null) {
			Iterator<String> it = photoMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				int photoId = Integer.parseInt(key);
				albumBiz.deleteAlbumPic(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
						BAApplication.mLocalUserInfo.uid.intValue(), mAlbumId, photoId, this);
			}
		}

	}

	private void clickCancel() {
		setBottomTab(false);
		mPhotosAdapter.freshAdpaterExitManage();
	}

	private void setBottomTab(boolean b) {

		Animation mAppShowAction = AnimationUtils.loadAnimation(this, R.anim.popwin_bottom_in);
		mAppShowAction.setDuration(300);
		Animation mAppHiddenAction = AnimationUtils.loadAnimation(this, R.anim.popwin_bottom_out);
		mAppHiddenAction.setDuration(300);
		mAppShowAction.setStartOffset(300);
		if (b) {
			mPhotoManage.setVisibility(View.GONE);
			mPhotoManage.startAnimation(mAppHiddenAction);
			mPhotoDelete.setVisibility(View.VISIBLE);
			mPhotoDelete.startAnimation(mAppShowAction);

		} else {
			mPhotoDelete.setVisibility(View.GONE);
			mPhotoDelete.startAnimation(mAppHiddenAction);
			mPhotoManage.setVisibility(View.VISIBLE);
			mPhotoManage.startAnimation(mAppShowAction);
		}
		mPhotosAdapter.setManage(b);
	}

	@Override
	public void getAlbumPhotoList(int retCode, int total, PhotoInfoList list) {
		sendHandlerMessage(mHandler, GETALBUM_PHOTOS, retCode, total, list);
	}

	@Override
	public void deletePhotoCallBack(int resultCode, int picId) {
		sendHandlerMessage(mHandler, DELETE_ALBUMPIC, resultCode, picId);
	}

	@Override
	protected void initData() {
		mUserEntity = UserUtils.getUserEntity(this);
	}

	@Override
	protected void initRecourse() {

	}

	@Override
	protected int initView() {

		return R.layout.activity_net_photo_list;
	}

}
