package com.tshang.peipei.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.LoginType;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.PeiPeiAppStartBiz;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.biz.user.UserRegisterBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackReportAppInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackUserIsExist;
import com.tshang.peipei.model.bizcallback.BizCallBackUserRegister;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: 注册界面
 *
 * @Description: 注册
 *
 * @author allen
 *
 * @version V1.0   
 */
public class RegisterActivity extends BaseActivity implements BizCallBackUserIsExist, BizCallBackUserRegister, BizCallBackReportAppInfo {

	private static final int IS_USER_Exist = 1;//用户是否存在
	private static final int REGISTER_USER = 2; //注册用户

	private EditText mPwdEdit;
	private EditText mNickEdit;
	private RadioGroup mGenderGroup;
	private EditText mInvitateText;

	private UserAccountBiz mUserBiz;
	private int mGender = -1;//性别

	private boolean doNotShowGenderDialog = false;

	private String phone, code;

	private String registErrorMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void dispatchMessage(Message msg) {
		int msgType = msg.what;
		switch (msgType) {
		case IS_USER_Exist:
			int retCode = msg.arg1;
			if (retCode == BAConstants.rspContMsgType.E_GG_USER_NO_EXIST) {
				int invate = 0;
				if (!TextUtils.isEmpty(mInvitateText.getText().toString().trim())) {
					try {
						invate = Integer.parseInt(mInvitateText.getText().toString().trim());
					} catch (NumberFormatException e) {
						invate = 0;
						e.printStackTrace();
					}
					UserAccountBiz uBiz = new UserAccountBiz(this);
					uBiz.getVerifyUid("".getBytes(), BAApplication.app_version_code, invate, mGender, mHandler);
				} else {
					submitAccount(0);
				}
			} else {
				BaseUtils.showTost(this, R.string.toast_register_account_exist);
			}
			break;
		case REGISTER_USER:
			int resultCode = msg.arg1;
			//成功
			if (resultCode == 0) {
				GoGirlUserInfo userInfo = (GoGirlUserInfo) msg.obj;
				UserSharePreference.getInstance(this).saveUserByKey(userInfo);
				UserUtils.getUserEntity(this);

				UserSharePreference.getInstance(RegisterActivity.this).saveStringKeyValue(new String(userInfo.username), BAConstants.LOGIN_ID);

				PeiPeiAppStartBiz appStartBiz = new PeiPeiAppStartBiz();
				String token = SharedPreferencesTools.getInstance(RegisterActivity.this).getStringValueByKey(BAConstants.PEIPEI_BAIDU_USERID);

				appStartBiz.reportAppInfoToSer(RegisterActivity.this, token, RegisterActivity.this);

				sendNoticeEvent(NoticeEvent.INOTICE_01);

				UserRegisterBiz uBiz = new UserRegisterBiz();
				uBiz.insertMessageToPeipei(RegisterActivity.this);

				Bundle b = new Bundle();
				b.putInt("sex", userInfo.sex.intValue());

				Intent intent = new Intent(this, UploadHeadActivity.class);
				if (b != null) {
					intent.putExtras(b);
				}

				startActivityForResult(intent, InvitationActivity.INVITATE);
				overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			} else if (resultCode == BAConstants.rspContMsgType.E_GG_BAN) {
				new HintToastDialog(this, R.string.tips_phone, R.string.ok).showDialog();
			} else if (resultCode == BAConstants.rspContMsgType.E_GG_REGIST_ONE) {
				if (!TextUtils.isEmpty(registErrorMsg)) {
					DialogFactory.warnMsgDialog(this, getResources().getString(R.string.dialog_hint), registErrorMsg,
							getResources().getString(R.string.dialog_confirm));
				}
			} else {

			}
			break;
		case HandlerValue.ACCOUNT_VERIFY_RESULT:
			if (msg.arg1 == 0) {
				submitAccount(msg.arg2);
			} else if (msg.arg1 == rspContMsgType.E_GG_USER_NO_EXIST) {
				BaseUtils.showTost(this, "ID不存在，请重新输入");
			} else if (msg.arg1 == rspContMsgType.E_GG_SEX) {
				BaseUtils.showTost(this, "此ID为男性帐号，暂不支持输入");
			} else {
				BaseUtils.showTost(this, "邀请码错误");
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == InvitationActivity.INVITATE) {
			setResult(InvitationActivity.INVITATE);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.register_btn_submit:
			String pwd = mPwdEdit.getText().toString().trim();
			String nick = mNickEdit.getText().toString().trim();
			if (verifyAll(pwd, nick)) {
				BaseUtils.showDialog(this, R.string.submitting);
				mUserBiz.isUserExist("".getBytes(), BAApplication.app_version_code, LoginType.BIND_PHONE.getValue(), phone, this);
			}
			break;
		default:
			break;
		}
	}

	private void submitAccount(long invitate) {
		UserAccountBiz mUserBiz = new UserAccountBiz(this);
		String imei = BasePhone.getMobileImei(this);
		mUserBiz.registerUser("".getBytes(), BAApplication.app_version_code, phone, mPwdEdit.getText().toString().trim(), code, mNickEdit.getText()
				.toString().trim(), mGender, invitate, imei, this);
	}

	/**
	 * RagioGroup 监听
	 * @author 
	 *
	 */
	class RadioGroupChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			int radioButtonId = group.getCheckedRadioButtonId();
			RadioButton rbn = (RadioButton) RegisterActivity.this.findViewById(radioButtonId);
			if (rbn.getText().toString().trim().equals("男")) {
				mGender = BAConstants.Gender.MALE.getValue();
			} else {
				mGender = BAConstants.Gender.FEMALE.getValue();
			}

			if (!doNotShowGenderDialog) {
				new HintToastDialog(RegisterActivity.this, R.string.toast_register_gender_hint, R.string.i_know).showDialog();
				doNotShowGenderDialog = true;
			}
		}
	}

	/**
	 * 验证账号
	 * @param account 账号
	 * @return
	 */
	//	private boolean verifyAccount(String account) {
	//		if (TextUtils.isEmpty(account)) {
	//			BaseUtils.showTost(this, R.string.toast_register_account_null);
	//			return false;
	//		}
	//
	//		if (!BaseString.isEmail(account)) {
	//			BaseUtils.showTost(this, R.string.toast_register_account_mustbe_numorchar);
	//			return false;
	//		}
	//
	//		return true;
	//	}

	/**
	 * 验证密码
	 * @param pwd 密码
	 * @return
	 */
	private boolean verifyPwd(String pwd) {
		if (TextUtils.isEmpty(pwd)) {
			BaseUtils.showTost(this, R.string.toast_register_pwd_null);
			return false;
		}
		if (pwd.length() < 6 || pwd.length() > 16) {
			BaseUtils.showTost(this, R.string.toast_register_pwd_illegal);
			return false;
		}
		return true;
	}

	/**
	 * 验证昵称
	 * @param nick 昵称
	 * @return
	 */
	private boolean verifyNick(String nick) {
		if (TextUtils.isEmpty(nick)) {
			BaseUtils.showTost(this, R.string.toast_register_nick_null);
			return false;
		}
		if (nick.length() < 3 || nick.length() > 8) {
			BaseUtils.showTost(this, R.string.toast_register_nick_illegal);
			return false;
		}
		return true;
	}

	/**
	 * 验证性别
	 * @return
	 */
	private boolean verifyGender() {
		if (mGender == -1) {
			BaseUtils.showTost(this, R.string.toast_register_gender_null);
			return false;
		}
		return true;
	}

	/**
	 * 验证所有通过
	 * @param account
	 * @param pwd
	 * @param nick
	 * @return
	 */
	private boolean verifyAll(String pwd, String nick) {
		if (!verifyPwd(pwd)) {
			return false;
		}
		if (!verifyNick(nick)) {
			return false;
		}
		if (!verifyGender()) {
			return false;
		}
		return true;
	}

	@Override
	public void isUserExist(int retCode, GoGirlUserInfo info) {
		sendHandlerMessage(mHandler, IS_USER_Exist, retCode, info);
	}

	@Override
	public void userRegister(int retCode, String errorMsg, GoGirlUserInfo userInfo) {
		this.registErrorMsg = errorMsg;
		sendHandlerMessage(mHandler, REGISTER_USER, retCode, userInfo);
	}

	@Override
	public void reportAppInfoBack(int retCode) {}

	@Override
	protected void initData() {
		mUserBiz = new UserAccountBiz(this);

		Bundle b = getIntent().getExtras();
		phone = b.getString(RegisterByPhoneActivity.REGISTER_PHONE);
		code = b.getString(RegisterByPhoneActivity.REGISTER_CODE);

	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_register_activity2);

		findViewById(R.id.register_btn_submit).setOnClickListener(this);
		mPwdEdit = (EditText) findViewById(R.id.register_password);
		mNickEdit = (EditText) findViewById(R.id.register_nick);
		mGenderGroup = (RadioGroup) findViewById(R.id.register_rg);
		mGenderGroup.setOnCheckedChangeListener(new RadioGroupChangeListener());

		mInvitateText = (EditText) findViewById(R.id.register_captcha_et);
	}

	@Override
	protected int initView() {
		return R.layout.activity_register;
	}

}
