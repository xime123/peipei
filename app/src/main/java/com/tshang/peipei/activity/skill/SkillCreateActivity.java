package com.tshang.peipei.activity.skill;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.dialog.SelectGiftDialog;
import com.tshang.peipei.activity.dialog.SelectGiftDialog.IGiftInfoItemOnClickCallBack;
import com.tshang.peipei.activity.dialog.SelectGiftNumDialog;
import com.tshang.peipei.activity.dialog.SelectGiftNumDialog.IGiftInfoNumCallBack;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.model.biz.baseviewoperate.SkillUtils;
import com.tshang.peipei.model.biz.space.SkillUtilsBiz;
import com.tshang.peipei.model.request.RequestAddSkill.IAddSkill;
import com.tshang.peipei.model.request.RequestEditSkill.IEditSkill;
import com.tshang.peipei.model.request.RequestGetGiftList.IGetGiftListCallBack;
import com.tshang.peipei.vender.common.util.ListUtils;

/**
 * 添加技能
 * @author Jeff
 *
 */
public class SkillCreateActivity extends BaseActivity implements IAddSkill, IGetGiftListCallBack, IGiftInfoItemOnClickCallBack, IGiftInfoNumCallBack,
		IEditSkill {
	private TextView tvSkillName;
	private EditText edt_skill;
	private EditText edt_describe;
	private TextView tv_gifts_type;
	private TextView tv_gifts_count;
	private TextView tv_limit;
	private int giftsId = -1;//默认选择礼物的id，-1代表未选礼物
	private int giftsCount = 1;//默认为1个礼物
	private static final int GET_GIFT_LIST = 0x10;
	private static final int ADD_SKILL = 0x11;
	private SelectGiftDialog dialog;
	private boolean isUpdateSkill = false;//默认为新建技能
	private int skillId = -1;
	private int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		int retCode = -1;
		switch (msg.what) {
		case GET_GIFT_LIST:
			retCode = msg.arg1;
			if (retCode == 0) {
				GiftInfoList lists = (GiftInfoList) msg.obj;
				if (!ListUtils.isEmpty(lists)) {
					dialog = new SelectGiftDialog(this, android.R.style.Theme_Translucent_NoTitleBar, lists, SkillCreateActivity.this);
					dialog.showDialog(0, 0);
				}
			} else {
				BaseUtils.showTost(SkillCreateActivity.this, R.string.str_get_gift_error);
			}
			break;
		case ADD_SKILL:
			retCode = msg.arg1;
			if (retCode == 0) {
				if (isUpdateSkill) {
					SkillUtils.resultEditSkill(SkillCreateActivity.this, edt_skill.getText().toString(), edt_describe.getText().toString(),
							tv_gifts_type.getText().toString(), giftsCount, giftsId);
				} else {
					setResult(RESULT_OK);
				}
				SkillCreateActivity.this.finish();
			} else if (retCode == -28042) {//私聊门槛比设置的礼物得到的魅力贡献值还高
				HintToastDialog dialog = new HintToastDialog(this, R.string.str_add_skill_failed, R.string.str_i_know);
				dialog.showDialog();
			} else if (retCode == rspContMsgType.E_GG_FORBIT) {
				new HintToastDialog(this, R.string.limit_talk, R.string.ok).showDialog();
			} else if (retCode == rspContMsgType.E_GG_PROPERTY_LACK) {//财富不足
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (retCode == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够银币
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			} else {
				BaseUtils.showTost(SkillCreateActivity.this, R.string.str_submit_failed);
			}
			break;
		}

	}

	@Override
	protected void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			mTitle.setText(R.string.str_edit_skill);
			edt_skill.setText(bundle.getString(SkillUtils.SKILL_TITLE));
			edt_describe.setText(bundle.getString(SkillUtils.SKILL_DESCRIBE));
			giftsId = bundle.getInt(SkillUtils.SKILL_GIFT_ID);
			giftsCount = bundle.getInt(SkillUtils.SKILL_GIFT_NUM);
			type = bundle.getInt(SkillUtils.SKILL_USER_TYPE);
			if (BAApplication.mLocalUserInfo != null) {
				type = BAApplication.mLocalUserInfo.sex.intValue();
			}
			if (type == 1) {//男的将技能改成打赏
				mTitle.setText(R.string.str_edit_enjoy);
				tvSkillName.setText(R.string.str_enjoy);
			}
			isUpdateSkill = bundle.getBoolean(SkillUtils.IS_ADD_OR_UPDATE_SKILL);
			tv_gifts_type.setText(bundle.getString(SkillUtils.SKILL_GIFT_NAME));
			tv_gifts_count.setText(String.valueOf(giftsCount));
			skillId = bundle.getInt(SkillUtils.SKILL_ID);
			tv_gifts_type.setClickable(false);
			tv_gifts_count.setClickable(false);
			int padingLeft = BaseUtils.dip2px(this, getResources().getDimension(R.dimen.default_padding_view_edge));
			tv_gifts_type.setBackgroundResource(R.drawable.main_img_list0_pr);
			tv_gifts_type.setPadding(padingLeft, 0, 0, 0);
			tv_gifts_count.setBackgroundResource(R.drawable.main_img_list0_pr);
			tv_gifts_count.setPadding(padingLeft, 0, 0, 0);
		} else {
			if (BAApplication.mLocalUserInfo != null) {
				type = BAApplication.mLocalUserInfo.sex.intValue();
			}
			if (type == 1) {//男的将技能改成打赏
				mTitle.setText("新建打赏");
				tvSkillName.setText(R.string.str_enjoy);
			}
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_skill_submit:
			String strSkillTitle = edt_skill.getText().toString();
			String strSkillDescribe = edt_describe.getText().toString();
			SkillUtilsBiz biz = new SkillUtilsBiz(this);
			if (!isUpdateSkill) {
				biz.getAddSkillCallBack(giftsId, giftsCount, strSkillDescribe, strSkillTitle, this);
			} else {
				biz.editSkill(strSkillTitle, giftsCount, strSkillDescribe, giftsId, skillId, this);
			}
			break;
		case R.id.tv_gifts_count:
			SelectGiftNumDialog numDialog = new SelectGiftNumDialog(this, android.R.style.Theme_Translucent_NoTitleBar, giftsCount,
					SkillCreateActivity.this);
			numDialog.showDialog(0, 0);
			break;
		case R.id.tv_gifts_type:
			new SkillUtilsBiz(this).getGiftList(this);
			break;

		default:
			break;
		}
	}

	@Override
	protected void initRecourse() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_new_skills);
		findViewById(R.id.btn_skill_submit).setOnClickListener(this);
		edt_skill = (EditText) findViewById(R.id.edt_skill_title);
		tv_limit = (TextView) findViewById(R.id.tv_limit);
		edt_describe = (EditText) findViewById(R.id.edt_skill_describe);
		edt_describe.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String mText = edt_describe.getText().toString();
				tv_limit.setText(mText.length() + "/" + 60);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		tvSkillName = (TextView) findViewById(R.id.tv_skill_name);
		tv_gifts_type = (TextView) findViewById(R.id.tv_gifts_type);
		tv_gifts_type.setOnClickListener(this);
		tv_gifts_count = (TextView) findViewById(R.id.tv_gifts_count);
		tv_gifts_count.setOnClickListener(this);
		showSoftInput(edt_skill);

	}

	@Override
	protected int initView() {
		return R.layout.activity_addskill;
	}

	@Override
	public void addSkillCallBack(int retCode, int skillId, String msg) {
		sendHandlerMessage(mHandler, ADD_SKILL, retCode, skillId, msg);
	}

	@Override
	public void getGiftListCallBack(int retCode, GiftInfoList giftInfoList) {//获取礼物列表
		sendHandlerMessage(mHandler, GET_GIFT_LIST, retCode, giftInfoList);
	}

	@Override
	public void getGiftInfoOnClickCallBack(GiftInfo giftInfo) {//选择的礼物
		tv_gifts_type.setText(new String(giftInfo.name));
		giftsId = giftInfo.id.intValue();
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;

		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		BaseUtils.hideKeyboard(this, edt_skill);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void getGiftInfoNumCallBack(int value) {//选择的礼物数量
		giftsCount = value;
		tv_gifts_count.setText(String.valueOf(value));
	}

	@Override
	public void editSkillCallBack(int retCode) {
		sendHandlerMessage(mHandler, ADD_SKILL, retCode);
	}

}
