package com.tshang.peipei.activity.main.message;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.DeleteDynamicInfoDialog;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.activity.space.adapter.DynamicAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.user.DynamicRequseControl;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestDynamicAll.GetDynamicAllCallBack;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfo;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfoList;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @Title: DynamicFragmentAll.java 
 *
 * @Description: 我的动态 
 *
 * @author Aaron  
 *
 * @date 2015-8-12 上午9:55:10 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class DynamicFragmentMe extends BaseFragment implements OnRefreshListener2<ListView>, GetDynamicAllCallBack, OnItemClickListener,
		OnItemLongClickListener {

	private final int REFRESH_VIEW = 234;
	private final int TYPE = 1;

	protected PullToRefreshListView mListView;
	private TextView remindTv;

	private DynamicAdapter mAdapter;

	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = -1;

	private boolean isRefresh = true;

	private Dialog dialog;

	private int topicuid;
	private int grobalid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dynamic_me_layout, null);
		findViewById(view);
		initListView();
		//全局缓存处理
		if (BAApplication.getInstance().getMeDynamicLists() != null) {
			Message msg = mHandler.obtainMessage();
			msg.what = HandlerValue.GET_DYNAMIC_SUCCESS;
			msg.obj = BAApplication.getInstance().getMeDynamicLists();
			mHandler.sendMessage(msg);
		}
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mListView.setRefreshing();
			}
		}, 500);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private void findViewById(View v) {
		mListView = (PullToRefreshListView) v.findViewById(R.id.dynamic_me_listview);
		remindTv = (TextView) v.findViewById(R.id.dynamic_me_not_data_tv);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.getRefreshableView().setOnItemLongClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	private void initListView() {
		mAdapter = new DynamicAdapter(getActivity(), mHandler, false, -1, 1);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_DYNAMIC_SUCCESS://获取动态成功
			DialogFactory.dimissDialog(dialog);
			mListView.onRefreshComplete();
			DynamicsInfoList infoList = (DynamicsInfoList) msg.obj;

			if (isRefresh) {
				startLoadPosition = -1;
				mAdapter.clearList();
			}

			if (infoList != null && infoList.size() > 0) {
				ArrayList<DynamicsInfo> list = new ArrayList<DynamicsInfo>();
				for (int i = (infoList.size() - 1); i >= 0; i--) {
					list.add((DynamicsInfo) infoList.get(i));
				}
				mAdapter.appendToList(list);
			}

			if (!isRefresh) {
				if (infoList == null || infoList.size() == 0) {
					startLoadPosition = startLoadPosition + 10;
					BaseUtils.showTost(getActivity(), "没有更多动态数据");
					mListView.setMode(Mode.PULL_FROM_START);
				}
			} else {
				if (infoList == null || infoList.size() == 0) {
					mListView.getRefreshableView().setEmptyView(remindTv);
					mListView.setMode(Mode.BOTH);
				}
			}

			if (isRefresh) {
				mListView.getRefreshableView().setSelection(0);
				BAApplication.getInstance().setMeDynamicLists(infoList);
			} else {
				DynamicsInfoList dynamicsInfoList = BAApplication.getInstance().getMeDynamicLists();
				dynamicsInfoList.indexOf(dynamicsInfoList);
				BAApplication.getInstance().setMeDynamicLists(dynamicsInfoList);
			}
			break;
		case HandlerValue.GET_DYNAMIC_ERROR://获取动态失败
			DialogFactory.dimissDialog(dialog);
			BaseUtils.showTost(getActivity(), R.string.toast_login_failure);
			mListView.onRefreshComplete();
			break;
		case HandlerValue.DYNAMIC_DELETE_SUCCESS://删除成功
			isRefresh = true;
			mListView.setRefreshing();

			NoticeEvent event = new NoticeEvent();
			event.setFlag(NoticeEvent.NOTICE90);
			event.setNum(topicuid);
			event.setNum2(grobalid);
			EventBus.getDefault().post(event);
			break;
		case HandlerValue.DYNAMIC_DELETE_ERROR://删除失败
			BaseUtils.showTost(getActivity(), R.string.delete_failure);
			break;
		case REFRESH_VIEW:
			isRefresh = true;
			mListView.setRefreshing();
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
		control.requstDynamicAll(start, num, TYPE, this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		getDynamicData(-1, LOADCOUNT);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		startLoadPosition = startLoadPosition - LOADCOUNT;
		getDynamicData(startLoadPosition, LOADCOUNT);

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
		mHandler.sendEmptyMessage(HandlerValue.GET_DYNAMIC_ERROR);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DynamicsInfo info = (DynamicsInfo) mAdapter.getItem(position - 1);
		Bundle bundle = new Bundle();
		bundle.putInt(DynamicDetailsActivity.TOPICID_FLAG, info.topicid.intValue());
		bundle.putInt(DynamicDetailsActivity.TOPIUID_FLAG, info.uid.intValue());
		bundle.putInt(DynamicDetailsActivity.ANONYMOUS_FLAG, info.isanonymous.intValue());
		bundle.putInt(DynamicDetailsActivity.TYPE_FLAG, info.dynamicstype.intValue());
		bundle.putInt(DynamicDetailsActivity.SYSTEMID_FLAG, -1);
		bundle.putInt(DynamicDetailsActivity.STATE_FLAG, info.dynamicsstatus.intValue());
		bundle.putInt(DynamicDetailsActivity.FROM_FLAG, 0);
		BaseUtils.openActivity(getActivity(), DynamicDetailsActivity.class, bundle);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		DynamicsInfo info = (DynamicsInfo) mAdapter.getItem(position - 1);
		topicuid = info.uid.intValue();
		grobalid = info.relativetopic.intValue();
		new DeleteDynamicInfoDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, info, 1, -1, mHandler).showDialog();
		return true;
	}

	public void onEvent(NoticeEvent event) {
		switch (event.getFlag()) {
		case NoticeEvent.NOTICE86:
		case NoticeEvent.NOTICE91:
			mHandler.sendEmptyMessage(REFRESH_VIEW);
			break;

		default:
			break;
		}
	}
}
