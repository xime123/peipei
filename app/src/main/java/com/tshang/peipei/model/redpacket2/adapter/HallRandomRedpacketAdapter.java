package com.tshang.peipei.model.redpacket2.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.vender.common.util.TimeUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: HallRandomRedpacketAdapter.java 
 *
 * @Description: 大厅拼手气红包记录 
 *
 * @author Administrator  
 *
 * @date 2016-1-19 下午8:28:20 
 *
 * @version V1.0   
 */
public class HallRandomRedpacketAdapter extends ArrayListAdapter<GoGirlUserInfo> {
	protected DisplayImageOptions options_uid_head;//通过UID加载
	private BroadcastRedPacketInfo info;
	
	public HallRandomRedpacketAdapter(Activity context) {
		super(context);
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
	}
	
	public HallRandomRedpacketAdapter(Activity context, BroadcastRedPacketInfo info) {
		super(context);
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		this.info = info;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_hall_random_redpacket, null);
			viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			viewHolder.tv_no1 = (TextView) convertView.findViewById(R.id.tv_no1);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		GoGirlUserInfo user = mList.get(position);
		if(user != null){
			viewHolder.tv_nick.setText(new String(user.nick));
			setHeadImage(viewHolder.iv_head, user.uid.intValue());
			viewHolder.tv_time.setText(TimeUtils.getHourMinuteTime(user.revint0.longValue()));
			if(info != null){
				String moneyType = "";
				if(info.totalgoldcoin.longValue() > 0){
					moneyType = mContext.getString(R.string.gold_money);
				}else{
					moneyType = mContext.getString(R.string.silver_money);
				}
				viewHolder.tv_money.setText(user.revint1.longValue() + moneyType);
				if(user.revint1.longValue() == info.maxgoldcoin.longValue()){
					viewHolder.tv_no1.setVisibility(View.VISIBLE);
				}else{
					viewHolder.tv_no1.setVisibility(View.GONE);
				}
				
				if(info.totalgoldcoin.longValue() == info.totalportionnum.longValue()
						|| info.totalsilvercoin.longValue() == info.totalportionnum.longValue() && info.totalportionnum.longValue() != 1){
					viewHolder.tv_no1.setVisibility(View.GONE);
				}
			}
		}
		return convertView;
	}
	
	protected void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_uid_head);
	}
	
	public ArrayList<GoGirlUserInfo> getHallRedpacketListData(BroadcastRedPacketInfo info, List<GoGirlUserInfo> list) {//去除重复的数据
		ArrayList<GoGirlUserInfo> newLists = new ArrayList<GoGirlUserInfo>();
		if(info != null){
			if(info.leftportionnum.intValue() >= 0){
				if (list != null && !list.isEmpty()) {
					GoGirlUserInfo no1 = null;
					for(int i=0; i<list.size(); i++){
						if(list.get(i).revint1.longValue() == info.maxgoldcoin.longValue()){
							no1 = list.get(i);
							list.remove(i);
						}
					}
					if(no1 != null){
						list.add(0, no1);
					}
					
					for (GoGirlUserInfo user : list) {
						newLists.add(user);
					}
				}
			}else{
				if (list != null && !list.isEmpty()) {
					for (GoGirlUserInfo user : list) {
						newLists.add(user);
					}
				}
			}
		}
		
		return newLists;
	}
	
	private class ViewHolder{
		public ImageView iv_head;
		public TextView tv_nick;
		public TextView tv_time;
		public TextView tv_money;
		public TextView tv_no1;
	}
}
