package com.tshang.peipei.activity.harem.adapter;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfo;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: SelectMemberAdapter.java 
 *
 * @Description: 翻牌子
 *
 * @author jeff
 *
 * @date 2014-9-20 下午2:21:16 
 *
 * @version V1.3.0   
 */
public class SelectMemberAdapter extends ArrayListAdapter<GroupMemberInfo> {

	private DisplayImageOptions options;

	private LinearLayout.LayoutParams imgParams;
	private AbsListView.LayoutParams linPareams;
	private int count;

	public SelectMemberAdapter(Activity context) {
		super(context);
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 128)) / 3;
		imgParams = new LinearLayout.LayoutParams(width, width);
		int linWidth = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 16)) / 3;
		int linHeight = linWidth * 14 / 11;
		linPareams = new AbsListView.LayoutParams(linWidth, linHeight);
		options = ImageOptionsUtils.GetGroupHeadKeySmall(context);
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int getCount() {
		return count > 0 ? count : 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_select_user_item, parent, false);
			viewholer.memberHead = (ImageView) convertView.findViewById(R.id.iv_group_member_head);
			viewholer.memberHead.setLayoutParams(imgParams);
			viewholer.memberName = (TextView) convertView.findViewById(R.id.tv_group_member_name);
			viewholer.menberLayout = (LinearLayout) convertView.findViewById(R.id.ll_group_member_layout);
			viewholer.menberLayout.setLayoutParams(linPareams);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		if (position < mList.size()) {
			GroupMemberInfo info = mList.get(position);
			if (info != null) {
				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						info.uid.intValue());
				String sendUserName = TextUtils.isEmpty(alias) ? new String(info.nick) : alias;

				viewholer.memberName.setText(sendUserName);
				imageLoader.displayImage("http://" + info.uid.intValue() + BAConstants.LOAD_HEAD_UID_GROUP, viewholer.memberHead, options);
			}
		} else {
			viewholer.menberLayout.setBackgroundResource(R.drawable.my_harem_brand_box_default);
			viewholer.memberName.setText("");
			viewholer.memberHead.setImageDrawable(new BitmapDrawable());
		}
		return convertView;
	}

	final class ViewHoler {
		ImageView memberHead;
		TextView memberName;
		LinearLayout menberLayout;
	}

}
