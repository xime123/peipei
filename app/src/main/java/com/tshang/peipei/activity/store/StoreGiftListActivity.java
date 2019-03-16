package com.tshang.peipei.activity.store;

import java.io.File;
import java.math.BigInteger;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.BuyGiftDialog;
import com.tshang.peipei.activity.dialog.SentGiftResultDialog;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.biz.chat.SaveChatData;
import com.tshang.peipei.model.biz.store.StoreGiftBiz;
import com.tshang.peipei.model.biz.store.StoreUserBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserProperty;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.viewdatacache.GiftListCacheViewData;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil.ConfigCacheModel;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.operate.RelationOperate;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.view.HeaderGridView;
import com.tshang.peipei.view.PullToRefreshHeaderGridView;

import de.greenrobot.event.EventBus;

/**
 * @Title: StoreGiftListActivity.java 
 *
 * @Description: 展示商城礼物列表
 *
 * @author allen  
 *
 * @date 2014-4-11 下午6:03:12 
 *
 * @version V1.0   
 */
public class StoreGiftListActivity extends BaseActivity implements BizCallBackGetUserProperty, OnItemClickListener,
		OnRefreshListener2<HeaderGridView> {

	private TextView mGoldText, mSilverText;
	private PullToRefreshHeaderGridView mGiftGridView;

	private StoreGiftListAdapter mAdapter;

	private int mFriendUid;
	private String mFriendNick;
	private int mSex;
	private int mGoldNum, mSilverNum;
	private int giftNum = -1;
	private long lastChatItemCreateTime = -1;
	private GiftInfo currGiftInfo = null;

	private int from;
	private boolean isGetNewData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		BaseUtils.cancelDialog();
		switch (msg.what) {
		case HandlerType.CREATE_GETDATA_BACK:
			mGiftGridView.onRefreshComplete();
			if (msg.arg1 == 0) {
				GiftInfoList infoList = (GiftInfoList) msg.obj;
				if (infoList != null) {
					mAdapter.setList(infoList);
					GiftListCacheViewData.setSendGiftListCacheData(this, GiftListCacheViewData.FILE_NAME_START, infoList);
					if (isGetNewData) {
						SharedPreferencesTools.getInstance(this).saveBooleanKeyValue(false, BAConstants.PEIPEI_APP_CONFIG_GIFT_NEED_UPDATE);
					}
				}
			}
			break;
		case HandlerType.GIFT_BUY_RETURN:
			giftNum = msg.arg1;

			switch (currGiftInfo.id.intValue()) {
			case 53:
				//				MobclickAgent.onEvent(StoreGiftListActivity.this, "YouZhiYueBingSongLiCiShu");
				break;
			case 54:
				//				MobclickAgent.onEvent(StoreGiftListActivity.this, "HeZhuangYueBingSongLiCiShu");
				break;
			case 55:
				//				MobclickAgent.onEvent(StoreGiftListActivity.this, "ChangXiangSiSongLiCiShu");
				break;
			case 56:
				//				MobclickAgent.onEvent(StoreGiftListActivity.this, "YueTuCiFuSongLiCiShu");
				break;
			}
			if (from == RewardListActivity.CHAT_FROM_REWARD) {
				new StoreGiftBiz().buyGiftV3(this, currGiftInfo.id.intValue(), mFriendUid, msg.arg1, 2, mHandler);
			} else
				new StoreGiftBiz().buyGift(this, currGiftInfo.id.intValue(), mFriendUid, msg.arg1, msg.arg2, mHandler);
			break;
		case HandlerType.USER_PROPERTY_BACK:
			if (msg.arg1 == 0) {
				UserPropertyInfo user = (UserPropertyInfo) msg.obj;
				if (BAApplication.mLocalUserInfo != null && user.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {//防止看别人的财富值
					mGoldNum = user.goldcoin.intValue();
					mSilverNum = user.silvercoin.intValue();
					mGoldText.setText(String.valueOf(mGoldNum));
					mSilverText.setText(String.valueOf(mSilverNum));
				}
			}
			break;
		case HandlerType.USER_GIFT_REQUEST://礼物购买成功
			if (msg.arg1 == 0) {
				getUserProperty(true);
				RelationOperate relationOperate = RelationOperate.getInstance(this);
				if (relationOperate.isHaveSession(mFriendUid)) {
					relationOperate.updateRelation(mFriendUid, 1);
				}
				saveGiftToDB();
				new SentGiftResultDialog(StoreGiftListActivity.this, R.string.gift_sent_success, R.string.str_find_her_chat,
						R.string.str_say_later_again, mFriendUid, mSex, mFriendNick).showDialog();
			} else if (msg.arg1 == -28210) {
				BaseUtils.showTost(this, "不能赠送挂件礼物!");
			} else if (msg.arg1 == BAConstants.rspContMsgType.E_GG_EFFECTS_GIFT_EXCEED_MAX_NUM) {
				BaseUtils.showTost(this, "挂件礼物每次只能送一个哦!");
			} else if (msg.arg2 == -28303) {
				BaseUtils.showTost(this, "匿名昵称已过期，不能继续送礼");
			} else {
				if (currGiftInfo.pricegold.intValue() * giftNum > mGoldNum) {
					new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, mGoldNum, mSilverNum).showDialog();
				}

				if (currGiftInfo.pricesilver.intValue() * giftNum > mSilverNum) {
					new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, mGoldNum, mSilverNum).showDialog();
				}
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	private void saveGiftToDB() {
		if (giftNum <= 0) {
			return;
		}
		ChatDatabaseEntity entity = new ChatDatabaseEntity();

		entity.setFromID(BAApplication.mLocalUserInfo.uid.intValue());
		entity.setToUid(mFriendUid);
		entity.setDes(BAConstants.ChatDes.TO_FRIEDN.getValue());
		if (from == RewardListActivity.CHAT_FROM_REWARD) {
			entity.setType(BAConstants.MessageType.GOGIRL_DATA_TYPE_NEW_RECEIVE_ANONYM_GIFT_PUSH_DATA.getValue());
		} else
			entity.setType(BAConstants.MessageType.NEW_GIFT.getValue());
		entity.setRevStr1(mSex + "");
		entity.setRevStr2(mFriendNick);
		entity.setRevStr3("1");
		entity.setMesLocalID(1);
		entity.setMesSvrID("");
		entity.setStatus(3);
		//处理本地时间与服务器时间不一致引起的顺序有误
		if (System.currentTimeMillis() >= lastChatItemCreateTime) {
			entity.setCreateTime(System.currentTimeMillis());
		} else {
			entity.setCreateTime(lastChatItemCreateTime + 60 * 1000);
		}

		GiftDealInfoList list = new GiftDealInfoList();
		for (int i = 0; i < giftNum; i++) {
			GiftDealInfo info = new GiftDealInfo();
			info.gift = currGiftInfo;
			info.giftnum = BigInteger.valueOf(giftNum);
			list.add(info);
		}
		String message = ChatMessageBiz.saveGiftMessage(list);
		entity.setMessage(message);
		ChatOperate chatDatabase = ChatOperate.getInstance(StoreGiftListActivity.this, entity.getToUid(), false);
		if (chatDatabase != null) {
			chatDatabase.insert(entity);
		}

		SaveChatData saveSession = new SaveChatData();
		ChatDatabaseEntity newEntity = new ChatDatabaseEntity();
		newEntity = entity;
		//		if (from == RewardListActivity.CHAT_FROM_REWARD) {
		//			newEntity.setFromID(BAApplication.mLocalUserInfo.uid.intValue());
		//		} else
		newEntity.setFromID(mFriendUid);
		newEntity.setToUid(BAApplication.mLocalUserInfo.uid.intValue());
		saveSession.saveSessionMessage(
				this,
				from == RewardListActivity.CHAT_FROM_REWARD ? 3 : 0,
				newEntity,
				String.format(getString(R.string.chat_gift_context_message), new String(entity.getRevStr2()), newEntity.getRevStr2(),
						newEntity.getRevStr1()), mFriendNick, mSex);

		NoticeEvent noticeEvent = new NoticeEvent();
		noticeEvent.setFlag(NoticeEvent.NOTICE97);
		noticeEvent.setObj(entity);
		EventBus.getDefault().postSticky(noticeEvent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getUserProperty(false);

	}

	private void getUserProperty(boolean isSendSuccess) {//获取用户的财富值
		if (null != BAApplication.mLocalUserInfo) {
			if (isSendSuccess) {
				//				MobclickAgent.onEvent(this, "songlirenshu");
			}
			StoreUserBiz.getInstance().getUserProperty(this, BAApplication.mLocalUserInfo.uid.intValue(), this);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.gift_recharge:
			Bundle bundle = new Bundle();
			bundle.putString("gold", String.valueOf(mGoldNum));
			bundle.putString("silver", String.valueOf(mSilverNum));
			BaseUtils.openActivity(this, StoreH5RechargeActivity.class, bundle);
			break;

		}
	}

	@Override
	public void getUserProperty(int retCode, UserPropertyInfo userPropertyInfo) {
		sendHandlerMessage(mHandler, HandlerType.USER_PROPERTY_BACK, retCode, userPropertyInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initData() {
		mFriendUid = getIntent().getExtras().getInt("fuid", -1);
		mFriendNick = getIntent().getExtras().getString("fNick");
		mSex = getIntent().getExtras().getInt("fSex", 0);
		lastChatItemCreateTime = getIntent().getLongExtra("time", -1);
		from = getIntent().getIntExtra("from", -1);
		File cacheFile = ConfigCacheUtil.getCacheFile(this, GiftListCacheViewData.FILE_NAME_START);//读取缓存数据
		isGetNewData = SharedPreferencesTools.getInstance(this).getBooleanKeyValue(BAConstants.PEIPEI_APP_CONFIG_GIFT_NEED_UPDATE); //是否使用缓存，为true则不使用，false为使用缓存
		if (isGetNewData) {
			getGiftList();
		} else if (cacheFile == null) {
			getGiftList();
		} else {
			GiftInfoList lists = GiftListCacheViewData.getSendGiftListCacheData(this, GiftListCacheViewData.FILE_NAME_START);
			if (lists != null && !lists.isEmpty()) {
				if (!ConfigCacheUtil.getUrlCacheisEffective(cacheFile, ConfigCacheModel.CONFIG_CACHE_MODEL_ML)) {
					getGiftList();
				}
				mAdapter.setList(lists);
			} else {
				getGiftList();
			}
		}
	}

	private void getGiftList() {
		BaseUtils.showDialog(this, R.string.loading);
		new StoreGiftBiz().getGiftList(this, mHandler);
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.gifts);

		mGiftGridView = (PullToRefreshHeaderGridView) findViewById(R.id.gift_gridview);
		mAdapter = new StoreGiftListAdapter(this);
		mGiftGridView.setOnItemClickListener(this);
		mGiftGridView.setOnRefreshListener(this);

		View headview = this.getLayoutInflater().inflate(R.layout.view_send_gift_wealth, null);
		mGoldText = (TextView) headview.findViewById(R.id.gift_gold);
		mSilverText = (TextView) headview.findViewById(R.id.gift_silver);
		headview.findViewById(R.id.gift_recharge).setOnClickListener(this);
		mGiftGridView.getRefreshableView().addHeaderView(headview);
		mGiftGridView.setAdapter(mAdapter);

	}

	@Override
	protected int initView() {
		return R.layout.activity_giftlist;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		GiftInfo giftInfo = (GiftInfo) parent.getAdapter().getItem(position);
		currGiftInfo = giftInfo;

		new BuyGiftDialog(StoreGiftListActivity.this, mGoldNum, mSilverNum, mFriendNick, new String(giftInfo.name),
				giftInfo.loyaltyeffect.intValue(), giftInfo.charmeffect.intValue(), new String(giftInfo.pickey), giftInfo, mHandler, from)
				.showDialog();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
		getUserProperty(false);
		new StoreGiftBiz().getGiftList(this, mHandler);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {}
}
