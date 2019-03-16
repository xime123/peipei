package com.tshang.peipei.model.biz.user;

import android.app.Activity;
import android.os.Message;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackUserExit;
import com.tshang.peipei.model.bizcallback.BizCallBackUserIsExist;
import com.tshang.peipei.model.bizcallback.BizCallBackUserLogin;
import com.tshang.peipei.model.bizcallback.BizCallBackUserRegister;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestGetUserInfo;
import com.tshang.peipei.model.request.RequestGetUserInfo.IGetUserInfo;
import com.tshang.peipei.model.request.RequestThirdLogin;
import com.tshang.peipei.model.request.RequestThirdLogin.IThirdLogin;
import com.tshang.peipei.model.request.RequestUpdateAlias;
import com.tshang.peipei.model.request.RequestUpdateAlias.iUpdateAlias;
import com.tshang.peipei.model.request.RequestUserExit;
import com.tshang.peipei.model.request.RequestUserExit.IUserExit;
import com.tshang.peipei.model.request.RequestUserIsExist;
import com.tshang.peipei.model.request.RequestUserIsExist.IUserIsExist;
import com.tshang.peipei.model.request.RequestUserLogin;
import com.tshang.peipei.model.request.RequestUserLogin.ILogin;
import com.tshang.peipei.model.request.RequestUserRegist;
import com.tshang.peipei.model.request.RequestUserRegist.IRegisterUser;
import com.tshang.peipei.model.request.RequestVerifyMsgCode;
import com.tshang.peipei.model.request.RequestVerifyMsgCode.iVerifyMsgCode;
import com.tshang.peipei.model.request.RequestVerifyUid;
import com.tshang.peipei.model.request.RequestVerifyUid.iVerifyUid;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

import de.greenrobot.event.EventBus;

/*
 *类        名 : UserAccountBiz.java
 *功能描述 : 用户信息相关接口具体实现
 *作　    者 : vactor
 *设计日期 :2014-3-20 下午5:55:35
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class UserAccountBiz implements IUserIsExist, ILogin, IRegisterUser, IThirdLogin, IUserExit, IGetUserInfo, iVerifyUid {

	// 用户注册回调
	private BizCallBackUserRegister mRegisterUserCallBack;
	// 用户登录回调
	private BizCallBackUserLogin mloginCallBack;
	// 用户是否存在回调
	private BizCallBackUserIsExist mUserExistBizCallBack;
	//退出
	private BizCallBackUserExit mUserExitCallBack;
	//获取用户信息
	private BizCallBackGetUserInfo mGetUserInfoCallBack;

	private BAHandler mHandler;

	private Activity mContext;

	public UserAccountBiz(Activity context) {
		this.mContext = context;
	}

	/**
	 * 用户是否存在
	 * @param auth
	 * @param ver
	 * @param name
	 * @param callBack
	 */
	public void isUserExist(byte[] auth, int ver, int type, String name, BizCallBackUserIsExist userExistBizCallBack) {
		RequestUserIsExist userExistImpl = new RequestUserIsExist();
		this.mUserExistBizCallBack = userExistBizCallBack;
		userExistImpl.isUserExist(auth, ver, type, name, this);
	}

	@Override
	public void isUserExistCallBack(int retCode, GoGirlUserInfo info) {
		if (null != mUserExistBizCallBack)
			mUserExistBizCallBack.isUserExist(retCode, info);
	}

	public void login(byte[] auth, int ver, String userAccount, String userPwd, String imei, BizCallBackUserLogin callBack) {
		RequestUserLogin userloginImpl = new RequestUserLogin();
		this.mloginCallBack = callBack;
		userloginImpl.login(auth, ver, userAccount, userPwd, imei, this);
	}

	//第三方登录
	public void loginThird(byte[] auth, int ver, String nick, int sex, int type, String userAccount, int invitate, String imei,
			BizCallBackUserLogin callBack) {
		RequestThirdLogin thirdLogin = new RequestThirdLogin();
		mloginCallBack = callBack;
		thirdLogin.login(auth, ver, nick, sex, type, userAccount, invitate, imei, this);
	}

	@Override
	public void loginCallBack(int retcode, String msg, GoGirlUserInfo userInfo, String userName) {
		if (null != mloginCallBack) {
			if (retcode == 0) {
				if (null != userInfo) {
					//存储用户信息
					UserSharePreference.getInstance(mContext).saveUserByKey(userInfo);
					//保存上一次用户登录名(用户退出登录不清)
					UserSharePreference.getInstance(mContext).saveStringKeyValue(userName, BAConstants.LOGIN_ID);

					//更新内存中的值
					BAApplication.mLocalUserInfo = userInfo;
					BAApplication.isShowPwd = true;
				}
			}
			mloginCallBack.loginCallBack(retcode, msg, userInfo);
		}
	}

	public void registerUser(byte[] auth, int ver, String userAccount, String userPwd, String msgcode, String userNick, int gender, long invitate,
			String imei, BizCallBackUserRegister callBack) {
		RequestUserRegist userRegisterImpl = new RequestUserRegist();
		this.mRegisterUserCallBack = callBack;
		userRegisterImpl.registerUser(auth, ver, userAccount, userPwd, msgcode, userNick, gender, invitate, imei, this);
	}

	@Override
	public void registerUserCallBack(int retCode, String errorMsg, GoGirlUserInfo userInfo) {
		if (null != mRegisterUserCallBack) {
			mRegisterUserCallBack.userRegister(retCode, errorMsg,userInfo);
			BAApplication.isShowPwd = true;
		}
	}

	@Override
	public void thirdLoginCallBack(int retcode, String errorMsg, GoGirlUserInfo userInfo) {
		if (null != mloginCallBack) {
			mloginCallBack.loginCallBack(retcode, errorMsg, userInfo);
			//存储用户信息(用户退出登录会清空)
			UserSharePreference.getInstance(mContext).saveUserByKey(userInfo);
			//			//保存上一次用户登录名(用户退出登录不清)
			//			UserSharePreference.getInstance(mContext).saveStringKeyValue(BAConstants.LOGIN_ID, userInfo.getUsername());
			//更新内存中的值
			BAApplication.mLocalUserInfo = userInfo;
			BAApplication.isShowPwd = false;
		}
	}

	//退出
	public void userExit(byte[] auth, int ver, int uid, BizCallBackUserExit callback) {
		RequestUserExit req = new RequestUserExit();
		mUserExitCallBack = callback;
		req.exit(auth, ver, uid, this);
	}

	@Override
	public void userExitCallBack(int resultCode) {
		if (mUserExitCallBack != null) {
			mUserExitCallBack.exit(resultCode);
		}

	}

	//获取用户信息
	public void getUserInfo(BizCallBackGetUserInfo callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(mContext);
		if (userEntity == null) {
			return;
		}
		RequestGetUserInfo req = new RequestGetUserInfo();
		this.mGetUserInfoCallBack = callBack;
		req.getUserInfo(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), this);
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {
		if (retCode == 0) {

			UserSharePreference.getInstance(mContext).saveUserByKey(userinfo);
			//更新内存中的值
			BAApplication.mLocalUserInfo = userinfo;

			NoticeEvent event = new NoticeEvent();
			event.setFlag(NoticeEvent.NOTICE18);
			EventBus.getDefault().postSticky(event);

			mGetUserInfoCallBack.getUserInfoCallBack(retCode, userinfo);
		}
	}

	public void getVerifyUid(byte[] auth, int ver, int uid, int sex, BAHandler handler) {
		RequestVerifyUid req = new RequestVerifyUid();
		this.mHandler = handler;
		req.getVerifyUid(auth, ver, uid, sex, this);
	}

	@Override
	public void resultVerifyUid(int retCode, int verifyUid) {
		if (mHandler != null) {
			Message msg = mHandler.obtainMessage();
			msg.what = HandlerValue.ACCOUNT_VERIFY_RESULT;
			msg.arg1 = retCode;
			msg.arg2 = verifyUid;
			mHandler.sendMessage(msg);
		}

	}

	public void getVerifyMsgCode(byte[] auth, int ver, int uid, String code, String phone, iVerifyMsgCode callback) {
		RequestVerifyMsgCode req = new RequestVerifyMsgCode();
		req.getVerifyMsgCode(auth, ver, uid, code, phone, callback);
	}

	public void updateAlias(byte[] auth, int ver, int uid, int followuid, int type, String alias, iUpdateAlias callback) {
		RequestUpdateAlias req = new RequestUpdateAlias();
		req.updateAlias(auth, ver, uid, followuid, type, alias, callback);
	}
}
