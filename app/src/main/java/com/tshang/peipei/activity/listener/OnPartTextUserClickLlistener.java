package com.tshang.peipei.activity.listener;

import android.app.Activity;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.tshang.peipei.R;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.model.space.SpaceUtils;

/**
 * @Title: OnPartTextUserClickLlistener.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 广播点击@的用户进入起空间
 *
 * @author jeff  
 *
 * @date 2015年1月14日 下午2:19:04 
 *
 * @version V2.1.0   
 */
public class OnPartTextUserClickLlistener extends ClickableSpan {//点击@的用户
	private GoGirlUserInfo userInfo;
	private Activity activity;

	public OnPartTextUserClickLlistener(Activity activity, GoGirlUserInfo userInfo) {
		this.activity = activity;
		this.userInfo = userInfo;
	}

	@Override
	public void updateDrawState(TextPaint ds) {//选中的文字变色
		super.updateDrawState(ds);//369a00
//		ds.setColor(activity.getResources().getColor(R.color.green_color));
		ds.setColor(activity.getResources().getColor(R.color.main_broadcast_at_name_color));
		ds.setUnderlineText(false);

	}

	@Override
	public void onClick(View widget) {
		if (userInfo != null) {
			SpaceUtils.toSpaceCustom(activity, userInfo, 1);
		}
	}

}
