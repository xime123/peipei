package com.tshang.peipei.base.babase;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 * Handler 基础类,防止HANDLER引用ACTIITY,造成内存溢出
 * @author 
 *
 */
public class BAHandler extends Handler {

	WeakReference<Activity> mActivityReference;

	public BAHandler(Activity activity) {
		mActivityReference = new WeakReference<Activity>(activity);
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (mActivityReference.get() == null) {
			throw new NullPointerException();
		}
	}

}