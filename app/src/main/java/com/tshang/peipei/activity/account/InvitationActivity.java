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
import android.widget.EditText;
import android.widget.TextView;

import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ShareType;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.PeiPeiAppStartBiz;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.space.SpaceCustomBiz;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.biz.user.UserRegisterBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackReportAppInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackUserLogin;
import com.tshang.peipei.model.bizcallback.BizCallBackUserRegister;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: CaptchaActivity
 *
 * @Description: 邀请码界面
 *
 * @author allen
 *
 * @version V1.0   
 */
public class InvitationActivity extends BaseActivity implements BizCallBackUserRegister, BizCallBackReportAppInfo, BizCallBackUserLogin {

	public static final String USERNAME = "username";
	public static final String USERPWD = "userpwd";
	public static final String USERNICK = "usernick";
	public static final String USERTYPE = "usertype";
	public static final String USERSEX = "usersex";
	public static final String USERCODE = "usercode";
	public static final int INVITATE = 1090;
	public static final int INVITATE_FAILED = 1091;

	public final int LOGIN = 0;

	private String username;
	private String userpwd;
	private String usernick;
	private int userThird;
	private EditText mInvitateText;
	private String userhead;
	private int usersex;
	private String code;

	private String loginErrorMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.captcha_btn_submit:
			int invate = 0;
			if (!TextUtils.isEmpty(mInvitateText.getText().toString().trim())) {
				try {
					invate = Integer.parseInt(mInvitateText.getText().toString().trim());
				} catch (NumberFormatException e) {
					invate = 0;
					e.printStackTrace();
				}
				UserAccountBiz uBiz = new UserAccountBiz(this);
				uBiz.getVerifyUid("".getBytes(), BAApplication.app_version_code, invate, usersex, mHandler);
			} else {
				submitAccount(0);
			}
			break;
		default:
			break;
		}
	}

	private void submitAccount(long invitate) {
		UserAccountBiz mUserBiz = new UserAccountBiz(this);
		String imei = BasePhone.getMobileImei(this);
		if (userThird == 0) {
			mUserBiz.registerUser("".getBytes(), BAApplication.app_version_code, username, userpwd, code, usernick, usersex, invitate, imei,
					InvitationActivity.this);
		} else {
			if (TextUtils.isEmpty(usernick)) {
				usernick = "";
			}
			if (usernick.length() > 8) {
				usernick = usernick.substring(0, 8);
			}
			mUserBiz.loginThird("".getBytes(), BAApplication.app_version_code, usernick, usersex, userThird, username, (int) invitate, imei,
					InvitationActivity.this);
		}
	}

	@Override
	public void reportAppInfoBack(int retCode) {}

	@Override
	public void userRegister(int retCode, String errorMsg, GoGirlUserInfo userInfo) {
		sendHandlerMessage(mHandler, LOGIN, retCode, userInfo);
	}

	@Override
	protected void initData() {
		username = getIntent().getStringExtra(USERNAME);
		userpwd = getIntent().getStringExtra(USERPWD);
		usernick = getIntent().getStringExtra(USERNICK);
		userThird = getIntent().getIntExtra(USERTYPE, 0);
		userhead = getIntent().getStringExtra("head");
		usersex = getIntent().getIntExtra(USERSEX, 0);
		code = getIntent().getStringExtra(USERCODE);
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.invite);

		findViewById(R.id.captcha_btn_submit).setOnClickListener(this);

		mInvitateText = (EditText) findViewById(R.id.captcha_et);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		int retCode = -1;
		switch (msg.what) {
		case LOGIN:
			retCode = msg.arg1;
			if (retCode == 0) {
				UserSharePreference.getInstance(this).saveUserByKey((GoGirlUserInfo) msg.obj);
				//更新内存中的值
				GoGirlUserInfo info = UserUtils.getUserEntity(this);

				PeiPeiAppStartBiz appStartBiz = new PeiPeiAppStartBiz();
				String token = SharedPreferencesTools.getInstance(InvitationActivity.this).getStringValueByKey(BAConstants.PEIPEI_BAIDU_USERID);

				if (info != null) {
					if (userThird == 0) {
						UserSharePreference.getInstance(InvitationActivity.this).saveStringKeyValue(new String(info.username), BAConstants.LOGIN_ID);
					}
					appStartBiz.reportAppInfoToSer(this, token, InvitationActivity.this);

					sendNoticeEvent(NoticeEvent.INOTICE_01);

					UserRegisterBiz uBiz = new UserRegisterBiz();
					uBiz.insertMessageToPeipei(InvitationActivity.this);

					//					if (userThird == 3) {
					//						wbThread.start();
					//					}

					if (!TextUtils.isEmpty(userhead) && !userhead.equals("true") && userThird != 0) {
						BaseUtils.showDialog(this, R.string.loading);
						imageLoader.loadImage("third://" + userhead, new ThirdLoadHeadListener(InvitationActivity.this, usersex));
					} else {
						Bundle b = new Bundle();
						b.putInt("sex", usersex);

						Intent intent = new Intent(InvitationActivity.this, UploadHeadActivity.class);
						if (b != null) {
							intent.putExtras(b);
						}

						startActivityForResult(intent, InvitationActivity.INVITATE);
						overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
					}
				}
			} else if (retCode == BAConstants.rspContMsgType.E_GG_BAN) {
				new HintToastDialog(this, R.string.tips_phone, R.string.ok).showDialog();
			} else if (retCode == BAConstants.rspContMsgType.E_GG_LOGIN_ONE) {
				if (!TextUtils.isEmpty(loginErrorMsg)) {
					DialogFactory.warnMsgDialog(this, getResources().getString(R.string.dialog_hint), loginErrorMsg,
							getResources().getString(R.string.dialog_confirm));
				}
			} else {
				BaseUtils.showTost(this, "登录失败");
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
	protected int initView() {
		return R.layout.activity_captcha;
	}

	@Override
	public void loginCallBack(int retcode, String msg, GoGirlUserInfo userInfo) {
		this.loginErrorMsg = msg;
		sendHandlerMessage(mHandler, LOGIN, retcode, userInfo);
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
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == InvitationActivity.INVITATE) {
			BaseUtils.cancelDialog();
			setResult(INVITATE);
			finish();
		} else if (arg1 == INVITATE_FAILED) {
			Bundle b = new Bundle();
			b.putInt("sex", usersex);

			Intent intent = new Intent(InvitationActivity.this, UploadHeadActivity.class);
			if (b != null) {
				intent.putExtras(b);
			}

			startActivityForResult(intent, InvitationActivity.INVITATE);
			overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
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
					//					mWeiboShareAPI.sendRequest(arg0, arg1, arg2, arg3, arg4)
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
