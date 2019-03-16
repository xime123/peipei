package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.RspGetLatestAppInfo;

/**
 * @Title: BizCallBackGetLastestAppInfo.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014-4-29 下午12:00:03 
 *
 * @version V1.0   
 */
public interface BizCallBackGetLastestAppInfo {
	public void checkLatestAppInfo(int retCode, RspGetLatestAppInfo rsp);
}
