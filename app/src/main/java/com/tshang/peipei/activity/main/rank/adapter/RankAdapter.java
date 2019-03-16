package com.tshang.peipei.activity.main.rank.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: RankFemaleAdapter.java 
 *
 * @Description: 排行适配器 
 *
 * @author Jeff 
 *
 * @date 2014年7月29日 下午9:02:40 
 *
 * @version V1.0   
 */
public class RankAdapter extends ArrayListAdapter<GoGirlUserInfo> {
	public static final int FEMALE_DAY_RANK = 0;
	public static final int FEMALE_WEEK_RANK = 1;
	public static final int FEMALE_TOTAL_RANK = 2;
	public static final int MALE_DAY_RANK = 3;
	public static final int MALE_WEEK_RANK = 4;
	public static final int MALE_TOTAL_RANK = 5;
	public static final int GAME_TOTAL_WIN_RANK = 6;
	private int current_rank_type = FEMALE_WEEK_RANK;
	private DisplayImageOptions options; //对加载的图片设置参数
	private DisplayImageOptions gradeInfoOptions;

	private Set<Integer> uidSet = new HashSet<Integer>();

	public void clearSet() {//清除之前的数据
		uidSet.clear();
	}

	public ArrayList<GoGirlUserInfo> getDifferentUserInfoData(List<GoGirlUserInfo> list) {//去除重复的数据
		ArrayList<GoGirlUserInfo> newLists = new ArrayList<GoGirlUserInfo>();
		if (list != null && !list.isEmpty()) {
			for (GoGirlUserInfo goGirlUserInfo : list) {
				if (!uidSet.contains(goGirlUserInfo.uid.intValue())) {
					newLists.add(goGirlUserInfo);
					uidSet.add(goGirlUserInfo.uid.intValue());
				}
			}
		}
		return newLists;
	}

	public RankAdapter(Activity context) {
		super(context);
		options = ImageOptionsUtils.GetHeadKeySmallRounded(context);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
	}

	public void setCurrent_rank_type(int current_rank_type) {
		this.current_rank_type = current_rank_type;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_rank_queen_mainlist, parent, false);
			viewholer.ivGradeInfo = (ImageView) convertView.findViewById(R.id.iv_rank_head_gradeinfo);
			viewholer.ivHead = (ImageView) convertView.findViewById(R.id.item_rank_queen_iv_head);
			viewholer.ivFlag = (ImageView) convertView.findViewById(R.id.item_rank_queen_iv_flag);
			viewholer.tvRank = (TextView) convertView.findViewById(R.id.item_rank_queen_tv_rank);
			viewholer.tvNick = (TextView) convertView.findViewById(R.id.item_rank_queen_tv_nick);
			viewholer.tvTitle = (TextView) convertView.findViewById(R.id.item_rank_queen_title);
			viewholer.tvGold = (TextView) convertView.findViewById(R.id.tv_rank_gold);
			viewholer.tvGlamour = (TextView) convertView.findViewById(R.id.tv_rank_glamour);
			viewholer.ll_glamour = (LinearLayout) convertView.findViewById(R.id.ll_rank_glamour);
			viewholer.ll_gold = (LinearLayout) convertView.findViewById(R.id.ll_rank_gold);
			viewholer.tvGoldShow = (TextView) convertView.findViewById(R.id.tv_gold);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		switch (current_rank_type) {//区别不同的布局
		case FEMALE_DAY_RANK:
			viewholer.ll_gold.setVisibility(View.GONE);
			viewholer.ll_glamour.setVisibility(View.VISIBLE);
			viewholer.tvTitle.setVisibility(View.VISIBLE);
			viewholer.tvTitle.setText("今日新增");
			viewholer.tvRank.setText((position + 4) + "");
			viewholer.ivFlag.setVisibility(View.GONE);
			break;
		case FEMALE_WEEK_RANK:
			viewholer.ll_gold.setVisibility(View.GONE);
			viewholer.ll_glamour.setVisibility(View.VISIBLE);
			viewholer.tvTitle.setVisibility(View.VISIBLE);
			viewholer.tvTitle.setText("本周新增");
			viewholer.tvRank.setText((position + 1) + "");
			if (position == 0) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_girl1);
			}
			if (position == 1) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_girl2);
			}
			if (position == 2) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_girl3);
			}
			if (position > 2) {
				viewholer.ivFlag.setVisibility(View.GONE);
			} else {
				viewholer.ivFlag.setVisibility(View.VISIBLE);
			}
			break;
		case FEMALE_TOTAL_RANK:
			viewholer.ll_gold.setVisibility(View.GONE);
			viewholer.ll_glamour.setVisibility(View.VISIBLE);
			viewholer.tvRank.setText((position + 1) + "");
			viewholer.tvTitle.setText("总计新增");
			if (position == 0) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_girl1);
			}
			if (position == 1) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_girl2);
			}
			if (position == 2) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_girl3);
			}
			if (position > 2) {
				viewholer.ivFlag.setVisibility(View.GONE);
			} else {
				viewholer.ivFlag.setVisibility(View.VISIBLE);
			}
			break;
		case MALE_DAY_RANK:
			viewholer.ll_gold.setVisibility(View.VISIBLE);
			viewholer.ll_glamour.setVisibility(View.GONE);
			viewholer.tvTitle.setVisibility(View.VISIBLE);
			viewholer.tvTitle.setText("今日消费");
			viewholer.tvRank.setText((position + 4) + "");
			viewholer.ivFlag.setVisibility(View.GONE);
			break;
		case MALE_WEEK_RANK:
			viewholer.ll_gold.setVisibility(View.VISIBLE);
			viewholer.tvGoldShow.setText("财富值");
			viewholer.ll_glamour.setVisibility(View.GONE);
			viewholer.tvTitle.setVisibility(View.VISIBLE);
			viewholer.tvTitle.setText("本周消费");
			viewholer.tvRank.setText((position + 1) + "");
			if (position == 0) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_boy1);
			}
			if (position == 1) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_boy2);
			}
			if (position == 2) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_boy3);
			}
			if (position > 2) {
				viewholer.ivFlag.setVisibility(View.GONE);
			} else {
				viewholer.ivFlag.setVisibility(View.VISIBLE);
			}
			break;
		case MALE_TOTAL_RANK:
			viewholer.ll_gold.setVisibility(View.VISIBLE);
			viewholer.tvGoldShow.setText("财富值");
			viewholer.ll_glamour.setVisibility(View.GONE);
			viewholer.tvTitle.setText("总计消费");
			viewholer.tvRank.setText((position + 1) + "");
			if (position == 0) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_boy1);
			}
			if (position == 1) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_boy2);
			}
			if (position == 2) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_boy3);
			}
			if (position > 2) {
				viewholer.ivFlag.setVisibility(View.GONE);
			} else {
				viewholer.ivFlag.setVisibility(View.VISIBLE);
			}
			break;
		case GAME_TOTAL_WIN_RANK:
			viewholer.ll_gold.setVisibility(View.VISIBLE);
			viewholer.ll_glamour.setVisibility(View.GONE);
			viewholer.tvTitle.setText("本周赢得");
			viewholer.tvGoldShow.setText("金币");
			viewholer.tvRank.setText((position + 1) + "");
			if (position == 0) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_boy1);
			}
			if (position == 1) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_boy2);
			}
			if (position == 2) {
				viewholer.ivFlag.setImageResource(R.drawable.rank_img_boy3);
			}
			if (position > 2) {
				viewholer.ivFlag.setVisibility(View.GONE);
			} else {
				viewholer.ivFlag.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}

		GoGirlUserInfo info = mList.get(position);
		if (info != null && info.authpickey != null && !TextUtils.isEmpty(new String(info.authpickey))
				&& (info.userstatus.intValue() & SwitchStatus.GG_US_AUTH_FLAG) > 0) {
			convertView.findViewById(R.id.item_broadcast_head_identify).setVisibility(View.VISIBLE);
		} else {
			convertView.findViewById(R.id.item_broadcast_head_identify).setVisibility(View.GONE);
		}

		GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, new String(info.gradeinfo), viewholer.ivGradeInfo, gradeInfoOptions);
		String key = new String(info.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR;
		imageLoader.displayImage("http://" + key, viewholer.ivHead, options);

		String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				info.uid.intValue());
		viewholer.tvNick.setText(TextUtils.isEmpty(alias) ? new String(info.nick) : alias);
		viewholer.tvGlamour.setText(String.valueOf(info.ranknum));
		viewholer.tvGold.setText(String.valueOf(info.ranknum));
		return convertView;
	}

	final class ViewHoler {
		ImageView ivGradeInfo;
		ImageView ivHead;
		ImageView ivFlag;
		TextView tvRank;
		TextView tvNick;
		TextView tvGlamour;
		TextView tvTitle;
		TextView tvGold;
		LinearLayout ll_glamour;
		LinearLayout ll_gold;
		TextView tvGoldShow;

	}
}
