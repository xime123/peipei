package com.tshang.peipei.activity.redpacket.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
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
public class RedPacketDeliverAndReceiverAdapter extends ArrayListAdapter<RedPacketInfo> {

	public RedPacketDeliverAndReceiverAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_deliver_and_receiver_redpacekt_item, parent, false);
			viewholer.tvName = (TextView) convertView.findViewById(R.id.tv_redpacket_user_name);
			viewholer.tvCoin = (TextView) convertView.findViewById(R.id.tv_redpacket_money);
			viewholer.tvTime = (TextView) convertView.findViewById(R.id.tv_redpacket_time);
			viewholer.tvStatue = (TextView) convertView.findViewById(R.id.tv_redpacket_status);
			viewholer.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		RedPacketInfo info = mList.get(position);
		if (info != null) {
			viewholer.tvName.setText(new String(info.desc));
			viewholer.tvCoin.setText(String.valueOf(info.totalgoldcoin));
			viewholer.tvTime.setText(BaseTimes.getChatDiffTimeMMDD(info.createtime.longValue() * 1000));
			int endtime = info.endtime.intValue();
			String status = mContext.getString(R.string.str_redpacket_getting);
			if (info.leftportionnum.intValue() == 0) {
				status = mContext.getString(R.string.str_redpacketget_complete);
			} else {
				if (endtime > 0) {
					status = mContext.getString(R.string.str_redpacket_time_out);
				}
			}
			viewholer.tvStatue.setText(status);
			viewholer.tvCount.setText((info.totalportionnum.intValue() - info.leftportionnum.intValue()) + "/" + info.totalportionnum.intValue());
		}
		return convertView;
	}

	final class ViewHoler {
		TextView tvName;
		TextView tvCoin;
		TextView tvTime;
		TextView tvStatue;
		TextView tvCount;

	}

}
