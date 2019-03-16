package com.tshang.peipei.model.biz.store;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDaybookInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackDaybook;
import com.tshang.peipei.model.bizcallback.BizCallBackGetExchangeUrl;
import com.tshang.peipei.model.request.RequestDaybookRecord;
import com.tshang.peipei.model.request.RequestDaybookRecord.IDaybookRecord;
import com.tshang.peipei.model.request.RequestGetExchangeUrl;
import com.tshang.peipei.model.request.RequestGetExchangeUrl.IGetExchangeUrl;

/**
 * @Title: RecordBiz.java 
 *
 * @Description: 消费记录，积分记录逻辑类
 *
 * @author allen  
 *
 * @date 2014-4-24 上午11:41:55 
 *
 * @version V1.0   
 */
public class RecordBiz implements IDaybookRecord, IGetExchangeUrl {

	private BizCallBackDaybook dayBookCallBack;
	private BizCallBackGetExchangeUrl getExchangeUrl;

	/**
	 * 获取记录列表
	 *
	 * @param auth
	 * @param ver
	 * @param uid
	 * @param type
	 * @param start
	 * @param num
	 */
	public void getDayBookRecord(Activity activity, int type, int start, int num, BizCallBackDaybook callback) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestDaybookRecord req = new RequestDaybookRecord();
		dayBookCallBack = callback;
		req.getDaybookRecord(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), userEntity.uid.intValue(), type,
				start, num, this);
	}

	@Override
	public void getDaybook(int retCode, GoGirlDaybookInfoList list, int total) {
		if (dayBookCallBack != null) {
			dayBookCallBack.getDaybook(retCode, list, total);
		}
	}

	public void getExchangeUrl(byte[] auth, int ver, int uid, BizCallBackGetExchangeUrl callBack) {
		RequestGetExchangeUrl req = new RequestGetExchangeUrl();
		getExchangeUrl = callBack;
		req.getExchangeUrl(auth, ver, uid, this);
	}

	@Override
	public void getUrl(int retCode, String url) {
		if (getExchangeUrl != null) {
			getExchangeUrl.getUrl(retCode, url);
		}

	}
}
