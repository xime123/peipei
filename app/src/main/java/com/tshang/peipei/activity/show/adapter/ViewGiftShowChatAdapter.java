package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.model.entity.ShowChatEntity;

/**
 * @Title: ViewGiftShowCHatAdapter.java 
 *
 * @Description: 秀场送礼   
 *
 * @author allen
 *
 * @date 2015-1-29 上午11:34:28 
 *
 * @version V1.0   
 */
public class ViewGiftShowChatAdapter extends ViewBaseShowChatAdapter {

	public ViewGiftShowChatAdapter(Activity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, ShowChatEntity chatEntity, String fileName) {
		ViewHolder mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_showrooms_gift, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.iv_show_item_gift_head);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.tv_show_item_gift_nick);
			mViewholer.tvData = (TextView) convertView.findViewById(R.id.tv_show_item_gift_data);
			mViewholer.ivGift = (ImageView) convertView.findViewById(R.id.iv_show_item_gift_view);
			mViewholer.tvNum = (TextView) convertView.findViewById(R.id.tv_show_item_gift_num);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();
		}

		imageLoader.displayImage("http://" + chatEntity.uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, mViewholer.ivHead, options_uid_head);
		mViewholer.tvNick.setText(chatEntity.nick + ":");
		if (chatEntity.sex == Gender.FEMALE.getValue()) {
			mViewholer.tvNick.setTextColor(activity.getResources().getColor(R.color.red_nick));
		} else {
			mViewholer.tvNick.setTextColor(activity.getResources().getColor(R.color.blue_nick));
		}
		HeadClickListener headClickListener = new HeadClickListener(chatEntity.uid, chatEntity.sex);
		mViewholer.ivHead.setOnClickListener(headClickListener);
		mViewholer.tvData.setText("向房主赠送" + chatEntity.giftName);
		imageLoader.displayImage("http://" + chatEntity.giftKey + BAConstants.LOAD_180_APPENDSTR, mViewholer.ivGift, options_gift);
		mViewholer.tvNum.setText("x" + chatEntity.giftNum);

		return convertView;
	}

	private final class ViewHolder {
		ImageView ivHead;
		TextView tvNick;
		TextView tvData;
		ImageView ivGift;
		TextView tvNum;
	}
}
