package com.tshang.peipei.view.showpic;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;

@SuppressLint("NewApi")
public class AdapterSDK {

	private static final int SIXTY_FPS_INTERVAL = 1000 / 60;

	/**
	 * view 执行动画效果
	 * 
	 * @param view
	 * @param runnable
	 */
	public static void postOnAnimation(View view, Runnable runnable) {
		if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			postOn(view, runnable);
		} else {
			view.postDelayed(runnable, SIXTY_FPS_INTERVAL);
		}
	}

	private static void postOn(View view, Runnable r) {
		view.postOnAnimation(r);
	}

}
