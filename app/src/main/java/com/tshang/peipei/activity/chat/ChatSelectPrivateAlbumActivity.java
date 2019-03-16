package com.tshang.peipei.activity.chat;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.WheelActivity;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.biz.user.UserAlbumBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetAlbumList;
import com.tshang.peipei.model.entity.ChatAlbumEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.view.PullToRefreshHeaderGridView;

import de.greenrobot.event.EventBus;

/**
 * @Title: 相册列表
 *
 * @Description: 显示相册列表
 *
 * @author allen
 *
 * @version V1.0   
 */
public class ChatSelectPrivateAlbumActivity extends BaseActivity implements OnItemClickListener, BizCallBackGetAlbumList {

	private static final int ERROR = -1;
	private static final int GET_ALBUM_LIST = 2;

	private int requestCode = 10;

	private PullToRefreshHeaderGridView mAlbumGridView;
	private List<AlbumInfo> mPhotoList;
	private ChatSelectPrivateAlbumAdapter mPhotosAdapter;
	private TextView etLoyalty;

	private UserAlbumBiz mUserAlbumBiz;
	private BAHandler mHandler;

	private int mCurrSelect = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photosalbum);
		mUserAlbumBiz = new UserAlbumBiz();

		initHandler();
		initUI();
		setListener();

		//加载相册
		getAlbum(0, 30);

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

	public void onEventMainThread(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE26) {
			mPhotoList.clear();
			getAlbum(0, 30);
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
				switch (msg.what) {
				case GET_ALBUM_LIST:
					AlbumInfoList list = (AlbumInfoList) msg.obj;
					if (null != list) {
						mPhotoList.addAll(list);
					}
					mPhotosAdapter.notifyDataSetChanged();
					break;

				case BAConstants.HandlerType.INPUT_MESSAGE:
					Intent intent = new Intent(ChatSelectPrivateAlbumActivity.this, WheelActivity.class);
					startActivityForResult(intent, requestCode);
					break;

				case ERROR:
					break;
				}
			}
		};
	}

	private void getAlbum(int start, int num) {
		mUserAlbumBiz.getAlbumList(this, 0, 10, 0, 2100000000, this);
	}

	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.cancel);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);

		mTitle.setText(R.string.photo_album);
		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setText(R.string.send);
		mAlbumGridView = (PullToRefreshHeaderGridView) findViewById(R.id.photos_gvw);
		mPhotoList = new ArrayList<AlbumInfo>();
		mPhotosAdapter = new ChatSelectPrivateAlbumAdapter(this, mPhotoList);
		mAlbumGridView.setAdapter(mPhotosAdapter);
		findViewById(R.id.photo_album_bottom_rl).setVisibility(View.GONE);
		findViewById(R.id.photos_title_text).setVisibility(View.VISIBLE);

	}

	private void setListener() {
		mAlbumGridView.setOnItemClickListener(this);
		mTextRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			if (mCurrSelect != -1) {
				Intent intent = new Intent();

				AlbumInfo info = mPhotoList.get(mCurrSelect);

				ChatAlbumEntity chatEntity = new ChatAlbumEntity();
				chatEntity.accessloyalty = info.accessloyalty;
				chatEntity.albumdesc = info.albumdesc;
				chatEntity.albumname = info.albumname;
				chatEntity.coverpic = info.coverpic;
				chatEntity.coverpicid = info.coverpicid;
				chatEntity.coverpickey = info.coverpickey;
				chatEntity.createtime = info.createtime;
				chatEntity.id = info.id;
				chatEntity.lastupdatetime = info.lastupdatetime;
				chatEntity.photototal = info.photototal;

				intent.putExtra("selectAlbum", chatEntity);

				setResult(RESULT_OK, intent);
				finish();
			} else {
				BaseUtils.showTost(this, "请先选择一个相册");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mCurrSelect = position;
		mPhotosAdapter.freshAdpaterByChecked(position);
	}

	@Override
	public void getAlbumListCallBack(int retcode, AlbumInfoList albumList) {
		HandlerUtils.sendHandlerMessage(mHandler, GET_ALBUM_LIST, albumList);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initRecourse() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int initView() {
		return R.layout.activity_photosalbum;
	}

}
