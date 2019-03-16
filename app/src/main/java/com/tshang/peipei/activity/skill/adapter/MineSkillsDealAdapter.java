package com.tshang.peipei.activity.skill.adapter;

import java.math.BigInteger;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GGSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.SkillStutas;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: MineSkillsDealAdapter.java 
 *
 * @Description: 技能列表适配器
 *
 * @author allen  
 *
 * @date 2014-10-22 下午7:44:52 
 *
 * @version V1.0   
 */
public class MineSkillsDealAdapter extends ArrayListAdapter<SkillDealInfo> {

	protected DisplayImageOptions options;

	public MineSkillsDealAdapter(Activity context) {
		super(context);

		options = ImageOptionsUtils.GetHeadUidSmallRounded(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_skill_order, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.item_skill_order_iv_head);
			mViewholer.tvType = (TextView) convertView.findViewById(R.id.item_skill_order_type);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.item_skill_order_name);
			mViewholer.tvCreateTime = (TextView) convertView.findViewById(R.id.item_skill_order_time);
			mViewholer.tvContent = (TextView) convertView.findViewById(R.id.item_skill_order_content);
			mViewholer.ivStatus = (ImageView) convertView.findViewById(R.id.item_skill_order_status);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}

		SkillDealInfo info = mList.get(position);
		if (info != null) {
			GGSkillInfo skillinfo = info.skillinfo;
			if (BAApplication.mLocalUserInfo.uid.intValue() == info.skilluid.intValue()) {
				imageLoader
						.displayImage("http://" + info.participateuid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR, mViewholer.ivHead, options);

				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						info.participateuid.intValue());
				String userName = TextUtils.isEmpty(alias) ? new String(info.participatenick) : alias;
				if (skillinfo.type.intValue() == Gender.FEMALE.getValue()) {
					mViewholer.tvContent.setText(mContext.getString(R.string.str_skill_order_content1) + userName);
				} else {
					mViewholer.tvContent.setText(mContext.getString(R.string.str_skill_order_content3) + userName);
				}
			} else {
				imageLoader.displayImage("http://" + info.skilluid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR, mViewholer.ivHead, options);
				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						info.skilluid.intValue());
				String userName = TextUtils.isEmpty(alias) ? new String(info.skillnick) : alias;

				if (skillinfo.type.intValue() == Gender.FEMALE.getValue()) {
					mViewholer.tvContent.setText(mContext.getString(R.string.str_skill_order_content2) + userName);
				} else {
					mViewholer.tvContent.setText(mContext.getString(R.string.str_skill_order_content4) + userName);
				}
			}
			if (skillinfo.type.intValue() == Gender.FEMALE.getValue()) {
				mViewholer.tvType.setText(R.string.str_skill_order_type1);
				mViewholer.tvType.setBackgroundResource(R.drawable.person_order_tag_bg_pleaseenjoy);
			} else if (skillinfo.type.intValue() == Gender.MALE.getValue()) {
				mViewholer.tvType.setText(R.string.str_skill_order_type2);
				mViewholer.tvType.setBackgroundResource(R.drawable.person_order_tag_bg_reward);
			}

			if (info.step.intValue() == SkillStutas.GG_SKILL_DEAL_STEP_END || info.step.intValue() == SkillStutas.GG_SKILL_DEAL_STEP_REFUSE
					|| info.step.intValue() == SkillStutas.GG_SKILL_DEAL_STEP_RECLAIN
					|| info.step.intValue() == SkillStutas.GG_SKILL_DEAL_STEP_APPEALED || info.step.intValue() == SkillStutas.GG_SKILL_DEAL_STEP_MARK) {
				mViewholer.ivStatus.setBackgroundResource(R.drawable.person_img_order_list_end);
			} else if (info.step.intValue() == SkillStutas.GG_SKILL_DEAL_STEP_START) {
				mViewholer.ivStatus.setBackgroundResource(R.drawable.person_img_order_list_confirming);
			} else {
				mViewholer.ivStatus.setBackgroundResource(R.drawable.person_img_order_list_underway);
			}

			mViewholer.tvNick.setText(new String(skillinfo.title));
			mViewholer.tvCreateTime.setText(BaseTimes.getChatDiffTimeYYMDHM(info.createtime.longValue() * 1000));
		}

		return convertView;
	}

	final class ViewHoler {
		ImageView ivHead;
		TextView tvType;
		TextView tvNick;
		TextView tvCreateTime;
		TextView tvContent;
		ImageView ivStatus;
	}

	public void setStepbyId(int step, int skilldealid) {
		for (SkillDealInfo info : mList) {
			if (info.id.intValue() == skilldealid) {
				info.step = BigInteger.valueOf(step);
			}
		}
		notifyDataSetChanged();
	}

}
