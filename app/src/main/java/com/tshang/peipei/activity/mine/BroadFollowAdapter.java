package com.tshang.peipei.activity.mine;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.view.SortModel;

/*
 *功能描述 : 客人态 个人中心 ADAPTER
 *作　    者 : vactor
 *设计日期 : 2014-3-27 下午1:38:52
 *修改日期 : 2014-05-28
 *修  改   人: Jeff
 *修 改内容: 
 */
public class BroadFollowAdapter extends ArrayListAdapter<SortModel> {

	private DisplayImageOptions options;

	public BroadFollowAdapter(Activity context) {
		super(context);
		options = ImageOptionsUtils.GetHeadKeySmallRounded(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_attention_list, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.item_attention_iv_head);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.item_attention_tv_nick);
			mViewholer.tvLoyal = (TextView) convertView.findViewById(R.id.item_attention_tv_loyal);
			mViewholer.btnDelete = (Button) convertView.findViewById(R.id.item_attenion_btn_del);
			mViewholer.btnAlias = (Button) convertView.findViewById(R.id.item_attenion_btn_remark);
			mViewholer.layout = (LinearLayout) convertView.findViewById(R.id.item_attention_iv_layout);
			mViewholer.tvLetter = (TextView) convertView.findViewById(R.id.item_attention_iv_sibe);
			mViewholer.ivSex = (ImageView) convertView.findViewById(R.id.iv_attention_sex);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}

		final RetFollowInfo info = mList.get(position).getInfo();
		if (info != null) {
			final GoGirlUserInfo userInfo = info.followuserinfo;
			if (userInfo != null) {
				String key = new String(userInfo.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR;
				imageLoader.displayImage("http://" + key, mViewholer.ivHead, options);

				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						userInfo.uid.intValue());
				mViewholer.tvNick.setText(TextUtils.isEmpty(alias) ? new String(userInfo.nick) : alias);
				mViewholer.tvLoyal.setText(info.loyalty.intValue() + "");

				mViewholer.btnDelete.setVisibility(View.GONE);

				mViewholer.btnAlias.setVisibility(View.GONE);
			}

			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);

			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				mViewholer.tvLetter.setVisibility(View.VISIBLE);
				mViewholer.tvLetter.setText(mList.get(position).getSortLetters());
			} else {
				mViewholer.tvLetter.setVisibility(View.GONE);
			}
			
			if (userInfo.sex.intValue() == Gender.FEMALE.getValue()) {
				mViewholer.ivSex.setImageResource(R.drawable.broadcast_img_girl);
			} else {
				mViewholer.ivSex.setImageResource(R.drawable.broadcast_img_boy);
			}

		}
		return convertView;
	}

	final class ViewHoler {
		ImageView ivHead;
		TextView tvNick;
		TextView tvLoyal;
		Button btnDelete;
		Button btnAlias;
		LinearLayout layout;
		TextView tvLetter;
		ImageView ivSex;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return mList.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}
}
