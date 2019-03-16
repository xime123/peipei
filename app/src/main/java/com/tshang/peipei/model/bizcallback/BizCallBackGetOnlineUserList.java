package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;

/**
 * @Title: BizCallBackGetOnlineUserList.java 
 *
 * @Description: 最新在线 
 *
 * @author vactor
 *
 * @date 2014-5-13 下午1:41:17 
 *
 * @version V1.0   
 */
public interface BizCallBackGetOnlineUserList {

	public void getOnlineUserListCallBack(int retCode, int isEnd, UserAndSkillInfoList list);
}
