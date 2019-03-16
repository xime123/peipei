package com.tshang.peipei.activity.harem.adapter;

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
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 *
 * @Description: 后宫对应的适配器
 *
 * @author Jeff 
 *
 * @version V1.3.0   
 */
public class HaremGroupAdapter extends ArrayListAdapter<GroupInfo> {
	private DisplayImageOptions options;
	private int friendUid;

	public HaremGroupAdapter(Activity context, int friendUid) {
		super(context);
		options = ImageOptionsUtils.GetGroupHeadKeySmallRounded(context);
		this.friendUid = friendUid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			row = inflater.inflate(R.layout.adapter_harem_group_item, null);
			holder = new ViewHolder();
			holder.headerImage = (ImageView) row.findViewById(R.id.iv_harem_group_head_item);
			holder.nametxt = (TextView) row.findViewById(R.id.tv_harem_group_name_item);
			holder.ownerTxt = (TextView) row.findViewById(R.id.tv_harem_owner_item);
			holder.iv_isOwner = (ImageView) row.findViewById(R.id.iv_isowner);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		GroupInfo info = mList.get(position);
		if (info != null) {
			byte[] badgekeys = info.groupbadgekey;
			if (badgekeys != null) {
				String badgeKey = new String(badgekeys);
				if (!TextUtils.isEmpty(badgeKey)) {
					imageLoader.displayImage("http://" + badgeKey + "@true@120@120", holder.headerImage, options);
				}
			}

			byte[] groupNames = info.groupname;

			if (groupNames != null) {
				String groupName = new String(groupNames);
				if (!TextUtils.isEmpty(groupName)) {
					holder.nametxt.setText(groupName + "( " + info.membernum.intValue() + "/" + info.maxmembernum.intValue() + " )");
				}
			}

			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					info.owner.intValue());
			String sendUserName = TextUtils.isEmpty(alias) ? new String(info.ownernick) : alias;

			holder.ownerTxt.setText("宫主:" + sendUserName);
			if (info.owner.intValue() == friendUid) {
				holder.iv_isOwner.setImageResource(R.drawable.homepage_myharem);
			} else {
				holder.iv_isOwner.setImageResource(R.drawable.homepage_joinharem);
			}
		}

		return row;
	}

	final class ViewHolder {
		ImageView headerImage;
		TextView nametxt;
		TextView ownerTxt;
		ImageView iv_isOwner;

	}

}
