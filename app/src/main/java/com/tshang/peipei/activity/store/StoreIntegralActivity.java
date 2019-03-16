package com.tshang.peipei.activity.store;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.BaseWebView;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.store.RecordBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetExchangeUrl;

/**
 * @Title: StoreIntegralActivity.java 
 *
 * @Description: 积分界面
 *
 * @author allen
 *
 * @date 2014-4-19 下午5:14:24 
 *
 * @version V1.0   
 */
@SuppressLint("SetJavaScriptEnabled")
public class StoreIntegralActivity extends BaseActivity implements BizCallBackGetExchangeUrl {

	private final static int GET_URL = 1;
	private final static int GET_URL_BACK = 2;

	private WebView mWebView;

	private BAHandler mHandler = new BAHandler(this) {
		public void handleMessage(android.os.Message msg) {
			try {
				super.handleMessage(msg);
			} catch (Exception e) {
				return;
			}

			switch (msg.what) {
			case GET_URL:
				GoGirlUserInfo userEntity = UserUtils.getUserEntity(StoreIntegralActivity.this);
				if (userEntity != null) {
					RecordBiz recordBiz = new RecordBiz();
					recordBiz.getExchangeUrl(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), StoreIntegralActivity.this);
				}
				break;
			case GET_URL_BACK:
				String url = (String) msg.obj;
				setWebView(url);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
		mHandler.sendEmptyMessage(GET_URL);
	}

	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mBackText.setText(R.string.mine);
		mBackText.setOnClickListener(this);
		mTitle.setText(R.string.integral_center);

		mWebView = (WebView) findViewById(R.id.faq_webview);

	}

	private void setWebView(String url) {
		BaseUtils.showDialog(this, R.string.loading);
		BaseWebView.setWebView(this, mWebView);
		mWebView.loadUrl(url);
	}

	@Override
	public void getUrl(int retCode, String url) {
		sendHandlerMessage(mHandler, GET_URL_BACK, retCode, url);

	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {}

	@Override
	protected int initView() {
		return R.layout.activity_faq;
	}

}
