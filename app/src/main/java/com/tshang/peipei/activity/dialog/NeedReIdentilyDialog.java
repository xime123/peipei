package com.tshang.peipei.activity.dialog;

import android.app.Activity;

/**
 * @Title: NeedReIdentilyDialog.java 
 *
 * @Description: 重新审核 
 *
 * @author allen  
 *
 * @date 2014-9-26 下午6:55:44 
 *
 * @version V1.0   
 */
public class NeedReIdentilyDialog extends BaseNormalDialog {

	public NeedReIdentilyDialog(Activity context, int title, int sure, int cancel) {
		super(context, title, sure, cancel);
	}

	@Override
	public void OnclickSure() {
		new PhotoSetDialog(context, android.R.style.Theme_Translucent_NoTitleBar).showDialog(0, 0);
	}

}
