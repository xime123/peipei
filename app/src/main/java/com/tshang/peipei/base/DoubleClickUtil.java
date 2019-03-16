package com.tshang.peipei.base;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * @Title: DoubleClickUtil.java 
 *
 * @Description: 防止重复点击工具类 
 *
 * @author DYH  
 *
 * @date 2016-1-24 下午3:42:48 
 *
 * @version V1.0   
 */
public class DoubleClickUtil implements OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

	@Override
	public void onClick(View v) {
		 long currentTime = System.currentTimeMillis();
	        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
	            lastClickTime = currentTime;
	            onNoDoubleClick(v);
	        } 
	}  
	
	public void onNoDoubleClick(View v){
		
	}
}
