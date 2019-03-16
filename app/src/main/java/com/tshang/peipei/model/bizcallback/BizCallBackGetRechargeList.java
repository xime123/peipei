package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.RechargeRatioInfoList;

/**
 * @Title: BizCallBackGetRechargeList.java 
 *
 * @Description: 获取充值列表回调 
 *
 * @author allen  
 *
 * @date 2014-4-19 上午9:55:27 
 *
 * @version V1.0   
 */
public interface BizCallBackGetRechargeList {
	public void getGetRechargeRatioList(int retCode, RechargeRatioInfoList rechargeRatioInfoList);
}
