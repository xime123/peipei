package com.tshang.peipei.base;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 
 * @ClassName: ISystemTool
 * 
 * @Description: 系统工具类
 * 
 * @Author Aaron
 * 
 * @CreateDate 2015-4-3 下午2:15:47
 * 
 * @Version V1.0
 */
public class ISystemTool {
	/**
	 * 
	 * @Title: getDataTime
	 * 
	 * @Description: 指定格式返回当前系统时间
	 * 
	 * @param format
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDataTime(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}

	/**
	 * 获取手机当前时间
	 * @return
	 */
	public static String getSystemDataTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String strTime = formatter.format(curDate);
		return strTime;
	}

	/**
	 * 
	 * @Title: getDataTime
	 * 
	 * @Description: 返回当前系统时间(格式以HH:mm形式)
	 * 
	 * @return
	 */
	public static String getDataTime() {
		return getDataTime("HH:mm");
	}

	/**
	 * 
	 * @Title: getPhoneIMEI
	 * 
	 * @Description: 获取手机IMEI码
	 * 
	 * @param cxt
	 * @return
	 */
	public static String getPhoneIMEI(Context cxt) {
		TelephonyManager tm = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 
	 * @Title: getSDKVersion
	 * 
	 * @Description: 获取手机系统SDK版本
	 * 
	 * @return 如API 17 则返回 17
	 */
	@SuppressLint("InlinedApi")
	public static int getSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 
	 * @Title: getSystemVersion
	 * 
	 * @Description: 获取系统版本
	 * 
	 * @return 形如2.3.3
	 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 
	 * @Title: sendSMS
	 * 
	 * @Description: 调用系统发送短信
	 * 
	 * @param cxt
	 *            上下文对象
	 * @param smsBody
	 *            发送目标
	 */
	public static void sendSMS(Context cxt, String smsBody) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		cxt.startActivity(intent);
	}

	/**
	 * 
	 * @Title: checkNet
	 * 
	 * @Description: 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null;// 网络是否连接
	}

	/**
	 * 
	 * @Title: isNetworkAvailable
	 * 
	 * @Description: 判断手机是否联网
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: isWiFi
	 * 
	 * @Description: 判断是否为wifi联网
	 * 
	 * @param cxt
	 * @return
	 */
	public static boolean isWiFi(Context cxt) {
		ConnectivityManager cm = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		// wifi的状态：ConnectivityManager.TYPE_WIFI
		// 3G的状态：ConnectivityManager.TYPE_MOBILE
		State state = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		return State.CONNECTED == state;
	}

	/**
	 * 隐藏系统键盘
	 * 
	 * <br>
	 * <b>警告</b> 必须是确定键盘显示时才能调用
	 */
	/**
	 * 
	 * @Title: hideKeyBoard
	 * 
	 * @Description: 隐藏系统键盘 <br>
	 *               <b>警告</b> 必须是确定键盘显示时才能调用
	 * 
	 * @param aty
	 *            当前界面对象
	 */
	@SuppressLint("InlinedApi")
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public static void hideKeyBoard(Activity aty) {
		((InputMethodManager) aty.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(aty.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 
	 * @Title: isShowKeyBoard
	 * 
	 * @Description: 判断软键盘是否显示
	 * 
	 * @param aty
	 * @return
	 */
	public static boolean isShowKeyBoard(Activity aty) {
		InputMethodManager imm = (InputMethodManager) aty.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive();
	}

	/**
	 * 
	 * @Title: isBackground
	 * 
	 * @Description: 判断当前应用程序是否后台运行
	 * 
	 * @param context
	 *            上下文对象
	 * @return
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					// 后台运行
					return true;
				} else {
					// 前台运行
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: isSleeping
	 * 
	 * @Description: 判断手机是否处理睡眠
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSleeping(Context context) {
		KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
		return isSleeping;
	}

	/**
	 * 
	 * @Title: installApk
	 * 
	 * @Description: 安装apk
	 * 
	 * @param context
	 *            上下文
	 * @param file
	 *            　apk文件
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("application/vnd.android.package-archive");
		intent.setData(Uri.fromFile(file));
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 
	 * @Title: getAppVersionName
	 * 
	 * @Description: 获取当前应用程序的版本名
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		String version = "0";
		try {
			version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			throw new RuntimeException(ISystemTool.class.getName() + "the application not found");
		}
		return version;
	}

	/**
	 * 
	 * @Title: getAppVersionCode
	 * 
	 * @Description: 获取系统版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		int version = 0;
		try {
			version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			throw new RuntimeException(ISystemTool.class.getName() + "the application not found");
		}
		return version;
	}

	/**
	 * 
	 * @Title: goHome
	 * 
	 * @Description: 回到home，后台运行
	 * 
	 * @param context
	 */
	public static void goHome(Context context) {
		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
		mHomeIntent.addCategory(Intent.CATEGORY_HOME);
		mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(mHomeIntent);
	}

	/**
	 * 
	 * @Title: getSign
	 * 
	 * @Description: 获取应用签名
	 * 
	 * @param context
	 *            上下文
	 * @param pkgName
	 *            　应用包名
	 * @return　
	 */
	public static String getSign(Context context, String pkgName) {
		try {
			PackageInfo pis = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
			return hexdigest(pis.signatures[0].toByteArray());
		} catch (NameNotFoundException e) {
			throw new RuntimeException(ISystemTool.class.getName() + "the " + pkgName + "'s application not found");
		}
	}

	/**
	 * 
	 * @Title: hexdigest
	 * 
	 * @Description: 将签名字符串转换成需要的32位签名
	 * 
	 * @param paramArrayOfByte
	 * @return
	 */
	private static String hexdigest(byte[] paramArrayOfByte) {
		final char[] hexDigits = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramArrayOfByte);
			byte[] arrayOfByte = localMessageDigest.digest();
			char[] arrayOfChar = new char[32];
			for (int i = 0, j = 0;; i++, j++) {
				if (i >= 16) {
					return new String(arrayOfChar);
				}
				int k = arrayOfByte[i];
				arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
				arrayOfChar[++j] = hexDigits[(k & 0xF)];
			}
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 
	 * @Title: getDeviceUsableMemory
	 * 
	 * @Description: 获取设备的可用内存大小
	 * 
	 * @param cxt
	 *            应用上下文对象context
	 * @return 当前内存大小
	 */
	public static int getDeviceUsableMemory(Context cxt) {
		ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// 返回当前系统的可用内存
		return (int) (mi.availMem / (1024 * 1024));
	}

	/**
	 * 
	 * @Title: gc
	 * 
	 * @Description: 清理后台进程与服务
	 * 
	 * @param cxt
	 *            应用上下文对象context
	 * @return 被清理的数量
	 */
	public static int gc(Context cxt) {
		// long i = getDeviceUsableMemory(cxt);
		int count = 0; // 清理掉的进程数
		ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取正在运行的service列表
		List<RunningServiceInfo> serviceList = am.getRunningServices(100);
		if (serviceList != null)
			for (RunningServiceInfo service : serviceList) {
				if (service.pid == android.os.Process.myPid())
					continue;
				try {
					android.os.Process.killProcess(service.pid);
					count++;
				} catch (Exception e) {
					e.getStackTrace();
					continue;
				}
			}

		// 获取正在运行的进程列表
		List<RunningAppProcessInfo> processList = am.getRunningAppProcesses();
		if (processList != null)
			for (RunningAppProcessInfo process : processList) {
				// 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
				// 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
				if (process.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
					// pkgList 得到该进程下运行的包名
					String[] pkgList = process.pkgList;
					for (String pkgName : pkgList) {
						// KJLoger.debug("======正在杀死包名：" + pkgName);
						try {
							am.killBackgroundProcesses(pkgName);
							count++;
						} catch (Exception e) { // 防止意外发生
							e.getStackTrace();
							continue;
						}
					}
				}
			}
		// ILog.debug("清理了" + (getDeviceUsableMemory(cxt) - i) + "M内存");
		return count;
	}
}
