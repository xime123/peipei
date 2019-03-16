package com.tshang.peipei.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.tshang.peipei.storage.SharedPreferencesTools;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("SimpleDateFormat")
public class BaseTimes {
	/**
	 * 计算两个日期型的时间相差多少时间 
	 * @author Jeff
	 *
	 * @param c
	 * @return
	 */
	public static String twoDateDistance(long c) {
		Date startDate = new Date(c);
		Date endDate = new Date(System.currentTimeMillis());
		if (startDate == null || endDate == null) {
			return null;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 60 * 1000)
			return timeLong / 1000 + "秒前";
		else if (timeLong < 60 * 60 * 1000) {
			timeLong = timeLong / 1000 / 60;
			return timeLong + "分钟前";
		} else if (timeLong < 60 * 60 * 24 * 1000) {
			timeLong = timeLong / 60 / 60 / 1000;
			return timeLong + "小时前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
			timeLong = timeLong / 1000 / 60 / 60 / 24;
			return timeLong + "天前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
			timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
			return timeLong + "周前";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(startDate);
		}
	}

	public static String getChatDiffTime(long c) {
		Date startDate = new Date(c);
		Date endDate = new Date(System.currentTimeMillis());
		if (startDate == null || endDate == null) {
			return null;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong <= 0) {
			return "刚刚";
		} else if (timeLong < 60 * 1000)
			return timeLong / 1000 + "秒前";
		else if (timeLong < 60 * 60 * 1000) {
			timeLong = timeLong / 1000 / 60;
			return timeLong + "分钟前";
		} else if (timeLong < 60 * 60 * 24 * 1000) {
			timeLong = timeLong / 60 / 60 / 1000;
			return timeLong + "小时前";
		} else if (timeLong < 60 * 60 * 24 * 1000) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			//			timeLong = timeLong / 1000 / 60 / 60 / 24;
			return "昨天 " + sdf.format(startDate);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd　HH:mm");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(startDate);
		}
	}

	/**
	 * 返回与当前系统时间的比较值
	 * 
	 * @param c
	 * @return
	 */
	public static String getDiffTime(long c) {
		String time;
		long diff = System.currentTimeMillis() - c;// 这样得到的差值是微秒级别
		long days = diff / 86400000L;
		if (days <= 0) {
			long hours = (diff - days * 86400000L) / 3600000;
			if (hours <= 0) {
				long minutes = (diff - days * 86400000L - hours * 3600000) / 60000;
				if (minutes <= 0) {
					time = "刚刚";
				} else {
					time = minutes + "分钟前";
				}
			} else {
				time = hours + "小时前";
			}
		} else {
			if (days > 30) {
				time = "1个月前";
			} else {
				time = days + "天前";
			}
		}
		return time;
	}

	//	/**
	//	 * 返回与当前时间的比较值，格式为yyyy-MM-dd HH:mm
	//	 * 
	//	 * @param c
	//	 * @return
	//	 */
	//
	//	@SuppressLint("SimpleDateFormat")
	//	public static String getChatDiffTime(long c) {
	//		String time = "";
	//
	//		Date d1 = new Date(System.currentTimeMillis());
	//		Date d2 = new Date(c);
	//		SimpleDateFormat sDateFormat = null;
	//
	//		if (d1.hashCode() - d2.hashCode() > 0 && d1.hashCode() - d2.hashCode() < 86400000) {
	//			int day_diff = d1.getDate() - d2.getDate();
	//			sDateFormat = new SimpleDateFormat("HH:mm");
	//			if (day_diff < 1) {
	//				time = sDateFormat.format(c);
	//			} else {
	//				time = "昨天 " + sDateFormat.format(c);
	//			}
	//		} else {
	//			sDateFormat = new SimpleDateFormat("yyyy-MM-dd     HH:mm");
	//			time = sDateFormat.format(c);
	//		}
	//
	//		return time;
	//	}

	/**
	 * 返回与当前时间的比较值，格式为yyyy-MM-dd HH:mm
	 * 
	 * @param c
	 * @return
	 */
	public static String getChatDiffTimeYYMDHM(long c) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd     HH:mm");

		return sDateFormat.format(c);
	}

	public static String getChatDiffTimeHHMM(long c) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm");

		return sDateFormat.format(c);
	}

	public static String getChatDiffTimeHHMMSS(long c) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss");

		return sDateFormat.format(c);
	}

	public static String getChatDiffTimeYYMDHMS(long c) {
		SimpleDateFormat sDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		sDateFormat1.setTimeZone(TimeZone.getTimeZone("GMT0"));
		SimpleDateFormat sDateFormat2 = new SimpleDateFormat("HH:mm:ss");
		sDateFormat2.setTimeZone(TimeZone.getTimeZone("GMT0"));

		return sDateFormat1.format(c) + "T" + sDateFormat2.format(c) + "Z";
	}

	public static String getChatDiffTimeMMDD(long c) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("MM月dd日");

		return sDateFormat.format(c);
	}

	// 时间
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	public static String getDiffTime2(long c) {
		String time = "";
		long diff = System.currentTimeMillis() - c;
		long days = diff / 86400000L;
		if (days < 1) {
			Date d1 = new Date(System.currentTimeMillis());
			Date d2 = new Date(c);
			int day_diff = d1.getDate() - d2.getDate();
			if (day_diff < 1) {
				time = "今天";
			} else {
				time = "昨天";
			}
		} else if (days == 1) {
			time = "昨天";
		} else {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			Date d = new Date(c);
			time = sDateFormat.format(d);
		}

		return time;
	}

	public static boolean isNewDay(long c) {
		long diff = System.currentTimeMillis() - c;
		long days = diff / 86400000L;
		if (days < 1) {
			Date d1 = new Date(System.currentTimeMillis());
			Date d2 = new Date(c);
			int day_diff = d1.getDate() - d2.getDate();
			if (day_diff < 1) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	/**
	 * 返回 yyyy-MM-dd 类型日期
	 * @param time
	 * @return
	 */
	public static String getFormatTime(long time) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String newTime = sDateFormat.format(time);
		return newTime;
	}

	/**
	 * 返回yy.mm.dd类型日期
	 *
	 * @param time
	 * @return
	 */
	public static String getFormatTime2(long time) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yy.MM.dd");
		String newTime = sDateFormat.format(time);
		return newTime;
	}

	//返回秒
	public static long getlongTime(String time) {
		long millionSeconds = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			millionSeconds = sdf.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millionSeconds / 1000;
	}

	/**
	 * 返回与当前时间的比较值，（超过一天返回空，其他方法获取具体日期）
	 * 
	 * @param c
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	public static String getDiffTimeMaxOneDay(long c) {
		String time = "";
		long diff = System.currentTimeMillis() - c;
		long days = diff / 86400000L;
		if (days < 1) {
			Date d1 = new Date(System.currentTimeMillis());
			Date d2 = new Date(c);
			int day_diff = d1.getDate() - d2.getDate();
			if (day_diff < 1) {
				time = "今天";
			} else {
				time = "昨天";
			}
		} else if (days == 1) {
			time = "昨天";
		}

		return time;
	}

	/**
	 * 判断当前时间是晚上还是白天(晚上:19:00-06:59,白天07:00-18:59)
	 * 
	 * @return boolean
	 */
	public static boolean isNightOrDay() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		boolean isNight = hour >= 19 && hour <= 23 || hour >= 0 && hour < 7 ? true : false;
		return isNight;

	}

	/**
	 * 判断是否超时
	 * 
	 * @param lastGetDataTime
	 *            当前时间
	 * @param outTime
	 *            超时时间
	 * @return
	 */
	public static boolean isTimeDistanceNow(long lastGetDataTime, long outTime) {
		if (lastGetDataTime == 0L) {
			return true;
		}
		long distanceTime = System.currentTimeMillis() - lastGetDataTime;

		if (distanceTime > outTime) {
			return true;
		}
		return false;
	}

	/**
	 * 计算星座
	 * @author vactor
	 *
	 * @param month 月
	 * @param day 天
	 * @return
	 */
	public static String getConstellation(int month, int day) {
		int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };
		String[] constellationArr = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };
		return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
	}

	public static boolean getShareDaily(long time) {
		Date d1 = new Date(System.currentTimeMillis());
		Date d2 = new Date(time);
		if (d1.equals(d2)) {
			return true;
		} else {
			return false;
		}

	}

	public static String getTime(long c) {
		Date date = new Date(c);
		String todySDF = "今天 HH:mm";
		String yesterDaySDF = "昨天 HH:mm";
		String otherSDF = "MM月dd日 HH:mm";
		SimpleDateFormat sfd = null;
		String time = "";
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		Date now = new Date();
		Calendar targetCalendar = Calendar.getInstance();
		targetCalendar.setTime(now);
		targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
		targetCalendar.set(Calendar.MINUTE, 0);
		if (dateCalendar.after(targetCalendar)) {
			sfd = new SimpleDateFormat(todySDF);
			time = sfd.format(date);
			return time;
		} else {
			targetCalendar.add(Calendar.DATE, -1);
			if (dateCalendar.after(targetCalendar)) {
				sfd = new SimpleDateFormat(yesterDaySDF);
				time = sfd.format(date);
				return time;
			}
		}
		sfd = new SimpleDateFormat(otherSDF);
		time = sfd.format(date);
		return time;
	}

	public static String getDistance(int distance) {
		if (distance < 100) {
			return "0.1km以内";
		}
		float dis = (float) (distance / 1000.0);
		float b = (float) (Math.round(dis * 10)) / 10;
		return b + "km";
	}

	public static String getTime1(long c) {
		Date date = new Date(c);
		String todySDF = "今天 HH:mm";
		String yesterDaySDF = "昨天 HH:mm";
		String otherSDF = "yyyy-MM-dd";
		SimpleDateFormat sfd = null;
		String time = "";
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		Date now = new Date();
		Calendar targetCalendar = Calendar.getInstance();
		targetCalendar.setTime(now);
		targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
		targetCalendar.set(Calendar.MINUTE, 0);
		if (dateCalendar.after(targetCalendar)) {
			sfd = new SimpleDateFormat(todySDF);
			time = sfd.format(date);
			return time;
		} else {
			targetCalendar.add(Calendar.DATE, -1);
			if (dateCalendar.after(targetCalendar)) {
				sfd = new SimpleDateFormat(yesterDaySDF);
				time = sfd.format(date);
				return time;
			}
		}
		sfd = new SimpleDateFormat(otherSDF);
		time = sfd.format(date);
		return time;
	}

	/**
	 * 
	 * @author Aaron
	 *
	 * @param time
	 * @return
	 */
	public static String getHH_mmTime(long time) {
		String str = "";
		if (time >= 60) {
			int hh = (int) time / (60 * 60);
			int mm = (int) time / 60;
			str = hh > 0 ? hh + "小时" + (mm - hh * 60) + "分钟" : mm + "分钟";

			if (mm % 60 == 0) {
				str = hh + "小时";
			}
		} else {
			str = time + "秒";
		}

		return str;
	}

	/**
	 * 判断每天凌晨2点后更换背景
	 * @author Aaron
	 *
	 * @param context
	 * @return
	 */
	public static boolean isChangeGroupChatBg(Context context) {
		String year_key = "YEAR_KEY";
		String moth_key = "MOTH_KEY";
		String day_key = "DAY_KEY";

		Calendar calendar = Calendar.getInstance();

		int year = SharedPreferencesTools.getInstance(context).getIntValueByKey(year_key);
		int moth = SharedPreferencesTools.getInstance(context).getIntValueByKey(moth_key);
		int day = SharedPreferencesTools.getInstance(context).getIntValueByKey(day_key);

		int new_year = calendar.get(Calendar.YEAR);
		int new_moth = calendar.get(Calendar.MONTH) + 1;
		int new_day = calendar.get(Calendar.DAY_OF_MONTH);

		int house = calendar.get(Calendar.HOUR_OF_DAY);
		if ((new_year > year || new_moth > moth || new_day > day) && house > 1) {
			SharedPreferencesTools.getInstance(context).saveIntKeyValue(new_year, year_key);
			SharedPreferencesTools.getInstance(context).saveIntKeyValue(new_moth, moth_key);
			SharedPreferencesTools.getInstance(context).saveIntKeyValue(new_day, day_key);
			return true;
		}
		return false;
	}
}
