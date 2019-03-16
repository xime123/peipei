package com.tshang.peipei.activity.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.chat.bean.CommonFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.EmotionBean;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;

/**
 * 
 ******************************************
 * @author Jeff
 * @文件名称	:  HaremEmotionAdapter.java
 * @创建时间	: 2014-12-04下午02:34:01
 * @文件描述	: 普通表情填充器
 ******************************************
 */
@SuppressLint("NewApi")
public class CommonEmotionAdapter extends ArrayListAdapter<EmotionBean> {
	RelativeLayout.LayoutParams params;
	private EditText edt_content;
	private Activity activity;

	public CommonEmotionAdapter(Activity context, EditText edt_content) {
		super(context);
		int width = BasePhone.getScreenWidth(context);
		int itemWidth = (width - BaseUtils.dip2px(context, 120)) / 7;
		params = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
		this.edt_content = edt_content;
		this.activity = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_harem_face, null);
			viewHolder.iv_face = (ImageView) convertView.findViewById(R.id.item_iv_face);
			viewHolder.iv_face.setLayoutParams(params);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final EmotionBean bean = mList.get(position);
		if (bean != null) {
			viewHolder.iv_face.setImageResource(bean.getId());

			viewHolder.iv_face.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (bean.getId() == R.drawable.message_img_cancel_selector) {
						//						int selection = edt_content.getSelectionStart();
						//						String text = edt_content.getText().toString();
						//						if (selection > 0) {
						//							String text2 = text.substring(selection - 1);
						//							if (">".equals(text2)) {
						//								int start = text.lastIndexOf("<");
						//								int end = selection;
						//								edt_content.getText().delete(start, end);
						//								return;
						//							}else if(){
						//								
						//							}
						//							edt_content.getText().delete(selection - 1, selection);
						//						}
						((FragmentActivity) mContext).dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
					}

					if (!TextUtils.isEmpty(bean.getCharacter())) {
						//						if (listener != null)
						//							listener.commonFaceOnClick(bean);
						SpannableString spannableString = CommonFaceConversionUtil.getInstace().addFace(activity, bean.getId(), bean.getCharacter());
						edt_content.append(spannableString);
						edt_content.requestFocus();
					}

				}
			});
		}
		return convertView;
	}

	class ViewHolder {

		public ImageView iv_face;
	}

	public interface CommonFaceListener {
		public void commonFaceOnClick(EmotionBean haremBean);
	}

	public interface CommonDelFaceListener {
		public void commonDelFaceOnClick(EmotionBean haremBean);
	}
}