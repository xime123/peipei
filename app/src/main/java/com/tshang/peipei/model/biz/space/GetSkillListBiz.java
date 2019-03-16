package com.tshang.peipei.model.biz.space;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestGetSkillDealList;
import com.tshang.peipei.model.request.RequestGetSkillDealList.iGetSkillDealList;
import com.tshang.peipei.model.request.RequestGetSkillList;
import com.tshang.peipei.model.request.RequestGetSkillList.IGetSkillList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * 
 * @author Jeff
 *
 */
public class GetSkillListBiz {
	private Activity activity;

	public GetSkillListBiz(Activity activity) {
		this.activity = activity;
	}

	public void getSkillList(int uid, IGetSkillList callBack) {
		int selfuid = 0;
		byte[] auth = "".getBytes();
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity != null) {
			selfuid = userEntity.uid.intValue();
			auth = userEntity.auth;
		}
		new RequestGetSkillList().getSkillList(auth, BAApplication.app_version_code, 10, 0, selfuid, uid, callBack);
	}

	public void getSkillDealList(int start, int num, iGetSkillDealList callback) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity != null) {
			RequestGetSkillDealList req = new RequestGetSkillDealList();
			req.getSkillDealList(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), start, num, callback);
		}
	}
}
