package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.model.biz.space.DeleteSkillBiz;
import com.tshang.peipei.model.request.RequestDelSkill.IDelSKill;

public class SkillDeleteDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private IDelSKill iDelSKill;
	private int skillid = 1;
	private int type = 0;

	public SkillDeleteDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public SkillDeleteDialog(Activity context, int theme, int skillid, int type, IDelSKill iDelSKill) {
		super(context, theme);
		this.context = context;
		this.iDelSKill = iDelSKill;
		this.skillid = skillid;
		this.type = type;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
		if (type == 0) {
			tvMsg.setText(R.string.str_skill_delete_mind);
		} else if (type == 1) {
			tvMsg.setText(R.string.str_male_skill_detele_mind);
		}

		findViewById(R.id.ok_sure).setOnClickListener(this);
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

	public interface IGiftInfoNumCallBack {
		public void getGiftInfoNumCallBack(int value);
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
			DeleteSkillBiz deleteBiz = new DeleteSkillBiz(context);
			deleteBiz.deleteSkillBiz(skillid, iDelSKill);
			break;

		default:
			break;
		}

	}
}
