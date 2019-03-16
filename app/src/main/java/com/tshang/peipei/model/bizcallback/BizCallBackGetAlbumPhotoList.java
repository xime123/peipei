package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.PhotoInfoList;

/**
 * @Title: BizCallBackUploadPhotos.java 
 *
 * @Description: 加载相册图片回调 
 *
 * @author vactor
 *
 * @date 2014-4-3 下午2:51:52 
 *
 * @version V1.0   
 */
public interface BizCallBackGetAlbumPhotoList {

	public void getAlbumPhotoList(int retCode,int total, PhotoInfoList list);
}
