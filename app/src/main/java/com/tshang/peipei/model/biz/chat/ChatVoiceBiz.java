package com.tshang.peipei.model.biz.chat;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;
import com.tshang.peipei.vender.micode.soundrecorder.RemainingTimeCalculator;
import com.tshang.peipei.vender.micode.soundrecorder.SoundRecorderPreferenceActivity;

/**
 * @Title: 聊天界面录音相关逻辑类
 *
 * @Description: 处理聊天界面录音方法
 *
 * @author allen  
 *
 * @date 2014-3-28 下午2:55:16 
 *
 * @version V1.0   
 */
public class ChatVoiceBiz {
	private static final String AUDIO_3GPP = "audio/3gpp";

	private static final String AUDIO_AMR = "audio/amr";

	private static final String AUDIO_ANY = "audio/*";

	private static final String ANY_ANY = "*/*";

	private static final String FILE_EXTENSION_AMR = ".amr";

	private static final String FILE_EXTENSION_3GPP = ".3gpp";

	private static long mMaxFileSize = -1; // can be specified in the intent

	public static boolean initInternalState(Context context, Intent i, String mRequestedType) {
		mRequestedType = AUDIO_ANY;

		if (i != null) {
			String s = i.getType();
			if (AUDIO_AMR.equals(s) || AUDIO_3GPP.equals(s) || AUDIO_ANY.equals(s) || ANY_ANY.equals(s)) {
				mRequestedType = s;

			} else if (s != null) {
				// we only support amr and 3gpp formats right now
				return false;
			}

			final String EXTRA_MAX_BYTES = android.provider.MediaStore.Audio.Media.EXTRA_MAX_BYTES;
			mMaxFileSize = i.getLongExtra(EXTRA_MAX_BYTES, -1);
		}

		if (AUDIO_ANY.equals(mRequestedType)) {
			mRequestedType = SoundRecorderPreferenceActivity.getRecordType(context);
		} else if (ANY_ANY.equals(mRequestedType)) {
			mRequestedType = AUDIO_3GPP;
		}

		return true;
	}

	/**
	 * 保存音频文件
	 *
	 * @param name
	 */
	public static void saveVoiceByType(String name, String mRequestedType, RemainingTimeCalculator mRemainingTimeCalculator, Recorder mVoiceRecod) {
		boolean isHighQuality = true;

		if (AUDIO_AMR.equals(mRequestedType)) {
			//			System.out.println("声音11====" + mRequestedType);
			mRemainingTimeCalculator.setBitRate(BAConstants.BITRATE_AMR);
			int outputfileformat = isHighQuality ? MediaRecorder.OutputFormat.AMR_WB : MediaRecorder.OutputFormat.AMR_NB;
			mVoiceRecod.startRecording(outputfileformat, name, FILE_EXTENSION_AMR, isHighQuality, mMaxFileSize);
		} else if (AUDIO_3GPP.equals(mRequestedType)) {
			//			System.out.println("声音11====" + mRequestedType);
			// HACKME: for HD2, there is an issue with high quality 3gpp
			// use low quality instead
			if (Build.MODEL.equals("HTC HD2")) {
				isHighQuality = false;
			}

			mRemainingTimeCalculator.setBitRate(BAConstants.BITRATE_3GPP);
			mVoiceRecod.startRecording(MediaRecorder.OutputFormat.THREE_GPP, name, FILE_EXTENSION_3GPP, isHighQuality, mMaxFileSize);
		} else {
			//			System.out.println("声音11====异常");
			throw new IllegalArgumentException("Invalid output file type requested");
		}

		if (mMaxFileSize != -1) {
			mRemainingTimeCalculator.setFileSizeLimit(mVoiceRecod.sampleFile(), mMaxFileSize);
		}
	}

}
