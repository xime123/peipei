package com.tshang.peipei.activity.chat.adapter;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.mine.MineSettingUserInfoActivity;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.display.RoundedBitmapDisplayer;

/**
 * @Title: BaseChatItemAdapterView.java 
 *
 * @Description: 聊天适配基础类
 *
 * @author Jeff  
 *
 * @date 2014年9月2日 上午9:43:46 
 *
 * @version V1.0   
 */
@SuppressLint("ResourceAsColor")
public abstract class ViewBaseChatItemAdapter {
	protected int selfuid = 0;
	protected int friendSex;
	protected int friendUid = 0;
	protected String friendNick;
	protected Activity activity;
	protected DisplayImageOptions options_uid_head;//通过UID加载
	protected DisplayImageOptions options_key_head;//通过key加载
	protected DisplayImageOptions options_video_thumb;//加载视频的缩略图
	protected DisplayImageOptions options_image;//加载图片
	protected DisplayImageOptions options_haremFaceimage;//加载图片
	protected ImageLoader imageLoader;
	protected FailedReSendListener resendListener;
	protected NickOnClickListener nickOnClickListener;
	protected boolean isGroupChat = false;

	protected int groupItemRightBg[] = { R.drawable.chat_grou_1, R.drawable.chat_grou_2, R.drawable.chat_grou_3, R.drawable.chat_grou_4,
			R.drawable.chat_grou_5, R.drawable.chat_grou_6, R.drawable.chat_grou_7, R.drawable.chat_grou_8, R.drawable.chat_grou_9,
			R.drawable.chat_grou_10, R.drawable.chat_grou_11 };

	protected int groupItemLeftBg[] = { R.drawable.chat_grou_left_1, R.drawable.chat_grou_left_2, R.drawable.chat_grou_left_3,
			R.drawable.chat_grou_left_4, R.drawable.chat_grou_left_5, R.drawable.chat_grou_left_6, R.drawable.chat_grou_left_7,
			R.drawable.chat_grou_left_8, R.drawable.chat_grou_left_9, R.drawable.chat_grou_left_10, R.drawable.chat_grou_left_11 };

	public ViewBaseChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, NickOnClickListener nickOnClickListener) {
		this.activity = activity;
		if (BAApplication.mLocalUserInfo != null) {
			this.selfuid = BAApplication.mLocalUserInfo.uid.intValue();
		}
		this.resendListener = resendListener;
		this.friendSex = friendSex;
		this.friendNick = friendNick;
		this.friendUid = friendUid;
		this.isGroupChat = isGroupChate;
		this.nickOnClickListener = nickOnClickListener;
		imageLoader = ImageLoader.getInstance();
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(activity);
		options_key_head = ImageOptionsUtils.GetHeadKeySmallRounded(activity);
		options_video_thumb = ImageOptionsUtils.getVideoThumbOptions(activity);
		options_image = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaultpic_small)
				.showImageForEmptyUri(R.drawable.main_img_defaultpic_small).showImageOnFail(R.drawable.main_img_defaultpic_small).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 8))).bitmapConfig(Bitmap.Config.RGB_565).build();
		options_haremFaceimage = new DisplayImageOptions.Builder().considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 8))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	protected void setTimeShow(TextView tv, long time) {//设置发送时间
		tv.setText(BaseTimes.getTime(time));
	}

	/**
	 * 设置群聊右边对话框背景
	 * @author Aaron
	 *
	 * @param view
	 * @param postion
	 */
	protected void setGroupTextRightItemBackground(TextView view, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemRightBg.length) {
			postion = new Random().nextInt(groupItemRightBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		view.setBackgroundResource(groupItemRightBg[postion]);
		view.setPadding(BaseUtils.dip2px(activity, 10), BaseUtils.dip2px(activity, 10), BaseUtils.dip2px(activity, 15),
				BaseUtils.dip2px(activity, 10));
	}

	/**
	 * 设置群聊左边对话框背景
	 * @author Aaron
	 *
	 * @param view
	 * @param postion
	 */
	protected void setGroupTextLeftItemBackground(TextView view, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		view.setBackgroundResource(groupItemLeftBg[postion]);
		view.setPadding(BaseUtils.dip2px(activity, 15), BaseUtils.dip2px(activity, 10), BaseUtils.dip2px(activity, 10),
				BaseUtils.dip2px(activity, 10));
		view.setTextColor(activity.getResources().getColor(R.color.white));
	}

	/**
	 * 右语音
	 * @author Aaron
	 *
	 * @param layout
	 * @param postion
	 */
	protected void setGroupVoiceRightItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemRightBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 8), BaseUtils.dip2px(activity, 1));
	}

	/**
	 * 左语音
	 * @author Aaron
	 *
	 * @param layout
	 * @param postion
	 */
	protected void setGroupVoiceLeftItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemLeftBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 8), BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 1));
	}

	/**
	 * 右图片 
	 * @author Aaron
	 *
	 * @param layout
	 * @param position
	 */
	protected void setGroupImageRightItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemRightBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 8), BaseUtils.dip2px(activity, 1));
	}

	/**
	 * 右图片 
	 * @author Aaron
	 *
	 * @param layout
	 * @param postion
	 */
	protected void setGroupImageLeftItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemLeftBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 8), BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 1));
	}

	/**
	 * 陪陪表情右图片 
	 * @author Aaron
	 *
	 * @param layout
	 * @param position
	 */
	protected void setGroupHaremFaceRightItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemRightBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 8), BaseUtils.dip2px(activity, 1));
	}

	/**
	 * 陪陪表情右图片 
	 * @author Aaron
	 *
	 * @param layout
	 * @param postion
	 */
	protected void setGroupHaremFaceLeftItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemLeftBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 8), BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 1), BaseUtils.dip2px(activity, 1));
	}

	/**
	 * 右红包
	 * @author Aaron
	 *
	 * @param layout
	 * @param position
	 */
	protected void setGroupRedpackRightItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemRightBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 17), BaseUtils.dip2px(activity, 14), BaseUtils.dip2px(activity, 17),
				BaseUtils.dip2px(activity, 14));
	}

	/**
	 * 左红包
	 * @author Aaron
	 *
	 * @param layout
	 * @param postion
	 */
	protected void setGroupRedpackLeftItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemLeftBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 17), BaseUtils.dip2px(activity, 14), BaseUtils.dip2px(activity, 17),
				BaseUtils.dip2px(activity, 14));
	}

	/**
	 * 右猜拳
	 * @author Aaron
	 *
	 * @param layout
	 * @param position
	 */
	protected void setGroupFingerRightItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemRightBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 10), BaseUtils.dip2px(activity, 10), BaseUtils.dip2px(activity, 10),
				BaseUtils.dip2px(activity, 10));
	}

	/**
	 * 左猜拳
	 * @author Aaron
	 *
	 * @param layout
	 * @param postion
	 */
	protected void setGroupFingerLeftItemBackground(LinearLayout layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemLeftBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 18), BaseUtils.dip2px(activity, 10), BaseUtils.dip2px(activity, 10),
				BaseUtils.dip2px(activity, 10));
	}

	/**
	 * 右大冒险
	 * @author Aaron
	 *
	 * @param layout
	 * @param position
	 */
	protected void setGroupDareRightItemBackground(TextView layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemRightBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 10), BaseUtils.dip2px(activity, 10), BaseUtils.dip2px(activity, 15),
				BaseUtils.dip2px(activity, 10));
	}

	/**
	 * 左大冒险
	 * @author Aaron
	 *
	 * @param layout
	 * @param postion
	 */
	protected void setGroupDareLeftItemBackground(TextView layout, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		layout.setBackgroundResource(groupItemLeftBg[postion]);
		layout.setPadding(BaseUtils.dip2px(activity, 15), BaseUtils.dip2px(activity, 10), BaseUtils.dip2px(activity, 8),
				BaseUtils.dip2px(activity, 10));
	}

	/**
	 * 左边item background
	 * @author Aaron
	 *
	 * @param view
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param position
	 */
	protected void setGroupLeftItemBackGround(View view, int left, int top, int right, int bottom, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		view.setBackgroundResource(groupItemLeftBg[postion]);
		view.setPadding(BaseUtils.dip2px(activity, left), BaseUtils.dip2px(activity, top), BaseUtils.dip2px(activity, right),
				BaseUtils.dip2px(activity, bottom));
	}

	/**
	 * 右边item background
	 * @author Aaron
	 *
	 * @param view
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param position
	 */
	protected void setGroupRightItemBackGround(View view, int left, int top, int right, int bottom, int postion, int toUid, int fromId) {
		if (postion < 0 || postion >= groupItemLeftBg.length) {
			postion = new Random().nextInt(groupItemLeftBg.length);
			String key = "Group_" + toUid + "#" + fromId;
			SharedPreferencesTools.getInstance(activity).saveIntKeyValue(postion, key);
		}
		view.setBackgroundResource(groupItemRightBg[postion]);
		view.setPadding(BaseUtils.dip2px(activity, left), BaseUtils.dip2px(activity, top), BaseUtils.dip2px(activity, right),
				BaseUtils.dip2px(activity, bottom));
	}

	/**
	 * nick点击监听 
	 * @author Aaron
	 *
	 * @param nick
	 */
	protected void setNickListener(TextView textView, final String nick) {
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				nickOnClickListener.onClickNick(nick);
			}
		});

	}

	protected void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_uid_head);
	}

	protected void setResendData(ImageButton ibtn, final ChatDatabaseEntity chatEntity, final int position) {
		ibtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				chatEntity.setStatus(ChatStatus.SENDING.getValue());
				resendListener.onClickReSend(chatEntity, position);
			}
		});
	}

	public abstract View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity);

	public void setGroupViewPading(View view) {
		view.setPadding(0, 0, 120, 0);
	}

	public void setGroupViewPading1(View view) {
		view.setPadding(20, 20, 100, 20);
	}

	protected class HeadClickListener implements OnClickListener {//头像点击事件
		public boolean isLeft = false;
		private Activity activiyt;
		private ChatDatabaseEntity entity;

		public void setEntity(ChatDatabaseEntity entity) {
			this.entity = entity;
		}

		public HeadClickListener(boolean isLeft, Activity activiyt) {
			this.isLeft = isLeft;
			this.activiyt = activiyt;
		}

		@Override
		public void onClick(View v) {
			if (isLeft) {
				if (entity == null) {
					return;
				}
				int uid = entity.getFromID();
				if (uid == BAConstants.PEIPEI_XIAOPEI || uid == BAConstants.PEIPEI_CHAT_TONGZHI) {
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, uid);
				String strSex = entity.getRevStr1();
				if (TextUtils.isEmpty(strSex)) {
					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, friendSex);
					BaseUtils.openActivityByNew(activity, SpaceActivity.class, bundle);

				} else {
					try {
						int sex = Integer.parseInt(strSex);
						bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
						BaseUtils.openActivityByNew(activity, SpaceActivity.class, bundle);

					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (BAApplication.mLocalUserInfo != null) {
					Bundle bundle = new Bundle();
					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, BAApplication.mLocalUserInfo.uid.intValue());
					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, BAApplication.mLocalUserInfo.sex.intValue());
					BaseUtils.openActivity(activiyt, MineSettingUserInfoActivity.class, bundle);
				}
			}
		}

	}

	public interface FailedReSendListener {
		public void onClickReSend(ChatDatabaseEntity chatEntity, int data);
	}

	public interface NickOnClickListener {
		public void onClickNick(String nick);
	}

}
