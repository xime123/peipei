package com.tshang.peipei.model.biz;

import com.tshang.peipei.model.bizcallback.BizCallBackDownloadPicKey;
import com.tshang.peipei.model.request.RequestDownloadHeadPic;
import com.tshang.peipei.model.request.RequestDownloadHeadPic.IGetPicKey;

/**
 * @Title: PeiPeiDownloadPicKey.java 
 *
 * @Description: 获取 pickey
 *
 * @author vactor
 *
 * @date 2014-5-4 下午6:39:03 
 *
 * @version V1.0   
 */
public class PeiPeiDownloadPicKey implements IGetPicKey {

	private BizCallBackDownloadPicKey bizCallBackDownloadPicKey;

	//通过 UID 获取 PICKEY
	public void getPicKey(byte[] auth, int ver, int uid, int height, int width, BizCallBackDownloadPicKey callBack) {
		RequestDownloadHeadPic req = new RequestDownloadHeadPic();
		bizCallBackDownloadPicKey = callBack;
		req.getPicKey(auth, ver, uid, height, width, this);
	}

	@Override
	public void getPicKeyCallBack(int retCode, int uid, String pic) {
		if (null != bizCallBackDownloadPicKey) {
			bizCallBackDownloadPicKey.getPicKeyCallBack(retCode, uid, pic);
		}
	}

}
