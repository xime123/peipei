package com.tshang.peipei.activity.listener;

import android.app.Activity;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.tshang.peipei.R;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;

import de.greenrobot.event.EventBus;

/**
 * @Title: OnPartTextUserClickLlistener.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 仙术回放
 *
 * @author jeff  
 *
 * @date 2015年1月14日 下午2:19:04 
 *
 * @version V2.1.0   
 */
public class OnPartTextPlayBackClickListener extends ClickableSpan {//点击@的用户
	private int type;
	private Activity activity;
	private BroadcastInfo info;

	public OnPartTextPlayBackClickListener(int type, Activity activity,BroadcastInfo info) {
		this.type = type;
		this.activity = activity;
		this.info = info;
	}

	@Override
	public void updateDrawState(TextPaint ds) {//选中的文字变色
		super.updateDrawState(ds);//369a00
		ds.setColor(activity.getResources().getColor(android.R.color.white));
		ds.setUnderlineText(false);
		ds.bgColor = activity.getResources().getColor(R.color.peach);

	}

	@Override
	public void onClick(View widget) {
		NoticeEvent noticeEvent = new NoticeEvent();
		noticeEvent.setFlag(NoticeEvent.NOTICE84);
		noticeEvent.setNum(type);
		noticeEvent.setObj(info);
		EventBus.getDefault().post(noticeEvent);
	}

}
