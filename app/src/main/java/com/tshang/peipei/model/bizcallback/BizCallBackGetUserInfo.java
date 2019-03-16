package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: BizCallBackGetUserInfo.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author aaa  
 *
 * @date 2014-4-29 下午4:02:58 
 *
 * @version V1.0   
 */
public interface BizCallBackGetUserInfo {

	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo);
}
