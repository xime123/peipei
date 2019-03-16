package com.tshang.peipei.activity.chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.reward.RewardListActivity;

/**
 * @Title: ChatBottomAdapter.java 
 *
 * @Description: 私聊底部按键适配
 *
 * @author allen  
 *
 * @date 2014-7-9 下午4:32:00 
 *
 * @version V1.0   
 */
public class ChatBottomAdapter extends ArrayListAdapter<String> {
	private int from;
	private int isOpenRedPacket;
	private String[] bottom;
	private Integer[] chatsImage;
//	private Integer[] privateChats = { R.drawable.message_icon_pic_selector, R.drawable.message_icon_camera_selector,
//			R.drawable.message_icon_fire_selector, R.drawable.message_icon_vedio_selector, R.drawable.message_icon_morra_selector,
//			R.drawable.message_icon_truth_selector };
	
	private Integer[] privateChats = { R.drawable.message_icon_pic_selector, R.drawable.message_icon_camera_selector,
			R.drawable.message_icon_fire_selector, R.drawable.message_icon_vedio_selector };

	private Integer[] groupChats = { R.drawable.message_icon_pic_selector, R.drawable.message_icon_camera_selector,
			R.drawable.message_icon_redpacket_selector };

	private Integer[] groupChats2 = { R.drawable.message_icon_pic_selector, R.drawable.message_icon_camera_selector,
			R.drawable.message_icon_morra_selector, R.drawable.message_icon_redpacket_selector, R.drawable.message_icon_xian_selector };

	private Integer[] rewardChats = { R.drawable.message_icon_pic_selector, R.drawable.message_icon_camera_selector,
			R.drawable.message_icon_fire_selector, R.drawable.message_icon_vedio_selector };

	public ChatBottomAdapter(Activity context, boolean isGroup, int from, int isOpenRedPacket) {
		super(context);
		this.from = from;
		this.isOpenRedPacket = isOpenRedPacket;
		if (isGroup) {
//			if (isOpenRedPacket == 1) {
//				bottom = context.getResources().getStringArray(R.array.chat_button_array_group);
//				chatsImage = groupChats;
//			} else {
				bottom = context.getResources().getStringArray(R.array.chat_button_array_group2);
				chatsImage = groupChats;
//			}
		} else {
			if (from == RewardListActivity.CHAT_FROM_REWARD) {
				bottom = context.getResources().getStringArray(R.array.chat_buttom_array_reward);
				chatsImage = rewardChats;
			} else {
				bottom = context.getResources().getStringArray(R.array.chat_button_array);
				chatsImage = privateChats;
			}
		}
		setList(bottom);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_button, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.bottomIv = (ImageView) convertView.findViewById(R.id.item_gv_chat_image);
			viewHolder.bottomTv = (TextView) convertView.findViewById(R.id.item_gv_chat_tv);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.bottomIv.setImageResource(chatsImage[position]);
		viewHolder.bottomTv.setText(bottom[position]);

		return convertView;
	}

	final class ViewHolder {
		ImageView bottomIv;
		TextView bottomTv;
	}

}
