package com.tshang.peipei.activity.show;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.adapter.BroadCastEmotionViewAdd;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.FinshShowDialog;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.dialog.ShareShowDialog;
import com.tshang.peipei.activity.dialog.ShowSendBroadcastDialog;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.show.adapter.ShowBottomAdapter;
import com.tshang.peipei.activity.show.adapter.ShowChatAdapter;
import com.tshang.peipei.activity.show.adapter.ShowGiftAdapter;
import com.tshang.peipei.activity.show.adapter.ShowOwnerChatAdapter;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.InOutAct;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAConstants.ProtobufErrorCode;
import com.tshang.peipei.base.babase.BAConstants.ShowMemberType;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatVoiceBiz;
import com.tshang.peipei.model.biz.store.StoreUserBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserProperty;
import com.tshang.peipei.model.entity.ShowChatEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestDeliverGift;
import com.tshang.peipei.model.request.RequestDeliverGift.IDeliverGiftCallBack;
import com.tshang.peipei.model.request.RequestLoadingPic;
import com.tshang.peipei.model.request.RequestLoadingPic.ILoadingPicHttp;
import com.tshang.peipei.model.showrooms.RoomsGetBiz;
import com.tshang.peipei.model.showrooms.ShowGiftNumUtils;
import com.tshang.peipei.model.showrooms.ShowsRoomDbBiz;
import com.tshang.peipei.model.viewdatacache.GiftListCacheViewData;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil.ConfigCacheModel;
import com.tshang.peipei.protocol.Gogirl.GiftDealInfoP;
import com.tshang.peipei.protocol.Gogirl.GiftInfoP;
import com.tshang.peipei.protocol.Gogirl.GoGirlChatDataP;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.ShowRoomLatestStatus;
import com.tshang.peipei.protocol.asn.gogirl.ShowRoomRoleChangeInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder.OnStateChangedListener;
import com.tshang.peipei.vender.micode.soundrecorder.RecorderReceiver;
import com.tshang.peipei.vender.micode.soundrecorder.RecorderService;
import com.tshang.peipei.vender.micode.soundrecorder.RemainingTimeCalculator;
import com.tshang.peipei.view.PageControlView;

/**
 * @Title: PeipeiShowActivity.java 
 *
 * @Description: 秀场界面 
 *
 * @author allen  
 *
 * @date 2015-1-15 上午10:28:34 
 *
 * @version V1.0   
 */
public class PeipeiShowActivity extends BaseActivity implements OnTouchListener, OnStateChangedListener, IDeliverGiftCallBack,
		BizCallBackGetUserProperty, ILoadingPicHttp {

	//语音
	public static final int RECORD_INIT_STATUS = 0;//初始状态
	public static final int RECORD_START_STATUS = 1;//录音
	public static final int RECORD_PLAY_STATUS = 2;//播放
	public static final int RECORD_PAUSE_STATUS = 3;//暂停
	public static final int RECORD_STOP_STATUS = 4;//停止
	public static final int RECORD_DOWNLOAD_STATUS = 5;//下载
	private int record_status = RECORD_INIT_STATUS;

	private ShowChatAdapter showChatAdapter;
	private ShowOwnerChatAdapter showOwnerChatAdapter;

	private LinearLayout showTitleLayout;
	private Animation menuShowAnim;

	private ListView mainListView;
	private ListView chatListView;
	private PopupWindow popWindow;
	private PopupWindow popWindowGift;

	private int showRoomId;
	private int roomUid;

	private ImageButton mChatPuls;
	private EditText mChatEditText;

	private ShowRoomInfo showRoomInfo;

	private TextView tvTitle;
	private TextView tvPraiseNum;
	private TextView tvGiftNum;
	private TextView tvMemberNum;
	private TextView tvBroadcast;
	private LinearLayout llSendLayout;
	private LinearLayout llSendText;
	private LinearLayout llSendVoice;
	private LinearLayout llSetGiftNum;
	private Button btnSendVoice;

	public ArrayList<ShowChatEntity> listChata = new ArrayList<ShowChatEntity>();
	public ArrayList<ShowChatEntity> listOwnerChata = new ArrayList<ShowChatEntity>();

	protected ViewStub vs_record;
	protected boolean isInflateRecord = false;
	protected LinearLayout ll_record;
	protected TextView tv_chat_record_time;
	protected ImageView iv_chat_record;

	private String mRequestedType = "audio/amr";
	private RecorderReceiver mReceiver;
	private boolean mStopUiUpdate;
	private int mAudioLenght = 0;
	private BroadcastReceiver mSDCardMountEventReceiver;
	private RemainingTimeCalculator mRemainingTimeCalculator;
	public static int MAX_TIME = 60; // 最长录制时间，单位秒，0为无时间限制
	public final String AUDIO_FILE_NAME = "w_show_voice_temp_";
	private AnimationDrawable mAnimationDrawable;
	private boolean mIsVoiceSent = false;

	protected ViewStub vs_gift;
	protected boolean isInflateGift = false;
	protected GridView gvGift;
	protected LinearLayout ll_showroom_gift;
	private TextView tvSendGiftNum;
	private RoomsGetBiz roomsGetBiz;
	private ShowsRoomDbBiz showsRoomDbBiz;

	private GiftInfoList giftList;
	private ShowGiftAdapter giftAdapter;

	private int curPosition = 0;

	private ViewStub vs_emotion;
	private boolean ViewStubIsInflate = false;
	private LinearLayout ll_emoji;
	private ViewPager mEmojiPager;
	private PageControlView pageControlView;
	private ImageView iv_common_face;
	private ImageView iv_emoji_face;

	private EditText edGiftNum;

	private ImageView ivHeatAdd;
	//	private long addHotTime;
	private ImageView ivHeatDot;
	private boolean isAddHot;
	private LinearLayout ll_left;

	protected DisplayImageOptions options_uid_head;//通过UID加载

	private ViewStub vs_show_animation;
	private boolean isLoadShowAnimationLayout = false;
	private LinearLayout rl_anim_one;
	private ImageView iv_anim_one;
	private Animation animFirst;
	private Animation animSecond;
	private Animation animImgFirst;
	private Animation animImgSecond;
	private LinearLayout rl_anim_two;
	private ImageView iv_anim_two;
	private TextView tv_anim_name_one;
	private TextView tv_anim_name_two;
	private TextView tv_anim_count_one;
	private TextView tv_anim_count_two;
	private ImageView iv_first_hundred;
	private ImageView iv_first_ten;
	private ImageView iv_first_one;
	private ImageView iv_second_hundred;
	private ImageView iv_second_ten;
	private ImageView iv_second_one;
	private RelativeLayout rl_animation_first;
	private RelativeLayout rl_animation_second;
	private ImageView iv_gift_one;
	private ImageView iv_gift_two;
	private ArrayList<ShowChatEntity> giftAnimationLists = new ArrayList<ShowChatEntity>();//收到礼物的连刷动画
	private DisplayImageOptions giftOptions;

	private int roomRole = 0;
	private int giftNum;
	private int giftGold;
	private int giftSilver;
	private int mGoldNum;
	private int mSilverNum;
	private boolean isCanSend = true;

	private GridView mBottomGridView;

	protected ViewStub vs_openbox;
	protected boolean isOpenBox = false;
	protected ImageView iv_openbox;
	protected ImageView iv_openbox_result;
	protected RelativeLayout ll_openbox;
	private TextView tv_openboxName;
	private ImageView iv_box_tips;

	private IWXAPI mWXapi;
	private Tencent mTencent;
	
	private Dialog dismissDialog;

	@Override
	protected void initData() {
		if (mWXapi == null) {
			new Thread() {
				public void run() {
					reqToWX();
				}
			}.start();
		}

		if (mTencent == null)
			mTencent = Tencent.createInstance(BAConstants.QQ_APP_KEY, this);

		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(this);
		giftOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).build();

		roomsGetBiz = new RoomsGetBiz(this, mHandler);
		showsRoomDbBiz = new ShowsRoomDbBiz(this);

		BAApplication.addHotTime = SharedPreferencesTools.getInstance(this).getLongKeyValue(BAConstants.SHOW_ADDHOT);

		if (System.currentTimeMillis() - BAApplication.addHotTime < 20000) {
			ivHeatAdd.setImageResource(R.drawable.casino_btn_praise_dis1);
		} else if (System.currentTimeMillis() - BAApplication.addHotTime < 40000) {
			ivHeatAdd.setImageResource(R.drawable.casino_btn_praise_dis2);
		} else if (System.currentTimeMillis() - BAApplication.addHotTime < 60000) {
			ivHeatAdd.setImageResource(R.drawable.casino_btn_praise_dis3);
		} else {
			ivHeatAdd.setImageResource(R.drawable.casino_btn_praise_un);
		}
		mHandler.sendEmptyMessageDelayed(HandlerValue.SHOW_ROOM_HOT_TIME, 1000);

		if (BAApplication.showRoomInfo != null) {
			showRoomInfo = BAApplication.showRoomInfo;

			ConfigCacheUtil.setUrlCache(this, showRoomInfo.toByteArray(), "roominfo" + BAApplication.mLocalUserInfo.uid.intValue());
			showRoomId = showRoomInfo.getRoomid();
			roomUid = showRoomInfo.getOwneruserinfo().getUid();
			roomsGetBiz.getSingleRoomInfo(showRoomId, roomUid);

			mainListView.setVisibility(View.VISIBLE);

			tvTitle.setText(new String(showRoomInfo.getOwneruserinfo().getNick().toByteArray()) + "的秀场");
			tvPraiseNum.setText(showRoomInfo.getHotnum() + "");
			tvGiftNum.setText(showRoomInfo.getGiftnum() + "");
			tvMemberNum.setText(showRoomInfo.getCurmembernum() + "/" + showRoomInfo.getMaxmembernum() + " 成员");

			if (showChatAdapter.getCount() == 0) {
				roomsGetBiz.getShowHistorData(showRoomId, roomUid, -1, 100, 1);//评论
			}

			giftAdapter = new ShowGiftAdapter(this);
			File cacheFile = ConfigCacheUtil.getCacheFile(this, GiftListCacheViewData.File_SHOW_NAME_START);//读取缓存数据
			if (cacheFile == null) {
				roomsGetBiz.getGiftShowList(0, 100);//礼物列表
			} else {
				GiftInfoList lists = GiftListCacheViewData.getSendGiftListCacheData(this, GiftListCacheViewData.File_SHOW_NAME_START);
				if (lists != null && !lists.isEmpty()) {
					if (!ConfigCacheUtil.getUrlCacheisEffective(cacheFile, ConfigCacheModel.CONFIG_CACHE_MODEL_ML)) {
						roomsGetBiz.getGiftShowList(0, 100);//礼物列表
					}
					giftList = lists;
				} else {
					roomsGetBiz.getGiftShowList(0, 100);//礼物列表
				}
			}

			BAApplication.mVoiceRecod.setHandler(this, mHandler);
			BAApplication.mVoiceRecod.setShow(true);
			BAApplication.mVoiceRecod.setOnStateChangedListener(this);

			setResult(RESULT_CANCELED);

			mReceiver = new RecorderReceiver(BAApplication.mVoiceRecod);
			mRemainingTimeCalculator = new RemainingTimeCalculator();

			setVolumeControlStream(AudioManager.STREAM_MUSIC);

			imageLoader.loadImage("http://" + showRoomInfo.getOwneruserinfo().getUid() + BAConstants.LOAD_HEAD_UID_APPENDSTR,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {}

						@SuppressWarnings("deprecation")
						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							Bitmap loadedImage = imageLoader.loadImageSync("drawable://" + R.drawable.logo);
							if (loadedImage != null) {
								loadedImage = BaseBitmap.BoxBlurFilter(loadedImage);
								if (loadedImage != null) {
									mainListView.setBackgroundDrawable(new BitmapDrawable(loadedImage));
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
									mainListView.setBackgroundDrawable(new BitmapDrawable(loadedImage));
								}
							}

						}

						@Override
						public void onLoadingCancelled(String imageUri, View view) {}
					});

//			if (PeipeiFloatingService.mFloatView != null) {
//				imageLoader.displayImage("http://" + showRoomInfo.getOwneruserinfo().getUid() + BAConstants.LOAD_HEAD_UID_APPENDSTR,
//						PeipeiFloatingService.mFloatView, options_uid_head);
//			}

			StoreUserBiz.getInstance().getUserProperty(this, BAApplication.mLocalUserInfo.uid.intValue(), this);
		} else {
			BaseUtils.showTost(this, "进入秀场失败，请重试");
			BAApplication.clearShow();
			finish();
		}
	}

	@Override
	protected void initRecourse() {
		findViewById(R.id.show_title_back_tv).setOnClickListener(this);
		findViewById(R.id.show_title_more).setOnClickListener(this);
		mainListView = (ListView) findViewById(R.id.show_listview_main);
		mainListView.setOnTouchListener(this);
		chatListView = (ListView) findViewById(R.id.show_listview_chat);
		chatListView.setOnTouchListener(this);

		tvTitle = (TextView) findViewById(R.id.show_title_tv_mid);
		tvPraiseNum = (TextView) findViewById(R.id.show_praise_text);
		tvGiftNum = (TextView) findViewById(R.id.show_gift_text);
		tvMemberNum = (TextView) findViewById(R.id.show_member_text);
		tvMemberNum.setOnClickListener(this);
		tvBroadcast = (TextView) findViewById(R.id.show_horn_text);

		showTitleLayout = (LinearLayout) findViewById(R.id.show_title_layout);
		btnSendVoice = (Button) findViewById(R.id.show_send_voice_btn);
		btnSendVoice.setOnTouchListener(this);

		mHandler.sendEmptyMessageDelayed(HandlerValue.SHOW_TITLE_HINT, 2000);

		mChatPuls = (ImageButton) findViewById(R.id.ibtn_show_plus);
		mChatPuls.setOnClickListener(this);

		mChatEditText = (EditText) findViewById(R.id.et_show_text);
		mChatEditText.setOnTouchListener(this);
		mChatEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.isEmpty(s)) {
					mChatPuls.setImageResource(R.drawable.message_btn_icon_gift);
				} else {
					mChatPuls.setImageResource(R.drawable.message_btn_send1_selector);
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

				if (strLen > 40) {
					isCanSend = false;
				} else {
					isCanSend = true;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {}
		});
		vs_record = (ViewStub) findViewById(R.id.viewstub_show_record);
		vs_emotion = (ViewStub) findViewById(R.id.viewstub_show_emotion);
		vs_gift = (ViewStub) findViewById(R.id.viewstub_show_gift);
		vs_openbox = (ViewStub) findViewById(R.id.viewstub_show_baohe);

		llSendLayout = (LinearLayout) findViewById(R.id.ll_show_send_text);
		llSendText = (LinearLayout) findViewById(R.id.ll_show_text);
		llSendVoice = (LinearLayout) findViewById(R.id.ll_show_send_voice);
		llSetGiftNum = (LinearLayout) findViewById(R.id.ll_show_set_gift_num);

		if (showChatAdapter == null) {
			showChatAdapter = new ShowChatAdapter(this);
			showChatAdapter.setList(listChata);
		}
		chatListView.setAdapter(showChatAdapter);
		chatListView.setSelection(showChatAdapter.getCount() - 1);

		if (showOwnerChatAdapter == null) {
			showOwnerChatAdapter = new ShowOwnerChatAdapter(this, mHandler);
			showOwnerChatAdapter.setList(listOwnerChata);
		}

		View headText = LayoutInflater.from(this).inflate(R.layout.head_empty_text2, null);
		mainListView.addHeaderView(headText);

		mainListView.setAdapter(showOwnerChatAdapter);

		findViewById(R.id.ibtn_show_emotion).setOnClickListener(this);
		findViewById(R.id.et_show_gift_btn).setOnClickListener(this);
		findViewById(R.id.ibtn_show_left_plus).setOnClickListener(this);
		edGiftNum = (EditText) findViewById(R.id.et_show_gift_num);

		ivHeatAdd = (ImageView) findViewById(R.id.show_heat_add_hot);
		ivHeatAdd.setOnClickListener(this);
		ll_left = (LinearLayout) findViewById(R.id.show_left_layout);

		vs_show_animation = (ViewStub) findViewById(R.id.vs_show_animation);

		animImgFirst = AnimationUtils.loadAnimation(this, R.anim.right_translate_in);
		animImgSecond = AnimationUtils.loadAnimation(this, R.anim.right_translate_in);
		animFirst = AnimationUtils.loadAnimation(this, R.anim.left_in);
		animSecond = AnimationUtils.loadAnimation(this, R.anim.left_in);

		ivHeatDot = (ImageView) findViewById(R.id.show_heat_add_hot_dot);

		mBottomGridView = (GridView) findViewById(R.id.gv_show_plus_select);
		findViewById(R.id.show_gift_casino).setOnClickListener(this);
		iv_box_tips = (ImageView) findViewById(R.id.ibtn_show_box_tip);
	}

	@Override
	protected int initView() {
		return R.layout.activity_show;
	}

	@Override
	public void onResume() {
//		Intent intent = new Intent(this, PeipeiFloatingService.class);
//		stopService(intent);

		sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_OPEN_BOX_TIPS, 0);
		BAApplication.isShowRomm = true;

		if (BAApplication.showVoiceList.size() == 0) {
			BaseUtils.showDialog(this, R.string.loading);
			roomsGetBiz.getShowHistorData(showRoomId, roomUid, -1, 100, 0);//语音
		} else {
			if (showOwnerChatAdapter.getCount() < BAApplication.showVoiceList.size()) {
				showOwnerChatAdapter.clearList();
				showOwnerChatAdapter.appendToList(BAApplication.showVoiceList);
				mainListView.setSelection(showOwnerChatAdapter.getCount() - 1);
			}
			if (!BAApplication.mVoiceRecod.isPlaying()) {
				if (BAApplication.showVoiceList.size() != 0) {
					if (BAApplication.showVoiceList.size() > 5) {
						BAApplication.getInstance().setTempList(BAApplication.showVoiceList.size() - 5);
					} else {
						BAApplication.getInstance().setTempList(BAApplication.showVoiceList.size());
					}
					BAApplication.getInstance().playVoice();
				}
			}
		}

		IntentFilter filter = new IntentFilter();
		filter.addAction(RecorderService.RECORDER_SERVICE_BROADCAST_NAME);
		registerReceiver(mReceiver, filter);

		registerExternalStorageListener();

		mStopUiUpdate = false;
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}

		BAApplication.isShowRomm = false;
		mStopUiUpdate = true;

		if (RecorderService.isRecording()) {
			Intent intent = new Intent(this, RecorderService.class);
			intent.putExtra(RecorderService.ACTION_NAME, RecorderService.ACTION_ENABLE_MONITOR_REMAIN_TIME);
			startService(intent);
		}

		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			if (showRoomInfo.getLefttime() <= 0) {
//				BAApplication.clearShow();
//			}
//			finish();
			leaveShowRoom();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.tshang.peipei.activity.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.show_title_back_tv:
//			if (showRoomInfo.getLefttime() <= 0) {
//				BAApplication.clearShow();
//			}
//			finish();
			leaveShowRoom();
			break;
		case R.id.show_title_more:
			hideGift();
			if (popWindow == null || !popWindow.isShowing()) {
				initPopuptWindow();
				popWindow.showAtLocation(showTitleLayout, Gravity.TOP, 0, 0);
			}
			break;
		case R.id.tv_show_inout://离开房间
			if (showRoomInfo.getLefttime() > 0) {
				roomsGetBiz.InOutRooms(InOutAct.out, showRoomId, BAApplication.mLocalUserInfo.uid.intValue());
			}
			BAApplication.clearShow();
			finish();
			BaseUtils.hidePopupWindow(popWindow);
			break;
		case R.id.tv_show_share://分享
			BaseUtils.hidePopupWindow(popWindow);
			if (BAApplication.mLocalUserInfo != null) {
				String url = BAConstants.SHOW_SHARE_URL + BAApplication.mLocalUserInfo.uid.intValue() + "&owneruid="
						+ showRoomInfo.getOwneruserinfo().getUid() + "&p=android";
				new ShareShowDialog(this, showRoomInfo.getOwneruserinfo().getUid(), new String(showRoomInfo.getOwneruserinfo().getNick()
						.toByteArray()), url, mHandler, mTencent, mWXapi).showDialog(0, 0);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//				MobclickAgent.onEvent(BAApplication.getInstance(), "dianjixiuchangfeixiang", map);
			}
			break;
		case R.id.tv_show_report://举报
			roomsGetBiz.delateShow();
			BaseUtils.hidePopupWindow(popWindow);
			break;
		case R.id.tv_show_close://关闭
			new FinshShowDialog(this, R.string.str_show_close_room, R.string.ok, R.string.cancel, roomsGetBiz).showDialog();
			BaseUtils.hidePopupWindow(popWindow);
			break;
		case R.id.ibtn_show_plus:
			String str = mChatEditText.getText().toString().trim();
			if (TextUtils.isEmpty(str)) {
				clickIbtnShowGift();
			} else {
				if (isCanSend) {
					RoomsGetBiz roomsGetBiz = new RoomsGetBiz(this, mHandler);
					hideSoftInput(mChatEditText);
					hideEmojiPanel();
					mChatEditText.setText("");

					ShowChatEntity chatEntity = new ShowChatEntity();

					chatEntity.data = str;
					chatEntity.nick = new String(BAApplication.mLocalUserInfo.nick);
					chatEntity.uid = BAApplication.mLocalUserInfo.uid.intValue();
					chatEntity.sex = BAApplication.mLocalUserInfo.sex.intValue();

					chatEntity.type = MessageType.TEXT.getValue();
					showChatAdapter.appendToList(chatEntity);
					chatListView.setSelection(showChatAdapter.getCount() - 1);
					roomsGetBiz.sendShowChat(EmojiFaceConversionUtil.convertUnicode2(str).getBytes(), showRoomId, "0", 0,
							MessageType.TEXT.getValue(), 1, chatEntity);
				} else {
					BaseUtils.showTost(this, "您输入的文字太长");
				}
			}
			break;
		case R.id.show_member_text:
			if (BAApplication.mLocalUserInfo != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//				MobclickAgent.onEvent(this, "dianjixiuchangzaixianrenshu", map);
			}
			BaseUtils.openActivity(this, ShowRoomMemberActivity.class);
			break;
		case R.id.showroom_gift_send:
			if (BAApplication.mLocalUserInfo != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//				MobclickAgent.onEvent(this, "dianjisonglirenshu", map);
			}

			if (ListUtils.isEmpty(giftList)) {
				return;
			}
			if (curPosition == -1) {
				BaseUtils.showTost(this, "请先选择一个礼物");
			} else {
				if (showRoomInfo.getLefttime() > 0) {
					giftNum = Integer.parseInt(tvSendGiftNum.getText().toString());
					if (giftNum <= 0) {
						giftNum = 1;
					}
					RequestDeliverGift requestDeliverGift = new RequestDeliverGift();
					GoGirlUserInfo userInfo = UserUtils.getUserEntity(PeipeiShowActivity.this);
					if (userInfo == null) {
						return;
					}
					GiftInfo info = (GiftInfo) giftList.get(curPosition);
					requestDeliverGift.getDeliverGift(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), info.id.intValue(),
							roomUid, giftNum, 0, this);

					giftGold = info.pricegold.intValue();
					giftSilver = info.pricesilver.intValue();
				} else {
					BaseUtils.showTost(this, R.string.str_show_room_isfinish);
					BAApplication.clearShow();
					finish();
				}
				hideGift();
			}
			break;
		case R.id.ibtn_show_emotion:
			if (!ViewStubIsInflate) {
				View emojiView = vs_emotion.inflate();
				ll_emoji = (LinearLayout) emojiView.findViewById(R.id.ll_emotion);
				mEmojiPager = (ViewPager) emojiView.findViewById(R.id.emoji_viewpager);
				pageControlView = (PageControlView) emojiView.findViewById(R.id.pageControlView);
				iv_common_face = (ImageView) emojiView.findViewById(R.id.iv_common_emotion);
				iv_common_face.setOnClickListener(this);
				iv_emoji_face = (ImageView) emojiView.findViewById(R.id.iv_emoji_emotion);
				iv_emoji_face.setOnClickListener(this);
				ViewStubIsInflate = true;

			}
			if (ll_emoji != null) {
				if (ll_emoji.getVisibility() == View.GONE) {
					new BroadCastEmotionViewAdd(this, mChatEditText, pageControlView, mEmojiPager, iv_common_face, iv_emoji_face);
					hideSoftInput(mChatEditText);
					showEmojiPanel();
				} else {
					hideEmojiPanel();
					showSoftInput(mChatEditText);
				}
			}
			mBottomGridView.setVisibility(View.GONE);
			break;
		case R.id.showroom_gift_num:
			if (popWindowGift == null || !popWindowGift.isShowing()) {
				initGiftPopuptWindow();
				popWindowGift.showAtLocation(tvSendGiftNum, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.popwindow_show_gift_other:
			hideGift();
			showSoftInput(edGiftNum);
			BaseUtils.hidePopupWindow(popWindowGift);
			showGiftNum();
			break;
		case R.id.popwindow_show_gift_1314:
			tvSendGiftNum.setText("1314");
			BaseUtils.hidePopupWindow(popWindowGift);
			break;
		case R.id.popwindow_show_gift_520:
			tvSendGiftNum.setText("520");
			BaseUtils.hidePopupWindow(popWindowGift);
			break;
		case R.id.popwindow_show_gift_188:
			tvSendGiftNum.setText("188");
			BaseUtils.hidePopupWindow(popWindowGift);
			break;
		case R.id.popwindow_show_gift_99:
			tvSendGiftNum.setText("99");
			BaseUtils.hidePopupWindow(popWindowGift);
			break;
		case R.id.popwindow_show_gift_10:
			tvSendGiftNum.setText("10");
			BaseUtils.hidePopupWindow(popWindowGift);
			break;
		case R.id.popwindow_show_gift_1:
			tvSendGiftNum.setText("1");
			BaseUtils.hidePopupWindow(popWindowGift);
			break;
		case R.id.et_show_gift_btn:
			if (!TextUtils.isEmpty(edGiftNum.getText().toString())) {
				tvSendGiftNum.setText(edGiftNum.getText().toString());
				if (Integer.parseInt(edGiftNum.getText().toString()) == 0) {
					tvSendGiftNum.setText("1");
				}
			} else {
				tvSendGiftNum.setText("1");
			}
			hideSoftInput(edGiftNum);
			llSetGiftNum.setVisibility(View.GONE);
			ll_showroom_gift.setVisibility(View.VISIBLE);
			break;
		case R.id.show_heat_add_hot:
			if (roomRole != ShowMemberType.owner) {
				if (System.currentTimeMillis() - BAApplication.addHotTime > 60000) {
					if (!isAddHot) {
						isAddHot = true;
						roomsGetBiz.addHot(showRoomId, roomUid);
						ivHeatDot.setVisibility(View.VISIBLE);
						AnimationDrawable anim = (AnimationDrawable) ivHeatDot.getBackground();
						anim.start();
					}
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
		case R.id.ibtn_show_left_plus:
			clickIbtnShowPlus();
			break;
		case R.id.show_gift_casino:
			BaseUtils.openActivity(this, ShowGiftHistoryActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if (BAApplication.showRoomInfo != null) {
//			Intent intent = new Intent(this, PeipeiFloatingService.class);
//			startService(intent);
//		}
		sendNoticeEvent(NoticeEvent.NOTICE83);

		if (!BAApplication.mVoiceRecod.isPlaying()) {
			showOwnerChatAdapter.setFileName("");
		}

		if (mSDCardMountEventReceiver != null) {
			unregisterReceiver(mSDCardMountEventReceiver);
			mSDCardMountEventReceiver = null;
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.SHOW_TITLE_HINT:
			//			if (menuShowAnim == null) {
			//				menuShowAnim = AnimationUtils.loadAnimation(this, R.anim.popwin_top_out);
			//			}
			//			showTitleLayout.setAnimation(menuShowAnim);
			//			menuShowAnim.setAnimationListener(new AnimationListener() {
			//
			//				@Override
			//				public void onAnimationStart(Animation animation) {}
			//
			//				@Override
			//				public void onAnimationRepeat(Animation animation) {}
			//
			//				@Override
			//				public void onAnimationEnd(Animation animation) {
			//					
			//				}
			//			});
			if (popWindow == null || !popWindow.isShowing()) {
				showTitleLayout.setVisibility(View.GONE);
			}
			break;
		case HandlerValue.SHOW_ROOM_CHAT_BACK://发送聊天
			if (msg.arg1 == 0) {
				if (msg.arg2 == 1) {
				} else if (msg.arg2 == 0) {
					GoGirlChatDataP dataP = (GoGirlChatDataP) msg.obj;
					showsRoomDbBiz.initDataToDB(dataP);
					showOwnerChatAdapter.notifyDataSetChanged();
				}
			} else if (msg.arg1 == ProtobufErrorCode.ShowRoomIsCloseError) {
				BaseUtils.showTost(this, R.string.str_show_room_isfinish);
				BAApplication.clearShow();
				finish();
			}
			break;
		case HandlerValue.SHOW_ROOM_GET_HISTORDATA://拉取聊天历史记录
			BaseUtils.cancelDialog();
			if (msg.arg1 == 0) {
				List<GoGirlChatDataP> list = (List<GoGirlChatDataP>) msg.obj;
				ArrayList<ShowChatEntity> aList = new ArrayList<ShowChatEntity>();

				ShowChatEntity chatEntity = new ShowChatEntity();
				for (GoGirlChatDataP chatdatap : list) {
					chatEntity = new ShowChatEntity();

					chatEntity.type = chatdatap.getChatdatalist(0).getType();
					chatEntity.voiceLength = chatdatap.getChatdatalist(0).getDatainfo();
					chatEntity.nick = new String(chatdatap.getFromnick().toByteArray());
					chatEntity.uid = chatdatap.getFrom();
					chatEntity.sex = chatdatap.getFromsex();
					chatEntity.data = new String(chatdatap.getChatdatalist(0).getData().toByteArray());

					if (chatEntity.type == 48) {
						try {
							GiftDealInfoP dealInfo = GiftDealInfoP.parseFrom(chatdatap.getChatdatalist(0).getData());

							GiftInfoP info = dealInfo.getGift();
							chatEntity.giftId = info.getId();
							chatEntity.giftKey = new String(info.getPickey().toByteArray());
							chatEntity.giftName = new String(info.getName().toByteArray());
							chatEntity.giftNum = dealInfo.getGiftnum();
						} catch (InvalidProtocolBufferException e) {
							e.printStackTrace();
						}
					}

					if (chatEntity.type == 0 || chatEntity.type == 48 || chatEntity.type == 45 || chatEntity.type == 2 || chatEntity.type == 47) {
						aList.add(chatEntity);
					}
				}
				Collections.reverse(aList);
				if (msg.arg2 == 1) {
					showChatAdapter.appendToList(aList);
					chatListView.setSelection(showChatAdapter.getCount() - 1);
				} else if (msg.arg2 == 0) {
					showOwnerChatAdapter.clearList();
					showOwnerChatAdapter.appendToList(aList);
					mainListView.setSelection(showOwnerChatAdapter.getCount() - 1);
					showsRoomDbBiz.initDataToDB(list);
					if (BAApplication.showVoiceList.size() == 0) {
						BAApplication.showVoiceList.addAll(aList);
						if (BAApplication.showVoiceList.size() > 5) {
							BAApplication.getInstance().setTempList(list.size() - 5);
						} else {
							BAApplication.getInstance().setTempList(0);
						}
						BAApplication.getInstance().playVoice();
					}
				}
			}
			break;
		case HandlerValue.SHOW_ROOM_IN_OUT:
			if (msg.arg1 == 0) {
				if (msg.arg2 == InOutAct.in) {
					roomRole = (Integer) msg.obj;
					if (roomRole == ShowMemberType.owner || roomRole == ShowMemberType.vip) {
						showVoice();
					} else {
						showText();
					}

					if (roomRole == ShowMemberType.owner) {
						mChatPuls.setVisibility(View.GONE);
					} else {
						mChatPuls.setVisibility(View.VISIBLE);
					}
				}
			} else {
				if (msg.arg2 == InOutAct.in) {
					if (msg.arg1 == ProtobufErrorCode.KickUserLessTimeError) {
						BaseUtils.showTost(this, "被踢出后5分钟内无法再次进入房间");
					} else if (msg.arg1 == ProtobufErrorCode.ShowRoomMemberIsFull) {
						BaseUtils.showTost(this, "此房间人数已满");
					} else {
						BaseUtils.showTost(this, "进入房间失败");
					}
					BAApplication.clearShow();
					finish();
				}
			}
			break;
		case HandlerType.CHAT_SENT_VOICE://发送语音
			if (BAApplication.mVoiceRecod.sampleFile() != null && BAApplication.mVoiceRecod.sampleFile().exists()) {
				if (BAApplication.mVoiceRecod.sampleLength() <= 1) {
					BaseUtils.showTost(this, R.string.voicelength_too_short);
					mAudioLenght = 0;
					return;
				} else {
					try {
						MessageType type = MessageType.SHOW_VOICE;
						if (BAApplication.mVoiceRecod.sampleFile() != null) {
							File file = BAApplication.mVoiceRecod.sampleFile();

							byte[] buffer = BaseFile.getByteFromVocieFile(file);
							String name = System.currentTimeMillis() + "";

							ShowChatEntity chatEntity = new ShowChatEntity();
							chatEntity.type = type.getValue();
							chatEntity.nick = new String(BAApplication.mLocalUserInfo.nick);
							chatEntity.uid = BAApplication.mLocalUserInfo.uid.intValue();
							chatEntity.sex = BAApplication.mLocalUserInfo.sex.intValue();

							File file1 = new File(SdCardUtils.getInstance().getDirectory(0), name);

							file1.createNewFile();
							BaseFile.saveByteToFile(buffer, file1.getAbsolutePath());
							chatEntity.voiceFile = file1.getAbsolutePath();

							BAApplication.showVoiceList.add(chatEntity);
							showOwnerChatAdapter.appendToList(chatEntity);
							mainListView.setSelection(showOwnerChatAdapter.getCount() - 1);
							showsRoomDbBiz.initDataToDBByVoice(chatEntity.voiceFile, MessageType.SHOW_VOICE.getValue());
							roomsGetBiz.sendShowChat(buffer, showRoomId, "0", mAudioLenght, MessageType.VOICE.getValue(), 0, chatEntity);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			break;
		case HandlerType.STATE_END_PLAYING://播放完了
			showOwnerChatAdapter.setFileName("");
			break;
		case HandlerValue.SHOW_ROOM_VOICE_ISEXITS://语音下载完了，可以开始听了
			BAApplication.mVoiceRecod.stop();
			String filePath = msg.obj.toString();
			BAApplication.mVoiceRecod.startPlayback(filePath, -1);
			String[] str = filePath.split("/");
			showsRoomDbBiz.updataStatus(str[str.length - 1], 1);
			showOwnerChatAdapter.setFileName(str[str.length - 1]);
			break;
		case HandlerValue.SHOW_ROOM_VOICE_NEEDLOAD:
			BAApplication.mVoiceRecod.stop();
			BAApplication.showTempList.clear();
			BAApplication.getInstance().setTempList(msg.arg1);
			showOwnerChatAdapter.setFileName("");
			break;
		case HandlerValue.SHOW_ROOM_GIFT_LIST:
			if (msg.arg1 == 0) {
				giftList = (GiftInfoList) msg.obj;
			}
			break;
		case HandlerValue.SHOW_ROOM_PUSH_MESSAGE://收到了刷礼物动画
			final ShowChatEntity showChatEntity = (ShowChatEntity) msg.obj;
			if (showChatEntity != null) {
				if (showChatEntity.series > 0) {//说明是连刷动画
					giftAnimationLists.add(showChatEntity);
					if (!giftAnimationLists.isEmpty()) {//说明需要执行动画
						if (!isLoadShowAnimationLayout) {//查找控件
							View showGiftView = vs_show_animation.inflate();
							isLoadShowAnimationLayout = true;

							rl_animation_first = (RelativeLayout) showGiftView.findViewById(R.id.rl_receiver_gift_first);
							rl_animation_second = (RelativeLayout) showGiftView.findViewById(R.id.rl_receiver_gift_second);

							rl_anim_one = (LinearLayout) showGiftView.findViewById(R.id.ll_bg_anim_1);
							rl_anim_two = (LinearLayout) showGiftView.findViewById(R.id.ll_bg_anim_2);

							iv_anim_one = (ImageView) showGiftView.findViewById(R.id.iv_animation_1);
							iv_anim_two = (ImageView) showGiftView.findViewById(R.id.iv_animation_2);

							tv_anim_count_one = (TextView) showGiftView.findViewById(R.id.tv_animtaion_one_count);
							tv_anim_count_two = (TextView) showGiftView.findViewById(R.id.tv_animation_two_count);

							tv_anim_name_one = (TextView) showGiftView.findViewById(R.id.tv_animation_name_one);
							tv_anim_name_two = (TextView) showGiftView.findViewById(R.id.tv_animation_two_name);

							iv_first_hundred = (ImageView) showGiftView.findViewById(R.id.iv_first_hundred);
							iv_first_ten = (ImageView) showGiftView.findViewById(R.id.iv_first_ten);
							iv_first_one = (ImageView) showGiftView.findViewById(R.id.iv_first_one);
							iv_second_hundred = (ImageView) showGiftView.findViewById(R.id.iv_second_hundred);
							iv_second_ten = (ImageView) showGiftView.findViewById(R.id.iv_second_ten);
							iv_second_one = (ImageView) showGiftView.findViewById(R.id.iv_second_one);

							iv_gift_one = (ImageView) showGiftView.findViewById(R.id.iv_gift_one);
							iv_gift_two = (ImageView) showGiftView.findViewById(R.id.iv_gift_two);

						}
						if (rl_animation_first.getVisibility() == View.GONE) {
							tv_anim_name_one.setText(giftAnimationLists.get(0).nick);
							int giftNum = giftAnimationLists.get(0).giftNum;
							tv_anim_count_one.setText(String.valueOf(giftNum) + "个");
							imageLoader.displayImage("http://" + new String(giftAnimationLists.get(0).giftKey) + BAConstants.LOAD_180_APPENDSTR,
									iv_gift_one, giftOptions);
							//							System.out.println("============1==="+giftAnimationLists.get(0).series);
							ShowGiftNumUtils.showImageCount(iv_first_one, iv_first_ten, iv_first_hundred, giftAnimationLists.get(0).series);
							rl_anim_one.startAnimation(animFirst);
							rl_animation_first.setVisibility(View.VISIBLE);
							animFirst.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {
									if (!giftAnimationLists.isEmpty()) {
										giftAnimationLists.remove(0);
									}
								}

								@Override
								public void onAnimationRepeat(Animation animation) {}

								@Override
								public void onAnimationEnd(Animation animation) {
									iv_anim_one.setVisibility(View.VISIBLE);
									iv_anim_one.startAnimation(animImgFirst);
								}
							});

							animImgFirst.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {}

								@Override
								public void onAnimationRepeat(Animation animation) {}

								@Override
								public void onAnimationEnd(Animation animation) {
									iv_anim_one.setVisibility(View.GONE);
									rl_animation_first.setVisibility(View.GONE);
									if (!giftAnimationLists.isEmpty()) {
										tv_anim_name_two.setText(giftAnimationLists.get(0).nick);
										int giftNum = giftAnimationLists.get(0).giftNum;
										imageLoader.displayImage("http://" + new String(giftAnimationLists.get(0).giftKey)
												+ BAConstants.LOAD_180_APPENDSTR, iv_gift_two, giftOptions);
										tv_anim_count_two.setText(String.valueOf(giftNum) + "个");
										//										System.out.println("============2==="+giftAnimationLists.get(0).series);
										ShowGiftNumUtils.showImageCount(iv_second_one, iv_second_ten, iv_second_hundred,
												giftAnimationLists.get(0).series);
										rl_animation_second.setVisibility(View.VISIBLE);
										rl_anim_two.startAnimation(animSecond);
									}
								}
							});

							animSecond.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {

									if (!giftAnimationLists.isEmpty()) {
										giftAnimationLists.remove(0);
									}
								}

								@Override
								public void onAnimationRepeat(Animation animation) {}

								@Override
								public void onAnimationEnd(Animation animation) {
									iv_anim_two.setVisibility(View.VISIBLE);
									iv_anim_two.startAnimation(animImgSecond);
								}
							});
							animImgSecond.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {

								}

								@Override
								public void onAnimationRepeat(Animation animation) {

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									iv_anim_two.setVisibility(View.GONE);
									rl_animation_second.setVisibility(View.GONE);
									if (!giftAnimationLists.isEmpty()) {
										rl_animation_first.setVisibility(View.VISIBLE);
										imageLoader.displayImage("http://" + new String(giftAnimationLists.get(0).giftKey)
												+ BAConstants.LOAD_180_APPENDSTR, iv_gift_one, giftOptions);
										tv_anim_name_one.setText(giftAnimationLists.get(0).nick);
										int giftNum = giftAnimationLists.get(0).giftNum;
										tv_anim_count_one.setText(String.valueOf(giftNum) + "个");
										//										System.out.println("============3==="+giftAnimationLists.get(0).series);
										ShowGiftNumUtils.showImageCount(iv_first_one, iv_first_ten, iv_first_hundred,
												giftAnimationLists.get(0).series);
										rl_anim_one.startAnimation(animFirst);
									}

								}
							});
						}

					}
				}
			}
		case HandlerValue.SHOW_ROOM_SEND_GIFT:
			showChatAdapter.appendToList((ShowChatEntity) msg.obj);
			chatListView.setSelection(showChatAdapter.getCount() - 1);
			break;
		case HandlerValue.SHOW_ROOM_PUSH_VOICE:
			ShowChatEntity chatEntity = (ShowChatEntity) msg.obj;
			if (chatEntity != null) {
				showOwnerChatAdapter.appendToList(chatEntity);

				if (BAApplication.mVoiceRecod.state() != Recorder.RECORDING_STATE) {
					mainListView.setSelection(showOwnerChatAdapter.getCount() - 1);
					if (BAApplication.mVoiceRecod.state() != Recorder.PLAYING_STATE) {
						BAApplication.getInstance().playVoice();

						String temp = chatEntity.data;
						if (!TextUtils.isEmpty(chatEntity.voiceFile)) {
							temp = chatEntity.voiceFile;
						}
						sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_VOICE_ISEXITS, 0, temp);//不能连续调用刷新方法
					}
				}
			}
			break;
		case HandlerValue.SHOW_ROOM_PUSH_ROLE:
			int uid = msg.arg2;
			ShowRoomRoleChangeInfo roleInfo = (ShowRoomRoleChangeInfo) msg.obj;
			if (BAApplication.mLocalUserInfo != null && uid == BAApplication.mLocalUserInfo.uid.intValue()) {
				roomRole = msg.arg1;
				if (msg.arg1 == ShowMemberType.vip) {
					new HintToastDialog(this, R.string.str_show_set_vip, R.string.ok).showDialog();

					hideGift();
					showVoice();

					hideSoftInput(edGiftNum);
					BaseUtils.hidePopupWindow(popWindowGift);
				} else {
					showText();
				}
			}
			String inoutStr1 = tvBroadcast.getText().toString();
			if (uid > 0) {
				if (msg.arg1 == ShowMemberType.vip) {
					inoutStr1 = String.format(getString(R.string.str_show_role_vip), new String(roleInfo.nick));
				} else if (msg.arg1 == ShowMemberType.normal) {
					inoutStr1 = String.format(getString(R.string.str_show_role_normal), new String(roleInfo.nick));
				}
			}

			tvBroadcast.setText(inoutStr1);
			break;
		case HandlerValue.SHOW_ROOM_PUSH_ROOMSINFO:
			ShowRoomLatestStatus lastestStatus = (ShowRoomLatestStatus) msg.obj;

			if (lastestStatus.hotnum.intValue() >= 0) {
				tvPraiseNum.setText(lastestStatus.hotnum.intValue() + "");
			}
			if (lastestStatus.giftnum.intValue() >= 0) {
				tvGiftNum.setText(lastestStatus.giftnum.intValue() + "");
			}
			if (lastestStatus.curmembernum.intValue() >= 0) {
				if (lastestStatus.maxmembernum.intValue() >= 0) {
					tvMemberNum.setText(lastestStatus.curmembernum.intValue() + "/" + lastestStatus.maxmembernum.intValue() + " 成员");
				} else {
					tvMemberNum.setText(lastestStatus.curmembernum.intValue() + "/" + showRoomInfo.getMaxmembernum() + " 成员");
				}
			}

			if (showRoomInfo.getLefttime() > 0) {
				if (BAApplication.mLocalUserInfo.uid.intValue() == showRoomInfo.getOwneruserinfo().getUid()) {
					if (lastestStatus.lefttime.intValue() >= 0) {
						if (lastestStatus.lefttime.intValue() == 0) {
							finish();
						}
					}
				} else {
					if (lastestStatus.lefttime.intValue() == 0) {
						finish();
					}
				}
			}

			String inoutStr = tvBroadcast.getText().toString();
			if (lastestStatus.inoutuid.intValue() > 0) {
				if (lastestStatus.inorout.intValue() == InOutAct.in) {
					inoutStr = String.format(getString(R.string.str_show_inorout_in), new String(lastestStatus.inoutnick));
					if (lastestStatus.ridingid.intValue() > 0) {
						ShowChatEntity entity = new ShowChatEntity();
						entity.ridingid = lastestStatus.ridingid.intValue();
						entity.nick = new String(lastestStatus.inoutnick);
						entity.type = 47;//宠物数据
						entity.uid = lastestStatus.inoutuid.intValue();
						entity.ridingName = new String(lastestStatus.ridingname);
						entity.sex = lastestStatus.inoutsex.intValue();
						showChatAdapter.appendToList(entity);
					}

				} else if (lastestStatus.inorout.intValue() == InOutAct.out) {
					inoutStr = String.format(getString(R.string.str_show_inorout_out), new String(lastestStatus.inoutnick));
				} else if (lastestStatus.inorout.intValue() == InOutAct.kick) {
					inoutStr = String.format(getString(R.string.str_show_inorout_kick), new String(lastestStatus.inoutnick));
					if (lastestStatus.inoutuid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
						BaseUtils.showTost(this, R.string.str_show_kick_room);
						BAApplication.clearShow();
						finish();
					}
				}
			}

			if (lastestStatus.addhotnick != null && !TextUtils.isEmpty(new String(lastestStatus.addhotnick))) {
				inoutStr = String.format(getString(R.string.str_show_add_hot), new String(lastestStatus.addhotnick));
			}

			tvBroadcast.setText(inoutStr);
			break;
		case HandlerValue.SHOW_ROOM_ADD_HOT:
			isAddHot = false;
			if (msg.arg1 == 0) {
				ivHeatAdd.setImageResource(R.drawable.casino_btn_praise_dis1);
				ivHeatDot.setVisibility(View.GONE);
				BAApplication.addHotTime = System.currentTimeMillis();
				SharedPreferencesTools.getInstance(this).saveLongKeyValue(BAApplication.addHotTime, BAConstants.SHOW_ADDHOT);
			} else if (msg.arg1 == ProtobufErrorCode.ShowRoomIsCloseError) {
				BaseUtils.showTost(this, R.string.str_show_room_isfinish);
				BAApplication.clearShow();
				finish();
			}
			break;
		case HandlerValue.SHOW_ROOM_HOT_TIME:
			if (System.currentTimeMillis() - BAApplication.addHotTime < 20000) {
				ivHeatAdd.setImageResource(R.drawable.casino_btn_praise_dis1);
			} else if (System.currentTimeMillis() - BAApplication.addHotTime < 40000) {
				ivHeatAdd.setImageResource(R.drawable.casino_btn_praise_dis2);
			} else if (System.currentTimeMillis() - BAApplication.addHotTime < 60000) {
				ivHeatAdd.setImageResource(R.drawable.casino_btn_praise_dis3);
			} else {
				ivHeatAdd.setImageResource(R.drawable.casino_btn_praise_un);
			}
			mHandler.sendEmptyMessageDelayed(HandlerValue.SHOW_ROOM_HOT_TIME, 1000);
			break;
		case HandlerValue.SHOW_ROOM_DELATESHOW:
			BaseUtils.showTost(this, R.string.str_show_report_ok);
			break;
		case HandlerValue.SHOW_ROOM_CLOSE:
			if (msg.arg1 == 0) {
				BAApplication.clearShow();
				finish();
			}
			break;
		case HandlerValue.SHOW_ROOM_GET_SINGLE_ROOM:
			if (msg.arg1 == 0) {
				showRoomInfo = (ShowRoomInfo) msg.obj;
				BAApplication.showRoomInfo = showRoomInfo;

				tvPraiseNum.setText(showRoomInfo.getHotnum() + "");
				tvGiftNum.setText(showRoomInfo.getGiftnum() + "");
				tvMemberNum.setText(showRoomInfo.getCurmembernum() + "/" + showRoomInfo.getMaxmembernum() + " 成员");

				if (showRoomInfo.getLefttime() > 0) {
					roomsGetBiz.InOutRooms(InOutAct.in, showRoomInfo.getRoomid(), BAApplication.mLocalUserInfo.uid.intValue());
				} else {
					llSendLayout.setVisibility(View.GONE);
					llSetGiftNum.setVisibility(View.GONE);
					ll_left.setVisibility(View.GONE);
					mBottomGridView.setVisibility(View.GONE);
				}
			} else {
				if (BAApplication.showRoomInfo != null)
					BAApplication.clearShow();
				finish();
			}
			break;
		case HandlerType.USER_GIFT_REQUEST:
			if (msg.arg1 != 0) {
				if (giftGold * giftNum > mGoldNum) {
					new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, mGoldNum, mSilverNum).showDialog();
				}

				if (giftSilver * giftNum > mSilverNum) {
					new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, mGoldNum, mSilverNum).showDialog();
				}
			}
			break;
		case HandlerValue.SHOW_ROOM_VOICE_CLOSE:
			showOwnerChatAdapter.setFileName("");
			break;
		case HandlerValue.SHOW_ROOM_OPEN_BOX:
			iv_openbox.setVisibility(View.GONE);
			ll_openbox.setVisibility(View.VISIBLE);
			tv_openboxName.setText(BAApplication.showBoxName);
			break;
		case HandlerValue.SHOW_ROOM_OPEN_BOX_RESULT:
			BaseUtils.cancelDialog();
			sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_OPEN_BOX_TIPS, 0);
			if (msg.arg1 == 0) {
				openBox();
				mHandler.sendEmptyMessageDelayed(HandlerValue.SHOW_ROOM_OPEN_BOX, 1200);
				//				imageLoader.displayImage("http://" + new String(info.getGift().getPickey().toByteArray()) + BAConstants.LOAD_180_APPENDSTR,
				//						mViewholer.ivGift, options_gift);

				if (!TextUtils.isEmpty(BAApplication.showBoxPic)) {
					try {
						String saveImageName = BAApplication.showBoxPic.substring(BAApplication.showBoxPic.lastIndexOf("/"));
						File file = new File(SdCardUtils.getInstance().getLoadPicDir(), saveImageName);
						if (file != null && file.exists()) {//说明闪屏存在
							DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(false).cacheInMemory(false)
									.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565)
									.build();
							ImageLoader.getInstance().displayImage("file://" + file.getAbsolutePath(), iv_openbox_result, options,
									new ImageLoadingListener() {

										@Override
										public void onLoadingStarted(String imageUri, View view) {

										}

										@Override
										public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

										}

										@Override
										public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
											if (loadedImage != null) {
												iv_openbox_result.setImageBitmap(loadedImage);
											}
										}

										@Override
										public void onLoadingCancelled(String imageUri, View view) {

										}
									});
						} else {
							RequestLoadingPic req = new RequestLoadingPic();
							req.getLoadingPicShow(this, BAApplication.showBoxPic, 0, this);
						}

					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

				}

			} else if (msg.arg1 == -28230) {
				BaseUtils.showTost(this, R.string.str_show_openbox_failed);
			} else {
				vs_openbox.setVisibility(View.GONE);
			}
			break;
		case HandlerValue.SHOW_ROOM_OPEN_BOX_RESULT_PIC:
			if (msg.arg1 == 0) {
				String path = (String) msg.obj;
				if (!TextUtils.isEmpty(path)) {
					iv_openbox_result.setImageBitmap(BaseFile.getImageFromFile(path));
				}
			}
			break;
		case HandlerValue.SHOW_ROOM_OPEN_BOX_RESULT_CLOSE:
			vs_openbox.setVisibility(View.GONE);
			break;
		case HandlerValue.SHOW_ROOM_OPEN_BOX_TIPS:
			if (BAApplication.isShowBox && BAApplication.showBoxNum > 0) {
				iv_box_tips.setVisibility(View.VISIBLE);

				iv_box_tips.setBackgroundResource(R.anim.casino_openbox_tip);
				AnimationDrawable mAnimationOpenBox = (AnimationDrawable) iv_box_tips.getBackground();
				mAnimationOpenBox.start();
			} else {
				iv_box_tips.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.show_listview_main:
			hideEmojiPanel();
			hideSoftInput(mChatEditText);
			hideGift();
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				if (showTitleLayout.getVisibility() == View.GONE) {
					if (menuShowAnim == null) {
						menuShowAnim = AnimationUtils.loadAnimation(this, R.anim.popwin_top_in);
					}
					showTitleLayout.setAnimation(menuShowAnim);
					showTitleLayout.setVisibility(View.VISIBLE);
					mHandler.sendEmptyMessageDelayed(HandlerValue.SHOW_TITLE_HINT, 5000);
				}
				break;
			}
			break;
		case R.id.show_listview_chat:
			hideEmojiPanel();
			hideSoftInput(mChatEditText);
			hideGift();
			break;
		case R.id.show_send_voice_btn:
			hideEmojiPanel();
			hideSoftInput(mChatEditText);
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
			break;
		case R.id.et_show_text:
			hideEmojiPanel();
			mBottomGridView.setVisibility(View.GONE);
			break;
		default:
			break;
		}

		return false;
	}

	/**
	 * 弹出筛选框
	 *
	 */
	private void initPopuptWindow() {//菜单悬浮框选择
		// 获取自定义布局文件xml的视图
		View popview = getLayoutInflater().inflate(R.layout.popupwindow_show, null, false);
		popWindow = new PopupWindow(popview, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		popWindow.getContentView().setFocusableInTouchMode(true);
		popWindow.getContentView().setFocusable(true);
		// 设置动画效果
		popWindow.setAnimationStyle(R.style.anim_popwindow_alpha);
		popview.findViewById(R.id.tv_show_share).setOnClickListener(this);
		if (roomUid == BAApplication.mLocalUserInfo.uid.intValue() && BAApplication.showRoomInfo.getLefttime() > 0) {
			popview.findViewById(R.id.tv_show_close).setOnClickListener(this);
			popview.findViewById(R.id.tv_show_inout).setVisibility(View.GONE);
			popview.findViewById(R.id.tv_show_report).setVisibility(View.GONE);
			popview.findViewById(R.id.tv_show_close).setVisibility(View.VISIBLE);
		} else {
			popview.findViewById(R.id.tv_show_inout).setOnClickListener(this);
			popview.findViewById(R.id.tv_show_report).setOnClickListener(this);
			popview.findViewById(R.id.tv_show_inout).setVisibility(View.VISIBLE);
			popview.findViewById(R.id.tv_show_report).setVisibility(View.VISIBLE);
			popview.findViewById(R.id.tv_show_close).setVisibility(View.GONE);
		}

		popview.findViewById(R.id.rl_main_message_root).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BaseUtils.hidePopupWindow(popWindow);
				showTitleLayout.setVisibility(View.GONE);
				return false;
			}
		});

	}

	/**
	 * 弹出筛选框
	 *
	 */
	private void initGiftPopuptWindow() {//礼物悬浮框选择
		// 获取自定义布局文件xml的视图
		View popview = getLayoutInflater().inflate(R.layout.popupwindow_show_gift, null, false);
		popWindowGift = new PopupWindow(popview, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popWindowGift.setTouchable(true);
		popWindowGift.setOutsideTouchable(true);
		popWindowGift.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		popWindowGift.getContentView().setFocusableInTouchMode(true);
		popWindowGift.getContentView().setFocusable(true);
		// 设置动画效果
		//		popWindowGift.setAnimationStyle(R.style.anim_popwindow_alpha);
		popview.findViewById(R.id.popwindow_show_gift_other).setOnClickListener(this);
		popview.findViewById(R.id.popwindow_show_gift_1314).setOnClickListener(this);
		popview.findViewById(R.id.popwindow_show_gift_520).setOnClickListener(this);
		popview.findViewById(R.id.popwindow_show_gift_188).setOnClickListener(this);
		popview.findViewById(R.id.popwindow_show_gift_99).setOnClickListener(this);
		popview.findViewById(R.id.popwindow_show_gift_10).setOnClickListener(this);
		popview.findViewById(R.id.popwindow_show_gift_1).setOnClickListener(this);

		popview.findViewById(R.id.rl_main_message_root).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BaseUtils.hidePopupWindow(popWindowGift);
				return false;
			}
		});

	}

	/**
	 * 点击 礼物键 界面处理
	 *
	 */
	private void clickIbtnShowGift() {
		if (!isInflateGift) {
			View recordView = vs_gift.inflate();
			ll_showroom_gift = (LinearLayout) recordView.findViewById(R.id.ll_showroom_gift);
			gvGift = (GridView) recordView.findViewById(R.id.showroom_gift_gridview);
			gvGift.setAdapter(giftAdapter);
			gvGift.setOnItemClickListener(new giftOnItemClickListener());
			recordView.findViewById(R.id.showroom_gift_send).setOnClickListener(this);
			tvSendGiftNum = (TextView) recordView.findViewById(R.id.showroom_gift_num);
			tvSendGiftNum.setOnClickListener(this);
			isInflateGift = true;
		}
		hideSoftInput(mChatEditText);
		hideEmojiPanel();
		mBottomGridView.setVisibility(View.GONE);

		if (ll_showroom_gift != null) {
			giftAdapter.setList(giftList);
			mBottomGridView.setVisibility(View.GONE);
			llSendLayout.setVisibility(View.GONE);
			llSetGiftNum.setVisibility(View.GONE);
			ll_showroom_gift.setVisibility(View.VISIBLE);
		}
	}

	private void hideGift() {
		if (ll_showroom_gift != null) {
			ll_showroom_gift.setVisibility(View.GONE);
		}
		if (showRoomInfo.getLefttime() > 0) {
			if (roomRole == ShowMemberType.owner || roomRole == ShowMemberType.vip) {
				showVoice();
			} else {
				showText();
			}

			if (roomRole == ShowMemberType.owner) {
				mChatPuls.setVisibility(View.GONE);
			} else {
				mChatPuls.setVisibility(View.VISIBLE);
			}
		} else {
			showGone();
		}
	}

	private void startRecording() {
		if (!isInflateRecord) {
			View recordView = vs_record.inflate();
			ll_record = (LinearLayout) recordView.findViewById(R.id.ll_chat_recording);
			tv_chat_record_time = (TextView) recordView.findViewById(R.id.tv_chat_recording);
			iv_chat_record = (ImageView) recordView.findViewById(R.id.iv_chat_recording);
			isInflateRecord = true;
		}

		ll_record.setVisibility(View.VISIBLE);

		btnSendVoice.setText(R.string.release_finish);
		try {
			if (BAApplication.mVoiceRecod.isPlaying()) {
				BAApplication.mVoiceRecod.stop();
			}
			showOwnerChatAdapter.setFileName("");

			mAudioLenght = 0;
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
			ChatVoiceBiz.saveVoiceByType(name, mRequestedType, mRemainingTimeCalculator, BAApplication.mVoiceRecod);
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

	@Override
	public void onStateChanged(int state) {
		updateUi();
	}

	@Override
	public void onError(int error) {}

	private void registerExternalStorageListener() {
		if (mSDCardMountEventReceiver == null) {
			mSDCardMountEventReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					BAApplication.mVoiceRecod.reset();
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

	public void updateVUMeterView() {
		final int MAX_VU_SIZE = 14;
		if (BAApplication.mVoiceRecod.state() == Recorder.RECORDING_STATE) {
			int vuSize = MAX_VU_SIZE * BAApplication.mVoiceRecod.getMaxAmplitude() / 32768;
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
		int state = BAApplication.mVoiceRecod.state();
		if (state == Recorder.RECORDING_STATE) {
			long time = BAApplication.mVoiceRecod.progress();
			mAudioLenght = (int) time;

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
		if (tv_chat_record_time != null) {
			tv_chat_record_time.setText("0/60“");
		}
		btnSendVoice.setText(R.string.press_on_record);
		BAApplication.mVoiceRecod.stop();
		mAnimationDrawable.stop();

		mHandler.sendEmptyMessageDelayed(HandlerType.CHAT_SENT_VOICE, 100);
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

	private void deleteRecording() {
		mChatPuls.setClickable(true);
		if (ll_record != null) {
			ll_record.setVisibility(View.GONE);
		}

		if (tv_chat_record_time != null) {
			tv_chat_record_time.setText("0/60“");
		}
		btnSendVoice.setText(R.string.press_on_record);
		mAnimationDrawable.stop();
		BAApplication.mVoiceRecod.delete();
	}

	private class giftOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			curPosition = position;
			giftAdapter.setCurpostion(position);
		}

	}

	@Override
	public void getDeliverGiftCallBack(int retCode) {
		sendHandlerMessage(mHandler, HandlerType.USER_GIFT_REQUEST, retCode);
	}

	/**
	 * 显示表情面板 
	 *
	 */
	private void showEmojiPanel() {
		if (ll_emoji != null && ll_emoji.getVisibility() == View.GONE) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Animation animation = AnimationUtils.loadAnimation(PeipeiShowActivity.this, R.anim.emoji_panel_translate_begin);
					ll_emoji.setVisibility(View.VISIBLE);
					ll_emoji.startAnimation(animation);
				}
			}, 500);
		}
	}

	/**
	 * 隐藏表情面板 
	 *
	 */
	private void hideEmojiPanel() {
		if (ll_emoji != null && ll_emoji.getVisibility() == View.VISIBLE) {
			Animation animation = AnimationUtils.loadAnimation(PeipeiShowActivity.this, R.anim.emoji_panel_translate_back);
			ll_emoji.setVisibility(View.GONE);
			ll_emoji.startAnimation(animation);
		}
	}

	@Override
	public void onEvent(NoticeEvent event) {
		super.onEvent(event);
		switch (event.getFlag()) {
		case NoticeEvent.NOTICE72:
			sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_PUSH_MESSAGE, 0, event.getObj());
			break;
		case NoticeEvent.NOTICE73:
			sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_PUSH_VOICE, 0, event.getObj());
			break;
		case NoticeEvent.NOTICE74:
			sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_PUSH_ROLE, event.getNum(), event.getNum2(), event.getObj());
			break;
		case NoticeEvent.NOTICE75:
			sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_PUSH_ROOMSINFO, 0, event.getObj());
			break;
		case NoticeEvent.NOTICE78:
			sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_VOICE_CLOSE, 0, event.getObj());
			break;
		case NoticeEvent.NOTICE79:
			sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_VOICE_ISEXITS, 0, event.getObj());
			break;
		case NoticeEvent.NOTICE82:
			sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_OPEN_BOX_TIPS, 0);
			break;
		default:
			break;
		}
	}

	@Override
	public void getUserProperty(int retCode, UserPropertyInfo userPropertyInfo) {
		if (retCode == 0) {
			mGoldNum = userPropertyInfo.goldcoin.intValue();
			mSilverNum = userPropertyInfo.silvercoin.intValue();
		}

	}
	
	private void leaveShowRoom(){
		dismissDialog = DialogFactory.showMsgDialog(this, "", getString(R.string.str_show_room_is_finish), new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (showRoomInfo.getLefttime() > 0) {
					roomsGetBiz.InOutRooms(InOutAct.out, showRoomId, BAApplication.mLocalUserInfo.uid.intValue());
				}
				BAApplication.clearShow();
				finish();
			}
		}, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismissLeaveRoomDialog();
			}
		});
	}
	
	private void dismissLeaveRoomDialog(){
		if(dismissDialog != null){
			dismissDialog.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mTencent != null)
			mTencent.onActivityResult(requestCode, resultCode, data);
	}

	private void reqToWX() {
		mWXapi = WXAPIFactory.createWXAPI(this, BAConstants.WXAPPID, true);
		mWXapi.registerApp(BAConstants.WXAPPID);
	}

	public void showText() {
		mBottomGridView.setVisibility(View.GONE);
		llSendLayout.setVisibility(View.VISIBLE);
		llSendText.setVisibility(View.VISIBLE);
		llSendVoice.setVisibility(View.GONE);
		llSetGiftNum.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mChatEditText.getText().toString())) {
			mChatPuls.setImageResource(R.drawable.message_btn_icon_gift);
		} else {
			mChatPuls.setImageResource(R.drawable.message_btn_send1_selector);
		}
	}

	public void showVoice() {
		mBottomGridView.setVisibility(View.GONE);
		llSendLayout.setVisibility(View.VISIBLE);
		llSendText.setVisibility(View.GONE);
		llSendVoice.setVisibility(View.VISIBLE);
		llSetGiftNum.setVisibility(View.GONE);
		mChatPuls.setImageResource(R.drawable.message_btn_icon_gift);
	}

	public void showGiftNum() {
		mBottomGridView.setVisibility(View.GONE);
		llSendLayout.setVisibility(View.GONE);
		llSetGiftNum.setVisibility(View.VISIBLE);
	}

	public void showGone() {
		mBottomGridView.setVisibility(View.GONE);
		llSendLayout.setVisibility(View.GONE);
		llSetGiftNum.setVisibility(View.GONE);
	}

	/**
	 * 点击 “+”键 界面处理
	 *
	 */
	private void clickIbtnShowPlus() {
		BAApplication.isShowBox = false;
		iv_box_tips.setVisibility(View.GONE);
		hideSoftInput(mChatEditText);
		hideEmojiPanel();

		mBottomGridView.setAdapter(new ShowBottomAdapter(this));
		mBottomGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String str = (String) parent.getAdapter().getItem(position);
				if (!TextUtils.isEmpty(str)) {
					if (str.equals("广播")) {
						mBottomGridView.setVisibility(View.GONE);
						new ShowSendBroadcastDialog(PeipeiShowActivity.this, 0, 0, 0).showDialog();
					} else if (str.equals("宝箱")) {
						mBottomGridView.setVisibility(View.GONE);
						//						BaseUtils.showDialog(PeipeiShowActivity.this, R.string.loading);
						roomsGetBiz.openBox();
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

	private void openBox() {
		if (!isOpenBox) {
			View recordView = vs_openbox.inflate();
			iv_openbox = (ImageView) recordView.findViewById(R.id.iv_show_baohe);
			ll_openbox = (RelativeLayout) recordView.findViewById(R.id.layout_show_baohe_result);
			iv_openbox_result = (ImageView) recordView.findViewById(R.id.iv_show_baohe_result);
			tv_openboxName = (TextView) recordView.findViewById(R.id.tv_show_baohe_result);
			isOpenBox = true;
		}
		vs_openbox.setVisibility(View.VISIBLE);

		ll_openbox.setVisibility(View.GONE);
		iv_openbox.setVisibility(View.VISIBLE);
		iv_openbox.setBackgroundResource(R.anim.show_openbox);
		AnimationDrawable mAnimationOpenBox = (AnimationDrawable) iv_openbox.getBackground();
		mAnimationOpenBox.start();

		mHandler.sendEmptyMessageDelayed(HandlerValue.SHOW_ROOM_OPEN_BOX_RESULT_CLOSE, 5000);
	}

	@Override
	public void loadingPic(int retCode, int type, String str) {
		sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_OPEN_BOX_RESULT_PIC, retCode, 0, str);
	}
}
