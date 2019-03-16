/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.tshang.peipei.activity.main.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.activity.listener.OnLevelClickListener;
import com.tshang.peipei.activity.listener.OnPartTextPlayBackClickListener;
import com.tshang.peipei.activity.listener.OnPartTextUserClickLlistener;
import com.tshang.peipei.activity.listener.OnUserClickListener;
import com.tshang.peipei.activity.listener.PartMagicListener;
import com.tshang.peipei.activity.main.adapter.MainBroadCastAdapter.IenterShowRoom;
import com.tshang.peipei.activity.mine.MineWriteBroadCastActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.base.textview.BaseTextUtils;
import com.tshang.peipei.model.broadcast.BroadCastBiz;
import com.tshang.peipei.model.broadcast.BroadCastUtils;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.model.broadcast.ItemVoiceClickListener;
import com.tshang.peipei.model.broadcast.WriteBroadCastBiz;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ShowShareBroadcastInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.FileUtils;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

public class TopBroadCastAdapter extends RecyclingPagerAdapter {

	private static final int SMALL_GIFT_FLAG = 1 << 20;
	private static final int MIDDLE_GIFT_FLAG = 1 << 21;
	private static final int BIG_GIFT_FLAG = 1 << 22;
	//帽子全以女性为标识
	private static final int QUEEN_GIFT_FLAG = 1 << 23;
	private static final int TRIAL_GIFT_FLAG = 1 << 24;
	private static final int FLOWERS_GIFT_FLAG = 1 << 25;
	private static final int NINE_GIFT_FLAG = 1 << 26;
	private static final int UPPER_GIFT_FLAG = 1 << 27;
	private static final int SEVEN_GIFT_FLAG = 1 << 28;

	//头像挂件
	private static final int DELIVER_GIFT_FOUR_FLAG = 1 << 19;
	private static final int DELIVER_GIFT_THREE_FLAG = 1 << 18;
	private static final int DELIVER_GIFT_TWO_FLAG = 1 << 17;
	private static final int DELIVER_GIFT_ONE_FLAG = 1 << 16;

	private Activity mContext;
	private List<BroadcastInfo> mList = new ArrayList<BroadcastInfo>();
	private boolean isInfiniteLoop;
	private DisplayImageOptions options;
	private DisplayImageOptions deliverOptions;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions gradeInfoOptions;

	private String playFileName = "";
	private BroadCastBiz broadCastBiz;
	private RelativeLayout.LayoutParams params1;
	private RelativeLayout.LayoutParams params2;
	private RelativeLayout.LayoutParams params3;

	private IenterShowRoom iEnterShowRoom;
	private DisplayImageOptions campaignHatOptions;

	private static final String PLAYBACK = "　回放　";
	private int imageSize = 24;

	public void setiEnterShowRoom(IenterShowRoom iEnterShowRoom) {
		this.iEnterShowRoom = iEnterShowRoom;
	}

	public void setPlayFileName(String playFileName) {
		this.playFileName = playFileName;
		notifyDataSetChanged();
	}

	public int getItemTypeColor(int position) {
		BroadcastInfo broadcastInfo = mList.get(getPosition(position));
		if (broadcastInfo != null) {

			byte[] datalist = broadcastInfo.datalist;
			if (datalist != null && datalist.length != 0) {
				GoGirlDataInfoList infoList = new GoGirlDataInfoList();
				BERDecoder dec = new BERDecoder(datalist);
				try {
					infoList.decode(dec);
				} catch (ASN1Exception e1) {
					e1.printStackTrace();
				}
				if (!infoList.isEmpty()) {
					GoGirlDataInfo datainfo = (GoGirlDataInfo) infoList.get(0);
					if (datainfo != null) {
						return datainfo.type.intValue();

					}
				}
			}
		}
		return 0;
	}

	public void setList(List<BroadcastInfo> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	private int mChildCount = 0;

	@Override
	public void notifyDataSetChanged() {//解决刷新问题 
		mChildCount = mList.size();
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

	public void removePos(int pos) {
		mList.remove(pos);
		notifyDataSetChanged();
	}

	public void appendPositionToList(int pos, BroadcastInfo t) {
		mList.add(pos, t);
		notifyDataSetChanged();
	}

	public List<BroadcastInfo> getmList() {
		return mList;
	}

	public void setmList(List<BroadcastInfo> mList) {
		this.mList = mList;
	}

	public void clearList() {
		mList.clear();
		notifyDataSetChanged();
	}

	public TopBroadCastAdapter(Activity context, BroadCastBiz broadCastBiz) {
		this.mContext = context;
		isInfiniteLoop = true;
		options = ImageOptionsUtils.GetHeadKeySmallRounded(context);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
		deliverOptions = ImageOptionsUtils.getDeliverGiftOptions(mContext);
		this.broadCastBiz = broadCastBiz;
		params1 = new RelativeLayout.LayoutParams(BaseUtils.dip2px(context, 80), RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2 = new RelativeLayout.LayoutParams(BaseUtils.dip2px(context, 120), RelativeLayout.LayoutParams.WRAP_CONTENT);
		params3 = new RelativeLayout.LayoutParams(BaseUtils.dip2px(context, 160), RelativeLayout.LayoutParams.WRAP_CONTENT);
		campaignHatOptions = ImageOptionsUtils.getCampaignHatOptions(context);
		imageSize = BaseUtils.dip2px(context, 24);
	}

	@Override
	public int getCount() {
		int size = ListUtils.getSize(mList);
		if (size == 0) {
			return 0;
		}
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(mList);
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		int size = ListUtils.getSize(mList);
		if (size == 0) {
			return 0;
		}
		return isInfiniteLoop ? position % size : position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		OnLevelClickListener levelListener = null;
		OnUserClickListener onHeadViewClick = null;
		OnUserClickListener onNameViewClick = null;
		if (row == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			row = inflater.inflate(R.layout.adapter_broadcast_item, null);
			holder = new ViewHolder();
			holder.headerImage = (ImageView) row.findViewById(R.id.item_broadcast_head);
			holder.deliverIv = (ImageView) row.findViewById(R.id.item_broadcast_head_deliver_gift_iv);
			holder.typeNametxt = (TextView) row.findViewById(R.id.tv_broadcast_item_type_name);
			holder.contentTxt = (TextView) row.findViewById(R.id.tv_broadcast_item_type_content);
			holder.timeTxt = (TextView) row.findViewById(R.id.tv_broadcast_item_type_time);
			holder.tv_voice_time = (TextView) row.findViewById(R.id.tv_broadcast_item_type_voice_time);
			holder.ll_voice_play = (LinearLayout) row.findViewById(R.id.ll_broadcast_item_type_voice);
			holder.iv_voice_play = (ImageView) row.findViewById(R.id.iv_broadcast_item_voice_play);
			holder.iv_voice_listen = (ImageView) row.findViewById(R.id.iv_broadcast_item_voice_islisten);
			holder.sexImg = (ImageView) row.findViewById(R.id.iv_broadcast_item_sex);
			holder.typeImg = (ImageView) row.findViewById(R.id.iv_broadcast_item_type);
			holder.iv_77_small_gift = (ImageView) row.findViewById(R.id.iv_broadcast_item_small_77_gift);
			holder.iv_77_big_gift = (ImageView) row.findViewById(R.id.iv_broadcast_item_big_77_gift);
			holder.progressBar = (ProgressBar) row.findViewById(R.id.pb_load_voice);
			holder.identityImage = (ImageView) row.findViewById(R.id.item_broadcast_head_identify);
			holder.ll_bg = (LinearLayout) row.findViewById(R.id.ll_broadcast_bg);
			holder.iv_decree = (ImageView) row.findViewById(R.id.iv_decree);
			holder.iv_bottom_line = (ImageView) row.findViewById(R.id.iv_braodcast_bottom_line);
			holder.iv_changer = (ImageView) row.findViewById(R.id.iv_broadcast_item_charge);
			holder.privilege = (ImageView) row.findViewById(R.id.iv_broadcast_item_privilege);
			levelListener = new OnLevelClickListener(mContext);
			holder.iv_77_small_gift.setOnClickListener(levelListener);//防止重复new 对象
			onHeadViewClick = new OnUserClickListener(mContext, OnUserClickListener.FLAG_SPACE);
			holder.headerImage.setOnClickListener(onHeadViewClick);
			onNameViewClick = new OnUserClickListener(mContext, OnUserClickListener.FLAG_WRITE_BROADCAST);
			holder.typeNametxt.setOnClickListener(onNameViewClick);
			row.setTag(holder);
			row.setTag(holder.iv_77_small_gift.getId(), levelListener);
			row.setTag(holder.headerImage.getId(), onHeadViewClick);
			row.setTag(holder.typeNametxt.getId(), onNameViewClick);
		} else {
			holder = (ViewHolder) row.getTag();
			levelListener = (OnLevelClickListener) row.getTag(holder.iv_77_small_gift.getId());
			onHeadViewClick = (OnUserClickListener) row.getTag(holder.headerImage.getId());
			onNameViewClick = (OnUserClickListener) row.getTag(holder.typeNametxt.getId());
		}
		holder.iv_bottom_line.setVisibility(View.GONE);
		BroadcastInfo broadcastInfo = mList.get(getPosition(position));
		if (broadcastInfo != null) {
			GoGirlUserInfo userInfo = broadcastInfo.senduser;
			if (userInfo != null && userInfo.authpickey != null && !TextUtils.isEmpty(new String(userInfo.authpickey))
					&& (userInfo.userstatus.intValue() & SwitchStatus.GG_US_AUTH_FLAG) > 0) {
				holder.identityImage.setVisibility(View.VISIBLE);
			} else {
				holder.identityImage.setVisibility(View.GONE);
			}
			if ((userInfo.userstatus.intValue() & SwitchStatus.GG_US_FIRST_RECHARGE_FLAG) > 0) {
				holder.iv_changer.setVisibility(View.VISIBLE);
			} else {
				holder.iv_changer.setVisibility(View.GONE);
			}

			if (userInfo.gradeinfo != null) {
				String gradeinfo = new String(userInfo.gradeinfo);
				if (!TextUtils.isEmpty(gradeinfo)) {
					holder.iv_77_small_gift.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, holder.iv_77_small_gift, gradeInfoOptions);
				} else {
					holder.iv_77_small_gift.setVisibility(View.GONE);
				}

			}
			StringBuffer sb = new StringBuffer();
			String[] usersStr = null;
			GoGirlUserInfoList userInfoList = broadcastInfo.tousers;
			if (!ListUtils.isEmpty(userInfoList)) {//@的用户
				int len = userInfoList.size();
				usersStr = new String[len];
				for (int i = 0; i < len; i++) {
					GoGirlUserInfo info = (GoGirlUserInfo) userInfoList.get(i);
					if (info != null) {
						String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark")
								.getAlias(info.uid.intValue());
						String userName = TextUtils.isEmpty(alias) ? new String(info.nick) : alias;
						usersStr[i] = "@" + userName;
						sb.append("@").append(userName);
					}
				}
			}
			String content = new String(broadcastInfo.contenttxt);
			String voice = new String(broadcastInfo.voiceinfo);
			byte[] datalist = broadcastInfo.datalist;
			if (datalist != null && datalist.length != 0) {
				GoGirlDataInfoList infoList = new GoGirlDataInfoList();
				BERDecoder dec = new BERDecoder(datalist);

				try {
					infoList.decode(dec);
				} catch (ASN1Exception e1) {
					e1.printStackTrace();
				}
				if (!infoList.isEmpty()) {
					GoGirlDataInfo datainfo = (GoGirlDataInfo) infoList.get(0);
					if (datainfo != null) {
						if (datainfo.type.intValue() == MessageType.BROADCASTCOLOR.getValue()
								|| datainfo.type.intValue() == MessageType.FEMALE_DECREE.getValue()
								|| datainfo.type.intValue() == MessageType.MALE_DECREE.getValue()) {//广播特殊颜色数据
							holder.iv_voice_listen.setVisibility(View.GONE);
							holder.ll_voice_play.setVisibility(View.GONE);
							holder.contentTxt.setVisibility(View.VISIBLE);
							int textColor = datainfo.datainfo.intValue();
							if (textColor == MineWriteBroadCastActivity.BROADCAST_TEXT_COLOR_ONE) {
								holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.broadcast_text_color_one));
							} else if (textColor == MineWriteBroadCastActivity.BROADCAST_TEXT_COLOR_TWO) {
								holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.broadcast_text_color_two));
							} else if (textColor == MineWriteBroadCastActivity.BROADCAST_TEXT_COLOR_THREE) {
								holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.broadcast_text_color_three));
							} else if (textColor == MineWriteBroadCastActivity.BROADCAST_TEXT_COLOR_BLACK) {
								holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.black));
							}
							content = new String(datainfo.data);
							if (!TextUtils.isEmpty(sb.toString()) && usersStr != null) {//有@的用户让其名字变色

								SpannableString style = getStyle1(sb.toString() + content, usersStr, userInfoList);
								holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
								holder.contentTxt.setText(style);

							} else {
								String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
								SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode,
										HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
								holder.contentTxt.setText(builder);
							}
							if (datainfo.type.intValue() == MessageType.FEMALE_DECREE.getValue()) {
								holder.iv_decree.setImageResource(R.drawable.broadcast_toplist_print_women);
							} else if (datainfo.type.intValue() == MessageType.MALE_DECREE.getValue()) {
								holder.iv_decree.setImageResource(R.drawable.broadcast_toplist_print_man);
							} else {
								holder.iv_decree.setImageDrawable(null);
							}

						} else if (datainfo.type.intValue() == MessageType.SHOWSHAREBROADCASTINFO.getValue()) {

							ShowShareBroadcastInfo showShareBroadcastInfo = BroadCastUtils.getShowShareBroadcastInfo(datainfo);

							holder.iv_voice_listen.setVisibility(View.GONE);
							holder.ll_voice_play.setVisibility(View.GONE);
							holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.black));
							holder.contentTxt.setVisibility(View.VISIBLE);
							if (showShareBroadcastInfo != null) {//005bd7
								String showUserName = new String(showShareBroadcastInfo.ownernick);
								content = new String(showShareBroadcastInfo.txt);
								holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
								SpannableStringBuilder style = new SpannableStringBuilder(content + showUserName);
								style.setSpan(new PartShowRoomClick(showShareBroadcastInfo), content.length(), (content + showUserName).length(),
										Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								holder.contentTxt.setText(style);

							}

						} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_ANIMATION_BROADCAST.getValue()) {//仙术广播
							holder.iv_voice_listen.setVisibility(View.GONE);
							holder.ll_voice_play.setVisibility(View.GONE);
							holder.contentTxt.setVisibility(View.VISIBLE);
							holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
							holder.iv_decree.setImageDrawable(null);
							int textColor = datainfo.datainfo.intValue();
							String strMagic = " (来自仙术-流星雨) ";
							if (textColor == WriteBroadCastBiz.MAGIC_ONE_VALUE) {
								strMagic = " (来自仙术-流星雨) ";
							} else if (textColor == WriteBroadCastBiz.MAGIC_TWO_VALUE) {
								strMagic = " (来自仙术-万箭阵) ";
							} else if (textColor == WriteBroadCastBiz.MAGIC_THREE_VALUE) {
								strMagic = " (来自仙术-鸿毛雨) ";
							} else if (textColor == WriteBroadCastBiz.MAGIC_FOUR_VALUE) {
								strMagic = " (来自仙术-玫瑰花语) ";
							} else if (textColor == WriteBroadCastBiz.MAGIC_FIVE_VALUE) {
								strMagic = " (来自仙术-一箭钟情) ";
							} else if (textColor == WriteBroadCastBiz.MAGIC_SIX_VALUE) {
								strMagic = " (来自仙术-变变变) ";
							} else if (textColor == WriteBroadCastBiz.MAGIC_SEVEN_VALUE) {
								strMagic = " (来自仙术-真爱永恒) ";
							} else if (textColor == WriteBroadCastBiz.MAGIC_EIGHT_VALUE) {
								strMagic = " (来自仙术-烈焰红唇) ";
							} else if (textColor == WriteBroadCastBiz.MAGIC_NINE_VALUE) {
								strMagic = " (来自仙术-天马流星拳) ";
							} else if (textColor == WriteBroadCastBiz.MAGIC_TEN_VALUE) {
								strMagic = " (来自仙术-甜蜜热气球) ";
							}

							content = new String(datainfo.data);
							if (!TextUtils.isEmpty(sb.toString()) && usersStr != null) {//有@的用户让其名字变色
								String messageContent = sb.toString() + content;
								SpannableStringBuilder builde = getStyle(messageContent, usersStr, userInfoList);
								SpannableStringBuilder style1 = PartMagicListener.setStyle(mContext, strMagic);
								builde.append(style1 + "\n");
								holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
								SpannableStringBuilder style2 = new SpannableStringBuilder(PLAYBACK);
								style2.setSpan(new OnPartTextPlayBackClickListener(textColor, mContext, broadcastInfo), 0, PLAYBACK.length(),
										Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								builde.append(style2);
								holder.contentTxt.setText(builde);
							} else {
								SpannableStringBuilder builder = getStyle(content);
								SpannableStringBuilder style1 = PartMagicListener.setStyle(mContext, strMagic);
								builder.append(style1);
								holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
								SpannableStringBuilder style2 = new SpannableStringBuilder(PLAYBACK);
								style2.setSpan(new OnPartTextPlayBackClickListener(textColor, mContext, broadcastInfo), 0, PLAYBACK.length(),
										Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								builder.append(style2);
								holder.contentTxt.setText(builder);
							}
						}
					}
				}

			} else {//兼容老数据
				if (!TextUtils.isEmpty(content)) {//文字广播
					content = BaseTextUtils.StringFilter(content);//对特殊字符对齐
					content = content.replaceAll("\n", "");
					holder.iv_voice_listen.setVisibility(View.GONE);
					holder.ll_voice_play.setVisibility(View.GONE);
					holder.contentTxt.setVisibility(View.VISIBLE);
					holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.black));
					if (!TextUtils.isEmpty(sb.toString()) && usersStr != null) {//有@的用户让其名字变色
						SpannableString style = getStyle1(sb.toString() + content, usersStr, userInfoList);
						holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
						holder.contentTxt.setText(style);

						//						SpannableString style = ParseMsgUtil.convetToHtml(sb.toString() + content, mContext, BaseUtils.dip2px(mContext, 24));
						//						holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
						//
						//						int len = usersStr.length;
						//						int startPos = 0;
						//						int endPos = 0;
						//						for (int i = 0; i < len; i++) {
						//							String aUsers = usersStr[i];
						//							endPos += aUsers.length();
						//							style.setSpan(new PartTextClick((GoGirlUserInfo) userInfoList.get(i)), startPos, endPos,
						//									Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
						//							startPos += aUsers.length();
						//						}
						//
						//						holder.contentTxt.setText(style);
					} else {
						String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
						SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode,
								HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
						holder.contentTxt.setText(builder);
					}
				} else if (!TextUtils.isEmpty(voice)) {//语音广播
					holder.iv_decree.setImageDrawable(null);
					holder.contentTxt.setVisibility(View.GONE);
					holder.ll_voice_play.setVisibility(View.VISIBLE);
					String voiceinfo = new String(broadcastInfo.voiceinfo);
					if (!TextUtils.isEmpty(voiceinfo)) {
						String[] broadVoice = voiceinfo.split(",");
						if (broadVoice != null && broadVoice.length == 2) {
							holder.tv_voice_time.setText(broadVoice[1] + "\"");
							try {
								int len = Integer.parseInt(broadVoice[1]);
								if (len <= 20) {
									params1.topMargin = BaseUtils.dip2px(mContext, 3);
									holder.ll_voice_play.setLayoutParams(params1);
								} else if (len <= 40) {
									params2.topMargin = BaseUtils.dip2px(mContext, 3);
									holder.ll_voice_play.setLayoutParams(params2);
								} else {
									params3.topMargin = BaseUtils.dip2px(mContext, 3);
									holder.ll_voice_play.setLayoutParams(params3);
								}

							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							if (FileUtils.fileExists(mContext, broadVoice[0], userInfo.uid.intValue())) {
								holder.iv_voice_listen.setVisibility(View.GONE);
							} else {
								holder.iv_voice_listen.setVisibility(View.VISIBLE);
							}
							holder.ll_voice_play.setOnClickListener(new ItemVoiceClickListener(holder.iv_voice_listen, holder.progressBar,
									ItemVoiceClickListener.VOICE_COMMON_BC, userInfo, broadVoice[0], broadCastBiz));
							if (!TextUtils.isEmpty(broadVoice[0]) && broadVoice[0].equals(playFileName)) {
								holder.iv_voice_play.setImageResource(R.drawable.message_img_broadcast_voice_grey);
								AnimationDrawable animationDrawable = (AnimationDrawable) holder.iv_voice_play.getDrawable();
								animationDrawable.start();
							} else {
								holder.iv_voice_play.setImageResource(R.drawable.broadcast_voice_logo3);
								holder.iv_voice_play.clearAnimation();
							}

						}
					}
				}
			}

			String sendUserName = "";
			if (userInfo != null) {
				int sex = 0;
				int userstatus = userInfo.userstatus.intValue();//活动戴帽,总共三定帽子
				int bigGiftValue = BIG_GIFT_FLAG & userstatus;
				int middleGiftValue = MIDDLE_GIFT_FLAG & userstatus;
				int smallGiftValue = SMALL_GIFT_FLAG & userstatus;
				if (bigGiftValue > 0) {//说明买过大礼物
					holder.iv_77_big_gift.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadCampaignHatImg(mContext, imageLoader, campaignHatOptions, BIG_GIFT_FLAG, holder.iv_77_big_gift);
				} else if (middleGiftValue > 0) {//显示第二个礼物 
					holder.iv_77_big_gift.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadCampaignHatImg(mContext, imageLoader, campaignHatOptions, MIDDLE_GIFT_FLAG, holder.iv_77_big_gift);
				} else if (smallGiftValue > 0) {//第三个礼物
					holder.iv_77_big_gift.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadCampaignHatImg(mContext, imageLoader, campaignHatOptions, SMALL_GIFT_FLAG, holder.iv_77_big_gift);
				} else {
					holder.iv_77_big_gift.setVisibility(View.GONE);
				}

				//特权帽子
				int seven_gift = SEVEN_GIFT_FLAG & userstatus;
				int upper_gift = UPPER_GIFT_FLAG & userstatus;
				int nine_gift = NINE_GIFT_FLAG & userstatus;
				int flowers_gift = FLOWERS_GIFT_FLAG & userstatus;
				int trial_gift = TRIAL_GIFT_FLAG & userstatus;
				int gueen_gift = QUEEN_GIFT_FLAG & userstatus;
				if (seven_gift > 0) {
					holder.privilege.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadCampaignHatImg(mContext, imageLoader, campaignHatOptions, SEVEN_GIFT_FLAG, holder.privilege);
				} else if (upper_gift > 0) {
					holder.privilege.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadCampaignHatImg(mContext, imageLoader, campaignHatOptions, UPPER_GIFT_FLAG, holder.privilege);
				} else if (nine_gift > 0) {
					holder.privilege.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadCampaignHatImg(mContext, imageLoader, campaignHatOptions, NINE_GIFT_FLAG, holder.privilege);
				} else if (flowers_gift > 0) {
					holder.privilege.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadCampaignHatImg(mContext, imageLoader, campaignHatOptions, FLOWERS_GIFT_FLAG, holder.privilege);
				} else if (trial_gift > 0) {
					holder.privilege.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadCampaignHatImg(mContext, imageLoader, campaignHatOptions, TRIAL_GIFT_FLAG, holder.privilege);
				} else if (gueen_gift > 0) {
					holder.privilege.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadCampaignHatImg(mContext, imageLoader, campaignHatOptions, QUEEN_GIFT_FLAG, holder.privilege);
				} else {
					holder.privilege.setVisibility(View.GONE);
				}

				holder.sexImg.setVisibility(View.GONE);
				if (userInfo.sex != null) {
					sex = userInfo.sex.intValue();//0是女，1为男
				}
				if (sex == BAConstants.Gender.FEMALE.getValue()) {
					holder.sexImg.setImageResource(R.drawable.broadcast_img_girl);
					holder.typeNametxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_female_name_color));
				} else {
					holder.sexImg.setImageResource(R.drawable.broadcast_img_boy);
					holder.typeNametxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_male_name_color));
				}

				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						userInfo.uid.intValue());
				sendUserName = TextUtils.isEmpty(alias) ? new String(userInfo.nick) : alias;
				String key = new String(userInfo.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR;
				imageLoader.displayImage("http://" + key, holder.headerImage, options);

				//头像挂件
				int deliver_four_gift = DELIVER_GIFT_FOUR_FLAG & userstatus;
				int deliver_three_gift = DELIVER_GIFT_THREE_FLAG & userstatus;
				int deliver_two_gift = DELIVER_GIFT_TWO_FLAG & userstatus;
				int deliver_one_gift = DELIVER_GIFT_ONE_FLAG & userstatus;

				if (deliver_four_gift > 0) {
					holder.deliverIv.setVisibility(View.VISIBLE);
					imageLoader.displayImage("http://@" + DELIVER_GIFT_FOUR_FLAG, holder.deliverIv, deliverOptions);
				} else if (deliver_three_gift > 0) {
					holder.deliverIv.setVisibility(View.VISIBLE);
					imageLoader.displayImage("http://@" + DELIVER_GIFT_THREE_FLAG, holder.deliverIv, deliverOptions);
				} else if (deliver_two_gift > 0) {
					holder.deliverIv.setVisibility(View.VISIBLE);
					imageLoader.displayImage("http://@" + DELIVER_GIFT_TWO_FLAG, holder.deliverIv, deliverOptions);
				} else if (deliver_one_gift > 0) {
					holder.deliverIv.setVisibility(View.VISIBLE);
					imageLoader.displayImage("http://@" + DELIVER_GIFT_ONE_FLAG, holder.deliverIv, deliverOptions);
				} else {
					holder.deliverIv.setVisibility(View.GONE);
				}
			}
			int type = broadcastInfo.broadcasttype.intValue();
			if (type == 1) {//系统广播
				holder.sexImg.setVisibility(View.GONE);
				holder.typeNametxt.setText(R.string.str_system_user);
				holder.typeNametxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_female_name_color));
				holder.typeImg.setVisibility(View.VISIBLE);
				holder.iv_77_small_gift.setVisibility(View.GONE);
				holder.typeImg.setImageResource(R.drawable.broadcast_img_official);
			} else {
				holder.typeImg.setVisibility(View.GONE);
				holder.typeNametxt.setText(sendUserName);
			}
			onHeadViewClick.setUserInfo(userInfo);
			onNameViewClick.setUserInfo(userInfo);
			holder.timeTxt.setText(BaseTimes.getChatDiffTimeHHMM(broadcastInfo.createtime.longValue() * 1000));
		}
		return row;
	}

	final class ViewHolder {
		ImageView headerImage;
		ImageView identityImage;
		TextView typeNametxt;
		TextView contentTxt;
		TextView timeTxt;
		TextView tv_voice_time;
		LinearLayout ll_voice_play;
		ImageView iv_voice_play;
		ImageView iv_voice_listen;
		ImageView sexImg;
		ImageView typeImg;
		ImageView iv_77_small_gift;
		ImageView iv_77_big_gift;
		ProgressBar progressBar;
		LinearLayout ll_bg;
		ImageView iv_decree;
		ImageView iv_bottom_line;
		ImageView iv_changer;
		ImageView privilege;//特权帽子
		ImageView deliverIv;//挂件
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop the isInfiniteLoop to set
	 */
	public TopBroadCastAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	public class PartShowRoomClick extends ClickableSpan {//点击分享房间到秀场
		private ShowShareBroadcastInfo info;

		public PartShowRoomClick(ShowShareBroadcastInfo info) {
			this.info = info;
		}

		@Override
		public void updateDrawState(TextPaint ds) {//选中的文字变色
			super.updateDrawState(ds);//005bd7
			ds.setColor(mContext.getResources().getColor(R.color.show_share_color));
			ds.setUnderlineText(true);
		}

		@Override
		public void onClick(View widget) {
			if (BAApplication.mLocalUserInfo != null) {
				if (iEnterShowRoom != null) {
					iEnterShowRoom.enterShowRoomCallback(info);
				}
			}
		}
	}

	private SpannableStringBuilder getStyle(String messageContent) {
		String unicode = EmojiParser.getInstance(mContext).parseEmoji(messageContent);
		SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode, HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
		SpannableStringBuilder style = ParseMsgUtil.convetToHtml(builder.toString(), mContext, imageSize);
		return style;
	}

	private SpannableStringBuilder getStyle(String messageContent, String[] usersStr, GoGirlUserInfoList userInfoList) {
		String unicode = EmojiParser.getInstance(mContext).parseEmoji(messageContent);
		SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode, HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
		SpannableStringBuilder style = ParseMsgUtil.convetToHtml(builder.toString(), mContext, imageSize);
		int len = usersStr.length;
		int startPos = 0;
		int endPos = 0;
		for (int i = 0; i < len; i++) {
			String aUsers = usersStr[i];
			endPos += aUsers.length();
			style.setSpan(new OnPartTextUserClickLlistener(mContext, (GoGirlUserInfo) userInfoList.get(i)), startPos, endPos,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			startPos += aUsers.length();
		}
		return style;
	}

	private SpannableString getStyle1(String messageContent, String[] usersStr, GoGirlUserInfoList userInfoList) {
		String unicode = EmojiParser.getInstance(mContext).parseEmoji(messageContent);
		SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode, HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
		int len = usersStr.length;
		int startPos = 0;
		int endPos = 0;
		for (int i = 0; i < len; i++) {
			String aUsers = usersStr[i];
			endPos += aUsers.length();
			builder.setSpan(new OnPartTextUserClickLlistener(mContext, (GoGirlUserInfo) userInfoList.get(i)), startPos, endPos,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			startPos += aUsers.length();
		}
		return builder;
	}
}
