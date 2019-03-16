package com.tshang.peipei.activity.skill.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.protocol.asn.gogirl.RetParticipantInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: 客人太访问男人的技能
 *
 * @Description: 匹配相册列表相应的数据
 *
 * @author Jeff
 *
 * @version V1.4.0   
 */
public class MaleSkillInterestinGuestAdapter extends ArrayListAdapter<RetParticipantInfo> {

	private RelativeLayout.LayoutParams linParams;

	private DisplayImageOptions options;

	public MaleSkillInterestinGuestAdapter(Activity context) {
		super(context);
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(mContext, 110)) / 5;
		linParams = new RelativeLayout.LayoutParams(width, width);

		options = ImageOptionsUtils.GetHeadKeyBigRounded(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_male_skill_interestin_guest_item, parent, false);
			mViewholer.imageview = (ImageView) convertView.findViewById(R.id.adapter_interestin_item_iv);
			mViewholer.imageview.setLayoutParams(linParams);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}

		RetParticipantInfo info = mList.get(position);
		if (info != null) {
			String key = new String(info.participantuserinfo.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR;
			imageLoader.displayImage("http://" + key, mViewholer.imageview, options);

		}
		return convertView;
	}

	final class ViewHoler {
		ImageView imageview;
	}

}
