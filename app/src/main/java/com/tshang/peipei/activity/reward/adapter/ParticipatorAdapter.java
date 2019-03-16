package com.tshang.peipei.activity.reward.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.biz.reward.RewardRequestControl;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.model.request.RequestSendParticipator.GetSendRewardParticipatorCallBack;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.ParticipateInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: ParticipatorAdapter.java 
 *
 * @Description: 参加列表适配器
 *
 * @author Aaron  
 *
 * @date 2015-9-29 下午4:47:13 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class ParticipatorAdapter extends ArrayListAdapter<ParticipateInfo> implements GetSendRewardParticipatorCallBack {

	private DisplayImageOptions head_option;

	private DisplayImageOptions gradeInfoOptions;

	private int type, awarduid, awardid;

	private Handler mHandler;

	private Dialog dialog;

	private int issndaward;

	private int uid;

	private int anonym;

	public ParticipatorAdapter(Activity context, Handler handler, int type, int awarduid, int awardid, int anonym) {
		super(context);
		head_option = ImageOptionsUtils.GetHeadKeyBigRounded(context);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);

		this.type = type;
		this.awarduid = awarduid;
		this.awardid = awardid;
		this.mHandler = handler;
		this.anonym = anonym;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.participator_item_layout, null);
			viewHolder.lineTv = (TextView) convertView.findViewById(R.id.participator_line_tv);
			viewHolder.participatorBtn = (TextView) convertView.findViewById(R.id.participator_item_btn);
			viewHolder.headIv = (ImageView) convertView.findViewById(R.id.participator_item_head_iv);
			viewHolder.nickTv = (TextView) convertView.findViewById(R.id.participator_item_nick_iv);
			viewHolder.manTv = (TextView) convertView.findViewById(R.id.participator_man_tv);
			viewHolder.womanTv = (TextView) convertView.findViewById(R.id.participator_woman_tv);
			viewHolder.lableIv = (ImageView) convertView.findViewById(R.id.participator_label_iv);
			viewHolder.lastLine = (TextView) convertView.findViewById(R.id.participator_last_line);
			viewHolder.chatTv = (TextView) convertView.findViewById(R.id.participator_item_chat_tv);
			viewHolder.bottomLayout = (LinearLayout) convertView.findViewById(R.id.participator_item_bottom_layout);
			viewHolder.line=(View)convertView.findViewById(R.id.participator_item_line);
			viewHolder.guidIv=(ImageView)convertView.findViewById(R.id.participator_item_flag_iv);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final GoGirlUserInfo userInfo = mList.get(position).userinfo;
		if (position == 0 && mList.size() > 0) {
			viewHolder.lineTv.setVisibility(View.INVISIBLE);
		}
		String key = userInfo.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + key, viewHolder.headIv, head_option);
		String mBirthday = new String(BaseTimes.getFormatTime(userInfo.birthday.longValue() * 1000));
		String age = OperateViewUtils.calculateAge(mBirthday);
		age = age.substring(0, age.length() - 1);
		//显示备注
		String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				userInfo.uid.intValue());
		viewHolder.nickTv.setText(TextUtils.isEmpty(alias) ? new String(userInfo.nick) : alias);
		if (userInfo.sex.intValue() == Gender.MALE.getValue()) {
			viewHolder.manTv.setVisibility(View.VISIBLE);
			viewHolder.womanTv.setVisibility(View.GONE);
			viewHolder.manTv.setText(age);
			if (anonym == 1) {
				viewHolder.headIv.setImageResource(R.drawable.dynamic_defalut_man);
			}
		} else {
			viewHolder.womanTv.setVisibility(View.VISIBLE);
			viewHolder.manTv.setVisibility(View.GONE);
			viewHolder.womanTv.setText(age);
			if (anonym == 1) {
				viewHolder.headIv.setImageResource(R.drawable.dynamic_defalut_woman);
			}
		}

		String gradeinfo = new String(userInfo.gradeinfo);
		if (!TextUtils.isEmpty(gradeinfo)) {
			viewHolder.lableIv.setVisibility(View.VISIBLE);
			GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, viewHolder.lableIv, gradeInfoOptions);
		} else {
			viewHolder.lableIv.setVisibility(View.GONE);
		}

		if (position == mList.size() - 1) {
			viewHolder.lastLine.setVisibility(View.VISIBLE);
		} else {
			viewHolder.lastLine.setVisibility(View.GONE);
		}

		if (userInfo.sex.intValue() == Gender.FEMALE.getValue()) {
			//			viewHolder.participatorBtn.setText(R.string.reward_woman_str);
			viewHolder.manTv.setVisibility(View.GONE);
			viewHolder.womanTv.setVisibility(View.VISIBLE);
		} else {
			//			viewHolder.participatorBtn.setText(R.string.reward_man_str);
			viewHolder.manTv.setVisibility(View.VISIBLE);
			viewHolder.womanTv.setVisibility(View.GONE);
		}
		if (issndaward == 1) {//没有悬赏
			viewHolder.participatorBtn.setBackgroundColor(Color.parseColor("#d2d2d2"));
			if (anonym == 1) {//匿名悬赏结束 私聊按钮变灰
				viewHolder.chatTv.setBackgroundColor(Color.parseColor("#d2d2d2"));
				viewHolder.line.setVisibility(View.VISIBLE);
			}
		}

		if (position == clickPosition) {
			viewHolder.bottomLayout.setVisibility(View.VISIBLE);
			viewHolder.guidIv.setImageResource(R.drawable.reward_list_reve_icon);
		} else {
			viewHolder.bottomLayout.setVisibility(View.GONE);
			viewHolder.guidIv.setImageResource(R.drawable.reward_list_arow_icon);
		}

		viewHolder.participatorBtn.setOnClickListener(new onItemClick(userInfo));
		viewHolder.headIv.setOnClickListener(new onItemClick(userInfo));
		viewHolder.chatTv.setOnClickListener(new onItemClick(userInfo));

		return convertView;
	}

	private class ViewHolder {
		private TextView lineTv;
		private TextView participatorBtn;
		private ImageView headIv;
		private TextView nickTv;
		private TextView manTv, womanTv;
		private ImageView lableIv;
		private TextView lastLine;
		private TextView chatTv;
		private LinearLayout bottomLayout;
		private View line;
		private ImageView guidIv;
	}

	/**
	 * 点击事件
	 * @author Aaron
	 *
	 */
	private class onItemClick implements OnClickListener {

		private GoGirlUserInfo userInfo;

		public onItemClick(GoGirlUserInfo userInfo) {
			this.userInfo = userInfo;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.participator_item_btn:
				if (issndaward == 1) {
					return;
				}
				View view = LayoutInflater.from(mContext).inflate(R.layout.pariticator_confirm_dialog_layout, null);
				TextView nickTv = (TextView) view.findViewById(R.id.pariticator_dialog_nice_tv);
				SpannableStringBuilder s = new SpannableStringBuilder("确定选择");
				SpannableStringBuilder ss = new SpannableStringBuilder(new String(userInfo.nick));
				ss.setSpan(new ForegroundColorSpan(Color.RED), 0, new String(userInfo.nick).length(), 0);
				ss.setSpan(new RelativeSizeSpan(1.1f), 0, new String(userInfo.nick).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				SpannableStringBuilder sss = new SpannableStringBuilder("为获得悬赏者吗?");
				s.append(ss);
				s.append(sss);
				nickTv.setText(s);
				uid = userInfo.uid.intValue();
				dialog = DialogFactory.showMsgDialog(mContext, null, view, null, null, null, new OnClickListener() {

					@Override
					public void onClick(View v) {
						DialogFactory.dimissDialog(dialog);

						dialog = DialogFactory.createLoadingDialog(mContext, R.string.rewarding_dialog);
						DialogFactory.showDialog(dialog);
						RewardRequestControl control = new RewardRequestControl();
						control.requestParticipator(type, awarduid, userInfo.uid.intValue(), awardid, 2, anonym, ParticipatorAdapter.this);
					}
				});
				break;
			case R.id.participator_item_head_iv:
				if (anonym == 1) {//匿名不做跳转
					return;
				}
				SpaceUtils.toSpaceCustom((Activity) mContext, userInfo.uid.intValue(), userInfo.sex.intValue());
				break;
			case R.id.participator_item_chat_tv:
				if (anonym == 1 ) {
					if (issndaward!=1) {
						ChatActivity.intentChatActivity(mContext, userInfo.uid.intValue(), new String(userInfo.nick), userInfo.sex.intValue(), false,
								false, RewardListActivity.CHAT_FROM_REWARD);	
					}
				} else {
					ChatActivity.intentChatActivity(mContext, userInfo.uid.intValue(), new String(userInfo.nick), userInfo.sex.intValue(), false,
							false, 0);
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onSendParticipatorSuccess(int code) {
		DialogFactory.dimissDialog(dialog);
		Message msg = new Message();
		msg.what = HandlerValue.PARTICIPATOR_SUCCESS;
		msg.arg1 = code;
		msg.arg2 = uid;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onSendParticipatorError(int code) {
		DialogFactory.dimissDialog(dialog);
		Message msg = new Message();
		msg.what = HandlerValue.PARTICIPATOR_ERROR;
		msg.arg1 = code;
		mHandler.sendMessage(msg);

	}

	public void setIssndaward(int issndaward) {
		this.issndaward = issndaward;
		notifyDataSetChanged();
	}

	private int clickPosition = 0;

	public void setClickPosition(int position) {
		this.clickPosition = position;
	}
}
