package com.tshang.peipei.model.bizcallback;

/**
 * @Title: BizCallBackDownloadPicKey.java 
 *
 * @Description: 获取key
 *
 * @author vactor
 *
 * @date 2014-5-4 下午6:37:42 
 *
 * @version V1.0   
 */
public interface BizCallBackDownloadPicKey {

	public void getPicKeyCallBack(int retCode, int uid, String pic);
}
