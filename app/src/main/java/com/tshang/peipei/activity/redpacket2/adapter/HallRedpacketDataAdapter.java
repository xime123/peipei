package com.tshang.peipei.activity.redpacket2.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: HallRedpacketDataAdapter.java 
 *
 * @Description: 大厅普通红包记录 
 *
 * @author DYH  
 *
 * @date 2016-1-20 下午4:52:20 
 *
 * @version V1.0   
 */
public class HallRedpacketDataAdapter extends ArrayListAdapter<BroadcastRedPacketInfo> {
	protected DisplayImageOptions options_uid_head;//通过UID加载
	private DisplayImageOptions gradeInfoOptions;

	public HallRedpacketDataAdapter(Activity context) {
		super(context);
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_hall_redpacket_data, null);
			viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
			viewHolder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
			viewHolder.tv_hall_desc = (TextView) convertView.findViewById(R.id.tv_hall_desc);
			viewHolder.tv_hall_redpacket_type = (TextView) convertView.findViewById(R.id.tv_hall_redpacket_type);
			viewHolder.iv_official = (ImageView) convertView.findViewById(R.id.iv_official);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BroadcastRedPacketInfo info = mList.get(position);
		if (info != null && info.userInfo != null) {
			if(info.type.intValue() == 1){
				viewHolder.tv_nick.setText(R.string.str_system_user);
				viewHolder.iv_level.setImageResource(R.drawable.broadcast_img_official);
				viewHolder.iv_head.setImageResource(R.drawable.logo_rounded);
				viewHolder.tv_hall_desc.setText(new String(info.desc));
				viewHolder.tv_hall_redpacket_type.setText(R.string.str_official_redpacket);
				viewHolder.iv_official.setVisibility(View.VISIBLE);
			}else{
				viewHolder.tv_nick.setText(new String(info.userInfo.nick));
				if(info.desc != null){
					viewHolder.tv_hall_desc.setText(new String(info.desc));
				}else{
					viewHolder.tv_hall_desc.setText("");
				}
				String gradeinfo = new String(info.userInfo.gradeinfo);
				if (!TextUtils.isEmpty(gradeinfo)) {
					viewHolder.iv_level.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, viewHolder.iv_level, gradeInfoOptions);
				} else {
					viewHolder.iv_level.setVisibility(View.GONE);
				}
				setHeadImage(viewHolder.iv_head, info.userInfo.uid.intValue());
				if(info.type.intValue() == 0){//用户红包
					viewHolder.iv_official.setVisibility(View.GONE);
					if(info.userInfo.nick != null){
						viewHolder.tv_hall_redpacket_type.setText(new String(info.userInfo.nick) + mContext.getString(R.string.str_someone_a_redpacket));
					}
				}else{//官方红包
					viewHolder.tv_nick.setText(R.string.str_system_user);
					viewHolder.iv_level.setImageResource(R.drawable.broadcast_img_official);
					viewHolder.iv_head.setImageResource(R.drawable.logo_rounded);
					viewHolder.tv_hall_desc.setText(new String(info.desc));
					viewHolder.tv_hall_redpacket_type.setText(R.string.str_official_redpacket);
					viewHolder.iv_official.setVisibility(View.VISIBLE);
				}
			}
		}

		return convertView;
	}
	
	public ArrayList<BroadcastRedPacketInfo> getHallRedpacketListData(List<BroadcastRedPacketInfo> list) {//去除重复的数据
		ArrayList<BroadcastRedPacketInfo> newLists = new ArrayList<BroadcastRedPacketInfo>();
		if (list != null && !list.isEmpty()) {
			for (BroadcastRedPacketInfo user : list) {
				newLists.add(user);
			}
		}		
		return newLists;
	}

	protected void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_uid_head);
	}

	protected void setHeadImage(ImageView iv, String uid) {
		imageLoader.displayImage("http://" + uid + "@true@210@210", iv, options_uid_head);
	}

	private class ViewHolder {
		public ImageView iv_head;
		public TextView tv_nick;
		public ImageView iv_level;
		public TextView tv_hall_desc;
		public TextView tv_hall_redpacket_type;
		public ImageView iv_official;
	}

}
