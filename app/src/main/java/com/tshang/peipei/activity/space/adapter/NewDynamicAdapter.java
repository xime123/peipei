package com.tshang.peipei.activity.space.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.space.SpaceUserDynamicActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.display.RoundedBitmapDisplayer;

/**
 * @Title: NewDynamicAdapter.java 
 *
 * @Description: 用户动态适配 
 *
 * @author Administrator  
 *
 * @date 2015-9-16 下午8:08:59 
 *
 * @version V1.0   
 */
@SuppressLint("ViewHolder")
public class NewDynamicAdapter extends ArrayListAdapter<DynamicsInfo> {

	private Context mContext;

	private DisplayImageOptions options_head;
	protected DisplayImageOptions options;

	private BAParseRspData parser;

	private int uid;

	public NewDynamicAdapter(Activity context, int uid) {
		super(context);
		this.mContext = context;
		this.uid = uid;
		options_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		options = ImageOptionsUtils.getImageKeyOptions(context);
		parser = new BAParseRspData();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dynamic_all_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.chatLayout = (LinearLayout) convertView.findViewById(R.id.dynamic_item_chat_layout);
			viewHolder.replyLayout = (LinearLayout) convertView.findViewById(R.id.dynamic_item_reply_layout);
			viewHolder.praiseLayout = (LinearLayout) convertView.findViewById(R.id.dynamic_item_praise_layout);
			viewHolder.nickTv = (TextView) convertView.findViewById(R.id.dynamic_nick_tv);
			viewHolder.timeTv = (TextView) convertView.findViewById(R.id.dynamic_time_tv);
			viewHolder.headiv = (ImageView) convertView.findViewById(R.id.dynamic_head_iv);
			viewHolder.replyNumber = (TextView) convertView.findViewById(R.id.dynamic_item_msg_number);
			viewHolder.pariseNumber = (TextView) convertView.findViewById(R.id.dynamic_item_reply_number);
			viewHolder.contentTv = (TextView) convertView.findViewById(R.id.dynamic_tv);
			viewHolder.mButton = (Button) convertView.findViewById(R.id.dynamic_btn);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.dynamic_iv);
			viewHolder.line = (View) convertView.findViewById(R.id.dynamic_item_line_1);
			viewHolder.officialTopsLayout = (RelativeLayout) convertView.findViewById(R.id.dynamic_official_tops);
			viewHolder.userTopsLayout = (LinearLayout) convertView.findViewById(R.id.dynamic_user_tops);
			viewHolder.userContentTV = (TextView) convertView.findViewById(R.id.dynamic_content_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final DynamicsInfo info = (DynamicsInfo) mList.get(position);
		viewHolder.chatLayout.setVisibility(View.GONE);

		GoGirlDataInfoList dataInfoList = info.dynamicscontentlist;
		final ContentData data = parser.parseTopicInfo(mContext, dataInfoList, info.sex.intValue());
		int styte = info.revint0.intValue();
		int anonymous = info.isanonymous.intValue();
		//判断动态类型，0-官方 ，1-用户
		if (styte == 1) {
			viewHolder.userTopsLayout.setVisibility(View.VISIBLE);
			viewHolder.officialTopsLayout.setVisibility(View.GONE);
			if (anonymous == 0 || BAApplication.mLocalUserInfo.uid.intValue() == info.uid.intValue()) {//匿名
				//显示备注
				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						info.uid.intValue());
				viewHolder.nickTv.setText(TextUtils.isEmpty(alias) ? new String(info.nick) : alias);
				String headKey = info.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
				imageLoader.displayImage("http://" + headKey, viewHolder.headiv, options_head);
			} else {
				viewHolder.nickTv.setText(mContext.getResources().getString(R.string.anonymous));
				if (info.sex.intValue() == 1) {
					options_head = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.dynamic_defalut_man).cacheOnDisk(false)
							.cacheInMemory(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
							.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(mContext, 27))).bitmapConfig(Bitmap.Config.RGB_565).build();
				} else {
					options_head = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.dynamic_defalut_woman).cacheOnDisk(false)
							.cacheInMemory(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
							.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(mContext, 27))).bitmapConfig(Bitmap.Config.RGB_565).build();
				}
				imageLoader.displayImage("http://" + 000, viewHolder.headiv, options_head);
			}
			String tt = BaseTimes.getTime(info.createtime.longValue() * 1000);
			viewHolder.timeTv.setText(tt);
			viewHolder.replyNumber.setText(info.replynum.intValue() + "");
			viewHolder.pariseNumber.setText(info.upvotenum.intValue() + "");
			if (info.dynamicsstatus.intValue() == 2 || BAApplication.mLocalUserInfo.uid.intValue() == info.uid.intValue()) {//图片审核通过
				String imgKey = data.getImageList().get(0) + "@false@500@500";
				imageLoader.displayImage("http://" + imgKey, viewHolder.imageView, options);
				viewHolder.imageView.setVisibility(View.VISIBLE);
				viewHolder.userContentTV.setVisibility(View.GONE);
			} else {//图片审核不通过
				String bgColor = new String(info.revstr0);//背景颜色值
				String content = data.getContent();//内容
				viewHolder.imageView.setVisibility(View.GONE);
				viewHolder.userContentTV.setVisibility(View.VISIBLE);
				viewHolder.userContentTV.setText(content);
				if (!TextUtils.isEmpty(bgColor)) {
					viewHolder.userContentTV.setBackgroundColor(Color.parseColor("#" + bgColor));
				} else {
					viewHolder.userContentTV.setBackgroundColor(mContext.getResources().getColor(R.color.official_bg1));
				}
				int fontType = info.fonttype.intValue();
				if (fontType == 0) {
					viewHolder.userContentTV.setTextColor(mContext.getResources().getColor(R.color.black));
					viewHolder.userContentTV.setShadowLayer(15, 5, 5, mContext.getResources().getColor(R.color.white));
				} else {
					viewHolder.userContentTV.setTextColor(mContext.getResources().getColor(R.color.white));
					viewHolder.userContentTV.setShadowLayer(15, 5, 5, mContext.getResources().getColor(R.color.black));
				}
			}
			//过虑用户与自己的动态
			if (BAApplication.mLocalUserInfo != null) {
				if (BAApplication.mLocalUserInfo.uid.intValue() == info.uid.intValue() || anonymous == 1) {
					viewHolder.chatLayout.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
				} else {
					viewHolder.chatLayout.setVisibility(View.VISIBLE);
					viewHolder.line.setVisibility(View.VISIBLE);
				}
			}
		}
		viewHolder.replyLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle2 = new Bundle();
				bundle2.putInt(SpaceUserDynamicActivity.UID_FLAG, uid);
				BaseUtils.openActivity((Activity) mContext, SpaceUserDynamicActivity.class, bundle2);
			}
		});
		viewHolder.praiseLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle2 = new Bundle();
				bundle2.putInt(SpaceUserDynamicActivity.UID_FLAG, uid);
				BaseUtils.openActivity((Activity) mContext, SpaceUserDynamicActivity.class, bundle2);
			}
		});
		viewHolder.chatLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChatActivity.intentChatActivity((Activity) mContext, info.uid.intValue(), new String(info.nick), info.sex.intValue(), false, false, 0);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		private LinearLayout chatLayout, replyLayout, praiseLayout;
		private TextView nickTv, timeTv, contentTv;
		private TextView replyNumber, pariseNumber;
		private ImageView headiv;
		private ImageView imageView;
		private View line;
		private TextView userContentTV;
		private LinearLayout userTopsLayout;
		private Button mButton;
		private RelativeLayout officialTopsLayout;
	}

}
