package com.tshang.peipei.activity.main.rank;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.main.rank.adapter.RankAdapter;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.rank.RankMale;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;

/**
 * @Title: RankMaleFt.java 
 *
 * 男性排行榜
 *
 * @author Jeff 
 *
 * @date 2014年7月29日 下午6:21:15 
 *
 * @version V1.3.0   
 */
public class RankMaleFragment extends RankGlamourWealthBaseFragment {
	private RankMale rankMale;
	private static final int INIT_MALE_DATA_VALUE = 0x202;
	public static int curType = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rankMale = new RankMale(getActivity(), mHandler);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		iv_top_level_first_head.setImageResource(R.drawable.rank_img_boy1_1);
		iv_top_level_second_head.setImageResource(R.drawable.rank_img_boy2_2);
		iv_top_level_third_head.setImageResource(R.drawable.rank_img_boy3_3);
		RANK_CURRENT_TYPE = RANK_DAY_TYPE;
		curType = RANK_DAY_TYPE;
		delayLoadData(INIT_MALE_DATA_VALUE);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		plistview.onRefreshComplete();
		switch (msg.what) {
		case RankMale.RANK_DAY_MALE_SUCCESS://日榜数据请求成功
			setData(true, msg.arg1, msg.arg2, true, (GoGirlUserInfoList) msg.obj);
			break;
		case RankMale.RANK_WEEK_MALE_SUCCESS://周榜数据请求成功
		case RankMale.RANK_TOTAL_MALE_SUCCESS://总榜数据请求成功
			setData(false, msg.arg1, msg.arg2, true, (GoGirlUserInfoList) msg.obj);
			break;
		case INIT_MALE_DATA_VALUE:
			initLoadData();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initLoadData() {
		if (RANK_CURRENT_TYPE == RANK_DAY_TYPE) {
			mRankAdapter.setCurrent_rank_type(RankAdapter.MALE_DAY_RANK);
			rankMale.getRankData(true, BAConstants.RankType.cosumeDayRank, true);
		} else if (RANK_CURRENT_TYPE == RANK_WEEK_TYPE) {
			mRankAdapter.setCurrent_rank_type(RankAdapter.MALE_WEEK_RANK);
			rankMale.getRankData(true, BAConstants.RankType.cosumeWeekRank, true);
		} else if (RANK_CURRENT_TYPE == RANK_TOTAL_TYPE) {
			mRankAdapter.setCurrent_rank_type(RankAdapter.MALE_TOTAL_RANK);
			rankMale.getRankData(true, BAConstants.RankType.cosumeTotalRank, true);
		}

	}

	@Override
	public synchronized void refresh(int type) {
		switch (type) {
		case RANK_TOTAL_TYPE:
			mRankAdapter.setCurrent_rank_type(RankAdapter.MALE_TOTAL_RANK);
			rankMale.getRankData(true, BAConstants.RankType.cosumeTotalRank, false);

			break;
		case RANK_WEEK_TYPE:
			mRankAdapter.setCurrent_rank_type(RankAdapter.MALE_WEEK_RANK);
			rankMale.getRankData(true, BAConstants.RankType.cosumeWeekRank, false);

			break;
		case RANK_DAY_TYPE:
			mRankAdapter.setCurrent_rank_type(RankAdapter.MALE_DAY_RANK);
			rankMale.getRankData(true, BAConstants.RankType.cosumeDayRank, false);
			break;

		}
	}

	@Override
	public synchronized void loadMore(int type) {
		switch (type) {
		case RANK_TOTAL_TYPE:
			rankMale.getRankData(false, BAConstants.RankType.cosumeTotalRank, false);
			break;
		case RANK_WEEK_TYPE:
			rankMale.getRankData(false, BAConstants.RankType.cosumeWeekRank, false);
			break;
		case RANK_DAY_TYPE:
			rankMale.getRankData(false, BAConstants.RankType.cosumeDayRank, false);
			break;

		}
	}
	
	public void onCheckedChanged(int arg1) {
		if (arg1 == RANK_DAY_TYPE) {//选中了日榜
			RANK_CURRENT_TYPE = RANK_DAY_TYPE;
			curType = RANK_DAY_TYPE;
		} else if (arg1 == RANK_WEEK_TYPE) {//选中了周榜
			RANK_CURRENT_TYPE = RANK_WEEK_TYPE;
			curType = RANK_WEEK_TYPE;
		} else {//选中了总榜
			RANK_CURRENT_TYPE = RANK_TOTAL_TYPE;
			curType = RANK_TOTAL_TYPE;
		}
		initLoadData();//点击完了开始刷新数据
	}

}
