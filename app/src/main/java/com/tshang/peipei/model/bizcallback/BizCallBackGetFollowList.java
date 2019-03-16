package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfoList;

/**
 * @Title: BizCallBackGetFollowList.java 
 *
 * @Description: 获取关注列表
 *
 * @author vactor
 *
 * @date 2014-5-4 上午11:09:27 
 *
 * @version V1.0   
 */
public interface BizCallBackGetFollowList {

	public void getFollowListCallBack(int retCode, RetFollowInfoList list,int isend);
}
