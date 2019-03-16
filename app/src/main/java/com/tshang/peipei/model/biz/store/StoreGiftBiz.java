package com.tshang.peipei.model.biz.store;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestDeliverGift;
import com.tshang.peipei.model.request.RequestDeliverGift.IDeliverGiftCallBack;
import com.tshang.peipei.model.request.RequestDeliverGiftV3.IDeliverGiftCallBackV3;
import com.tshang.peipei.model.request.RequestDeliverGiftV3;
import com.tshang.peipei.model.request.RequestGetGiftList;
import com.tshang.peipei.model.request.RequestGetGiftList.IGetGiftListCallBack;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: StoreGiftBiz.java 
 *
 * @Description: 礼品列表相关的逻辑处理
 *
 * @author allen  
 *
 * @date 2014-4-12 上午11:02:45 
 *
 * @version V1.0   
 */
public class StoreGiftBiz implements IGetGiftListCallBack, IDeliverGiftCallBack ,IDeliverGiftCallBackV3 {

	private BAHandler handler;

	/**
	 * 购买礼物
	 *
	 */
	public void buyGift(Activity activity, int id, int fuid, int num, int isanonymous, BAHandler handler) {
		RequestDeliverGift requestDeliverGift = new RequestDeliverGift();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		this.handler = handler;
		if (userInfo == null) {
			return;
		}
		requestDeliverGift.getDeliverGift(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), id, fuid, num,
				isanonymous, this);
	};
	
	/**
	 * 购买礼物
	 *
	 */
	public void buyGiftV3(Activity activity, int id, int fuid, int num, int isanonymous, BAHandler handler) {
		RequestDeliverGiftV3 requestDeliverGift = new RequestDeliverGiftV3();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		this.handler = handler;
		if (userInfo == null) {
			return;
		}
		requestDeliverGift.getDeliverGift(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), id, fuid, num,
				isanonymous, this);
	};

	@Override
	public void getDeliverGiftCallBack(int retCode) {
		handler.sendMessage(handler.obtainMessage(HandlerType.USER_GIFT_REQUEST, retCode, retCode));
	};

	/**
	 * 拉取礼物列表
	 */
	public void getGiftList(Activity activity, BAHandler handler) {
		RequestGetGiftList requestGetGiftList = new RequestGetGiftList();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		this.handler = handler;
		if (userInfo == null) {
			return;
		}
		requestGetGiftList.getGiftList(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), this);
	}

	@Override
	public void getGiftListCallBack(int retCode, GiftInfoList giftInfoList) {
		handler.sendMessage(handler.obtainMessage(HandlerType.CREATE_GETDATA_BACK, retCode, retCode, giftInfoList));
	}

	@Override
	public void getDeliverGiftCallBackV3(int retCode) {
		handler.sendMessage(handler.obtainMessage(HandlerType.USER_GIFT_REQUEST, retCode, retCode));
	}

}
