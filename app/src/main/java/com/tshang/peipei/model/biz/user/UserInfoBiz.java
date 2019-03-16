package com.tshang.peipei.model.biz.user;

import android.app.Activity;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.store.StoreUserBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserProperty;
import com.tshang.peipei.model.request.RequestGetDistance;
import com.tshang.peipei.model.request.RequestGetRelasionship;
import com.tshang.peipei.model.request.RequestGetDistance.IGetDistance;
import com.tshang.peipei.model.request.RequestGetRelasionship.IGetRelationship;

/**
 * @Title: UserInfoBiz.java 
 *
 * @Description: TODO获取用户的数据
 *
 * @author Jeff 
 *
 * @date 2014年7月15日 下午7:39:31 
 *
 * @version V1.0   
 */
public class UserInfoBiz {
	private Activity activity;

	public UserInfoBiz(Activity activity) {
		this.activity = activity;

	}

	public void getRelationship(int otherUid, int isAttention, IGetRelationship mIGetRelationshi) {//获取用户的间的魅力贡献值
		GoGirlUserInfo userEntity = BAApplication.mLocalUserInfo;
		if (userEntity == null) {
			return;
		}
		if (isAttention == 1) {
			BaseUtils.showDialog(activity, R.string.str_check_relationsship);
		}
		RequestGetRelasionship req = new RequestGetRelasionship();
		req.getRelashionShip(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), otherUid, isAttention, mIGetRelationshi);
	}

	/**
	 * 获取用户的金币，银币，魅力值
	 * @author Administrator
	 *
	 * @param ver
	 * @param callback
	 */
	public void getStoreUser(Activity activity, BizCallBackGetUserProperty callback) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		BaseUtils.showDialog(activity, "正在获取您的财富");
		StoreUserBiz.getInstance().getUserProperty(activity, userEntity.uid.intValue(), callback);
	}

	public void getDistance(Activity activity, int peerUid, IGetDistance callback) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestGetDistance req = new RequestGetDistance();
		req.getDistance(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), peerUid, callback);

	}
}
