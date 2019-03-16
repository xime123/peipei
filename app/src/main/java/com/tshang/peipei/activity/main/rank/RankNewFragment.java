package com.tshang.peipei.activity.main.rank;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.main.rank.adapter.RankAdapter;
import com.tshang.peipei.model.rank.RankBase;
import com.tshang.peipei.model.rank.RankNewCommersFemale;
import com.tshang.peipei.model.rank.RankNewCommersMale;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: RankNewRank.java 
 *
 * 新人排行榜
 *
 * @author allen 
 *
 * @date 2014年9月16日 下午4:57:15 
 *
 * @version V1.3.0   
 */
public class RankNewFragment extends RankBaseFragment {
	private RankNewCommersFemale rankNewFemale;
	private RankNewCommersMale rankNewmale;
	private static final int INIT_NEW_DATA_VALUE = 0x200;
	public static int curType = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rankNewFemale = new RankNewCommersFemale(getActivity(), mHandler);
		rankNewmale = new RankNewCommersMale(getActivity(), mHandler);
		RANK_CURRENT_TYPE = RANK_NEW_FEMALE_TYPE;
		curType = RANK_NEW_FEMALE_TYPE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rank_new, null);

		initNewView(view);
		delayLoadData(INIT_NEW_DATA_VALUE);
		return view;

	}

	private void initNewView(View view) {
		plistview = (PullToRefreshListView) view.findViewById(R.id.rank_new_listview);
		plistview.setAdapter(mRankAdapter);
		plistview.setOnRefreshListener(this);
		plistview.setMode(Mode.BOTH);
		plistview.setOnItemClickListener(this);
	}

	private void setListViewShowData(boolean showHead, int isEnd, int isRefresh, GoGirlUserInfoList giftInfoList) {
		if (isRefresh == RankBase.REFRESH_CODE) {//是刷新数据
			if (!ListUtils.isEmpty(giftInfoList)) {
				mRankAdapter.clearSet();
				mRankAdapter.setList(mRankAdapter.getDifferentUserInfoData(giftInfoList));
			}
		} else {//不是刷新就在底部添加加载更多的数据
			mRankAdapter.appendToList(mRankAdapter.getDifferentUserInfoData(giftInfoList));
		}
		if (isEnd == 0) {//还有数据
			plistview.setMode(Mode.BOTH);
		} else {//加到底了
			plistview.setMode(Mode.PULL_FROM_START);
		}

	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		plistview.onRefreshComplete();
		switch (msg.what) {
		case RankNewCommersFemale.RANK_NEW_FEMALE_SUCCESS://新人女请求成功
		case RankNewCommersMale.RANK_NEW_MALE_SUCCESS://新人男请求成功
			setListViewShowData(false, msg.arg1, msg.arg2, (GoGirlUserInfoList) msg.obj);
			break;
		case INIT_NEW_DATA_VALUE:
			loadInitData();
			break;

		}
	}

	@Override
	public synchronized void refresh(int type) {
		switch (type) {
		case RANK_NEW_MALE_TYPE:
			mRankAdapter.setCurrent_rank_type(RankAdapter.MALE_WEEK_RANK);
			rankNewmale.getRankData(true, 1, false);
			break;
		case RANK_NEW_FEMALE_TYPE:
			mRankAdapter.setCurrent_rank_type(RankAdapter.FEMALE_WEEK_RANK);
			rankNewFemale.getRankData(true, 1, false);
			break;
		}
	}

	@Override
	public synchronized void loadMore(int type) {
		switch (type) {
		case RANK_NEW_FEMALE_TYPE:
			rankNewFemale.getRankData(false, 1, false);
			break;
		case RANK_NEW_MALE_TYPE:
			rankNewmale.getRankData(false, 1, false);
			break;

		}
	}

	private void loadInitData() {
		if (RANK_CURRENT_TYPE == RANK_NEW_FEMALE_TYPE) {
			rankNewFemale.getRankData(true, 1, false);
		} else if (RANK_CURRENT_TYPE == RANK_NEW_MALE_TYPE) {
			rankNewmale.getRankData(true, 1, false);
		}
	}

	@Override
	protected void ViewStateRestored() {//恢复标题栏上面的选择 
		loadInitData();
	}

	public void onCheckedChanged(int arg1) {//选中
		if (arg1 == RANK_NEW_FEMALE_TYPE) {//选中了女的
			RANK_CURRENT_TYPE = RANK_NEW_FEMALE_TYPE;
			curType = RANK_NEW_FEMALE_TYPE;
			rankNewFemale.getRankData(true, 1, true);

		} else {
			RANK_CURRENT_TYPE = RANK_NEW_MALE_TYPE;
			curType = RANK_NEW_MALE_TYPE;
			rankNewmale.getRankData(true, 1, true);
		}
	}
}
