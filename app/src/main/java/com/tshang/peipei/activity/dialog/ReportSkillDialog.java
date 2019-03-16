package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestTipoffSkill;
import com.tshang.peipei.model.request.RequestTipoffSkill.ITipoffSkill;

public class ReportSkillDialog extends Dialog implements android.view.View.OnClickListener, ITipoffSkill {

	private Activity context;
	private int otherUid = -1;
	private int skillid;
	private BAHandler handler;

	public ReportSkillDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public ReportSkillDialog(Activity context, int theme, int otherUid, int skillid, BAHandler handler) {
		super(context, theme);
		this.context = context;
		this.otherUid = otherUid;
		this.skillid = skillid;
		this.handler = handler;

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
		RequestTipoffSkill req = new RequestTipoffSkill();
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		req.tipoffSkill(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), reasonid, "",
				skillid, otherUid, this);
	}

	@Override
	public void delSkillCallBack(int retCode, String msg) {
		handler.sendMessage(handler.obtainMessage(HandlerValue.SPACE_REPORT_SKILL_VALUE, retCode));

	}

}
