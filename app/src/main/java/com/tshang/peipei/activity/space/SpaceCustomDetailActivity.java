package com.tshang.peipei.activity.space;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.ImageDetailActivity;
import com.tshang.peipei.activity.dialog.DeleteCommentDialog;
import com.tshang.peipei.activity.dialog.GoLoginDialog;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.mine.ReportActivity;
import com.tshang.peipei.activity.space.SpaceCustomDetailListAdapter.IAddReply;
import com.tshang.peipei.activity.space.SpaceCustomDetailListAdapter.IIntoSpace;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.babase.BAMessageBean;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.main.MainMySpaceBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackAddComment;
import com.tshang.peipei.model.bizcallback.BizCallBackAddReply;
import com.tshang.peipei.model.bizcallback.BizCallBackAppreciateTopic;
import com.tshang.peipei.model.bizcallback.BizCallBackGetTopicCounter;
import com.tshang.peipei.model.bizcallback.BizCallBackGetTopicDetail;
import com.tshang.peipei.model.bizcallback.BizCallBackTipoffTopic;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.CommentInfo;
import com.tshang.peipei.protocol.asn.gogirl.CommentInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicCounterInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.operate.AppreciateOperate;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/*
 *类        名 : SpaceCustomActivity.java
 *功能描述 : 游客态  动态详情
 *作　    者 : vactor
 *设计日期 : 2014-4-19 
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class SpaceCustomDetailActivity extends BaseActivity implements IAddReply, BizCallBackAddReply, BizCallBackGetTopicDetail,
		BizCallBackAddComment, BizCallBackAppreciateTopic, OnItemClickListener, IIntoSpace, BizCallBackTipoffTopic, OnItemLongClickListener,
		BizCallBackGetTopicCounter {

	private static final int GETTOPICDETAIL = 1;
	private final int APPRECIATE = 2;
	private final int ADDCOMMENT = 3;//一级回复
	private final int ADDREPLY = 4;//二级回复
	private final int TIPOFFTOPIC = 5;

	public static final String APPRECIATENUM = "appreciatenum";
	public static final String REPLYNUM = "replynum";
	public static final String TOPICID = "topicid";
	public static final String TOPICUID = "topicuid";
	public static final String ISADDCOMMENT = "isaddcoment"; //是否弹出软键盘

	private int topicId;
	private int topoicUid;
	private static int RATIOLOADCOUNT = 10;
	private int appNum;
	private int replyNum;
	private boolean isAddComment = false;
	private int position;

	private int oneWidth;//图片只有一张时的布局大小 
	private int twoWidth;//图片只有两张时的布局大小
	//	private int normalWidth;//正常时的图片布局大小

	private PullToRefreshListView mListView;
	private SpaceCustomDetailListAdapter adapter;
	private LinearLayout.LayoutParams paramsBig;
	private LinearLayout.LayoutParams paramsMid;
	//	private LinearLayout.LayoutParams paramsMin;

	//*******************头部信息**************
	private LinearLayout mLinBottomReply;
	private EditText etReply;

	private ImageView mImageHead; //头像
	private TextView mTxtNick;//昵称
	private TextView mTxtCreateTime; //时间 
	private TextView mTxtContent; //文字内容
	private GridView mGridView;//图片内容(多张情况时,用gridview显示)
	private ImageView mImageContent;//图片内容(一张情况时)
	private TextView mTxtAppNum;
	private TextView mTxtReplyNum;
	//*******************头部信息**************
	private BAParseRspData parser;

	private TopicInfo topicInfo;
	private String comment;
	private CommentInfo commentInfo;

	private int startLoadPosition = 0;
	private boolean isRefresh = true;

	private View footerText;

	protected DisplayImageOptions options;
	protected DisplayImageOptions options_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		//动态详情
		case GETTOPICDETAIL:
			mListView.onRefreshComplete();
			BAMessageBean msgBean = (BAMessageBean) msg.obj;
			int retCode = msgBean.getRetCode();
			if (retCode == BAConstants.rspContMsgType.E_CACHE_NO_DATA || retCode == -31005) {
				finish();
				BaseUtils.showTost(this, "动态已被删除");
			}
			TopicInfo info = msgBean.getTopicInfo();
			//帖子信息
			if (null != info && info.topiccontentlist.size() > 0) {
				topicInfo = info;
			}
			//帖子信息
			if (null != topicInfo) {
				final ContentData content = parser.parseTopicInfo(this, topicInfo.topiccontentlist, topicInfo.sex.intValue());

				String alias = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						topicInfo.uid.intValue());

				mTxtNick.setText(TextUtils.isEmpty(alias) ? new String(topicInfo.nick) : alias);
				mTxtCreateTime.setText(new String(topicInfo.createtime + ""));
				//文字内容 
				mTxtContent.setText(ParseMsgUtil.convetToHtml(content.getContent(), SpaceCustomDetailActivity.this, BaseUtils.dip2px(this, 24)));

				mTxtCreateTime.setText(BaseTimes.getDiffTime2(topicInfo.createtime.longValue() * 1000));
				String uidKey = topicInfo.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
				imageLoader.displayImage("http://" + uidKey, mImageHead, options_head);
				boolean flag = false;
				if (null != BAApplication.mLocalUserInfo) {
					AppreciateOperate appreciateOperate = AppreciateOperate.getInstance(SpaceCustomDetailActivity.this,
							BAApplication.mLocalUserInfo.uid.intValue() + "");

					//判断是否已经点过赞了
					flag = appreciateOperate.isExist(topicInfo.topicid.intValue(), topicInfo.uid.intValue());
				}
				if (flag) {
					setTextViewLeftDrawable(R.drawable.person_icon_praise, mTxtAppNum);
					mTxtAppNum.setTextColor(SpaceCustomDetailActivity.this.getResources().getColor(R.color.peach));
					mTxtAppNum.setOnClickListener(null);
				} else {
					setTextViewLeftDrawable(R.drawable.person_icon_praise_un, mTxtAppNum);
					mTxtAppNum.setTextColor(SpaceCustomDetailActivity.this.getResources().getColor(R.color.black));
				}

				//一张图片时
				if (content.getImageList().size() == 1) {
					if (content.getType() == BAConstants.MessageType.UPLOAD_PHOTO.getValue()) {
						String imageKey = content.getImageList().get(0) + BAConstants.LOAD_210_APPENDSTR;
						imageLoader.displayImage("http://" + imageKey, mImageContent, options);
						mImageContent.setLayoutParams(paramsMid);
					} else {
						String imageKey = content.getImageList().get(0) + "@false@500@500";
						imageLoader.displayImage("http://" + imageKey, mImageContent);
						mImageContent.setLayoutParams(paramsBig);
					}
					mImageContent.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							bundle.putInt(ImageDetailActivity.POSITION, position);
							bundle.putStringArrayList(ImageDetailActivity.EXTRA_IMAGE, content.getImageList());
							bundle.putBoolean(ImageDetailActivity.ISREPORT, true);
							bundle.putInt(ImageDetailActivity.PIC_UID, topoicUid);
							BaseUtils.openActivity(SpaceCustomDetailActivity.this, ImageDetailActivity.class, bundle);
						}
					});
					mGridView.setVisibility(View.GONE);
					mImageContent.setVisibility(View.VISIBLE);
				}
				//两张图片时
				if (content.getImageList().size() == 2 || content.getImageList().size() == 4) {
					SpaceCustomGridViewAdapter adapter = new SpaceCustomGridViewAdapter(SpaceCustomDetailActivity.this, content,
							topicInfo.uid.intValue(), topicInfo.sex.intValue());
					mGridView.setVisibility(View.VISIBLE);
					mImageContent.setVisibility(View.GONE);
					mGridView.setNumColumns(2);
					mGridView.setAdapter(adapter);
				}
				//三张以上
				if (content.getImageList().size() > 2 && content.getImageList().size() != 4) {
					SpaceCustomGridViewAdapter adapter = new SpaceCustomGridViewAdapter(SpaceCustomDetailActivity.this, content,
							topicInfo.uid.intValue(), topicInfo.sex.intValue());
					mGridView.setVisibility(View.VISIBLE);
					mImageContent.setVisibility(View.GONE);
					mGridView.setNumColumns(3);
					mGridView.setAdapter(adapter);
				}

			}

			CommentInfoList commentList = msgBean.getCommentList();
			if (commentList != null && !commentList.isEmpty() && commentList.size() == RATIOLOADCOUNT) {
				mListView.setMode(Mode.BOTH);
			} else {
				mListView.setMode(Mode.PULL_FROM_START);
				if (!isRefresh)
					BaseUtils.showTost(this, "没有更多了");
			}
			if (isRefresh) {
				adapter.clearList();
			}
			adapter.appendToList(commentList);
			if (adapter.getCount() == 0) {
				mListView.getRefreshableView().removeFooterView(footerText);
				mListView.getRefreshableView().addFooterView(footerText);
			} else {
				mListView.getRefreshableView().removeFooterView(footerText);
			}
			break;
		//一级回复
		case ADDCOMMENT:
			retCode = msg.arg1;
			if (retCode == 0) {
//				MobclickAgent.onEvent(this, "PingLunZongCiShu");
				startLoadPosition = 0;
				isRefresh = true;
				getTopicDetail(startLoadPosition, RATIOLOADCOUNT, 0);
				mListView.getRefreshableView().setSelection(adapter.getCount());
				//								BaseUtils.hideKeyboard(SpaceCustomDetailActivity.this, etReply);
				hideSoftInput(etReply);
				mLinBottomReply.setVisibility(View.GONE);
				replyNum++;
				mTxtReplyNum.setText(String.valueOf(replyNum));
				noticeCustomActivitytoRefresh();
				comment = "";
				etReply.setText(comment);
			} else if (retCode == rspContMsgType.E_GG_FORBIT) {
				new HintToastDialog(this, R.string.limit_talk, R.string.ok).showDialog();
			} else {
				BaseUtils.showTost(this, "回复失败");
			}
			break;
		//二级回复
		case ADDREPLY:
			retCode = msg.arg1;
			if (retCode == 0) {
//				MobclickAgent.onEvent(this, "PingLunZongCiShu");
				CommentInfo commentInfo = new MainMySpaceBiz().createCommentInfo("", "", comment, new String(BAApplication.mLocalUserInfo.nick),
						BAApplication.mLocalUserInfo.sex.intValue(), BAApplication.mLocalUserInfo.uid.intValue());
				CommentInfo data = adapter.getList().get(position);
				replyNum++;
				mTxtReplyNum.setText(replyNum + "");
				data.replylist.add(commentInfo);
				adapter.notifyDataSetChanged();
				noticeCustomActivitytoRefresh();
				comment = "";
				etReply.setText(comment);
			} else if (retCode == rspContMsgType.E_GG_FORBIT) {
				new HintToastDialog(this, R.string.limit_talk, R.string.ok).showDialog();
			} else {
				BaseUtils.showTost(this, "回复失败");
			}
			mLinBottomReply.setVisibility(View.GONE);
			//			BaseUtils.hideKeyboard(SpaceCustomDetailActivity.this, etReply);
			hideSoftInput(etReply);
			break;
		case HandlerValue.SPACE_DELETE_REPLY_VALUE:
			retCode = msg.arg1;
			if (retCode == 0) {
				BaseUtils.showTost(this, "删除成功");
				isRefresh = true;
				startLoadPosition = 0;
				getTopicDetail(startLoadPosition, RATIOLOADCOUNT, 1);
				replyNum--;
				mTxtReplyNum.setText(replyNum + "");
				noticeCustomActivitytoRefresh1();

			} else if (retCode == rspContMsgType.E_GG_FORBIT) {
				new HintToastDialog(this, R.string.limit_talk, R.string.ok).showDialog();
			} else {
				BaseUtils.showTost(this, "删除失败");
			}
			break;

		//点赞
		case APPRECIATE:
			retCode = msg.arg1;
			if (retCode == 0) {
//				MobclickAgent.onEvent(this, "DiANJiDianZanAnNiuCiShu");
				setTextViewLeftDrawable(R.drawable.person_icon_praise, mTxtAppNum);
				mTxtAppNum.setOnClickListener(null);
				appNum++;
				mTxtAppNum.setText(String.valueOf(appNum));
				mTxtAppNum.setTextColor(SpaceCustomDetailActivity.this.getResources().getColor(R.color.peach));
				//通知动态列表界面点赞数+1
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE21);
				event.setNum(topicId);
				event.setNum2(topoicUid);
				EventBus.getDefault().post(event);
			} else {
				BaseUtils.showTost(this, "点赞失败");
			}
			break;
		case TIPOFFTOPIC:
			retCode = msg.arg1;
			if (retCode == 0) {
				BaseUtils.showTost(this, "举报成功");
			} else {
				BaseUtils.showTost(this, "网络错误");
			}
			break;
		case HandlerValue.GET_TOPIC_COUNTER:
			if (msg.arg1 == 0) {
				TopicCounterInfo counterInfo = (TopicCounterInfo) msg.obj;
				appNum = counterInfo.appreciatenum.intValue();
				replyNum = counterInfo.commentnum.intValue();

				mTxtAppNum.setText(appNum + "");
				mTxtReplyNum.setText(replyNum + "");
			}
			break;
		}
	}

	//通知动态界面刷新回复数
	private void noticeCustomActivitytoRefresh() {
		NoticeEvent event = new NoticeEvent();
		event.setFlag(NoticeEvent.NOTICE23);
		event.setNum(topicId);
		event.setNum2(topoicUid);
		event.setNum3(replyNum);
		EventBus.getDefault().post(event);
	}

	//通知动态界面刷新回复数
	private void noticeCustomActivitytoRefresh1() {
		NoticeEvent event = new NoticeEvent();
		event.setFlag(NoticeEvent.NOTICE64);
		event.setNum(topicId);
		event.setNum2(topoicUid);
		event.setNum3(replyNum);
		EventBus.getDefault().post(event);
	}

	//上拉刷新 ,下拉加载更多刷新 
	class PullToRefreshListener implements OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isRefresh = true;
			startLoadPosition = 0;
			getTopicDetail(startLoadPosition, RATIOLOADCOUNT, 1);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isRefresh = false;
			startLoadPosition = startLoadPosition + RATIOLOADCOUNT;
			getTopicDetail(startLoadPosition, RATIOLOADCOUNT, 0);
		}

	}

	private void getTopicDetail(int start, int num, int isGetTopic) {
		new MainMySpaceBiz().getTopicDetail("".getBytes(), BAApplication.app_version_code, topoicUid, topoicUid, topicId, isGetTopic, start, num,
				this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.head_detail_space_tv_appreciate_num:
			if (BAApplication.mLocalUserInfo == null) {
				createLoginDialog();
			} else {
				appreciate();
			}
			break;
		case R.id.head_detail_space_tv_reply_num:
			if (BAApplication.mLocalUserInfo == null) {
				createLoginDialog();
			} else {
				mLinBottomReply.setVisibility(View.VISIBLE);
				showSoftInput(etReply);
				isAddComment = true;
			}
			break;
		case R.id.detail_space_reply_input_btn:
			comment = etReply.getText().toString();
			if (TextUtils.isEmpty(comment)) {
				BaseUtils.showTost(this, "回复内容不能为空!");
				return;
			}
			if (BAApplication.mLocalUserInfo != null) {
//				MobclickAgent.onEvent(this, "pinglun");
			}
			if (isAddComment) {
				mLinBottomReply.setVisibility(View.VISIBLE);
				addComment(comment);
			} else {
				addReply(comment);
			}
			BaseUtils.showDialog(this, R.string.write_sending);
			break;
		case R.id.title_tv_right:
			if (UserUtils.getUserEntity(this) == null) {
				createLoginDialog();
			} else {
//				new ReportDialog(this, android.R.style.Theme_Translucent_NoTitleBar, topoicUid, 0, false).showDialog(0, 0);
				ReportActivity.openMineFaqActivity(this, topoicUid);
			}
			break;

		}
	}

	private void createLoginDialog() {
		new GoLoginDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog();
	}

	//点赞请求
	private void appreciate() {
		new MainMySpaceBiz().appreciate(this, topicId, topoicUid, 1, this);
	}

	public void addReply(String coment) {

		if (commentInfo != null) {
			int commentId = commentInfo.id.intValue();
			new MainMySpaceBiz().addReply(this, "", "", topicId, topoicUid, commentId, coment, this);
		}

	}

	@Override
	public void getTopicDetailCallBack(int retCode, TopicInfo topicInfo, CommentInfoList commentList, int commentTotal) {
		BAMessageBean msgBean = new BAMessageBean();
		msgBean.setRetCode(retCode);
		msgBean.setTopicInfo(topicInfo);
		msgBean.setCommentList(commentList);
		msgBean.setTotal(commentTotal);
		mHandler.sendMessage(mHandler.obtainMessage(GETTOPICDETAIL, msgBean));
	}

	@Override
	public void appreciateCallBack(int retCode) {
		if (null != mHandler) {
			mHandler.sendEmptyMessage(APPRECIATE);
		}
	}

	private void addComment(String comment) {
		new MainMySpaceBiz().addComment(this, "", "", topicId, topoicUid, comment, this);

	}

	@Override
	public void addCommentCallBack(int retCode) {
		if (null != mHandler) {
			sendHandlerMessage(mHandler, ADDCOMMENT, retCode);
		}
	}

	@Override
	public void addReply(int position, CommentInfo info) {
		if (BAApplication.mLocalUserInfo == null) {
			createLoginDialog();
		} else {
			mLinBottomReply.setVisibility(View.VISIBLE);
			showSoftInput(etReply);
			isAddComment = false;
			this.position = position;
			this.commentInfo = info;
		}
	}

	@Override
	public void addReplyCallBack(int retCode) {
		sendHandlerMessage(mHandler, ADDREPLY, retCode);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (BAApplication.mLocalUserInfo == null) {
			createLoginDialog();
		} else {
			mLinBottomReply.setVisibility(View.VISIBLE);
			CommentInfo info = (CommentInfo) arg0.getAdapter().getItem(arg2);
			this.position = arg2 - 2;
			this.commentInfo = info;
			isAddComment = false;
			showSoftInput(etReply);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mLinBottomReply.isShown()) {
				hideSoftInput(etReply);
				//				BaseUtils.hideKeyboard(this, etReply);
				mLinBottomReply.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {

		options_head = ImageOptionsUtils.GetHeadUidSmallRounded(this);
		options = ImageOptionsUtils.getImageKeyOptions(this);

		parser = new BAParseRspData();
		int screenWidth = BasePhone.getScreenWidth(this);
		oneWidth = (screenWidth - BaseUtils.dip2px(this, 80));
		twoWidth = oneWidth / 2;
		//		normalWidth = oneWidth / 3;
		paramsBig = new LinearLayout.LayoutParams(oneWidth, oneWidth);
		paramsMid = new LinearLayout.LayoutParams(twoWidth, twoWidth);
		//		paramsMin = new LinearLayout.LayoutParams(normalWidth, normalWidth);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			topicId = bundle.getInt(TOPICID, -1);
			topoicUid = bundle.getInt(TOPICUID, -1);
			appNum = bundle.getInt(APPRECIATENUM, 0);
			replyNum = bundle.getInt(REPLYNUM, 0);

			mListView = (PullToRefreshListView) this.findViewById(R.id.detail_space_list);
			mListView.getRefreshableView().setOnItemLongClickListener(this);
			footerText = getLayoutInflater().inflate(R.layout.footer_dynamic, null);
			mListView.getRefreshableView().setFooterDividersEnabled(false);
			View headView = this.getLayoutInflater().inflate(R.layout.head_detail_space, null);
			mListView.getRefreshableView().addHeaderView(headView);
			mImageHead = (ImageView) headView.findViewById(R.id.head_detail_space_head);
			String uidKey = topoicUid + BAConstants.LOAD_HEAD_UID_APPENDSTR;
			imageLoader.displayImage("http://" + uidKey, mImageHead, options_head);
			mTxtNick = (TextView) headView.findViewById(R.id.head_detail_space_nick);
			mTxtCreateTime = (TextView) headView.findViewById(R.id.head_detail_space_create_time);
			mTxtContent = (TextView) headView.findViewById(R.id.head_detail_space_tv_content);

			mTxtAppNum = (TextView) headView.findViewById(R.id.head_detail_space_tv_appreciate_num);
			mTxtReplyNum = (TextView) headView.findViewById(R.id.head_detail_space_tv_reply_num);
			mTxtReplyNum.setOnClickListener(this);

			mGridView = (GridView) headView.findViewById(R.id.head_detail_space_gridview);
			mImageContent = (ImageView) headView.findViewById(R.id.head_detail_space_list_rc_imageview);
			TextView mTxtTitle = (TextView) findViewById(R.id.title_tv_mid);
			mTxtTitle.setText(R.string.space_detail);

			mBackText = (TextView) findViewById(R.id.title_tv_left);
			mBackText.setText(R.string.private_page);
			mBackText.setOnClickListener(this);

			mTextRight = (TextView) findViewById(R.id.title_tv_right);
			mTextRight.setVisibility(View.VISIBLE);
			mTextRight.setText(R.string.report);
			mTextRight.setOnClickListener(this);

			mLinBottomReply = (LinearLayout) this.findViewById(R.id.detail_space_ll_reply);
			etReply = (EditText) this.findViewById(R.id.detail_spacereply_input_et);
			findViewById(R.id.detail_space_reply_input_btn).setOnClickListener(this);
			adapter = new SpaceCustomDetailListAdapter(this, topoicUid, topicId, mHandler);
			mListView.setAdapter(adapter);

			mListView.setOnItemClickListener(this);
			mListView.setOnRefreshListener(new PullToRefreshListener());
			//二级回复监听
			adapter.setAddReplyListener(this);
			adapter.setIntoSpace(this);

			mTxtAppNum.setOnClickListener(this);

			if (appNum > 0 && replyNum > 0) {
				mTxtAppNum.setText(appNum + "");
				mTxtReplyNum.setText(replyNum + "");
			} else {
				new MainMySpaceBiz().getTopicCounter(topoicUid, topicId, 1, this);
			}
			BaseUtils.showDialog(this, R.string.loading);
			//传 1 表示要加载主贴内容
			getTopicDetail(0, RATIOLOADCOUNT, 1);
			isAddComment = bundle.getBoolean(ISADDCOMMENT, false);
			if (isAddComment) {
				mLinBottomReply.setVisibility(View.VISIBLE);
				showSoftInput(etReply);
			}

		}
	}

	@Override
	protected int initView() {
		return R.layout.activity_deatil_space;
	}

	private void setTextViewLeftDrawable(int source, TextView tv) {
		if (tv != null) {
			Drawable drawable = getResources().getDrawable(source);
			if (drawable != null) {
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
				tv.setCompoundDrawables(drawable, null, null, null);
			}
		}
	}

	@Override
	public void intoSpace(int uid, int sex) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if (userEntity != null) {
			if (uid != userEntity.uid.intValue() && uid != topoicUid) {
				SpaceUtils.toSpaceCustom(this, uid, sex);
			}
		}
	}

	@Override
	public void tipoffTopic(int retCode) {
		sendHandlerMessage(mHandler, TIPOFFTOPIC, retCode);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == topoicUid) {//只有自己才能够删帖
			if (position >= 2) {
				CommentInfo info = adapter.getList().get(position - 2);
				if (info != null) {
					new DeleteCommentDialog(this, android.R.style.Theme_Translucent_NoTitleBar, info.id.intValue(), topicId, topoicUid, topoicUid,
							mHandler).showDialog();
				}
			}
		}
		return false;
	}

	@Override
	public void getTopicCounterCallBack(int retCode, TopicCounterInfo info) {
		sendHandlerMessage(mHandler, HandlerValue.GET_TOPIC_COUNTER, retCode, info);

	}
}
