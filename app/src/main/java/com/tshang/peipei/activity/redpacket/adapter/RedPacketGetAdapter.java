package com.tshang.peipei.activity.redpacket.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.UserSimpleInfo;
import com.tshang.peipei.base.BaseTimes;

/**
 * @Title: RedPacketGetAdapter.java 
 *
 * @Description: 领取红包适配
 *
 * @author Jeff 
 *
 * @date 2014年10月20日 下午18:02:40 
 *
 * @version V1.4.0   
 */
public class RedPacketGetAdapter extends ArrayListAdapter<UserSimpleInfo> {

	public RedPacketGetAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_get_redpacket_user, parent, false);
			viewholer.tvName = (TextView) convertView.findViewById(R.id.tv_get_redpacket_username);
			viewholer.tvCoin = (TextView) convertView.findViewById(R.id.tv_get_redpacket_coin);
			viewholer.tvTime = (TextView) convertView.findViewById(R.id.tv_get_redpacket_time);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		UserSimpleInfo info = mList.get(position);
		if (info != null) {
			viewholer.tvName.setText(new String(info.strdata));
			viewholer.tvCoin.setText(String.valueOf(info.intdata.intValue()));
			viewholer.tvTime.setText(BaseTimes.getChatDiffTimeHHMM(info.createtime.longValue() * 1000));
		}
		return convertView;
	}

	final class ViewHoler {
		TextView tvName;
		TextView tvCoin;
		TextView tvTime;

	}

}
