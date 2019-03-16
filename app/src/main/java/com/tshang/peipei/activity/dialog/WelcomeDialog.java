package com.tshang.peipei.activity.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.tshang.peipei.R;

public class WelcomeDialog extends Dialog {

	private Activity context;
	private Window window = null;

	public WelcomeDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public WelcomeDialog(Activity context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_welcome);
		//		initAibei();
		//		WindowManager.LayoutParams params = getWindow().getAttributes();
		//		params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//		getWindow().setAttributes(params);
		//		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

		mHandler.sendEmptyMessageDelayed(0x01, 3000);
	}

	private void initAibei() {
		//		SDKApi.init(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, BAConstants.APP_ID);
	}

	public void showDialog(int x, int y) {
		try {
			windowDeploy(x, y);

			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy(int x, int y) {
		window = getWindow(); // 得到对话框
		window.setWindowAnimations(R.style.anim_popwindow_alpha); // 设置窗口弹出动画
		WindowManager.LayoutParams wl = window.getAttributes();
		// 根据x，y坐标设置窗口需要显示的位置
		wl.x = x; // x小于0左移，大于0右移
		wl.y = y; // y小于0上移，大于0下移
		// wl.alpha = 0.6f; //设置透明度
		wl.gravity = Gravity.BOTTOM; // 设置重力
		window.setAttributes(wl);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(params);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			WelcomeDialog.this.dismiss();
		}

	};

}
