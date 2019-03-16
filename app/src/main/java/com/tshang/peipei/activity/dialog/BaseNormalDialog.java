package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tshang.peipei.R;

/**
 * @Title: UpdateApkDialog.java 
 *
 * @Description: 2个按键+1个提示的对话框基类
 *
 * @author allen  
 *
 * @date 2014-7-17 上午10:25:44 
 *
 * @version V1.0   
 */
public abstract class BaseNormalDialog extends Dialog implements OnClickListener {

	public Activity context;
	private int title;
	private int sure;
	private int cancel;
	private String titleStr;

	private BaseNormalDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public BaseNormalDialog(Activity context, int theme, int title, int sure, int cancel) {
		super(context, theme);
		this.context = context;
		this.title = title;
		this.sure = sure;
		this.cancel = cancel;
	}

	public BaseNormalDialog(Activity context, int title, int sure, int cancel) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.title = title;
		this.sure = sure;
		this.cancel = cancel;
	}

	public BaseNormalDialog(Activity context, int title, int sure, int cancel, String titleString) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.title = title;
		this.sure = sure;
		this.cancel = cancel;
		titleStr = titleString;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.dialog_view);
			TextView tvMsg = (TextView) findViewById(R.id.msg);
			if (TextUtils.isEmpty(titleStr)) {
				tvMsg.setText(title);
			} else {
				tvMsg.setText(titleStr);
			}
			Button btn_Ok = (Button) findViewById(R.id.ok_sure);
			Button btn_Can = (Button) findViewById(R.id.ok_cancel);
			if (sure != 0) {
				btn_Ok.setText(sure);
			}
			if (cancel != 0) {
				btn_Can.setText(cancel);
			}

			btn_Ok.setOnClickListener(this);
			btn_Can.setOnClickListener(this);
			setCanceledOnTouchOutside(true);

			final Window w = getWindow();
			final WindowManager.LayoutParams wlps = w.getAttributes();
			wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
			wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
			wlps.dimAmount = 0.6f;
			wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
			wlps.gravity = Gravity.CENTER;
			w.setAttributes(wlps);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_cancel:
			dismiss();
			break;
		case R.id.ok_sure:
			dismiss();
			OnclickSure();
			break;
		default:
			break;
		}

	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void OnclickSure();
}
