package com.tshang.peipei.activity.dialog;

import java.util.HashMap;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.showrooms.RoomsGetBiz;

/**
 * @Title: FinshShowDialog.java 
 *
 * @Description: 关闭秀场 
 *
 * @author allen  
 *
 * @date 2015-2-2 上午11:15:49 
 *
 * @version V1.0   
 */
public class FinshShowDialog extends BaseNormalDialog {
	private RoomsGetBiz roomsGetBiz;

	public FinshShowDialog(Activity context, int title, int sure, int cancel, RoomsGetBiz roomsGetBiz) {
		super(context, title, sure, cancel);
		this.roomsGetBiz = roomsGetBiz;
		if (BAApplication.mLocalUserInfo != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//			MobclickAgent.onEvent(BAApplication.getInstance(), "dianjiguanbixiuchang", map);
		}
	}

	@Override
	public void OnclickSure() {
		if (BAApplication.mLocalUserInfo != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//			MobclickAgent.onEvent(BAApplication.getInstance(), "dianjiguanbixiuchangchenggong", map);
		}
		
		roomsGetBiz.finishShow(0);
	}
}
