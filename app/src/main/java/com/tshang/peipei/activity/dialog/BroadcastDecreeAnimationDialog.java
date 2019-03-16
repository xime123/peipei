package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ListUtils;

/**
 * 广播动画接口
 * @author Jeff
 *
 */
public class BroadcastDecreeAnimationDialog extends Dialog implements DialogInterface.OnDismissListener {

	private Activity context;
	private BroadcastInfo castInfo;
	private BroadcastQueue broadcast;

	public BroadcastDecreeAnimationDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public BroadcastDecreeAnimationDialog(Activity context, int theme, BroadcastInfo castInfo, BroadcastQueue broadcast, BAHandler handler) {
		super(context, theme);
		this.context = context;
		this.castInfo = castInfo;
		this.broadcast = broadcast;

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.popupwindow_decree);
		RelativeLayout imageview = (RelativeLayout) findViewById(R.id.rl_decree);
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.flip_horizontal_in);
		imageview.startAnimation(anim);

		TextView tv_decree_content = (TextView) findViewById(R.id.tv_broadcast_pop_content);

		TextView tv_decree_name = (TextView) findViewById(R.id.tv_decree_name);

		ImageView iv_title = (ImageView) findViewById(R.id.iv_broad_title);
		ImageView iv_flag = (ImageView) findViewById(R.id.iv_decree_flag);

		if (castInfo != null) {
			GoGirlUserInfo userInfo = castInfo.senduser;

			String alias = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					userInfo.uid.intValue());
			tv_decree_name.setText(TextUtils.isEmpty(alias) ? new String(userInfo.nick) : alias);

			StringBuffer sb = new StringBuffer();
			String[] usersStr = null;
			GoGirlUserInfoList userInfoList = castInfo.tousers;
			if (!ListUtils.isEmpty(userInfoList)) {//@的用户
				int len = userInfoList.size();
				usersStr = new String[len];
				for (int i = 0; i < len; i++) {
					GoGirlUserInfo info = (GoGirlUserInfo) userInfoList.get(i);
					if (info != null) {
						String alias1 = SharedPreferencesTools.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "_remark")
								.getAlias(info.uid.intValue());

						String userName = TextUtils.isEmpty(alias1) ? new String(info.nick) : alias1;
						usersStr[i] = "@" + userName;
						sb.append("@").append(userName);
					}
				}
			}
			byte[] datalist = castInfo.datalist;
			if (datalist != null && datalist.length != 0) {
				GoGirlDataInfoList infoList = new GoGirlDataInfoList();
				BERDecoder dec = new BERDecoder(datalist);

				try {
					infoList.decode(dec);
				} catch (ASN1Exception e1) {
					e1.printStackTrace();
				}
				if (!infoList.isEmpty()) {
					GoGirlDataInfo datainfo = (GoGirlDataInfo) infoList.get(0);
					if (datainfo != null) {
						if (!TextUtils.isEmpty(sb.toString()) && usersStr != null) {//有@的用户让其名字变色
							String messageContent = sb.toString() + new String(datainfo.data);
							SpannableString style = getStyle1(messageContent, usersStr, userInfoList);
							tv_decree_content.setMovementMethod(LinkMovementMethod.getInstance());
							tv_decree_content.setText(style);
						} else {
							String unicode = EmojiParser.getInstance(context).parseEmoji(new String(datainfo.data));
							SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(context, unicode,
									HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
							tv_decree_content.setText(builder);
							//							tv_decree_content.setText(ParseMsgUtil.convetToHtml(new String(datainfo.data), context, BaseUtils.dip2px(context, 24)));
						}
						if (datainfo.type.intValue() == MessageType.FEMALE_DECREE.getValue()) {
							iv_title.setImageResource(R.drawable.broadcast_list_text);
							iv_flag.setImageResource(R.drawable.broadcast_toplist_print_women);
						} else if (datainfo.type.intValue() == MessageType.MALE_DECREE.getValue()) {
							iv_title.setImageResource(R.drawable.broadcast_list_text1);
							iv_flag.setImageResource(R.drawable.broadcast_toplist_print_man);
						}
						//						iv_flag.startAnimation(animImg);
						anim.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {

							}

							@Override
							public void onAnimationRepeat(Animation animation) {

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								broadcast.setSynchWork(false);
								BroadcastDecreeAnimationDialog.this.dismiss();
							}
						});
					}
				}
			}
		}
		BroadcastDecreeAnimationDialog.this.setOnDismissListener(this);
	}

	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window window = getWindow(); // 得到对话框
		final WindowManager.LayoutParams wlps = window.getAttributes();
		wlps.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		broadcast.setSynchWork(false);
	}

	private SpannableString getStyle1(String messageContent, String[] usersStr, GoGirlUserInfoList userInfoList) {
		String unicode = EmojiParser.getInstance(context).parseEmoji(messageContent);
		SpannableString builder = EmojiFaceConversionUtil.getInstace().getExpressionString(context, unicode, HaremEmotionUtil.EMOJI_MIDDLE_SIZE);
		int len = usersStr.length;
		int startPos = 0;
		int endPos = 0;
		for (int i = 0; i < len; i++) {
			String aUsers = usersStr[i];
			endPos += aUsers.length();
			builder.setSpan(null, startPos, endPos, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			startPos += aUsers.length();
		}
		return builder;
	}

}
