package com.tshang.peipei.model.hall;

import java.io.File;

import android.app.Activity;

import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.viewdatacache.HallRankCacheViewData;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil.ConfigCacheModel;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserInfoAndSkillInfoList;

/**
 * @Title: HallOnTimeListFemale.java 
 *
 * @Description: 女性附近
 *
 * @author Jeff  
 *
 * @date 2014年10月10日 上午11:20:19 
 *
 * @version V1.3.0   
 */
public class HallNearListFemale extends HallBase {
	private boolean isRefresh = true;//区分是刷新状态还是夹在更多状态
	private int startLoad = 0;//加载位置
	private int nextdis = 0;

	public int getNextdis() {
		return nextdis;
	}

	public void setNextdis(int nextdis) {
		this.nextdis = nextdis;
	}

	public HallNearListFemale(Activity activity, BAHandler mHandler) {
		super(activity);
		this.mHandler = mHandler;
	}

	@Override
	public void getHallListData(boolean isUseCacheData, boolean isRefresh) {//女性日榜
		this.isRefresh = isRefresh;
		if (isRefresh) {
			nextdis = 0;
			startLoad = 0;
		}
		if (isUseCacheData) {
			ThreadPoolService.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					String suffix = HallRankCacheViewData.HALL_NEAR_FEMALE_FILE_NAME_START;
					File cacheFile = ConfigCacheUtil.getCacheFile(activity, suffix);//读取缓存数据
					if (cacheFile != null) {
						UserAndSkillInfoList lists = HallRankCacheViewData.getGoGirlUserInfoAndSkillListCacheData(activity, suffix);
						mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HALL_GET_NEAR_LIST_FEMALE_SUCCESS_VALUE, -1, REFRESH_CODE, lists));//通过handler把数据传到UI界面去更新
						if (!ConfigCacheUtil.getUrlCacheisEffective(cacheFile, ConfigCacheModel.CONFIG_CACHE_MODEL_SHORT)) {
							getHallNearUserList(Gender.FEMALE.getValue(), startLoad, nextdis);//加载女性最新在线
						}
					} else {
						getHallNearUserList(Gender.FEMALE.getValue(), startLoad, nextdis);//加载女性最新在线
					}
				}
			});
		} else {
			getHallNearUserList(Gender.FEMALE.getValue(), startLoad, nextdis);//加载女性最新在线
		}

	}

	@Override
	public void getNearUserListCallBack(int retCode, int isEnd, int nextstart, int nextdis, UserAndSkillInfoList list) {
		if (retCode == 0) {//网络请求数据成功回调
			this.startLoad = nextstart;
			this.nextdis = nextdis;
			if (nextdis == -1) {
				isEnd = 1;
			}
			if (isRefresh) {
				mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HALL_GET_NEAR_LIST_FEMALE_SUCCESS_VALUE, isEnd, REFRESH_CODE, list));//通过handler把数据传到UI界面去更新
				HallRankCacheViewData.setGoGirlUserInfoAndSkillListCacheData(activity, HallRankCacheViewData.HALL_NEAR_FEMALE_FILE_NAME_START, list);
			} else {
				mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HALL_GET_NEAR_LIST_FEMALE_SUCCESS_VALUE, isEnd, LOADMORE_CODE, list));
			}
		} else {
			mHandler.sendEmptyMessage(HandlerValue.HALL_GET_NEAR_LIST_FEMALE_FAILED_VALUE);//加载失败处理
		}
	}

}
