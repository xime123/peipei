package com.tshang.peipei.activity.chat.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.FormatUtil;
import com.tshang.peipei.base.NumericUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: SolitaireAdapter.java 
 *
 * @Description: 用于展示可以抢的红包 
 *
 * @author DYH  
 *
 * @date 2015-12-14 下午6:48:43 
 *
 * @version V1.0   
 */
public class SolitaireAdapter extends ArrayListAdapter<ChatDatabaseEntity> {
	protected DisplayImageOptions options_uid_head;//通过UID加载
	private DisplayImageOptions gradeInfoOptions;

	public SolitaireAdapter(Activity context) {
		super(context);
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_solitaire_item, null);
			viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
			viewHolder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
			viewHolder.tv_redpacket_money = (TextView) convertView.findViewById(R.id.tv_redpacket_money);
			viewHolder.tv_jion_person = (TextView) convertView.findViewById(R.id.tv_jion_person);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ChatDatabaseEntity entity = mList.get(position);
		ChatMessageEntity chatEntity = ChatMessageBiz.decodeMessage(entity.getMessage());
		if (chatEntity != null) {
			if (Integer.parseInt(chatEntity.getOrggoldcoin()) > 0) {
				viewHolder.tv_redpacket_money.setText(mContext.getString(R.string.str_current_money, mContext.getString(R.string.gold_money),
						FormatUtil.formatNumber(NumericUtils.parseLong(chatEntity.getTotalgoldcoin(), 0)) + mContext.getString(R.string.gold_money)));
			} else {
				viewHolder.tv_redpacket_money.setText(mContext.getString(R.string.str_current_money, mContext.getString(R.string.silver_money),
						FormatUtil.formatNumber(NumericUtils.parseLong(chatEntity.getTotalgoldcoin(), 0)) + mContext.getString(R.string.silver_money)));
			}

			if (chatEntity.getJionPersons() == null || chatEntity.getJionPersons().size() == 0) {
				viewHolder.tv_jion_person.setText(mContext.getString(R.string.str_has_join_person, 0));
				setHeadImage(viewHolder.iv_head, entity.getFromID());
				viewHolder.tv_nick.setText(chatEntity.getCreatenick());

				String gradeinfo = chatEntity.getGradeinfo();
				if (!TextUtils.isEmpty(gradeinfo)) {
					viewHolder.iv_level.setVisibility(View.VISIBLE);
					GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, viewHolder.iv_level, gradeInfoOptions);
				} else {
					viewHolder.iv_level.setVisibility(View.GONE);
				}

			} else {
				viewHolder.tv_jion_person.setText(mContext.getString(R.string.str_has_join_person, chatEntity.getJionPersons().size()));
				ChatMessageEntity personEntity = chatEntity.getJionPersons().get(chatEntity.getJionPersons().size() - 1);
				if (personEntity != null) {
					setHeadImage(viewHolder.iv_head, personEntity.getCreateAvatar());
					viewHolder.tv_nick.setText(personEntity.getHaveGetRedPacketCoinUserName());

					String gradeinfo = personEntity.getGradeinfo();
					if (!TextUtils.isEmpty(gradeinfo)) {
						viewHolder.iv_level.setVisibility(View.VISIBLE);
						GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, viewHolder.iv_level, gradeInfoOptions);
					} else {
						viewHolder.iv_level.setVisibility(View.GONE);
					}

				}
			}
		}

		return convertView;
	}

	protected void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_uid_head);
	}

	protected void setHeadImage(ImageView iv, String uid) {
		imageLoader.displayImage("http://" + uid + "@true@210@210", iv, options_uid_head);
	}

	private class ViewHolder {
		public ImageView iv_head;
		public TextView tv_nick;
		public ImageView iv_level;
		public TextView tv_redpacket_money;
		public TextView tv_jion_person;
	}

}
