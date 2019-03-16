package com.tshang.peipei.activity.dialog;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.request.RequestAddBlacklist.IAddBlacklist;

/**
 * @Title: AddBlackListDialog.java 
 *
 * @Description: 加入黑名单
 *
 * @author allen  
 *
 * @date 2014-8-11 下午5:30:29 
 *
 * @version V1.0   
 */
public class AddBlackListDialog extends BaseNormalDialog implements IAddBlacklist {

	private int mFriendUid;
	private BAHandler mHandler;

	public AddBlackListDialog(Activity context, int title, int sure, int cancel, int uid, BAHandler mHandler) {
		super(context, title, sure, cancel);
		mFriendUid = uid;
		this.mHandler = mHandler;
	}

	@Override
	public void OnclickSure() {
		ChatRecordBiz cBiz = new ChatRecordBiz();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(context);
		if (userInfo != null) {
			cBiz.addBlack(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), mFriendUid, this);
		}
	}

	@Override
	public void addBlackList(int retCode) {
		// 拉黑提示
		ChatSessionManageBiz.removeChatSessionWithUserID(context, mFriendUid, 0);
		mHandler.sendEmptyMessage(HandlerValue.CHAT_REPORT_VALUE);
	}

}
