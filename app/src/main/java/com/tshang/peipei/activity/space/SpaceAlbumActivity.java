package com.tshang.peipei.activity.space;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.biz.user.UserAlbumBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetAlbumList;

/**
 * @Title: 相册列表
 *
 * @Description: 显示相册列表
 *
 * @author vactor
 *
 * @version V1.0   
 */
public class SpaceAlbumActivity extends BaseActivity implements OnItemClickListener, BizCallBackGetAlbumList {

	private static final int ERROR = -1;
	private static final int GET_ALBUM_LIST = 2;

	private GridView mAlbumGridView;
	private List<AlbumInfo> mPhotoList;
	private SpaceAlbumAdapter mPhotosAdapter;
	private LinearLayout mPhotoNew, mPhotoManage, mPhotoDelete, mPhotoCancel;

	private UserAlbumBiz mUserAlbumBiz;
	private BAHandler mHandler;
	private int mUid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		mUid = intent.getIntExtra(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, -1);
		mUserAlbumBiz = new UserAlbumBiz();

		initHandler();
		initUI();
		setListener();

		//加载相册
		BaseUtils.showDialog(this, R.string.loading);
		mUserAlbumBiz.getAlbumList(this, 0, 10, 0, 2100000000, this);

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
				switch (msg.what) {

				case GET_ALBUM_LIST:
					AlbumInfoList list = (AlbumInfoList) msg.obj;
					if (null != list) {
						mPhotoList.addAll(list);
					}
					BaseUtils.cancelDialog();
					mPhotosAdapter.notifyDataSetChanged();
					break;
				case ERROR:
					break;
				}
			}
		};
	}

	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mBackText.setText(R.string.private_page);
		mBackText.setOnClickListener(this);
		mTitle.setText(R.string.photo_album);
		mAlbumGridView = (GridView) findViewById(R.id.photos_gvw);
		mPhotoList = new ArrayList<AlbumInfo>();
		mPhotosAdapter = new SpaceAlbumAdapter(this, mPhotoList);
		mAlbumGridView.setAdapter(mPhotosAdapter);
		mPhotoNew = (LinearLayout) findViewById(R.id.photo_album_new);
		mPhotoManage = (LinearLayout) findViewById(R.id.photo_album_manage);
		mPhotoDelete = (LinearLayout) findViewById(R.id.photo_album_delete);
		mPhotoCancel = (LinearLayout) findViewById(R.id.photo_album_cancel);
		findViewById(R.id.photo_album_bottom_rl).setVisibility(View.GONE);

	}

	private void setListener() {
		mPhotoNew.setOnClickListener(this);
		mPhotoManage.setOnClickListener(this);
		mPhotoDelete.setOnClickListener(this);
		mPhotoCancel.setOnClickListener(this);
		mAlbumGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(SpaceAlbumActivity.this, SpaceNetPhotosListActivity.class);
		//将相册ID传到上传界面
		intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, mPhotoList.get(position).id.intValue());
		intent.putExtra(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, mUid);
		startActivity(intent);
		overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);

	}

	@Override
	public void getAlbumListCallBack(int retcode, AlbumInfoList albumList) {
		HandlerUtils.sendHandlerMessage(mHandler, GET_ALBUM_LIST, albumList);
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
