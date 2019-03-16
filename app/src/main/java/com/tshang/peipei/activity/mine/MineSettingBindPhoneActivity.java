package com.tshang.peipei.activity.mine;

import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.request.RequestBindPhone.iBindPhone;
import com.tshang.peipei.model.request.RequestGetMsgCode.iGetMsgCode;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: MineSettingBindPhoneActivity.java 
 *
 * @Description: 绑定手机 
 *
 * @author allen  
 *
 * @date 2014-11-15 上午10:15:53 
 *
 * @version V1.0   
 */
public class MineSettingBindPhoneActivity extends BaseActivity implements iGetMsgCode, iBindPhone {

	private EditText mMobilePhone;
	private EditText mMoblieCode;
	private EditText mMobilePwd;
	private Button mBtnInput;

	@Override
	protected void initData() {
		mBtnInput.setOnClickListener(this);
		findViewById(R.id.bind_phone_sure_btn).setOnClickListener(this);

		mHandler.sendEmptyMessageDelayed(HandlerValue.BIND_GET_PHONE, 0);
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle.setText(R.string.str_bind_phone);

		TextView tv = (TextView) findViewById(R.id.bind_phone_content_tv);

		SpannableStringBuilder style = new SpannableStringBuilder(getString(R.string.str_mobile_phone_content));
		SpannableStringBuilder style1 = new SpannableStringBuilder(getString(R.string.str_mobile_phone_content1));
		style1.setSpan(new PhoneRedStyle(), 0, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		style.append(style1);
		tv.setText(style);

		mBtnInput = (Button) findViewById(R.id.bind_phone_input_btn);

		mMobilePhone = (EditText) findViewById(R.id.bind_phone_input_mobile);
		mMoblieCode = (EditText) findViewById(R.id.bind_phone_input_code);
		mMobilePwd = (EditText) findViewById(R.id.bind_phone_input_password);

	}

	@Override
	protected int initView() {
		return R.layout.activity_mobilephone;
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
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bind_phone_input_btn:
			String phone = mMobilePhone.getText().toString().trim();
			if (TextUtils.isEmpty(phone)) {
				BaseUtils.showTost(this, R.string.str_mobile_phone_null);
			} else {
				if (!BaseString.isMobileNO(phone)) {
					BaseUtils.showTost(this, R.string.str_mobile_phone_error);
				} else {
					UserSettingBiz uBiz = new UserSettingBiz();
					uBiz.getMsgCode(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
							phone, "", this);
					SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").saveLongKeyValue(
							System.currentTimeMillis() + 60000, BAConstants.PEIPEI_APP_BIND_PHONE);
					mBtnInput.setText(String.format(getString(R.string.str_get_code_again), 59));
					mBtnInput.setBackgroundResource(R.drawable.sign_code_btn_grey);
					mBtnInput.setClickable(false);
					mHandler.sendEmptyMessageDelayed(HandlerValue.BIND_GET_PHONE, 1000);
				}
			}
			break;
		case R.id.bind_phone_sure_btn:
			checkPhoneAndCode();
			break;
		default:
			break;
		}
	}

	private void checkPhoneAndCode() {
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
						String passwd = mMobilePwd.getText().toString().trim();
						if (TextUtils.isEmpty(passwd)) {
							BaseUtils.showTost(this, R.string.toast_login_pwd_null);
						} else {
							if (passwd.length() > 16 || passwd.length() <= 5) {
								BaseUtils.showTost(this, R.string.toast_login_pwd_illegal);
							} else {
								UserSettingBiz uBiz = new UserSettingBiz();
								uBiz.bindPhone(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
										BAApplication.mLocalUserInfo.uid.intValue(), phone1, code, passwd, this);
							}
						}
					}
				}
			}
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
			long oldtime = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getLongKeyValue(
					BAConstants.PEIPEI_APP_BIND_PHONE);
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
		case HandlerValue.BIND_PHONE_RESULT:
			if (msg.arg1 == 0) {
				String str = (String) msg.obj;
				BAApplication.mLocalUserInfo.phone = str.getBytes();
				BaseUtils.showTost(this, R.string.str_bind_success);
				finish();
			} else if (msg.arg1 == rspContMsgType.E_GG_USER_EXIST) {
				BaseUtils.showTost(this, R.string.str_bind_error1);
			} else if (msg.arg1 == rspContMsgType.E_GG_MSG_CODE || msg.arg1 == rspContMsgType.E_CACHE_NO_DATA) {
				BaseUtils.showTost(this, R.string.str_bind_error2);
			}//TODO 错误提示
			break;
		case HandlerValue.BIND_GET_PHONE_RESULT:
			if (msg.arg1 != 0) {
				SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").saveLongKeyValue(0,
						BAConstants.PEIPEI_APP_BIND_PHONE);
				//TODO 错误提示
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
	public void resultBindPhone(int retCode, String phoneno) {
		sendHandlerMessage(mHandler, HandlerValue.BIND_PHONE_RESULT, retCode, phoneno);
	}
}
