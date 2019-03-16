package com.tshang.peipei.activity.main.adapter;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.activity.dialog.HallRedpacketInfoDialog;
import com.tshang.peipei.activity.dialog.PlayFingerDialog;
import com.tshang.peipei.activity.listener.OnLevelClickListener;
import com.tshang.peipei.activity.listener.OnPartTextPlayBackClickListener;
import com.tshang.peipei.activity.listener.OnPartTextUserClickLlistener;
import com.tshang.peipei.activity.listener.OnUserClickListener;
import com.tshang.peipei.activity.listener.PartMagicListener;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.activity.mine.MineWriteBroadCastActivity;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.activity.store.StoreH5RechargeActivity;
import com.tshang.peipei.activity.suspension.SuspensionActivity;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.IAPKInfoUtil;
import com.tshang.peipei.base.IFileUtils;
import com.tshang.peipei.base.ILog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAConstants.RewardType;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.base.textview.BaseTextUtils;
import com.tshang.peipei.model.ReceiverChatData;
import com.tshang.peipei.model.broadcast.BroadCastBiz;
import com.tshang.peipei.model.broadcast.BroadCastUtils;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.model.broadcast.ItemVoiceClickListener;
import com.tshang.peipei.model.broadcast.WriteBroadCastBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.model.redpacket2.HallRedpacket;
import com.tshang.peipei.model.redpacket2.RedDecodeUtil;
import com.tshang.peipei.model.redpacket2.SolitaireRedpacket;
import com.tshang.peipei.model.space.SpaceBiz;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.EnterBroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.ShowShareBroadcastInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.FileUtils;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 *
 * @Description: 0.00
 *
 * @author Jeff 
 *
 * @version V1.1   
 */
public class MainBroadCastAdapter extends ArrayListAdapter<BroadcastInfo> {
	private DisplayImageOptions options;
	private DisplayImageOptions gradeInfoOptions;
	private String playFileName = "";
	private BroadCastBiz broadCastBiz;
	private BAHandler mHandler;
	private RelativeLayout.LayoutParams params1;
	private RelativeLayout.LayoutParams params2;
	private RelativeLayout.LayoutParams params3;
	private DisplayImageOptions campaignHatOptions;
	private DisplayImageOptions deliverOptions;
	private IenterShowRoom iEnterShowRoom;
	private int imageSize = 24;

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

	private static final String PLAYBACK = "\n　回放　";
	private int petSize = 48;
	private int petSize2 = 60;

	public void setPlayFileName(String playFileName) {
		this.playFileName = playFileName;
		notifyDataSetChanged();
	}

	public MainBroadCastAdapter(Activity context, BroadCastBiz broadCastBiz, BAHandler mHandler, IenterShowRoom iEnterShowRoom) {
		super(context);
		options = ImageOptionsUtils.GetHeadKeySmallRounded(context);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
		this.broadCastBiz = broadCastBiz;
		params1 = new RelativeLayout.LayoutParams(BaseUtils.dip2px(context, 80), RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2 = new RelativeLayout.LayoutParams(BaseUtils.dip2px(context, 120), RelativeLayout.LayoutParams.WRAP_CONTENT);
		params3 = new RelativeLayout.LayoutParams(BaseUtils.dip2px(context, 160), RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.mHandler = mHandler;
		campaignHatOptions = ImageOptionsUtils.getCampaignHatOptions(context);
		deliverOptions = ImageOptionsUtils.getDeliverGiftOptions(context);
		this.iEnterShowRoom = iEnterShowRoom;
		imageSize = BaseUtils.dip2px(context, 24);
		petSize = BaseUtils.dip2px(mContext, 48);
		petSize2 = BaseUtils.dip2px(mContext, 60);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		OnLevelClickListener levelListener = null;
		OnUserClickListener onHeadViewClick = null;
		OnUserClickListener onNameViewClick = null;
		GrabSolitaireListener onGrabSolitaireClick = null;
		GrabRedpacketListener onGrabRedpacketClick = null;
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
			holder.iv_changer = (ImageView) row.findViewById(R.id.iv_broadcast_item_charge);
			holder.privilege = (ImageView) row.findViewById(R.id.iv_broadcast_item_privilege);
			holder.ll_braodcast_item_solitaire = row.findViewById(R.id.ll_braodcast_item_solitaire);
			holder.tv_broadcast_redpacket_money = (TextView) row.findViewById(R.id.tv_broadcast_redpacket_money);
			holder.tv_broadcast_jion_person = (TextView) row.findViewById(R.id.tv_broadcast_jion_person);
			holder.ll_braodcast_item_redpacket = row.findViewById(R.id.ll_braodcast_item_redpacket);
			holder.tv_hall_desc = (TextView) row.findViewById(R.id.tv_hall_desc);
			holder.tv_hall_redpacket_type = (TextView) row.findViewById(R.id.tv_hall_redpacket_type);
			holder.iv_official = (ImageView) row.findViewById(R.id.iv_official);

			levelListener = new OnLevelClickListener(mContext);
			holder.iv_77_small_gift.setOnClickListener(levelListener);//防止重复new 对象
			onHeadViewClick = new OnUserClickListener(mContext, OnUserClickListener.FLAG_SPACE);
			holder.headerImage.setOnClickListener(onHeadViewClick);
			onNameViewClick = new OnUserClickListener(mContext, OnUserClickListener.FLAG_WRITE_BROADCAST);
			holder.typeNametxt.setOnClickListener(onNameViewClick);
			onGrabSolitaireClick = new GrabSolitaireListener();
			holder.ll_braodcast_item_solitaire.setOnClickListener(onGrabSolitaireClick);
			onGrabRedpacketClick = new GrabRedpacketListener();
			holder.ll_braodcast_item_redpacket.setOnClickListener(onGrabRedpacketClick);
			row.setTag(holder);
			row.setTag(holder.iv_77_small_gift.getId(), levelListener);
			row.setTag(holder.headerImage.getId(), onHeadViewClick);
			row.setTag(holder.typeNametxt.getId(), onNameViewClick);
			row.setTag(holder.ll_braodcast_item_solitaire.getId(), onGrabSolitaireClick);
			row.setTag(holder.ll_braodcast_item_redpacket.getId(), onGrabRedpacketClick);
		} else {
			holder = (ViewHolder) row.getTag();
			levelListener = (OnLevelClickListener) row.getTag(holder.iv_77_small_gift.getId());
			onHeadViewClick = (OnUserClickListener) row.getTag(holder.headerImage.getId());
			onNameViewClick = (OnUserClickListener) row.getTag(holder.typeNametxt.getId());
			onGrabSolitaireClick = (GrabSolitaireListener) row.getTag(holder.ll_braodcast_item_solitaire.getId());
			onGrabRedpacketClick = (GrabRedpacketListener) row.getTag(holder.ll_braodcast_item_redpacket.getId());
		}
		BroadcastInfo broadcastInfo = mList.get(position);
		holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
		holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
		if (broadcastInfo != null) {
			//			long startTime = System.currentTimeMillis();
			GoGirlUserInfo userInfo = broadcastInfo.senduser;
			if (userInfo != null && userInfo.authpickey != null && !TextUtils.isEmpty(new String(userInfo.authpickey))
					&& (userInfo.userstatus.intValue() & SwitchStatus.GG_US_AUTH_FLAG) > 0) {
				holder.identityImage.setVisibility(View.VISIBLE);
			} else {
				holder.identityImage.setVisibility(View.GONE);
			}
			//充值帽子
			if ((userInfo.userstatus.intValue() & SwitchStatus.GG_US_FIRST_RECHARGE_FLAG) > 0) {
				holder.iv_changer.setVisibility(View.VISIBLE);
			} else {
				holder.iv_changer.setVisibility(View.GONE);
			}
			//等级帽子
			if (userInfo.gradeinfo != null) {
				String gradeinfo = new String(userInfo.gradeinfo);
				if (!TextUtils.isEmpty(gradeinfo)) {
					holder.iv_77_small_gift.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, holder.iv_77_small_gift, gradeInfoOptions);
				} else {
					holder.iv_77_small_gift.setVisibility(View.GONE);
				}
			}

			//置顶帽子
			int type = broadcastInfo.broadcasttype.intValue();
			if (userInfo.uid.intValue() == 50000) {//系统广播
				holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
				holder.iv_decree.setImageDrawable(null);
				holder.sexImg.setVisibility(View.GONE);
				holder.typeNametxt.setText(R.string.str_system_user);
				holder.typeImg.setVisibility(View.VISIBLE);
				holder.typeNametxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_female_name_color));
				holder.iv_77_small_gift.setVisibility(View.GONE);
				holder.typeImg.setImageResource(R.drawable.broadcast_img_official);
			} else {
				if (type == 2) {//置顶广播
					holder.typeImg.setVisibility(View.VISIBLE);
					holder.typeImg.setImageResource(R.drawable.broadcast_img_top);
				} else {
					holder.typeImg.setVisibility(View.GONE);
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
			GoGirlDataInfo datainfo = null;
			if (datalist != null && datalist.length > 0) {
				datainfo = BroadCastUtils.getGoGirlDataInfo(datalist);
			}
			Log.i("Aaron", "content=================" + content);
			if (datainfo != null) {
				Log.d("Aaron", "datainfo.type.intValue()==" + datainfo.type.intValue() + ", content===" + content);
			}
			String userName = "";
			if (datalist != null && datalist.length != 0) {
				TextPaint tp = holder.contentTxt.getPaint();
				holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_item_text_color));
				tp.setFakeBoldText(false);

				if (datainfo != null) {
					if (datainfo.type.intValue() == MessageType.WITHANTEFINGER.getValue()
							|| datainfo.type.intValue() == MessageType.NEWFINGER.getValue()) {//猜拳数据
						final FingerGuessingInfo fingerInfo = BroadCastUtils.getFingerGuessingInfo(datainfo);
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
						holder.iv_voice_listen.setVisibility(View.GONE);
						holder.ll_voice_play.setVisibility(View.GONE);
						holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_item_text_color));
						holder.contentTxt.setVisibility(View.VISIBLE);
						if (fingerInfo != null) {
							String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark")
									.getAlias(fingerInfo.uid1.intValue());
							userName = TextUtils.isEmpty(alias) ? new String(fingerInfo.nick1) : alias;
							holder.typeNametxt.setText(userName);
							holder.typeImg.setVisibility(View.GONE);
							holder.typeNametxt.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									//									SpaceUtils.toSpaceCustom(mContext, fingerInfo, 1);
									SpaceUtils.toSpaceCustom(mContext, fingerInfo.uid1.intValue(), 1);
								}
							});
							String monkey = fingerInfo.ante.intValue() + "金币";
							if (fingerInfo.antetype.intValue() == 1) {
								monkey = fingerInfo.ante.intValue() + "银币";
							}
							content = String.format(mContext.getResources().getString(R.string.str_finger_broadcast_content), userName, monkey);

							if (!TextUtils.isEmpty(sb.toString()) && usersStr != null) {//有@的用户让其名字变色
								//								String messageContent = sb.toString() + content + "\n";
								String messageContent = content + "\n";
								//								SpannableStringBuilder style = new SpannableStringBuilder(getStyle(messageContent, usersStr, userInfoList));
								//								SpannableStringBuilder	style=new SpannableStringBuilder(usersStr);
								SpannableStringBuilder style = new SpannableStringBuilder(messageContent);
								holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());

								SpannableStringBuilder style1 = new SpannableStringBuilder(" 去挑战TA ");
								style1.setSpan(new PartFingerClick(fingerInfo), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								style.append(style1);

								holder.contentTxt.setText(style);
							} else {
								String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
								SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode,
										HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
								holder.contentTxt.setText(builder);
							}
						}
					} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_PUBLICTION_AWARD_BROADCAST.getValue()) {//悬赏广播
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
						holder.iv_voice_listen.setVisibility(View.GONE);
						holder.ll_voice_play.setVisibility(View.GONE);
						holder.contentTxt.setVisibility(View.VISIBLE);
						content = new String(datainfo.data);
						try {
							if (content.contains("某")) {//匿名悬赏
								String str0 = content.substring(0, content.indexOf("\"") + 1);
								String str1 = content.substring(str0.length(), content.indexOf("悬") - 1);
								String str2 = content.substring(str0.length() + str1.length(), content.indexOf("值") + 1);
								String str3 = content.substring(str0.length() + str1.length() + str2.length(), content.lastIndexOf("，") - 3);
								String str4 = content.substring(str0.length() + str1.length() + str2.length() + str3.length(), content.length() - 1);

								SpannableStringBuilder s0 = new SpannableStringBuilder(str0);
								SpannableStringBuilder s1 = new SpannableStringBuilder(str1);
								SpannableStringBuilder s2 = new SpannableStringBuilder(str2);
								SpannableStringBuilder s3 = new SpannableStringBuilder(str3);
								SpannableStringBuilder s4 = new SpannableStringBuilder(str4 + "\n");
								SpannableStringBuilder ss = new SpannableStringBuilder(" 我要抢单  ");

								holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());

								String sexStr = content.substring(content.indexOf("某") + 1, content.indexOf("某") + 2);

								if ("女".equals(sexStr)) {
									s1.setSpan(new ForegroundColorSpan(Color.parseColor("#f05b58")), 0, str1.length(), 0);
									s3.setSpan(new ForegroundColorSpan(Color.parseColor("#f05b58")), 0, str3.length(), 0);
									ss.setSpan(new BackgroundColorSpan(Color.parseColor("#f05b58")), 0, 6, 0);
									s1.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str1.length(), 0);
									s3.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str3.length(), 0);
									ss.setSpan(new BackgroundColorSpan(Color.parseColor("#f05b58")), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								} else {
									s1.setSpan(new ForegroundColorSpan(Color.parseColor("#2898fe")), 0, str1.length(), 0);
									s3.setSpan(new ForegroundColorSpan(Color.parseColor("#2898fe")), 0, str3.length(), 0);
									ss.setSpan(new BackgroundColorSpan(Color.parseColor("#2898fe")), 0, 6, 0);
									s1.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str1.length(), 0);
									s3.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str3.length(), 0);
									ss.setSpan(new BackgroundColorSpan(Color.parseColor("#2898fe")), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								}
								ss.setSpan(new RewardonClick(), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								s0.append(s1).append(s2).append(s3).append(s4).append(ss);
								holder.contentTxt.setText(s0);
							} else {
								String str0 = content.substring(0, content.indexOf("\"") + 1);
								String str1 = content.substring(content.indexOf("\"") + 1, content.indexOf("刚") - 1);
								String str2 = content.substring(str0.length() + str1.length(), content.indexOf("了") + 2);
								String str3 = content.substring(str0.length() + str1.length() + str2.length(), content.indexOf("悬") - 1);
								String str4 = content.substring(str0.length() + str1.length() + str2.length() + str3.length(),
										content.indexOf("值") + 1);
								String str5 = content.substring(str0.length() + str1.length() + str2.length() + str3.length() + str4.length(),
										content.lastIndexOf("，") - 3);
								String str6 = content.substring(
										str0.length() + str1.length() + str2.length() + str3.length() + str4.length() + str5.length(),
										content.length() - 1);

								SpannableStringBuilder s0 = new SpannableStringBuilder(str0);
								SpannableStringBuilder s1 = new SpannableStringBuilder(str1);
								SpannableStringBuilder s2 = new SpannableStringBuilder(str2);
								SpannableStringBuilder s3 = new SpannableStringBuilder(str3);
								SpannableStringBuilder s4 = new SpannableStringBuilder(str4);
								SpannableStringBuilder s5 = new SpannableStringBuilder(str5);
								SpannableStringBuilder s6 = new SpannableStringBuilder(str6 + "\n");
								SpannableStringBuilder ss = new SpannableStringBuilder(" 我要抢单  ");

								holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());

								String sexStr = content.substring(content.indexOf("，") + 1, content.indexOf("，") + 2);
								if ("女".equals(sexStr)) {
									s1.setSpan(new ForegroundColorSpan(Color.parseColor("#f05b58")), 0, str1.length(), 0);
									s3.setSpan(new ForegroundColorSpan(Color.parseColor("#f05b58")), 0, str3.length(), 0);
									s5.setSpan(new ForegroundColorSpan(Color.parseColor("#f05b58")), 0, str5.length(), 0);
									ss.setSpan(new BackgroundColorSpan(Color.parseColor("#f05b58")), 0, 6, 0);
									s1.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str1.length(), 0);
									s3.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str3.length(), 0);
									s5.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str5.length(), 0);
									ss.setSpan(new BackgroundColorSpan(Color.parseColor("#f05b58")), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								} else {
									s1.setSpan(new ForegroundColorSpan(Color.parseColor("#2898fe")), 0, str1.length(), 0);
									s3.setSpan(new ForegroundColorSpan(Color.parseColor("#2898fe")), 0, str3.length(), 0);
									s5.setSpan(new ForegroundColorSpan(Color.parseColor("#2898fe")), 0, str5.length(), 0);
									ss.setSpan(new BackgroundColorSpan(Color.parseColor("#2898fe")), 0, 6, 0);
									s1.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str1.length(), 0);
									s3.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str3.length(), 0);
									s5.setSpan(new AbsoluteSizeSpan(BaseUtils.dip2px(mContext, 15)), 0, str5.length(), 0);
									ss.setSpan(new BackgroundColorSpan(Color.parseColor("#2898fe")), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								}
								ss.setSpan(new RewardonClick(), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								s0.append(s1).append(s2).append(s3).append(s4).append(s5).append(s6).append(ss);
								holder.contentTxt.setText(s0);
							}
						} catch (Exception e) {
							e.printStackTrace();

							String rewardMsgContent = content + "\n";
							SpannableStringBuilder s = new SpannableStringBuilder(rewardMsgContent);
							holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());

							SpannableStringBuilder ss = new SpannableStringBuilder(" 我要抢单  ");
							ss.setSpan(new RewardonClick(), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
							ss.setSpan(new BackgroundColorSpan(Color.parseColor("#2898fe")), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
							ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
							s.append(ss);

							holder.contentTxt.setText(s);
						}
					} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_ORNAMENT_BROADCAST.getValue()) {//挂件礼物广播
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
						holder.iv_voice_listen.setVisibility(View.GONE);
						holder.ll_voice_play.setVisibility(View.GONE);
						holder.contentTxt.setVisibility(View.VISIBLE);
						content = new String(datainfo.data);

						content = BaseTextUtils.StringFilter(content);//对特殊字符对齐
						content = content.replaceAll("\n", "");

						if (!ListUtils.isEmpty(userInfoList)) {//@的用户
							int len = userInfoList.size();
							usersStr = new String[len];
							sb.delete(0, sb.length());
							for (int i = 0; i < len; i++) {
								GoGirlUserInfo info = (GoGirlUserInfo) userInfoList.get(i);
								if (info != null) {
									String alias = SharedPreferencesTools.getInstance(mContext,
											BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(info.uid.intValue());
									String giftUserName = TextUtils.isEmpty(alias) ? new String(info.nick) : alias;
									usersStr[i] = "@" + giftUserName;
									if (len == 2 && i == 0) {
										sb.append("@").append(giftUserName);
									}
								}
							}

							if (len == 2) {
								if (!TextUtils.isEmpty(content) && content.contains("[peer_nick]")) {
									content = content.replace("[peer_nick]", usersStr[1]);
								}
							} else if (len == 1) {
								if (!TextUtils.isEmpty(content) && content.contains("[peer_nick]")) {
									content = content.replace("[peer_nick]", usersStr[0]);
								}
							}
						}

						holder.iv_voice_listen.setVisibility(View.GONE);
						holder.ll_voice_play.setVisibility(View.GONE);
						holder.contentTxt.setVisibility(View.VISIBLE);
						TextPaint tp1 = holder.contentTxt.getPaint();
						if (userInfo.uid.intValue() == 50000 && content.endsWith("组")) {
							holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.show_broadcast_system_color));
							tp1.setFakeBoldText(true);
						} else {
							holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_item_text_color));
							tp1.setFakeBoldText(false);
						}

						if (usersStr != null) {//有@的用户让其名字变色

							SpannableString style = getStyle2(sb.toString() + content, usersStr, userInfoList);
							holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
							holder.contentTxt.setText(style);

						} else {
							String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
							SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode,
									HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
							holder.contentTxt.setText(builder);
						}
					} else if (datainfo.type.intValue() == MessageType.BROADCASTCOLOR.getValue()
							|| datainfo.type.intValue() == MessageType.FEMALE_DECREE.getValue()
							|| datainfo.type.intValue() == MessageType.MALE_DECREE.getValue()) {//广播特殊颜色数据
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
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
							holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_item_text_color));
						}
						content = new String(datainfo.data);
						if (!TextUtils.isEmpty(sb.toString()) && usersStr != null) {//有@的用户让其名字变色
							//TODO
							String messageContent = sb.toString() + content;
							SpannableString style = getStyle1(messageContent, usersStr, userInfoList);
							holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
							holder.contentTxt.setText(style);
						} else {
							;
							String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
							SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode,
									HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
							holder.contentTxt.setText(builder);
						}
						if (datainfo.type.intValue() == MessageType.FEMALE_DECREE.getValue()
								|| datainfo.type.intValue() == MessageType.MALE_DECREE.getValue()) {
							holder.ll_bg.setBackgroundResource(R.drawable.broadcast_toplist_bg);
							if (datainfo.type.intValue() == MessageType.FEMALE_DECREE.getValue()) {
								holder.iv_decree.setImageResource(R.drawable.broadcast_toplist_print_women);
							} else if (datainfo.type.intValue() == MessageType.MALE_DECREE.getValue()) {
								holder.iv_decree.setImageResource(R.drawable.broadcast_toplist_print_man);
							}
						} else {
							holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
							holder.iv_decree.setImageDrawable(null);
						}

					} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_RECHARGE_BROADCAST.getValue()) {
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
						holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
						holder.iv_decree.setImageDrawable(null);
						if (!TextUtils.isEmpty(content)) {//文字广播
							content = BaseTextUtils.StringFilter(content);//对特殊字符对齐
							content = content.replaceAll("\n", "");
							holder.iv_voice_listen.setVisibility(View.GONE);
							holder.ll_voice_play.setVisibility(View.GONE);
							holder.contentTxt.setVisibility(View.VISIBLE);
							holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_item_text_color));
							holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
							if (!TextUtils.isEmpty(sb.toString()) && usersStr != null) {//有@的用户让其名字变色
								SpannableStringBuilder style = new SpannableStringBuilder(getStyle(sb.toString() + content + "\n", usersStr,
										userInfoList));
								SpannableStringBuilder style1 = new SpannableStringBuilder(" 我也要试试 ");
								style1.setSpan(new PartChargeClick(), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								style.append(style1);
								holder.contentTxt.setText(style);//加入去充值点击事件
							} else {
								String aa = content + "\n";
								SpannableStringBuilder style = new SpannableStringBuilder(aa + " 我也要试试 ");
								style.setSpan(new PartChargeClick(), aa.length(), aa.length() + 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								holder.contentTxt.setText(style);
							}
						}
					} else if (datainfo.type.intValue() == MessageType.INOUT_BROADCASE.getValue()) {//进入了广播通知
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
						holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
						holder.iv_decree.setImageDrawable(null);
						holder.iv_voice_listen.setVisibility(View.GONE);
						holder.ll_voice_play.setVisibility(View.GONE);
						holder.contentTxt.setVisibility(View.VISIBLE);
						EnterBroadcastInfo enterInfo = BroadCastUtils.getEnterBroadcastInfo(datainfo);
						if (enterInfo != null) {
							Bitmap bitmap = null;
							if (enterInfo.ridingid.intValue() == SpaceBiz.DEER_RIDING_VALUE) {
								bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.deer2);
							} else if (enterInfo.ridingid.intValue() == SpaceBiz.BIRD_RIDING_VALUE) {
								bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.bird2);
							} else if (enterInfo.ridingid.intValue() == SpaceBiz.PEACOCK_RIDING_VALUE) {
								bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.peacock5);
							} else if (enterInfo.ridingid.intValue() == SpaceBiz.CAR_MOTORING) {
								bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.brocast_car);
							} else if (enterInfo.ridingid.intValue() == SpaceBiz.BEAST_VALUE){
								bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.beast1);
							}
							// 计算该图片名字的长度，也就是要替换的字符串的长度
							SpannableString spannableString = new SpannableString("带着-" + new String(enterInfo.ridingname) + "坐骑进入大厅");
							if (bitmap != null) {
								if (enterInfo.ridingid.intValue() == SpaceBiz.CAR_MOTORING) {
									bitmap = Bitmap.createScaledBitmap(bitmap, petSize2, imageSize, true);
								} else {
									bitmap = Bitmap.createScaledBitmap(bitmap, petSize, petSize, true);
								}
								// 通过图片资源id来得到bitmap，用一个ImageSpan来包装
								ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
								// 将该图片替换字符串中规定的位置中
								spannableString.setSpan(imageSpan, 2, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//替换--
							}
							holder.contentTxt.setText(spannableString);
						}
					} else if (datainfo.type.intValue() == MessageType.SHOWSHAREBROADCASTINFO.getValue()) {
						ShowShareBroadcastInfo showShareBroadcastInfo = BroadCastUtils.getShowShareBroadcastInfo(datainfo);
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
						holder.iv_voice_listen.setVisibility(View.GONE);
						holder.ll_voice_play.setVisibility(View.GONE);
						holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_item_text_color));
						holder.contentTxt.setVisibility(View.VISIBLE);
						if (showShareBroadcastInfo != null) {//005bd7
							if (showShareBroadcastInfo.ownernick != null) {
								String showUserName = new String(showShareBroadcastInfo.ownernick) + "的房间";
								content = new String(showShareBroadcastInfo.txt);
								holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
								SpannableStringBuilder style = new SpannableStringBuilder(content + showUserName);
								style.setSpan(new PartShowRoomClick(showShareBroadcastInfo), content.length(), (content + showUserName).length(),
										Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
								holder.contentTxt.setText(style);
							} else {
								holder.contentTxt.setText("");
							}
						}

					} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_ANIMATION_BROADCAST.getValue()) {//仙术广播
						holder.iv_voice_listen.setVisibility(View.GONE);
						holder.ll_voice_play.setVisibility(View.GONE);
						holder.contentTxt.setVisibility(View.VISIBLE);
						holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
						holder.iv_decree.setImageDrawable(null);
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
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
							SpannableStringBuilder builde = new SpannableStringBuilder(getStyle(messageContent, usersStr, userInfoList));
							SpannableStringBuilder style1 = PartMagicListener.setStyle(mContext, strMagic);
							builde.append(style1);
							holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
							SpannableStringBuilder style2 = new SpannableStringBuilder(PLAYBACK);
							style2.setSpan(new OnPartTextPlayBackClickListener(textColor, mContext, broadcastInfo), 0, PLAYBACK.length(),
									Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
							builde.append(style2);
							holder.contentTxt.setText(builde);
						} else {
							SpannableStringBuilder builder = new SpannableStringBuilder(getStyle(content));
							SpannableStringBuilder style1 = PartMagicListener.setStyle(mContext, strMagic);
							builder.append(style1);
							holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
							SpannableStringBuilder style2 = new SpannableStringBuilder(PLAYBACK);
							style2.setSpan(new OnPartTextPlayBackClickListener(textColor, mContext, broadcastInfo), 0, PLAYBACK.length(),
									Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
							builder.append(style2);
							holder.contentTxt.setText(builder);
						}
					} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET.getValue()) {//红包接龙广播
						holder.iv_voice_listen.setVisibility(View.GONE);
						holder.ll_voice_play.setVisibility(View.GONE);
						holder.contentTxt.setVisibility(View.GONE);
						holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
						holder.iv_decree.setImageDrawable(null);
						holder.ll_braodcast_item_solitaire.setVisibility(View.VISIBLE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
						SolitaireRedpacket redpacket = RedDecodeUtil.parseSolitaireRedpacketXml(new String(datainfo.data));
						if (redpacket != null) {
							onGrabSolitaireClick.setRedpacket(redpacket);
							holder.tv_broadcast_jion_person.setText(mContext.getString(R.string.str_has_join_person, redpacket.getJionPerson()));
							if (redpacket.getGold() > 0) {
								holder.tv_broadcast_redpacket_money.setText(mContext.getString(R.string.str_current_money,
										mContext.getString(R.string.gold_money), redpacket.getGold() + mContext.getString(R.string.gold_money)));
							} else {
								holder.tv_broadcast_redpacket_money
										.setText(mContext.getString(R.string.str_current_money, mContext.getString(R.string.silver_money),
												redpacket.getSilver() + mContext.getString(R.string.silver_money)));
							}
						}
					} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET.getValue()) { //大厅普通红包
						holder.iv_voice_listen.setVisibility(View.GONE);
						holder.ll_voice_play.setVisibility(View.GONE);
						holder.contentTxt.setVisibility(View.GONE);
						holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
						holder.iv_decree.setImageDrawable(null);
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.VISIBLE);
						HallRedpacket hallRedpacket = RedDecodeUtil.parseRedpacketXml(new String(datainfo.data));
						if (hallRedpacket != null) {
							onGrabRedpacketClick.setRedPacketInfo(hallRedpacket);
							if (!TextUtils.isEmpty(hallRedpacket.getDesc())) {
								holder.tv_hall_desc.setText(hallRedpacket.getDesc());
							} else {
								holder.tv_hall_desc.setText("");
							}
							if (hallRedpacket.getRedpacketType() == 0) {
								holder.iv_official.setVisibility(View.GONE);
								if (userInfo != null) {
									holder.tv_hall_redpacket_type.setText(new String(userInfo.nick)
											+ mContext.getString(R.string.str_someone_a_redpacket));
								}
							} else {
								holder.tv_hall_redpacket_type.setText(R.string.str_official_redpacket);
								holder.iv_official.setVisibility(View.VISIBLE);
							}
						}
					}else if(datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_SPECIAL_EFFECT.getValue()){
						holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
						holder.iv_decree.setImageDrawable(null);
						holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
						holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
						content = new String(datainfo.data);
						if (!TextUtils.isEmpty(content)) {//文字广播
							content = BaseTextUtils.StringFilter(content);//对特殊字符对齐
							content = content.replaceAll("\n", "");
							holder.iv_voice_listen.setVisibility(View.GONE);
							holder.ll_voice_play.setVisibility(View.GONE);
							holder.contentTxt.setVisibility(View.VISIBLE);
							TextPaint tp2 = holder.contentTxt.getPaint();
							if (userInfo.uid.intValue() == 50000 && content.endsWith("组")) {
								holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.show_broadcast_system_color));
								tp2.setFakeBoldText(true);
							} else {
								holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_item_text_color));
								tp2.setFakeBoldText(false);
							}

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
						}
					} else {
						holder.contentTxt.setText("");
					}
				}
			} else {//兼容老数据
				holder.ll_bg.setBackgroundColor(Color.TRANSPARENT);
				holder.iv_decree.setImageDrawable(null);
				holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
				holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
				if (!TextUtils.isEmpty(content)) {//文字广播
					content = BaseTextUtils.StringFilter(content);//对特殊字符对齐
					content = content.replaceAll("\n", "");
					holder.iv_voice_listen.setVisibility(View.GONE);
					holder.ll_voice_play.setVisibility(View.GONE);
					holder.contentTxt.setVisibility(View.VISIBLE);
					TextPaint tp = holder.contentTxt.getPaint();
					if (userInfo.uid.intValue() == 50000 && content.endsWith("组")) {
						holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.show_broadcast_system_color));
						tp.setFakeBoldText(true);
					} else {
						holder.contentTxt.setTextColor(mContext.getResources().getColor(R.color.main_broadcast_item_text_color));
						tp.setFakeBoldText(false);
					}

					if (!TextUtils.isEmpty(sb.toString()) && usersStr != null) {//有@的用户让其名字变色

						SpannableString style = getStyle1(sb.toString() + content, usersStr, userInfoList);
						holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
						holder.contentTxt.setText(style);
					} else {
						//TODO
						String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
						SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode,
								HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
						if (builder.toString().contains("好运连连，大家恭喜他吧")) {
							holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
							SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
							SpannableStringBuilder action = new SpannableStringBuilder(" 我也要牛牛 ");
							action.setSpan(new BackgroundColorSpan(Color.parseColor("#2898fe")), 0, 6, 0);

							action.setSpan(new ClickableSpan() {

								@Override
								public void onClick(View arg0) {
									if (IAPKInfoUtil.isInstallApk(mContext, "com.example.gamedemo")) {
										String auth = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "")
												.getStringValueByKey(SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
										Bundle bundle = new Bundle();
										bundle.putString("auth", auth);
										bundle.putString("login_info", "4asfasfasfaaasfasf48");
										bundle.putString("silver", "0");
										ComponentName componentName = new ComponentName("com.example.gamedemo",
												"org.CrossApp.niuniuapp.NiuniuAppMain");
										IAPKInfoUtil.startApk(mContext, componentName, bundle);
									} else {
										//									DownLoadAPKDialog(apkUrl, "游戏未安装,请下载安装");
										MineFaqActivity.openMineFaqActivity(mContext, MineFaqActivity.GAMES_VALUE);
									}
								}
							}, 0, action.length(), 0);
							action.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
							spannableStringBuilder.append("\n").append(action).toString();
							holder.contentTxt.setText(spannableStringBuilder);
						} else {
							holder.contentTxt.setText(builder);
						}
					}
				} else if (!TextUtils.isEmpty(voice)) {//语音广播
					holder.ll_braodcast_item_solitaire.setVisibility(View.GONE);
					holder.ll_braodcast_item_redpacket.setVisibility(View.GONE);
					holder.contentTxt.setVisibility(View.GONE);
					holder.ll_voice_play.setVisibility(View.VISIBLE);
					holder.iv_decree.setImageDrawable(null);
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
				//礼物帽子
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
				//性别帽子
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

				int deliver_four_gift = DELIVER_GIFT_FOUR_FLAG & userstatus;
				int deliver_three_gift = DELIVER_GIFT_THREE_FLAG & userstatus;
				int deliver_two_gift = DELIVER_GIFT_TWO_FLAG & userstatus;
				int deliver_one_gift = DELIVER_GIFT_ONE_FLAG & userstatus;
				//头像挂件
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

			if (userInfo.uid.intValue() != 5000) {
				if (datainfo != null) {
					if (datainfo.type.intValue() == MessageType.WITHANTEFINGER.getValue()
							|| datainfo.type.intValue() == MessageType.NEWFINGER.getValue()) {
					} else {
						holder.typeNametxt.setText(sendUserName);
					}
				} else {
					holder.typeNametxt.setText(sendUserName);
				}

			}
			//			broadcastInfo.

			onHeadViewClick.setUserInfo(userInfo);
			onNameViewClick.setUserInfo(userInfo);
			//			System.out.println("结束时间========="+(System.currentTimeMillis()-startTime));
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
		ImageView iv_changer;
		ImageView privilege;//特权帽子
		ImageView deliverIv;//挂件
		View ll_braodcast_item_solitaire; //红包
		TextView tv_broadcast_redpacket_money; //红包金额
		TextView tv_broadcast_jion_person;
		//////////////////////大厅红包///////////////////
		View ll_braodcast_item_redpacket;
		TextView tv_hall_desc;
		TextView tv_hall_redpacket_type;
		ImageView iv_official;
	}

	public class RewardonClick extends ClickableSpan {

		@Override
		public void onClick(View widget) {
			Bundle bundle = new Bundle();
			bundle.putInt(RewardListActivity.LABLE_FLAG, RewardType.PROCCED.getValue());
			BaseUtils.openActivity(mContext, RewardListActivity.class, bundle);
		}
	}

	public class PartFingerClick extends ClickableSpan {//点击猜拳
		private FingerGuessingInfo fingerInfo;

		public PartFingerClick(FingerGuessingInfo fingerInfo) {
			this.fingerInfo = fingerInfo;
		}

		@Override
		public void updateDrawState(TextPaint ds) {//选中的文字变色
			super.updateDrawState(ds);//369a00
			ds.setColor(mContext.getResources().getColor(R.color.white));
			ds.setUnderlineText(false);

			ds.bgColor = mContext.getResources().getColor(R.color.orange);
			if (fingerInfo.antetype.intValue() == 1) {
				ds.bgColor = mContext.getResources().getColor(R.color.finger_backgroud_silver);
			}

		}

		@Override
		public void onClick(View widget) {
			if (BAApplication.mLocalUserInfo != null) {
				if (fingerInfo != null && fingerInfo.uid1.intValue() != BAApplication.mLocalUserInfo.uid.intValue()) {
					String monkey = fingerInfo.ante.intValue() + "金币";
					if (fingerInfo.antetype.intValue() == 1) {
						monkey = fingerInfo.ante.intValue() + "银币";
					}

					String content = String.format(mContext.getString(R.string.str_finger_broadcast_content1), monkey);
					SpannableStringBuilder style = new SpannableStringBuilder(content);
					style.setSpan(new GlodFingerClick(fingerInfo.antetype.intValue() == 0), 14, 16 + (fingerInfo.ante.intValue() + "").length(),
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					new PlayFingerDialog(mContext, style, mHandler, fingerInfo).showDialog();
				}
			}
		}
	}

	public class PartChargeClick extends ClickableSpan {//点击猜拳

		@Override
		public void updateDrawState(TextPaint ds) {//选中的文字变色
			super.updateDrawState(ds);//369a00
			ds.setColor(mContext.getResources().getColor(R.color.white));
			ds.setUnderlineText(false);
			ds.bgColor = mContext.getResources().getColor(R.color.broadcast_text_charge_color);
		}

		@Override
		public void onClick(View widget) {
			if (BAApplication.mLocalUserInfo != null) {
				BaseUtils.openActivity(mContext, StoreH5RechargeActivity.class);
			}
		}
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

	public class GlodFingerClick extends ClickableSpan {//点击猜拳

		private boolean isGload;

		public GlodFingerClick(boolean isGlod) {
			this.isGload = isGlod;
		}

		@Override
		public void updateDrawState(TextPaint ds) {//选中的文字变色
			super.updateDrawState(ds);//369a00
			if (isGload) {
				ds.setColor(mContext.getResources().getColor(R.color.orange));
			} else {
				ds.setColor(mContext.getResources().getColor(R.color.finger_silver_color));
			}
			ds.setUnderlineText(false);
		}

		@Override
		public void onClick(View widget) {}
	}

	private SpannableString getStyle(String messageContent, String[] usersStr, GoGirlUserInfoList userInfoList) {
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

	private SpannableString getStyle(String messageContent) {
		String unicode = EmojiParser.getInstance(mContext).parseEmoji(messageContent);
		SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode, HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
		return builder;
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

	private SpannableString getStyle2(String messageContent, String[] usersStr, GoGirlUserInfoList userInfoList) {
		String unicode = EmojiParser.getInstance(mContext).parseEmoji(messageContent);
		SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext, unicode, HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
		int len = usersStr.length;
		int startPos = 0;
		int endPos = 0;
		if (len == 2) {
			String aUsers = usersStr[0];
			String aUsers2 = usersStr[1];
			endPos = aUsers.length();
			builder.setSpan(new OnPartTextUserClickLlistener(mContext, (GoGirlUserInfo) userInfoList.get(0)), startPos, endPos,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			startPos += aUsers.length();
			if (!TextUtils.isEmpty(messageContent) && messageContent.contains(aUsers2)) {
				startPos = messageContent.indexOf(aUsers2);
				endPos = messageContent.indexOf("，并");

			}

			builder.setSpan(new OnPartTextUserClickLlistener(mContext, (GoGirlUserInfo) userInfoList.get(1)), startPos, endPos,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			startPos += aUsers2.length();
		} else if (len == 1) {
			String aUsers1 = usersStr[0];
			if (!TextUtils.isEmpty(messageContent) && messageContent.contains(aUsers1)) {
				startPos = messageContent.indexOf(aUsers1);
				endPos = messageContent.indexOf("，并");
			}
			builder.setSpan(new OnPartTextUserClickLlistener(mContext, (GoGirlUserInfo) userInfoList.get(0)), startPos, endPos,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			startPos += aUsers1.length();
		}
		return builder;
	}

	//红包接龙抢红包点击
	private class GrabSolitaireListener implements OnClickListener {
		private SolitaireRedpacket redpacket;

		public void setRedpacket(SolitaireRedpacket redpacket) {
			this.redpacket = redpacket;
		}

		@Override
		public void onClick(View v) {
			if (redpacket != null) {
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_CHECK_SOLITAIRE_REDPACKET_STATUS, redpacket);
			}
		}

	}

	//大厅红包抢红包点击
	private class GrabRedpacketListener implements OnClickListener {
		private HallRedpacket hallRedpacket;

		public void setRedPacketInfo(HallRedpacket hallRedpacket) {
			this.hallRedpacket = hallRedpacket;
		}

		@Override
		public void onClick(View v) {
			if (hallRedpacket != null) {
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_CHECK_REDPACKET_STATUS, hallRedpacket);
			}
		}

	}

	public interface IenterShowRoom {
		public void enterShowRoomCallback(ShowShareBroadcastInfo info);
	}
}
