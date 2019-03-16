package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.biz.space.SkillUtilsBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.model.request.RequestConfirmSkillDeal.iConfirmSkillDeal;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.table.ChatTable;

/**
 * @Title: JoinHaremChatItemAdapter.java 
 *
 * @Description: 技能下单推送消息过来
 *
 * @author Jeff  
 *
 * @date 2014年10月24日 上午11:39:45 
 *
 * @version V1.4.0   
 */
public class ViewSkillOrderChatItemAdapter extends ViewBaseChatItemAdapter {
	public ViewSkillOrderChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate,
			FailedReSendListener resendListener, BAHandler handler, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.handler = handler;
	}

	private BAHandler handler;

	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		ViewHolder mViewholer;
		HeadClickListener leftHeadListener = null;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_skill_order_type, parent, false);
			mViewholer.chatLeftHead = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			mViewholer.chatLeftTime = (TextView) convertView.findViewById(R.id.chat_item_skill_order_receive_time);
			mViewholer.btnIgnore = (Button) convertView.findViewById(R.id.ok_cancel);
			mViewholer.btnReceive = (Button) convertView.findViewById(R.id.ok_sure);
			mViewholer.tv_step = (TextView) convertView.findViewById(R.id.chat_skill_order_step_text);
			mViewholer.title = (TextView) convertView.findViewById(R.id.tv_skill_order_toast);
			mViewholer.ll_dialog = (LinearLayout) convertView.findViewById(R.id.ll_dialog);

			leftHeadListener = new HeadClickListener(true, activity);
			mViewholer.chatLeftHead.setOnClickListener(leftHeadListener);

			convertView.setTag(mViewholer);
			convertView.setTag(mViewholer.chatLeftHead.getId(), leftHeadListener);

		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			leftHeadListener = (HeadClickListener) convertView.getTag(mViewholer.chatLeftHead.getId());

		}
		if (chatEntity != null) {
			String message = chatEntity.getMessage();
			if (!TextUtils.isEmpty(message)) {
				ChatMessageEntity messageEntity = ChatMessageBiz.decodeMessage(message);
				if (messageEntity != null) {
					String type = messageEntity.getType();
					String strSkillToast = "求赏";
					if (!TextUtils.isEmpty(type) && type.equals("1")) {
						strSkillToast = "打赏";
					}
					mViewholer.title.setText(String.format(activity.getString(R.string.str_skill_order_success), messageEntity.getTitle(),
							strSkillToast, strSkillToast));
					String strStep = messageEntity.getStep();
					if (!TextUtils.isEmpty(strStep)) {
						try {
							int step = Integer.parseInt(strStep);
							if (step == BAConstants.SkillStutas.GG_SKILL_DEAL_STEP_START) {
								mViewholer.ll_dialog.setVisibility(View.VISIBLE);
								mViewholer.btnIgnore
										.setOnClickListener(new ClickListener(chatEntity, messageEntity.getId(), BAConstants.SkillAct.NO));
								mViewholer.btnReceive.setOnClickListener(new ClickListener(chatEntity, messageEntity.getId(),
										BAConstants.SkillAct.YES));
							} else if (step == BAConstants.SkillStutas.GG_SKILL_DEAL_STEP_ACCEPT) {//已接受
								if (type.equals("0")) {//求赏
									if (BAApplication.mLocalUserInfo != null
											&& BAApplication.mLocalUserInfo.uid.intValue() == Integer.parseInt(messageEntity.getSkilluid())) {//主态

										mViewholer.ll_dialog.setVisibility(View.VISIBLE);
										mViewholer.tv_step.setText(R.string.str_skill_order_step_three);
										setBtnEnabled(mViewholer.btnIgnore, mViewholer.btnReceive);
										mViewholer.btnReceive.setText(activity.getString(R.string.str_have_recivie));
									} else {
										mViewholer.tv_step.setText(String.format(activity.getString(R.string.str_skill_order_step_four),
												strSkillToast));
										mViewholer.ll_dialog.setVisibility(View.GONE);
									}
								} else {//打赏
									if (BAApplication.mLocalUserInfo != null
											&& BAApplication.mLocalUserInfo.uid.intValue() == Integer.parseInt(messageEntity.getSkilluid())) {//主态
										mViewholer.tv_step.setText(String.format(activity.getString(R.string.str_skill_order_step_four),
												strSkillToast));
										mViewholer.ll_dialog.setVisibility(View.GONE);
									} else {
										mViewholer.ll_dialog.setVisibility(View.VISIBLE);
										mViewholer.tv_step.setText(R.string.str_skill_order_step_three);
										setBtnEnabled(mViewholer.btnIgnore, mViewholer.btnReceive);
										mViewholer.btnReceive.setText(activity.getString(R.string.str_have_recivie));

									}
								}
							} else if (step == BAConstants.SkillStutas.GG_SKILL_DEAL_STEP_REFUSE) {//已拒绝
								if (type.equals("0")) {//求赏
									if (BAApplication.mLocalUserInfo != null
											&& BAApplication.mLocalUserInfo.uid.intValue() == Integer.parseInt(messageEntity.getSkilluid())) {//主态
										setBtnEnabled(mViewholer.btnIgnore, mViewholer.btnReceive);
										mViewholer.btnIgnore.setText("已拒绝");
										mViewholer.ll_dialog.setVisibility(View.VISIBLE);
										mViewholer.tv_step.setVisibility(View.GONE);
									} else {
										mViewholer.tv_step.setText(String.format(activity.getString(R.string.str_skill_order_step_five),
												strSkillToast));
										mViewholer.ll_dialog.setVisibility(View.GONE);
									}
								} else {//打赏
									if (BAApplication.mLocalUserInfo != null
											&& BAApplication.mLocalUserInfo.uid.intValue() == Integer.parseInt(messageEntity.getSkilluid())) {//主态
										mViewholer.tv_step.setText(String.format(activity.getString(R.string.str_skill_order_step_five),
												strSkillToast));
										mViewholer.ll_dialog.setVisibility(View.GONE);
									} else {
										setBtnEnabled(mViewholer.btnIgnore, mViewholer.btnReceive);
										mViewholer.btnIgnore.setText("已拒绝");
										mViewholer.ll_dialog.setVisibility(View.VISIBLE);
										mViewholer.tv_step.setVisibility(View.GONE);

									}
								}

							} else if (step == BAConstants.SkillStutas.GG_SKILL_DEAL_STEP_RECLAIN) {
								mViewholer.ll_dialog.setVisibility(View.GONE);
								mViewholer.tv_step.setText(String.format(activity.getString(R.string.str_skill_order_step_two,
										messageEntity.getTitle())));
								mViewholer.ll_dialog.setVisibility(View.GONE);
								setBtnEnabled(mViewholer.btnIgnore, mViewholer.btnReceive);
							} else {
								mViewholer.ll_dialog.setVisibility(View.GONE);
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
			long time = chatEntity.getCreateTime();
			setHeadImage(mViewholer.chatLeftHead, chatEntity.getFromID());
			leftHeadListener.setEntity(chatEntity);
			setTimeShow(mViewholer.chatLeftTime, time);
		}
		return convertView;
	}

	private final class ViewHolder {
		//左边布局元素
		ImageView chatLeftHead;//头像
		Button btnIgnore;//忽略
		Button btnReceive;//接受
		TextView chatLeftTime;//聊天时间
		TextView title;//下单标题
		TextView tv_step;//下单提示
		LinearLayout ll_dialog;//对话框拒绝和接受
	}

	private class ClickListener implements OnClickListener, iConfirmSkillDeal {

		private ChatDatabaseEntity chatEntity;
		private String skilldealid;
		private String act;

		public ClickListener(ChatDatabaseEntity chatEntity, String skilldealid, String act) {
			this.chatEntity = chatEntity;
			this.skilldealid = skilldealid;
			this.act = act;
		}

		@Override
		public void onClick(View v) {
			if (TextUtils.isEmpty(skilldealid)) {
				return;
			}

			try {
				new SkillUtilsBiz(activity).confirmSkillDeal(act, Integer.parseInt(skilldealid), this);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void resultConfirmSkillDeal(int retCode, SkillDealInfo info) {//数据返回回来了
			if (retCode == 0) {
				if (info != null) {
					String message = ChatMessageBiz.saveSkillDealInfo(info);
					chatEntity.setMessage(message);
					ContentValues values = new ContentValues();
					values.put(ChatTable.Message, message);
					String whereClause = ChatTable.MesLocalID + "=?";
					String[] whereArgs = new String[] { String.valueOf(chatEntity.getMesLocalID()) };
					ChatOperate.getInstance(activity, friendUid, false).update(values, whereClause, whereArgs);
				}
			} else if (retCode == E_GG_SKILL_DEAL_STEP) {
				HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_SKILL_ORDER_TIME_OUT_VALUE);
			} else if (retCode == BAConstants.rspContMsgType.E_GG_PROPERTY_LACK) {
				HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_MONEY_NOT_ENGOUG_VALUE);
			} else {
				HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_SKILL_ORDER_FAILED);
			}
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_REFLESH_UI_VALUE, retCode, 0);

		}
	}

	public static int E_GG_SKILL_DEAL_STEP = -28063;// 技能订单的状态流转错误

	private void setBtnEnabled(Button b1, Button b2) {
		b1.setEnabled(false);
		b2.setEnabled(false);
		b1.setTextColor(activity.getResources().getColor(R.color.gray));
		b2.setTextColor(activity.getResources().getColor(R.color.gray));
	}

}
