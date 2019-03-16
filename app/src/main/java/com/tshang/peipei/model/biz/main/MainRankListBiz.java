package com.tshang.peipei.model.biz.main;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestGetRankGroupList;
import com.tshang.peipei.model.request.RequestGetRankGroupList.IGetRankGroupList;
import com.tshang.peipei.model.request.RequestGetRankGroupListV2;
import com.tshang.peipei.model.request.RequestGetRankGroupListV2.IGetRankGroupListV2;
import com.tshang.peipei.model.request.RequestGetRankList;
import com.tshang.peipei.model.request.RequestGetRankList.IGetRankList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;

public class MainRankListBiz implements IGetRankGroupList {

	private IGetRankGroupList haremCallBack;

	//拉取排行
	public void getRanklist(Activity activity, int rankId, int start, int num, IGetRankList callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		int uid = 0;
		byte[] auth = "".getBytes();
		if (userEntity != null) {
			uid = userEntity.uid.intValue();
			auth = userEntity.auth;
		}
		RequestGetRankList req = new RequestGetRankList();
		req.getRanklist(auth, BAApplication.app_version_code, uid, rankId, start, num, callBack);

	}

	//拉取排行
	public void getHaremRanklist(Activity activity, int start, int num, IGetRankGroupList callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		int uid = 0;
		byte[] auth = "".getBytes();
		if (userEntity != null) {
			uid = userEntity.uid.intValue();
			auth = userEntity.auth;
		}
		RequestGetRankGroupList req = new RequestGetRankGroupList();
		this.haremCallBack = callBack;
		req.getRankGroupList(auth, BAApplication.app_version_code, uid, num, start, this);
	}

	//拉取排行
	public void getHaremRanklistV2(Activity activity, int start, int num, IGetRankGroupListV2 callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		int uid = 0;
		byte[] auth = "".getBytes();
		if (userEntity != null) {
			uid = userEntity.uid.intValue();
			auth = userEntity.auth;
		}
		RequestGetRankGroupListV2 req = new RequestGetRankGroupListV2();
		req.getRankGroupListV2(auth, BAApplication.app_version_code, uid, num, start, callBack);
	}

	@Override
	public void getGroupInfoList(int retCode, GroupInfoList infoList, int isEnd, int total) {
		if (null != haremCallBack) {
			haremCallBack.getGroupInfoList(retCode, infoList, isEnd, total);
		}
	}

}
