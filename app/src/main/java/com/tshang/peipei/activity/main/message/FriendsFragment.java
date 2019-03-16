package com.tshang.peipei.activity.main.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.main.BaseFragment;
import com.tshang.peipei.activity.space.SpaceFollowAdapter;
import com.tshang.peipei.activity.space.SpaceFollowAdapter.deleteFollow;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.biz.space.SpaceRelationshipBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackDeletFollow;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFansList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFollowList;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.view.CharacterParser;
import com.tshang.peipei.view.CornerListView;
import com.tshang.peipei.view.PinyinComparator;
import com.tshang.peipei.view.SideBar;
import com.tshang.peipei.view.SideBar.OnTouchingLetterChangedListener;
import com.tshang.peipei.view.SortModel;

import de.greenrobot.event.EventBus;

public class FriendsFragment extends BaseFragment implements OnClickListener, BizCallBackGetFansList, BizCallBackGetFollowList,
		BizCallBackDeletFollow, deleteFollow, OnItemClickListener {
	private LinearLayout ll_fans;
	private TextView tv_follows;
	private TextView tv_fans;
	private CornerListView listView;
	private SpaceFollowAdapter mFollowAdapter;
	private int startLoadPos = -1;
	private static final int LOADCOUNT = 200;
	private boolean isRefresh = true;
	private boolean isFans = true;
	private TextView tv_noDatas;
	private TextView tv_fanscount;

	private TextView dialog;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	private SideBar sideBar;

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		BaseUtils.cancelDialog();
		switch (msg.what) {
		case HandlerValue.GET_MY_FANS_LIST_VALUE:
		case HandlerValue.GET_MY_FOLLOWS_LIST_VALUE:
			RetFollowInfoList infoList = (RetFollowInfoList) msg.obj;
			List<SortModel> mList = new ArrayList<SortModel>();
			List<SortModel> mListHead = new ArrayList<SortModel>();
			if (isRefresh) {
				mFollowAdapter.clearList();
			}
			if (null != infoList && infoList.size() > 0) {
				String newFans = "";
				if (msg.what == HandlerValue.GET_MY_FOLLOWS_LIST_VALUE) {
					newFans = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "")
							.getStringValueByKey(BAConstants.PEIPEI_FANS_UNREAD_UID);
				}

				String alias;
				SortModel model;
				String[] fansuid = new String[] {};
				if (!TextUtils.isEmpty(newFans)) {
					fansuid = newFans.split(",");
				}
				for (Object object : infoList) {
					RetFollowInfo info = (RetFollowInfo) object;
					alias = new String(info.followinfo.alias);
					model = new SortModel();
					model.setInfo(info);
					if (!TextUtils.isEmpty(alias)) {
						SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "_remark")
								.saveStringKeyValue(new String(info.followinfo.alias), info.followuserinfo.uid.intValue() + "");
					} else {
						SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "_remark").remove(
								info.followuserinfo.uid.intValue() + "");
						alias = new String(info.followuserinfo.nick);
					}
					model.setName(alias);

					if (!TextUtils.isEmpty(alias)) {
						// 汉字转换成拼音
						String pinyin = characterParser.getSelling(alias);
						String sortString = pinyin.substring(0, 1).toUpperCase();

						// 正则表达式，判断首字母是否是英文字母
						if (sortString.matches("[A-Z]")) {
							model.setSortLetters(sortString.toUpperCase());
						} else {
							model.setSortLetters("#");
						}
					} else {
						model.setSortLetters("#");
					}
					mList.add(0, model);

					if (fansuid.length > 0) {
						for (int i = 0; i < fansuid.length; i++) {
							if (fansuid[i].equals(info.followinfo.followuid.intValue() + "")) {
								SortModel modelHead = new SortModel();
								modelHead.setInfo(info);
								modelHead.setName(alias);
								modelHead.setSortLetters("-");
								mListHead.add(modelHead);
							}
						}
					}

				}
				if (mListHead.size() > 0)
					mFollowAdapter.appendToList(mListHead);
				Collections.sort(mList, pinyinComparator);
				mFollowAdapter.appendToList(mList);
			}
			if (mFollowAdapter.getCount() == 0) {
				listView.setEmptyView(tv_noDatas);
			}
			break;
		case HandlerValue.DELETE_MY_FOLLOWS_VALUE:
			if (msg.arg1 == 0) {
				mFollowAdapter.removeItem(msg.arg2);
			}
			if (mFollowAdapter.getCount() == 0) {
				listView.setEmptyView(tv_noDatas);
			}
			break;
		case HandlerValue.ALIAS_UPDATE_RESULT:
			if (msg.arg1 == 0) {
				SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "_remark").saveStringKeyValue(
						(String) msg.obj, msg.arg2 + "");
				mFollowAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fansfollows, null);
		initUi(view);

		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (this.isVisible()) {
			if (isVisibleToUser) {
				loadData(true);
				int newFans = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
						BAConstants.PEIPEI_FANS_UNREAD_NUM);
				OperateViewUtils.setTextViewShowCount(tv_fanscount, newFans, false);
			} else {
				SharedPreferencesTools.getInstance(BAApplication.getInstance().getApplicationContext(),
						BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(0, BAConstants.PEIPEI_FANS_UNREAD_NUM);
				SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").saveStringKeyValue("",
						BAConstants.PEIPEI_FANS_UNREAD_UID);
				NoticeEvent noticeEvent = new NoticeEvent();
				int num = ChatSessionManageBiz.isExistUnreadMessage(getActivity());
				noticeEvent.setNum(num);
				noticeEvent.setFlag(NoticeEvent.NOTICE68);
				EventBus.getDefault().postSticky(noticeEvent);
			}
		}

		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_follower:
			isFans = false;
			mFollowAdapter.setFollow(true);
			tv_follows.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
			ll_fans.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
			tv_follows.setTextColor(getResources().getColor(R.color.peach));
			tv_fans.setTextColor(getResources().getColor(R.color.gray));
			loadData(true);
			break;
		case R.id.ll_fans:
			isFans = true;
			mFollowAdapter.setFollow(false);
			ll_fans.setBackgroundResource(R.drawable.main_bar_tab1_bg_pr);
			tv_follows.setBackgroundResource(R.drawable.main_bar_tab1_bg_on);
			tv_fans.setTextColor(getResources().getColor(R.color.peach));
			tv_follows.setTextColor(getResources().getColor(R.color.gray));
			loadData(true);
			break;

		}
	}

	private void initUi(View view) {
		mFollowAdapter = new SpaceFollowAdapter(getActivity(), mHandler);
		mFollowAdapter.setDeleteListener(this);
		listView = (CornerListView) view.findViewById(R.id.plv_friends);
		listView.setAdapter(mFollowAdapter);
		listView.setOnItemClickListener(this);
		ll_fans = (LinearLayout) view.findViewById(R.id.ll_fans);
		ll_fans.setOnClickListener(this);
		tv_follows = (TextView) view.findViewById(R.id.tv_follower);
		tv_follows.setOnClickListener(this);
		tv_fans = (TextView) view.findViewById(R.id.tv_fans);
		tv_noDatas = (TextView) view.findViewById(R.id.tv_no_fans);
		tv_fanscount = (TextView) view.findViewById(R.id.tv_friends_count);
		int newFans = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKey(
				BAConstants.PEIPEI_FANS_UNREAD_NUM);
		BaseUtils.showDialog(getActivity(), R.string.loading);
		getFansList(startLoadPos);
		SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").saveIntKeyValue(0,
				BAConstants.PEIPEI_FANS_UNREAD_NUM);
		SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "").saveStringKeyValue("",
				BAConstants.PEIPEI_FANS_UNREAD_UID);
		OperateViewUtils.setTextViewShowCount(tv_fanscount, newFans, false);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) view.findViewById(R.id.sidebar);
		dialog = (TextView) view.findViewById(R.id.tv_side_dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = mFollowAdapter.getPositionForSection(s.charAt(0)) + 1;
				if (position != 0) {
					listView.setSelection(position);
				}

			}
		});
	}

	private void getFansList(int start) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(getActivity());
		if (userEntity != null) {
			SpaceRelationshipBiz space = new SpaceRelationshipBiz(getActivity());
			space.getFansList(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), start, LOADCOUNT, this);
		}
	}

	private void getFollowList(int start) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(getActivity());
		if (userEntity != null) {
			SpaceRelationshipBiz space = new SpaceRelationshipBiz(getActivity());
			space.getfollowList(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), start, LOADCOUNT, this);
		}
	}

	@Override
	public void deleteFollowById(int uid, int followuid) {
		if (BAApplication.mLocalUserInfo != null) {
			BaseUtils.showDialog(getActivity(), R.string.str_deleting);
			SpaceRelationshipBiz biz = new SpaceRelationshipBiz(getActivity());
			biz.delFollow(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, uid, BAApplication.mLocalUserInfo.uid.intValue(), this);
		}
	}

	@Override
	public void deleteFollowCallBack(int retCode, int followId) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.DELETE_MY_FOLLOWS_VALUE, retCode, followId, followId);
	}

	@Override
	public void getFollowListCallBack(int retCode, RetFollowInfoList list, int isend) {
		if (retCode == 0) {
			startLoadPos -= LOADCOUNT;
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.GET_MY_FANS_LIST_VALUE, isend, isend, list);
		} else if (retCode == -21001) {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.GET_MY_FANS_LIST_VALUE, isend, isend, list);
		} else {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.GET_MY_FOLLOWS_LIST_FAILED_VALUE);
		}
	}

	private void loadData(boolean isloadMore) {
		isRefresh = isloadMore;
		if (isRefresh) {
			startLoadPos = -1;
			BaseUtils.showDialog(getActivity(), R.string.loading);
		}
		if (isFans) {
			getFansList(startLoadPos);
		} else {
			getFollowList(startLoadPos);
		}
	}

	@Override
	public void getFansList(int retCode, RetFollowInfoList list, int isend) {
		if (retCode == 0) {
			startLoadPos -= LOADCOUNT;
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.GET_MY_FOLLOWS_LIST_VALUE, isend, isend, list);
		} else {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.GET_MY_FANS_LIST_FAILED_VALUE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SortModel model = (SortModel) parent.getAdapter().getItem(position);
		if (model != null) {
			RetFollowInfo info = model.getInfo();
			if (info != null) {
				SpaceUtils.toSpaceCustom(getActivity(), info.followuserinfo, 0);
			}
		}
	}

}
