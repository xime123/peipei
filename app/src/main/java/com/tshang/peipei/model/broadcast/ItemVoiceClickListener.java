package com.tshang.peipei.model.broadcast;

import java.io.File;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;

/**
 * @Title: ItemVoiceClickListener.java 
 *
 * @Description:点击广播里面的语音
 *
 * @author Jeff  
 *
 * @date 2014年8月13日 下午2:49:49 
 *
 * @version V1.0   
 */
public class ItemVoiceClickListener implements OnClickListener {
	private String fileName;
	private BroadCastBiz broadCastBiz;
	private GoGirlUserInfo userInfo;
	public static final int VOICE_TOP_BC = 0;//播放的是置顶广播
	public static final int VOICE_COMMON_BC = 1;//播放的是普通广播
	private int playVoiceType = VOICE_TOP_BC;
	private ProgressBar pb;
	private ImageView imageView;

	public ItemVoiceClickListener(ImageView imageView, ProgressBar pb, int playVoiceType, GoGirlUserInfo userInfo, String fileName,
			BroadCastBiz broadCastBiz) {
		this.fileName = fileName;
		this.broadCastBiz = broadCastBiz;
		this.userInfo = userInfo;
		this.playVoiceType = playVoiceType;
		this.pb = pb;
		this.imageView = imageView;

	}

	@Override
	public void onClick(View v) {
		imageView.setVisibility(View.GONE);
		if (!TextUtils.isEmpty(fileName)) {
			pb.setVisibility(View.VISIBLE);
			GoGirlUserInfo userEntity = UserUtils.getUserEntity(broadCastBiz.getActivity());
			boolean isNeedLoad = true;
			int uid = 0;
			if (userEntity != null) {//区分不同用户的文件夹
				if (userInfo != null) {
					if (userInfo.uid.intValue() == userEntity.uid.intValue()) {
						uid = userEntity.uid.intValue();
					}
				}
			}
			String path = SdCardUtils.getInstance().getDirectory(uid);
			File dirFile = new File(path);
			if (dirFile.exists()) {
				File file = new File(path + "/" + fileName);
				if (file.exists()) {//如果本地存在这个文件就不需要重复下载
					isNeedLoad = false;
					pb.setVisibility(View.GONE);
					broadCastBiz.sendHandlerMessage(HandlerValue.BROADCAST_VOIDE_LOAD_COMPLETE_PLAY_VALUE, playVoiceType, file.getAbsolutePath());
				}
			}
			if (isNeedLoad) {
				broadCastBiz.getVoiceByKey(pb, fileName.getBytes(), playVoiceType);
			}
		}
	}
}
