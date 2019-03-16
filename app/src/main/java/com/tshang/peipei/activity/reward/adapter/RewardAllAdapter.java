package com.tshang.peipei.activity.reward.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.reward.AnonymNickDialog;
import com.tshang.peipei.activity.reward.ParticipatorListActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.RewardType;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.reward.RewardRequestControl;
import com.tshang.peipei.model.request.RequestRewardChatV2.GetRewardChatCallBackV2;
import com.tshang.peipei.model.request.RequestRewardJoin2.GetRewardJoinCallBack2;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.AwardDetail;
import com.tshang.peipei.protocol.asn.gogirl.AwardGiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.AwardTextInfo;
import com.tshang.peipei.protocol.asn.gogirl.ParticipateInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.display.RoundedBitmapDisplayer;

/**
 * @Title: RewardAllAdapter.java 
 *
 * @Description: 悬赏列表适配 
 *
 * @author Aaron  
 *
 * @date 2015-9-28 下午5:03:09 
 *
 * @version V1.0   
 */
public class RewardAllAdapter extends ArrayListAdapter<AwardDetail> implements GetRewardJoinCallBack2, GetRewardChatCallBackV2 {

	private final String TAG = this.getClass().getSimpleName();

	private DisplayImageOptions option;
	private DisplayImageOptions head_option;

	private Handler mHandler;

	private AwardDetail awardDetail;

	private Dialog dialog;

	private int type;

	private int anonymNickId;

	private AnonymNickDialog anonymNickDialog;

	public RewardAllAdapter(Activity context, Handler handler, int type) {
		super(context);
		this.mHandler = handler;
		option = ImageOptionsUtils.getRewardGiftOptions(context);
		head_option = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.homepage_headimg_defaut).cacheOnDisk(false)
				.cacheInMemory(false).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(context, 70))).bitmapConfig(Bitmap.Config.RGB_565).build();
		this.type = type;
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler = null;
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.reward_item_layout, null);
			viewHodler.giftIv = (ImageView) convertView.findViewById(R.id.reward_item_gift_iv);
			viewHodler.initiatorHeadIv = (ImageView) convertView.findViewById(R.id.reward_item_initiator_iv);
			viewHodler.lableIv = (ImageView) convertView.findViewById(R.id.reward_item_label_iv);

			viewHodler.coastTv = (TextView) convertView.findViewById(R.id.reward_item_cost_tv);
			viewHodler.charmTv = (TextView) convertView.findViewById(R.id.reward_item_charm_tv);
			viewHodler.initiatorAliasTv = (TextView) convertView.findViewById(R.id.reward_item_initiator_alias_tv);
			viewHodler.rewardContentTv = (TextView) convertView.findViewById(R.id.reward_item_content_tv);
			viewHodler.surplusTimeTv = (TextView) convertView.findViewById(R.id.reward_item_time_tv);
			viewHodler.joinTv = (TextView) convertView.findViewById(R.id.reward_item_join_tv);
			viewHodler.joinFullTv = (TextView) convertView.findViewById(R.id.reward_item_join_full_tv);
			viewHodler.chatTv = (TextView) convertView.findViewById(R.id.reward_item_chat_tv);
			viewHodler.giftTv = (TextView) convertView.findViewById(R.id.reward_item_gift_tv);
			viewHodler.initiatorTV = (TextView) convertView.findViewById(R.id.reward_item_initiator_tv);
			viewHodler.participationTv = (TextView) convertView.findViewById(R.id.reward_item_participation_tv);
			viewHodler.timeLayout = (LinearLayout) convertView.findViewById(R.id.reward_item_time_layout);
			viewHodler.participationLayout = (LinearLayout) convertView.findViewById(R.id.reward_item_participation_layout);
			viewHodler.integralTv = (TextView) convertView.findViewById(R.id.reward_item_integral_tv);
			viewHodler.moreIcon = (ImageView) convertView.findViewById(R.id.reward_item_participation_more_icon);
			viewHodler.patticipationHeadLayout = (LinearLayout) convertView.findViewById(R.id.reward_item_participation_head_layout);
			viewHodler.skillDesc = (TextView) convertView.findViewById(R.id.reward_item_skill_desc);
			viewHodler.tvSex = (TextView) convertView.findViewById(R.id.tv_reward_item_sex);
			viewHodler.sexIv = (ImageView) convertView.findViewById(R.id.reward_item_initiator_sex_iv);

			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		final AwardDetail entity = mList.get(position);
		final AwardTextInfo textInfo = entity.awardTextInfo;
		AwardGiftInfo giftInfo = entity.awardGiftInfo;
		//悬赏内容样式
		viewHodler.rewardContentTv.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, Color.parseColor("#" + new String(textInfo.fillcolor)),
				180, Color.parseColor("#" + new String(textInfo.fillcolor))));
		viewHodler.rewardContentTv.setTextColor(Color.parseColor("#" + new String(textInfo.textcolor)));
		viewHodler.rewardContentTv.setText(new String(textInfo.content));
		viewHodler.skillDesc.setText(new String(textInfo.revstr0));
		//悬赏礼物
		String imgKey = new String(giftInfo.pickey) + BAConstants.LOAD_180_APPENDSTR;
		imageLoader.displayImage("http://" + imgKey, viewHodler.giftIv, option);
		//实物属性
		if (giftInfo.pricegold.intValue() > 0) {
			viewHodler.coastTv.setText(mContext.getResources().getString(R.string.reward_affirm_cost) + giftInfo.pricegold.intValue()
					+ mContext.getResources().getString(R.string.gold_money));
		} else {
			viewHodler.coastTv.setText(mContext.getResources().getString(R.string.reward_affirm_cost) + giftInfo.pricesilver.intValue()
					+ mContext.getResources().getString(R.string.silver_money));
		}
		viewHodler.integralTv.setText(mContext.getResources().getString(R.string.reward_affirm_integral) + giftInfo.scoreeffect.intValue() + "");
		viewHodler.charmTv.setText(mContext.getResources().getString(R.string.reward_affirm_charm) + giftInfo.charmeffect.intValue());

		//参与人数
		int participateNum = entity.participateInfoList.size();
		viewHodler.participationTv.setText(participateNum + "/" + entity.maxparticipatenum.intValue()
				+ mContext.getResources().getString(R.string.pariti_str));
		viewHodler.patticipationHeadLayout.removeAllViews();
		lab: for (int i = 0; i < participateNum; i++) {
			if (i > 4) {
				break lab;
			}
			viewHodler.headaView = LayoutInflater.from(mContext).inflate(R.layout.reward_paritipation_head_view, null);
			viewHodler.headIv = (ImageView) viewHodler.headaView.findViewById(R.id.reward_item_participation_headview_iv);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0);
			params.leftMargin = 5;
			viewHodler.headaView.setLayoutParams(params);
			if (entity.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()
					|| BAApplication.mLocalUserInfo.uid.intValue() == ((ParticipateInfo) entity.participateInfoList.get(i)).userinfo.uid.intValue()
					|| ((ParticipateInfo) entity.participateInfoList.get(i)).userinfo.uid.intValue() == anonymNickId
					|| anonymNickId == entity.uid.intValue()) {
				if (entity.isanonymous.intValue() == 1) {
					if (((ParticipateInfo) entity.participateInfoList.get(i)).userinfo.sex.intValue() == Gender.MALE.getValue()) {
						viewHodler.headIv.setImageResource(R.drawable.dynamic_defalut_man);
					} else {
						viewHodler.headIv.setImageResource(R.drawable.dynamic_defalut_woman);
					}
				} else {
					imageLoader.displayImage("http://" + ((ParticipateInfo) entity.participateInfoList.get(i)).userinfo.uid.intValue()
							+ BAConstants.LOAD_HEAD_UID_APPENDSTR, viewHodler.headIv, head_option);
				}
			} else {
				imageLoader.displayImage("http://" + 000, viewHodler.headIv, head_option);
			}
			viewHodler.patticipationHeadLayout.addView(viewHodler.headaView);
		}
		if (BAApplication.mLocalUserInfo.uid.intValue() == entity.uid.intValue() || entity.uid.intValue() == anonymNickId && participateNum > 0) {
			viewHodler.moreIcon.setVisibility(View.VISIBLE);
		} else {
			viewHodler.moreIcon.setVisibility(View.GONE);
		}

		//悬赏状态
		final int rewardStatus = entity.awardstatus.intValue();
		switch (rewardStatus) {
		case 0://未参加
				//女性发布悬赏，男女都可以参加，男性发布悬赏只有女性可以参加
			if (entity.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue() || entity.uid.intValue() == anonymNickId) {
				viewHodler.joinTv.setVisibility(View.GONE);
			} else if (entity.sex.intValue() == Gender.MALE.getValue() && BAApplication.mLocalUserInfo.sex.intValue() == Gender.MALE.getValue()) {
				viewHodler.joinTv.setVisibility(View.GONE);
			} else {
				viewHodler.joinTv.setVisibility(View.VISIBLE);
			}
			viewHodler.lableIv.setVisibility(View.GONE);
			viewHodler.joinFullTv.setVisibility(View.GONE);
			viewHodler.chatTv.setVisibility(View.GONE);
			viewHodler.timeLayout.setVisibility(View.VISIBLE);
			break;
		case 1://人数已满
			viewHodler.lableIv.setVisibility(View.VISIBLE);
			viewHodler.lableIv.setBackgroundResource(R.drawable.reward_join_full);

			if (BAApplication.mLocalUserInfo.sex.intValue() == Gender.FEMALE.getValue()) {
				viewHodler.joinFullTv.setVisibility(View.VISIBLE);
			} else {
				viewHodler.joinFullTv.setVisibility(View.GONE);
			}
			viewHodler.joinTv.setVisibility(View.GONE);
			viewHodler.chatTv.setVisibility(View.GONE);
			viewHodler.timeLayout.setVisibility(View.VISIBLE);
			break;
		case 2://已参加
			if (entity.uid.intValue() != BAApplication.mLocalUserInfo.uid.intValue()) {
				viewHodler.lableIv.setVisibility(View.VISIBLE);
				viewHodler.lableIv.setBackgroundResource(R.drawable.reward_join_over);

				viewHodler.chatTv.setVisibility(View.VISIBLE);
			}
			viewHodler.joinFullTv.setVisibility(View.GONE);
			viewHodler.joinTv.setVisibility(View.GONE);
			viewHodler.timeLayout.setVisibility(View.VISIBLE);
			break;
		case 3://已结束
			viewHodler.lableIv.setVisibility(View.VISIBLE);
			viewHodler.lableIv.setBackgroundResource(R.drawable.reward_join_end);

			viewHodler.joinFullTv.setVisibility(View.GONE);
			viewHodler.joinTv.setVisibility(View.GONE);
			viewHodler.chatTv.setVisibility(View.GONE);
			viewHodler.timeLayout.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		//发布悬赏，悬赏结束，赢得悬赏，（受赏者）
		final int winuid = entity.winuid.intValue();
		final String winNick = new String(entity.winnick);
		if (rewardStatus == 3 && winuid >= 0 && !TextUtils.isEmpty(winNick) && type == RewardType.PULISH.getValue()) {
			//赢得悬赏者信息
			viewHodler.initiatorTV.setText(R.string.reward_receive);
			String headKey = winuid + BAConstants.LOAD_HEAD_UID_APPENDSTR;
			//显示备注
			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(winuid);

			viewHodler.initiatorTV.setText(mContext.getResources().getString(R.string.reward_receive));
			if (entity.winsex.intValue() == Gender.MALE.getValue()) {
				viewHodler.initiatorTV.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, Color.parseColor("#668ef2"), 3,
						Color.parseColor("#668ef2")));
			} else {
				viewHodler.initiatorTV.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, Color.parseColor("#f68aa3"), 3,
						Color.parseColor("#f68aa3")));
			}
			//匿名
			if (entity.isanonymous.intValue() == 1) {
				viewHodler.initiatorAliasTv.setText(new String(entity.winnick));
				if (entity.winsex.intValue() == Gender.MALE.getValue()) {
					viewHodler.initiatorHeadIv.setImageResource(R.drawable.dynamic_defalut_man);
					viewHodler.sexIv.setImageResource(R.drawable.broadcast_img_boy);
				} else {
					viewHodler.initiatorHeadIv.setImageResource(R.drawable.dynamic_defalut_woman);
					viewHodler.sexIv.setImageResource(R.drawable.broadcast_img_girl);
				}
			} else {
				viewHodler.initiatorAliasTv.setText(TextUtils.isEmpty(alias) ? winNick : alias);
				imageLoader.displayImage("http://" + headKey, viewHodler.initiatorHeadIv, head_option);
			}

			//性别
			if (entity.winsex.intValue() == Gender.MALE.getValue()) {
				viewHodler.sexIv.setImageResource(R.drawable.broadcast_img_boy);
			} else {
				viewHodler.sexIv.setImageResource(R.drawable.broadcast_img_girl);
			}
		} else {
			//赏主信息
			if (entity.isanonymous.intValue() == 1) {
				viewHodler.initiatorTV.setText("匿名赏主");
			} else {
				viewHodler.initiatorTV.setText(R.string.reward_lead);
			}
			String headKey = entity.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
			//显示备注
			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					entity.uid.intValue());
			viewHodler.initiatorAliasTv.setText(entity.isanonymous.intValue() == 1 ? new String(entity.nick) : TextUtils.isEmpty(alias) ? new String(
					entity.nick) : alias);
//			viewHodler.initiatorTV.setText(mContext.getResources().getString(R.string.reward_lead));
			if (entity.sex.intValue() == Gender.MALE.getValue()) {
				viewHodler.initiatorTV.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, Color.parseColor("#668ef2"), 3,
						Color.parseColor("#668ef2")));
			} else {
				viewHodler.initiatorTV.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, Color.parseColor("#f68aa3"), 3,
						Color.parseColor("#f68aa3")));
			}
			//匿名
			if (entity.isanonymous.intValue() == 1) {
				if (entity.sex.intValue() == Gender.MALE.getValue()) {
					viewHodler.initiatorHeadIv.setImageResource(R.drawable.dynamic_defalut_man);
				} else {
					viewHodler.initiatorHeadIv.setImageResource(R.drawable.dynamic_defalut_woman);
				}
			} else {
				imageLoader.displayImage("http://" + headKey, viewHodler.initiatorHeadIv, head_option);
			}

			//性别
			if (entity.sex.intValue() == Gender.MALE.getValue()) {
				viewHodler.sexIv.setImageResource(R.drawable.broadcast_img_boy);
			} else {
				viewHodler.sexIv.setImageResource(R.drawable.broadcast_img_girl);
			}
		}

		//剩余时间
		long endTime = entity.leftime.intValue();
		viewHodler.surplusTimeTv.setText(BaseTimes.getHH_mmTime(endTime));
		viewHodler.giftTv.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, Color.parseColor("#FFA623"), 3, Color.parseColor("#FFA623")));

		viewHodler.participationLayout.setOnClickListener(new onItemBtnOnClick(entity, textInfo));
		viewHodler.joinTv.setOnClickListener(new onItemBtnOnClick(entity, textInfo));
		viewHodler.chatTv.setOnClickListener(new onItemBtnOnClick(entity, textInfo));
		viewHodler.initiatorHeadIv.setOnClickListener(new onItemBtnOnClick(entity, textInfo));
		return convertView;
	}

	private class ViewHodler {
		private ImageView giftIv;
		private ImageView initiatorHeadIv;
		private ImageView lableIv;
		private TextView coastTv;
		private TextView charmTv;
		private TextView initiatorAliasTv;
		private TextView rewardContentTv;
		private TextView surplusTimeTv;
		private TextView joinTv, joinFullTv, chatTv;
		private TextView giftTv;
		private TextView initiatorTV;
		private TextView participationTv;
		private LinearLayout timeLayout;
		private TextView integralTv;
		private ImageView moreIcon;
		private LinearLayout patticipationHeadLayout;
		private TextView skillDesc;
		private TextView tvSex;
		private ImageView sexIv;

		private LinearLayout participationLayout;

		private View headaView;
		private ImageView headIv;
	}

	private boolean isJoin = false;//处理变太测试

	/**
	 * 点击事件
	 * @author Aaron
	 *
	 */
	private class onItemBtnOnClick implements OnClickListener {

		private AwardDetail entity;
		private AwardTextInfo textInfo;

		public onItemBtnOnClick(AwardDetail entity, AwardTextInfo textInfo) {
			this.entity = entity;
			this.textInfo = textInfo;
		}

		@SuppressLint("InflateParams")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.reward_item_participation_layout:
				if (entity.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue() || entity.uid.intValue() == anonymNickId) {
					Bundle bundle = new Bundle();
					bundle.putInt("type", type);
					bundle.putInt("awarduid", entity.uid.intValue());
					bundle.putInt("awardid", entity.id.intValue());
					bundle.putInt("anonym", entity.isanonymous.intValue());
					BaseUtils.openActivity(mContext, ParticipatorListActivity.class, bundle);
				}
				break;
			case R.id.reward_item_join_tv:
				if (isJoin) {
					return;
				}
				isJoin = true;
				View view = LayoutInflater.from(mContext).inflate(R.layout.reward_join_comfir_dialog, null);
				TextView skill = (TextView) view.findViewById(R.id.reward_join_comfir_skill);
				TextView skillDesc = (TextView) view.findViewById(R.id.reward_join_comfir_skill_desc);
				skill.setText(new String(textInfo.content) + "：");
				skillDesc.setText(new String(textInfo.revstr0));
				dialog = DialogFactory.showMsgDialog(mContext, "悬赏说明", view, "", "", new OnClickListener() {

					@Override
					public void onClick(View v) {
						DialogFactory.dimissDialog(dialog);
						isJoin = false;
					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (anonymNickDialog != null) {
							anonymNickId = anonymNickDialog.getAnonymNickId();
						}
						DialogFactory.dimissDialog(dialog);
						dialog = DialogFactory.createLoadingDialog(mContext, R.string.reward_req_paritici_dialog);
						DialogFactory.showDialog(dialog);
						awardDetail = entity;
						RewardRequestControl control = new RewardRequestControl();
						control.requestJoinReward(type, entity.uid.intValue(), entity.isanonymous.intValue() == 1 ? anonymNickId
								: BAApplication.mLocalUserInfo.uid.intValue(), entity.id.intValue(), entity.isanonymous.intValue(),
								RewardAllAdapter.this);
					}
				});
				break;
			case R.id.reward_item_chat_tv:
				if (anonymNickDialog != null) {
					anonymNickId = anonymNickDialog.getAnonymNickId();
				}
				dialog = DialogFactory.createLoadingDialog(mContext, mContext.getResources().getString(R.string.reward_req_chat));
				DialogFactory.showDialog(dialog);
				awardDetail = entity;
				RewardRequestControl control2 = new RewardRequestControl();
				control2.requestChatV2(type, entity.uid.intValue(), entity.isanonymous.intValue() == 1 ? anonymNickId
						: BAApplication.mLocalUserInfo.uid.intValue(), entity.id.intValue(), entity.isanonymous.intValue(), RewardAllAdapter.this);
				break;
			case R.id.reward_item_initiator_iv:
				//匿名悬赏点击头像不做处理
				if (entity.isanonymous.intValue() == 1) {
					return;
				}
				if (entity.awardstatus.intValue() == 3 && entity.winuid.intValue() >= 0 && !TextUtils.isEmpty(new String(entity.winnick))
						&& type == 4) {
					SpaceUtils.toSpaceCustom((Activity) mContext, entity.winuid.intValue(), entity.winsex.intValue());
				} else {
					SpaceUtils.toSpaceCustom((Activity) mContext, entity.uid.intValue(), entity.sex.intValue());
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onJoinSuccess(int code, final String message) {
		isJoin = false;
		if (code == -28100) {//不能参加悬赏
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					dialog = DialogFactory.warnMsgDialog(mContext, null, message, mContext.getResources().getString(R.string.dialog_confirm));
				}
			});
		} else if (code == 0) {
			RewardRequestControl control2 = new RewardRequestControl();
			control2.requestChatV2(type, awardDetail.uid.intValue(), awardDetail.isanonymous.intValue() == 1 ? anonymNickId
					: BAApplication.mLocalUserInfo.uid.intValue(), awardDetail.id.intValue(), awardDetail.isanonymous.intValue(),
					RewardAllAdapter.this);
		} else if (code == -21001) {//匿名Nick过期
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					anonymNickDialog = new AnonymNickDialog(mContext);
					anonymNickDialog.bindAnonymNick();
				}
			});
		} else {
			Message msg = new Message();
			msg.what = HandlerValue.JOIN_REWARD_SUCCESS;
			msg.arg1 = code;
			msg.obj = awardDetail;
			mHandler.sendMessage(msg);
		}
		DialogFactory.dimissDialog(dialog);
	}

	@Override
	public void onJoinError(int code) {
		isJoin = false;
		Message msg = new Message();
		msg.what = HandlerValue.JOIN_REWARD_ERROR;
		mHandler.sendMessage(msg);
		DialogFactory.dimissDialog(dialog);
	}

	@Override
	public void onChatSuccess(int code) {
		if (code == -21001) {//匿名Nick过期
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					anonymNickDialog = new AnonymNickDialog(mContext);
					anonymNickDialog.bindAnonymNick();
				}
			});
		} else {
			Message msg = new Message();
			msg.what = HandlerValue.REWARD_CHAT_SUCCESS;
			msg.arg1 = code;
			msg.obj = awardDetail;
			mHandler.sendMessage(msg);
		}
		DialogFactory.dimissDialog(dialog);
	}

	@Override
	public void onChatError(int code) {
		Message msg = new Message();
		msg.what = HandlerValue.REWARD_CHAT_ERROR;
		mHandler.sendMessage(msg);
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				DialogFactory.dimissDialog(dialog);
			}
		});
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setAnonymNickId(int anonymNickId) {
		this.anonymNickId = anonymNickId;
	}
}
