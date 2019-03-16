package com.tshang.peipei.activity.chat.adapter;

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
import com.tshang.peipei.base.FormatUtil;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: SolitaireAdapter.java 
 *
 * @Description: 用于展示可以抢的大厅接龙红包 
 *
 * @author DYH  
 *
 * @date 2015-12-14 下午6:48:43 
 *
 * @version V1.0   
 */
public class HallSolitaireAdapter extends ArrayListAdapter<RedPacketBetCreateInfo> {
	protected DisplayImageOptions options_uid_head;//通过UID加载
	private DisplayImageOptions gradeInfoOptions;

	public HallSolitaireAdapter(Activity context) {
		super(context);
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_solitaire_item, null);
			viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
			viewHolder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
			viewHolder.tv_redpacket_money = (TextView) convertView.findViewById(R.id.tv_redpacket_money);
			viewHolder.tv_jion_person = (TextView) convertView.findViewById(R.id.tv_jion_person);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		RedPacketBetCreateInfo info = mList.get(position);
		if (info != null) {
			if (info.orggoldcoin.longValue() > 0) {
				viewHolder.tv_redpacket_money.setText(mContext.getString(R.string.str_hall_current_money, mContext.getString(R.string.gold_money),
						FormatUtil.formatNumber(info.totalgoldcoin.longValue())));
			} else {
				viewHolder.tv_redpacket_money.setText(mContext.getString(R.string.str_hall_current_money, mContext.getString(R.string.silver_money),
						FormatUtil.formatNumber(info.totalgoldcoin.longValue())));
			}

			if (info.records == null || info.records.size() == 0) {
				viewHolder.tv_jion_person.setText(mContext.getString(R.string.str_has_join_person, 0));
				setHeadImage(viewHolder.iv_head, info.createuid.intValue());
				viewHolder.tv_nick.setText(new String(info.userInfo.nick));

				String gradeinfo = new String(info.userInfo.gradeinfo);
				if (!TextUtils.isEmpty(gradeinfo)) {
					viewHolder.iv_level.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, viewHolder.iv_level, gradeInfoOptions);
				} else {
					viewHolder.iv_level.setVisibility(View.GONE);
				}

			} else {
				viewHolder.tv_jion_person.setText(mContext.getString(R.string.str_has_join_person, info.records.size()));
				GoGirlUserInfo userinfo = (GoGirlUserInfo) info.records.get(info.records.size() - 1);
				if (userinfo != null) {
					setHeadImage(viewHolder.iv_head, userinfo.uid.intValue());
					viewHolder.tv_nick.setText(new String(userinfo.nick));

					String gradeinfo = new String(userinfo.gradeinfo);
					if (!TextUtils.isEmpty(gradeinfo)) {
						viewHolder.iv_level.setVisibility(View.VISIBLE);
						GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, viewHolder.iv_level, gradeInfoOptions);
					} else {
						viewHolder.iv_level.setVisibility(View.GONE);
					}

				}
			}
		}

		return convertView;
	}

	protected void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_uid_head);
	}

	protected void setHeadImage(ImageView iv, String uid) {
		imageLoader.displayImage("http://" + uid + "@true@210@210", iv, options_uid_head);
	}

	public ArrayList<RedPacketBetCreateInfo> getHallSolitaireRedpacketListData(List<RedPacketBetCreateInfo> list) {//去除重复的数据
		ArrayList<RedPacketBetCreateInfo> newLists = new ArrayList<RedPacketBetCreateInfo>();
		if (list != null && !list.isEmpty()) {
			for (RedPacketBetCreateInfo user : list) {
				newLists.add(user);
			}
		}		
		return newLists;
	}
	
	private class ViewHolder {
		public ImageView iv_head;
		public TextView tv_nick;
		public ImageView iv_level;
		public TextView tv_redpacket_money;
		public TextView tv_jion_person;
	}

}
