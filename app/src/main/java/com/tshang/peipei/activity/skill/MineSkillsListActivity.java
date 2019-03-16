package com.tshang.peipei.activity.skill;

import java.util.Collections;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.skill.adapter.MineSkillsDealAdapter;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.space.GetSkillListBiz;
import com.tshang.peipei.model.request.RequestGetSkillDealList.iGetSkillDealList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: MineSkillsListActivity.java 
 *
 * @Description: 我的技能订单界面 
 *
 * @author allen  
 *
 * @date 2014-10-21 上午10:09:24 
 *
 * @version V1.0   
 */
public class MineSkillsListActivity extends BaseActivity implements iGetSkillDealList, OnItemClickListener {

	public final static int RESULT_CODE = 10011;

	private TextView mEmpty;
	protected PullToRefreshListView mPullRefreshListView;
	protected MineSkillsDealAdapter mAdapter;

	protected boolean isRefresh = true;
	private int start = -1;
	private int num = 10;

	@Override
	protected void initData() {
		BaseUtils.showDialog(this, R.string.loading);
		reload();
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.mine_skills_order);

		mEmpty = (TextView) findViewById(R.id.skills_order_empty_tv);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.skills_order_list_lv);
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshListener());
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);

		mAdapter = new MineSkillsDealAdapter(this);
		mPullRefreshListView.setAdapter(mAdapter);
	}

	@Override
	protected int initView() {
		return R.layout.activity_skills_list;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.SKILL_DEAL_LIST_RESULT:
			mPullRefreshListView.onRefreshComplete();
			if (msg.arg1 == 0) {
				SkillDealInfoList list = (SkillDealInfoList) msg.obj;
				Collections.reverse(list);
				if (isRefresh) {
					mAdapter.clearList();
				}
				mAdapter.appendToList(list);
			}

			if (msg.arg2 == 1) {
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);//说明没有数据了
			} else {
				mPullRefreshListView.setMode(Mode.BOTH);
			}
			if (mAdapter.getCount() == 0) {
				mPullRefreshListView.setEmptyView(mEmpty);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void resultSkillDealList(int retCode, int end, SkillDealInfoList list) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.SKILL_DEAL_LIST_RESULT;
		msg.arg1 = retCode;
		msg.arg2 = end;
		msg.obj = list;
		mHandler.sendMessage(msg);

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

	private void reload() {
		if (BAApplication.mLocalUserInfo != null) {
			isRefresh = true;
			start = -1;
			GetSkillListBiz gBiz = new GetSkillListBiz(this);
			gBiz.getSkillDealList(start, num, this);
		}
	}

	protected void loadMore() {
		if (BAApplication.mLocalUserInfo != null) {
			isRefresh = false;
			start = start - num;
			GetSkillListBiz gBiz = new GetSkillListBiz(this);
			gBiz.getSkillDealList(start, num, this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SkillDealInfo info = (SkillDealInfo) parent.getAdapter().getItem(position);

		if (info != null) {
			Bundle b = new Bundle();
			b.putInt("skill_type", info.skillinfo.type.intValue());
			b.putString("skill_title", new String(info.skillinfo.title));
			b.putLong("skill_time", info.createtime.longValue() * 1000);
			b.putInt("skill_status", info.step.intValue());
			if (info.skilluid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {

				String alias = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						info.participateuid.intValue());
				String userName = TextUtils.isEmpty(alias) ? new String(info.participatenick) : alias;
				if (info.skillinfo.type.intValue() == Gender.FEMALE.getValue()) {
					b.putString("skill_content", getString(R.string.str_skill_order_content1) + userName);
				} else {
					b.putString("skill_content", getString(R.string.str_skill_order_content3) + userName);
				}
			} else {
				String alias = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						info.skilluid.intValue());
				String userName = TextUtils.isEmpty(alias) ? new String(info.skillnick) : alias;
				if (info.skillinfo.type.intValue() == Gender.FEMALE.getValue()) {
					b.putString("skill_content", getString(R.string.str_skill_order_content2) + userName);
				} else {
					b.putString("skill_content", getString(R.string.str_skill_order_content4) + userName);
				}
			}
			b.putInt("skill_uid", info.skilluid.intValue());
			b.putInt("skill_dealid", info.id.intValue());
			Intent intent = new Intent(this, SkillDealInfoActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, RESULT_CODE);
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_CODE) {
			Bundle b = arg2.getExtras();
			mAdapter.setStepbyId(b.getInt("step"), b.getInt("skilldealid"));

			//			reload();
		}
	}
}
