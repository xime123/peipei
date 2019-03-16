package com.tshang.peipei.activity.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserInfoAndSkillInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: MainHallAdapter.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Jeff 
 *
 * @date 2014年12月22日 下午9:09:48 
 *
 * @version V2.0   
 */
public class MainHallAdapter extends ArrayListAdapter<UserAndSkillInfo> {
	private DisplayImageOptions gradeInfoOptions;
	private Set<Integer> uidSet = new HashSet<Integer>();
	private LinearLayout.LayoutParams params;

	public void clearSet() {//清除之前的数据
		uidSet.clear();
	}

	public ArrayList<UserAndSkillInfo> getDifferentUserInfoData(List<UserAndSkillInfo> list) {//去除重复的数据
		ArrayList<UserAndSkillInfo> newLists = new ArrayList<UserAndSkillInfo>();
		if (list != null && !list.isEmpty()) {
			for (UserAndSkillInfo goGirlUserInfo : list) {
				if (!uidSet.contains(goGirlUserInfo.userinfo.uid.intValue())) {
					newLists.add(goGirlUserInfo);
					uidSet.add(goGirlUserInfo.userinfo.uid.intValue());
				}
			}
		}
		return newLists;
	}

	public MainHallAdapter(Activity context) {
		super(context);
		options_head = ImageOptionsUtils.GetHeadKeyBigRounded(context);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
		params = new LinearLayout.LayoutParams(getSkillWidth(), LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 5, 5, 0);
	}

	private DisplayImageOptions options_head;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_friends_item, parent, false);
			viewholer.headerImage = (ImageView) convertView.findViewById(R.id.item_head);
			viewholer.ivLevel = (ImageView) convertView.findViewById(R.id.iv_level);
			viewholer.sexImage = (ImageView) convertView.findViewById(R.id.iv_item_sex);
			viewholer.nameText = (TextView) convertView.findViewById(R.id.tv_name);
			viewholer.tvAge = (TextView) convertView.findViewById(R.id.tv_age);
			viewholer.timeText = (TextView) convertView.findViewById(R.id.tv_time);
			viewholer.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
			viewholer.ivOnlineFlag = (ImageView) convertView.findViewById(R.id.iv_online_flag);
			viewholer.ll_distance = (LinearLayout) convertView.findViewById(R.id.ll_distance);
			viewholer.identityImage = (ImageView) convertView.findViewById(R.id.item_head_identity);
			viewholer.ll_skill = (LinearLayout) convertView.findViewById(R.id.ll_skill);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		UserAndSkillInfo info = mList.get(position);
		GoGirlUserInfo userInfo = info.userinfo;
//		SkillTextInfoList skillInfo = info.skillTextInfoList;
		if (userInfo != null) {
			String key = new String(userInfo.headpickey) + "@true@210@210";
			imageLoader.displayImage("http://" + key, viewholer.headerImage, options_head);

			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					userInfo.uid.intValue());
			String userName = TextUtils.isEmpty(alias) ? new String(userInfo.nick) : alias;

			viewholer.nameText.setText(userName);
			String mBirthday = new String(BaseTimes.getFormatTime(userInfo.birthday.longValue() * 1000));
			viewholer.tvAge.setText(OperateViewUtils.calculateAge(mBirthday) + " " + OperateViewUtils.getConstellation(mBirthday));

			if (userInfo.gradeinfo != null) {
				String gradeinfo = new String(userInfo.gradeinfo);
				if (!TextUtils.isEmpty(gradeinfo)) {
					viewholer.ivLevel.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, viewholer.ivLevel, gradeInfoOptions);
				} else {
					viewholer.ivLevel.setVisibility(View.GONE);
				}

			}
			if (userInfo.islocale.intValue() > 0) {
				viewholer.tvDistance.setVisibility(View.VISIBLE);
				viewholer.ll_distance.setVisibility(View.VISIBLE);
				if (userInfo.sex.intValue() == Gender.FEMALE.getValue()) {
					viewholer.tvDistance.setBackgroundResource(R.drawable.makefriend_kilometer_women);
				} else {
					viewholer.tvDistance.setBackgroundResource(R.drawable.makefriend_kilometer_man);
				}
				viewholer.tvDistance.setPadding((int) mContext.getResources().getDimension(R.dimen.default_padding_gridview_edge), 0, (int) mContext
						.getResources().getDimension(R.dimen.default_padding_gridview_edge), 0);
				viewholer.tvDistance.setText(BaseTimes.getDistance(userInfo.islocale.intValue()));
			} else {
				viewholer.tvDistance.setVisibility(View.INVISIBLE);
				//				viewholer.ll_distance.setVisibility(View.GONE);
			}
			if (userInfo.lastlogtime.intValue() > 0) {
				viewholer.timeText.setText(R.string.online);
				viewholer.ivOnlineFlag.setVisibility(View.VISIBLE);
				viewholer.timeText.setTextColor(mContext.getResources().getColor(R.color.near_online_text_color));
			} else {
				viewholer.ivOnlineFlag.setVisibility(View.INVISIBLE);
				long time = Math.abs(userInfo.lastlogtime.longValue()) * 1000;
				//				viewholer.timeText.setText(BaseTimes.getDiffTime(time) + "");
				viewholer.timeText.setText(R.string.offline);
				viewholer.timeText.setTextColor(mContext.getResources().getColor(R.color.gray));
			}
			if (userInfo.sex.intValue() == Gender.FEMALE.getValue()) {
				viewholer.sexImage.setImageResource(R.drawable.broadcast_img_girl);
				//				OperateViewUtils.setTextViewLeftDrawable(mContext, R.drawable.icon_female, viewholer.tvAge);
				//				viewholer.tvAge.setBackgroundResource(R.drawable.activity_space_age_female_bg);
			} else {
				viewholer.sexImage.setImageResource(R.drawable.broadcast_img_boy);
				//				OperateViewUtils.setTextViewLeftDrawable(mContext, R.drawable.icon_male, viewholer.tvAge);
				//				viewholer.tvAge.setBackgroundResource(R.drawable.activity_space_age_male_bg);
			}
			if (userInfo != null && userInfo.authpickey != null && !TextUtils.isEmpty(new String(userInfo.authpickey))
					&& (userInfo.userstatus.intValue() & SwitchStatus.GG_US_AUTH_FLAG) > 0) {
				viewholer.identityImage.setVisibility(View.VISIBLE);
			} else {
				viewholer.identityImage.setVisibility(View.GONE);
			}
		}

//		if (skillInfo != null) {
//			viewholer.ll_skill.removeAllViews();
//			viewholer.ll_skill.setVisibility(View.VISIBLE);
//			ArrayList<SkillTextInfo> skillList = getSkillListData(skillInfo);
//			if (skillList.size() > 3) {
//				for (int i = 0; i < 3; i++) {
//					SkillTextInfo item = skillList.get(i);
//					TextView tv = new TextView(mContext);
//					tv.setText(new String(item.content));
//					tv.setBackgroundDrawable(BaseUtils.createGradientDrawable(2, Color.parseColor("#" + new String(item.framecolor)), 180, mContext
//							.getResources().getColor(R.color.transparent)));
//					tv.setTextColor(Color.parseColor("#" + new String(item.textcolor)));
//					setTextView(tv);
//					viewholer.ll_skill.addView(tv);
//				}
//			} else {
//				for (SkillTextInfo item : skillList) {
//					TextView tv = new TextView(mContext);
//					tv.setText(new String(item.content));
//					tv.setBackgroundDrawable(BaseUtils.createGradientDrawable(2, Color.parseColor("#" + new String(item.framecolor)), 180, mContext
//							.getResources().getColor(R.color.transparent)));
//					tv.setTextColor(Color.parseColor("#" + new String(item.textcolor)));
//					setTextView(tv);
//					viewholer.ll_skill.addView(tv);
//				}
//			}
//		} else {
//			viewholer.ll_skill.setVisibility(View.GONE);
//		}
		return convertView;
	}

	private void setTextView(TextView tv) {
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(12);
		tv.setPadding(5, 5, 5, 5);
		tv.setSingleLine(true);
		tv.setEllipsize(TruncateAt.END);
		tv.setLayoutParams(params);
	}

	final class ViewHoler {
		ImageView headerImage;
		ImageView identityImage;
		ImageView ivLevel;
		ImageView sexImage;
		TextView nameText;
		TextView tvAge;
		TextView timeText;
		TextView tvDistance;
		ImageView ivOnlineFlag;
		LinearLayout ll_distance;
		LinearLayout ll_skill;
	}

	private ArrayList<SkillTextInfo> getSkillListData(List<SkillTextInfo> list) {//去除重复的数据
		ArrayList<SkillTextInfo> newLists = new ArrayList<SkillTextInfo>();
		if (list != null && !list.isEmpty()) {
			for (SkillTextInfo skillTextInfo : list) {
				newLists.add(skillTextInfo);
			}
		}
		return newLists;
	}

	private int getSkillWidth() {
		int width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		if (width >= 1080) {
			width = (width / 3) - 100;
		} else if (width >= 720) {
			width = (width / 3) - 85;
		} else {
			width = (width / 3) - 70;
		}
		return width;
	}

}
