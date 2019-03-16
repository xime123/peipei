package com.tshang.peipei.activity.chat.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.ILog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.privatechat.PlayTruth;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: ViewTruthChatItemAdapter.java 
 *
 * @Description: 真心话适配器
 *
 * @author Aaron  
 *
 * @date 2015-6-12 下午4:47:36 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class ViewTruthChatItemAdapter extends ViewBaseChatItemAdapter {

	private final String TAG = this.getClass().getSimpleName();

	private Context mContext;

	private String[] answerList;

	private List<ChatDatabaseEntity> entities;
	private ChatDatabaseEntity entity;
	private BAHandler mHandler;

	public ViewTruthChatItemAdapter(Activity activity, int friendUid, int friendSex, String friendNick, boolean isGroupChate, BAHandler mHandler,
			FailedReSendListener resendListener, NickOnClickListener nickOnClickListener) {
		super(activity, friendUid, friendSex, friendNick, isGroupChate, resendListener, nickOnClickListener);
		this.mContext = activity;
		this.mHandler = mHandler;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent, ChatDatabaseEntity chatEntity) {
		this.entity = chatEntity;
		ViewHolder viewHolder;
		HeadClickListener leftHeadClickListener = null;
		HeadClickListener rightHeadClickListener = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat_listview_truth_type, parent, false);
			viewHolder.leftHeadIv = (ImageView) convertView.findViewById(R.id.iv_chat_item_head_left);
			viewHolder.rightHeadIv = (ImageView) convertView.findViewById(R.id.chat_item_head_right);
			viewHolder.leftMsgTv = (TextView) convertView.findViewById(R.id.chat_item_truth_content_text_left);
			viewHolder.rightMsgTv = (TextView) convertView.findViewById(R.id.chat_item_truth_content_text_right);
			viewHolder.probleBoardLayout = (LinearLayout) convertView.findViewById(R.id.chat_item_truth_problem_board_layout);
			viewHolder.rightHeadLayout = (LinearLayout) convertView.findViewById(R.id.chat_item_truth_right_head_layout);
			viewHolder.leftHeadLayout = (LinearLayout) convertView.findViewById(R.id.chat_item_truth_left_head_layout);

			leftHeadClickListener = new HeadClickListener(true, activity);
			viewHolder.leftHeadIv.setOnClickListener(leftHeadClickListener);
			rightHeadClickListener = new HeadClickListener(false, activity);
			viewHolder.rightHeadIv.setOnClickListener(rightHeadClickListener);

			convertView.setTag(viewHolder);
			convertView.setTag(viewHolder.leftHeadIv.getId(), leftHeadClickListener);
			convertView.setTag(viewHolder.rightHeadIv.getId(), rightHeadClickListener);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			leftHeadClickListener = (HeadClickListener) convertView.getTag(viewHolder.leftHeadIv.getId());
			rightHeadClickListener = (HeadClickListener) convertView.getTag(viewHolder.rightHeadIv.getId());
		}
		//判断哪方发送
		int des = chatEntity.getDes();
		if (des != BAConstants.ChatDes.TO_ME.getValue()) {
			viewHolder.rightHeadLayout.setVisibility(View.GONE);
			viewHolder.rightMsgTv.setVisibility(View.GONE);
			viewHolder.leftHeadLayout.setVisibility(View.VISIBLE);
			viewHolder.leftMsgTv.setVisibility(View.VISIBLE);
			viewHolder.leftMsgTv.setText(mContext.getResources().getString(R.string.truth_head) + ":\n" + new String(chatEntity.getMessage()));
			setHeadImage(viewHolder.leftHeadIv, chatEntity.getFromID());
			leftHeadClickListener.setEntity(chatEntity);
		} else {
			viewHolder.leftHeadLayout.setVisibility(View.GONE);
			viewHolder.leftMsgTv.setVisibility(View.GONE);
			viewHolder.rightHeadLayout.setVisibility(View.VISIBLE);
			viewHolder.rightMsgTv.setVisibility(View.VISIBLE);
			viewHolder.rightMsgTv.setText(mContext.getResources().getString(R.string.truth_head) + ":\n" + chatEntity.getMessage());
			setHeadImage(viewHolder.rightHeadIv, BAApplication.mLocalUserInfo.uid.intValue());
			rightHeadClickListener.setEntity(chatEntity);
		}
		//解析答案选项
		viewHolder.probleBoardLayout.removeAllViews();
		String[] answers = chatEntity.getRevStr3().split(",");
		answerList = new String[answers.length];
		for (int i = 0; i < answers.length; i++) {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			String answer = answers[i].substring(answers[i].lastIndexOf("+") + 1);
			String tag = answers[i].substring(0, answers[i].lastIndexOf("+"));
			answers[i] = answer;

			viewHolder.probleBoardLayout.addView(parseAnswerLayout(answer, tag, chatEntity.getProgress(), chatEntity.getMesSvrID(), chatEntity),
					params);
		}

		ILog.d(TAG, "progress==" + chatEntity.getProgress());

		return convertView;
	}

	private class ViewHolder {
		private ImageView leftHeadIv, rightHeadIv;
		private TextView leftMsgTv, rightMsgTv;
		private LinearLayout probleBoardLayout;
		private LinearLayout leftHeadLayout, rightHeadLayout;

	}

	/**
	 * 答案布局item初始化
	 * @author Aaron
	 *
	 * @param answer
	 * @param tag
	 * @return
	 */
	private View parseAnswerLayout(String answer, String tag, int progress, String answerId, ChatDatabaseEntity entity) {
		View mInflater = LayoutInflater.from(activity).inflate(R.layout.chat_truth_problem_board_layout, null);
		TextView textView = (TextView) mInflater.findViewById(R.id.chat_truth_problem_board_tv);
		textView.setText(answer);
		textView.setTag(tag + "," + progress);
		//		if (progress == 1) {//已参加
		//			//			android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
		//			//					android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		//			//			params.topMargin = 10;
		//			//			textView.setTextColor(mContext.getResources().getColor(R.color.gray));
		//			//			textView.setBackgroundResource(R.drawable.message_img_btn_hua_pr);
		//			//			textView.setPadding(BaseUtils.dip2px(mContext, 10), 0, 0, 0);
		//			//			textView.setTextSize(15);
		//			//			textView.setLayoutParams(params);
		//			BaseUtils.showTost(mContext, "本局真话话你已参加过!");
		//		} else {
		textView.setOnClickListener(new AnswerItemOnClick(entity));
		//		}

		return mInflater;
	}

	/**
	 * 答案选择监听
	 * @author Aaron
	 *
	 */
	public class AnswerItemOnClick implements OnClickListener {

		private ChatDatabaseEntity entity;

		public AnswerItemOnClick(ChatDatabaseEntity entity) {
			this.entity = entity;
		}

		@Override
		public void onClick(View v) {
			String tag = v.getTag().toString();
			int answerid = Integer.parseInt(tag.substring(0, tag.lastIndexOf(",")));
			int progress = Integer.parseInt(tag.substring(tag.lastIndexOf(",") + 1, tag.length()));
			if (progress == 1) {//已参加
				BaseUtils.showTost(mContext, R.string.truth_pass);
			} else if (progress == 2) {

			} else {
				String answer = null;
				String string[] = entity.getRevStr3().split(",");
				for (int i = 0; i < string.length; i++) {
					String a = string[i].substring(string[i].lastIndexOf("+") + 1);
					int aid = Integer.parseInt(string[i].substring(0, string[i].lastIndexOf("+")));
					if (aid == answerid) {
						answer = a;
						break;
					}
				}
				PlayTruth playTruth = new PlayTruth();
				playTruth.sendAnswer(entity.getFromID(), answerid, entity.getMesSvrID(), answer, mHandler);
				Message msg = mHandler.obtainMessage();
				msg.what = HandlerValue.CHAT_TRUTH_ANSWER_SUCCESS_RESULT_CODE;
				msg.obj = entity.getMesSvrID();
				msg.arg1 = 2;
				mHandler.sendMessage(msg);
			}
		}
	}
}
