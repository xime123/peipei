package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.os.Message;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestDareBegPass;
import com.tshang.peipei.model.request.RequestDareBegPass.IDareBegPass;

/**
 * @Title: DarePassDialog.java 
 *
 * @Description: 大冒险求通过
 *
 * @author allen  
 *
 * @date 2015-7-21 下午5:30:29 
 *
 * @version V1.0   
 */
public class DarePassDialog extends BaseNormalDialog implements IDareBegPass {

	private int mFriendUid;
	private BAHandler mHandler;
	private String gameid;

	public DarePassDialog(Activity context, int title, int sure, int cancel, int uid, String gameid, BAHandler mHandler) {
		super(context, title, sure, cancel);
		mFriendUid = uid;
		this.mHandler = mHandler;
		this.gameid = gameid;
	}

	@Override
	public void OnclickSure() {
		if (BAApplication.mLocalUserInfo != null) {
			RequestDareBegPass req = new RequestDareBegPass();
			req.dareBegPass(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
					mFriendUid, gameid, this);
		}
	}

	@Override
	public void resultDareBegPass(int retCode) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.CHTA_DARE_PASS_RESULT_CODE;
		msg.arg1 = retCode;
		mHandler.sendMessage(msg);
	}

}
