package com.tshang.peipei.activity.dialog;

import android.app.Activity;

import com.tshang.peipei.activity.chat.ChatActivity;

/**
 * @Title: SentGiftResultDialog.java 
 *
 * @Description: 送礼成功后的对话框 
 *
 * @author allen  
 *
 * @date 2014-7-17 上午11:31:45 
 *
 * @version V1.0   
 */
public class SentGiftResultDialog extends BaseNormalDialog {

	private int mFriendUid, mSex;
	private String mFriendNick;

	public SentGiftResultDialog(Activity context, int title, int sure, int cancel, int mFriendUid, int mSex, String mFriendNick) {
		super(context, title, sure, cancel);
		this.mFriendNick = mFriendNick;
		this.mFriendUid = mFriendUid;
		this.mSex = mSex;
	}

	@Override
	public void OnclickSure() {
		ChatActivity.intentChatActivity(context, mFriendUid, mFriendNick, mSex, false, false, 0);
	}

}
