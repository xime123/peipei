package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfoList;

/**
 * @Title: BizCallBackGetFansList.java 
 *
 * @Description: 获取粉丝列表回调 
 *
 * @author allen  
 *
 * @date 2014-5-21 上午9:58:07 
 *
 * @version V1.0   
 */
public interface BizCallBackGetFansList {
	public void getFansList(int retCode, RetFollowInfoList list, int isend);
}
