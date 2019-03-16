package com.tshang.peipei.activity.mine;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
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
 * @Title: MineFollowerActivity.java 
 *
 * @Description: 我关注的用户
 *
 * @author allen  
 *
 * @date 2014-9-12 下午9:49:18 
 *
 * @version V1.0   
 */
public class MineFollowerActivity extends BaseActivity implements OnItemClickListener {
	protected boolean isRefresh = true;
	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = -1;

	protected SpaceBiz spaceBiz;
	protected PullToRefreshListView mPullRefreshListView;
	protected SpaceCustomAdapter mAdapter;
	protected TextView mEmptyDynamic;
	private TextView tvHead;

	@Override
	protected void initData() {
		mAdapter = new SpaceCustomAdapter(this, 0, 0);
		mPullRefreshListView.setAdapter(mAdapter);

		reload();
	}

	@Override
	protected void initRecourse() {
		spaceBiz = new SpaceBiz(this, mHandler);

		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_my_follower);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.follower_listview);
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshListener());
		mPullRefreshListView.setOnItemClickListener(this);

		//		LayoutInflater mInflater = getLayoutInflater();
		//		View headView = mInflater.inflate(R.layout.view_dynamic_head, null);
		//		mPullRefreshListView.getRefreshableView().addHeaderView(headView);

		findViewById(R.id.followers_for_me).setOnClickListener(this);

		tvHead = (TextView) findViewById(R.id.dynamic_header_text);
		int unread = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
				BAConstants.PEIPEI_FANS_UNREAD_NUM, 0);

		if (unread > 0) {
			tvHead.setText(unread + "");
			tvHead.setVisibility(View.VISIBLE);
		} else {
			tvHead.setVisibility(View.GONE);
		}

		mEmptyDynamic = (TextView) findViewById(R.id.follower_empty_tv);
	}

	@Override
	protected int initView() {
		return R.layout.activity_mine_followers;
	}

	@Override
	protected void onResume() {
		super.onResume();

		int unread = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
				BAConstants.PEIPEI_FANS_UNREAD_NUM, 0);
		if (unread > 0) {
			tvHead.setText(unread + "");
			tvHead.setVisibility(View.VISIBLE);
		} else {
			tvHead.setVisibility(View.GONE);
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.SPACE_MY_FOLLOW_LIST_VALUE://我的关注列表
			mPullRefreshListView.onRefreshComplete();
			if (msg.arg1 == 0) {
				ArrayList<TopicInfo> list = (ArrayList<TopicInfo>) msg.obj;

				if (isRefresh) {
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
			BaseUtils.openActivity(this, MineDynamicActivity.class);
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
			ContentData data = parser.parseTopicInfo(this, dataInfoList, 0);
			if (data.getType() == BAConstants.MessageType.UPLOAD_PHOTO.getValue()) {
				Intent intent = new Intent(this, MineNetPhotosListActivity.class);
				//将相册ID传到上传界面
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, 0);
				String albumName = "相册";
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMNAME, albumName);
				intent.putExtra("viewpeopleuid", topicInfo.uid.intValue());
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMCOUNT, 0);
				startActivity(intent);
				overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			} else {
				OperateViewUtils.intentSpaceCustomDetailActivity(this, mAdapter, topicInfo, false);
			}
		}
	}

	//上拉刷新 ,下拉加载更多刷新 
	class PullToRefreshListener implements OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			setRefreshTextLable(mPullRefreshListView, R.string.pull_to_refresh_refreshing_label, R.string.pull_to_refresh_release_label,
					R.string.pull_to_refresh_pull_label);
			reload();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			setRefreshTextLable(mPullRefreshListView, R.string.pull_to_load_loading_label, R.string.pull_to_load_release_label,
					R.string.pull_to_load_pull_label);
			loadMore();
		}

	}

}
