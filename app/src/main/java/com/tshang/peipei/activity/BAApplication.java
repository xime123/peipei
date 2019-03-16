package com.tshang.peipei.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;
import com.tencent.connect.auth.QQAuth;
import com.tshang.peipei.activity.chat.bean.CommonFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.HaremFaceConversionUtil;
import com.tshang.peipei.activity.show.PeipeiShowActivity;
import com.tshang.peipei.base.BaseHttpUtils;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.InOutAct;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.entity.ShowChatEntity;
import com.tshang.peipei.model.entity.SuspensionEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.showrooms.RoomsPublicBiz;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.LoginRewardInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ParticipateInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfo;
import com.tshang.peipei.storage.database.entity.NewDynamicReplyEntity;
import com.tshang.peipei.storage.database.operate.ShowsOperate;
import com.tshang.peipei.vender.common.util.StringUtils;
import com.tshang.peipei.vender.crash.CrashHandler;
import com.tshang.peipei.vender.imageloader.cache.disc.impl.ext.LruDiskCache;
import com.tshang.peipei.vender.imageloader.cache.disc.naming.Md5FileNameGenerator;
import com.tshang.peipei.vender.imageloader.cache.memory.impl.WeakMemoryCache;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.ImageLoaderConfiguration;
import com.tshang.peipei.vender.imageloader.core.assist.QueueProcessingType;
import com.tshang.peipei.vender.imageloader.utils.StorageUtils;
import com.tshang.peipei.vender.micode.soundrecorder.Recorder;

import de.greenrobot.event.EventBus;

/**
 * @Title: Application类 用来存储一些全局变量
 *
 * @Description: 开启线程
 *
 * @author allen
 *
 * @version V1.0   
 */
public class BAApplication extends Application {

	public static final String PEIPEI_IMAGECACHE_TAG = "peipei_imagecache_tag";

	public static LoginRewardInfoList lists = null;
	public static int loginDay = 0;

	private static BAApplication mInstance;
	public static int app_version_code = 1;
	public static boolean isOnLine = false;
	public static boolean isCreateLongConnectedSuccess = false;

	public static boolean isLogin = false;

	public static QQAuth mQQAuth;

	public static GoGirlUserInfo mLocalUserInfo; //用户 信息

	public static ParticipateInfoList participateInfoList;

	public static String Channel = "";

	public static String wxCode = "";//微信code

	public static ShowRoomInfo showRoomInfo;
	//	public static ShowChatAdapter showChatAdapter;
	//	public static ShowOwnerChatAdapter showOwnerChatAdapter;
	public static ArrayList<ShowChatEntity> showVoiceList = new ArrayList<ShowChatEntity>();
	public static ArrayList<ShowChatEntity> showTempList = new ArrayList<ShowChatEntity>();
	public static Bitmap showImage;
	public static long addHotTime;

	public static boolean isShowRomm = false;

	public static int showBoxNum = 0;
	public static boolean isShowBox = false;
	public static String showBoxName = "";
	public static String showBoxPic = "";

	public static boolean isShowPwd = true;
	private List<Activity> activityList = new LinkedList<Activity>();

	public static StringBuffer dareUidList;

	public static String dareId = "";

	private DynamicsInfoList allDynamicLists;
	private DynamicsInfoList meDynamicLists;
	private List<NewDynamicReplyEntity> replyInfoLists;
	private SuspensionEntity suspEntity;
	private SkillTextInfo skillTextInfo;
	public static ArrayList<RedPacketBetInfo> redList;
	public static ArrayList<RedPacketBetInfo> hallRedList; //大厅接龙红包信息
	public static String hallRedHelp; //大厅红包信息

	public static BAApplication getInstance() {
		return mInstance;
	}

	private HashMap<String, Handler> mUpdateHanders = null;// 消息通知handler集合
	private JobManager mJobManager;

	public static long dareClickTime;

	public BAApplication() {
		mInstance = this;
	}

	/**
	 * 添加更新handler
	 * 
	 * @param key
	 * @param mHandler
	 */
	public void addUpdateFlag(String key, Handler mHandler) {
		if (mUpdateHanders == null) {
			mUpdateHanders = new HashMap<String, Handler>();
		}
		mUpdateHanders.put(key, mHandler);
	}

	private void configureJobManager() {
		Configuration configuration = new Configuration.Builder(this).customLogger(new CustomLogger() {
			private static final String TAG = "JOBS";

			@Override
			public void d(String text, Object... args) {
				BaseLog.d(TAG, String.format(text, args));
			}

			@Override
			public void e(String text, Object... args) {
				BaseLog.e(TAG, String.format(text, args));
			}

			@Override
			public void e(Throwable t, String text, Object... args) {
				BaseLog.e(TAG, String.format(text, args), t);
			}

			@Override
			public boolean isDebugEnabled() {
				return true;
			}
		}).minConsumerCount(1)// always keep at least one consumer alive
				.maxConsumerCount(3)// up to 3 consumers at a time
				.loadFactor(3)// 3 jobs per consumer
				.consumerKeepAlive(120)// wait 2 minute
				.build();

		try {
			mJobManager = new JobManager(this, configuration);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据相应的key删除相应的handler
	 * 
	 * @param key
	 */
	public void deleteUpdateFlag(String key) {
		if (mUpdateHanders != null && mUpdateHanders.containsKey(key)) {
			mUpdateHanders.remove(key);
		}
	}

	public JobManager getJobManager() {
		return mJobManager;
	}

	public HashMap<String, Handler> getUpdateHanders() {
		return mUpdateHanders;
	}

	public static final boolean DEVELOPER_MODE = false;

	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		
		super.onCreate();
		mQQAuth = QQAuth.createInstance(BAConstants.QQ_APP_KEY, this);
		app_version_code = BaseTools.getAppVersionCode(this);
		try {
			mLocalUserInfo = UserSharePreference.getInstance(this).getGoGirlUserInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			configureJobManager();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			Channel = appInfo.metaData.getString("UMENG_CHANNEL");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		AppQueueManager.getInstance().initWork();
		initImageLoader(getApplicationContext());

		ThreadPoolService.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				HaremFaceConversionUtil.getInstace().getFileText(getApplicationContext());
				CommonFaceConversionUtil.getInstace().getFileText(getApplicationContext());
				EmojiFaceConversionUtil.getInstace().getFileText(getApplicationContext());
				EmojiParser.getInstance(getApplicationContext());
			}
		});

		mVoiceRecod = Recorder.getInstance();
		mVoiceRecod.setState(0);
		//		registerExternalStorageListener();
		//		mReceiver = new RecorderReceiver(mVoiceRecod);
		//		IntentFilter filter = new IntentFilter();
		//		filter.addAction(RecorderService.RECORDER_SERVICE_BROADCAST_NAME);
		//		registerReceiver(mReceiver, filter);
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}

	public void setUpdateHanders(HashMap<String, Handler> updateHanders) {
		this.mUpdateHanders = updateHanders;
	}

	public static void initImageLoader(Context context) {
		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		int cacheSize = 1024 * 1024 * memClass / 4; // 硬引用缓存容量，为系统可用内存的1/4
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "PeiPei/imagesCache");
		ImageLoaderConfiguration config;
		try {
			config = new ImageLoaderConfiguration.Builder(context)
			//设置线程池大小
					.threadPriority(Thread.NORM_PRIORITY - 2)
					// 设置线程的优先级  
					.denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator())
					//当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
					.memoryCacheSize(cacheSize).tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序  
					.memoryCache(new WeakMemoryCache()).diskCache(new LruDiskCache(cacheDir, new Md5FileNameGenerator(), 200 * 1024 * 1024))
					//自定义缓存路径  
					.writeDebugLogs() // Remove for release app
					.build();
			// Initialize ImageLoader with configuration.
			ImageLoader.getInstance().init(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//开始构建  
	}

	public void setTempList(int index) {
		if (index < 0) {
			index = 0;
		}
		showTempList.clear();
		if (showVoiceList.size() > 0 && index <= showVoiceList.size()) {
			showTempList.addAll(showVoiceList.subList(index, showVoiceList.size()));
		}
	}

	public String resultVoiceName() {
		String path = "";

		if (showTempList.size() > 0) {
			if (TextUtils.isEmpty(showTempList.get(0).voiceFile)) {
				path = showTempList.get(0).data;
			} else {
				path = showTempList.get(0).voiceFile;
			}
			String[] s1 = path.split("/");
			if (s1 != null && s1.length > 0) {
				String str = s1[s1.length - 1];
				ShowsOperate operate = ShowsOperate.getInstance(this);
				int type = showTempList.get(0).type;
				showTempList.remove(0);
				if (operate.selectStatus(str, type) == 1) {
					return resultVoiceName();
				}
			} else {
				showTempList.remove(0);
				return resultVoiceName();
			}
		}

		return path;
	}

	public static Recorder mVoiceRecod;

	//	private RecorderReceiver mReceiver;

	public void playVoice() {
		if (mVoiceRecod.state() != Recorder.RECORDING_STATE && mVoiceRecod.state() != Recorder.PLAYING_STATE) {
			//			showOwnerChatAdapter.setFileName("");
			ThreadPoolService.getInstance().execute(new Runnable() {

				public void run() {
					String path = resultVoiceName();

					Message msg = Message.obtain();
					msg.what = HandlerValue.SHOW_ROOM_SERVER_PLAY_VOICE;
					msg.obj = "";
					if (!TextUtils.isEmpty(path)) {
						String[] s1 = path.split("/");
						String str = s1[s1.length - 1];
						File f = new File(SdCardUtils.getInstance().getDirectory(0));
						if (!f.exists()) {
							f.mkdirs();
						}
						File file = new File(SdCardUtils.getInstance().getDirectory(0), str);

						if (file.exists()) {
							msg.obj = file.getAbsolutePath();
						} else {
							msg.obj = BaseHttpUtils.downLoadFile(BAApplication.this, path, s1[s1.length - 1]);
						}

					}
					handler.sendMessage(msg);
				}
			});
		}
	}

	//	private void registerExternalStorageListener() {
	//		BroadcastReceiver mSDCardMountEventReceiver = new BroadcastReceiver() {
	//			@Override
	//			public void onReceive(Context context, Intent intent) {
	//				mVoiceRecod.reset();
	//			}
	//		};
	//		IntentFilter iFilter = new IntentFilter();
	//		iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
	//		iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
	//		iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
	//		iFilter.addDataScheme("file");
	//		registerReceiver(mSDCardMountEventReceiver, iFilter);
	//	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HandlerValue.SHOW_ROOM_SERVER_PLAY_VOICE:
				ShowsOperate operate = ShowsOperate.getInstance(BAApplication.this);
				String temp = (String) msg.obj;
				if (!TextUtils.isEmpty(temp)) {
					String[] str = temp.split("/");

					operate.updateStutas(str[str.length - 1], 1);

					mVoiceRecod.startPlayback((String) msg.obj, -1);

					NoticeEvent event = new NoticeEvent();
					event.setObj(temp);
					event.setFlag(NoticeEvent.NOTICE79);
					EventBus.getDefault().post(event);
				}
				break;
			default:
				break;
			}
		};
	};

	public static void clearShow() {
		showRoomInfo = null;
		showImage = null;
		//		if (showChatAdapter != null)
		//			showChatAdapter.clearList();
		//		if (showOwnerChatAdapter != null)
		//			showOwnerChatAdapter.clearList();
		showTempList.clear();
		showVoiceList.clear();
		mVoiceRecod.stop();

		if (BAApplication.mLocalUserInfo != null) {
			try {
				File f = new File(BAApplication.getInstance().getExternalCacheDir().getAbsolutePath() + "/roominfo"
						+ BAApplication.mLocalUserInfo.uid.intValue());
				ConfigCacheUtil.clearCache(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void closeOrOutRoom(int act) {
		if (showRoomInfo != null && mLocalUserInfo != null) {
			RoomsPublicBiz roomsGetBiz = new RoomsPublicBiz();
			if (showRoomInfo.getOwneruserinfo().getUid() == mLocalUserInfo.uid.intValue()) {
				roomsGetBiz.finishShow(this, act);
			} else {
				roomsGetBiz.InOutRooms(this, InOutAct.out, showRoomInfo.getRoomid(), showRoomInfo.getOwneruserinfo().getUid());
			}
		}
	}

	public void exit() {
		for (Activity activity : activityList) {
			if (activity != null)
				activity.finish();
		}
	}

	public void exitShowActivity() {
		for (Activity activity : activityList) {
			if (activity instanceof PeipeiShowActivity) {
				activity.finish();
			}
		}
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	public DynamicsInfoList getAllDynamicLists() {
		return allDynamicLists;
	}

	public void setAllDynamicLists(DynamicsInfoList allDynamicLists) {
		this.allDynamicLists = allDynamicLists;
	}

	public DynamicsInfoList getMeDynamicLists() {
		return meDynamicLists;
	}

	public void setMeDynamicLists(DynamicsInfoList meDynamicLists) {
		this.meDynamicLists = meDynamicLists;
	}

	public List<NewDynamicReplyEntity> getReplyInfoLists() {
		return replyInfoLists;
	}

	public void setReplyInfoLists(List<NewDynamicReplyEntity> replyInfoLists) {
		this.replyInfoLists = replyInfoLists;
	}

	public SuspensionEntity getSuspEntity() {
		return suspEntity;
	}

	public void setSuspEntity(SuspensionEntity suspEntity) {
		this.suspEntity = suspEntity;
	}

	public SkillTextInfo getSkillTextInfo() {
		return skillTextInfo;
	}

	public void setSkillTextInfo(SkillTextInfo skillTextInfo) {
		this.skillTextInfo = skillTextInfo;
	}

}
