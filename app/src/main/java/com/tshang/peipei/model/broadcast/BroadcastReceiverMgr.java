package com.tshang.peipei.model.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;

/**
 * @Title: BroadcastReceiverMgr.java 
 *
 * @Description: 录制语音的时候来电话了
 *
 * @author Jeff  
 *
 * @date 2014年8月12日 下午8:09:26 
 *
 * @version V1.0   
 */
public class BroadcastReceiverMgr extends BroadcastReceiver {
	protected BAHandler handler;

	public BroadcastReceiverMgr(BAHandler handler) {
		this.handler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		//		Log.i(TAG, "[Broadcast]" + action);

		//呼入电话  
		if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
			doReceivePhone(context, intent);
		}
	}

	/** 
	 * 处理电话广播. 
	 * @param context 
	 * @param intent 
	 */
	public void doReceivePhone(Context context, Intent intent) {
//		String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int state = telephony.getCallState();
		switch (state) {
		case TelephonyManager.CALL_STATE_RINGING:
			//			Log.i(TAG, "[Broadcast]等待接电话=" + phoneNumber);
			handler.sendEmptyMessage(HandlerValue.BROADCAST_VOICE_CALL_STATE_RINGING_VALUE);
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			//			Log.i(TAG, "[Broadcast]电话挂断=" + phoneNumber);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			//			Log.i(TAG, "[Broadcast]通话中=" + phoneNumber);
			break;
		}
	}
}