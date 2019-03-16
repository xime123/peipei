package com.tshang.peipei.activity.main.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.display.RoundedBitmapDisplayer;

/**
 *
 * @Description: 0.00
 *
 * @author Jeff 
 *
 * @version V1.1   
 */
public class ShowRoomsAdapter extends ArrayListAdapter<ShowRoomInfo> {
	private DisplayImageOptions options;
	private RelativeLayout.LayoutParams params2;
	private RelativeLayout.LayoutParams params1;
	private static final int official_value = 1;//说明是官方的房间
	private static final int player_value = 0;//玩家的房间

	public ShowRoomsAdapter(Activity context) {
		super(context);
		int screenWidth = BasePhone.getScreenWidth(context);
		int height = (screenWidth - BaseUtils.dip2px(context, 70)) / 4;
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.casino_pic).cacheOnDisk(true).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).displayer(new RoundedBitmapDisplayer(height / 12))
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		params2 = new RelativeLayout.LayoutParams(height, height);
		params1 = new RelativeLayout.LayoutParams(height, LayoutParams.WRAP_CONTENT);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			row = inflater.inflate(R.layout.adapter_show_room_item, null);
			holder = new ViewHolder();
			holder.iv_head = (ImageView) row.findViewById(R.id.iv_show_room_user);
			holder.tv_status = (TextView) row.findViewById(R.id.tv_show_room_status);
			holder.ll_bottom = (LinearLayout) row.findViewById(R.id.ll_show_room_item_bottom);
			holder.iv_offical = (ImageView) row.findViewById(R.id.iv_show_room_offical);

			holder.iv_status = (ImageView) row.findViewById(R.id.iv_show_room_status);
			holder.rl_show_room = (RelativeLayout) row.findViewById(R.id.rl_show_room_item);
			holder.iv_head.setLayoutParams(params2);
			holder.ll_bottom.setLayoutParams(params1);
			params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();

		}

		ShowRoomInfo info = mList.get(position);
		if (info != null) {
			if (info.getIsofficial() == official_value) {//官方房间
				holder.iv_offical.setVisibility(View.VISIBLE);
			} else if (info.getIsofficial() == player_value) {//普通玩家可以抢的房间
				holder.iv_offical.setVisibility(View.INVISIBLE);
			}

			String headKey = new String(info.getOwneruserinfo().getHeadpickey().toByteArray());

			if (info.getLefttime() > 0) {
				holder.ll_bottom.setVisibility(View.VISIBLE);
				holder.tv_status.setText("在秀");
				AnimationDrawable anim = (AnimationDrawable) holder.iv_status.getBackground();
				anim.start();
				if (TextUtils.isEmpty(headKey)) {
					holder.iv_head.setImageResource(R.drawable.casino_pic_1);
				} else {
					imageLoader.displayImage("http://" + headKey + BAConstants.LOAD_210_IDENTITY, holder.iv_head, options);
				}
			} else {
				imageLoader.displayImage("http://" + headKey + BAConstants.LOAD_210_IDENTITY, holder.iv_head, options);
				holder.ll_bottom.setVisibility(View.GONE);
			}
		}

		return row;
	}

	final class ViewHolder {
		ImageView iv_offical;
		ImageView iv_head;
		TextView tv_status;
		LinearLayout ll_bottom;
		ImageView iv_status;
		RelativeLayout rl_show_room;
	}

}
