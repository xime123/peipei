package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.harem.JoinHarem;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * 登录dialog
 * @author Jeff
 *
 */
public class KickedHaremMemberDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private BAHandler handler;
	private int groupid;
	private int kickedId;
	private String memberName;
	private boolean isRequestQuit;

	public KickedHaremMemberDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public KickedHaremMemberDialog(Activity context, int theme, BAHandler handler, int groupid, String memberName, int kickedId, boolean isRequestQuit) {
		super(context, theme);
		this.context = context;
		this.handler = handler;
		this.groupid = groupid;
		this.memberName = memberName;
		this.kickedId = kickedId;
		this.isRequestQuit = isRequestQuit;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
		if (isRequestQuit) {
			tvMsg.setText("确定要退出此宫?");
		} else {
			String title = context.getString(R.string.str_kicked_member);

			String alias = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(kickedId);
			String sendUserName = TextUtils.isEmpty(alias) ? memberName : alias;

			tvMsg.setText(title.replace("$", "\"" + sendUserName + "\""));
		}

		Button btn = (Button) findViewById(R.id.ok_sure);
		btn.setText(R.string.ok);
		btn.setOnClickListener(this);
		findViewById(R.id.ok_cancel).setOnClickListener(this);
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
		case R.id.ok_sure://踢出某人
			this.dismiss();
			JoinHarem.getInstance().reqQuitGroup(context, kickedId, groupid, handler, isRequestQuit);
			break;
		default:
			break;
		}

	}
}
