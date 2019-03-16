package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;

/**
 * @Title: BizCallBackShowGiftToday.java 
 *
 * @Description: 今天收到的礼物接口 
 *
 * @author allen  
 *
 * @date 2014-4-12 下午2:39:47 
 *
 * @version V1.0   
 */
public interface BizCallBackShowGiftToday {

	public void showGiftToday(int retcode, int total, int mCurrPage, GiftDealInfoList giftInfoList);
}
