package com.tshang.peipei.model.biz.user;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackShowGiftToday;
import com.tshang.peipei.model.request.RequestShowGiftToday;
import com.tshang.peipei.model.request.RequestShowGiftToday.IShowGiftTodayCallBack;

/**
 * @Title: ShowGiftBiz.java 
 *
 * @Description: 展示今天收到的礼物，全部礼物，回赠飞吻
 *
 * @author allen  
 *
 * @date 2014-4-12 下午2:35:40 
 *
 * @version V1.0   
 */
public class ShowGiftBiz implements IShowGiftTodayCallBack {

	private BizCallBackShowGiftToday mBizCallBackShowGiftToday;

	/**
	 * 今天收到的礼物
	 */
	public void showGiftToday(Activity activity, int uid, int start, int num, BizCallBackShowGiftToday showGift, int mCurrPage) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		int selfuid = -1;
		byte[] auth = "".getBytes();
		if (userEntity != null) {
			selfuid = userEntity.uid.intValue();
			auth = userEntity.auth;
		}
		RequestShowGiftToday requestShowGiftToday = new RequestShowGiftToday();
		mBizCallBackShowGiftToday = showGift;
		requestShowGiftToday.showGiftListToday(auth, BAApplication.app_version_code, uid, selfuid, start, num, mCurrPage, this);
	}

	/**
	 * 所有礼物
	 */
	public void showGiftAll() {}

	/**
	 * 回赠飞吻
	 */
	public void feedKiss() {}

	@Override
	public void showGiftTodayCallBack(int retCode, int total, int currPage, GiftDealInfoList giftInfoList) {
		if (mBizCallBackShowGiftToday != null) {
			mBizCallBackShowGiftToday.showGiftToday(retCode, total, currPage, giftInfoList);
		}
	}
}
