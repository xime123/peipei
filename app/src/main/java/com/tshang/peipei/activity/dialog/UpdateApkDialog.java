package com.tshang.peipei.activity.dialog;

import android.app.Activity;

import com.tshang.peipei.base.BaseDownLoadApk;

/**
 * @Title: UpdateApkDialog.java 
 *
 * @Description: 升级提示
 *
 * @author allen  
 *
 * @date 2014-7-17 上午10:38:50 
 *
 * @version V1.0   
 */
public class UpdateApkDialog extends BaseNormalDialog {

	private String url;

	public UpdateApkDialog(Activity context, int title, int sure, int cancel, String url, String titleName) {
		super(context, title, sure, cancel, titleName);
		this.url = url;
	}

	@Override
	public void OnclickSure() {
		new BaseDownLoadApk(context, url,false).start();
	}

}
