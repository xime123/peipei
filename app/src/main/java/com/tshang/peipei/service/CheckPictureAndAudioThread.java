package com.tshang.peipei.service;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.Context;

import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseTimes;

public class CheckPictureAndAudioThread extends Thread {

	private Context mContext;

	public CheckPictureAndAudioThread(Context context) {
		super();
		WeakReference<Context> aWeakRef = new WeakReference<Context>(context);
		mContext = aWeakRef.get();
	}

	public void run() {
		toDelete();
	}

	public void toDelete() {
		try {
			if (null != mContext) {
				File filePic = BaseFile.getStoregeDirectory("", mContext);
				deleteFiles(filePic);
				filePic = BaseFile.getExternalCacheDir(mContext);
				deleteFiles(filePic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteFiles(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {

			for (File file : files) {
				if (file.isDirectory()) {
					deleteFiles(file);
				}
				if (BaseTimes.isTimeDistanceNow(Long.valueOf(file.lastModified()), 24 * 60 * 60 * 1000)) {
					file.delete();
				}
			}
		}
	}
}
