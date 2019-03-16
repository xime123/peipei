package com.tshang.peipei.activity.show;

import java.util.ArrayList;
import java.util.List;

import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.show.adapter.GiftHistoryAdapter;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.showrooms.RoomsGetBiz;
import com.tshang.peipei.protocol.Gogirl.GiftDealInfoP;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: ShowGiftHistoryActivity.java 
 *
 * @Description: 秀场礼物详情
 *
 * @author allen 
 *
 * @date 2015-3-9 下午5:12:37 
 *
 * @version V1.0   
 */
public class ShowGiftHistoryActivity extends BaseActivity {

	private RoomsGetBiz roomsGetBiz;

	private PullToRefreshListView prListViewGift;
	private GiftHistoryAdapter mGiftAdapter;

	private List<GiftDealInfoP> mListInfo = new ArrayList<GiftDealInfoP>();
	private boolean isFresh;

	private TextView emptyGift;

	@Override
	protected void initData() {
		roomsGetBiz = new RoomsGetBiz(this, mHandler);
		roomsGetBiz.getShowGiftHistory(BAApplication.showRoomInfo.getOwneruserinfo().getUid(), 0, 20);
	}

	@Override
	protected void initRecourse() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_show_gift_title);

		emptyGift = (TextView) findViewById(R.id.show_empty_tv);
		prListViewGift = (PullToRefreshListView) findViewById(R.id.gift_history_list_lv);

		prListViewGift.setOnRefreshListener(new PullToRefreshListener());
		prListViewGift.setMode(Mode.BOTH);

		mGiftAdapter = new GiftHistoryAdapter(this);
		mGiftAdapter.setList(mListInfo);
		prListViewGift.setAdapter(mGiftAdapter);
	}

	@Override
	protected int initView() {
		return R.layout.activity_show_gift_history;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_left:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.SHOW_ROOM_GIFT_HISTORY:
			prListViewGift.onRefreshComplete();
			if (msg.arg1 == 0) {
				if (isFresh) {
					mGiftAdapter.clearList();
				}
				mGiftAdapter.appendToList((List<GiftDealInfoP>) msg.obj);
				if (msg.arg2 == 1) {
					prListViewGift.setMode(Mode.PULL_FROM_START);
				} else {
					prListViewGift.setMode(Mode.BOTH);
				}

				if (mGiftAdapter.getCount() == 0) {
					prListViewGift.setEmptyView(emptyGift);
				}
			}
			break;
		default:
			break;
		}
	}

	//上拉刷新 ,下拉加载更多刷新 
	class PullToRefreshListener implements OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = true;
			roomsGetBiz.getShowGiftHistory(BAApplication.showRoomInfo.getOwneruserinfo().getUid(), 0, 20);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = false;
			roomsGetBiz.getShowGiftHistory(BAApplication.showRoomInfo.getOwneruserinfo().getUid(), mGiftAdapter.getCount(), 20);
		}

	}
}
