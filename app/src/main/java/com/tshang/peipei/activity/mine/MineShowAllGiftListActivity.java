package com.tshang.peipei.activity.mine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.main.MainHallPagerAdapter;
import com.tshang.peipei.activity.mine.MineShowAllGiftListAdapter.IFeedKiss;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.model.biz.space.SpaceRelationshipBiz;
import com.tshang.peipei.model.biz.user.ShowGiftBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackFeedBackKiss;
import com.tshang.peipei.model.bizcallback.BizCallBackShowGiftToday;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.view.HeaderGridView;
import com.tshang.peipei.view.PullToRefreshHeaderGridView;

/**
 * @Title: ShowAllGiftListActivity.java 
 *
 * @Description: 女生收到礼物的列表
 *
 * @author allen  
 *
 * @date 2014-4-12 下午1:50:50 
 *
 * @version V1.0   
 */
public class MineShowAllGiftListActivity extends BaseActivity implements BizCallBackShowGiftToday, IFeedKiss, BizCallBackFeedBackKiss {

	private PullToRefreshHeaderGridView mShowSendGiftGridView;
	private PullToRefreshHeaderGridView mShowReceiveGiftGridView;

	private MineShowAllGiftListAdapter mSendAdapter;
	private MineShowAllGiftListAdapter mReceiveAdapter;
	private int LOADCOUNT = 12;

	private int mUid = -1;
	private int startLoadPosition1 = -1;
	private boolean isRefreshSend = true;
	private TextView mTextEmpty1;
	private int startLoadPosition2 = -1;
	private boolean isRefreshReceive = true;
	private TextView mTextEmpty2;
	private int mCurrPostion;

	private ViewPager mViewPager;
	private View mViewSend, mViewReceive;
	private int mCurrPage = 0;// 当前页卡编号

	private TextView mSendTv, mReceiveTv;
	private int mSex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		BaseUtils.cancelDialog();
		switch (msg.what) {
		case HandlerType.CREATE_GETDATA_BACK:
			mShowSendGiftGridView.onRefreshComplete();
			mShowReceiveGiftGridView.onRefreshComplete();
			if (msg.arg1 == 0) {
				GiftDealInfoList gift = (GiftDealInfoList) msg.obj;
				if (msg.arg2 == 0) {
					resultSendGift(gift);
				} else {
					resultReceiveGift(gift);
				}
			}
			if (msg.arg2 == 0) {
				if (mSendAdapter.getCount() == 0) {
					mShowSendGiftGridView.setEmptyView(mTextEmpty1);
				}
			} else {
				if (mReceiveAdapter.getCount() == 0) {
					mShowReceiveGiftGridView.setEmptyView(mTextEmpty2);
				}
			}
			break;
		case HandlerType.FEED_BACK_KISS:
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, R.string.feedkiss_success);
				mReceiveAdapter.freshAdapterByFeed(mCurrPostion);
			} else {
				BaseUtils.showTost(this, R.string.feedkiss_failed);
			}
			break;
		default:
			break;
		}
	}

	private void resultSendGift(GiftDealInfoList gift) {
		if (null != gift && !gift.isEmpty()) {
			if (isRefreshSend) {
				mSendAdapter.clearList();
				if (gift.size() == LOADCOUNT) {
					mShowSendGiftGridView.setMode(Mode.BOTH);//允许加载更多
				}
			}
			if (gift.size() < LOADCOUNT) {
				mShowSendGiftGridView.setMode(Mode.PULL_FROM_START);//如果加载更多小于12条数据，就不能够继续上拉了
			}
			Collections.reverse(gift);
			for (Object object : gift) {
				GiftDealInfo info = (GiftDealInfo) object;
				int giftNum = info.giftnum.intValue();
				for (int i = 0; i < giftNum; i++) {
					GiftDealInfo temp = new GiftDealInfo();
					temp.createtime = info.createtime;
					temp.from = info.from;
					temp.fromnick = info.fromnick;
					temp.fromsex = info.fromsex;
					temp.gift = info.gift;
					temp.giftdealstatus = info.giftdealstatus;
					temp.giftnum = info.giftnum;
					temp.id = info.id;
					temp.to = info.to;
					temp.tonick = info.tonick;
					temp.tosex = info.tosex;
					mSendAdapter.appendPositionToList(mSendAdapter.getCount(), temp);
				}

			}
		} else {
			mShowSendGiftGridView.setMode(Mode.PULL_FROM_START);
		}
	}

	private void resultReceiveGift(GiftDealInfoList gift) {
		if (null != gift && !gift.isEmpty()) {
			if (isRefreshReceive) {
				mReceiveAdapter.clearList();
				if (gift.size() == LOADCOUNT) {
					mShowReceiveGiftGridView.setMode(Mode.BOTH);//允许加载更多
				}
			}
			if (gift.size() < LOADCOUNT) {
				mShowReceiveGiftGridView.setMode(Mode.PULL_FROM_START);//如果加载更多小于12条数据，就不能够继续上拉了
			}
			Collections.reverse(gift);
			for (Object object : gift) {
				GiftDealInfo info = (GiftDealInfo) object;
				int giftNum = info.giftnum.intValue();
				for (int i = 0; i < giftNum; i++) {
					GiftDealInfo temp = new GiftDealInfo();
					temp.createtime = info.createtime;
					temp.from = info.from;
					temp.fromnick = info.fromnick;
					temp.fromsex = info.fromsex;
					temp.gift = info.gift;
					temp.giftdealstatus = info.giftdealstatus;
					temp.giftnum = info.giftnum;
					temp.id = info.id;
					temp.to = info.to;
					temp.tonick = info.tonick;
					temp.tosex = info.tosex;
					mReceiveAdapter.appendPositionToList(mReceiveAdapter.getCount(), temp);
				}
			}
		} else {
			mShowReceiveGiftGridView.setMode(Mode.PULL_FROM_START);
		}
	}

	@Override
	public void showGiftToday(int retcode, int total, int currpage, GiftDealInfoList giftInfoList) {
		sendHandlerMessage(mHandler, HandlerType.CREATE_GETDATA_BACK, retcode, currpage, giftInfoList);

	}

	/**
	 * 刷新数据源
	 * @author Jeff
	 *
	 */
	private void setRefreshData() {
		if (mCurrPage == 0) {
			isRefreshSend = true;
			startLoadPosition1 = -1;
		} else {
			isRefreshReceive = true;
			startLoadPosition2 = -1;
		}
		loadData();
	}

	//上拉刷新 ,下拉加载更多刷新 
	class PullToRefreshListener implements OnRefreshListener2<HeaderGridView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
			setRefreshData();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
			if (mCurrPage == 0) {
				isRefreshSend = false;
				startLoadPosition1 = startLoadPosition1 - LOADCOUNT;
			} else {
				isRefreshReceive = false;
				startLoadPosition2 = startLoadPosition2 - LOADCOUNT;
			}
			loadData();
		}

	}

	private void loadData() {
		ShowGiftBiz storeGiftBiz = new ShowGiftBiz();
		if (mCurrPage == 0) {
			storeGiftBiz.showGiftToday(this, mUid, startLoadPosition1, LOADCOUNT, this, mCurrPage);
		} else {
			storeGiftBiz.showGiftToday(this, mUid, startLoadPosition2, LOADCOUNT, this, mCurrPage);
		}
	}

	@Override
	protected void initData() {
		setViewPageAndUnderline();

		setRefreshData();

		if (mSex == Gender.FEMALE.getValue()) {
			mViewPager.setCurrentItem(1);
		}
	}

	@Override
	protected void initRecourse() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			mUid = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, -1);
			mSex = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, 0);
		}

		LayoutInflater mInflater = getLayoutInflater();
		mViewSend = mInflater.inflate(R.layout.viewpager_send, null);
		mViewReceive = mInflater.inflate(R.layout.viewpager_receive, null);

		mViewPager = (ViewPager) findViewById(R.id.gift_viewpager);

		mSendTv = (TextView) findViewById(R.id.tv_send_gift);
		mReceiveTv = (TextView) findViewById(R.id.tv_receive_gift);
		mSendTv.setOnClickListener(this);
		mReceiveTv.setOnClickListener(this);

		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.gifts);

		mShowSendGiftGridView = (PullToRefreshHeaderGridView) mViewSend.findViewById(R.id.showgift_all_gridview_send);
		mShowSendGiftGridView.setMode(Mode.PULL_FROM_START);
		mShowReceiveGiftGridView = (PullToRefreshHeaderGridView) mViewReceive.findViewById(R.id.showgift_all_gridview_receive);
		mShowReceiveGiftGridView.setMode(Mode.PULL_FROM_START);

		mSendAdapter = new MineShowAllGiftListAdapter(this, 1);
		mReceiveAdapter = new MineShowAllGiftListAdapter(this, 0);
		if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == mUid) {
			mReceiveAdapter.setMIsMySelf(true);
			mReceiveAdapter.setFeedListener(this);
		}
		mShowSendGiftGridView.setAdapter(mSendAdapter);
		mShowSendGiftGridView.setOnRefreshListener(new PullToRefreshListener());
		mShowReceiveGiftGridView.setAdapter(mReceiveAdapter);
		mShowReceiveGiftGridView.setOnRefreshListener(new PullToRefreshListener());
		BaseUtils.showDialog(this, R.string.loading);
		mTextEmpty1 = (TextView) findViewById(R.id.tv_no_send);
		mTextEmpty2 = (TextView) findViewById(R.id.tv_no_receive);

	}

	@Override
	protected int initView() {
		return R.layout.activity_showgift_all;
	}

	@Override
	public void feedKiss(int fuid, int dealId, int position, View v) {
		if (BAApplication.mLocalUserInfo != null) {
			//			BaseUtils.getDialog(MineShowAllGiftListActivity.this, R.string.write_sending);
			mCurrPostion = position;
			SpaceRelationshipBiz sBiz = new SpaceRelationshipBiz(this);
			sBiz.feedbackKiss(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), fuid,
					dealId, this);
		}
	}

	private void setViewPageAndUnderline() {
		List<View> mViewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		mViewList.add(mShowSendGiftGridView);
		mViewList.add(mShowReceiveGiftGridView);

		mViewPager.setAdapter(new MainHallPagerAdapter(mViewList));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new FansOnPageChangeListener());
	}

	/**
	 * 页卡切换监听
	 */
	public class FansOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			mCurrPage = arg0;
			if (arg0 == 0) {
				mSendTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
				mReceiveTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
				mSendTv.setTextColor(getResources().getColor(R.color.peach));
				mReceiveTv.setTextColor(getResources().getColor(R.color.gray));

				if (mSendAdapter.getCount() == 0) {
					loadData();
				}
			} else {
				mReceiveTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
				mSendTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
				mReceiveTv.setTextColor(getResources().getColor(R.color.peach));
				mSendTv.setTextColor(getResources().getColor(R.color.gray));
				if (mReceiveAdapter.getCount() == 0) {
					loadData();
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

	@Override
	public void feedBackKiss(int retCode) {
		sendHandlerMessage(mHandler, HandlerType.FEED_BACK_KISS, retCode);
	}

	@Override
	public void onItem(GiftDealInfo giftInfo) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		if (BAApplication.mLocalUserInfo.uid.intValue() != mUid) {
			return;
		}

		Intent intent = new Intent(MineShowAllGiftListActivity.this, MineShowGiftDialogActivity.class);
		intent.putExtra("nick", new String(giftInfo.fromnick));
		intent.putExtra("name", new String(giftInfo.gift.name));
		intent.putExtra("invate", giftInfo.gift.scoreeffect.intValue() + "");
		intent.putExtra("glamour", giftInfo.gift.charmeffect.intValue() + "");
		intent.putExtra("loyalty", giftInfo.gift.loyaltyeffect.intValue() + "");
		intent.putExtra("imagepic", new String(giftInfo.gift.pickey));
		intent.putExtra("friendsex", giftInfo.fromsex.intValue());
		intent.putExtra("frienduid", giftInfo.from.intValue());
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_send_gift:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.tv_receive_gift:
			mViewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}
}
