package com.tshang.peipei.activity.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.tauth.Tencent;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.EmotionViewAdd;
import com.tshang.peipei.activity.chat.adapter.EmotionViewAdd.HaremEmotionClickListener;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.FailedReSendListener;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.activity.chat.adapter.ViewBurnImageChatItemAdapter.ChatBurnImageInterface;
import com.tshang.peipei.activity.chat.adapter.ViewGoddessSkillChatItemAdapter.SkillInviteResCallBack;
import com.tshang.peipei.activity.chat.adapter.ViewHaremFaceChatItemAdapter.OnClickListenrHaremVoice;
import com.tshang.peipei.activity.chat.adapter.ViewVoiceChatItemAdapter.ChatClickVoiceInterface;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.EmotionBean;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.activity.dialog.CanGrapSolitaireRedpacketDialog;
import com.tshang.peipei.activity.dialog.ChatMoreDialog;
import com.tshang.peipei.activity.dialog.ChatVoiceDialog;
import com.tshang.peipei.activity.dialog.DarePassDialog;
import com.tshang.peipei.activity.dialog.DareResultDialog;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.GetRedPacketDialog;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.dialog.PhotoSetDialog;
import com.tshang.peipei.activity.dialog.PlayFingerDialog;
import com.tshang.peipei.activity.dialog.SendGiftByChatDialog;
import com.tshang.peipei.activity.dialog.ShareSystemNoticeDialog;
import com.tshang.peipei.activity.dialog.SolitaireGrapRedPacketFailDialog;
import com.tshang.peipei.activity.dialog.SolitaireGrapRedPacketSuccessDialog;
import com.tshang.peipei.activity.dialog.SolitaireRedPacketInfoDialog;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.redpacket.SendRedPacketActivity;
import com.tshang.peipei.activity.redpacket2.SendSolitaireRedPacketActivity;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.activity.skillnew.SkillInviteActivity;
import com.tshang.peipei.activity.skillnew.adapter.ChatSkillAdapter;
import com.tshang.peipei.activity.skillnew.bean.SkillResultBean;
import com.tshang.peipei.activity.store.StoreGiftListActivity;
import com.tshang.peipei.base.BaseCameraGalleryPhoto;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatDes;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.BaiduCloudUtils;
import com.tshang.peipei.model.biz.chat.BaseChatSendMessage;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.biz.chat.ChatManageBiz.IPersistListener;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.biz.chat.ChatVoiceBiz;
import com.tshang.peipei.model.biz.chat.SaveChatData;
import com.tshang.peipei.model.biz.chat.VideoUtils;
import com.tshang.peipei.model.biz.chat.groupchat.FingerGruessMessage;
import com.tshang.peipei.model.biz.chat.groupchat.GroupChatUtils;
import com.tshang.peipei.model.biz.chat.groupchat.PlayDareBiz;
import com.tshang.peipei.model.biz.chat.groupchat.SendGroupChatRedPacketMessage;
import com.tshang.peipei.model.biz.chat.groupchat.SendGroupChatTextMessage;
import com.tshang.peipei.model.biz.chat.privatechat.PlayTruth;
import com.tshang.peipei.model.biz.chat.privatechat.SendPrivateChatImageMessage;
import com.tshang.peipei.model.biz.chat.privatechat.SendPrivateChatTextMessage;
import com.tshang.peipei.model.biz.store.StoreGiftBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.model.event.ChatEvent;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.harem.CreateHarem;
import com.tshang.peipei.model.redpacket2.SolitaireRedPacketBiz;
import com.tshang.peipei.model.request.RequestCheckRedpacketState.CheckRedpacketStateCallBack;
import com.tshang.peipei.model.request.RequestDareSendFlowers;
import com.tshang.peipei.model.request.RequestGetGroupInfo;
import com.tshang.peipei.model.request.RequestDareSendFlowers.IDareSendFlowers;
import com.tshang.peipei.model.request.RequestGetGroupInfo.IGetGroupInfo;
import com.tshang.peipei.model.request.RequestSkillInviteResult.SkillInviteResultCallBack;
import com.tshang.peipei.model.request.RequestSolitaireRedPacketMoney.GetSolitaireRedPacketCallBack;
import com.tshang.peipei.model.skillnew.GoddessSkillEngine;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserSimpleInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserSimpleInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.entity.RelationEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.operate.RedpacketOperate;
import com.tshang.peipei.storage.database.operate.RelationOperate;
import com.tshang.peipei.storage.database.table.ChatTable;
import com.tshang.peipei.vender.common.util.ImageUtils;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.assist.ImageSize;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;
import com.tshang.peipei.vender.micode.soundrecorder.RecorderReceiver;
import com.tshang.peipei.vender.micode.soundrecorder.RecorderService;
import com.tshang.peipei.vender.micode.soundrecorder.RemainingTimeCalculator;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView.ChatScrollListener;
import com.tshang.peipei.view.BurnPicView;
import com.tshang.peipei.view.HorizontalListView;
import com.tshang.peipei.view.PageControlView;
import com.tshang.peipei.view.RepeatButton;
import com.tshang.peipei.view.fireview.ObserveFireView;
import com.tshang.peipei.view.fireview.OnDismissCallback;
import com.tshang.peipei.view.fireview.SwipeDismissAdapter;

import de.greenrobot.event.EventBus;

/**
 * @Title: 聊天界面
 *
 * @Description: 显示聊天数据
 *
 * @author allen
 *
 * @date 2014-3-26 下午4:00:06 
 *
 * @version V1.0   
 */
public class ChatActivity extends ChatBaseActivity implements OnTouchListener, OnDismissCallback, Recorder.OnStateChangedListener, IPersistListener,
		ChatScrollListener, ChatClickVoiceInterface, ChatBurnImageInterface, FailedReSendListener, OnScrollListener, HaremEmotionClickListener,
		OnClickListenrHaremVoice, IDareSendFlowers, SkillInviteResCallBack, SkillInviteResultCallBack, CheckRedpacketStateCallBack,
		GetSolitaireRedPacketCallBack, NickOnClickListener {

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.CHAT_RECEIVE_ONLINE_MESSAGE_NEW://停留在当前聊天界面收到消息了，更改返回按钮的数量
			tvBack.setText("返回(" + msg.arg1 + ")");
			break;
		case HandlerValue.CHAT_SYSTEM_NOTICE_SHARE_VALUE://系统通知分享过来
			ChatMessageEntity chatMessageEntity = (ChatMessageEntity) msg.obj;
			if (chatMessageEntity != null) {
				new ShareSystemNoticeDialog(this, chatMessageEntity, mHandler, mTencent, mWXapi).showDialog(0, 0);
			}
			break;
		case HandlerValue.HAREM_GROUP_IS_OWNER_VALUE://判断是否是群主进入到群管理界面
			GroupInfo info = (GroupInfo) msg.obj;
			new ChatMoreDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mFriendUid, mFriendNick, mHandler, true, info, from).showDialog(0,
					0);
			break;
		case HandlerValue.CHAT_REMOVE_GUESS_FINGER_VALUE://已经回复了猜拳，移除掉发起猜拳数据
			mChatListAdpater.removeObject((ChatDatabaseEntity) msg.obj);
			break;
		case HandlerValue.CHAT_GUESS_FINGER_TIME_OUT_VALUE://猜拳失效
			ChatDatabaseEntity entity = (ChatDatabaseEntity) msg.obj;
			if (entity != null) {
				mChatListAdpater.removeObject(entity);
				String timeOutStr = this.getString(R.string.str_chat_gueess_finger_private_failed);
				if (isGroupChatValue) {
					timeOutStr = this.getString(R.string.str_chat_gueess_finger_failed);
				}
				mChatListAdpater
						.appendToList(ChatManageBiz.insertMessage(this, mFriendUid, timeOutStr, System.currentTimeMillis(), isGroupChatValue));
				mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
			}
			break;
		case HandlerValue.CHAT_DARE_RPS_GRADE:
			BaseUtils.showTost(this, "等级达到将军/嫔妃将开启猜拳功能。");
			break;
		case HandlerValue.RED_PACKET_UNPAKCET_SUCCESS_VALUE://红包领取成功
			RedPacketInfo redPacketinfo = (RedPacketInfo) msg.obj;
			if (redPacketinfo != null) {
				UserSimpleInfoList list = redPacketinfo.records;
				if (list != null && !list.isEmpty()) {
					UserSimpleInfo userSimpleInfo = (UserSimpleInfo) list.get(0);
					if (userSimpleInfo != null) {
						ChatDatabaseEntity chatEntity = new ChatDatabaseEntity();//组装消息 
						chatEntity.setDes(ChatDes.TO_ME.getValue());
						chatEntity.setFromID(userSimpleInfo.uid.intValue());
						chatEntity.setToUid(mFriendUid);
						chatEntity.setStatus(ChatStatus.READED.getValue());
						chatEntity.setType(MessageType.UNPACKETREDPACKET.getValue());//就是自己拆红包成功了
						chatEntity.setMesSvrID("");
						chatEntity.setCreateTime(userSimpleInfo.createtime.longValue() * 1000);
						chatEntity.setMesLocalID(-1);
						long isSaveSuccess = SaveChatData.saveRedPacketInfoMsg(this, BaseChatSendMessage.GROUP_CHAT_TYPE, mFriendSex, mFriendNick,
								chatEntity, redPacketinfo);
						if (isSaveSuccess >= 0) {
							appendChatData(chatEntity);
						}
					}
				}
			}
			new GetRedPacketDialog(this, android.R.style.Theme_Translucent_NoTitleBar, redPacketinfo, false).showDialog();
			break;
		case HandlerValue.RED_PACKET_UNPACKET_NO_MONEY_VALUE://红包被领完了，你晚来了
			BaseUtils.showTost(this, R.string.str_redpacket_get_over);
			break;
		case HandlerValue.RED_PACKET_UNPACKET_TIMEOUT_VALUE://红包超时失效了
			BaseUtils.showTost(this, R.string.str_redpacket_timeout);
			break;
		case HandlerValue.RED_PACKET_UNPACKET_HAVE_RECEIVER_SUCCESS_VALUE://您已经领取过红包了
			RedPacketInfo redPacketinfoget = (RedPacketInfo) msg.obj;
			new GetRedPacketDialog(this, android.R.style.Theme_Translucent_NoTitleBar, redPacketinfoget, true).showDialog();
			break;
		case HandlerValue.RED_PACKET_UNPACKET_FAILED_VALUE://各种原因红包领取失败
			BaseUtils.showTost(this, R.string.str_unpacket_failed);
			break;
		case HandlerValue.CHAT_LOAD_HISTORY_DATA_VALUE://加载更多聊天数据
			mChatListView.onRefreshComplete();
			List<ChatDatabaseEntity> temp = (List<ChatDatabaseEntity>) msg.obj;
			if (isLoadMore) {//点击关于我加载更多
				mChatListView.onRefreshComplete();
				if (temp != null && !temp.isEmpty()) {
					int oldListSize = mChatListAdpater.getCount();
					//TODO
					mChatListAdpater.appendPositionToList(0, temp);
					int position = -1;
					a: for (int i = 0; i < temp.size(); i++) {
						if (temp.get(i).getMessage().contains("@" + new String(BAApplication.mLocalUserInfo.nick))) {
							position = i;
							break a;
						}
					}
					Log.d("Aaron", "position===" + position);
					if (position == -1) {
						mChatListView.setRefreshing();
						mChatListView.getRefreshableView().setSelection(temp.size());
					} else {
						mChatListView.getRefreshableView().setSelection(position + 1);
					}
					//					mChatListAdpater.notifyDataSetChanged();
				}
			} else {//手动刷新更多
				if (temp != null && !temp.isEmpty()) {
					mChatListAdpater.appendPositionToList(0, temp);
					if (mChatListAdpater.getCount() > temp.size()) {//控制加载显示的位置
						mChatListView.getRefreshableView().setSelection(temp.size());
					} else {
						mChatListView.getRefreshableView().setSelection(temp.size() - 1);
					}
					//				mChatListAdpater.notifyDataSetInvalidated();
					mChatListAdpater.notifyDataSetChanged();
				}
			}
			break;
		case HandlerValue.CHAT_LOAD_HISTORY_NO_DATA_VALUE://没有聊天数据了
			BaseUtils.showTost(this, "已加载全部数据了");
			mChatListView.setMode(Mode.PULL_FROM_END);
			mChatListView.onRefreshComplete();
			break;
		case HandlerValue.CHAT_RECEIVER_NEW_MESSAGE_SUCCESS_VALUE://停留在当前界面收到新的消息
			ChatDatabaseEntity cEntity = (ChatDatabaseEntity) msg.obj;
			if (cEntity != null) {
				int fromId = cEntity.getFromID();
				if (msg.arg1 == 1) {//群聊
					fromId = cEntity.getToUid();
				}
				int type = cEntity.getType();
				if (type == 36 || type == 86) {//后宫表情
					smileVoiceList.add(cEntity.getMessage());
				} else if (type == 54) {
					int des = cEntity.getDes();
					if (des == BAConstants.ChatDes.TO_ME.getValue()) {
						currDareId = cEntity.getMesSvrID();
						dareHintLayout.setVisibility(View.VISIBLE);
						BAApplication.dareId = currDareId;
					} else {
						dareHintLayout.setVisibility(View.GONE);
						currDareId = "";
					}
				} else if (type == 59) {
					dareLayout.setVisibility(View.GONE);
					dareHintLayout.setVisibility(View.GONE);
					currDareId = "";
					mChatListAdpater.setDareByBurnId(cEntity.getProgress(), cEntity.getMesSvrID());
				} else if (type == 55) {
					if (dareDialog != null) {
						dareDialog.dismiss();
					}
					mChatListAdpater.setDareByBurnId(cEntity.getProgress(), cEntity.getMesSvrID());//改变状态
					dareDialog = new DareResultDialog(this, cEntity, mHandler);
					dareDialog.showDialog();
					dareHintLayout.setVisibility(View.GONE);
					currDareId = cEntity.getMesSvrID();
					String sb = BAApplication.dareUidList.toString().substring(0, BAApplication.dareUidList.length() - 1);
					String[] strs = sb.split(",");
					boolean isShow = false;
					for (int i = 0; i < strs.length; i++) {
						if (BAApplication.mLocalUserInfo.uid.intValue() == Integer.parseInt(strs[i])) {
							isShow = true;
						}
					}
					if (!TextUtils.isEmpty(currDareId) && isShow) {
						dareLayout.setVisibility(View.VISIBLE);
						String showMessage = cEntity.getMessage();
						if (!TextUtils.isEmpty(showMessage)) {
							ChatMessageEntity entityG = ChatMessageBiz.decodeMessage(showMessage);

							if (isLoster(entityG)) {
								dareEgg.setVisibility(View.GONE);
								dareBrick.setVisibility(View.GONE);
								dareFlower.setVisibility(View.GONE);
								darePass.setVisibility(View.VISIBLE);
							} else {
								dareEgg.setVisibility(View.VISIBLE);
								dareBrick.setVisibility(View.VISIBLE);
								dareFlower.setVisibility(View.VISIBLE);
								darePass.setVisibility(View.GONE);
							}
						}
					}
				} else if (type == 57) {
					BaseUtils.showTost(this, cEntity.getMessage());
				}
				if (!smileVoiceList.isEmpty() && !mVoiceRecod.isPlaying()) {
					playSmileVoice(smileVoiceList.get(0), true);
				}
				if (fromId == mFriendUid
						&& ((msg.arg1 == BaseChatSendMessage.GROUP_CHAT_TYPE && isGroupChatValue)
								|| (msg.arg1 == BaseChatSendMessage.PRIVATE_CHAT_TYPE && !isGroupChatValue) || (msg.arg1 == BaseChatSendMessage.PRIVATE_CHAT_ANONYM_TYPE && !isGroupChatValue))
						&& type != 57) {//说明不是在当前的界面聊天
					appendChatData(cEntity);
					if (!TextUtils.isEmpty(cEntity.getMessage())
							&& cEntity.getMessage().contains("@" + new String(BAApplication.mLocalUserInfo.nick))
							&& aboutLayout.getVisibility() == View.INVISIBLE) {
						aboutLayout.setVisibility(View.VISIBLE);
					}
				}
			}
			break;
		case HandlerValue.CHAT_FORBIT_MESSAGE_VALUE://被禁言了
			new HintToastDialog(this, R.string.limit_talk, R.string.ok).showDialog();
			break;
		case HandlerValue.CHAT_MONEY_NOT_ENGOUG_VALUE://财富不足
			new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			break;
		case HandlerType.GIFT_BUY_RETURN:
			RelationOperate relationOperate = RelationOperate.getInstance(this);
			RelationEntity relationEntity = relationOperate.selectSessionDate(mFriendUid);

			if (relationEntity != null) {
				int id = 1;
				if (giftInfo != null) {
					id = giftInfo.id.intValue();
				}
				new StoreGiftBiz().buyGift(this, id, mFriendUid, giftNum, 0, mHandler);
			}
			break;
		case HandlerType.USER_GIFT_REQUEST:
			if (msg.arg1 == 0) {
				getRelationship(BAApplication.mLocalUserInfo);
			} else {
				int count = 100;
				if (giftInfo != null) {
					count = giftInfo.pricesilver.intValue();
				}
				if (count * giftNum > SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
						"silvercoin", 0)) {
					new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, SharedPreferencesTools.getInstance(this,
							BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey("goldcoin", 0), SharedPreferencesTools.getInstance(
							this, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey("silvercoin", 0)).showDialog();
				}
			}
			break;
		case HandlerValue.CHAT_DARE_SEND_RESULT:
			dareHintLayout.setVisibility(View.GONE);
			ChatOperate chatOperate = ChatOperate.getInstance(this, mFriendUid, isGroupChatValue);
			if (chatOperate != null)
				chatOperate.updateProgressByMesSvrID((String) msg.obj, 1);

			chatOperate = null;

			mChatListAdpater.setDareByBurnId(1, (String) msg.obj);
			break;
		case HandlerValue.CHAT_DARE_SEND_RESULT_CODE:
			dareHintLayout.setVisibility(View.GONE);
			if (msg.arg1 == -29000) {
				BaseUtils.showTost(this, "请勿重复参加同一局大冒险游戏");
			} else if (msg.arg1 == -29001) {
				BaseUtils.showTost(this, "上一局大冒险游戏还没结束，请稍后再试");
			} else if (msg.arg1 == -29003) {//大冒险结束
				ChatOperate chatO = ChatOperate.getInstance(this, mFriendUid, isGroupChatValue);
				if (chatO != null)
					chatO.updateProgressByMesSvrID(BAApplication.dareId, 5);

				chatO = null;

				mChatListAdpater.setDareByBurnId(5, BAApplication.dareId);
				BaseUtils.showTost(this, "参与人数已满，请积极参与下一轮");
			} else if (msg.arg1 == -29005) {//超时
				ChatOperate chatO = ChatOperate.getInstance(this, mFriendUid, isGroupChatValue);
				if (chatO != null)
					chatO.updateProgressByMesSvrID(BAApplication.dareId, 5);

				chatO = null;

				mChatListAdpater.setDareByBurnId(5, BAApplication.dareId);
				BaseUtils.showTost(this, "骰子已过期");
			} else {
				ChatOperate chatO = ChatOperate.getInstance(this, mFriendUid, isGroupChatValue);
				if (chatO != null)
					chatO.updateProgressByMesSvrID(BAApplication.dareId, 5);

				chatO = null;

				mChatListAdpater.setDareByBurnId(5, BAApplication.dareId);
				BaseUtils.showTost(this, "操作失败");
			}
			break;
		case HandlerValue.CHTA_DARE_PASS_RESULT_CODE:
			dareLayout.setVisibility(View.GONE);
			if (msg.arg1 == 0) {
				new HintToastDialog(this, R.string.dare_pass_content, R.string.ok).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_NOT_ENGOUH_WELTH) {//财富不够
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			}
			break;
		case HandlerValue.CHAT_DARE_INFO_ANIM:
			ImageView iv = (ImageView) msg.obj;
			iv.setBackgroundDrawable(null);
			switch (msg.arg1) {
			case 1:
				iv.setImageResource(R.drawable.message_img_dice1);
				break;
			case 2:
				iv.setImageResource(R.drawable.message_img_dice2);
				break;
			case 3:
				iv.setImageResource(R.drawable.message_img_dice3);
				break;
			case 4:
				iv.setImageResource(R.drawable.message_img_dice4);
				break;
			case 5:
				iv.setImageResource(R.drawable.message_img_dice5);
				break;
			case 6:
				iv.setImageResource(R.drawable.message_img_dice6);
				break;
			default:
				break;
			}
			break;
		case HandlerValue.CHAT_DARE_DIALOG_SHOW:
			if (dareDialog != null) {
				dareDialog.dismiss();
			}
			break;
		case HandlerValue.CHAT_TRUTH_SEND_SUCCESS_RESULT_CODE://真心话成功

			break;
		case HandlerValue.CHAT_TRUTH_SEND_ERROR_RESULT_CODE://真心话错误
			switch (msg.arg1) {
			case -30000:////发起真心话超过最大的次数
				BaseUtils.showTost(ChatActivity.this, R.string.truth_reply);
				break;

			default:
				BaseUtils.showTost(this, R.string.truth_error);
				break;
			}

			break;
		case HandlerValue.CHAT_TRUTH_ANSWER_SUCCESS_RESULT_CODE://真心话选择答案成功
			ChatOperate chatOperateTruth = ChatOperate.getInstance(this, mFriendUid, isGroupChatValue);
			if (chatOperateTruth != null)
				chatOperateTruth.updateProgressByMesSvrID((String) msg.obj, msg.arg1);

			mChatListAdpater.setTruthByBurnid(msg.arg1, (String) msg.obj);//刷新界面
			break;
		case HandlerValue.CHAT_TRUTH_ANSWER_ERROR_RESULT_CODE://真心话选择答案错误
			switch (msg.arg1) {
			case -30001://大冒险过期
				//				BaseUtils.showTost(this, "大冒险已过期");
				//大冒险过期，走基本的消息类型
				String obj = msg.obj.toString();
				String truthid = obj.substring(0, obj.lastIndexOf(","));
				String answer = obj.substring(obj.lastIndexOf(",") + 1, obj.length());

				//更新状态
				ChatOperate chatTruth = ChatOperate.getInstance(this, mFriendUid, isGroupChatValue);
				if (chatTruth != null)
					chatTruth.updateProgressByMesSvrID(truthid, 1);

				mChatListAdpater.setTruthByBurnid(1, truthid);//刷新界面

				if (!TextUtils.isEmpty(answer)) {
					sendTextMessage(answer, null, false);
				}
				break;

			default:
				BaseUtils.showTost(this, R.string.truth_error);
				break;
			}
			break;
		case HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_SUCCESS:
			if (msg.arg1 == 0) {
				SkillTextInfoList list = (SkillTextInfoList) msg.obj;
				if (!ListUtils.isEmpty(list)) {
					skillIv.setVisibility(View.VISIBLE);
					skillAdapter.clearList();
					skillAdapter.appendToList(skillAdapter.getGoddessSkillListData(list));
				} else {
					skillIv.setVisibility(View.GONE);
				}
			} else {
				skillIv.setVisibility(View.GONE);
				BaseUtils.showTost(this, R.string.str_get_skill_fail);
			}
			break;
		case HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_ERROR:
			skillIv.setVisibility(View.GONE);
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.GET_GODDESS_SKILL_INVITE_RES_SUCCESS:
			if (msg.arg1 == 0) {
				SkillResultBean bean = (SkillResultBean) msg.obj;
				if (bean != null) {
					ChatOperate chatSkill = ChatOperate.getInstance(this, mFriendUid, isGroupChatValue);
					if (chatSkill != null) {
						chatSkill.updateProgressByMesSvrID(bean.getCreatetime() + "", bean.getInvitedstatus());
					}
					mChatListAdpater.setSkillByBurnid(bean.getInvitedstatus(), String.valueOf(bean.getCreatetime()));
				}
			} else if (msg.arg1 == -28010) {
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE27);
				EventBus.getDefault().post(event);
			} else {
				ChatOperate skillOperate = ChatOperate.getInstance(this, mFriendUid, isGroupChatValue);
				if (skillOperate != null) {
					skillOperate.updateProgressByMesSvrID(String.valueOf(createtime), 3);
					mChatListAdpater.setSkillByBurnid(3, String.valueOf(createtime));
				}
				BaseUtils.showTost(this, R.string.str_the_invite_has_fail);
			}
			break;
		case HandlerValue.GET_GODDESS_SKILL_INVITE_RES_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.UPDATE_GODDESS_SKILL_INVITE_TIME:
			ChatDatabaseEntity dbEntity = (ChatDatabaseEntity) msg.obj;
			int progress = msg.arg1;
			if (dbEntity != null) {
				ChatOperate skillOperate = ChatOperate.getInstance(this, dbEntity.getFromID(), isGroupChatValue);
				if (skillOperate != null) {
					if (dbEntity != null) {
						ChatMessageEntity chatEntity = ChatMessageBiz.decodeMessage(dbEntity.getMessage());
						SaveChatData.updateGoddessSkillStatus(this, dbEntity, progress, isGroupChatValue);
						mChatListAdpater.setSkillInviteTimeByBurnid(chatEntity.getSkilllistid());
					}
				}
			}
			break;
		case HandlerValue.HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS:
			ChatMessageEntity checkEntity = (ChatMessageEntity) msg.obj;
			checkSolitaireRedpacketStatus(checkEntity);
			break;
		case HandlerValue.HAREM_GRAB_SOLITAIRE_REDPACKET_ENSURE:
			RedPacketBetCreateInfo grapSolitaireinfo = (RedPacketBetCreateInfo) msg.obj;
			grapSolitaireRedPacket(grapSolitaireinfo);
			break;
		case HandlerValue.HAREM_GRAB_SOLITAIRE_REDPACKET_SUCCESS:
			RedPacketBetCreateInfo solitaireinfo = (RedPacketBetCreateInfo) msg.obj;
			if (msg.arg1 == 0) {
				if (solitaireinfo != null) {
					if (solitaireinfo.redpacketstatus.intValue() == 1) {
						showOpenSolitaireRedpacketSuccess(solitaireinfo);
						saveGrapSolitaireRedpacket(solitaireinfo);
						updateSolitaireRedpacketStatus(solitaireinfo);
					} else if (solitaireinfo.redpacketstatus.intValue() == 2) {
						BaseUtils.showTost(ChatActivity.this, R.string.str_the_redpacket_open_success_by_other);
						updateSolitaireRedpacketStatus(solitaireinfo);
					} else if (solitaireinfo.redpacketstatus.intValue() == 3) {
						showOpenSolitaireRedpacketFail(solitaireinfo);
						saveGrapSolitaireRedpacket(solitaireinfo);
					} else if (solitaireinfo.redpacketstatus.intValue() == 4) {
						BaseUtils.showTost(ChatActivity.this, R.string.str_the_redpacket_is_overdue);
						updateSolitaireRedpacketStatus(solitaireinfo);
					}
				}
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_CANNOT_PARTICIPATE_REDPACKETBET) {
				if (solitaireinfo.redpacketstatus.intValue() == 1) {
					showOpenSolitaireRedpacketSuccess(solitaireinfo);
					updateSolitaireRedpacketStatus(solitaireinfo);
				} else if (solitaireinfo.redpacketstatus.intValue() == 2) {
					BaseUtils.showTost(ChatActivity.this, R.string.str_the_redpacket_open_success_by_other);
					updateSolitaireRedpacketStatus(solitaireinfo);
				} else if (solitaireinfo.redpacketstatus.intValue() == 3) {
					showOpenSolitaireRedpacketFail(solitaireinfo);
					saveGrapSolitaireRedpacket(solitaireinfo);
				} else if (solitaireinfo.redpacketstatus.intValue() == 4) {
					BaseUtils.showTost(ChatActivity.this, R.string.str_the_redpacket_is_overdue);
					updateSolitaireRedpacketStatus(solitaireinfo);
				}
			} else if (msg.arg1 == rspContMsgType.E_GG_PROPERTY_LACK) {//财富不足
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够银币
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			}
			break;
		case HandlerValue.HAREM_GRAB_SOLITAIRE_REDPACKET_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS_SUCCESS:
			RedPacketBetCreateInfo checkRedpacketInfo = (RedPacketBetCreateInfo) msg.obj;
			if (msg.arg1 == 0) {
				if (checkRedpacketInfo != null) {
					if (checkRedpacketInfo.redpacketstatus.intValue() == 0) {
						new SolitaireRedPacketInfoDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mFriendUid, checkRedpacketInfo, mHandler)
								.showDialog();
					} else if (checkRedpacketInfo.redpacketstatus.intValue() == 1) {
						BaseUtils.showTost(ChatActivity.this, R.string.str_the_redpacket_open_success_by_you);
						updateSolitaireRedpacketStatus(checkRedpacketInfo);
					} else if (checkRedpacketInfo.redpacketstatus.intValue() == 2) {
						BaseUtils.showTost(ChatActivity.this, R.string.str_the_redpacket_open_success_by_other);
						updateSolitaireRedpacketStatus(checkRedpacketInfo);
					} else if (checkRedpacketInfo.redpacketstatus.intValue() == 3) {
						new SolitaireRedPacketInfoDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mFriendUid, checkRedpacketInfo, mHandler)
								.showDialog();
					} else if (checkRedpacketInfo.redpacketstatus.intValue() == 4) {
						BaseUtils.showTost(ChatActivity.this, R.string.str_the_redpacket_is_overdue);
						updateSolitaireRedpacketStatus(checkRedpacketInfo);
					}
				}
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_DECODE) {
				BaseUtils.showTost(ChatActivity.this, R.string.str_system_error);
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_RED_TIMEOUT) {
				BaseUtils.showTost(ChatActivity.this, R.string.str_the_redpacket_is_overdue);
				if (checkRedpacketInfo != null) {
					updateSolitaireRedpacketStatus(checkRedpacketInfo);
				}
			} else if (msg.arg1 == rspContMsgType.E_GG_PROPERTY_LACK) {//财富不足
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够银币
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			}
			break;
		case HandlerValue.HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.HAREM_UPDATE_CAN_GRAB_REDPACKET:
			RedpacketOperate operate = RedpacketOperate.getInstance(this, mFriendUid);
			if (operate != null) {
				int num = operate.getCount();
				if (num > 0) {
					fl_redpacket.setVisibility(View.VISIBLE);
					tv_redpacket.setText(num + "");
				} else {
					fl_redpacket.setVisibility(View.GONE);
				}
			}
			break;
		case HandlerValue.HAREM_SOLITAIRE_REDPACKET_INFO_SUCCESS:
			if (0 == msg.arg1) {
				isOpenRedpacket = msg.arg2;
				RedPacketBetInfoList redList = (RedPacketBetInfoList) msg.obj;
				BAApplication.getInstance().redList = getSolitaireRedpacketListData(redList);
			} else {
				BaseUtils.showTost(this, "获取失败");
			}
			break;
		case HandlerValue.CHAT_ANONYM_NICK_PAST:
			ChatSessionManageBiz.removeChatSessionWithUserID(this, mFriendUid, 3);
			BaseUtils.showTost(this, "昵称已过期,你们不能继续聊天了。");
			break;
		default:
			break;
		}
	}

	private ArrayList<RedPacketBetInfo> getSolitaireRedpacketListData(List<RedPacketBetInfo> list) {//去除重复的数据
		ArrayList<RedPacketBetInfo> newLists = new ArrayList<RedPacketBetInfo>();
		if (list != null && !list.isEmpty()) {
			for (RedPacketBetInfo betInfo : list) {
				newLists.add(betInfo);
			}
		}
		return newLists;
	}

	private void updateSolitaireRedpacketStatus(RedPacketBetCreateInfo checkRedpacketInfo) {
		RedpacketOperate operate = RedpacketOperate.getInstance(this, mFriendUid);
		if (operate != null) {
			if (checkRedpacketInfo.redpacketstatus.intValue() == 1 || checkRedpacketInfo.redpacketstatus.intValue() == 2
					|| checkRedpacketInfo.redpacketstatus.intValue() == 4) {
				operate.delete(checkRedpacketInfo.id.intValue());
			} else {
				String message = ChatMessageBiz.saveSolitaireRedPacketInfo(this, checkRedpacketInfo);
				operate.updateMessageByMesSvrID(String.valueOf(checkRedpacketInfo.id.intValue()), message);
				operate.updateProgressByMesSvrID(String.valueOf(checkRedpacketInfo.id.intValue()), checkRedpacketInfo.redpacketstatus.intValue());
			}
			int num = operate.getCount();
			if (num > 0) {
				fl_redpacket.setVisibility(View.VISIBLE);
				tv_redpacket.setText(num + "");
			} else {
				fl_redpacket.setVisibility(View.GONE);
			}
		}

	}

	private void saveGrapSolitaireRedpacket(RedPacketBetCreateInfo solitaireinfo) {
		ChatDatabaseEntity entity = SendGroupChatRedPacketMessage.sendGrapSolitaireRedPacketMsg(this, solitaireinfo, mFriendUid, mFriendNick);
		if (entity != null) {
			long messageId = entity.getMesLocalID();
			if (messageId >= 0) {
				ChatDatabaseEntity grapEntity = ChatOperate.getInstance(this, mFriendUid, true).getMessageByLocalId(messageId);
				if (entity != null) {
					mChatListAdpater.appendToList(grapEntity);
					mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				}
			}
		}
	}

	private void showOpenSolitaireRedpacketFail(RedPacketBetCreateInfo solitaireinfo) {
		if (solitaireinfo == null)
			return;
		new SolitaireGrapRedPacketFailDialog(this, android.R.style.Theme_Translucent_NoTitleBar, solitaireinfo).showDialog();

	}

	private void showOpenSolitaireRedpacketSuccess(RedPacketBetCreateInfo solitaireinfo) {
		if (solitaireinfo == null)
			return;

		new SolitaireGrapRedPacketSuccessDialog(this, android.R.style.Theme_Translucent_NoTitleBar, solitaireinfo).showDialog();

	}

	private void grapSolitaireRedPacket(RedPacketBetCreateInfo grapSolitaireinfo) {
		if (grapSolitaireinfo == null || grapSolitaireinfo.userInfo == null)
			return;

		solitaireRedPacketBiz.requestGrapSolitaireRedPacket(grapSolitaireinfo.userInfo.uid.intValue(), grapSolitaireinfo.id.intValue(), 0, this);
	}

	private void getRedPacketMoneyInfo() {
		solitaireRedPacketBiz.reqeustSolitaireRedPacketMoney(0, this);
	}

	private void checkSolitaireRedpacketStatus(ChatMessageEntity grapEntity) {
		if (grapEntity == null)
			return;

		solitaireRedPacketBiz.requestCheckRekpackeState(Integer.parseInt(grapEntity.getId()), 0, this);
	}

	private boolean isLoster(ChatMessageEntity entity) {
		if (Integer.parseInt(entity.getDareuid1()) == BAApplication.mLocalUserInfo.uid.intValue()) {
			return true;
		} else if (!TextUtils.isEmpty(entity.getDareuid2()) && Integer.parseInt(entity.getDareuid2()) == BAApplication.mLocalUserInfo.uid.intValue()) {
			return true;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancelAll();

		initDefaul();

		if (unReadNum > 0) {
			tvBack.setText(tvBack.getText() + "(" + unReadNum + ")");
		}

		initUI();
		initListener();

		mHandler.sendEmptyMessage(INIT_FLAG);
	}

	@Override
	public void onResume() {
		super.onResume();

		IntentFilter filter = new IntentFilter();
		filter.addAction(RecorderService.RECORDER_SERVICE_BROADCAST_NAME);
		registerReceiver(mReceiver, filter);
		registerExternalStorageListener();

		mStopUiUpdate = false;

		GoGirlUserInfo userInfo = UserUtils.getUserEntity(this);
		if (userInfo != null) {
			SharedPreferencesTools.getInstance(this, userInfo.uid.intValue() + "").saveIntKeyValue(0, BAConstants.PEIPEI_NOTIFICATION_CHAT_NUM);

			ChatManageBiz.getInManage(this).setPersistListener(this, mFriendUid, isGroupChatValue);
			mChatListView.setOnRefreshListener(new ChatRefreshListener(this, mChatListAdpater, ChatManageBiz.getInManage(this), mFriendUid,
					isGroupChatValue, mChatListView, mHandler));
			if (from != RewardListActivity.CHAT_FROM_REWARD && !isGroupChatValue) {
				getPersonSkillInfo();
			}
			RelationOperate relationOperate = RelationOperate.getInstance(this);
			if (relationOperate.isHaveSession(mFriendUid)) {
				RelationEntity relationEntity = relationOperate.selectSessionDate(mFriendUid);
				if (relationEntity.getRelationship() < 100) {
					getRelationship(userInfo);
				} else {
					setLoyalty(0, 0);
				}
			} else {
				RelationEntity relationEntity = new RelationEntity();
				relationEntity.setChatthreshold(0);
				relationEntity.setIsUpdate(0);
				relationEntity.setRelationship(0);
				relationEntity.setRelationshipTime(System.currentTimeMillis());
				relationEntity.setToUid(mFriendUid);
				relationOperate.insert(relationEntity);
				getRelationship(userInfo);
			}
		}
		if (from == RewardListActivity.CHAT_FROM_REWARD) {
			emptyChatTv.setText("匿名悬赏聊天记录将于12小时后自动销毁。");
			emptyChatTv.setVisibility(View.VISIBLE);
			emptyChatTv.setGravity(Gravity.CENTER);
			emptyChatTv.setTextSize(13);
		}
	}

	@Override
	protected void onPause() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}

		mStopUiUpdate = true;

		if (RecorderService.isRecording()) {
			Intent intent = new Intent(this, RecorderService.class);
			intent.putExtra(RecorderService.ACTION_NAME, RecorderService.ACTION_ENABLE_MONITOR_REMAIN_TIME);
			startService(intent);
		}

		super.onPause();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 != RESULT_OK) {
			return;
		}

		switch (arg0) {
		case PhotoSetDialog.GET_PHOTO_BY_CAMERA:
			String path = BaseFile.getTempFile().getAbsolutePath();
			sendChatImage(null, path, false);
			//			if (mChatSentLayoutByBurn.getVisibility() == View.VISIBLE) {
			//				mBottomGridView.setVisibility(View.GONE);
			//				mChatSentLayout.setVisibility(View.GONE);
			//				mChatSentLayoutByBurn.setVisibility(View.VISIBLE);
			//			}
			break;
		case PhotoSetDialog.GET_PHOTO_BY_GALLERY:
			if (arg2 != null) {
				Uri uri = arg2.getData(); // 读取相册图片
				if (uri != null) {
					String path1 = BaseFile.getFilePathFromContentUri(uri, getContentResolver());
					sendChatImage(null, path1, false);
				}
			}
			//			if (mChatSentLayoutByBurn.getVisibility() == View.VISIBLE) {
			//				mBottomGridView.setVisibility(View.GONE);
			//				mChatSentLayout.setVisibility(View.GONE);
			//				mChatSentLayoutByBurn.setVisibility(View.VISIBLE);
			//			}
			break;
		case GET_VIDEO_SELECT:
			if (arg2 != null) {
				int selectValue = arg2.getIntExtra(SelectVedioActivity.VIDEO_SELECT_VALUE, -1);
				if (selectValue == SelectVedioActivity.VIDEO_CAPTURE || selectValue == SelectVedioActivity.VIDEO_ALL_SELECT) {
					Uri captureuri = arg2.getParcelableExtra(SelectVedioActivity.VIDEO_SELECT_URI);
					operateVideoThread(captureuri);
				} else if (selectValue == SelectVedioActivity.VIDEO_MY_SELECT) {
					long size = arg2.getLongExtra(SelectVedioActivity.VIDEO_SELECT_FILE_SIZE, 0);
					String strSize = VideoUtils.getDataSize(size);
					String videoPath = arg2.getStringExtra("compressGoodVideoPath");
					strSize += "," + videoPath;//拼接大小和路径
					mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_COMPRESSION_SUCCESS_VALUE, strSize));
				}
			}
			break;
		case GET_RETURN_SEND_REDPACKET://发送红包返回数据
			long localId = arg2.getLongExtra(SendRedPacketActivity.STR_RETURN_MESSAGE_ID, -1);
			if (localId >= 0) {
				ChatDatabaseEntity entity = ChatOperate.getInstance(this, mFriendUid, true).getMessageByLocalId(localId);
				if (entity != null) {
					mChatListAdpater.appendToList(entity);
					mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				}
			}
			break;
		case GET_RETURN_SEND_SOLITAIRE_REDPACKET: //发送红包接龙返回数据
			long localSolitaireId = arg2.getLongExtra(SendRedPacketActivity.STR_RETURN_MESSAGE_ID, -1);
			if (localSolitaireId >= 0) {
				ChatDatabaseEntity entity = ChatOperate.getInstance(this, mFriendUid, true).getMessageByLocalId(localSolitaireId);
				if (entity != null) {
					mChatListAdpater.appendToList(entity);
					mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				}
			}
			break;

		default:
			if (mTencent != null)
				mTencent.onActivityResult(arg0, arg1, arg2);
			break;
		}

	}

	private void sendChatImage(final ChatDatabaseEntity chatEntity, String path, final boolean isResend) {//私聊发送图片
		if (!TextUtils.isEmpty(path)) {
			try {
				if (isGroupChatValue) {
					if (!isResend) {
						GroupChatUtils.sendGroupChatImage(this, mFriendUid, mFriendNick, path, mHandler, isResend, new ChatDatabaseEntity());
					} else {
						GroupChatUtils.sendGroupChatImage(this, mFriendUid, mFriendNick,
								BaseString.getFilePath(this, BAApplication.mLocalUserInfo.uid.intValue(), MessageType.IMAGE.getValue())
										+ File.separator + chatEntity.getMesLocalID(), mHandler, isResend, chatEntity);
					}
				} else {

					DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).considerExifParams(true)
							.cacheInMemory(false).cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565).build();

					ImageSize imageSize = new ImageSize(BasePhone.getScreenWidth(this), BasePhone.getScreenHeight(this));

					imageLoader.loadImage("file://" + path, imageSize, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							if (loadedImage != null) {
								byte[] bitmapBytes = ImageUtils.bitmapToByte(loadedImage);
								int type = MessageType.IMAGE.getValue();

								if (mIsBurn) {
									if (from == RewardListActivity.CHAT_FROM_REWARD) {
										type = MessageType.GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_PIC.getValue();
									} else
										type = MessageType.BURN_IMAGE.getValue();
								} else {
									if (from == RewardListActivity.CHAT_FROM_REWARD) {
										type = MessageType.GOGIRL_DATA_TYPE_ANONYM_PIC.getValue();
									} else
										type = MessageType.IMAGE.getValue();
								}
								SendPrivateChatImageMessage.getInstance().sendPrivateChatImageMessage(ChatActivity.this, bitmapBytes, type,
										mFriendUid, mFriendNick, mFriendSex, mHandler, chatEntity, isResend, from);
							}
						}

						@Override
						public void onLoadingCancelled(String imageUri, View view) {

						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			BaseUtils.showTost(this, R.string.msg_rechoice_gallery);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (mVoiceRecod != null && !mVoiceRecod.isShow()) {
			mVoiceRecod.stop();
		}
		setAudiIbtnBgStopAudioAmin();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mSDCardMountEventReceiver != null) {
			unregisterReceiver(mSDCardMountEventReceiver);
			mSDCardMountEventReceiver = null;
		}
		if (BAApplication.mLocalUserInfo != null) {
			ChatManageBiz.getInManage(this).setPersistListener(null, 0, isGroupChatValue);
		}
		mChatListView.setOnScrollListener(null);
		if (mVoiceRecod != null && !mVoiceRecod.isShow()) {
			mVoiceRecod.stop();
		}
		mChatEditText.getText().clear();
	}

	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.message_title_back_tv:
			this.finish();
			break;
		case R.id.ibtn_chat_keyboard_voice:
			clickIbtnChatKeyboardVoice();
			break;
		case R.id.ibtn_chat_plus:
			String str = mChatEditText.getText().toString().trim();
			if (!TextUtils.isEmpty(isAbout) && str.equals(isAbout.trim())) {
				str = str + " ";
			}
			if (TextUtils.isEmpty(str)) {
				clickIbtnChatPlus();
			} else {
				sendTextMessage(str, null, false);
				mChatEditText.setText("");
			}
			break;
		case R.id.ibtn_chat_emotion:
			if (!isInflateEmotion) {
				View emojiView = vs_emotion.inflate();
				ll_emoji = (LinearLayout) emojiView.findViewById(R.id.ll_emotion);
				mEmojiPager = (ViewPager) emojiView.findViewById(R.id.emoji_viewpager);
				pageControlView = (PageControlView) emojiView.findViewById(R.id.pageControlView);
				iv_common_face = (ImageView) emojiView.findViewById(R.id.iv_common_emotion);
				iv_common_face.setOnClickListener(this);
				iv_emoji_face = (ImageView) emojiView.findViewById(R.id.iv_emoji_emotion);
				iv_emoji_face.setOnClickListener(this);
				iv_harem_face = (ImageView) emojiView.findViewById(R.id.iv_harem_emotion);
				iv_harem_face.setOnClickListener(this);
				isInflateEmotion = true;
			}

			mBottomGridView.setVisibility(View.GONE);
			BaseUtils.hideKeyboard(this, mChatEditText);
			if (ll_emoji != null) {

				iv_common_face.setBackgroundColor(getResources().getColor(R.color.upload));
				iv_emoji_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				iv_harem_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				if (ll_emoji.isShown()) {
					ll_emoji.setVisibility(View.GONE);
				} else {
					new EmotionViewAdd(this, mChatEditText, pageControlView, mEmojiPager, iv_common_face, iv_emoji_face, iv_harem_face, this);
					try {
						mChatListView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					} catch (Exception e) {//魅族手机会报错，只能够这样捕获了
						e.printStackTrace();
					}
					ll_emoji.setVisibility(View.VISIBLE);

				}
			}
			break;
		case R.id.iv_harem_emotion:
			if (mEmojiPager != null) {
				iv_common_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				iv_emoji_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				iv_harem_face.setBackgroundColor(getResources().getColor(R.color.upload));
				mEmojiPager.setCurrentItem(7);
			}
			break;
		case R.id.iv_emoji_emotion:
			if (mEmojiPager != null) {
				iv_common_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				iv_emoji_face.setBackgroundColor(getResources().getColor(R.color.upload));
				iv_harem_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				mEmojiPager.setCurrentItem(2);
			}
			break;
		case R.id.iv_common_emotion:
			if (mEmojiPager != null) {
				iv_common_face.setBackgroundColor(getResources().getColor(R.color.upload));
				iv_emoji_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				iv_harem_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				mEmojiPager.setCurrentItem(0);
			}
			break;
		case R.id.iv_chat_cancel:
			clickIvCancel2Burn();
			break;
		case R.id.iv_chat_burn_image:
			new PhotoSetDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog(0, 0);
			break;
		case R.id.chat_btn_close:
			showTranslate();
			break;
		case R.id.message_title_more:
			if (isGroupChatValue) {
				CreateHarem.getInstance().getGroupInfo(this, mFriendUid, mHandler);
			} else {
				//				new ChatMoreDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mFriendUid, mFriendNick, mHandler, false, null,1).showDialog(0,
				//						0);
				new ChatMoreDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mFriendUid, mFriendNick, mHandler, false, null, from)
						.showDialog(0, 0);
			}
			break;
		case R.id.message_title_present_iv:
			gotoStoreGiftListActivity();
			break;
		case R.id.message_title_skill_iv:
			showSkillWindow();
			break;
		case R.id.chat_gift_icon:
			gotoStoreGiftListActivity();
			break;
		case R.id.btn_chat_loyalty:
			String name = "红玫瑰x" + giftNum;
			String charmeffect = "2x" + giftNum;
			String loyaltyeffect = "2x" + giftNum;
			String giftkey = "gg_gift_1_pic";
			String giftsilver = "100x" + giftNum;
			if (giftInfo != null) {
				name = new String(giftInfo.name);
				charmeffect = giftInfo.charmeffect.intValue() + "";
				loyaltyeffect = giftInfo.loyaltyeffect.intValue() + "";
				giftkey = new String(giftInfo.pickey);
				giftsilver = giftInfo.pricesilver.intValue() + "";
			}
			new SendGiftByChatDialog(this, SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "")
					.getIntValueByKey("goldcoin", 0), SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "")
					.getIntValueByKey("silvercoin", 0), mFriendNick, name, loyaltyeffect, charmeffect, giftkey, giftsilver, mHandler).showDialog();
			break;
		case R.id.message_title_tv_mid:
			if (!isGroupChatValue && from != RewardListActivity.CHAT_FROM_REWARD) {
				if (mFriendUid != 50002) {
					SpaceUtils.toSpaceCustom(this, mFriendUid, mFriendSex);
				}
			}
			break;
		case R.id.image_dare_pass:
			new DarePassDialog(this, R.string.dare_pass_title, R.string.ok, R.string.cancel, mFriendUid, currDareId, mHandler).showDialog();
			break;
		case R.id.image_dare_egg:
			sendFlowers(2);
			dareEgg.setVisibility(View.GONE);
			break;
		case R.id.image_dare_flower:
			sendFlowers(1);
			dareFlower.setVisibility(View.GONE);
			break;
		case R.id.image_dare_brick:
			sendFlowers(3);
			dareBrick.setVisibility(View.GONE);
			break;
		case R.id.iv_chat_dice_image:
			if (!TextUtils.isEmpty(currDareId)) {
				PlayDareBiz playB = new PlayDareBiz();
				playB.PlayDare(mFriendUid, currDareId, mHandler);
				dareHintLayout.setVisibility(View.GONE);
				currDareId = "";
			}
			break;
		case R.id.chat_redpacket_icon:
			new CanGrapSolitaireRedpacketDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mFriendUid, mHandler).showDialog();
			break;
		case R.id.chat_group_action_redpacket_iv://红包接龙
			Bundle bundle = new Bundle();
			bundle.putInt(SendSolitaireRedPacketActivity.STR_GROUP_ID, mFriendUid);
			bundle.putString(SendSolitaireRedPacketActivity.STR_GROUP_NAME, mFriendNick);
			BaseUtils.openResultActivity(ChatActivity.this, SendSolitaireRedPacketActivity.class, bundle, GET_RETURN_SEND_SOLITAIRE_REDPACKET);
			break;
		case R.id.chat_group_action_finger_iv://猜拳
			new PlayFingerDialog(ChatActivity.this, mFriendSex, isGroupChatValue, mFriendUid, mFriendNick, mHandler).showDialog();
			break;
		case R.id.chat_grop_action_crazy_iv://大冒险
			PlayDareBiz playB = new PlayDareBiz();
			playB.PlayDare(mFriendUid, "", mHandler);
			break;
		case R.id.chat_about_close_icon:
			aboutLayout.setVisibility(View.INVISIBLE);
			SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(false, "@" + BAApplication.mLocalUserInfo.uid + "_" + mFriendUid);
			break;
		case R.id.chat_about_text://有人@我
			//TODO
			List<ChatDatabaseEntity> arrayList = mChatListAdpater.getList();
			int position = -1;
			for (int i = arrayList.size() - 1; i >= 0; i--) {
				if (!TextUtils.isEmpty(arrayList.get(i).getMessage())
						&& arrayList.get(i).getMessage().contains("@" + new String(BAApplication.mLocalUserInfo.nick))) {
					position = i;
					break;
				}
			}
			if (position == -1) {
				isLoadMore = true;
				mChatListView.setRefreshing();
				aboutLayout.setVisibility(View.INVISIBLE);
			} else {
				aboutLayout.setVisibility(View.INVISIBLE);
				if (position != 0) {
					position = position + 1;
				}
				mChatListView.getRefreshableView().setSelection(position);
				//				mChatListAdpater.notifyDataSetChanged();
			}
			SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(false, "@" + BAApplication.mLocalUserInfo.uid + "_" + mFriendUid);
			break;
		default:
			break;
		}
	}

	private void sendFlowers(int gifttype) {
		if (BAApplication.mLocalUserInfo != null) {
			RequestDareSendFlowers req = new RequestDareSendFlowers();
			req.dareSendFlowers(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
					mFriendUid, gifttype, currDareId, this);
		}
	}

	private void sendTextMessage(String message, ChatDatabaseEntity chatDatabaseEntity, boolean isResend) {//发送纯文本消息
		message = EmojiFaceConversionUtil.convertUnicode2(message);
		//TODO
		if (isGroupChatValue) {
			SendGroupChatTextMessage.getInstance().sendTextGroupMsg(this, message.getBytes(), mFriendUid, mFriendNick, mHandler, chatDatabaseEntity,
					isResend);
		} else {
			SendPrivateChatTextMessage.getInstance().sendPrivateChatTextMessage(this, message, mFriendUid, mFriendNick, mFriendSex, mHandler,
					chatDatabaseEntity, isResend, from);
		}
	}

	private void gotoStoreGiftListActivity() {
		Bundle b1 = new Bundle();
		b1.putInt("fuid", mFriendUid);
		b1.putString("fNick", mFriendNick);
		b1.putInt("fSex", mFriendSex);
		long lastItemTime = 0;
		if (mChatListAdpater.getList().size() > 0) {
			lastItemTime = ((ChatDatabaseEntity) mChatListAdpater.getItem(mChatListAdpater.getCount() - 1)).getCreateTime();
		}
		b1.putLong("time", lastItemTime);
		b1.putInt("from", from);
		BaseUtils.openActivity(this, StoreGiftListActivity.class, b1);
	}

	//******************************************私有方法*************************
	private void initUI() {
		mChatTextLayout = (LinearLayout) findViewById(R.id.ll_chat_text);
		mTitle = (TextView) findViewById(R.id.message_title_tv_mid);
		if (mFriendUid == BAConstants.PEIPEI_XIAOPEI) {
			mTitle.setText(R.string.str_system_user);
		} else if (mFriendUid == BAConstants.PEIPEI_CHAT_TONGZHI) {
			mTitle.setText(R.string.sys_toggle);
		} else if (mFriendUid == BAConstants.PEIPEI_CHAT_XIAOPEI) {
			mTitle.setText(R.string.xiaopei);
			mTitle.setOnClickListener(this);
		} else {
			String alias = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(mFriendUid);
			mTitle.setText(TextUtils.isEmpty(alias) ? mFriendNick : alias);
			mTitle.setOnClickListener(this);
		}

		mChatBgLayout = (RelativeLayout) findViewById(R.id.chat_bg);

		mChatSentLayout = (LinearLayout) findViewById(R.id.chat_sent_ll);
		mChatSentLayoutByBurn = (LinearLayout) findViewById(R.id.chat_sent_ll_burn);

		if (mFriendUid == BAConstants.PEIPEI_XIAOPEI || mFriendUid == BAConstants.PEIPEI_CHAT_TONGZHI) {
			mChatSentLayout.setVisibility(View.GONE);
		}

		mChatLoyaltyText = (TextView) findViewById(R.id.tv_chat_loyalty);
		mChatLoyaltyLayout = (LinearLayout) findViewById(R.id.tv_chat_loyalty_ll);
		findViewById(R.id.btn_chat_loyalty).setOnClickListener(this);

		mChatBurnView = (BurnPicView) findViewById(R.id.chat_image_full);

		mChatHintLayout = (RelativeLayout) findViewById(R.id.chat_hint_rl);
		findViewById(R.id.chat_btn_close).setOnClickListener(this);
		mChatVoiceText = (TextView) findViewById(R.id.tv_chat_voice);
		mChatVoiceTextByBurn = (TextView) findViewById(R.id.tv_chat_voice1);

		mChatKeyboardVoice = (ImageButton) findViewById(R.id.ibtn_chat_keyboard_voice);
		mChatPuls = (ImageButton) findViewById(R.id.ibtn_chat_plus);

		mChatEditText = (EditText) findViewById(R.id.et_chat_text);
		//TODO
		mChatEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.isEmpty(s)) {
					mChatPuls.setImageResource(R.drawable.message_icon_add_selector);
					aboutTextLenght = -1;
					oldTextLenght = 0;
					isAbout = "";
					toUserName = "";
				} else {
					mChatPuls.setImageResource(R.drawable.message_btn_send1_selector);

					if (TextUtils.isEmpty(isAbout)) {
						return;
					}
					String str = ParseMsgUtil.convertUnicode2(s.toString());
					int strLen = str.length();
					SpannableString spannableString = new SpannableString(str);
					String zhengze = "<[p][+][0-9].[0-9][0-9]>";
					// 通过传入的正则表达式来生成一个pattern
					Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);

					Matcher matcher = sinaPatten.matcher(spannableString);
					while (matcher.find()) {//普通表情长度计算
						strLen = strLen - 6;
					}
					int len = 0;
					if (str.startsWith(isAbout)) {//以@开头
						len = strLen - isAbout.length();
						oldTextLenght = str.length();
					} else {
						try {
							String msg = "";
							if (!str.startsWith("@") && str.contains(isAbout)) {
								if (oldTextLenght == 0) {
									oldTextLenght = isAbout.length();
								}
								String a = str.substring(0, str.length() - oldTextLenght);
								String b = str.substring(a.length());
								msg = b + a;
								String unicode = EmojiParser.getInstance(ChatActivity.this).parseEmoji(msg);
								SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(ChatActivity.this, unicode,
										HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
								mChatEditText.setText(builder);
								oldTextLenght = str.length();
							} else {
								mChatEditText.getText().delete(0, isAbout.length() - 1);
								isAbout = "";
							}
							toUserName = "";
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				Log.w("Aaron", "beforeTextChanged strat==" + start + ", after==" + after + ", count=" + count);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		mChatBurnImage = (ImageButton) findViewById(R.id.iv_chat_burn_image);
		mChatCancel = (ImageButton) findViewById(R.id.iv_chat_cancel);
		ibtn_emotion = (ImageButton) findViewById(R.id.ibtn_chat_emotion);
		ibtn_emotion.setOnClickListener(this);

		mMoreLayout = (ImageView) findViewById(R.id.message_title_more);
		mChatListView = (PullToRefreshListView) findViewById(R.id.lv_chat);

		GoGirlUserInfo userInfo = UserUtils.getUserEntity(this);

		if (userInfo != null) {
			mChatListAdpater = new ChatAdapter(this, mFriendUid, new String(userInfo.headpickey), mFriendSex, new String(userInfo.nick), mFriendNick,
					isGroupChatValue, mHandler, this, this, this, this, this, this);
		}
		mSwipAdapter = new SwipeDismissAdapter(mChatListAdpater, this);
		mSwipAdapter.setAbsListView(mChatListView.getRefreshableView());
		mChatListView.setAdapter(mSwipAdapter);
		//		findViewById(R.id.message_title_present_iv).setOnClickListener(this);
		skillIv = (ImageView) findViewById(R.id.message_title_skill_iv);
		skillIv.setOnClickListener(this);
		giftIv = (ImageView) findViewById(R.id.chat_gift_icon);
		giftIv.setOnClickListener(this);

		if (isGroupChatValue || mFriendUid == BAConstants.PEIPEI_XIAOPEI || mFriendUid == BAConstants.PEIPEI_CHAT_TONGZHI) {
			findViewById(R.id.message_title_present_iv).setVisibility(View.GONE);
			giftIv.setVisibility(View.GONE);
			skillIv.setVisibility(View.GONE);
		}

		mEmpty = (TextView) findViewById(R.id.tv_chat_empyt);

		vs_emotion = (ViewStub) findViewById(R.id.viewstub_chat_emotion);
		vs_record = (ViewStub) findViewById(R.id.viewstub_chat_record);

		dareLayout = (LinearLayout) findViewById(R.id.layout_dare_gift);
		dareFlower = (ImageView) findViewById(R.id.image_dare_flower);
		dareEgg = (ImageView) findViewById(R.id.image_dare_egg);
		dareBrick = (ImageView) findViewById(R.id.image_dare_brick);
		darePass = (ImageView) findViewById(R.id.image_dare_pass);

		emptyChatTv = (TextView) findViewById(R.id.chat_frist_hint_tv);

		gropActionLayout = (LinearLayout) findViewById(R.id.chat_grop_action_layout);
		redpacketAction = (ImageView) findViewById(R.id.chat_group_action_redpacket_iv);
		fingerAction = (ImageView) findViewById(R.id.chat_group_action_finger_iv);
		crazyAction = (ImageView) findViewById(R.id.chat_grop_action_crazy_iv);

		aboutLayout = (LinearLayout) findViewById(R.id.chat_about_layout);
		aboutText = (TextView) findViewById(R.id.chat_about_text);
		aboutCloseIv = (ImageView) findViewById(R.id.chat_about_close_icon);
		groupBackgound = (RelativeLayout) findViewById(R.id.chat_group_bg);

		dareFlower.setOnClickListener(this);
		dareEgg.setOnClickListener(this);
		dareBrick.setOnClickListener(this);
		darePass.setOnClickListener(this);

		dareHintLayout = (LinearLayout) findViewById(R.id.dare_hint_layout);
		ivDareSend = (ImageView) findViewById(R.id.iv_chat_dice_image);
		ivDareSend.setOnClickListener(this);

		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		skillAdapter = new ChatSkillAdapter(this);
		initPopupWindow();

		tv_redpacket = (TextView) findViewById(R.id.tv_redpacket);
		fl_redpacket = findViewById(R.id.fl_redpacket);
		chat_redpacket_icon = (ImageView) findViewById(R.id.chat_redpacket_icon);
		chat_redpacket_icon.setOnClickListener(this);

		if (isGroupChatValue) {
			gropActionLayout.setVisibility(View.VISIBLE);

			//			getGroupInfo();
			try {
				boolean isShowAbout = SharedPreferencesTools.getInstance(this).getBooleanKeyValue(
						"@" + BAApplication.mLocalUserInfo.uid + "_" + mFriendUid);
				if (isShowAbout) {
					aboutLayout.setVisibility(View.VISIBLE);
				} else {
					aboutLayout.setVisibility(View.INVISIBLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			gropActionLayout.setVisibility(View.GONE);
			aboutLayout.setVisibility(View.INVISIBLE);
		}
	}

	private void initListener() {
		mChatVoiceText.setOnClickListener(this);
		mChatVoiceText.setOnTouchListener(this);
		mChatVoiceTextByBurn.setOnClickListener(this);
		mChatVoiceTextByBurn.setOnTouchListener(this);
		mChatEditText.setOnTouchListener(this);

		mChatKeyboardVoice.setOnClickListener(this);
		mChatPuls.setOnClickListener(this);
		mChatBurnImage.setOnClickListener(this);
		mChatCancel.setOnClickListener(this);

		mMoreLayout.setOnClickListener(this);
		mVoiceRecod.setOnStateChangedListener(this);
		mChatListView.setOnTouchListener(this);
		mChatListView.setOnScrollListener(this);
		mChatListView.setChatListener(this);

		redpacketAction.setOnClickListener(this);
		fingerAction.setOnClickListener(this);
		crazyAction.setOnClickListener(this);
		aboutCloseIv.setOnClickListener(this);
		aboutText.setOnClickListener(this);
	}

	private void initDefaul() {
		if (!ChatVoiceBiz.initInternalState(this, getIntent(), mRequestedType)) {
			setResult(RESULT_CANCELED);
			finish();
		}
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			mFriendUid = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, -1);
			mFriendNick = bundle.getString(BAConstants.IntentType.MAINHALLFRAGMENT_USERNICK);
			mFriendSex = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, Gender.MALE.getValue());
			isGroupChatValue = bundle.getBoolean(BAConstants.IntentType.MAINHALLFRAGMENT_ISGROUPCHAT, false);
			from = bundle.getInt(SpaceUtils.SPACE_FROM, -1);
			if (from == RewardListActivity.CHAT_FROM_REWARD || isGroupChatValue) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, BaseUtils.dip2px(this, 100));
				mBottomGridView.setLayoutParams(params);
				if (isGroupChatValue) {
					mBottomGridView.setNumColumns(3);
				}
			}

		}
		if (isGroupChatValue) {
			unReadNum = ChatManageBiz.refrushSession(this, mFriendUid, 1);
		} else {
			if (from == RewardListActivity.CHAT_FROM_REWARD) {
				unReadNum = ChatManageBiz.refrushSession(this, mFriendUid, 3);
			} else
				unReadNum = ChatManageBiz.refrushSession(this, mFriendUid, 0);
		}
		mVoiceRecod = Recorder.getInstance();
		mVoiceRecod.setHandler(this, mHandler);

		setResult(RESULT_CANCELED);

		mReceiver = new RecorderReceiver(mVoiceRecod);
		mRemainingTimeCalculator = new RemainingTimeCalculator();

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		solitaireRedPacketBiz = new SolitaireRedPacketBiz();
		if (mTencent == null)
			mTencent = Tencent.createInstance(BAConstants.QQ_APP_KEY, this);

		getRedPacketMoneyInfo();
	}

	/**
	 * 阅后即焚模式点击取消
	 *
	 */
	private void clickIvCancel2Burn() {
		mIsBurn = false;
		mChatHintLayout.setVisibility(View.GONE);
		mChatSentLayoutByBurn.setVisibility(View.GONE);
		mChatSentLayout.setVisibility(View.VISIBLE);
		if (mChatListAdpater.getList().size() <= 0 || from == RewardListActivity.CHAT_FROM_REWARD) {
			emptyChatTv.setVisibility(View.VISIBLE);
		} else {
			emptyChatTv.setVisibility(View.GONE);
		}
	}

	/**
	 * 点击声音按键界面处理
	 *
	 */
	private void clickIbtnChatKeyboardVoice() {
		hideEmoji();
		if (mBottomGridView.getVisibility() == View.VISIBLE) {
			mBottomGridView.setVisibility(View.GONE);
		}
		BaseUtils.hideKeyboard(this, mChatEditText);
		if (mChatVoiceTextByBurn.getVisibility() == View.GONE) {
			mChatTextLayout.setVisibility(View.GONE);
			mChatVoiceTextByBurn.setVisibility(View.VISIBLE);
			mChatKeyboardVoice.setImageResource(R.drawable.message_icon_keyboard_selector);
		} else {
			mChatTextLayout.setVisibility(View.VISIBLE);
			mChatVoiceTextByBurn.setVisibility(View.GONE);
			mChatKeyboardVoice.setImageResource(R.drawable.message_icon_voice_selector);
		}
	}

	private void hideEmoji() {
		if (ll_emoji != null) {
			ll_emoji.setVisibility(View.GONE);
		}
	}

	/**
	 * 点击 “+”键 界面处理
	 *
	 */
	private void clickIbtnChatPlus() {
		hideEmoji();
		mBottomGridView.setAdapter(new ChatBottomAdapter(this, isGroupChatValue, from, isOpenRedpacket));
		mBottomGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String str = (String) parent.getAdapter().getItem(position);
				if (!TextUtils.isEmpty(str)) {
					if (str.equals("照片")) {
						mBottomGridView.setVisibility(View.GONE);
						BaseCameraGalleryPhoto.intentSelectImage(ChatActivity.this, true, PhotoSetDialog.GET_PHOTO_BY_GALLERY);
					} else if (str.equals("拍照")) {
						mBottomGridView.setVisibility(View.GONE);
						BaseCameraGalleryPhoto.intentSelectImage(ChatActivity.this, false, PhotoSetDialog.GET_PHOTO_BY_CAMERA);
					} else if (str.equals("猜拳")) {
						new PlayFingerDialog(ChatActivity.this, mFriendSex, isGroupChatValue, mFriendUid, mFriendNick, mHandler).showDialog();
					} else if (str.equals("发红包")) {
						mBottomGridView.setVisibility(View.GONE);
						Bundle bundle = new Bundle();
						bundle.putInt(SendRedPacketActivity.STR_GROUP_ID, mFriendUid);
						bundle.putString(SendRedPacketActivity.STR_GROUP_NAME, mFriendNick);
						BaseUtils.openResultActivity(ChatActivity.this, SendRedPacketActivity.class, bundle, GET_RETURN_SEND_REDPACKET);
					} else if ("红包接龙".equals(str)) {
						mBottomGridView.setVisibility(View.GONE);
						Bundle bundle = new Bundle();
						bundle.putInt(SendSolitaireRedPacketActivity.STR_GROUP_ID, mFriendUid);
						bundle.putString(SendSolitaireRedPacketActivity.STR_GROUP_NAME, mFriendNick);
						BaseUtils.openResultActivity(ChatActivity.this, SendSolitaireRedPacketActivity.class, bundle,
								GET_RETURN_SEND_SOLITAIRE_REDPACKET);
					} else if (str.equals("阅后即焚")) {
						clickIvBurn();
					} else if (str.equals("视频")) {
						if (!SdCardUtils.isExistSdCard()) {
							BaseUtils.showTost(ChatActivity.this, R.string.nosdcard);
							return;
						}
						BaseUtils.openResultActivity(ChatActivity.this, SelectVedioActivity.class, null, GET_VIDEO_SELECT);
					} else if (str.equals(getResources().getString(R.string.str_truth))) {//发起心话
						PlayTruth truthBiz = new PlayTruth();
						truthBiz.sendTruth(mFriendUid, 0, "", mHandler);
					} else if (str.equals("大冒险")) {
						PlayDareBiz playB = new PlayDareBiz();
						playB.PlayDare(mFriendUid, "", mHandler);
					}
				}
			}
		});
		BottomAnimation();
	}

	private void BottomAnimation() {
		BaseUtils.hideKeyboard(this, mChatEditText);
		if (mBottomGridView == null) {
			return;
		}
		if (mBottomGridView.getVisibility() == View.GONE) {
			Animation mAnimIn = AnimationUtils.loadAnimation(this, R.anim.popwin_bottom_in);
			showLlPicSelect(300, mAnimIn);
		} else {
			mBottomGridView.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示图片
	 *
	 * @param delayTime 时间
	 */
	private void showLlPicSelect(int delayTime, final Animation mAnimIn) {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mBottomGridView.startAnimation(mAnimIn);
				mBottomGridView.setVisibility(View.VISIBLE);
			}

		}, delayTime);
	}

	private void deleteRecording() {
		mChatPuls.setClickable(true);
		mChatKeyboardVoice.setClickable(true);
		if (ll_record != null) {
			ll_record.setVisibility(View.GONE);
		}

		if (tv_chat_record_time != null) {
			tv_chat_record_time.setText("0/60“");
		}
		if (!mIsBurn) {
			mChatVoiceText.setText(R.string.press_on_record);
		} else {
			mChatVoiceTextByBurn.setText(R.string.press_on_record);
		}
		mAnimationDrawable.stop();
		mVoiceRecod.delete();
	}

	private void startRecording() {
		mVoiceRecod.setShow(false);
		if (!isInflateRecord) {
			View recordView = vs_record.inflate();
			ll_record = (LinearLayout) recordView.findViewById(R.id.ll_chat_recording);
			tv_chat_record_time = (TextView) recordView.findViewById(R.id.tv_chat_recording);
			iv_chat_record = (ImageView) recordView.findViewById(R.id.iv_chat_recording);
			isInflateRecord = true;
		}

		ll_record.setVisibility(View.VISIBLE);

		if (!mIsBurn) {
			mChatVoiceText.setText(R.string.release_finish);
		} else {
			mChatVoiceTextByBurn.setText(R.string.release_finish);
		}
		try {
			if (mVoiceRecod.isPlaying()) {
				mVoiceRecod.stop();
			}

			//			mAudioLenght = 0;
			recording(AUDIO_FILE_NAME);
			mAnimationDrawable = (AnimationDrawable) iv_chat_record.getBackground();
			mAnimationDrawable.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void recording(String name) {
		mRemainingTimeCalculator.reset();
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			updateUi();
		} else if (!mRemainingTimeCalculator.diskSpaceAvailable()) {
			updateUi();
		} else {
			stopAudioPlayback();
			ChatVoiceBiz.saveVoiceByType(name, mRequestedType, mRemainingTimeCalculator, mVoiceRecod);
		}
	}

	/**
	 * 音频刷新界面
	 *
	 */
	private void updateUi() {
		updateVUMeterView();
		updateTimerView();
	}

	private void stopAudioPlayback() {
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "pause");
		sendBroadcast(i);
	}

	private void registerExternalStorageListener() {
		if (mSDCardMountEventReceiver == null) {
			mSDCardMountEventReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					mVoiceRecod.reset();
					updateUi();
				}
			};
			IntentFilter iFilter = new IntentFilter();
			iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
			iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
			iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
			iFilter.addDataScheme("file");
			registerReceiver(mSDCardMountEventReceiver, iFilter);
		}
	}

	//******************************************实现接口的实现方法**************************************
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.et_chat_text:
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				try {
					mChatListView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				} catch (Exception e) {//魅族手机会报错，只能够这样捕获了
					e.printStackTrace();
				}
				mBottomGridView.setVisibility(View.GONE);
				hideEmoji();
			}
			break;
		case R.id.lv_chat://触摸了listview，软键盘消失,
			mBottomGridView.setVisibility(View.GONE);
			hideEmoji();
			BaseUtils.hideKeyboard(this, mChatEditText);
			break;
		case R.id.tv_chat_voice:
		case R.id.tv_chat_voice1:
			touchVoice(event);
			break;
		}
		return false;
	}

	private void touchVoice(MotionEvent event) {
		try {
			float y = 0.0f;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startRecording();
				y = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				if (y - event.getY() > 90) {
					deleteRecording();
				} else {
					if (!mIsVoiceSent) {
						stopRecording();
					}
					mIsVoiceSent = false;
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
		for (int position : reverseSortedPositions) {
			if (position < 0 || null == mChatListAdpater || mChatListAdpater.getCount() < 0) {
				return;
			}

			try {
				if (mChatListAdpater != null && position < mChatListAdpater.getCount()) {
					ChatDatabaseEntity info = (ChatDatabaseEntity) mChatListAdpater.getItem(position);
					if (!TextUtils.isEmpty(info.getMesSvrID()) && !info.getMesSvrID().equals("0")) {
						ChatManageBiz.deleteMessage(ChatActivity.this, mFriendUid, info.getMesSvrID(), isGroupChatValue);
						mChatListAdpater.removePos(position);
						ObserveFireView.getInstance().getMap().remove(mFriendUid + "_" + info.getMesSvrID());
						mHandler.sendEmptyMessage(HandlerType.CHAT_REFRUSH);

						//判断是否是最后一条消息被焚烧，是则需要更新消息列表的数据
						if (position == mChatListAdpater.getCount()) {
							position = position - 1;
							String sessionMsg = null;
							if (position < 0) {
								sessionMsg = getString(R.string.no_message);
							} else {
								info = (ChatDatabaseEntity) mChatListAdpater.getItem(position);
								MessageType chatType = MessageType.getMessage(info.getType());
								if (chatType != null)
									switch (chatType) {
									case TEXT:
										sessionMsg = info.getMessage();
										break;
									case BURN_VOICE:
									case VOICE:
									case VOICE_KEY:
									case GOGIRL_DATA_TYPE_ANONYM_VOICE:
									case GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_VOICE:
										sessionMsg = getString(R.string.session_voice);
										break;
									case IMAGE:
									case IMAGE_KEY:
									case BURN_IMAGE:
									case GOGIRL_DATA_TYPE_ANONYM_PIC:
									case GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_PICKEY:
										sessionMsg = getString(R.string.session_image);
										break;
									default:
										sessionMsg = getString(R.string.no_message);
										break;
									}
							}
							if (sessionMsg != null)
								ChatSessionManageBiz.upDataSession(ChatActivity.this, sessionMsg, mFriendNick, mFriendUid,
										System.currentTimeMillis(), 0);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onStateChanged(int state) {
		updateUi();
	}

	@Override
	public void onError(int error) {}

	@Override
	public void onClickItem(ChatDatabaseEntity chatEntity, int pos, final View view, final boolean isLeft) {
		if (chatEntity.getType() == MessageType.BURN_VOICE.getValue() || chatEntity.getType() == MessageType.BURN_VOICE_KEY.getValue()
				|| chatEntity.getType() == MessageType.GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_VOICE.getValue()
				|| chatEntity.getType() == MessageType.DATA_TYPE_TRANSITORY_ANONYM_VOICEKEY.getValue()) {
			String content = BaseString.getFilePath(this, mFriendUid, MessageType.VOICE.getValue()) + File.separator + chatEntity.getMesLocalID();
			ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(chatEntity.getMessage());
			int index = 0;
			if (!TextUtils.isEmpty(messageEntity.getVoicelength())) {
				index = Integer.parseInt(messageEntity.getVoicelength());
			}

			String bmp = isLeft ? loadHeadImgStr() : (new String(BAApplication.mLocalUserInfo.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR);
			mDialog = new ChatVoiceDialog(this, index + "", bmp, content, mVoiceRecod, mAnimDrawable, chatEntity.getMesSvrID(), mFriendUid,
					mFriendSex, mFriendNick, mHandler, ChatManageBiz.getInManage(this), mChatListAdpater, isLeft, from);
			if (mDialog != null)
				mDialog.show();
		}
	}

	@Override
	public void onClickRepeat(final View v, int repeatcount, long mid, boolean isLeft, final String burnId, final ProgressBar pb) {
		String content = BaseString.getFilePath(this, mFriendUid, MessageType.IMAGE.getValue()) + File.separator + mid;
		if (!isLeft) {
			if (repeatcount == -1) {
				mChatBurnView.setVisibility(View.GONE);
				mChatListView.setMode(Mode.PULL_FROM_START);
				((RepeatButton) v).setBurn(true);
			} else {
				if (mInput.isActive()) {
					BaseUtils.hideKeyboard(this, mChatEditText);
				}
				mChatBurnView.setPreButton((RepeatButton) v);
				mChatListView.setMode(Mode.BOTH);

				mChatBurnView.setBurnViewUI(this, -1, content);
				mChatBurnView.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void toDismiss(int pos, View convertView, boolean b) {
		mSwipAdapter.notifyToDismiss(pos, convertView, b);
	}

	@Override
	public void fireTimeSend(String burnId) {//阅后即焚
		if (null != BAApplication.mLocalUserInfo) {
			ChatManageBiz.getInManage(this).changeMessageStatusBySerID(ChatActivity.this, mFriendUid, ChatStatus.READED_BURN.getValue(), burnId,
					false);
			mChatListAdpater.setStatusByBurnId(ChatStatus.READED_BURN.getValue(), burnId);
			mHandler.sendEmptyMessageAtTime(HandlerType.CHAT_REFRUSH, 0);
			if (from == RewardListActivity.CHAT_FROM_REWARD) {
				ChatManageBiz.getInManage(this).sentMsg(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
						BAApplication.mLocalUserInfo.uid.intValue(), burnId.getBytes(), MessageType.GOGIRL_DATA_TYPE_ANONYM_RECEIPT.getValue(), -1,
						mFriendUid, burnId, new String(BAApplication.mLocalUserInfo.nick), mFriendNick, BAApplication.mLocalUserInfo.sex.intValue(),
						mFriendSex, ChatActivity.this, 0, from);
			}
			ChatManageBiz.getInManage(this).sentMsg(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
					BAApplication.mLocalUserInfo.uid.intValue(), burnId.getBytes(), MessageType.RECEIPT.getValue(), -1, mFriendUid, burnId,
					new String(BAApplication.mLocalUserInfo.nick), mFriendNick, BAApplication.mLocalUserInfo.sex.intValue(), mFriendSex,
					ChatActivity.this, 0, 0);
		}
	}

	@Override
	public void goneViewAndSend(String burnId) {//阅后即焚，看完图后
		mChatBurnView.setVisibility(View.GONE);
		mChatListView.setMode(Mode.PULL_FROM_START);

		if (null != BAApplication.mLocalUserInfo) {
			ChatManageBiz.getInManage(this).sentMsg(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
					BAApplication.mLocalUserInfo.uid.intValue(), burnId.getBytes(), MessageType.RECEIPT.getValue(), -1, mFriendUid, burnId,
					new String(BAApplication.mLocalUserInfo.nick), mFriendNick, BAApplication.mLocalUserInfo.sex.intValue(), mFriendSex,
					ChatActivity.this, 0, from);
		}
	}

	@Override
	public void showViewByBurn(View v, String burnId, int pro, long mid) {
		String content = BaseString.getFilePath(this, mFriendUid, MessageType.IMAGE.getValue()) + File.separator + mid;
		BaseUtils.hideKeyboard(this, mChatEditText);
		mChatBurnView.setPreButton((RepeatButton) v);

		if (pro > 0) {
			mChatListView.setMode(Mode.BOTH);
			mChatBurnView.setBurnViewUI(ChatActivity.this, pro, content);

			mChatBurnView.setVisibility(View.VISIBLE);
		} else {
			mChatBurnView.setVisibility(View.GONE);
			mChatListView.setMode(Mode.PULL_FROM_START);
			mHandler.sendEmptyMessage(HandlerType.CHAT_REFRUSH);
		}
	}

	public void onEvent(ChatEvent event) {
		try {
			mChatListAdpater.setStatusByBurnId(ChatStatus.READED_BURN.getValue(), event.getBurnId());
			mHandler.sendEmptyMessage(HandlerType.CHAT_REFRUSH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEvent(NoticeEvent event) {
		super.onEvent(event);
		if (event.getFlag() == NoticeEvent.NOTICE66) {//把这个界面销毁掉，就是退出群就退出群聊界面
			this.defaultFinish();
		} else if (event.getFlag() == NoticeEvent.NOTICE76) {//更改未读的消息
			int num = event.getNum();
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_RECEIVE_ONLINE_MESSAGE_NEW, num, num);
		} else if (event.getFlag() == NoticeEvent.NOTICE80) {//系统通知分享
			ChatMessageEntity entity = (ChatMessageEntity) event.getObj();
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_SYSTEM_NOTICE_SHARE_VALUE, entity);
		} else if (event.getFlag() == NoticeEvent.NOTICE95) {
			final long createtime = event.getNum4();
			final int invitedstatus = event.getNum2();
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					mChatListAdpater.setSkillByBurnid(invitedstatus, String.valueOf(createtime));
					mChatListView.getRefreshableView().setSelection(mChatListView.getBottom());
				}
			});
		} else if (event.getFlag() == NoticeEvent.NOTICE97) {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_APPEND_DATA_VALUE, event.getObj());
		} else if (event.getFlag() == NoticeEvent.NOTICE99) {
			ChatDatabaseEntity entity = (ChatDatabaseEntity) event.getObj();
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HAREM_UPDATE_CAN_GRAB_REDPACKET, entity);
		}
	}

	public void onBurn() {
		mChatBurnView.setVisibility(View.GONE);
		mChatListView.setMode(Mode.PULL_FROM_START);
		if (mChatBurnView.getPreButton() != null) {
			mChatBurnView.getPreButton().onBurnFile();
		}
		mChatBurnView.setPreButton(null);
	}

	@Override
	public void onClickReSend(final ChatDatabaseEntity chatEntity, int postion) {//点击重发
		if (chatEntity == null) {
			return;
		}
		if (chatEntity.getType() == MessageType.TEXT.getValue()) {
			sendTextMessage(chatEntity.getMessage(), chatEntity, true);
		} else if (chatEntity.getType() == MessageType.IMAGE.getValue() || chatEntity.getType() == MessageType.BURN_IMAGE.getValue()) {
			String data = BaseString.getFilePath(this, mFriendUid, MessageType.IMAGE.getValue()) + File.separator + chatEntity.getMesLocalID();
			sendChatImage(chatEntity, data, true);
		} else if (chatEntity.getType() == MessageType.VOICE.getValue() || chatEntity.getType() == MessageType.BURN_VOICE.getValue()) {
			byte[] data = BaseFile.getBytesByFilePath(BaseString.getFilePath(this, mFriendUid, MessageType.VOICE.getValue()) + File.separator
					+ chatEntity.getMesLocalID());
			sendVoiceMsg(data, chatEntity.getType(), chatEntity, true);
		} else if (chatEntity.getType() == MessageType.VIDEO.getValue()) {
			ThreadPoolService.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					boolean isUploadSuccess = false;
					String path = "peipei";
					if (BAConstants.IS_TEST) {
						path = "peipeitest2";
					}
					if (BaiduCloudUtils.getObjectMetaFromCloud("/" + path + "/" + chatEntity.getMessage())) {
						isUploadSuccess = true;
					} else {//文件不存在
						try {
							if (!SdCardUtils.isExistSdCard())
								return;

							File file = new File(Environment.getExternalStorageDirectory(), chatEntity.getMessage() + ".mp4");

							BaiduCloudUtils.putFileToCloud(file);

							isUploadSuccess = true;
						} catch (Exception e) {
							isUploadSuccess = false;
							e.printStackTrace();
						}

						if (isUploadSuccess) {//上传成功
							mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_SEND_SUCCESS_VALUE, 0, 0, chatEntity));
						} else {//上传失败
							mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.CHAT_VEDIO_SEND_FAILED_VALUE, 0, 0, chatEntity));
						}
					}
				}
			});
		} else if (chatEntity.getType() == MessageType.WITHANTEFINGER.getValue() || chatEntity.getType() == MessageType.NEWFINGER.getValue()) {
			String message = chatEntity.getMessage();
			if (!TextUtils.isEmpty(message)) {
				ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(message);
				try {
					int bet = Integer.parseInt(messageEntity.getBet());
					int finger1 = Integer.parseInt(messageEntity.getFinger1());
					int finger2 = Integer.parseInt(messageEntity.getFinger2());
					int fingerUid2 = Integer.parseInt(messageEntity.getFingerUid2());
					FingerGuessingInfo fingerInfo = null;
					if (BAApplication.mLocalUserInfo != null) {
						if (BAApplication.mLocalUserInfo.uid.intValue() == fingerUid2) {
							fingerInfo = GroupChatUtils.getReplyFingerGuessInfo(this, messageEntity, finger2);
							finger1 = finger2;
						}
					}
					FingerGruessMessage.getInstance().playFinger(this, mFriendUid, chatEntity, isGroupChatValue, fingerInfo, mFriendNick, mFriendSex,
							mHandler, true, finger1, bet, Integer.parseInt(messageEntity.getAntetype()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else if (chatEntity.getType() == MessageType.GOGIRL_DATA_TYPE_SMILE.getValue()) {
			if (isGroupChatValue) {
				SendGroupChatTextMessage.getInstance().sendHaremEmotionGroupMsg(this, chatEntity.getMessage().getBytes(), mFriendUid, mFriendNick,
						mHandler, chatEntity, true);
			} else {
				SendPrivateChatTextMessage.getInstance().sendPrivateChatHaremEmotionMessage(this, chatEntity.getMessage(), mFriendUid, mFriendNick,
						mFriendSex, mHandler, chatEntity, true, from);
			}
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		try {
			smileVoiceList.clear();
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				if (mChatBurnImage.getVisibility() == View.VISIBLE) {
					onBurn();
				}
				break;
			}
			return super.dispatchTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onGone() {
		BaseUtils.hideKeyboard(this, mChatEditText);
		if (mBottomGridView.getVisibility() == View.VISIBLE) {
			mBottomGridView.setVisibility(View.GONE);
		}
		hideEmoji();
	}

	////////////////////////----------------------JEFF-------------------------------//////////////////////////////////

	private void operateVideoThread(final Uri uri) {//执行视频压缩处理
		if (uri == null) {
			return;
		}
		ThreadPoolService.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				VideoUtils.operateVideo(ChatActivity.this, uri, mHandler);
			}
		});
	}

	@Override
	public void onClickVoice(ChatDatabaseEntity chatEntity, int pos, final ImageButton view, final boolean isLeft) {//点击播放声音
		String content = BaseString.getFilePath(this, mFriendUid, MessageType.VOICE.getValue()) + File.separator + chatEntity.getMesLocalID();
		if (mVoiceRecod.isPlaying()) {
			mVoiceRecod.setShow(false);
			mVoiceRecod.stopPlayback();
			setAudiIbtnBgStopAudioAmin();

			if (mCurrVoiceIndex != pos) {
				mCurrVoiceIndex = pos;
				mCurrPlayImage = view;
				mCurrPlayImage.setId((int) chatEntity.getMesLocalID());
				mCurrPlayImage.setTag(isLeft == true ? 0 + "" : 1 + "");

				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (isLeft && !isGroupChatValue) {
							view.setBackgroundResource(R.drawable.message_img_voice_grey);
						} else {
							view.setBackgroundResource(R.drawable.message_img_voice_white);
						}
						try {
							mAnimDrawable = (AnimationDrawable) mCurrPlayImage.getBackground();
							mAnimDrawable.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, 500);

				mVoiceRecod.startPlayback(content, -1);
			}
		} else {
			mCurrVoiceIndex = pos;
			if (isLeft && !isGroupChatValue) {
				view.setBackgroundResource(R.drawable.message_img_voice_grey);
			} else {
				view.setBackgroundResource(R.drawable.message_img_voice_white);
			}
			mCurrPlayImage = view;
			mCurrPlayImage.setTag(isLeft == true ? 0 + "" : 1 + "");
			mCurrPlayImage.setId((int) chatEntity.getMesLocalID());
			mAnimDrawable = (AnimationDrawable) mCurrPlayImage.getBackground();
			mAnimDrawable.start();
			mVoiceRecod.setShow(false);
			mVoiceRecod.startPlayback(content, -1);
		}
	}

	/**
	 * 跳转到私聊界面
	 * @author Jeff
	 *
	 * @param activity
	 * @param fUid
	 * @param fNick
	 * @param fSex
	 * @param fHeadKey
	 * @param isGroupChat
	 */
	public static void intentChatActivity(Activity activity, int fUid, String fNick, int fSex, boolean isGroupChat, boolean isCheckLoyalty, int from) {
		Bundle bundle = new Bundle();
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, fUid);
		bundle.putString(BAConstants.IntentType.MAINHALLFRAGMENT_USERNICK, fNick);
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, fSex);
		bundle.putBoolean(BAConstants.IntentType.MAINHALLFRAGMENT_ISGROUPCHAT, isGroupChat);
		bundle.putInt(SpaceUtils.SPACE_FROM, from);
		BaseUtils.openActivity(activity, ChatActivity.class, bundle);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {//activity被销毁时，恢复数据
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putBoolean("isGroupChat", isGroupChatValue);
			outState.putInt("mFriendUid", mFriendUid);
		}

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {//当按home按钮是保存数据
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			isGroupChatValue = savedInstanceState.getBoolean("isGroupChat");
			mFriendUid = savedInstanceState.getInt("mFriendUid", -1);
		}

	}

	@Override
	public void decodeMsg(ChatDatabaseEntity chatEntity, int type) {//在聊天界面收到新消息
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_RECEIVER_NEW_MESSAGE_SUCCESS_VALUE, type, type, chatEntity);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mChatEditText.getText().clear();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			int uid = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, -1);
			boolean isGroup = bundle.getBoolean(BAConstants.IntentType.MAINHALLFRAGMENT_ISGROUPCHAT, false);

			if (uid != mFriendUid || isGroup != isGroupChatValue) {
				mFriendUid = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, -1);
				mFriendNick = bundle.getString(BAConstants.IntentType.MAINHALLFRAGMENT_USERNICK);
				mFriendSex = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, Gender.MALE.getValue());
				isGroupChatValue = bundle.getBoolean(BAConstants.IntentType.MAINHALLFRAGMENT_ISGROUPCHAT, false);
				if (isGroupChatValue) {
					unReadNum = ChatManageBiz.refrushSession(this, mFriendUid, 1);
					//					findViewById(R.id.message_title_present_iv).setVisibility(View.GONE);
					giftIv.setVisibility(View.GONE);
					skillIv.setVisibility(View.GONE);
				} else {
					unReadNum = ChatManageBiz.refrushSession(this, mFriendUid, 0);
					if (mFriendUid == BAConstants.PEIPEI_XIAOPEI || mFriendUid == BAConstants.PEIPEI_CHAT_TONGZHI) {
						//						findViewById(R.id.message_title_present_iv).setVisibility(View.GONE);
						giftIv.setVisibility(View.GONE);
						skillIv.setVisibility(View.GONE);
					} else {
						//						findViewById(R.id.message_title_present_iv).setVisibility(View.VISIBLE);
						giftIv.setVisibility(View.VISIBLE);
						if (ListUtils.isEmpty(skillAdapter.getList())) {
							skillIv.setVisibility(View.GONE);
						} else {
							skillIv.setVisibility(View.VISIBLE);
						}
					}
				}

				if (mFriendUid == BAConstants.PEIPEI_XIAOPEI) {
					mTitle.setText(R.string.str_system_user);
				} else if (mFriendUid == BAConstants.PEIPEI_CHAT_TONGZHI) {
					mTitle.setText(R.string.sys_toggle);
				} else if (mFriendUid == BAConstants.PEIPEI_CHAT_XIAOPEI) {
					mTitle.setText(R.string.xiaopei);
				} else {
					mTitle.setText(mFriendNick);
				}
				if (mFriendUid == BAConstants.PEIPEI_XIAOPEI || mFriendUid == BAConstants.PEIPEI_CHAT_TONGZHI) {
					mChatSentLayout.setVisibility(View.GONE);
				}

				mChatListAdpater.clearList();
				mChatListView.setEmptyView(mEmpty);
				mHandler.sendEmptyMessage(INIT_FLAG);

				mChatBgLayout.setBackgroundResource(R.drawable.chat_bg);
				groupBackgound.setBackgroundColor(Color.parseColor("#00000000"));
				isGroupChatValue = false;
				gropActionLayout.setVisibility(View.GONE);
				mBottomGridView.setNumColumns(4);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				mBottomGridView.setLayoutParams(params);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE://停止滚动
			// 判断滚动到底部  
			if (mChatListView.getRefreshableView().getLastVisiblePosition() == (mChatListView.getRefreshableView().getCount() - 1)) {
				isScrollBottom = true;
			} else {
				isScrollBottom = false;
			}
			break;
		case OnScrollListener.SCROLL_STATE_FLING://开始滚动
			mChatListView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
			isScrollBottom = false;
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://正在滚动
			mChatListView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
			isScrollBottom = false;
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void OnHaremEmotionClick(EmotionBean haremEmotionBean) {//后宫表情点击事件，发送消息
		if (haremEmotionBean != null) {
			if (TextUtils.isEmpty(haremEmotionBean.getCharacter())) {
				return;
			}
			if (isGroupChatValue) {
				SendGroupChatTextMessage.getInstance().sendHaremEmotionGroupMsg(this, haremEmotionBean.getCharacter().getBytes(), mFriendUid,
						mFriendNick, mHandler, null, false);
			} else {
				SendPrivateChatTextMessage.getInstance().sendPrivateChatHaremEmotionMessage(this, haremEmotionBean.getCharacter(), mFriendUid,
						mFriendNick, mFriendSex, mHandler, null, false, from);
			}
		}

	}

	@Override
	public void OnClickHaremVoice(String path) {//点击后宫特殊表情的声音
		playSmileVoice(path, false);
	}

	@Override
	public void resDareSendFlowers(int retCode) {

	}

	private void initPopupWindow() {
		View view = View.inflate(this, R.layout.activity_skill_popupwindow, null);
		//		ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
		HorizontalListView skill_hzlistview = (HorizontalListView) view.findViewById(R.id.skill_hzlistview);
		skill_hzlistview.setAdapter(skillAdapter);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		//		iv_close.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View arg0) {
		//				BaseUtils.hidePopupWindow(popupWindow);
		//			}
		//		});
		skill_hzlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				SkillTextInfo info = (SkillTextInfo) skillAdapter.getItem(arg2);
				if (info != null) {
					BAApplication.getInstance().setSkillTextInfo(info);
					SkillInviteActivity.openMineFaqActivity(mFriendUid, mFriendSex, mFriendNick, ChatActivity.this);
					BaseUtils.hidePopupWindow(popupWindow);
				}
			}
		});

	}

	private void showSkillWindow() {
		if (popupWindow != null) {
			popupWindow.showAsDropDown(rl_title, 0, 0);
		}
	}

	@Override
	public void skillInviteAccept(ChatDatabaseEntity dbEntity) {
		String message = dbEntity.getMessage();
		ChatMessageEntity entity = ChatMessageBiz.decodeMessage(message);
		createtime = dbEntity.getMesSvrID();
		GoddessSkillEngine engine = new GoddessSkillEngine();
		engine.requestSkillInviteResult(dbEntity.getFromID(), Integer.valueOf(entity.getId()), Integer.valueOf(entity.getGiftId()), 0,
				Integer.valueOf(entity.getSkilllistid()), this);
	}

	@Override
	public void skillInviteRefuse(ChatDatabaseEntity dbEntity) {
		String message = dbEntity.getMessage();
		ChatMessageEntity entity = ChatMessageBiz.decodeMessage(message);
		createtime = dbEntity.getMesSvrID();
		GoddessSkillEngine engine = new GoddessSkillEngine();
		engine.requestSkillInviteResult(dbEntity.getFromID(), Integer.valueOf(entity.getId()), Integer.valueOf(entity.getGiftId()), 1,
				Integer.valueOf(entity.getSkilllistid()), this);
	}

	@Override
	public void skillInviteResultOnSuccess(int resCode, Object object) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_INVITE_RES_SUCCESS, resCode, object);
	}

	@Override
	public void skillInviteResultOnError(int resCode) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_INVITE_RES_ERROR, resCode);
	}

	@Override
	public void skillInviteStatusCallBack(ChatDatabaseEntity dbEntity, int progress, boolean isGroupChat) {
		sendHandlerMessage(mHandler, HandlerValue.UPDATE_GODDESS_SKILL_INVITE_TIME, progress, dbEntity);
	}

	@Override
	public void checkRedpacketStateOnSuccess(int code, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS_SUCCESS, code, obj);

	}

	@Override
	public void checkRedpacketStateOnError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS_ERROR, code);
	}

	@Override
	public void onSolitaireRedPacketMoneySuccess(int code, int isOpen, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_SOLITAIRE_REDPACKET_INFO_SUCCESS, code, isOpen, obj);
	}

	@Override
	public void onSolitaireRedPacketMoneyError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_SOLITAIRE_REDPACKET_INFO_ERROR, code);
	}

	/**
	 * 获取皇宫信息
	 * @author Aaron
	 *
	 */
	//	private void getGroupInfo() {
	//		groupBackgroundKey = SharedPreferencesTools.getInstance(this).getStringValueByKey("group_bg_" + mFriendUid);
	//		groupBackgroundKey="";
	//		if (TextUtils.isEmpty(groupBackgroundKey)) {
	//			RequestGetGroupInfo info = new RequestGetGroupInfo();
	//			info.reqgetGroupInfo(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
	//					mFriendUid, new IGetGroupInfo() {
	//
	//						@Override
	//						public void getGroupInfo(int retCode, int uid, GroupInfo info) {
	//							groupBackgroundKey = new String(info.groupbadgekey);
	//							SharedPreferencesTools.getInstance(ChatActivity.this).saveStringKeyValue(groupBackgroundKey, "group_bg_" + mFriendUid);
	//							Log.d("Aaron", "groupBackgroundKey=="+groupBackgroundKey);
	//							setBackgroud(groupBackgroundKey + "@false@120@120");
	//						}
	//					});
	//		} else
	//			setBackgroud(groupBackgroundKey + "@false@120@120");
	//	}

	@Override
	public void onClickNick(String nick) {
		String msg = mChatEditText.getText().toString().trim();
		try {
			if (!TextUtils.isEmpty(msg)) {
				if (!TextUtils.isEmpty(isAbout)) {
					String newAbout = "@" + nick + " ";
					mChatEditText.setText(msg.replaceAll(isAbout, newAbout));
					isAbout = newAbout;
				} else {
					isAbout = "@" + nick + " ";
					StringBuilder builder = new StringBuilder();
					builder.append(isAbout).append(mChatEditText.getText().toString());
					mChatEditText.setText(builder.toString());
				}
			} else {
				isAbout = "@" + nick + " ";
				mChatEditText.setText(isAbout.replace("[", "").replace("]", ""));
			}
			mChatEditText.setSelection(isAbout.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void checkHallRedpacketStateOnSuccess(int code, Object obj) {

	}
}
