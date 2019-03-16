package com.tshang.peipei.activity.mine;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackAddSuggestion;
import com.tshang.peipei.model.request.RequestGoGirlAddSuggestion;
import com.tshang.peipei.model.request.RequestGoGirlAddSuggestion.IAddSuggestion;

/**
 * 意见反馈
 * @author Jeff
 *
 */
public class MineSettingFeedback extends BaseActivity implements IAddSuggestion {

	private EditText mEtContent;
	private EditText mEtLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.setting_feedback_lin_submit:
			String content = mEtContent.getText().toString();
			if (TextUtils.isEmpty(content)) {
				BaseUtils.showTost(this, R.string.str_write_something);
				return;
			}
			String link = mEtLink.getText().toString();
			if (TextUtils.isEmpty(link)) {
				link = "";
			}
			BaseUtils.showDialog(this, R.string.str_summiting);
			GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
			if (userEntity != null) {
				addSuggestion(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, content, link, new String(userEntity.nick),
						userEntity.uid.intValue(), null);
			}
			break;

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			hideSoftInput(mEtContent);
			return super.onTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void addSuggestion(byte[] auth, int ver, String con, String link, String nick, int uid, BizCallBackAddSuggestion callBack) {
		RequestGoGirlAddSuggestion req = new RequestGoGirlAddSuggestion();
		req.addSuggestion(auth, ver, con, link, nick, uid, this);
	}

	@Override
	public void addSuggestionCallBack(int retCode) {
		HandlerUtils.sendHandlerMessage(mHandler, retCode, null);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case 0:
			BaseUtils.showTost(this, R.string.str_success_thanks);
			MineSettingFeedback.this.finish();
			break;

		default:
			BaseUtils.showTost(this, R.string.write_failure);
			break;
		}
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) this.findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.setting_feedback);
		mBackText = (TextView) this.findViewById(R.id.title_tv_left);
		mBackText.setText("设置");
		mBackText.setOnClickListener(this);
		mEtContent = (EditText) findViewById(R.id.setting_feedback_et_content);
		findViewById(R.id.setting_feedback_lin_submit).setOnClickListener(this);
		mEtLink = (EditText) findViewById(R.id.setting_feedback_edt_link);
		showSoftInput(mEtContent);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		BaseUtils.hideKeyboard(this, mEtContent);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected int initView() {
		return R.layout.activity_setting_feedback;
	}

}
