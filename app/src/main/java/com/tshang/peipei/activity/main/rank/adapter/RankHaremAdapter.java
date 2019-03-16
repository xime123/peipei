package com.tshang.peipei.activity.main.rank.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: RankHaremAdapter.java 
 *
 * @Description: 后宫排行适配器 
 *
 * @author Jeff 
 *
 * @date 2014年7月29日 下午9:02:40 
 *
 * @version V1.5.0   
 */
public class RankHaremAdapter extends ArrayListAdapter<GroupInfo> {

	private DisplayImageOptions options; //对加载的图片设置参数

	private Set<Integer> uidSet = new HashSet<Integer>();

	public void clearSet() {//清除之前的数据
		uidSet.clear();
	}

	public ArrayList<GroupInfo> getDifferentUserInfoData(List<GroupInfo> list) {//去除重复的数据
		ArrayList<GroupInfo> newLists = new ArrayList<GroupInfo>();
		if (list != null && !list.isEmpty()) {
			for (GroupInfo goGirlUserInfo : list) {
				if (!uidSet.contains(goGirlUserInfo.groupid.intValue())) {
					newLists.add(goGirlUserInfo);
					uidSet.add(goGirlUserInfo.groupid.intValue());
				}
			}
		}
		return newLists;
	}

	public RankHaremAdapter(Activity context) {
		super(context);
		options = ImageOptionsUtils.GetHeadKeySmallRounded(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_rank_harem, parent, false);
			viewholer.ivHead = (ImageView) convertView.findViewById(R.id.item_rank_queen_iv_head);
			viewholer.tvRank = (TextView) convertView.findViewById(R.id.item_rank_queen_tv_rank);
			viewholer.tvNick = (TextView) convertView.findViewById(R.id.item_rank_queen_tv_nick);
			viewholer.tvTitle = (TextView) convertView.findViewById(R.id.item_rank_queen_title);
			viewholer.tvNum = (TextView) convertView.findViewById(R.id.item_rank_queen_num);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		GroupInfo info = mList.get(position);
		String key = new String(info.groupbadgekey) + "@true@120@120";
		imageLoader.displayImage("http://" + key, viewholer.ivHead, options);
		viewholer.tvNick.setText(new String(info.groupname));

		String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				info.owner.intValue());
		String sendUserName = TextUtils.isEmpty(alias) ? new String(info.ownernick) : alias;

		viewholer.tvTitle.setText("宫主:" + sendUserName);
		viewholer.tvRank.setText(String.valueOf(position + 1));
		viewholer.tvNum.setText("活跃度：" + info.ranknum.intValue());
		return convertView;
	}

	final class ViewHoler {
		ImageView ivGradeInfo;
		ImageView ivHead;
		ImageView ivFlag;
		TextView tvRank;
		TextView tvNick;
		TextView tvGlamour;
		TextView tvTitle;
		TextView tvGold;
		LinearLayout ll_glamour;
		LinearLayout ll_gold;
		TextView tvNum;

	}

}
