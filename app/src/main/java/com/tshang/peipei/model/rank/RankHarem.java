package com.tshang.peipei.model.rank;

import java.io.File;
import java.util.Collections;

import android.app.Activity;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.request.RequestGetRankGroupList.IGetRankGroupList;
import com.tshang.peipei.model.viewdatacache.HallRankCacheViewData;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil.ConfigCacheModel;
import com.tshang.peipei.network.socket.ThreadPoolService;

/**
 * @Title: RankGame.java 
 *
 * @Description: 后宫排行
 *
 * @author Jeff  
 *
 * @date 2014年7月29日 下午4:47:19 
 *
 * @version V1.3.0   
 */
public class RankHarem extends RankBase implements IGetRankGroupList {
	public static final int RANK_HAREM_SUCCESS = 0x129;//定义一个值通知去更新界面，数据是成功的
	public static final int RANK_HAREM_FAILED = 0x130;//定义一个值加载失败了
	public static final int RANK_HAREM_LOAD_ALL = 0x131;//已经加载到底了
	private boolean isRefresh = true;//区分是刷新状态还是夹在更多状态
	private int startLoad = -1;

	public RankHarem(Activity activity, BAHandler mHandler) {
		super(activity);
		this.mHandler = mHandler;
	}

	@Override
	public void getRankData(boolean isRefresh, int type, boolean isUseCache) {

		this.isRefresh = isRefresh;
		if (isRefresh) {
			startLoad = -1;
			if (isUseCache) {
				ThreadPoolService.getInstance().execute(new Runnable() {

					@Override
					public void run() {
						File cacheFile = ConfigCacheUtil.getCacheFile(activity, HallRankCacheViewData.Rank_GAME_HAREM_FILE_NAME_START);//读取缓存数据
						if (cacheFile != null) {
							GroupInfoList lists = HallRankCacheViewData.getGroupInfoListCacheData(activity,
									HallRankCacheViewData.Rank_GAME_HAREM_FILE_NAME_START);
							HandlerUtils.sendHandlerMessage(mHandler, RANK_HAREM_SUCCESS, 0, REFRESH_CODE, lists);
							if (!ConfigCacheUtil.getUrlCacheisEffective(cacheFile, ConfigCacheModel.CONFIG_CACHE_MODEL_SHORT)) {
								rankBiz.getHaremRanklist(activity, startLoad, LOADCOUNT, RankHarem.this);//请求网络加载数据
							} else {
								startLoad -= LOADCOUNT;
							}
						} else {
							rankBiz.getHaremRanklist(activity, startLoad, LOADCOUNT, RankHarem.this);//请求网络加载数据
						}
					}
				});
			} else {
				rankBiz.getHaremRanklist(activity, startLoad, LOADCOUNT, this);//请求网络加载数据
			}
		} else {
			rankBiz.getHaremRanklist(activity, startLoad, LOADCOUNT, this);//请求网络加载数据
		}

	}

	@Override
	public void getGroupInfoList(int retCode, GroupInfoList infoList, int isEnd, int total) {
		if (retCode == 0) {//网络请求数据成功回调
			startLoad -= LOADCOUNT;
			Collections.reverse(infoList);
			if (isRefresh) {
				HandlerUtils.sendHandlerMessage(mHandler, RANK_HAREM_SUCCESS, isEnd, REFRESH_CODE, infoList);
				HallRankCacheViewData.setGroupInfoListCacheData(activity, HallRankCacheViewData.Rank_GAME_HAREM_FILE_NAME_START, infoList);
			} else {
				HandlerUtils.sendHandlerMessage(mHandler, RANK_HAREM_SUCCESS, isEnd, LOADMORE_CODE, infoList);
			}
		} else {
			HandlerUtils.sendHandlerMessage(mHandler, RANK_HAREM_FAILED);//加载失败处理
		}
	}

}
