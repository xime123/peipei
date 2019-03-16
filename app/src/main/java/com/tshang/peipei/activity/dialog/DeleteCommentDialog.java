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
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestDelComment;
import com.tshang.peipei.model.request.RequestDelComment.IDelCommentCallBack;

/**
 * 删除一级回复dialog
 * @author Jeff
 *
 */
public class DeleteCommentDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener, IDelCommentCallBack {

	private Activity context;
	private int commentid;
	private int topicid;
	private int uid;
	private int topicuid;
	private BAHandler mHandler;

	public DeleteCommentDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public DeleteCommentDialog(Activity context, int theme, int commentid, int topicid, int uid, int topicuid, BAHandler mHandler) {
		super(context, theme);
		this.context = context;
		this.commentid = commentid;
		this.topicid = topicid;
		this.topicuid = topicuid;
		this.uid = uid;
		this.mHandler = mHandler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
		tvMsg.setText(R.string.dialog_msg1);

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
		wlps.dimAmount = 0.3f;
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
			RequestDelComment request = new RequestDelComment();
			BaseUtils.showDialog(context, context.getString(R.string.str_deleting));
			request.delReply(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, commentid, topicid, uid, topicuid, this);
			break;

		default:
			break;
		}

	}

	@Override
	public void delSkillCallBack(int retCode, String msg) {
		mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.SPACE_DELETE_REPLY_VALUE, retCode, retCode, msg));

	}

}
