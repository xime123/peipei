package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/*
 *类        名 : BizCallBackUserRegister.java
 *功能描述 : 
 *作　    者 : vactor
 *设计日期 : 2014-3-28 下午2:07:00
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public interface BizCallBackUserRegister {

	public void userRegister(int retCode, String errorMsg,GoGirlUserInfo userInfo);
}
