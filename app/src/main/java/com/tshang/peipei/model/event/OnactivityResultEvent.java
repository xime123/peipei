package com.tshang.peipei.model.event;

import android.content.Intent;

/**
 * @Title: OnactivityResultEvent.java 
 *
 * @Description:  onactivityresult 无法在 fragment 中收到,使用event bus 传递,主要 是因为 调用了context.startactivityforesult
 *                写成了工具类,去掉context即可以在fragment中收到,但因为调用了 工具类的context.start,如果要修改,把工具类提到fragment中,单独使用即可
 *
 * @author vactor
 *
 * @date 2014-4-24 上午10:11:47 
 *
 * @version V1.0   
 */
public class OnactivityResultEvent {

	//femalefragment 中 使用startactivityforresult 无法接收,通过 activity 中的onactivityresult 发送 到fragment中接收
	public static final int NOTICE17 = 1;

	int flag;
	int requestCode;
	int resultCode;
	Intent data;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public Intent getData() {
		return data;
	}

	public void setData(Intent data) {
		this.data = data;
	}

}
