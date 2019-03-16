package com.tshang.peipei.activity.dialog;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.showrooms.RoomsGetBiz;

/**
 * @Title: OpenShowDialog.java 
 *
 * @Description: 开秀 
 *
 * @author allen  
 *
 * @date 2015-2-4 上午10:17:56 
 *
 * @version V1.0   
 */
public class OpenShowDialog extends BaseNormalDialog {

	private RoomsGetBiz roomsGetBiz;

	public OpenShowDialog(Activity context, int title, int sure, int cancel, RoomsGetBiz roomsGetBiz) {
		super(context, title, sure, cancel);
		this.roomsGetBiz = roomsGetBiz;
	}

	@Override
	public void OnclickSure() {
		BAApplication.clearShow();
		roomsGetBiz.openShow();
	}

}
