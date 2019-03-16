package com.tshang.peipei.base;

import android.util.Log;

public class BaseLog {
	/**
	 * app日志标签
	 */
	public static final String ISTAG = "PeiPei";

	/**
	 * 打印日志控制开关
	 */
	private static boolean isISLog = true;

	static boolean VERBOSE = true;

	static boolean DEBUG = true;

	static boolean INFO = true;

	static boolean WARN = true;

	static boolean ERROR = true;

	public static void setLogAtt(boolean result) {
		isISLog = result;
		VERBOSE = VERBOSE && isISLog;
		DEBUG = DEBUG && isISLog;
		INFO = INFO && isISLog;
		WARN = WARN && isISLog;
		ERROR = ERROR && isISLog;
	}

	public static void v(String tag, String msg, Throwable th) {
		if (VERBOSE && isISLog)
			Log.v(tag, msg, th);
	}

	public static void d(String tag, String msg, Throwable th) {
		if (DEBUG && isISLog)
			Log.d(tag, msg, th);
	}

	public static void i(String tag, String msg, Throwable th) {
		if (INFO && isISLog)
			Log.i(tag, msg, th);
	}

	public static void w(String tag, String msg, Throwable th) {
		if (WARN && isISLog)
			Log.w(tag, msg, th);
	}

	public static void e(String tag, String msg, Throwable th) {
		if (ERROR && isISLog)
			Log.e(tag, msg, th);
	}

	public static void v(String tag, String msg) {
		if (VERBOSE && isISLog)
			Log.v(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (DEBUG && isISLog)
			Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (INFO && isISLog)
			Log.i(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (WARN && isISLog)
			Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (ERROR && isISLog)
			Log.e(tag, msg);
	}

	public static void v(String msg) {
		if (VERBOSE && isISLog)
			Log.v(BaseLog.ISTAG, msg);
	}

	public static void d(String msg) {
		if (DEBUG && isISLog)
			Log.d(BaseLog.ISTAG, msg);
	}

	public static void i(String msg) {
		if (INFO && isISLog)
			Log.i(BaseLog.ISTAG, msg);
	}

	public static void w(String msg) {
		if (WARN && isISLog)
			Log.w(BaseLog.ISTAG, msg, null);
	}

	public static void e(String msg) {
		if (ERROR && isISLog)
			Log.e(BaseLog.ISTAG, msg, null);
	}
}
