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
import com.tshang.peipei.activity.chat.ChatActivity;

/**
 * 下单成功回来
 * @author Jeff
 *
 */
public class SkillGiftSuccessDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private int otherUid = -1;
	private String userName;
	private int type = 0;

	public SkillGiftSuccessDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public SkillGiftSuccessDialog(Activity context, int theme, int otherUid, String userName, int type) {
		super(context, theme);
		this.context = context;
		this.otherUid = otherUid;
		this.userName = userName;
		this.type = type;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
//		if (type == 0) {
			tvMsg.setText(R.string.str_gift_success_find_her_chat);
//		} else {
//			tvMsg.setText("订单已生成，您的礼物扣压在陪陪平台，对方确认后，礼物送出，如果对方未确认，您可手动去我的技能订单里把礼物要回！");
//		}

		Button btn = (Button) findViewById(R.id.ok_sure);
		btn.setText(R.string.str_find_her_chat);
		btn.setOnClickListener(this);
		Button btncancel = (Button) findViewById(R.id.ok_cancel);
		btncancel.setText(R.string.str_say_later_again);
		btncancel.setOnClickListener(this);
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
		case R.id.ok_sure:
			this.dismiss();
			ChatActivity.intentChatActivity(context, otherUid, userName, type, false, false, 0);
			break;

		default:
			break;
		}

	}
}
