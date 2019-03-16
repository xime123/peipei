package com.tshang.peipei.activity.main.message;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.activity.main.message.adapter.DynamicAboutMeAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestDynamicAboutMe.GetDynamicAboutMeCallBack;
import com.tshang.peipei.storage.database.entity.NewDynamicReplyEntity;
import com.tshang.peipei.storage.database.operate.NewDynamicReplyOperate;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @Title: DynamicFragmentAbout.java 
 *
 * @Description: 动态-关于我 
 *
 * @author Aaron  
 *
 * @date 2015-8-12 上午9:55:10 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class DynamicFragmentAbout extends BaseFragment implements GetDynamicAboutMeCallBack, OnRefreshListener2<ListView>, OnItemClickListener,
		OnItemLongClickListener {

	private final int REPLY_NEW = 543;
	private final int TYPE = 1;

	private PullToRefreshListView mListView;
	private TextView emptyTv;

	private DynamicAboutMeAdapter mAdapter;

	//	private DynamicRequseControl control;

	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = 0;
	protected boolean isRefresh = true;

	private List<NewDynamicReplyEntity> list;

	private Dialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dynamic_about_me_layout, null);
		findViewById(view);
		initListView();
		//全局缓存处理
		if (BAApplication.getInstance().getReplyInfoLists() == null) {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mListView.setRefreshing();
				}
			}, 500);
		} else {
			Message msg = mHandler.obtainMessage();
			msg.what = HandlerValue.GET_ABOUT_DYNAMIC_SUCCESS;
			msg.obj = BAApplication.getInstance().getReplyInfoLists();
			mHandler.sendMessage(msg);
		}
		return view;
	}

	private void findViewById(View v) {
		mListView = (PullToRefreshListView) v.findViewById(R.id.dynamic_about_me_listview);
		emptyTv = (TextView) v.findViewById(R.id.dynamic_about_me_empty_tv);
		mListView.setMode(Mode.PULL_FROM_END);
	}

	private void initListView() {
		mAdapter = new DynamicAboutMeAdapter(getActivity());
		mListView.setMode(Mode.BOTH);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		mListView.getRefreshableView().setOnItemLongClickListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_ABOUT_DYNAMIC_SUCCESS://获取成功
			BaseUtils.cancelDialog();
			mListView.onRefreshComplete();

			if (isRefresh) {
				mAdapter.clearList();
			}

			list = (List<NewDynamicReplyEntity>) msg.obj;
			mAdapter.appendToList(list);

			if (list == null || list.size() <= 0) {
				if (isRefresh) {
					mListView.getRefreshableView().setEmptyView(emptyTv);
				} else {
					BaseUtils.showTost(getActivity(), getResources().getString(R.string.namypic_data_null));
					mListView.setMode(Mode.PULL_FROM_START);
				}
			} else {
				mListView.setMode(Mode.BOTH);
			}

			if (isRefresh) {
				mListView.getRefreshableView().setSelection(0);
				BAApplication.getInstance().setReplyInfoLists(list);
			} else {
				List<NewDynamicReplyEntity> aboutMeReplyInfoList = BAApplication.getInstance().getReplyInfoLists();
				aboutMeReplyInfoList.indexOf(list);
				BAApplication.getInstance().setReplyInfoLists(aboutMeReplyInfoList);
			}

			break;
		case HandlerValue.GET_ABOUT_DYNAMIC_ERROR://获取失败
			BaseUtils.cancelDialog();
			mListView.onRefreshComplete();
			BaseUtils.showTost(getActivity(), R.string.get_data_failure);
			break;
		case REPLY_NEW:
			isRefresh = true;
			mListView.setRefreshing();
			break;

		default:
			break;
		}
	}

	/**
	 * 获取数据
	 * @author Aaron
	 *
	 * @param start
	 * @param num
	 */
	private void getAboutMeReply(int start, int num) {
		List<NewDynamicReplyEntity> list = NewDynamicReplyOperate.getInstance(getActivity()).seleteDynamicReplyList(start, num);
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.GET_ABOUT_DYNAMIC_SUCCESS;
		msg.obj = list;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onSuccess(int code, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.GET_ABOUT_DYNAMIC_SUCCESS;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onError(int code) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.GET_ABOUT_DYNAMIC_ERROR;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		startLoadPosition = 0;
		getAboutMeReply(startLoadPosition, LOADCOUNT);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		startLoadPosition = startLoadPosition + LOADCOUNT;
		getAboutMeReply(startLoadPosition, LOADCOUNT);
	}

	public void onEvent(final NoticeEvent event) {
		switch (event.getFlag()) {
		case NoticeEvent.NOTICE87:
		case NoticeEvent.NOTICE92:
		case NoticeEvent.NOTICE86:
			Message msg = new Message();
			msg.what = REPLY_NEW;
			mHandler.sendMessage(msg);
			break;
		case NoticeEvent.NOTICE90:
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					int topicuid = event.getNum();
					int globalid = event.getNum2();
					NewDynamicReplyOperate.getInstance(getActivity()).deleteReply(topicuid, globalid);
					isRefresh = true;
					mListView.setRefreshing();
				}
			});

			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		NewDynamicReplyEntity info = (NewDynamicReplyEntity) mAdapter.getItem(position - 1);
		Bundle bundle = new Bundle();
		bundle.putInt(DynamicDetailsActivity.TOPICID_FLAG, info.getTopicid());
		bundle.putInt(DynamicDetailsActivity.TOPIUID_FLAG, info.getTopicuid());
		bundle.putInt(DynamicDetailsActivity.ANONYMOUS_FLAG, 1);
		bundle.putInt(DynamicDetailsActivity.TYPE_FLAG, info.getType());
		if (info.getType() == 2) {//判断话题动态
			bundle.putInt(DynamicDetailsActivity.SYSTEMID_FLAG, info.getRevint0());
		} else
			bundle.putInt(DynamicDetailsActivity.SYSTEMID_FLAG, -1);
		bundle.putInt(DynamicDetailsActivity.FROM_FLAG, 1);
		BaseUtils.openActivity(getActivity(), DynamicDetailsActivity.class, bundle);
		if (info.getStatus() == 1) {
			NewDynamicReplyOperate.getInstance(getActivity()).updateReadStatus(0, info.getCreatetime());
			mAdapter.updateReadStatus(info.getCreatetime());
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		final NewDynamicReplyEntity info = (NewDynamicReplyEntity) mAdapter.getItem(position - 1);
		dialog = DialogFactory.showMsgDialog(getActivity(), "", "是否删除评论?", new OnClickListener() {

			@Override
			public void onClick(View v) {
				NewDynamicReplyOperate.getInstance(getActivity()).deleteReply(info.getCreatetime());
				mListView.setRefreshing();
				DialogFactory.dimissDialog(dialog);
			}
		});
		return true;
	}
}
