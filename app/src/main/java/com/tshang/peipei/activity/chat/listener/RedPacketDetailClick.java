package com.tshang.peipei.activity.chat.listener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.tshang.peipei.activity.redpacket.RedPacketDetailActivity;
import com.tshang.peipei.base.BaseUtils;

/**
 * @Title: RedPacketDetailClick.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014年10月20日 下午7:57:08 
 *
 * @version V1.0   
 */
public class RedPacketDetailClick implements OnClickListener {

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		bundle.putInt(RedPacketDetailActivity.STR_REDPACKETID, redpacketid);
		bundle.putInt(RedPacketDetailActivity.STR_REDPACKETUID, redpacketuid);
		BaseUtils.openActivity(activity, RedPacketDetailActivity.class, bundle);

	}

	private Activity activity;
	private int redpacketid;
	private int redpacketuid;

	public void setData(int redpacketid, int redpacketuid) {
		this.redpacketid = redpacketid;
		this.redpacketuid = redpacketuid;
	}

	public RedPacketDetailClick(Activity activity) {
		this.activity = activity;

	}

}
