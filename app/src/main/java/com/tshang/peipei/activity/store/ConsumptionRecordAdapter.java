package com.tshang.peipei.activity.store;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDaybookInfo;
import com.tshang.peipei.base.BaseTimes;

/**
 * @Title: ConsumptionRecordAdapter.java 
 *
 * @Description: 消费记录适配
 *
 * @author allen  
 *
 * @date 2014-4-24 上午10:36:38 
 *
 * @version V1.0   
 */
public class ConsumptionRecordAdapter extends ArrayListAdapter<GoGirlDaybookInfo> {

	public ConsumptionRecordAdapter(Activity context, List<GoGirlDaybookInfo> list) {
		super(context);
		mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record, parent, false);
			viewholer.recordname = (TextView) convertView.findViewById(R.id.record_item_name);
			viewholer.recordView = (ImageView) convertView.findViewById(R.id.record_item_image);
			viewholer.recordtime = (TextView) convertView.findViewById(R.id.record_item_time);
			viewholer.recordprice = (TextView) convertView.findViewById(R.id.record_item_price);
			//			viewholer.recordfrom = (TextView) convertView.findViewById(R.id.record_item_from);

			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		GoGirlDaybookInfo bookInfo = mList.get(position);
		if (bookInfo != null) {
			viewholer.recordname.setText(new String(bookInfo.log));

			viewholer.recordtime.setText(BaseTimes.getChatDiffTimeYYMDHM(bookInfo.createtime.longValue() * 1000));

			int price = bookInfo.tonum.intValue() - bookInfo.fromnum.intValue();
			if (price > 0) {
				viewholer.recordView.setBackgroundResource(R.drawable.consumption_img_in);
				viewholer.recordprice.setText("+ " + price);
				convertView.setVisibility(View.VISIBLE);
			} else if (price < 0) {
				viewholer.recordView.setBackgroundResource(R.drawable.consumption_img_out);
				viewholer.recordprice.setText(" " + price);
				convertView.setVisibility(View.VISIBLE);
			}

			//			if (bookInfo.resultby == null) {
			//			viewholer.recordfrom.setVisibility(View.GONE);
			//			} else {
			//				viewholer.recordfrom.setVisibility(View.VISIBLE);
			//				viewholer.recordfrom.setText(new String(bookInfo.resultby));
			//			}

		}

		return convertView;
	}

	final class ViewHoler {
		TextView recordname;
		ImageView recordView;
		TextView recordtime;
		TextView recordprice;
		//		TextView recordfrom;
	}

}
