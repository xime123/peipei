package com.tshang.peipei.activity.space;

import java.math.BigInteger;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DeleteTopicInfoDialog;
import com.tshang.peipei.activity.mine.MineNetPhotosListActivity;
import com.tshang.peipei.activity.mine.MineWriteActivity;
import com.tshang.peipei.activity.space.adapter.NewDynamicAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.jobs.GetFailureOrSendingTopicFromDbJob;
import com.tshang.peipei.model.biz.user.DynamicRequseControl;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestDynamicAll.GetDynamicAllCallBack;
import com.tshang.peipei.model.space.SpaceBiz;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfo;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicCounterInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfoList;
import com.tshang.peipei.storage.database.entity.PublishDatabaseEntity;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: SpaceAllDynamicActivity.java 
 *
 * @Description: 个人所有动态界面
 *
 * @author DYH  
 *
 * @date 2015-10-12 下午2:38:49 
 *
 * @version V1.0   
 */
public class SpaceAllDynamicActivity extends BaseActivity implements OnRefreshListener2<ListView>, OnItemClickListener, OnItemLongClickListener,
		GetDynamicAllCallBack {

	protected PullToRefreshListView mPullRefreshListView;
	private TextView tv_foot_empty;
	private View footerText;
	private ImageView mRightView;
	private int mFriendUid; //客人UID
	private int mSex;
	private GridView newDynamicGridView;
	private SpaceCustomAdapter mAdapter;
	private boolean oldDyanmicData = true;
	private boolean newDynamicData = true;
	protected SpaceBiz spaceBiz;

	@Override
	protected void initData() {
		spaceBiz = new SpaceBiz(this, mHandler);
		Bundle bundle = getIntent().getExtras();
		mFriendUid = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, -1);
		mSex = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, Gender.MALE.getValue());

		mAdapter = new SpaceCustomAdapter(this, mFriendUid, mSex);
		mPullRefreshListView.setAdapter(mAdapter);

		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if (userEntity != null && userEntity.uid.intValue() == mFriendUid) {//主人态
			mTitle.setText(getString(R.string.dynamic));
		} else {//客人太
			if (mSex == Gender.MALE.getValue()) {
				mTitle.setText(getString(R.string.str_king_dynamic));
			} else {
				mTitle.setText(getString(R.string.queena_dynamic));
			}
		}

		refreshData();
	}

	private void refreshData() {
		getTopicList(SpaceBiz.REFRESH_CODE);
	}

	private void getNewDynamicList() {
		DynamicRequseControl control = new DynamicRequseControl();
		control.getDynamicList(-1, 1, 1 | (1 << 30), mFriendUid, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.SPACE_TOPIC_LIST_VALUE:
			mPullRefreshListView.onRefreshComplete();
			TopicInfoList infolist = (TopicInfoList) msg.obj;
			if (!ListUtils.isEmpty(infolist)) {
				if (msg.arg1 == SpaceBiz.REFRESH_CODE) {
					mAdapter.clearList();
				}
				mAdapter.appendToList(infolist);
				for (Object object : infolist) {
					TopicInfo topicinfo = (TopicInfo) object;
					GoGirlDataInfoList dataInfoList = topicinfo.topiccontentlist;
					for (Object object2 : dataInfoList) {
						GoGirlDataInfo dataInfo = (GoGirlDataInfo) object2;
						if (dataInfo.type.intValue() == BAConstants.MessageType.GIFT.getValue()) {//礼物数据单独拿出来
							List<TopicInfo> topList = mAdapter.getList();
							if (topList != null && topList.contains(topicinfo)) {
								mAdapter.removeObject(topicinfo);
							}
						}
					}
				}
				if (msg.arg2 == 1) {
					mPullRefreshListView.setMode(Mode.PULL_FROM_START);
				} else {
					mPullRefreshListView.setMode(Mode.BOTH);
				}
			} else {
				mPullRefreshListView.setMode(Mode.BOTH);
			}
			if (BAApplication.mLocalUserInfo != null) {
				if (BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {//是自己才去加载本地失败数据
					BAApplication.getInstance().getJobManager().addJobInBackground(new GetFailureOrSendingTopicFromDbJob(this, false));
				}
			}
			if (mAdapter.getCount() == 1 && newDynamicData) {//空数据
				tv_foot_empty.setVisibility(View.VISIBLE);
			} else {
				oldDyanmicData = false;
			}
			getNewDynamicList();
			break;
		case HandlerValue.GET_DYNAMIC_SUCCESS:
			DynamicsInfoList dynamicsInfoList = (DynamicsInfoList) msg.obj;
			if (dynamicsInfoList != null && dynamicsInfoList.size() > 0) {
				NewDynamicAdapter adapter = new NewDynamicAdapter(this, mFriendUid);
				newDynamicGridView.setAdapter(adapter);
				adapter.appendToList((DynamicsInfo) dynamicsInfoList.get(0));
				newDynamicGridView.setVisibility(View.VISIBLE);
				newDynamicData = false;
			} else {
				newDynamicGridView.setVisibility(View.GONE);
				newDynamicData = true;
			}
			if (oldDyanmicData && newDynamicData) {
				tv_foot_empty.setVisibility(View.VISIBLE);
			} else {
				tv_foot_empty.setVisibility(View.GONE);
			}
			break;
		case HandlerValue.SPACE_QUERY_LOCAL_TOPIC_VALUE:
			TopicInfo tInfos = (TopicInfo) msg.obj;
			if (tInfos != null) {
				boolean flag = true;
				List<TopicInfo> topiclists = mAdapter.getList();
				for (TopicInfo topicInfo : topiclists) {
					if (topicInfo.createtime.intValue() == tInfos.createtime.intValue()) {
						flag = false;
					}
				}
				if (flag) {
					mAdapter.appendPositionToList(0, tInfos);
				}
			}
			break;
		case HandlerValue.SPACE_APPRECITATE_VALUE:
			refreshApprecitate(msg.arg1 + "" + msg.arg2);
			break;
		case HandlerValue.SPACE_REPLAYCOUNT_VALUE:
			refreshReplyCount(msg.arg1 + "" + msg.arg2);
			break;
		case HandlerValue.SPACE_REDUCE_REPLY_COUNT_VALUE:
			refreshReplyCountReduse(msg.arg1 + "" + msg.arg2);
			break;
		case HandlerValue.SPACE_GET_TOPIC_COUNT_VALUE:
			TopicCounterInfo info = (TopicCounterInfo) msg.obj;//贴子计数map
			String key = info.topicid.intValue() + "" + info.topicuid.intValue();
			mAdapter.getCountMap().put(key, info);
			mAdapter.notifyDataSetChanged();
			break;
		case HandlerValue.SPACE_DELETE_TOPICINFO_VALUE://删帖回来
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, R.string.str_delete_success);
				if (mAdapter.getCount() > msg.arg2)
					mAdapter.removePos(msg.arg2);
			} else {
				BaseUtils.showTost(this, R.string.str_delete_failed);
			}
			break;
		default:
			break;
		}
	}

	public void onEventMainThread(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE21) {
			sendHandlerMessage(mHandler, HandlerValue.SPACE_APPRECITATE_VALUE, event.getNum(), event.getNum2());
		}
		if (event.getFlag() == NoticeEvent.NOTICE23) {
			sendHandlerMessage(mHandler, HandlerValue.SPACE_REPLAYCOUNT_VALUE, event.getNum(), event.getNum2());
		}
		if (event.getFlag() == NoticeEvent.NOTICE64) {
			sendHandlerMessage(mHandler, HandlerValue.SPACE_REDUCE_REPLY_COUNT_VALUE, event.getNum(), event.getNum2());
		}
	}

	@SuppressWarnings("unchecked")
	public void onEvent(NoticeEvent event) {
		super.onEvent(event);
		if (event.getFlag() == NoticeEvent.NOTICE16) {//查询发送失败数据
			List<PublishDatabaseEntity> list = (List<PublishDatabaseEntity>) event.getObj();
			List<TopicInfo> lists = mAdapter.getList();
			spaceBiz.queryLocalTopicData(list, lists);
		}

	}

	/**
	 * @param key 点赞计数加1
	 */
	private void refreshApprecitate(String key) {
		TopicCounterInfo countInfo = mAdapter.getCountMap().get(key);
		if (countInfo != null) {
			countInfo.appreciatenum = BigInteger.valueOf((countInfo.appreciatenum.intValue() + 1));
		}
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 回复数加1
	 * @param key
	 */
	private void refreshReplyCount(String key) {
		TopicCounterInfo countInfo = mAdapter.getCountMap().get(key);
		if (countInfo != null) {
			countInfo.commentnum = BigInteger.valueOf((countInfo.commentnum.intValue() + 1));
		}
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 回复数加1
	 * @param key
	 */
	private void refreshReplyCountReduse(String key) {
		TopicCounterInfo countInfo = mAdapter.getCountMap().get(key);
		if (countInfo != null) {
			countInfo.commentnum = BigInteger.valueOf((countInfo.commentnum.intValue() - 1));
		}
		mAdapter.notifyDataSetChanged();
	}

	@SuppressLint("InflateParams")
	@Override
	protected void initRecourse() {
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mRightView = (ImageView) findViewById(R.id.title_iv_right);
		mRightView.setImageResource(R.drawable.broadcast_icon_write);
		mRightView.setVisibility(View.VISIBLE);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.space_custom_pulllistview);
		View headView = this.getLayoutInflater().inflate(R.layout.activity_all_dynamic_custom_head, null);
		View footerText = getLayoutInflater().inflate(R.layout.footer_custom, null);
		tv_foot_empty = (TextView) footerText.findViewById(R.id.space_custom_footer_empyt);
		newDynamicGridView = (GridView) headView.findViewById(R.id.space_new_dynamic_gridview);
		newDynamicGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle2 = new Bundle();
				bundle2.putInt(SpaceUserDynamicActivity.UID_FLAG, mFriendUid);
				bundle2.putInt(SpaceUserDynamicActivity.SEX_FLAG, mSex);
				BaseUtils.openActivity(SpaceAllDynamicActivity.this, SpaceUserDynamicActivity.class, bundle2);
			}
		});

		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.getRefreshableView().setOnItemLongClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.getRefreshableView().addHeaderView(headView);
		mPullRefreshListView.getRefreshableView().addFooterView(footerText);

		setListener();
	}

	private void setListener() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		findViewById(R.id.title_lin_right).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString(MineWriteActivity.FROM_FLAG, MineWriteActivity.PUBLIC_DYNAMIC);
				BaseUtils.openActivity(SpaceAllDynamicActivity.this, MineWriteActivity.class, bundle);
			}
		});
	}

	private void getTopicList(int code) {
		spaceBiz.getUserTopicList(mFriendUid, code);
	}

	@Override
	protected int initView() {
		return R.layout.activity_space_all_dynamic;
	}

	public static void openMineFaqActivity(Activity activity, int fuid, int sex) {
		Bundle bundle = new Bundle();
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, fuid);
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
		BaseUtils.openActivity(activity, SpaceAllDynamicActivity.class, bundle);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {
			if (position > 1) {
				TopicInfo info = mAdapter.getList().get(position - 2);
				new DeleteTopicInfoDialog(this, android.R.style.Theme_Translucent_NoTitleBar, info, position, mHandler).showDialog();
			}
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		if (position < 2) {
			return;
		}

		try {
			TopicInfo topicInfo = (TopicInfo) parent.getAdapter().getItem(position);
			BAParseRspData parser = new BAParseRspData();
			GoGirlDataInfoList dataInfoList = topicInfo.topiccontentlist;
			ContentData data = parser.parseTopicInfo(this, dataInfoList, mSex);
			if (data.getType() == BAConstants.MessageType.GIFT.getValue()) {
				OperateViewUtils.intentMineShowAllGiftListActivity(this, mFriendUid, mSex);
			} else if (data.getType() == BAConstants.MessageType.UPLOAD_PHOTO.getValue()) {
				Intent intent = new Intent(this, MineNetPhotosListActivity.class);
				//将相册ID传到上传界面
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, 0);
				String albumName = "相册";
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMNAME, albumName);
				intent.putExtra("viewpeopleuid", topicInfo.uid.intValue());
				intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMCOUNT, 0);
				startActivity(intent);
				overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
			} else {
				OperateViewUtils.intentSpaceCustomDetailActivity(this, mAdapter, topicInfo, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSuccess(int code, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.GET_DYNAMIC_SUCCESS;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onError(int code) {

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		getTopicList(SpaceBiz.LOAD_MORE_CODE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		spaceBiz.setHandler(null);//防止数据继续返回回来
		mHandler.removeCallbacksAndMessages(null);
	}

}
