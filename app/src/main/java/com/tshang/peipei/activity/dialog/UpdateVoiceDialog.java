package com.tshang.peipei.activity.dialog;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Build;
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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseHttpUtils;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.broadcast.BroadcastReceiverMgr;
import com.tshang.peipei.vender.common.util.FileUtils;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;
import com.tshang.peipei.vender.micode.soundrecorder.RecorderService;
import com.tshang.peipei.vender.micode.soundrecorder.RemainingTimeCalculator;
import com.tshang.peipei.vender.micode.soundrecorder.SoundRecorderPreferenceActivity;
import com.tshang.peipei.view.TasksCompletedView;

/**
 * @Title: UpdateVoiceDialog.java 
 *
 * @Description: webview语音
 *
 * @author allen  
 *
 * @date 2014-12-6 下午4:01:05 
 *
 * @version V1.0   
 */
public class UpdateVoiceDialog extends Dialog implements OnClickListener, OnDismissListener {

	private static final String WEBVIEW_FILE_TEMP_NAME = "webview_voice_temp";
	public static final int BITRATE_3GPP = 20 * 1024 * 8; // bits/sec
	private static final String FILE_EXTENSION_3GPP = ".3gpp";
	private static final String FILE_EXTENSION_AMR = ".amr";
	private static final int BROADCAST_VOICE_MAX_LEN_TIME = 90;
	private int currentVoiceTimeLen = BROADCAST_VOICE_MAX_LEN_TIME;//当前录制的时间时长
	private boolean canSendVoiceBroadcast = false;

	//语音
	public static final int RECORD_INIT_STATUS = 0;//初始状态
	public static final int RECORD_START_STATUS = 1;//录音
	public static final int RECORD_PLAY_STATUS = 2;//播放
	public static final int RECORD_PAUSE_STATUS = 3;//暂停
	public static final int RECORD_STOP_STATUS = 4;//停止
	public static final int RECORD_DOWNLOAD_STATUS = 5;//下载
	private int record_status = RECORD_INIT_STATUS;

	private Activity mContext;

	private BAHandler mHandler;

	protected Recorder mVoiceRecod;
	protected RecorderReceiver mReceiver;
	private RemainingTimeCalculator mRemainingTimeCalculator;
	private BroadcastReceiverMgr mBroadcastReceiver;
	private Button btn_rerecording;
	private TasksCompletedView task_voice_progress;
	private TextView tv_voice_time;
	private Button btn_update;

	private WebView mWebView;
	private String act;
	private String content;

	int timeLen = 0;
	private Runnable mSleepTask = new Runnable() {//更新录语音的时间
		public void run() {
			timeLen++;
			mHandler.sendEmptyMessage(HandlerValue.BROADCAST_TIME_LENGTH_VALUE);
			mHandler.postDelayed(mSleepTask, 1000);
		}
	};

	public UpdateVoiceDialog(Activity context, WebView webView, String act, String content) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;
		this.mWebView = webView;
		this.act = act;
		this.content = content;
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
				case HandlerValue.BROADCAST_VOIDE_LOAD_COMPLETE_PLAY_VALUE://语音下载完了，可以开始听了
					String filePath = msg.obj.toString();
					mVoiceRecod.setShow(false);
					mVoiceRecod.startPlayback(filePath, -1);
					record_status = RECORD_PLAY_STATUS;
					break;
				case HandlerValue.RESULT_UPDATE_BY_WEBVIEW:
					JSONObject jo = new JSONObject();
					try {
						jo.put("type", BAConstants.PP_VOICE);
						jo.put("voice", (String) msg.obj);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					mWebView.loadUrl("javascript:fromandroidrun(" + jo + ")");
					break;
				case HandlerType.STATE_END_PLAYING://播放完了
					mHandler.removeCallbacks(mSleepTask);
					task_voice_progress.setProgress(0);
					record_status = RECORD_STOP_STATUS;
					tv_voice_time.setText(0 + "/" + currentVoiceTimeLen + "\"");
					task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_start_selector);
					break;
				case HandlerValue.BROADCAST_TIME_LENGTH_VALUE://时长
					if (timeLen >= BROADCAST_VOICE_MAX_LEN_TIME) {
						canSendVoiceBroadcast = true;
						mHandler.removeCallbacks(mSleepTask);
						stopRecord();
						btn_rerecording.setVisibility(View.VISIBLE);
					} else {
						task_voice_progress.setProgress(timeLen);
					}
					tv_voice_time.setText(timeLen + "/" + currentVoiceTimeLen + "\"");
					break;
				case HandlerValue.BROADCAST_VOICE_CALL_STATE_RINGING_VALUE://来电话了如果是录音状态就废弃掉，如果是播放状态就暂停
					mHandler.removeCallbacks(mSleepTask);
					if (record_status == RECORD_START_STATUS) {
						restoreInitRecordStatus();
					}
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_sure:
			if (canSendVoiceBroadcast) {

				final File file = mVoiceRecod.sampleFile();
				byte[] voicedata = FileUtils.getBytesFromFile(file);

				if (voicedata == null || voicedata.length == 0) {
					BaseUtils.showTost(mContext, "录音失败，请检查sd卡是否正常");
					record_status = RECORD_INIT_STATUS;
					return;
				}

				new Thread() {
					public void run() {
						BaseHttpUtils.uploadFile(BAConstants.WEBVIEW_UPDATE_VOICE_URL, file.getAbsolutePath(), act,
								BAApplication.mLocalUserInfo.uid.intValue(), mHandler);
					}
				}.start();

				dismiss();
			} else {
				if (RECORD_INIT_STATUS == record_status) {
					BaseUtils.showTost(mContext, R.string.str_not_start_record);
				} else {
					BaseUtils.showTost(mContext, R.string.str_toast_is_recording);
				}
			}
			break;
		case R.id.btn_rerecording:
			btn_rerecording.setVisibility(View.GONE);
			btn_update.setBackgroundResource(R.drawable.popup_btn_up_ids);
			mVoiceRecod.stopPlayback();
			mHandler.removeCallbacks(mSleepTask);
			restoreInitRecordStatus();
			break;
		case R.id.task_voice_webview:
			if (record_status == RECORD_INIT_STATUS) {//初始状态就开始录音
				if (!Environment.getExternalStorageDirectory().exists()) {
					BaseUtils.showTost(mContext, "SD卡不存在");
					return;
				}
				btn_rerecording.setVisibility(View.GONE);
				btn_update.setBackgroundResource(R.drawable.popup_btn_up_ids);
				task_voice_progress.setmTotalProgress(BROADCAST_VOICE_MAX_LEN_TIME);//暂时设置最大可以录制60秒
				task_voice_progress.setProgress(timeLen);
				task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_stop_selector);
				mHandler.postDelayed(mSleepTask, 1000);
				startRecording(WEBVIEW_FILE_TEMP_NAME);
				record_status = RECORD_START_STATUS;
			} else if (record_status == RECORD_START_STATUS) {//在录音状态，点击就停止
				btn_rerecording.setVisibility(View.VISIBLE);
				stopRecord();
				if (timeLen < 3) {
					canSendVoiceBroadcast = false;
					restoreInitRecordStatus();
					btn_rerecording.setVisibility(View.GONE);
					btn_update.setBackgroundResource(R.drawable.popup_btn_up_ids);
					BaseUtils.showTost(mContext, "您录的时间太短了");
					task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_record_selector);
				} else {
					canSendVoiceBroadcast = true;
					task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_start_selector);
				}
				mHandler.removeCallbacks(mSleepTask);
				task_voice_progress.setProgress(0);
			} else if (record_status == RECORD_STOP_STATUS) {//停止状态点击就播放
				File file1 = mVoiceRecod.sampleFile();
				if (file1 == null) {
					return;
				}
				btn_rerecording.setVisibility(View.VISIBLE);
				btn_update.setBackgroundResource(R.drawable.popup_btn_up_selector);
				record_status = RECORD_PLAY_STATUS;
				mVoiceRecod.startPlayback(file1);
				task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_pause_selector);
				currentVoiceTimeLen = mVoiceRecod.getDuration() / 1000;
				timeLen = 0;
				mHandler.postDelayed(mSleepTask, 1000);
				task_voice_progress.setmTotalProgress(currentVoiceTimeLen);
				task_voice_progress.setProgress(timeLen);
			} else if (record_status == RECORD_PLAY_STATUS) {//播放状态点击就暂停
				btn_rerecording.setVisibility(View.VISIBLE);
				btn_update.setBackgroundResource(R.drawable.popup_btn_up_selector);
				record_status = RECORD_PAUSE_STATUS;
				mHandler.removeCallbacks(mSleepTask);
				mVoiceRecod.pausePlayback();
				task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_start_selector);
			} else if (record_status == RECORD_PAUSE_STATUS) {//暂停状态点击播放
				btn_rerecording.setVisibility(View.VISIBLE);
				btn_update.setBackgroundResource(R.drawable.popup_btn_up_selector);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mVoiceRecod = Recorder.getInstance();
		mVoiceRecod.setHandler(mContext, mHandler);
		mReceiver = new RecorderReceiver();
		mRemainingTimeCalculator = new RemainingTimeCalculator();

		IntentFilter filter = new IntentFilter();
		filter.addAction(RecorderService.RECORDER_SERVICE_BROADCAST_NAME);
		mContext.registerReceiver(mReceiver, filter);

		registerIt();

		try {
			setContentView(R.layout.dialog_updatevoice);

			btn_update = (Button) findViewById(R.id.ok_sure);
			btn_update.setOnClickListener(this);

			btn_rerecording = (Button) findViewById(R.id.btn_rerecording);
			btn_rerecording.setOnClickListener(this);
			task_voice_progress = (TasksCompletedView) findViewById(R.id.task_voice_webview);
			task_voice_progress.setRingColor(mContext.getResources().getColor(R.color.green));
			task_voice_progress.setOnClickListener(this);
			tv_voice_time = (TextView) findViewById(R.id.tv_voice_time);
			setCanceledOnTouchOutside(true);

			TextView tv_voice_content = (TextView) findViewById(R.id.tv_voice_content);
			if (TextUtils.isEmpty(content)) {
				tv_voice_content.setVisibility(View.GONE);
			} else {
				tv_voice_content.setVisibility(View.VISIBLE);
				tv_voice_content.setText(content);
			}

			TextView title = (TextView) findViewById(R.id.dialog_update_title);
			title.setText(R.string.update_voice);

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

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startRecording(String fileName) {
		mVoiceRecod.setShow(false);
		mRemainingTimeCalculator.reset();
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		} else if (!mRemainingTimeCalculator.diskSpaceAvailable()) {
		} else {
			stopAudioPlayback();
			boolean isHighQuality = SoundRecorderPreferenceActivity.isHighQuality(mContext);

			if (Build.MODEL.equals("HTC HD2")) {
				isHighQuality = false;
			}

			if (android.os.Build.VERSION.SDK_INT > 9) {
				mRemainingTimeCalculator.setBitRate(BAConstants.BITRATE_AMR);
				mVoiceRecod.startRecording(MediaRecorder.OutputFormat.AMR_WB, fileName, FILE_EXTENSION_AMR, isHighQuality, -1);
			} else {
				mRemainingTimeCalculator.setBitRate(BITRATE_3GPP);
				mVoiceRecod.startRecording(MediaRecorder.OutputFormat.THREE_GPP, fileName, FILE_EXTENSION_3GPP, isHighQuality, -1);
			}
		}
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

	/*
	* Make sure we're not recording music playing in the background, ask the
	* MediaPlaybackService to pause playback.
	*/
	private void stopAudioPlayback() {
		// Shamelessly copied from MediaPlaybackService.java, which
		// should be public, but isn't.
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "pause");
		mContext.sendBroadcast(i);
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
		btn_update.setBackgroundResource(R.drawable.popup_btn_up_selector);
	}

	private void restoreInitRecordStatus() {//恢复重新录语音
		record_status = RECORD_INIT_STATUS;
		timeLen = 0;
		currentVoiceTimeLen = BROADCAST_VOICE_MAX_LEN_TIME;
		task_voice_progress.setProgress(timeLen);
		tv_voice_time.setText(mContext.getString(R.string.str_click_recording));
		mVoiceRecod.stopRecording();
		task_voice_progress.setBackgroundResource(R.drawable.popup_music_voice_record_selector);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.dismiss();

		if (!mVoiceRecod.isShow())
			mVoiceRecod.stopPlayback();
		if (mBroadcastReceiver != null) {
			mContext.unregisterReceiver(mBroadcastReceiver);
		}
		if (mReceiver != null) {
			mContext.unregisterReceiver(mReceiver);
		}

		mHandler.removeCallbacks(mSleepTask);
	}

}
