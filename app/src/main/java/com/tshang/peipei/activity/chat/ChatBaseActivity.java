package com.tshang.peipei.activity.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.AddBlackListDialog;
import com.tshang.peipei.activity.dialog.DareResultDialog;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.dialog.VedioSendDialog;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.mine.ReportActivity;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.activity.skillnew.adapter.ChatSkillAdapter;
import com.tshang.peipei.activity.skillnew.adapter.SpaceSkillAdapter;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.biz.chat.groupchat.GroupChatUtils;
import com.tshang.peipei.model.biz.chat.groupchat.SendGroupChatRedPacketMessage;
import com.tshang.peipei.model.biz.chat.privatechat.SendPrivateChatVoiceMessage;
import com.tshang.peipei.model.biz.space.SpaceRelationshipBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetRelationship;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackSentChatMessage;
import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;
import com.tshang.peipei.model.hall.MainHallBiz;
import com.tshang.peipei.model.redpacket2.SolitaireRedPacketBiz;
import com.tshang.peipei.model.request.RequestGrapSolitaireRedpacket.GetGrapSolitaireRedPacketCallBack;
import com.tshang.peipei.model.request.RequestPersonSkillInfo.GetPersonSkillInfo;
import com.tshang.peipei.model.skillnew.GoddessSkillEngine;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfo;
import com.tshang.peipei.protocol.asn.gogirl.RelationshipInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.operate.RedpacketOperate;
import com.tshang.peipei.storage.database.operate.RelationOperate;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;
import com.tshang.peipei.vender.micode.soundrecorder.RecorderReceiver;
import com.tshang.peipei.vender.micode.soundrecorder.RemainingTimeCalculator;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;
import com.tshang.peipei.view.BurnPicView;
import com.tshang.peipei.view.PageControlView;
import com.tshang.peipei.view.fireview.SwipeDismissAdapter;

/**
 * @Title: ChatBaseActivity.java 
 *
 * @Description: 实现私聊的一些接口
 *
 * @author allen  
 *
 * @date 2014-7-30 下午5:12:45 
 *
 * @version V1.0   
 */
public class ChatBaseActivity extends BaseActivity implements BizCallBackSentChatMessage, BizCallBackGetRelationship, BizCallBackGetUserInfo,
		GetPersonSkillInfo, GetGrapSolitaireRedPacketCallBack {
	//************************************常量声明 start****************************
	public static final String TAG = "ChatActivity";

	public static final int GET_PRIVATE_ALBUM_BY_NETWORK = 1011;
	public static final int GET_VIDEO_SELECT = 1012;
	public static final int GET_RETURN_SEND_REDPACKET = 1013;
	public static final int GET_RETURN_SEND_SOLITAIRE_REDPACKET = 1014;

	public final static String CHECK_LOYALTY = "check_loyalty";

	protected final static int INIT_FLAG = 1;
	public final static int MSG_CURR_SECOND = 4;
	public final static int UPDATE_UI_FLAG = 7;
	public final static int REFRESH_FLAG = 9;
	public final static int ADD_CHAT_RECORD = 10;// 增加一条聊天记录,并更新UI
	public final static int ADD_ALL_CHAT_RECORD = 11;
	public final static int HEAD_BACK = 12;
	public final static int NOTIFICATION_MESSAGE = 13;
	public final static int GETRELATIONSHIP = 14;//是否已经关注
	public final static int GETLOYALTY = 15;
	public final static int GET_CHAT_MESSAGE = 16;
	public final static int MSHOWNUM = 10;

	// *** 音频录制相关参数 *** //
	public static int MAX_TIME = 60; // 最长录制时间，单位秒，0为无时间限制
	public final String AUDIO_FILE_NAME = "w_audio_temp_";

	//************************************常量end****************************
	String mFriendNick;
	protected int mFriendUid = -1;
	protected int from;
	protected int isOpenRedpacket = 0; //是否打开接龙红包 0默认关闭

	boolean mIsBurn = false;
	int mFriendSex;
	AnimationDrawable mAnimDrawable;

	SwipeDismissAdapter mSwipAdapter;
	ChatAdapter mChatListAdpater;

	Recorder mVoiceRecod;
	List<AnimationDrawable> mAnimDrawableList = new ArrayList<AnimationDrawable>();
	String mRequestedType = "audio/amr";
	RecorderReceiver mReceiver;
	boolean mStopUiUpdate;
	BroadcastReceiver mSDCardMountEventReceiver;

	ImageButton mChatBurnImage, mChatCancel;
	protected FragmentManager mFragmentManager = null;
	RelativeLayout mChatHintLayout;
	Dialog mDialog;

	BurnPicView mChatBurnView;
	PullToRefreshListView mChatListView;
	protected RelativeLayout mChatBgLayout;
	LinearLayout mChatTextLayout;

	ImageButton mChatKeyboardVoice;
	ImageButton mChatPuls;
	ImageButton ibtn_emotion;
	EditText mChatEditText;
	RelativeLayout groupBackgound;

	TextView mChatVoiceText, mChatVoiceTextByBurn;
	AnimationDrawable mAnimationDrawable;
	LinearLayout mChatSentLayout, mChatSentLayoutByBurn;
	TextView mChatLoyaltyText;
	LinearLayout mChatLoyaltyLayout;

	RemainingTimeCalculator mRemainingTimeCalculator;
	ImageView mMoreLayout;

	int mCurrVoiceIndex;
	ImageButton mCurrPlayImage;

	TextView mEmpty;
	int mLoyalty = 0;//聊天需要的魅力贡献值
	int mUserLoyalty = 0;//用户的魅力贡献值
	boolean mIsVoiceSent = false;

	GridView mBottomGridView;
	protected int unReadNum = 0;
	protected TextView tvBack;

	protected ViewStub vs_emotion;
	protected boolean isInflateEmotion = false;
	protected LinearLayout ll_emoji;
	protected ViewPager mEmojiPager;
	protected PageControlView pageControlView;
	protected ImageView iv_common_face;
	protected ImageView iv_emoji_face;
	protected ImageView iv_harem_face;

	protected int giftNum = 0;
	protected GiftInfo giftInfo;

	protected ViewStub vs_record;
	protected boolean isInflateRecord = false;
	protected LinearLayout ll_record;
	protected TextView tv_chat_record_time;
	protected ImageView iv_chat_record;
	protected boolean isLoadMore = false;
	protected String isAbout;
	protected int oldTextLenght = 0;
	protected int aboutTextLenght = -1;
	protected String toUserName;

	protected ArrayList<String> smileVoiceList = new ArrayList<String>();//后宫表情语音播放队列

	protected IWXAPI mWXapi;
	protected Tencent mTencent;
	protected DareResultDialog dareDialog;

	protected LinearLayout dareLayout;
	protected ImageView dareFlower;
	protected ImageView dareEgg;
	protected ImageView dareBrick;
	protected ImageView darePass;
	protected String currDareId;
	protected LinearLayout dareHintLayout;
	protected ImageView ivDareSend;
	protected TextView emptyChatTv;
	protected ImageView giftIv;
	protected ImageView skillIv;
	protected LinearLayout aboutLayout;
	protected TextView aboutText;
	protected ImageView aboutCloseIv;

	protected PopupWindow popupWindow;
	protected RelativeLayout rl_title;
	protected LinearLayout gropActionLayout;
	protected ImageView redpacketAction, fingerAction, crazyAction;

	protected String groupBackgroundKey;

	protected ChatSkillAdapter skillAdapter;
	protected String createtime; //用于消息超时的时候更新女神技能的状态
	protected SolitaireRedPacketBiz solitaireRedPacketBiz;
	protected Dialog openFailDialog;
	protected Dialog openSuccessDialog;
	protected TextView tv_redpacket;
	protected View fl_redpacket;
	protected ImageView chat_redpacket_icon;
	protected PopupWindow redPopupWindow;

	//**************************************变量end*********************************

	@SuppressWarnings("unchecked")
	//*************************************handler start******************************
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		final GoGirlUserInfo userInfo = UserUtils.getUserEntity(this);
		if (userInfo == null) {
			return;
		}
		try {
			switch (msg.what) {
			case INIT_FLAG:
				if (isGroupChatValue) {
					setBackgroud();
				}

				ThreadPoolService.getInstance().execute(new Runnable() {

					@Override
					public void run() {

						reqToWX();

						List<ChatDatabaseEntity> temp = ChatManageBiz.getInManage(ChatBaseActivity.this).getChatList(ChatBaseActivity.this,
								mFriendUid, 0, MSHOWNUM, isGroupChatValue);
						HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_LOCAL_MESSAGE_LIST_VALUE, temp);
						List<ChatDatabaseEntity> deleteTemp = ChatManageBiz.getInManage(ChatBaseActivity.this).getChatList(ChatBaseActivity.this,
								mFriendUid, 5000, 1, isGroupChatValue);
						if (deleteTemp != null && !deleteTemp.isEmpty()) {//超过5000条数据清掉之前的
							ChatDatabaseEntity deleteEntity = deleteTemp.get(0);
							if (deleteEntity != null) {
								long messageLocalId = deleteEntity.getMesLocalID();
								ChatManageBiz.getInManage(ChatBaseActivity.this).deleteChatList(ChatBaseActivity.this, mFriendUid, isGroupChatValue,
										messageLocalId);
							}
						}
					}
				});

				break;
			case HandlerValue.CHAT_LOCAL_MESSAGE_LIST_VALUE:
				List<ChatDatabaseEntity> temp = (List<ChatDatabaseEntity>) msg.obj;
				if (temp != null) {
					mChatListAdpater.appendToList(temp);
					mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
					//					mChatListAdpater.notifyDataSetChanged();
					if (mFriendUid != 50000 && mFriendUid != 50002 && from != RewardListActivity.CHAT_FROM_REWARD) {//过虑系统通知与系统消息
						emptyChatTv.setVisibility(View.GONE);
					}
				}
				if (mChatListAdpater.getCount() == 0) {
					mChatListView.setEmptyView(mEmpty);
					if (mFriendUid != 5000 && mFriendUid != 50002 || from == RewardListActivity.CHAT_FROM_REWARD) {//过虑系统通知与系统消息
						emptyChatTv.setVisibility(View.VISIBLE);
					}
				}

				if (isGroupChatValue) {
					fl_redpacket.setVisibility(View.VISIBLE);
					//用来显示可以抢的接龙红包数量
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
				} else {
					fl_redpacket.setVisibility(View.GONE);
				}
				break;
			case ADD_ALL_CHAT_RECORD:
				mChatListAdpater.notifyDataSetChanged();
				mSwipAdapter.notifyDataSetChanged();
				mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				break;
			case HandlerType.CHAT_SENT_VOICE:
				if (mVoiceRecod.sampleFile() != null && mVoiceRecod.sampleFile().exists()) {
					if (mVoiceRecod.sampleLength() <= 1) {
						BaseUtils.showTost(this, R.string.voicelength_too_short);
						//						mAudioLenght = 0;
						return;
					} else {
						try {
							MessageType type;
							if (mIsBurn) {
								if (from == RewardListActivity.CHAT_FROM_REWARD) {
									type = MessageType.GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_VOICE;
								} else
									type = MessageType.BURN_VOICE;
							} else {
								if (from == RewardListActivity.CHAT_FROM_REWARD) {
									type = MessageType.GOGIRL_DATA_TYPE_ANONYM_VOICE;
								} else
									type = MessageType.VOICE;
							}
							if (mVoiceRecod.sampleFile() != null) {
								File file = mVoiceRecod.sampleFile();

								byte[] buffer = BaseFile.getByteFromVocieFile(file);
								sendVoiceMsg(buffer, type.getValue(), null, false);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				break;
			case HandlerType.CHAT_REFRUSH:
				mChatListAdpater.notifyDataSetChanged();
				mSwipAdapter.notifyDataSetChanged();
				break;
			case HandlerType.SENT_MESSAGE_CALLBACK:
				ChatMessageReceiptEntity recepit = (ChatMessageReceiptEntity) msg.obj;
				if (recepit.getType() != MessageType.RECEIPT.getValue()) {
					if (msg.arg1 == 0) {
						ChatManageBiz.getInManage(this).changeMessageStatusByLocalID(this, mFriendUid, ChatStatus.SUCCESS.getValue(),
								recepit.getLocalId(), recepit.getTime(), isGroupChatValue);
					} else {
						if (msg.arg1 == rspContMsgType.E_GG_BLACKLIST) {
							ChatManageBiz.getInManage(this).changeMessageStatusByLocalID(this, mFriendUid, ChatStatus.SUCCESS.getValue(),
									recepit.getLocalId(), recepit.getTime(), isGroupChatValue);
							mChatListAdpater.appendToList(ChatManageBiz.insertMessage(this, mFriendUid, getString(R.string.add_black_content),
									recepit.getTime(), isGroupChatValue));
							mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
						} else {
							if (msg.arg1 == rspContMsgType.E_GG_FORBIT) {
								new HintToastDialog(this, R.string.limit_talk, R.string.ok).showDialog();
							} else if (msg.arg1 == rspContMsgType.E_GG_LOYALTY) {
								getRelationship(userInfo);
							}
							ChatManageBiz.getInManage(this).changeMessageStatusByLocalID(this, mFriendUid, ChatStatus.FAILED.getValue(),
									recepit.getLocalId(), recepit.getTime(), isGroupChatValue);
						}
					}
					mChatListAdpater.setStatusByLocalID(msg.arg1, recepit.getLocalId());
					mHandler.sendEmptyMessage(HandlerType.CHAT_REFRUSH);
				} else {
					if (msg.arg1 != 0) {
						ChatRecordBiz.saveReceipt(this, mFriendUid, recepit.getBurnId(), mFriendNick, mFriendSex);
					}
				}
				break;
			case HandlerValue.CHAT_SMILE_VOICE_PLAY_ERROR_VALUE:
				if (msg.arg1 >= 0) {
					if (!smileVoiceList.isEmpty())
						smileVoiceList.remove(0);
					if (!smileVoiceList.isEmpty()) {
						playSmileVoice(smileVoiceList.get(0), true);
					}
				}
				break;
			case HandlerType.STATE_END_PLAYING:
				int msg1 = msg.arg1;
				if (msg1 < 0) {
					if (mChatListAdpater != null) {
						setAudiIbtnBgStopAudioAmin();
					}
					mVoiceRecod.stop();
					mAnimDrawableList.clear();
					if (mDialog != null) {
						mDialog.findViewById(R.id.chat_item_left_voice_image).setBackgroundResource(R.drawable.message_img_voice3_white);
					}
				} else {
					if (!smileVoiceList.isEmpty())
						smileVoiceList.remove(0);
					if (!smileVoiceList.isEmpty()) {
						playSmileVoice(smileVoiceList.get(0), true);
					}
				}
				break;
			case REFRESH_FLAG:
				mChatListView.onRefreshComplete();
				mChatListAdpater.notifyDataSetChanged();
				break;
			case GETRELATIONSHIP:
				int retCode = msg.arg1;
				if (retCode == 0) {
					RelationshipInfo relation = (RelationshipInfo) msg.obj;
					RelationOperate relationOperate = RelationOperate.getInstance(this);
					relationOperate.updateRelationByShip(mFriendUid, relation.loyalty.intValue());
					mUserLoyalty = relation.loyalty.intValue();
					setLoyalty(mUserLoyalty, mLoyalty);
				}
				break;
			case GETLOYALTY:
				if (msg.arg1 == 0) {
					GoGirlUserInfo userinfo = (GoGirlUserInfo) msg.obj;
					RelationOperate relationOperate = RelationOperate.getInstance(this);
					relationOperate.updateRelationByChat(mFriendUid, userinfo.chatthreshold.intValue());
					mLoyalty = userinfo.chatthreshold.intValue();
					mFriendSex = userinfo.sex.intValue();

					if (userinfo.chatthreshholdgift.length > 0) {
						GiftInfo giftinfo = new GiftInfo();
						try {
							BERDecoder dec = new BERDecoder(userinfo.chatthreshholdgift);

							giftinfo.decode(dec);
						} catch (ASN1Exception e) {
							e.printStackTrace();
						}

						giftInfo = giftinfo;
					}
					setLoyalty(mUserLoyalty, mLoyalty);
				}
				break;
			case HandlerValue.CHAT_WEALTH_NOT_ENGOUH_VALUE:
				mChatListAdpater.appendToList(ChatManageBiz.insertMessage(this, mFriendUid, "金额不足，不能够发送猜拳", System.currentTimeMillis(),
						isGroupChatValue));
				mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
				break;
			case rspContMsgType.E_GG_LACK_OF_SILVER:
				mChatListAdpater.appendToList(ChatManageBiz.insertMessage(this, mFriendUid, "金额不足，不能够发送猜拳", System.currentTimeMillis(),
						isGroupChatValue));
				mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
				break;
			case HandlerValue.CHAT_VEDIO_CUT_VALUE://正在截取视频
				break;
			case HandlerValue.CHAT_VEDIO_CUT_SUCCESS_VALUE://截取视频成功
				break;
			case HandlerValue.CHAT_VEDIO_CUT_FAILED_VALUE://视频截取失败
				break;
			case HandlerValue.CHAT_VEDIO_COMPRESSION_VALUE://视频正在压缩
				BaseUtils.showDialog(this, R.string.str_compressioning);
				break;
			case HandlerValue.CHAT_VEDIO_COMPRESSION_SUCCESS_VALUE://压缩成功
				if (SdCardUtils.isExistSdCard()) {
					String strSize = (String) msg.obj;
					if (!TextUtils.isEmpty(strSize)) {
						String[] strs = strSize.split(",");
						if (strs != null && strs.length == 2) {
							new VedioSendDialog(this, android.R.style.Theme_Translucent_NoTitleBar, strs[0], strs[1], mFriendUid, mFriendNick,
									mFriendSex, mHandler, isGroupChatValue, from).showDialog();
						}
					}

				}
				break;
			case HandlerValue.CHAT_VEDIO_COMPRESSION_FAILED_VALUE://压缩失败
				try {
//					MobclickAgent.reportError(this, "视频压缩失败" + android.os.Build.VERSION.RELEASE + "机型==" + android.os.Build.MODEL + "失败日志==="
//							+ msg.obj);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				BaseUtils.showTost(this, R.string.str_compression_failed);
				break;
			case HandlerValue.CHAT_VEDIO_SENDING_VALUE://视频正在上传
				break;
			case HandlerValue.CHAT_VEDIO_SEND_SUCCESS_VALUE://视频上传成功
				ChatDatabaseEntity cdbeSuccess = (ChatDatabaseEntity) msg.obj;
				if (cdbeSuccess != null) {
					try {
						if (null != BAApplication.mLocalUserInfo) {
							ChatManageBiz.getInManage(this).sentMsg(
									BAApplication.mLocalUserInfo.auth,
									BAApplication.app_version_code,
									BAApplication.mLocalUserInfo.uid.intValue(),
									cdbeSuccess.getMessage().getBytes(),
									from == RewardListActivity.CHAT_FROM_REWARD ? MessageType.GOGIRL_DATA_TYPE_ANONYM_VEDIO.getValue()
											: MessageType.VIDEO.getValue(), -1, mFriendUid, cdbeSuccess.getMesSvrID(),
									new String(BAApplication.mLocalUserInfo.nick), mFriendNick, BAApplication.mLocalUserInfo.sex.intValue(),
									mFriendSex, this, (int) cdbeSuccess.getMesLocalID(), from);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case HandlerValue.CHAT_VEDIO_DOWNLOAD_FAILED_VALUE://视频下载失败
				BaseUtils.showTost(this, "下载失败原因：1、网络异常，稍后重试  2、此视频已被删除");
				try {
//					MobclickAgent.reportError(this, "视频下载失败");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			case HandlerValue.CHAT_VEDIO_SEND_FAILED_VALUE://视频上传失败
				ChatDatabaseEntity cdbe = (ChatDatabaseEntity) msg.obj;
				if (cdbe != null) {
					ChatManageBiz.getInManage(this).changeMessageStatusByLocalID(this, mFriendUid, ChatStatus.FAILED.getValue(),
							cdbe.getMesLocalID(), cdbe.getCreateTime(), isGroupChatValue);
					mHandler.sendEmptyMessage(HandlerType.CHAT_REFRUSH);
				}
				try {
//					MobclickAgent.reportError(this, "视频上传失败");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
			case HandlerValue.CHAT_VEDIO_DOWNLOAD_SUCCESS_VALUE:
				File file = (File) msg.obj;
				mChatListAdpater.setStatusByLocalID(0, msg.arg1);
				mHandler.sendEmptyMessage(ADD_ALL_CHAT_RECORD);

				if (file != null) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri uri = Uri.fromFile(file);
					intent.setDataAndType(uri, "video/mp4");
					startActivity(intent);
				}
				break;
			case HandlerValue.CHAT_VEDIO_DOWNLOAD_CLICK_VALUE:
				mChatListAdpater.setStatusByLocalID(ChatStatus.SENDING.getValue(), msg.arg1);
				mHandler.sendEmptyMessage(ADD_ALL_CHAT_RECORD);
				break;
			case HandlerValue.CHAT_CLEAR_CONTENT_INFO_SUCCESS_VALUE://清除聊天记录
				mChatListAdpater.clearList();
				mChatListView.setEmptyView(mEmpty);
				fl_redpacket.setVisibility(View.GONE);

				break;
			case HandlerValue.CHAT_ADD_BLACK_LIST_VALUE:
				new AddBlackListDialog(this, R.string.sure_addblack, R.string.ok, R.string.cancel, mFriendUid, mHandler).showDialog();
				break;
			case HandlerValue.CHAT_REPORT_VALUE:
				//				new ReportDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mFriendUid, 0,true).showDialog(0, 0);
				ReportActivity.openMineFaqActivity(this, mFriendUid);
				break;
			case HandlerValue.HAREM_AGREE_GROUP_SUCCESS_VALUE://同意加入后宫
				ChatDatabaseEntity haremChat = (ChatDatabaseEntity) msg.obj;
				if (haremChat != null) {
					ChatManageBiz.getInManage(this).changeMessageStatusByLocalID(this, mFriendUid, ChatStatus.SENDING.getValue(),
							haremChat.getMesLocalID(), haremChat.getCreateTime(), isGroupChatValue);
					mChatListAdpater.appendToList(ChatManageBiz.insertMessage(this, mFriendUid, getString(R.string.str_have_join_your_harem),
							haremChat.getCreateTime() + 1, isGroupChatValue));
					mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				}
				break;
			case HandlerValue.HAREM_AGREE_GROUP_HAS_JOIN_VALUE://已经是群成员
				ChatDatabaseEntity hasmember = (ChatDatabaseEntity) msg.obj;
				if (hasmember != null) {
					ChatManageBiz.getInManage(this).changeMessageStatusByLocalID(this, mFriendUid, ChatStatus.SENDING.getValue(),
							hasmember.getMesLocalID(), hasmember.getCreateTime(), isGroupChatValue);
					mChatListAdpater.appendToList(ChatManageBiz.insertMessage(this, mFriendUid, "他/她已经存在此宫中", hasmember.getCreateTime() + 1,
							isGroupChatValue));
					mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				}
				break;
			case HandlerValue.HAREM_AGREE_GROUP_ARRAIVE_LIMIT_VALLUE://已达群上限
				ChatDatabaseEntity limitmember = (ChatDatabaseEntity) msg.obj;
				if (limitmember != null) {
					ChatManageBiz.getInManage(this).changeMessageStatusByLocalID(this, mFriendUid, ChatStatus.SENDING.getValue(),
							limitmember.getMesLocalID(), limitmember.getCreateTime(), isGroupChatValue);
					mChatListAdpater.appendToList(ChatManageBiz.insertMessage(this, mFriendUid, "已达群人数上限", limitmember.getCreateTime() + 1,
							isGroupChatValue));
					mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				}
				break;
			case HandlerValue.HAREM_JOIN_GROUP_REPEAT_JOIN_VALUE://已经加入过后宫
				ChatDatabaseEntity limitgroup = (ChatDatabaseEntity) msg.obj;
				if (limitgroup != null) {
					ChatManageBiz.getInManage(this).changeMessageStatusByLocalID(this, mFriendUid, ChatStatus.SENDING.getValue(),
							limitgroup.getMesLocalID(), limitgroup.getCreateTime(), isGroupChatValue);
					mChatListAdpater.appendToList(ChatManageBiz.insertMessage(this, mFriendUid, "他/她已加入了后宫，不能够重复加入", limitgroup.getCreateTime() + 1,
							isGroupChatValue));
					mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
				}
				break;
			case HandlerValue.HAREM_AGREE_GROUP_IGNORE_VALUE:
				ChatDatabaseEntity ignoreChat = (ChatDatabaseEntity) msg.obj;
				if (ignoreChat != null) {
					ChatManageBiz.getInManage(this).changeMessageStatusByLocalID(this, mFriendUid, ChatStatus.FAILED.getValue(),
							ignoreChat.getMesLocalID(), ignoreChat.getCreateTime(), isGroupChatValue);
				}
				mHandler.sendEmptyMessage(HandlerType.CHAT_REFRUSH);
				break;
			case HandlerValue.HAREM_AGREE_GROUP_FAILED_VALUE:
			case HandlerValue.CHAT_REFLESH_UI_VALUE:
				if (msg.arg1 == rspContMsgType.E_GG_LOYALTY) {
					getRelationship(userInfo);
				}
				mChatListAdpater.notifyDataSetChanged();
				break;
			case HandlerValue.CHAT_APPEND_DATA_VALUE://添加刚刚发送的那条消息,
			case HandlerValue.CHAT_VEDIO_SEND_APPEND_VALUE://追加视频数据
				ChatDatabaseEntity cdbeappend = (ChatDatabaseEntity) msg.obj;
				appendChatData(cdbeappend);
				if (cdbeappend.getType() != 36) {
					mChatEditText.setText("");
				}
				break;
			case HandlerValue.CHAT_SKILL_ORDER_FAILED:
				BaseUtils.showTost(this, "操作失败");
				break;
			case HandlerValue.CHAT_SKILL_ORDER_TIME_OUT_VALUE:
				BaseUtils.showTost(this, "该订单已失效");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean isScrollBottom = true;

	protected void appendChatData(ChatDatabaseEntity entity) {//追加聊天数据
		if (entity != null) {
			mChatListAdpater.appendToList(entity);
			if (isScrollBottom)
				mChatListView.getRefreshableView().setSelection(mChatListAdpater.getCount() - 1);
		}
	}

	private void reqToWX() {
		mWXapi = WXAPIFactory.createWXAPI(this, BAConstants.WXAPPID, true);
		mWXapi.registerApp(BAConstants.WXAPPID);
	}

	/**
	 * 发送语音消息
	 *
	 */
	protected void sendVoiceMsg(byte[] data, int type, ChatDatabaseEntity entity, boolean isResend) {
		if (data == null || data.length == 0) {
			return;
		}

		if (isGroupChatValue) {
			GroupChatUtils.sendGroupChatVoice(this, data, mVoiceRecod.sampleLength(), mFriendUid, mFriendNick, mHandler, false,
					new ChatDatabaseEntity());
		} else {
			SendPrivateChatVoiceMessage.getInstance().sendPrivateChatVoiceMessage(this, data, mVoiceRecod.sampleLength(), type, mFriendUid,
					mFriendNick, mFriendSex, mHandler, entity, isResend, from);

		}
	}

	public void setLoyalty(int relationship, int chatthreshold) {
		if (relationship < chatthreshold && !isGroupChatValue) {
			if (!mIsBurn) {
				mChatLoyaltyLayout.setVisibility(View.VISIBLE);
				mChatSentLayout.setVisibility(View.GONE);
			}
			if (giftInfo == null) {
				giftNum = (chatthreshold + 1 - relationship) / 2;
				mChatLoyaltyText.setText(String.format(getString(R.string.loyalty_content), "玫瑰花 x " + giftNum));
			} else {
				giftNum = 1;
				mChatLoyaltyText.setText(String.format(getString(R.string.loyalty_content), new String(giftInfo.name)));
			}

			mChatKeyboardVoice.setEnabled(false);
			mChatPuls.setEnabled(false);
			mChatVoiceTextByBurn.setEnabled(false);
			mChatBurnImage.setEnabled(false);
			mChatCancel.setEnabled(false);

			mBottomGridView.setVisibility(View.GONE);
		} else {
			if (mFriendUid != BAConstants.PEIPEI_XIAOPEI && mFriendUid != BAConstants.PEIPEI_CHAT_TONGZHI) {
				if (!mIsBurn) {
					mChatSentLayout.setVisibility(View.VISIBLE);
				}
			}
			mChatLoyaltyLayout.setVisibility(View.GONE);
			mChatKeyboardVoice.setEnabled(true);
			mChatPuls.setEnabled(true);
			mChatVoiceTextByBurn.setEnabled(true);
			mChatBurnImage.setEnabled(true);
			mChatCancel.setEnabled(true);
		}
	}

	protected String loadHeadImgStr() {
		String loadUid = "";
		if (isGroupChatValue) {
			loadUid = "-" + mFriendUid + "@false@120@120@uid";
		}
		//		else {
		//			loadUid = mFriendUid + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		//		}
		return loadUid;
	}

	protected void setBackgroud() {//设置整个布局的背景，多样化，通过头像来做的,没有头像的就加载logo的图来做
		imageLoader.loadImage("http://" + loadHeadImgStr(), new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {

			}

			@SuppressWarnings("deprecation")
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				Bitmap loadedImage = imageLoader.loadImageSync("drawable://" + R.drawable.logo);
				if (loadedImage != null) {
					loadedImage = BaseBitmap.BoxBlurFilter(loadedImage);
					if (loadedImage != null) {
						mChatBgLayout.setBackgroundDrawable(new BitmapDrawable(loadedImage));
						groupBackgound.setBackgroundColor(Color.parseColor("#55000000"));
					}
				}
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (loadedImage == null) {
					loadedImage = imageLoader.loadImageSync("drawable://" + R.drawable.logo);
				}
				if (loadedImage != null) {
					loadedImage = BaseBitmap.BoxBlurFilter(loadedImage);
					if (loadedImage != null) {
						mChatBgLayout.setBackgroundDrawable(new BitmapDrawable(loadedImage));
						groupBackgound.setBackgroundColor(Color.parseColor("#55000000"));
					}
				}
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {}
		});
	}

	/**
	 * 点击阅后即焚模式
	 *
	 */
	public void clickIvBurn() {
		mIsBurn = true;
		mBottomGridView.setVisibility(View.GONE);
		mChatSentLayout.setVisibility(View.GONE);
		mChatSentLayoutByBurn.setVisibility(View.VISIBLE);
		showPromptOrNot();
	}

	/**
	 * 是否显示阅后即焚提示
	 *
	 */
	private void showPromptOrNot() {
		boolean flag = SharedPreferencesTools.getInstance(this).getBooleanKeyValue(BAConstants.PEIPEI_CHAT_PROMPT);
		if (flag == false && from != RewardListActivity.CHAT_FROM_REWARD) {
			mChatHintLayout.setVisibility(View.VISIBLE);
			emptyChatTv.setVisibility(View.GONE);
		} else {
			mChatHintLayout.setVisibility(View.GONE);
			if (mChatListAdpater.getList().size() == 0 || from == RewardListActivity.CHAT_FROM_REWARD) {
				emptyChatTv.setVisibility(View.VISIBLE);
			} else {
				emptyChatTv.setVisibility(View.GONE);
			}
		}
	}

	public synchronized void setAudiIbtnBgStopAudioAmin() {
		if (mAnimDrawable != null) {
			mAnimDrawable.stop();
		}
		for (int i = 0; i < mAnimDrawableList.size(); i++) {
			AnimationDrawable animDrawable = mAnimDrawableList.get(i);
			animDrawable.stop();
		}
		mAnimDrawableList.clear();
		if (mCurrPlayImage != null) {
			mCurrPlayImage.clearAnimation();
			if (Integer.parseInt((String) mCurrPlayImage.getTag()) == 0 && !isGroupChatValue) {
				mCurrPlayImage.setBackgroundResource(R.drawable.message_img_voice3_grey);
			} else {
				mCurrPlayImage.setBackgroundResource(R.drawable.message_img_voice3_white);
			}
		}
	}

	/**
	 * 隐藏阅后即焚提示
	 *
	 */
	public void showTranslate() {
		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.activity_alpha_out);
		mChatHintLayout.setAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				SharedPreferencesTools.getInstance(ChatBaseActivity.this).saveBooleanKeyValue(true, BAConstants.PEIPEI_CHAT_PROMPT);
			}
		});

		mChatHintLayout.setVisibility(View.GONE);
		if (mChatListAdpater.getList().size() == 0 || from == RewardListActivity.CHAT_FROM_REWARD) {
			emptyChatTv.setVisibility(View.VISIBLE);
		} else {
			emptyChatTv.setVisibility(View.GONE);
		}
	}

	public void updateVUMeterView() {
		final int MAX_VU_SIZE = 14;
		if (mVoiceRecod.state() == Recorder.RECORDING_STATE) {
			int vuSize = MAX_VU_SIZE * mVoiceRecod.getMaxAmplitude() / 32768;
			if (vuSize >= MAX_VU_SIZE) {
				vuSize = MAX_VU_SIZE - 1;
			}
			// 切换录音时的图片
			mHandler.postDelayed(mUpdateVUMetur, 100);
		}
	}

	/**
	 * 循环刷新 
	 *
	 */
	public void updateTimerView() {
		int state = mVoiceRecod.state();
		if (state == Recorder.RECORDING_STATE) {
			long time = mVoiceRecod.progress();
			//			mAudioLenght = (int) time;

			String timeStr = String.format(getResources().getString(R.string.timer_format), time, 60);
			if (tv_chat_record_time != null) {
				tv_chat_record_time.setText(timeStr);
			}
			mHandler.postDelayed(mUpdateTimer, 500);

			if (time >= MAX_TIME) {
				mIsVoiceSent = true;
				stopRecording();
			}
		}
	}

	public void stopRecording() {
		if (ll_record != null) {
			ll_record.setVisibility(View.GONE);
		}
		mChatPuls.setClickable(true);
		mChatKeyboardVoice.setClickable(true);
		if (tv_chat_record_time != null) {
			tv_chat_record_time.setText("0/60“");
		}
		if (!mIsBurn) {
			mChatVoiceText.setText(R.string.press_on_record);
		} else {
			mChatVoiceTextByBurn.setText(R.string.press_on_record);
		}
		mVoiceRecod.stop();
		mAnimationDrawable.stop();

		mHandler.sendEmptyMessageDelayed(HandlerType.CHAT_SENT_VOICE, 100);
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {
		tvBack = (TextView) findViewById(R.id.message_title_back_tv);
		tvBack.setOnClickListener(this);
		mBottomGridView = (GridView) findViewById(R.id.gv_chat_pic_select);
	}

	@Override
	protected int initView() {
		return R.layout.activity_chat;
	}

	@Override
	public void getSentChatMessageCallBack(int retcode, ChatMessageReceiptEntity recepit) {
		sendHandlerMessage(mHandler, HandlerType.SENT_MESSAGE_CALLBACK, retcode, recepit);
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {
		sendHandlerMessage(mHandler, GETLOYALTY, retCode, userinfo);
	}

	@Override
	public void getRelationshipCallBack(int retCode, RelationshipInfo relation) {
		sendHandlerMessage(mHandler, GETRELATIONSHIP, retCode, relation);
	}

	private Runnable mUpdateVUMetur = new Runnable() {
		@Override
		public void run() {
			if (!mStopUiUpdate) {
				updateVUMeterView();
			}
		}
	};

	private Runnable mUpdateTimer = new Runnable() {
		public void run() {
			if (!mStopUiUpdate) {
				updateTimerView();
			}
		}
	};

	/**
	 * 播放后宫语音
	 * @author Administrator
	 *
	 * @param path
	 * @param isRemove
	 */
	protected void playSmileVoice(String path, boolean isRemove) {
		if (mVoiceRecod == null) {
			return;
		}
		if (mVoiceRecod.isPlaying()) {
			mVoiceRecod.stopPlayback();
		}

		String playPaht = SdCardUtils.getInstance().getHaremVoiceDir(path);
		File file = new File(playPaht);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null && files.length != 0) {
				int value = -1;
				if (isRemove) {
					value = 0;
				}
				mVoiceRecod.setShow(false);
				mVoiceRecod.startPlayback(files[0].getAbsolutePath(), value);
			} else {
				if (isRemove) {
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.CHAT_SMILE_VOICE_PLAY_ERROR_VALUE, 0, 0);
				}
			}
		}
	}

	public void getRelationship(GoGirlUserInfo userInfo) {
		//获取魅力贡献值和聊天限制
		SpaceRelationshipBiz space = new SpaceRelationshipBiz(this);
		space.getRelashionShip(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), mFriendUid, this);

		MainHallBiz.getInstance().getUserInfo(this, mFriendUid, this);

		RelationOperate relationOperate = RelationOperate.getInstance(this);
		if (relationOperate.isHaveSession(mFriendUid)) {
			relationOperate.updateRelation(mFriendUid, 0);
		}
	}

	public void getPersonSkillInfo() {
		GoddessSkillEngine engine = new GoddessSkillEngine();
		engine.requestPersonSkillInfo(mFriendUid, this);
	}

	@Override
	public void getPersonSkillInfoOnSuccess(int resCode, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_SUCCESS, resCode, obj);
	}

	@Override
	public void getPersonSkillInfoOnError(int resCode) {
		sendHandlerMessage(mHandler, HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_ERROR, resCode);
	}

	@Override
	public void onGrapSolitaireRedPacketSuccess(int code, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_GRAB_SOLITAIRE_REDPACKET_SUCCESS, code, obj);
	}

	@Override
	public void onGrapSolitaireRedPacketError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_GRAB_SOLITAIRE_REDPACKET_ERROR, code);
	}

	@Override
	public void onGrapHallSolitaireRedpacketSuccee(int code, Object obj) {
		// TODO Auto-generated method stub

	}
}
