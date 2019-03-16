package com.tshang.peipei.activity.space;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.main.message.DynamicDetailsActivity;
import com.tshang.peipei.activity.mine.MineWriteActivity;
import com.tshang.peipei.activity.space.adapter.DynamicAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.user.DynamicRequseControl;
import com.tshang.peipei.model.request.RequestDynamicAll.GetDynamicAllCallBack;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfo;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfoList;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: SpaceUserDynamicActivity.java 
 *
 * @Description: 用户新动态列表 
 *
 * @author Aaron  
 *
 * @date 2015-9-17 上午9:53:02 
 *
 * @version V1.0   
 */
public class SpaceUserDynamicActivity extends BaseActivity implements GetDynamicAllCallBack, OnRefreshListener2<ListView>, OnItemClickListener {

	private final static String TAG = "Aaron";
	public final static String UID_FLAG = "uid";
	public final static String SEX_FLAG = "sex";

	private PullToRefreshListView mListView;
	private TextView emptyTextView;
	private ImageView rightIv;

	private DynamicRequseControl control;

	private DynamicAdapter adapter;

	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = -1;
	private int uid;
	private int sex;

	private boolean isRefresh = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		control = new DynamicRequseControl();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mListView.setRefreshing();
			}
		}, 600);
	}

	@Override
	protected void initData() {
		uid = getIntent().getExtras().getInt(UID_FLAG);
		sex = getIntent().getExtras().getInt(SEX_FLAG);
		if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == uid) {
			mTitle.setText("我的动态");
		} else {
			if (sex == 0) {
				mTitle.setText("她的动态");
			} else {
				mTitle.setText("他的动态");
			}
		}
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.dynamic_official_title_tv_left);
		rightIv=(ImageView)findViewById(R.id.dynamic_official_title_iv_right);
		rightIv.setImageResource(R.drawable.broadcast_icon_write);
		rightIv.setVisibility(View.VISIBLE);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.dynamic_official_title_tv_mid);
		mListView = (PullToRefreshListView) findViewById(R.id.dynamic_official_listview);
		emptyTextView = (TextView) findViewById(R.id.dynamic_official_empty_tv);

		initListView();
		
		findViewById(R.id.dynamic_official_title_lin_right).setOnClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_dynamic_official;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_DYNAMIC_SUCCESS:
			mListView.onRefreshComplete();

			DynamicsInfoList infoList = (DynamicsInfoList) msg.obj;

			if (isRefresh) {
				adapter.clearList();
			}

			if (infoList != null && infoList.size() > 0) {
				ArrayList<DynamicsInfo> list = new ArrayList<DynamicsInfo>();
				for (int i = (infoList.size() - 1); i >= 0; i--) {
					list.add((DynamicsInfo) infoList.get(i));
				}
				adapter.appendToList(list);
				mListView.setMode(Mode.BOTH);
			} else {
				if (isRefresh) {
					mListView.getRefreshableView().setEmptyView(emptyTextView);
				} else {
					mListView.setMode(Mode.PULL_FROM_START);
					BaseUtils.showTost(this, "没有更多动态数据");

				}
			}
			break;
		case HandlerValue.GET_DYNAMIC_ERROR:
			BaseUtils.showDialog(this, R.string.get_data_failure);
			mListView.onRefreshComplete();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.dynamic_official_title_tv_left:
			this.finish();
			break;
		case R.id.dynamic_official_title_lin_right:
			Bundle bundle = new Bundle();
			bundle.putString(MineWriteActivity.FROM_FLAG, MineWriteActivity.PUBLIC_DYNAMIC);
			BaseUtils.openActivity(this, MineWriteActivity.class, bundle);
			break;

		default:
			break;
		}
	}

	private void initListView() {
		adapter = new DynamicAdapter(this, mHandler, false, -1, 4);
		mListView.setAdapter(adapter);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
	}

	private void getDynamicData(int start, int num, int uid) {
		control = new DynamicRequseControl();
		control.getDynamicList(start, num, 1 | (1 << 30), uid, this);
	}

	@Override
	public void onSuccess(int code, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.GET_DYNAMIC_SUCCESS;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onError(int code) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.GET_DYNAMIC_ERROR;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		startLoadPosition = -1;
		getDynamicData(startLoadPosition, LOADCOUNT, uid);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		startLoadPosition = startLoadPosition - LOADCOUNT;
		getDynamicData(startLoadPosition, LOADCOUNT, uid);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DynamicsInfo info = (DynamicsInfo) adapter.getItem(position - 1);
		Bundle bundle = new Bundle();
		bundle.putInt(DynamicDetailsActivity.TOPICID_FLAG, info.topicid.intValue());
		bundle.putInt(DynamicDetailsActivity.TOPIUID_FLAG, info.uid.intValue());
		bundle.putInt(DynamicDetailsActivity.ANONYMOUS_FLAG, info.isanonymous.intValue());
		bundle.putInt(DynamicDetailsActivity.TYPE_FLAG, 1 | (1 << 30));
		bundle.putInt(DynamicDetailsActivity.SYSTEMID_FLAG, -1);
		bundle.putInt(DynamicDetailsActivity.STATE_FLAG, info.dynamicsstatus.intValue());
		bundle.putInt(DynamicDetailsActivity.FROM_FLAG, 0);
		BaseUtils.openActivity(this, DynamicDetailsActivity.class, bundle);
	}
}
