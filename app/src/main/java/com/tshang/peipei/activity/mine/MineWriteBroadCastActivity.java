package com.tshang.peipei.activity.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.adapter.BroadCastEmotionViewAdd;
import com.tshang.peipei.activity.dialog.GiveUpVoiceDialog;
import com.tshang.peipei.activity.dialog.GiveUpVoiceDialog.SureOnclick;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.dialog.HintToastDialog.HintSureOnClickListener;
import com.tshang.peipei.activity.dialog.PrivilegeLevelDialog;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.redpacket2.SendHallRedpacketActivity;
import com.tshang.peipei.activity.redpacket2.adapter.TipsAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.base.json.GoGirlUserInfoFunctions;
import com.tshang.peipei.base.json.GoGirlUserJson;
import com.tshang.peipei.model.broadcast.BroadcastReceiverMgr;
import com.tshang.peipei.model.broadcast.WriteBroadCastBiz;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.redpacket2.SolitaireRedPacketBiz;
import com.tshang.peipei.model.request.RequestGetPrivilegeStatus;
import com.tshang.peipei.model.request.RequestIsSendHallRedpacket.GetIsSendHallRedpacketCallBack;
import com.tshang.peipei.model.request.RequestSendSolitaireRedpacket.GetSendSolitaireRedpacketCallBack;
import com.tshang.peipei.model.request.RequestSolitaireRedPacketMoney.GetSolitaireRedPacketCallBack;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.db.BroadCastColumn;
import com.tshang.peipei.storage.db.DBHelper;
import com.tshang.peipei.vender.common.util.FileUtils;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;
import com.tshang.peipei.vender.micode.soundrecorder.RecorderService;
import com.tshang.peipei.vender.micode.soundrecorder.RemainingTimeCalculator;
import com.tshang.peipei.vender.micode.soundrecorder.SoundRecorderPreferenceActivity;
import com.tshang.peipei.view.MySlipSwitch;
import com.tshang.peipei.view.MySlipSwitch.OnSwitchListener;
import com.tshang.peipei.view.MySlipSwitchTwo;
import com.tshang.peipei.view.MySlipSwitchTwo.OnSwitchTwoListener;
import com.tshang.peipei.view.PageControlView;
import com.tshang.peipei.view.PeiPeiListView;
import com.tshang.peipei.view.RedPacketCheckButton;
import com.tshang.peipei.view.TasksCompletedView;

import de.greenrobot.event.EventBus;

/**
 * 发送广播
 * @author Jeff
 *
 */
public class MineWriteBroadCastActivity extends BaseActivity implements OnSwitchListener, OnSwitchTwoListener, OnItemClickListener,
		HintSureOnClickListener, OnTouchListener, GetSolitaireRedPacketCallBack, GetSendSolitaireRedpacketCallBack, GetIsSendHallRedpacketCallBack {

	private static final String STR_MAGIC_STAR = "陪你一起看流星雨，一起许下心愿;";
	private static final String STR_MAGIC_ARROW = "你夸我百毒不侵，其实，我微笑下早已万箭穿心";
	private static final String STR_MAGIC_FEATHER = "优美的小精灵，在天空飞舞，如雪花般消失在了天际";
	private static final String STR_MAGIC_ROSE = "可爱的玫瑰，如一团团燃烧着青春的火焰，个个都是那样的红，那样的美！";
	private static final String STR_MAGIC_FIVE = "自从第一次见到你，我就爱上了你，让爱情的箭把我们串在一起吧.";
	private static final String STR_MAGIC_SIX = "让你欺负我，我要把你变猪头。";
	private static final String STR_MAGIC_SEVEN = "都说真爱没有永恒，可是我们的爱就是永恒。";
	private static final String STR_MAGIC_EIGHT = "爱你么么哒。";
	private static final String STR_MAGIC_NINE = "让你欺负我，看我天马流星拳。";
	private static final String STR_MAGIC_TEN = "让这爱的热气球带我们飞遍天涯海角。";

	/** 654863
	 * 广播特殊字体的颜色常量
	 */
	public static final int BROADCAST_TEXT_COLOR_ONE = 11277086;
	public static final int BROADCAST_TEXT_COLOR_TWO = 8130155;
	public static final int BROADCAST_TEXT_COLOR_THREE = 3219092;
	public static final int BROADCAST_TEXT_COLOR_BLACK = 2105376;
	private int current_select_text_color = BROADCAST_TEXT_COLOR_BLACK;

	public static final int REQUESTCODE = 10;

	private static final int BROADCAST_MAX_LENGTH = 40;
//	private static final int BROADCAST_INIT = 50;

	private EditText edtContent;
	private TextView tvLimit;
	private String mText = "";
	private ViewStub viewStub;
	private ViewStub imageColorStub;
	private ImageView mTxtEmoj;
	private TextView tvRight;
	private boolean canSend = true;
	private TextView tvCoinCost;
	private LinearLayout llBottomLayout;

	protected InputMethodManager mInput;

	public static final String STR_GOGIRLUSERINFO = "str_gogirluserinfo";
	private String str_gogirluserinfo = "";

	private String touserName = "";

	private LinearLayout ll_voice_layout;
	private Button btn_text_broadcast;
	private Button btn_voice_broadcast;
	private Button btn_rerecording;
	private Button btn_finger;
	private TextView tv_voice_time;
	private TasksCompletedView task_voice_progress;
	private RelativeLayout ll_content;
	private LinearLayout ll_finger;

	public static final int RECORD_INIT_STATUS = 0;
	public static final int RECORD_START_STATUS = 1;
	public static final int RECORD_PLAY_STATUS = 2;
	public static final int RECORD_PAUSE_STATUS = 3;
	public static final int RECORD_STOP_STATUS = 4;
	private int record_status = RECORD_INIT_STATUS;
	private static final String BROADCAST_FILE_TEMP_NAME = "broadcast_voice_temp";
	private static final int BROADCAST_VOICE_MAX_LEN_TIME = 60;
	private int currentVoiceTimeLen = BROADCAST_VOICE_MAX_LEN_TIME;//当前录制的时间时长
	private boolean isSending;

	/*****************************DYH 修改猜拳为大厅红包 start ************************/
	//	private PeiPeiCheckButton fingerNum100, fingerNum1000, fingerNum5000;
	//	private PeiPeiCheckButton fingerStone, fingerScissors, fingerCloth;
	//	private PeiPeiCheckButton1 fingerGold, fingerSilver;
	//
	//	private int fingerCurr;//猜拳数值
	//	private int fingerNum = 1000;//猜拳赌注数值

	private RedPacketCheckButton redpacket_money1, redpacket_money2, redpacket_money3;
	private TextView tv_send;
	/*****************************DYH 修改猜拳为大厅红包 end ************************/

	private List<GoGirlUserInfo> userInfos = new ArrayList<GoGirlUserInfo>();
	private List<GoGirlUserInfo> tempInfo = new ArrayList<GoGirlUserInfo>();

	private MySlipSwitch slipswitch_MSL;
	private MySlipSwitchTwo slipswithcTwo_MSL;

	private LinearLayout ll_color;
	private ImageView iv_select_color;
	private ImageView iv_black;
	private ImageView iv_red;
	private ImageView iv_blue;
	private ImageView iv_purple;

	private LinearLayout ll_emoji;
	private ViewPager mEmojiPager;
	private PageControlView pageControlView;
	private ImageView iv_common_face;
	private ImageView iv_emoji_face;

	private LinearLayout ll_bottom;
	/*****************************DYH 修改猜拳为大厅红包 start ************************/
	private SolitaireRedPacketBiz redPacketBiz;
	private List<RedPacketBetInfo> list;
	private TipsAdapter adapter;
	private List<RedPacketCheckButton> views = new ArrayList<RedPacketCheckButton>();
	private PeiPeiListView tips_list;
	private int redId;
	private View ll_tips;
	//	private int antetype = 0;
	/*****************************DYH 修改猜拳为大厅红包 end ************************/

	private GridView gv_specific;
	private List<String> strLists = new ArrayList<String>();//可以发的仙术

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showSoftInput(edtContent);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			str_gogirluserinfo = bundle.getString(STR_GOGIRLUSERINFO);
			if (!TextUtils.isEmpty(str_gogirluserinfo) && userInfos.size() < 3) {
				try {
					JSONObject jobjce = new JSONObject(str_gogirluserinfo);
					if (!jobjce.isNull("user")) {
						JSONArray jsonArray = jobjce.getJSONArray("user");
						GoGirlUserInfo[] infos = GoGirlUserInfoFunctions.getGoGirlUserInfo(jsonArray);
						if (infos != null && infos.length != 0) {
							GoGirlUserInfo userInfo = infos[0];
							userInfos.add(userInfo);

							String alias = SharedPreferencesTools.getInstance(MineWriteBroadCastActivity.this,
									BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(userInfo.uid.intValue());

							touserName = "@" + (TextUtils.isEmpty(alias) ? new String(userInfo.nick) : alias);
							edtContent.setText(touserName);
							edtContent.setSelection(touserName.length());
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.MAIN_BROADCAST_NO_ADD_MAGIC_VALUE://流星雨没有@用户
			new HintToastDialog(this, R.string.str_select_add_user, R.string.ok, this).showDialog();//TODO
			break;
		case HandlerValue.BROADCAST_MAGIC_LOWER_VALUE://不可以发送仙术,等级不够
			//TODO
			new PrivilegeLevelDialog(this).showDialog();
			break;
		case HandlerValue.BROADCAST_MAGIC_VALUE://可以发送仙术
			List<Integer> integerLists = (List<Integer>) msg.obj;
			strLists.clear();
			for (Integer integer : integerLists) {
				if (integer == 1) {
					strLists.add("流星雨");
				} else if (integer == 2) {
					strLists.add("万箭阵");
				} else if (integer == 3) {
					strLists.add("鸿毛雨");
				} else if (integer == 4) {
					strLists.add("玫瑰花语");
				} else if (integer == 5) {
					strLists.add("一箭钟情");
				} else if (integer == 6) {
					strLists.add("变变变");
				} else if (integer == 7) {
					strLists.add("真爱永恒");
				} else if (integer == 8) {
					strLists.add("烈焰红唇");
				} else if (integer == 9) {
					strLists.add("天马流星拳");
				} else if (integer == 10) {
					strLists.add("甜蜜热气球");
				}
			}
			//TODO
			if (ll_color != null)
				ll_color.setVisibility(View.GONE);
			if (ll_emoji != null) {
				ll_emoji.setVisibility(View.GONE);
			}
			if (gv_specific.getVisibility() == View.GONE) {
				showSpecificPanel();
				hideSoftInput(edtContent);
			} else {
				hideSpecificPanel();
			}
			break;
		case HandlerValue.BRAODCAST_USERWEALTH_SUCCESS_VALUE:
			String text = msg.obj.toString();
			if (!TextUtils.isEmpty(text)) {
				if (text.equals(getString(R.string.str_silver_fifty)) || text.equals("-5000银币") || text.equals("-50000银币")) {
					tvCoinCost.setTextColor(getResources().getColor(R.color.gray));
				} else {
					tvCoinCost.setTextColor(getResources().getColor(R.color.integer_value_color));
				}
				tvCoinCost.setText(msg.obj.toString());
			}
			break;
		case HandlerValue.BROADCAST_SEND_SUCCESS_VALUE:
			isSending = false;
			if (msg.arg1 == 0) {
				if (BAApplication.mLocalUserInfo != null) {
					String sUser = GoGirlUserJson.changeObjectDateToJson(BAApplication.mLocalUserInfo);
					if (TextUtils.isEmpty(sUser)) {
						sUser = "";
					}
					ContentValues values = new ContentValues();
					if (userInfos.size() == 0) {
						str_gogirluserinfo = "";
					} else {
						str_gogirluserinfo = GoGirlUserJson.changeArrayDateToJson(userInfos);
					}
					long time = System.currentTimeMillis() / 1000;
					values.put(BroadCastColumn.TOUSER, str_gogirluserinfo);
					values.put(BroadCastColumn.SENDUSER, sUser);
					values.put(BroadCastColumn.CREATETIME, time);
					values.put(BroadCastColumn.USERUID, BAApplication.mLocalUserInfo.uid.intValue());
					values.put(BroadCastColumn.STAUTS, "1");
					if (writeBiz.isUseMagic()) {
						values.put(BroadCastColumn.REVINT0, writeBiz.getCurrentMagicValue());
					} else {
						values.put(BroadCastColumn.REVINT0, current_select_text_color);
					}
					values.put(BroadCastColumn.REVINT1, writeBiz.getTypecolor());
					if (msg.arg2 == WriteBroadCastBiz.BROADCAST_TEXT_TYPE || writeBiz.isUseMagic()) {
						values.put(BroadCastColumn.CONTNET, mText);
					} else if (msg.arg2 == WriteBroadCastBiz.BROADCAST_VOICE_TYPE) {
						values.put(BroadCastColumn.CONTNET, "");
					}
					values.put(BroadCastColumn.REVINT, msg.obj.toString());
					values.put(BroadCastColumn.TYPE, writeBiz.getBroadcastType());
					if (msg.arg2 != WriteBroadCastBiz.BROADCAST_FINGER_TYPE) {
						DBHelper.getInstance(this).insert(BroadCastColumn.TABLE_NAME, values);
						NoticeEvent noticeEvent = new NoticeEvent();
						noticeEvent.setNum(writeBiz.getBroadcastType());
						noticeEvent.setFlag(NoticeEvent.NOTICE51);
						noticeEvent.setObj(time);
						EventBus.getDefault().post(noticeEvent);
					}

					this.finish();
				}
			} else if (msg.arg1 == rspContMsgType.E_GG_FORBIT) {
				new HintToastDialog(MineWriteBroadCastActivity.this, R.string.limit_talk, R.string.ok).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_NOT_ENGOUH_WELTH) {//财富不够
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够银币
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			}
			break;
		case HandlerType.STATE_END_PLAYING://播放完了
			mHandler.removeCallbacks(mSleepTask);
			task_voice_progress.setProgress(0);
			record_status = RECORD_STOP_STATUS;
			task_voice_progress.setBackgroundResource(R.drawable.task_braodcast_voice_start_selector);
			break;
		case HandlerValue.BROADCAST_TIME_LENGTH_VALUE://时长
			if (timeLen >= BROADCAST_VOICE_MAX_LEN_TIME) {
				canSendVoiceBroadcast = true;
				mHandler.removeCallbacks(mSleepTask);
				stopRecord();
			}
			task_voice_progress.setProgress(timeLen);
			tv_voice_time.setText(timeLen + "/" + currentVoiceTimeLen + "\"");
			break;
		case HandlerValue.BROADCAST_VOICE_CALL_STATE_RINGING_VALUE://来电话了如果是录音状态就废弃掉，如果是播放状态就暂停
			mHandler.removeCallbacks(mSleepTask);
			if (record_status == RECORD_START_STATUS) {
				restoreInitRecordStatus();
			}
			break;
		case HandlerValue.BROADCAST_VOICE_CALL_STATE_IDLE_VALUE://电话结束了，如果是播放状态就继续播放
			mHandler.postDelayed(mSleepTask, 1000);
			break;
		case HandlerValue.BROADCAST_COLOR_PRIVILEGE_STATUS_SUCCESS_VALUE:
			if (BAApplication.mLocalUserInfo != null) {
				SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(true,
						SharedPreferencesTools.BROADCAST_COLOR_PRIVILEGE + BAApplication.mLocalUserInfo.uid.intValue());
			}
			break;
		case HandlerValue.BROADCAST_COLOR_PRIVILEGE_STATUS_LOWER_VALUE:
			if (BAApplication.mLocalUserInfo != null) {
				SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(false,
						SharedPreferencesTools.BROADCAST_COLOR_PRIVILEGE + BAApplication.mLocalUserInfo.uid.intValue());
			}
			break;
		case HandlerValue.BROADCAST_DECREE_PRIVILEGE_STATUS_SUCCESS_VALUE:
			writeBiz.setDecree(true);
			writeBiz.setLeftNumDecree(msg.arg1);
			writeBiz.setBroadcastType(WriteBroadCastBiz.TOP_BRAODCAST);

			break;
		case HandlerValue.BROADCAST_DECREE_PRIVILEGE_STATUS_LOWER_VALUE:
			new PrivilegeLevelDialog(this).showDialog();
			slipswitch_MSL.setCurrentX(0);
			slipswitch_MSL.updateSwitchState(MySlipSwitch.SELECT_FIRST);
			writeBiz.setDecree(false);
			writeBiz.setBroadcastType(WriteBroadCastBiz.COMMON_BROADCAST);
			break;
		case HandlerValue.BROADCAST_DECREE_PRIVILEGE_STATUS_NO_MORE_VALUE:
			slipswitch_MSL.setCurrentX(0);
			slipswitch_MSL.updateSwitchState(MySlipSwitch.SELECT_FIRST);
			writeBiz.setDecree(false);
			writeBiz.setBroadcastType(WriteBroadCastBiz.COMMON_BROADCAST);
			BaseUtils.showTost(this, "您今天的特权已经用完");
			break;
		case HandlerValue.HALL_SOLITAIRE_REDPACKET_INFO_SUCCESS:
			if (0 == msg.arg1) {
				RedPacketBetInfoList redList = (RedPacketBetInfoList) msg.obj;
				list = getSolitaireRedpacketListData(redList);
				setFirstTip(list);
				setMoney(list);
				redpacket_money1.setCheck(true);

			} else {
				BaseUtils.showTost(this, "获取失败");
			}
			break;
		case HandlerValue.HALL_SOLITAIRE_REDPACKET_INFO_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.HALL_SEND_SOLITAIRE_REDPACKET_SUCCESS:
			int retCode = msg.arg1;
			if (retCode == 0) {
				BroadcastInfo info = (BroadcastInfo) msg.obj;
				appentSolitaireBroadcast(info);
			} else if (retCode == rspContMsgType.E_GG_PROPERTY_LACK) {//财富不足
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (retCode == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够银币
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			} else{
				Bundle bundle = msg.getData();
				String retMsg = bundle.getString("data");
				BaseUtils.showTost(this, retMsg);
			}
			break;
		case HandlerValue.HALL_SEND_SOLITAIRE_REDPACKET_ERROR:
		case HandlerValue.HALL_GET_ENABLE_SEND_REDPACKET_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.HALL_GET_ENABLE_SEND_SOLITAIRE_REDPACKET_SUCCESS:
			if(msg.arg1 == 0){
				if(msg.arg2 == 1){
					sendRedpacket();
				}else{
					String retMsg = (String) msg.obj;
					BaseUtils.showTost(this, retMsg);
				}
			}else{
				String retMsg = (String) msg.obj;
				BaseUtils.showTost(this, retMsg);
			}
			break;
		default:
			break;
		}
	}

	private void appentSolitaireBroadcast(BroadcastInfo info) {
		NoticeEvent noticeEvent = new NoticeEvent();
		noticeEvent.setFlag(NoticeEvent.NOTICE101);
		noticeEvent.setObj(info);
		EventBus.getDefault().post(noticeEvent);
		finish();
	}

	private boolean getColorPrivilegeStatus() {
		if (BAApplication.mLocalUserInfo != null) {
			return SharedPreferencesTools.getInstance(this).getBooleanKeyValue(
					SharedPreferencesTools.BROADCAST_COLOR_PRIVILEGE + BAApplication.mLocalUserInfo.uid.intValue());
		}
		return false;
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

	private void restoreInitRecordStatus() {//恢复重新录语音
		record_status = RECORD_INIT_STATUS;
		timeLen = 0;
		currentVoiceTimeLen = BROADCAST_VOICE_MAX_LEN_TIME;
		task_voice_progress.setProgress(timeLen);
		tv_voice_time.setText(getString(R.string.str_click_recording));
		mVoiceRecod.stopRecording();
		task_voice_progress.setBackgroundResource(R.drawable.task_braodcast_voice_recording_selector);
	}

	private boolean ViewStubIsInflate = false;
	private boolean viewStubTextColorInflate = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_broadcast_special_effects:
			//TODO
			writeBiz.requestMagic();
			break;

		case R.id.iv_broadcast_emoji:
			if (ll_color != null)
				ll_color.setVisibility(View.GONE);
			if (!ViewStubIsInflate) {
				View emojiView = viewStub.inflate();
				ll_emoji = (LinearLayout) emojiView.findViewById(R.id.ll_emotion);
				mEmojiPager = (ViewPager) emojiView.findViewById(R.id.emoji_viewpager);
				pageControlView = (PageControlView) emojiView.findViewById(R.id.pageControlView);
				iv_common_face = (ImageView) emojiView.findViewById(R.id.iv_common_emotion);
				iv_common_face.setOnClickListener(this);
				iv_emoji_face = (ImageView) emojiView.findViewById(R.id.iv_emoji_emotion);
				iv_emoji_face.setOnClickListener(this);
				ViewStubIsInflate = true;

			}

			hideSpecificPanel();

			if (ll_emoji != null) {
				if (ll_emoji.getVisibility() == View.GONE) {
					new BroadCastEmotionViewAdd(this, edtContent, pageControlView, mEmojiPager, iv_common_face, iv_emoji_face);
					hideSoftInput(edtContent);

					showEmojiPanel();
				} else {
					hideEmojiPanel();
					showSoftInput(edtContent);
				}
			}
			break;
		case R.id.iv_emoji_emotion:
			if (mEmojiPager != null) {
				iv_common_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				iv_emoji_face.setBackgroundColor(getResources().getColor(R.color.upload));
				mEmojiPager.setCurrentItem(2);
			}
			break;
		case R.id.iv_common_emotion:
			if (mEmojiPager != null) {
				iv_common_face.setBackgroundColor(getResources().getColor(R.color.upload));
				iv_emoji_face.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				mEmojiPager.setCurrentItem(0);
			}
			break;
		case R.id.iv_broadcast_color:
			if (!viewStubTextColorInflate) {
				View colorView = imageColorStub.inflate();
				iv_black = (ImageView) colorView.findViewById(R.id.iv_broadcast_textcolor_black);
				iv_black.setOnClickListener(this);
				iv_red = (ImageView) colorView.findViewById(R.id.iv_broadcast_textcolor_red);
				iv_red.setOnClickListener(this);
				iv_purple = (ImageView) colorView.findViewById(R.id.iv_broadcast_textcolor_purple);
				iv_purple.setOnClickListener(this);
				iv_blue = (ImageView) colorView.findViewById(R.id.iv_broadcast_textcolor_blue);
				iv_blue.setOnClickListener(this);
				ll_color = (LinearLayout) colorView.findViewById(R.id.ll_image_broadcast_text_color);
			}
			viewStubTextColorInflate = true;
			if (ll_color != null && !ll_color.isShown()) {
				ll_color.setVisibility(View.VISIBLE);
			} else {
				ll_color.setVisibility(View.GONE);
			}
			break;
		case R.id.iv_broadcast_textcolor_black:
			ll_color.setVisibility(View.GONE);
			if (!getColorPrivilegeStatus()) {
				new PrivilegeLevelDialog(this).showDialog();
			} else {
				current_select_text_color = BROADCAST_TEXT_COLOR_BLACK;
				iv_select_color.setImageResource(R.drawable.write_icon_broadcastcolor_selector);
				edtContent.setTextColor(getResources().getColor(R.color.black));
				if (BAApplication.mLocalUserInfo != null) {
					SharedPreferencesTools.getInstance(this).saveLongKeyValue(BROADCAST_TEXT_COLOR_BLACK,
							SharedPreferencesTools.BROADCAST_TEXT_COLOR + BAApplication.mLocalUserInfo.uid.intValue());
				}
			}
			break;
		case R.id.iv_broadcast_textcolor_red:
			ll_color.setVisibility(View.GONE);
			if (!getColorPrivilegeStatus()) {
				new PrivilegeLevelDialog(this).showDialog();
			} else {
				current_select_text_color = BROADCAST_TEXT_COLOR_ONE;
				iv_select_color.setImageResource(R.drawable.write_icon_text_pr);
				edtContent.setTextColor(getResources().getColor(R.color.broadcast_text_color_one));
				if (BAApplication.mLocalUserInfo != null) {
					SharedPreferencesTools.getInstance(this).saveLongKeyValue(BROADCAST_TEXT_COLOR_ONE,
							SharedPreferencesTools.BROADCAST_TEXT_COLOR + BAApplication.mLocalUserInfo.uid.intValue());
				}
			}
			break;
		case R.id.iv_broadcast_textcolor_purple:
			ll_color.setVisibility(View.GONE);
			if (!getColorPrivilegeStatus()) {
				new PrivilegeLevelDialog(this).showDialog();
			} else {
				current_select_text_color = BROADCAST_TEXT_COLOR_TWO;
				iv_select_color.setImageResource(R.drawable.write_icon_text3);
				edtContent.setTextColor(getResources().getColor(R.color.broadcast_text_color_two));
				if (BAApplication.mLocalUserInfo != null) {
					SharedPreferencesTools.getInstance(this).saveLongKeyValue(BROADCAST_TEXT_COLOR_TWO,
							SharedPreferencesTools.BROADCAST_TEXT_COLOR + BAApplication.mLocalUserInfo.uid.intValue());
				}
			}
			break;
		case R.id.iv_broadcast_textcolor_blue:
			ll_color.setVisibility(View.GONE);
			if (!getColorPrivilegeStatus()) {
				new PrivilegeLevelDialog(this).showDialog();
			} else {
				current_select_text_color = BROADCAST_TEXT_COLOR_THREE;
				iv_select_color.setImageResource(R.drawable.write_icon_text4);

				edtContent.setTextColor(getResources().getColor(R.color.broadcast_text_color_three));
				if (BAApplication.mLocalUserInfo != null) {
					SharedPreferencesTools.getInstance(this).saveLongKeyValue(BROADCAST_TEXT_COLOR_THREE,
							SharedPreferencesTools.BROADCAST_TEXT_COLOR + BAApplication.mLocalUserInfo.uid.intValue());
				}
			}
			break;

		case R.id.iv_broadcast_follower:
			//			if (writeBiz.isUseMagic()) {
			//				if (writeBiz.getCurrentMagicValue() == writeBiz.MAGIC_ONE_VALUE || writeBiz.getCurrentMagicValue() == writeBiz.MAGIC_THREE_VALUE) {
			//
			//				}
			//			}
			hideSpecificPanel();
			if (ll_color != null)
				ll_color.setVisibility(View.GONE);
			if (userInfos != null && userInfos.size() >= 3) {
				BaseUtils.showTost(this, R.string.str_at_maxnum);
				return;
			}
			Intent intent = new Intent(this, BroadFollersActivity.class);
			intent.putExtra("ismagic", false);
			startActivityForResult(intent, REQUESTCODE);
			break;
		case R.id.title_lin_right:
			if (isSending) {
				return;
			}
			if (writeBiz.getType() == WriteBroadCastBiz.BROADCAST_TEXT_TYPE) {//文字广播
				if (canSend) {
					for (GoGirlUserInfo info : userInfos) {
						String alias = SharedPreferencesTools.getInstance(MineWriteBroadCastActivity.this,
								BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(info.uid.intValue());

						mText = mText.replace("@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias), "");//去掉@用户的内容
					}
					mText = mText.replace(touserName, "");//去掉@用户的内容
					mText = mText.replaceAll("\n", "").trim();
					isSending = writeBiz.sendTextBroadCast(userInfos, mText.getBytes(), current_select_text_color, false);
				} else {
					BaseUtils.showTost(this, R.string.str_long_lenth);
				}
			} else if (writeBiz.getType() == WriteBroadCastBiz.BROADCAST_VOICE_TYPE) {//语音广播
				if (canSendVoiceBroadcast) {
					File file = mVoiceRecod.sampleFile();
					byte[] voicedata = FileUtils.getBytesFromFile(file);

					if (voicedata != null) {
						isSending = writeBiz.sendVoiceBroadCast(voicedata, currentVoiceTimeLen);
					}
				} else {
					if (RECORD_INIT_STATUS == record_status) {
						BaseUtils.showTost(this, R.string.str_not_start_record);
					} else {
						BaseUtils.showTost(this, R.string.str_toast_is_recording);
					}
				}
			} else if (writeBiz.getType() == WriteBroadCastBiz.BROADCAST_SOLITAIRE_REDPACKET) {//猜拳广播
				/*****************************DYH 修改猜拳为大厅红包 start ************************/
				//				isSending = writeBiz.sendFingerBroadcast(this, BAConstants.PEIPEI_BROADCASET, fingerCurr, fingerNum, "", antetype);
				sendRedpacket();
				/*****************************DYH 修改猜拳为大厅红包 end ************************/
			}

			break;
		case R.id.btn_text_broadcast:
			llBottomLayout.setVisibility(View.VISIBLE);
			iv_select_color.setVisibility(View.VISIBLE);
			slipswithcTwo_MSL.setVisibility(View.GONE);
			slipswitch_MSL.setVisibility(View.VISIBLE);
			ll_tips.setVisibility(View.GONE);
			exitThisActivity(1);
			writeBiz.setDecree(false);
			writeBiz.setType(WriteBroadCastBiz.BROADCAST_TEXT_TYPE);
			writeBiz.setBroadcastType(WriteBroadCastBiz.COMMON_BROADCAST);
			slipswitch_MSL.setCurrentX(0);
			slipswitch_MSL.updateSwitchState(MySlipSwitch.SELECT_FIRST);
			break;
		case R.id.btn_voice_broadcast:
			restoreCommon();
			if (ll_color != null)
				ll_color.setVisibility(View.GONE);
			llBottomLayout.setVisibility(View.VISIBLE);
			slipswithcTwo_MSL.setVisibility(View.VISIBLE);
			slipswitch_MSL.setVisibility(View.GONE);
			iv_select_color.setVisibility(View.GONE);
			ll_content.setVisibility(View.VISIBLE);
			ll_finger.setVisibility(View.GONE);
			writeBiz.setDecree(false);
			writeBiz.setType(WriteBroadCastBiz.BROADCAST_VOICE_TYPE);
			writeBiz.setBroadcastType(WriteBroadCastBiz.COMMON_BROADCAST);
			slipswithcTwo_MSL.setCurrentX(0);
			slipswithcTwo_MSL.updateSwitchState(false);
			btn_text_broadcast.setBackgroundResource(R.drawable.broadcast_voice_bg);
			btn_voice_broadcast.setBackgroundResource(R.drawable.broadcast_voice_bg_pr);
			btn_text_broadcast.setTextColor(getResources().getColor(R.color.write_broadcast_text_color));
			btn_voice_broadcast.setTextColor(getResources().getColor(R.color.peach));
			btn_finger.setBackgroundResource(R.drawable.broadcast_voice_bg);
			btn_finger.setTextColor(getResources().getColor(R.color.write_broadcast_text_color));
			ll_voice_layout.setVisibility(View.VISIBLE);
			edtContent.setVisibility(View.GONE);
			ll_bottom.setVisibility(View.GONE);
			tvLimit.setVisibility(View.GONE);
			tvCoinCost.setVisibility(View.VISIBLE);
			ll_tips.setVisibility(View.GONE);
			hideEmojiPanel();
			hideSoftInput(edtContent);
			break;
		case R.id.btn_finger_broadcast:
			restoreCommon();
			if (ll_color != null)
				ll_color.setVisibility(View.GONE);
			llBottomLayout.setVisibility(View.GONE);
			writeBiz.setDecree(false);
			slipswithcTwo_MSL.setVisibility(View.GONE);
			slipswitch_MSL.setVisibility(View.GONE);
			iv_select_color.setVisibility(View.GONE);
			ll_content.setVisibility(View.GONE);
			ll_finger.setVisibility(View.VISIBLE);
			ll_tips.setVisibility(View.VISIBLE);
			//			writeBiz.setType(WriteBroadCastBiz.BROADCAST_FINGER_TYPE);
			writeBiz.setType(WriteBroadCastBiz.BROADCAST_SOLITAIRE_REDPACKET);
			btn_text_broadcast.setBackgroundResource(R.drawable.broadcast_voice_bg);
			btn_voice_broadcast.setBackgroundResource(R.drawable.broadcast_voice_bg);
			btn_text_broadcast.setTextColor(getResources().getColor(R.color.write_broadcast_text_color));
			btn_voice_broadcast.setTextColor(getResources().getColor(R.color.write_broadcast_text_color));
			btn_finger.setBackgroundResource(R.drawable.broadcast_voice_bg_pr);
			btn_finger.setTextColor(getResources().getColor(R.color.peach));
			tvLimit.setVisibility(View.GONE);
			ll_bottom.setVisibility(View.GONE);
			hideEmojiPanel();
			hideSoftInput(edtContent);
			break;
		case R.id.task_voice_broadcast:
			if (record_status == RECORD_INIT_STATUS) {//初始状态就开始录音
				if (!Environment.getExternalStorageDirectory().exists()) {
					BaseUtils.showTost(this, "SD卡不存在");
					return;
				}
				btn_rerecording.setVisibility(View.GONE);
				task_voice_progress.setmTotalProgress(BROADCAST_VOICE_MAX_LEN_TIME);//暂时设置最大可以录制60秒
				task_voice_progress.setProgress(timeLen);
				task_voice_progress.setBackgroundResource(R.drawable.task_braodcast_voice_stop_selector);
				mHandler.postDelayed(mSleepTask, 1000);
				startRecording(BROADCAST_FILE_TEMP_NAME);
				record_status = RECORD_START_STATUS;
			} else if (record_status == RECORD_START_STATUS) {//在录音状态，点击就停止
				btn_rerecording.setVisibility(View.VISIBLE);
				stopRecord();
				if (timeLen < 3) {
					canSendVoiceBroadcast = false;
					restoreInitRecordStatus();
					btn_rerecording.setVisibility(View.GONE);
					BaseUtils.showTost(this, "您录的时间太短了");
				} else {
					canSendVoiceBroadcast = true;
				}
				task_voice_progress.setProgress(0);
			} else if (record_status == RECORD_STOP_STATUS) {//停止状态点击就播放
				File file = mVoiceRecod.sampleFile();
				if (file == null) {
					return;
				}
				btn_rerecording.setVisibility(View.VISIBLE);
				record_status = RECORD_PLAY_STATUS;
				mVoiceRecod.startPlayback(file);
				task_voice_progress.setBackgroundResource(R.drawable.task_braodcast_voice_pause_selector);
				currentVoiceTimeLen = mVoiceRecod.getDuration() / 1000;
				timeLen = 0;
				mHandler.postDelayed(mSleepTask, 1000);
				task_voice_progress.setmTotalProgress(currentVoiceTimeLen);
				task_voice_progress.setProgress(timeLen);

			} else if (record_status == RECORD_PLAY_STATUS) {//播放状态点击就暂停
				btn_rerecording.setVisibility(View.VISIBLE);
				record_status = RECORD_PAUSE_STATUS;
				mHandler.removeCallbacks(mSleepTask);
				mVoiceRecod.pausePlayback();
				task_voice_progress.setBackgroundResource(R.drawable.task_braodcast_voice_start_selector);
			} else if (record_status == RECORD_PAUSE_STATUS) {//暂停状态点击播放
				btn_rerecording.setVisibility(View.VISIBLE);
				record_status = RECORD_PLAY_STATUS;
				if (currentVoiceTimeLen > 0) {
					float timeProgress = ((float) timeLen) / currentVoiceTimeLen;
					mHandler.postDelayed(mSleepTask, 1000);
					mVoiceRecod.startPlayback(timeProgress);
				}
				task_voice_progress.setBackgroundResource(R.drawable.task_braodcast_voice_pause_selector);
			}
			break;
		case R.id.btn_rerecording:
			btn_rerecording.setVisibility(View.GONE);
			mVoiceRecod.stopPlayback();
			mHandler.removeCallbacks(mSleepTask);
			restoreInitRecordStatus();
			break;
		case R.id.title_tv_left:
			exitThisActivity(0);
			break;
		/*****************************DYH 修改猜拳为大厅红包 start ************************/
		//		case R.id.broadcast_finger_100:
		//			fingerNum = 100;
		//			fingerNum100.setCheck(true);
		//			fingerNum1000.setCheck(false);
		//			fingerNum5000.setCheck(false);
		//			break;
		//		case R.id.broadcast_finger_1000:
		//			fingerNum = 1000;
		//			fingerNum100.setCheck(false);
		//			fingerNum1000.setCheck(true);
		//			fingerNum5000.setCheck(false);
		//			break;
		//		case R.id.broadcast_finger_5000:
		//			fingerNum = 5000;
		//			fingerNum100.setCheck(false);
		//			fingerNum1000.setCheck(false);
		//			fingerNum5000.setCheck(true);
		//			break;
		//		case R.id.broadcast_finger_stone:
		//			fingerCurr = 0;
		//			fingerStone.setCheck(true);
		//			fingerScissors.setCheck(false);
		//			fingerCloth.setCheck(false);
		//			break;
		//		case R.id.broadcast_finger_scissors:
		//			fingerCurr = 2;
		//			fingerStone.setCheck(false);
		//			fingerScissors.setCheck(true);
		//			fingerCloth.setCheck(false);
		//			break;
		//		case R.id.broadcast_finger_cloth:
		//			fingerCurr = 1;
		//			fingerStone.setCheck(false);
		//			fingerScissors.setCheck(false);
		//			fingerCloth.setCheck(true);
		//			break;
		//		case R.id.broadcast_finger_gold:
		//			antetype = 0;
		//			fingerGold.setBackgroundRes(0, R.drawable.message_select_goldaward, true);
		//			fingerSilver.setBackgroundRes(0, R.drawable.message_select_silveraward, false);
		//			fingerNum100.setBackgroundRes(0, R.drawable.message_select_gold1, fingerNum == 100);
		//			fingerNum1000.setBackgroundRes(0, R.drawable.message_select_gold2, fingerNum == 1000);
		//			fingerNum5000.setBackgroundRes(0, R.drawable.message_select_gold3, fingerNum == 5000);
		//			break;
		//		case R.id.broadcast_finger_silver:
		//			antetype = 1;
		//			fingerGold.setBackgroundRes(0, R.drawable.message_select_goldaward, false);
		//			fingerSilver.setBackgroundRes(0, R.drawable.message_select_silveraward, true);
		//			fingerNum100.setBackgroundRes(0, R.drawable.message_select_silver1, fingerNum == 100);
		//			fingerNum1000.setBackgroundRes(0, R.drawable.message_select_silver2, fingerNum == 1000);
		//			fingerNum5000.setBackgroundRes(0, R.drawable.message_select_silver3, fingerNum == 5000);
		//			break;
		case R.id.redpacket_money1:
			redpacket_money1.setCheck(true);
			redpacket_money2.setCheck(false);
			redpacket_money3.setCheck(false);
			setFirstTip(list);
			break;
		case R.id.redpacket_money2:
			redpacket_money1.setCheck(false);
			redpacket_money2.setCheck(true);
			redpacket_money3.setCheck(false);
			setSecondTip(list);
			break;
		case R.id.redpacket_money3:
			redpacket_money1.setCheck(false);
			redpacket_money2.setCheck(false);
			redpacket_money3.setCheck(true);
			setThirdTip(list);
			break;
		case R.id.tv_send:
			checkEnableSendRedpacket();
			break;
		/*****************************DYH 修改猜拳为大厅红包 end ************************/
		case R.id.iv_broadcast_redpacket:
			SendHallRedpacketActivity.openMineFaqActivity(this);
			break;
		default:
			break;

		}

	}
	
	private void checkEnableSendRedpacket(){
		redPacketBiz.requestIsSendHallRedpacket(0, this);
	}

	private void restoreCommon() {//TODO
		mText = "";
		edtContent.setText(mText);
		writeBiz.setUseMagic(false);
		if (specificAdapter != null)
			specificAdapter.setSelectCurrentPos(-1);
		hideSpecificPanel();
	}

	private void changeTextBroadCast() {
		ll_content.setVisibility(View.VISIBLE);
		ll_finger.setVisibility(View.GONE);
		writeBiz.setType(WriteBroadCastBiz.BROADCAST_TEXT_TYPE);
		btn_text_broadcast.setBackgroundResource(R.drawable.broadcast_voice_bg_pr);
		btn_voice_broadcast.setBackgroundResource(R.drawable.broadcast_voice_bg);
		btn_text_broadcast.setTextColor(getResources().getColor(R.color.peach));
		btn_voice_broadcast.setTextColor(getResources().getColor(R.color.write_broadcast_text_color));
		btn_finger.setBackgroundResource(R.drawable.broadcast_voice_bg);
		btn_finger.setTextColor(getResources().getColor(R.color.write_broadcast_text_color));
		ll_voice_layout.setVisibility(View.GONE);
		edtContent.setVisibility(View.VISIBLE);
		ll_bottom.setVisibility(View.VISIBLE);
		btn_rerecording.setVisibility(View.GONE);
		tvCoinCost.setVisibility(View.VISIBLE);
	}

	private void exitThisActivity(final int flag22) {
		if (record_status == RECORD_START_STATUS) {
			GiveUpVoiceDialog voiceDialog = new GiveUpVoiceDialog(this);
			voiceDialog.showDialog();
			voiceDialog.setCallback(new SureOnclick() {

				@Override
				public void sureOnclick() {
					if (flag22 == 0) {
						MineWriteBroadCastActivity.this.finish();
					} else {
						btn_rerecording.setVisibility(View.GONE);
						mHandler.removeCallbacks(mSleepTask);
						restoreInitRecordStatus();
						changeTextBroadCast();
					}
				}
			});
		} else {
			if (flag22 == 0) {
				this.finish();
			} else {
				changeTextBroadCast();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exitThisActivity(0);
		}
		return false;
	}

	private boolean canSendVoiceBroadcast = false;

	private void stopRecord() {
		mVoiceRecod.stopRecording();
		record_status = RECORD_STOP_STATUS;
		currentVoiceTimeLen = timeLen;
		mHandler.removeCallbacks(mSleepTask);
		task_voice_progress.setBackgroundResource(R.drawable.task_braodcast_voice_start_selector);
	}

	/**
	 * 显示表情面板 
	 * @author vactor
	 *
	 */
	private void showEmojiPanel() {
		if (ll_emoji != null && ll_emoji.getVisibility() == View.GONE && mTxtEmoj != null) {
			mTxtEmoj.setImageResource(R.drawable.message_icon_keyboard1_selector);
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Animation animation = AnimationUtils.loadAnimation(MineWriteBroadCastActivity.this, R.anim.emoji_panel_translate_begin);
					ll_emoji.setVisibility(View.VISIBLE);
					ll_emoji.startAnimation(animation);
				}
			}, 500);
		}
	}

	private void showSpecificPanel() {
		if (gv_specific.getVisibility() == View.GONE) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Animation animation = AnimationUtils.loadAnimation(MineWriteBroadCastActivity.this, R.anim.emoji_panel_translate_begin);
					gv_specific.setVisibility(View.VISIBLE);
					gv_specific.startAnimation(animation);
				}
			}, 500);
		}
	}

	private BroadcastReceiverMgr mBroadcastReceiver;

	public void registerIt() {//用于监听来电的广播
		mBroadcastReceiver = new BroadcastReceiverMgr(mHandler);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		intentFilter.setPriority(Integer.MAX_VALUE);
		registerReceiver(mBroadcastReceiver, intentFilter);
	}

	/**
	 * 隐藏表情面板 
	 * @author vator
	 *
	 */
	private void hideEmojiPanel() {
		if (ll_emoji != null && ll_emoji.getVisibility() == View.VISIBLE && mTxtEmoj != null) {
			mTxtEmoj.setImageResource(R.drawable.write_icon_emotion_selector);
			Animation animation = AnimationUtils.loadAnimation(MineWriteBroadCastActivity.this, R.anim.emoji_panel_translate_back);
			ll_emoji.setVisibility(View.GONE);
			ll_emoji.startAnimation(animation);
		}
	}

	private void hideSpecificPanel() {
		if (gv_specific.getVisibility() == View.VISIBLE) {
			Animation animation = AnimationUtils.loadAnimation(MineWriteBroadCastActivity.this, R.anim.emoji_panel_translate_back);
			gv_specific.setVisibility(View.GONE);
			gv_specific.startAnimation(animation);
		}
	}

	private WriteBroadCastBiz writeBiz;
	private Recorder mVoiceRecod;
	private RemainingTimeCalculator mRemainingTimeCalculator;

	@Override
	protected void initData() {
		mVoiceRecod = Recorder.getInstance();
		mVoiceRecod.setHandler(this, mHandler);
		mReceiver = new RecorderReceiver();
		mRemainingTimeCalculator = new RemainingTimeCalculator();
		IntentFilter filter = new IntentFilter();
		filter.addAction(RecorderService.RECORDER_SERVICE_BROADCAST_NAME);
		registerReceiver(mReceiver, filter);

		writeBiz = new WriteBroadCastBiz(this, mHandler);
		writeBiz.getWealth();//获取财富值
		if (!getColorPrivilegeStatus()) {
			writeBiz.getPrivilegeStatus(this, RequestGetPrivilegeStatus.BROADCAST_COLOR_TYPE);//先获取
		}
		redPacketBiz = new SolitaireRedPacketBiz();
		getRedPacketMoneyInfo();
		registerIt();
	}

	private void getRedPacketMoneyInfo() {
		redPacketBiz.reqeustSolitaireRedPacketMoney(1, this);
	}

	private void sendRedpacket() {
		redPacketBiz.requestSendSolitaireRedPacket(redId, BAConstants.PEIPEI_BROADCASET, 1, this);
	}

	private void setFirstTip(List<RedPacketBetInfo> list) {
		if (list != null && !list.isEmpty()) {
			RedPacketBetInfo info = (RedPacketBetInfo) list.get(0);
			if (info != null) {
				List<String> strList = divisionStr(new String(info.desc));
				setTips(strList);
			}

			redId = info.id.intValue();
		}
	}

	private void setSecondTip(List<RedPacketBetInfo> list) {
		if (list != null && list.size() > 1) {
			RedPacketBetInfo info = (RedPacketBetInfo) list.get(1);
			if (info != null) {
				List<String> strList = divisionStr(new String(info.desc));
				setTips(strList);
			}
			redId = info.id.intValue();
		}
	}

	private void setThirdTip(List<RedPacketBetInfo> tipList) {
		if (tipList != null && tipList.size() > 2) {
			RedPacketBetInfo info = (RedPacketBetInfo) list.get(2);
			if (info != null) {
				List<String> strList = divisionStr(new String(info.desc));
				setTips(strList);
			}
			redId = info.id.intValue();
		}
	}

	private void setTips(List<String> list) {
		if (list != null) {
			adapter.clearList();
			adapter.appendToList(list);
			adapter.notifyDataSetChanged();
			tips_list.setSelection(tips_list.getCount() - 1);
			//			sv_scrollview.smoothScrollTo(0, 0);

		}
	}

	private List<String> divisionStr(String str) {
		List<String> strList = new ArrayList<String>();
		if (!TextUtils.isEmpty(str) && str.contains("·")) {
			String[] strArr = str.split("·");
			if (strArr != null && strArr.length > 0) {
				List<String> list = Arrays.asList(strArr);
				strList.addAll(list);
			}
		}
		return strList;
	}

	private void setMoney(List<RedPacketBetInfo> list) {
		for (int i = 0; i < list.size(); i++) {
			if (i < views.size()) {
				views.get(i).setText(list.get(i).gold.intValue() + "");
			}
		}
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.str_broadcast);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_send_broadcast);
		tvRight = (TextView) findViewById(R.id.title_tv_right);
		findViewById(R.id.title_lin_right).setOnClickListener(this);
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setText(R.string.publish);

		findViewById(R.id.iv_broadcast_special_effects).setOnClickListener(this);

		edtContent = (EditText) findViewById(R.id.edt_broadcast_content);
		edtContent.setOnTouchListener(this);
		tvLimit = (TextView) findViewById(R.id.tv_limit);
		tvLimit.setText(edtContent.length() + "/" + BROADCAST_MAX_LENGTH);
		edtContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
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

				tempInfo = new ArrayList<GoGirlUserInfo>();

				int len = 0;
				if (str.startsWith(touserName)) {//以@开头
					len = strLen - touserName.length();
				} else {//非@或者删除了@对象
					if (!userInfos.isEmpty()) {//有@对象时
						String temp = touserName;//@字符
						String userStr = mText;//用户自己输入的字符
						String alias = "";
						String userNick;
						for (GoGirlUserInfo info : userInfos) {

							alias = SharedPreferencesTools.getInstance(MineWriteBroadCastActivity.this,
									BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(info.uid.intValue());
							userNick = TextUtils.isEmpty(alias) ? new String(info.nick) : alias;

							userStr = userStr.replace("@" + userNick, "");//获取用户输入的字符 
							if (!str.contains("@" + userNick)) {
								temp = temp.replace("@" + userNick, "");//删除被用户删掉的@对象
							} else {
								tempInfo.add(info);
					 		}
						}

						if (temp.equals(touserName)) {//@对象不变
							len = strLen;
						} else {
							touserName = temp;//重新赋值@对象
							str = touserName + userStr;//重新赋值整个文字
							len = strLen - touserName.length();
							userInfos = tempInfo;
							edtContent.setText(str);
							try {
								edtContent.setSelection(len);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						len = strLen;
					}

				}

				len = BROADCAST_MAX_LENGTH - len;
				if (len >= 0) {
					canSend = true;
					tvLimit.setTextColor(getResources().getColor(R.color.gray));
				} else {
					canSend = false;
					tvLimit.setTextColor(getResources().getColor(R.color.peach));
				}

				tvLimit.setText(String.valueOf(BROADCAST_MAX_LENGTH - len) + "/" + BROADCAST_MAX_LENGTH);
				mText = ParseMsgUtil.convertUnicode2(edtContent.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mTxtEmoj = (ImageView) findViewById(R.id.iv_broadcast_emoji);
		mTxtEmoj.setOnClickListener(this);
		findViewById(R.id.iv_broadcast_follower).setOnClickListener(this);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_writebroadcast_bottom);

		llBottomLayout = (LinearLayout) findViewById(R.id.ll_broadcast_bottom);

		tvCoinCost = (TextView) findViewById(R.id.tv_show_mind);

		ll_voice_layout = (LinearLayout) findViewById(R.id.ll_voice_broadcast);
		btn_text_broadcast = (Button) findViewById(R.id.btn_text_broadcast);
		btn_text_broadcast.setOnClickListener(this);
		btn_voice_broadcast = (Button) findViewById(R.id.btn_voice_broadcast);
		btn_voice_broadcast.setOnClickListener(this);
		task_voice_progress = (TasksCompletedView) findViewById(R.id.task_voice_broadcast);
		task_voice_progress.setOnClickListener(this);
		btn_finger = (Button) findViewById(R.id.btn_finger_broadcast);
		btn_finger.setOnClickListener(this);
		task_voice_progress.setProgress(0);
		tv_voice_time = (TextView) findViewById(R.id.tv_broadcast_time);
		btn_rerecording = (Button) findViewById(R.id.btn_rerecording);
		btn_rerecording.setOnClickListener(this);
		ll_content = (RelativeLayout) findViewById(R.id.ll_broadcase_content);
		ll_finger = (LinearLayout) findViewById(R.id.ll_broadcast_finger);
		/*****************************DYH 修改猜拳为大厅红包 start ************************/
		//		fingerNum100 = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_100);
		//		fingerNum1000 = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_1000);
		//		fingerNum5000 = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_5000);
		//		fingerStone = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_stone);
		//		fingerScissors = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_scissors);
		//		fingerCloth = (PeiPeiCheckButton) findViewById(R.id.broadcast_finger_cloth);
		//
		//		fingerGold = (PeiPeiCheckButton1) findViewById(R.id.broadcast_finger_gold);
		//		fingerSilver = (PeiPeiCheckButton1) findViewById(R.id.broadcast_finger_silver);
		tips_list = (PeiPeiListView) findViewById(R.id.tips_list);
		tv_send = (TextView) findViewById(R.id.tv_send);
		redpacket_money1 = (RedPacketCheckButton) findViewById(R.id.redpacket_money1);
		redpacket_money2 = (RedPacketCheckButton) findViewById(R.id.redpacket_money2);
		redpacket_money3 = (RedPacketCheckButton) findViewById(R.id.redpacket_money3);
		ll_tips = findViewById(R.id.ll_tips);
		redpacket_money1.setBackgroundRes(R.drawable.solitaire_redpacket_unselect, R.drawable.solitaire_redpacket_selected, true);
		redpacket_money2.setBackgroundRes(R.drawable.solitaire_redpacket_unselect, R.drawable.solitaire_redpacket_selected, false);
		redpacket_money3.setBackgroundRes(R.drawable.solitaire_redpacket_unselect, R.drawable.solitaire_redpacket_selected, false);
		adapter = new TipsAdapter(this);
		tips_list.setAdapter(adapter);
		views.add(redpacket_money1);
		views.add(redpacket_money2);
		views.add(redpacket_money3);
		
		findViewById(R.id.iv_broadcast_redpacket).setOnClickListener(this);
		/*****************************DYH 修改猜拳为大厅红包 end ************************/
//		mHandler.sendEmptyMessageAtTime(BROADCAST_INIT, 10);

		slipswitch_MSL = (MySlipSwitch) findViewById(R.id.myslipswitch);
//		slipswitch_MSL.setImageResource(R.drawable.broadcast_switch_bg, R.drawable.broadcast_switch_bg1, R.drawable.broadcast_switch_bg2,
//				R.drawable.broadcast_switch1);
		slipswitch_MSL.setImageResource(R.drawable.broadcast_switch_bg, R.drawable.broadcast_switch_bg1, R.drawable.broadcast_switch_bg2,
				R.drawable.broadcast_switch1);
		slipswitch_MSL.setIsSwitchOn(MySlipSwitch.SELECT_FIRST);
		slipswitch_MSL.setOnSwitchListener(this);

		slipswithcTwo_MSL = (MySlipSwitchTwo) findViewById(R.id.myslipswitch_two);
		slipswithcTwo_MSL.setImageResource(R.drawable.broadcast_voice1_switch_bg, R.drawable.broadcast_voice_switch_bg);
		slipswithcTwo_MSL.setSwitchState(false);
		slipswithcTwo_MSL.setOnSwitchListener(this);

		viewStub = (ViewStub) findViewById(R.id.viewstub_write_broadcast);
		imageColorStub = (ViewStub) findViewById(R.id.viewstub_select_text_color);
		iv_select_color = (ImageView) findViewById(R.id.iv_broadcast_color);
		iv_select_color.setOnClickListener(this);

		if (BAApplication.mLocalUserInfo != null) {
			long colorValue = SharedPreferencesTools.getInstance(this).getLongKeyValue(
					SharedPreferencesTools.BROADCAST_TEXT_COLOR + BAApplication.mLocalUserInfo.uid.intValue());
			if (colorValue == BROADCAST_TEXT_COLOR_ONE) {
				current_select_text_color = BROADCAST_TEXT_COLOR_ONE;
				edtContent.setTextColor(getResources().getColor(R.color.broadcast_text_color_one));
				iv_select_color.setImageResource(R.drawable.write_icon_text_pr);
			} else if (colorValue == BROADCAST_TEXT_COLOR_TWO) {
				current_select_text_color = BROADCAST_TEXT_COLOR_TWO;
				edtContent.setTextColor(getResources().getColor(R.color.broadcast_text_color_two));
				iv_select_color.setImageResource(R.drawable.write_icon_text3);
			} else if (colorValue == BROADCAST_TEXT_COLOR_THREE) {
				current_select_text_color = BROADCAST_TEXT_COLOR_THREE;
				edtContent.setTextColor(getResources().getColor(R.color.broadcast_text_color_three));
				iv_select_color.setImageResource(R.drawable.write_icon_text4);
			} else {
				current_select_text_color = BROADCAST_TEXT_COLOR_BLACK;
				edtContent.setTextColor(getResources().getColor(R.color.black));
				iv_select_color.setImageResource(R.drawable.write_icon_broadcastcolor_selector);
			}
		}
		gv_specific = (GridView) findViewById(R.id.gv_specific);
		specificAdapter = new WriteBroadSpecificAdapter(this);
		gv_specific.setAdapter(specificAdapter);
		gv_specific.setOnItemClickListener(this);
		setListener();
	}

	private void setListener() {
		tv_send.setOnClickListener(this);
		redpacket_money1.setOnClickListener(this);
		redpacket_money2.setOnClickListener(this);
		redpacket_money3.setOnClickListener(this);
	}

	private WriteBroadSpecificAdapter specificAdapter;

	@Override
	protected int initView() {
		return R.layout.activity_write_broadcast;
	}

	/*
	* Make sure we're not recording music playing in the background, ask the
	* MediaPlaybackService to pause playback.
	*/
	private void stopAudioPlayback() {
		// Shamelessly copied from MediaPlaybackService.java, which
		// should be public, but isn't.
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "pause");
		sendBroadcast(i);
	}

	public static final int BITRATE_3GPP = 20 * 1024 * 8; // bits/sec
	private static final String FILE_EXTENSION_3GPP = ".3gpp";
	private static final String FILE_EXTENSION_AMR = ".amr";
	private RecorderReceiver mReceiver;

	private void startRecording(String fileName) {
		mVoiceRecod.setShow(false);
		mRemainingTimeCalculator.reset();
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			//			mErrorUiMessage = getResources().getString(R.string.insert_sd_card);
		} else if (!mRemainingTimeCalculator.diskSpaceAvailable()) {
			//			mErrorUiMessage = getResources().getString(R.string.storage_is_full);
		} else {
			stopAudioPlayback();

			boolean isHighQuality = SoundRecorderPreferenceActivity.isHighQuality(this);

			// HACKME: for HD2, there is an issue with high quality 3gpp
			// use low quality instead
			if (Build.MODEL.equals("HTC HD2")) {
				isHighQuality = false;
			}
			if (android.os.Build.VERSION.SDK_INT > 9) {
				mRemainingTimeCalculator.setBitRate(BAConstants.BITRATE_AMR);
				mVoiceRecod.startRecording(MediaRecorder.OutputFormat.AMR_WB, fileName, FILE_EXTENSION_AMR, isHighQuality, -1);
			} else {
				mRemainingTimeCalculator.setBitRate(BITRATE_3GPP);
				mVoiceRecod.startRecording(MediaRecorder.OutputFormat.THREE_GPP, fileName, FILE_EXTENSION_3GPP, isHighQuality, -1);
			}
		}
	}

	int timeLen = 0;
	private Runnable mSleepTask = new Runnable() {//更新录语音的时间
		public void run() {
			timeLen++;
			mHandler.sendEmptyMessage(HandlerValue.BROADCAST_TIME_LENGTH_VALUE);
			mHandler.postDelayed(mSleepTask, 1000);
		}
	};

	private class RecorderReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_STATE)) {
				boolean isRecording = intent.getBooleanExtra(RecorderService.RECORDER_SERVICE_BROADCAST_STATE, false);
				mVoiceRecod.setState(isRecording ? Recorder.RECORDING_STATE : Recorder.IDLE_STATE);
			} else if (intent.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_ERROR)) {
				int error = intent.getIntExtra(RecorderService.RECORDER_SERVICE_BROADCAST_ERROR, 0);
				mVoiceRecod.setError(error);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (!mVoiceRecod.isShow())
			mVoiceRecod.stopPlayback();
		if (mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
		}
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == REQUESTCODE && null != arg2) {
			String result = arg2.getStringExtra(STR_GOGIRLUSERINFO);
			if (!TextUtils.isEmpty(result) && userInfos.size() < 3) {
				GoGirlUserInfo info = GoGirlUserJson.getGoGirlUserInfo(result);
				if (writeBiz.isUseMagic()
						&& (writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_ONE_VALUE || writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_THREE_VALUE)) {
					userInfos.clear();
					userInfos.add(info);
				} else {
					for (GoGirlUserInfo goGirlUserInfo : userInfos) {
						if (goGirlUserInfo.uid.intValue() == info.uid.intValue()) {
							return;
						}
					}
					userInfos.add(info);
				}

				String alias = SharedPreferencesTools.getInstance(MineWriteBroadCastActivity.this,
						BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(info.uid.intValue());

				if (writeBiz.isUseMagic()) {
					touserName = "";
				}
				String tempStr = touserName + "@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias);
				String edtStr = edtContent.getText().toString();
				edtStr = edtStr.replace(touserName, "");
				touserName = tempStr;
				mText = touserName + edtStr;
				if (writeBiz.isUseMagic()) {
					if (writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_ONE_VALUE) {
						mText = "@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias) + STR_MAGIC_STAR;
					} else if (writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_THREE_VALUE) {
						mText = "@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias) + STR_MAGIC_FEATHER;
					} else if (writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_FIVE_VALUE) {
						mText = "@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias) + STR_MAGIC_FIVE;
					} else if (writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_SIX_VALUE) {
						mText = "@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias) + STR_MAGIC_SIX;
					} else if (writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_SEVEN_VALUE) {
						mText = "@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias) + STR_MAGIC_SEVEN;
					} else if (writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_EIGHT_VALUE) {
						mText = "@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias) + STR_MAGIC_EIGHT;
					} else if (writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_NINE_VALUE) {
						mText = "@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias) + STR_MAGIC_NINE;
					} else if (writeBiz.getCurrentMagicValue() == WriteBroadCastBiz.MAGIC_TEN_VALUE) {
						mText = "@" + (TextUtils.isEmpty(alias) ? new String(info.nick) : alias) + STR_MAGIC_TEN;
					}

				}
				edtContent.setText(mText);

				edtContent.setSelection(mText.length());
			} else {
				mText = "";
				edtContent.setText(mText);
			}
		} else if(arg1 == SendHallRedpacketActivity.REQUEST_CODE){
			finish();
		}

	}

	@Override
	public void onSwitched(int isSwitchOn) {//文字广播三个tab的切换按钮

		if (isSwitchOn == MySlipSwitch.SELECT_FIRST) {
			if (writeBiz.isUseMagic()) {
				slipswitch_MSL.setCurrentX(1);
				slipswitch_MSL.updateSwitchState(MySlipSwitch.SELECT_SECOND);
			} else {
				writeBiz.setDecree(false);
				writeBiz.setBroadcastType(WriteBroadCastBiz.COMMON_BROADCAST);
			}
		} else if (isSwitchOn == MySlipSwitch.SELECT_SECOND) {
			writeBiz.setDecree(false);
			writeBiz.setBroadcastType(WriteBroadCastBiz.TOP_BRAODCAST);
		} else {
			if (writeBiz.isUseMagic()) {
				slipswitch_MSL.setCurrentX(1);
				slipswitch_MSL.updateSwitchState(MySlipSwitch.SELECT_SECOND);
			} else {
				writeBiz.getPrivilegeStatus(this, RequestGetPrivilegeStatus.BROADCAST_DECREE_TYPE);//选择获取
			}
		}
	}

	@Override
	public void onSwitched(boolean isSwitchOn) {//语音里面两个切换的按钮
		writeBiz.setType(WriteBroadCastBiz.BROADCAST_VOICE_TYPE);
		writeBiz.setDecree(false);
		if (!isSwitchOn) {
			writeBiz.setBroadcastType(WriteBroadCastBiz.COMMON_BROADCAST);
		} else {
			writeBiz.setBroadcastType(WriteBroadCastBiz.TOP_BRAODCAST);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//点击特效的
		hideEmojiPanel();
		String str = (String) arg0.getAdapter().getItem(arg2);
		if (!strLists.contains(str)) {//等级不够
			new PrivilegeLevelDialog(this).showDialog();
			return;
		}
		specificAdapter.setSelectCurrentPos(arg2);
		if (str.equals("流星雨")) {//流星雨
			if (specificAdapter.isSelect(arg2)) {
				Intent intent = new Intent(this, BroadFollersActivity.class);
				intent.putExtra("ismagic", true);
				startActivityForResult(intent, REQUESTCODE);
			}
			userMagic(arg2, WriteBroadCastBiz.MAGIC_ONE_VALUE);

		} else if (str.equals("万箭阵")) {//万箭阵
			mText = STR_MAGIC_ARROW;
			edtContent.setText(mText);
			userMagic(arg2, WriteBroadCastBiz.MAGIC_TWO_VALUE);
		} else if (str.equals("鸿毛雨")) {//鸿毛雨
			if (specificAdapter.isSelect(arg2)) {
				Intent intent = new Intent(this, BroadFollersActivity.class);
				startActivityForResult(intent, REQUESTCODE);
			}
			userMagic(arg2, WriteBroadCastBiz.MAGIC_THREE_VALUE);
		} else if (str.equals("玫瑰花语")) {//玫瑰花语
			mText = STR_MAGIC_ROSE;
			edtContent.setText(mText);
			userMagic(arg2, WriteBroadCastBiz.MAGIC_FOUR_VALUE);

		} else if (str.equals("一箭钟情")) {
			if (specificAdapter.isSelect(arg2)) {
				Intent intent = new Intent(this, BroadFollersActivity.class);
				intent.putExtra("ismagic", true);
				startActivityForResult(intent, REQUESTCODE);
			}
			userMagic(arg2, WriteBroadCastBiz.MAGIC_FIVE_VALUE);
		} else if (str.equals("变变变")) {
			if (specificAdapter.isSelect(arg2)) {
				Intent intent = new Intent(this, BroadFollersActivity.class);
				intent.putExtra("ismagic", true);
				startActivityForResult(intent, REQUESTCODE);
			}
			userMagic(arg2, WriteBroadCastBiz.MAGIC_SIX_VALUE);
		} else if (str.equals("真爱永恒")) {
			if (specificAdapter.isSelect(arg2)) {
				Intent intent = new Intent(this, BroadFollersActivity.class);
				intent.putExtra("ismagic", true);
				startActivityForResult(intent, REQUESTCODE);
			}
			userMagic(arg2, WriteBroadCastBiz.MAGIC_SEVEN_VALUE);
		} else if (str.equals("烈焰红唇")) {
			if (specificAdapter.isSelect(arg2)) {
				Intent intent = new Intent(this, BroadFollersActivity.class);
				intent.putExtra("ismagic", true);
				startActivityForResult(intent, REQUESTCODE);
			}
			userMagic(arg2, WriteBroadCastBiz.MAGIC_EIGHT_VALUE);
		} else if (str.equals("天马流星拳")) {
			if (specificAdapter.isSelect(arg2)) {
				Intent intent = new Intent(this, BroadFollersActivity.class);
				intent.putExtra("ismagic", true);
				startActivityForResult(intent, REQUESTCODE);
			}
			userMagic(arg2, WriteBroadCastBiz.MAGIC_NINE_VALUE);
		} else if (str.equals("甜蜜热气球")) {
			if (specificAdapter.isSelect(arg2)) {
				Intent intent = new Intent(this, BroadFollersActivity.class);
				intent.putExtra("ismagic", true);
				startActivityForResult(intent, REQUESTCODE);
			}
			userMagic(arg2, WriteBroadCastBiz.MAGIC_TEN_VALUE);
		}
	}

	private void userMagic(int pos, int magicValue) {
		writeBiz.setDecree(false);
		if (!specificAdapter.isSelect(pos)) {
			writeBiz.setUseMagic(false);
			slipswitch_MSL.setCurrentX(0);
			writeBiz.setType(WriteBroadCastBiz.BROADCAST_TEXT_TYPE);
			writeBiz.setBroadcastType(WriteBroadCastBiz.COMMON_BROADCAST);
			slipswitch_MSL.updateSwitchState(MySlipSwitch.SELECT_FIRST);
		} else {
			writeBiz.setUseMagic(true);
			writeBiz.setCurrentMagicValue(magicValue);
			writeBiz.setType(WriteBroadCastBiz.BROADCAST_TEXT_TYPE);
			writeBiz.setBroadcastType(WriteBroadCastBiz.TOP_BRAODCAST);
			slipswitch_MSL.setCurrentX(1);
			slipswitch_MSL.updateSwitchState(MySlipSwitch.SELECT_SECOND);
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		hideEmojiPanel();
		hideSpecificPanel();
		return false;
	}

	@Override
	public void sureOnClick() {
		Intent intent = new Intent(this, BroadFollersActivity.class);
		intent.putExtra("ismagic", true);
		startActivityForResult(intent, REQUESTCODE);

	}

	@Override
	public void onSolitaireRedPacketMoneySuccess(int code, int isOpen, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.HALL_SOLITAIRE_REDPACKET_INFO_SUCCESS, code, obj);
	}

	@Override
	public void onSolitaireRedPacketMoneyError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.HALL_SOLITAIRE_REDPACKET_INFO_ERROR, code);
	}

	@Override
	public void onSendSolitaireRedpacketSuccess(int code, Object obj) {

	}

	@Override
	public void onSendSolitaireRedpacketError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.HALL_SEND_SOLITAIRE_REDPACKET_ERROR, code);
	}

	@Override
	public void onSendHallSolitaireRedpacketSuccess(int code, Object obj, String errMsg) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_SEND_SOLITAIRE_REDPACKET_SUCCESS, code, obj, errMsg);
	}

	@Override
	public void getIsSendHallRedpacketOnSuccess(int code, int obj, String retMsg) {
		
	}

	@Override
	public void getIsSendHallSolitaireRedpacketOnSuccess(int code, int obj, String retMsg) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_ENABLE_SEND_SOLITAIRE_REDPACKET_SUCCESS, code, obj, retMsg);
	}

	@Override
	public void getIsSendHallRedpacketOnError(int code) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_ENABLE_SEND_REDPACKET_ERROR, code);
	}
}
