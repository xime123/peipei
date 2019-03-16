package com.tshang.peipei.activity.mine;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;

/**
 * @Title: MineSettingAbout.java 
 *
 * @Description: 关于
 *
 * @author vactor
 *
 * @date 2014-4-29 上午11:00:29 
 *
 * @version V1.0   
 */
public class MineSettingAboutActivity extends BaseActivity {

	private TextView txtTitle;
	private TextView tvAppName;
	private TextView tvLastUpdateDate;
	private TextView tvServiceAgreement;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUi();
	}

	@SuppressLint("SimpleDateFormat")
	private void initUi() {
		txtTitle = (TextView) this.findViewById(R.id.title_tv_mid);
		txtTitle.setText(getString(R.string.setting_about) + getString(R.string.app_name));
		mBackText = (TextView) this.findViewById(R.id.title_tv_left);
		mBackText.setText(getString(R.string.setting));
		mBackText.setOnClickListener(this);
		tvAppName = (TextView) findViewById(R.id.setting_about_tv_appname);
		tvAppName.setText(getString(R.string.app_name) + BaseTools.getVersion(this));
		tvServiceAgreement = (TextView) findViewById(R.id.about_service_agreement);
		tvServiceAgreement.setOnClickListener(this);
		tvServiceAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvLastUpdateDate = (TextView) findViewById(R.id.setting_userinfo_right);
		try {
			tvLastUpdateDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(BaseTools.getAppUpdateTime(this))).replace("-", "."));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.about_service_agreement:
			BaseUtils.openActivity(this, MineSettingServiceAgreementActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {}

	@Override
	protected int initView() {
		return R.layout.activity_setting_about;
	}

}
