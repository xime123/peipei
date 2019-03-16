package com.tshang.peipei.activity.main.message.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.activity.main.message.MessageFragment;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.SessionDatabaseEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: MainMessageAdapter
 *
 * @Description: 主界面消息界面对应的adapter
 *
 * @author jeff
 *
 * @version V1.0   
 */
public class MainMessageAdapter extends ArrayListAdapter<SessionDatabaseEntity> {
	private GoGirlUserInfo userInfo;

	public MainMessageAdapter(Activity context) {
		super(context);
		options_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		userInfo = UserUtils.getUserEntity(context);
	}

	private DisplayImageOptions options_head;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;

		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_list, parent, false);
			viewholer.headerImage = (ImageView) convertView.findViewById(R.id.message_list_header_image);
			viewholer.headerNew = (TextView) convertView.findViewById(R.id.message_list_header_text);
			viewholer.nameText = (TextView) convertView.findViewById(R.id.message_list_chat_name);
			viewholer.contentText = (TextView) convertView.findViewById(R.id.message_list_chat_content);
			viewholer.timeText = (TextView) convertView.findViewById(R.id.message_list_chat_time);
			viewholer.nameImage = (ImageView) convertView.findViewById(R.id.message_list_chat_img);
			viewholer.imgGroupFlat = (ImageView) convertView.findViewById(R.id.is_group_flat);
			viewholer.iv_view_new = (ImageView) convertView.findViewById(R.id.message_list_header_text1);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		final SessionDatabaseEntity messageEntity = mList.get(position);
		if (messageEntity != null) {
			OperateViewUtils.setTextViewShowCount(viewholer.headerNew, messageEntity.getUnreadCount(), true);
			viewholer.timeText.setText(BaseTimes.getTime(messageEntity.getLatestUpdateTime()));
			if (messageEntity.getUserID() == MessageFragment.VIEW_ME_USER_ID) {
				boolean isNew = false;
				if (userInfo != null) {
					isNew = SharedPreferencesTools.getInstance(mContext, userInfo.uid.intValue() + "").getBooleanKeyValue(
							BAConstants.PEIPEI_INTERESTED_NEW);

					if (SharedPreferencesTools.getInstance(mContext, userInfo.uid.intValue() + "").getLongKeyValue(BAConstants.PEIPEI_INTERESTED) > 0) {
						viewholer.timeText.setText(BaseTimes.getTime(SharedPreferencesTools.getInstance(mContext, userInfo.uid.intValue() + "")
								.getLongKeyValue(BAConstants.PEIPEI_INTERESTED)));
					} else {
						viewholer.timeText.setText("");
					}
				}
				if (!isNew) {
					viewholer.iv_view_new.setVisibility(View.INVISIBLE);
				} else {
					viewholer.iv_view_new.setVisibility(View.VISIBLE);
				}

				viewholer.nameText.setTextColor(mContext.getResources().getColor(R.color.black));
				viewholer.nameText.setText(R.string.who_saw_me);
				viewholer.nameImage.setImageResource(android.R.color.transparent);
			} else if (messageEntity.getUserID() == BAConstants.PEIPEI_CHAT_XIAOPEI) {
				viewholer.iv_view_new.setVisibility(View.INVISIBLE);
				viewholer.nameText.setTextColor(mContext.getResources().getColor(R.color.peach));
				viewholer.nameText.setText(R.string.xiaopei);
				viewholer.nameImage.setImageResource(R.drawable.broadcast_img_with);
			} else if (messageEntity.getUserID() == BAConstants.PEIPEI_XIAOPEI) {
				viewholer.iv_view_new.setVisibility(View.INVISIBLE);
				viewholer.nameText.setTextColor(mContext.getResources().getColor(R.color.black));
				viewholer.nameText.setText(R.string.str_system_user);
				viewholer.nameImage.setImageResource(R.drawable.broadcast_img_official);
			} else if (messageEntity.getUserID() == 50002) {
				viewholer.nameText.setText(messageEntity.getNick());
				viewholer.nameText.setTextColor(mContext.getResources().getColor(R.color.black));
				viewholer.nameImage.setImageResource(R.drawable.broadcast_img_official);
			} else {
				viewholer.iv_view_new.setVisibility(View.INVISIBLE);
				viewholer.nameText.setTextColor(mContext.getResources().getColor(R.color.black));
				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						messageEntity.getUserID());
				String sendUserName = TextUtils.isEmpty(alias) ? messageEntity.getNick() : alias;

				int type = messageEntity.getType();
				if (type == 0 || type == 3) {
					viewholer.nameText.setText(sendUserName);
					if (messageEntity.getSex() == Gender.FEMALE.getValue()) {
						viewholer.nameImage.setImageResource(R.drawable.broadcast_img_girl);
					} else {
						viewholer.nameImage.setImageResource(R.drawable.broadcast_img_boy);
					}
				} else if (type == 1) {
					viewholer.nameText.setText(messageEntity.getNick());
					viewholer.nameImage.setImageResource(R.drawable.broadcast_img_palace);
				}
			}
			viewholer.imgGroupFlat.setVisibility(View.GONE);
			SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(mContext,
					EmojiParser.getInstance(mContext).parseEmoji(messageEntity.getSessionData().replace(" #@#", " ")), HaremEmotionUtil.EMOJI_SAMLL_SIZE);
			viewholer.contentText.setText(builder);
			if (messageEntity.getUserID() == MessageFragment.VIEW_ME_USER_ID) {
				viewholer.headerImage.setImageResource(R.drawable.message_head_look);
			} else if (messageEntity.getUserID() != 0) {
				int type = messageEntity.getType();
				if (type == 0) {
					String key = messageEntity.getUserID() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
					viewholer.headerImage.setImageResource(R.drawable.main_img_defaulthead_selector);
					imageLoader.displayImage("http://" + key, viewholer.headerImage, options_head);
				} else if (type == 1) {
					viewholer.imgGroupFlat.setVisibility(View.VISIBLE);
					imageLoader.displayImage("http://-" + messageEntity.getUserID() + "@true@120@120@uid", viewholer.headerImage, options_head);
				} else if (type == 3) {
					if (messageEntity.getSex() == Gender.MALE.getValue()) {
						viewholer.headerImage.setImageResource(R.drawable.dynamic_defalut_man);
					} else {
						viewholer.headerImage.setImageResource(R.drawable.dynamic_defalut_woman);
					}
				}
			} else {
				viewholer.headerImage.setImageResource(R.drawable.main_img_defaulthead_selector);
			}
		}
		return convertView;
	}

	final class ViewHoler {
		ImageView headerImage;
		TextView headerNew;
		TextView nameText;
		TextView contentText;
		TextView timeText;
		ImageView nameImage;
		ImageView imgGroupFlat;
		ImageView iv_view_new;
	}

}
