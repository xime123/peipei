package com.tshang.peipei.activity.main.rank;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.model.rank.RankBase;
import com.tshang.peipei.model.rank.RankTopClickListener;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshBase.Mode;
import com.tshang.peipei.vender.pulltoprefresh.library.PullToRefreshListView;

/**
 * @Title: RankBaseFt.java 
 *
 * @Description: 排行基类
 *
 * @author Jeff 
 *
 * @date 2014年7月29日 下午5:42:21 
 *
 * @version V1.0   
 */
public class RankGlamourWealthBaseFragment extends RankBaseFragment {

	protected View headview;
	protected LinearLayout ll_top_three;

	protected ImageView iv_top_first;
	protected ImageView iv_top_second;
	protected ImageView iv_top_third;
	protected ImageView iv_top_gradeinfo;
	protected ImageView iv_second_gradeinfo;
	protected ImageView iv_third_gradeinfo;
	protected ImageView iv_top_level_first_head;
	protected ImageView iv_top_level_second_head;
	protected ImageView iv_top_level_third_head;
	private TextView tv_top_nick;
	private TextView tv_second_nick;
	private TextView tv_third_nick;
	private TextView tv_top_gold;
	private TextView tv_second_gold;
	private TextView tv_third_gold;
	private TextView tv_top_glamour;
	private TextView tv_second_glamour;
	private TextView tv_third_glamour;
	private LinearLayout ll_top_glamour;
	private LinearLayout ll_second_glamour;
	private LinearLayout ll_third_glamour;
	private LinearLayout ll_top_gold;
	private LinearLayout ll_second_gold;
	private LinearLayout ll_third_gold;
	private ImageView iv_idently_first, iv_idently_second, iv_idently_three;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rank_glamour, null);
		initView(view);
		initLoadData();
		return view;
	}

	protected void initView(View view) {//查找控件
		plistview = (PullToRefreshListView) view.findViewById(R.id.rank_glamour_listview);
		plistview.setOnRefreshListener(this);
		plistview.setOnItemClickListener(this);

		headview = getActivity().getLayoutInflater().inflate(R.layout.head_rank, null);
		plistview.getRefreshableView().addHeaderView(headview);
		plistview.setAdapter(mRankAdapter);

		ll_top_three = (LinearLayout) headview.findViewById(R.id.ll_head_rank_top_three);
		int screenWidth = BasePhone.getScreenWidth(getActivity());
		int itemWidth = (screenWidth - BaseUtils.dip2px(getActivity(), 23)) / 3;
		ll_top_three.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2 * itemWidth
				+ BaseUtils.dip2px(getActivity(), 30)));
		iv_top_first = (ImageView) headview.findViewById(R.id.iv_rank_first);
		iv_top_second = (ImageView) headview.findViewById(R.id.iv_rank_second);
		iv_top_third = (ImageView) headview.findViewById(R.id.iv_rank_third);
		iv_top_level_first_head = (ImageView) headview.findViewById(R.id.iv_top_level_head1);
		iv_top_level_second_head = (ImageView) headview.findViewById(R.id.iv_second_level_head);
		iv_top_level_third_head = (ImageView) headview.findViewById(R.id.iv_third_level_head);
		tv_top_nick = (TextView) headview.findViewById(R.id.tv_rank_top_first_name);
		tv_second_nick = (TextView) headview.findViewById(R.id.tv_rank_top_second_name);
		tv_third_nick = (TextView) headview.findViewById(R.id.tv_rank_third_first_name);
		tv_top_gold = (TextView) headview.findViewById(R.id.tv_rank_top_male_gold);
		tv_second_gold = (TextView) headview.findViewById(R.id.tv_rank_second_male_gold);
		tv_third_gold = (TextView) headview.findViewById(R.id.tv_rank_third_male_gold);
		tv_top_glamour = (TextView) headview.findViewById(R.id.tv_rank_top_female_glamour);
		tv_second_glamour = (TextView) headview.findViewById(R.id.tv_rank_second_female_glamour);
		tv_third_glamour = (TextView) headview.findViewById(R.id.tv_rank_third_female_glamour);
		ll_top_glamour = (LinearLayout) headview.findViewById(R.id.ll_rank_top_female_glamour);
		ll_second_glamour = (LinearLayout) headview.findViewById(R.id.ll_rank_second_female_glamour);
		ll_third_glamour = (LinearLayout) headview.findViewById(R.id.ll_rank_third_female_glamour);
		ll_top_gold = (LinearLayout) headview.findViewById(R.id.ll_rank_top_male_gold);
		ll_second_gold = (LinearLayout) headview.findViewById(R.id.ll_rank_second_male_gold);
		ll_third_gold = (LinearLayout) headview.findViewById(R.id.ll_rank_third_male_gold);
		iv_idently_first = (ImageView) headview.findViewById(R.id.item_broadcast_head_identify_first);
		iv_idently_second = (ImageView) headview.findViewById(R.id.item_broadcast_head_identify_sec);
		iv_idently_three = (ImageView) headview.findViewById(R.id.item_broadcast_head_identify_three);

		iv_top_gradeinfo = (ImageView) headview.findViewById(R.id.iv_top_gradeinfo);
		iv_second_gradeinfo = (ImageView) headview.findViewById(R.id.iv_second_gradeinfo);
		iv_third_gradeinfo = (ImageView) headview.findViewById(R.id.iv_third_gradeinfo);
	}

	@Override
	protected void ViewStateRestored() {
		initLoadData();
	}

	@SuppressWarnings("unchecked")
	protected synchronized void setData(boolean showHead, int isEnd, int isRefresh, boolean isMan, GoGirlUserInfoList giftInfoList) {
		plistview.onRefreshComplete();
		if (isMan) {//男人显示积分
			ll_top_gold.setVisibility(View.VISIBLE);
			ll_second_gold.setVisibility(View.VISIBLE);
			ll_third_gold.setVisibility(View.VISIBLE);
			ll_top_glamour.setVisibility(View.GONE);
			ll_second_glamour.setVisibility(View.GONE);
			ll_third_glamour.setVisibility(View.GONE);
		} else {//女人显示魅力
			ll_top_gold.setVisibility(View.GONE);
			ll_second_gold.setVisibility(View.GONE);
			ll_third_gold.setVisibility(View.GONE);
			ll_top_glamour.setVisibility(View.VISIBLE);
			ll_second_glamour.setVisibility(View.VISIBLE);
			ll_third_glamour.setVisibility(View.VISIBLE);
		}
		if (mRankAdapter == null) {
			return;
		}
		if (isRefresh == RankBase.REFRESH_CODE) {//是刷新数据

			mRankAdapter.clearList();
			if (showHead) {//显示头部的top前三
				ArrayList<GoGirlUserInfo> list = new ArrayList<GoGirlUserInfo>();
				if (!ListUtils.isEmpty(giftInfoList)) {
					ll_top_three.setVisibility(View.VISIBLE);
					int giftInfoSize = giftInfoList.size();
					if (giftInfoSize == 1) {//只有一条数据
						setTopShowData(giftInfoList, false, false);
					} else if (giftInfoSize == 2) {//只有两条数据情况
						setTopShowData(giftInfoList, true, false);
					} else {
						for (int i = 3, len = giftInfoList.size(); i < len; i++) {
							list.add((GoGirlUserInfo) giftInfoList.get(i));
						}
						setTopShowData(giftInfoList, true, true);
					}
					mRankAdapter.setList(list);
				} else {//空的数据是让前三不显示
					ll_top_three.setVisibility(View.GONE);
					mRankAdapter.setList(giftInfoList);
				}

			} else {
				ll_top_three.setVisibility(View.GONE);
				mRankAdapter.setList(giftInfoList);
			}

		} else {//不是刷新就在底部添加加载更多的数据
			mRankAdapter.appendToList(mRankAdapter.getDifferentUserInfoData(giftInfoList));
		}
		if (isEnd == 0) {//还有数据
			plistview.setMode(Mode.BOTH);
		} else {//加到底了
			plistview.setMode(Mode.PULL_FROM_START);
		}

	}

	/**
	 * 处理只有一条，两条特殊数据的情况
	 */
	private void setTopShowData(GoGirlUserInfoList giftInfoList, boolean hasTwo, boolean hasThress) {
		GoGirlUserInfo topInfo = (GoGirlUserInfo) giftInfoList.get(0);//第一条数据
		setTopSingleData(topInfo, tv_top_nick, iv_top_first, tv_top_gold, tv_top_glamour, iv_top_gradeinfo, iv_idently_first);
		if (hasTwo) {
			GoGirlUserInfo secondInfo = (GoGirlUserInfo) giftInfoList.get(1);//第二条数据
			setTopSingleData(secondInfo, tv_second_nick, iv_top_second, tv_second_gold, tv_second_glamour, iv_second_gradeinfo, iv_idently_second);
		}
		if (hasThress) {
			GoGirlUserInfo thirdInfo = (GoGirlUserInfo) giftInfoList.get(2);//第三条数据
			setTopSingleData(thirdInfo, tv_third_nick, iv_top_third, tv_third_gold, tv_third_glamour, iv_third_gradeinfo, iv_idently_three);
		}

	}

	/**
	 * 	设置日榜前三名的数据
	 * @param info
	 * @param tvName
	 * @param imageViw
	 * @param tvGold
	 * @param tvGlamour
	 */
	private void setTopSingleData(GoGirlUserInfo info, TextView tvName, ImageView imageViw, TextView tvGold, TextView tvGlamour,
			ImageView ivGradeInfo, ImageView ivIdently) {
		if (info == null) {
			return;
		}
		if ((info.userstatus.intValue() & SwitchStatus.GG_US_AUTH_FLAG) > 0) {
			ivIdently.setVisibility(View.VISIBLE);
		} else {
			ivIdently.setVisibility(View.GONE);
		}

		String alias = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				info.uid.intValue());
		tvName.setText(TextUtils.isEmpty(alias) ? new String(info.nick) : alias);
		imageViw.setOnClickListener(new RankTopClickListener(info, getActivity()));
		String key = new String(info.headpickey) + BAConstants.LOAD_425_APPENDSTR;
		imageLoader.displayImage("http://" + key, imageViw, options);
		tvGold.setText(String.valueOf(info.ranknum.intValue()));
		tvGlamour.setText(String.valueOf(info.ranknum.intValue()));
		DisplayImageOptions options = ImageOptionsUtils.getGradeInfoImageKeyOptions(getActivity());
		GradeInfoImgUtils.loadGradeInfoImg(getActivity(), imageLoader, options, new String(info.gradeinfo), ivGradeInfo);
	}
}
