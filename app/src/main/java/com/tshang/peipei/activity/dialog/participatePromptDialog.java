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
import com.tshang.peipei.activity.mine.MineMissionsActivity;
import com.tshang.peipei.activity.store.StoreH5RechargeActivity;
import com.tshang.peipei.base.BaseUtils;

/**
 * 去充值
 * @author Jeff
 *
 */
public class participatePromptDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private boolean isGoldValue = false;
	private int goldevalue = 0;
	private int silvervalue = 0;

	public participatePromptDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public participatePromptDialog(Activity context, int theme, boolean isGoldValue, int goldevalue, int silvervalue) {
		super(context, theme);
		this.context = context;
		this.isGoldValue = isGoldValue;
		this.goldevalue = goldevalue;
		this.silvervalue = silvervalue;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
		Button btn = (Button) findViewById(R.id.ok_sure);
		if (isGoldValue) {
			tvMsg.setText(R.string.str_gold_is_not_enough);
			btn.setText(R.string.recharge);
		} else {
			tvMsg.setText(R.string.str_silver_is_not_enough);
			btn.setText(R.string.str_complete);
		}

		btn.setOnClickListener(this);
		findViewById(R.id.ok_cancel).setVisibility(View.GONE);
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
		case R.id.ok_sure:
			this.dismiss();
			if (isGoldValue) {
				BaseUtils.openActivity(context, StoreH5RechargeActivity.class);
			} else {
				BaseUtils.openActivity(context, MineMissionsActivity.class);
			}
			break;

		default:
			break;
		}

	}
}
