package com.tshang.peipei.activity.space.adapter;

import java.math.BigInteger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.main.message.DynamicDetailsActivity;
import com.tshang.peipei.activity.main.message.DynamicOfficalActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.model.biz.user.DynamicRequseControl;
import com.tshang.peipei.model.request.RequestDynamicPraiseNumber.AppPariseCallBack;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.display.RoundedBitmapDisplayer;

/**
 * @Title: DynamicAdapter.java 
 *
 * @Description: 动态适配器 
 *
 * @author Aaron  
 *
 * @date 2015-8-12 上午11:51:54 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class DynamicAdapter extends ArrayListAdapter<DynamicsInfo> {

	private Context mContext;

	private boolean isOfficial;
	private int systemid;
	private int from;

	private DisplayImageOptions options_head;
	protected DisplayImageOptions options;

	private BAParseRspData parser;

	private BAHandler handler;

	private DynamicRequseControl control;

	private int[] officialBg = { R.drawable.dynamic_official_item_bg1, R.drawable.dynamic_official_item_bg2, R.drawable.dynamic_official_item_bg3,
			R.drawable.dynamic_official_item_bg4, R.drawable.dynamic_official_item_bg5, R.drawable.dynamic_official_item_bg6,
			R.drawable.dynamic_official_item_bg7, R.drawable.dynamic_official_item_bg8, R.drawable.dynamic_official_item_bg9 };

	private Dialog dialog;

	public DynamicAdapter(Activity context, BAHandler handler, boolean isOfficial, int systemid, int from) {
		super(context);
		this.mContext = context;
		this.isOfficial = isOfficial;
		this.systemid = systemid;
		options_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		options = ImageOptionsUtils.getImageKeyOptions(context);
		parser = new BAParseRspData();
		this.handler = handler;
		this.from = from;
		control = new DynamicRequseControl();
	}

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

		viewHolder.chatLayout.setOnClickListener(new onItemOnClick(info, position));
		viewHolder.replyLayout.setOnClickListener(new onItemOnClick(info, position));
		viewHolder.praiseLayout.setOnClickListener(new onItemOnClick(info, position));
		viewHolder.headiv.setOnClickListener(new onItemOnClick(info, position));
		int anonymous = info.isanonymous.intValue();
		if (isOfficial) {
			viewHolder.chatLayout.setVisibility(View.GONE);
			viewHolder.line.setVisibility(View.GONE);
		}

		GoGirlDataInfoList dataInfoList = info.dynamicscontentlist;
		final ContentData data = parser.parseTopicInfo(mContext, dataInfoList, info.sex.intValue());
		int styte = info.revint0.intValue();
		//判断动态类型，0-官方 ，1-用户
		if (styte == 0) {
			viewHolder.userTopsLayout.setVisibility(View.GONE);
			viewHolder.officialTopsLayout.setVisibility(View.VISIBLE);
			//通过服务配制背景图片
			int p = 0;
			String bgColor = data.getImageList().get(0);
			if ("30b063".equals(bgColor)) {
				p = 0;
			} else if ("eb713c".equals(bgColor)) {
				p = 1;
			} else if ("468cd3".equals(bgColor)) {
				p = 2;
			} else if ("ef4753".equals(bgColor)) {
				p = 3;
			} else if ("8c4cbe".equals(bgColor)) {
				p = 4;
			} else if ("be61c1".equals(bgColor)) {
				p = 5;
			} else if ("5252d2".equals(bgColor)) {
				p = 6;
			} else if ("75a338".equals(bgColor)) {
				p = 7;
			} else if ("dfb91f".equals(bgColor)) {
				p = 8;
			}
			viewHolder.officialTopsLayout.setBackgroundResource(officialBg[p]);//随机获取一个背景色
			viewHolder.imageView.setImageResource(R.drawable.dynamicinfo_defaultimage);
			viewHolder.contentTv.setText("#" + data.getContent() + "#");
			viewHolder.mButton.setText(info.replynum.intValue() + "条回答");
			viewHolder.mButton.setOnClickListener(new onItemOnClick(mList.get(position), position));
		} else if (styte == 1) {
			viewHolder.userTopsLayout.setVisibility(View.VISIBLE);
			viewHolder.officialTopsLayout.setVisibility(View.GONE);
			if (anonymous == 0) {//匿名
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

	/**
	 * 点击事件
	 * @author Aaron
	 *
	 */
	private class onItemOnClick implements OnClickListener {
		DynamicsInfo info;
		int position;

		public onItemOnClick(DynamicsInfo info, int position) {
			this.info = info;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dynamic_item_chat_layout://私聊
				ChatActivity
						.intentChatActivity((Activity) mContext, info.uid.intValue(), new String(info.nick), info.sex.intValue(), false, false, 0);
				break;
			case R.id.dynamic_btn://话题
				BAParseRspData parse = new BAParseRspData();
				final ContentData data = parse.parseTopicInfo(mContext, info.dynamicscontentlist, info.sex.intValue());
				Bundle bundle1 = new Bundle();
				bundle1.putString("title", data.getContent());
				bundle1.putInt("topicid", info.topicid.intValue());
				bundle1.putInt("systemid", info.topicid.intValue());
				BaseUtils.openActivity((Activity) mContext, DynamicOfficalActivity.class, bundle1);
				break;
			case R.id.dynamic_item_reply_layout://回复   动态详情
				Bundle bundle = new Bundle();
				bundle.putInt(DynamicDetailsActivity.TOPICID_FLAG, info.topicid.intValue());
				bundle.putInt(DynamicDetailsActivity.TOPIUID_FLAG, info.uid.intValue());
				bundle.putInt(DynamicDetailsActivity.ANONYMOUS_FLAG, info.isanonymous.intValue());
				bundle.putInt(DynamicDetailsActivity.TYPE_FLAG, from == 4 ? (1 | (1 << 30)) : info.dynamicstype.intValue());
				bundle.putInt(DynamicDetailsActivity.SYSTEMID_FLAG, systemid);
				bundle.putInt(DynamicDetailsActivity.STATE_FLAG, info.dynamicsstatus.intValue());
				bundle.putInt(DynamicDetailsActivity.FROM_FLAG, 0);
				BaseUtils.openActivity((Activity) mContext, DynamicDetailsActivity.class, bundle);
				break;
			case R.id.dynamic_item_praise_layout://点赞
				Log.d("Aaron", "createTime===" + info.createtime.intValue() + ", isParise=="
						+ SharedPreferencesTools.getInstance(mContext).getBooleanKeyValue(String.valueOf(info.createtime.intValue())));
				final boolean isParise = SharedPreferencesTools.getInstance(mContext).getBooleanKeyValue(String.valueOf(info.createtime.intValue()));
				int upvotenum = 1;
				if (isParise) {//如果是已经赞过了，取消点赞
					upvotenum = -1;
					dialog = DialogFactory.createLoadingDialog(mContext, "取消点赞...");
				} else {
					dialog = DialogFactory.createLoadingDialog(mContext, "发布点赞...");
				}
				DialogFactory.showDialog(dialog);
				/**
				 * 请求点赞
				 */
				control.appPriaseNum(info.topicid.intValue(), info.uid.intValue(), info.dynamicstype.intValue(), upvotenum, systemid,
						new AppPariseCallBack() {

							@Override
							public void onSuccess(int code, int topicuid, int topicid) {
								if (isParise) {
									SharedPreferencesTools.getInstance(mContext).saveBooleanKeyValue(false,
											String.valueOf(info.createtime.intValue()));//取消已经点赞过了
									int num = info.upvotenum.intValue() - 1;
									if (num < 0) {
										num = 0;
									}
									info.upvotenum = BigInteger.valueOf(num);
								} else {
									SharedPreferencesTools.getInstance(mContext)
											.saveBooleanKeyValue(true, String.valueOf(info.createtime.intValue()));//记录已经点赞过了
									int num = info.upvotenum.intValue() + 1;
									if (num < 0) {
										num = 0;
									}
									info.upvotenum = BigInteger.valueOf(num);
								}

								handler.post(new Runnable() {

									@Override
									public void run() {
										notifyDataSetChanged();
									}
								});
								DialogFactory.dimissDialog(dialog);
							}

							@Override
							public void onError(int code) {
								DialogFactory.dimissDialog(dialog);
								BaseUtils.showTost(mContext, "点赞失败!");
							}
						});
				break;
			case R.id.dynamic_head_iv://头像点击
				//匿名不做跳转
				if (info.isanonymous.intValue() == 0) {
					SpaceUtils.toSpaceCustom((Activity) mContext, info.uid.intValue(), info.sex.intValue());
				}
				break;

			default:
				break;
			}
		}
	}

	public boolean getIsParise(DynamicsInfo info) {
		Log.d("Aaron",
				"uid==" + info.uid + ", topicid=" + (info.dynamicstype.intValue() == 0 ? info.topicid.intValue() : info.relativetopic.intValue()));
		return SharedPreferencesTools.getInstance(mContext).getBooleanKeyValue(
				info.uid + "-" + (info.dynamicstype.intValue() == 0 ? info.topicid.intValue() : info.relativetopic.intValue()));
	}
}
