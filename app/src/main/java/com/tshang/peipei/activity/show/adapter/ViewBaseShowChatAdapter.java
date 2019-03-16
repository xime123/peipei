package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.mine.MineSettingUserInfoActivity;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.entity.ShowChatEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: ViewBaseShowChatAdapter.java 
 *
 * @Description: 秀场聊天适配器基类
 *
 * @author allen  
 *
 * @date 2015-1-22 上午10:44:15 
 *
 * @version V1.0   
 */
public abstract class ViewBaseShowChatAdapter {
	protected Activity activity;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options_uid_head;//通过UID加载
	protected DisplayImageOptions options_gift;
	protected BAHandler mHandler;

	public ViewBaseShowChatAdapter(Activity activity) {
		this.activity = activity;
		imageLoader = ImageLoader.getInstance();
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(activity);
		options_gift = ImageOptionsUtils.getImageKeyOptions(activity);
	}

	public abstract View getView(int position, View convertView, ViewGroup parent, ShowChatEntity chatdatap, String fileName);

	protected class HeadClickListener implements OnClickListener {//头像点击事件
		private int sex;
		private int uid;

		public HeadClickListener(int uid, int sex) {
			this.sex = sex;
			this.uid = uid;
		}

		@Override
		public void onClick(View v) {
			if (uid != BAApplication.mLocalUserInfo.uid.intValue()) {
				Bundle bundle = new Bundle();
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, uid);
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
				BaseUtils.openActivityByNew(activity, SpaceActivity.class, bundle);
			} else {
				if (BAApplication.mLocalUserInfo != null) {
					Bundle bundle = new Bundle();
					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, BAApplication.mLocalUserInfo.uid.intValue());
					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, BAApplication.mLocalUserInfo.sex.intValue());
					BaseUtils.openActivity(activity, MineSettingUserInfoActivity.class, bundle);
				}
			}
		}

	}

}
