package com.tshang.peipei.activity.mine;

import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;

/**
 * @Title: CustomIdentyActivity.java 
 *
 * @Description: 客态查看认证界面
 *
 * @author allen  
 *
 * @date 2014-9-12 下午3:26:12 
 *
 * @version V1.0   
 */
public class CustomIdentifyActivity extends BaseActivity {

	@Override
	protected void initData() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.identify_head);

	}

	@Override
	protected void initRecourse() {}

	@Override
	protected int initView() {
		return R.layout.activity_head_identifed_custom;
	}

}
