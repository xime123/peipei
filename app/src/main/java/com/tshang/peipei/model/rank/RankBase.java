package com.tshang.peipei.model.rank;

import android.app.Activity;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.main.MainRankListBiz;
import com.tshang.peipei.model.request.RequestGetRankList.IGetRankList;

/**
 * @Title: BaseRank.java 
 *
 * @Description: 排行逻辑基类
 *
 * @author Jeff  
 *
 * @date 2014年7月29日 下午4:44:16 
 *
 * @version V1.0   
 */
public class RankBase implements IGetRankList {

	public static final int REFRESH_CODE = 1;//下拉刷新
	public static final int LOADMORE_CODE = 2;//上拉加载更多

	protected Activity activity;//上下文对象
	protected MainRankListBiz rankBiz;
	protected BAHandler mHandler;
	protected static final int LOADCOUNT = 20;

	public RankBase(Activity activity) {
		this.activity = activity;
		rankBiz = new MainRankListBiz();
	}

	protected void getRankList(int rankId, int start) {
		rankBiz.getRanklist(activity, rankId, start, LOADCOUNT, this);
	}

	public void getRankData(boolean isRefresh) {

	}

	public void getRankData(boolean isRefresh, int type, boolean isUseCache) {

	}

	@Override
	public void getRankListCallBack(int retCode, int total, int end, int type, GoGirlUserInfoList giftInfoList) {}
}
