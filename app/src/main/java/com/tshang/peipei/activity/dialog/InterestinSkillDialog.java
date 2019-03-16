package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.model.skill.SkillJoin;

/**
 * 感兴趣技能留言
 * @author Jeff
 *
 */
public class InterestinSkillDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {

	private Activity context;
	private EditText edt_content;
	private TextView tv_limit;
	private boolean canSend = true;
	private BAHandler handler;
	private int skillid;
	private int skilluid;

	public InterestinSkillDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public InterestinSkillDialog(Activity context, int theme, int skillid, int skilluid, BAHandler handler) {
		super(context, theme);
		this.context = context;
		this.skillid = skillid;
		this.skilluid = skilluid;
		this.handler = handler;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_interestin_skill_message);

		findViewById(R.id.ok_sure).setOnClickListener(this);
		findViewById(R.id.ok_cancel).setOnClickListener(this);
		edt_content = (EditText) findViewById(R.id.edt_interestin_skill_content);
		edt_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str = ParseMsgUtil.convertUnicode2(s.toString());

				int len = str.length();
				len = 20 - len;
				if (len >= 0) {
					canSend = true;
					tv_limit.setTextColor(context.getResources().getColor(R.color.gray));
				} else {
					canSend = false;
					tv_limit.setTextColor(context.getResources().getColor(R.color.peach));
				}

				tv_limit.setText(String.valueOf(20 - len) + "/" + 20);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		tv_limit = (TextView) findViewById(R.id.tv_interestin_limit);
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
			if (canSend) {
				String text = edt_content.getText().toString();
				if (TextUtils.isEmpty(text)) {
					BaseUtils.showTost(context, R.string.str_leave_messave_not_empty);
					return;
				}
				this.dismiss();
				SkillJoin.getInstance().reqInterestinSkill(context, text, skillid, skilluid, handler);
			} else {
				BaseUtils.showTost(context, "您输入了过长的文字");
			}

			break;
		default:
			break;
		}

	}
}
