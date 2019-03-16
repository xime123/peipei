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
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserInfoAndSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserInfoAndSkillInfoList;

/**
 * @Title: HallNewListFemale.java 
 *
 * @Description: 女性大厅新人榜
 *
 * @author Jeff  
 *
 * @date 2014年10月10日 上午11:21:53 
 *
 * @version V1.3.0   
 */
public class HallNewListFemale extends HallBase {
	private boolean isRefresh = true;//区分是刷新状态还是夹在更多状态
	private int startLoad = 2100000000;;//加载位置

	public HallNewListFemale(Activity activity, BAHandler mHandler) {
		super(activity);
		this.mHandler = mHandler;
	}

	@Override
	public void getHallListData(boolean isUseCacheData, boolean isRefresh) {
		//女性日榜
		this.isRefresh = isRefresh;
		if (isRefresh) {
			startLoad = 2100000000;
		}
		if (isUseCacheData) {
			ThreadPoolService.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					String suffix = HallRankCacheViewData.HALL_NEW_FEMALE_FILE_NAME_START;
					File cacheFile = ConfigCacheUtil.getCacheFile(activity, suffix);//读取缓存数据
					if (cacheFile != null) {
						UserAndSkillInfoList lists = HallRankCacheViewData.getGoGirlUserInfoAndSkillListCacheData(activity, suffix);
						mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HALL_GET_NEW_FEMALE_LIST_SUCCESS_VALUE, -1, REFRESH_CODE, lists));//通过handler把数据传到UI界面去更新
						if (!ConfigCacheUtil.getUrlCacheisEffective(cacheFile, ConfigCacheModel.CONFIG_CACHE_MODEL_SHORT)) {
							getFreshOnline(Gender.FEMALE.getValue(), startLoad);//加载女性新人榜
						}
					} else {
						getFreshOnline(Gender.FEMALE.getValue(), startLoad);//加载女性新人榜
					}
				}
			});
		} else {
			getFreshOnline(Gender.FEMALE.getValue(), startLoad);//加载女性新人榜
		}

	}

	@Override
	public void getFreshList(int retCode, int isend, UserAndSkillInfoList userlist) {
		if (retCode == 0) {//网络请求数据成功回调

//			if (userlist != null && !userlist.isEmpty()) {
//				UserInfoAndSkillInfo info = (UserInfoAndSkillInfo) userlist.get(userlist.size() - 1);
//				GoGirlUserInfo userInfo = info.userinfo;
//				startLoad = userInfo.uid.intValue() - 1;
//			}
			if (isRefresh) {
				mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HALL_GET_NEW_FEMALE_LIST_SUCCESS_VALUE, isend, REFRESH_CODE, userlist));//通过handler把数据传到UI界面去更新
				HallRankCacheViewData.setGoGirlUserInfoAndSkillListCacheData(activity, HallRankCacheViewData.HALL_NEW_FEMALE_FILE_NAME_START, userlist);
			} else {
				mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HALL_GET_NEW_FEMALE_LIST_SUCCESS_VALUE, isend, LOADMORE_CODE, userlist));
			}
		} else {
			mHandler.sendEmptyMessage(HandlerValue.HALL_GET_NEW_FEMALE_LIST_FAILED_VALUE);//加载失败处理
		}
	}

}
