package com.tshang.peipei.activity.reward;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.reward.adapter.RewardGiftGridViewAdapter;
import com.tshang.peipei.activity.reward.adapter.RewardSkillGridViewAdapter;
import com.tshang.peipei.activity.reward.adapter.RewardSkillGridViewAdapter.SkillCloseCallBack;
import com.tshang.peipei.activity.reward.adapter.RewardViewPageAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.reward.RewardRequestControl;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestRewardGiftInfo.GetRewardGiftInfoCallBack;
import com.tshang.peipei.model.request.RequestRewardPublish2.publishRewardCallBack;
import com.tshang.peipei.model.request.RequestRewardSkillInfo.GetRewardSkillInfoCallBack;
import com.tshang.peipei.model.request.RequestRewardTipInfo.GetRewardTipInfoCallBack;
import com.tshang.peipei.protocol.asn.gogirl.AwardGiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.AwardGiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.AwardTextInfo;
import com.tshang.peipei.protocol.asn.gogirl.AwardTextInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.view.RewardSkillGridView;

import de.greenrobot.event.EventBus;

/**
 * @Title: PublishRewardActivity.java 
 *
 * @Description: 发布悬赏
 *
 * @author Aaron  
 *
 * @date 2015-9-24 下午2:27:19 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class PublishRewardActivity extends BaseActivity implements GetRewardSkillInfoCallBack, GetRewardGiftInfoCallBack, GetRewardTipInfoCallBack,
		publishRewardCallBack, SkillCloseCallBack {

	private final String TAG = this.getClass().getSimpleName();

	private ViewPager mGiftViewPage;
	private TextView tipTv;
	private TextView giftSeleteTv;
	private TextView skillSeleteTv;
	private ProgressBar skillPro, giftPro;
	private RewardSkillGridView mSkillGridView;
	private TextView skillTitleTv;

	private RewardViewPageAdapter adapter;

	private RewardRequestControl control;

	private List<RewardSkillGridViewAdapter> skillGridViewAdapters = new ArrayList<RewardSkillGridViewAdapter>();
	private List<RewardGiftGridViewAdapter> giftGridViewAdapters = new ArrayList<RewardGiftGridViewAdapter>();
	private List<View> skillViews = new ArrayList<View>();
	private List<View> giftViews = new ArrayList<View>();

	private int skillId = -1, giftId = -1;

	private int type = 0;
	private String skillDesc = "";
	private String skill = "";

	private AwardTextInfo textInfo;
	private AwardGiftInfo giftInfo;

	private ImageView[] skillTips, giftTips;

	private DisplayImageOptions option;

	private Dialog dialog;

	private RewardSkillGridViewAdapter gridViewAdapter;

	public static int COSTOM_CODE = 332;

	private int anonymNickId;
	private String anonymNick;

	private AnonymNickDialog anonymNickDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		control = new RewardRequestControl();
		control.requestRewardSkillInfo(this);
		control.requestRewardGift(this);
		control.requestRewardTip(this);
		option = ImageOptionsUtils.getRewardGiftOptions(this);

		anonymNickId = getIntent().getExtras().getInt("nickId");
		anonymNick = getIntent().getExtras().getString("nick");
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {
		mTitle = (TextView) this.findViewById(R.id.title_tv_mid);
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setOnClickListener(this);
		mLinRight = (LinearLayout) this.findViewById(R.id.title_lin_right);
		mLinRight.setVisibility(View.VISIBLE);
		mLinRight.setOnClickListener(this);
		mTextRight = (TextView) this.findViewById(R.id.title_tv_right);
		mTextRight.setVisibility(View.VISIBLE);
		mTextRight.setText(R.string.publish);
		mTitle.setText(R.string.publish_reward_title);

		tipTv = (TextView) findViewById(R.id.reward_tip_tv);
		giftSeleteTv = (TextView) findViewById(R.id.reward_gift_selete_tv);
		skillSeleteTv = (TextView) findViewById(R.id.reward_sill_selete_tv);
		skillPro = (ProgressBar) findViewById(R.id.reward_skill_pro);
		giftPro = (ProgressBar) findViewById(R.id.reward_gift_pro);
		skillTitleTv = (TextView) findViewById(R.id.reward_skill_title);

		//		mSkillViewPage = (ViewPager) findViewById(R.id.reward_skill_viewpage);
		mGiftViewPage = (ViewPager) findViewById(R.id.reward_gift_viewpage);

		//		skillIndextLayout = (LinearLayout) findViewById(R.id.reward_skill_viewGroup);
		mSkillGridView = (RewardSkillGridView) findViewById(R.id.reward_skill_gridView);

		if (BAApplication.mLocalUserInfo.sex.intValue() == Gender.FEMALE.getValue()) {
			skillTitleTv.setText(R.string.reward_skill_man_title);
		} else {
			skillTitleTv.setText(R.string.reward_skill_woman_title);
		}
	}

	@Override
	protected int initView() {
		return R.layout.activity_new_reward;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initSkillView(AwardTextInfoList infoList) {
		gridViewAdapter = new RewardSkillGridViewAdapter(this, this);
		mSkillGridView.setAdapter(gridViewAdapter);
		ArrayList<AwardTextInfo> list = new ArrayList<AwardTextInfo>();
		for (int i = 0; i < infoList.size(); i++) {
			list.add((AwardTextInfo) infoList.get(i));
		}
		gridViewAdapter.appendToList(list);
		//自定义添加悬赏按钮
		if (list.get(0).revint0.intValue() != 0) {
			addCostomSkillBtn(gridViewAdapter);
		}
		mSkillGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				final AwardTextInfo entity = (AwardTextInfo) gridViewAdapter.getItem(position);
				if (entity.id.intValue() == -1) {//添加按钮
					Intent intent = new Intent(PublishRewardActivity.this, RewardCostomActivity.class);
					startActivityForResult(intent, COSTOM_CODE);
					overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
				} else {
					if (entity.id.intValue() == -2) {//自定义悬赏技能
						type = 1;
					} else {//系统配制悬赏
						type = 0;
					}
					View dialogView = LayoutInflater.from(PublishRewardActivity.this)
							.inflate(R.layout.reward_skill_selete_comfir_dialog_layout, null);
					TextView tv = (TextView) dialogView.findViewById(R.id.reward_skill_comfir_tv);
					TextView descTv = (TextView) dialogView.findViewById(R.id.reward_skill_desc_comfir_tv);
					descTv.setText(new String(entity.revstr0));
					tv.setText(new String(entity.content));
					dialog = DialogFactory.showMsgDialog(PublishRewardActivity.this, "", dialogView, "", "", null, new OnClickListener() {

						@Override
						public void onClick(View v) {
							gridViewAdapter.setClickPosition(position);
							skillId = entity.id.intValue();
							skillSeleteTv.setText("(" + getResources().getString(R.string.reward_seletor) + new String(entity.content) + ")");
							textInfo = entity;
							DialogFactory.dimissDialog(dialog);
						}
					});
				}
			}
		});
	}

	private void addCostomSkillBtn(RewardSkillGridViewAdapter gridViewAdapter) {
		AwardTextInfo info = new AwardTextInfo();
		info.content = "自定义".getBytes();
		info.fillcolor = "B1936E".getBytes();
		info.framecolor = "B1936E".getBytes();
		info.id = BigInteger.valueOf(-1);
		info.textcolor = "FFFFFF".getBytes();
		gridViewAdapter.appendToList(info);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent intent) {
		super.onActivityResult(arg0, arg1, intent);
		if (arg1 == arg0) {
			AwardTextInfo entity = (AwardTextInfo) gridViewAdapter.getList().get(gridViewAdapter.getCount() - 1);
			entity.id = BigInteger.valueOf(-2);
			entity.content = intent.getStringExtra("skill").getBytes();
			entity.revstr0 = intent.getStringExtra("skillDesc").getBytes();
			gridViewAdapter.setClickPosition(gridViewAdapter.getCount() - 1);
			skillSeleteTv.setText("(" + getResources().getString(R.string.reward_seletor) + intent.getStringExtra("skill") + ")");
			skill = intent.getStringExtra("skill");
			skillDesc = intent.getStringExtra("skillDesc");
			textInfo = entity;
			type = 1;
			skillId = -2;
		}
	}

	/**
	 * 技能适配View
	 * @author Aaron
	 *
	 */
	//	private void initSkillView(AwardTextInfoList infoList) {
	//		//		int count = (int) infoList.size() / 12;
	//		//		//计算分页显示
	//		//		if (infoList.size() % 12 > 0) {
	//		//			count += 1;
	//		//		}
	//		//		for (int i = 0; i < count; i++) {
	//		GridView skillGridView = new GridView(this);
	//		skillGridView.setNumColumns(4);
	//		skillGridView.setHorizontalSpacing(10);
	//		skillGridView.setVerticalSpacing(30);
	//		skillGridView.setSelector(android.R.color.transparent);
	//
	//		skillViews.add(skillGridView);
	//		final RewardSkillGridViewAdapter gridViewAdapter = new RewardSkillGridViewAdapter(this);
	//		skillGridView.setAdapter(gridViewAdapter);
	//		skillGridViewAdapters.add(gridViewAdapter);
	//		skillGridView.setOnItemClickListener(new OnItemClickListener() {
	//
	//			@Override
	//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	//				AwardTextInfo entity = (AwardTextInfo) gridViewAdapter.getItem(position);
	//				for (int j = 0; j < skillGridViewAdapters.size(); j++) {
	//					if (mSkillViewPage.getCurrentItem() == j) {
	//						skillGridViewAdapters.get(j).setClickPosition(position);
	//					} else {
	//						skillGridViewAdapters.get(j).setClickPosition(-1);
	//					}
	//				}
	//				skillId = entity.id.intValue();
	//				skillSeleteTv.setText("(" + getResources().getString(R.string.reward_seletor) + new String(entity.content) + ")");
	//				textInfo = entity;
	//			}
	//		});
	//
	//		List<AwardTextInfo> mList = new ArrayList<AwardTextInfo>();
	//		//计算每页显示个数
	//		//			int num = (i + 1) * 12;
	//		//			if (infoList.size() < num) {
	//		//				num = infoList.size();
	//		//			}
	//		for (int j = 0; j < infoList.size(); j++) {
	//			mList.add((AwardTextInfo) infoList.get(j));
	//		}
	//		gridViewAdapter.appendToList(mList);
	//		//		}
	//		adapter = new RewardViewPageAdapter(skillViews);
	//		mSkillViewPage.setAdapter(adapter);
	//		mSkillViewPage.setPageMargin(25);
	//		//		initViewPagerIndex(count);
	//		//		final int views = count;
	//		//		mSkillViewPage.setOnPageChangeListener(new OnPageChangeListener() {
	//		//
	//		//			@Override
	//		//			public void onPageSelected(int arg0) {
	//		//				/**
	//		//				 * 设置指引下标
	//		//				 */
	//		//				setImageBackground(arg0 % views);
	//		//			}
	//		//
	//		//			@Override
	//		//			public void onPageScrolled(int arg0, float arg1, int arg2) {
	//		//				// TODO Auto-generated method stub
	//		//
	//		//			}
	//		//
	//		//			@Override
	//		//			public void onPageScrollStateChanged(int arg0) {
	//		//				// TODO Auto-generated method stub
	//		//
	//		//			}
	//		//		});
	//	}

	/**
	 * 礼物适配View
	 * @author Aaron
	 *
	 */
	private void initGiftView(AwardGiftInfoList infoList) {
		//计算分页显示
		int count = (int) infoList.size() / 3;
		if (infoList.size() % 3 > 0) {
			count += 1;
		}
		for (int i = 0; i < count; i++) {
			GridView gridView = new GridView(this);
			gridView.setNumColumns(3);
			gridView.setHorizontalSpacing(60);
			gridView.setSelector(android.R.color.transparent);

			giftViews.add(gridView);
			final RewardGiftGridViewAdapter giftGridViewAdapter = new RewardGiftGridViewAdapter(this);
			giftGridViewAdapters.add(giftGridViewAdapter);
			gridView.setAdapter(giftGridViewAdapter);

			//计算每页显示个数
			int num = (i + 1) * 3;
			if (infoList.size() < num) {
				num = infoList.size();
			}
			List<AwardGiftInfo> giftList = new ArrayList<AwardGiftInfo>();
			for (int j = i * 3; j < num; j++) {
				giftList.add((AwardGiftInfo) infoList.get(j));
			}
			giftGridViewAdapter.appendToList(giftList);

			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					AwardGiftInfo entity = (AwardGiftInfo) giftGridViewAdapter.getItem(position);
					for (int j = 0; j < giftGridViewAdapters.size(); j++) {
						if (mGiftViewPage.getCurrentItem() == j) {
							giftGridViewAdapters.get(j).setClickPosition(position);
						} else {
							giftGridViewAdapters.get(j).setClickPosition(-1);
						}
					}
					giftId = entity.id.intValue();
					giftSeleteTv.setText("(" + getResources().getString(R.string.reward_seletor) + new String(entity.name) + ")");
					giftInfo = entity;
				}
			});
		}
		adapter = new RewardViewPageAdapter(giftViews);
		mGiftViewPage.setAdapter(adapter);
		mGiftViewPage.setPageMargin(65);
	}

	//	private void initViewPagerIndex(int count) {
	//		skillTips = new ImageView[count];
	//		for (int i = 0; i < count; i++) {
	//			// 初始化ImageView
	//			ImageView imageView = new ImageView(this);
	//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	//			params.setMargins(10, 30, 10, 0);
	//			imageView.setLayoutParams(params);
	//			skillTips[i] = imageView;
	//			if (i == 0) {
	//				skillTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
	//			} else {
	//				skillTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
	//			}
	//			skillIndextLayout.addView(imageView);
	//		}
	//	}

	//	/**
	//	 * 设置选中的tip的背景
	//	 * 
	//	 * @param selectItems
	//	 */
	//	private void setImageBackground(int selectItems) {
	//		for (int i = 0; i < skillTips.length; i++) {
	//			if (i == selectItems) {
	//				skillTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
	//			} else {
	//				skillTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
	//			}
	//		}
	//	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_lin_right://发布
			if (skillId == -1) {
				BaseUtils.showTost(this, R.string.reward_skill_null);
				return;
			}
			if (giftId == -1) {
				BaseUtils.showTost(this, R.string.reward_gift_null);
				return;
			}
			View view = LayoutInflater.from(this).inflate(R.layout.reward_pulish_affirm_layout, null);
			TextView content = (TextView) view.findViewById(R.id.reward_comfir_content);
			TextView gift_name = (TextView) view.findViewById(R.id.reward_comfir_giftName);
			TextView glod = (TextView) view.findViewById(R.id.reward_comfir_glod);
			TextView integral = (TextView) view.findViewById(R.id.reward_comfir_integral);
			TextView charm = (TextView) view.findViewById(R.id.reward_comfir_charm);
			ImageView iv = (ImageView) view.findViewById(R.id.reward_comfir_gift_iv);
			TextView titleTv = (TextView) view.findViewById(R.id.reward_comfir_title);
			TextView checkBoxText = (TextView) view.findViewById(R.id.reward_checkbox_text);
			final CheckBox checkBox = (CheckBox) view.findViewById(R.id.reward_confirm_checkbox);
			final TextView anonymExplan = (TextView) view.findViewById(R.id.reward_public_anonym_toast_tv);
			if (anonymNickId == AnonymNickDialog.REWARD_SWITCH) {
				checkBox.setVisibility(View.GONE);
				anonymExplan.setVisibility(View.GONE);
				checkBoxText.setVisibility(View.GONE);
			}
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						anonymExplan.setVisibility(View.VISIBLE);
					} else {
						anonymExplan.setVisibility(View.GONE);
					}
				}
			});
			if (BAApplication.mLocalUserInfo.sex.intValue() == Gender.FEMALE.getValue()) {
				titleTv.setText(R.string.reward_affirm_skill_woman_title);
			} else {
				titleTv.setText(R.string.reward_affirm_skill_man_tile);
			}
			if (giftInfo == null || textInfo == null) {
				return;
			}
			imageLoader.displayImage("http://" + new String(giftInfo.pickey) + BAConstants.LOAD_180_APPENDSTR, iv, option);
			content.setBackgroundDrawable(BaseUtils.createGradientDrawable(1, Color.parseColor("#" + new String(textInfo.fillcolor)), 180,
					Color.parseColor("#" + new String(textInfo.fillcolor))));
			if (textInfo.id.intValue() == -2) {
				content.setTextColor(Color.WHITE);
			} else {
				content.setTextColor(Color.parseColor("#" + new String(textInfo.textcolor)));
			}
			content.setText(new String(textInfo.content));
			gift_name.setText(new String(giftInfo.name));
			if (giftInfo.pricegold.intValue() > 0) {
				glod.setText(giftInfo.pricegold.intValue() + getResources().getString(R.string.gold_money));
			} else {
				glod.setText(giftInfo.pricesilver.intValue() + getResources().getString(R.string.silver_money));
			}
			integral.setText(giftInfo.scoreeffect.intValue() + "");
			charm.setText(giftInfo.charmeffect.intValue() + "");

			dialog = DialogFactory.showMsgDialog(this, null, view, "", getResources().getString(R.string.reward_slete_age), null,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (anonymNickDialog != null) {
								anonymNickId = anonymNickDialog.getAnonymNickId();
								anonymNick = anonymNickDialog.getAnonymNick();
							}
							DialogFactory.dimissDialog(dialog);
							dialog = DialogFactory.createLoadingDialog(PublishRewardActivity.this, R.string.publish_loading);
							DialogFactory.showDialog(dialog);
							//如果是发布匿名悬赏，传匿名Nick id
							int id = -1;
							if (checkBox.isChecked() && anonymNickId != AnonymNickDialog.REWARD_SWITCH) {
								id = anonymNickId;
							} else {
								id = BAApplication.mLocalUserInfo.uid.intValue();
							}

							int anonym = 1;
							if (anonymNickId != AnonymNickDialog.REWARD_SWITCH && checkBox.isChecked()) {
								anonym = 1;
							} else {
								anonym = 0;
							}
							control.publishReward(id, type, skill, skillDesc, skillId, giftId, anonym, PublishRewardActivity.this);
						}
					});
			DialogFactory.showDialog(dialog);
			break;

		default:
			break;
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_REWARD_SKILL_SUCCESS://技能成功回调
			skillPro.setVisibility(View.GONE);
			if (msg.arg1 == 0) {
				AwardTextInfoList infoList = (AwardTextInfoList) msg.obj;
				initSkillView(infoList);
			}
			break;
		case HandlerValue.GET_REWARD_SKILL_ERROR:
			skillPro.setVisibility(View.GONE);
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.GET_REWARD_GIFT_SUCCESS://礼物回调成功
			giftPro.setVisibility(View.GONE);
			if (msg.arg1 == 0) {
				AwardGiftInfoList infoList = (AwardGiftInfoList) msg.obj;
				initGiftView(infoList);
			}
			break;
		case HandlerValue.GET_REWARD_GIFT_ERROR:
			giftPro.setVisibility(View.GONE);
			break;
		case HandlerValue.GET_REWARD_TIP_SUCCESS://提示语回调成功
			if (msg.arg1 == 0) {
				GoGirlDataInfoList infoList = (GoGirlDataInfoList) msg.obj;
				BAParseRspData parse = null;
				for (int i = 0; i < infoList.size(); i++) {
					parse = new BAParseRspData();
					ContentData data = parse.parseTopicInfo(this, infoList, 0);
					tipTv.setText(data.getContent().trim() + "\n");
				}
			}
			break;
		case HandlerValue.GET_REWARD_TIP_ERROR:

			break;
		case HandlerValue.PULISH_REWARD_SUCCESS:
			DialogFactory.dimissDialog(dialog);
			if (msg.arg1 == 0) {
				NoticeEvent n = new NoticeEvent();
				n.setFlag(NoticeEvent.NOTICE94);
				EventBus.getDefault().post(n);
				this.finish();
			} else if (msg.arg1 == -28021) {
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (msg.arg1 == -28076) {
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			} else if (msg.arg1 == -28010) {
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE27);
				EventBus.getDefault().post(event);
			} else if (msg.arg1 == -28099) {
				dialog = DialogFactory.warnMsgDialog(this, null, msg.obj.toString(), getResources().getString(R.string.dialog_confirm));
			} else if (msg.arg1 == -28077) {
				DialogFactory.warnMsgDialog(this, null, msg.obj.toString(), getResources().getString(R.string.dialog_confirm));
			} else if (msg.arg1 == -28102) {
				DialogFactory.warnMsgDialog(this, null, msg.obj.toString(), getResources().getString(R.string.dialog_confirm));
			} else if (msg.arg1 == -28105) {//预留错误字段
				DialogFactory.warnMsgDialog(this, null, msg.obj.toString(), getResources().getString(R.string.dialog_confirm));
			} else if (msg.arg1 == -21001) {
				anonymNickDialog = new AnonymNickDialog(this);
				anonymNickDialog.bindAnonymNick();
			} else {
				BaseUtils.showTost(this, R.string.publist_reward_failure);
			}
			break;
		case HandlerValue.PULISH_REWARD_ERROR:
			DialogFactory.dimissDialog(dialog);
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;

		default:
			break;
		}
	}

	@Override
	public void onSkillSuccess(int code, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_REWARD_SKILL_SUCCESS, code, obj);
	}

	@Override
	public void onSillError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.GET_REWARD_SKILL_ERROR, code);
	}

	@Override
	public void onGiftSuccess(int code, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_REWARD_GIFT_SUCCESS, code, obj);
	}

	@Override
	public void onGiftError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.GET_REWARD_GIFT_ERROR, code);
	}

	@Override
	public void onTipSuccess(int code, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_REWARD_TIP_SUCCESS, code, obj);
	}

	@Override
	public void onTipError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.GET_REWARD_TIP_ERROR, code);
	}

	@Override
	public void publishOnSuccess(int code, String msg) {
		sendHandlerMessage(mHandler, HandlerValue.PULISH_REWARD_SUCCESS, code, msg);
	}

	@Override
	public void publishOnError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.PULISH_REWARD_ERROR, code);
	}

	@Override
	public void close() {
		gridViewAdapter.getList().remove(gridViewAdapter.getCount() - 1);
		addCostomSkillBtn(gridViewAdapter);
		gridViewAdapter.setClickPosition(-1);
		skillSeleteTv.setText(R.string.reward_skill_seletor);
		gridViewAdapter.notifyDataSetChanged();
	}
}
