package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;

/**
 * @Title: BizCallBackAddTopic.java 
 *
 * @Description: 写贴第一步骤回调,如果贴子只有一张图片,那么也是终极回调 
 *
 * @author vactor
 *
 * @date 2014-4-10 下午8:11:08 
 *
 * @version V1.0   
 */
public interface BizCallBackAddTopic {
	public void addTopicCallBack(int retCode, int userTopicId, int charmnum, GoGirlDataInfoList list);
}
