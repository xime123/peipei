package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.model.entity.ShowChatEntity;
import com.tshang.peipei.model.space.SpaceBiz;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

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
public class ViewPetShowChatAdapter extends ViewBaseShowChatAdapter {

	public ViewPetShowChatAdapter(Activity activity) {
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

		imageLoader.displayImage("http://" + chatEntity.uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, mViewholer.ivHead, options_uid_head);
		mViewholer.tvNick.setText(chatEntity.nick);
		if (chatEntity.sex == Gender.FEMALE.getValue()) {
			mViewholer.tvNick.setTextColor(activity.getResources().getColor(R.color.red_nick));
		} else {
			mViewholer.tvNick.setTextColor(activity.getResources().getColor(R.color.blue_nick));
		}

		HeadClickListener headClickListener = new HeadClickListener(chatEntity.uid, chatEntity.sex);
		mViewholer.ivHead.setOnClickListener(headClickListener);

		setChatTextShow(chatEntity, mViewholer.tvData);

		//		mViewholer.tvData.setText(new String(chatEntity.chatdatap.getChatdatalist(0).getData().toByteArray()));

		return convertView;
	}

	private final class ViewHolder {
		ImageView ivHead;
		TextView tvNick;
		TextView tvData;
	}

	private void setChatTextShow(ShowChatEntity chatEntity, TextView tv) {//设置纯文本数据显示

		if (chatEntity.ridingid > 0) {//说名有坐骑
			String inoutStr = "带着-" + chatEntity.ridingName + "坐骑进入秀场";
			Bitmap bitmap = null;
			int size = BaseUtils.dip2px(activity, 48);
			if (chatEntity.ridingid == SpaceBiz.DEER_RIDING_VALUE) {
				bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.deer1);
			} else if (chatEntity.ridingid == SpaceBiz.BIRD_RIDING_VALUE) {
				bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.bird2);
			} else if (chatEntity.ridingid == SpaceBiz.PEACOCK_RIDING_VALUE) {
				bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.peacock5);
			}
			// 计算该图片名字的长度，也就是要替换的字符串的长度
			SpannableString spannableString = new SpannableString(inoutStr);
			if (bitmap != null) {
				bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
				// 通过图片资源id来得到bitmap，用一个ImageSpan来包装
				ImageSpan imageSpan = new ImageSpan(activity, bitmap);
				// 将该图片替换字符串中规定的位置中
				spannableString.setSpan(imageSpan, 2, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//替换--
			}
			tv.setText(spannableString);

		}

	}
}
