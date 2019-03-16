package com.tshang.peipei.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @Title: BaseWebView.java 
 *
 * @Description: 设置webview
 *
 * @author allen  
 *
 * @date 2014-7-31 下午1:55:18 
 *
 * @version V1.0   
 */
public class BaseWebView {
	public static void setWebView(final Activity context, final WebView webView) {
		// 设置支持JavaScript脚本
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置不支持缩放
		webSettings.setBuiltInZoomControls(false);

		webSettings.setDatabaseEnabled(true);
		String dir = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setDatabasePath(dir);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		// 使用localStorage则必须打开
		webSettings.setDomStorageEnabled(true);
		webSettings.setAppCacheEnabled(true);

		webSettings.setGeolocationEnabled(true);

		if (Build.VERSION.SDK_INT >= 19) {
			webView.getSettings().setLoadsImagesAutomatically(true);
		} else {
			webView.getSettings().setLoadsImagesAutomatically(false);
		}
		// webSettings.setGeolocationDatabasePath(dir);

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if (!webView.getSettings().getLoadsImagesAutomatically()) {
					webView.getSettings().setLoadsImagesAutomatically(true);
				}
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
		});

		// 设置WebChromeClient
		webView.setWebChromeClient(new WebChromeClient() {
			// 处理javascript中的alert
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
				// 构建一个Builder来显示网页中的对话框
				Builder builder = new Builder(context);
				builder.setTitle("Alert");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}
				});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			};

			// 处理javascript中的confirm
			public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
				Builder builder = new Builder(context);
				builder.setTitle("confirm");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}
				});
				builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						result.cancel();
					}
				});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			};

			@Override
			// 设置网页加载的进度条
			public void onProgressChanged(WebView view, int newProgress) {
				context.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
				if (newProgress == 100) {
					BaseUtils.cancelDialog();
				}
				super.onProgressChanged(view, newProgress);
			}

			// 设置应用程序的标题title
			public void onReceivedTitle(WebView view, String title) {
				context.setTitle(title);
				super.onReceivedTitle(view, title);
			}

			public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota,
					WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(estimatedSize * 2);
			}

			public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(spaceNeeded * 2);
			}
		});
		// 覆盖默认后退按钮的作用，替换成WebView里的查看历史页面
		webView.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
						webView.goBack();
						return true;
					}
				}
				return false;
			}
		});
	}
}
