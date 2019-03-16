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

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.main.MainActivity;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ShareType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.biz.PeiPeiAppStartBiz;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.space.SpaceCustomBiz;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackReportAppInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackUserLogin;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: RegisterThirdActivity.java 
 *
 * @Description: 第三方登录注册界面 
 *
 * @author allen  
 *
 * @date 2014-4-18 下午5:48:40 
 *
 * @version V1.0   
 */
public class RegisterThirdActivity extends BaseActivity implements BizCallBackUserLogin, BizCallBackReportAppInfo {
	private static final String TAG = "RegisterThirdActivity";

	private static final int USER_LOGIN = 1;

	private Button mSubmit;
	private EditText mNickEdit;
	private RadioGroup mGenderGroup;
	private BAHandler mHandler;

	private int mGender = -1;//性别
	private String mUserName;
	private String mUserHead = "";
	private EditText mInvitateText;
	private int userThird;

	private boolean doNotShowGenderDialog = false;

	private String loginErrorMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_third);
		mUserName = getIntent().getStringExtra("name");
		mUserHead = getIntent().getStringExtra("head");
		initHandler();
		initUI();
		setlistener();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == InvitationActivity.INVITATE) {
			BaseUtils.openActivity(this, MainActivity.class);
			finish();
		}
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		mHandler = new BAHandler(this) {
			@Override
			public void handleMessage(Message msg) {
				try {
					super.handleMessage(msg);
				} catch (NullPointerException e) {
					return;
				}

				int msgType = msg.what;
				int retCode = -1;

				switch (msgType) {
				case USER_LOGIN:
					retCode = msg.arg1;
					if (retCode == BAConstants.rspContMsgType.E_GG_USER_NO_EXIST) {
						BaseUtils.showTost(RegisterThirdActivity.this, R.string.toast_user_not_exist);
					} else if (retCode == BAConstants.rspContMsgType.E_GG_PASSWD) {
						BaseUtils.showTost(RegisterThirdActivity.this, R.string.toast_user_pwd_error);
					} else if (retCode == BAConstants.rspContMsgType.E_GG_BAN) {
						new HintToastDialog(RegisterThirdActivity.this, R.string.tips_phone, R.string.ok).showDialog();
					} else if (retCode == 0) {
						PeiPeiAppStartBiz appStartBiz = new PeiPeiAppStartBiz();
						String token = SharedPreferencesTools.getInstance(RegisterThirdActivity.this).getStringValueByKey(
								BAConstants.PEIPEI_BAIDU_USERID);
						UserUtils.getUserEntity(RegisterThirdActivity.this);
						appStartBiz.reportAppInfoToSer(RegisterThirdActivity.this, token, RegisterThirdActivity.this);

						sendNoticeEvent(NoticeEvent.INOTICE_01);
						//						if (userThird == 3) {
						//							wbThread.start();
						//						}

						if (!TextUtils.isEmpty(mUserHead)) {
							imageLoader.displayImage("third://" + mUserHead, new ImageView(RegisterThirdActivity.this), new ThirdLoadHeadListener(
									RegisterThirdActivity.this, mGender));
						} else {
							Bundle b = new Bundle();
							b.putInt("sex", mGender);
							Intent intent = new Intent(RegisterThirdActivity.this, UploadHeadActivity.class);
							if (b != null) {
								intent.putExtras(b);
							}

							startActivityForResult(intent, InvitationActivity.INVITATE);
							overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
						}
					} else if (retCode == BAConstants.rspContMsgType.E_GG_LOGIN_ONE) {
						if (!TextUtils.isEmpty(loginErrorMsg)) {
							DialogFactory.warnMsgDialog(RegisterThirdActivity.this, getResources().getString(R.string.dialog_hint), loginErrorMsg,
									getResources().getString(R.string.dialog_confirm));
						}
					} else {
						BaseUtils.showTost(RegisterThirdActivity.this, R.string.toast_login_failure);
					}
					break;
				}
			}
		};
	}

	/**
	 * 初始化
	 */
	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);

		mTitle.setText(R.string.register);

		mSubmit = (Button) findViewById(R.id.register_btn_submit);
		mNickEdit = (EditText) findViewById(R.id.register_nick);
		mGenderGroup = (RadioGroup) findViewById(R.id.register_rg);
		mInvitateText = (EditText) findViewById(R.id.register_captcha_et);
	}

	/**
	 * 设置监听
	 */
	private void setlistener() {
		mSubmit.setOnClickListener(this);
		mGenderGroup.setOnCheckedChangeListener(new RadioGroupChangeListener());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_tv_left:
			BaseUtils.openActivity(this, LoginActivity.class);
			finish();
			break;
		case R.id.register_btn_submit:
			String nick = mNickEdit.getText().toString().trim();
			if (verifyAll(nick)) {
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
			}
			break;
		default:
			break;
		}

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
	 * RagioGroup 监听
	 * @author 
	 *
	 */
	class RadioGroupChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			int radioButtonId = group.getCheckedRadioButtonId();
			RadioButton rbn = (RadioButton) RegisterThirdActivity.this.findViewById(radioButtonId);
			if (rbn.getText().toString().trim().equals("男")) {
				mGender = BAConstants.Gender.MALE.getValue();
			} else {
				mGender = BAConstants.Gender.FEMALE.getValue();
			}

			if (!doNotShowGenderDialog) {
				new HintToastDialog(RegisterThirdActivity.this, R.string.toast_register_gender_hint, R.string.i_know).showDialog();
				doNotShowGenderDialog = true;
			}
		}
	}

	@Override
	public void loginCallBack(int retcode, String msg, GoGirlUserInfo userInfo) {
		this.loginErrorMsg = msg;
		HandlerUtils.sendHandlerMessage(mHandler, USER_LOGIN, userInfo);

	}

	@Override
	public void reportAppInfoBack(int retCode) {
		if (retCode == 0) {
			BaseLog.i(TAG, "report success");
		} else {
			BaseLog.i(TAG, "report failed");
		}
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {}

	@Override
	protected int initView() {
		return R.layout.activity_register_third;
	}

	/**
	 * 验证所有通过
	 * @param account
	 * @param pwd
	 * @param nick
	 * @return
	 */
	private boolean verifyAll(String nick) {
		if (!verifyNick(nick)) {
			return false;
		}
		if (!verifyGender()) {
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

	private void submitAccount(long invitate) {
		userThird = getIntent().getIntExtra("type", -1);
		UserAccountBiz mUserBiz = new UserAccountBiz(this);
		String imei = BasePhone.getMobileImei(this);
		String nick = mNickEdit.getText().toString().trim();
		if (verifyAll(nick)) {
			if (nick.length() > 8) {
				nick = nick.substring(0, 8);
			}
			mUserBiz.loginThird("".getBytes(), BAApplication.app_version_code, nick, mGender, userThird, mUserName, (int) invitate, imei, this);
		}
	}

	//分享到新浪微博
	private void shareToSina(String url) {
		// 创建微博 SDK 接口实例
		IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, BAConstants.SINA_APP_KEY);

		// 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
		boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();

		// 如果未安装微博客户端，设置下载微博对应的回调
		if (!isInstalledWeibo) {
//			mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
//				@Override
//				public void onCancel() {}
//			});
		}

		mWeiboShareAPI.registerApp();

		try {
			if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
				int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
				SpaceCustomBiz spaceCustomBiz2 = new SpaceCustomBiz();
				boolean b = false;
				if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
					b = mWeiboShareAPI.sendRequest(
							this,
							spaceCustomBiz2.sendMultiMessage(getString(R.string.share_mm1),
									BitmapFactory.decodeResource(getResources(), R.drawable.logo), url, getString(R.string.share_content1)));
				} else {
					b = mWeiboShareAPI.sendRequest(
							this,
							spaceCustomBiz2.sendSingleMessage(getString(R.string.share_mm1),
									BitmapFactory.decodeResource(getResources(), R.drawable.logo), url, getString(R.string.share_content1)));
				}

				if (b) {
					if (BAApplication.mLocalUserInfo != null) {
						SpaceCustomBiz sBiz = new SpaceCustomBiz();
						if (BAApplication.mLocalUserInfo != null) {
							sBiz.shareTask(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
									BAApplication.mLocalUserInfo.uid.intValue(), ShareType.SHARE_TO_SINA.getValue());
						}
					}
				}
			} else {
				//				BaseUtils.showTost(context, R.string.weibosdk_demo_not_support_api_hint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Thread wbThread = new Thread() {
		public void run() {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("status", getString(R.string.share_content1) + BAConstants.REDIRECT_URL));

			// 对参数编码
			String param = URLEncodedUtils.format(params, "UTF-8");

			String httpurl = "https://api.weibo.com/2/statuses/update.json?" + param;
			HttpGet httpRequest = new HttpGet(httpurl);
			HttpClient httpclient = new DefaultHttpClient();
			try {
				// 请求httpClient ，取得HttpRestponse
				HttpResponse httpResponse = httpclient.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// 取得相关信息 取得HttpEntiy
					HttpEntity httpEntity = httpResponse.getEntity();

					String s = EntityUtils.toString(httpEntity, "utf-8");
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
}
