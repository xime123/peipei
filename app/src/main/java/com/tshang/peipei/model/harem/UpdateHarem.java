package com.tshang.peipei.model.harem;

import android.app.Activity;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestUpdateGroupInfo;
import com.tshang.peipei.model.request.RequestUpdateGroupInfo.IUpdateGroupInfo;

/**
 * @Title: UpdateHarem.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014年9月18日 下午5:41:52 
 *
 * @version V1.0   
 */
public class UpdateHarem implements IUpdateGroupInfo {
	private BAHandler mHandler;

	private static UpdateHarem instance = null;

	public static UpdateHarem getInstance() {
		if (instance == null) {
			synchronized (UpdateHarem.class) {
				if (instance == null) {
					instance = new UpdateHarem();
				}
			}
		}
		return instance;
	}

	public void reqUpdateGroupInfo(Activity activity, byte[] pic, int groupid, String name, String notice, BAHandler mHandler) {
		RequestUpdateGroupInfo req = new RequestUpdateGroupInfo();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.mHandler = mHandler;
		BaseUtils.showDialog(activity, R.string.submitting);
		req.updateGroupInfo(info.auth, BAApplication.app_version_code, pic, groupid, name, notice, info.uid.intValue(), this);
	}

	@Override
	public void updateGroupInfo(int retCode, int groupId) {
		if (mHandler == null) {
			return;
		}
		if (retCode == 0) {//修改成功
			mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HAREM_UPDATE_GROUP_INFO_SUCCESS_VALUE, groupId));
		} else {//修改失败
			mHandler.sendEmptyMessage(HandlerValue.HAREM_UPDATE_GROUP_INFO_FAILED_VALUE);
		}
	}
}
