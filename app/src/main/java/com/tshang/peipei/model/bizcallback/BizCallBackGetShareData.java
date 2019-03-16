package com.tshang.peipei.model.bizcallback;

/**
 * @Title: BizCallBackGetShareData.java 
 *
 * @Description: 获取分享url接口 
 *
 * @author allen
 *
 * @date 2014-6-21 下午2:04:41 
 *
 * @version V1.0   
 */
public interface BizCallBackGetShareData {
	public void getShareUrl(int retCode, int type, String url);
}
