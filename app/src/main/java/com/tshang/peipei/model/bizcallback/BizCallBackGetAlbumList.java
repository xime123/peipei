package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.AlbumInfoList;

/*
 *类        名 : IGetAlbumList.java
 *功能描述 : 
 *作　    者 : vactor
 *设计日期 : 2014-3-26 上午10:33:08
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public interface BizCallBackGetAlbumList {

	public void getAlbumListCallBack(int retcode, AlbumInfoList albumList);

}