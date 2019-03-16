package com.tshang.peipei.activity.listener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.account.LoginActivity;
import com.tshang.peipei.activity.mine.MineWriteBroadCastActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.json.GoGirlUserJson;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.space.SpaceUtils;

/**
 * @Title: UserClickListener.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Jeff  
 *
 * @date 2015年1月14日 下午1:56:27 
 *
 * @version V2.1.0   
 */
public class OnUserClickListener implements OnClickListener {
	public static final int FLAG_SPACE = 1;
	public static final int FLAG_WRITE_BROADCAST = 2;
	private int flag;//1为头像查看发帖人，@的人的信息  2为@某人进入发帖界面
	private GoGirlUserInfo userInfo;
	private Activity activity;

	public OnUserClickListener(Activity activity, int flag) {
		this.activity = activity;
		this.flag = flag;
	}

	public void setUserInfo(GoGirlUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		if (userInfo == null) {
			return;
		}
		if (userInfo.uid.intValue() == 50000) {//系统消息不可点击
			return;
		}
		if (flag == FLAG_SPACE) {//看空间
			SpaceUtils.toSpaceCustom(activity, userInfo, 1);
		} else if (flag == FLAG_WRITE_BROADCAST) {//@某人发送广播
			if (!UserUtils.isLogin(activity)) {//未登录
				BaseUtils.openActivity(activity, LoginActivity.class);
				return;
			}
			if (userInfo.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
				BaseUtils.showTost(activity, R.string.str_about_yourself);
				return;
			}
			Bundle bundle = new Bundle();
			GoGirlUserInfoList infoList = new GoGirlUserInfoList();
			infoList.add(userInfo);
			bundle.putString(MineWriteBroadCastActivity.STR_GOGIRLUSERINFO, GoGirlUserJson.changeArrayDateToJson(infoList));
			BaseUtils.openActivity(activity, MineWriteBroadCastActivity.class, bundle);
		}

	}
}
