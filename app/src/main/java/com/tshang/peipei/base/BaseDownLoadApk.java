package com.tshang.peipei.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;

/**
 * @Title: BaseDownLoadApk.java 
 *
 * @Description: 下载应用自升级 
 *
 * @author allen  
 *
 * @date 2014-4-29 下午2:46:33 
 *
 * @version V1.0   
 */
public class BaseDownLoadApk extends Thread {

	private static final int MSG_DOWNLOAD_SHOW_PROGRESS = 0;// 显示下载进度框
	private static final int MSG_DOWNLOAD_PROGRESS = 1;//显示下载进度
	private static final int MSG_DOWNLOAD_ERROR_INFO = 2;//错误提示

	private ProgressBar mDownloadProgress;
	private int mDownFileSize = 0;// 下载文件的进度
	private int mTotalFileSize = 0;// 下载文件的大小
	private BAHandler mHandler;
	private String mDownloadUrl;
	private Context mContext;
	private boolean execDownload = true;

	private Dialog mApkDLProgressDialog;
	private boolean isForce = false;

	public BaseDownLoadApk(Context context, String url, boolean isForce) {
		mContext = context;
		mDownloadUrl = url;
		this.isForce = isForce;

		mHandler = new BAHandler((FragmentActivity) context) {
			public void handleMessage(android.os.Message msg) {
				try {
					super.handleMessage(msg);
				} catch (Exception e) {
					return;
				}
				switch (msg.what) {
				case MSG_DOWNLOAD_SHOW_PROGRESS: {// 下载apk进度弹出框
					showDownLoadProgress();
				}
					break;
				case MSG_DOWNLOAD_PROGRESS: {// 下载apk进度显示
					mDownloadProgress.setProgress(mDownFileSize);
				}
					break;
				case MSG_DOWNLOAD_ERROR_INFO: {// 下载apk错误显示
					execDownload = false;
					BaseUtils.showTost(mContext, R.string.apk_download_error);
					closeDownloadDialog();
				}
					break;
				default:
					break;
				}
			};
		};
	}

	public void run() {
		execDownloadApk(mDownloadUrl);
	}

	private void execDownloadApk(String url) {
		String downloadPath = Environment.getExternalStorageDirectory().getPath() + "/" + BAConstants.PEIPEI_FILE;

		HttpGet httpGet = new HttpGet(url);
		DefaultHttpClient mHttpClient = null;
		HttpResponse mHttpResponse = null;

		try {
			File file = new File(downloadPath, "/PEIPEI.apk");
			if (!file.exists()) {
				file.createNewFile();
			}

			mHttpClient = new DefaultHttpClient();
			mHttpResponse = mHttpClient.execute(httpGet);

			if (mHttpResponse.getStatusLine().getStatusCode() == 200) {
				mTotalFileSize = (int) mHttpResponse.getEntity().getContentLength();

				InputStream is = mHttpResponse.getEntity().getContent();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new WeakReference<byte[]>(new byte[8192]).get();
				int count = 0;
				mHandler.sendEmptyMessage(MSG_DOWNLOAD_SHOW_PROGRESS);
				while ((count = is.read(buffer)) != -1 && execDownload) {
					fos.write(buffer, 0, count);
					mDownFileSize += count;
					mHandler.sendEmptyMessage(MSG_DOWNLOAD_PROGRESS);
				}

				fos.flush();

				fos.close();
				is.close();
				if (execDownload) {
					mApkDLProgressDialog.dismiss();
					BaseTools.installApk(file.getAbsolutePath(), mContext);
					//					if (!isForce) {
					//						mApkDLProgressDialog.dismiss();
					//					}
				}

			} else {
				mHandler.sendEmptyMessage(MSG_DOWNLOAD_ERROR_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mHandler.sendEmptyMessage(MSG_DOWNLOAD_ERROR_INFO);
		} finally {
			mHttpResponse = null;

			httpGet.abort();
			httpGet = null;
			mHttpClient = null;

		}
	}

	private void showDownLoadProgress() {
		mApkDLProgressDialog = crateDialogByDownLoad(mContext, null, 0);
		mDownloadProgress = (ProgressBar) mApkDLProgressDialog.findViewById(R.id.down_progress);
		mDownloadProgress.setProgress(0);
		mDownloadProgress.incrementProgressBy(1);
		mDownloadProgress.setMax(mTotalFileSize);
		Button btn_cancel = (Button) mApkDLProgressDialog.findViewById(R.id.download_cancel);
		if (isForce) {
			btn_cancel.setVisibility(View.GONE);
		}
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHandler.sendEmptyMessage(MSG_DOWNLOAD_ERROR_INFO);

				String downloadPath = Environment.getExternalStorageDirectory().getPath() + "/" + BAConstants.PEIPEI_FILE;

				File file = new File(downloadPath, "/Whisper.apk");
				if (file.exists()) {
					file.delete();
				}
			}
		});
		mApkDLProgressDialog.show();
	}

	private void closeDownloadDialog() {
		if (mApkDLProgressDialog != null) {
			mApkDLProgressDialog.dismiss();
			mApkDLProgressDialog = null;
		}
	}

	public Dialog crateDialogByDownLoad(final Context context, final View contentView, int theme) {
		final Dialog d = new Dialog(context, theme == 0 ? R.style.dialogStyle : theme);
		final Window w = d.getWindow();
		final WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.3f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		w.setAttributes(wlps);
//		w.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		d.setContentView(R.layout.dialog_download_view);

		if (isForce) {
			// 添加物理返回键对话框不消失
			OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
						return true;
					} else {
						return false;
					}
				}
			};
			d.setOnKeyListener(keylistener);
			// 点击对话框外不消失
			d.setCanceledOnTouchOutside(false);
		}

		return d;
	}
}
