package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.CommentInfoList;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;

/*
 *类        名 : BizCallBackUserRegister.java
 *功能描述 : 获取动态详情
 *作　    者 : vactor
 *设计日期 : 2014-4-19 
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public interface BizCallBackGetTopicDetail {

	public void getTopicDetailCallBack(int retCode, TopicInfo topicInfo, CommentInfoList commentList, int commentTotal);
}
