package com.tshang.peipei.activity.chat.adapter;

import java.io.File;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.chat.DownVideoListener;
import com.tshang.peipei.model.biz.chat.VideoUtils;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: VideoItemView.java 
 *
 * @Description: 视频模块的选项
 *
 * @author Jeff 
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewVideoChatItemAdapter extends ViewBaseChatItemAdapter {

	public ViewVideoChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler mHandler, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.mHandler = mHandler;
	}

	private BAHandler mHandler;

	/**
	 * 设置缩略图
	 * @author Jeff
	 *
	 * @param chatEntity 聊天实体bean
	 * @param imageview
	 */
	private void showVideoThumbImage(ChatDatabaseEntity chatEntity, ImageView imageview) {
		if (SdCardUtils.isExistSdCard() && chatEntity != null) {
			String message = chatEntity.getMessage();
			if (!TextUtils.isEmpty(message)) {
				File file = new File(SdCardUtils.getInstance().getVedioDirectory(), message + ".mp4");
				Uri uri = VideoUtils.queryUriForVideo(activity, file);
				imageview.setOnClickListener(new DownVideoListener(activity, chatEntity, mHandler));
				if (uri != null) {
					imageLoader.displayImage(uri.toString(), imageview, options_video_thumb);
					imageview.setOnClickListener(new DownVideoListener(activity, chatEntity, mHandler));
				}

			}
		}
	}

	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_video_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_video);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.leftVideoThumb = (ImageView) convertView.findViewById(R.id.chat_item_video_left_pic);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_video_receive_time);
			mViewholer.receivePb = (ProgressBar) convertView.findViewById(R.id.chat_item_video_left_receive_pb);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_video);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			mViewholer.rightVideoThumb = (ImageView) convertView.findViewById(R.id.chat_item_video_right_pic);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_send_video_time_right);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);

			leftHeadListener = new HeadClickListener(true, activity);
			mViewholer.chatLeftHead.setOnClickListener(leftHeadListener);
			rightHeadListener = new HeadClickListener(false, activity);
			mViewholer.chatRightHead.setOnClickListener(rightHeadListener);

			convertView.setTag(mViewholer);
			convertView.setTag(mViewholer.chatLeftHead.getId(), leftHeadListener);
			convertView.setTag(mViewholer.chatRightHead.getId(), rightHeadListener);

		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			leftHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatLeftHead.getId());
			rightHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatRightHead.getId());
		}
		int sendStatus = chatEntity.getStatus();
		long time = chatEntity.getCreateTime();
		if (des == BAConstants.ChatDes.TO_ME.getValue()) {
			mViewholer.llLeft.setVisibility(View.VISIBLE);
			mViewholer.llRight.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(chatEntity.getRevStr3()) && Integer.parseInt(chatEntity.getRevStr3()) == 1) {
				if (Integer.parseInt(chatEntity.getRevStr1()) == Gender.MALE.getValue()) {
					mViewholer.chatLeftHead.setImageResource(R.drawable.dynamic_defalut_man);
				} else {
					mViewholer.chatLeftHead.setImageResource(R.drawable.dynamic_defalut_woman);
				}
			} else {
				setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
				leftHeadListener.setEntity(chatEntity);
			}
			setTimeShow(mViewholer.chatLeftTime, time);
			if (ChatStatus.SENDING.getValue() == sendStatus) {
				mViewholer.receivePb.setVisibility(View.VISIBLE);
			} else {
				mViewholer.receivePb.setVisibility(View.GONE);
			}
			showVideoThumbImage(chatEntity, mViewholer.leftVideoThumb);
		} else {
			mViewholer.llLeft.setVisibility(View.GONE);
			mViewholer.llRight.setVisibility(View.VISIBLE);
			setTimeShow(mViewholer.chatRightTime, time);
			setHeadImage(mViewholer.chatRightHead, selfuid);
			setResendData(mViewholer.ibtnResend, chatEntity, position);
			if (ChatStatus.SENDING.getValue() == sendStatus) {
				mViewholer.progressBar.setVisibility(View.VISIBLE);
				mViewholer.ibtnResend.setVisibility(View.GONE);
			} else if (ChatStatus.FAILED.getValue() == sendStatus) {
				mViewholer.progressBar.setVisibility(View.GONE);
				mViewholer.ibtnResend.setVisibility(View.VISIBLE);
			} else {
				mViewholer.progressBar.setVisibility(View.GONE);
				mViewholer.ibtnResend.setVisibility(View.GONE);
			}
			showVideoThumbImage(chatEntity, mViewholer.rightVideoThumb);
		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		ImageView leftVideoThumb;//左边视频缩略图
		TextView chatLeftTime;//聊天时间
		ProgressBar receivePb;//接受视频的进度调

		//右边布局元素
		LinearLayout llRight;
		ImageView chatRightHead;//头像
		ImageView rightVideoThumb;//右边视频缩略图
		TextView chatRightTime;//聊天时间
		ProgressBar progressBar;//发送消息进度条
		ImageButton ibtnResend;

	}
}
