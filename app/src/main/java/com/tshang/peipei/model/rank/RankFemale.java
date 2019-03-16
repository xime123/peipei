package com.tshang.peipei.model.rank;

import java.io.File;
import java.util.Collections;

import android.app.Activity;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.viewdatacache.HallRankCacheViewData;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil.ConfigCacheModel;
import com.tshang.peipei.network.socket.ThreadPoolService;

/**
 * @Title: RankDayFemale.java 
 *
 * @Description: 女性日榜
 *
 * @author Administrator  
 *
 * @date 2014年7月29日 下午4:47:19 
 *
 * @version V1.0   
 */
public class RankFemale extends RankBase {
	public static final int RANK_DAY_FEMALE_SUCCESS = 0x101;//定义一个值通知去更新界面，数据是成功的
	public static final int RANK_DAY_FEMALE_FAILED = 0x102;//定义一个值加载失败了
	public static final int RANK_TOTAL_FEMALE_SUCCESS = 0x107;//定义一个值通知去更新界面，数据是成功的
	public static final int RANK_WEEK_FEMALE_SUCCESS = 0x104;//定义一个值通知去更新界面，数据是成功的
	private boolean isRefresh = true;//区分是刷新状态还是夹在更多状态
	private int startLoad = -1;

	public RankFemale(Activity activity, BAHandler mHandler) {
		super(activity);
		this.mHandler = mHandler;
	}

	@Override
	public void getRankData(boolean isRefresh, final int type, final boolean isUseCache) {
		this.isRefresh = isRefresh;
		if (isRefresh) {
			startLoad = -1;
			if (isUseCache) {
				ThreadPoolService.getInstance().execute(new Runnable() {

					@Override
					public void run() {
						String suffixName = getMaleSuffixName(type);
						File cacheFile = ConfigCacheUtil.getCacheFile(activity, suffixName);//读取缓存数据
						if (cacheFile != null) {
							GoGirlUserInfoList lists = HallRankCacheViewData.getGoGirlUserInfoListCacheData(activity, suffixName);
							if (type == BAConstants.RankType.femalDayRank) {
								HandlerUtils.sendHandlerMessage(mHandler, RANK_DAY_FEMALE_SUCCESS, 0, REFRESH_CODE, lists);
							} else if (type == BAConstants.RankType.femalWeekRank) {
								HandlerUtils.sendHandlerMessage(mHandler, RANK_WEEK_FEMALE_SUCCESS, 0, REFRESH_CODE, lists);
							} else if (type == BAConstants.RankType.femalMonthRank) {
								HandlerUtils.sendHandlerMessage(mHandler, RANK_TOTAL_FEMALE_SUCCESS, 0, REFRESH_CODE, lists);
							}
							if (!ConfigCacheUtil.getUrlCacheisEffective(cacheFile, ConfigCacheModel.CONFIG_CACHE_MODEL_SHORT)) {
								getRankList(type, startLoad);//请求网络夹在数据
							} else {
								startLoad -= LOADCOUNT;
							}
						} else {
							getRankList(type, startLoad);//请求网络夹在数据
						}
					}
				});
			} else {
				getRankList(type, startLoad);//请求网络夹在数据
			}
		} else {
			getRankList(type, startLoad);//请求网络夹在数据
		}

	}

	private String getMaleSuffixName(int type) {
		if (type == BAConstants.RankType.femalDayRank) {
			return HallRankCacheViewData.RANK_GLAMOUR_DAY_FILE_NAME_START;
		} else if (type == BAConstants.RankType.femalWeekRank) {
			return HallRankCacheViewData.RANK_GLAMOUR_WEEK_FILE_NAME_START;
		} else {
			return HallRankCacheViewData.RANK_GLAMOUR_TOTAL_FILE_NAME_START;
		}

	}

	@Override
	public void getRankListCallBack(int retCode, int total, int end, int type, GoGirlUserInfoList giftInfoList) {//end ==1说明到底了，最后一条数据
		int what_success = RANK_DAY_FEMALE_SUCCESS;
		if (type == BAConstants.RankType.femalDayRank) {
			what_success = RANK_DAY_FEMALE_SUCCESS;
		} else if (type == BAConstants.RankType.femalWeekRank) {
			what_success = RANK_WEEK_FEMALE_SUCCESS;
		} else {
			what_success = RANK_TOTAL_FEMALE_SUCCESS;
		}

		if (retCode == 0) {//网络请求数据成功回调
			startLoad -= LOADCOUNT;
			Collections.reverse(giftInfoList);
			if (isRefresh) {
				mHandler.sendMessage(mHandler.obtainMessage(what_success, end, REFRESH_CODE, giftInfoList));
				HallRankCacheViewData.setGoGirlUserInfoListCacheData(activity, getMaleSuffixName(type), giftInfoList);
			} else {
				mHandler.sendMessage(mHandler.obtainMessage(what_success, end, LOADMORE_CODE, giftInfoList));
			}
		} else {
			mHandler.sendEmptyMessage(RANK_DAY_FEMALE_FAILED);//加载失败处理
		}
	}

}
