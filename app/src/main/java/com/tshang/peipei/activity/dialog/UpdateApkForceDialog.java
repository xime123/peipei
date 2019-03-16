package com.tshang.peipei.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseDownLoadApk;
import com.tshang.peipei.base.BaseUtils;

/**
 * @Title: UpdateApkForceDialog.java 
 *
 * @Description: 强制升级 
 *
 * @author allen  
 *
 * @date 2014-7-17 上午11:46:08 
 *
 * @version V1.0   
 */
public class UpdateApkForceDialog extends Dialog implements OnClickListener {

	private TextView contentTv;
	private Button button;

	private String url;
	private Context context;
	private String title;

	public UpdateApkForceDialog(Context context, String url, String title) {
		super(context, R.style.dialogStyle);
		this.url = url;
		this.context = context;
		this.title = title;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_force_upgrad_view);
		button = (Button) findViewById(R.id.btn_grade_ok);
		button.setOnClickListener(this);
		contentTv = (TextView) findViewById(R.id.download_title);
		if (!TextUtils.isEmpty(title)) {
			contentTv.setText(title);
		}
		button.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, context.getResources().getColor(R.color.red), 8, context.getResources()
				.getColor(R.color.red)));
		button.setText("下载更新");
		button.setTextColor(Color.WHITE);

//		final Window w = getWindow();
//		final WindowManager.LayoutParams wlps = w.getAttributes();
//		// wlps.width = WindowManager.LayoutParams.WRAP_CONTENT;
//		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
//		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
//		wlps.dimAmount = 0.6f;
//		wlps.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
//		wlps.gravity = Gravity.CENTER;
//		w.setAttributes(wlps);
//		w.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//		// 添加物理返回键对话框不消失
//		OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//					return true;
//				} else {
//					return false;
//				}
//			}
//		};
////		setOnKeyListener(keylistener);
////		// 点击对话框外不消失
////		setCanceledOnTouchOutside(false);
//		w.setLayout(context.getResources().getDisplayMetrics().widthPixels * 3 / 4, WindowManager.LayoutParams.WRAP_CONTENT);
		
		
		final Window w = getWindow();
		final WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		setCanceledOnTouchOutside(false);
		setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					return true;
				} else {
					return false;
				}
			}
		});
		w.setAttributes(wlps);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		new BaseDownLoadApk(context, url, true).start();
	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
