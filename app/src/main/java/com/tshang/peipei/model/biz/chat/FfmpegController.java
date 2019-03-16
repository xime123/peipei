package com.tshang.peipei.model.biz.chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseLog;

public class FfmpegController {

	private String mFfmpegBin;

	private final static String TAG = "FFMPEG";

	public FfmpegController(Context context) throws FileNotFoundException, IOException {
		installBinaries(context, false);
	}

	public void installBinaries(Context context, boolean overwrite) {
		mFfmpegBin = installBinary(context, R.raw.ffmpeg, "ffmpeg", overwrite);
	}

	public String getBinaryPath() {
		return mFfmpegBin;
	}

	private static String installBinary(Context ctx, int resId, String filename, boolean upgrade) {
		try {
			File f = new File(ctx.getDir("peipei_bin", 0), filename);
			if (f.exists()) {
				f.delete();
			}
			copyRawFile(ctx, resId, f, "0755");
			return f.getCanonicalPath();
		} catch (Exception e) {
			BaseLog.e(TAG, "installBinary failed: " + e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * Copies a raw resource file, given its ID to the given location
	 * @param ctx context
	 * @param resid resource id
	 * @param file destination file
	 * @param mode file permissions (E.g.: "755")
	 * @throws IOException on error
	 * @throws InterruptedException when interrupted
	 */
	private static void copyRawFile(Context ctx, int resid, File file, String mode) throws IOException, InterruptedException {
		final String abspath = file.getAbsolutePath();
		// Write the iptables binary
		final FileOutputStream out = new FileOutputStream(file);
		final InputStream is = ctx.getResources().openRawResource(resid);
		byte buf[] = new byte[1024];
		int len;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		is.close();
		// Change the permissions
		Runtime.getRuntime().exec("chmod " + mode + " " + abspath).waitFor();
	}

}
