package com.tshang.peipei.vender.micode.soundrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RecorderReceiver extends BroadcastReceiver {

	private Recorder mRecorder;

	public RecorderReceiver(Recorder mRecorcer) {
		this.mRecorder = mRecorcer;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_STATE)) {
			boolean isRecording = intent.getBooleanExtra(
					RecorderService.RECORDER_SERVICE_BROADCAST_STATE, false);
			mRecorder.setState(isRecording
					? Recorder.RECORDING_STATE
					: Recorder.IDLE_STATE);
		} else if (intent
				.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_ERROR)) {
			int error = intent.getIntExtra(
					RecorderService.RECORDER_SERVICE_BROADCAST_ERROR, 0);
			mRecorder.setError(error);
		}
	}

}
