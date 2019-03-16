package com.tshang.peipei.activity.mine;

import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.ClearBlackListDialog;
import com.tshang.peipei.protocol.asn.gogirl.RetRelevantPeopleInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetRelevantPeopleInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.space.BlackListBiz;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: MineBlackListActivity.java 
 *
 * @Description: 黑名单列表
 *
 * @author allen  
 *
 * @date 2014-10-15 上午10:45:59 
 *
 * @version V1.0   
 */
public class MineBlackListActivity extends BaseActivity {

	protected boolean isRefresh = true;
	protected static final int LOADCOUNT = 20;
	protected int startLoadPosition = 0;

	private final int BLACK_LIST_CLEAN = -99;//清空

	protected PullToRefreshListView mPullRefreshListView;
	protected MineBlackListAdapter mAdapter;
	protected TextView mEmptyDynamic;
	private BlackListBiz biz = new BlackListBiz();

	@Override
	protected void initData() {
		mBackText.setOnClickListener(this);
		mTextRight.setOnClickListener(this);

		mAdapter = new MineBlackListAdapter(this, mHandler);
		mPullRefreshListView.setAdapter(mAdapter);

		getBlackList(BAApplication.mLocalUserInfo.uid.intValue(), startLoadPosition, LOADCOUNT);
		BaseUtils.showDialog(this, R.string.loading);
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.message);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_blacklist);
		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setText(R.string.str_clean);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.blacklist_list);
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshListener());
		mEmptyDynamic = (TextView) findViewById(R.id.blacklist_empty_tv);
	}

	@Override
	protected int initView() {
		return R.layout.activity_blacklist;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			new ClearBlackListDialog(this, R.string.str_clean_content, R.string.ok, R.string.cancel, BLACK_LIST_CLEAN, mHandler).showDialog();
			break;

		default:
			break;
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		BaseUtils.cancelDialog();
		switch (msg.what) {
		case HandlerValue.BLACK_LIST_SUCCESS:
			mPullRefreshListView.onRefreshComplete();
			if (msg.arg1 == 0) {
				RetRelevantPeopleInfoList list = (RetRelevantPeopleInfoList) msg.obj;

				if (isRefresh) {
					mAdapter.clearList();
				}
				mAdapter.appendToList(list);
			}
			if (msg.arg2 == 1) {
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);//说明没有数据了
			}

			mTextRight.setClickable(true);
			if (mAdapter.getCount() == 0) {
				mTextRight.setClickable(false);
				mTextRight.setTextColor(getResources().getColor(R.color.gray));
				mPullRefreshListView.setEmptyView(mEmptyDynamic);
			}
			break;
		case HandlerValue.BLACK_LIST_REMOVE:
			if (msg.arg1 == 0) {
				if (msg.arg2 == BLACK_LIST_CLEAN) {
					mAdapter.clearList();
				} else {
					for (RetRelevantPeopleInfo info : mAdapter.getList()) {
						if (info.userinfo.uid.intValue() == msg.arg2) {
							mAdapter.removeObject(info);
							break;
						}
					}
				}

				if (mAdapter.getCount() == 0) {
					mTextRight.setClickable(false);
					mTextRight.setTextColor(getResources().getColor(R.color.gray));
					mPullRefreshListView.setEmptyView(mEmptyDynamic);
				}
			}
			break;
		default:
			break;
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

	private void reload() {
		if (BAApplication.mLocalUserInfo != null) {
			isRefresh = true;
			startLoadPosition = -1;
			getBlackList(BAApplication.mLocalUserInfo.uid.intValue(), startLoadPosition, LOADCOUNT);
		}
	}

	protected void loadMore() {
		if (BAApplication.mLocalUserInfo != null) {
			isRefresh = false;
			startLoadPosition = startLoadPosition - LOADCOUNT;
			getBlackList(BAApplication.mLocalUserInfo.uid.intValue(), startLoadPosition, LOADCOUNT);
		}
	}

	protected void getBlackList(int uid, int start, int num) {//获取我关注人的动态
		biz.getBlackList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, uid, startLoadPosition, LOADCOUNT, mHandler);
	}
}
