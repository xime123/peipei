package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.os.Bundle;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.mine.MineSettingUserInfoActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;

/**
 * @Title: NeedUploadHeadDialog.java 
 *
 * @Description: 没有头像提示
 *
 * @author allen  
 *
 * @date 2014-9-12 下午6:05:31 
 *
 * @version V1.0   
 */
public class NeedUploadHeadDialog extends BaseNormalDialog {

	public NeedUploadHeadDialog(Activity context, int title, int sure, int cancel) {
		super(context, title, sure, cancel);
	}

	@Override
	public void OnclickSure() {
		if (BAApplication.mLocalUserInfo != null) {
			Bundle bundle = new Bundle();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, BAApplication.mLocalUserInfo.uid.intValue());
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, BAApplication.mLocalUserInfo.sex.intValue());
			BaseUtils.openActivity(context, MineSettingUserInfoActivity.class, bundle);
		}
	}

}
