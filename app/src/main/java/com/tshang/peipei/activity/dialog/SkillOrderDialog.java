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
import com.tshang.peipei.protocol.asn.gogirl.RetParticipantInfo;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.skill.SkillJoin;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * 男人技能下单dialog
 * @author Jeff
 *
 */
public class SkillOrderDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private RetParticipantInfo info;
	private String skillGiftName;
	private int skillGiftNum;
	private BAHandler handler;

	private int skillid;
	private int skilluid;

	public SkillOrderDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public SkillOrderDialog(Activity context, int theme, RetParticipantInfo info, String skillGiftName, int skillGiftNum, int skillid, int skilluid,
			BAHandler handler) {
		super(context, theme);
		this.context = context;
		this.info = info;
		this.skillGiftNum = skillGiftNum;
		this.skillGiftName = skillGiftName;
		this.handler = handler;
		this.skillid = skillid;
		this.skilluid = skilluid;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_view);
		TextView tvMsg = (TextView) findViewById(R.id.msg);
		String message = context.getString(R.string.str_order_skill_confirm);
		
		String alias = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				 info.participantuserinfo.uid.intValue());
		
		tvMsg.setText(String.format(message, TextUtils.isEmpty(alias) ? new String(info.participantuserinfo.nick) : alias, skillGiftName + "( x" + skillGiftNum + " )"));

		Button btn = (Button) findViewById(R.id.ok_sure);
		btn.setText(R.string.ok);
		btn.setOnClickListener(this);
		findViewById(R.id.ok_cancel).setOnClickListener(this);
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
		case R.id.ok_sure://下单
			this.dismiss();
			SkillJoin.getInstance().reqaddSkillDeal(context, skillid, skilluid, info.participantuserinfo.uid.intValue(), handler);
			break;
		default:
			break;
		}

	}
}
