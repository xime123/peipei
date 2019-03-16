package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/*
 *类        名 : BizCallBackUserLogin.java
 *功能描述 : 用户登录
 *作　    者 : vactor
 *设计日期 :2014 2014-3-22 下午3:27:01
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public interface BizCallBackUserLogin {

	public void loginCallBack(int retcode, String msg, GoGirlUserInfo userInfo);
}
