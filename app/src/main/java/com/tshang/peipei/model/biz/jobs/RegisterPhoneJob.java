package com.tshang.peipei.model.biz.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

/*
 *类        名 : RegisterPhoneJob.java
 *功能描述 : 注册手机,得到一个auth
 *作　    者 : vactor
 *设计日期 :2014 2014-3-24 下午3:41:35
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class RegisterPhoneJob extends Job {

	private static final long serialVersionUID = 1L;
	private static final int PRIORITY = 1;

	protected RegisterPhoneJob(Params params) {
		super(new Params(PRIORITY).setRequiresNetwork(false));
	}

	@Override
	public void onAdded() {

	}

	@Override
	protected void onCancel() {

	}

	@Override
	public void onRun() throws Throwable {
	}

	@Override
	protected boolean shouldReRunOnThrowable(Throwable arg0) {
		return false;
	}

}
