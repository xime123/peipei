package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.request.RequestUpdateAlias.iUpdateAlias;

/**
 * @Title: RemarkDialog.java 
 *
 * @Description: 设置备注对话框
 *
 * @author allen  
 *
 * @date 2014-11-28 下午4:11:59 
 *
 * @version V1.0   
 */
public class RemarkDialog extends Dialog implements android.view.View.OnClickListener, iUpdateAlias {

	private int friendUid;
	private int sure;
	private int cancel;
	private String name;
	private Activity context;
	private BAHandler mHandler;
	private int type;
	private String hint;

	private EditText etMsg;

	public RemarkDialog(Activity context, int sure, int cancel, int uid, int type, String name, String hint, BAHandler mHandler) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);

		friendUid = uid;
		this.sure = sure;
		this.cancel = cancel;
		this.name = name;
		this.type = type;
		this.context = context;
		this.hint = hint;
		this.mHandler = mHandler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.dialog_remark);
			etMsg = (EditText) findViewById(R.id.edittext_remark);
			etMsg.setText(name);
			etMsg.setHint(hint);

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
			wlps.y = -70;
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
			OnclickSure();
			break;
		default:
			break;
		}

	}

	private void OnclickSure() {
		String remark = etMsg.getText().toString().trim();

		if (!TextUtils.isEmpty(remark) && remark.length() > 8) {
			BaseUtils.showTost(context, R.string.toast_remark_long);
			dismiss();
			return;
		}

		UserAccountBiz uBiz = new UserAccountBiz(context);
		if (BAApplication.mLocalUserInfo != null) {
			uBiz.updateAlias(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
					friendUid, type, remark, this);
		}
		dismiss();
	}

	@Override
	public void resultAlias(int retCode, int followuid, String alias) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.ALIAS_UPDATE_RESULT;
		msg.arg1 = retCode;
		msg.arg2 = followuid;
		msg.obj = alias;

		mHandler.sendMessage(msg);
	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
