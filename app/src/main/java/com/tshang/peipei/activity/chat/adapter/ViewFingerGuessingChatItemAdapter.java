package com.tshang.peipei.activity.chat.adapter;

import java.util.Random;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.Finger;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.biz.chat.groupchat.FingerGruessMessage;
import com.tshang.peipei.model.biz.chat.groupchat.GroupChatUtils;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.view.PeiPeiCheckButton;

/**
 * @Title: VideoItemView.java 
 *
 * @Description: 猜拳不同的布局
 *
 * @author Jeff 
 *
 * @date 2014年9月1日 下午1:39:45 
 *
 * @version V1.0   
 */
public class ViewFingerGuessingChatItemAdapter extends ViewBaseChatItemAdapter {
	public ViewFingerGuessingChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler mHandler, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		int fingerWidth = (BasePhone.getScreenWidth(activity) - BaseUtils.dip2px(activity, 178)) / 3;
		this.mHandler = mHandler;
		linParams = new LinearLayout.LayoutParams(fingerWidth, fingerWidth);
	}

	private LinearLayout.LayoutParams linParams;
	private BAHandler mHandler;

	public View getView(int position, View convertView, ViewGroup parent, final ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		HeadClickListener rightHeadListener = null;
		int des = chatEntity.getDes();
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_fingerguess_type, parent, false);
			mViewholer.llLeft = (LinearLayout) convertView.findViewById(R.id.ll_chat_left_finger_guess);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_image_receive_time);
			mViewholer.leftStone = (PeiPeiCheckButton) convertView.findViewById(R.id.chat_item_left_finger_stone);
			mViewholer.leftScissors = (PeiPeiCheckButton) convertView.findViewById(R.id.chat_item_left_finger_scissors);
			mViewholer.leftCloth = (PeiPeiCheckButton) convertView.findViewById(R.id.chat_item_left_finger_cloth);
			mViewholer.leftFingerButton = (Button) convertView.findViewById(R.id.chat_item_left_finger_button);
			mViewholer.chatLeftText = (TextView) convertView.findViewById(R.id.chat_item_left_finger_tv);
			mViewholer.leftNick = (TextView) convertView.findViewById(R.id.chat_item_nick_finger_left);
			mViewholer.leftBg = (LinearLayout) convertView.findViewById(R.id.chat_item_ginger_left);
			mViewholer.leftOutBg = (LinearLayout) convertView.findViewById(R.id.chat_item_left_finger_ll);

			//设置接收内容
			mViewholer.leftStone.setLayoutParams(linParams);
			mViewholer.leftScissors.setLayoutParams(linParams);
			mViewholer.leftCloth.setLayoutParams(linParams);

			mViewholer.llRight = (LinearLayout) convertView.findViewById(R.id.ll_chat_right_finger_guess);
			mViewholer.chatRightHead = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			mViewholer.chatRightTime = (TextView) convertView.findViewById(R.id.chat_item_send_finger_time_right);
			mViewholer.ibtnResend = (ImageButton) convertView.findViewById(R.id.chat_item_right_sendflag_ibtn);
			mViewholer.progressBar = (ProgressBar) convertView.findViewById(R.id.chat_item_right_send_pb);
			mViewholer.rightFingerImage = (ImageView) convertView.findViewById(R.id.chat_item_right_finger_image);
			mViewholer.rightFingerText = (TextView) convertView.findViewById(R.id.chat_item_right_finger_text);
			mViewholer.rightFingerImage.setLayoutParams(linParams);
			mViewholer.rightNick = (TextView) convertView.findViewById(R.id.chat_item_nick_finger_right);
			mViewholer.rightBg = (LinearLayout) convertView.findViewById(R.id.chat_item_ginger_right);

			mViewholer.llMiddler = (LinearLayout) convertView.findViewById(R.id.ll_finger_result);
			mViewholer.fingerLeftHead = (ImageView) convertView.findViewById(R.id.chat_item_finger_winner_left_head);
			mViewholer.fingerRightHead = (ImageView) convertView.findViewById(R.id.chat_item_finger_winner_right_head);
			mViewholer.fingerLeftImage = (ImageView) convertView.findViewById(R.id.chat_item_finger_winner_left_image);
			mViewholer.fingerRightImage = (ImageView) convertView.findViewById(R.id.chat_item_finger_winner_right_image);
			mViewholer.fingerText = (TextView) convertView.findViewById(R.id.chat_item_finger_winner_text);
			mViewholer.fingerImage = (ImageView) convertView.findViewById(R.id.chat_item_finger_winner_image);

			mViewholer.rootLayout = (RelativeLayout) convertView.findViewById(R.id.chat_finger_root_layout);

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
		try {
			if (chatEntity != null) {
				String message = chatEntity.getMessage();
				int sendStatus = chatEntity.getStatus();
				long time = chatEntity.getCreateTime();
				if (!TextUtils.isEmpty(message)) {
					ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(message);
					int fingerwinid = Integer.parseInt(messageEntity.getFingerWinId());
					int ante = Integer.parseInt(messageEntity.getBet());
					int fingerUid1 = Integer.parseInt(messageEntity.getFingerUid1());
					int fingerUid2 = Integer.parseInt(messageEntity.getFingerUid2());
					if (des == BAConstants.ChatDes.TO_ME.getValue()) {//我收到的
						mViewholer.llRight.setVisibility(View.GONE);
						setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
						leftHeadListener.setEntity(chatEntity);
						setTimeShow(mViewholer.chatLeftTime, time);
						if (fingerwinid == -1) {//说明我收到了别人的猜拳
							mViewholer.llLeft.setVisibility(View.VISIBLE);
							mViewholer.llMiddler.setVisibility(View.GONE);
							mViewholer.leftStone.setBackgroundRes(R.drawable.message_img_morra_stone, R.drawable.message_img_morra_stone1, false);
							mViewholer.leftScissors.setBackgroundRes(R.drawable.message_img_morra_scissors, R.drawable.message_img_morra_scissors1,
									false);
							mViewholer.leftCloth.setBackgroundRes(R.drawable.message_img_morra_cloth, R.drawable.message_img_morra_cloth1, false);
							String fingerSend = "";
							if (ante > 0) {
								fingerSend = "赌注" + ante + "金币，";
								if (Integer.parseInt(messageEntity.getAntetype()) == 1) {
									fingerSend = "赌注" + ante + "银币，";
								}
							}
							mViewholer.chatLeftText.setText(String.format(activity.getResources().getString(R.string.finger_left_content1),
									fingerSend));

							SelectFingerGuessListener listener = new SelectFingerGuessListener(mViewholer.leftStone, mViewholer.leftScissors,
									mViewholer.leftCloth, messageEntity, chatEntity);
							mViewholer.leftStone.setOnClickListener(listener);
							mViewholer.leftScissors.setOnClickListener(listener);
							mViewholer.leftCloth.setOnClickListener(listener);
							mViewholer.leftFingerButton.setOnClickListener(listener);

						} else {//猜拳结果
							mViewholer.llLeft.setVisibility(View.GONE);
							mViewholer.llMiddler.setVisibility(View.VISIBLE);
							String nick1 = messageEntity.getFingerNick1();
							String nick2 = messageEntity.getFingerNick2();
							int finge1 = Integer.parseInt(messageEntity.getFinger1());
							int finge2 = Integer.parseInt(messageEntity.getFinger2());
							String winContent = "";
							if (ante > 0) {
								int ratio = SharedPreferencesTools.getInstance(activity).getIntValueByKey(BAConstants.PEIPEI_WIN_RATIO, 99);
								String str = "金币,系统消耗";
								if (Integer.parseInt(messageEntity.getAntetype()) == 1) {
									str = "银币,系统消耗";
								}
								winContent = "赢得" + ((int) (ante * ratio / 100.0)) + str + (100 - ratio) + "%";
							}
							if (fingerwinid == 0) {//说明打平了
								mViewholer.fingerText.setText(R.string.finger_winner_content2);
								if (selfuid == fingerUid1) {
									setHeadImage(mViewholer.fingerRightHead, fingerUid1);
									setHeadImage(mViewholer.fingerLeftHead, fingerUid2);
									if (finge1 == Finger.STONE.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_stone1);
									} else if (finge1 == Finger.CLOTH.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_cloth1);
									} else if (finge1 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_scissors1);
									}
									if (finge2 == Finger.STONE.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_stone2);
									} else if (finge2 == Finger.CLOTH.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_cloth2);
									} else if (finge2 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_scissors2);
									}

								} else {
									setHeadImage(mViewholer.fingerRightHead, fingerUid2);
									setHeadImage(mViewholer.fingerLeftHead, fingerUid1);
									if (finge2 == Finger.STONE.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_stone1);
									} else if (finge2 == Finger.CLOTH.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_cloth1);
									} else if (finge2 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_scissors1);
									}
									if (finge1 == Finger.STONE.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_stone2);
									} else if (finge1 == Finger.CLOTH.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_cloth2);
									} else if (finge1 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_scissors2);
									}

								}
								if (isGroupChat) {
									mViewholer.fingerImage.setVisibility(View.INVISIBLE);
								} else {
									mViewholer.fingerImage.setImageResource(R.drawable.message_results_flat);
								}
							} else if (fingerwinid == fingerUid1) {
								//赢得%2$s金币
								mViewholer.fingerText.setText(String.format(activity.getString(R.string.finger_winner_content1), nick1, winContent));
								if (isGroupChat) {
									mViewholer.fingerImage.setVisibility(View.INVISIBLE);
								}
								if (selfuid == fingerUid1) {
									setHeadImage(mViewholer.fingerRightHead, fingerUid1);
									setHeadImage(mViewholer.fingerLeftHead, fingerUid2);
									mViewholer.fingerImage.setImageResource(R.drawable.message_results_win);
									if (finge1 == Finger.STONE.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_stone1);
									} else if (finge1 == Finger.CLOTH.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_cloth1);
									} else if (finge1 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_scissors1);
									}
									if (finge2 == Finger.STONE.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_stone2);
									} else if (finge2 == Finger.CLOTH.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_cloth2);
									} else if (finge2 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_scissors2);
									}

								} else {
									setHeadImage(mViewholer.fingerRightHead, fingerUid2);
									setHeadImage(mViewholer.fingerLeftHead, fingerUid1);
									mViewholer.fingerImage.setImageResource(R.drawable.message_results_lose);
									if (finge2 == Finger.STONE.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_stone1);
									} else if (finge2 == Finger.CLOTH.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_cloth1);
									} else if (finge2 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_scissors1);
									}
									if (finge1 == Finger.STONE.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_stone2);
									} else if (finge1 == Finger.CLOTH.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_cloth2);
									} else if (finge1 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_scissors2);
									}
								}
							} else if (fingerwinid == fingerUid2) {
								mViewholer.fingerText.setText(String.format(activity.getString(R.string.finger_winner_content1), nick2, winContent));
								if (isGroupChat) {
									mViewholer.fingerImage.setVisibility(View.INVISIBLE);
								}
								if (selfuid == fingerUid1) {
									setHeadImage(mViewholer.fingerRightHead, fingerUid1);
									setHeadImage(mViewholer.fingerLeftHead, fingerUid2);
									mViewholer.fingerImage.setImageResource(R.drawable.message_results_lose);
									if (finge1 == Finger.STONE.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_stone1);
									} else if (finge1 == Finger.CLOTH.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_cloth1);
									} else if (finge1 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_scissors1);
									}
									if (finge2 == Finger.STONE.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_stone2);
									} else if (finge2 == Finger.CLOTH.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_cloth2);
									} else if (finge2 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_scissors2);
									}

								} else {
									setHeadImage(mViewholer.fingerRightHead, fingerUid2);
									setHeadImage(mViewholer.fingerLeftHead, fingerUid1);
									mViewholer.fingerImage.setImageResource(R.drawable.message_results_win);
									if (finge2 == Finger.STONE.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_stone1);
									} else if (finge2 == Finger.CLOTH.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_cloth1);
									} else if (finge2 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerRightImage.setImageResource(R.drawable.message_img_morra_scissors1);
									}
									if (finge1 == Finger.STONE.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_stone2);
									} else if (finge1 == Finger.CLOTH.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_cloth2);
									} else if (finge1 == Finger.SCISSORS.getValue()) {
										mViewholer.fingerLeftImage.setImageResource(R.drawable.message_img_morra_scissors2);
									}
								}

							}
						}

					} else {//我发送的
						mViewholer.llLeft.setVisibility(View.GONE);
						mViewholer.llRight.setVisibility(View.VISIBLE);
						mViewholer.llMiddler.setVisibility(View.GONE);
						setHeadImage(mViewholer.chatRightHead, selfuid);
						//						mViewholer.chatRightHead.setOnClickListener(new RightHeadClickListener());
						setResendData(mViewholer.ibtnResend, chatEntity, position);
						setTimeShow(mViewholer.chatRightTime, time);
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

						int finge1 = Integer.parseInt(messageEntity.getFinger1());
						if (fingerUid1 == selfuid) {//说明是我发起的猜拳

							String str = "";
							if (Integer.parseInt(messageEntity.getBet()) != 0) {
								String monkey = Integer.parseInt(messageEntity.getBet()) + "金币";
								if (Integer.parseInt(messageEntity.getAntetype()) == 1) {
									monkey = Integer.parseInt(messageEntity.getBet()) + "银币";
								}
								str = String.format(activity.getString(R.string.str_finger_ante), monkey);
							}

							mViewholer.rightFingerText.setText(String.format(activity.getString(R.string.finger_right_content1), str));
						} else if (fingerUid2 == selfuid) {//说明是我回复的猜拳
							mViewholer.rightFingerText.setText(activity.getString(R.string.finger_right_content2));
							finge1 = Integer.parseInt(messageEntity.getFinger2());
						}

						if (finge1 == Finger.STONE.getValue()) {
							mViewholer.rightFingerImage.setImageResource(R.drawable.message_img_morra_stone1);
						} else if (finge1 == Finger.CLOTH.getValue()) {
							mViewholer.rightFingerImage.setImageResource(R.drawable.message_img_morra_cloth1);
						} else if (finge1 == Finger.SCISSORS.getValue()) {
							mViewholer.rightFingerImage.setImageResource(R.drawable.message_img_morra_scissors1);
						}
					}
				} else {
					mViewholer.llLeft.setVisibility(View.GONE);
					mViewholer.llRight.setVisibility(View.GONE);
					mViewholer.llMiddler.setVisibility(View.GONE);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			mViewholer.llLeft.setVisibility(View.GONE);
			mViewholer.llRight.setVisibility(View.GONE);
			mViewholer.llMiddler.setVisibility(View.GONE);
		}
		if (isGroupChat) {
			String key = "Group_" + String.valueOf(chatEntity.getToUid()) + "#" + String.valueOf(chatEntity.getFromID());
			int bgIndex = SharedPreferencesTools.getInstance(activity).getIntValueByKey(key, 0);

			mViewholer.chatLeftTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);
			mViewholer.chatRightTime.setBackgroundResource(R.drawable.chat_item_time_group_bg);

			mViewholer.chatLeftText.setTextColor(activity.getResources().getColor(R.color.white));
			mViewholer.rightFingerText.setTextColor(activity.getResources().getColor(R.color.white));

			setGroupFingerLeftItemBackground(mViewholer.leftOutBg, bgIndex,chatEntity.getToUid(), chatEntity.getFromID());
			setGroupFingerRightItemBackground(mViewholer.rightBg, bgIndex,chatEntity.getToUid(), chatEntity.getFromID());

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = BaseUtils.dip2px(activity, 5);
			params.rightMargin = BaseUtils.dip2px(activity, 5);
			mViewholer.leftBg.setLayoutParams(params);

			mViewholer.rightNick.setVisibility(View.VISIBLE);
			mViewholer.leftNick.setVisibility(View.VISIBLE);
			mViewholer.rightNick.setText(new String(BAApplication.mLocalUserInfo.nick));
			mViewholer.leftNick.setText(chatEntity.getRevStr2());

			setNickListener(mViewholer.leftNick, mViewholer.leftNick.getText().toString());
		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		LinearLayout llLeft;
		ImageView chatLeftHead;//头像
		TextView chatLeftTime;//聊天时间
		PeiPeiCheckButton leftStone;
		PeiPeiCheckButton leftScissors;
		PeiPeiCheckButton leftCloth;
		Button leftFingerButton;
		TextView chatLeftText;
		TextView leftNick;
		LinearLayout leftBg;
		LinearLayout leftOutBg;

		//右边布局元素
		LinearLayout llRight;
		ImageView chatRightHead;//头像
		TextView chatRightTime;//聊天时间
		ProgressBar progressBar;//发送消息进度条
		ImageButton ibtnResend;
		ImageView rightFingerImage;
		TextView rightFingerText;
		TextView rightNick;

		LinearLayout llMiddler;//中间结果
		ImageView fingerLeftHead;
		ImageView fingerRightHead;
		ImageView fingerLeftImage;
		ImageView fingerRightImage;
		TextView fingerText;
		ImageView fingerImage;

		LinearLayout rightBg;

		RelativeLayout rootLayout;

	}

	private class SelectFingerGuessListener implements OnClickListener {
		PeiPeiCheckButton leftStone;
		PeiPeiCheckButton leftScissors;
		PeiPeiCheckButton leftCloth;
		ChatMessageEntity messageEntity;
		ChatDatabaseEntity chatEntity;

		public SelectFingerGuessListener(PeiPeiCheckButton leftStone, PeiPeiCheckButton leftScissors, PeiPeiCheckButton leftCloth,
				ChatMessageEntity messageEntity, ChatDatabaseEntity chatEntity) {
			this.leftStone = leftStone;
			this.leftScissors = leftScissors;
			this.leftCloth = leftCloth;
			this.messageEntity = messageEntity;
			this.chatEntity = chatEntity;
		}

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.chat_item_left_finger_stone:
				leftScissors.setCheck(false);
				leftStone.setCheck(true);
				leftCloth.setCheck(false);
				break;
			case R.id.chat_item_left_finger_scissors:
				leftScissors.setCheck(true);
				leftStone.setCheck(false);
				leftCloth.setCheck(false);
				break;
			case R.id.chat_item_left_finger_cloth:
				leftScissors.setCheck(false);
				leftStone.setCheck(false);
				leftCloth.setCheck(true);
				break;
			case R.id.chat_item_left_finger_button:
				if (messageEntity == null) {
					return;
				}
				int checkid = -1;
				if (leftStone.isCheck()) {
					checkid = Finger.STONE.getValue();
				} else if (leftScissors.isCheck()) {
					checkid = Finger.SCISSORS.getValue();
				} else if (leftCloth.isCheck()) {
					checkid = Finger.CLOTH.getValue();
				}

				if (checkid < 0) {
					BaseUtils.showTost(activity, "请选择您要出的猜拳");
					return;
				}
				try {
					int bet = Integer.parseInt(messageEntity.getBet());
					FingerGuessingInfo fingerInfo = GroupChatUtils.getReplyFingerGuessInfo(activity, messageEntity, checkid);
					FingerGruessMessage.getInstance().playFinger(activity, friendUid, chatEntity, isGroupChat, fingerInfo, friendNick, friendSex,
							mHandler, false, checkid, bet, Integer.parseInt(messageEntity.getAntetype()));

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		}

	}
}
