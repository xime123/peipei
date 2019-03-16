package com.tshang.peipei.activity.main.rank;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.main.rank.adapter.RankAdapter;
import com.tshang.peipei.activity.main.rank.adapter.RankHaremAdapter;
import com.tshang.peipei.model.rank.RankBase;
import com.tshang.peipei.model.rank.RankGame;
import com.tshang.peipei.model.rank.RankHarem;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;
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
public class RankGameFragment extends RankBaseFragment {
	private RankGame rankGame;
	private RankHarem rankHarem;
	private static final int INIT_NEW_DATA_VALUE = 0x205;
	private RankHaremAdapter haremAdapter;
	public static int curType = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rankGame = new RankGame(getActivity(), mHandler);
		rankHarem = new RankHarem(getActivity(), mHandler);
		RANK_CURRENT_TYPE = RANK_NEW_FEMALE_TYPE;
		curType = RANK_NEW_FEMALE_TYPE;
		haremAdapter = new RankHaremAdapter(getActivity());
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
		plistview.setOnRefreshListener(this);
		plistview.setMode(Mode.BOTH);
		plistview.setOnItemClickListener(this);
		mRankAdapter.setCurrent_rank_type(RankAdapter.GAME_TOTAL_WIN_RANK);
	}

	private void setListViewShowData(boolean showHead, int isEnd, int isRefresh, boolean isMan, GoGirlUserInfoList giftInfoList) {
		if (isRefresh == RankBase.REFRESH_CODE) {//是刷新数据
			mRankAdapter.clearSet();
			mRankAdapter.clearList();
			mRankAdapter.setList(mRankAdapter.getDifferentUserInfoData(giftInfoList));
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
		case RankGame.RANK_GAME_SUCCESS://游戏榜请求成功
			setListViewShowData(false, msg.arg1, msg.arg2, false, (GoGirlUserInfoList) msg.obj);
			break;

		case INIT_NEW_DATA_VALUE:
			loadInitData();
			break;
		case RankHarem.RANK_HAREM_SUCCESS:
			GroupInfoList groupInfoList = (GroupInfoList) msg.obj;
			if (msg.arg2 == RankBase.REFRESH_CODE) {//是刷新数据
				if (!ListUtils.isEmpty(groupInfoList)) {
					haremAdapter.clearSet();
					haremAdapter.setList(haremAdapter.getDifferentUserInfoData(groupInfoList));
				}
			} else {//不是刷新就在底部添加加载更多的数据
				haremAdapter.appendToList(haremAdapter.getDifferentUserInfoData(groupInfoList));
			}
			if (msg.arg1 == 0) {//还有数据
				plistview.setMode(Mode.BOTH);
			} else {//加到底了
				plistview.setMode(Mode.PULL_FROM_START);
			}
			break;

		}
	}

	@Override
	public synchronized void refresh(int type) {
		switch (type) {
		case RANK_NEW_MALE_TYPE:
			rankHarem.getRankData(true, 1, false);
			break;
		case RANK_NEW_FEMALE_TYPE:
			mRankAdapter.setCurrent_rank_type(RankAdapter.GAME_TOTAL_WIN_RANK);
			rankGame.getRankData(true, 1, false);
			break;
		}
	}

	@Override
	public synchronized void loadMore(int type) {
		switch (type) {
		case RANK_NEW_FEMALE_TYPE:
			rankGame.getRankData(false, 1, false);
			break;
		case RANK_NEW_MALE_TYPE:
			rankHarem.getRankData(false, 1, false);
			break;

		}
	}

	private void loadInitData() {
		if (RANK_CURRENT_TYPE == RANK_NEW_FEMALE_TYPE) {
			plistview.setAdapter(mRankAdapter);
			rankGame.getRankData(true, 1, false);
		} else if (RANK_CURRENT_TYPE == RANK_NEW_MALE_TYPE) {
			rankHarem.getRankData(true, 1, false);
		}
	}

	@Override
	protected void ViewStateRestored() {//恢复标题栏上面的选择 
		loadInitData();
	}

	public void onCheckedChanged(int arg1) {
		if (arg1 == RANK_NEW_FEMALE_TYPE) {//选中了女的
			RANK_CURRENT_TYPE = RANK_NEW_FEMALE_TYPE;
			curType = RANK_NEW_FEMALE_TYPE;
			plistview.setAdapter(mRankAdapter);
			rankGame.getRankData(true, 1, true);
		} else {
			RANK_CURRENT_TYPE = RANK_NEW_MALE_TYPE;
			curType = RANK_NEW_MALE_TYPE;
			plistview.setAdapter(haremAdapter);
			rankHarem.getRankData(true, 1, true);
		}

	}

}
