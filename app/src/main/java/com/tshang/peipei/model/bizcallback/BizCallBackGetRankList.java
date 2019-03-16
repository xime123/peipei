package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;

/**
 * @Title: BizCallBackGetRankList.java 
 *
 * @Description: 排行 
 *
 * @author vactor
 *
 * @date 2014-4-24 下午1:53:37 
 *
 * @version V1.0   
 */
public interface BizCallBackGetRankList {

	public void getRankListCallBack(int retCode, int total, int end, int type, GoGirlUserInfoList giftInfoList);
}
