package com.tshang.peipei.model.bizcallback;

/**
 * @Title: BizCallBackGetExchangeUrl.java 
 *
 * @Description: 获取积分商城URL回调
 *
 * @author allen  
 *
 * @date 2014-5-20 下午5:19:17 
 *
 * @version V1.0   
 */
public interface BizCallBackGetExchangeUrl {
	public void getUrl(int retCode, String url);
}
