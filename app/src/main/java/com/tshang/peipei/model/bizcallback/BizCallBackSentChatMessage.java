package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;

/**
 * @Title: 发送聊天信息接口 
 *
 * @Description: 发送聊天信息接口 
 *
 * @author allen 
 *
 * @date 2014-4-10 下午1:46:10 
 *
 * @version V1.0   
 */
public interface BizCallBackSentChatMessage {
	public void getSentChatMessageCallBack(int retcode, ChatMessageReceiptEntity recepit);
}
