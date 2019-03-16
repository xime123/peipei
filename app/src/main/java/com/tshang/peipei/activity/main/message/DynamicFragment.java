package com.tshang.peipei.activity.main.message;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.activity.mine.MineDynamicActivity;
import com.tshang.peipei.activity.mine.MineNetPhotosListActivity;
import com.tshang.peipei.activity.space.SpaceCustomAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.TopicCounterInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.space.SpaceBiz;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: DynamicFragment
 *
 * @Description: 动态
 *
 * @author jeff
 *
 * @version V2.0   
 */
public class DynamicFragment extends BaseFragment implements OnItemClickListener, OnClickListener {

	protected boolean isRefresh = true;
	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = -1;

	protected SpaceBiz spaceBiz;
	protected PullToRefreshListView mPullRefreshListView;
	protected SpaceCustomAdapter mAdapter;
	protected TextView mEmptyDynamic;
	private TextView tvHead;

	//	@Override
	//	protected void onResume() {
	//		super.onResume();
	//
	//		int unread = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
	//				BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM, 0);
	//		if (unread > 0) {
	//			tvHead.setText(unread + "");
	//			tvHead.setVisibility(View.VISIBLE);
	//		} else {
	//			tvHead.setVisibility(View.GONE);
	//		}
	//	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.SPACE_MY_FOLLOW_LIST_VALUE://我的关注列表
			mPullRefreshListView.onRefreshComplete();
			if (msg.arg1 == 0) {
				ArrayList<TopicInfo> list = (ArrayList<TopicInfo>) msg.obj;

				if (isRefresh) {
					currentTime = System.currentTimeMillis();
					mAdapter.clearList();
				}
				mAdapter.appendToList(list);
			}
			if (msg.arg2 == 1) {
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);//说明没有数据了
			}
			if (mAdapter.getCount() == 0) {
				mPullRefreshListView.setEmptyView(mEmptyDynamic);
			}
			break;
		case HandlerValue.SPACE_GET_TOPIC_COUNT_VALUE:
			TopicCounterInfo info = (TopicCounterInfo) msg.obj;//贴子计数map
			String key = info.topicid.intValue() + "" + info.topicuid.intValue();
			mAdapter.getCountMap().put(key, info);
			mAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	protected void getMyFollowList(int uid, int start, int num) {//获取我关注人的动态
		spaceBiz.getFollowList(uid, start, num);
	}

	private void reload() {
		if (BAApplication.mLocalUserInfo != null) {
			isRefresh = true;
			startLoadPosition = -1;
			getMyFollowList(BAApplication.mLocalUserInfo.uid.intValue(), startLoadPosition, LOADCOUNT);
		}
	}

	protected void loadMore() {
		if (BAApplication.mLocalUserInfo != null) {
			isRefresh = false;
			startLoadPosition = startLoadPosition - LOADCOUNT;
			getMyFollowList(BAApplication.mLocalUserInfo.uid.intValue(), startLoadPosition, LOADCOUNT);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.followers_for_me:
			BaseUtils.openActivity(getActivity(), MineDynamicActivity.class);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TopicInfo topicInfo = (TopicInfo) parent.getAdapter().getItem(position);
		if (topicInfo != null) {
			BAParseRspData parser = new BAParseRspData();
			GoGirlDataInfoList dataInfoList = topicInfo.topiccontentlist;
			ContentData data = parser.parseTopicInfo(getActivity(), dataInfoList, 0);
			if (data.getType() == BAConstants.MessageType.UPLOAD_PHOTO.getValue()) {
				Intent intent = new Intent(getActivity(), MineNetPhotosListActivity.class);
				//将相册ID传到上传界面
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, 0);
				String albumName = "相册";
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMNAME, albumName);
				intent.putExtra("viewpeopleuid", topicInfo.uid.intValue());
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMCOUNT, 0);
				startActivity(intent);
				//				overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			} else {
				OperateViewUtils.intentSpaceCustomDetailActivity(getActivity(), mAdapter, topicInfo, false);
			}
		}
	}

	//上拉刷新 ,下拉加载更多刷新 
	class PullToRefreshListener implements OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			//			setRefreshTextLable(mPullRefreshListView, R.string.pull_to_refresh_refreshing_label, R.string.pull_to_refresh_release_label,
			//					R.string.pull_to_refresh_pull_label);
			reload();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			//			setRefreshTextLable(mPullRefreshListView, R.string.pull_to_load_loading_label, R.string.pull_to_load_release_label,
			//					R.string.pull_to_load_pull_label);
			loadMore();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_mine_followers, null);

		initUi(view);
		return view;
	}

	private void initUi(View view) {
		spaceBiz = new SpaceBiz(getActivity(), mHandler);
		mAdapter = new SpaceCustomAdapter(getActivity(), 0, 0);

		View headText = LayoutInflater.from(getActivity()).inflate(R.layout.head_empty_text2, null);
		
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.follower_listview);
		mPullRefreshListView.getRefreshableView().addHeaderView(headText);
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshListener());
		mPullRefreshListView.setAdapter(mAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);

		view.findViewById(R.id.followers_for_me).setOnClickListener(this);


		tvHead = (TextView) view.findViewById(R.id.dynamic_header_text);

		mEmptyDynamic = (TextView) view.findViewById(R.id.follower_empty_tv);

		reload();
	}

	private long currentTime = 0;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (this.isVisible()) {
			if (isVisibleToUser) {
				int newFans = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
						BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM);
				OperateViewUtils.setTextViewShowCount(tvHead, newFans, false);
				if (System.currentTimeMillis() - currentTime > 1 * 60 * 60 * 1000) {
					reload();
				}
			}
		}

		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		int newFans = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
				BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM);
		OperateViewUtils.setTextViewShowCount(tvHead, newFans, false);
	}

	@Override
	public void onResume() {
		int newFans = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
				BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM);
		OperateViewUtils.setTextViewShowCount(tvHead, newFans, false);
		super.onResume();
	}
}
