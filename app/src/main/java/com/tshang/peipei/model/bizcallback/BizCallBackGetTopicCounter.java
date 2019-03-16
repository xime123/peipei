package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.TopicCounterInfo;

/**
 * @Title: BizCallBackAddTopic.java 
 *
 * @Description: 计数
 *
 * @author vactor
 *
 * @date 2014-4-22
 *
 * @version V1.0   
 */
public interface BizCallBackGetTopicCounter {
	public void getTopicCounterCallBack(int retCode, TopicCounterInfo info);
}
