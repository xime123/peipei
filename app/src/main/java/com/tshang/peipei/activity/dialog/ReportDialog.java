package com.tshang.peipei.activity.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.biz.space.SpaceCustomBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackTipoffUser;

public class ReportDialog extends Dialog implements android.view.View.OnClickListener, BizCallBackTipoffUser, OnDismissListener {

	private Activity context;
	private int otherUid = -1;
	private boolean isClose;
	private int type = 0;//0：用户，1:动态
	private TextView titleTV;

	public ReportDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public ReportDialog(Activity context, int theme, int otherUid, int type, boolean isClose) {
		super(context, theme);
		this.context = context;
		this.otherUid = otherUid;
		this.isClose = isClose;
		this.type = type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.popupwindow_report);
		findViewById(R.id.report_abuse).setOnClickListener(this);
		findViewById(R.id.report_sex).setOnClickListener(this);
		findViewById(R.id.report_ad).setOnClickListener(this);
		findViewById(R.id.report_political).setOnClickListener(this);
		findViewById(R.id.report_other).setOnClickListener(this);
		findViewById(R.id.report_cancel).setOnClickListener(this);
		titleTV = (TextView) findViewById(R.id.report_title);
		if (type == 1) {
			titleTV.setText("该动态涉及以下内容违规:");
		}
		if (isClose)
			this.setOnDismissListener(this);
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
		case R.id.report_abuse:
			toTipoff(0);
			break;
		case R.id.report_sex:
			toTipoff(1);
			break;
		case R.id.report_ad:
			toTipoff(2);
			break;
		case R.id.report_political:
			toTipoff(3);
			break;
		case R.id.report_other:
			toTipoff(4);
			break;
		case R.id.report_cancel:
			break;

		default:
			break;
		}
	}

	private void toTipoff(int reasonid) {
		new SpaceCustomBiz().tipoffUser(context, otherUid, reasonid, this);
	}

	@Override
	public void tipoffUserCallBack(int retCode) {
		mHandler.sendMessage(mHandler.obtainMessage(retCode));
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			BaseUtils.cancelDialog();
			if (msg.what == 0) {
				BaseUtils.showTost(context, "举报成功");
			} else {
				BaseUtils.showTost(context, "举报失败");
			}
		}

	};

	@Override
	public void onDismiss(DialogInterface dialog) {
		((Activity) context).finish();
	}

}
