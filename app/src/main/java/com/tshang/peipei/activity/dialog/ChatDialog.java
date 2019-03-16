package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.store.StoreGiftListActivity;
import com.tshang.peipei.base.BaseUtils;

/**
 * 私聊魅力贡献值不够弹出dialog
 * @author Jeff
 *
 */
public class ChatDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private String msg;
	private int otherId;
	private String name;

	public ChatDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public ChatDialog(Activity context, int theme, String msg, int otherId, String name) {
		super(context, theme);
		this.context = context;
		this.msg = msg;
		this.otherId = otherId;
		this.name = name;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
		tvMsg.setText(msg);

		Button btn = (Button) findViewById(R.id.ok_sure);
		btn.setText(R.string.str_send_gifts);
		btn.setOnClickListener(this);
		Button btnLeft = (Button) findViewById(R.id.ok_cancel);
		btnLeft.setText(R.string.str_i_know);
		btnLeft.setOnClickListener(this);
		//		showDialog();
	}

	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window window = getWindow(); // 得到对话框
		final WindowManager.LayoutParams wlps = window.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_cancel:
			this.dismiss();
			break;
		case R.id.ok_sure://去登录
			this.dismiss();
			Bundle b = new Bundle();
			b.putInt("fuid", otherId);
			b.putString("fNick", name);
			BaseUtils.openActivity(context, StoreGiftListActivity.class, b);
			break;

		default:
			break;
		}

	}
}
