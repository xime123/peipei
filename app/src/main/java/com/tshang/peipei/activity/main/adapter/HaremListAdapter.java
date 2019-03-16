package com.tshang.peipei.activity.main.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: HaremListAdapter.java 
 *
 * @Description: 后宫排行
 *
 * @author Aaron  
 *
 * @date 2016-1-21 下午3:09:52 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class HaremListAdapter extends ArrayListAdapter<GroupInfo> {

	private DisplayImageOptions options;
	private DisplayImageOptions HaremHeadoptions; //对加载的图片设置参数

	public HaremListAdapter(Activity context) {
		super(context);
		options = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		HaremHeadoptions = ImageOptionsUtils.GetHeadKeySmallRounded(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder = null;
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.harem_list_item_layout, null);

			mViewHolder.haremHead = (ImageView) convertView.findViewById(R.id.harem_head_iv);
			mViewHolder.haremName = (TextView) convertView.findViewById(R.id.harem_name_tv);
			mViewHolder.haremDes = (TextView) convertView.findViewById(R.id.harem_des_tv);
			mViewHolder.haremCreator = (TextView) convertView.findViewById(R.id.harem_ctreator_tv);
			mViewHolder.haremCreatorHead = (ImageView) convertView.findViewById(R.id.harem_ctreator_head_iv);
			mViewHolder.man = (TextView) convertView.findViewById(R.id.harem_man_tv);
			mViewHolder.woman = (TextView) convertView.findViewById(R.id.harem_woman_tv);
			mViewHolder.creatorTitle = (TextView) convertView.findViewById(R.id.harem_ctreator_title);

			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		GroupInfo groupInfo = mList.get(position);
		if (groupInfo != null) {
			mViewHolder.haremName.setText(new String(groupInfo.groupname));
			String desc = new String(groupInfo.groupnotice);
			if (TextUtils.isEmpty(desc)) {
				mViewHolder.haremDes.setText("暂没有宫告");
			} else {
				mViewHolder.haremDes.setText(desc);
			}
			mViewHolder.man.setText(groupInfo.membernum.intValue() + "/" + groupInfo.maxmembernum.intValue());
			//			mViewHolder.woman.setText(String.valueOf(groupInfo.womennum.intValue()));
			mViewHolder.haremCreator.setText(new String(groupInfo.ownernick));
			imageLoader.displayImage("http://" + groupInfo.owner.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR, mViewHolder.haremCreatorHead,
					options);
			String key = new String(groupInfo.groupbadgekey) + "@true@120@120";
			imageLoader.displayImage("http://" + key, mViewHolder.haremHead, HaremHeadoptions);
		}

		mViewHolder.creatorTitle.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, Color.parseColor("#FFC601"), 180,
				Color.parseColor("#FFC601")));
		mViewHolder.creatorTitle.setTextColor(mContext.getResources().getColor(R.color.white));
		return convertView;
	}

	private class ViewHolder {
		private ImageView haremHead;
		private TextView haremName;
		private TextView haremDes;
		private TextView haremCreator;
		private ImageView haremCreatorHead;
		private TextView man;
		private TextView woman;
		private TextView creatorTitle;
	}
}
