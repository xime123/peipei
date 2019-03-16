package com.tshang.peipei.activity.listener;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.tshang.peipei.activity.mine.MineFaqActivity;

/**
 * @Title: OnLevelClickListener.java 
 *
 * @Description: TODO  adapter里面点击等级跳转
 *
 * @author jeff  
 *
 * @date 2015年1月14日 下午2:07:04 
 *
 * @version V2.1.0   
 */
public class OnLevelClickListener implements OnClickListener {
	private Activity activity;

	public OnLevelClickListener(Activity activity) {
		this.activity = activity;

	}

	@Override
	public void onClick(View v) {
		MineFaqActivity.openMineFaqActivity(activity, MineFaqActivity.LEVEL_VALUE);
	}

}
