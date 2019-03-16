package com.tshang.peipei.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.LoginType;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackChangePwd;
import com.tshang.peipei.model.bizcallback.BizCallBackUpdateUserInfo;
import com.tshang.peipei.model.request.RequestChangePhonePasswd.IChangePwdByPhone;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: 用户信息界面
 *
 * @Description: 展示用户信息
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MineSettingUserInfoUpdateActivity extends BaseActivity implements OnClickListener, BizCallBackUpdateUserInfo, BizCallBackChangePwd,
		IChangePwdByPhone {

	private final int UPDATE_NICK = 1;
	private final int UPDATE_PWD = 2;

	private EditText mEdit1; //文本框
	private EditText mEdit2; //密码框
	private EditText mEdit3; //密码框
	private LinearLayout mRel2;
	private LinearLayout mRel3;

	private Button mBtnSave;

	private boolean isUpdateNick;
	private boolean isUpdatePassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		isUpdateNick = intent.getBooleanExtra(BAConstants.IntentType.SETTINGUSERINFOACTIVITY_UPDATENICK, false);
		isUpdatePassword = intent.getBooleanExtra(BAConstants.IntentType.SETTINGUSERINFOACTIVITY_UPDATEPASSWORD, false);
		initUI();
		showSoftInput(mEdit1);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		BaseUtils.hideKeyboard(this, mEdit1);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.setting_user_update_btn_save:
			//如果是修改用户昵称
			if (isUpdateNick) {
				if (TextUtils.isEmpty(mEdit1.getText().toString().trim())) {
					BaseUtils.showTost(this, "用户名不能为空");
				} else if (mEdit1.getText().toString().length() > 8 || mEdit1.getText().toString().length() < 3) {
					BaseUtils.showTost(this, R.string.toast_register_nick_illegal);
				} else {
					updateNick(mEdit1.getText().toString().trim(), -1);
				}

			}
			//如果是修改用户密码
			if (isUpdatePassword) {
				if (checkPwdIsEmpty()) {
					changePwd();
				}
			}
			break;
		}
	}

	//验证密码是否为空
	private boolean checkPwdIsEmpty() {
		if (TextUtils.isEmpty(mEdit2.getText().toString())) {
			BaseUtils.showTost(this, "密码不能为空");
			return false;
		}
		if (TextUtils.isEmpty(mEdit3.getText().toString())) {
			BaseUtils.showTost(this, "密码不能为空");
			return false;
		}

		if (mEdit2.getText().toString().length() < 6 || mEdit3.getText().toString().length() < 6) {
			BaseUtils.showTost(this, "密码不能为小于6位");
			return false;
		}

		if (mEdit2.getText().toString().length() > 16 || mEdit3.getText().toString().length() > 16) {
			BaseUtils.showTost(this, "密码不能为大于16位");
			return false;
		}
		return true;

	}

	private void initUI() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.setting_userinfo);
		mLinRight = (LinearLayout) findViewById(R.id.title_lin_right);
		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mLinRight.setVisibility(View.VISIBLE);
		mTextRight.setText(R.string.save);
		mEdit1 = (EditText) this.findViewById(R.id.setting_user_update_et1);
		mEdit2 = (EditText) this.findViewById(R.id.setting_user_update_et2);
		mEdit3 = (EditText) this.findViewById(R.id.setting_user_update_et3);
		mRel2 = (LinearLayout) this.findViewById(R.id.setting_user_update_ll2);
		mRel3 = (LinearLayout) this.findViewById(R.id.setting_user_update_ll3);
		mBtnSave = (Button) this.findViewById(R.id.setting_user_update_btn_save);
		mBtnSave.setOnClickListener(this);
		if (isUpdateNick) {
			mEdit2.setVisibility(View.GONE);
			mEdit3.setVisibility(View.GONE);
			mRel2.setVisibility(View.GONE);
			mRel3.setVisibility(View.GONE);
			GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
			if (userEntity != null) {
				mEdit1.setText(new String(userEntity.nick));
				mEdit1.setSelection(new String(userEntity.nick).length());
			}
			mTitle.setText("修改昵称");

		}
		if (isUpdatePassword) {
			mEdit1.setVisibility(View.GONE);
			mEdit2.setVisibility(View.VISIBLE);
			mEdit3.setVisibility(View.VISIBLE);
			mRel2.setVisibility(View.VISIBLE);
			mRel3.setVisibility(View.VISIBLE);
			mTitle.setText("修改密码");
		}

	}

	private void updateNick(String newNick, int birthday) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		UserSettingBiz setBiz = new UserSettingBiz();
		String nick = "";
		if (!TextUtils.isEmpty(newNick)) {
			nick = newNick;
		} else {
			if (userEntity != null) {
				nick = new String(userEntity.nick);
			}
		}
		if (birthday == -1) {
			birthday = 0;
		}

		setBiz.updateUserInfo(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), nick, userEntity.sex.intValue(), birthday,
				new String(userEntity.email), "", this);
	}

	/**
	 * 修改密码
	 * @author vactor
	 *
	 */
	private void changePwd() {
		UserSettingBiz userSetting = new UserSettingBiz();
		String oldPwd = mEdit2.getText().toString();
		String newPwd = mEdit3.getText().toString();
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if (userEntity != null) {
			if (userEntity.type.intValue() != LoginType.BIND_PHONE.getValue()) {
				userSetting.changePassword(userEntity.auth, UPDATE_NICK, userEntity.uid.intValue(), oldPwd, newPwd, this);
			} else {
				userSetting.changePasswordByPhone(userEntity.auth, UPDATE_NICK, userEntity.uid.intValue(), oldPwd, newPwd, this);
			}
		}
	}

	@Override
	public void updateUserInfoCallBack(int retCode) {
		if (isUpdateNick) {
			sendHandlerMessage(mHandler, UPDATE_NICK, retCode);
		}
	}

	@Override
	public void changePwdCallBack(int retCode) {
		HandlerUtils.sendHandlerMessage(mHandler, UPDATE_PWD, retCode, retCode);

	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case UPDATE_NICK:
			showToast(msg.arg1, "修改用户名成功", "修改用户名失败");
			break;
		case UPDATE_PWD:
			showToast(msg.arg1, "修改密码成功", "修改密码失败");
			break;

		}
	}

	private void showToast(int code, String success, String failed) {
		if (code == 0) {
			BaseUtils.showTost(MineSettingUserInfoUpdateActivity.this, success);
			this.setResult(RESULT_OK, new Intent(this, MineSettingUserInfoActivity.class));
			finish();
		} else {
			BaseUtils.showTost(MineSettingUserInfoUpdateActivity.this, failed);
		}
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {}

	@Override
	protected int initView() {
		return R.layout.activity_setting_userinfo_update;
	}

	@Override
	public void changePwdByPhoneCallBack(int retCode) {
		HandlerUtils.sendHandlerMessage(mHandler, UPDATE_PWD, retCode, retCode);
	}

}
