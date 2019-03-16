package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.platformtools.Log;
import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.entity.ChatMessageEntity;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: DareResultDialog.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-7-10 上午10:18:34 
 *
 * @version V1.0   
 */
public class DareResultDialog extends Dialog {

	public Activity context;
	private BAHandler mHandler;
	private ChatDatabaseEntity chatEntity;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options_uid_head;//通过UID加载

	public DareResultDialog(Activity context, ChatDatabaseEntity cEntity, BAHandler mHandler) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.mHandler = mHandler;
		this.chatEntity = cEntity;
		imageLoader = ImageLoader.getInstance();
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_dare_result);

		try {
			ImageView head1 = (ImageView) findViewById(R.id.dare_item_user_head_1);
			TextView nick1 = (TextView) findViewById(R.id.dare_item_user_nick_1);
			LinearLayout layout2 = (LinearLayout) findViewById(R.id.dare_item_user_2);
			ImageView head2 = (ImageView) findViewById(R.id.dare_item_user_head_2);
			TextView nick2 = (TextView) findViewById(R.id.dare_item_user_nick_2);
			TextView content = (TextView) findViewById(R.id.dare_item_user_content);

			String showMessage = chatEntity.getMessage();
			if (!TextUtils.isEmpty(showMessage)) {
				ChatMessageEntity entity = ChatMessageBiz.decodeMessage(showMessage);
//				Log.i("Aaron", "entity is null==" + (entity == null));
				if (TextUtils.isEmpty(entity.getDareuid2())) {
					layout2.setVisibility(View.GONE);
				} else {
					layout2.setVisibility(View.VISIBLE);
					nick2.setText(new String(Base64.decode(entity.getDarenick2().getBytes(), Base64.DEFAULT)));
					imageLoader.displayImage("http://" + Integer.parseInt(entity.getDareuid2()) + BAConstants.LOAD_HEAD_UID_APPENDSTR, head2,
							options_uid_head);
				}
				nick1.setText(new String(Base64.decode(entity.getDarenick1().getBytes(), Base64.DEFAULT)));
				imageLoader.displayImage("http://" + Integer.parseInt(entity.getDareuid1()) + BAConstants.LOAD_HEAD_UID_APPENDSTR, head1,
						options_uid_head);
				content.setText(entity.getMemo());
			}

			setCanceledOnTouchOutside(true);

			final Window w = getWindow();
			final WindowManager.LayoutParams wlps = w.getAttributes();
			wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
			wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
			wlps.dimAmount = 0.6f;
			wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
			wlps.gravity = Gravity.CENTER;
			w.setAttributes(wlps);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showDialog() {
		try {
			show();
			mHandler.sendEmptyMessageDelayed(HandlerValue.CHAT_DARE_DIALOG_SHOW, 2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
