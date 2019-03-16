package com.tshang.peipei.activity.main.message;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.dialog.DeleteDynamicInfoDialog;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.GoLoginDialog;
import com.tshang.peipei.activity.dialog.ReportDialog;
import com.tshang.peipei.activity.main.message.adapter.DynamicDetailsAdapter;
import com.tshang.peipei.activity.main.message.adapter.DynamicDetailsAdapter.ChildReplyCallBack;
import com.tshang.peipei.activity.mine.ReportActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.user.DynamicRequseControl;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestDynamicPraiseNumber.AppPariseCallBack;
import com.tshang.peipei.model.request.RequestDynamicReply.addDynamicReplyCallBack;
import com.tshang.peipei.model.request.RequestDynamicReply2.addDynamicReplyCallBack2;
import com.tshang.peipei.model.request.RequestDynamicReplyDetails.GetDynamicReplyDetails;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsCommentInfo;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsCommentInfoList;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetCommentReplyInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.operate.NewDynamicReplyOperate;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @Title: DynamicDetailsActivity.java 
 *
 * @Description: 动态详情 
 *
 * @author Aaron  
 *
 * @date 2015-8-17 上午10:39:41 
 *
 * @version V1.0   
 */
@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class DynamicDetailsActivity extends BaseActivity implements OnTouchListener, GetDynamicReplyDetails, OnRefreshListener2<ListView>,
		addDynamicReplyCallBack, ChildReplyCallBack {

	public final static String SYSTEMID_FLAG = "systemid";
	public final static String RELATIVEID_FLAG = "relativeid";
	public final static String TYPE_FLAG = "type";
	public final static String ANONYMOUS_FLAG = "isAnonymous";
	public final static String TOPICID_FLAG = "topicid";
	public final static String TOPIUID_FLAG = "topicuid";
	public final static String STATE_FLAG = "state";
	public final static String FROM_FLAG = "from";

	private PullToRefreshListView mListView;

	private LinearLayout boardLayout;
	private LinearLayout inputLayout;
	private EditText mEditText;
	private TextView mSendTV;
	private ImageView imageView;
	private TextView emptyTv;
	private TextView replyNumTv;
	private TextView pariseNumTv;
	private LinearLayout mChatLayout;
	private TextView mContentTV;

	private DynamicDetailsAdapter mAdapter;

	private DisplayImageOptions options;

	private BAParseRspData parser;
	private DynamicsCommentInfoList replyList;
	private DynamicsCommentInfo commentInfo;

	private boolean isRefresh = true;
	private boolean isReplyChild = false;
	private int isAnonymous = 0;
	private int state;
	private int frome = 0;

	private int topicuid;
	private int topicid;
	private int systemid;

	protected static final int LOADCOUNT = 10;
	protected int startLoadPosition = 0;

	private DynamicRequseControl control;
	private DynamicsInfo dynamicsInfo;

	//目前给默认值
	private String province = "广东";
	private String city = "深圳";
	private int upnum;
	private int type = 0;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY_STRETCHED).considerExifParams(true).cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		parser = new BAParseRspData();
	}

	@Override
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		topicuid = bundle.getInt(TOPIUID_FLAG);
		topicid = bundle.getInt(TOPICID_FLAG);
		isAnonymous = bundle.getInt(ANONYMOUS_FLAG);
		type = bundle.getInt(TYPE_FLAG);
		systemid = bundle.getInt(SYSTEMID_FLAG);
		state = bundle.getInt(STATE_FLAG);
		frome = bundle.getInt(FROM_FLAG);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mListView.setRefreshing();
			}
		}, 500);
	}

	/**
	 * 请求动态详情
	 * @author Aaron
	 *
	 * @param start
	 * @param num
	 */
	private void getDynamicDetailsInfo(int start, int num) {
		control = new DynamicRequseControl();
		control.getDynamicDetails(topicuid, topicid, systemid, start, num, type, this);
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);

		mTextRight = (TextView) findViewById(R.id.title_tv_right);
		mTextRight.setVisibility(View.VISIBLE);

		mTextRight.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);

		findViewById(R.id.dynamic_details_bottom_priase).setOnClickListener(this);
		mChatLayout = (LinearLayout) findViewById(R.id.dynamic_details_bottom_chat);
		findViewById(R.id.dynamic_details_bottom_reply).setOnClickListener(this);
		findViewById(R.id.dynamic_details_layout).setOnTouchListener(this);

		boardLayout = (LinearLayout) findViewById(R.id.dynamic_details_bottom_layout);
		inputLayout = (LinearLayout) findViewById(R.id.dynamic_details_bottom_ll_reply);
		mEditText = (EditText) findViewById(R.id.dynamic_details_bottom_input_et);
		mSendTV = (TextView) findViewById(R.id.dynamic_details_bottom_input_btn);

		replyNumTv = (TextView) findViewById(R.id.dynamic_details_bottom_reply).findViewById(R.id.dynamic_details_reply_num);
		pariseNumTv = (TextView) findViewById(R.id.dynamic_details_bottom_priase).findViewById(R.id.dynamic_details_priase_num_tv);

		mSendTV.setOnClickListener(this);
		mChatLayout.setOnClickListener(this);

		View headView = getLayoutInflater().inflate(R.layout.dynamic_details_head_layout, null);
		imageView = (ImageView) headView.findViewById(R.id.dynamic_details_head_iv);
		emptyTv = (TextView) headView.findViewById(R.id.dynamic_deatils_empty_tv);
		mContentTV = (TextView) headView.findViewById(R.id.dynamic_details_content_tv);
		mListView = (PullToRefreshListView) findViewById(R.id.dynamic_deatils_listview);
		mListView.setMode(Mode.BOTH);
		mListView.getRefreshableView().addHeaderView(headView);
		mAdapter = new DynamicDetailsAdapter(this);
		mAdapter.setReplyCallBack(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(this);

		imageView.setOnTouchListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_dynamic_deatils;
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_DYNAMIC_REPLY_SUCCESS://回复列表获取成功
			goneEditViewLayout();
			DialogFactory.dimissDialog(dialog);
			mListView.onRefreshComplete();
			if (msg.arg1 == 0) {
				//动态详情结构体
				RspGetCommentReplyInfo info = (RspGetCommentReplyInfo) msg.obj;
				dynamicsInfo = info.dynamicsinfo;//动态信息结构体
				replyList = info.commentlist;//回复列表结构体
				//点赞数量
				upnum = info.upvotenum.intValue();
				pariseNumTv.setText(upnum + "");
				if (dynamicsInfo.isanonymous.intValue() == 1) {
					mTitle.setText(getResources().getString(R.string.anonymous));
					mChatLayout.setVisibility(View.GONE);
				} else {
					//显示备注
					String alias = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
							dynamicsInfo.uid.intValue());
					mTitle.setText(TextUtils.isEmpty(alias) ? new String(dynamicsInfo.nick) : alias);
				}

				state = dynamicsInfo.dynamicsstatus.intValue();
				//判断是不是自己发布的动态
				if (BAApplication.mLocalUserInfo != null && dynamicsInfo.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
					mChatLayout.setVisibility(View.GONE);
					mTextRight.setText(R.string.delete);
				} else {
					mTextRight.setText(R.string.report);
				}
				//解析数据  
				final ContentData data = parser.parseTopicInfo(this, dynamicsInfo.dynamicscontentlist, dynamicsInfo.sex.intValue());
				if (state == 2 || BAApplication.mLocalUserInfo.uid.intValue() == dynamicsInfo.uid.intValue()) {
					imageView.setVisibility(View.VISIBLE);
					mContentTV.setVisibility(View.GONE);
					String imgKey = data.getImageList().get(0) + "@false@500@500";
					initDynamicInfo(imgKey);
				} else {
					String bgColor = new String(dynamicsInfo.revstr0);
					if (!TextUtils.isEmpty(bgColor)) {
						mContentTV.setBackgroundColor(Color.parseColor("#" + bgColor));
					} else {
						mContentTV.setBackgroundResource(getResources().getColor(R.color.official_bg1));
					}
					//字体颜色
					int fontType = dynamicsInfo.fonttype.intValue();
					if (fontType == 0) {
						mContentTV.setTextColor(getResources().getColor(R.color.black));
						mContentTV.setShadowLayer(15, 5, 5, getResources().getColor(R.color.white));
					} else {
						mContentTV.setTextColor(getResources().getColor(R.color.white));
						mContentTV.setShadowLayer(15, 5, 5, getResources().getColor(R.color.black));
					}
					imageView.setVisibility(View.GONE);
					mContentTV.setVisibility(View.VISIBLE);
					mContentTV.setText(data.getContent());
				}

				if (isRefresh) {
					startLoadPosition = 0;
					mAdapter.clearChildReplyList();
					mAdapter.clearList();
				}

				if (replyList == null || replyList.size() == 0) {
					if (isRefresh) {
						emptyTv.setVisibility(View.VISIBLE);
					} else {
						startLoadPosition = startLoadPosition + 10;
						BaseUtils.showTost(this, getResources().getString(R.string.namypic_data_null));
						mListView.setMode(Mode.PULL_FROM_START);
					}
				} else {
					ArrayList<DynamicsCommentInfo> list = new ArrayList<DynamicsCommentInfo>();
					for (int i = 0; i < replyList.size(); i++) {
						list.add((DynamicsCommentInfo) replyList.get(i));
					}
					mAdapter.appendToList(list);
					emptyTv.setVisibility(View.GONE);
					mListView.setMode(Mode.BOTH);
				}
				replyNumTv.setText(dynamicsInfo.replynum.intValue() + "");
			} else if (msg.arg1 == -31005) {//动态不存在
				NewDynamicReplyOperate.getInstance(this).deleteReplyTo(topicuid, topicid);
				NoticeEvent n = new NoticeEvent();
				n.setFlag(NoticeEvent.NOTICE92);
				EventBus.getDefault().post(n);
				BaseUtils.showTost(this, "动态不存在");
				finish();
			} else
				BaseUtils.showTost(this, R.string.get_data_failure);
			break;
		case HandlerValue.GET_DYNAMIC_REPLY_ERROR://回复列表获取失败
			DialogFactory.dimissDialog(dialog);
			mListView.onRefreshComplete();
			BaseUtils.showTost(this, R.string.get_data_failure);
			break;
		case HandlerValue.DYNAMIC_REPLY_SUCCESS://评论成功
			DialogFactory.dimissDialog(dialog);
			isRefresh = true;
			//			mListView.setRefreshing();
			mListView.setRefreshing(true);
			mEditText.getText().clear();
			goneEditViewLayout();
			break;
		case HandlerValue.DYNAMIC_REPLY_ERROR://评论失败
			DialogFactory.dimissDialog(dialog);
			BaseUtils.showTost(this, R.string.reply_failure);
			break;
		case HandlerValue.DYNAMIC_REPLY2_SUCCESS:
			DialogFactory.dimissDialog(dialog);
			isRefresh = true;
			mListView.setRefreshing();
			mEditText.getText().clear();
			goneEditViewLayout();
			break;
		case HandlerValue.DYNAMIC_REPLY2_ERROR:
			DialogFactory.dimissDialog(dialog);
			BaseUtils.showTost(this, R.string.reply_failure);
			break;
		case HandlerValue.DYNAMIC_DELETE_SUCCESS://删除成功
			if (msg.arg1 == 0) {
				NewDynamicReplyOperate.getInstance(this).deleteReplyTo(topicuid, topicid);
				isRefresh = true;
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE86);
				EventBus.getDefault().post(event);

				event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE90);
				if (dynamicsInfo.dynamicstype.intValue() == 0) {
					event.setNum(dynamicsInfo.uid.intValue());
					event.setNum2(dynamicsInfo.topicid.intValue());
				} else {
					event.setNum(dynamicsInfo.uid.intValue());
					event.setNum2(dynamicsInfo.relativetopic.intValue());
				}
				EventBus.getDefault().post(event);
				this.finish();
			} else
				BaseUtils.showTost(this, R.string.delete_failure);
			break;
		case HandlerValue.DYNAMIC_DELETE_ERROR:
			BaseUtils.showTost(this, R.string.delete_failure);
			break;

		default:
			break;
		}
	}

	/**
	 * 初始化动态图片
	 * @author Aaron
	 *
	 * @param imgKey 图片Key
	 */
	private void initDynamicInfo(String imgKey) {
		imageLoader.displayImage("http://" + imgKey, imageView, options);
	}

	private void goneEditViewLayout() {
		boardLayout.setVisibility(View.VISIBLE);
		inputLayout.setVisibility(View.GONE);
		hideSoftInput(mEditText);
	}

	private void visibilityEditViewLayout() {
		inputLayout.setVisibility(View.VISIBLE);
		boardLayout.setVisibility(View.GONE);
		showSoftInput(mEditText);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right://举报
			if (UserUtils.getUserEntity(this) == null) {
				createLoginDialog();
			} else {
				if (BAApplication.mLocalUserInfo != null && dynamicsInfo.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
					new DeleteDynamicInfoDialog(this, android.R.style.Theme_Translucent_NoTitleBar, dynamicsInfo, 1, systemid, mHandler).showDialog();
				} else {
//					new ReportDialog(this, android.R.style.Theme_Translucent_NoTitleBar, dynamicsInfo.uid.intValue(), 1, false).showDialog(0, 0);
					ReportActivity.openMineFaqActivity(this, dynamicsInfo.uid.intValue());
				}
			}
			break;
		case R.id.dynamic_details_bottom_priase://点赞
			final boolean isParise = SharedPreferencesTools.getInstance(this).getBooleanKeyValue(String.valueOf(dynamicsInfo.createtime.intValue()));
			int upvotenum = 1;
			if (isParise) {//如果是已经赞过了，取消点赞
				upvotenum = -1;
				dialog = DialogFactory.createLoadingDialog(this, "取消点赞...");
			} else {
				dialog = DialogFactory.createLoadingDialog(this, "发布点赞...");
			}
			//防止-1值出现
			if (upnum == 0) {
				upvotenum = 1;
			}
			DialogFactory.showDialog(dialog);
			/**
			 * 请求点赞
			 */
			control.appPriaseNum(dynamicsInfo.topicid.intValue(), dynamicsInfo.uid.intValue(), dynamicsInfo.dynamicstype.intValue(), upvotenum,
					systemid, new AppPariseCallBack() {

						@Override
						public void onSuccess(int code, int topicuid, int topicid) {//成功
							if (isParise) {
								upnum = upnum - 1;
								if (upnum < 0) {
									upnum = 0;
								}
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										SharedPreferencesTools.getInstance(DynamicDetailsActivity.this).saveBooleanKeyValue(false,
												String.valueOf(dynamicsInfo.createtime.intValue()));//取消已经点赞过了
										pariseNumTv.setText(upnum + "");
									}
								});
							} else {
								upnum = upnum + 1;
								if (upnum < 0) {
									upnum = 0;
								}
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										SharedPreferencesTools.getInstance(DynamicDetailsActivity.this).saveBooleanKeyValue(true,
												String.valueOf(dynamicsInfo.createtime.intValue()));//已经点赞
										pariseNumTv.setText(upnum + "");
									}
								});
							}
							DialogFactory.dimissDialog(dialog);
						}

						@Override
						public void onError(int code) {//失败
							DialogFactory.dimissDialog(dialog);
							BaseUtils.showTost(DynamicDetailsActivity.this, R.string.thump_up_failure);
						}
					});
			break;
		case R.id.dynamic_details_bottom_chat://私聊
			ChatActivity.intentChatActivity(this, dynamicsInfo.uid.intValue(), new String(dynamicsInfo.nick), dynamicsInfo.sex.intValue(), false,
					false, 0);
			break;
		case R.id.dynamic_details_bottom_reply://回复
			isReplyChild = false;
			visibilityEditViewLayout();
			mEditText.setHint("输入评论内容");
			break;
		case R.id.dynamic_details_bottom_input_btn://发表评论
			if (TextUtils.isEmpty(mEditText.getText().toString())) {
				BaseUtils.showTost(this, "输入回复内容");
				return;
			}
			dialog = DialogFactory.createLoadingDialog(this, R.string.publish_reply);
			DialogFactory.showDialog(dialog);
			//请求添加回复评论
			DynamicRequseControl control = new DynamicRequseControl();
			//判断动态类型
			int type1 = dynamicsInfo.dynamicstype.intValue();
			if (!isReplyChild) {//一级回复
				//如果是话题动态，systemtopicid 传最外层id,否则传-1
				control.addDynamicReply(dynamicsInfo.uid.intValue(), dynamicsInfo.topicid.intValue(), type1, systemid, province, city, mEditText
						.getText().toString(), this);
			} else {//二级回复
				//如果是话题动态，systemtopicid 传最外层id,否则传-1
				control.addDynamicReply2(commentInfo.topicuid.intValue(), dynamicsInfo.topicid.intValue(), commentInfo.id.intValue(),
						dynamicsInfo.dynamicstype.intValue(), systemid, province, city, mEditText.getText().toString(),
						new addDynamicReplyCallBack2() {

							@Override
							public void onSuccess(int code) {
								Message msg = mHandler.obtainMessage();
								msg.what = HandlerValue.DYNAMIC_REPLY2_SUCCESS;
								msg.arg1 = code;
								mHandler.sendMessage(msg);
							}

							@Override
							public void onFailure(int code) {
								Message msg = mHandler.obtainMessage();
								msg.what = HandlerValue.DYNAMIC_REPLY2_ERROR;
								msg.arg1 = code;
								mHandler.sendMessage(msg);
							}
						});
			}

			break;

		default:
			break;
		}
	}

	private void createLoginDialog() {
		new GoLoginDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		goneEditViewLayout();
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (inputLayout.isShown()) {
				goneEditViewLayout();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onSuccess(int code, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.GET_DYNAMIC_REPLY_SUCCESS;
		msg.arg1 = code;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onError(int code) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.GET_DYNAMIC_REPLY_SUCCESS;
		msg.arg1 = code;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		getDynamicDetailsInfo(0, LOADCOUNT);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		startLoadPosition = startLoadPosition + LOADCOUNT;
		getDynamicDetailsInfo(startLoadPosition, LOADCOUNT);
	}

	@Override
	public void onSuccess(int code) {//评论成功回调
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.DYNAMIC_REPLY_SUCCESS;
		msg.arg1 = code;
		mHandler.sendMessage(msg);

	}

	@Override
	public void onFailure(int code) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.DYNAMIC_REPLY_ERROR;
		msg.arg1 = code;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onClickReplyBtn(DynamicsCommentInfo info) {//二级回复点击回调
		commentInfo = info;
		isReplyChild = true;
		visibilityEditViewLayout();
		mEditText.setHint("输入回复内容");
	}
}
