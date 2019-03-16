package com.tshang.peipei.activity.skill;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.ConfirmSkillDialog;
import com.tshang.peipei.activity.dialog.MarkSkillDialog;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.SkillAct;
import com.tshang.peipei.base.babase.BAConstants.SkillStutas;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.space.SkillUtilsBiz;
import com.tshang.peipei.model.request.RequestAppealSkillDeal.iAppealSkillDeal;
import com.tshang.peipei.model.request.RequestConfirmSkillDeal.iConfirmSkillDeal;
import com.tshang.peipei.model.request.RequestMarkSkillDeal.IMarkSkillDeal;
import com.tshang.peipei.model.request.RequestReclainGift.iReclainGift;

/**
 * @Title: SkillDealInfoActivity.java 
 *
 * @Description: 订单详情界面	
 *
 * @author allen
 *
 * @date 2014-10-23 下午1:48:52 
 *
 * @version V1.0   
 */
public class SkillDealInfoActivity extends BaseActivity implements iConfirmSkillDeal, iReclainGift, iAppealSkillDeal, IMarkSkillDeal {

	private TextView mSkillType;
	private TextView mSkillTitle;
	private TextView mSkillTime;
	private TextView mSkillContent;
	private TextView mSkillStatus;
	private TextView mSkillHint;
	private LinearLayout mLayoutContent;
	private LinearLayout mLayoutHint;
	private Button mSkillSure;
	private Button mSkillCancel;
	private TextView mSkillOperate;
	private LinearLayout mLayoutOperate;

	private int mSkillStep = 0;
	private int mSkillUid = 0;
	private int mSKillDealId = 0;
	private int mSkillGender = 0;

	private SkillUtilsBiz sBiz;

	@Override
	protected void initData() {
		sBiz = new SkillUtilsBiz(this);

		Bundle b = getIntent().getExtras();
		mSkillStep = b.getInt("skill_status");
		mSkillUid = b.getInt("skill_uid");
		mSKillDealId = b.getInt("skill_dealid");
		mSkillGender = b.getInt("skill_type");

		if (mSkillGender == Gender.FEMALE.getValue()) {//求赏
			mSkillType.setText(R.string.str_skill_order_type1);
			mSkillType.setBackgroundResource(R.drawable.person_order_tag_bg_pleaseenjoy);
		} else {//打赏
			mSkillType.setText(R.string.str_skill_order_type2);
			mSkillType.setBackgroundResource(R.drawable.person_order_tag_bg_reward);
		}

		mSkillTitle.setText(b.getString("skill_title"));
		mSkillTime.setText(BaseTimes.getChatDiffTimeYYMDHM(b.getLong("skill_time")));
		mSkillContent.setText(b.getString("skill_content"));
		if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_START) {
			setStepByStart();
		} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_ACCEPT) {
			setStepbyAccept();
		} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_RECLAIN) {
			setStepByReclain();
		} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_MARK) {
			setStepByPointed();
		} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_APPEALING) {
			setStepByAppealing();
		} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_REFUSE) {
			setStepByRefuse();
		} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_END) {
			setStepByEnd();
		} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_APPEALED) {
			setStepByAppealed();
		}
	}

	private void setStepByAppealed() {
		mSkillOperate.setText(R.string.str_skill_order_appealing);
		mSkillStatus.setText(R.string.str_skill_order_status_appealend);
		mLayoutContent.setBackgroundResource(R.drawable.main_img_list3_selector);
		mLayoutContent.setPadding(BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10));
		mLayoutHint.setVisibility(View.GONE);
		mSkillCancel.setVisibility(View.GONE);
		mSkillSure.setVisibility(View.GONE);
	}

	private void setStepByEnd() {
		mSkillOperate.setText(R.string.str_skill_order_end);
		mSkillStatus.setText(R.string.str_skill_order_status_end);
		mLayoutContent.setBackgroundResource(R.drawable.main_img_list3_selector);
		mLayoutContent.setPadding(BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10));
		mLayoutHint.setVisibility(View.GONE);
		mSkillCancel.setVisibility(View.GONE);
		mSkillSure.setVisibility(View.GONE);
	}

	private void setStepByRefuse() {
		if (mSkillGender == Gender.FEMALE.getValue()) {//是自己发布的技能
			mSkillOperate.setText(R.string.str_skill_order_refuse2);
		} else {
			mSkillOperate.setText(R.string.str_skill_order_refuse1);
		}
		mSkillStatus.setText(R.string.str_skill_order_status_refuse);
		mLayoutContent.setBackgroundResource(R.drawable.main_img_list3_selector);
		mLayoutContent.setPadding(BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10));
		mLayoutHint.setVisibility(View.GONE);
		mSkillCancel.setVisibility(View.GONE);
		mSkillSure.setVisibility(View.GONE);
	}

	private void setStepByAppealing() {
		//申诉中状态
		if (mSkillGender == Gender.FEMALE.getValue()) {
			//对方申诉
			mSkillStatus.setText(R.string.str_skill_order_status_appeal1);
			mLayoutOperate.setVisibility(View.GONE);
			mLayoutContent.setBackgroundResource(R.drawable.main_img_list3_selector);
			mLayoutContent.setPadding(BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10));
			mLayoutHint.setVisibility(View.GONE);
		} else {
			mSkillStatus.setText(R.string.str_skill_order_status_appeal1);
			mSkillHint.setText(R.string.str_skill_order_content_accept2);
			mSkillOperate.setText(R.string.str_skill_order_appealing);
			mSkillCancel.setVisibility(View.GONE);
			mSkillSure.setVisibility(View.GONE);
		}
	}

	private void setStepByPointed() {
		//点评
		mLayoutOperate.setVisibility(View.GONE);
		mSkillStatus.setText(R.string.str_skill_order_status_accept4);
		mSkillHint.setVisibility(View.GONE);
		mLayoutContent.setBackgroundResource(R.drawable.main_img_list3_selector);
		mLayoutContent.setPadding(BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10));
		mLayoutHint.setVisibility(View.GONE);
	}

	private void setStepByStart() {
		//开始状态
		if (BAApplication.mLocalUserInfo.uid.intValue() == mSkillUid) {//是自己发布的技能
			if (mSkillGender == Gender.FEMALE.getValue()) {
				mSkillStatus.setText(R.string.str_skill_order_status_start1);
				mSkillSure.setOnClickListener(this);
				mSkillSure.setText(R.string.str_receive);
				mSkillCancel.setOnClickListener(this);
				mSkillCancel.setText(R.string.str_skill_order_refuse);
				mSkillHint.setText(R.string.str_skill_order_content_start1);
			} else {
				mSkillStatus.setText(R.string.str_skill_order_status_start2);
				mSkillCancel.setVisibility(View.GONE);
				mSkillSure.setOnClickListener(this);
				mSkillSure.setText(R.string.str_skill_order_result_gift);
				mSkillHint.setText(R.string.str_skill_order_content_start2);
			}
		} else {
			if (mSkillGender == Gender.FEMALE.getValue()) {
				mSkillStatus.setText(R.string.str_skill_order_status_start2);
				mSkillCancel.setVisibility(View.GONE);
				mSkillSure.setOnClickListener(this);
				mSkillSure.setText(R.string.str_skill_order_result_gift);
				mSkillHint.setText(R.string.str_skill_order_content_start2);
			} else {
				mSkillStatus.setText(R.string.str_skill_order_status_start3);
				mSkillSure.setOnClickListener(this);
				mSkillSure.setText(R.string.str_receive);
				mSkillCancel.setOnClickListener(this);
				mSkillCancel.setText(R.string.str_skill_order_refuse);
				mSkillHint.setText(R.string.str_skill_order_content_start1);
			}
		}
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.skill_status);

		mSkillType = (TextView) findViewById(R.id.skill_order_type);
		mSkillTitle = (TextView) findViewById(R.id.skill_order_name);
		mSkillTime = (TextView) findViewById(R.id.skill_order_time);
		mLayoutContent = (LinearLayout) findViewById(R.id.skill_order_content_ll);
		mSkillContent = (TextView) findViewById(R.id.skill_order_from);
		mSkillStatus = (TextView) findViewById(R.id.skill_order_status);
		mLayoutHint = (LinearLayout) findViewById(R.id.skill_order_toast_ll);
		mSkillHint = (TextView) findViewById(R.id.skill_order_toast);
		mSkillSure = (Button) findViewById(R.id.skill_order_sure);
		mSkillCancel = (Button) findViewById(R.id.skill_order_cancel);
		mLayoutOperate = (LinearLayout) findViewById(R.id.skill_order_operate_ll);
		mSkillOperate = (TextView) findViewById(R.id.skill_order_operate_content);
	}

	@Override
	protected int initView() {
		return R.layout.activity_skill_order;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_tv_left:
			Bundle b = new Bundle();
			Intent intent = new Intent();
			b.putInt("step", mSkillStep);
			b.putInt("skilldealid", mSKillDealId);
			intent.putExtras(b);
			setResult(MineSkillsListActivity.RESULT_CODE, intent);
			finish();
			break;
		case R.id.skill_order_sure:
			if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_START) {//开始状态
				if (mSkillGender == Gender.FEMALE.getValue()) {
					if (BAApplication.mLocalUserInfo.uid.intValue() == mSkillUid) {//是自己发布的技能
						sBiz.confirmSkillDeal(SkillAct.YES, mSKillDealId, this);
					} else {
						sBiz.reclainGift(mSKillDealId, this);
					}
				} else {
					if (BAApplication.mLocalUserInfo.uid.intValue() == mSkillUid) {//是自己发布的技能
						sBiz.reclainGift(mSKillDealId, this);
					} else {
						sBiz.confirmSkillDeal(SkillAct.YES, mSKillDealId, this);
					}
				}
			} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_ACCEPT) {
				if (BAApplication.mLocalUserInfo.uid.intValue() == mSkillUid) {//是自己发布的技能
					if (mSkillGender != Gender.FEMALE.getValue()) {
						new ConfirmSkillDialog(this, R.string.str_skill_order_appeal_content, R.string.str_skill_order_appeal, mSKillDealId, mHandler)
								.showDialog();
					}
				} else {
					if (mSkillGender == Gender.FEMALE.getValue()) {
						new MarkSkillDialog(this, android.R.style.Theme_Translucent_NoTitleBar, BAApplication.mLocalUserInfo.uid.intValue(),
								mSKillDealId, this).showDialog();
					}
				}
			}
			break;
		case R.id.skill_order_cancel:
			if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_START) {//开始状态
				sBiz.confirmSkillDeal(SkillAct.NO, mSKillDealId, this);
			} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_ACCEPT) {
				if (BAApplication.mLocalUserInfo.uid.intValue() != mSkillUid) {//不是自己发布的技能
					if (mSkillGender == Gender.FEMALE.getValue()) {
						new ConfirmSkillDialog(this, R.string.str_skill_order_appeal_content, R.string.str_skill_order_appeal, mSKillDealId, mHandler)
								.showDialog();
					}
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.SKILL_DEAL_ORDER_OPERATE:
			if (msg.arg1 == 0) {
				SkillDealInfo info = (SkillDealInfo) msg.obj;
				if (info != null) {
					mSkillStep = info.step.intValue();
					if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_ACCEPT) {//接受状态
						setStepbyAccept();
					} else if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_REFUSE) {
						setStepByRefuse();
					}
				}
			}
			break;
		case HandlerValue.SKILL_DEAL_ORDER_RECLAINGIFT:
			if (msg.arg1 == 0) {
				SkillDealInfo info1 = (SkillDealInfo) msg.obj;
				if (info1 != null) {
					mSkillStep = info1.step.intValue();
					if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_RECLAIN) {
						setStepByReclain();
					}
				}
			} else if (msg.arg1 == rspContMsgType.E_GG_SKILL_DEAL_RECLAIN) {
				BaseUtils.showTost(this, R.string.str_skill_order_appeal_toast);
			}
			break;
		case HandlerValue.SKILL_DEAL_ORDER_APPEAL:
			if (msg.arg1 == 0) {
				SkillDealInfo info1 = (SkillDealInfo) msg.obj;
				if (info1 != null) {
					mSkillStep = info1.step.intValue();
					if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_APPEALING) {
						setStepByAppealing();
					}
				}
			}
			break;
		case HandlerValue.SKILL_DEAL_ORDER_POINT:
			if (msg.arg1 == 0) {
				SkillDealInfo info1 = (SkillDealInfo) msg.obj;
				if (info1 != null) {
					mSkillStep = info1.step.intValue();
					if (mSkillStep == SkillStutas.GG_SKILL_DEAL_STEP_MARK) {
						setStepByPointed();
					}
				}
			}
			break;
		default:
			break;
		}
	}

	private void setStepByReclain() {
		//已退回礼物
		if (BAApplication.mLocalUserInfo.uid.intValue() == mSkillUid) {//是自己发布的技能
			if (mSkillGender == Gender.FEMALE.getValue()) {
				mSkillOperate.setText(R.string.str_skill_order_operate_content1);
			} else {
				mSkillOperate.setText(R.string.str_skill_order_operate_content2);
			}
		} else {
			if (mSkillGender == Gender.FEMALE.getValue()) {
				mSkillOperate.setText(R.string.str_skill_order_operate_content2);
			} else {
				mSkillOperate.setText(R.string.str_skill_order_operate_content1);
			}
		}
		mLayoutHint.setVisibility(View.GONE);
		mLayoutContent.setBackgroundResource(R.drawable.main_img_list3_selector);
		mLayoutContent.setPadding(BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10));
		mSkillSure.setVisibility(View.GONE);
		mSkillCancel.setVisibility(View.GONE);
		mSkillOperate.setVisibility(View.VISIBLE);
		mSkillStatus.setText(R.string.str_skill_order_status_end);
	}

	private void setStepbyAccept() {
		if (BAApplication.mLocalUserInfo.uid.intValue() == mSkillUid) {//是自己发布的技能
			if (mSkillGender == Gender.FEMALE.getValue()) {
				//提醒去点评 
				mSkillStatus.setText(R.string.str_skill_order_status_accept3);
				mSkillHint.setText(R.string.str_skill_order_content_accept2);
				mLayoutOperate.setVisibility(View.GONE);
			} else {
				//申诉
				mSkillSure.setText(R.string.str_skill_order_appeal);
				mSkillSure.setOnClickListener(this);
				mSkillStatus.setText(R.string.str_skill_order_status_accept2);
				mSkillHint.setText(R.string.str_skill_order_content_accept2);
				mSkillCancel.setVisibility(View.GONE);
			}
		} else {
			if (mSkillGender == Gender.FEMALE.getValue()) {
				//点评申诉
				mSkillStatus.setText(R.string.str_skill_order_status_accept3);
				mLayoutOperate.setVisibility(View.VISIBLE);
				mLayoutContent.setBackgroundResource(R.drawable.main_img_list3_selector);
				mLayoutContent.setPadding(BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10), BaseUtils.dip2px(this, 10),
						BaseUtils.dip2px(this, 10));
				mSkillSure.setText(R.string.str_skill_order_agree);
				mSkillCancel.setText(R.string.str_skill_order_appeal_gift);
				mSkillSure.setOnClickListener(this);
				mSkillCancel.setOnClickListener(this);
				mLayoutHint.setVisibility(View.GONE);
			} else {
				//订单交流
				mLayoutOperate.setVisibility(View.GONE);
				mSkillStatus.setText(R.string.str_skill_order_status_accept1);
				mSkillHint.setText(R.string.str_skill_order_content_accept1);
			}
		}
	}

	@Override
	public void resultConfirmSkillDeal(int retCode, SkillDealInfo info) {
		sendHandlerMessage(mHandler, HandlerValue.SKILL_DEAL_ORDER_OPERATE, retCode, info);
	}

	@Override
	public void resultReclainGift(int retCode, SkillDealInfo info) {
		sendHandlerMessage(mHandler, HandlerValue.SKILL_DEAL_ORDER_RECLAINGIFT, retCode, info);
	}

	@Override
	public void resultAppealSkillDeal(int retCode, SkillDealInfo skilldeal) {
		sendHandlerMessage(mHandler, HandlerValue.SKILL_DEAL_ORDER_APPEAL, retCode, skilldeal);
	}

	@Override
	public void resultMarkSkillDeal(int retCode, SkillDealInfo skilldeal) {
		sendHandlerMessage(mHandler, HandlerValue.SKILL_DEAL_ORDER_POINT, retCode, skilldeal);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Bundle b = new Bundle();
			Intent intent = new Intent();
			b.putInt("step", mSkillStep);
			b.putInt("skilldealid", mSKillDealId);
			intent.putExtras(b);
			setResult(MineSkillsListActivity.RESULT_CODE, intent);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
