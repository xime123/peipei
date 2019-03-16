package com.tshang.peipei.activity.dialog;

import android.app.Activity;

import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;

/**
 * @Title: CleanDynamicDialog.java 
 *
 * @Description: 清空动态
 *
 * @author allen  
 *
 * @date 2014-12-4 下午4:46:52 
 *
 * @version V1.0   
 */
public class CleanDynamicDialog extends BaseNormalDialog {

	private BAHandler mHandler;

	public CleanDynamicDialog(Activity context, int title, int sure, int cancel, BAHandler mHandler) {
		super(context, title, sure, cancel);
		this.mHandler = mHandler;
	}

	@Override
	public void OnclickSure() {
		mHandler.sendEmptyMessage(HandlerValue.CLEAN_DYNAMIC_VALUE);
		dismiss();
	}
}
