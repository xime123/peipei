package com.tshang.peipei.activity.show;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.main.MainHallPagerAdapter;
import com.tshang.peipei.activity.show.adapter.MembersDevoteAdapter;
import com.tshang.peipei.activity.show.adapter.RoomMembersAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.InOutAct;
import com.tshang.peipei.base.babase.BAConstants.ProtobufErrorCode;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.showrooms.RoomsGetBiz;
import com.tshang.peipei.protocol.Gogirl.RspGetRoomMemberList;
import com.tshang.peipei.protocol.Gogirl.ShowRoomMemberInfo;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: ShowRoomMemberActivity.java 
 *
 * @Description: 秀场成员列表界面
 *
 * @author allen
 *
 * @date 2015-1-22 下午4:41:19 
 *
 * @version V1.0   
 */
public class ShowRoomMemberActivity extends BaseActivity {

	private int showRoomId;
	private int roomUid;

	private RoomsGetBiz roomsGetBiz;

	private ViewPager mViewPager;
	private View mViewMember, mViewDevote;
	private TextView mTvMember;
	private List<ShowRoomMemberInfo> mListMemberInfos = new ArrayList<ShowRoomMemberInfo>();
	private ListView mMemberListVIew;
	private RoomMembersAdapter mRoomAdapter;
	private TextView mTvMemberTitle, mTvDevoteTitle;
	private TextView mTvDevoteDay, mTvDevoteAll;
	private PullToRefreshListView prListViewDay, prListViewAll;
	private MembersDevoteAdapter mDevoteAdapter;
	private List<ShowRoomMemberInfo> mListDevoteDay = new ArrayList<ShowRoomMemberInfo>();
	private List<ShowRoomMemberInfo> mListDevoteAll = new ArrayList<ShowRoomMemberInfo>();

	private TextView emptyShowMember, emptyDevoteDay, emptyDevoteAll;

	private boolean isFreshMember;
	private boolean isFreshDevoteDay, isFreshDevoteAll;

	private int type = 0;

	@Override
	protected void initData() {
		roomsGetBiz = new RoomsGetBiz(this, mHandler);
		isFreshMember = true;
		roomsGetBiz.getRoomMemberList(showRoomId, roomUid, -1, 100);
	}

	@Override
	protected void initRecourse() {
		if (BAApplication.showRoomInfo != null) {
			showRoomId = BAApplication.showRoomInfo.getRoomid();
			roomUid = BAApplication.showRoomInfo.getOwneruserinfo().getUid();
		}

		findViewById(R.id.show_member_title_tv_back).setOnClickListener(this);
		mTvMemberTitle = (TextView) findViewById(R.id.show_member_title_tv_member);
		mTvDevoteTitle = (TextView) findViewById(R.id.show_member_title_tv_devote);
		mTvDevoteTitle.setOnClickListener(this);
		mTvMemberTitle.setOnClickListener(this);

		mViewPager = (ViewPager) findViewById(R.id.show_member_viewpager);

		LayoutInflater mInflater = getLayoutInflater();
		mViewMember = mInflater.inflate(R.layout.viewpager_show_menber, null);
		mViewDevote = mInflater.inflate(R.layout.viewpager_show_devote, null);

		mTvMember = (TextView) mViewMember.findViewById(R.id.show_member_num);
		mMemberListVIew = (ListView) mViewMember.findViewById(R.id.show_member_listview);
		mRoomAdapter = new RoomMembersAdapter(this, roomUid, showRoomId, mHandler);
		mRoomAdapter.setList(mListMemberInfos);
		mMemberListVIew.setAdapter(mRoomAdapter);
		emptyShowMember = (TextView) mViewMember.findViewById(R.id.show_member_empty_tv);

		mTvDevoteDay = (TextView) mViewDevote.findViewById(R.id.tv_show_devote_day);
		mTvDevoteAll = (TextView) mViewDevote.findViewById(R.id.tv_show_devote_all);
		mTvDevoteDay.setOnClickListener(this);
		mTvDevoteAll.setOnClickListener(this);
		emptyDevoteDay = (TextView) mViewDevote.findViewById(R.id.show_devote_day_empty_tv);
		emptyDevoteAll = (TextView) mViewDevote.findViewById(R.id.show_devote_all_empty_tv);

		List<View> mViewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		mViewList.add(mViewMember);
		mViewList.add(mViewDevote);

		mViewPager.setAdapter(new MainHallPagerAdapter(mViewList));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new FindOnPageChangeListener());

		prListViewDay = (PullToRefreshListView) mViewDevote.findViewById(R.id.devote_day_listview);
		prListViewAll = (PullToRefreshListView) mViewDevote.findViewById(R.id.devote_all_listview);

		prListViewDay.setOnRefreshListener(new PullToRefreshListener());
		prListViewAll.setOnRefreshListener(new PullToRefreshListener());
		prListViewDay.setMode(Mode.BOTH);
		prListViewAll.setMode(Mode.BOTH);

		mDevoteAdapter = new MembersDevoteAdapter(this);
		prListViewDay.setAdapter(mDevoteAdapter);
		prListViewAll.setAdapter(mDevoteAdapter);
	}

	@Override
	protected int initView() {
		return R.layout.activity_show_member;
	}

	/**
	 * 页卡切换监听
	 */
	public class FindOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				mTvMemberTitle.setTextColor(getResources().getColor(R.color.white));
				mTvDevoteTitle.setTextColor(getResources().getColor(R.color.peach));
				mTvMemberTitle.setBackgroundResource(R.drawable.main_bar_tab_pr);
				mTvDevoteTitle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			} else {
				mTvMemberTitle.setTextColor(getResources().getColor(R.color.peach));
				mTvDevoteTitle.setTextColor(getResources().getColor(R.color.white));
				mTvMemberTitle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				mTvDevoteTitle.setBackgroundResource(R.drawable.main_bar_tab_pr);
				if (mListDevoteDay.size() == 0) {
					roomsGetBiz.getDevoteRank(showRoomId, roomUid, -1, 10, 0);
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.SHOW_ROOM_GET_MEMBER_LIST:
			if (msg.arg1 == 0) {
				RspGetRoomMemberList list = (RspGetRoomMemberList) msg.obj;
				if (list != null) {
					List<ShowRoomMemberInfo> getMemberlistList = list.getMemberlistList();
					mTvMember.setText("房间成员（" + list.getTotal() + "/" + BAApplication.showRoomInfo.getMaxmembernum() + ")");
					ArrayList<ShowRoomMemberInfo> aList = new ArrayList<ShowRoomMemberInfo>();
					aList.addAll(getMemberlistList);
					Collections.reverse(aList);
					if (isFreshMember) {
						mRoomAdapter.clearList();
					}
					mRoomAdapter.appendToList(aList);
					if (mRoomAdapter.getCount() == 0) {
						mMemberListVIew.setEmptyView(emptyShowMember);
					}
				}
			}
			break;
		case HandlerValue.SHOW_ROOM_GET_DEVOTERANK:
			prListViewDay.onRefreshComplete();
			prListViewAll.onRefreshComplete();
			if (msg.arg1 == 0) {
				if (msg.arg2 == 0) {
					mDevoteAdapter.setType(0);
					if (isFreshDevoteDay) {
						mListDevoteDay.clear();
					}
					mListDevoteDay.addAll((List<ShowRoomMemberInfo>) msg.obj);
					mDevoteAdapter.setList(mListDevoteDay);
					if (mDevoteAdapter.getCount() == 0) {
						prListViewDay.setEmptyView(emptyDevoteDay);
					}
				} else if (msg.arg2 == 1) {
					if (isFreshDevoteAll) {
						mListDevoteAll.clear();
					}
					mDevoteAdapter.setType(1);
					mListDevoteAll.addAll((List<ShowRoomMemberInfo>) msg.obj);
					mDevoteAdapter.setList(mListDevoteAll);
					if (mDevoteAdapter.getCount() == 0) {
						prListViewAll.setEmptyView(emptyDevoteAll);
					}
				}

			}
			break;
		case HandlerValue.SHOW_ROOM_MEMBER_ROLE:
			if (msg.arg1 == 0) {
				isFreshMember = true;
				roomsGetBiz.getRoomMemberList(showRoomId, roomUid, -1, 10);
			} else if (msg.arg1 == ProtobufErrorCode.MaxRoleCountError) {
				BaseUtils.showTost(this, "设置失败，超过设置嘉宾人数限制");
			} else {
				BaseUtils.showTost(this, R.string.operate_faile);
			}
			break;
		case HandlerValue.SHOW_ROOM_IN_OUT:
			if (msg.arg1 == 0) {
				if (msg.arg2 == InOutAct.kick) {
					isFreshMember = true;
					roomsGetBiz.getRoomMemberList(showRoomId, roomUid, -1, 10);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.show_member_title_tv_back:
			finish();
			break;
		case R.id.show_member_title_tv_member:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.show_member_title_tv_devote:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.tv_show_devote_day:
			type = 0;
			mTvDevoteDay.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
			mTvDevoteDay.setTextColor(getResources().getColor(R.color.peach));
			mTvDevoteAll.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
			mTvDevoteAll.setTextColor(getResources().getColor(R.color.gray));

			mDevoteAdapter.setType(0);
			mDevoteAdapter.setList(mListDevoteDay);
			if (mDevoteAdapter.getCount() == 0) {
				prListViewDay.setEmptyView(emptyDevoteDay);
			}

			if (mListDevoteDay.size() == 0) {
				roomsGetBiz.getDevoteRank(showRoomId, roomUid, -1, 10, 0);
			}
			break;
		case R.id.tv_show_devote_all:
			type = 1;
			mTvDevoteDay.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
			mTvDevoteDay.setTextColor(getResources().getColor(R.color.gray));
			mTvDevoteAll.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
			mTvDevoteAll.setTextColor(getResources().getColor(R.color.peach));

			mDevoteAdapter.setType(1);
			mDevoteAdapter.setList(mListDevoteAll);
			if (mDevoteAdapter.getCount() == 0) {
				prListViewAll.setEmptyView(emptyDevoteAll);
			}

			if (mListDevoteAll.size() == 0) {
				roomsGetBiz.getDevoteRank(showRoomId, roomUid, -1, 10, 1);
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
			if (type == 0) {
				isFreshDevoteDay = true;
			} else {
				isFreshDevoteAll = true;
			}
			roomsGetBiz.getDevoteRank(showRoomId, roomUid, -1, 10, type);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (type == 0) {
				isFreshDevoteDay = false;
				roomsGetBiz.getDevoteRank(showRoomId, roomUid, -mListDevoteDay.size() - 1, 10, type);
			} else {
				isFreshDevoteAll = false;
				roomsGetBiz.getDevoteRank(showRoomId, roomUid, -mListDevoteAll.size() - 1, 10, type);
			}
		}

	}
}
