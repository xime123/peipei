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
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.storage.database.entity.SessionDatabaseEntity;

/**
 * 登录dialog
 * @author Jeff
 *
 */
public class DeleteMessageRecordDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private SessionDatabaseEntity entity;
	private RefreshMessageListener listener;

	public DeleteMessageRecordDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public DeleteMessageRecordDialog(Activity context, int theme, SessionDatabaseEntity entity, RefreshMessageListener listener) {
		super(context, theme);
		this.context = context;
		this.entity = entity;
		this.listener = listener;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
		tvMsg.setText("确认删除该会话？");

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
		case R.id.ok_sure://确定删除
			this.dismiss();
			if (entity != null) {
				ChatSessionManageBiz.removeChatSessionWithUserID(context, entity.getUserID(), entity.getType());
				if (listener != null) {
					listener.refreshMessage();
				}
			}

			break;
		default:
			break;
		}

	}

	public interface RefreshMessageListener {
		public void refreshMessage();
	};
}
