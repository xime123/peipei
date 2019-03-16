package com.tshang.peipei.vender.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

import com.tshang.peipei.R;
import com.tshang.peipei.base.NumericUtils;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_HOUR_MINUTE    = new SimpleDateFormat("HH:mm:ss");

    /**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
    
	public static String getLeftTime(Context context, long leftTime){
		long minute = 0;
		if(leftTime > 60){
			minute = leftTime / 60;
			return minute + context.getString(R.string.str_minute);
		}else{
			return leftTime + context.getString(R.string.str_millis);
		}
    }
	
	public static String getHourMinuteTime(long timeInMillis){
		timeInMillis = timeInMillis * 1000;
		return getTime(timeInMillis, DATE_FORMAT_HOUR_MINUTE);
	}
}
