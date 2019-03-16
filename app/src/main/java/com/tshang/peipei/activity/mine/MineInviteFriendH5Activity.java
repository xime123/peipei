package com.tshang.peipei.activity.mine;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.ShareDialog;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.BaseWebView;
import com.tshang.peipei.base.babase.BAConstants;

/**
 * @Title: MineInviteFriendH5Activity.java 
 *
 * @Description: 邀请好友 
 *
 * @author allen  
 *
 * @date 2014-10-20 下午4:30:07 
 *
 * @version V1.0   
 */
public class MineInviteFriendH5Activity extends BaseActivity {

	private IWXAPI mWXapi;
	private Tencent mTencent;

	private WebView mWebView;

	@Override
	protected void initData() {
		if (mWXapi == null) {
			new Thread() {
				public void run() {
					reqToWX();
				}
			}.start();
		}

		BaseUtils.showDialog(this, R.string.loading);
		BaseWebView.setWebView(this, mWebView);
		mWebView.loadUrl(BAConstants.INVITE_URL + BAApplication.mLocalUserInfo.uid.intValue());
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.invite_friends);
		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mTextRight.setText(R.string.share);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setOnClickListener(this);
		mWebView = (WebView) findViewById(R.id.faq_webview);
	}

	@Override
	protected int initView() {
		return R.layout.activity_faq;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			new ShareDialog(this, android.R.style.Theme_Translucent_NoTitleBar, BAApplication.mLocalUserInfo.uid.intValue(), new String(
					BAApplication.mLocalUserInfo.nick), mHandler, mTencent, mWXapi).showDialog(0, 0);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mTencent != null)
			mTencent.onActivityResult(requestCode, resultCode, data);
	}

	private void reqToWX() {
		mWXapi = WXAPIFactory.createWXAPI(this, BAConstants.WXAPPID, true);
		mWXapi.registerApp(BAConstants.WXAPPID);
	}
}
