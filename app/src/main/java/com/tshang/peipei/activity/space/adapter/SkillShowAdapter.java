package com.tshang.peipei.activity.space.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.skill.SkillCreateActivity;
import com.tshang.peipei.protocol.asn.gogirl.GGSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;

/**
 * 
 * @author Jeff
 *
 */
public class SkillShowAdapter extends ArrayListAdapter<RetGGSkillInfo> {
	private LinearLayout.LayoutParams linParams;
	private boolean isHost = false;

	public SkillShowAdapter(Activity context, int uid) {
		super(context);
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(mContext, 15)) / 4;
		linParams = new LinearLayout.LayoutParams(width, width);
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(context);
		if (userEntity != null) {
			if (userEntity.uid.intValue() == uid) {
				isHost = true;
			}
		}
	}

	@Override
	public int getCount() {
		if (!isHost) {//非主人态就不需要有一个加号
			if (mList != null)
				return mList.size();
			return 0;
		}
		if (mList == null) {
			return 1;
		}
		if (mList.size() >= 4) {
			return 4;
		}
		return mList.size() + 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_skill, parent, false);
			mViewholer.tv_name = (TextView) convertView.findViewById(R.id.tv_item_skill_title);
			mViewholer.tv_join_people = (TextView) convertView.findViewById(R.id.tv_item_skill_join_people);
			mViewholer.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingbar);
			mViewholer.rl_bg = (RelativeLayout) convertView.findViewById(R.id.rl_skill_bg);
			mViewholer.rl_bg.setLayoutParams(linParams);
			mViewholer.ll_bg = (LinearLayout) convertView.findViewById(R.id.ll_skill_bg);
			mViewholer.btn_addSkill = (Button) convertView.findViewById(R.id.btn_add_skill);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}
		if (position % 4 == 0) {
			mViewholer.ll_bg.setBackgroundResource(R.color.skill_blue_color);
		} else if (position % 4 == 1) {
			mViewholer.ll_bg.setBackgroundResource(R.color.skill_red_color);
		} else if (position % 4 == 2) {
			mViewholer.ll_bg.setBackgroundResource(R.color.skill_purple_color);
		} else if (position % 4 == 3) {
			mViewholer.ll_bg.setBackgroundResource(R.color.skill_yellow_color);
		}
		if (mList != null && position < mList.size()) {
			RetGGSkillInfo retGGSkillInfo = mList.get(position);
			if (retGGSkillInfo != null) {
				GGSkillInfo ggSkillInfo = retGGSkillInfo.skillinfo;
				if (ggSkillInfo != null) {
					mViewholer.tv_name.setText(new String(ggSkillInfo.title));
					mViewholer.tv_join_people.setText(ggSkillInfo.participantnum.intValue() + "人参与");
					mViewholer.ratingBar.setProgress(ggSkillInfo.averagepoint.intValue());
				}

			}
			mViewholer.btn_addSkill.setVisibility(View.GONE);
			if (retGGSkillInfo.hostuserinfo.sex.intValue() == Gender.MALE.getValue()) {//男的技能不显示评分
				mViewholer.ratingBar.setVisibility(View.GONE);
			} else {
				mViewholer.ratingBar.setVisibility(View.VISIBLE);
			}
		} else {
			if (isHost) {
				mViewholer.btn_addSkill.setVisibility(View.VISIBLE);
				mViewholer.ratingBar.setVisibility(View.GONE);
			} else {
				mViewholer.btn_addSkill.setVisibility(View.GONE);
			}
		}
		if (mList != null && mList.size() == 0 && !isHost) {
			mViewholer.btn_addSkill.setVisibility(View.GONE);
		}
		mViewholer.btn_addSkill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseUtils.openResultActivity(mContext, SkillCreateActivity.class, null, 100);
			}
		});

		return convertView;
	}

	final class ViewHoler {
		TextView tv_name;
		TextView tv_join_people;
		RatingBar ratingBar;
		LinearLayout ll_bg;
		RelativeLayout rl_bg;
		Button btn_addSkill;

	}

}
