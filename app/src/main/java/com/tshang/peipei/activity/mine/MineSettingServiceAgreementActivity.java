package com.tshang.peipei.activity.mine;

import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;

/**
 * @Title: MineSettingServiceAgreementActivity.java 
 *
 * @Description: 用户协议
 *
 * @author allen  
 *
 * @date 2014-6-21 上午11:06:22 
 *
 * @version V1.0   
 */
public class MineSettingServiceAgreementActivity extends BaseActivity {

	@Override
	protected void initData() {

		findViewById(R.id.title_tv_left).setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.agreement);
	}

	@Override
	protected void initRecourse() {

	}

	@Override
	protected int initView() {
		return R.layout.activity_protocol;
	}

}
