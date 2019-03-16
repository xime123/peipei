package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;

import com.tshang.peipei.R;
import com.tshang.peipei.model.biz.space.SkillUtilsBiz;
import com.tshang.peipei.model.request.RequestMarkSkillDeal.IMarkSkillDeal;

/**
 * 评分dialog
 * @author Jeff
 *
 */
public class MarkSkillDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener,
		RatingBar.OnRatingBarChangeListener {

	private Activity context;
	private int otherUid = -1;
	private int skillId = -1;
	private int point = 50;
	private IMarkSkillDeal iMarkSkill;

	public MarkSkillDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public MarkSkillDialog(Activity context, int theme, int otherUid, int skillId, IMarkSkillDeal iMarkSkill) {
		super(context, theme);
		this.context = context;
		this.otherUid = otherUid;
		this.skillId = skillId;
		this.iMarkSkill = iMarkSkill;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_mark_skill_score);

		findViewById(R.id.ok_sure).setOnClickListener(this);
		findViewById(R.id.ok_cancel).setOnClickListener(this);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.rb_dialog_ratingbar);
		ratingBar.setOnRatingBarChangeListener(this);
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
			new SkillUtilsBiz(context).markSkill(otherUid, skillId, point, iMarkSkill);
			break;

		default:
			break;
		}

	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		point = (int) (rating * 10);
	}
}
