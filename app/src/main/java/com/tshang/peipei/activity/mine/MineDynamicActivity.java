package com.tshang.peipei.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.CleanDynamicDialog;
import com.tshang.peipei.activity.space.SpaceCustomDetailActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.DynamicEntity;
import com.tshang.peipei.storage.database.operate.DynamicOperate;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: MineDynamicActivity.java 
 *
 * @Description: 我的动态 
 *
 * @author Allen  
 *
 * @date 2014-12-3 下午2:01:26 
 *
 * @version V1.0   
 */
public class MineDynamicActivity extends BaseActivity implements OnItemClickListener {

	private TextView tvEmpty;

	private ArrayList<DynamicEntity> mList = new ArrayList<DynamicEntity>();
	protected PullToRefreshListView mPullRefreshListView;
	private MineDynamicAdapter mineDynamicAdapter;

	private final int dbNum = 20;
	private int dynamicNum;
	private long time;

	@Override
	protected void initData() {
		DynamicOperate dynamicOperate = DynamicOperate.getInstance(this);

		SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(0,
				BAConstants.PEIPEI_DYNAMIC_UNREAD_NUM);

		dynamicNum = dynamicOperate.getCount();
		mList = dynamicOperate.selectChatList(0, dbNum);
		if (mList.size() == 0) {
			mPullRefreshListView.setEmptyView(tvEmpty);
			mTextRight.setClickable(false);
			mTextRight.setTextColor(getResources().getColor(R.color.gray));
		} else {
			mineDynamicAdapter.setList(mList);
			if (dynamicNum <= dbNum) {
				mPullRefreshListView.setMode(Mode.DISABLED);
			}
		}

	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_about_me_title);

		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mTextRight.setText(R.string.str_clean);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setOnClickListener(this);

		tvEmpty = (TextView) findViewById(R.id.dynamic_empty_tv);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.dynamic_listview);
		mPullRefreshListView.setMode(Mode.PULL_UP_TO_REFRESH);
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshListener());
		mPullRefreshListView.setOnItemClickListener(this);

		time = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getLongKeyValue(
				BAConstants.PEIPEI_DYNAMIC_UNREAD_TIME);
		SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").saveLongKeyValue(System.currentTimeMillis(),
				BAConstants.PEIPEI_DYNAMIC_UNREAD_TIME);

		mineDynamicAdapter = new MineDynamicAdapter(this, time);

		mPullRefreshListView.setAdapter(mineDynamicAdapter);
	}

	@Override
	protected int initView() {
		return R.layout.activity_dynamic;
	}

	//上拉刷新 ,下拉加载更多刷新 
	class PullToRefreshListener implements OnRefreshListener<ListView> {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			try {
				mPullRefreshListView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
			} catch (Exception e) {//防止魅族手机崩溃
				e.printStackTrace();
			}

			int size = mineDynamicAdapter.getCount();
			DynamicOperate dynamicOperate = DynamicOperate.getInstance(MineDynamicActivity.this);

			dynamicNum = dynamicOperate.getCount();
			if (size < dynamicNum) {
				List<DynamicEntity> temp;
				if (dynamicNum - size >= dbNum) {
					temp = dynamicOperate.selectChatList(size, dbNum);
				} else {
					temp = dynamicOperate.selectChatList(size, dynamicNum - size);
				}
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_LOAD_HISTORY_DATA_VALUE, temp);
			} else {
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_LOAD_HISTORY_NO_DATA_VALUE);
			}

		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			new CleanDynamicDialog(this, R.string.str_clean_dynamic_content, R.string.ok, R.string.cancel, mHandler).showDialog();
			break;
		default:
			break;
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.CHAT_LOAD_HISTORY_DATA_VALUE://加载更多聊天数据
			mPullRefreshListView.onRefreshComplete();
			List<DynamicEntity> temp = (List<DynamicEntity>) msg.obj;
			if (temp != null && !temp.isEmpty()) {
				mineDynamicAdapter.appendToList(temp);
				if (mineDynamicAdapter.getCount() > temp.size()) {//控制加载显示的位置
					mPullRefreshListView.getRefreshableView().setSelection(temp.size());
				} else {
					mPullRefreshListView.getRefreshableView().setSelection(temp.size() - 1);
				}
			}
			break;
		case HandlerValue.CHAT_LOAD_HISTORY_NO_DATA_VALUE://没有聊天数据了
			mPullRefreshListView.onRefreshComplete();
			BaseUtils.showTost(this, "已加载全部数据了");
			mPullRefreshListView.setMode(Mode.DISABLED);
			break;
		case HandlerValue.CLEAN_DYNAMIC_VALUE:
			DynamicOperate dynamicOperate = DynamicOperate.getInstance(this);
			dynamicOperate.deleteTable();

			mineDynamicAdapter.clearList();
			mPullRefreshListView.setEmptyView(tvEmpty);

			mTextRight.setClickable(false);
			mTextRight.setTextColor(getResources().getColor(R.color.gray));
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		DynamicEntity entity = (DynamicEntity) parent.getAdapter().getItem(position);

		Bundle bundle = new Bundle();
		bundle.putInt(SpaceCustomDetailActivity.TOPICID, entity.getTopicId());
		bundle.putInt(SpaceCustomDetailActivity.TOPICUID, entity.getTopicUid());
		bundle.putBoolean(SpaceCustomDetailActivity.ISADDCOMMENT, false);
		bundle.putInt(SpaceCustomDetailActivity.APPRECIATENUM, 0);
		bundle.putInt(SpaceCustomDetailActivity.REPLYNUM, 0);
		BaseUtils.openActivity(this, SpaceCustomDetailActivity.class, bundle);
	}
}
