package com.tshang.peipei.activity.mine;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.WheelActivity;
import com.tshang.peipei.base.BaseDialog;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.user.UserAlbumBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackCreateAlbum;
import com.tshang.peipei.model.bizcallback.BizCallBackDelAlbum;
import com.tshang.peipei.model.bizcallback.BizCallBackGetAlbumList;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.view.HeaderGridView;
import com.tshang.peipei.view.PullToRefreshHeaderGridView;

/**
 * @Title: 相册列表
 *
 * @Description: 显示相册列表
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MineAlbumActivity extends BaseActivity implements OnItemClickListener, BizCallBackGetAlbumList, BizCallBackCreateAlbum,
		BizCallBackDelAlbum {

	private static final int ERROR = -1;
	private static final int CREATE_ALBUM_SUCCESS = 1;
	private static final int GET_ALBUM_LIST = 2;
	private static final int DELETE_ALBUM = 3;

	private int startLoadPosition = -1;
	private boolean isRefresh = true;
	private int loyal = 50;
	private int requestCode = 10;

	private PullToRefreshHeaderGridView mAlbumGridView;
	private MineAlbumAdapter mPhotosAdapter;
	private LinearLayout mPhotoManage, mPhotoDelete;
	private TextView etLoyalty;

	private BAHandler mHandler;
	private Dialog mCreateDialog;
	private HashMap<String, AlbumInfo> albumMap = new HashMap<String, AlbumInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHandler();
		initUI();
		//加载相册
		BaseUtils.showDialog(this, R.string.loading);
		getAlbum(startLoadPosition, 9);

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == requestCode && null != arg2) {
			int value = arg2.getIntExtra(WheelActivity.RESULT, 0);
			if (null != etLoyalty) {
				etLoyalty.setText(value + "");
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void onEventMainThread(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE26) {
			setRefreshData();
		}
	}

	private void initHandler() {
		mHandler = new BAHandler(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				try {
					super.handleMessage(msg);
				} catch (NullPointerException e) {
					return;
				}
				int retCode = -1;
				switch (msg.what) {
				case GET_ALBUM_LIST:
					mAlbumGridView.onRefreshComplete();
					AlbumInfoList list = (AlbumInfoList) msg.obj;
					if (null != list && !list.isEmpty()) {
						Collections.reverse(list);
						if (isRefresh) {
							mPhotosAdapter.clearList();
							if (list.size() == 9) {
								mAlbumGridView.setMode(Mode.BOTH);//允许加载更多
							}
						}
						if (list.size() < 9) {
							mAlbumGridView.setMode(Mode.PULL_FROM_START);//如果加载更多小于10条数据，就不能够继续上拉了
						}
						mPhotosAdapter.appendToList(list);
					} else {
						mAlbumGridView.setMode(Mode.PULL_FROM_START);
					}
					BaseUtils.cancelDialog();
					break;
				case BAConstants.HandlerType.CREATE_ALBUM_SURE:
					String name = (String) msg.obj;
					loyal = msg.arg1;
					new UserAlbumBiz().createAlbum(MineAlbumActivity.this, name, loyal, name, MineAlbumActivity.this);
					break;
				case CREATE_ALBUM_SUCCESS:
					retCode = msg.arg1;
					if (retCode == 0) {//添加刚创建的相册
//						MobclickAgent.onEvent(MineAlbumActivity.this, "ChuangJianSiMiXiangCeZongCiShu");
						//						if (BAApplication.mUserEntity != null) {
						//							MobclickAgent.onEvent(MineAlbumActivity.this, "ChuangJianSiMiXiangCeZongRenShu", BAApplication.mUserEntity.getUid() + "");
						//						}
						BaseUtils.showTost(MineAlbumActivity.this, R.string.str_only_yourself);
						AlbumInfo albumInfo = (AlbumInfo) msg.obj;
						albumInfo.photototal = BigInteger.valueOf(0);
						albumInfo.accessloyalty = BigInteger.valueOf(loyal);
						if (albumInfo != null) {
							mPhotosAdapter.appendPositionToList(0, albumInfo);
						}
					} else if (retCode == -28011) {
						BaseUtils.showTost(MineAlbumActivity.this, R.string.str_album_exist);
					} else {
						BaseUtils.showTost(MineAlbumActivity.this, R.string.str_new_album_failed);
					}
					break;
				case BAConstants.HandlerType.INPUT_NULL:
					BaseUtils.showTost(MineAlbumActivity.this, "请输入相册名字");
					break;

				case BAConstants.HandlerType.INPUT_NULL2:
					BaseUtils.showTost(MineAlbumActivity.this, "请输入访问值");
					break;

				case BAConstants.HandlerType.INPUT_MESSAGE:
					Intent intent = new Intent(MineAlbumActivity.this, WheelActivity.class);
					startActivityForResult(intent, requestCode);
					break;

				case DELETE_ALBUM:
					BaseUtils.cancelDialog();
					retCode = msg.arg1;
					if (retCode == 0) {
						setRefreshData();
					} else {
						BaseUtils.showTost(MineAlbumActivity.this, R.string.str_delete_failed);
					}
					break;

				case ERROR:
					break;
				}
			}
		};
	}

	private void getAlbum(int start, int num) {
		new UserAlbumBiz().getAlbumList(this, start, num, -1, 10000, this);
	}

	/**
	 * 刷新数据源
	 * @author Jeff
	 *
	 */
	private void setRefreshData() {
		isRefresh = true;
		startLoadPosition = -1;
		getAlbum(startLoadPosition, 9);
	}

	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.private_page);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);

		mTitle.setText(R.string.photo_album);
		mAlbumGridView = (PullToRefreshHeaderGridView) findViewById(R.id.photos_gvw);
		mAlbumGridView.setMode(Mode.PULL_FROM_START);
		mAlbumGridView.setScrollingWhileRefreshingEnabled(true);
		mAlbumGridView.setOnRefreshListener(new OnRefreshListener2<HeaderGridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
				setRefreshData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
				isRefresh = false;
				startLoadPosition = startLoadPosition - 9;
				getAlbum(startLoadPosition, 9);
			}
		});
		mPhotosAdapter = new MineAlbumAdapter(this);
		mAlbumGridView.setAdapter(mPhotosAdapter);
		mPhotoManage = (LinearLayout) findViewById(R.id.photo_album_manager_ll);
		mPhotoDelete = (LinearLayout) findViewById(R.id.photo_album_delete_ll);

		findViewById(R.id.photo_album_manage).setOnClickListener(this);
		findViewById(R.id.photo_album_delete).setOnClickListener(this);
		findViewById(R.id.photo_album_new).setOnClickListener(this);
		findViewById(R.id.photo_album_cancel).setOnClickListener(this);

		mAlbumGridView.setOnItemClickListener(this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.photo_album_new:
			mCreateDialog = BaseDialog.createDialogByTwoInput2(this, null, 0, mHandler);
			etLoyalty = (TextView) mCreateDialog.findViewById(R.id.dialog_edit_second);
			etLoyalty.setText("50");
			etLoyalty.setKeyListener(new DigitsKeyListener(false, true));
			mCreateDialog.show();
			break;
		case R.id.photo_album_manage:
			clickManage();
			break;
		case R.id.photo_album_delete:
			clickDelete();
			break;
		case R.id.photo_album_cancel:
			clickCancel();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mPhotosAdapter.isManage()) {
			//mPhotoList.get(position).setCheck(!mPhotoList.get(position).isCheck());
			AlbumInfo album = (AlbumInfo) parent.getAdapter().getItem(position);
			//公开相册不能删除
			if (album.accessloyalty.intValue() <= 0) {
				BaseUtils.showTost(MineAlbumActivity.this, "公开相册不能删除");
				return;
			}
			if (albumMap.containsKey(album.id.intValue() + "")) {
				albumMap.remove(album.id.intValue() + "");
			} else {
				albumMap.put(album.id.intValue() + "", album);
			}
			mPhotosAdapter.freshAdpaterByChecked(albumMap);
		} else {
			Intent intent = new Intent(MineAlbumActivity.this, MineNetPhotosListActivity.class);
			AlbumInfo info = mPhotosAdapter.getList().get(position);
			if (info != null) {
				//将相册ID传到上传界面
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, info.id.intValue());
				String albumName = "相册";
				if (info.accessloyalty.intValue() > 0) {
					albumName = new String(info.albumname);
				}
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMNAME, albumName);
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMCOUNT, info.photototal.intValue());
				GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
				if (userEntity != null) {
					intent.putExtra("viewpeopleuid", userEntity.uid.intValue());
				}
				startActivity(intent);

				overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			}
		}

	}

	private void clickManage() {
		setBottomTab(true);
		mPhotosAdapter.freshAdapter();
	}

	private void clickDelete() {
		if (albumMap.isEmpty()) {
			BaseUtils.showTost(MineAlbumActivity.this, R.string.str_please_select_album);
			return;
		}
		BaseUtils.showDialog(this, R.string.loading);
		deleteAlbum();
		setBottomTab(false);
		mPhotosAdapter.notifyDataSetChanged();
	}

	public void deleteAlbum() {
		UserAlbumBiz albumBiz = new UserAlbumBiz();

		Iterator<String> it = albumMap.keySet().iterator();
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if (userEntity != null) {
			while (it.hasNext()) {
				String key = it.next();
				int albumId = Integer.parseInt(key);
				albumBiz.deleteAlbum(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), albumId, MineAlbumActivity.this);
			}
		}

	}

	private void clickCancel() {
		setBottomTab(false);
		albumMap.clear();
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
	public void getAlbumListCallBack(int retcode, AlbumInfoList albumList) {
		HandlerUtils.sendHandlerMessage(mHandler, GET_ALBUM_LIST, albumList);
	}

	@Override
	public void createAlbumCallBack(int retCode, AlbumInfo album) {//-28011相册已经存在,0成功
		sendHandlerMessage(mHandler, CREATE_ALBUM_SUCCESS, retCode, album);
	}

	@Override
	public void deleteAlbumCallBack(int resultCode) {
		sendHandlerMessage(mHandler, DELETE_ALBUM, resultCode);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {

	}

	@Override
	protected int initView() {
		return R.layout.activity_photosalbum;
	}

}
