package com.tshang.peipei.model.hall;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackGetOnlineUserList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetSkillList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserInfo;
import com.tshang.peipei.model.request.RequestGetAdvUrl;
import com.tshang.peipei.model.request.RequestGetAdvUrl.IGetAdv;
import com.tshang.peipei.model.request.RequestGetNearUserList;
import com.tshang.peipei.model.request.RequestGetNearUserList.IGetNearUserList;
import com.tshang.peipei.model.request.RequestGetOnLineUserList;
import com.tshang.peipei.model.request.RequestGetOnLineUserList.IGetOnlineUserList;
import com.tshang.peipei.model.request.RequestGetOnlineFreshUserList;
import com.tshang.peipei.model.request.RequestGetOnlineFreshUserList.iGetOnlineFresh;
import com.tshang.peipei.model.request.RequestGetRankSkillListV2;
import com.tshang.peipei.model.request.RequestGetRankSkillListV2.IMainSkillV2;
import com.tshang.peipei.model.request.RequestGetUserInfo;
import com.tshang.peipei.model.request.RequestGetUserInfo.IGetUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;

public class MainHallBiz implements IMainSkillV2, IGetOnlineUserList, IGetUserInfo {

	private BizCallBackGetSkillList mGetMainHallList;
	private BizCallBackGetOnlineUserList mGetOnlineUserList;
	//获取用户信息
	private BizCallBackGetUserInfo mGetUserInfoCallBack;
	private static MainHallBiz instance = null;

	public static MainHallBiz getInstance() {
		if (instance == null) {
			synchronized (MainHallBiz.class) {
				if (instance == null) {
					instance = new MainHallBiz();
				}
			}
		}
		return instance;
	}

	//最热
	public void getSkillList(Activity activity, int start, int num, int type, BizCallBackGetSkillList callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (userEntity != null) {
			auth = userEntity.auth;
			uid = userEntity.uid.intValue();
		}

		RequestGetRankSkillListV2 requestMainHot = new RequestGetRankSkillListV2();
		this.mGetMainHallList = callBack;
		requestMainHot.getHallSkillV2List(auth, BAApplication.app_version_code, uid, type, start, num, this);
	}

	@Override
	public void getMainSkillV2CallBack(int retCode, int isEnd, RetGGSkillInfoList userInfoList) {
		if (mGetMainHallList != null) {
			mGetMainHallList.getHallSkillListCallBack(retCode, isEnd, userInfoList);
		}
	}

	//最新在线
	public void getUserOnLineList(Activity activity, int sex, int start, int num, int dis, int la, int lo, BizCallBackGetOnlineUserList callBack) {
		RequestGetOnLineUserList req = new RequestGetOnLineUserList();
		this.mGetOnlineUserList = callBack;
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (userInfo != null) {
			auth = userInfo.auth;
			uid = userInfo.uid.intValue();
		}

		req.getUserOnLineList(auth, BAApplication.app_version_code, uid, sex, start, num, dis, la, lo, this);
	}

	//附近
	public void getUserNearList(Activity activity, int sex, int start, int num, int dis, int la, int lo, IGetNearUserList callBack) {
		RequestGetNearUserList req = new RequestGetNearUserList();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (userInfo != null) {
			auth = userInfo.auth;
			uid = userInfo.uid.intValue();
		}
		req.getUserNearList(auth, BAApplication.app_version_code, uid, sex, start, num, dis, la, lo, callBack);
	}

	@Override
	public void getOnlineUserListCallBack(int retCode, int isEnd, UserAndSkillInfoList list) {
		if (null != mGetOnlineUserList) {
			mGetOnlineUserList.getOnlineUserListCallBack(retCode, isEnd, list);
		}
	}

	//新人在线
	public void getFreshOnLineList(Activity activity, int sex, int start, int num, iGetOnlineFresh callBack) {
		RequestGetOnlineFreshUserList req = new RequestGetOnlineFreshUserList();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (userInfo != null) {
			auth = userInfo.auth;
			uid = userInfo.uid.intValue();
		}
		req.getOnlineFreshUserList(auth, BAApplication.app_version_code, uid, sex, start, num, callBack);
	}

	//获取用户信息
	public void getUserInfo(Activity activity, int uid, BizCallBackGetUserInfo callBack) {
		RequestGetUserInfo req = new RequestGetUserInfo();
		this.mGetUserInfoCallBack = callBack;
		req.getUserInfo("".getBytes(), BAApplication.app_version_code, uid, this);
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {
		mGetUserInfoCallBack.getUserInfoCallBack(retCode, userinfo);
	}

	public void getAdvUrl(Activity activity, IGetAdv callback) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}

		RequestGetAdvUrl req = new RequestGetAdvUrl();
		req.getAdvUrl("".getBytes(), BAApplication.app_version_code, info.uid.intValue(), callback);
	}

}
