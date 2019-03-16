package com.tshang.peipei.model;

import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: ISentMessageCallBack.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-12-1 下午3:07:49 
 *
 * @version V1.0   
 */

public interface ISentMessageCallBack {
	public void sentMessageCallBack(byte[] auth, int retCode, ChatMessageReceiptEntity receipt, ChatDatabaseEntity chatDatabaseEntity);
}
