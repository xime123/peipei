package com.tshang.peipei.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.dialog.CreatHintAlertDialog;
import com.tshang.peipei.activity.dialog.CreatIntoAlertDialog;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.PeiPeiPersistBiz;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.ShowRoomLatestStatus;
import com.tshang.peipei.service.PeipeiFloatingService;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.greenrobot.event.EventBus;

/**
 * @author allen
 * @version V1.0
 * @Title: 基础activity
 * @Description: 继承FragmengtActivity，实现onClickListener接口，以及一些常用方法
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
    protected static final int GET_PHOTO_BY_CAMERA_GALLERY = 1030;

    protected TextView mBackText;
    protected TextView mTitle;
    protected LinearLayout mLinRight;
    protected TextView mTextRight;
    protected LinearLayout mTitleLayout;
    protected ImageView mSuspIcon;


    protected Toast mToast;
    protected InputMethodManager mInput;

    protected BAHandler mHandler;
    private HomeKeyEventBroadCastReceiver homePressReceiver;
    protected ImageLoader imageLoader;

    protected boolean isGroupChatValue = false;//判断是否是群聊还是私聊，默认是私聊

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            Log.i("BaseActivity", "onCreate fixOrientation when Oreo, result = " + result);
        }

        super.onCreate(savedInstanceState);
        setContentView(initView());

        EventBus.getDefault().register(this);
        BAApplication.getInstance().addActivity(this);
        imageLoader = ImageLoader.getInstance();

        mInput = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        mHandler = new BAHandler(this) {
            @Override
            public void handleMessage(Message msg) {
                BaseUtils.cancelDialog();
                BaseActivity.this.dispatchMessage(msg);
            }
        };
        initView();//设置布局
        initRecourse();//加载assert里面的数据 查找控件和监听事件
        initData();//数据加载
//		initSuspension();
        homePressReceiver = new HomeKeyEventBroadCastReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homePressReceiver, homeFilter);
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            Log.i("BaseActivity", "avoid calling setRequestedOrientation when Oreo.");
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化资源
     */
    protected abstract void initRecourse();

    /**
     * 初始化界面
     */
    protected abstract int initView();

    /**
     * 处理handler回传的信息
     */
    public void dispatchMessage(Message msg) {
        switch (msg.what) {
            case HandlerValue.LOOK_ME_NUM:
                BaseUtils.showTost(this, "魅力值+" + msg.arg1);
                break;
            case HandlerValue.SHOW_ROOM_PUSH_ROOMSINFO1:
                ShowRoomLatestStatus lastestStatus = (ShowRoomLatestStatus) msg.obj;

                if (BAApplication.mLocalUserInfo != null && BAApplication.showRoomInfo != null && BAApplication.showRoomInfo.getLefttime() > 0) {
                    if (BAApplication.mLocalUserInfo.uid.intValue() == BAApplication.showRoomInfo.getOwneruserinfo().getUid()) {
                        if (lastestStatus.lefttime.intValue() >= 0) {
                            if (lastestStatus.lefttime.intValue() == 0) {
                                BaseUtils.showTost(this, R.string.str_show_close_timeover);
//							Intent intent = new Intent(BaseActivity.this, PeipeiFloatingService.class);
//							stopService(intent);
                                BAApplication.clearShow();
                            } else if (lastestStatus.lefttime.intValue() < 10) {
                                if (CreatHintAlertDialog.ad == null) {
                                    new CreatHintAlertDialog(R.string.str_show_time_left1, R.string.ok).hintDialog();
                                }
                                if (CreatHintAlertDialog.ad != null) {
                                    CreatHintAlertDialog.showDialog();
                                }
                            } else if (lastestStatus.lefttime.intValue() == 5 * 60) {
                                if (CreatHintAlertDialog.ad == null) {
                                    new CreatHintAlertDialog(R.string.str_show_time_left, R.string.ok).hintDialog();
                                }
                                if (CreatHintAlertDialog.ad != null) {
                                    CreatHintAlertDialog.showDialog();
                                }
                            } else if (lastestStatus.lefttime.intValue() == 60) {
                                if (CreatHintAlertDialog.ad == null) {
                                    new CreatHintAlertDialog(R.string.str_show_time_left2, R.string.ok).hintDialog();
                                }
                                if (CreatHintAlertDialog.ad != null) {
                                    CreatHintAlertDialog.showDialog();
                                }
                            } else if (lastestStatus.lefttime.intValue() == 150) {
                                if (CreatIntoAlertDialog.ad == null) {
                                    new CreatIntoAlertDialog().intoShowDialog();
                                }
                                if (CreatIntoAlertDialog.ad != null) {
                                    CreatIntoAlertDialog.showIntoDialog();
                                }
                            }
                        }
                    } else {
                        if (lastestStatus.lefttime.intValue() == 0) {
                            BaseUtils.showTost(this, R.string.str_show_close_timeover1);
//						Intent intent = new Intent(BaseActivity.this, PeipeiFloatingService.class);
//						stopService(intent);
                            BAApplication.clearShow();
                        }
                    }

                    if (lastestStatus.lefttime.intValue() == 299) {//强制关闭
                        if (CreatHintAlertDialog.ad == null) {
                            new CreatHintAlertDialog(R.string.str_show_time_left3, R.string.ok).hintDialog();
                        }
                        if (CreatHintAlertDialog.ad != null) {
                            CreatHintAlertDialog.showDialog();
                        }

                        BAApplication.getInstance().exitShowActivity();
//					Intent intent = new Intent(BAApplication.getInstance(), PeipeiFloatingService.class);
//					BAApplication.getInstance().stopService(intent);

                        BAApplication.getInstance().closeOrOutRoom(1);
                        BAApplication.clearShow();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            BAApplication.getInstance().removeActivity(this);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            mHandler.removeCallbacksAndMessages(null);
            EventBus.getDefault().unregister(this);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (homePressReceiver != null) {
            try {
                unregisterReceiver(homePressReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onResume() {
        super.onResume();
        GoGirlUserInfo info = UserUtils.getUserEntity(this);
        if (info != null) {
            PeiPeiPersistBiz.getInstance().openPersist(info.auth, BAApplication.app_version_code, info.uid.intValue(),
                    ChatManageBiz.getInManage(this));
        }
//		MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
//		MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_tv_left:
                finish();
                break;
//		case R.id.mSuspIcon:
//			SuspensionActivity.openMineFaqActivity(this, BAConstants.FAQ_URL);
//			break;
        }
    }

    /**
     * 使用HANDLER 发送消息
     *
     * @param handler
     * @param what
     * @param arg1
     * @param obj
     */
    public void sendHandlerMessage(Handler handler, int what, int arg1, Object obj) {
        if (handler == null) {
            return;
        }
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    public void sendHandlerMessage(Handler handler, int what, int arg1, Object obj, String retMsg) {
        if (handler == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("data", retMsg);
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.obj = obj;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    /**
     * 使用HANDLER 发送消息
     *
     * @param handler
     * @param what
     * @param arg1
     */
    public void sendHandlerMessage(Handler handler, int what, int arg1) {
        if (handler == null) {
            return;
        }
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        handler.sendMessage(msg);
    }

    /**
     * 使用HANDLER 发送消息
     *
     * @param handler
     * @param what
     * @param arg1
     */
    public void sendHandlerMessage(Handler handler, int what, int arg1, int arg2) {
        if (handler == null) {
            return;
        }
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        handler.sendMessage(msg);
    }

    /**
     * 使用HANDLER 发送消息
     *
     * @param handler
     * @param what
     * @param arg1
     */
    public void sendHandlerMessage(Handler handler, int what, int arg1, int arg2, Object obj) {
        if (handler == null) {
            return;
        }
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    /**
     * 显示软键盘
     *
     * @param edittext
     * @param time
     */
    public void showSoftInput(final EditText edittext) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                edittext.requestFocus();
                if (mInput != null)
                    mInput.showSoftInput(edittext, 0);
            }
        }, 700);
    }

    /**
     * 收起软键盘
     *
     * @param et
     */
    public void hideSoftInput(EditText et) {
        if (null != mInput && mInput.isActive())
            mInput.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 发送事件
     *
     * @param flag
     * @author Jeff
     */
    protected void sendNoticeEvent(int flag) {
        NoticeEvent noticeEvent = new NoticeEvent();
        noticeEvent.setFlag(flag);
        EventBus.getDefault().post(noticeEvent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
    }

    protected void defaultFinish() {
        super.finish();
    }

    protected void SuccessFinish() {
        super.finish();
        overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
    }

    protected void scaleFinish() {
        super.finish();
        overridePendingTransition(R.anim.fragment_slide_right_scale_enter, R.anim.fragment_slide_right_scale_exit);
    }

//	private void initSuspension(){
//		View view = View.inflate(this, R.layout.suspension_icon, null);
//		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		addContentView(view, params);
//		mSuspIcon = (ImageView) view.findViewById(R.id.mSuspIcon);
//		mSuspIcon.setOnClickListener(this);
//	}

    /**
     * @author Jeff
     * 设置刷新文字
     */
    protected void setRefreshTextLable(PullToRefreshListView mPullRefreshListView, int refreshing, int start, int end) {
        mPullRefreshListView.getLoadingLayoutProxy().setRefreshingLabel(getString(refreshing));
        mPullRefreshListView.getLoadingLayoutProxy().setPullLabel(getString(end));
        mPullRefreshListView.getLoadingLayoutProxy().setReleaseLabel(getString(start));
    }

    class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";// home key
        static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    if (reason.equals(SYSTEM_HOME_KEY) || reason.equals(SYSTEM_RECENT_APPS)) {
                        //						// home key处理点
                        //						new CheckPictureAndAudioThread(BaseActivity.this).start();
                        //关闭连接开关
                        BAApplication.isCreateLongConnectedSuccess = false;
                        BAApplication.isOnLine = false;
                        PeiPeiPersistBiz.getInstance().closePersist(BaseActivity.this);
                        PeiPeiRequest.close();
//						Intent intent2 = new Intent(BaseActivity.this, PeipeiFloatingService.class);
//						stopService(intent2);
                    }
                }
            }
        }
    }

    public void onEvent(NoticeEvent event) {
        if (event.getFlag() == NoticeEvent.NOTICE69) {
            if (event.getNum() > 0) {
                mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.LOOK_ME_NUM, event.getNum(), 0));
            }
        } else if (event.getFlag() == NoticeEvent.NOTICE77) {
            sendHandlerMessage(mHandler, HandlerValue.SHOW_ROOM_PUSH_ROOMSINFO1, 0, event.getObj());
        }
    }
}
