package com.tshang.peipei.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;

/**
 * 
 * @ClassName: ILog
 * 
 * @Description: 日志工具类
 * 
 * @Author Aaron
 * 
 * @CreateDate 2015-3-30 下午4:32:27
 * 
 * @Version V1.0
 */
public class ILog {
	/**
	 * 用于自定义TAG
	 */
	public static String LOG_TAG = "tag";
	/**
	 * 日志前缀
	 */
	public static String LOG_PRE = " <||> ";
	/**
	 * 安全级别日志,true:不输出或保存日志，false:可输出或保存日志
	 */
	public static boolean IS_SECURITY_LOG = false;
	/**
	 * 是否输出Log的位置，true:输出；false:不输出
	 */
	public static boolean IS_LOG_POSITION = false;
	/**
	 * 日志分隔字符
	 */
	private static final String LOG_SPLIT = "  \t<========>  ";

	/**
	 * 输出调试Log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void d(String tag, String msg) {
		if (IS_SECURITY_LOG) {
			return;
		}
		tag = LOG_TAG == null ? tag : LOG_TAG;
		String logMsg = (msg == null ? "" : msg);

		if (IS_LOG_POSITION) {
			logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
		}
		Log.d(LOG_PRE + tag, logMsg);

		saveLog(tag, logMsg, "D");
	}

	/**
	 * 输出浏览级别Log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void v(String tag, String msg) {
		if (IS_SECURITY_LOG) {
			return;
		}
		tag = LOG_TAG == null ? tag : LOG_TAG;
		String logMsg = (msg == null ? "" : msg);

		if (IS_LOG_POSITION) {
			logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
		}
		Log.v(LOG_PRE + tag, logMsg);

		saveLog(tag, logMsg, "V");
	}

	/**
	 * 输出警告级别Log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void w(String tag, String msg) {
		if (IS_SECURITY_LOG) {
			return;
		}
		tag = LOG_TAG == null ? tag : LOG_TAG;
		String logMsg = (msg == null ? "" : msg);

		if (IS_LOG_POSITION) {
			logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
		}
		Log.w(LOG_PRE + tag, logMsg);

		saveLog(tag, logMsg, "W");
	}

	/**
	 * 输出错误级别Log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void e(String tag, String msg) {
		if (IS_SECURITY_LOG) {
			return;
		}
		tag = LOG_TAG == null ? tag : LOG_TAG;
		String logMsg = (msg == null ? "" : msg);

		if (IS_LOG_POSITION) {
			logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
		}
		Log.e(LOG_PRE + tag, logMsg);

		saveLog(tag, logMsg, "E");
	}

	/**
	 * 输出信息级别
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (IS_SECURITY_LOG) {
			return;
		}
		tag = LOG_TAG == null ? tag : LOG_TAG;
		String logMsg = (msg == null ? "" : msg);

		if (IS_LOG_POSITION) {
			logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
		}
		Log.i(LOG_PRE + tag, logMsg);

		saveLog(tag, logMsg, "I");
	}

	/**
	 * 获取Log信息
	 * 
	 * @return
	 */
	private static String getPositionInfo() {
		StackTraceElement element = new Throwable().getStackTrace()[2];
		return element.getFileName() + " ；Line " + element.getLineNumber() + " ；Method: " + element.getMethodName();
	}

	/**
	 * 保存日志 [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param tag
	 * @param msg
	 * @param priority
	 */
	private synchronized static void saveLog(String tag, String msg, String priority) {

		// 获取当前时间
		String curTime = ISystemTool.getDataTime("yyyy-MM-dd");
		// 打印到哪个文件
		File logFile = getLogFile(curTime);

		FileWriter printWriter = null;
		try {
			if (logFile != null && logFile.isFile()) {
				String logMessage = "" + curTime + " : " + priority + " / " + tag + LOG_SPLIT + msg + "\r\n";
				printWriter = new FileWriter(logFile, true);
				printWriter.append(logMessage);
				printWriter.flush();
			}
		} catch (FileNotFoundException e) {
			// Log当前的异常信息
			Log.e(LOG_TAG, e.toString());
		} catch (IOException e) {
			// Log当前的异常信息
			Log.e(LOG_TAG, e.toString());
		} finally {
			try {
				if (printWriter != null) {
					printWriter.close();
				}
			} catch (IOException e) {
				// Log当前的异常信息
				Log.e(LOG_TAG, e.toString());
			}
		}
	}

	/**
	 * 
	 * @Title: getLogFile
	 * 
	 * @Description: 获取打印日志文件
	 * 
	 * @param curTime
	 * @return
	 */
	private synchronized static File getLogFile(String curTime) {
		File file = null;
		if (IFileUtils.isExistSDCard()) {
			String logPath = IFileUtils.getSDCardRootDirectory() + File.separator + "peipei" + File.separator + "log" + File.separator;// 存储日记文件

			File pathFile = new File(logPath);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}

			file = new File(logPath, curTime + ".txt");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}
}
