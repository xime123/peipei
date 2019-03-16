package com.tshang.peipei.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.main.MainActivity;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.request.RequestGetMsgCode.iGetMsgCode;
import com.tshang.peipei.model.request.RequestVerifyMsgCode.iVerifyMsgCode;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: RigisterByPhoneActivity.java 
 *
 * @Description: 手机注册
 *
 * @author allen  
 *
 * @date 2014-11-18 下午8:40:55 
 *
 * @version V1.0   
 */
public class RegisterByPhoneActivity extends BaseActivity implements iGetMsgCode, iVerifyMsgCode {

	public static final String REGISTER_PHONE = "PHONE";
	public static final String REGISTER_CODE = "CODE";

	private EditText mMobilePhone;
	private EditText mMoblieCode;
	private Button mBtnInput;

	private String resultPhone, resultCode;

	@Override
	protected void initData() {
		mBtnInput.setOnClickListener(this);

		findViewById(R.id.register_phone_next_btn).setOnClickListener(this);

		mHandler.sendEmptyMessageDelayed(HandlerValue.BIND_GET_PHONE, 0);

	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle.setText(R.string.str_register_activity1);

		TextView tv = (TextView) findViewById(R.id.register_phone_content_tv);

		SpannableStringBuilder style = new SpannableStringBuilder(getString(R.string.str_mobile_phone_content));
		SpannableStringBuilder style1 = new SpannableStringBuilder(getString(R.string.str_mobile_phone_content1));
		style1.setSpan(new PhoneRedStyle(), 0, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		style.append(style1);
		tv.setText(style);

		mBtnInput = (Button) findViewById(R.id.register_phone_input_btn);

		mMobilePhone = (EditText) findViewById(R.id.register_phone_input_mobile);
		mMoblieCode = (EditText) findViewById(R.id.register_phone_input_code);

	}

	@Override
	protected int initView() {
		return R.layout.activity_registerphone;
	}

	public class PhoneRedStyle extends ClickableSpan {

		@Override
		public void updateDrawState(TextPaint ds) {//选中的文字变色
			super.updateDrawState(ds);//369a00
			ds.setColor(getResources().getColor(R.color.peach));
			ds.setUnderlineText(false);
		}

		@Override
		public void onClick(View widget) {}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_tv_left:
			BaseUtils.openActivity(this, LoginActivity.class);
			finish();
			break;
		case R.id.register_phone_input_btn:
			String phone = mMobilePhone.getText().toString().trim();
			if (TextUtils.isEmpty(phone)) {
				BaseUtils.showTost(this, R.string.str_mobile_phone_null);
			} else {
				if (!BaseString.isMobileNO(phone)) {
					BaseUtils.showTost(this, R.string.str_mobile_phone_error);
				} else {
					UserSettingBiz uBiz = new UserSettingBiz();
					uBiz.getMsgCode("".getBytes(), BAApplication.app_version_code, -1, phone, "", this);
					SharedPreferencesTools.getInstance(this).saveLongKeyValue(System.currentTimeMillis() + 60000, BAConstants.PEIPEI_APP_BIND_PHONE);
					mBtnInput.setText(String.format(getString(R.string.str_get_code_again), 59));
					mBtnInput.setBackgroundResource(R.drawable.sign_code_btn_grey);
					mBtnInput.setClickable(false);
					mHandler.sendEmptyMessageDelayed(HandlerValue.BIND_GET_PHONE, 1000);
				}
			}
			break;
		case R.id.register_phone_next_btn:
			String phone1 = mMobilePhone.getText().toString().trim();
			if (TextUtils.isEmpty(phone1)) {
				BaseUtils.showTost(this, R.string.str_mobile_phone_null);
			} else {
				if (!BaseString.isMobileNO(phone1)) {
					BaseUtils.showTost(this, R.string.str_mobile_phone_error);
				} else {
					String code = mMoblieCode.getText().toString().trim();
					if (TextUtils.isEmpty(code)) {
						BaseUtils.showTost(this, R.string.str_right_code);
					} else {
						if (code.length() < 4) {
							BaseUtils.showTost(this, R.string.str_right_code);
						} else {
							BaseUtils.showDialog(this, R.string.loading);
							UserAccountBiz uBiz = new UserAccountBiz(this);
							uBiz.getVerifyMsgCode("".getBytes(), BAApplication.app_version_code, -1, code, phone1, this);
						}
					}
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void resultMsgCode(int code) {
		sendHandlerMessage(mHandler, HandlerValue.BIND_GET_PHONE_RESULT, code);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.BIND_GET_PHONE:
			long oldtime = SharedPreferencesTools.getInstance(this).getLongKeyValue(BAConstants.PEIPEI_APP_BIND_PHONE);
			int time = (int) ((oldtime - System.currentTimeMillis()) / 1000);
			if (time >= 0) {
				mBtnInput.setText(String.format(getString(R.string.str_get_code_again), time));
				mBtnInput.setBackgroundResource(R.drawable.sign_code_btn_grey);
				mHandler.sendEmptyMessageDelayed(HandlerValue.BIND_GET_PHONE, 1000);
				mBtnInput.setClickable(false);
			} else {
				mBtnInput.setText(R.string.str_get_code);
				mBtnInput.setBackgroundResource(R.drawable.sign_code_btn_selector);
				mBtnInput.setClickable(true);
			}
			break;
		case HandlerValue.BIND_GET_PHONE_RESULT:
			if (msg.arg1 != 0) {
				SharedPreferencesTools.getInstance(this).saveLongKeyValue(0, BAConstants.PEIPEI_APP_BIND_PHONE);
				if (msg.arg1 == rspContMsgType.E_GG_MSG_CODE || msg.arg1 == rspContMsgType.E_CACHE_NO_DATA) {
					BaseUtils.showTost(this, R.string.str_bind_error2);
				} else if (msg.arg1 == rspContMsgType.E_GG_USER_EXIST) {
					BaseUtils.showTost(this, R.string.str_bind_error1);
				}
			}
			break;
		case HandlerValue.BIND_PHONE_REGISTER_RESULT:
			BaseUtils.cancelDialog();
			if (msg.arg1 == 0) {
				Intent intent = new Intent(this, RegisterActivity.class);
				Bundle b = new Bundle();
				b.putString(REGISTER_PHONE, resultPhone);
				b.putString(REGISTER_CODE, resultCode);
				intent.putExtras(b);
				startActivityForResult(intent, InvitationActivity.INVITATE);
				overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			} else {
				if (msg.arg1 == rspContMsgType.E_GG_MSG_CODE || msg.arg1 == rspContMsgType.E_CACHE_NO_DATA) {
					BaseUtils.showTost(this, R.string.str_bind_error2);
				} else if (msg.arg1 == rspContMsgType.E_GG_USER_EXIST) {
					BaseUtils.showTost(this, R.string.str_bind_error1);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			BaseUtils.openActivity(this, LoginActivity.class);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);

		if (arg1 == InvitationActivity.INVITATE) {
			BaseUtils.openActivity(this, MainActivity.class);
			finish();
		}
	}

	@Override
	public void resultVerifyMsgCode(int retCode, String phone, String code) {
		resultPhone = phone;
		resultCode = code;
		sendHandlerMessage(mHandler, HandlerValue.BIND_PHONE_REGISTER_RESULT, retCode);
	}

}
