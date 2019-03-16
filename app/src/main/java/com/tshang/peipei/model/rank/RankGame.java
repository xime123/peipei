package com.tshang.peipei.model.rank;

import java.io.File;
import java.util.Collections;

import android.app.Activity;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.viewdatacache.HallRankCacheViewData;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil.ConfigCacheModel;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;

/**
 * @Title: RankGame.java 
 *
 * @Description: 游戏榜
 *
 * @author Jeff  
 *
 * @date 2014年7月29日 下午4:47:19 
 *
 * @version V1.3.0   
 */
public class RankGame extends RankBase {
	public static final int RANK_GAME_SUCCESS = 0x126;//定义一个值通知去更新界面，数据是成功的
	public static final int RANK_GAME_FAILED = 0x127;//定义一个值加载失败了
	public static final int RANK_GAME_LOAD_ALL = 0x128;//已经加载到底了
	private boolean isRefresh = true;//区分是刷新状态还是夹在更多状态
	private int startLoad = -1;

	public RankGame(Activity activity, BAHandler mHandler) {
		super(activity);
		this.mHandler = mHandler;
	}

	@Override
	public void getRankData(boolean isRefresh, int type, boolean isUseCache) {
		System.out.println("这里没有捡来吗？？？？？？、、、、");
		this.isRefresh = isRefresh;
		if (isRefresh) {
			startLoad = -1;
			if (isUseCache) {
				ThreadPoolService.getInstance().execute(new Runnable() {

					@Override
					public void run() {
						File cacheFile = ConfigCacheUtil.getCacheFile(activity, HallRankCacheViewData.RANK_GAME_FILE_NAME_START);//读取缓存数据
						if (cacheFile != null) {
							GoGirlUserInfoList lists = HallRankCacheViewData.getGoGirlUserInfoListCacheData(activity,
									HallRankCacheViewData.RANK_GAME_FILE_NAME_START);
							HandlerUtils.sendHandlerMessage(mHandler, RANK_GAME_SUCCESS, 0, REFRESH_CODE, lists);
							if (!ConfigCacheUtil.getUrlCacheisEffective(cacheFile, ConfigCacheModel.CONFIG_CACHE_MODEL_SHORT)) {
								getRankList(BAConstants.RankType.gameRank, startLoad);//请求网络加载数据
							} else {
								startLoad -= LOADCOUNT;
							}
						} else {
							getRankList(BAConstants.RankType.gameRank, startLoad);//请求网络加载数据
						}
					}
				});
			} else {
				getRankList(BAConstants.RankType.gameRank, startLoad);//请求网络加载数据
			}

		} else {
			getRankList(BAConstants.RankType.gameRank, startLoad);//请求网络加载数据
		}

	}

	@Override
	public void getRankListCallBack(int retCode, int total, int end, int type, GoGirlUserInfoList giftInfoList) {//end ==1说明到底了，最后一条数据

		if (retCode == 0) {//网络请求数据成功回调
			startLoad -= LOADCOUNT;
			Collections.reverse(giftInfoList);
			if (isRefresh) {
				mHandler.sendMessage(mHandler.obtainMessage(RANK_GAME_SUCCESS, end, REFRESH_CODE, giftInfoList));
				HallRankCacheViewData.setGoGirlUserInfoListCacheData(activity, HallRankCacheViewData.RANK_GAME_FILE_NAME_START, giftInfoList);
			} else {
				mHandler.sendMessage(mHandler.obtainMessage(RANK_GAME_SUCCESS, end, LOADMORE_CODE, giftInfoList));
			}
		} else {
			HandlerUtils.sendHandlerMessage(mHandler, RANK_GAME_FAILED);//加载失败处理

		}
	}

}
