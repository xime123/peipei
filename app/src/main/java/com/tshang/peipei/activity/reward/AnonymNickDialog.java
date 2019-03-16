package com.tshang.peipei.activity.reward;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.reward.entity.AnonymNickInfo;
import com.tshang.peipei.activity.reward.entity.BindAnonymNickEntity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.reward.RewardRequestControl;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestBindAnonymNice.BindAnonymNickCallBack;
import com.tshang.peipei.model.request.RequestGetAnonymNiceInfo.GetAnonyNickInfoCallBack;
import com.tshang.peipei.model.request.RequestUserAnonymNiceInfo.GetUserAnonyNickCallBack;

import de.greenrobot.event.EventBus;

/**
 * @Title: AnonymNickDialog.java 
 *
 * @Description: 更换匿名Nick Dialog 
 *
 * @author Aaron  
 *
 * @date 2015-11-28 下午5:57:28 
 *
 * @version V1.0   
 */
public class AnonymNickDialog implements BindAnonymNickCallBack, GetAnonyNickInfoCallBack, GetUserAnonyNickCallBack {

	private Context context;

	private final int OBLIGATE_CODE = -28208;//预留字段

	private Dialog dialog;
	private Dialog pressDialog;

	private RewardRequestControl control;

	private int anonymNickId;
	private String anonymNick;

	private int reqCount;
	private int isBind = -1;

	private TextView nickTV;
	private ProgressBar progress;
	private TextView nickPastTv;

	private boolean isChangeNick = false;

	//匿名悬赏开关
	public static final int REWARD_SWITCH = 0;//0:开；1:关

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HandlerValue.BIND_ANONYM_NICK_REWARD_SUCCESS:
				DialogFactory.dimissDialog(pressDialog);
				BindAnonymNickEntity entity = (BindAnonymNickEntity) msg.obj;
				Log.d("Aaron", "bind code====" + entity.getCode() + ", nickId==" + entity.getNickId() + ", nick==" + entity.getNick());
				if (entity.getCode() == 0) {
					isBind = 1;
					anonymNick = entity.getNick();
					anonymNickId = entity.getNickId();
					//匿名Nick有效期结束
					if (entity.getStatus() == 1) {
						anonymNickId = entity.getNickId();
						anonymNick = entity.getNick();
						createAnonymNickDialog(context, anonymNick, "该昵称已到期，点击确认继续使用");
					} else if (entity.getStatus() == 999) {//匿名悬赏开关
						NoticeEvent event = new NoticeEvent();
						event.setFlag(NoticeEvent.NOTICE100);
						event.setNum(REWARD_SWITCH);
						EventBus.getDefault().post(event);
					} else {
						anonymNickId = entity.getNickId();
						anonymNick = entity.getNick();
						mHandler.sendEmptyMessage(HandlerValue.BIND_ANONYM_NICK_SUCCESS);
					}
				} else if (entity.getCode() == -21001) {//用户未绑定匿名Nick
					isBind = 0;
					getAnonymNickInfo();
				} else if (entity.getCode() == -28097) {//有参加、发布的悬赏还未结束
					anonymNickId = entity.getNickId();
					anonymNick = entity.getNick();
					mHandler.sendEmptyMessage(HandlerValue.BIND_ANONYM_NICK_SUCCESS);
				} else if (entity.getCode() == OBLIGATE_CODE) {
					BaseUtils.showTost(context, entity.getMessage());
				} else {
					isBind = 0;
					getAnonymNickInfo();
				}
				break;
			case HandlerValue.BIND_ANONYM_NICK_REWARD_ERROR:
				DialogFactory.dimissDialog(pressDialog);
				BaseUtils.showTost(context, R.string.toast_login_failure);
				break;
			case HandlerValue.GET_ANONYM_NICK_SUCCESS:
				isChangeNick = false;
				DialogFactory.dimissDialog(dialog);
				AnonymNickInfo info = (AnonymNickInfo) msg.obj;
				Log.d("Aaron", "get Anonym code====" + info.getCode() + ", nickId==" + info.getNickId() + ", nick==" + info.getNick() + ", isBind=="
						+ isBind);
				int code = info.getCode();
				anonymNick = info.getNick();
				anonymNickId = info.getNickId();
				if (code == 0) {
					if (isBind != -1 || isBind == 0) {//未绑定过
						createAnonymNickDialog(context, info.getNick(), "");
					} else {
						nickTV.setText(anonymNick);
						progress.setVisibility(View.INVISIBLE);
						nickPastTv.setVisibility(View.GONE);
					}
				} else if (code == -28206) {//没有昵称可以使用，重复请求两次
					if (reqCount > 2) {
						if (progress != null) {
							progress.setVisibility(View.INVISIBLE);
						}
						createAnonymNickDialog(context, info.getNick(), "更换昵称失败");
					} else {
						getAnonymNickInfo();
						reqCount++;
					}
				} else if (code == -28097) {//有参加、发布的悬赏还未结束
					if (reqCount > 2) {
						createAnonymNickDialog(context, anonymNick, "昵称绑定失败");
					} else {
						getAnonymNickInfo();
						reqCount++;
					}
				} else if (code == OBLIGATE_CODE) {//预留错误字段
					BaseUtils.showTost(context, msg.obj.toString());
				}
				break;
			case HandlerValue.GET_ANONYM_NICK_ERROR:
				isChangeNick = false;
				DialogFactory.dimissDialog(dialog);
				progress.setVisibility(View.INVISIBLE);
				createAnonymNickDialog(context, anonymNick, "");
				BaseUtils.showTost(context, R.string.toast_login_failure);
				break;

			case HandlerValue.USER_ANONYM_NICK_SUCCESS:
				DialogFactory.dimissDialog(pressDialog);
				DialogFactory.dimissDialog(dialog);
				Log.d("Aaron", "user_anonym=" + msg.arg1);
				if (msg.arg1 == 0) {
					mHandler.sendEmptyMessage(HandlerValue.BIND_ANONYM_NICK_SUCCESS);
				} else if (msg.arg1 == -28204) {
					createAnonymNickDialog(context, anonymNick, context.getResources().getString(R.string.anonym_dialog_nick_past_toast));
				} else if (msg.arg1 == -28207) {
					BaseUtils.showTost(context, context.getResources().getString(R.string.anonym_dialog_nick_limit_24));
				} else if (msg.arg1 == OBLIGATE_CODE) {//预留错误码
					BaseUtils.showTost(context, msg.obj.toString());
				} else if (msg.arg1 == -28097) {////有参加、发布的悬赏还未结束
					mHandler.sendEmptyMessage(HandlerValue.BIND_ANONYM_NICK_SUCCESS);
				} else {
					createAnonymNickDialog(context, anonymNick, "昵称使用失败");
					//					mHandler.sendEmptyMessage(HandlerValue.BIND_ANONYM_NICK_SUCCESS);
					//					BaseUtils.showTost(context, R.string.anonym_dialog_nick_user_error);
				}
				break;
			case HandlerValue.USER_ANONYM_NICK_ERROR:
				DialogFactory.dimissDialog(pressDialog);
				BaseUtils.showTost(context, R.string.toast_login_failure);
				break;
			case HandlerValue.BIND_ANONYM_NICK_SUCCESS://匿名绑定成功
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE98);
				event.setNum(anonymNickId);
				event.setObj(anonymNick);
				EventBus.getDefault().post(event);
				break;
			default:
				break;
			}
		};
	};

	public AnonymNickDialog(Activity context) {
		control = new RewardRequestControl();
		this.context = context;
	}

	/**
	 * 匿名Nick检测是否过期
	 *
	 * @param uid
	 */
	public void bindAnonymNick() {
		pressDialog = DialogFactory.createLoadingDialog(context, R.string.loading);
		DialogFactory.showDialog(pressDialog);
		control.requestBindAnonymNick(this);
	}

	/**
	 * 匿名Nick更换
	 *
	 */
	public void getAnonymNickInfo() {
		control.requestGetAnonymNickInfo(this);
	}

	/**
	 * 匿名Nick确定使用
	 *
	 * @param id
	 */
	public void userAnonymNickInfo(int id) {
		pressDialog = DialogFactory.createLoadingDialog(context, "绑定中...");
		DialogFactory.showDialog(pressDialog);
		control.requestUserAnonyNickInfo(id, this);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void createAnonymNickDialog(Context context, String nick, String toast) {
		View view = LayoutInflater.from(context).inflate(R.layout.reward_nick_dialog_layout, null);
		nickTV = (TextView) view.findViewById(R.id.reward_dialog_nick_tv);
		TextView nextNickBtn = (TextView) view.findViewById(R.id.reward_dialog_nick_next_tv);
		progress = (ProgressBar) view.findViewById(R.id.reward_dialog_progress);
		nickPastTv = (TextView) view.findViewById(R.id.reward_dialog_nick_past_tv);
		if (!TextUtils.isEmpty(toast)) {
			nickPastTv.setVisibility(View.VISIBLE);
			nickPastTv.setText(toast);
		} else {
			nickPastTv.setVisibility(View.GONE);
		}
		nickTV.setBackgroundDrawable(BaseUtils.createGradientDrawable(context.getResources().getColor(R.color.gray),
				context.getResources().getColor(R.color.gray), 8, context.getResources().getColor(R.color.gray)));
		nickTV.setText(nick);
		nextNickBtn.setBackgroundDrawable(BaseUtils.createGradientDrawable(context.getResources().getColor(R.color.red), context.getResources()
				.getColor(R.color.red), 8, context.getResources().getColor(R.color.red)));

		nextNickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				if (progress.getVisibility() == View.INVISIBLE) {
				if (isChangeNick) {
					return;
				}
				isBind = 1;
				progress.setVisibility(View.VISIBLE);
				getAnonymNickInfo();
				isChangeNick = true;
				//				}
			}
		});
	
		dialog = DialogFactory.showMsgDialog(context, "", view, false, context.getResources().getString(R.string.dialog_btn_confirm_str), "", null,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						//更换昵称过程中不能点击确定
						if (progress.getVisibility() == View.VISIBLE) {
							return;
						}
						DialogFactory.dimissDialog(dialog);
						userAnonymNickInfo(anonymNickId);
					}
				});

		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	@Override
	public void onGetUserAnonyNickSuccess(final int code, final String msg) {
		Message message = new Message();
		message.what = HandlerValue.USER_ANONYM_NICK_SUCCESS;
		message.arg1 = code;
		message.obj = msg;
		mHandler.sendMessage(message);
	}

	@Override
	public void onGetUserAnonyNickError(int code) {
		Message message = new Message();
		message.what = HandlerValue.USER_ANONYM_NICK_ERROR;
		message.arg1 = code;
		mHandler.sendEmptyMessage(code);
	}

	@Override
	public void onGetAnonyNickInfoSuccess(int code, String msg, final int id, final String nick) {
		AnonymNickInfo entity = new AnonymNickInfo();
		entity.setCode(code);
		entity.setMsg(msg);
		entity.setNickId(id);
		entity.setNick(nick);

		Message message = new Message();
		message.what = HandlerValue.GET_ANONYM_NICK_SUCCESS;
		message.obj = entity;

		mHandler.sendMessage(message);
	}

	@Override
	public void onGetAnonyNickInfoError(int code) {
		Message msg = new Message();
		msg.what = HandlerValue.GET_ANONYM_NICK_ERROR;
		msg.arg1 = code;

		mHandler.sendMessage(msg);
	}

	@Override
	public void onBindAnonymNickSuccess(int code, String msg, final int id, final String nick, int status) {
		BindAnonymNickEntity entity = new BindAnonymNickEntity();
		entity.setCode(code);
		entity.setMessage(msg);
		entity.setNickId(id);
		entity.setNick(nick);
		entity.setStatus(status);

		Message message = new Message();
		message.what = HandlerValue.BIND_ANONYM_NICK_REWARD_SUCCESS;
		message.obj = entity;

		mHandler.sendMessage(message);
	}

	@Override
	public void onBindAnonymNickError(int code) {
		Message message = new Message();
		message.what = HandlerValue.BIND_ANONYM_NICK_REWARD_ERROR;
		message.arg1 = code;
		mHandler.sendMessage(message);
	}

	public int getAnonymNickId() {
		return anonymNickId;
	}

	public String getAnonymNick() {
		return anonymNick;
	}
}
