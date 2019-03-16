package com.tshang.peipei.activity.account;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.main.MainHallPagerAdapter;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.request.RequestGetMsgCode.iGetMsgCode;
import com.tshang.peipei.model.request.RequestReSetPasswd;
import com.tshang.peipei.model.request.RequestReSetPasswd.ISetPasswd;
import com.tshang.peipei.model.request.RequestReSetPhoneAccountPasswd.iReSetPhoneAccountPasswd;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * 重置密码界面
 * @author Jeff
 *
 */
public class SetPasswdActivity extends BaseActivity implements ISetPasswd, iGetMsgCode, iReSetPhoneAccountPasswd {

	private EditText edtSetPasswdEmail, edtSetPasswdPhone, edtSetPasswdCode;
	private EditText edtNewPwd1, edtNewPwd2;

	private ViewPager mViewPager;
	private View mViewPhone, mViewEmail;

	private TextView mPhoneTv, mEmailTv;

	private Button mBtnInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {//处理监听
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_phone:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.tv_email:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.btn_setpasswd_email:
			String account = edtSetPasswdEmail.getText().toString().trim();
			if (TextUtils.isEmpty(account)) {
				BaseUtils.showTost(this, R.string.str_please_input_account);
				return;
			}
			BaseUtils.showDialog(this, R.string.submitting);
			RequestReSetPasswd req = new RequestReSetPasswd();
			req.addReply("".getBytes(), BAApplication.app_version_code, account, this);
			break;
		case R.id.btn_setpasswd_phone_code:
			String phone = edtSetPasswdPhone.getText().toString().trim();

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
		case R.id.btn_setpasswd_phone:
			String phone1 = edtSetPasswdPhone.getText().toString().trim();
			if (TextUtils.isEmpty(phone1)) {
				BaseUtils.showTost(this, R.string.str_mobile_phone_null);
			} else {
				if (!BaseString.isMobileNO(phone1)) {
					BaseUtils.showTost(this, R.string.str_mobile_phone_error);
				} else {
					String code = edtSetPasswdCode.getText().toString().trim();

					if (TextUtils.isEmpty(code)) {
						BaseUtils.showTost(this, R.string.str_code_no_null);
					} else {
						if (checkPwdIsEmpty()) {
							if (changePwd()) {
								String phonePwd = edtNewPwd1.getText().toString();
								UserSettingBiz uBiz = new UserSettingBiz();
								uBiz.setPwdByPhone("".getBytes(), BAApplication.app_version_code, phone1, code, phonePwd, this);
							}
						}
					}

				}
			}
			break;
		}

	}

	//验证密码是否为空
	private boolean checkPwdIsEmpty() {
		if (TextUtils.isEmpty(edtNewPwd1.getText().toString())) {
			BaseUtils.showTost(this, "密码不能为空");
			return false;
		}
		if (TextUtils.isEmpty(edtNewPwd2.getText().toString())) {
			BaseUtils.showTost(this, "密码不能为空");
			return false;
		}

		if (edtNewPwd1.getText().toString().length() < 6 || edtNewPwd2.getText().toString().length() < 6) {
			BaseUtils.showTost(this, "密码不能为小于6位");
			return false;
		}

		if (edtNewPwd1.getText().toString().length() > 16 || edtNewPwd2.getText().toString().length() > 16) {
			BaseUtils.showTost(this, "密码不能为大于16位");
			return false;
		}
		return true;

	}

	/**
	 * 修改密码
	 * @author vactor
	 *
	 */
	private boolean changePwd() {
		String oldPwd = edtNewPwd1.getText().toString();
		String newPwd = edtNewPwd2.getText().toString();

		if (oldPwd.equals(newPwd)) {
			return true;
		}
		BaseUtils.showTost(this, "您2次输入的密码不相同，请重新输入!");
		return false;
	}

	@Override
	public void addCommentCallBack(int retCode) {//提交意见反馈返回结果
		HandlerUtils.sendHandlerMessage(mHandler, COMMIT_COMMENT_VALUE, retCode, retCode);
	}

	private static final int COMMIT_COMMENT_VALUE = 0x10;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {//监听触摸空白区域
		hideSoftInput(edtSetPasswdPhone);//隐藏软键盘
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void initData() {//初始化数据
		showSoftInput(edtSetPasswdPhone);//显示软键盘
	}

	@Override
	protected void initRecourse() {//查找控件及设置监听
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.login);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_set_passwd);

		mPhoneTv = (TextView) findViewById(R.id.tv_phone);
		mEmailTv = (TextView) findViewById(R.id.tv_email);

		LayoutInflater mInflater = getLayoutInflater();
		mViewPhone = mInflater.inflate(R.layout.viewpager_phone, null);
		mViewEmail = mInflater.inflate(R.layout.viewpager_email, null);

		mViewPager = (ViewPager) findViewById(R.id.find_viewpager);

		edtSetPasswdEmail = (EditText) mViewEmail.findViewById(R.id.edt_setpasswd_email);
		edtSetPasswdPhone = (EditText) mViewPhone.findViewById(R.id.edt_setpasswd_phone);
		edtSetPasswdCode = (EditText) mViewPhone.findViewById(R.id.edt_setpasswd_code);
		mViewEmail.findViewById(R.id.btn_setpasswd_email).setOnClickListener(this);
		mViewPhone.findViewById(R.id.btn_setpasswd_phone).setOnClickListener(this);
		mViewPhone.findViewById(R.id.btn_setpasswd_phone_code).setOnClickListener(this);

		mBtnInput = (Button) mViewPhone.findViewById(R.id.btn_setpasswd_phone_code);

		edtNewPwd1 = (EditText) mViewPhone.findViewById(R.id.edt_setpasswd_newpwd1);
		edtNewPwd2 = (EditText) mViewPhone.findViewById(R.id.edt_setpasswd_newpwd2);

		mPhoneTv.setOnClickListener(this);
		mEmailTv.setOnClickListener(this);
		setViewPageAndUnderline();
	}

	@Override
	protected int initView() {//加载界面布局文件
		return R.layout.activity_setpasswd;
	}

	@Override
	public void dispatchMessage(Message msg) {//消息返回，UI线程更新界面
		super.dispatchMessage(msg);
		switch (msg.what) {
		case COMMIT_COMMENT_VALUE:
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, R.string.str_reset_success);
				SetPasswdActivity.this.finish();
			} else {
				BaseUtils.showTost(this, R.string.operate_faile);
			}
			break;
		case HandlerValue.BIND_GET_PHONE_RESULT:
			if (msg.arg1 != 0) {
				SharedPreferencesTools.getInstance(this).saveLongKeyValue(0, BAConstants.PEIPEI_APP_BIND_PHONE);
				if (msg.arg1 == rspContMsgType.E_GG_MSG_CODE || msg.arg1 == rspContMsgType.E_CACHE_NO_DATA) {
					BaseUtils.showTost(this, R.string.str_bind_error2);
				} else if (msg.arg1 == rspContMsgType.E_GG_USER_EXIST) {
					BaseUtils.showTost(this, R.string.str_bind_error1);
				} else if (msg.arg1 == rspContMsgType.E_GG_USER_NO_EXIST) {
					BaseUtils.showTost(this, R.string.str_bind_error3);
				}
			}
			break;
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
		case HandlerValue.SET_PWD_BY_PHONE:
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, R.string.str_setpwd_success);
				finish();
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_MSG_CODE) {
				BaseUtils.showTost(this, R.string.str_bind_error2);
			} else if (msg.arg1 == rspContMsgType.E_GG_USER_NO_EXIST) {
				BaseUtils.showTost(this, R.string.str_bind_error3);
			} else {
				BaseUtils.showTost(this, "重置密码失败");
			}
			break;
		}

	}

	private void setViewPageAndUnderline() {
		List<View> mViewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		mViewList.add(mViewPhone);
		mViewList.add(mViewEmail);

		mViewPager.setAdapter(new MainHallPagerAdapter(mViewList));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new FindOnPageChangeListener());
	}

	/**
	 * 页卡切换监听
	 */
	public class FindOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				mPhoneTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
				mEmailTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
				mPhoneTv.setTextColor(getResources().getColor(R.color.peach));
				mEmailTv.setTextColor(getResources().getColor(R.color.gray));
			} else {
				mEmailTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
				mPhoneTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
				mEmailTv.setTextColor(getResources().getColor(R.color.peach));
				mPhoneTv.setTextColor(getResources().getColor(R.color.gray));
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

	@Override
	public void resultMsgCode(int code) {
		sendHandlerMessage(mHandler, HandlerValue.BIND_GET_PHONE_RESULT, code);
	}

	@Override
	public void resultSetPhoneAccountPasswd(int retCode) {
		sendHandlerMessage(mHandler, HandlerValue.SET_PWD_BY_PHONE, retCode);
	}

}
