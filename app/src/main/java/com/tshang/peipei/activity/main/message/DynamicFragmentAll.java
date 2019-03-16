package com.tshang.peipei.activity.main.message;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.activity.space.adapter.DynamicAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
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
 * @Description: 所有动态 
 *
 * @author Aaron  
 *
 * @date 2015-8-12 上午9:55:10 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class DynamicFragmentAll extends BaseFragment implements OnItemClickListener, GetDynamicAllCallBack, OnRefreshListener2<ListView> {

	private final int REFRESH_VIEW = 123;
	private final int TYPE = 0;

	protected PullToRefreshListView mPullRefreshListView;

	private DynamicAdapter mAdapter;

	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = -1;
	protected boolean isRefresh = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dynamic_all_layout, null);
		findViewById(view);
		initListView();
		//全局缓存处理
		if (BAApplication.getInstance().getAllDynamicLists() != null && BAApplication.getInstance().getAllDynamicLists().size() > 0) {
			Message msg = mHandler.obtainMessage();
			msg.what = HandlerValue.GET_DYNAMIC_SUCCESS;
			msg.obj = BAApplication.getInstance().getAllDynamicLists();
			mHandler.sendMessage(msg);
		}
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullRefreshListView.setRefreshing();
			}
		}, 500);
		return view;
	}

	private void findViewById(View v) {
		mPullRefreshListView = (PullToRefreshListView) v.findViewById(R.id.dynamic_listview);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnItemClickListener(this);

	}

	private void initListView() {
		mAdapter = new DynamicAdapter(getActivity(), mHandler, false, -1, 0);
		mPullRefreshListView.setAdapter(mAdapter);
		mPullRefreshListView.setOnRefreshListener(this);
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
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DynamicsInfo info = (DynamicsInfo) mAdapter.getItem(position - 1);
		if (info.revint0.intValue() == 0) {//官方话题
			BAParseRspData parse = new BAParseRspData();
			final ContentData data = parse.parseTopicInfo(getActivity(), info.dynamicscontentlist, info.sex.intValue());
			Bundle bundle = new Bundle();
			bundle.putString("title", data.getContent());
			bundle.putInt("topicid", info.topicid.intValue());
			bundle.putInt("systemid", info.topicid.intValue());
			BaseUtils.openActivity(getActivity(), DynamicOfficalActivity.class, bundle);
		} else if (info.revint0.intValue() == 1) {//用户动态
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
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_DYNAMIC_SUCCESS://获取动态成功
			BaseUtils.cancelDialog();
			mPullRefreshListView.onRefreshComplete();

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
				//官方话题位置处理
				if (infoList != null && infoList.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).revint0.intValue() == 0) {
							DynamicsInfo info = list.get(i);
							list.remove(i);
							list.add(((int) list.size() / 2), info);
							break;
						}
					}
				}
				mAdapter.appendToList(list);
			}

			if (!isRefresh) {
				if (infoList == null || infoList.size() == 0) {
					mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					BaseUtils.showTost(getActivity(), getResources().getString(R.string.namypic_data_null));
				}
			} else {
				mPullRefreshListView.setMode(Mode.BOTH);
			}

			if (isRefresh) {
				mPullRefreshListView.getRefreshableView().setSelection(0);
				BAApplication.getInstance().setAllDynamicLists(infoList);
			} else {
				DynamicsInfoList dynamicsInfoList = BAApplication.getInstance().getAllDynamicLists();
				dynamicsInfoList.indexOf(dynamicsInfoList);
				BAApplication.getInstance().setAllDynamicLists(dynamicsInfoList);
			}
			break;
		case HandlerValue.GET_DYNAMIC_ERROR://获取动态失败
			mPullRefreshListView.onRefreshComplete();
			BaseUtils.showTost(getActivity(), R.string.toast_login_failure);
			break;
		case REFRESH_VIEW://刷新界面 
			isRefresh = true;
			mPullRefreshListView.setRefreshing();
			break;

		default:
			break;
		}
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
		mHandler.sendEmptyMessage(HandlerValue.GET_DYNAMIC_ERROR);
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
		startLoadPosition = startLoadPosition - LOADCOUNT + 1;
		getDynamicData(startLoadPosition, LOADCOUNT);
	}

	public void onEvent(NoticeEvent event) {
		switch (event.getFlag()) {
		case NoticeEvent.NOTICE86:
		case NoticeEvent.NOTICE90:
		case NoticeEvent.NOTICE91:
			mHandler.sendEmptyMessage(REFRESH_VIEW);
			break;

		default:
			break;
		}
	}

}
