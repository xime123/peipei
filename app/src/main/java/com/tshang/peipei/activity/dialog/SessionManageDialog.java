package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;

public class SessionManageDialog extends Dialog implements android.view.View.OnClickListener {

	private Activity context;
	private BAHandler mHandler;

	public SessionManageDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public SessionManageDialog(Activity context, BAHandler mHandler) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.mHandler = mHandler;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.popupwindow_session_manage);
		findViewById(R.id.session_chat_limit).setOnClickListener(this);
		findViewById(R.id.session_my_black).setOnClickListener(this);
		findViewById(R.id.session_cancel).setOnClickListener(this);
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
		final Window w = getWindow();
		w.setWindowAnimations(R.style.anim_popwindow_bottombar); // 设置窗口弹出动画
		final WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = WindowManager.LayoutParams.MATCH_PARENT;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.gravity = Gravity.BOTTOM;
		w.setAttributes(wlps);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
		switch (v.getId()) {
		case R.id.session_chat_limit:
			mHandler.sendEmptyMessage(HandlerValue.SESSION_CHAT_LIMET);
			break;
		case R.id.session_my_black:
			mHandler.sendEmptyMessage(HandlerValue.SESSION_CHAT_BLACK);
			break;
		case R.id.session_cancel:
			break;

		default:
			break;
		}
	}

}
