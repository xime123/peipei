package com.tshang.peipei.model.biz.user;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackChangePwd;
import com.tshang.peipei.model.bizcallback.BizCallBackSetChatReshold;
import com.tshang.peipei.model.bizcallback.BizCallBackSwitchPush;
import com.tshang.peipei.model.bizcallback.BizCallBackUpdateUserInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackUploadHeadPic;
import com.tshang.peipei.model.request.RequestBindPhone;
import com.tshang.peipei.model.request.RequestBindPhone.iBindPhone;
import com.tshang.peipei.model.request.RequestChangePassword;
import com.tshang.peipei.model.request.RequestChangePassword.IChangePwd;
import com.tshang.peipei.model.request.RequestChangePhonePasswd;
import com.tshang.peipei.model.request.RequestChangePhonePasswd.IChangePwdByPhone;
import com.tshang.peipei.model.request.RequestGetMsgCode;
import com.tshang.peipei.model.request.RequestGetMsgCode.iGetMsgCode;
import com.tshang.peipei.model.request.RequestReSetPhoneAccountPasswd;
import com.tshang.peipei.model.request.RequestReSetPhoneAccountPasswd.iReSetPhoneAccountPasswd;
import com.tshang.peipei.model.request.RequestSetChatReshold;
import com.tshang.peipei.model.request.RequestSetChatReshold.ISetChatReshold;
import com.tshang.peipei.model.request.RequestSetUserBit;
import com.tshang.peipei.model.request.RequestSetUserBit.ISwitchPush;
import com.tshang.peipei.model.request.RequestUpdateNormalUserInfo;
import com.tshang.peipei.model.request.RequestUpdateNormalUserInfo.IUpdateUserInfo;
import com.tshang.peipei.model.request.RequestUploadHeadPic;
import com.tshang.peipei.model.request.RequestUploadHeadPic.IUploadHeadPic;

/**
 * @Title: UserSettingBiz.java 
 *
 * @Description: 设置 界面的一些逻辑
 *
 * @author vactor
 *
 * @date 2014-4-21 下午3:40:22 
 *
 * @version V1.0   
 */
public class UserSettingBiz implements IUploadHeadPic, ISwitchPush, IUpdateUserInfo, IChangePwd, ISetChatReshold {

	private BizCallBackUploadHeadPic bizCallBackUploadHeadPic;
	private BizCallBackSwitchPush bizCallBackSwitchPush;
	private BizCallBackUpdateUserInfo bizCallBackUpdateUserInfo;
	private BizCallBackChangePwd bizCallBackChangePwd;
	private BizCallBackSetChatReshold bicCallBackSetReshold;

	//上传头像
	public void uploadHeadPic(Activity activity, byte[] pic, int type, BizCallBackUploadHeadPic callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		BaseUtils.showDialog(activity, "正在上传");
		RequestUploadHeadPic req = new RequestUploadHeadPic();
		this.bizCallBackUploadHeadPic = callBack;
		req.uploadHeadPic(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), pic, type, this);
	}

	@Override
	public void uploadHeadPicCallBack(int retCode) {
		if (null != bizCallBackUploadHeadPic) {
			bizCallBackUploadHeadPic.uploadHeadPicCallBack(retCode);
		}
	}

	//各种消息开关
	public void setUserBit(byte[] auth, int ver, int uid, int acttion, int type, BizCallBackSwitchPush callBack) {
		RequestSetUserBit req = new RequestSetUserBit();
		this.bizCallBackSwitchPush = callBack;
		req.setUserBit(auth, ver, uid, acttion, type, this);
	}

	@Override
	public void switchPushCallBack(int retCode, int status) {
		if (null != bizCallBackSwitchPush) {
			bizCallBackSwitchPush.switchPushCallBack(retCode, status);
		}

	}

	//修改用户信息
	public void updateUserInfo(byte[] auth, int ver, int uid, String nick, int sex, long birthday, String email, String phone,
			BizCallBackUpdateUserInfo callBack) {
		RequestUpdateNormalUserInfo req = new RequestUpdateNormalUserInfo();
		this.bizCallBackUpdateUserInfo = callBack;
		req.updateUserInfo(auth, ver, uid, nick, sex, birthday, email, phone, this);

	}

	@Override
	public void updateUserInfoCallBack(int retCode) {
		if (null != bizCallBackUpdateUserInfo) {
			bizCallBackUpdateUserInfo.updateUserInfoCallBack(retCode);
		}
	}

	//修改密码
	public void changePassword(byte[] auth, int ver, int uid, String oldPwd, String newPwd, BizCallBackChangePwd callBack) {
		RequestChangePassword req = new RequestChangePassword();
		this.bizCallBackChangePwd = callBack;
		req.changePassword(auth, ver, uid, oldPwd, newPwd, this);
	}

	@Override
	public void changePwdCallBack(int retCode) {
		if (null != bizCallBackChangePwd) {
			bizCallBackChangePwd.changePwdCallBack(retCode);
		}
	}

	//修改密码
	public void changePasswordByPhone(byte[] auth, int ver, int uid, String oldPwd, String newPwd, IChangePwdByPhone callBack) {
		RequestChangePhonePasswd req = new RequestChangePhonePasswd();
		req.changePassword(auth, ver, uid, oldPwd, newPwd, callBack);
	}

	//设置私聊门槛
	public void setChatReshold(byte[] auth, int ver, int uid, int value, BizCallBackSetChatReshold callBack) {
		RequestSetChatReshold request = new RequestSetChatReshold();
		this.bicCallBackSetReshold = callBack;
		request.setChatReshold(auth, ver, uid, value, this);
	}

	@Override
	public void setChatResholdCallBack(int retCode) {
		if (null != bicCallBackSetReshold) {
			bicCallBackSetReshold.setChatResholdCallBack(retCode);
		}
	}

	public void getMsgCode(byte[] auth, int ver, int uid, String phone, String piccode, iGetMsgCode callback) {
		RequestGetMsgCode req = new RequestGetMsgCode();
		req.getMsgCode(auth, ver, uid, phone, piccode, callback);
	}

	public void bindPhone(byte[] auth, int ver, int uid, String phone, String code, String passwd, iBindPhone callback) {
		RequestBindPhone req = new RequestBindPhone();
		req.bindPhone(auth, ver, uid, phone, code, passwd, callback);
	}

	public void setPwdByPhone(byte[] auth, int ver, String phoneno, String msgcode, String phonePwd, iReSetPhoneAccountPasswd callback) {
		RequestReSetPhoneAccountPasswd req = new RequestReSetPhoneAccountPasswd();
		req.reSetPhoneAccountPasswd(auth, ver, phoneno, msgcode, phonePwd, callback);
	}

}
