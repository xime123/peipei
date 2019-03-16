package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.activity.show.adapter.ViewBaseShowChatAdapter.HeadClickListener;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.model.entity.ShowChatEntity;

/**
 * @Title: ViewTextShowChatAdapter.java 
 *
 * @Description: 秀场文本消息 
 *
 * @author allen  
 *
 * @date 2015-1-22 上午10:51:42 
 *
 * @version V1.0   
 */
public class ViewTextShowChatAdapter extends ViewBaseShowChatAdapter {

	public ViewTextShowChatAdapter(Activity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, ShowChatEntity chatEntity, String fileName) {
		ViewHolder mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_showrooms_text, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.iv_show_item_text_head);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.tv_show_item_text_nick);
			mViewholer.tvData = (TextView) convertView.findViewById(R.id.tv_show_item_text_data);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();
		}

		imageLoader.displayImage("http://" + chatEntity.uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, mViewholer.ivHead,
				options_uid_head);
		mViewholer.tvNick.setText(chatEntity.nick);
		if (chatEntity.sex == Gender.FEMALE.getValue()) {
			mViewholer.tvNick.setTextColor(activity.getResources().getColor(R.color.red_nick));
		} else {
			mViewholer.tvNick.setTextColor(activity.getResources().getColor(R.color.blue_nick));
		}

		HeadClickListener headClickListener = new HeadClickListener(chatEntity.uid, chatEntity.sex);
		mViewholer.ivHead.setOnClickListener(headClickListener);
		
		setChatTextShow(chatEntity.data, mViewholer.tvData);

		//		mViewholer.tvData.setText(new String(chatEntity.chatdatap.getChatdatalist(0).getData().toByteArray()));

		return convertView;
	}

	private final class ViewHolder {
		ImageView ivHead;	
		TextView tvNick;
		TextView tvData;
	}

	private void setChatTextShow(String message, TextView tv) {//设置纯文本数据显示
		if (!TextUtils.isEmpty(message)) {
			String unicode = EmojiParser.getInstance(activity).parseEmoji(message);
			SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(activity, unicode, HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
			tv.setText(builder);
		}

	}
}
