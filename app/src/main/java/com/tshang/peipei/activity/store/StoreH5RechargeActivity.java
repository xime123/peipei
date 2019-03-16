package com.tshang.peipei.activity.store;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.utils.RSAHelper;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.BaseWebView;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.store.StoreUserBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetRechargeNo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRechargeNoV2;

/**
 * @Title: StoreH5RechargeActivity.java 
 *
 * @Description: h5界面充值
 *
 * @author allen  
 *
 * @date 2014-7-29 下午5:20:04 
 *
 * @version V1.0   
 */
public class StoreH5RechargeActivity extends BaseActivity implements BizCallBackGetRechargeNo {
	private WebView webView = null;

	final class PeipeiInJavaScript {
		@JavascriptInterface
		public void runPeiPeiJavaScript(final String str) {
			mHandler.post(new Runnable() {
				public void run() {
//					MobclickAgent.onEvent(StoreH5RechargeActivity.this, "DiANJiChongZhiAnNiuCiShu");
					try {
						JSONObject js = new JSONObject(str);
						if (js.has("rechargeId")) {
							int id = js.getInt("rechargeId");
							BigInteger rechargeId = BigInteger.valueOf(id);

							int anonymity = 0;
							if (js.has("anonymity")) {
								anonymity = js.getInt("anonymity");
							}

							if (BAApplication.mLocalUserInfo != null) {
								byte[] auth = BAApplication.mLocalUserInfo.auth;
								int uid = BAApplication.mLocalUserInfo.uid.intValue();
								StoreUserBiz.getInstance().reChargeToUser(auth, BAApplication.app_version_code, uid, rechargeId, anonymity,
										StoreH5RechargeActivity.this);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	@SuppressLint("JavascriptInterface")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (System.currentTimeMillis() - BAConstants.PEIPEI_AIBEI_INIT_TIME > 30 * 60 * 1000) {
			IAppPay.init(this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, BAConstants.APP_ID);
			BAConstants.PEIPEI_AIBEI_INIT_TIME = System.currentTimeMillis();
		}

		//		SDKApi.preGettingData(this);
		webView = (WebView) findViewById(R.id.faq_webview);

		// 把本类的一个实例添加到js的全局对象window中，
		// 这样就可以使用window.injs来调用它的方法
		webView.addJavascriptInterface(new PeipeiInJavaScript(), "peipeiinjs");

		BaseUtils.showDialog(this, R.string.loading);
		BaseWebView.setWebView(this, webView);

		GoGirlUserInfo mUserEntity = UserUtils.getUserEntity(this);
		if (mUserEntity != null) {
			if (BAConstants.IS_TEST) {
				webView.loadUrl("http://pptest.yidongmengxiang.com/recharge/index?uid=" + mUserEntity.uid.intValue());
			} else {
				webView.loadUrl(BAConstants.RECHARGE_URL + mUserEntity.uid.intValue());
			}
		}
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.mine);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_account);
		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setText(R.string.consumption_record);
		mTextRight.setOnClickListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_faq;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerType.USER_RECHARGE_GET_NO:
			try {
				BaseLog.i("aipay", "server result =" + msg.arg1);
				if (msg.arg1 == 0) {
					if (msg.obj instanceof RspGetRechargeNoV2) {
						BaseLog.i("aipay", "server result package is right");
						RspGetRechargeNoV2 rechargeNo = (RspGetRechargeNoV2) msg.obj;

						String param = genUrl(BAApplication.mLocalUserInfo.uid.intValue() + "", "", rechargeNo.waresid.intValue(),
								rechargeNo.rechargecny.intValue() / 100, new String(rechargeNo.rechargeno), new String(rechargeNo.notifyurl));

						IAppPay.startPay(this, param, new IPayResultCallback() {

							@Override
							public void onPayResult(int resultCode, String signvalue, String resultInfo) {
								switch (resultCode) {
								case IAppPay.PAY_SUCCESS:
									dealPaySuccess(signvalue);
									break;
								case IAppPay.PAY_ING:
									Toast.makeText(StoreH5RechargeActivity.this, "成功下单", Toast.LENGTH_LONG).show();
									break;
								default:
									dealPayError(resultCode, resultInfo);
									break;
								}
								Log.d("GoodsActivity", "requestCode:" + resultCode + ",signvalue:" + signvalue + ",resultInfo:" + resultInfo);
							}

							/*4.支付成功。
							 *  需要对应答返回的签名做验证，只有在签名验证通过的情况下，才是真正的支付成功
							 * 
							 * */
							private void dealPaySuccess(String signValue) {
								Log.i("ipay", "sign = " + signValue);
								if (TextUtils.isEmpty(signValue)) {
									/**
									 *  没有签名值
									 */
									Log.e("ipay", "pay success,but it's signValue is null");
									Toast.makeText(StoreH5RechargeActivity.this, "pay success, but sign value is null", Toast.LENGTH_LONG).show();
									return;
								}

								boolean isvalid = false;
								try {
									isvalid = signCpPaySuccessInfo(signValue);
								} catch (Exception e) {

									isvalid = false;
								}
								if (isvalid) {
									Toast.makeText(StoreH5RechargeActivity.this, "pay success", Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(StoreH5RechargeActivity.this, "pay success, sign error", Toast.LENGTH_LONG).show();
								}

							}

							/**
							 * valid cp callback sign
							 * @param signValue
							 * @return
							 * @throws Exception
							 * 
							 * transdata={"cporderid":"1","transid":"2","appid":"3","waresid":31,
							 * "feetype":4,"money":5,"count":6,"result":0,"transtype":0,
							 * "transtime":"2012-12-12 12:11:10","cpprivate":"7",
							 * "paytype":1}&sign=xxxxxx&signtype=RSA
							 */
							private boolean signCpPaySuccessInfo(String signValue) throws Exception {
								int transdataLast = signValue.indexOf("&sign=");
								String transdata = URLDecoder.decode(signValue.substring("transdata=".length(), transdataLast));

								int signLast = signValue.indexOf("&signtype=");
								String sign = URLDecoder.decode(signValue.substring(transdataLast + "&sign=".length(), signLast));

								String signtype = signValue.substring(signLast + "&signtype=".length());

								if (signtype.equals("RSA") && RSAHelper.verify(transdata, BAConstants.APP_PUBLIC_KEY, sign)) {

									return true;
								} else {
									Log.e("ipay", "wrong type ");
								}
								return false;
							}

							private void dealPayError(int resultCode, String resultInfo) {
								Log.e("ipay", "failure pay, callback cp errorinfo : " + resultCode + "," + resultInfo);
								Toast.makeText(
										StoreH5RechargeActivity.this,
										"payfail:[" + "resultCode:" + resultCode + ","
												+ (TextUtils.isEmpty(resultInfo) ? "unkown error" : resultInfo) + "]", Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			Intent intent = new Intent(this, ConsumptionRecordActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			break;

		default:
			break;
		}
	}

	@Override
	public void getRechargeNo(int retCode, RspGetRechargeNoV2 data) {
		sendHandlerMessage(mHandler, HandlerType.USER_RECHARGE_GET_NO, retCode, data);
	}

	/**
	 * 获取收银台参数
	 */
	private String genUrl(String appuserid, String cpprivateinfo, int waresid, double price, String cporderid, String notifyurl) {
		String json = "";

		JSONObject obj = new JSONObject();
		try {
			obj.put("appid", BAConstants.APP_ID);
			obj.put("waresid", waresid);
			obj.put("cporderid", cporderid);
			obj.put("appuserid", appuserid);
			obj.put("price", price);//单位 元
			obj.put("currency", "RMB");//如果做服务器下单 该字段必填
			obj.put("waresname", "金币");//开放价格名称(用户可自定义，如果不传以后台配置为准)

			/*CP私有信息，选填*/
			String cpprivateinfo0 = cpprivateinfo;
			if (!TextUtils.isEmpty(cpprivateinfo0)) {
				obj.put("cpprivateinfo", cpprivateinfo0);
			}

			/*支付成功的通知地址。选填。如果客户端不设置本参数，则使用服务端配置的地址。*/
			if (!TextUtils.isEmpty(notifyurl)) {
				obj.put("notifyurl", notifyurl);
			}
			json = obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = "";
		try {
			String cppk = BAConstants.APP_KEY;
			sign = RSAHelper.signForPKCS1(json, cppk);

		} catch (Exception e) {

		}

		return "transdata=" + URLEncoder.encode(json) + "&sign=" + URLEncoder.encode(sign) + "&signtype=" + "RSA";
	}
}