package com.tshang.peipei.activity.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.activity.space.SpaceFollowAdapter;
import com.tshang.peipei.activity.space.SpaceFollowAdapter.deleteFollow;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.space.SpaceRelationshipBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackDeletFollow;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFansList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFollowList;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.view.CharacterParser;
import com.tshang.peipei.view.PinyinComparator;
import com.tshang.peipei.view.SideBar;
import com.tshang.peipei.view.SortModel;
import com.tshang.peipei.view.SideBar.OnTouchingLetterChangedListener;

/**
 * @Title: MainFansActivity.java 
 *
 * @Description: 粉丝,关注列表界面
 *
 * @author allen  
 *
 * @date 2014-6-26 下午6:02:05 
 *
 * @version V1.0   
 */
public class MainFriendsActivity extends BaseActivity implements BizCallBackGetFansList, BizCallBackGetFollowList, BizCallBackDeletFollow,
		deleteFollow {

	private final int GETFOLLOWLIST = 2;
	private final int LOAD_MORE = 3;
	private final int GETFANSLIST = 4;
	private final int DELETEFOLLOW = 5;
	private static final int LOADCOUNT = 10000;

	private TextView mFansTv, mFollowTv;
	private LinearLayout mLinFans;
	private ListView mFansListView, mFollowListView;
	private TextView mNoFans, mNoFollow;

	private boolean isRefresh = true;

	private SpaceFollowAdapter mFansAdapter;
	private SpaceFollowAdapter mFollowAdapter;

	private ViewPager mViewPager;
	private View mViewFans, mViewFollow;

	private TextView mTvNewFans;

	private int mCurrPage = 0;// 当前页卡编号

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	private SideBar sideBar;

	private TextView dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initListener();

		BaseUtils.showDialog(this, R.string.loading);
		getFansList(-1, LOADCOUNT);
		getFollowList(-1, LOADCOUNT);
	}

	@Override
	protected void initData() {
		int newFans = SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
				BAConstants.PEIPEI_FANS_UNREAD_NUM);

		SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(0,
				BAConstants.PEIPEI_FANS_UNREAD_NUM);
		OperateViewUtils.setTextViewShowCount(mTvNewFans, newFans, false);
		LayoutInflater mInflater = getLayoutInflater();
		mViewFans = mInflater.inflate(R.layout.viewpager_fans, null);
		mViewFollow = mInflater.inflate(R.layout.viewpager_follow, null);

		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.mine);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.myfriends);
		mFansTv = (TextView) findViewById(R.id.tv_fans);
		mFollowTv = (TextView) findViewById(R.id.tv_follower);

		mLinFans = (LinearLayout) findViewById(R.id.ll_fans);
		mTvNewFans = (TextView) findViewById(R.id.tv_friends_count);

		ImageView mImageRight = (ImageView) findViewById(R.id.title_iv_right);
		mImageRight.setVisibility(View.VISIBLE);
		mImageRight.setBackgroundResource(R.drawable.homepage_myfried_nav_search);
		mImageRight.setOnClickListener(this);

		mNoFans = (TextView) findViewById(R.id.tv_no_fans);
		mNoFollow = (TextView) findViewById(R.id.tv_no_follow);

		mViewPager = (ViewPager) findViewById(R.id.fans_viewpager);

		mFansListView = (ListView) mViewFans.findViewById(R.id.fans_listview);
		mFansListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SortModel model = (SortModel) parent.getAdapter().getItem(position);
				if (model != null) {
					RetFollowInfo info = model.getInfo();
					if (info != null) {
						SpaceUtils.toSpaceCustom(MainFriendsActivity.this, info.followuserinfo, 0);
					}
				}
			}
		});
		mFollowListView = (ListView) mViewFollow.findViewById(R.id.follow_listview);
		mFollowListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SortModel model = (SortModel) parent.getAdapter().getItem(position);
				if (model != null) {
					RetFollowInfo info = model.getInfo();
					if (info != null) {
						SpaceUtils.toSpaceCustom(MainFriendsActivity.this, info.followuserinfo, 0);
					}
				}
			}
		});

		mFansAdapter = new SpaceFollowAdapter(this, mHandler);
		mFansListView.setAdapter(mFansAdapter);
		mFansAdapter.setFollow(false);

		mFollowAdapter = new SpaceFollowAdapter(this, mHandler);
		mFollowListView.setAdapter(mFollowAdapter);
		mFollowAdapter.setFollow(true);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidebar);
		dialog = (TextView) findViewById(R.id.tv_side_dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = mFollowAdapter.getPositionForSection(s.charAt(0)) + 1;
				if (position != 0) {
					if (mCurrPage == 0) {
						mFansListView.setSelection(position);
					} else {
						mFollowListView.setSelection(position);
					}
				}

			}
		});
	}

	@Override
	protected void initRecourse() {}

	@Override
	protected int initView() {
		return R.layout.activity_fansfollows;
	}

	private void initListener() {
		setViewPageAndUnderline();

		mBackText.setOnClickListener(this);

		mLinFans.setOnClickListener(this);
		mFollowTv.setOnClickListener(this);

		mFollowAdapter.setDeleteListener(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		BaseUtils.cancelDialog();
		switch (msg.what) {
		case GETFANSLIST:
			BaseUtils.cancelDialog();
			if (msg.arg1 == 0) {

				RetFollowInfoList infoList = (RetFollowInfoList) msg.obj;
				if (null != infoList && infoList.size() > 0) {
					if (isRefresh) {
						mFansAdapter.clearList();
					}

					String alias;
					SortModel model;
					List<SortModel> mList = new ArrayList<SortModel>();
					for (Object object : infoList) {
						RetFollowInfo info = (RetFollowInfo) object;
						alias = new String(info.followinfo.alias);
						model = new SortModel();
						model.setInfo(info);
						if (!TextUtils.isEmpty(alias)) {
							SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").saveStringKeyValue(
									new String(info.followinfo.alias), info.followuserinfo.uid.intValue() + "");
						} else {
							SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").remove(
									info.followuserinfo.uid.intValue() + "");
							alias = new String(info.followuserinfo.nick);
						}
						model.setName(alias);

						// 汉字转换成拼音
						String pinyin = characterParser.getSelling(alias);
						String sortString = pinyin.substring(0, 1).toUpperCase();

						// 正则表达式，判断首字母是否是英文字母
						if (sortString.matches("[A-Z]")) {
							model.setSortLetters(sortString.toUpperCase());
						} else {
							model.setSortLetters("#");
						}
						mList.add(0, model);

					}
					Collections.sort(mList, pinyinComparator);

					mFansAdapter.appendToList(mList);
				}

			}

			if (mFansAdapter.getCount() == 0) {
				mFansListView.setEmptyView(mNoFans);
			}
			break;
		case GETFOLLOWLIST:
			if (msg.arg1 == 0) {
				RetFollowInfoList infoList = (RetFollowInfoList) msg.obj;
				if (null != infoList && !infoList.isEmpty()) {
					if (isRefresh) {
						mFollowAdapter.clearList();
					}

					String alias;
					SortModel model;
					List<SortModel> mList = new ArrayList<SortModel>();
					for (Object object : infoList) {
						RetFollowInfo info = (RetFollowInfo) object;
						alias = new String(info.followinfo.alias);
						model = new SortModel();
						model.setInfo(info);
						if (!TextUtils.isEmpty(alias)) {
							SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").saveStringKeyValue(
									new String(info.followinfo.alias), info.followuserinfo.uid.intValue() + "");
						} else {
							SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").remove(
									info.followuserinfo.uid.intValue() + "");
							alias = new String(info.followuserinfo.nick);
						}
						model.setName(alias);

						// 汉字转换成拼音
						String pinyin = characterParser.getSelling(alias);
						String sortString = pinyin.substring(0, 1).toUpperCase();

						// 正则表达式，判断首字母是否是英文字母
						if (sortString.matches("[A-Z]")) {
							model.setSortLetters(sortString.toUpperCase());
						} else {
							model.setSortLetters("#");
						}
						mList.add(0, model);

					}
					Collections.sort(mList, pinyinComparator);

					mFollowAdapter.appendToList(mList);
				}
			}

			if (mFollowAdapter.getCount() == 0) {
				mFollowListView.setEmptyView(mNoFollow);
			}
			break;
		case DELETEFOLLOW:
			int followId = msg.arg2;
			if (msg.arg1 == 0) {
				mFollowAdapter.removeItem(followId);
			}
			if (mFollowAdapter.getCount() == 0) {
				mFollowListView.setEmptyView(mNoFollow);
			}
			break;
		case HandlerValue.ALIAS_UPDATE_RESULT:
			if (msg.arg1 == 0) {
				SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").saveStringKeyValue(
						(String) msg.obj, msg.arg2 + "");
				mFansAdapter.notifyDataSetChanged();
				mFollowAdapter.notifyDataSetChanged();
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
		case R.id.ll_fans:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.tv_follower:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.title_iv_right:
			MineFaqActivity.openMineFaqActivity(this, MineFaqActivity.SEARCH_VALUE);
			break;
		default:
			break;
		}
	}

	/**
	 *刷新数据
	 */
	private void refreshData() {
		isRefresh = true;
		BaseUtils.showDialog(this, R.string.loading);
		if (mCurrPage == 0) {
			getFansList(-1, LOADCOUNT);
		} else if (mCurrPage == 1) {
			getFollowList(-1, LOADCOUNT);
		}
	}

	//上拉刷新 ,下拉加载更多刷新 
	class PullToRefreshListener implements OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			refreshData();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isRefresh = false;
			mHandler.sendEmptyMessage(LOAD_MORE);
		}

	}

	private void getFansList(int start, int num) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if (userEntity != null) {
			SpaceRelationshipBiz space = new SpaceRelationshipBiz(this);
			space.getFansList(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), start, num, this);
		}
	}

	@Override
	public void getFansList(int retCode, RetFollowInfoList list, int isend) {
		sendHandlerMessage(mHandler, GETFANSLIST, retCode, isend, list);
	}

	private void getFollowList(int start, int num) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if (userEntity != null) {
			SpaceRelationshipBiz space = new SpaceRelationshipBiz(this);
			space.getfollowList(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), start, num, this);
		}
	}

	@Override
	public void getFollowListCallBack(int retCode, RetFollowInfoList list, int isend) {
		sendHandlerMessage(mHandler, GETFOLLOWLIST, retCode, isend, list);
	}

	@Override
	public void deleteFollowById(int uid, int followuid) {
		if (BAApplication.mLocalUserInfo != null) {
			BaseUtils.showDialog(this, R.string.str_deleting);
			SpaceRelationshipBiz biz = new SpaceRelationshipBiz(this);
			biz.delFollow(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, uid, BAApplication.mLocalUserInfo.uid.intValue(), this);
		}
	}

	@Override
	public void deleteFollowCallBack(int retCode, int followId) {
		sendHandlerMessage(mHandler, DELETEFOLLOW, retCode, followId);
	}

	private void setViewPageAndUnderline() {
		List<View> mViewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		mViewList.add(mViewFans);
		mViewList.add(mViewFollow);

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
				mLinFans.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
				mFollowTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
				mFansTv.setTextColor(getResources().getColor(R.color.peach));
				mFollowTv.setTextColor(getResources().getColor(R.color.gray));

				if (mFansAdapter.getCount() == 0) {
					getFansList(-1, LOADCOUNT);
				}
			} else {
				mFollowTv.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
				mLinFans.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
				mFollowTv.setTextColor(getResources().getColor(R.color.peach));
				mFansTv.setTextColor(getResources().getColor(R.color.gray));
				if (mFollowAdapter.getCount() == 0) {
					getFollowList(-1, LOADCOUNT);
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

}
