package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlDaybookInfoList;

/**
 * @Title: BizCallBackDaybook.java 
 *
 * @Description: 获取记录列表回调
 *
 * @author allen  
 *
 * @date 2014-4-24 上午11:54:28 
 *
 * @version V1.0   
 */
public interface BizCallBackDaybook {
	public void getDaybook(int retCode, GoGirlDaybookInfoList list, int total);
}
