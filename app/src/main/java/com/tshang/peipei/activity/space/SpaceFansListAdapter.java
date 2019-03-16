package com.tshang.peipei.activity.space;

import java.util.HashMap;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.RemarkDialog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.view.SortModel;

/*
 *类        名 : SpaceCustomAdapter.java
 *功能描述 : 客人态 个人中心 ADAPTER
 *作　    者 : vactor
 *设计日期 : 2014-3-27 下午1:38:52
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class SpaceFansListAdapter extends ArrayListAdapter<SortModel> {
	private DisplayImageOptions options;
	private BAHandler mHandler;
	private boolean isAlias;
	private OnItemFans onItemFans;

	public SpaceFansListAdapter(Activity context, BAHandler mHandler, boolean b) {
		super(context);
		options = ImageOptionsUtils.GetHeadKeySmallRounded(context);
		this.mHandler = mHandler;
		isAlias = b;
	}

	private boolean isManage = false;

	private HashMap<String, RetFollowInfo> followInfoMap = new HashMap<String, RetFollowInfo>();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fans_list, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.item_attention_iv_head);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.item_attention_tv_nick);
			mViewholer.tvGlamour = (TextView) convertView.findViewById(R.id.item_attention_tv_loyal);
			mViewholer.ivCheck = (ImageView) convertView.findViewById(R.id.item_attention_iv_check);
			mViewholer.btnAlias = (Button) convertView.findViewById(R.id.item_attenion_btn_remark);
			mViewholer.layout = (RelativeLayout) convertView.findViewById(R.id.item_attention_iv_layout);
			mViewholer.tvLetter = (TextView) convertView.findViewById(R.id.item_attention_iv_sibe);
			mViewholer.ivSex = (ImageView) convertView.findViewById(R.id.iv_attention_sex);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}

		final RetFollowInfo info = mList.get(position).getInfo();
		final GoGirlUserInfo userInfo = info.followuserinfo;
		String key = new String(userInfo.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR;
		mViewholer.ivHead.setTag(key);
		imageLoader.displayImage("http://" + key, mViewholer.ivHead, options);

		String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				userInfo.uid.intValue());

		mViewholer.tvNick.setText(TextUtils.isEmpty(alias) ? new String(userInfo.nick) : alias);
		mViewholer.tvGlamour.setText(info.loyalty.intValue() + "");

		if (isManage) {
			mViewholer.ivCheck.setVisibility(View.VISIBLE);

			if (null != followInfoMap.get(userInfo.uid.intValue() + "")) {
				mViewholer.ivCheck.setBackgroundResource(R.drawable.album_img_choose_pr);
			} else {
				mViewholer.ivCheck.setBackgroundResource(R.drawable.album_img_choose_un);
			}

		} else {
			mViewholer.ivCheck.setVisibility(View.GONE);
		}

		if (isAlias) {
			mViewholer.btnAlias.setVisibility(View.VISIBLE);
		} else {
			mViewholer.btnAlias.setVisibility(View.GONE);
		}

		mViewholer.layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemFans != null)
					onItemFans.onItem(userInfo);
			}
		});

		mViewholer.btnAlias.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						userInfo.uid.intValue());
				String sendUserName = TextUtils.isEmpty(alias) ? new String(info.followuserinfo.nick) : alias;

				new RemarkDialog(mContext, R.string.ok, R.string.cancel, info.followuserinfo.uid.intValue(), 1, sendUserName, new String(
						info.followuserinfo.nick), mHandler).showDialog();
			}
		});

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
		return convertView;
	}

	public boolean isManage() {
		return isManage;
	}

	//显示打勾
	public void freshApdaterByManage() {
		this.isManage = true;
		notifyDataSetChanged();
	}

	//退出打勾
	public void freshAdapterExitManage() {
		this.isManage = false;
		notifyDataSetChanged();
	}

	//选中
	public void freshAdapterByCheck(HashMap<String, RetFollowInfo> map) {
		this.followInfoMap = map;
		notifyDataSetChanged();
	}

	final class ViewHoler {
		ImageView ivHead;
		ImageView ivCheck;
		TextView tvNick;
		TextView tvGlamour;
		Button btnAlias;
		RelativeLayout layout;
		TextView tvLetter;
		ImageView ivSex;
	}

	public void setOnFansListener(OnItemFans de) {
		onItemFans = de;
	}

	public interface OnItemFans {
		public void onItem(GoGirlUserInfo userInfo);
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
