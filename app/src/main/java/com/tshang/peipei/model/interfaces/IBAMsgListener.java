package com.tshang.peipei.model.interfaces;

/**
 * 
 * 
 * @category 消息回调接口
 * 
 */
public interface IBAMsgListener {

	enum HandleMsgType {
		CURR_FRIEND, NO_CURR_FRIEND;
	}

	/**
	 * @param info
	 * @return 0-当前聊天窗口，2
	 */
	HandleMsgType handleMsg(int uid, String nick);

	/**
	 * 刷新状态
	 * @param post
	 * @param currIndex
	 */
	// void handleMsg(int post,int currIndex);
	void handleMsg(int post, int currIndex);

	/**
	 * 阅后即焚删除
	 */
	void deleteMsg();

}
