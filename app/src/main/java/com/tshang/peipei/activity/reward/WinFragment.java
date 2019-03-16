package com.tshang.peipei.activity.reward;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.activity.reward.adapter.RewardAllAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.RewardType;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.reward.RewardRequestControl;
import com.tshang.peipei.model.request.RequestRewardListInfo.GetRewardListInfoCallBack;
import com.tshang.peipei.protocol.asn.gogirl.AwardDetail;
import com.tshang.peipei.protocol.asn.gogirl.AwardDetailList;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: JoinFragment.java 
 *
 * @Description: 赢得悬赏
 *
 * @author Aaron  
 *
 * @date 2015-9-29 下午4:08:19 
 *
 * @version V1.0   
 */
public class WinFragment extends BaseFragment implements OnRefreshListener2<ListView>, GetRewardListInfoCallBack {

	private PullToRefreshListView pullToRefreshListView;
	private TextView emptyTv;

	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = -1;

	private boolean isRefresh = true;

	private RewardRequestControl control;

	private RewardAllAdapter adapter;

	private int anonymNickId;

	public WinFragment(int anonymNickId) {
		this.anonymNickId = anonymNickId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		control = new RewardRequestControl();
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.win_reward_layout, null);
		findViewById(view);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				pullToRefreshListView.setRefreshing();
			}
		}, 600);
		return view;
	}

	private void findViewById(View view) {
		emptyTv = (TextView) view.findViewById(R.id.win_reward_not_data_tv);
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.win_reward_listview);

		adapter = new RewardAllAdapter(getActivity(), mHandler, RewardType.WIN.getValue());
		adapter.setAnonymNickId(anonymNickId);
		pullToRefreshListView.setAdapter(adapter);
		pullToRefreshListView.setOnRefreshListener(this);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_WIN_REWARD_LIST_SUCCESS:
			pullToRefreshListView.onRefreshComplete();
			if (msg.arg1 == 0) {
				AwardDetailList detailLists = (AwardDetailList) msg.obj;
				if (isRefresh) {
					adapter.clearList();
				}

				List<AwardDetail> list = new ArrayList<AwardDetail>();
				for (int i = detailLists.size() - 1; i >= 0; i--) {
					list.add((AwardDetail) detailLists.get(i));
				}
				adapter.appendToList(list);
				if (!isRefresh) {
					BaseUtils.showTost(getActivity(), R.string.namypic_data_null);
					pullToRefreshListView.setMode(Mode.PULL_FROM_START);
				}
				if (isRefresh) {
					if (detailLists.size() <= 0) {
						pullToRefreshListView.setEmptyView(emptyTv);
					}
				}
			} else {
				BaseUtils.showTost(getActivity(), getResources().getString(R.string.get_data_failure));
			}
			break;
		case HandlerValue.GET_WIN_REWARD_LIST_ERROR:
			pullToRefreshListView.onRefreshComplete();
			BaseUtils.showTost(getActivity(), R.string.toast_login_failure);
			break;

		default:
			break;
		}
	}

	private void getRewardListInfo(int type, int start, int num) {
		control.requestListInfo(anonymNickId, type, start, num, this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		startLoadPosition = -1;
		getRewardListInfo(RewardType.WIN.getValue(), startLoadPosition, LOADCOUNT);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		startLoadPosition = startLoadPosition - LOADCOUNT;
		getRewardListInfo(RewardType.WIN.getValue(), startLoadPosition, LOADCOUNT);
	}

	@Override
	public void onGetRewardListInfoSuccess(int code, int isend, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_WIN_REWARD_LIST_SUCCESS, code, isend, obj);
	}

	@Override
	public void onGetRewardListInfoError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.GET_WIN_REWARD_LIST_ERROR, code);
	}

	/**
	 * 使用HANDLER 发送消息
	 * @param handler
	 * @param what
	 * @param arg1
	 */
	public void sendHandlerMessage(Handler handler, int what, int arg1, int arg2, Object obj) {
		if (handler == null) {
			return;
		}
		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	/**
	 * 使用HANDLER 发送消息
	 * @param handler
	 * @param what
	 * @param arg1
	 */
	public void sendHandlerMessage(Handler handler, int what, int arg1) {
		if (handler == null) {
			return;
		}
		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.arg1 = arg1;
		handler.sendMessage(msg);
	}
}
