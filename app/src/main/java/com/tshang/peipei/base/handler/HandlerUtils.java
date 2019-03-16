package com.tshang.peipei.base.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @Title: HandlerUtils.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) handler发送消息的工具类
 *
 * @author Jeff  
 *
 * @date 2014年10月13日 下午2:39:32 
 *
 * @version V1.3.0   
 */
public class HandlerUtils {

	public static void sendHandlerMessage(Handler handler, int what, Object obj) {
		if (handler == null) {
			return;
		}
		handler.sendMessage(handler.obtainMessage(what, obj));
	}

	public static void sendHandlerMessage(Handler handler, int what) {

		if (handler == null) {
			return;
		}
		handler.sendEmptyMessage(what);
	}

	public static void sendHandlerMessage(Handler handler, int what, int arg1, int arg2) {
		if (handler == null) {
			return;
		}
		handler.sendMessage(handler.obtainMessage(what, arg1, arg2));
	}

	public static void sendHandlerMessage(Handler handler, int what, int arg1, int arg2, Object obj) {
		if (handler == null) {
			return;
		}
		handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
	}

	public static void sendHandlerMessageDelayTime(Handler handler, int what, int arg1, int arg2, Object obj, long time) {
		if (handler == null) {
			return;
		}
		handler.sendMessageDelayed(handler.obtainMessage(what, arg1, arg2, obj), time);
	}
	
	public static void sendHandlerMessage(Handler handler, int what, int arg1, Object obj, String retMsg) {
		if (handler == null) {
			return;
		}
		Bundle bundle = new Bundle();
		bundle.putString("data", retMsg);
		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.arg1 = arg1;
		msg.obj = obj;
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
}
