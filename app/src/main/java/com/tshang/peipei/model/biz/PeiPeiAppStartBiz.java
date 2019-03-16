package com.tshang.peipei.model.biz;

import android.app.Activity;
import android.content.Context;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackGetLastestAppInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackReportAppInfo;
import com.tshang.peipei.model.request.RequestGetAppConf;
import com.tshang.peipei.model.request.RequestGetAppConf.IGetAppConf;
import com.tshang.peipei.model.request.RequestGetAppConfV2;
import com.tshang.peipei.model.request.RequestGetAppConfV2.IGetAppConfV2;
import com.tshang.peipei.model.request.RequestGetLatestAppInfo;
import com.tshang.peipei.model.request.RequestGetLatestAppInfo.ILatestAppInfo;
import com.tshang.peipei.model.request.RequestGetSmileConf;
import com.tshang.peipei.model.request.RequestReportAppInfo;
import com.tshang.peipei.model.request.RequestReportAppInfo.IReportAppInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetLatestAppInfo;

/**
 * @Title: PeiPeiAppStartBiz.java 
 *
 * @Description: 软件启动时的操作（检测升级,上报信息）
 *
 * @author allen 
 *
 * @date 2014-4-29 上午11:48:57 
 *
 * @version V1.0   
 */
public class PeiPeiAppStartBiz implements ILatestAppInfo, IReportAppInfo {

	private BizCallBackGetLastestAppInfo mCallBack;
	private BizCallBackReportAppInfo mReportAppInfo;

	public void checkAppInfo(Activity activity, BizCallBackGetLastestAppInfo callback) {
		byte[] auth = "".getBytes();
		int uid = 0;
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity != null) {
			auth = userEntity.auth;
			uid = userEntity.uid.intValue();
		}
		RequestGetLatestAppInfo req = new RequestGetLatestAppInfo();
		mCallBack = callback;
		req.getAppInfo(auth, BAApplication.app_version_code, uid, this);
	}

	@Override
	public void getLatestAppInfo(int retCode, RspGetLatestAppInfo rsp) {
		if (mCallBack != null) {
			mCallBack.checkLatestAppInfo(retCode, rsp);
		}

	}

	public void reportAppInfoToSer(Context activity, String token, BizCallBackReportAppInfo report) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestReportAppInfo req = new RequestReportAppInfo();
		mReportAppInfo = report;
		req.reportInfo(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), token, this);
	}

	@Override
	public void reportAppInfo(int retCode) {
		if (mReportAppInfo != null) {
			mReportAppInfo.reportAppInfoBack(retCode);
		}

	}

	public void getAppConf(byte[] auth, int ver, int uid, IGetAppConf callback) {
		RequestGetAppConf req = new RequestGetAppConf();
		req.getAppConf(auth, ver, uid, callback);
	}

	/**
	 * 拉取配制文件
	 * @author Aaron
	 *
	 * @param auth
	 * @param ver
	 * @param uid
	 * @param channel
	 * @param callback
	 */
	public void getAppConfV2(byte[] auth, int ver, int uid, String channel, IGetAppConfV2 callback) {
		RequestGetAppConfV2 req = new RequestGetAppConfV2();
		req.getAppConf(auth, ver, uid, channel, callback);
	}

	public void getSmileConf(byte[] auth, int confver, int uid) {
		RequestGetSmileConf req = new RequestGetSmileConf();
		req.getSmileConf(auth, BAApplication.app_version_code, confver, uid, 0);
	}
}
