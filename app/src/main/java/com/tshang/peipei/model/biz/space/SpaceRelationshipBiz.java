package com.tshang.peipei.model.biz.space;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackAddFollow;
import com.tshang.peipei.model.bizcallback.BizCallBackDeletFollow;
import com.tshang.peipei.model.bizcallback.BizCallBackFeedBackKiss;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFansList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFollowList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetRelationship;
import com.tshang.peipei.model.request.RequestAddFollow;
import com.tshang.peipei.model.request.RequestAddFollow.IAddFollow;
import com.tshang.peipei.model.request.RequestDelFollow;
import com.tshang.peipei.model.request.RequestDelFollow.IDeleteFollow;
import com.tshang.peipei.model.request.RequestDelFollowByUid;
import com.tshang.peipei.model.request.RequestDelFollowByUid.IDeleteFollowByUid;
import com.tshang.peipei.model.request.RequestFeedbackKiss;
import com.tshang.peipei.model.request.RequestFeedbackKiss.iFeedBack;
import com.tshang.peipei.model.request.RequestGetFansList;
import com.tshang.peipei.model.request.RequestGetFansList.IGetFansList;
import com.tshang.peipei.model.request.RequestGetFollowList;
import com.tshang.peipei.model.request.RequestGetFollowList.IGetFollowList;
import com.tshang.peipei.model.request.RequestGetRelasionship;
import com.tshang.peipei.model.request.RequestGetRelasionship.IGetRelationship;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RelationshipInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfoList;

/**
 * @Title: SpaceGetRelationshipBiz.java 
 *
 * @Description: 关注
 *
 * @author vactor 
 *
 * @date 2014-5-4 上午10:22:59 
 *
 * @version V1.0   
 */
public class SpaceRelationshipBiz implements IGetRelationship, IAddFollow, IGetFollowList, IDeleteFollow, IGetFansList, iFeedBack {

	//是否关注
	private BizCallBackGetRelationship bizCallBackGetRelationship;

	//加关注
	private BizCallBackAddFollow bizCallBackAddFollow;

	//获取关注列表
	private BizCallBackGetFollowList bizCallBackGetFollowList;

	//取消关注
	private BizCallBackDeletFollow bizCallBackDeleteFollow;
	//获取粉丝
	private BizCallBackGetFansList mGetFansList;
	//回赠飞吻
	private BizCallBackFeedBackKiss mFeedBack;
	private Activity activity;

	public SpaceRelationshipBiz(Activity activity) {
		this.activity = activity;
	}

	public void getRelashionShip(byte[] auth, int ver, int uid, int uid2, BizCallBackGetRelationship callBack) {
		RequestGetRelasionship request = new RequestGetRelasionship();
		this.bizCallBackGetRelationship = callBack;
		request.getRelashionShip(auth, ver, uid, uid2, 1, this);
	}

	@Override
	public void getRelationshipCallBack(int retCode, int attention, RelationshipInfo relation) {
		if (null != bizCallBackGetRelationship) {
			bizCallBackGetRelationship.getRelationshipCallBack(retCode, relation);
		}
	}

	public void addFollow(int uid2, BizCallBackAddFollow callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestAddFollow req = new RequestAddFollow();
		this.bizCallBackAddFollow = callBack;
		req.addFollow(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), uid2, this);
	}

	@Override
	public void addFollowCallBack(int retCode, int followid) {
		if (null != bizCallBackAddFollow) {
			bizCallBackAddFollow.addFollowCallBack(retCode);
		}
	}

	public void getfollowList(byte[] auth, int ver, int uid, int start, int num, BizCallBackGetFollowList callBack) {
		RequestGetFollowList req = new RequestGetFollowList();
		this.bizCallBackGetFollowList = callBack;
		req.getfollowList(auth, ver, uid, start, num, this);
	}

	@Override
	public void getFollowListCallBack(int retCode, RetFollowInfoList list, int isend) {

		if (null != bizCallBackGetFollowList) {
			bizCallBackGetFollowList.getFollowListCallBack(retCode, list, isend);
		}
	}

	public void delFollow(byte[] auth, int ver, int followId, int uid, BizCallBackDeletFollow callBack) {
		RequestDelFollow req = new RequestDelFollow();
		this.bizCallBackDeleteFollow = callBack;
		req.delFollow(auth, ver, followId, uid, this);
	}

	@Override
	public void deleteFollowCallBack(int retCode, int followId) {
		if (null != bizCallBackDeleteFollow) {
			bizCallBackDeleteFollow.deleteFollowCallBack(retCode, followId);
		}
	}

	public void delFollowByUid(byte[] auth, int ver, int followUid, int uid, IDeleteFollowByUid callBack) {
		RequestDelFollowByUid req = new RequestDelFollowByUid();
		req.delFollow(auth, ver, followUid, uid, callBack);
	}

	/**
	 * 获取粉丝列表
	 *
	 */
	public void getFansList(byte[] auth, int ver, int uid, int start, int num, BizCallBackGetFansList callBack) {
		RequestGetFansList req = new RequestGetFansList();
		mGetFansList = callBack;
		req.getFans(auth, ver, uid, start, num, this);
	}

	@Override
	public void getFansList(int retCode, RetFollowInfoList list, int isend) {
		if (mGetFansList != null) {
			mGetFansList.getFansList(retCode, list, isend);
		}
	}

	public void feedbackKiss(byte[] auth, int ver, int uid, int fuid, int dealid, BizCallBackFeedBackKiss callback) {
		mFeedBack = callback;
		RequestFeedbackKiss req = new RequestFeedbackKiss();
		req.feedbackKiss(auth, ver, uid, fuid, dealid, this);
	}

	@Override
	public void feedback(int resultCode) {
		if (mFeedBack != null) {
			mFeedBack.feedBackKiss(resultCode);
		}
	}

}
