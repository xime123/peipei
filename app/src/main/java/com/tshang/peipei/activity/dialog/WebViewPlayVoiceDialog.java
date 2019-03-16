package com.tshang.peipei.activity.dialog;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseHttpUtils;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.broadcast.BroadcastReceiverMgr;
import com.tshang.peipei.vender.common.util.FileUtils;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;
import com.tshang.peipei.vender.micode.soundrecorder.RecorderService;
import com.tshang.peipei.view.TasksCompletedView;

/**
 * @Title: WebViewPlayVoiceDialog.java 
 *
 * @Description: webview点击播放语音
 *
 * @author allen  
 *
 * @date 2014-12-8 下午2:23:47 
 *
 * @version V1.0   
 */
public class WebViewPlayVoiceDialog extends Dialog implements OnClickListener, OnDismissListener {

	//语音
	public static final int RECORD_INIT_STATUS = 0;//初始状态
	public static final int RECORD_START_STATUS = 1;//录音
	public static final int RECORD_PLAY_STATUS = 2;//播放
	public static final int RECORD_PAUSE_STATUS = 3;//暂停
	public static final int RECORD_STOP_STATUS = 4;//停止
	public static final int RECORD_DOWNLOAD_STATUS = 5;//下载
	private int record_status = RECORD_INIT_STATUS;

	private static final int BROADCAST_VOICE_MAX_LEN_TIME = 90;
	private int currentVoiceTimeLen = BROADCAST_VOICE_MAX_LEN_TIME;//当前录制的时间时长
	protected RecorderReceiver mReceiver;

	int timeLen = 0;
	private Runnable mSleepTask = new Runnable() {//更新录语音的时间
		public void run() {
			timeLen++;
			mHandler.sendEmptyMessage(HandlerValue.BROADCAST_TIME_LENGTH_VALUE);
			mHandler.postDelayed(mSleepTask, 1000);
		}
	};

	private String voice_url;
	private Activity mContext;

	private BAHandler mHandler;

	protected Recorder mVoiceRecod;
	private BroadcastReceiverMgr mBroadcastReceiver;
	private TasksCompletedView task_voice_progress;
	private TextView tv_voice_time;
	private ProgressBar pb_download;

	private String filePath;

	public WebViewPlayVoiceDialog(Activity context, String voiceurl) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;
		voice_url = voiceurl;

		mHandler = new BAHandler(mContext) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case HandlerType.VOICE_RECOD_OK:
					File file = mVoiceRecod.sampleFile();
					byte[] voicedata = FileUtils.getBytesFromFile(file);

					if (voicedata == null || voicedata.length == 0) {
						BaseUtils.showTost(mContext, "录音失败，请检查sd卡是否正常");
						record_status = RECORD_INIT_STATUS;
						return;
					}
					break;
				case HandlerType.VOICE_RECOD_RESULT:
					break;
				case HandlerValue.RESULT_DOWNLOAD_BY_WEBVIEW://语音下载完了，可以开始听了
					filePath = msg.obj.toString();
					record_status = RECORD_PLAY_STATUS;
					mVoiceRecod.setShow(false);
					mVoiceRecod.startPlayback(filePath, -1);
					task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_pause_selector);
					pb_download.setVisibility(View.GONE);
					currentVoiceTimeLen = mVoiceRecod.getDuration() / 1000;
					timeLen = 0;
					mHandler.postDelayed(mSleepTask, 1000);
					task_voice_progress.setmTotalProgress(currentVoiceTimeLen);
					task_voice_progress.setProgress(timeLen);
					break;
				case HandlerType.STATE_END_PLAYING://播放完了
					mHandler.removeCallbacks(mSleepTask);
					task_voice_progress.setProgress(0);
					record_status = RECORD_STOP_STATUS;
					tv_voice_time.setText(0 + "/" + currentVoiceTimeLen + "\"");
					task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_start_pr);
					break;
				case HandlerValue.BROADCAST_TIME_LENGTH_VALUE://时长
					if (timeLen >= BROADCAST_VOICE_MAX_LEN_TIME) {
						mHandler.removeCallbacks(mSleepTask);
						stopRecord();
					}
					task_voice_progress.setProgress(timeLen);
					tv_voice_time.setText(timeLen + "/" + currentVoiceTimeLen + "\"");
					break;
				case HandlerValue.BROADCAST_VOICE_CALL_STATE_RINGING_VALUE://来电话了如果是录音状态就废弃掉，如果是播放状态就暂停
					mHandler.removeCallbacks(mSleepTask);
					break;
				case HandlerValue.BROADCAST_VOICE_CALL_STATE_IDLE_VALUE://电话结束了，如果是播放状态就继续播放
					mHandler.postDelayed(mSleepTask, 1000);
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mVoiceRecod = Recorder.getInstance();
		mVoiceRecod.setHandler(mContext, mHandler);
		mReceiver = new RecorderReceiver();

		IntentFilter filter = new IntentFilter();
		filter.addAction(RecorderService.RECORDER_SERVICE_BROADCAST_NAME);
		mContext.registerReceiver(mReceiver, filter);

		registerIt();

		try {
			setContentView(R.layout.dialog_updatevoice);
			findViewById(R.id.ok_sure).setVisibility(View.GONE);

			task_voice_progress = (TasksCompletedView) findViewById(R.id.task_voice_webview);
			task_voice_progress.setRingColor(mContext.getResources().getColor(R.color.green));
			task_voice_progress.setOnClickListener(this);
			task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_start_pr);
			tv_voice_time = (TextView) findViewById(R.id.tv_voice_time);
			tv_voice_time.setText(R.string.click_download);

			pb_download = (ProgressBar) findViewById(R.id.task_voice_pb);

			TextView title = (TextView) findViewById(R.id.dialog_update_title);
			title.setText(R.string.play_voice);
			setCanceledOnTouchOutside(true);

			final Window w = getWindow();
			final WindowManager.LayoutParams wlps = w.getAttributes();
			wlps.width = mContext.getResources().getDisplayMetrics().widthPixels * 3 / 4;
			wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
			wlps.dimAmount = 0.6f;
			wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
			wlps.gravity = Gravity.CENTER;
			w.setAttributes(wlps);

			setOnDismissListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.task_voice_webview:
			if (record_status == RECORD_INIT_STATUS) {//初始状态下载
				if (!Environment.getExternalStorageDirectory().exists()) {
					BaseUtils.showTost(mContext, "SD卡不存在");
					return;
				}

				task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_load_bg);
				pb_download.setVisibility(View.VISIBLE);

				new Thread() {
					public void run() {
						Message msg = mHandler.obtainMessage();
						msg.what = HandlerValue.RESULT_DOWNLOAD_BY_WEBVIEW;
						String[] s1 = voice_url.split("/");
						File f = new File(SdCardUtils.getInstance().getDirectory(0));
						if (!f.exists()) {
							f.mkdirs();
						}
						File file = new File(SdCardUtils.getInstance().getDirectory(0), s1[s1.length - 1]);
						if (file.exists()) {
							msg.obj = file.getAbsolutePath();
						} else {
							msg.obj = BaseHttpUtils.downLoadFile(mContext, voice_url, s1[s1.length - 1]);
						}
						mHandler.sendMessageAtTime(msg, 10);
					}
				}.start();

				record_status = RECORD_START_STATUS;
			} else if (record_status == RECORD_START_STATUS) {//下载状态
				BaseUtils.showTost(mContext, R.string.loading);
			} else if (record_status == RECORD_STOP_STATUS) {//停止状态点击就播放
				if (TextUtils.isEmpty(filePath)) {
					return;
				}
				record_status = RECORD_PLAY_STATUS;
				mVoiceRecod.setShow(false);
				mVoiceRecod.startPlayback(filePath, -1);
				task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_pause_selector);
				currentVoiceTimeLen = mVoiceRecod.getDuration() / 1000;
				timeLen = 0;
				mHandler.postDelayed(mSleepTask, 1000);
				task_voice_progress.setmTotalProgress(currentVoiceTimeLen);
				task_voice_progress.setProgress(timeLen);
			} else if (record_status == RECORD_PLAY_STATUS) {//播放状态点击就暂停
				record_status = RECORD_PAUSE_STATUS;
				mHandler.removeCallbacks(mSleepTask);
				mVoiceRecod.pausePlayback();
				task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_start_selector);
			} else if (record_status == RECORD_PAUSE_STATUS) {//暂停状态点击播放
				record_status = RECORD_PLAY_STATUS;
				if (currentVoiceTimeLen > 0) {
					float timeProgress = ((float) timeLen) / currentVoiceTimeLen;
					mHandler.postDelayed(mSleepTask, 1000);
					mVoiceRecod.startPlayback(timeProgress);
				}
				task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_pause_selector);
			}
			break;
		default:
			break;
		}

	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registerIt() {//用于监听来电的广播
		mBroadcastReceiver = new BroadcastReceiverMgr(mHandler);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		intentFilter.setPriority(Integer.MAX_VALUE);
		mContext.registerReceiver(mBroadcastReceiver, intentFilter);
	}

	private void stopRecord() {
		mVoiceRecod.stop();
		record_status = RECORD_STOP_STATUS;
		task_voice_progress.setProgress(0);
		task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_start_selector);
	}

	private class RecorderReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_STATE)) {
				boolean isRecording = intent.getBooleanExtra(RecorderService.RECORDER_SERVICE_BROADCAST_STATE, false);
				mVoiceRecod.setState(isRecording ? Recorder.RECORDING_STATE : Recorder.IDLE_STATE);
			} else if (intent.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_ERROR)) {
				int error = intent.getIntExtra(RecorderService.RECORDER_SERVICE_BROADCAST_ERROR, 0);
				mVoiceRecod.setError(error);
			}
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		dismiss();
		
		if (!mVoiceRecod.isShow())
			mVoiceRecod.stopPlayback();
		if (mBroadcastReceiver != null) {
			mContext.unregisterReceiver(mBroadcastReceiver);
		}
		mHandler.removeCallbacks(mSleepTask);
	}

}
