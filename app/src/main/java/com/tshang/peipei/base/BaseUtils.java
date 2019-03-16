package com.tshang.peipei.base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.view.CustomDialog;

/**
 * @Title: BaseUtils.java 
 *
 * @Description: TODO工具类
 *
 * @author Jeff  
 *
 * @date 2014年5月23日 下午6:11:43 
 *
 * @version V1.0   
 */
@SuppressLint("DefaultLocale")
public class BaseUtils {
	/**
	 * 隐藏悬浮框，popupwindow
	 * @author Jeff
	 *
	 * @param popWindow
	 */
	public static void hidePopupWindow(PopupWindow popWindow) {
		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
			popWindow = null;
		}
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void hideKeyboard(Context context, View view) {
		if (view == null || context == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * dip单位转换成像素单位
	 * @author Jeff
	 *
	 * @param context 上下文对象
	 * @param dipValue 需要转换的值
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		if (context == null) {
			return 1;
		}
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 像素转换成dip单位
	 * @author Jeff
	 *
	 * @param context 上下文对象
	 * @param pxValue 需要转换的值
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		if (context == null) {
			return 1;
		}
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 带返回值的页面跳转
	 * @author Jeff
	 *
	 * @param context
	 * @param pClass
	 * @param pBundle
	 * @param requestCode
	 */
	public static void openResultActivity(Activity context, Class<?> pClass, Bundle pBundle, int requestCode) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(context, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivityForResult(intent, requestCode);
		context.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
	}

	/**
	 * 不带参数的页面跳转
	 * @author Jeff
	 *
	 * @param context
	 * @param pClass
	 */
	public static void openActivity(Activity context, Class<?> pClass) {
		openActivity(context, pClass, null);
	}

	/**
	 * 带参数的页面跳转
	 * @author Jeff
	 *
	 * @param context
	 * @param pClass
	 * @param pBundle
	 */
	public static void openActivity(Activity context, Class<?> pClass, Bundle pBundle) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(context, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
	}

	public static void openNormalActivity(Activity context, Class<?> pClass, Bundle pBundle) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(context, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.fragment_slide_left_scale_enter, R.anim.fragment_slide_left_scale_exit);
	}

	public static void openActivityUp(Activity context, Class<?> pClass, Bundle pBundle) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(context, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
	}

	/**
	 * 带参数的页面跳转
	 * @author Jeff
	 *
	 * @param context
	 * @param pClass
	 * @param pBundle
	 */
	public static void openActivityByNew(Activity context, Class<?> pClass, Bundle pBundle) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(context, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		//		intent.setFlags(Intent.flag_);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
	}

	/**
	 * 显示进度条  调用dialog.show()方法
	 * @author Administrator
	 *
	 * @param context
	 * @param str
	 * @return
	 */
	public static void showDialog(Activity context, String str) {
		try {
			if (context == null) {
				return;
			}
			cancelDialog();
			if (cDialog == null) {
				synchronized (BaseUtils.class) {
					if (cDialog == null) {
						cDialog = new CustomDialog(context, R.layout.progressbar_layout, R.style.DialogStyle);// Dialog使用默认大小
					}
				}
			}
			TextView mMessage = (TextView) cDialog.findViewById(R.id.progress_loading_tv);
			mMessage.setText(str);
			cDialog.setCanceledOnTouchOutside(false);

			if (!cDialog.isShowing() && !context.isFinishing()) {
				cDialog.show();
			}
		} catch (Exception e) {
			cancelDialog();
			try {
				//		MobclickAgent.reportError(BAApplication.getInstance().getApplicationContext(), "创建对话框报错了");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public static void showDialog(Activity context, int id) {
		showDialog(context, context.getString(id));
	}

	public static void cancelDialog() {
		try {
			if (cDialog != null && cDialog.isShowing()) {
				cDialog.dismiss();
				cDialog = null;
			}
		} catch (Exception e) {
			try {
				//				MobclickAgent.reportError(BAApplication.getInstance().getApplicationContext(), "取消对话框报错了");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 隐藏进度条
	 * @author Administrator
	 *
	 * @param dialog
	 */
	public static void disDialog(Dialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

	private static CustomDialog cDialog;
	private static Toast mToast;
	public static int width = 0;

	/**
	 * 消息提示
	 * 
	 * @param c
	 * @param msg
	 */
	public static void showTost(Context context, int id) {
		if (context == null || ((Activity) context).isFinishing()) {
			return;
		}
		showTost(context, context.getString(id));
	}

	public static void showTost(Context context, String msg) {
		try {
			if (context == null) {
				return;
			}
			if (mToast == null) {
				synchronized (BaseUtils.class) {
					if (mToast == null) {
						mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
					} else {
						mToast.setText(msg);
						mToast.setDuration(Toast.LENGTH_SHORT);
					}
				}
			} else {
				mToast.setText(msg);
				mToast.setDuration(Toast.LENGTH_SHORT);
			}
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.show();
		} catch (Exception e) {
			try {
//				MobclickAgent.reportError(BAApplication.getInstance().getApplicationContext(), "创建提示是不是也会走这里");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public static void cancelToast() {
		try {
			if (mToast != null) {
				mToast.cancel();
			}
		} catch (Exception e) {
			try {
//				MobclickAgent.reportError(BAApplication.getInstance().getApplicationContext(), "取消提示是不是也会走这里");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	//判断sd卡是否存在
	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	* 将sp值转换为px值，保证文字大小不变
	*
	* @param spValue
	* @param fontScale
	*            （DisplayMetrics类中属性scaledDensity）
	* @return
	*/
	public static int sp2px(Context context, float spValue) {
		if (context == null) {
			return 1;
		}
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * scale + 0.5f);
	}

	public static String byteToString(byte[] bytes) {//字节数组转换成字符串
		if (bytes != null) {
			return new String(bytes);
		}
		return "";
	}

	/**
	 * 替换文本为图片
	 * 
	 * @param charSequence
	 * @param regPattern
	 * @param drawable
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static SpannableString replaceImageSpan(CharSequence charSequence, String regPattern, Bitmap drawable) {
		SpannableString ss = charSequence instanceof SpannableString ? (SpannableString) charSequence : new SpannableString(charSequence);
		try {
			ImageSpan is = new ImageSpan(drawable);
			Pattern pattern = Pattern.compile(regPattern);
			Matcher matcher = pattern.matcher(ss);
			while (matcher.find()) {
				String key = matcher.group();
				ss.setSpan(is, matcher.start(), matcher.start() + key.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
		} catch (Exception ex) {
		}

		return ss;
	}

	/**
	 * 创建控件半园形效果
	 * @author Aaron
	 *
	 * @param strokeWidth 边框宽
	 * @param storkeColor 边框辨色
	 * @param roundRadius 圆角半径
	 * @param fillColor 填充颜色
	 * @return
	 */
	public static Drawable createGradientDrawable(int strokeWidth, int storkeColor, int roundRadius, int fillColor) {
		GradientDrawable drawable = new GradientDrawable();//创建drawable
		drawable.setColor(fillColor);
		drawable.setCornerRadius(roundRadius);
		drawable.setStroke(strokeWidth, storkeColor);
		return drawable;
	}

	/**
	 * 
	 * @Title: isEmulator
	 * @author Aaron
	 * @Description: 通过IMEI号判断是否是模拟器
	 * @param context
	 * @return boolean
	 */
	public static boolean isEmulator(final Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tm.getDeviceId();
			new AlertDialog.Builder(context).setMessage("imei===" + imei).setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					CheckDeviceIDS(context);
				}
			}).show();
			if (imei != null && imei.equals("000000000000000")) {
				return true;
			}
			Log.d("Aaron", "imei===" + imei);

		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return false;
	}

	private static String[] known_device_ids = { "310260000000000" };// 默认ID

	/**
	 * 
	 * @Title: CheckDeviceIDS
	 * @author Aaron
	 * @Description: 检测设备IDS是不是" 310260000000000"
	 * @param context
	 * @return boolean
	 */
	public static Boolean CheckDeviceIDS(final Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		String device_ids = telephonyManager.getSubscriberId();
		Log.d("Aaron", "drvice ids===" + device_ids);
		new AlertDialog.Builder(context).setMessage("drvice ids===" + device_ids).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				CheckOperatorNameAndroid(context);
			}
		}).show();
		for (String know_deviceid : known_device_ids) {
			if (know_deviceid.equalsIgnoreCase(device_ids)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @Title: CheckOperatorNameAndroid
	 * @author Aaron
	 * @Description: 检测手机运营厂商
	 * @param context
	 * @return boolean
	 */
	public static boolean CheckOperatorNameAndroid(final Context context) {
		String szOperatorName = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
		BaseUtils.showDialog((Activity) context, "szOperatorName===" + szOperatorName);
		new AlertDialog.Builder(context).setMessage("szOperatorName===" + szOperatorName).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				CheckEmulatorBuild(context);
			}
		}).show();
		if (szOperatorName.toLowerCase().equals("android") == true) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: CheckEmulatorBuild
	 * @author Aaron
	 * @Description: 检测手机硬件信息
	 * @param context
	 * @return boolean
	 */
	public static boolean CheckEmulatorBuild(final Context context) {
		String BOARD = android.os.Build.BOARD;
		String BOOTLOADER = android.os.Build.BOOTLOADER;
		String BRAND = android.os.Build.BRAND;
		String DEVICE = android.os.Build.DEVICE;
		String HARDWARE = android.os.Build.HARDWARE;
		String MODEL = android.os.Build.MODEL;
		String PRODUCT = android.os.Build.PRODUCT;
		Log.d("Aaron", "Board==" + BOARD + ", bootLoader==" + BOOTLOADER + ", brand==" + BRAND + ", device==" + DEVICE + ", hardware==" + HARDWARE
				+ ", model==" + MODEL + ", product==" + PRODUCT);
		new AlertDialog.Builder(context)
				.setMessage(
						"Board==" + BOARD + ", bootLoader==" + BOOTLOADER + ", brand==" + BRAND + ", device==" + DEVICE + ", hardware==" + HARDWARE
								+ ", model==" + MODEL + ", product==" + PRODUCT).setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						CheckPhoneNumber(context);
					}
				}).show();
		if (BOARD == "unknown" || BOOTLOADER == "unknown" || BRAND == "generic" || DEVICE == "generic" || MODEL == "sdk" || PRODUCT == "sdk"
				|| HARDWARE == "goldfish") {
			return true;
		}
		return false;
	}

	private static String[] known_numbers = { "15555215554", "15555215556", "15555215558", "15555215560", "15555215562", "15555215564",
			"15555215566", "15555215568", "15555215570", "15555215572", "15555215574", "15555215576", "15555215578", "15555215580", "15555215582",
			"15555215584" };

	/**
	 * 
	 * @Title: CheckEmulatorBuild
	 * @author Aaron
	 * @Description: 检测模拟器默认的电话号码
	 * @param context
	 * @return boolean
	 */
	public static Boolean CheckPhoneNumber(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		String phonenumber = telephonyManager.getLine1Number();
		Log.d("Aaron", "phoneNumber==" + phonenumber);
		new AlertDialog.Builder(context).setMessage("phoneNumber:\n" + phonenumber).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
		for (String number : known_numbers) {
			if (number.equalsIgnoreCase(phonenumber)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @author Aaron
	 * @return
	 */
	public static boolean getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			localBufferedReader.close();
		} catch (IOException e) {
		}
		if (!cpuInfo[0].toLowerCase().contains("arm")) {
			return true;
		}
		return false;
	}
}
