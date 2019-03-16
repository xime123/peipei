package com.tshang.peipei.activity.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.main.adapter.HaremListAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.main.MainRankListBiz;
import com.tshang.peipei.model.harem.CreateHarem;
import com.tshang.peipei.model.harem.HaremUtils;
import com.tshang.peipei.model.request.RequestGetRankGroupList.IGetRankGroupList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfoList;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: HaremListActivity.java 
 *
 * @Description: 后宫推荐列表
 *
 * @author Aaron  
 *
 * @date 2016-1-21 下午2:45:30 
 *
 * @version V1.0   
 */
public class HaremListActivity extends BaseActivity implements IGetRankGroupList, OnRefreshListener2<ListView>, OnItemClickListener {

	private PullToRefreshListView mListView;
	private TextView emptyTextView;

	private MainRankListBiz rankListBiz;

	protected static final int LOADCOUNT = 20;
	protected int startLoadPosition = -1;
	private boolean isRefresh = true;

	private final int GET_HAREMLIST_SUCCESS = 0x999999;
	private final int GET_HAREMLIST_ERROR = 0x000000;

	private HaremListAdapter adapter;

	private Dialog dialog;

	private GroupInfo groupInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rankListBiz = new MainRankListBiz();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mListView.setRefreshing();
			}
		}, 600);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) this.findViewById(R.id.title_tv_mid);
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setOnClickListener(this);
		mLinRight = (LinearLayout) this.findViewById(R.id.title_lin_right);
		mLinRight.setVisibility(View.VISIBLE);
		mLinRight.setOnClickListener(this);
		mTextRight = (TextView) this.findViewById(R.id.title_tv_right);
		mTextRight.setVisibility(View.GONE);
		mTextRight.setText(R.string.publish);
		mTitle.setText("后宫列表");

		mListView = (PullToRefreshListView) findViewById(R.id.harem_list_listview);
		emptyTextView = (TextView) findViewById(R.id.harem_list_not_data_tv);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.getRefreshableView().setOnItemClickListener(this);

		adapter = new HaremListAdapter(this);
		mListView.setAdapter(adapter);
	}

	@Override
	protected int initView() {
		return R.layout.activity_harem_list;
	}

	private void getHaremListDataInfo() {
		rankListBiz.getHaremRanklist(this, startLoadPosition, LOADCOUNT, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case GET_HAREMLIST_SUCCESS:
			mListView.onRefreshComplete();
			GroupInfoList groupInfoList = (GroupInfoList) msg.obj;
			if (isRefresh) {
				adapter.clearList();
				if (ListUtils.isEmpty(groupInfoList)) {
					mListView.setEmptyView(emptyTextView);
				} else {
					List<GroupInfo> mList = new ArrayList<GroupInfo>();
					for (int i = groupInfoList.size() - 1; i >= 0; i--) {
						mList.add((GroupInfo) groupInfoList.get(i));
					}
					adapter.appendToList(mList);
				}

			} else {
				if (groupInfoList.size() == 0) {
					BaseUtils.showTost(this, "没有更多数据");
				} else {
					List<GroupInfo> mList = new ArrayList<GroupInfo>();
					for (int i = groupInfoList.size() - 1; i >= 0; i--) {
						mList.add((GroupInfo) groupInfoList.get(i));
					}
					adapter.appendToList(mList);
				}
			}
			break;
		case GET_HAREMLIST_ERROR:
			mListView.onRefreshComplete();
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;

		case HandlerValue.HAREM_GET_GROUP_MEMBER_LIST_SUCCESS_VALUE:
			GroupMemberInfoList infoList = (GroupMemberInfoList) msg.obj;
			if (infoList != null) {
				GoGirlUserInfo userInfo = UserUtils.getUserEntity(this);
				boolean isJoin = false;
				if (userInfo != null) {
					for (Object object : infoList) {
						GroupMemberInfo info = (GroupMemberInfo) object;
						if (info.uid.intValue() == userInfo.uid.intValue()) {//说明自己已经存在这个群
							isJoin = true;
							break;
						}
					}
				}
				if (isJoin) {
					ChatActivity.intentChatActivity(this, groupInfo.groupid.intValue(), new String(groupInfo.groupname), 1, true, false, 0);
				} else {
					HaremUtils.intentManagerHaremActivity(this, groupInfo, false, false);
				}
			}
			DialogFactory.dimissDialog(dialog);

		default:
			break;
		}
	}

	@Override
	public void getGroupInfoList(int retCode, GroupInfoList infoList, int isEnd, int total) {
		if (retCode == 0) {
			sendHandlerMessage(mHandler, GET_HAREMLIST_SUCCESS, retCode, infoList);
		} else {
			sendHandlerMessage(mHandler, GET_HAREMLIST_ERROR, retCode);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		startLoadPosition = -1;
		getHaremListDataInfo();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		startLoadPosition = startLoadPosition - LOADCOUNT;
		getHaremListDataInfo();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		groupInfo = (GroupInfo) arg0.getAdapter().getItem(arg2);
		if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == groupInfo.owner.intValue()) {//说明进入的是自己个人主页，底部不可见
			ChatActivity.intentChatActivity(this, groupInfo.groupid.intValue(), new String(groupInfo.groupname), 1, true, false, 0);
		} else {
			dialog = DialogFactory.createLoadingDialog(this, "获取后宫信息...");
			dialog.show();
			CreateHarem.getInstance().getGroupMemberInfoList(this, groupInfo.groupid.intValue(), mHandler);//获取后宫成员列表
		}
	}
}
