package com.tshang.peipei.activity.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.path.android.jobqueue.JobManager;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.ImageDetailActivity;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfo;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.jobs.ReUploadPhotosJob;
import com.tshang.peipei.model.biz.user.UserAlbumBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetAlbumPhotoList;
import com.tshang.peipei.view.PullToRefreshHeaderGridView;

/**
 * @Title: 相片列表界面
 *
 * @Description: 展示相片
 *
 * @author vactor
 *
 * @version V1.0   
 */
public class SpaceNetPhotosListActivity extends BaseActivity implements OnItemClickListener, BizCallBackGetAlbumPhotoList {

	private static final int GETALBUM_PHOTOS = 1;

	private PullToRefreshHeaderGridView mPhotoGridView;
	private LinearLayout mLinCancelUpload;
	private LinearLayout mLinReupload;

	private SpaceNetPhotosListAdapter mPhotosAdapter;

	//相册ID
	private int mAlbumId;
	private int mUid;
	private BAHandler mHander;
	private JobManager mJobManager;
	private String mStrTitle, mStrBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		mAlbumId = intent.getIntExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, -1);
		mUid = intent.getIntExtra(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, -1);
		mStrTitle = intent.getStringExtra(BAConstants.IntentType.PHOTOS_TITLE);
		mStrBack = intent.getStringExtra(BAConstants.IntentType.PHOTOS_BACK);

		if (TextUtils.isEmpty(mStrBack)) {
			mStrBack = getString(R.string.photo_album);
		}
		if (TextUtils.isEmpty(mStrTitle)) {
			mStrTitle = getString(R.string.photo_public);
		}

		initHandler();
		initUI();
		setListener();
		BaseUtils.showDialog(this, R.string.loading);
		getNetPhotos();

	}

	private void getNetPhotos() {
		mPhotosAdapter.clearList();
		new UserAlbumBiz().getAlbumPhotoList(this, mUid, mAlbumId, 0, 50, this);
	}

	private void initHandler() {
		mHander = new BAHandler(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				try {
					super.handleMessage(msg);
				} catch (NullPointerException e) {
					return;
				}
				switch (msg.what) {
				case GETALBUM_PHOTOS:
					mTitle.setText(mStrTitle + "(" + msg.arg2 + "张)");
					BaseUtils.cancelDialog();
					if (msg.arg1 == 0) {
						PhotoInfoList list = (PhotoInfoList) msg.obj;
						if (null != list && list.size() > 0) {
							Collections.reverse(list);
							mPhotosAdapter.setList(list);
						}
					}
					break;

				default:
					break;
				}

			}
		};
	}

	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);

		mLinReupload = (LinearLayout) findViewById(R.id.net_photolist_ll_reupload);

		mBackText.setText(mStrBack);
		mBackText.setOnClickListener(this);
		mTitle.setText(mStrTitle);

		mPhotoGridView = (PullToRefreshHeaderGridView) findViewById(R.id.photolist_gvw);

		mPhotosAdapter = new SpaceNetPhotosListAdapter(this);
		mPhotoGridView.setAdapter(mPhotosAdapter);

		mLinCancelUpload = (LinearLayout) findViewById(R.id.net_photolist_lin_cancel);

	}

	private void setListener() {

		mPhotoGridView.setOnItemClickListener(this);
		mLinReupload.setOnClickListener(this);
		mLinCancelUpload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.net_photolist_ll_reupload:
			mJobManager.addJobInBackground(new ReUploadPhotosJob(this, mAlbumId));
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mPhotosAdapter.isManage()) {
			mPhotosAdapter.notifyDataSetChanged();
		} else {

			List<PhotoInfo> lists = mPhotosAdapter.getList();
			if (lists != null && !lists.isEmpty()) {
				Bundle bundle = new Bundle();
				ArrayList<String> list = new ArrayList<String>();
				for (PhotoInfo photoInfo2 : lists) {
					list.add(new String(photoInfo2.key));
				}
//				System.out.println("是不是走这里呢???");
				bundle.putInt(ImageDetailActivity.POSITION, position);
				bundle.putStringArrayList(ImageDetailActivity.EXTRA_IMAGE, list);
				bundle.putBoolean(ImageDetailActivity.ISREPORT, true);
				bundle.putInt(ImageDetailActivity.PIC_UID, mUid);
				BaseUtils.openActivity(this, ImageDetailActivity.class, bundle);
			}
		}

	}

	@Override
	public void getAlbumPhotoList(int retCode, int total, PhotoInfoList list) {
		sendHandlerMessage(mHander, GETALBUM_PHOTOS, retCode, total, list);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {

	}

	@Override
	protected int initView() {
		return R.layout.activity_net_photo_list;
	}

}
