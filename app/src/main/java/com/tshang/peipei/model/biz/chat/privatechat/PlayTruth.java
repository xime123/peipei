package com.tshang.peipei.model.biz.chat.privatechat;

import android.os.Message;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestTruth;
import com.tshang.peipei.model.request.RequestTruth.ITruthCallBack;
import com.tshang.peipei.model.request.RequestTruthAnswer;
import com.tshang.peipei.model.request.RequestTruthAnswer.IAnswerCallBack;

/**
 * @Title: SendTruthBiz.java 
 *
 * @Description: 真心话处理逻辑
 *
 * @author Aaron  
 *
 * @date 2015-6-17 上午10:39:06 
 *
 * @version V1.0   
 */
public class PlayTruth implements ITruthCallBack, IAnswerCallBack {

	private BAHandler handler;

	private String answer;

	/**
	 * 
	 *发送大冒险请求
	 * @param touid 
	 * @param answerid 答案id
	 * @param truthid 真心话id
	 * @param handler
	 */
	public void sendTruth(int touid, int answerid, String truthid, BAHandler handler) {
		if (handler != null) {
			this.handler = handler;
		}
		if (BAApplication.mLocalUserInfo != null) {
			RequestTruth req = new RequestTruth();
			req.requestTruth(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), touid,
					answerid, truthid, this);
		}
	}

	/**
	 * 请求大冒险答案
	 * @author Aaron
	 *
	 * @param touid
	 * @param answerid
	 * @param truthid
	 * @param handler
	 */
	public void sendAnswer(int touid, int answerid, String truthid, String answer, BAHandler handler) {
		if (handler != null) {
			this.handler = handler;
		}
		this.answer = answer;
		if (BAApplication.mLocalUserInfo != null) {
			RequestTruthAnswer req = new RequestTruthAnswer();
			req.requestAnswer(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), touid,
					answerid, truthid, this);
		}
	}

	//大冒险发送成功
	@Override
	public void resultTruth(int code) {
		if (code != 0) {
			Message msg = handler.obtainMessage();
			msg.what = HandlerValue.CHAT_TRUTH_SEND_ERROR_RESULT_CODE;
			msg.arg1 = code;
			handler.sendMessage(msg);
		} else {
			Message msg = handler.obtainMessage();
			msg.what = HandlerValue.CHAT_TRUTH_SEND_SUCCESS_RESULT_CODE;
			handler.sendMessage(msg);
		}
	}

	//答案请求回调
	@Override
	public void resultAnswer(int code, String truthid) {
		if (code == 0) {
			Message msg = handler.obtainMessage();
			msg.what = HandlerValue.CHAT_TRUTH_ANSWER_SUCCESS_RESULT_CODE;
			msg.obj = truthid;
			msg.arg1 = 1;
			handler.sendMessage(msg);
		} else {
			Message msg = handler.obtainMessage();
			msg.what = HandlerValue.CHAT_TRUTH_ANSWER_ERROR_RESULT_CODE;
			msg.obj = truthid + "," + answer;
			msg.arg1 = code;
			handler.sendMessage(msg);
		}
	}
}
