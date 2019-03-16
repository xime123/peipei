package com.tshang.peipei.activity.main.message;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.mine.MineWriteActivity;
import com.tshang.peipei.activity.space.adapter.DynamicAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.user.DynamicRequseControl;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestDynamicOfficial.GetDynamicOfficialCallBack;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfo;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfoList;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: DynamicOfficalActivity.java 
 *
 * @Description: 官方话题
 *
 * @author Aaron  
 *
 * @date 2015-8-27 下午3:09:19 
 *
 * @version V1.0   
 */
public class DynamicOfficalActivity extends BaseActivity implements OnRefreshListener2<ListView>, OnItemClickListener, GetDynamicOfficialCallBack {
	private final int REFRESH_VIEW = 1232;
	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = -1;
	private final int TYPE = 2;

	private PullToRefreshListView mListView;
	private TextView emptyTextView;
	private ImageView rightiv;

	private int topicid;
	private int systemid;
	private String title;
	private DynamicAdapter mAdapter;

	protected boolean isRefresh = true;

	@Override
	protected void initData() {
		mTitle.setText("#" + title + "#");
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mListView.setRefreshing();
			}
		}, 500);
	}

	@Override
	protected void initRecourse() {
		topicid = getIntent().getExtras().getInt("topicid");
		title = getIntent().getExtras().getString("title");
		systemid = getIntent().getExtras().getInt("systemid");

		mBackText = (TextView) findViewById(R.id.dynamic_official_title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTextRight = (TextView) findViewById(R.id.dynamic_official_title_tv_right);
		mTextRight.setVisibility(View.GONE);
		rightiv = (ImageView) findViewById(R.id.dynamic_official_title_iv_right);
		rightiv.setImageResource(R.drawable.broadcast_icon_write);
		rightiv.setVisibility(View.VISIBLE);
		rightiv.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.dynamic_official_title_tv_mid);

		mListView = (PullToRefreshListView) findViewById(R.id.dynamic_official_listview);
		emptyTextView = (TextView) findViewById(R.id.dynamic_official_empty_tv);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		mAdapter = new DynamicAdapter(this, mHandler, true, systemid, 2);
		mListView.setAdapter(mAdapter);
	}

	@Override
	protected int initView() {
		return R.layout.activity_dynamic_official;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.DYNAMIC_OFFICIAL_SUCCESS://获取成功
			mListView.onRefreshComplete();

			if (isRefresh) {
				startLoadPosition = -1;
				mAdapter.clearList();
			}

			DynamicsInfoList infoLists = (DynamicsInfoList) msg.obj;
			ArrayList<DynamicsInfo> list = new ArrayList<DynamicsInfo>();
			for (int i = (infoLists.size() - 1); i >= 0; i--) {
				list.add((DynamicsInfo) infoLists.get(i));
			}
			mAdapter.appendToList(list);

			if (!isRefresh) {
				BaseUtils.showTost(this, getResources().getString(R.string.namypic_data_null));
				mListView.setMode(Mode.PULL_FROM_START);
			} else {
				if (infoLists == null || infoLists.size() == 0) {
					mListView.getRefreshableView().setEmptyView(emptyTextView);
				} else {
					mListView.setMode(Mode.BOTH);
				}
			}
			break;
		case HandlerValue.DYNAMIC_OFFICIAL_ERROR://获取失败
			mListView.onRefreshComplete();
			BaseUtils.showTost(this, getResources().getString(R.string.toast_login_failure));
			break;
		case REFRESH_VIEW:
			isRefresh = true;
			mListView.setRefreshing();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_left:
			this.finish();
			break;
		case R.id.dynamic_official_title_iv_right:
			Bundle bundle = new Bundle();
			bundle.putString(MineWriteActivity.FROM_FLAG, MineWriteActivity.OFFICIAL_DYNAMIC);
			bundle.putString("topic", title);
			bundle.putInt("topicid", topicid);
			BaseUtils.openActivity(this, MineWriteActivity.class, bundle);
			break;
		case R.id.dynamic_official_title_tv_left:
			this.finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 获取动态数据
	 * @author Aaron
	 *
	 * @param start 开始位置
	 * @param num 数量
	 */
	private void getDynamicData(int start, int num) {
		DynamicRequseControl control = new DynamicRequseControl();
		control.getDynamicOfficial(topicid, start, num, TYPE, this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		startLoadPosition = -1;
		getDynamicData(startLoadPosition, LOADCOUNT);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		startLoadPosition = startLoadPosition - LOADCOUNT;
		getDynamicData(startLoadPosition, LOADCOUNT);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DynamicsInfo info = (DynamicsInfo) mAdapter.getItem(position - 1);
		Bundle bundle = new Bundle();
		bundle.putInt(DynamicDetailsActivity.TOPICID_FLAG, info.topicid.intValue());
		bundle.putInt(DynamicDetailsActivity.TOPIUID_FLAG, info.uid.intValue());
		bundle.putInt(DynamicDetailsActivity.ANONYMOUS_FLAG, info.isanonymous.intValue());
		bundle.putInt(DynamicDetailsActivity.TYPE_FLAG, info.dynamicstype.intValue());
		bundle.putInt(DynamicDetailsActivity.SYSTEMID_FLAG, systemid);
		bundle.putInt(DynamicDetailsActivity.STATE_FLAG, info.dynamicsstatus.intValue());
		bundle.putInt(DynamicDetailsActivity.FROM_FLAG, 0);
		BaseUtils.openActivity(this, DynamicDetailsActivity.class, bundle);
	}

	@Override
	public void onSuccess(int code, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.DYNAMIC_OFFICIAL_SUCCESS;
		msg.arg1 = code;
		msg.obj = obj;
		mHandler.sendMessage(msg);

	}

	@Override
	public void onError(int code) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.DYNAMIC_OFFICIAL_ERROR;
		msg.arg1 = code;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onEvent(NoticeEvent event) {
		super.onEvent(event);
		switch (event.getFlag()) {
		case NoticeEvent.NOTICE86:
			mHandler.sendEmptyMessage(REFRESH_VIEW);
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
