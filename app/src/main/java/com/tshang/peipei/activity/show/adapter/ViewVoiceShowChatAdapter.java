package com.tshang.peipei.activity.show.adapter;

import java.io.File;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseHttpUtils;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.entity.ShowChatEntity;
import com.tshang.peipei.model.showrooms.ShowsRoomDbBiz;
import com.tshang.peipei.network.socket.ThreadPoolService;

/**
 * @Title: ViewVoiceShowChatAdapter.java 
 *
 * @Description: 秀场语音adapter
 *
 * @author allen  
 *
 * @date 2015-1-27 上午11:25:26 
 *
 * @version V1.0   
 */
public class ViewVoiceShowChatAdapter extends ViewBaseShowChatAdapter {

	private ShowsRoomDbBiz showsRoomDbBiz;

	public ViewVoiceShowChatAdapter(Activity activity, BAHandler mHandler) {
		super(activity);
		this.mHandler = mHandler;
		showsRoomDbBiz = new ShowsRoomDbBiz(activity);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent, final ShowChatEntity chatEntity, final String fileName) {
		final ViewHolder mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(activity).inflate(R.layout.item_showrooms_voice, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.iv_show_item_voice_head);
			mViewholer.llVoice = (LinearLayout) convertView.findViewById(R.id.ll_show_item_voice);
			mViewholer.ivPlay = (ImageView) convertView.findViewById(R.id.iv_show_item_voice_play);
			mViewholer.tvTime = (TextView) convertView.findViewById(R.id.tv_show_item_voice_time);
			mViewholer.ivListen = (ImageView) convertView.findViewById(R.id.iv_show_item_voice_islisten);
			mViewholer.pbVoice = (ProgressBar) convertView.findViewById(R.id.pb_show_item_voice);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();
		}

		if (chatEntity != null) {
			imageLoader.displayImage("http://" + chatEntity.uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, mViewholer.ivHead, options_uid_head);
			if (chatEntity.voiceLength == 0) {
				mViewholer.llVoice.setLayoutParams(new LayoutParams(BaseUtils.dip2px(activity, 80), LayoutParams.WRAP_CONTENT));
				mViewholer.tvTime.setText("");
			} else if (chatEntity.voiceLength <= 10) {
				mViewholer.llVoice.setLayoutParams(new LayoutParams(BaseUtils.dip2px(activity, 120), LayoutParams.WRAP_CONTENT));
				mViewholer.tvTime.setText("" + chatEntity.voiceLength + "”");
			} else if (chatEntity.voiceLength <= 30) {
				mViewholer.llVoice.setLayoutParams(new LayoutParams(BaseUtils.dip2px(activity, 150), LayoutParams.WRAP_CONTENT));
				mViewholer.tvTime.setText("" + chatEntity.voiceLength + "”");
			} else {
				mViewholer.llVoice.setLayoutParams(new LayoutParams(BaseUtils.dip2px(activity, 180), LayoutParams.WRAP_CONTENT));
				mViewholer.tvTime.setText("" + chatEntity.voiceLength + "”");
			}

			String url = chatEntity.data;
			if (!TextUtils.isEmpty(chatEntity.voiceFile)) {
				url = chatEntity.voiceFile;
			}
			
			String[] s1 = null;
			if(url != null && !TextUtils.isEmpty(url) && url.contains("/")){
				s1 = url.split("/");
			}
			
			if (s1!=null && !TextUtils.isEmpty(fileName) && fileName.equals(s1[s1.length - 1])) {
				mViewholer.pbVoice.setVisibility(View.GONE);
				mViewholer.ivPlay.setImageResource(R.drawable.message_img_voice_red);
				AnimationDrawable animationDrawable = (AnimationDrawable) mViewholer.ivPlay.getDrawable();
				animationDrawable.start();
			} else {
				mViewholer.ivPlay.setImageResource(R.drawable.message_img_voice3_red);
				mViewholer.ivPlay.clearAnimation();
			}

			HeadClickListener headClickListener = new HeadClickListener(chatEntity.uid, chatEntity.sex);
			mViewholer.ivHead.setOnClickListener(headClickListener);

			if (s1!=null && !TextUtils.isEmpty(s1[s1.length - 1]) && showsRoomDbBiz.getStatusByData(s1[s1.length - 1], chatEntity.type) == 1) {
				mViewholer.ivListen.setVisibility(View.GONE);
			} else {
				mViewholer.ivListen.setVisibility(View.VISIBLE);
			}
			final String[] s2 = s1;
			mViewholer.llVoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (chatEntity.type == 45 || chatEntity.type == 2) {
						Message msg = mHandler.obtainMessage();
						msg.what = HandlerValue.SHOW_ROOM_VOICE_NEEDLOAD;
						msg.arg1 = position;
						mHandler.sendMessageAtTime(msg, 10);
						if (s2!=null && (TextUtils.isEmpty(fileName) || !fileName.equals(s2[s2.length - 1]))) {
							mViewholer.pbVoice.setVisibility(View.VISIBLE);

							ThreadPoolService.getInstance().execute(new Runnable() {

								@Override
								public void run() {
									File f = new File(SdCardUtils.getInstance().getDirectory(0));
									if (!f.exists()) {
										f.mkdirs();
									}
									Message msg = mHandler.obtainMessage();
									File file = new File(SdCardUtils.getInstance().getDirectory(0), s2[s2.length - 1]);
									msg.what = HandlerValue.SHOW_ROOM_VOICE_ISEXITS;
									if (file.exists()) {
										msg.obj = file.getAbsolutePath();
									} else {
										msg.obj = BaseHttpUtils.downLoadFile(activity, chatEntity.data, s2[s2.length - 1]);
									}
									mHandler.sendMessageAtTime(msg, 10);
								}
							});

						}
					}
				}
			});
		}

		return convertView;
	}

	private final class ViewHolder {
		ImageView ivHead;
		LinearLayout llVoice;
		ImageView ivPlay;
		TextView tvTime;
		ImageView ivListen;
		ProgressBar pbVoice;
	}

}
