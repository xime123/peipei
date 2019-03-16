package com.tshang.peipei.model.harem;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.harem.ManagerHaremActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoV2;

/**
 * @Title: HaremUtils.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014年11月21日 下午3:17:25 
 *
 * @version V1.0   
 */
public class HaremUtils {
	public static void intentManagerHaremActivity(Activity activity, GroupInfo info, boolean isCreate, boolean isHost) {
		if (info != null) {
			int groupid = info.groupid.intValue();
			String groupName = new String(info.groupname);
			if (TextUtils.isEmpty(groupName)) {
				groupName = "";
			}
			if (isHost) {//主人进入到群聊
				ChatActivity.intentChatActivity(activity, groupid, groupName, 1, true, false, 0);
			} else {//客人看群信息，可以申请加入
				Bundle bundle = new Bundle();
				bundle.putInt("groupid", groupid);
				bundle.putInt("from", 0);
				BaseUtils.openActivity(activity, ManagerHaremActivity.class, bundle);
			}

		}
	}
	
	public static void intentManagerHaremActivityV2(Activity activity, GroupInfoV2 info, boolean isCreate, boolean isHost) {
		if (info != null) {
			int groupid = info.groupid.intValue();
			String groupName = new String(info.groupname);
			if (TextUtils.isEmpty(groupName)) {
				groupName = "";
			}
			if (isHost) {//主人进入到群聊
				ChatActivity.intentChatActivity(activity, groupid, groupName, 1, true, false, 0);
			} else {//客人看群信息，可以申请加入
				Bundle bundle = new Bundle();
				bundle.putInt("groupid", groupid);
				bundle.putInt("from", 0);
				BaseUtils.openActivity(activity, ManagerHaremActivity.class, bundle);
			}

		}
	}
}
