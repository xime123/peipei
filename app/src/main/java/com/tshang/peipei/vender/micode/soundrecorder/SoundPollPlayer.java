package com.tshang.peipei.vender.micode.soundrecorder;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

import com.tshang.peipei.R;

/*
 *类        名 : 
 *功能描述 : 消息提示声音播放类
 *作　    者 :
 *设计日期 :2013-12-30 下午2:45:26
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class SoundPollPlayer {

	private int resourceId;
	private static SoundPollPlayer soundPlayer;

	public static SoundPollPlayer getInstance(Context mContext) {
		if (soundPlayer == null) {
			soundPlayer = new SoundPollPlayer();
		}
		return soundPlayer;
	}


	private SoundPollPlayer() {
		
	}


	public void play(Context mContext) {
		try {
			SoundPool soundPoll = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			resourceId = soundPoll.load(mContext, R.raw.notice, 1);
			Log.i("vactor_log", "sound play ready");
			soundPoll.setOnLoadCompleteListener(new OnLoadCompleteListener() {

				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					Log.i("vactor_log", "sound play");
					soundPool.play(resourceId, 0.5f, 0.5f, 5, 0, 1);
				}
			});

		} catch (Exception e) {
			Log.i("vactor_log", "sound play exception");
			e.printStackTrace();
		}
	}


	// private int resourceId;
	// private static SoundPollPlayer soundPlayer;
	//
	// public static SoundPollPlayer getInstance(Context mContext) {
	// if (soundPlayer == null) {
	// soundPlayer = new SoundPollPlayer(mContext);
	// }
	// return soundPlayer;
	// }
	//
	// private SoundPollPlayer(Context mContext) {
	//
	// }
	//
	// public void play(final Context mContext) {
	// Thread streamThread = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// final SoundPool soundPoll = new SoundPool(1,
	// AudioManager.STREAM_MUSIC, 0);
	// resourceId = soundPoll.load(mContext, R.raw.notice, 1);
	// soundPoll
	// .setOnLoadCompleteListener(new OnLoadCompleteListener() {
	// @Override
	// public void onLoadComplete(SoundPool soundPool,
	// int sampleId, int status) {
	// Log.i("vactor_log", "play......");
	// soundPool.play(resourceId, 100, 100, 0, 0, 1f);
	// }
	// });
	// }
	// });
	// streamThread.start();
	// }

}
