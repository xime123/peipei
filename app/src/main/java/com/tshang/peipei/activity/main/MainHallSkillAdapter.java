package com.tshang.peipei.activity.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfo;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: MainHallSkillAdapter.java 
 *
 * @Description: 获取技能
 *
 * @author allen  
 *
 * @date 2014-7-14 下午7:02:38 
 *
 * @version V1.0   
 */
public class MainHallSkillAdapter extends ArrayListAdapter<RetGGSkillInfo> {

	private DisplayImageOptions options;
	private Set<String> uidSkillSet = new HashSet<String>();

	public MainHallSkillAdapter(Activity context) {
		super(context);
		options = ImageOptionsUtils.GetHeadKeySmallRounded(context);
	}

	public void clearSet() {//清除set的值
		uidSkillSet.clear();
	}

	public ArrayList<RetGGSkillInfo> getDifferentSkillData(List<RetGGSkillInfo> list) {//去除重复的数据
		ArrayList<RetGGSkillInfo> newLists = new ArrayList<RetGGSkillInfo>();
		if (list != null && !list.isEmpty()) {
			for (RetGGSkillInfo retGGSkillInfo : list) {
				String strUidSkillId = retGGSkillInfo.hostuserinfo.uid.intValue() + "" + retGGSkillInfo.skillinfo.id.intValue();
				if (!uidSkillSet.contains(strUidSkillId)) {
					newLists.add(retGGSkillInfo);
					uidSkillSet.add(strUidSkillId);
				}
			}
		}
		return newLists;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_hall_skill_lv, parent, false);
			viewholer.skill_head = (ImageView) convertView.findViewById(R.id.item_hall_skill_head);
			viewholer.skill_nick = (TextView) convertView.findViewById(R.id.item_hall_skill_nick);
			viewholer.skill_name = (TextView) convertView.findViewById(R.id.item_hall_skill_name);
			viewholer.skill_content = (TextView) convertView.findViewById(R.id.item_hall_skill_content);
			viewholer.skill_num = (TextView) convertView.findViewById(R.id.item_hall_skill_join);
			viewholer.skill_rb = (RatingBar) convertView.findViewById(R.id.item_hall_skill_rb);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		RetGGSkillInfo info = (RetGGSkillInfo) mList.get(position);
		if (info != null) {
			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					info.hostuserinfo.uid.intValue());
			viewholer.skill_nick.setText(TextUtils.isEmpty(alias) ? new String(info.hostuserinfo.nick) : alias);

			viewholer.skill_name.setText(new String(info.skillinfo.title).trim());
			viewholer.skill_num.setText(String.format(mContext.getString(R.string.skill_join), info.skillinfo.participantnum));
			viewholer.skill_content.setText(new String(info.skillinfo.desc).trim());

			if (info.hostuserinfo.sex.intValue() == Gender.MALE.getValue()) {
				viewholer.skill_rb.setVisibility(View.INVISIBLE);
			} else {
				viewholer.skill_rb.setVisibility(View.VISIBLE);
				viewholer.skill_rb.setProgress(info.skillinfo.averagepoint.intValue());
			}

			viewholer.skill_head.setTag(new String(info.hostuserinfo.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR);
			imageLoader.displayImage("http://" + new String(info.hostuserinfo.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR,
					viewholer.skill_head, options);
		}
		return convertView;
	}

	final class ViewHoler {
		ImageView skill_head;
		TextView skill_nick;
		TextView skill_name;
		TextView skill_content;
		TextView skill_num;
		RatingBar skill_rb;
	}
}
