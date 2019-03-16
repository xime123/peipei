package com.tshang.peipei.model.biz.store;

import java.math.BigInteger;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RechargeRatioInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRechargeNoV2;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackGetRechargeList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetRechargeNo;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserProperty;
import com.tshang.peipei.model.request.RequestGetRechargeNo;
import com.tshang.peipei.model.request.RequestGetRechargeNo.IGetRechargeNoCallBack;
import com.tshang.peipei.model.request.RequestGetRechargeRatioList;
import com.tshang.peipei.model.request.RequestGetRechargeRatioList.IGetRechargeRatioListCallBack;
import com.tshang.peipei.model.request.RequestGetUserProperty;
import com.tshang.peipei.model.request.RequestGetUserProperty.IGetUserPropertyCallBack;

/**
 * @Title: StoreUserBiz.java 
 *
 * @Description: 商城中用户信息的拉取，充值等
 *
 * @author allen  
 *
 * @date 2014-4-12 上午11:59:35 
 *
 * @version V1.0   
 */
public class StoreUserBiz implements IGetUserPropertyCallBack, IGetRechargeRatioListCallBack, IGetRechargeNoCallBack {

	private BizCallBackGetUserProperty mUserProperty;
	private BizCallBackGetRechargeList mGetRecharge;
	private BizCallBackGetRechargeNo mGetRechargeNo;

	private static StoreUserBiz instance = null;

	public static StoreUserBiz getInstance() {
		if (instance == null) {
			synchronized (StoreUserBiz.class) {
				if (instance == null) {
					instance = new StoreUserBiz();
				}
			}
		}
		return instance;
	}

	/**
	 * 获取用户信息
	 *
	 */
	public void getUserProperty(Activity activity, int uid, BizCallBackGetUserProperty callback) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		if (userEntity != null) {
			auth = userEntity.auth;
		}
		RequestGetUserProperty req = new RequestGetUserProperty();
		mUserProperty = callback;
		req.getUserProperty(auth, BAApplication.app_version_code, uid, this);
	};

	@Override
	public void getGetUserPropertyCallBack(int retCode, UserPropertyInfo userPropertyInfo) {
		if (mUserProperty != null) {
			mUserProperty.getUserProperty(retCode, userPropertyInfo);
		}
	};

	/**
	 * 获取充值列表
	 */
	public void getRechargeRatioList(byte[] auth, int ver, int uid, BizCallBackGetRechargeList callback) {
		RequestGetRechargeRatioList req = new RequestGetRechargeRatioList();
		mGetRecharge = callback;
		req.getRechargeRatioList(auth, ver, uid, this);
	}

	@Override
	public void getGetRechargeRatioListCallBack(int retCode, RechargeRatioInfoList rechargeRatioInfoList) {
		if (mGetRecharge != null) {
			mGetRecharge.getGetRechargeRatioList(retCode, rechargeRatioInfoList);
		}
	}

	/**
	 * 用户充值
	 *
	 */
	public void reChargeToUser(byte[] auth, int ver, int uid, BigInteger rechargeid, int anonymity, BizCallBackGetRechargeNo callBack) {
		RequestGetRechargeNo req = new RequestGetRechargeNo();
		mGetRechargeNo = callBack;
		req.getRechargeNo(auth, ver, uid, rechargeid, anonymity, this);
	}

	@Override
	public void getRechargeNoCallBack(int retCode, RspGetRechargeNoV2 data) {
		if (mGetRechargeNo != null) {
			mGetRechargeNo.getRechargeNo(retCode, data);
		}
	}

}
