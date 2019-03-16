package com.tshang.peipei.model.biz.baseviewoperate;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.mine.MineShowAllGiftListActivity;
import com.tshang.peipei.activity.space.SpaceCustomAdapter;
import com.tshang.peipei.activity.space.SpaceCustomDetailActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.asn.gogirl.TopicCounterInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: OperateViewUtils.java 
 *
 * @Description: 用来操作一些基本页面的共同属性的元素操作
 *
 * @author Jeff 
 *
 * @date 2014年7月3日 上午10:59:23 
 *
 * @version V1.1   
 */
public class OperateViewUtils {
	//设置数量的显示
	public static void setTextViewShowCount(TextView tv, int num, boolean showBg, boolean isChecked) {
		if (tv != null) {
			if (num > 0) {
				tv.setVisibility(View.VISIBLE);
				if (num > 99) {
					tv.setText("");
					if (showBg) {
						if (isChecked) {
							tv.setBackgroundResource(R.drawable.message_img_redot4_more);
						} else {
							tv.setBackgroundResource(R.drawable.message_img_redot1_more);
						}
					}
				} else {
					tv.setText(String.valueOf(num));
					if (showBg) {
						if (isChecked) {
							tv.setBackgroundResource(R.drawable.message_img_redot4);
							tv.setTextColor(BAApplication.getInstance().getResources().getColor(R.color.main_bottom_bg_pre));
						} else {
							tv.setBackgroundResource(R.drawable.message_img_redot1);
							tv.setTextColor(BAApplication.getInstance().getResources().getColor(R.color.white));
						}
					}
				}
			} else {
				tv.setVisibility(View.GONE);
			}
		}
	}

	//设置数量的显示
	public static void setTextViewShowCount(TextView tv, int num, boolean showBg) {
		if (tv != null) {
			if (num > 0) {
				tv.setVisibility(View.VISIBLE);
				if (num > 99) {
					tv.setText("");
					if (showBg) {
						tv.setBackgroundResource(R.drawable.message_img_redot1_more);
					}
				} else {
					tv.setText(String.valueOf(num));
					if (showBg) {
						tv.setBackgroundResource(R.drawable.message_img_redot1);
					}
				}
			} else {
				tv.setVisibility(View.GONE);
			}
		}
	}

	//设置数量的显示
	public static void setTextViewShowCount(Context context, ImageView tv) {
		if (tv != null) {
			int numGift = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKeyToZero(
					BAConstants.GIFTS_NUM);
			
			if (numGift > 0) {
				tv.setVisibility(View.VISIBLE);
			} else {
				tv.setVisibility(View.GONE);
			}
		}
	}
	
	//设置数量的显示
	public static void setTextViewShowCount(Context context, ImageView tv, boolean isChecked) {
		if (tv != null) {
			int numGift = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "").getIntValueByKeyToZero(
					BAConstants.GIFTS_NUM);
			if(isChecked){
				tv.setBackgroundResource(R.drawable.message_img_redot5);
			}else{
				tv.setBackgroundResource(R.drawable.message_img_redot0);
			}
			if (numGift > 0) {
				tv.setVisibility(View.VISIBLE);
			} else {
				tv.setVisibility(View.GONE);
			}
		}
	}

	//设置活动数量的显示
	public static void setTextViewActivityCount(Context context, ImageView tv) {
		if (tv != null) {
			if (SharedPreferencesTools.getInstance(context).getBooleanKeyValue(BAConstants.PEIPEI_NEW_URL)) {
				tv.setVisibility(View.VISIBLE);
			} else {
				tv.setVisibility(View.GONE);
			}
		}
	}
	
	//设置活动数量的显示
	public static void setTextViewActivityCount(Context context, ImageView tv, boolean isChecked) {
		if (tv != null) {
			if(isChecked){
				tv.setBackgroundResource(R.drawable.message_img_redot5);
			}else{
				tv.setBackgroundResource(R.drawable.message_img_redot0);
			}
			if (SharedPreferencesTools.getInstance(context).getBooleanKeyValue(BAConstants.PEIPEI_NEW_URL)) {
				tv.setVisibility(View.VISIBLE);
			} else {
				tv.setVisibility(View.GONE);
			}
		}
	}

	//设置textview上方显示图片
	public static void setTextViewTopDrawable(Context context, int source, TextView tv) {
		Drawable drawable = context.getResources().getDrawable(source);
		tv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
	}

	public static void setTextViewLeftDrawable(Context context, int source, TextView tv) {
		if (tv != null) {
			Drawable drawable = context.getResources().getDrawable(source);
			if (drawable != null) {
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
				tv.setCompoundDrawables(drawable, null, null, null);
			}
		}
	}

	public static void setNullTextViewLeftDrawable(TextView tv) {
		if (tv != null) {
			tv.setCompoundDrawables(null, null, null, null);
		}
	}

	/**
	 * 
	 * @author Jeff 跳转到礼物列表
	 *
	 */
	public static void intentMineShowAllGiftListActivity(Activity activity, int OtherUid, int sex) {
		Bundle bundle = new Bundle();
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, OtherUid);
		bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
		BaseUtils.openActivity(activity, MineShowAllGiftListActivity.class, bundle);
	}

	public static void intentSpaceCustomDetailActivity(Activity activity, SpaceCustomAdapter mAdapter, TopicInfo topicInfo, boolean flag) {
		Bundle bundle = new Bundle();
		String key = topicInfo.id + "" + topicInfo.uid;
		bundle.putInt(SpaceCustomDetailActivity.TOPICID, topicInfo.id.intValue());
		bundle.putInt(SpaceCustomDetailActivity.TOPICUID, topicInfo.uid.intValue());
		bundle.putBoolean(SpaceCustomDetailActivity.ISADDCOMMENT, flag);
		TopicCounterInfo countInfo = mAdapter.getCountMap().get(key);
		if (countInfo != null) {
			bundle.putInt(SpaceCustomDetailActivity.APPRECIATENUM, countInfo.appreciatenum.intValue());
			bundle.putInt(SpaceCustomDetailActivity.REPLYNUM, countInfo.commentnum.intValue());
			BaseUtils.openActivity(activity, SpaceCustomDetailActivity.class, bundle);
		}
	}

	/**
	 * 计算年龄
	 * @author Administrator
	 *
	 * @param birthDay
	 * @return
	 */
	public static String calculateAge(String birthDay) {
		if (TextUtils.isEmpty(birthDay) || birthDay.length() < 4) {
			return "0岁";
		}
		String strYear = birthDay.substring(0, 4);
		int year = Integer.parseInt(strYear);
		Calendar c = Calendar.getInstance();
		int now = c.get(Calendar.YEAR);
		int age = now - year;
		if (age < 0) {
			age = 0;
		}
		return age + "岁";//
	}

	public static String getConstellation(String birthDay) {//获取星座
		if (TextUtils.isEmpty(birthDay)) {
			return "摩羯座";
		}
		String[] birthdays = birthDay.split("-");
		if (birthdays == null || birthdays.length != 3) {
			return "摩羯座";
		}
		String month = birthdays[1];
		String day = birthdays[2];
		int intMonth = Integer.parseInt(month);
		int intDay = Integer.parseInt(day);
		String constellation = BaseTimes.getConstellation(intMonth, intDay);
		return constellation;
	}

}
