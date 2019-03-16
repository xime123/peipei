package com.tshang.peipei.model.biz.chat.groupchat;

import android.os.Message;
import android.text.TextUtils;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestPlayDare;
import com.tshang.peipei.model.request.RequestPlayDare.IPlayDare;

/**
 * @Title: PlayDareBiz.java 
 *
 * @Description 大冒险逻辑
 *
 * @author allen  
 *
 * @date 2015-6-16 上午9:37:51 
 *
 * @version V1.0   
 */
public class PlayDareBiz implements IPlayDare {

	private BAHandler mHandler;

	public void PlayDare(int groupid, String dareid, BAHandler mHandler) {
		this.mHandler = mHandler;
		if (System.currentTimeMillis() - BAApplication.dareClickTime < 1500) {
			return;
		}
		BAApplication.dareClickTime = System.currentTimeMillis();
		if (BAApplication.mLocalUserInfo != null) {
			RequestPlayDare req = new RequestPlayDare();
			req.playdare(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), groupid,
					dareid, this);
		}
	}

	@Override
	public void resultPlayDare(int retCode, String dareid) {
		if (retCode == 0) {
			if (!TextUtils.isEmpty(dareid)) {
				Message msg = mHandler.obtainMessage();
				msg.what = HandlerValue.CHAT_DARE_SEND_RESULT;
				msg.obj = dareid;
				mHandler.sendMessage(msg);
			}
		} else {
			Message msg = mHandler.obtainMessage();
			msg.what = HandlerValue.CHAT_DARE_SEND_RESULT_CODE;
			msg.arg1 = retCode;
			msg.obj = dareid;
			mHandler.sendMessage(msg);
		}
	}
}
