package com.tshang.peipei.activity.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: UploadingIdentifyActivity.java 
 *
 * @Description: 认证审核中
 *
 * @author allen  
 *
 * @date 2014-9-12 下午3:27:02 
 *
 * @version V1.0   
 */
public class UploadingIdentifyActivity extends BaseActivity {

	private TextView identityText;
	private ImageView identityImage;
	private ImageView identifyIv;
	private boolean isIdentify;

	@Override
	protected void initData() {
		Bundle b = getIntent().getExtras();
		isIdentify = b.getBoolean("isIdentify");
		String authKey = b.getString("authKey");

		if (!TextUtils.isEmpty(authKey)) {
			DisplayImageOptions options = ImageOptionsUtils.GetHeadKeyBigRounded(this);
			imageLoader.displayImage("http://" + authKey + BAConstants.LOAD_210_IDENTITY, identityImage, options);
		}
		identityImage.setOnClickListener(this);

		if (isIdentify) {//通过认证
			identityText.setText(R.string.indentify_uploading_content2);
			identifyIv.setBackgroundResource(R.drawable.homepage_identifed);
		} else {
			identityText.setText(R.string.indentify_uploading_content);
			identifyIv.setBackgroundResource(R.drawable.homepage_identifing);
		}
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.identify_head);

		identityText = (TextView) findViewById(R.id.identify_uploading_text);
		identityImage = (ImageView) findViewById(R.id.identify_uploading_imageview);
		identifyIv = (ImageView) findViewById(R.id.identify_uploading_iv);
	}

	@Override
	protected int initView() {
		return R.layout.activity_head_identifed_uploading;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.identify_uploading_imageview:
			if (!isIdentify) {//未通过认证
				BaseUtils.openActivity(this, UploadIdentifyActivity.class);
				finish();
			}
			break;

		default:
			break;
		}
	}
}
