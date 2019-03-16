package com.tshang.peipei.model.biz.chat;

import java.io.File;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: DownVideoListener.java 
 *
 * @Description:点击视频播放
 *
 * @author Jeff  
 *
 * @date 2014年9月1日 下午5:01:15 
 *
 * @version V1.0   
 */
public class DownVideoListener implements OnClickListener {
	private Activity activity;
	private ChatDatabaseEntity cdbe;
	private BAHandler mHandler;

	public DownVideoListener(Activity activity, ChatDatabaseEntity cdbe, BAHandler mHandler) {
		this.activity = activity;
		this.cdbe = cdbe;
		this.mHandler = mHandler;
	}

	@Override
	public void onClick(View v) {
		if (!SdCardUtils.isExistSdCard()) {
			BaseUtils.showTost(activity, R.string.nosdcard);
			return;
		}
		if (cdbe == null) {
			return;
		}
		if (cdbe.getStatus() == ChatStatus.SENDING.getValue()) {//发送状态中就不让继续点击
			return;
		}
		final String dir = SdCardUtils.getInstance().getVedioDirectory();
		if (TextUtils.isEmpty(dir)) {
			return;
		}
		final String fileName = cdbe.getMessage();
		File file = new File(dir, fileName + ".mp4");
		if (file != null && file.exists()) {//播放文件
			mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_DOWNLOAD_SUCCESS_VALUE, (int) cdbe.getMesLocalID(), 0, file));
		} else {//下载文件
			new Thread(new Runnable() {

				@Override
				public void run() {
					mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_DOWNLOAD_CLICK_VALUE, (int) cdbe.getMesLocalID(), 0));
					File dirFile = new File(dir);
					if (!dirFile.exists())
						dirFile.mkdirs();

					String path = "peipei";
					if (BAConstants.IS_TEST) {
						path = "peipeitest2";
					}
					try {
						BaiduCloudUtils.downloadFileFromCloud("/" + path + "/" + fileName, fileName + ".mp4");
						File file = new File(dir + "/" + fileName + ".mp4");
						if (file != null && file.exists()) {//播放文件
							mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_DOWNLOAD_SUCCESS_VALUE, (int) cdbe.getMesLocalID(),
									0, file));
						}
					} catch (Exception e) {
						e.printStackTrace();
						mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_DOWNLOAD_FAILED_VALUE, cdbe));
						try {
//							MobclickAgent.reportError(activity, "视频下载失败");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}).start();
		}
	}
}
