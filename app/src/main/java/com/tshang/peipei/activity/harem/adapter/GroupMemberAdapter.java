package com.tshang.peipei.activity.harem.adapter;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * @Title: ShowAllGiftAdapter.java 
 *
 * @Description: 三宫六院界面
 *
 * @author jeff
 *
 * @date 2014-9-20 下午2:21:16 
 *
 * @version V1.3.0   
 */
public class GroupMemberAdapter extends ArrayListAdapter<GroupMemberInfo> {

	private DisplayImageOptions options;

	private LinearLayout.LayoutParams linParams;

	private int count;

	public GroupMemberAdapter(Activity context) {
		super(context);
		int width = (BasePhone.getScreenWidth(context) - BaseUtils.dip2px(context, 36)) / 3;
		linParams = new LinearLayout.LayoutParams(width, width);
		options = ImageOptionsUtils.GetGroupHeadKeySmall(context);
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int getCount() {
		return count > 0 ? count : 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_group_member_item, parent, false);
			viewholer.memberHead = (ImageView) convertView.findViewById(R.id.iv_group_member_head);
			viewholer.memberHead.setLayoutParams(linParams);
			viewholer.memberName = (TextView) convertView.findViewById(R.id.tv_group_member_name);

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
				imageLoader.displayImage("http://" + info.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR, viewholer.memberHead, options);
			}
		} else {
			viewholer.memberName.setText("");
			viewholer.memberHead.setImageDrawable(new BitmapDrawable());
		}
		return convertView;
	}

	final class ViewHoler {
		ImageView memberHead;
		TextView memberName;
	}

}
