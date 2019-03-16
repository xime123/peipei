package com.tshang.peipei.activity.dialog;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.space.BlackListBiz;

/**
 * @Title: ClearBlackListDialog.java 
 *
 * @Description: 清空黑名单
 *
 * @author allen  
 *
 * @date 2014-10-28 下午1:48:43 
 *
 * @version V1.0   
 */
public class ClearBlackListDialog extends BaseNormalDialog {

	private int flag;
	private BAHandler mHandler;

	public ClearBlackListDialog(Activity context, int title, int sure, int cancel, int flag, BAHandler mHandler) {
		super(context, title, sure, cancel);
		this.flag = flag;
		this.mHandler = mHandler;
	}

	@Override
	public void OnclickSure() {
		BlackListBiz biz = new BlackListBiz();
		biz.removeBlackList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), flag,
				mHandler);
	}

}
