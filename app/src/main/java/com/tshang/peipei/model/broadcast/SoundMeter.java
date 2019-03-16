package com.tshang.peipei.model.broadcast;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class SoundMeter {
	static final private double EMA_FILTER = 0.6;

	private static final String BRAOD_CAST_VOICE_TEMP_DIR = "PeiPei/broadcast_temp";

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;

	public void start(String name) {
		if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(android.os.Environment.getExternalStorageDirectory() + "/" + BRAOD_CAST_VOICE_TEMP_DIR + "/" + name);
			try {
				mRecorder.prepare();
				mRecorder.start();

				mEMA = 0.0;
			} catch (IllegalStateException e) {
				System.out.print(e.getMessage());
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}

		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	public double getAmplitude() {//录音中出现的最大振幅
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
}
