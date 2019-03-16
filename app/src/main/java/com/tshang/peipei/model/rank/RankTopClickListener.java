package com.tshang.peipei.model.rank;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.model.space.SpaceUtils;

/**
 * @Title: RankTopClickListener.java 
 *
 * @Description: 日榜排行前三的点击事件
 *
 * @author Jeff  
 *
 * @date 2014年7月31日 下午3:06:14 
 *
 * @version V1.0   
 */
public class RankTopClickListener implements OnClickListener {
	private GoGirlUserInfo useinfo;
	private Activity activity;

	public RankTopClickListener(GoGirlUserInfo useinfo, Activity activity) {
		this.useinfo = useinfo;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		if (useinfo == null)
			return;
		SpaceUtils.toSpaceCustom(activity, useinfo, 2);
	}

}
