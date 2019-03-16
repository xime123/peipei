package com.tshang.peipei.activity.skill;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.GoLoginDialog;
import com.tshang.peipei.activity.dialog.InterestinSkillDialog;
import com.tshang.peipei.activity.dialog.ReportSkillDialog;
import com.tshang.peipei.activity.dialog.SkillDeleteDialog;
import com.tshang.peipei.activity.dialog.SkillGiftSuccessDialog;
import com.tshang.peipei.activity.dialog.SkillSendGiftDialog;
import com.tshang.peipei.activity.dialog.SkillSendGiftDialog.BizCallBackSendGiftSuccess;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.skill.adapter.MaleSkillInterestinGuestAdapter;
import com.tshang.peipei.activity.skill.adapter.MaleSkillInterestinHostAdapter;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.SkillUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.space.SkillUtilsBiz;
import com.tshang.peipei.model.biz.user.UserInfoBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserProperty;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestDelSkill.IDelSKill;
import com.tshang.peipei.model.request.RequestGetRelasionship.IGetRelationship;
import com.tshang.peipei.model.request.RequestGetSingleSkillInfo.BizCallBackGetSingleSkillInfo;
import com.tshang.peipei.model.request.RequestMarkSkillDeal.IMarkSkillDeal;
import com.tshang.peipei.model.skill.SkillJoin;
import com.tshang.peipei.model.space.SpaceBiz;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RelationshipInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetParticipantInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetParticipantInfoList;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.view.PeipeiGridView;
import com.tshang.peipei.view.ReplyChildListView;

import de.greenrobot.event.EventBus;

public class SkillDetailActivity extends BaseActivity implements IDelSKill, IGetRelationship, BizCallBackGetUserProperty, IMarkSkillDeal,
		BizCallBackGetSingleSkillInfo, BizCallBackSendGiftSuccess {
	private RatingBar ratingBar;
	private TextView tv_rating;
	private TextView tv_left;
	private TextView tv_right;
	private TextView tv_need_gift;
	private int skillid = -1;
	private int giftNum = 1;
	private int giftId = -1;
	private int skillUid = -1;
	private String userName = "";
	private String headKey = "";
	private String giftKey = "";
	private int charmValue = 0;
	private int pricegold = 0;//礼物需要的金币数量
	private int pricesilver = 0;
	private int loyaltyeffect = 0;
	private long ontime = 0;
	private int type = 0;//0代表是女的，1代表是男的
	private int joinPeopleCount = 0;//参加的人数
	private boolean fromChat = false;

	private TextView tv_skill_name;
	private TextView tv_skill_describe;
	private TextView tv_giftName;
	private TextView tv_giftNum;
	private TextView tv_joinPeopleCount;
	private TextView tv_desc;
	private TextView tv_interest;
	private ImageView iv_user_level;
	private LinearLayout ll_bottom_female;
	private LinearLayout ll_bottom_male;
	private LinearLayout ll_bottom_right;
	private ImageView iv_bottom_left;
	private ImageView ivHead;
	private ImageView ivGift;
	private TextView tv_onTime;
	private TextView tv_user_name;

	private TextView tv_interestin_data;
	private PeipeiGridView gridView;
	private ReplyChildListView listview;
	private MaleSkillInterestinGuestAdapter maleGuestAdapter;
	private MaleSkillInterestinHostAdapter maleHostAdapter;

	private static final int BIZ_CALLBACK_DELETE = 0x10;
	private static final int BIZ_CALLBACK_GET_RELATIONSHIP = 0x11;//检测魅力贡献值成功
	private static final int BIZ_CALLBACK_GET_USERPROPERTY = 0x12;
	private static final int BIZ_CALLBACK_MARK_SKILL = 0x13;
	private static final int BIZ_CALLBACK_GET_SINGLE_SKILLINFO = 0x14;
	private boolean isSelf = false;

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		int retCode = msg.arg1;
		switch (msg.what) {
		case BIZ_CALLBACK_DELETE:
			if (retCode == 0) {//删除成功了
				setResult(RESULT_OK, new Intent(SkillDetailActivity.this, SpaceActivity.class));
				SkillDetailActivity.this.finish();
				BaseUtils.showTost(SkillDetailActivity.this, R.string.str_delete_success);
			} else {//删除失败了
				BaseUtils.showTost(SkillDetailActivity.this, R.string.str_delete_failed);
			}
			break;
		case BIZ_CALLBACK_GET_USERPROPERTY:
			if (retCode == 0) {
				UserPropertyInfo userPropertyInfo = (UserPropertyInfo) msg.obj;
				if (userPropertyInfo != null) {
					new SkillSendGiftDialog(SkillDetailActivity.this, android.R.style.Theme_Translucent_NoTitleBar, userPropertyInfo, giftKey,
							userName, tv_giftName.getText().toString(), loyaltyeffect, charmValue, giftNum, pricegold, pricesilver, skillid,
							skillUid, mHandler, SkillDetailActivity.this, SkillDetailActivity.this).showDialog();
				}
			} else {

			}
			break;
		case BIZ_CALLBACK_GET_SINGLE_SKILLINFO:
			if (retCode == 0) {
				RetGGSkillInfo skillInfo = (RetGGSkillInfo) msg.obj;
				if (skillInfo != null) {

					pricegold = skillInfo.giftinfo.pricegold.intValue();
					loyaltyeffect = skillInfo.giftinfo.loyaltyeffect.intValue();
					pricesilver = skillInfo.giftinfo.pricesilver.intValue();
					charmValue = skillInfo.giftinfo.charmeffect.intValue();
					skillUid = skillInfo.hostuserinfo.uid.intValue();
					type = skillInfo.skillinfo.type.intValue();

					String alias = SharedPreferencesTools.getInstance(SkillDetailActivity.this,
							BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(skillInfo.hostuserinfo.uid.intValue());

					userName = TextUtils.isEmpty(alias) ? new String(skillInfo.hostuserinfo.nick) : alias;
					joinPeopleCount = skillInfo.skillinfo.participantnum.intValue();

					String skillName = new String(skillInfo.skillinfo.title);
					String giftName = new String(skillInfo.giftinfo.name);
					giftNum = skillInfo.skillinfo.giftnum.intValue();
					giftId = skillInfo.skillinfo.giftid.intValue();
					headKey = new String(skillInfo.hostuserinfo.headpickey);
					giftKey = new String(skillInfo.giftinfo.pickey);
					String deseribe = new String(skillInfo.skillinfo.desc);
					ontime = skillInfo.hostuserinfo.lastlogtime.longValue();
					int skillRating = skillInfo.skillinfo.averagepoint.intValue();
					getDataShow(giftName, skillName, deseribe, skillRating);
				}
			} else {
				BaseUtils.showTost(SkillDetailActivity.this, R.string.str_loading_failed);
			}
			break;
		case HandlerValue.SPACE_USER_INFO_VALUE://获取到用户的基本信息，比如昵称等数据
			if (msg.arg1 == 0) {
				GoGirlUserInfo userInfo1 = (GoGirlUserInfo) msg.obj;
				if (userInfo1 != null) {
					String gradeinfo = new String(userInfo1.gradeinfo);
					if (TextUtils.isEmpty(gradeinfo)) {
						iv_user_level.setVisibility(View.GONE);
					} else {
						iv_user_level.setVisibility(View.VISIBLE);
						DisplayImageOptions campaignHatOptions = ImageOptionsUtils.getCampaignHatOptions(this);
						GradeInfoImgUtils.loadGradeInfoImg(this, imageLoader, campaignHatOptions, gradeinfo, iv_user_level);
					}
				}
			}
			break;
		case HandlerValue.SPACE_REPORT_SKILL_VALUE:
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, "举报成功");
			} else {
				BaseUtils.showTost(this, "举报失败");
			}
			break;
		case HandlerValue.SKILL_INTERESTIN_JOIN_SUCCESS_VALUE:
			SkillJoin.getInstance().reqInterestinSkillList(this, skillid, skillUid, mHandler);
			BaseUtils.showTost(this, R.string.str_leave_messave_success);
			break;
		case HandlerValue.SKILL_INTERESTIN_JOIN_FAILED_VALUE:
			BaseUtils.showTost(this, R.string.str_leave_messave_failed);
			break;
		case HandlerValue.SKILL_INTERESTIN_JOIN_REPEAT_VALUE:
			BaseUtils.showTost(this, R.string.str_leave_message_repeat);
			break;
		case HandlerValue.SKILL_GET_SKILL_INTERESTIN_LIST_SUCCESS_VALUE://获取感兴趣的技能列表用户成功
			RetParticipantInfoList lists = (RetParticipantInfoList) msg.obj;
			if (lists != null && !lists.isEmpty()) {
				tv_interestin_data.setVisibility(View.GONE);
				if (isSelf) {
					maleHostAdapter.setList(lists);
				} else {
					maleGuestAdapter.setList(lists);
				}
			} else {
				tv_interestin_data.setVisibility(View.VISIBLE);
			}
			break;
		case HandlerValue.SKILL_GET_SKILL_INTERESTIN_LIST_FAILED_VALUE://失败
			break;
		case HandlerValue.SKILL_ADD_SKILL_DEAL_SUCCESS_VALUE:
			//说明送礼成功
			if (type == 0) {
				new SkillGiftSuccessDialog(this, android.R.style.Theme_Translucent_NoTitleBar, skillUid, userName, type).showDialog();
			} else {
				BaseUtils.showTost(this, "订单已生成，您的礼物扣压在陪陪平台，对方确认后，礼物送出，如果对方未确认，您可手动去我的技能订单里把礼物要回！");
			}
			break;
		case HandlerValue.SKILL_ADD_SKILL_DEAL_FAILED_VALUE:
			BaseUtils.showTost(this, "下单失败");
			break;
		case HandlerValue.SKILL_ADD_SKILL_DEAL_MONEY_NOT_ENGOUH://财富不够
			new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			break;
		default:
			break;
		}
	}

	protected void getUserInfo() {//获取用户的信息
		new SpaceBiz(this, mHandler).getUserInfo(skillUid);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		MobclickAgent.onEvent(this, "JiNengXiangQingChaKanCiShu");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_bottom_interested_in:
			if (BAApplication.mLocalUserInfo == null) {
				return;
			}
			if (BAApplication.mLocalUserInfo.sex.intValue() == Gender.MALE.getValue()) {
				BaseUtils.showTost(this, "您不能够参与他的打赏");
			} else {
				new InterestinSkillDialog(this, android.R.style.Theme_Translucent_NoTitleBar, skillid, skillUid, mHandler).showDialog();
			}
			break;
		case R.id.ll_skill_detail_left:
			ClickButton(true);
			break;
		case R.id.ll_skill_detail_right:
			ClickButton(false);
			break;
		case R.id.ll_skill_user:
			Bundle bundle = new Bundle();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, skillUid);
			bundle.putString(BAConstants.IntentType.MAINHALLFRAGMENT_USERNICK, userName);
			bundle.putString(BAConstants.IntentType.MAINHALLFRAGMENT_HEADPIC, headKey);
			bundle.putLong(BAConstants.IntentType.MAINHALLFRAGMENT_USERGLAMOUR, charmValue);
			bundle.putLong(BAConstants.IntentType.MAINHALLFRAGMENT_USERLATESTTIME, ontime);
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, type);
			BaseUtils.openActivity(this, SpaceActivity.class, bundle);

			break;
		case R.id.title_tv_right:
			if (UserUtils.getUserEntity(this) == null) {
				new GoLoginDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog();
			} else {
				new ReportSkillDialog(this, android.R.style.Theme_Translucent_NoTitleBar, skillUid, skillid, mHandler).showDialog(0, 0);
			}
			break;
		}
	}

	private void ClickButton(boolean isleft) {
		if (BAApplication.mLocalUserInfo == null) {
			new GoLoginDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog();
		} else {
			UserInfoBiz userInfoBiz = new UserInfoBiz(this);
			if (isleft) {//左边
				if (isSelf) {//是自己点击左边删除技能
					new SkillDeleteDialog(this, android.R.style.Theme_Translucent_NoTitleBar, skillid, type, this).showDialog();
				} else {//别人点击左边送礼
					if (pricesilver > 0) {
						BaseUtils.showTost(this, "她是银币技能礼物，不能够参与");
					} else {
						userInfoBiz.getStoreUser(this, this);
					}
				}
			} else {//右边 是自己点击右边编辑技能
				SkillUtils.intentAddSkill(this, skillid, tv_skill_name.getText().toString(), tv_skill_describe.getText().toString(), tv_giftName
						.getText().toString(), giftNum, giftId, type);

			}
		}
	}

	@Override
	protected void initRecourse() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_skill_detail);

		mTextRight = (TextView) findViewById(R.id.title_tv_right);

		mTextRight.setText(R.string.report);
		mTextRight.setOnClickListener(this);

		ratingBar = (RatingBar) findViewById(R.id.rb_skill_detail);
		tv_rating = (TextView) findViewById(R.id.tv_rating);

		findViewById(R.id.ll_skill_detail_left).setOnClickListener(this);
		ll_bottom_right = (LinearLayout) findViewById(R.id.ll_skill_detail_right);
		ll_bottom_right.setOnClickListener(this);
		findViewById(R.id.ll_skill_user).setOnClickListener(this);
		iv_bottom_left = (ImageView) findViewById(R.id.iv_bottom_left);

		tv_left = (TextView) findViewById(R.id.tv_skill_detail_left);
		tv_right = (TextView) findViewById(R.id.tv_skill_detail_right);
		iv_user_level = (ImageView) findViewById(R.id.iv_user_level_head);
		tv_desc = (TextView) findViewById(R.id.tv_skill_desc);
		tv_interest = (TextView) findViewById(R.id.tv_bottom_interested_in);
		ll_bottom_female = (LinearLayout) findViewById(R.id.ll_bottom_female);

		tv_user_name = (TextView) findViewById(R.id.tv_skill_user_name);
		tv_skill_name = (TextView) findViewById(R.id.tv_skill_name);
		ivHead = (ImageView) findViewById(R.id.iv_skill_user_head);
		ivGift = (ImageView) findViewById(R.id.iv_skill_gift);
		tv_skill_describe = (TextView) findViewById(R.id.tv_skill_describe);
		tv_onTime = (TextView) findViewById(R.id.tv_skill_detail_isonline);
		tv_giftName = (TextView) findViewById(R.id.tv_skill_detail_gift_name);
		tv_giftNum = (TextView) findViewById(R.id.tv_skill_detail_gift_count);
		tv_joinPeopleCount = (TextView) findViewById(R.id.tv_skill_join_people);

		gridView = (PeipeiGridView) findViewById(R.id.gv_interestin);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RetParticipantInfo info = (RetParticipantInfo) parent.getAdapter().getItem(position);
				if (info != null) {
					SpaceUtils.toSpaceCustom(SkillDetailActivity.this, info.participantuserinfo, 0);
				}
			}
		});
		listview = (ReplyChildListView) findViewById(R.id.lv_interestin);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RetParticipantInfo info = (RetParticipantInfo) parent.getAdapter().getItem(position);
				if (info != null) {
					SpaceUtils.toSpaceCustom(SkillDetailActivity.this, info.participantuserinfo, 0);
				}
			}
		});
		tv_interestin_data = (TextView) findViewById(R.id.tv_no_sign_up);
		ll_bottom_male = (LinearLayout) findViewById(R.id.ll_male_skill_bottom);
		tv_need_gift = (TextView) findViewById(R.id.tv_need_gift);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			skillid = bundle.getInt(SkillUtils.SKILL_ID);
			fromChat = bundle.getBoolean(SkillUtils.SKILL_FROM, false);
			if (!fromChat) {
				pricegold = bundle.getInt(SkillUtils.SKILL_GIFT_PRICEGOLD);
				loyaltyeffect = bundle.getInt(SkillUtils.SKILL_GIFT_LOYALTY_VALUE);
				pricesilver = bundle.getInt(SkillUtils.SKILL_GIFT_PRICESILVER);
				charmValue = bundle.getInt(SkillUtils.SKILL_GIFT_CHARM_VALUE);
				skillUid = bundle.getInt(SkillUtils.SKILL_USER_ID);
				type = bundle.getInt(SkillUtils.SKILL_TYPE);
				userName = bundle.getString(SkillUtils.SKILL_USER_NAME);
				joinPeopleCount = bundle.getInt(SkillUtils.SkILL_JOIN_PEOPLE_COUNT);

				String skillName = bundle.getString(SkillUtils.SKILL_TITLE);
				String giftName = bundle.getString(SkillUtils.SKILL_GIFT_NAME);
				giftNum = bundle.getInt(SkillUtils.SKILL_GIFT_NUM);
				giftId = bundle.getInt(SkillUtils.SKILL_GIFT_ID);
				headKey = bundle.getString(SkillUtils.SKILL_USER_HEADKEY);
				giftKey = bundle.getString(SkillUtils.SKILL_GIFT_KEY);
				String deseribe = bundle.getString(SkillUtils.SKILL_DESCRIBE);
				ontime = bundle.getLong(SkillUtils.SKILL_USER_ONTIME);
				int skillRating = Integer.parseInt(bundle.getString(SkillUtils.SKILL_RATING));
				getDataShow(giftName, skillName, deseribe, skillRating);
			} else {
				if (BAApplication.mLocalUserInfo != null) {
					new SkillUtilsBiz(this).getSingleSkillInfo(BAApplication.mLocalUserInfo.uid.intValue(), skillid, this);
				}
			}

		}
	}

	private void getDataShow(String giftName, String skillName, String deseribe, int skillRating) {
		tv_giftName.setText(giftName);
		tv_skill_name.setText(skillName);
		tv_skill_describe.setText(deseribe);
		tv_giftNum.setText("X" + giftNum);
		tv_user_name.setText(userName);
		tv_joinPeopleCount.setText(joinPeopleCount + "人参与");
		DisplayImageOptions options_head = ImageOptionsUtils.GetHeadKeyBigRounded(this);
		imageLoader.displayImage("http://" + headKey + "@true@210@210", ivHead, options_head);

		DisplayImageOptions options = ImageOptionsUtils.getImageKeyOptions(this);
		imageLoader.displayImage("http://" + giftKey + BAConstants.LOAD_180_APPENDSTR, ivGift, options);

		if (type == 0) {//女的技能
			ratingBar.setProgress(skillRating);
			tv_need_gift.setText(R.string.str_need_gift);
		} else {//男的技能
			tv_need_gift.setText(R.string.str_give_gift);
			mTitle.setText(R.string.str_man_skill_detail);
			tv_desc.setText(R.string.str_give);
			ratingBar.setVisibility(View.INVISIBLE);
		}
		String time = "";
		if (ontime > 0) {
			time = getString(R.string.online);
		} else {
			time = BaseTimes.getDiffTime(Math.abs(ontime) * 1000);
		}
		tv_onTime.setText(time);
		LinearLayout ll_gift = (LinearLayout) findViewById(R.id.ll_gift);
		if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == skillUid) {//说明是进入到了自己技能页面
			tv_rating.setVisibility(View.GONE);
			isSelf = true;
			ll_gift.setBackgroundResource(R.drawable.main_img_list3_un);
			gridView.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			if (type == 0) {
				ll_bottom_male.setVisibility(View.GONE);
				tv_right.setText(R.string.str_edit_skill);
				tv_left.setText(R.string.str_delete_skill);
			} else if (type == 1) {
				tv_right.setText(R.string.str_edit_enjoy);
				tv_left.setText(R.string.str_delete_enjoy);
			}
		} else {
			isSelf = false;
			gridView.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
			ll_gift.setBackgroundResource(R.drawable.main_img_list2_un);
			ll_bottom_right.setVisibility(View.GONE);
			tv_rating.setVisibility(View.VISIBLE);
			tv_left.setText("送礼(不满意,就申请退礼物)");
			if (type == 0) {
				tv_rating.setText(R.string.str_send_skill_gift_step);
				iv_bottom_left.setImageResource(R.drawable.person_icon_gift_2_pr);
				ll_bottom_female.setVisibility(View.VISIBLE);
				tv_interest.setVisibility(View.GONE);
				ll_bottom_male.setVisibility(View.GONE);
			} else {
				tv_rating.setText(R.string.str_enjoy_step);
				ll_bottom_female.setVisibility(View.GONE);
				tv_interest.setVisibility(View.VISIBLE);
				tv_interest.setOnClickListener(this);
			}
		}
		maleGuestAdapter = new MaleSkillInterestinGuestAdapter(this);
		gridView.setAdapter(maleGuestAdapter);
		maleHostAdapter = new MaleSkillInterestinHostAdapter(this, skillid, skillUid, tv_giftName.getText().toString(), giftNum, mHandler);
		listview.setAdapter(maleHostAdapter);
		if (type == 1) {
			SkillJoin.getInstance().reqInterestinSkillList(this, skillid, skillUid, mHandler);
			mTextRight.setVisibility(View.GONE);
		}
	}

	@Override
	protected int initView() {
		return R.layout.activity_skill_detail;
	}

	@Override
	protected void initData() {
		getUserInfo();
		if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == skillUid) {
			mTextRight.setVisibility(View.GONE);
		} else {
			mTextRight.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1000 && data != null) {
				tv_skill_name.setText(data.getStringExtra(SkillUtils.SKILL_TITLE));
				tv_skill_describe.setText(data.getStringExtra(SkillUtils.SKILL_DESCRIBE));
				tv_giftName.setText(data.getStringExtra(SkillUtils.SKILL_GIFT_NAME));
				giftId = data.getIntExtra(SkillUtils.SKILL_GIFT_ID, -1);
				giftNum = data.getIntExtra(SkillUtils.SKILL_GIFT_NUM, 1);
				tv_giftNum.setText("X" + giftNum);
				NoticeEvent event = new NoticeEvent();
				event.setFlag(NoticeEvent.NOTICE57);
				EventBus.getDefault().postSticky(event);
			}
		}
	}

	@Override
	public void delSkillCallBack(int retCode, String msg) {
		sendHandlerMessage(mHandler, BIZ_CALLBACK_DELETE, retCode, msg);
	}

	@Override
	public void getRelationshipCallBack(int retCode, int isAttention, RelationshipInfo relation) {
		sendHandlerMessage(mHandler, BIZ_CALLBACK_GET_RELATIONSHIP, retCode, relation);
	}

	@Override
	public void getUserProperty(int retCode, UserPropertyInfo userPropertyInfo) {
		sendHandlerMessage(mHandler, BIZ_CALLBACK_GET_USERPROPERTY, retCode, userPropertyInfo);
	}

	@Override
	public void getSingleSkillInfo(int retCode, RetGGSkillInfo skillInfo) {
		sendHandlerMessage(mHandler, BIZ_CALLBACK_GET_SINGLE_SKILLINFO, retCode, skillInfo);
	}

	@Override
	public void getSendGiftSuccess() {
		sendHandlerMessage(mHandler, BIZ_CALLBACK_MARK_SKILL, 0);
	}

	@Override
	public void resultMarkSkillDeal(int retCode, SkillDealInfo skilldeal) {
		sendHandlerMessage(mHandler, BIZ_CALLBACK_MARK_SKILL, retCode);
	}

}
