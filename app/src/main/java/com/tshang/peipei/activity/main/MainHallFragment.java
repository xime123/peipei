package com.tshang.peipei.activity.main;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.main.adapter.TopAdvAdapter;
import com.tshang.peipei.activity.main.adapter.bean.AdvBean;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.base.json.AdvBeanFunctions;
import com.tshang.peipei.model.hall.HallBase;
import com.tshang.peipei.model.hall.HallNearListFemale;
import com.tshang.peipei.model.hall.HallNearListMale;
import com.tshang.peipei.model.hall.HallNewListFemale;
import com.tshang.peipei.model.hall.HallNewListMale;
import com.tshang.peipei.model.hall.HallOnTimeListFemale;
import com.tshang.peipei.model.hall.HallOnTimeListMale;
import com.tshang.peipei.model.hall.MainHallBiz;
import com.tshang.peipei.model.request.RequestGetAdv;
import com.tshang.peipei.model.request.RequestGetAdv.IGetAdvUrl;
import com.tshang.peipei.model.request.RequestGetAdvUrl.IGetAdv;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;
import com.tshang.peipei.view.AutoScrollViewPager;
import com.tshang.peipei.view.PageControlView;

public class MainHallFragment extends BaseFragment implements OnPageChangeListener, IGetAdv, OnCheckedChangeListener, OnItemClickListener,
		OnRefreshListener2<ListView>, IGetAdvUrl {

	private static final int NEAR_TYPE = 0;
	private static final int NEW_TYPE = 1;
	private static final int ONLINE_TYPE = 2;

	private PullToRefreshListView pListView;
	private MainHallAdapter adapter;
	private TextView tvEmpty;
	private TextView tvTitle;
	private HallNearListFemale nearFemale;
	private HallNearListMale nearMale;
	private HallNewListMale newMale;
	private HallNewListFemale newFemale;
	private HallOnTimeListFemale onTimeFemale;
	private HallOnTimeListMale onTimeMale;
	private AutoScrollViewPager autoViewPager;
	private TopAdvAdapter advAdapter;
	private PageControlView pageControlView;
	private View advHeadView;
	private RelativeLayout rl_adv;
	private PopupWindow popWindow;
	private RadioButton rb_male, rb_female, rb_near, rb_online, rb_new;

	private int currentSex = Gender.FEMALE.getValue();
	private int currentType = ONLINE_TYPE;

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.HALL_GET_NEAR_LIST_FEMALE_SUCCESS_VALUE:
		case HandlerValue.HALL_GET_NEAR_LIST_MALE_SUCCESS_VALUE:
		case HandlerValue.HALL_GET_NEW_FEMALE_LIST_SUCCESS_VALUE:
		case HandlerValue.HALL_GET_NEW_MALE_LIST_SUCCESS_VALUE:
		case HandlerValue.HALL_GET_ONTIME_LIST_FEMALE_SUCCESS_VALUE:
		case HandlerValue.HALL_GET_ONTIME_LIST_MALE_SUCCESS_VALUE:
			pListView.onRefreshComplete();
			UserAndSkillInfoList list = (UserAndSkillInfoList) msg.obj;
			setDataShow(list, msg.arg1, msg.arg2);
			break;
		case HandlerValue.HALL_GET_NEAR_LIST_FEMALE_FAILED_VALUE:
		case HandlerValue.HALL_GET_NEAR_LIST_MALE_FAILED_VALUE:
		case HandlerValue.HALL_GET_NEW_FEMALE_LIST_FAILED_VALUE:
		case HandlerValue.HALL_GET_NEW_MALE_LIST_FAILED_VALUE:
		case HandlerValue.HALL_GET_ONTIME_LIST_FEMALE_FAILED_VALUE:
		case HandlerValue.HALL_GET_ONTIME_LIST_MALE_FAILED_VALUE:
			pListView.onRefreshComplete();
			break;

		case HandlerValue.MAIN_ADV_ACTION_URL_VALUE://说明有广告了
			if (rl_adv == null) {
				return;
			}
			String url = SharedPreferencesTools.getInstance(getActivity()).getStringValueByKey(BAConstants.PEIPEI_ADV_URL);
			if (TextUtils.isEmpty(url)) {
				rl_adv.setVisibility(View.GONE);
			} else {
				if (BAApplication.mLocalUserInfo != null) {
					String verifystr = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "")
							.getStringValueByKey(SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
					if (!TextUtils.isEmpty(verifystr)) {
						rl_adv.setVisibility(View.VISIBLE);
						if (advAdapter != null) {
							try {
								JSONArray jsonArray = new JSONArray(url);
								AdvBean[] beans = AdvBeanFunctions.getAdvBean(jsonArray);
								advAdapter.setList(beans);
								if (beans != null && beans.length > 1) {
									pageControlView.count = beans.length;
									autoViewPager.startAutoScroll();
								} else {
									autoViewPager.stopAutoScroll();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			break;

		default:
			break;
		}

	}

	private void setDataShow(UserAndSkillInfoList list, int isEnd, int isRefresh) {
		if (list == null) {
			return;
		}
		if (isRefresh == HallBase.REFRESH_CODE) {
			adapter.clearList();
			adapter.clearSet();
		}
		
		adapter.appendToList(adapter.getDifferentUserInfoData(list));

		if (isEnd == 1) {//说明到底了
			pListView.setMode(Mode.PULL_FROM_START);
		} else {
			pListView.setMode(Mode.BOTH);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_hall, null);
		advHeadView = (View) inflater.inflate(R.layout.head_adv, null);
		rl_adv = (RelativeLayout) advHeadView.findViewById(R.id.rl_adv);
		autoViewPager = (AutoScrollViewPager) advHeadView.findViewById(R.id.autopager_adv);
		advAdapter = new TopAdvAdapter(getActivity());
		autoViewPager.setAdapter(advAdapter);
		autoViewPager.setInterval(3000);//设置为5秒开始滚动
		autoViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
		autoViewPager.setOnPageChangeListener(this);
		pageControlView = (PageControlView) advHeadView.findViewById(R.id.pageControlView_adv);

		adapter = new MainHallAdapter(getActivity());
		initUi(view);
		nearFemale = new HallNearListFemale(getActivity(), mHandler);
		nearMale = new HallNearListMale(getActivity(), mHandler);
		newMale = new HallNewListMale(getActivity(), mHandler);
		newFemale = new HallNewListFemale(getActivity(), mHandler);
		onTimeFemale = new HallOnTimeListFemale(getActivity(), mHandler);
		onTimeMale = new HallOnTimeListMale(getActivity(), mHandler);
		BaseUtils.showDialog(getActivity(), R.string.loading);
		onTimeFemale.getHallListData(true, true);
//		getAdv(false);
		return view;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_select:
			if (popWindow == null || !popWindow.isShowing()) {
				initPopuptWindow();
				popWindow.showAtLocation(pListView, Gravity.TOP, 0, 0);
			}
			break;
		case R.id.btn_sure://点击了确定
			BaseUtils.showDialog(getActivity(), R.string.loading);

			reRefreshData(true, true);

		case R.id.btn_cancel:
			BaseUtils.hidePopupWindow(popWindow);
			break;

		}
	}

	private void reRefreshData(boolean isLoadCacheData, boolean isRefresh) {
		if (currentSex == Gender.FEMALE.getValue()) {
			if (currentType == NEAR_TYPE) {
				nearFemale.getHallListData(isLoadCacheData, isRefresh);
				tvTitle.setText(R.string.near);
			} else if (currentType == NEW_TYPE) {
				tvTitle.setText(R.string.hall_new);
				newFemale.getHallListData(isLoadCacheData, isRefresh);
			} else if (currentType == ONLINE_TYPE) {
				tvTitle.setText(R.string.hall_time);
				onTimeFemale.getHallListData(isLoadCacheData, isRefresh);
			}
		} else {
			if (currentType == NEAR_TYPE) {
				tvTitle.setText(R.string.near);
				nearMale.getHallListData(isLoadCacheData, isRefresh);
			} else if (currentType == NEW_TYPE) {
				tvTitle.setText(R.string.hall_new);
				newMale.getHallListData(isLoadCacheData, isRefresh);
			} else if (currentType == ONLINE_TYPE) {
				tvTitle.setText(R.string.hall_time);
				onTimeMale.getHallListData(isLoadCacheData, isRefresh);
			}
		}
	}

	private void initUi(View view) {
		pListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefreshlistview);
		if (rl_adv != null) {
			pListView.getRefreshableView().addHeaderView(rl_adv);
		}
		pListView.setAdapter(adapter);
		pListView.setOnRefreshListener(this);
		tvEmpty = (TextView) view.findViewById(R.id.tv_empty_data);
		pListView.setEmptyView(tvEmpty);
		pListView.getRefreshableView().setOnItemClickListener(this);
		view.findViewById(R.id.tv_select).setOnClickListener(this);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		if (pageControlView != null) {
			List<AdvBean> lists = advAdapter.getmList();
			if (lists != null && !lists.isEmpty()) {
				pageControlView.generatePageControl(arg0 % lists.size());
			}
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {//被销毁了数据恢复
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			currentSex = savedInstanceState.getInt("mHallCurrSex");
			currentType = savedInstanceState.getInt("mHallCurrType");
			//			getAdv(true);
			//			reRefreshData(false, true);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {//销毁了数据保存
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putInt("mHallCurrSex", currentSex);
			outState.putInt("mHallCurrType", currentType);
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}

	private long currentRequestAdvTime = 0;
	private static final int INTERVALTIME = 1 * 60 * 60 * 1000;//一个小时请求一次广告位

	protected void getAdv(boolean isRefresh) {
		if (System.currentTimeMillis() - currentRequestAdvTime >= INTERVALTIME || isRefresh) {
			MainHallBiz.getInstance().getAdvUrl(getActivity(), this);//获取用户auth值
			new RequestGetAdv().getAdv(getActivity(), this);//拉取广告url数组
		}
	}

	@Override
	public void getAdv(int retCode, String url, String verifystr) {

		if (retCode == 0 && BAApplication.mLocalUserInfo != null) {
			SharedPreferencesTools.getInstance(getActivity(), String.valueOf(BAApplication.mLocalUserInfo.uid.intValue())).saveStringKeyValue(
					verifystr, SharedPreferencesTools.PEI_PEI_ADV_AUTH_URL);
		}

	}

	/**
	 * 弹出筛选框
	 * @author jeff
	 *
	 */
	private void initPopuptWindow() {//广播悬浮框选择
		// 获取自定义布局文件xml的视图
		View popview = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_hall, null, false);
		popWindow = new PopupWindow(popview, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		popWindow.getContentView().setFocusableInTouchMode(true);
		popWindow.getContentView().setFocusable(true);
		// 设置动画效果
		popWindow.setAnimationStyle(R.style.anim_popwindow_alpha);
		popview.findViewById(R.id.btn_cancel).setOnClickListener(this);
		popview.findViewById(R.id.btn_sure).setOnClickListener(this);

		popview.findViewById(R.id.rl_pop_hall_root).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BaseUtils.hidePopupWindow(popWindow);
				return false;
			}
		});
		rb_male = (RadioButton) popview.findViewById(R.id.rb_male);
		rb_male.setOnCheckedChangeListener(this);
		rb_female = (RadioButton) popview.findViewById(R.id.rb_female);
		rb_female.setOnCheckedChangeListener(this);
		rb_near = (RadioButton) popview.findViewById(R.id.rb_near);
		rb_near.setOnCheckedChangeListener(this);
		rb_online = (RadioButton) popview.findViewById(R.id.rb_online);
		rb_online.setOnCheckedChangeListener(this);
		rb_new = (RadioButton) popview.findViewById(R.id.rb_new);
		rb_new.setOnCheckedChangeListener(this);
		if (currentSex == Gender.FEMALE.getValue()) {
			rb_female.setChecked(true);
		} else {
			rb_male.setChecked(true);
		}
		if (currentType == NEAR_TYPE) {
			rb_near.setChecked(true);
		} else if (currentType == NEW_TYPE) {
			rb_new.setChecked(true);
		} else {
			rb_online.setChecked(true);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//每项的点击事件
		UserAndSkillInfo info = (UserAndSkillInfo) parent.getAdapter().getItem(position);
		if (info != null) {
			GoGirlUserInfo useinfo = info.userinfo;
			if(useinfo != null){
				SpaceUtils.toSpaceCustom(getActivity(), useinfo, 0);
			}
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {//筛选条件
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.rb_male:
				currentSex = Gender.MALE.getValue();
				break;
			case R.id.rb_female:
				currentSex = Gender.FEMALE.getValue();
				break;
			case R.id.rb_near:
				currentType = NEAR_TYPE;
				break;
			case R.id.rb_new:
				currentType = NEW_TYPE;
				break;
			case R.id.rb_online:
				currentType = ONLINE_TYPE;
				break;

			}
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//		getAdv(true);
		reRefreshData(false, true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		reRefreshData(false, false);
	}

	@Override
	public void getAdvUrl(String urlData) {
		if (!TextUtils.isEmpty(urlData)) {
			currentRequestAdvTime = System.currentTimeMillis();
			String old_url = SharedPreferencesTools.getInstance(getActivity()).getStringValueByKey(BAConstants.PEIPEI_ADV_URL);

			if (TextUtils.isEmpty(old_url)) {
				SharedPreferencesTools.getInstance(getActivity()).saveBooleanKeyValue(true, BAConstants.PEIPEI_NEW_URL);
			} else {
				if (!TextUtils.isEmpty(old_url) && !old_url.equals(urlData)) {
					SharedPreferencesTools.getInstance(getActivity()).saveBooleanKeyValue(true, BAConstants.PEIPEI_NEW_URL);
				}
			}
			SharedPreferencesTools.getInstance(getActivity()).saveStringKeyValue(urlData, BAConstants.PEIPEI_ADV_URL);
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.MAIN_ADV_ACTION_URL_VALUE);
		}
	}

}
