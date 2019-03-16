package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;

/**
 * @Title: BizCallBackGetUserProperty.java 
 *
 * @Description: 获取账号金币回调
 *
 * @author allen 
 *
 * @date 2014-4-18 上午11:47:36 
 *
 * @version V1.0   
 */
public interface BizCallBackGetUserProperty {
	public void getUserProperty(int retCode, UserPropertyInfo userPropertyInfo);
}
