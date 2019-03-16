package com.tshang.peipei.model.harem;

import android.app.Activity;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestApplyJoinGroup;
import com.tshang.peipei.model.request.RequestApplyJoinGroup.IApplyJoinGroup;
import com.tshang.peipei.model.request.RequestJoinGroup;
import com.tshang.peipei.model.request.RequestJoinGroup.IJoinGroup;
import com.tshang.peipei.model.request.RequestQuitGroup;
import com.tshang.peipei.model.request.RequestQuitGroup.IQuitGroup;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: JoinHarem.java 
 *
 * @Description: 申请加入或退出后宫
 *
 * @author Jeff
 *
 * @date 2014年9月18日 下午5:41:52 
 *
 * @version V1.3.0   
 */
public class JoinHarem implements IApplyJoinGroup, IJoinGroup, IQuitGroup {
	private BAHandler mHandler;

	private static JoinHarem instance = null;

	public static JoinHarem getInstance() {
		if (instance == null) {
			synchronized (JoinHarem.class) {
				if (instance == null) {
					instance = new JoinHarem();
				}
			}
		}
		return instance;
	}

	/**
	 * 请求加入后宫
	 * @author Administrator
	 *
	 * @param activity
	 * @param groupid
	 * @param mHandler
	 */
	public void reqJoinGroup(Activity activity, int groupid, BAHandler mHandler) {
		RequestApplyJoinGroup req = new RequestApplyJoinGroup();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.mHandler = mHandler;
		BaseUtils.showDialog(activity, R.string.str_requesting);
		req.applyJoinGroup(info.auth, BAApplication.app_version_code, info.uid.intValue(), groupid, "".getBytes(), "".getBytes(), this);
	}

	public void reqQuitGroup(Activity activity, int uid, int groupid, BAHandler mHandler, boolean isRequestQuit) {
		RequestQuitGroup req = new RequestQuitGroup();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.mHandler = mHandler;
		BaseUtils.showDialog(activity, "正在操作...");
		if (isRequestQuit) {
			uid = info.uid.intValue();
		}
		req.reqQuitGroup(info.auth, BAApplication.app_version_code, uid, info.uid.intValue(), groupid, this);
	}

	private ChatDatabaseEntity chatEntity;

	/**
	 * 同意加入后宫
	 * @author Administrator
	 *
	 * @param activity
	 * @param groupid
	 * @param mHandler
	 */
	public void agreeJoinGroup(Activity activity, int groupid, int uid, BAHandler mHandler, ChatDatabaseEntity chatEntity) {
		RequestJoinGroup req = new RequestJoinGroup();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.mHandler = mHandler;
		this.chatEntity = chatEntity;
		BaseUtils.showDialog(activity, R.string.str_requesting);
		req.rJoinGroup(info.auth, BAApplication.app_version_code, uid, info.uid.intValue(), groupid, "".getBytes(), "".getBytes(), this);
	}

	@Override
	public void applyJoinGroup(int retCode, int applytime) {//const int E_GG_GROUP_NUM= -28049; 已经加入过其他后宫
		if (mHandler == null) {
			return;
		}
		if (retCode == 0) {//申请加入成功
			mHandler.sendEmptyMessage(HandlerValue.HAREM_JOIN_GROUP_SUCCESS_VALUE);
		} else if (retCode == -28049) {//加入过其他的后宫
			mHandler.sendEmptyMessage(HandlerValue.HAREM_JOIN_GROUP_REPEAT_JOIN_VALUE);
		} else {//申请失败
			mHandler.sendEmptyMessage(HandlerValue.HAREM_JOIN_GROUP_FAILED_VALUE);
		}

	}

	@Override
	public void joinHarem(int retCode) {
		if (mHandler == null) {
			return;
		}
		if (retCode == 0) {//申请加入成功
			mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HAREM_AGREE_GROUP_SUCCESS_VALUE, 1000, 0, chatEntity));
		} else if (retCode == rspContMsgType.E_GG_IS_GROUP_MEMBER) {//已经是群成员
			mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HAREM_AGREE_GROUP_HAS_JOIN_VALUE, 1000, 0, chatEntity));
		} else if (retCode == rspContMsgType.E_GG_GROUP_MEMBER_NUM) {//群成员数量限制
			mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HAREM_AGREE_GROUP_ARRAIVE_LIMIT_VALLUE, 1000, 0, chatEntity));
		} else if (retCode == rspContMsgType.E_GG_GROUP_NUM) {//群成员数量限制
			mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HAREM_JOIN_GROUP_REPEAT_JOIN_VALUE, 1000, 0, chatEntity));
		} else {
			mHandler.sendEmptyMessage(HandlerValue.HAREM_AGREE_GROUP_FAILED_VALUE);
		}
	}

	@Override
	public void callbackQuitGroup(int retCode, int quitid) {
		if (mHandler == null) {
			return;
		}
		if (retCode == 0) {//申请退群成功
			mHandler.sendEmptyMessage(HandlerValue.HAREM_GROUP_QUIT_SUCCESS_VALUE);
		} else {//申请失败
			mHandler.sendEmptyMessage(HandlerValue.HAREM_GROUP_QUIT_FAILED_VALUE);
		}
	}
}
