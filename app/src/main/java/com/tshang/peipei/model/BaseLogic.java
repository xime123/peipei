package com.tshang.peipei.model;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 * @Title: BaseLogic.java 
 *
 * @Description: 逻辑层根类
 *
 * @author Jeff
 *
 * @date 2014年7月25日 上午10:27:25 
 *
 * @version V1.0   
 */
public class BaseLogic {
	protected Activity activity;
	protected Handler handler;

	public Activity getActivity() {
		return activity;
	}

	public Handler getHandler() {
		return handler;
	}

	public BaseLogic(Activity activity, Handler handler) {
		this.activity = activity;
		this.handler = handler;
	}

	/**
	 * 使用HANDLER 发送消息
	 * @param handler 
	 * @param what
	 * @param obj
	 */
	public void sendHandlerMessage(Object obj) {
		handler.sendMessage(handler.obtainMessage(0, obj));
	}

	/**
	 * 使用HANDLER 发送消息
	 * @param handler 
	 * @param what
	 * @param obj
	 */
	public void sendHandlerMessage(int what, Object obj) {
		if (null == handler) {
			return;
		}
		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	/**
	 * 使用HANDLER 发送消息
	 * @param handler
	 * @param what
	 * @param arg1
	 * @param obj
	 */
	public void sendHandlerMessage(int what, int arg1, Object obj) {
		if (handler == null) {
			return;
		}
		handler.sendMessage(handler.obtainMessage(what, arg1, arg1, obj));
	}

	/**
	 * 使用HANDLER 发送消息
	 * @param handler
	 * @param what
	 * @param arg1
	 */
	public void sendHandlerMessage(int what, int arg1) {
		if (handler == null) {
			return;
		}
		handler.sendMessage(handler.obtainMessage(what, arg1, arg1));
	}

	/**
	 * 使用HANDLER 发送消息
	 * @param handler
	 * @param what
	 * @param arg1
	 */
	public void sendHandlerMessage(int what, int arg1, int arg2) {
		if (handler == null) {
			return;
		}
		handler.sendMessage(handler.obtainMessage(what, arg1, arg2));
	}

	/**
	 * 使用HANDLER 发送消息
	 * @param handler
	 * @param what
	 * @param arg1
	 */
	public void sendHandlerMessage(int what, int arg1, int arg2, Object obj) {
		if (handler == null) {
			return;
		}
		handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
	}
}
