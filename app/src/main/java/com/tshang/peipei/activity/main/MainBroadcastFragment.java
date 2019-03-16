package com.tshang.peipei.activity.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.ChatEmojiPagerAdapter;
import com.tshang.peipei.activity.dialog.ArchwayAnimationDialog;
import com.tshang.peipei.activity.dialog.ArrowAnimationDialog;
import com.tshang.peipei.activity.dialog.BalloonAnimationDialog;
import com.tshang.peipei.activity.dialog.BroadcastDecreeAnimationDialog;
import com.tshang.peipei.activity.dialog.BroadcastFingerWinDialog;
import com.tshang.peipei.activity.dialog.CanGrapHallRedpacketDialog;
import com.tshang.peipei.activity.dialog.CanGrapHallSolitaireRedpacketDialog;
import com.tshang.peipei.activity.dialog.CarAnimationDialog;
import com.tshang.peipei.activity.dialog.ChangeAnimationDialog;
import com.tshang.peipei.activity.dialog.CoupletAnimationDialog;
import com.tshang.peipei.activity.dialog.CrownAnimationDialog;
import com.tshang.peipei.activity.dialog.FeatherAnimationDialog;
import com.tshang.peipei.activity.dialog.FinshShowDialog;
import com.tshang.peipei.activity.dialog.FireAnimationDialog;
import com.tshang.peipei.activity.dialog.FireCrackersAnimationDialog;
import com.tshang.peipei.activity.dialog.FistAnimationDialog;
import com.tshang.peipei.activity.dialog.GrapHallNormalRedpacketSuccessDialog;
import com.tshang.peipei.activity.dialog.GrapHallRandomRedpacketLuckDialog;
import com.tshang.peipei.activity.dialog.GrapHallRandomRedpacketSuccessDialog;
import com.tshang.peipei.activity.dialog.HallRedpacketInfoDialog;
import com.tshang.peipei.activity.dialog.HallRedpacketNoMoneyDialog;
import com.tshang.peipei.activity.dialog.HallRedpacketTimeOutDialog;
import com.tshang.peipei.activity.dialog.HappyNewYearAnimationDialog;
import com.tshang.peipei.activity.dialog.HeartAnimationDialog;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.dialog.LipsAnimationDialog;
import com.tshang.peipei.activity.dialog.LoveOathAnimationDialog;
import com.tshang.peipei.activity.dialog.OpenShowDialog;
import com.tshang.peipei.activity.dialog.RedpacketAnimationDialog;
import com.tshang.peipei.activity.dialog.RoseAnimationDialog;
import com.tshang.peipei.activity.dialog.RoseRainAnimationDialog;
import com.tshang.peipei.activity.dialog.SolitaireGrapRedPacketFailDialog;
import com.tshang.peipei.activity.dialog.SolitaireGrapRedPacketSuccessDialog;
import com.tshang.peipei.activity.dialog.SolitaireRedPacketInfoDialog;
import com.tshang.peipei.activity.dialog.StarFallFlakeAnimationDialog;
import com.tshang.peipei.activity.dialog.ValentineDayDialog;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.main.adapter.MainBroadCastAdapter;
import com.tshang.peipei.activity.main.adapter.MainBroadCastAdapter.IenterShowRoom;
import com.tshang.peipei.activity.main.adapter.ShowRoomsAdapter;
import com.tshang.peipei.activity.main.adapter.TopAdvAdapter;
import com.tshang.peipei.activity.main.adapter.TopBroadCastAdapter;
import com.tshang.peipei.activity.main.adapter.bean.AdvBean;
import com.tshang.peipei.activity.mine.MinePlayGameActivity;
import com.tshang.peipei.activity.mine.MineWriteBroadCastActivity;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.activity.show.PeipeiShowActivity;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.GiftId;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.InOutAct;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAConstants.ProtobufErrorCode;
import com.tshang.peipei.base.babase.BAConstants.RewardType;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.base.json.AdvBeanFunctions;
import com.tshang.peipei.base.json.GoGirlUserJson;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.biz.chat.ChatManageBiz.IGetPushBroadcastListener;
import com.tshang.peipei.model.broadcast.BroadCastBiz;
import com.tshang.peipei.model.broadcast.BroadCastUtils;
import com.tshang.peipei.model.broadcast.BroadcastMessage;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.model.broadcast.ItemVoiceClickListener;
import com.tshang.peipei.model.broadcast.OnPullEventChangeRefreshTextListener;
import com.tshang.peipei.model.broadcast.WriteBroadCastBiz;
import com.tshang.peipei.model.entity.SuspensionEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.hall.MainHallBiz;
import com.tshang.peipei.model.redpacket2.HallRedpacket;
import com.tshang.peipei.model.redpacket2.RedDecodeUtil;
import com.tshang.peipei.model.redpacket2.SolitaireRedPacketBiz;
import com.tshang.peipei.model.redpacket2.SolitaireRedpacket;
import com.tshang.peipei.model.request.RequestAvailHallRedpacketList.GetAvailHallRedpacketListCallBack;
import com.tshang.peipei.model.request.RequestCheckHallRedpacketStatus.CheckHallRedpacketStatusCallBack;
import com.tshang.peipei.model.request.RequestCheckRedpacketState.CheckRedpacketStateCallBack;
import com.tshang.peipei.model.request.RequestGetAdvUrl.IGetAdv;
import com.tshang.peipei.model.request.RequestGetShowAdv;
import com.tshang.peipei.model.request.RequestGetShowAdv.IGetShowAdvUrl;
import com.tshang.peipei.model.request.RequestGrabHallRedpacket.GrabHallRedpacketCallBack;
import com.tshang.peipei.model.request.RequestGrapSolitaireRedpacket.GetGrapSolitaireRedPacketCallBack;
import com.tshang.peipei.model.request.RequestHallRedpacketAvail.GetHallRedpacketAvailCallBack;
import com.tshang.peipei.model.request.RequestSolitaireRedPacketMoney.GetSolitaireRedPacketCallBack;
import com.tshang.peipei.model.showrooms.RoomsGetBiz;
import com.tshang.peipei.model.showrooms.ShowGiftNumUtils;
import com.tshang.peipei.model.space.SpaceBiz;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfoList;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfoList;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastSeriesShowGiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.EnterBroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ShowShareBroadcastInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.db.BroadCastColumn;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;
import com.tshang.peipei.view.AutoScrollViewPager;
import com.tshang.peipei.view.HorizontalListView;
import com.tshang.peipei.view.PageControlView;

import de.greenrobot.event.EventBus;

public class MainBroadcastFragment extends BaseFragment implements IGetPushBroadcastListener, OnScrollListener, IGetAdv, IGetShowAdvUrl,
		IenterShowRoom, CheckRedpacketStateCallBack, GetSolitaireRedPacketCallBack, GetGrapSolitaireRedPacketCallBack, GrabHallRedpacketCallBack,
		CheckHallRedpacketStatusCallBack, GetHallRedpacketAvailCallBack, GetAvailHallRedpacketListCallBack {
	private ViewPager mViewPager;
	private TextView tv_broadcast;
	private TextView tv_game;
	private TextView tv_reward;
	private View viewBroadcast;
	private View viewGame;
	private ArrayList<View> views = new ArrayList<View>();
	private LinearLayout ll_broadcast;
	private ImageView iv_arrow;
	private ImageView iv_noticeMe;

	private AutoScrollViewPager autoPager;
	private PullToRefreshListView plv_broadcast;
	private PullToRefreshListView plv_game;
	private TextView tv_broadcast_no_data;
	private TextView tv_game_no_data;
	private PageControlView pageControlView;
	private RelativeLayout rlTopBroadCast;
	private ImageView ivBroadcastScroll;
	private ImageView ivGameScroll;
	private LinearLayout ll_open_show;
	private AutoScrollViewPager autoAdvPager;
	private RelativeLayout rl_adv;
	private PageControlView pageTopAdvView;
	private TopAdvAdapter advAdapter;

	private MainBroadCastAdapter broadcastAdapter;
	private MainBroadCastAdapter gameAdapter;
	private TopBroadCastAdapter topBroadCastAdapter;

	private BroadCastBiz broadCastBiz;
	private Recorder mVoiceRecod;
	private Queue<BroadcastMessage> sendQueue = new LinkedList<BroadcastMessage>();
	private BroadcastQueue broadcast;

	private WriteBroadCastBiz writeBiz;
	private int currentPage;

	private RoomsGetBiz roomsGetBiz;
	private ShowRoomsAdapter showRoomAdapter;
	private HorizontalListView hListView;

	private ShowRoomInfo showRoomInfoTmp;
	private ViewStub viewStub;
	private View animationViewStub;
	private RelativeLayout rl_broadcast_show_animation;
	private LinearLayout ll_animation_bg;
	private TextView tv_sendUserName;
	private TextView tv_receiveUserName;
	private ImageView iv_showGift;
	private ImageView iv_count_first;
	private ImageView iv_count_second;
	private ImageView iv_count_third;
	private ImageView iv_count_four;
	private ImageView iv_count_five;
	private ImageView iv_count_six;
	private ImageView iv_count_seven;
	private DisplayImageOptions giftOptions;
	private Animation animFirst;

	private ShowShareBroadcastInfo shareInfo;
	private SolitaireRedPacketBiz solitaireRedPacketBiz;

	public static int curView = SuspensionEntity.ACTIVITY_BOARDCAST;;

	private View view;

	private View fl_redpacket;
	private View iv_redpacket_icon;
	private ImageView iv_redpacket;

	private View fl_hall_redpacket;
	private View iv_hall_redpacket_icon;
	private ImageView iv_hall_redpacket;
	private long preSolitaireTime;
	private long preHallTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().registerSticky(this);
		broadCastBiz = new BroadCastBiz(getActivity(), mHandler);
		broadcastAdapter = new MainBroadCastAdapter(getActivity(), broadCastBiz, mHandler, this);
		gameAdapter = new MainBroadCastAdapter(getActivity(), broadCastBiz, mHandler, null);
		topBroadCastAdapter = new TopBroadCastAdapter(getActivity(), broadCastBiz);
		topBroadCastAdapter.setiEnterShowRoom(this);
		ChatManageBiz.getInManage(getActivity()).setBroadcastListener(this);
		mVoiceRecod = Recorder.getInstance();
		broadcast = new BroadcastQueue(sendQueue);
		broadcast.start();

		writeBiz = new WriteBroadCastBiz(getActivity(), mHandler);
		roomsGetBiz = new RoomsGetBiz(getActivity(), mHandler);
		solitaireRedPacketBiz = new SolitaireRedPacketBiz();
		getRedPacketMoneyInfo();
		giftOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).build();
		animFirst = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_show_left_in);
		animFirst.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.BROADCAST_SHOW_ANIMATION_CONTINUE_VALUE);
			}
		});

	}

	private void getHallRedpacketAvail() {
		solitaireRedPacketBiz.requestCurrentRedpacketAvail(0, this);
		solitaireRedPacketBiz.requestCurrentRedpacketAvail(1, this);
	}

	private void getRedPacketMoneyInfo() {
		solitaireRedPacketBiz.reqeustSolitaireRedPacketMoney(1, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_main_broadcast, null);
		initUi(view);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_DELAY_LOAD_DATA_VALUE);
			}
		}, 1);
		return view;
	}

	private void getBroadCastList(int type, boolean isRefresh) {
		broadCastBiz.getBroadCastList(type, LOADCOUNT, isRefresh);

	};

	public void onResume() {
		//		mHandler.sendEmptyMessageDelayed(HandlerValue.BROADCAST_FEATHER_VALUE, 2000);
		super.onResume();
		if (!this.isHidden()) {
			broadCastBiz.pushSwitch(true);
			if (autoPager != null)
				autoPager.startAutoScroll(); // start auto scroll when onResume
			mVoiceRecod.setHandler(getActivity(), mHandler);//放在这里保证从发送广播里面回到当前页面可以收到播放完了语音通知
		}
	}

	public void onPause() {
		super.onPause();
		if (autoPager != null)
			autoPager.stopAutoScroll();// stop auto scroll when onPause
	}

	private void initUi(View view) {
		mViewPager = (ViewPager) view.findViewById(R.id.broadcast_viewpager);

		tv_broadcast = (TextView) view.findViewById(R.id.tv_broadcast);
		tv_game = (TextView) view.findViewById(R.id.tv_game);
		tv_game.setOnClickListener(this);
		tv_reward = (TextView) view.findViewById(R.id.tv_reward);
		tv_reward.setOnClickListener(this);
		view.findViewById(R.id.ibtn_broadcast_write).setOnClickListener(this);
		ll_broadcast = (LinearLayout) view.findViewById(R.id.ll_broadcast);
		ll_broadcast.setOnClickListener(this);
		iv_arrow = (ImageView) view.findViewById(R.id.iv_broadcast_center);
		iv_noticeMe = (ImageView) view.findViewById(R.id.iv_broadcast_new_notify_about_me);
		showAboutMeCount();
	}

	@Override
	public void onClick(View v) {//点击监听
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_open_show://我要开秀监听
			if (BAApplication.mLocalUserInfo != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//				MobclickAgent.onEvent(getActivity(), "dianjiwoyaokaixiurenshu", map);
			}
			new OpenShowDialog(getActivity(), R.string.str_show_open_show_title, R.string.ok, R.string.cancel, roomsGetBiz).showDialog();
			break;
		case R.id.ibtn_broadcast_write:
			if (currentPage == 0) {
				Bundle bundle = new Bundle();
				bundle.putString("user", "user");
				BaseUtils.openActivity(getActivity(), MineWriteBroadCastActivity.class, bundle);
			} else if (currentPage == 1) {
				BaseUtils.openActivity(getActivity(), MinePlayGameActivity.class);
			}
			break;
		case R.id.ll_broadcast:
			if (mViewPager.getCurrentItem() == 1) {
				mViewPager.setCurrentItem(0);
			} else {
				if (popWindow == null || !popWindow.isShowing()) {
					rotate(true);
					initPopuptWindow();
					popWindow.showAtLocation(tv_broadcast, Gravity.TOP, 0, 0);
					setSelectOperate();
				}
			}
			break;
		case R.id.tv_game:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.iv_broadcast_scroll_bottom:
			if (currentPage == 0) {
				//				isShowBroadcastBottom = true;
				ivBroadcastScroll.setVisibility(View.GONE);
				int count = broadcastAdapter.getCount();
				if (count > 0) {
					plv_broadcast.getRefreshableView().setSelection(count - 1);
				}
			} else if (currentPage == 1) {
				//				isShowGameBottom = true;
				ivGameScroll.setVisibility(View.GONE);
				int count = gameAdapter.getCount();
				if (count > 0) {
					plv_game.getRefreshableView().setSelection(count - 1);
				}
			}
			break;
		case R.id.pop_broadcast_about_me_rl://@我的广播
			rlTopBroadCast.setVisibility(View.GONE);
			popClick(R.string.str_about_me, 2);
			getLocalDbData(true, 2, "0," + LOADCOUNT);
			if (BAApplication.mLocalUserInfo != null) {
				SharedPreferencesTools.getInstance(getActivity()).saveIntKeyValue(0,
						GoGirlUserJson.BRAODCAST + BAApplication.mLocalUserInfo.uid.intValue());
				NoticeEvent noticeEvent = new NoticeEvent();//通知到主界面把下方导航的数字去掉
				noticeEvent.setFlag(NoticeEvent.NOTICE52);
				EventBus.getDefault().postSticky(noticeEvent);
				showAboutMeCount();
			}
			break;
		case R.id.pop_broadcast_all_rl://全部广播
			popClick(R.string.str_all_broadcast, 0);
			topBroadCastAdapter.clearList();
			BaseUtils.showDialog(getActivity(), R.string.loading);
			getBroadCastList(BroadCastBiz.ALL_BROADCAST_TYPE, true);
			getBroadCastList(BroadCastBiz.TOP_BROADCAST_TYPE, true);

			break;
		case R.id.pop_broadcast_my_rl://我发送的广播 ，从本地数据库读取
			rlTopBroadCast.setVisibility(View.GONE);
			popClick(R.string.str_my_broadcast, 1);
			getLocalDbData(true, 1, "0," + LOADCOUNT);
			break;
		case R.id.tv_reward:
			Bundle bundle = new Bundle();
			bundle.putInt(RewardListActivity.LABLE_FLAG, RewardType.ALL.getValue());
			BaseUtils.openActivity(getActivity(), RewardListActivity.class, bundle);
			break;
		case R.id.iv_redpacket_icon:
			long time = System.currentTimeMillis() - preSolitaireTime;
			if (time > 1000) {
				getAvailHallRedpacketList(0);
			}
			preSolitaireTime = System.currentTimeMillis();
			break;
		case R.id.iv_hall_redpacket_icon:
			long hallTime = System.currentTimeMillis() - preHallTime;

			if (hallTime > 1000) {
				getAvailHallRedpacketList(1);
			}
			preHallTime = System.currentTimeMillis();
			break;
		default:
			break;
		}
	}

	private void getAvailHallRedpacketList(int type) {
		solitaireRedPacketBiz.requestGetAvailRedpacketList(type, this);
	}

	private BroadcastInfoList getLocalBroadcast(String status, String limit) {//获取广播数据
		return broadCastBiz.getLocalBroadcast(new String[] { status, String.valueOf(BAApplication.mLocalUserInfo.uid.intValue()) },
				BroadCastColumn.STAUTS + "=? and " + BroadCastColumn.USERUID + "=?", limit);
	}

	private void getLocalDbData(final boolean isRefresh, final int type, final String limit) {
		ThreadPoolService.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				BroadcastInfoList list = getLocalBroadcast(String.valueOf(type), limit);
				int isEnd = -1;
				if (list != null) {
					if (list.size() < LOADCOUNT) {
						isEnd = 1;
					}
				}
				int code = BroadCastBiz.REFRESH_CODE;
				if (!isRefresh) {
					code = BroadCastBiz.LOADMORE_CODE;
				}
				if (type == 2) {
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_ABOUT_ME_LIST, isEnd, code, list);
				} else if (type == 1) {
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_MY_DATA_LIST, isEnd, code, list);
				}
			}

		});
	}

	private int startLocalLoadPos = 0;//加载本地数据

	private void popClick(int textid, int pos) {//悬浮框点击事件
		startLocalLoadPos = 0;
		ivBroadcastScroll.setVisibility(View.GONE);
		broadcastAdapter.clearList();
		mHandler.removeCallbacksAndMessages(null);
		tv_broadcast.setText(textid);//设置标题
		rememberClickPos = pos;
		setSelectOperate();
		BaseUtils.hidePopupWindow(popWindow);
		plv_broadcast.getRefreshableView().setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	}

	//轩圈动画,顺45度
	private void rotate(boolean isReverse) {
		Animation animation = null;
		if (isReverse) {//反转
			animation = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_rotate_180);
		} else {//顺转
			animation = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_rotate_180_back);
		}
		animation.setFillAfter(true);
		iv_arrow.startAnimation(animation);
	}

	private PopupWindow popWindow;
	private TextView tv_pop_all_broadcast;
	private ImageView iv_pop_all_broadcast;
	private TextView tv_pop_my_broadcast;
	private ImageView iv_pop_my_broadcast;
	private TextView tv_pop_about_me_broadcast;
	private ImageView iv_pop_about_me_broadcast;
	private TextView tv_pop_about_me_broadcast_count;

	/**
	 * 弹出选择框
	 * @author jeff
	 *
	 */
	private void initPopuptWindow() {//广播悬浮框选择
		// 获取自定义布局文件xml的视图
		View popview = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_select_broadcast_type, null, false);
		popWindow = new PopupWindow(popview, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		popWindow.getContentView().setFocusableInTouchMode(true);
		popWindow.getContentView().setFocusable(true);
		// 设置动画效果
		popWindow.setAnimationStyle(R.style.anim_popwindow_alpha);
		popview.findViewById(R.id.pop_broadcast_all_rl).setOnClickListener(this);
		popview.findViewById(R.id.pop_broadcast_my_rl).setOnClickListener(this);
		popview.findViewById(R.id.pop_broadcast_about_me_rl).setOnClickListener(this);
		popview.findViewById(R.id.rl_pop_braodcast_root).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BaseUtils.hidePopupWindow(popWindow);

				return false;
			}
		});
		popWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				rotate(false);
			}
		});
		tv_pop_all_broadcast = (TextView) popview.findViewById(R.id.pop_broadcast_all_tv);
		iv_pop_all_broadcast = (ImageView) popview.findViewById(R.id.pop_broadcast_all_iv);
		tv_pop_my_broadcast = (TextView) popview.findViewById(R.id.pop_broadcast_my_tv);
		iv_pop_my_broadcast = (ImageView) popview.findViewById(R.id.pop_broadcast_my_iv);
		tv_pop_about_me_broadcast = (TextView) popview.findViewById(R.id.pop_broadcast_about_me_tv);
		iv_pop_about_me_broadcast = (ImageView) popview.findViewById(R.id.pop_broadcast_about_me_iv);
		tv_pop_about_me_broadcast_count = (TextView) popview.findViewById(R.id.pop_broadcast_num);
		if (BAApplication.mLocalUserInfo != null) {
			int count = SharedPreferencesTools.getInstance(getActivity()).getIntValueByKeyToZero(
					GoGirlUserJson.BRAODCAST + BAApplication.mLocalUserInfo.uid.intValue());
			if (count > 0) {
				tv_pop_about_me_broadcast_count.setText(String.valueOf(count));
				tv_pop_about_me_broadcast_count.setVisibility(View.VISIBLE);
			} else {
				tv_pop_about_me_broadcast_count.setVisibility(View.GONE);
			}
		}
	}

	private void setBroadCastDataShow(PullToRefreshListView listView, MainBroadCastAdapter adapter, int refreshCode, BroadcastInfoList lists,
			int isEnd) {
		if (listView != null) {
			listView.onRefreshComplete();
			if (lists != null && !lists.isEmpty()) {
				if (refreshCode == BroadCastBiz.REFRESH_CODE) {
					adapter.clearList();
				}
				adapter.appendPositionToList(0, lists);
				if (refreshCode == BroadCastBiz.REFRESH_CODE) {
					listView.getRefreshableView().setSelection(listView.getBottom());
				} else {
					if (adapter.getCount() > lists.size()) {
						listView.getRefreshableView().setSelection(lists.size());
					} else {
						listView.getRefreshableView().setSelection(lists.size() - 1);
					}
				}
				adapter.notifyDataSetInvalidated();
				if (isEnd == 1) {
					listView.setMode(Mode.PULL_FROM_END);
				} else {
					listView.setMode(Mode.BOTH);
				}
				tv_broadcast_no_data.setText(R.string.str_no_data);
			}
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {//被销毁了数据恢复
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			rememberClickPos = savedInstanceState.getInt("rememberClickPos");
			currentPage = savedInstanceState.getInt("currentPage");
			mViewPager.setCurrentItem(currentPage);
			startLocalLoadPos = 0;
			setMainView(currentPage);
			//			refreshData(true);

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {//销毁了数据保存
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putInt("rememberClickPos", rememberClickPos);
			outState.putInt("currentPage", currentPage);
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		BaseUtils.cancelDialog();
		switch (msg.what) {

		case HandlerValue.MAIN_BROADCAST_MAGIC_VALUE://仙术回放
			int magicValue = msg.arg1;
			BroadcastInfo broadcastInfo = (BroadcastInfo) msg.obj;
			clearQueue();
			if (magicValue == WriteBroadCastBiz.MAGIC_ONE_VALUE) {//流星雨动画
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_STARFALLFLAKE_VALUE, mHandler, broadcastInfo));
			} else if (magicValue == WriteBroadCastBiz.MAGIC_TWO_VALUE) {//万剑阵动画
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_ARROW_VALUE, mHandler, broadcastInfo));
			} else if (magicValue == WriteBroadCastBiz.MAGIC_THREE_VALUE) {//鸿毛雨动画
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_FEATHER_VALUE, mHandler, broadcastInfo));
			} else if (magicValue == WriteBroadCastBiz.MAGIC_FOUR_VALUE) {//玫瑰花语动画
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_ROSE_RAIN_VALUE, mHandler, broadcastInfo));
			} else if (magicValue == WriteBroadCastBiz.MAGIC_FIVE_VALUE) {//一箭钟情
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_FIVE_VALUE, mHandler, broadcastInfo));
			} else if (magicValue == WriteBroadCastBiz.MAGIC_SIX_VALUE) {//变变变
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_SIX_VALUE, mHandler, broadcastInfo));
			} else if (magicValue == WriteBroadCastBiz.MAGIC_SEVEN_VALUE) {//真爱永恒
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_SEVEN_VALUE, mHandler, broadcastInfo));
			} else if (magicValue == WriteBroadCastBiz.MAGIC_EIGHT_VALUE) {//烈焰红唇
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_EIGHT_VALUE, mHandler, broadcastInfo));
			} else if (magicValue == WriteBroadCastBiz.MAGIC_NINE_VALUE) {//天马流星拳
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_NINE_VALUE, mHandler, broadcastInfo));
			} else if (magicValue == WriteBroadCastBiz.MAGIC_TEN_VALUE) {//甜蜜热气球
				addRequest(new BroadcastMessage(HandlerValue.BROADCAST_TEN_VALUE, mHandler, broadcastInfo));
			}
			break;
		case HandlerValue.BROADCAST_SHOW_ANIMATION_CONTINUE_VALUE://有动画继续
			if (!showEntityLists.isEmpty()) {
				showEntityLists.removeFirst();
				ll_animation_bg.setVisibility(View.GONE);
			}

			if (!showEntityLists.isEmpty()) {
				tv_sendUserName.setText(new String(showEntityLists.get(0).fromnick));
				tv_receiveUserName.setText(new String(showEntityLists.get(0).tonick));
				int giftNum = showEntityLists.get(0).giftnum.intValue();
				imageLoader.displayImage("http://" + new String(showEntityLists.get(0).giftpickey) + BAConstants.LOAD_180_APPENDSTR, iv_showGift,
						giftOptions);

				ShowGiftNumUtils.showImageCount(iv_count_four, iv_count_third, iv_count_second, iv_count_first, giftNum);
				ShowGiftNumUtils.showImageCount1(iv_count_seven, iv_count_six, iv_count_five, showEntityLists.get(0).series.intValue());
				ll_animation_bg.setVisibility(View.VISIBLE);
				ll_animation_bg.startAnimation(animFirst);
			} else {
				ll_animation_bg.setVisibility(View.GONE);
				rl_broadcast_show_animation.setVisibility(View.GONE);
			}
			break;
		case HandlerValue.BROADCAST_SHOW_ANIMATION_VALUE:
			if (!showEntityLists.isEmpty()) {
				if (animationViewStub == null) {
					animationViewStub = viewStub.inflate();
					rl_broadcast_show_animation = (RelativeLayout) animationViewStub.findViewById(R.id.rl_broadcast_show_animation);
					tv_sendUserName = (TextView) animationViewStub.findViewById(R.id.tv_animation_sendname);
					tv_receiveUserName = (TextView) animationViewStub.findViewById(R.id.tv_animation_receivename);
					iv_showGift = (ImageView) animationViewStub.findViewById(R.id.iv_gift_one);
					iv_count_first = (ImageView) animationViewStub.findViewById(R.id.iv_first);
					iv_count_second = (ImageView) animationViewStub.findViewById(R.id.iv_second);
					iv_count_third = (ImageView) animationViewStub.findViewById(R.id.iv_third);
					iv_count_four = (ImageView) animationViewStub.findViewById(R.id.iv_forth);
					iv_count_five = (ImageView) animationViewStub.findViewById(R.id.iv_five);
					iv_count_six = (ImageView) animationViewStub.findViewById(R.id.iv_six);
					iv_count_seven = (ImageView) animationViewStub.findViewById(R.id.iv_seven);
					ll_animation_bg = (LinearLayout) animationViewStub.findViewById(R.id.ll_bg_anim);
				}
				if (rl_broadcast_show_animation.getVisibility() == View.GONE && !showEntityLists.isEmpty()) {
					rl_broadcast_show_animation.setVisibility(View.VISIBLE);
					tv_sendUserName.setText(new String(showEntityLists.get(0).fromnick));
					tv_receiveUserName.setText(new String(showEntityLists.get(0).tonick));
					int giftNum = showEntityLists.get(0).giftnum.intValue();

					imageLoader.displayImage("http://" + new String(showEntityLists.get(0).giftpickey) + BAConstants.LOAD_180_APPENDSTR, iv_showGift,
							giftOptions);
					ShowGiftNumUtils.showImageCount(iv_count_four, iv_count_third, iv_count_second, iv_count_first, giftNum);
					ShowGiftNumUtils.showImageCount1(iv_count_seven, iv_count_six, iv_count_five, showEntityLists.get(0).series.intValue());
					ll_animation_bg.startAnimation(animFirst);
					ll_animation_bg.setVisibility(View.VISIBLE);

				}
			}
			break;
		case HandlerValue.MAIN_BROADCAST_DELAY_LOAD_DATA_VALUE:
			viewBroadcast = getActivity().getLayoutInflater().inflate(R.layout.view_broadcast, null);//广播view
			viewStub = (ViewStub) viewBroadcast.findViewById(R.id.viewstub_braodcast_animation);
			plv_broadcast = (PullToRefreshListView) viewBroadcast.findViewById(R.id.pulltorefreshlistview);
			plv_broadcast.setAdapter(broadcastAdapter);
			plv_broadcast.setOnScrollListener(this);
			plv_broadcast.setMode(Mode.BOTH);
			plv_broadcast.setOnRefreshListener(new PullToRefreshListener(PullToRefreshListener.BROADCAST_FLAG));
			plv_broadcast.getRefreshableView().setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			plv_broadcast.setOnPullEventListener(new OnPullEventChangeRefreshTextListener(getActivity()));//更改上下刷新的文字

			ivBroadcastScroll = (ImageView) viewBroadcast.findViewById(R.id.iv_broadcast_scroll_bottom);
			ivBroadcastScroll.setOnClickListener(this);
			tv_broadcast_no_data = (TextView) viewBroadcast.findViewById(R.id.tv_empty_data);
			plv_broadcast.setEmptyView(tv_broadcast_no_data);
			autoPager = (AutoScrollViewPager) viewBroadcast.findViewById(R.id.autopager_topbroadcast);
			autoPager.setAdapter(topBroadCastAdapter);
			autoPager.setInterval(5000);//设置为5秒开始滚动
			autoPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
			autoPager.setOnPageChangeListener(new OnPageListener(OnPageListener.TOP_FLAG));
			pageControlView = (PageControlView) viewBroadcast.findViewById(R.id.pageControlView);
			RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
			lps.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.autopager_topbroadcast);
			lps.bottomMargin = 3;
			pageControlView.setLayoutParams(lps);
			rlTopBroadCast = (RelativeLayout) viewBroadcast.findViewById(R.id.ll_top_broadcast);
			fl_redpacket = viewBroadcast.findViewById(R.id.fl_redpacket);
			iv_redpacket_icon = viewBroadcast.findViewById(R.id.iv_redpacket_icon);
			iv_redpacket = (ImageView) viewBroadcast.findViewById(R.id.iv_redpacket);
			iv_redpacket_icon.setOnClickListener(this);

			fl_hall_redpacket = viewBroadcast.findViewById(R.id.fl_hall_redpacket);
			iv_hall_redpacket_icon = viewBroadcast.findViewById(R.id.iv_hall_redpacket_icon);
			iv_hall_redpacket = (ImageView) viewBroadcast.findViewById(R.id.iv_hall_redpacket);
			iv_hall_redpacket_icon.setOnClickListener(this);

			viewGame = getActivity().getLayoutInflater().inflate(R.layout.view_game_show, null);//游戏场view
			ivGameScroll = (ImageView) viewGame.findViewById(R.id.iv_broadcast_scroll_bottom);
			ivGameScroll.setOnClickListener(this);
			rl_adv = (RelativeLayout) viewGame.findViewById(R.id.rl_show_adv);
			autoAdvPager = (AutoScrollViewPager) viewGame.findViewById(R.id.autopager_show_adv);
			advAdapter = new TopAdvAdapter(getActivity());
			autoAdvPager.setAdapter(advAdapter);
			autoAdvPager.setInterval(3000);//设置为5秒开始滚动
			autoAdvPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
			autoAdvPager.setOnPageChangeListener(new OnPageListener(OnPageListener.TOP_ADV_FLAG));
			pageTopAdvView = (PageControlView) viewGame.findViewById(R.id.pageControlView_show_adv);
			viewGame.findViewById(R.id.tv_open_show).setOnClickListener(this);

			plv_game = (PullToRefreshListView) viewGame.findViewById(R.id.pulltorefreshlistview);
			plv_game.setMode(Mode.BOTH);
			plv_game.setAdapter(gameAdapter);
			plv_game.setOnScrollListener(this);
			plv_game.setOnRefreshListener(new PullToRefreshListener(PullToRefreshListener.GAME_FLAG));
			plv_game.getRefreshableView().setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			plv_game.setOnPullEventListener(new OnPullEventChangeRefreshTextListener(getActivity()));//更改上下刷新的文字
			tv_game_no_data = (TextView) viewGame.findViewById(R.id.tv_empty_data);
			plv_game.setEmptyView(tv_game_no_data);
			ll_open_show = (LinearLayout) viewGame.findViewById(R.id.ll_show_room);
			int screenWidth = BasePhone.getScreenWidth(getActivity());
			int height = (screenWidth - BaseUtils.dip2px(getActivity(), 70)) / 4 + BaseUtils.dip2px(getActivity(), 10);
			ll_open_show.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));
			hListView = (HorizontalListView) viewGame.findViewById(R.id.broad_show_head_hzlistview);
			showRoomAdapter = new ShowRoomsAdapter(getActivity());
			hListView.setAdapter(showRoomAdapter);
			hListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					showRoomInfoTmp = (ShowRoomInfo) parent.getAdapter().getItem(position);
					if (showRoomInfoTmp != null) {
						if (showRoomInfoTmp.getRoomid() > 0 && showRoomInfoTmp.getOwneruserinfo().getUid() > 0) {//在秀的房间才处理点击事件
							if (BAApplication.showRoomInfo == null) {//第一次点击进房间，进入房间后调用进入秀场接口
								BaseUtils.showDialog(getActivity(), "获取房间信息...");
								roomsGetBiz.getSingleRoomInfo(showRoomInfoTmp.getRoomid(), showRoomInfoTmp.getOwneruserinfo().getUid());
							} else {//已在房间内
								if (showRoomInfoTmp.getOwneruserinfo().getUid() == BAApplication.showRoomInfo.getOwneruserinfo().getUid()) {//同一个房间，再次进入，不再调用进入秀场操作
									BAApplication.showRoomInfo = showRoomInfoTmp;
									closeOperate();
									BaseUtils.openActivity(getActivity(), PeipeiShowActivity.class);
								} else {
									if (BAApplication.showRoomInfo.getOwneruserinfo().getUid() == BAApplication.mLocalUserInfo.uid.intValue()) {//不同房间，且所在房间是自己开的，切换到其他房间
										new FinshShowDialog(getActivity(), R.string.str_show_change_room, R.string.ok, R.string.cancel, roomsGetBiz)
												.showDialog();
									} else {//切换到其他房间，调用退出秀场接口，在接口返回中进入秀场
										roomsGetBiz.InOutRooms(InOutAct.out, BAApplication.showRoomInfo.getRoomid(),
												BAApplication.mLocalUserInfo.uid.intValue());
									}
								}
							}
						} else {
							BaseUtils.showTost(getActivity(), "该房间暂时没人开秀");
						}
					}
				}
			});

			views.add(viewBroadcast);
//			views.add(viewGame);

			ChatEmojiPagerAdapter mEmojiAdapter = new ChatEmojiPagerAdapter(views);
			mViewPager.setAdapter(mEmojiAdapter);
			mViewPager.setCurrentItem(0);
			mViewPager.setOffscreenPageLimit(2);
			mViewPager.setOnPageChangeListener(new OnPageListener(OnPageListener.BROADCAST_FLAG));

			BaseUtils.showDialog(getActivity(), R.string.loading);
			getBroadCastList(BroadCastBiz.ALL_BROADCAST_TYPE, true);
			getBroadCastList(BroadCastBiz.TOP_BROADCAST_TYPE, true);
//			getHallRedpacketAvail();
			roomsGetBiz.getEnterBroadcast();
			break;

		case HandlerValue.CACHE_BRAODCAST_ALL_VALUE://缓存的广播数据
		case HandlerValue.MAIN_BROADCAST_ALL_LIST_SUCCESS://获取到的网络所有广播数据
			setBroadCastDataShow(plv_broadcast, broadcastAdapter, msg.arg2, (BroadcastInfoList) msg.obj, msg.arg1);
			break;
		case HandlerValue.MAIN_BROADCAST_MY_DATA_LIST:
		case HandlerValue.MAIN_BROADCAST_ABOUT_ME_LIST:
			setBroadCastDataShow(plv_broadcast, broadcastAdapter, msg.arg2, (BroadcastInfoList) msg.obj, msg.arg1);
			break;
		case HandlerValue.CACHE_BROADCAST_TOP_VALUE:
		case HandlerValue.MAIN_BROADCAST_TOP_LIST_SUCCESS:
			BroadcastInfoList topLists = (BroadcastInfoList) msg.obj;
			if (rlTopBroadCast != null && topLists != null) {
				int size = topLists.size();
				rlTopBroadCast.setVisibility(View.VISIBLE);
				Collections.reverse(topLists);
				topBroadCastAdapter.setList(topLists);
				pageControlView.count = size;
				if (size > 1) {
					autoPager.startAutoScroll();
				} else {
					autoPager.stopAutoScroll();
				}
				if (autoPager.getCurrentItem() == 0) {
					//						long startTime = System.currentTimeMillis();
					autoPager.setCurrentItem(5 * size);//用来显示
					//						System.out.println("这个时间差====" + (System.currentTimeMillis() - startTime));
				}
			}
			break;
		case HandlerValue.CACHE_BROADCAST_GAME_VALUE:
		case HandlerValue.MAIN_BROADCAST_GAME_LIST_SUCCESS:
			setBroadCastDataShow(plv_game, gameAdapter, msg.arg2, (BroadcastInfoList) msg.obj, msg.arg1);
			break;
		case HandlerValue.MAIN_BROADCAST_APPEND_ABOUT_ME_DATA:
		case HandlerValue.MAIN_BROADCAST_APPEND_ALL_DATA://追加所有广播
		case HandlerValue.MAIN_BROADCAST_APPEND_MY_DATA:
			broadcastAdapter.appendToList((BroadcastInfo) msg.obj);
			if (isShowBroadcastBottom) {
				/**
				 * DOTO 界面销毁会报空指针
				 */
				if (plv_broadcast != null) {
					plv_broadcast.getRefreshableView().setSelection(broadcastAdapter.getCount() - 1);
				}
			}
			break;
		case HandlerValue.MAIN_BROADCAST_APPEND_GAME_DATA://追加游戏广播
			gameAdapter.appendToList((BroadcastInfo) msg.obj);
			if (isShowGameBottom) {
				if (plv_game == null) {
					return;
				}
				plv_game.getRefreshableView().setSelection(gameAdapter.getCount() - 1);
			}
			break;
		case HandlerValue.MAIN_BROADCAST_APPEND_TOP_DATA://追加置顶广播
			List<BroadcastInfo> infoLists = topBroadCastAdapter.getmList();
			if (!ListUtils.isEmpty(infoLists)) {
				int size = ListUtils.getSize(infoLists);
				if (size >= 3) {
					topBroadCastAdapter.removePos(2);
				}
				topBroadCastAdapter.appendPositionToList(0, (BroadcastInfo) msg.obj);
				//				int currentItem = autoPager.getCurrentItem();
				//				if (currentItem != 0) {
				//					autoPager.setCurrentItem(autoPager.getCurrentItem() + 2 * size - currentItem % size);//选择到最新的置顶广播项
				//				}
			}
			break;
		case HandlerValue.MAIN_BROADCAST_RECEIVE_NEW_BROADCAST://收到了新的广播
			showAboutMeCount();
			break;
		case HandlerType.STATE_END_PLAYING://播放完了
			if (!isPlayVoiceTop) {
				broadcastAdapter.setPlayFileName("");
			} else {
				topBroadCastAdapter.setPlayFileName("");
				autoPager.startAutoScroll();
			}
			break;
		case HandlerValue.BROADCAST_VOIDE_LOAD_COMPLETE_PLAY_VALUE://语音下载完了，可以开始听了
			String filePath = msg.obj.toString();

			if (msg.arg1 == ItemVoiceClickListener.VOICE_COMMON_BC) {
				if (!isPlayVoiceTop && mVoiceRecod.isPlaying() && strPlayingVoicePath.equals(filePath)) {
					broadcastAdapter.setPlayFileName("");
					mVoiceRecod.stop();
				} else {
					mVoiceRecod.setShow(false);
					mVoiceRecod.startPlayback(filePath, -1);
					strPlayingVoicePath = filePath;
					broadcastAdapter.setPlayFileName(new File(filePath).getName());
				}
				if (isPlayVoiceTop) {
					autoPager.startAutoScroll();
					topBroadCastAdapter.setPlayFileName("");
				}
				isPlayVoiceTop = false;

			} else if (msg.arg1 == ItemVoiceClickListener.VOICE_TOP_BC) {

				if (isPlayVoiceTop && mVoiceRecod.isPlaying() && strPlayingVoicePath.equals(filePath)) {
					topBroadCastAdapter.setPlayFileName("");
					autoPager.startAutoScroll();
					mVoiceRecod.stop();
				} else {
					mVoiceRecod.setShow(false);
					mVoiceRecod.startPlayback(filePath, -1);
					strPlayingVoicePath = filePath;
					topBroadCastAdapter.setPlayFileName(new File(filePath).getName());
					autoPager.stopAutoScroll();
				}
				isPlayVoiceTop = true;
				broadcastAdapter.setPlayFileName("");

			}
			break;
		case HandlerValue.BROADCAST_VOIDE_PB_GONE_VALUE:
			ProgressBar pb = (ProgressBar) msg.obj;
			pb.setVisibility(View.GONE);
			break;
		case HandlerValue.BROADCAST_BIG_GIFT:
			showPopupWindowBig();
			break;
		case HandlerValue.BROADCAST_CASTLE:
			showPopupWindowCastle();
			break;
		case HandlerValue.BROADCAST_DECREE_VALUE://圣旨动画
			new BroadcastDecreeAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, (BroadcastInfo) msg.obj, broadcast,
					mHandler).showDialog();
			break;
		case HandlerValue.BROADCAST_FIREWORD_VALUE:
			new FireAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_ROSE_VALUE:
			new RoseAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_CROWN_VALUE:
			BroadcastInfo crownInfo = (BroadcastInfo) msg.obj;
			new CrownAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, crownInfo, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_CHRISTMAS_VALUE:
			BroadcastInfo christmasInfo = (BroadcastInfo) msg.obj;
//			new ChristmasAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, christmasInfo, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_YUANDAN_VALUE:
			BroadcastInfo yuanInfo = (BroadcastInfo) msg.obj;
//			new YuanDanAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, yuanInfo, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_FIRE_VALUE:
			new FireCrackersAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_COUPLET_VALUE:
			BroadcastInfo coupletInfo = (BroadcastInfo) msg.obj;
			new CoupletAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, coupletInfo, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_HAPPY_NEW_YEAR_VALUE:
			new HappyNewYearAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_ARCHWAY_VALUE:
			BroadcastInfo archwayInfo = (BroadcastInfo) msg.obj;
			new ArchwayAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, archwayInfo, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_FLOWER_SEA_VALUE:
			BroadcastInfo flowerInfo = (BroadcastInfo) msg.obj;
			new ValentineDayDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, flowerInfo, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_REDPACKET:
			new RedpacketAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_STARFALLFLAKE_VALUE://流星雨
			BroadcastInfo info = (BroadcastInfo) msg.obj;
			int touid = 50001;
			if (info.tousers.size() > 0) {
				GoGirlUserInfo touser = (GoGirlUserInfo) info.tousers.get(0);
				touid = touser.uid.intValue();
			}
			new StarFallFlakeAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, touid, info.senduser.uid.intValue(),
					broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_FEATHER_VALUE:
			BroadcastInfo info1 = (BroadcastInfo) msg.obj;
			int touid1 = 50001;
			if (info1.tousers.size() > 0) {
				GoGirlUserInfo touser = (GoGirlUserInfo) info1.tousers.get(0);
				touid1 = touser.uid.intValue();
			}
			new FeatherAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, touid1, info1.senduser.uid.intValue(), broadcast)
					.showDialog();
			break;
		case HandlerValue.BROADCAST_ARROW_VALUE://万箭阵
			new ArrowAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_ROSE_RAIN_VALUE://玫瑰花雨
			new RoseRainAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_FIVE_VALUE://一箭种情
			BroadcastInfo info2 = (BroadcastInfo) msg.obj;
			int touid2 = 50001;
			if (info2.tousers.size() > 0) {
				GoGirlUserInfo touser = (GoGirlUserInfo) info2.tousers.get(0);
				touid2 = touser.uid.intValue();
			}
			new HeartAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, touid2, info2.senduser.uid.intValue(), broadcast)
					.showDialog();
			break;
		case HandlerValue.BROADCAST_SIX_VALUE://变变变
			BroadcastInfo info3 = (BroadcastInfo) msg.obj;
			int touid3 = 50001;
			if (info3.tousers.size() > 0) {
				GoGirlUserInfo touser = (GoGirlUserInfo) info3.tousers.get(0);
				touid3 = touser.uid.intValue();
			}
			new ChangeAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, touid3, broadcast).showDialog();

			break;
		case HandlerValue.BROADCAST_SEVEN_VALUE://真爱永恒
			BroadcastInfo info4 = (BroadcastInfo) msg.obj;
			int touid4 = 50001;
			if (info4.tousers.size() > 0) {
				GoGirlUserInfo touser = (GoGirlUserInfo) info4.tousers.get(0);
				touid4 = touser.uid.intValue();
			}
			new LoveOathAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, touid4, info4.senduser.uid.intValue(), broadcast)
					.showDialog();
			break;
		case HandlerValue.BROADCAST_EIGHT_VALUE://烈焰红唇
			BroadcastInfo info5 = (BroadcastInfo) msg.obj;
			int touid5 = 50001;
			if (info5.tousers.size() > 0) {
				GoGirlUserInfo touser = (GoGirlUserInfo) info5.tousers.get(0);
				touid5 = touser.uid.intValue();
			}
			new LipsAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, touid5, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_NINE_VALUE://天马流星拳
			BroadcastInfo info6 = (BroadcastInfo) msg.obj;
			int touid6 = 50001;
			if (info6.tousers.size() > 0) {
				GoGirlUserInfo touser = (GoGirlUserInfo) info6.tousers.get(0);
				touid6 = touser.uid.intValue();
			}
			new FistAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, touid6, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_TEN_VALUE://甜蜜热气球
			BroadcastInfo info7 = (BroadcastInfo) msg.obj;
			int touid7 = 50001;
			if (info7.tousers.size() > 0) {
				GoGirlUserInfo touser = (GoGirlUserInfo) info7.tousers.get(0);
				touid7 = touser.uid.intValue();
			}
			new BalloonAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, touid7, info7.senduser.uid.intValue(), broadcast)
					.showDialog();
			break;
		case WriteBroadCastBiz.MAGIC_ELEVEN_VALUE:
			EnterBroadcastInfo enterInfo = (EnterBroadcastInfo) msg.obj;
			new CarAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, enterInfo, broadcast).showDialog();
			break;
		case HandlerValue.BROADCAST_GIFT_END:
			BaseUtils.hidePopupWindow(mPopupWindow);
			mPopupWindow = null;
			broadcast.setSynchWork(false);
			break;
		case HandlerType.CHAT_FINGER_START:
			FingerGuessingInfo fingerInfo = (FingerGuessingInfo) msg.obj;
			writeBiz.sendFingerBroadcast(getActivity(), fingerInfo.uid1.intValue(), msg.arg1, fingerInfo.ante.intValue(), new String(
					fingerInfo.globalid), fingerInfo.antetype.intValue());
			break;
		case HandlerValue.BROADCAST_SEND_SUCCESS_VALUE:
			if (msg.arg1 == 0) {
				FingerGuessingInfo fingerInfo1 = (FingerGuessingInfo) msg.obj;
				String content = null;

				if (fingerInfo1.winuid.intValue() == fingerInfo1.uid1.intValue()) {
					if (fingerInfo1.ante.intValue() != 0) {
						String monkey = fingerInfo1.ante.intValue() + "金币";
						if (fingerInfo1.antetype.intValue() == 1) {
							monkey = fingerInfo1.ante.intValue() + "银币";
						}
						content = String.format(getActivity().getString(R.string.str_finger_winner_content), new String(fingerInfo1.nick1), monkey);
					} else {
						content = String.format(getActivity().getString(R.string.finger_winner_content1), new String(fingerInfo1.nick1));
					}
				} else if (fingerInfo1.winuid.intValue() == fingerInfo1.uid2.intValue()) {
					if (fingerInfo1.ante.intValue() != 0) {
						String monkey = fingerInfo1.ante.intValue() + "金币";
						if (fingerInfo1.antetype.intValue() == 1) {
							monkey = fingerInfo1.ante.intValue() + "银币";
						}
						content = String.format(getActivity().getString(R.string.str_finger_winner_content), new String(fingerInfo1.nick2), monkey);
					} else {
						content = String.format(getActivity().getString(R.string.finger_winner_content1), new String(fingerInfo1.nick2));
					}
				} else {
					content = getActivity().getString(R.string.finger_winner_content2);
				}

				new BroadcastFingerWinDialog(getActivity(), content, fingerInfo1.uid1.intValue(), fingerInfo1.uid2.intValue(),
						fingerInfo1.finger1.intValue(), fingerInfo1.finger2.intValue(), fingerInfo1.winuid.intValue(), mHandler).showDialog();
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_GAME_NO_EXIST
					|| msg.arg1 == BAConstants.rspContMsgType.E_GG_FINGER_GUESSING_TIMEOUT) {
				BaseUtils.showTost(getActivity(), R.string.str_chat_gueess_finger_failed);
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_GRADE) {//等级不够
				BaseUtils.showTost(getActivity(), "等级不够，您还不能够发送圣旨/懿旨");
			} else if (msg.arg1 == rspContMsgType.E_GG_NOT_ENGOUH_WELTH) {//财富不够
				new participatePromptDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够
				new participatePromptDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			}
			break;
		case HandlerValue.MAIN_BROADCAST_TOP_ADV_VALUE://秀场广告位
			if (rl_adv == null) {
				return;
			}
			String url = SharedPreferencesTools.getInstance(getActivity()).getStringValueByKey(BAConstants.PEIPEI_SHOW_ADV_URL);
			if (TextUtils.isEmpty(url)) {
				rl_adv.setVisibility(View.GONE);
			} else {
				if (BAApplication.mLocalUserInfo != null) {
					String verifystr = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "")
							.getStringValueByKey(SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
					if (!TextUtils.isEmpty(verifystr)) {
						rl_adv.setVisibility(View.VISIBLE);
						if (advAdapter != null) {
							try {
								JSONArray jsonArray = new JSONArray(url);
								AdvBean[] beans = AdvBeanFunctions.getAdvBean(jsonArray);
								if (beans == null || beans.length == 0) {
									rl_adv.setVisibility(View.GONE);
								} else {
									rl_adv.setVisibility(View.VISIBLE);
								}
								advAdapter.setList(beans);
								if (beans != null && beans.length > 1) {
									pageTopAdvView.count = beans.length;
									autoAdvPager.startAutoScroll();
								} else {
									autoAdvPager.stopAutoScroll();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					} else {
						rl_adv.setVisibility(View.GONE);
					}
				}
			}
			break;
		case HandlerValue.SHOW_ROOM_LISTS_VALUE://获取到了秀场列表
			List<ShowRoomInfo> roomInfoLists = (List<ShowRoomInfo>) msg.obj;
			showRoomAdapter.setList(roomInfoLists);
			for (ShowRoomInfo showRoomInfo : roomInfoLists) {
				showRoomInfo.getOwneruserinfo();
			}
			break;
		case HandlerValue.SHOW_ROOM_OPEN_SHOW://开秀返回
			if (msg.arg1 == 0) {
				BAApplication.clearShow();
				ShowRoomInfo showRoomInfo = (ShowRoomInfo) msg.obj;
				BAApplication.showRoomInfo = showRoomInfo;
				if (BAApplication.showRoomInfo != null) {
					closeOperate();
					BaseUtils.openActivity(getActivity(), PeipeiShowActivity.class);
				} else {
					BaseUtils.showTost(getActivity(), "开秀失败");
				}
			} else if (msg.arg1 == ProtobufErrorCode.ShowRoomFullError) {
				new HintToastDialog(getActivity(), R.string.str_show_rooms_full, R.string.ok).showDialog();
			} else if (msg.arg1 == ProtobufErrorCode.OpenShowLessGradeError) {
				new HintToastDialog(getActivity(), R.string.str_show_level_low, R.string.ok).showDialog();
			} else if (msg.arg1 == ProtobufErrorCode.OpenShowIsBusyError) {
				new HintToastDialog(getActivity(), R.string.str_show_open_busy, R.string.ok).showDialog();
			} else if (msg.arg1 == ProtobufErrorCode.IsShowingError) {
				ShowRoomInfo showRoomInfo = (ShowRoomInfo) msg.obj;
				BAApplication.showRoomInfo = showRoomInfo;
				if (BAApplication.showRoomInfo != null) {
					BaseUtils.showDialog(getActivity(), "获取房间信息...");
					roomsGetBiz.getSingleRoomInfo(showRoomInfo.getRoomid(), showRoomInfo.getOwneruserinfo().getUid());
				}
			} else {
				BaseUtils.showTost(getActivity(), "开秀失败");
			}
			break;
		case HandlerValue.SHOW_ROOM_IN_OUT:
			BAApplication.clearShow();
			if (msg.arg1 == 0) {
				if (msg.arg2 == InOutAct.out) {
					BaseUtils.showDialog(getActivity(), "获取房间信息...");
					if (showRoomInfoTmp != null) {
						roomsGetBiz.getSingleRoomInfo(showRoomInfoTmp.getRoomid(), showRoomInfoTmp.getOwneruserinfo().getUid());
					} else if (shareInfo != null) {
						roomsGetBiz.getSingleRoomInfo(shareInfo.roomid.intValue(), shareInfo.owneruid.intValue());
					}
				}
			} else {
				BaseUtils.showTost(getActivity(), "进入秀场失败");
			}
			break;
		case HandlerValue.SHOW_ROOM_CLOSE:
			BAApplication.clearShow();
			BaseUtils.showDialog(getActivity(), "获取房间信息...");
			if (showRoomInfoTmp != null) {
				roomsGetBiz.getSingleRoomInfo(showRoomInfoTmp.getRoomid(), showRoomInfoTmp.getOwneruserinfo().getUid());
			} else if (shareInfo != null) {
				roomsGetBiz.getSingleRoomInfo(shareInfo.roomid.intValue(), shareInfo.owneruid.intValue());
			}
			break;
		case HandlerValue.SHOW_ROOM_GET_SINGLE_ROOM:
			BaseUtils.cancelDialog();
			if (msg.arg1 == 0) {
				ShowRoomInfo roomInfo = (ShowRoomInfo) msg.obj;
				BAApplication.showRoomInfo = roomInfo;
				if (roomInfo.getCurmembernum() < roomInfo.getMaxmembernum()) {
					closeOperate();
					BaseUtils.openActivity(getActivity(), PeipeiShowActivity.class);
				} else {
					BAApplication.showRoomInfo = null;
					BaseUtils.showTost(getActivity(), "该秀场人数已满");
				}
			}
			break;
		case HandlerValue.HALL_CHECK_SOLITAIRE_REDPACKET_STATUS:
			SolitaireRedpacket solitaireRedpacket = (SolitaireRedpacket) msg.obj;
			checkSolitaireRedpacketStatus(solitaireRedpacket);
			break;
		case HandlerValue.HALL_CHECK_SOLITAIRE_REDPACKET_STATUS_SUCCESS:
			BroadcastInfo checkBroadcastInfo = (BroadcastInfo) msg.obj;
			RedPacketBetCreateInfo checkRedpacketInfo = RedDecodeUtil.decodeGetHallRedpacketRedpacket(checkBroadcastInfo);
			if (msg.arg1 == 0) {
				if (checkRedpacketInfo != null) {
					if (checkRedpacketInfo.redpacketstatus.intValue() == 0) {
						new SolitaireRedPacketInfoDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, 1, checkRedpacketInfo, mHandler)
								.showDialog();
					} else if (checkRedpacketInfo.redpacketstatus.intValue() == 1) {
						BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_open_success_by_you);
					} else if (checkRedpacketInfo.redpacketstatus.intValue() == 2) {
						BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_open_success_by_other);
					} else if (checkRedpacketInfo.redpacketstatus.intValue() == 3) {
						new SolitaireRedPacketInfoDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, 1, checkRedpacketInfo, mHandler)
								.showDialog();
					} else if (checkRedpacketInfo.redpacketstatus.intValue() == 4) {
						BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_is_overdue);
					}
				}
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_DECODE) {
				BaseUtils.showTost(getActivity(), R.string.str_system_error);
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_RED_TIMEOUT) {
				BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_is_overdue);
			} else if (msg.arg1 == rspContMsgType.E_GG_PROPERTY_LACK) {//财富不足
				new participatePromptDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够银币
				new participatePromptDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			}
			break;
		case HandlerValue.HALL_CHECK_SOLITAIRE_REDPACKET_STATUS_ERROR:
			BaseUtils.showTost(getActivity(), R.string.toast_login_failure);
			break;
		case HandlerValue.HALL_SOLITAIRE_REDPACKET_INFO_SUCCESS:
			if (0 == msg.arg1) {
				RedPacketBetInfoList redList = (RedPacketBetInfoList) msg.obj;
				BAApplication.getInstance().hallRedList = getSolitaireRedpacketListData(redList);
			} else {
				BaseUtils.showTost(getActivity(), "获取失败");
			}
			break;
		case HandlerValue.HALL_GRAB_SOLITAIRE_REDPACKET_ENSURE:
			RedPacketBetCreateInfo grapSolitaireinfo = (RedPacketBetCreateInfo) msg.obj;
			grapSolitaireRedPacket(grapSolitaireinfo);
			break;
		case HandlerValue.HALL_GRAB_SOLITAIRE_REDPACKET_SUCCESS:
			BroadcastInfo grapbroadcastInfo = (BroadcastInfo) msg.obj;
			RedPacketBetCreateInfo solitaireinfo = RedDecodeUtil.decodeGetHallRedpacketRedpacket(grapbroadcastInfo);

			if (msg.arg1 == 0) {
				if (grapbroadcastInfo.revint1.intValue() == 1) {
					fl_redpacket.setVisibility(View.GONE);
				} else {
					fl_redpacket.setVisibility(View.VISIBLE);
				}
				if (solitaireinfo != null && grapbroadcastInfo != null && grapbroadcastInfo.senduser != null) {
					if (solitaireinfo.redpacketstatus.intValue() == 1) {
						showOpenSolitaireRedpacketSuccess(solitaireinfo);
					} else if (solitaireinfo.redpacketstatus.intValue() == 2) {
						BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_open_success_by_other);
					} else if (solitaireinfo.redpacketstatus.intValue() == 3) {
						showOpenSolitaireRedpacketFail(solitaireinfo);
					} else if (solitaireinfo.redpacketstatus.intValue() == 4) {
						BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_is_overdue);
					}
				}
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_CANNOT_PARTICIPATE_REDPACKETBET) {
				BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_is_overdue);
			} else if (msg.arg1 == rspContMsgType.E_GG_PROPERTY_LACK) {//财富不足
				new participatePromptDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够银币
				new participatePromptDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_DECODE) {
				BaseUtils.showTost(getActivity(), R.string.str_system_error);
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_RED_TIMEOUT) {
				BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_is_overdue);
			}
			break;
		case HandlerValue.HALL_GRAB_SOLITAIRE_REDPACKET_ERROR:
			BaseUtils.showTost(getActivity(), R.string.toast_login_failure);
			break;
		case HandlerValue.HALL_CHECK_REDPACKET_STATUS:
			//检查大厅普通红包的状态
			HallRedpacket checkHallRedPacketInfo = (HallRedpacket) msg.obj;
			checkHallRedpacketStatus(checkHallRedPacketInfo);
			break;
		case HandlerValue.HALL_GRAB_REDPACKET_ENSURE:
			BroadcastRedPacketInfo grabHallRedpacketInfo = (BroadcastRedPacketInfo) msg.obj;
			grabHallRedpacket(grabHallRedpacketInfo);
			break;
		case HandlerValue.HALL_GRAB_REDPACKET_SEE_LUCK:
			BroadcastRedPacketInfo redpacketInfo = (BroadcastRedPacketInfo) msg.obj;
			new GrapHallRandomRedpacketLuckDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, redpacketInfo, mHandler).showDialog();
			break;
		case HandlerValue.HALL_GRAB_REDPACKET_SUCCESS:
			BroadcastInfo grabBroadcastInfo = (BroadcastInfo) msg.obj;
			BroadcastRedPacketInfo grabHallRedPacketInfo = RedDecodeUtil.decodeGetHallRedpacket(grabBroadcastInfo);
			if (msg.arg1 == 0) {
				if (grabBroadcastInfo.revint1.intValue() == 1) {
					fl_hall_redpacket.setVisibility(View.GONE);
				} else {
					fl_hall_redpacket.setVisibility(View.VISIBLE);
				}
				if (grabHallRedPacketInfo != null) {
					if (grabHallRedPacketInfo.redpacketstatus.intValue() == 1 || grabHallRedPacketInfo.redpacketstatus.intValue() == 4) {
						if (grabHallRedPacketInfo.cointype.intValue() == 0) {
							new GrapHallNormalRedpacketSuccessDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar,
									grabHallRedPacketInfo, mHandler).showDialog();
						} else {
							new GrapHallRandomRedpacketSuccessDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar,
									grabHallRedPacketInfo, mHandler).showDialog();
						}
					} else if (grabHallRedPacketInfo.redpacketstatus.intValue() == 2) {
						new HallRedpacketTimeOutDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, grabHallRedPacketInfo)
								.showDialog();
					} else if (grabHallRedPacketInfo.redpacketstatus.intValue() == 3) {
						new HallRedpacketNoMoneyDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, grabHallRedPacketInfo, mHandler)
								.showDialog();
					}
				}
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_DECODE) {
				BaseUtils.showTost(getActivity(), R.string.str_system_error);
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_RED_TIMEOUT) {
				BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_is_overdue);
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_CANNOT_PARTICIPATE_REDPACKETBET) {
				BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_is_overdue);
			}
			break;
		case HandlerValue.HALL_GRAB_REDPACKET_ERROR:
			BaseUtils.showTost(getActivity(), R.string.toast_login_failure);
			break;
		case HandlerValue.HALL_CHECK_REDPACKET_STATUS_SUCCESS:
			BroadcastInfo checkHallRedpacketInfo = (BroadcastInfo) msg.obj;
			BroadcastRedPacketInfo checkBroadcastRedPacketInfo = RedDecodeUtil.decodeGetHallRedpacket(checkHallRedpacketInfo);
			if (msg.arg1 == 0) {
				if (checkBroadcastRedPacketInfo != null) {
					if (checkBroadcastRedPacketInfo.redpacketstatus.intValue() == 0) {
						new HallRedpacketInfoDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, checkBroadcastRedPacketInfo,
								mHandler).showDialog();
					} else if (checkBroadcastRedPacketInfo.redpacketstatus.intValue() == 1
							|| checkBroadcastRedPacketInfo.redpacketstatus.intValue() == 4) {
						if (checkBroadcastRedPacketInfo.cointype.intValue() == 0) {
							new GrapHallNormalRedpacketSuccessDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar,
									checkBroadcastRedPacketInfo, mHandler).showDialog();
						} else {
							new GrapHallRandomRedpacketSuccessDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar,
									checkBroadcastRedPacketInfo, mHandler).showDialog();

						}
					} else if (checkBroadcastRedPacketInfo.redpacketstatus.intValue() == 2) {
						new HallRedpacketTimeOutDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, checkBroadcastRedPacketInfo)
								.showDialog();
					} else if (checkBroadcastRedPacketInfo.redpacketstatus.intValue() == 3) {
						new HallRedpacketNoMoneyDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, checkBroadcastRedPacketInfo,
								mHandler).showDialog();
					}
				}
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_DECODE) {
				BaseUtils.showTost(getActivity(), R.string.str_system_error);
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_RED_TIMEOUT) {
				BaseUtils.showTost(getActivity(), R.string.str_the_redpacket_is_overdue);
			}
			break;
		case HandlerValue.HALL_CHECK_REDPACKET_STATUS_ERROR:
		case HandlerValue.HALL_GET_HALL_REDPACKET_ERROR:
		case HandlerValue.HALL_GET_AVAIL_HALL_REDPACKET_LIST_ERROR:
			BaseUtils.showTost(getActivity(), R.string.toast_login_failure);
			break;
		case HandlerValue.HALL_GET_HALL_REDPACKET_AVAIL:
			if (msg.arg1 == 0) {
				if (msg.arg2 == 1) {
					fl_hall_redpacket.setVisibility(View.VISIBLE);
				} else {
					fl_hall_redpacket.setVisibility(View.GONE);
				}
			} else {
				String retMsg = (String) msg.obj;
				BaseUtils.showTost(getActivity(), retMsg);
			}
			break;
		case HandlerValue.HALL_GET_HALL_SOLITAIRE_REDPACKET_AVAIL:
			if (msg.arg1 == 0) {
				if (msg.arg2 == 1) {
					fl_redpacket.setVisibility(View.VISIBLE);
				} else {
					fl_redpacket.setVisibility(View.GONE);
				}
			} else {
				String retMsg = (String) msg.obj;
				BaseUtils.showTost(getActivity(), retMsg);
			}
			break;
		case HandlerValue.HALL_GET_AVAIL_HALL_REDPACKET_LIST_SUCCESS:
			if (msg.arg1 == 0) {
				BroadcastRedPacketInfoList redpacketList = (BroadcastRedPacketInfoList) msg.obj;
				new CanGrapHallRedpacketDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, redpacketList, mHandler).showDialog();
			} else {
				Bundle bundle = msg.getData();
				String retMsg = bundle.getString("data");
				BaseUtils.showTost(getActivity(), retMsg);
			}
			break;
		case HandlerValue.HALL_GET_AVAIL_HALL_SOLITAIRE_REDPACKET_LIST_SUCCESS:
			if (msg.arg1 == 0) {
				RedPacketBetCreateInfoList soliatireRedpacketList = (RedPacketBetCreateInfoList) msg.obj;
				new CanGrapHallSolitaireRedpacketDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, soliatireRedpacketList, mHandler)
						.showDialog();
			} else {
				Bundle bundle = msg.getData();
				String retMsg = bundle.getString("data");
				BaseUtils.showTost(getActivity(), retMsg);
			}
			break;
		case HandlerValue.HALL_GET_GONE_SOLITAIRE_REDPACKET_BUBBLE:
			fl_redpacket.setVisibility(View.GONE);
			break;
		case HandlerValue.HALL_GET_SHOW_SOLITAIRE_REDPACKET_BUBBLE:
			fl_redpacket.setVisibility(View.VISIBLE);
			break;
		case HandlerValue.HALL_GET_GONE_REDPACKET_BUBBLE:
			fl_hall_redpacket.setVisibility(View.GONE);
			break;
		case HandlerValue.HALL_GET_SHOW_REDPACKET_BUBBLE:
			fl_hall_redpacket.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void checkHallRedpacketStatus(HallRedpacket checkHallRedPacketInfo) {
		if (checkHallRedPacketInfo != null) {
			solitaireRedPacketBiz.requestCheckHallRecpacket(checkHallRedPacketInfo.getRedpacketId(), 0, this);
		}
	}

	private void grabHallRedpacket(BroadcastRedPacketInfo redpacketInfo) {
		if (redpacketInfo != null && redpacketInfo.userInfo != null) {
			solitaireRedPacketBiz.requestGrabHallRedpacket(redpacketInfo.userInfo.uid.intValue(), redpacketInfo.id.intValue(), this);
		}
	}

	private void showOpenSolitaireRedpacketFail(RedPacketBetCreateInfo solitaireinfo) {
		if (solitaireinfo == null)
			return;
		new SolitaireGrapRedPacketFailDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, solitaireinfo).showDialog();

	}

	private void showOpenSolitaireRedpacketSuccess(RedPacketBetCreateInfo solitaireinfo) {
		if (solitaireinfo == null)
			return;

		new SolitaireGrapRedPacketSuccessDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, solitaireinfo).showDialog();

	}

	private void grapSolitaireRedPacket(RedPacketBetCreateInfo grapSolitaireinfo) {
		if (grapSolitaireinfo == null || grapSolitaireinfo.userInfo == null)
			return;

		solitaireRedPacketBiz.requestGrapSolitaireRedPacket(grapSolitaireinfo.userInfo.uid.intValue(), grapSolitaireinfo.id.intValue(), 1, this);
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

	private void checkSolitaireRedpacketStatus(SolitaireRedpacket solitaireRedpacket) {
		if (solitaireRedpacket == null)
			return;

		solitaireRedPacketBiz.requestCheckRekpackeState(solitaireRedpacket.getRedpacketId(), 1, this);
	}

	private void showAboutMeCount() {//显示标题上面的小圆点
		if (UserUtils.getUserEntity(getActivity()) != null) {
			int count = SharedPreferencesTools.getInstance(getActivity()).getIntValueByKeyToZero(
					GoGirlUserJson.BRAODCAST + BAApplication.mLocalUserInfo.uid.intValue());
			if (currentPage == 1) {
				if (iv_noticeMe != null) {
					if (count > 0) {
						iv_noticeMe.setVisibility(View.VISIBLE);
					} else {
						iv_noticeMe.setVisibility(View.GONE);
					}
				}
			} else {
				iv_noticeMe.setVisibility(View.GONE);
			}
		}

	}

	private void refreshData(boolean flag) {
		broadCastBiz.pushSwitch(true);
		setSelectOperate();
		if (flag) {
			if (autoPager != null) {
				autoPager.startAutoScroll(); // start auto scroll when onResume
			}
		}
		if (currentPage == 1) {
			getBroadCastList(BroadCastBiz.GAME_BROADCAST_TYPE, true);
		} else if (currentPage == 0) {
			if (rememberClickPos == 0) {
				BaseUtils.showDialog(getActivity(), R.string.loading);
				getBroadCastList(BroadCastBiz.TOP_BROADCAST_TYPE, true);
				getBroadCastList(BroadCastBiz.ALL_BROADCAST_TYPE, true);
			} else if (rememberClickPos == 1) {
				String limit = startLocalLoadPos + "," + LOADCOUNT;
				getLocalDbData(true, 1, limit);
			} else if (rememberClickPos == 2) {
				String limit = startLocalLoadPos + "," + LOADCOUNT;
				getLocalDbData(true, 2, limit);
			}
		}
	}

	//下拉加载更多
	class PullToRefreshListener implements OnRefreshListener2<ListView> {
		public static final int GAME_FLAG = 0;
		public static final int BROADCAST_FLAG = 1;
		private int flag = GAME_FLAG;

		public PullToRefreshListener(int flag) {
			this.flag = flag;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (flag == GAME_FLAG) {
				getBroadCastList(BroadCastBiz.GAME_BROADCAST_TYPE, false);
			} else if (flag == BROADCAST_FLAG) {
				if (rememberClickPos == 0) {
					getBroadCastList(BroadCastBiz.ALL_BROADCAST_TYPE, false);
				} else if (rememberClickPos == 1) {
					startLocalLoadPos += LOADCOUNT;
					String limit = startLocalLoadPos + "," + LOADCOUNT;
					getLocalDbData(false, 1, limit);
				} else if (rememberClickPos == 2) {
					startLocalLoadPos += LOADCOUNT;
					String limit = startLocalLoadPos + "," + LOADCOUNT;
					getLocalDbData(false, 2, limit);
				}
			}
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			//			new FireAnimationDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar, broadcast).showDialog();
			if (flag == GAME_FLAG) {
				getAdv(true);
				getBroadCastList(BroadCastBiz.GAME_BROADCAST_TYPE, true);
			} else if (flag == BROADCAST_FLAG) {
				if (rememberClickPos == 0) {
					getBroadCastList(BroadCastBiz.TOP_BROADCAST_TYPE, true);
					getBroadCastList(BroadCastBiz.ALL_BROADCAST_TYPE, true);
				} else if (rememberClickPos == 1) {
					startLocalLoadPos = 0;
					String limit = startLocalLoadPos + "," + LOADCOUNT;
					getLocalDbData(true, 1, limit);
				} else if (rememberClickPos == 2) {
					startLocalLoadPos = 0;
					String limit = startLocalLoadPos + "," + LOADCOUNT;
					getLocalDbData(true, 2, limit);
				}
			}
		}

	}

	public void onEvent(final NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE51) {
			ThreadPoolService.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					String time = String.valueOf(event.getObj());//通过时间查找刚刚发送成功的那条数据
					BroadcastInfoList lists = broadCastBiz.getLocalBroadcast(new String[] { "1", BAApplication.mLocalUserInfo.uid.intValue() + "",
							time }, BroadCastColumn.STAUTS + "=? and " + BroadCastColumn.USERUID + "=? and " + BroadCastColumn.CREATETIME + "=?",
							null);
					if (!ListUtils.isEmpty(lists)) {
						BroadcastInfo info = (BroadcastInfo) lists.get(0);
						appendBroadcastData(info, false);
					}
				}
			});

		} else if (event.getFlag() == NoticeEvent.NOTICE83) {//刷新秀场
			refreshData(true);
			roomsGetBiz.getShowRoomLists();
		} else if (event.getFlag() == NoticeEvent.NOTICE84) {//点击了仙术回放
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_MAGIC_VALUE, event.getNum(), event.getNum(), event.getObj());

		} else if (event.getFlag() == NoticeEvent.NOTICE101) {
			BroadcastInfo info = (BroadcastInfo) event.getObj();
			if (info != null) {
				appendBroadcastData(info, false);
			}
		}
	}

	private LinkedList<BroadcastSeriesShowGiftInfo> showEntityLists = new LinkedList<BroadcastSeriesShowGiftInfo>();//连刷动画

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private boolean isShowBroadcastBottom = true;//是否消息一直往上推送
	private boolean isShowGameBottom = true;//是否消息一直往上推送

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE://停止滚动
			if (currentPage == 0) {
				// 判断滚动到底部  
				if (plv_broadcast.getRefreshableView().getLastVisiblePosition() == (plv_broadcast.getRefreshableView().getCount() - 1)) {
					ivBroadcastScroll.setVisibility(View.GONE);//滑动到底部
					isShowBroadcastBottom = true;
				} else {
					ivBroadcastScroll.setVisibility(View.VISIBLE);
					isShowBroadcastBottom = false;
				}
			} else if (currentPage == 1) {
				// 判断滚动到底部  
				if (plv_game.getRefreshableView().getLastVisiblePosition() == (plv_game.getRefreshableView().getCount() - 1)) {
					ivGameScroll.setVisibility(View.GONE);//滑动到底部
					isShowGameBottom = true;
				} else {
					ivGameScroll.setVisibility(View.VISIBLE);
					isShowGameBottom = false;
				}
			}

			break;
		case OnScrollListener.SCROLL_STATE_FLING://开始滚动
			//			System.out.println("这里有没有进来=======");
			if (currentPage == 0) {
				plv_broadcast.getRefreshableView().setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
				isShowBroadcastBottom = false;
			} else if (currentPage == 1) {
				plv_game.getRefreshableView().setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
				isShowGameBottom = false;
			}
			//			imageLoader.pause();
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://正在滚动
			if (currentPage == 0) {
				plv_broadcast.getRefreshableView().setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
				isShowBroadcastBottom = false;
			} else if (currentPage == 1) {
				plv_game.getRefreshableView().setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
				isShowGameBottom = false;
			}
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void getPushBroadcastData(BroadcastInfo broadcastInfo, boolean isAddMe) {//收到广播推送过来的消息
		appendBroadcastData(broadcastInfo, isAddMe);
	}

	private void appendBroadcastData(BroadcastInfo broadcastInfo, boolean isAddMe) {
		System.out.println("收到了广播======");

		boolean appendData = true;
		if (broadcastInfo != null) {
			//						addRequest(new BroadcastMessage(HandlerValue.BROADCAST_FIREWORD_VALUE, mHandler, broadcastInfo));
			int type = broadcastInfo.broadcasttype.intValue();
			if (type == WriteBroadCastBiz.TOP_BRAODCAST || type == WriteBroadCastBiz.COMMON_BROADCAST || type == 1) {//1为系统广播
				if (broadcastInfo.animationid != null) {
					if (broadcastInfo.animationid.intValue() == GiftId.GIFT_CASTLE) {
						addRequest(new BroadcastMessage(HandlerValue.BROADCAST_CASTLE, mHandler, broadcastInfo));
					} else if (broadcastInfo.animationid.intValue() == GiftId.GIFT_BIG) {
						addRequest(new BroadcastMessage(HandlerValue.BROADCAST_BIG_GIFT, mHandler, broadcastInfo));
					} else if (broadcastInfo.animationid.intValue() == GiftId.GIFT_ROSE) {
						addRequest(new BroadcastMessage(HandlerValue.BROADCAST_ROSE_VALUE, mHandler, broadcastInfo));
					} else if (broadcastInfo.animationid.intValue() == GiftId.GIFT_FIREWORK) {
						addRequest(new BroadcastMessage(HandlerValue.BROADCAST_FIREWORD_VALUE, mHandler, broadcastInfo));
					} else if (broadcastInfo.animationid.intValue() == GiftId.GIFT_CROWN) {
						addRequest(new BroadcastMessage(HandlerValue.BROADCAST_CROWN_VALUE, mHandler, broadcastInfo));
					} else if (broadcastInfo.animationid.intValue() == GiftId.GIFT_CHRISTMAS) {
						addRequest(new BroadcastMessage(HandlerValue.BROADCAST_CHRISTMAS_VALUE, mHandler, broadcastInfo));
					} else if (broadcastInfo.animationid.intValue() == GiftId.GIFT_YUANDAN) {
						addRequest(new BroadcastMessage(HandlerValue.BROADCAST_YUANDAN_VALUE, mHandler, broadcastInfo));
					}
				}
				//				broadcastInfo.b
				byte[] datalist = broadcastInfo.datalist;
				if (datalist != null && datalist.length != 0) {//圣旨动画
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
							if (datainfo.type.intValue() == MessageType.FEMALE_DECREE.getValue()
									|| datainfo.type.intValue() == MessageType.MALE_DECREE.getValue()) {
								addRequest(new BroadcastMessage(HandlerValue.BROADCAST_DECREE_VALUE, mHandler, broadcastInfo));
							} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_SERIES_SHOW_GIFT.getValue()) {

								BroadcastSeriesShowGiftInfo showGiftInfo = new BroadcastSeriesShowGiftInfo();
								BERDecoder seriesdec = new BERDecoder(datainfo.data);
								try {
									showGiftInfo.decode(seriesdec);
								} catch (ASN1Exception e1) {
									e1.printStackTrace();
								}

								if (showGiftInfo.series.intValue() > 0) {
									showEntityLists.add(showGiftInfo);
									HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.BROADCAST_SHOW_ANIMATION_VALUE);
								}
								appendData = false;
							} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_ANIMATION_BROADCAST.getValue()) {
								if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_ONE_VALUE) {//流星雨动画
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_STARFALLFLAKE_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_TWO_VALUE) {//万剑阵动画
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_ARROW_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_THREE_VALUE) {//鸿毛雨动画
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_FEATHER_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_FOUR_VALUE) {//玫瑰花语动画
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_ROSE_RAIN_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_FIVE_VALUE) {//一箭种情
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_FIVE_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_SIX_VALUE) {//变变变
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_SIX_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_SEVEN_VALUE) {//真爱永恒
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_SEVEN_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_EIGHT_VALUE) {//烈焰红唇
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_EIGHT_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_NINE_VALUE) {//天马流星拳
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_NINE_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == WriteBroadCastBiz.MAGIC_TEN_VALUE) {//甜蜜热气球
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_TEN_VALUE, mHandler, broadcastInfo));
								}
							} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET_NOT_AVAIL.getValue()) {
								HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_GONE_SOLITAIRE_REDPACKET_BUBBLE);
								return; //广播红包接龙不可用
							} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_NOT_AVAIL.getValue()) {
								HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_GONE_REDPACKET_BUBBLE);
								return;
							} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET.getValue()) {
								HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_SHOW_SOLITAIRE_REDPACKET_BUBBLE);
							} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET.getValue()) {
								HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_SHOW_REDPACKET_BUBBLE);
								if (datainfo.datainfo.intValue() == GiftId.GIFT_REDPACKET) {
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_REDPACKET, mHandler, broadcastInfo));
								}
							} else if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_SPECIAL_EFFECT.getValue()) {
								if (datainfo.datainfo.intValue() == GiftId.GIFT_FIRE) {
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_FIRE_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == GiftId.GIFT_COUPLET) {
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_COUPLET_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == GiftId.GIFT_HAPPY_NEW_YEAR) {
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_HAPPY_NEW_YEAR_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == GiftId.GIFT_ARCHWAY) {
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_ARCHWAY_VALUE, mHandler, broadcastInfo));
								} else if (datainfo.datainfo.intValue() == GiftId.GIFT_FLOWER_SEA){
									addRequest(new BroadcastMessage(HandlerValue.BROADCAST_FLOWER_SEA_VALUE, mHandler, broadcastInfo));
								}
							}
						}
						//进场动画
						EnterBroadcastInfo enterInfo = BroadCastUtils.getEnterBroadcastInfo(datainfo);
						if (enterInfo != null && enterInfo.ridingid != null) {
							if (enterInfo.ridingid.intValue() == SpaceBiz.CAR_MOTORING) {
								//								mHandler.sendEmptyMessage(WriteBroadCastBiz.MAGIC_ELEVEN_VALUE);
								HandlerUtils.sendHandlerMessage(mHandler, WriteBroadCastBiz.MAGIC_ELEVEN_VALUE, enterInfo);
							}
						}
					}
				}
				if (!appendData) {//连续刷礼物就不追加
					return;
				}
				if (isAddMe) {
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_RECEIVE_NEW_BROADCAST);
				}
				if (isAddMe && rememberClickPos == 2) {
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_APPEND_ABOUT_ME_DATA, broadcastInfo);
				} else if (rememberClickPos == 0) {
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_APPEND_ALL_DATA, broadcastInfo);
				} else if (rememberClickPos == 1) {
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_APPEND_MY_DATA, broadcastInfo);
				}
				if (type == WriteBroadCastBiz.TOP_BRAODCAST) {
					HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_APPEND_TOP_DATA, broadcastInfo);
				}
			} else if (type == BroadCastBiz.GAME_BROADCAST_TYPE) {//游戏场的
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_APPEND_GAME_DATA, broadcastInfo);
			}
		}
	}

	public void addRequest(BroadcastMessage task) {
		// int cur_task_id = mTaskid++;
		synchronized (sendQueue) {
			sendQueue.offer(task);
			sendQueue.notify();
		}
	}

	public void clearQueue() {
		synchronized (sendQueue) {
			sendQueue.clear();
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {//隐藏了当前页面
			closeOperate();
			showEntityLists.clear();
			mHandler.removeCallbacksAndMessages(null);
			BaseUtils.hidePopupWindow(mPopupWindow);
			clearQueue();
			broadcast.setSynchWork(true);
		} else {//显示了当前页面
			if (plv_broadcast == null || plv_game == null) {
				initUi(view);
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_DELAY_LOAD_DATA_VALUE);
					}
				}, 1);
			} else {
				refreshData(true);
				if (!broadcast.isAlive()) {
					broadcast.start();
				}
				broadcast.setSynchWork(false);
				roomsGetBiz.getEnterBroadcast();//进入了广播
//				getHallRedpacketAvail();
			}
		}
	}

	private String strPlayingVoicePath = "";
	private boolean isPlayVoiceTop = false;//是否播放的为置顶广播

	private class OnPageListener implements OnPageChangeListener {//page切换监听
		public static final int TOP_FLAG = 0;
		public static final int BROADCAST_FLAG = 1;
		public static final int TOP_ADV_FLAG = 2;
		private int flag = TOP_FLAG;

		public OnPageListener(int flag) {
			this.flag = flag;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			if (flag == TOP_FLAG) {
				pageControlView.generatePageControl(arg0 % 3);
				if (arg0 % 3 == 0) {
					broadCastBiz.pushSwitch(true);
				}
				if (isPlayVoiceTop && !mVoiceRecod.isShow()) {
					mVoiceRecod.stopPlayback();
					topBroadCastAdapter.setPlayFileName("");
				}
				int type = topBroadCastAdapter.getItemTypeColor(arg0);
				if (type == MessageType.FEMALE_DECREE.getValue() || type == MessageType.MALE_DECREE.getValue()) {
					autoPager.setBackgroundResource(R.drawable.broadcast_toplist_bg);
				} else {
					//					autoPager.setBackgroundResource(R.drawable.broadcast_bg_top);
					autoPager.setBackgroundColor(getResources().getColor(R.color.main_broadcast_top_item_bg));
				}
			} else if (flag == BROADCAST_FLAG) {
				currentPage = arg0;
				setTitleResource(arg0);
				setMainView(arg0);
				if (arg0 == 1 && gameAdapter != null) {
//					getAdv(false);
					if (gameAdapter.getCount() == 0) {
						getBroadCastList(BroadCastBiz.GAME_BROADCAST_TYPE, true);
					}
				}
			} else if (flag == TOP_ADV_FLAG) {
				if (pageTopAdvView != null) {
					List<AdvBean> lists = advAdapter.getmList();
					if (lists != null && !lists.isEmpty()) {
						pageTopAdvView.generatePageControl(arg0 % lists.size());
					}
				}
			}

		}

	}

	private void setTitleResource(int select) {
		if (select == 0) {
			iv_arrow.setImageResource(R.drawable.broadcast_myreform_icon_pr);
			tv_broadcast.setTextColor(getResources().getColor(R.color.white));
			ll_broadcast.setBackgroundResource(R.drawable.main_bar_tab_left_pr);
			tv_game.setTextColor(getResources().getColor(R.color.main_bottom_bg_pre));
			tv_game.setBackgroundResource(R.drawable.main_bar_tab_center_un);
			tv_reward.setTextColor(getResources().getColor(R.color.main_bottom_bg_pre));
			tv_reward.setBackgroundResource(R.drawable.main_bar_tab_right_un);
		} else if (select == 1) {
			iv_arrow.setImageResource(R.drawable.broadcast_myreform_icon_un);
			tv_game.setTextColor(getResources().getColor(R.color.white));
			tv_game.setBackgroundResource(R.drawable.main_bar_tab_center_pr);
			tv_broadcast.setTextColor(getResources().getColor(R.color.main_bottom_bg_pre));
			ll_broadcast.setBackgroundResource(R.drawable.main_bar_tab_left_un);
			tv_reward.setTextColor(getResources().getColor(R.color.main_bottom_bg_pre));
			tv_reward.setBackgroundResource(R.drawable.main_bar_tab_right_un);
		}
		showAboutMeCount();
	}

	private void setMainView(int select) {
		if (select == 0) {
			curView = SuspensionEntity.ACTIVITY_BOARDCAST;
		}
		if (select == 1) {
			curView = SuspensionEntity.ACTIVITY_GAME;
		}
		notifySuspsionFlush();
	}

	private void notifySuspsionFlush() {
		NoticeEvent notice = new NoticeEvent();
		notice.setFlag(NoticeEvent.NOTICE93);
		notice.setNum(curView);
		EventBus.getDefault().post(notice);
	}

	private static int rememberClickPos = 0;//记录选择广播的类型

	private void setSelectOperate() {//设置选择的文字态
		if (tv_pop_all_broadcast == null) {
			return;
		}
		try {
			if (rememberClickPos == 0) {
				tv_pop_all_broadcast.setTextColor(getActivity().getResources().getColor(R.color.peach));
				tv_pop_my_broadcast.setTextColor(getActivity().getResources().getColor(R.color.gray));
				tv_pop_about_me_broadcast.setTextColor(getActivity().getResources().getColor(R.color.gray));
				iv_pop_all_broadcast.setVisibility(View.VISIBLE);
				iv_pop_my_broadcast.setVisibility(View.GONE);
				iv_pop_about_me_broadcast.setVisibility(View.GONE);
			} else if (rememberClickPos == 1) {
				tv_pop_all_broadcast.setTextColor(getActivity().getResources().getColor(R.color.gray));
				tv_pop_my_broadcast.setTextColor(getActivity().getResources().getColor(R.color.peach));
				tv_pop_about_me_broadcast.setTextColor(getActivity().getResources().getColor(R.color.gray));

				iv_pop_all_broadcast.setVisibility(View.GONE);
				iv_pop_my_broadcast.setVisibility(View.VISIBLE);
				iv_pop_about_me_broadcast.setVisibility(View.GONE);
			} else if (rememberClickPos == 2) {
				tv_pop_all_broadcast.setTextColor(getActivity().getResources().getColor(R.color.gray));
				tv_pop_my_broadcast.setTextColor(getActivity().getResources().getColor(R.color.gray));
				tv_pop_about_me_broadcast.setTextColor(getActivity().getResources().getColor(R.color.peach));
				iv_pop_all_broadcast.setVisibility(View.GONE);
				iv_pop_my_broadcast.setVisibility(View.GONE);
				iv_pop_about_me_broadcast.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeOperate() {//离开当前页面不操作的
		if (autoPager != null)
			autoPager.stopAutoScroll();// stop auto scroll when onPause
		broadCastBiz.pushSwitch(false);
		stopVoice();
	}

	private void stopVoice() {//停止掉正在听的语音
		if (!mVoiceRecod.isShow())
			mVoiceRecod.stopPlayback();
		broadcastAdapter.setPlayFileName("");
		topBroadCastAdapter.setPlayFileName("");
	}

	private PopupWindow mPopupWindow = null;

	private void showPopupWindowBig() {
		View popupWindow_view = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_gift_big, null);
		mPopupWindow = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		ImageView bus = (ImageView) popupWindow_view.findViewById(R.id.broadcast_bus);
		ImageView plane = (ImageView) popupWindow_view.findViewById(R.id.broadcast_plane);
		ImageView trip = (ImageView) popupWindow_view.findViewById(R.id.broadcast_trip);

		Animation busAnimIn = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_biggift_bus);
		busAnimIn.setInterpolator(new AccelerateInterpolator(2f));
		bus.startAnimation(busAnimIn);

		Animation tripAnimIn = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_biggift_trip);
		tripAnimIn.setInterpolator(new AccelerateInterpolator(2f));
		trip.startAnimation(tripAnimIn);

		Animation planeAnimIn = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_biggift_plane);
		planeAnimIn.setInterpolator(new AccelerateInterpolator(2f));
		planeAnimIn.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				mHandler.sendEmptyMessage(HandlerValue.BROADCAST_GIFT_END);
			}
		});
		plane.startAnimation(planeAnimIn);

		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BaseUtils.hidePopupWindow(mPopupWindow);
				broadcast.setSynchWork(false);
				return false;
			}
		});

		mPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
	}

	private void showPopupWindowCastle() {
		View popupWindow_view = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_gift_castle, null);
		mPopupWindow = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		ImageView castle = (ImageView) popupWindow_view.findViewById(R.id.broadcast_castle);
		ImageView cloud = (ImageView) popupWindow_view.findViewById(R.id.broadcast_cloud);

		Animation castleAnimIn = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_biggift_castle);
		castleAnimIn.setInterpolator(new AccelerateInterpolator(2f));
		castle.startAnimation(castleAnimIn);

		Animation cloudAnimIn = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_biggift_cloud);
		cloudAnimIn.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				mHandler.sendEmptyMessage(HandlerValue.BROADCAST_GIFT_END);
			}
		});
		cloud.startAnimation(cloudAnimIn);

		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BaseUtils.hidePopupWindow(mPopupWindow);
				broadcast.setSynchWork(false);
				return false;
			}
		});

		mPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
	}

	private long currentRequestAdvTime = 0;
	private static final int INTERVALTIME = 1 * 60 * 60 * 1000;//一个小时请求一次广告位

	protected void getAdv(boolean isRefresh) {
		if (System.currentTimeMillis() - currentRequestAdvTime >= INTERVALTIME || isRefresh) {
			MainHallBiz.getInstance().getAdvUrl(getActivity(), this);//获取用户auth值
			new RequestGetShowAdv().getAdv(getActivity(), this);//拉取广告url数组
		}
		roomsGetBiz.getShowRoomLists();
	}

	@Override
	public void getAdv(int retCode, String url, String verifystr) {

		if (retCode == 0 && BAApplication.mLocalUserInfo != null) {
			SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").saveStringKeyValue(verifystr,
					SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
		}

	}

	@Override
	public void getAdvUrl(String urlData) {
		if (!TextUtils.isEmpty(urlData)) {
			currentRequestAdvTime = System.currentTimeMillis();
			SharedPreferencesTools.getInstance(getActivity()).saveStringKeyValue(urlData, BAConstants.PEIPEI_SHOW_ADV_URL);
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_BROADCAST_TOP_ADV_VALUE);
		}
	}

	@Override
	public void enterShowRoomCallback(ShowShareBroadcastInfo info) {//从分享房间点击进来
		if (info.roomid.intValue() > 0 && info.owneruid.intValue() > 0) {//在秀的房间才处理点击事件
			if (BAApplication.showRoomInfo == null) {//第一次点击进房间，进入房间后调用进入秀场接口
				BaseUtils.showDialog(getActivity(), "获取房间信息...");
				roomsGetBiz.getSingleRoomInfo(info.roomid.intValue(), info.owneruid.intValue());
			} else {//已在房间内
				showRoomInfoTmp = null;
				if (info.owneruid.intValue() == BAApplication.showRoomInfo.getOwneruserinfo().getUid()) {//同一个房间，再次进入，不再调用进入秀场操作
					closeOperate();
					BaseUtils.openActivity(getActivity(), PeipeiShowActivity.class);
				} else {
					shareInfo = info;
					if (BAApplication.showRoomInfo.getOwneruserinfo().getUid() == BAApplication.mLocalUserInfo.uid.intValue()) {//不同房间，且所在房间是自己开的，切换到其他房间
						new FinshShowDialog(getActivity(), R.string.str_show_change_room, R.string.ok, R.string.cancel, roomsGetBiz).showDialog();
					} else {//切换到其他房间，调用退出秀场接口，在接口返回中进入秀场
						roomsGetBiz.InOutRooms(InOutAct.out, BAApplication.showRoomInfo.getRoomid(), BAApplication.mLocalUserInfo.uid.intValue());
					}
				}
			}
		} else {
			BaseUtils.showTost(getActivity(), "该房间暂时没人开秀");
		}
	}

	@Override
	public void checkRedpacketStateOnSuccess(int code, Object obj) {

	}

	@Override
	public void checkRedpacketStateOnError(int code) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_CHECK_SOLITAIRE_REDPACKET_STATUS_ERROR, code);
	}

	@Override
	public void onSolitaireRedPacketMoneySuccess(int code, int isOpen, Object obj) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_SOLITAIRE_REDPACKET_INFO_SUCCESS, code, 0, obj);
	}

	@Override
	public void onSolitaireRedPacketMoneyError(int code) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_SOLITAIRE_REDPACKET_INFO_ERROR, code);
	}

	@Override
	public void onGrapSolitaireRedPacketSuccess(int code, Object obj) {

	}

	@Override
	public void onGrapSolitaireRedPacketError(int code) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GRAB_SOLITAIRE_REDPACKET_ERROR, code);
	}

	@Override
	public void onGrapHallSolitaireRedpacketSuccee(int code, Object obj) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GRAB_SOLITAIRE_REDPACKET_SUCCESS, code, 0, obj);
	}

	@Override
	public void checkHallRedpacketStateOnSuccess(int code, Object obj) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_CHECK_SOLITAIRE_REDPACKET_STATUS_SUCCESS, code, 0, obj);
	}

	@Override
	public void grabHallRedpacketOnSuccess(int code, String retMsg, Object obj) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GRAB_REDPACKET_SUCCESS, code, obj, retMsg);
	}

	@Override
	public void grabHallRedpacketOnError(int code) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GRAB_REDPACKET_ERROR, code);
	}

	@Override
	public void checkHallRedpacketStatusOnSuccess(int code, String retMsg, Object obj) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_CHECK_REDPACKET_STATUS_SUCCESS, code, obj, retMsg);
	}

	@Override
	public void checkHallRedpacketStatusOnError(int code) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_CHECK_REDPACKET_STATUS_ERROR, code);
	}

	@Override
	public void getHallRedpacketAvailOnSuccess(int code, int result, String retMsg) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_HALL_REDPACKET_AVAIL, code, result, retMsg);
	}

	@Override
	public void getHallSolitaireRedpacketAvailOnSuccess(int code, int result, String retMsg) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_HALL_SOLITAIRE_REDPACKET_AVAIL, code, result, retMsg);
	}

	@Override
	public void getHallRedpacketAvailOnError(int code) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_HALL_REDPACKET_ERROR, code);
	}

	@Override
	public void getAvailHallRedpacketListOnSuccess(int code, Object obj, String retMsg) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_AVAIL_HALL_REDPACKET_LIST_SUCCESS, code, obj, retMsg);
	}

	@Override
	public void getAvailHallSolitaireRedpacketListOnSuccess(int code, Object obj, String retMsg) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HALL_GET_AVAIL_HALL_SOLITAIRE_REDPACKET_LIST_SUCCESS, code, obj, retMsg);
	}

	@Override
	public void getAvailHallRedpacketListOnError(int code) {
		HandlerUtils.sendHandlerMessage(mHandler, code);
	}
}
