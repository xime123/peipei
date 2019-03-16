package com.tshang.peipei.activity.mine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.main.MainActivity;
import com.tshang.peipei.activity.main.MainHallPagerAdapter;
import com.tshang.peipei.activity.space.SpaceFansListAdapter;
import com.tshang.peipei.activity.space.SpaceFansListAdapter.OnItemFans;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.json.GoGirlUserJson;
import com.tshang.peipei.model.biz.PeiPeiPersistBiz;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.space.SpaceRelationshipBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFansList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFollowList;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfoList;
import com.tshang.peipei.service.PeipeiFloatingService;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.db.DBHelper;
import com.tshang.peipei.view.CharacterParser;
import com.tshang.peipei.view.PinyinComparator;
import com.tshang.peipei.view.SideBar;
import com.tshang.peipei.view.SideBar.OnTouchingLetterChangedListener;
import com.tshang.peipei.view.SortModel;

import de.greenrobot.event.EventBus;

/**
 * @Title: BroadFollersActivity.java 
 *
 * @Description: 广播@对象
 *
 * @author allen  
 *
 * @date 2014-6-26 下午6:02:05 
 *
 * @version V1.0   
 */
public class BroadFollersActivity extends BaseActivity implements OnItemClickListener, BizCallBackGetFansList, BizCallBackGetFollowList, OnItemFans {

	private final int GETFOLLOWLIST = 2;
	private final int LOAD_MORE = 3;
	private final int GETFANSLIST = 4;
	private final int DELETEFOLLOW = 5;
	private static final int LOADCOUNT = 10;

	private TextView mFansTv, mFollowTv;
	private LinearLayout mLinFans;
	private ListView mFansListView, mFollowListView;
	private TextView mNoFans, mNoFollow;

	private boolean isRefresh = true;

	private SpaceFansListAdapter mFansAdapter;
	private BroadFollowAdapter mFollowAdapter;

	private ViewPager mViewPager;
	private View mViewFans, mViewFollow;

	private int mCurrPage = 0;// 当前页卡编号

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	private TextView dialog;
	private SideBar sideBar;

	private boolean isMagic = false;

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
		isMagic = this.getIntent().getBooleanExtra("ismagic", false);
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
		findViewById(R.id.tv_friends_count).setVisibility(View.GONE);

		mNoFans = (TextView) findViewById(R.id.tv_no_fans);
		mNoFollow = (TextView) findViewById(R.id.tv_no_follow);

		mViewPager = (ViewPager) findViewById(R.id.fans_viewpager);

		mFansListView = (ListView) mViewFans.findViewById(R.id.fans_listview);
		mFollowListView = (ListView) mViewFollow.findViewById(R.id.follow_listview);

		mFansAdapter = new SpaceFansListAdapter(this, mHandler, false);
		mFansListView.setAdapter(mFansAdapter);

		mFollowAdapter = new BroadFollowAdapter(this);
		mFollowListView.setAdapter(mFollowAdapter);

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

		mFansAdapter.setOnFansListener(this);
		mFollowListView.setOnItemClickListener(this);
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
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_fans:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.tv_follower:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.title_tv_left:
			goBack();
			break;
		default:
			break;
		}
	}

	private void goBack() {
		if (isMagic) {
			Intent intent = new Intent();
			intent.putExtra(MineWriteBroadCastActivity.STR_GOGIRLUSERINFO, "");
			setResult(MineWriteBroadCastActivity.REQUESTCODE, intent);
		}
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			goBack();
		}
		return false;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SortModel model = (SortModel) parent.getAdapter().getItem(position);
		GoGirlUserInfo userInfo = model.getInfo().followuserinfo;

		Intent intent = new Intent();
		intent.putExtra(MineWriteBroadCastActivity.STR_GOGIRLUSERINFO, GoGirlUserJson.changeObjectDateToJson(userInfo));

		setResult(MineWriteBroadCastActivity.REQUESTCODE, intent);
		finish();
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

	@Override
	public void onItem(GoGirlUserInfo userInfo) {

		Intent intent = new Intent();
		intent.putExtra(MineWriteBroadCastActivity.STR_GOGIRLUSERINFO, GoGirlUserJson.changeObjectDateToJson(userInfo));

		setResult(MineWriteBroadCastActivity.REQUESTCODE, intent);
		finish();

	}
}
