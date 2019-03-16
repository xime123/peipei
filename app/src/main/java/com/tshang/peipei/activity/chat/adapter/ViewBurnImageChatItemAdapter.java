package com.tshang.peipei.activity.chat.adapter;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseString;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackSentChatMessage;
import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.view.RepeatButton;
import com.tshang.peipei.view.RepeatListener;
import com.tshang.peipei.view.fireview.FireItem;
import com.tshang.peipei.view.fireview.ObserveFireView;

/**
 * @Title: ViewImageChatItemAdapter.java 
 *
 * @Description: 阅后即焚图片模块的选项
 *
 * @author Jeff 
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewBurnImageChatItemAdapter extends ViewBaseChatItemAdapter implements BizCallBackSentChatMessage {
	public ViewBurnImageChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler handler, ChatBurnImageInterface callBack, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.handler = handler;
		this.callBack = callBack;
	}

	private BAHandler handler;
	private ChatBurnImageInterface callBack;

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent, final ChatDatabaseEntity chatEntity) {
		final ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_burn_imageview_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_image);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_image_receive_time);
			mViewholer.chatLeftRepeatButton = (RepeatButton) convertView.findViewById(R.id.chat_item_content_pic_left_burn_image);
			mViewholer.chatLeftBurnIvLayout = (RelativeLayout) convertView.findViewById(R.id.chat_item_content_iv_ll_left_burn_image);
			mViewholer.leftBurnProgress = (ProgressBar) convertView.findViewById(R.id.chat_item_left_burn_pb);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_image);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			//			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_send_image_time_right);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);
			mViewholer.chatRightRepeatButton = (RepeatButton) convertView.findViewById(R.id.chat_item_content_pic_right_burn_image);
			mViewholer.chatRightBurnIvLayout = (RelativeLayout) convertView.findViewById(R.id.chat_item_content_iv_ll_right_burn_image);

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
			if (!TextUtils.isEmpty(chatEntity.getRevStr3()) && "1".equals(chatEntity.getRevStr3())) {
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

			FireItem item = ObserveFireView.getInstance().getMap().get(chatEntity.getFromID() + "_" + chatEntity.getMesSvrID());
			if (null != item) {
				mViewholer.leftBurnProgress.setVisibility(View.VISIBLE);
				if (null != item.getCounTimer()) {
					item.getCounTimer().cancel();
				}
				CountDownTimer timer = null;

				if (item.getTime() - System.currentTimeMillis() > 0) {
					timer = new CountDownTimer(item.getTime() - System.currentTimeMillis(), 100) {

						@Override
						public void onTick(long millisUntilFinished) {
							mViewholer.leftBurnProgress.setProgress((int) millisUntilFinished);
						}

						@Override
						public void onFinish() {
							mViewholer.leftBurnProgress.setVisibility(View.GONE);
							mViewholer.leftBurnProgress.setProgress(0);
							mViewholer.chatLeftRepeatButton.setRepeatListener(null, 0);
							if (null != BAApplication.mLocalUserInfo) {
								ChatManageBiz.getInManage(activity).changeMessageStatusBySerID(activity, chatEntity.getFromID(),
										ChatStatus.READED_BURN.getValue(), chatEntity.getMesSvrID(), isGroupChat);
								chatEntity.setStatus(ChatStatus.READED_BURN.getValue());
								handler.sendEmptyMessageAtTime(HandlerType.CHAT_REFRUSH, 0);

								ChatManageBiz.getInManage(activity).sentMsg(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
										BAApplication.mLocalUserInfo.uid.intValue(), chatEntity.getMesSvrID().getBytes(),
										MessageType.RECEIPT.getValue(), -1, chatEntity.getFromID(), chatEntity.getMesSvrID(),
										new String(BAApplication.mLocalUserInfo.nick), friendNick, BAApplication.mLocalUserInfo.sex.intValue(),
										friendSex, ViewBurnImageChatItemAdapter.this, 0, 0);
							}
						}
					}.start();
				}
				item.setCounTimer(timer);

			} else {
				mViewholer.leftBurnProgress.setVisibility(View.GONE);
			}

			String fileName = BaseString.getFilePath(activity, chatEntity.getFromID(), MessageType.IMAGE.getValue()) + File.separator
					+ chatEntity.getMesLocalID();
			Bitmap bitmap = imageLoader.loadImageSync("file://" + fileName);
			if (bitmap != null) {
				bitmap = BaseBitmap.createBitmap(bitmap, 12, 12);
				if (bitmap != null) {
					mViewholer.chatLeftBurnIvLayout.setBackgroundDrawable(new BitmapDrawable(BaseBitmap.toRoundCornerToWH(bitmap, 4)));
				}
			}

			mViewholer.chatLeftRepeatButton.setLayoutParams(new FrameLayout.LayoutParams(BaseUtils.dip2px(activity, 120), BaseUtils.dip2px(activity,
					120)));
			mViewholer.chatLeftRepeatButton.setRepeatListener(new RepeatListener() {

				@Override
				public void onRepeat(final View v, long duration, int repeatcount) {
					long time = System.currentTimeMillis() + 10000;//10秒倒计时

					long temp = ((RepeatButton) v).getTime();
					if (temp == 0 || System.currentTimeMillis() > temp + 2000) {
						((RepeatButton) v).setTime(time);
					}

					FireItem fireItem = null;
					if (!ObserveFireView.getInstance().getMap().containsKey(chatEntity.getMesSvrID() + "_" + chatEntity.getMesSvrID())) {
						fireItem = new FireItem();
						fireItem.setId(chatEntity.getMesSvrID() + "_" + chatEntity.getMesLocalID());
						fireItem.setTime(((RepeatButton) v).getTime());
						CountDownTimer timer = new CountDownTimer(((RepeatButton) v).getTime() - System.currentTimeMillis(), 100) {

							@Override
							public void onTick(long millisUntilFinished) {
								mViewholer.leftBurnProgress.setProgress((int) millisUntilFinished);
							}

							@Override
							public void onFinish() {
								mViewholer.leftBurnProgress.setProgress(0);
								mViewholer.leftBurnProgress.setVisibility(View.GONE);
								((RepeatButton) v).setRepeatListener(null, 0);

								callBack.fireTimeSend(chatEntity.getMesSvrID());
							}
						}.start();
						fireItem.setCounTimer(timer);

						ObserveFireView.getInstance().getMap().put(chatEntity.getMesSvrID() + "_" + chatEntity.getMesSvrID(), fireItem);
					}

					if (repeatcount == -1) {
						callBack.goneViewAndSend(chatEntity.getMesSvrID());
						mViewholer.leftBurnProgress.setVisibility(View.VISIBLE);
					} else {
						if (!((RepeatButton) v).isBurn()) {
							int pro = 0;
							if (ObserveFireView.getInstance().getMap().containsKey(chatEntity.getMesSvrID() + "_" + chatEntity.getMesSvrID())) {
								FireItem item = ObserveFireView.getInstance().getMap().get(chatEntity.getMesSvrID() + "_" + chatEntity.getMesSvrID());
								pro = (int) (item.getTime() - System.currentTimeMillis());
							} else {
								pro = (int) (((RepeatButton) v).getTime() - System.currentTimeMillis());
							}

							callBack.showViewByBurn(v, chatEntity.getMesSvrID(), pro, chatEntity.getMesLocalID());
						}
					}

				}
			}, 50);

			if (chatEntity.getStatus() == ChatStatus.READED_BURN.getValue()) {
				callBack.toDismiss(position + 1, convertView, false);
			}

		} else {
			mViewholer.llLeft.setVisibility(View.GONE);
			mViewholer.llRight.setVisibility(View.VISIBLE);
			//			setTimeShow(mViewholer.chatRightTime, time);
			setResendData(mViewholer.ibtnResend, chatEntity, position);
			setHeadImage(mViewholer.chatRightHead, selfuid);
			//			mViewholer.chatRightHead.setOnClickListener(new RightHeadClickListener());
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

			String fileName = BaseString.getFilePath(activity, friendUid, MessageType.IMAGE.getValue()) + File.separator + chatEntity.getMesLocalID();
			Bitmap bitmap = imageLoader.loadImageSync("file://" + fileName);
			if (bitmap != null) {
				bitmap = BaseBitmap.createBitmap(bitmap, 12, 12);
				if (bitmap != null) {
					mViewholer.chatRightBurnIvLayout.setBackgroundDrawable(new BitmapDrawable(BaseBitmap.toRoundCornerToWH(bitmap, 4)));
				}
			}

			mViewholer.chatRightRepeatButton.setRepeatListener(new RepeatListener() {

				@Override
				public void onRepeat(View v, long duration, int repeatcount) {
					callBack.onClickRepeat(v, repeatcount, chatEntity.getMesLocalID(), false, "", null);
				}
			}, 50);

			if (chatEntity.getStatus() == ChatStatus.READED_BURN.getValue()) {
				callBack.toDismiss(position + 1, convertView, true);
			}

		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		TextView chatLeftTime;//聊天时间
		RepeatButton chatLeftRepeatButton;
		RelativeLayout chatLeftBurnIvLayout;
		ProgressBar leftBurnProgress;

		//右边布局元素
		LinearLayout llRight;
		ImageView chatRightHead;//头像
		TextView chatRightTime;//聊天时间
		ProgressBar progressBar;//发送消息进度条
		ImageButton ibtnResend;
		RepeatButton chatRightRepeatButton;
		RelativeLayout chatRightBurnIvLayout;

	}

	@Override
	public void getSentChatMessageCallBack(int retcode, ChatMessageReceiptEntity recepit) {
		HandlerUtils.sendHandlerMessage(handler, HandlerType.SENT_MESSAGE_CALLBACK, retcode, retcode, recepit);
	}

	public interface ChatBurnImageInterface {
		public void onClickItem(ChatDatabaseEntity chatEntity, int pos, View view, boolean isLeft);

		public void onClickRepeat(View v, int repeatcount, long mid, boolean isLeft, String burnId, ProgressBar pb);

		public void toDismiss(int pos, View convertView, boolean b);

		public void fireTimeSend(String burnid);

		public void goneViewAndSend(String burnId);

		public void showViewByBurn(View v, String burnId, int pro, long mid);

	}

}
