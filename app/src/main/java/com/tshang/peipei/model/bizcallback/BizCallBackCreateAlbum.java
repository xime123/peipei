package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;

/*
 *类        名 : BizCallBackCreateAlbum.java
 *功能描述 : 创建相册回调
 *作　    者 : vactor
 *设计日期 :2014 2014-3-25 上午11:22:57
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public interface BizCallBackCreateAlbum {

	public void createAlbumCallBack(int retCode, AlbumInfo album);
}
