package com.tshang.peipei.activity.skillnew;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.reward.adapter.RewardGiftGridViewAdapter;
import com.tshang.peipei.activity.reward.adapter.RewardViewPageAdapter;
import com.tshang.peipei.activity.skillnew.adapter.SkillInviteGiftAdapter;
import com.tshang.peipei.activity.skillnew.adapter.SkillViewPageAdapter;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestGetSkillGiftList.GetSkillGiftListCallBack;
import com.tshang.peipei.model.request.RequestGetSkillTipInfo.GetSkillInfoCallBack;
import com.tshang.peipei.model.request.RequestSndSkillInvite.SendSkillInviteCallBack;
import com.tshang.peipei.model.skillnew.GoddessSkillEngine;
import com.tshang.peipei.protocol.asn.gogirl.AwardGiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.AwardGiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.SkillGiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillGiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfo;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.view.HorizontalListView;

/**
 * @Title: SkillInviteActivity.java 
 *
 * @Description: 女神技邀请 
 *
 * @author DYH  
 *
 * @date 2015-10-29 下午3:06:53 
 *
 * @version V1.0   
 */
public class SkillInviteActivity extends BaseActivity implements GetSkillGiftListCallBack, GetSkillInfoCallBack, SendSkillInviteCallBack {
	private SkillTextInfo info;
	private TextView tv_skill;
	private TextView title_tv_right;
	private ViewPager mGiftViewPage;
	private SkillViewPageAdapter adapter;
	private TextView tv_gift_name;
	private EditText et_postscript;
	private TextView tv_postscript_count;
	private TextView tv_tip;
	private TextView tv_skill_des;
	private GoddessSkillEngine skillEngine;
	private static final int POSTSCRIPT_COUNT = 50;
	private int mSex;
	private int mFriendUid; //客人UID
	private String mNick;
	private List<View> giftViews = new ArrayList<View>();
	private int giftId = -1;
	private SkillGiftInfo giftInfo;
	private List<SkillInviteGiftAdapter> giftGridViewAdapters = new ArrayList<SkillInviteGiftAdapter>();
	

	@Override
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mFriendUid = bundle.getInt("mFriendUid");
			mSex = bundle.getInt("sex");
			mNick = bundle.getString("mNick");
		}
		info = BAApplication.getInstance().getSkillTextInfo();
		skillEngine = new GoddessSkillEngine();
		if (info != null) {
			tv_skill.setBackgroundDrawable(BaseUtils.createGradientDrawable(2, Color.parseColor("#" + new String(info.framecolor)), 180,
					getResources().getColor(R.color.transparent)));
			tv_skill.setText(new String(info.content));
			tv_skill.setTextColor(Color.parseColor("#" + new String(info.textcolor)));
			tv_skill_des.setText(new String(info.desc));
		}
		getGiftList();
		getTipInfo();
	}

	private void getGiftList() {
		skillEngine.requestGetSkillGiftList(mSex, mFriendUid, this);
	}
	
	private void getTipInfo(){
		skillEngine.requestGetSkillTipInfo(mSex, mFriendUid, this);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.GET_GODDESS_SKILL_GIFT_LIST_SUCCESS:
			if (msg.arg1 == 0) {
				SkillGiftInfoList list = (SkillGiftInfoList) msg.obj;
				if (!ListUtils.isEmpty(list)) {
					initGiftView(list);
				}
			}
			break;
		case HandlerValue.GET_GODDESS_SKILL_GIFT_LIST_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.GET_GODDESS_SKILL_TIP_INFO_SUCCESS:
			if(msg.arg1 == 0){
				GoGirlDataInfoList list = (GoGirlDataInfoList) msg.obj;
				if(list != null){
					BAParseRspData parser = new BAParseRspData();
					for (int i = 0; i < list.size(); i++) {
						ContentData data = parser.parseTopicInfo(this, list, 0);
						tv_tip.setText(data.getContent().trim());
					}
				}
			}
			break;
		case HandlerValue.GET_GODDESS_SKILL_TIP_INFO_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.GET_GODDESS_SKILL_INVITE_SUCCESS:
			DialogFactory.dimissDialog();
			int retCode = msg.arg1;
			if(retCode == 0){
				ChatActivity.intentChatActivity(this, mFriendUid, mNick, mSex, false, false,
						0);
				finish();
			}else if (retCode == rspContMsgType.E_GG_PROPERTY_LACK) {//财富不足
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			}else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			}else if(msg.arg1 == -28013){
				BaseUtils.showTost(this, R.string.str_skill_info_no_exits);
			}else if(msg.arg1 == -28020){
				BaseUtils.showTost(this, R.string.str_gift_no_exits);
			}
			break;
		case HandlerValue.GET_GODDESS_SKILL_INVITE_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		}
	}

	@Override
	protected void initRecourse() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		tv_skill = (TextView) findViewById(R.id.tv_skill);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_goddess_skill_invite);
		title_tv_right = (TextView) findViewById(R.id.title_tv_right);
		title_tv_right.setVisibility(View.VISIBLE);
		title_tv_right.setText(R.string.str_send_invite);
		mGiftViewPage = (ViewPager) findViewById(R.id.skill_gift_viewpage);
		tv_gift_name = (TextView) findViewById(R.id.tv_gift_name);
		et_postscript = (EditText) findViewById(R.id.et_postscript);
		tv_postscript_count = (TextView) findViewById(R.id.tv_postscript_count);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		tv_skill_des = (TextView) findViewById(R.id.tv_skill_des);
		setListener();
	}
	
	/**
	 * 礼物适配View
	 * @author Aaron
	 *
	 */
	private void initGiftView(SkillGiftInfoList infoList) {
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
			final SkillInviteGiftAdapter giftGridViewAdapter = new SkillInviteGiftAdapter(this);
			giftGridViewAdapters.add(giftGridViewAdapter);
			gridView.setAdapter(giftGridViewAdapter);

			//计算每页显示个数
			int num = (i + 1) * 3;
			if (infoList.size() < num) {
				num = infoList.size();
			}
			List<SkillGiftInfo> giftList = new ArrayList<SkillGiftInfo>();
			for (int j = i * 3; j < num; j++) {
				giftList.add((SkillGiftInfo) infoList.get(j));
			}
			giftGridViewAdapter.appendToList(giftList);

			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					SkillGiftInfo entity = (SkillGiftInfo) giftGridViewAdapter.getItem(position);
					for (int j = 0; j < giftGridViewAdapters.size(); j++) {
						if (mGiftViewPage.getCurrentItem() == j) {
							giftGridViewAdapters.get(j).setClickPosition(position);
						} else {
							giftGridViewAdapters.get(j).setClickPosition(-1);
						}
					}
					giftId = entity.id.intValue();
					tv_gift_name.setText("(" + getResources().getString(R.string.reward_seletor) + new String(entity.name) + ")");
					giftInfo = entity;
				}
			});
		}
		adapter = new SkillViewPageAdapter(giftViews);
		mGiftViewPage.setAdapter(adapter);
		mGiftViewPage.setPageMargin(65);
	}

	private void setListener() {
		title_tv_right.setOnClickListener(this);

		et_postscript.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				tv_postscript_count.setText(getBodyLength() + "/" + POSTSCRIPT_COUNT);
			}
		});
	}

	/**
	 * 获取输入内容的长度
	 * 
	 * @return
	 */
	private int getBodyLength() {
		String body = et_postscript.getText().toString();
		if (body == null || body.trim().length() == 0) {
			return 0;
		}
		return body.length();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_tv_right:
			sendInvite();
			break;
		}
	}
	
	private void sendInvite(){
		if (info == null) {
			BaseUtils.showTost(this, R.string.str_please_select_skill);
			return;
		}
		if (giftId == -1) {
			BaseUtils.showTost(this, R.string.str_please_select_skill_gift);
			return;
		}
		Dialog dialog = DialogFactory.createLoadingDialog(this, "");
		DialogFactory.showDialog(dialog);
		String additionalword = et_postscript.getText().toString();
		if(additionalword == null){
			additionalword = "";
		}
		skillEngine.requestSkillInvite(mFriendUid, info.id.intValue(), giftId, additionalword, this);
	}

	public static void openMineFaqActivity(int mFriendUid, int mSex, String mNick, Activity activity) {
		Bundle bundle = new Bundle();
		bundle.putInt("mFriendUid", mFriendUid);
		bundle.putInt("sex", mSex);
		bundle.putString("mNick", mNick);
		BaseUtils.openActivity(activity, SkillInviteActivity.class, bundle);
	}

	@Override
	protected int initView() {
		return R.layout.activity_skill_invite;
	}

	@Override
	public void getSkillGiftListOnSuccess(int resCode, Object object) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_GIFT_LIST_SUCCESS, resCode, object);
	}

	@Override
	public void getSkillGiftListOnError(int resCode) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_GIFT_LIST_SUCCESS, resCode);
	}

	@Override
	public void getSkillInfoOnSuccess(int resCode, Object object) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_TIP_INFO_SUCCESS, resCode, object);
	}

	@Override
	public void getSkillInfoOnError(int resCode) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_TIP_INFO_ERROR, resCode);
	}

	@Override
	public void sendSkillInviteOnSuccess(int resCode, Object object) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_INVITE_SUCCESS, resCode, object);
	}

	@Override
	public void sendSkillInviteOnError(int resCode) {
		sendHandlerMessage(mHandler, HandlerValue.GET_GODDESS_SKILL_INVITE_ERROR, resCode);
	}

}
