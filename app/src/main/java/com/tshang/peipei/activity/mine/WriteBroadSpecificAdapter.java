package com.tshang.peipei.activity.mine;

import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: ChatBottomAdapter.java 
 *
 * @Description: 私聊底部按键适配
 *
 * @author jeff 
 *
 * @date 2014-7-9 下午4:32:00 
 *
 * @version V1.0   
 */
public class WriteBroadSpecificAdapter extends ArrayListAdapter<String> {
	private String[] bottom;
	private Integer[] manIntegers = { R.drawable.magic_star_selector, R.drawable.magic_arrow_selector, R.drawable.magic_hear_selector,
			R.drawable.magic_change_selector, R.drawable.magic_love_selector };
	private Integer[] womanIntegers = { R.drawable.magic_slim_selector, R.drawable.magic_flower_selector, R.drawable.magic_red_selector,
			R.drawable.magic_boxing_selector, R.drawable.magic_box_selector };
	private int len = manIntegers.length;
	private int sex = Gender.MALE.getValue();

	private HashMap<Integer, Boolean> maps = new HashMap<Integer, Boolean>();
	private int selectCurrentPos = -1;

	public int getSelectCurrentPos() {
		return selectCurrentPos;
	}

	public boolean isSelect(int pos) {
		return maps.get(pos);
	}

	public void setSelectCurrentPos(int selectCurrentPos) {
		if (selectCurrentPos == -1) {
			this.selectCurrentPos = selectCurrentPos;
			for (int i = 0; i < len; i++) {
				maps.put(i, false);
			}
		} else {
			if (this.selectCurrentPos != selectCurrentPos) {
				this.selectCurrentPos = selectCurrentPos;
				for (int i = 0; i < len; i++) {
					if (selectCurrentPos == i) {
						maps.put(i, true);
					} else {
						maps.put(i, false);
					}
				}
			} else {

				if (isSelect(selectCurrentPos)) {
					maps.put(selectCurrentPos, false);
				} else {
					maps.put(selectCurrentPos, true);
				}
			}
		}
		notifyDataSetChanged();
	}

	public WriteBroadSpecificAdapter(Activity context) {
		super(context);
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(context);
		if (userInfo != null) {
			if (userInfo.sex.intValue() == Gender.MALE.getValue()) {
				bottom = context.getResources().getStringArray(R.array.specific_man_array);
				sex = Gender.MALE.getValue();
			} else {
				sex = Gender.FEMALE.getValue();
				bottom = context.getResources().getStringArray(R.array.specific_woman_array);
			}
			for (int i = 0; i < len; i++) {
				maps.put(i, false);
			}

			setList(bottom);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_specific, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.bottomIv = (ImageView) convertView.findViewById(R.id.item_gv_chat_image);
			viewHolder.bottomTv = (TextView) convertView.findViewById(R.id.item_gv_chat_tv);
			viewHolder.ivSelect = (ImageView) convertView.findViewById(R.id.item_gv_chat_select);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (selectCurrentPos == position && maps.get(position)) {
			viewHolder.ivSelect.setVisibility(View.VISIBLE);
		} else {
			viewHolder.ivSelect.setVisibility(View.GONE);
		}
		if (sex == Gender.MALE.getValue()) {
			viewHolder.bottomIv.setImageResource(manIntegers[position]);
		} else {
			viewHolder.bottomIv.setImageResource(womanIntegers[position]);
		}
		viewHolder.bottomTv.setText(bottom[position]);

		return convertView;
	}

	final class ViewHolder {
		ImageView ivSelect;
		ImageView bottomIv;
		TextView bottomTv;
	}

}
