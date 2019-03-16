package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.TopicInfoList;

/**
 * @Title: BizCallBackSpaceGetUserTopicList.java 
 *
 * @Description: 用户动态界面回调
 *
 * @author vactor
 *
 * @date 2014-4-12 下午5:44:37 
 *
 * @version V1.0   
 */
public interface BizCallBackSpaceGetUserTopicList {

	public void getUserTopicListCallBack(int retCode, int isEnd, int total, int code, TopicInfoList list);
}
