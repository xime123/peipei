package com.tshang.peipei.activity.main.rank;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.main.rank.adapter.RankAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.rank.RankFemale;

/**
 * @Title: RankFemaleRank.java 
 *
 * 魅力排行榜
 *
 * @author Jeff 
 *
 * @date 2014年7月29日 下午6:21:15 
 *
 * @version V1.0   
 */
public class RankFemaleFragment extends RankGlamourWealthBaseFragment {
	private RankFemale rankFemale;
	private static final int INTI_FEMALE_DATA_VALUE = 0x201;
	public static int curType = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rankFemale = new RankFemale(getActivity(), mHandler);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		iv_top_level_first_head.setImageResource(R.drawable.rank_img_girl1_1);
		iv_top_level_second_head.setImageResource(R.drawable.rank_img_girl2_2);
		iv_top_level_third_head.setImageResource(R.drawable.rank_img_girl3_3);
		RANK_CURRENT_TYPE = RANK_DAY_TYPE;
		curType = RANK_DAY_TYPE;
		delayLoadData(INTI_FEMALE_DATA_VALUE);
	}

	@Override
	protected void initLoadData() {
		if (RANK_CURRENT_TYPE == RANK_DAY_TYPE) {
			mRankAdapter.setCurrent_rank_type(RankAdapter.FEMALE_DAY_RANK);
			rankFemale.getRankData(true, BAConstants.RankType.femalDayRank, true);
		} else if (RANK_CURRENT_TYPE == RANK_WEEK_TYPE) {
			mRankAdapter.setCurrent_rank_type(RankAdapter.FEMALE_WEEK_RANK);
			rankFemale.getRankData(true, BAConstants.RankType.femalWeekRank, true);
		} else if (RANK_CURRENT_TYPE == RANK_TOTAL_TYPE) {
			mRankAdapter.setCurrent_rank_type(RankAdapter.FEMALE_TOTAL_RANK);
			rankFemale.getRankData(true, BAConstants.RankType.femalMonthRank, true);
		}

	}

	@Override
	public void dispatchMessage(Message msg) {
		plistview.onRefreshComplete();
		super.dispatchMessage(msg);
		switch (msg.what) {
		case RankFemale.RANK_DAY_FEMALE_SUCCESS://日榜数据请求成功
			setData(true, msg.arg1, msg.arg2, false, (GoGirlUserInfoList) msg.obj);
			break;
		case RankFemale.RANK_TOTAL_FEMALE_SUCCESS://总榜数据请求成功
		case RankFemale.RANK_WEEK_FEMALE_SUCCESS://周榜数据请求成功
			setData(false, msg.arg1, msg.arg2, false, (GoGirlUserInfoList) msg.obj);
			break;
		case INTI_FEMALE_DATA_VALUE:
			initLoadData();
			break;

		default:
			break;
		}
	}

	@Override
	public synchronized void refresh(int type) {
		switch (type) {
		case RANK_TOTAL_TYPE:
			mRankAdapter.setCurrent_rank_type(RankAdapter.FEMALE_TOTAL_RANK);
			rankFemale.getRankData(true, BAConstants.RankType.femalMonthRank, false);

			break;
		case RANK_WEEK_TYPE:
			mRankAdapter.setCurrent_rank_type(RankAdapter.FEMALE_WEEK_RANK);
			rankFemale.getRankData(true, BAConstants.RankType.femalWeekRank, false);

			break;
		case RANK_DAY_TYPE:
			mRankAdapter.setCurrent_rank_type(RankAdapter.FEMALE_DAY_RANK);
			rankFemale.getRankData(true, BAConstants.RankType.femalDayRank, false);
			break;

		}
	}

	@Override
	public synchronized void loadMore(int type) {
		switch (type) {
		case RANK_TOTAL_TYPE:
			rankFemale.getRankData(false, BAConstants.RankType.femalMonthRank, false);
			break;
		case RANK_WEEK_TYPE:
			rankFemale.getRankData(false, BAConstants.RankType.femalWeekRank, false);
			break;
		case RANK_DAY_TYPE:
			rankFemale.getRankData(false, BAConstants.RankType.femalDayRank, false);
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
