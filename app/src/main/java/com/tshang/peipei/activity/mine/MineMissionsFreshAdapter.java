package com.tshang.peipei.activity.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.GGTaskInfo;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;

/**
 * @Title: MineMissionsFreshAdapter.java 
 *
 * @Description: 新手任务适配器
 *
 * @author allen  
 *
 * @date 2014-7-18 下午1:54:30 
 *
 * @version V1.0   
 */
public class MineMissionsFreshAdapter extends ArrayListAdapter<GGTaskInfo> {

	private boolean isDaily;

	public MineMissionsFreshAdapter(Activity context, boolean b) {
		super(context);
		isDaily = b;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mission_listview, parent, false);
			viewholer.missionName = (TextView) convertView.findViewById(R.id.item_mission_content);
			viewholer.missionHint = (TextView) convertView.findViewById(R.id.item_mission_hint);
			viewholer.missionMonkey = (TextView) convertView.findViewById(R.id.item_mission_monkey);
			viewholer.missionContent = (TextView) convertView.findViewById(R.id.item_mission_content_hint);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}
		GGTaskInfo info = mList.get(position);
		viewholer.missionName.setText(new String(info.desc));
		viewholer.missionContent.setText(new String(info.opdesc));
		if (info.rewardgoldcoin.intValue() > 0) {
			viewholer.missionMonkey.setBackgroundResource(R.drawable.person_earn_icon_gold);
			viewholer.missionMonkey.setTextColor(mContext.getResources().getColor(R.color.orange));
			viewholer.missionMonkey.setText(info.rewardgoldcoin.intValue() + "");
		} else if (info.rewardsilvercoin.intValue() > 0) {
			viewholer.missionMonkey.setBackgroundResource(R.drawable.person_earn_icon_silver);
			viewholer.missionMonkey.setTextColor(mContext.getResources().getColor(R.color.gray));
			viewholer.missionMonkey.setText(info.rewardsilvercoin.intValue() + "");
		}

		int status = info.status.intValue() & SwitchStatus.GG_US_CHAT_PUSH_FLAG;
		if (status != 0) {
			viewholer.missionMonkey.setBackgroundResource(R.drawable.person_earn_icon_dis);
			viewholer.missionMonkey.setTextColor(mContext.getResources().getColor(R.color.gray1));
		}

		String hint = info.finishnum.intValue() + "/" + info.standardnum.intValue();

		if (isDaily) {
			viewholer.missionHint.setText(hint);
			viewholer.missionHint.setVisibility(View.VISIBLE);
		} else {
			viewholer.missionHint.setVisibility(View.GONE);
		}

		return convertView;
	}

	final class ViewHoler {
		TextView missionName, missionHint, missionMonkey, missionContent;
	}

}
