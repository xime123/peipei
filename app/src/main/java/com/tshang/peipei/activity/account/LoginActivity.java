package com.tshang.peipei.activity.account;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.tencent.connect.UserInfo;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.main.MainActivity;
import com.tshang.peipei.activity.mine.MineSettingServiceAgreementActivity;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.LoginType;
import com.tshang.peipei.base.babase.ErrorInfo;
import com.tshang.peipei.base.babase.User;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.PeiPeiAppStartBiz;
import com.tshang.peipei.model.biz.PeiPeiPersistBiz;
import com.tshang.peipei.model.biz.space.SpaceRelationshipBiz;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFansList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFollowList;
import com.tshang.peipei.model.bizcallback.BizCallBackReportAppInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackUserIsExist;
import com.tshang.peipei.model.bizcallback.BizCallBackUserLogin;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfoList;
import com.tshang.peipei.service.GpsLocationThread;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.sina.AccessTokenKeeper;

/**
 * @Title: 登录界面
 *
 * @Description: 登录界面
 *
 * @author allen
 *
 * @version V1.0   
 */
public class LoginActivity extends BaseActivity implements BizCallBackUserLogin, BizCallBackUserIsExist, BizCallBackReportAppInfo, OnTouchListener,
		BizCallBackGetFansList, BizCallBackGetFollowList {

	private static final int USER_LOGIN = 1;

	private static final int FLAG_ACCOUNT = 1;
	private static final int FLAG_QQ = 2;
	private static final int FLAG_WEIBO = 3;
	private static final int FLAG_WEIXIN = 4;
	private int FLAG = 0;

	private TextView mTextViewRegister;
	private TextView mTextViewFindpwd;
	private EditText mAccountEdit, mPwdEdit;
	private UserAccountBiz mUserBiz;
	private ImageView mDeletaPwd, mDeleteAccount;

	private String mThirdId = "";
	private String qqOpenId, wxOpenId;
	private String access_token = "";

	private Tencent mTencent;

	private SsoHandler mSsoHandler;

	private String sina_name;
	private String sina_gender;
	private String third_head = "";
	private ImageView bgline_login, bgline_pwd;

	private Dialog dialog;

	private String loginErrorMsg;//登录错误提示信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.login_btn_login:
			third_head = "";
			FLAG = FLAG_ACCOUNT;
			String account = mAccountEdit.getText().toString().trim();
			String pwd = mPwdEdit.getText().toString().trim();
			if (verifyAll(account, pwd)) {
				BAApplication.isLogin = true;
				BaseUtils.showDialog(this, R.string.str_logining);
				String userPwd = mPwdEdit.getText().toString().trim();
				String imei = BasePhone.getMobileImei(this);
				mUserBiz.login("".getBytes(), BAApplication.app_version_code, account, userPwd, imei, this);
			}
			break;
		case R.id.tv_register:
			BAApplication.isLogin = false;
			BaseUtils.openActivity(this, RegisterByPhoneActivity.class);
			this.defaultFinish();
			break;
		case R.id.login_tv_qq:
			third_head = "";
			doLoginByQQ();
			break;
		case R.id.login_tv_sina:
			third_head = "";
			doLoginBySina();
			break;
		case R.id.login_tv_wechat:
			third_head = "";
			doLoginByWeChat();
			break;
		case R.id.tv_findpwd:
			BaseUtils.openActivity(this, SetPasswdActivity.class);
			break;
		case R.id.login_delete_btn:
			UserSharePreference.getInstance(this).saveStringKeyValue("", BAConstants.LOGIN_ID);
			mAccountEdit.setText("");
			mDeleteAccount.setVisibility(View.GONE);
			break;
		case R.id.login_delete_psw:
			mPwdEdit.setText("");
			mDeletaPwd.setVisibility(View.GONE);
			break;
		case R.id.login_service_agreement:
			BaseUtils.openActivity(this, MineSettingServiceAgreementActivity.class);
			break;
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (mTencent != null)
			mTencent.onActivityResult(arg0, arg1, arg2);

		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(arg0, arg1, arg2);
			BaseUtils.cancelDialog();
		}

		if (arg1 == InvitationActivity.INVITATE) {
			BaseUtils.openActivity(this, MainActivity.class);
			finish();
			getLocation();
		}
	}

	/**
	 * qq登录
	 */
	private void doLoginByQQ() {
		FLAG = FLAG_QQ;
		BaseUtils.showDialog(this, R.string.submitting);
		IUiListener listener = new BaseUiListener() {
			@Override
			protected void doComplete(JSONObject values) {
				try {
					Log.i("Aaron", "values====" + values);
					String openid = values.getString("openid");
					qqOpenId = mThirdId = openid;
					mUserBiz.isUserExist("".getBytes(), BAApplication.app_version_code, LoginType.QQ.getValue(), openid, LoginActivity.this);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

		if (TextUtils.isEmpty(qqOpenId)) {
			mTencent.login(this, BAConstants.QQ_SCOPE, listener);
		} else {
			mThirdId = qqOpenId;
			mUserBiz.isUserExist("".getBytes(), BAApplication.app_version_code, LoginType.QQ.getValue(), qqOpenId, LoginActivity.this);
		}
	}

	private void doLoginBySina() {
		FLAG = FLAG_WEIBO;
		BaseUtils.showDialog(this, R.string.str_logining);

		/** 微博 Web 授权类，提供登陆等功能 */
		//		WeiboAuth mWeiboAuth = new WeiboAuth(this, BAConstants.SINA_APP_KEY, BAConstants.REDIRECT_URL, BAConstants.SINA_SCOPE);

		AuthInfo authInfo = new AuthInfo(this, BAConstants.SINA_APP_KEY, BAConstants.REDIRECT_URL, BAConstants.SINA_SCOPE);

		/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
		mSsoHandler = new SsoHandler(this, authInfo);
		mSsoHandler.authorize(new AuthListener() {
			@Override
			public void onComplete(Bundle values) {
				super.onComplete(values);
				Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
				if (accessToken != null && accessToken.isSessionValid()) {
					String uid = accessToken.getUid();
					//					WeiboParameters params = new WeiboParameters(BAConstants.SINA_APP_KEY);
					//					params.put("uid", Long.parseLong(uid));
					//					params.put("access_token", accessToken.getToken());
					//										new AsyncWeiboRunner(getApplicationContext(),BAConstants.SINA_USERINFO, params, "GET", mListener);
					//					new AsyncWeiboRunner(getApplicationContext()).requestAsync(BAConstants.SINA_USERINFO, params, "POST", mListener);
//					UsersAPI api = new UsersAPI(LoginActivity.this, BAConstants.SINA_APP_KEY, accessToken);
//					api.show(Long.parseLong(uid), mListener);
//					AccessTokenKeeper.writeAccessToken(LoginActivity.this, accessToken);
//					mThirdId = uid;
				} else {
					BaseUtils.cancelDialog();
				}
			}
		});
	}

	private void doLoginByWeChat() {
		FLAG = FLAG_WEIXIN;

		if (TextUtils.isEmpty(wxOpenId)) {
			IWXAPI mWXapi = WXAPIFactory.createWXAPI(this, BAConstants.WXAPPID, true);

			if (isWXAppInstalledAndSupported(mWXapi)) {
				mWXapi.registerApp(BAConstants.WXAPPID);

				final SendAuth.Req req = new SendAuth.Req();
				req.scope = "snsapi_userinfo";
				req.state = "com.tshang.peipei";
				mWXapi.sendReq(req);
			} else {
				BaseUtils.showTost(this, R.string.str_wetchat_install);
			}
		} else {
			mThirdId = wxOpenId;
			mHandler.sendEmptyMessage(HandlerValue.WX_LOGIN_RESULT);
		}
	}

	/**
	 * 验证账号
	 * @param id
	 * @return
	 */
	private boolean verifyAccount(String account) {
		if (TextUtils.isEmpty(account)) {
			BaseUtils.showTost(this, R.string.toast_login_account_null);
			return false;
		}
		if (account.length() < 4) {
			BaseUtils.showTost(this, R.string.toast_login_account_illegal);
			return false;
		}
		return true;

	}

	/**
	 * 验证密码
	 * @param pwd
	 * @return
	 */
	private boolean verifyPwd(String pwd) {
		if (TextUtils.isEmpty(pwd)) {
			BaseUtils.showTost(this, R.string.toast_login_pwd_null);
			return false;
		}
		if (pwd.length() < 6 || pwd.length() > 16) {
			BaseUtils.showTost(this, R.string.toast_login_pwd_illegal);
			return false;
		}
		return true;
	}

	/**
	 * 验证账户,密码
	 * @param account
	 * @param pwd
	 * @return
	 */
	private boolean verifyAll(String account, String pwd) {
		if (!verifyAccount(account)) {
			return false;
		}
		if (!verifyPwd(pwd)) {
			return false;
		}
		return true;
	}

	@Override
	public void loginCallBack(int retcode, String msg, GoGirlUserInfo userInfo) {
		loginErrorMsg = msg;
		sendHandlerMessage(mHandler, USER_LOGIN, retcode, userInfo);
	}

	@Override
	public void isUserExist(int retCode, GoGirlUserInfo info) {
		sendHandlerMessage(mHandler, HandlerType.LOGIN_CALLBACK, retCode, info);
	}

	/**
	 * qq登录使用的接口
	 *
	 */
	private class BaseUiListener implements IUiListener {

		public BaseUiListener() {}

		protected void doComplete(JSONObject values) {
			BaseUtils.cancelDialog();
		}

		@Override
		public void onError(UiError e) {
			BaseLog.w("Tencent", "error");
			BaseUtils.cancelDialog();
		}

		@Override
		public void onCancel() {
			BaseLog.w("Tencent", "cancel");
			BaseUtils.cancelDialog();
		}

		@Override
		public void onComplete(Object arg0) {
			doComplete((JSONObject) arg0);

		}
	}

	/**
	 * 微博登录用的接口
	 *
	 */
	private class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {}

		@Override
		public void onWeiboException(WeiboException e) {}

		@Override
		public void onCancel() {}
	}

	@Override
	public void reportAppInfoBack(int retCode) {

	}

	@Override
	protected void initData() {
		mUserBiz = new UserAccountBiz(this);
		mTencent = Tencent.createInstance(BAConstants.QQ_APP_KEY, this);
	}

	@Override
	protected void initRecourse() {
		mAccountEdit = (EditText) findViewById(R.id.login_email_01);
		mDeleteAccount = (ImageView) findViewById(R.id.login_delete_btn);
		mDeleteAccount.setOnClickListener(this);
		String name = UserSharePreference.getInstance(this).getStringValueByKey(BAConstants.LOGIN_ID);
		if (!TextUtils.isEmpty(name)) {
			mAccountEdit.setText(name);
			mDeleteAccount.setVisibility(View.VISIBLE);
		}

		mPwdEdit = (EditText) findViewById(R.id.login_password);

		mTextViewRegister = (TextView) findViewById(R.id.tv_register);
		mTextViewFindpwd = (TextView) findViewById(R.id.tv_findpwd);

		findViewById(R.id.login_btn_login).setOnClickListener(this);
		findViewById(R.id.login_tv_qq).setOnClickListener(this);
		findViewById(R.id.login_tv_sina).setOnClickListener(this);
		findViewById(R.id.login_tv_wechat).setOnClickListener(this);

		mDeletaPwd = (ImageView) findViewById(R.id.login_delete_psw);
		mDeletaPwd.setOnClickListener(this);
		mPwdEdit.addTextChangedListener(new PwdTextWatcher());
		mPwdEdit.setOnTouchListener(this);
		mAccountEdit.setOnTouchListener(this);
		mAccountEdit.addTextChangedListener(new AccountTextWatcher());

		mTextViewRegister.setOnClickListener(this);
		mTextViewFindpwd.setOnClickListener(this);

		bgline_login = (ImageView) findViewById(R.id.login_bg1_iv);
		bgline_pwd = (ImageView) findViewById(R.id.login_bg2_iv);

		TextView tvServiceAgreement = (TextView) findViewById(R.id.login_service_agreement);
		tvServiceAgreement.setOnClickListener(this);
		tvServiceAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		int msgType = msg.what;
		int retCode = -1;
		switch (msgType) {
		case USER_LOGIN:
			retCode = msg.arg1;
			if (retCode == BAConstants.rspContMsgType.E_GG_USER_NO_EXIST) {
				BaseUtils.showTost(this, R.string.toast_user_not_exist);
			} else if (retCode == BAConstants.rspContMsgType.E_GG_PASSWD) {
				BaseUtils.showTost(this, R.string.toast_user_pwd_error);
			} else if (retCode == BAConstants.rspContMsgType.E_GG_LOGIN_ONE) {//账号限制登录
				if (!TextUtils.isEmpty(loginErrorMsg)) {
					DialogFactory.warnMsgDialog(this, getResources().getString(R.string.dialog_hint), loginErrorMsg,
							getResources().getString(R.string.dialog_confirm));
				}
			} else if (retCode == BAConstants.rspContMsgType.E_GG_BAN) {
				new HintToastDialog(this, R.string.tips_phone, R.string.ok).showDialog();
			} else if (retCode == 0) {
				PeiPeiAppStartBiz appStartBiz = new PeiPeiAppStartBiz();
				String token = SharedPreferencesTools.getInstance(LoginActivity.this).getStringValueByKey(BAConstants.PEIPEI_BAIDU_USERID);
				BAApplication.mLocalUserInfo = (GoGirlUserInfo) msg.obj;

				sendNoticeEvent(NoticeEvent.INOTICE_01);
				appStartBiz.reportAppInfoToSer(LoginActivity.this, token, LoginActivity.this);

				//*******************
				SpaceRelationshipBiz space = new SpaceRelationshipBiz(this);
				space.getFansList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), -1,
						100, this);

				space.getfollowList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
						-1, 100, this);

				//*********************
				if (FLAG != FLAG_ACCOUNT) {
					if (!TextUtils.isEmpty(third_head) && !third_head.equals("true")) {//检测没有使用过该账户，有头像上传头像
						imageLoader.displayImage("third://" + third_head, new ImageView(this), new ThirdLoadHeadListener(LoginActivity.this,
								Gender.MALE.getValue()));
						BaseUtils.openActivity(this, MainActivity.class);
						getLocation();
					} else {
						if (TextUtils.isEmpty(third_head)) {//没有使用过该账户，没有拉取到头像
							Bundle b = new Bundle();
							b.putInt("sex", Gender.MALE.getValue());

							Intent intent = new Intent(this, UploadHeadActivity.class);
							if (b != null) {
								intent.putExtras(b);
							}

							startActivityForResult(intent, InvitationActivity.INVITATE);
							overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
							getLocation();
						} else {
							BaseUtils.openActivity(this, MainActivity.class);
							SuccessFinish();
							getLocation();
						}
					}
				} else {
					BaseUtils.openActivity(this, MainActivity.class);
					SuccessFinish();
					getLocation();
				}
			} else {
				BaseUtils.showTost(this, R.string.toast_login_failure);
			}
			break;
		case HandlerType.LOGIN_CALLBACK:
			retCode = msg.arg1;
			GoGirlUserInfo info = (GoGirlUserInfo) msg.obj;
			if (retCode == 0) {
				third_head = "true";
				String imei = BasePhone.getMobileImei(this);
				BaseUtils.showDialog(this, R.string.str_logining);
				String usernick = new String(info.nick);
				if (TextUtils.isEmpty(usernick)) {
					usernick = "";
				}
				if (usernick.length() > 8) {
					usernick = usernick.substring(0, 8);
				}

				mUserBiz.loginThird("".getBytes(), BAApplication.app_version_code, usernick, info.sex.intValue(), FLAG, mThirdId, 0, imei,
						LoginActivity.this);
			} else if (retCode == BAConstants.rspContMsgType.E_GG_USER_NO_EXIST) {
				if (!TextUtils.isEmpty(mThirdId)) {
					if (FLAG == FLAG_QQ) {
						back_toqq();
					} else if (FLAG == FLAG_WEIBO) {
						if (sina_gender.equals("m")) {
							check_third_userinfo(sina_name, "男");
						} else {
							check_third_userinfo(sina_name, "女");
						}
					} else if (FLAG == FLAG_WEIXIN) {
						new ToWxGetUserInfoThread().start();
					}
				}
			}

			break;
		case HandlerType.LOGIN_GET_USERINFO:
			BaseUtils.showDialog(this, R.string.str_logining);
			String imei = BasePhone.getMobileImei(this);
			String nick = msg.obj.toString();
			if (nick.length() > 8) {
				nick = nick.substring(0, 7);
			}
			mUserBiz.loginThird("".getBytes(), BAApplication.app_version_code, nick, msg.arg1, FLAG, mThirdId, 0, imei, LoginActivity.this);
			break;
		case HandlerValue.WX_LOGIN_RESULT:
			mUserBiz.isUserExist("".getBytes(), BAApplication.app_version_code, LoginType.TWX.getValue(), mThirdId, LoginActivity.this);
			break;
		case HandlerValue.WX_LOGIN_GET_USERINFO:
			BaseUtils.cancelDialog();
			String gender = "";
			if (msg.arg1 == 1) {
				gender = "男";
			} else if (msg.arg1 == 2) {
				gender = "女";
			}
			check_third_userinfo((String) msg.obj, gender);
			break;
		}

	}

	private void getLocation() {
		new GpsLocationThread(getApplicationContext()).start();
	}

	private void back_toqq() {
		UserInfo mInfo = new UserInfo(this, BAApplication.mQQAuth.getQQToken());
		IUiListener userInfolistener = new BaseUiListener() {
			@Override
			protected void doComplete(JSONObject values) {
				try {
					Log.d("Aaron", "doComplete==" + values.toString());
					String nick = values.getString("nickname");
					String gender = values.getString("gender");
					third_head = values.getString("figureurl_qq_2");
					check_third_userinfo(nick, gender);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		};

		mInfo.getUserInfo(userInfolistener);
	}

	@Override
	protected int initView() {
		return R.layout.activity_login;
	}

	private void check_third_userinfo(String nick, String gender) {
		int sex = -1;
		if (gender.equals("男")) {
			sex = Gender.MALE.getValue();
		} else if (gender.equals("女")) {
			sex = Gender.FEMALE.getValue();
		}

		if (TextUtils.isEmpty(nick) || sex == -1) {
			//获取用户信息失败
			Intent intent = new Intent(LoginActivity.this, RegisterThirdActivity.class);
			intent.putExtra("name", mThirdId);
			intent.putExtra("type", FLAG);
			intent.putExtra("head", third_head);
			startActivity(intent);
			overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			finish();
		} else {
			Intent intent = new Intent(LoginActivity.this, InvitationActivity.class);
			intent.putExtra(InvitationActivity.USERNAME, mThirdId);
			intent.putExtra(InvitationActivity.USERPWD, "");
			intent.putExtra(InvitationActivity.USERNICK, nick);
			intent.putExtra(InvitationActivity.USERTYPE, FLAG);
			intent.putExtra(InvitationActivity.USERSEX, sex);
			intent.putExtra("head", third_head);
			startActivityForResult(intent, InvitationActivity.INVITATE);
			overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
		}
	}

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				// 调用 User#parse 将JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {
					sina_name = user.screen_name;
					sina_gender = user.gender;
					third_head = user.avatar_hd;
				}
			}

			mUserBiz.isUserExist("".getBytes(), BAApplication.app_version_code, LoginType.SINA.getValue(), mThirdId, LoginActivity.this);
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			BaseUtils.showTost(LoginActivity.this, info.toString());
		}
	};

	public class PwdTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			int length = s.length();
			if (length > 0) {
				mDeletaPwd.setVisibility(View.VISIBLE);
			} else {
				mDeletaPwd.setVisibility(View.GONE);
			}

		}

		@Override
		public void afterTextChanged(Editable s) {}
	}

	public class AccountTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			int length = s.length();
			if (length > 0) {
				mDeleteAccount.setVisibility(View.VISIBLE);
			} else {
				mDeleteAccount.setVisibility(View.GONE);
			}

		}

		@Override
		public void afterTextChanged(Editable s) {}
	}

	/**
	 * 验证昵称
	 * @param nick 昵称
	 * @return
	 */
	//	private boolean verifyNick(String nick) {
	//		if (TextUtils.isEmpty(nick)) {
	//			BaseUtils.showTost(this, R.string.toast_register_nick_null);
	//			return false;
	//		}
	//		if (nick.length() < 3 || nick.length() > 8) {
	//			BaseUtils.showTost(this, R.string.toast_register_nick_illegal);
	//			return false;
	//		}
	//		return true;
	//	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.login_email_01:
			bgline_login.setBackgroundResource(R.drawable.main_loginandpassword_pr);
			bgline_pwd.setBackgroundResource(R.drawable.main_loginandpassword_un);
			break;
		case R.id.login_password:
			bgline_login.setBackgroundResource(R.drawable.main_loginandpassword_un);
			bgline_pwd.setBackgroundResource(R.drawable.main_loginandpassword_pr);
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					PeiPeiPersistBiz.getInstance().closePersist(LoginActivity.this);
					PeiPeiRequest.close();
					//关闭线程池
					AppQueueManager.getInstance().shutdownWork();
					System.exit(0);
					System.gc();
					BAApplication.getInstance().exit();
				}
			}).start();

		}
		return false;
	}

	@Override
	protected void onResume() {
		if (!TextUtils.isEmpty(BAApplication.wxCode)) {
			BaseUtils.showDialog(this, R.string.loading);

			if (!wxThread.isAlive())
				wxThread.start();
		}

		super.onResume();
	}

	Thread wxThread = new Thread() {
		public void run() {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", BAConstants.WXAPPID));
			params.add(new BasicNameValuePair("secret", BAConstants.WXAppSecret));
			params.add(new BasicNameValuePair("code", BAApplication.wxCode));
			params.add(new BasicNameValuePair("grant_type", "authorization_code"));

			// 对参数编码
			String param = URLEncodedUtils.format(params, "UTF-8");

			String httpurl = "https://api.weixin.qq.com/sns/oauth2/access_token?" + param;
			HttpGet httpRequest = new HttpGet(httpurl);
			HttpClient httpclient = new DefaultHttpClient();
			try {
				// 请求httpClient ，取得HttpRestponse
				HttpResponse httpResponse = httpclient.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// 取得相关信息 取得HttpEntiy
					HttpEntity httpEntity = httpResponse.getEntity();

					String s = EntityUtils.toString(httpEntity, "utf-8");

					JSONObject j = new JSONObject(s);
					if (j.has("access_token")) {
						access_token = (String) j.get("access_token");
					}

					if (j.has("openid")) {
						wxOpenId = mThirdId = (String) j.get("openid");

						mHandler.sendEmptyMessage(HandlerValue.WX_LOGIN_RESULT);
					}
					BAApplication.wxCode = "";
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private boolean isWXAppInstalledAndSupported(IWXAPI api) {
		return api.isWXAppInstalled() && api.isWXAppSupportAPI();
	}

	class ToWxGetUserInfoThread extends Thread {
		public void run() {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("access_token", access_token));
			params.add(new BasicNameValuePair("openid", mThirdId));

			// 对参数编码
			String param = URLEncodedUtils.format(params, "UTF-8");

			String httpurl = "https://api.weixin.qq.com/sns/userinfo?" + param;
			HttpGet httpRequest = new HttpGet(httpurl);
			HttpClient httpclient = new DefaultHttpClient();
			try {
				// 请求httpClient ，取得HttpRestponse
				HttpResponse httpResponse = httpclient.execute(httpRequest);
				String nickname = null;
				int sex = -1;
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// 取得相关信息 取得HttpEntiy
					HttpEntity httpEntity = httpResponse.getEntity();

					String s = EntityUtils.toString(httpEntity, "utf-8");

					JSONObject j = new JSONObject(s);
					if (j.has("nickname")) {
						nickname = j.getString("nickname");
					}

					if (j.has("sex")) {
						sex = j.getInt("sex");
					}

					if (j.has("headimgurl")) {
						third_head = j.getString("headimgurl");
						third_head = third_head.substring(0, third_head.length() - 1) + "132";
					}

					sendHandlerMessage(mHandler, HandlerValue.WX_LOGIN_GET_USERINFO, sex, nickname);
					BaseUtils.cancelDialog();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void getFollowListCallBack(int retCode, RetFollowInfoList list, int isend) {
		if (retCode == 0) {
			if (null != list && list.size() > 0) {
				for (Object object : list) {
					RetFollowInfo info = (RetFollowInfo) object;
					SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").saveStringKeyValue(
							new String(info.followinfo.alias), info.followuserinfo.uid.intValue() + "");
				}
			}
		}
	}

	@Override
	public void getFansList(int retCode, RetFollowInfoList list, int isend) {
		if (retCode == 0) {
			if (null != list && list.size() > 0) {
				for (Object object : list) {
					RetFollowInfo info = (RetFollowInfo) object;
					SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").saveStringKeyValue(
							new String(info.followinfo.alias), info.followuserinfo.uid.intValue() + "");
				}
			}
		}
	}
}
