package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/*
 *类        名 : BizCallBackUserIsExist.java
 *功能描述 : 
 *作　    者 : vactor
 *设计日期 : 2014-3-28 上午11:16:29
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public interface BizCallBackUserIsExist {

	public void isUserExist(int retCode, GoGirlUserInfo info);

}
