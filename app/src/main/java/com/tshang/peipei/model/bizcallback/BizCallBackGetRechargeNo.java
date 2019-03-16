package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.RspGetRechargeNoV2;

/**
 * @Title: BizCallBackGetRechargeNo.java 
 *
 * @Description: 充值回调
 *
 * @author allen  
 *
 * @date 2014-4-19 上午11:04:30 
 *
 * @version V1.0   
 */
public interface BizCallBackGetRechargeNo {
	public void getRechargeNo(int retCode, RspGetRechargeNoV2 data);
}
