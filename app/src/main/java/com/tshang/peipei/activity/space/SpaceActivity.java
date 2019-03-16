package com.tshang.peipei.activity.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.chat.MessageVisitorActivity;
import com.tshang.peipei.activity.dialog.FinshShowDialog;
import com.tshang.peipei.activity.dialog.GoLoginDialog;
import com.tshang.peipei.activity.dialog.PhotoSetDialog;
import com.tshang.peipei.activity.harem.CreateHaremActivity;
import com.tshang.peipei.activity.harem.adapter.HaremGroupAdapter;
import com.tshang.peipei.activity.main.MainFriendsActivity;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.activity.mine.MineSettingUserInfoActivity;
import com.tshang.peipei.activity.mine.ReportActivity;
import com.tshang.peipei.activity.show.PeipeiShowActivity;
import com.tshang.peipei.activity.skillnew.GoddessSkillListActivity;
import com.tshang.peipei.activity.skillnew.SkillInviteActivity;
import com.tshang.peipei.activity.skillnew.adapter.SpaceSkillAdapter;
import com.tshang.peipei.activity.space.adapter.SpaceCustomHzListViewAdapter;
import com.tshang.peipei.activity.space.adapter.SpaceGiftAdapter;
import com.tshang.peipei.activity.store.StoreGiftListActivity;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.ILog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.InOutAct;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.jobs.GetFailureOrSendingTopicFromDbJob;
import com.tshang.peipei.model.biz.space.SpaceCustomBiz;
import com.tshang.peipei.model.biz.space.SpaceRelationshipBiz;
import com.tshang.peipei.model.biz.user.DynamicRequseControl;
import com.tshang.peipei.model.biz.user.UserInfoBiz;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.harem.CreateHarem;
import com.tshang.peipei.model.harem.HaremUtils;
import com.tshang.peipei.model.request.RequestDelFollowByUid.IDeleteFollowByUid;
import com.tshang.peipei.model.request.RequestDynamicAll.GetDynamicAllCallBack;
import com.tshang.peipei.model.request.RequestGetDistance.IGetDistance;
import com.tshang.peipei.model.request.RequestPersonSkillInfo.GetPersonSkillInfo;
import com.tshang.peipei.model.showrooms.RoomsGetBiz;
import com.tshang.peipei.model.skillnew.GoddessSkillEngine;
import com.tshang.peipei.model.space.SpaceBiz;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfo;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GiftFeedInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfoList;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.service.GpsLocationThread;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;
import com.tshang.peipei.view.ObservableScrollView;
import com.tshang.peipei.view.ObservableScrollView.ScrollListener;
import com.tshang.peipei.view.ReplyChildListView;
import com.tshang.peipei.view.UnScrollGridview;
import com.tshang.peipei.view.fall.BlessingFlakeView;
import com.tshang.peipei.view.fall.CloudFlakeView;
import com.tshang.peipei.view.fall.FlowerFlakeView;
import com.tshang.peipei.view.fall.Rose1FlakeView;
import com.tshang.peipei.view.fall.SnowFlakeView;
import com.tshang.peipei.view.fall.StarFallFlakeView;

public class SpaceActivity extends BaseActivity implements GetDynamicAllCallBack, IDeleteFollowByUid, IGetDistance, GetPersonSkillInfo {

	private final int DELETEFOLLOW = 5;
	private RelativeLayout rl_top_bg;
	private ImageView mIvHead;

	private int mFriendUid; //客人UID
	private String mPicHead = "";//客人头像KEY
	private String mNick = ""; //客人昵称
	protected String mShowKey;//客人形象照
	private boolean isFromSecenid = false;
	private long mLatestOnlineTime; //客人最近上线时间
	private int mSex;
	private ImageView identifyImage;
	private ImageView iv_tips;
	private ImageView iv_userlevel;
	private TextView tv_id;
	private TextView tv_age;
	private TextView tv_onLine;
	private TextView tv_distance;
	private ImageView iv_pet;
	private AnimationDrawable anim_pet;
	private AnimationDrawable anim_change;
	private ImageView iv_onShow;
	private ImageView rechargeImage;
	private String authKey;

	private SpaceBiz spaceBiz;
	private RoomsGetBiz roomsGetBiz;//
	private GoddessSkillEngine skillEngine;
	private ShowRoomInfo showRoomInfo;
	private TextView tv_property_title;
	private TextView tv_property_value;
	private TextView tv_new_increase_fans;
	private TextView tv_new_increase_visitor;

	private StarFallFlakeView starFallFlakeView;
	private CloudFlakeView cloudFlakeView;
	private Rose1FlakeView rose1FlakeView;
	private FlowerFlakeView flowerFlakeView;
	private SnowFlakeView snowFlakeView;
	private BlessingFlakeView blessingFlakeView;
	private ImageView ivChange;
	private LinearLayout ll_container;//添加特效的
	private boolean mIsFollow = false;

	private TextView tv_space_fans;
	private TextView tv_space_visitor;
	private TextView tv_ablum_title;

	private SpaceCustomHzListViewAdapter mHzListViewAdpater;
	private UnScrollGridview gv_photo;
	private TextView tv_foot_empty;
	private TextView tv_space_dynamic;
	private TextView tv_space_gift_title;

	private RelativeLayout rl_gift;
	private UnScrollGridview gv_gift;
	private TextView tv_gift_empty;
	private TextView tv_harem;

	private TextView tv_space_group;
	private View ll_space_group;
	private TextView tvHaremEmptey;
	private ReplyChildListView haremList;
	private HaremGroupAdapter haremAdapter;

	private LinearLayout ll_bottom;//底部四个按钮，如果是主人态，下面四个按钮就隐藏
	private static final int CREATE_HAREM_REQUEST_CODE = 1;

	private RelativeLayout rl_dynamic_layout;
	private boolean oldDyanmicData = true;
	private boolean newDynamicData = true;

	private ImageView dynamic_iv;
	private TextView dynamic_content_tv;
	private TextView tv_dynamic_time;
	private BAParseRspData parser;

	private PopupWindow rightPopupWindow;
	private ImageView title_iv_right;
	private TextView title_tv_right;
	private TextView mTvFollow;
	private DisplayImageOptions options;

	private ObservableScrollView sv_scroll;
	private LinearLayout ll_title;

	private UnScrollGridview gv_skill;
	private TextView tvSkillEmpty;
	private TextView tv_add_skill;
	private LinearLayout ll_skill_empty;
	private SpaceSkillAdapter skillAdapter;
	private LinearLayout ll_skill_title;
	private FrameLayout fl_skill_content;
	private TextView tv_skill_title;
	private TextView tv_skill_second_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void initData() {

		mHandler.removeCallbacksAndMessages(null);
		spaceBiz = new SpaceBiz(this, mHandler);
		roomsGetBiz = new RoomsGetBiz(this, mHandler);
		skillEngine = new GoddessSkillEngine();
		parser = new BAParseRspData();
		options = ImageOptionsUtils.getImageKeyOptions(this);

		Bundle bundle = getIntent().getExtras();
		mFriendUid = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, -1);
		mPicHead = bundle.getString(BAConstants.IntentType.MAINHALLFRAGMENT_HEADPIC);
		mNick = bundle.getString(BAConstants.IntentType.MAINHALLFRAGMENT_USERNICK);
		int sceneid = bundle.getInt(BAConstants.IntentType.MINE_GOODSID, -1);
		if (sceneid > 0) {//从道具商城预览过来
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.SPACE_SPECIAL_VALUE, sceneid, sceneid);
			isFromSecenid = true;
		} else {//请求是否有坐骑,背景
			isFromSecenid = false;
		}
		if (TextUtils.isEmpty(mNick)) {
			mNick = "";
		}
		mShowKey = bundle.getString(SpaceUtils.SHOWKEY);
		mLatestOnlineTime = bundle.getLong(BAConstants.IntentType.MAINHALLFRAGMENT_USERLATESTTIME, 0);
		mSex = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, Gender.MALE.getValue());

		if (mSex == Gender.MALE.getValue()) {
			tv_property_title.setText("财富值");
			tv_age.setBackgroundResource(R.drawable.activity_space_age_male_bg);
			OperateViewUtils.setTextViewLeftDrawable(this, R.drawable.icon_male, tv_age);
			ll_skill_title.setVisibility(View.GONE);
			fl_skill_content.setVisibility(View.GONE);
		} else {
			tv_property_title.setText(R.string.glamour);
			OperateViewUtils.setTextViewLeftDrawable(this, R.drawable.icon_female, tv_age);
			tv_age.setBackgroundResource(R.drawable.activity_space_age_female_bg);
			ll_skill_title.setVisibility(View.VISIBLE);
			fl_skill_content.setVisibility(View.VISIBLE);
		}

		mHzListViewAdpater = new SpaceCustomHzListViewAdapter(this, mFriendUid);
		gv_photo.setAdapter(mHzListViewAdpater);
		ArrayList<PhotoInfo> photoInfoList = new ArrayList<PhotoInfo>();
		for (int i = 0; i < 4; i++) {//拼接假数据
			photoInfoList.add(SpaceUtils.GetPhotoInfo());
		}
		mHzListViewAdpater.setList(photoInfoList);

		skillAdapter = new SpaceSkillAdapter(this, mFriendUid);
		gv_skill.setAdapter(skillAdapter);

		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if (userEntity != null && userEntity.uid.intValue() == mFriendUid) {//主人态
			tv_ablum_title.setText(R.string.str_my_album);
			tv_space_gift_title.setText(getString(R.string.str_my_gift) + " (0)");
			tv_harem.setText(R.string.str_my_harem);
			ll_bottom.setVisibility(View.GONE);
			rl_top_bg.setOnClickListener(this);
			tv_space_dynamic.setText(R.string.my_dynamic);
			title_iv_right.setVisibility(View.GONE);
			title_tv_right.setVisibility(View.VISIBLE);
			tv_distance.setVisibility(View.GONE);
			tv_skill_title.setText(R.string.str_what_can_i_do_for_you);
			tv_skill_second_title.setText(R.string.str_select_skill_for_service_to_me);
		} else {//客人太
			new UserInfoBiz(this).getDistance(this, mFriendUid, this);
			title_iv_right.setVisibility(View.VISIBLE);
			title_tv_right.setVisibility(View.GONE);
			tv_distance.setVisibility(View.VISIBLE);
			tv_skill_second_title.setText(R.string.str_select_skill_for_service);
			if (mSex == Gender.MALE.getValue()) {
				tv_ablum_title.setText("他的相册");
				tv_space_dynamic.setText(R.string.str_king_dynamic);
				tv_space_gift_title.setText(getString(R.string.str_he_gift) + " (0)");
				tv_harem.setText(R.string.str_male_other_harem);
				tv_skill_title.setText(getString(R.string.str_what_can_he_do_for_you));
			} else {
				tv_ablum_title.setText("她的相册");
				tv_space_gift_title.setText(getString(R.string.str_her_gift) + " (0)");
				tv_harem.setText(R.string.str_female_other_harem);
				tv_space_dynamic.setText(R.string.queena_dynamic);
				tv_skill_title.setText(getString(R.string.str_what_can_she_do_for_you));
			}
			if (userEntity != null) {
				new SpaceCustomBiz().addFootprint(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), mFriendUid);
			}
		}
		setuserInfo();

		haremAdapter = new HaremGroupAdapter(this, mFriendUid);
		haremList.setAdapter(haremAdapter);
		refreshData();
	}

	private void refreshData() {
		roomsGetBiz.getSingleRoomInfo(-1, mFriendUid);
		getAlbumPhotoList();
		getUserInfo();
		getRelationship();
		getUserProperty();
		spaceBiz.getUserPropertyFanse(mFriendUid);
		getTopicList(SpaceBiz.REFRESH_CODE);
		if (!isFromSecenid) {//如果从道具商城预览过来就不请求
			spaceBiz.getSpecialEffect(mFriendUid);
		}
		CreateHarem.getInstance().getRelevantGroupList(this, mFriendUid, mHandler);//获取后宫列表
		//		getNewDynamicList();
		getPersonSkill();
	}

	protected void getPersonSkill() {
		skillEngine.requestPersonSkillInfo(mFriendUid, this);
	}

	protected void getAlbumPhotoList() {//获取六张公开相册
		spaceBiz.getAlbumPhotoListData(mFriendUid, 0, -1, 4);
	}

	protected void getUserInfo() {//获取用户的信息
		spaceBiz.getUserInfo(mFriendUid);
	}

	protected void getUserProperty() {//获取到用户的财富值，比如魅力值和金币
		spaceBiz.getUserProperty(mFriendUid);
	}

	protected void getRelationship() {//获取我与该用户的关系
		spaceBiz.getRelationship(mFriendUid);
	}

	protected void getTopicList(int code) {
		spaceBiz.getUserTopicList(mFriendUid, code);
	}

	protected void addFollow() {
		spaceBiz.addFollow(mFriendUid);
	}

	protected void deleteFollow() {
		if (BAApplication.mLocalUserInfo != null) {
			SpaceRelationshipBiz biz = new SpaceRelationshipBiz(this);
			biz.delFollowByUid(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, mFriendUid,
					BAApplication.mLocalUserInfo.uid.intValue(), this);
		}
	}

	@SuppressWarnings("unchecked")
	public void onEvent(NoticeEvent event) {
		super.onEvent(event);
		if (event.getFlag() == NoticeEvent.NOTICE26) {//发帖回来,上传照片都要刷新的
			getAlbumPhotoList();//获取相册
			getTopicList(SpaceBiz.REFRESH_CODE);
		} else if (event.getFlag() == NoticeEvent.NOTICE62) {
			getUserInfo();
		} else if (event.getFlag() == NoticeEvent.NOTICE65) {
			CreateHarem.getInstance().getRelevantGroupList(this, mFriendUid, mHandler);//获取后宫列表
		} else if (event.getFlag() == NoticeEvent.NOTICE96) {
			getPersonSkill();
		}

	}

	private void setPetAnim(int source) {//设置座驾
		iv_pet.setBackgroundResource(source);//获取AnimationDrawable动画对象
		anim_pet = (AnimationDrawable) iv_pet.getBackground();
		anim_pet.start();
	}

	private void setSpecial(View view, boolean isRose) {//设置特效
		if (!isRose) {
			ll_container.setBackgroundColor(this.getResources().getColor(R.color.fall_bg_color));
		}
		ll_container.removeAllViews();
		ll_container.addView(view);
	}

	protected void getNewDynamicList() {
		DynamicRequseControl control = new DynamicRequseControl();
		control.getDynamicList(-1, 1, 1 | (1 << 30), mFriendUid, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.SPACE_ALBUM_PHOTO_LIST_VALUE://获取到公开相册列表数据
			List<PhotoInfo> photos = (List<PhotoInfo>) msg.obj;
			if (photos != null) {
				if (!ListUtils.isEmpty(photos)) {
					Collections.reverse(photos);
				} else {
					for (int i = 0; i < 4; i++) {
						photos.add(SpaceUtils.GetPhotoInfo());
					}
				}
				mHzListViewAdpater.setList(photos);
			}
			break;
		case HandlerValue.SPACE_USER_INFO_VALUE://获取到用户的基本信息，比如昵称等数据
			if (msg.arg1 == 0) {
				GoGirlUserInfo userInfo1 = (GoGirlUserInfo) msg.obj;
				if (userInfo1 != null && userInfo1.uid.intValue() == mFriendUid) {
					mPicHead = new String(userInfo1.headpickey);
					mShowKey = new String(userInfo1.showpickey);
					mNick = new String(userInfo1.nick);
					mLatestOnlineTime = userInfo1.lastlogtime.longValue();
					String gradeinfo = new String(userInfo1.gradeinfo);
					if (TextUtils.isEmpty(gradeinfo)) {
						iv_userlevel.setVisibility(View.GONE);
					} else {
						iv_userlevel.setVisibility(View.VISIBLE);
						//						DisplayImageOptions campaignHatOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
						//								.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).build();
						DisplayImageOptions campaignHatOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(this);
						String[] gradeinfos = gradeinfo.split(",");
						if (gradeinfos != null && gradeinfos.length == 2) {//第一位为id,第二位为等级头像
							//							String key = "http://" + gradeinfos[1] + "@false@640@0";
							//							imageLoader.displayImage(key, iv_userlevel, campaignHatOptions);
							GradeInfoImgUtils.loadGradeInfoImgInRL(this, imageLoader, campaignHatOptions, gradeinfo, iv_userlevel);
						}
					}

					if (userInfo1.forbidtime.intValue() > 0 || (userInfo1.userstatus.intValue() & SwitchStatus.GG_US_BAN_FLAG) > 0) {
						iv_tips.setVisibility(View.VISIBLE);
						if ((userInfo1.userstatus.intValue() & SwitchStatus.GG_US_BAN_FLAG) > 0) {
							iv_tips.setImageResource(R.drawable.homepage_tips_termination);
						} else {
							iv_tips.setImageResource(R.drawable.homepage_tips_gag);
						}

					} else {
						iv_tips.setVisibility(View.GONE);
					}
					authKey = new String(userInfo1.authpickey);

					if (userInfo1.authpickey != null && !TextUtils.isEmpty(new String(userInfo1.authpickey)) && (userInfo1.userstatus.intValue() & SwitchStatus.GG_US_AUTH_FLAG) > 0) {
						identifyImage.setVisibility(View.VISIBLE);
						identifyImage.setBackgroundResource(R.drawable.homepage_icon_identifed);
						GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
						if (userEntity != null && userEntity.uid.intValue() == mFriendUid) {
							SharedPreferencesTools.getInstance(this, userEntity.uid.intValue() + "").saveBooleanKeyValue(true,
									BAConstants.PEIPEI_IDENTY);
						}
					} else {
						identifyImage.setVisibility(View.INVISIBLE);
						GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
						if (userEntity != null && userEntity.uid.intValue() == mFriendUid) {
							SharedPreferencesTools.getInstance(this, userEntity.uid.intValue() + "").saveBooleanKeyValue(false,
									BAConstants.PEIPEI_IDENTY);
						}
					}
					if ((userInfo1.userstatus.intValue() & SwitchStatus.GG_US_FIRST_RECHARGE_FLAG) > 0) {
						rechargeImage.setVisibility(View.VISIBLE);
					} else {
						rechargeImage.setVisibility(View.GONE);
					}

					String mBirthday = new String(BaseTimes.getFormatTime(userInfo1.birthday.longValue() * 1000));
					tv_age.setText(OperateViewUtils.calculateAge(mBirthday));

					identifyImage.setClickable(true);
					setuserInfo();
				}
			}
			break;
		case HandlerValue.SPACE_GET_RELATIONSHIP_VALUE://说明已经关注过该用户
			mTvFollow.setClickable(true);
			if (msg.arg2 == BAConstants.RelationshipType.followed) {//说明已经关注
				mTvFollow.setText("已关注");
				mIsFollow = true;
			}
			break;
		case HandlerValue.SPACE_TOPIC_LIST_VALUE:
			TopicInfoList infolist = (TopicInfoList) msg.obj;
			if (!ListUtils.isEmpty(infolist)) {
				List<TopicInfo> oldDynamicList = new LinkedList<TopicInfo>();
				oldDynamicList.addAll(infolist);
				for (Object object : infolist) {
					TopicInfo topicinfo = (TopicInfo) object;
					GoGirlDataInfoList dataInfoList = topicinfo.topiccontentlist;
					for (Object object2 : dataInfoList) {
						GoGirlDataInfo dataInfo = (GoGirlDataInfo) object2;
						if (dataInfo.type.intValue() == BAConstants.MessageType.GIFT.getValue()) {//礼物数据单独拿出来
							if (oldDynamicList != null && oldDynamicList.contains(topicinfo)) {
								if (topicinfo != null) {
									oldDynamicList.remove(topicinfo);
								}
							}

							GiftFeedInfo giftFeedInfo = new GiftFeedInfo();
							BERDecoder dec = new BERDecoder(dataInfo.data);
							try {
								giftFeedInfo.decode(dec);
								if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {
									tv_space_gift_title.setText(getString(R.string.str_my_gift) + " (" + giftFeedInfo.total.intValue() + ")");
								} else {
									if (mSex == Gender.MALE.getValue()) {
										tv_space_gift_title.setText(getString(R.string.str_he_gift) + " (" + giftFeedInfo.total.intValue() + ")");
									} else {
										tv_space_gift_title.setText(getString(R.string.str_her_gift) + " (" + giftFeedInfo.total.intValue() + ")");
									}
								}
								GiftDealInfoList giftInfoList = giftFeedInfo.giftdeallist;//做礼物列表数据展示
								if (giftInfoList != null && !giftInfoList.isEmpty()) {
									SpaceGiftAdapter giftAdapter = new SpaceGiftAdapter(this, mSex, mFriendUid);
									gv_gift.setAdapter(giftAdapter);
									Collections.reverse(giftInfoList);
									giftAdapter.setList(giftInfoList);
									gv_gift.setVisibility(View.VISIBLE);
									tv_gift_empty.setVisibility(View.GONE);
								} else {
									gv_gift.setVisibility(View.GONE);
									tv_gift_empty.setVisibility(View.VISIBLE);
								}
							} catch (ASN1Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				}

				if (oldDynamicList.size() > 0) {
					TopicInfo info = oldDynamicList.get(0);
					setOldDynamic(info);
				}
			}
			if (BAApplication.mLocalUserInfo != null) {
				if (BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {//是自己才去加载本地失败数据
					BAApplication.getInstance().getJobManager().addJobInBackground(new GetFailureOrSendingTopicFromDbJob(this, false));
				}
			}
			if (ListUtils.isEmpty(infolist) || (infolist.size() == 1 && newDynamicData)) {//空数据
				tv_foot_empty.setVisibility(View.VISIBLE);
				rl_dynamic_layout.setVisibility(View.GONE);
			} else {
				oldDyanmicData = false;
			}
			getNewDynamicList();
			break;
		case HandlerValue.SPACE_ADD_FOLLOW_VALUE:
			BaseUtils.showTost(this, "关注成功");
			mTvFollow.setText("已关注");
			mIsFollow = true;
			break;
		case HandlerValue.SPACE_UPLOAD_BG_VALUE:
			if (msg.arg1 == 0) {//上传形象照成功
				getUserInfo();
				BaseUtils.showTost(this, "上传成功");
			} else {//上传形象照失败
				BaseUtils.showTost(this, R.string.str_update_error);
			}
			break;
		case HandlerType.OPERATE_SUCCESS:
			BaseUtils.showTost(this, R.string.share_success);
			if (BAApplication.mLocalUserInfo != null) {
				SpaceCustomBiz sBiz = new SpaceCustomBiz();
				if (BAApplication.mLocalUserInfo != null) {
					sBiz.shareTask(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
							msg.arg1);
				}
			}
			break;
		case HandlerValue.SPACE_DELETE_TOPICINFO_VALUE://删帖回来
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, R.string.str_delete_success);
				//				if (mAdapter.getCount() > msg.arg2)
				//					mAdapter.removePos(msg.arg2);
			} else {
				BaseUtils.showTost(this, R.string.str_delete_failed);
			}
			break;
		case HandlerValue.SPACE_USER_PROPERTY_VALUE:
			UserPropertyInfo userPropertyInfo = (UserPropertyInfo) msg.obj;
			if (userPropertyInfo != null) {
				if (mSex == Gender.MALE.getValue()) {
					tv_property_value.setText(String.valueOf(userPropertyInfo.fortunenum.intValue()));
				} else {
					tv_property_value.setText(String.valueOf(userPropertyInfo.charmnum.intValue()));
				}
			}
			break;
		case HandlerValue.HAREM_GET_GROUP_LIST_SUCCESS_VALUE:
			GroupInfoList infoList = (GroupInfoList) msg.obj;
			if (infoList != null) {
				if (BAApplication.mLocalUserInfo == null || BAApplication.mLocalUserInfo.uid.intValue() != mFriendUid) {//客态
					if (infoList.isEmpty()) {
						tvHaremEmptey.setText(getString(R.string.str_other_side_not_create_harem));
						tvHaremEmptey.setVisibility(View.VISIBLE);
					}
				} else if (BAApplication.mLocalUserInfo == null || BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {//主态
					boolean isCreate = false;
					for (Object object : infoList) {
						GroupInfo groupinfo = (GroupInfo) object;
						if (groupinfo != null) {
							if (groupinfo.owner.intValue() == mFriendUid) {
								//我创建的后宫
								isCreate = true;
							}
						}
					}
					if (isCreate) {
						ll_space_group.setVisibility(View.GONE);
					} else {
						ll_space_group.setVisibility(View.VISIBLE);
					}
				}
				haremAdapter.setList(infoList);
			}
			break;
		case DELETEFOLLOW:
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, "取消关注成功");
				mTvFollow.setText("关注");
				mIsFollow = false;

				SharedPreferencesTools.getInstance(this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").remove(mFriendUid + "");
			}
			break;
		case HandlerValue.SPACE_USER_FANSE_VALUE:
			int fansCount = msg.arg1;
			int visitorCount = msg.arg2;
			if (BAApplication.mLocalUserInfo != null) {
				if (mFriendUid == BAApplication.mLocalUserInfo.uid.intValue()) {//说明是自己的空间
					int oldFansCount = SharedPreferencesTools.getInstance(this).getIntValueByKey(mFriendUid + "_fans_count", -1);
					int oldVisitorCount = SharedPreferencesTools.getInstance(this).getIntValueByKey(mFriendUid + "_visitor_count", -1);
					if (oldFansCount > 0 && (fansCount - oldFansCount) > 0) {
						tv_new_increase_fans.setText(String.valueOf("+" + (fansCount - oldFansCount)));
					}
					if (oldVisitorCount > 0 && (visitorCount - oldVisitorCount) > 0) {
						tv_new_increase_visitor.setText(String.valueOf("+" + (visitorCount - oldVisitorCount)));
					}

					SharedPreferencesTools.getInstance(this).saveIntKeyValue(fansCount, mFriendUid + "_fans_count");
					SharedPreferencesTools.getInstance(this).saveIntKeyValue(visitorCount, mFriendUid + "_visitor_count");
				}
			}

			tv_space_fans.setText(String.valueOf(fansCount));
			tv_space_visitor.setText(String.valueOf(visitorCount));
			break;
		case HandlerValue.SPACE_SPECIAL_VALUE:
			if (msg.arg1 == SpaceBiz.DEER_RIDING_VALUE) {//小鹿座驾
				setPetAnim(R.anim.fat_po_deer);
			} else if (msg.arg1 == SpaceBiz.BIRD_RIDING_VALUE) {//九头鸟座驾
				setPetAnim(R.anim.fat_po_bird);
			} else if (msg.arg1 == SpaceBiz.PEACOCK_RIDING_VALUE) {//孔雀座驾
				setPetAnim(R.anim.fat_po_peacock);
			} else if (msg.arg1 == SpaceBiz.CAR_MOTORING) {
				setPetAnim(R.anim.fat_po_car);
			} else if (msg.arg1 == SpaceBiz.BEAST_VALUE){
				setPetAnim(R.anim.fat_po_beast);
			}
			if (msg.arg2 == SpaceBiz.ROSE_SPECIAL_VALUE) {//玫瑰雨特效
				if (rose1FlakeView == null)
					rose1FlakeView = new Rose1FlakeView(this);
				setSpecial(rose1FlakeView, true);

			} else if (msg.arg2 == SpaceBiz.STAR_FALL_SPECIAL_VALUE) {//流星雨特效
				if (starFallFlakeView == null)
					starFallFlakeView = new StarFallFlakeView(this);
				setSpecial(starFallFlakeView, false);
			} else if (msg.arg2 == SpaceBiz.CLOUD_SPECIAL_VALUE) {//祥云漂移特效
				if (cloudFlakeView == null)
					cloudFlakeView = new CloudFlakeView(this);
				setSpecial(cloudFlakeView, true);
			} else if (msg.arg2 == SpaceBiz.FLOWER_VALUE) {//百花经验
				if (flowerFlakeView == null)
					flowerFlakeView = new FlowerFlakeView(this);
				setSpecial(flowerFlakeView, true);
			} else if (msg.arg2 == SpaceBiz.CHANGE_VALUE) {//三十六变
				if (ivChange == null) {
					ivChange = new ImageView(this);
					ivChange.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				}
				setSpecial(ivChange, true);
				ivChange.setBackgroundResource(R.anim.fat_change);//获取AnimationDrawable动画对象
				anim_change = (AnimationDrawable) ivChange.getBackground();
				anim_change.start();
			} else if (msg.arg2 == SpaceBiz.SNOW_VALUE) { //大雪纷飞
				if (snowFlakeView == null)
					snowFlakeView = new SnowFlakeView(this);
				setSpecial(snowFlakeView, true);
			} else if(msg.arg2 == SpaceBiz.BLEESSING_VALUE){
				if (blessingFlakeView == null){ //福
					blessingFlakeView = new BlessingFlakeView(this);
					setSpecial(blessingFlakeView, true);
				}
			}
			break;
		case HandlerValue.SHOW_ROOM_GET_SINGLE_ROOM://获取是否在秀
			ShowRoomInfo roomInfo = (ShowRoomInfo) msg.obj;
			if (roomInfo != null) {
				if (roomInfo.getLefttime() > 0) {//说明在秀
					showRoomInfo = roomInfo;
					iv_onShow.setVisibility(View.VISIBLE);
					AnimationDrawable anim_onShow = (AnimationDrawable) iv_onShow.getBackground();
					anim_onShow.start();
				} else {
					iv_onShow.setVisibility(View.GONE);
				}
			} else {
				iv_onShow.setVisibility(View.GONE);
			}
			break;
		case HandlerValue.SHOW_ROOM_IN_OUT:
			BAApplication.clearShow();
			if (msg.arg1 == 0) {
				if (msg.arg2 == InOutAct.out) {
					BAApplication.showRoomInfo = showRoomInfo;
					BaseUtils.openActivity(this, PeipeiShowActivity.class);
				}
			} else {
				BaseUtils.showTost(this, "进入秀场失败");
			}
			break;
		case HandlerValue.SHOW_ROOM_CLOSE:
			BAApplication.clearShow();
			BAApplication.showRoomInfo = showRoomInfo;
			if (BAApplication.showRoomInfo != null)
				BaseUtils.openActivity(this, PeipeiShowActivity.class);
			break;
		case HandlerValue.GET_DYNAMIC_SUCCESS:
			DynamicsInfoList dynamicsInfoList = (DynamicsInfoList) msg.obj;
			if (dynamicsInfoList != null && dynamicsInfoList.size() > 0) {
				DynamicsInfo info = (DynamicsInfo) dynamicsInfoList.get(0);
				setNewDynamicPic(info);
				newDynamicData = false;
			} else {
				newDynamicData = true;
			}
			if (oldDyanmicData && newDynamicData) {
				tv_foot_empty.setVisibility(View.VISIBLE);
				rl_dynamic_layout.setVisibility(View.GONE);
			} else {
				tv_foot_empty.setVisibility(View.GONE);
				rl_dynamic_layout.setVisibility(View.VISIBLE);
			}
			break;
		case HandlerValue.USER_DISTANCE_VALUE:
			if (msg.arg1 < 0) {
				tv_distance.setText("未知");
				new GpsLocationThread(getApplicationContext()).start();
			} else {
				tv_distance.setText(BaseTimes.getDistance(msg.arg1));
			}
			break;
		case HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_SUCCESS:
			if (msg.arg1 == 0) {
				SkillTextInfoList list = (SkillTextInfoList) msg.obj;
				if (!ListUtils.isEmpty(list)) {
					skillAdapter.clearList();
					skillAdapter.appendToList(skillAdapter.getGoddessSkillListData(list));
					GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
					if (userEntity != null && userEntity.uid.intValue() == mFriendUid) {//主人态
						skillAdapter.appendToList(new SkillTextInfo());
					}
					ll_skill_empty.setVisibility(View.GONE);
				} else {
					setSkillEmptyView();
				}
			} else {
				setSkillEmptyView();
			}
			break;
		case HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			setSkillEmptyView();
			break;
		}
	}

	private void setSkillEmptyView() {
		skillAdapter.clearList();
		skillAdapter.notifyDataSetChanged();
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		ll_skill_empty.setVisibility(View.VISIBLE);
		if (userEntity != null && userEntity.uid.intValue() == mFriendUid) {//主人态
			tvSkillEmpty.setText(R.string.str_unadd_skill);
			tv_add_skill.setText(R.string.str_space_add_skill);
			tv_add_skill.setOnClickListener(addSkillClickListener);
		} else {
			tvSkillEmpty.setText(getString(R.string.str_unadd_skill_talk_it, mSex == 0 ? "她" : "他"));
			tv_add_skill.setText(R.string.str_talk);
			tv_add_skill.setOnClickListener(talkClickListener);
		}
	}

	private OnClickListener talkClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			ClickBottomButton(0);
		}
	};

	private OnClickListener addSkillClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			GoddessSkillListActivity.openMineFaqActivity(SpaceActivity.this);
		}
	};

	private void setNewDynamicPic(DynamicsInfo info) {
		GoGirlDataInfoList dataInfoList = info.dynamicscontentlist;
		final ContentData data = parser.parseTopicInfo(this, dataInfoList, info.sex.intValue());
		int styte = info.revint0.intValue();
		//判断动态类型，0-官方 ，1-用户
		if (styte == 1) {
			setDefaultDynamic();

			String tt = BaseTimes.getTime(info.createtime.longValue() * 1000);
			dynamic_iv.setVisibility(View.VISIBLE);
			tv_dynamic_time.setText(tt);
			if (info.dynamicsstatus.intValue() == 2 || BAApplication.mLocalUserInfo.uid.intValue() == info.uid.intValue()) {//图片审核通过
				ILog.d("DYH", "来来来，图片审核通过咯，到这里来了");
				String imgKey = data.getImageList().get(0) + "@false@500@500";
				imageLoader.displayImage("http://" + imgKey, dynamic_iv, options);
				dynamic_content_tv.setText(data.getContent());
			} else {//图片审核不通过
				ILog.d("DYH", "来来来，图片审核不通过，到这里来了");
				String bgColor = new String(info.revstr0);//背景颜色值
				String content = data.getContent();//内容
				dynamic_content_tv.setText(content);
				imageLoader.cancelDisplayTask(dynamic_iv);
				if (!TextUtils.isEmpty(bgColor)) {
					dynamic_iv.setBackgroundColor(Color.parseColor("#" + bgColor));
				} else {
					dynamic_iv.setBackgroundColor(getResources().getColor(R.color.official_bg1));
				}
			}
		}
	}

	private void setDefaultDynamic() {
		dynamic_iv.setImageDrawable(null);
		dynamic_iv.setBackgroundDrawable(null);
		dynamic_content_tv.setText("");
	}

	private void setOldDynamic(TopicInfo topicInfo) {
		if (null != topicInfo) {
			ILog.d("DYH", "来来来，来设置老动态咯");
			GoGirlDataInfoList dataInfoList = topicInfo.topiccontentlist;
			final ContentData data = parser.parseTopicInfo(this, dataInfoList, topicInfo.sex.intValue());
			//设置gridview的显示和隐藏,以及只有一张图片时的UI,注意这里顺序不能调整
			if (data.getImageList() != null) {
				if (data.getType() == BAConstants.MessageType.UPLOAD_PHOTO.getValue()) {//礼物，上传照片，还有一种就是发布心情和加照片
					setOploadPhotoDynamicPic(data);
				} else if (data.getType() == BAConstants.MessageType.GIFT.getValue()) {
					setGiftDynamicPic(data);
				} else {//相册动态
					setPhotoDynamicPic(data);
				}
			}

			String tt = BaseTimes.getDiffTime2(Long.valueOf(topicInfo.createtime.longValue() * 1000));
			tv_dynamic_time.setText(tt);
			dynamic_content_tv.setText(ParseMsgUtil.convetToHtml(data.getContent(), this, BaseUtils.dip2px(this, 24)));
		}
	}

	private void setPhotoDynamicPic(final ContentData data) {
		if (data.getImageList().size() > 0) {
			if (data.getImageList().size() > 1) {
				String key = data.getImageList().get(0);
				imageLoader.displayImage("http://" + key + "@false@" + 245 + "@" + 245, dynamic_iv, options);
				if (key.contains("/sdcard") || key.contains("/mnt")) {
					imageLoader.displayImage("file://" + key, dynamic_iv, options, null);
				}
			} else {
				if (data.getImageList().get(0).contains("sdcard")) {
					dynamic_iv.setImageBitmap(BaseFile.getImageFromFile(data.getImageList().get(0)));
				} else {
					String key = data.getImageList().get(0) + "@false@500@500";
					//						mViewholer.ivOneImage.setImageResource(R.drawable.main_img_defaultpic_big);
					//						imageLoaderMy.addTask(key, mViewholer.ivOneImage);
					imageLoader.displayImage("http://" + key, dynamic_iv, options);
				}
			}
			//显示一张图片
			dynamic_iv.setVisibility(View.VISIBLE);
		} else {
			dynamic_iv.setVisibility(View.GONE);
		}
	}

	private void setGiftDynamicPic(final ContentData data) {
		if (data.getImageList().size() > 0) {
			if (data.getImageList().size() > 1) {
				String key = data.getImageList().get(0);
				imageLoader.displayImage("http://" + key + "@false@" + 180 + "@" + 180, dynamic_iv, options);

				if (key.contains("/sdcard") || key.contains("/mnt")) {
					imageLoader.displayImage("file://" + key, dynamic_iv, options, null);
				}
			} else {
				String key = data.getImageList().get(0) + BAConstants.LOAD_180_APPENDSTR;
				imageLoader.displayImage("http://" + key, dynamic_iv, options);
				//显示一张图片
			}
			dynamic_iv.setVisibility(View.VISIBLE);
		} else {
			dynamic_iv.setVisibility(View.GONE);
		}
	}

	private void setOploadPhotoDynamicPic(final ContentData data) {
		if (data.getImageList().size() > 0) {
			if (data.getImageList().size() > 1) {
				String key = data.getImageList().get(0);
				imageLoader.displayImage("http://" + key + "@false@" + 245 + "@" + 245, dynamic_iv, options);

				if (key.contains("/sdcard") || key.contains("/mnt")) {
					imageLoader.displayImage("file://" + key, dynamic_iv, options, null);
				}
			} else {
				String key = data.getImageList().get(0) + BAConstants.LOAD_210_APPENDSTR;
				//					mViewholer.ivOneImage.setImageResource(R.drawable.main_img_defaultpic_big);
				imageLoader.displayImage("http://" + key, dynamic_iv, options);
				//					imageLoaderMy.addTask(key, mViewholer.ivOneImage);
				//发布心情显示，其他都不显示  三种动态，上传照片，礼物不显示
				//显示一张图片

			}
			dynamic_iv.setVisibility(View.VISIBLE);
		} else {
			dynamic_iv.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.space_custom_talk_ll:
			ClickBottomButton(0);
			break;
		case R.id.space_custom_gift_tv:
			ClickBottomButton(1);
			break;
		case R.id.title_iv_right:
			ClickBottomButton(3);
			break;
		case R.id.space_custom_attention_tv:
			ClickBottomButton(2);//友盟统计在这个方法里面
			break;
		case R.id.space_custom_warn_tv:
			ClickBottomButton(4);
			break;
		case R.id.title_tv_right:
			if (BAApplication.mLocalUserInfo != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
				if (BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {
//					MobclickAgent.onEvent(BAApplication.getInstance(), "zhurentaidianjitouxiang", map);
				} else {
//					MobclickAgent.onEvent(BAApplication.getInstance(), "kerentaidianjitouxiang", map);
				}
			}
			Bundle bundle = new Bundle();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, mFriendUid);
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, mSex);
			BaseUtils.openActivity(this, MineSettingUserInfoActivity.class, bundle);
			break;
		case R.id.rl_space_bg://设置自己的背景
			new PhotoSetDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog(0, 0);
			break;
		case R.id.head_custom_include_iv_head:
			if (!TextUtils.isEmpty(mPicHead)) {
				SpaceAvatarActivity.openMineFaqActivity(this, mPicHead);
			}
			break;
		case R.id.ll_space_glamour:
			if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {//只有主人太才可以有点击事件
				MineFaqActivity.openMineFaqActivity(this, MineFaqActivity.LEVEL_VALUE);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//				MobclickAgent.onEvent(BAApplication.getInstance(), "zhurentaidianjimeilizhi", map);
			}
			break;
		case R.id.ll_space_vistor://到访问页面
			if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {//只有主人太才可以有点击事件
				tv_new_increase_visitor.setText("");
				BaseUtils.openActivity(this, MessageVisitorActivity.class);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//				MobclickAgent.onEvent(BAApplication.getInstance(), "zhurentaidianjifanwenliang", map);
			}
			break;
		case R.id.ll_space_fans:
			if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {//只有主人太才可以有点击事件
				tv_new_increase_fans.setText("");
				BaseUtils.openActivity(this, MainFriendsActivity.class);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//				MobclickAgent.onEvent(BAApplication.getInstance(), "zhurentaidianjifensiliang", map);
			}
			break;
		case R.id.iv_userlevel:
			MineFaqActivity.openMineFaqActivity(this, MineFaqActivity.LEVEL_VALUE);
			break;
		case R.id.tv_space_group://后宫
			BaseUtils.openResultActivity(this, CreateHaremActivity.class, null, CREATE_HAREM_REQUEST_CODE);
			break;
		case R.id.tv_harem_rule:
			MineFaqActivity.openMineFaqActivity(this, MineFaqActivity.GROUP_VALUE);
			break;
		case R.id.ll_space_title_click:
			if (showRoomInfo != null) {
				if (BAApplication.mLocalUserInfo != null) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//					MobclickAgent.onEvent(this, "gerenzhuyejinruxiuchang", map);
				}
				if (showRoomInfo.getRoomid() > 0 && showRoomInfo.getOwneruserinfo().getUid() > 0) {//在秀的房间才处理点击事件
					if (BAApplication.showRoomInfo == null) {//第一次点击进房间，进入房间后调用进入秀场接口
						BAApplication.showRoomInfo = showRoomInfo;
						BaseUtils.openActivity(this, PeipeiShowActivity.class);
					} else {//已在房间内
						if (showRoomInfo.getOwneruserinfo().getUid() == BAApplication.showRoomInfo.getOwneruserinfo().getUid()) {//同一个房间，再次进入，不再调用进入秀场操作
							BAApplication.showRoomInfo = showRoomInfo;
							BaseUtils.openActivity(this, PeipeiShowActivity.class);
						} else {
							if (BAApplication.showRoomInfo.getOwneruserinfo().getUid() == BAApplication.mLocalUserInfo.uid.intValue()) {//不同房间，且所在房间是自己开的，切换到其他房间
								new FinshShowDialog(this, R.string.str_show_change_room, R.string.ok, R.string.cancel, roomsGetBiz).showDialog();
							} else {//切换到其他房间，调用退出秀场接口，在接口返回中进入秀场
								roomsGetBiz.InOutRooms(InOutAct.out, BAApplication.showRoomInfo.getRoomid(),
										BAApplication.mLocalUserInfo.uid.intValue());
							}
						}
					}
				}
			}

			break;
		case R.id.rl_dynamic_layout:
			SpaceAllDynamicActivity.openMineFaqActivity(this, mFriendUid, mSex);
			break;
		}
	}

	private void ClickBottomButton(int type) {//点击的是底部
		if (UserUtils.getUserEntity(this) == null) {
			new GoLoginDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog();
		} else {
			switch (type) {
			case 0://私聊
				ChatActivity.intentChatActivity(this, mFriendUid, mNick, mSex, false, false, 0);
				break;
			case 1://送礼
				Bundle b = new Bundle();
				b.putInt("fuid", mFriendUid);
				b.putString("fNick", mNick);
				BaseUtils.openActivity(this, StoreGiftListActivity.class, b);
				break;
			case 2:
				if (!mIsFollow) {
					addFollow();
				} else {
					deleteFollow();
				}
				dismissPopWindow();
				break;
			case 3:
				showPopWindow();
				break;
			case 4:
				//				new ReportDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mFriendUid, 0, false).showDialog(0, 0);
				ReportActivity.openMineFaqActivity(this, mFriendUid);
				dismissPopWindow();
				break;
			}
		}
	}

	private void showPopWindow() {
		if (rightPopupWindow != null)
			rightPopupWindow.showAsDropDown(title_iv_right, -90, 0);
	}

	private void dismissPopWindow() {
		if (rightPopupWindow != null)
			rightPopupWindow.dismiss();
	}

	@Override
	protected void initRecourse() {
		int screenWidth = BasePhone.getScreenWidth(this);
		iv_onShow = (ImageView) findViewById(R.id.iv_onshow);
		rl_top_bg = (RelativeLayout) findViewById(R.id.rl_space_bg);
		setTopViewHeight();
		mIvHead = (ImageView) findViewById(R.id.head_custom_include_iv_head);
		identifyImage = (ImageView) findViewById(R.id.iv_identify);
		iv_tips = (ImageView) findViewById(R.id.head_custom_include_iv_tips);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);//标题
		iv_userlevel = (ImageView) findViewById(R.id.iv_userlevel);
		tv_id = (TextView) findViewById(R.id.tv_id);
		tv_age = (TextView) findViewById(R.id.tv_age);
		iv_pet = (ImageView) findViewById(R.id.iv_pet);
		tv_onLine = (TextView) findViewById(R.id.tv_online);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		rechargeImage = (ImageView) findViewById(R.id.iv_recharge);
		tv_property_title = (TextView) findViewById(R.id.tv_space_glamour_title);
		tv_property_value = (TextView) findViewById(R.id.tv_space_glamour);
		tv_new_increase_fans = (TextView) findViewById(R.id.tv_new_increase_fans);
		tv_new_increase_visitor = (TextView) findViewById(R.id.tv_new_increase_vistor);
		tv_space_visitor = (TextView) findViewById(R.id.tv_space_vistor);
		tv_ablum_title = (TextView) findViewById(R.id.tv_space_album);
		tv_space_fans = (TextView) findViewById(R.id.tv_space_fans);
		gv_photo = (UnScrollGridview) findViewById(R.id.space_custom_head_gv_photo);
		gv_photo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenWidth - BaseUtils.dip2px(this, 15)) / 4
				+ BaseUtils.dip2px(this, 6)));
		tv_space_dynamic = (TextView) findViewById(R.id.tv_space_dynamic);
		tv_foot_empty = (TextView) findViewById(R.id.space_custom_footer_empyt);
		tv_space_gift_title = (TextView) findViewById(R.id.tv_space_gift);
		rl_gift = (RelativeLayout) findViewById(R.id.rl_space_gift);
		rl_gift.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenWidth - BaseUtils.dip2px(this, 15)) / 4
				+ BaseUtils.dip2px(this, 6)));
		gv_gift = (UnScrollGridview) findViewById(R.id.space_custom_head_gridview);
		tv_gift_empty = (TextView) findViewById(R.id.space_custom_gift_empty_tv);
		tv_harem = (TextView) findViewById(R.id.tv_harem);
		TextView tv = (TextView) findViewById(R.id.tv_harem_rule);
		tv.setOnClickListener(this);
		tv_space_group = (TextView) findViewById(R.id.tv_space_group);
		ll_space_group = findViewById(R.id.ll_space_group);
		tvHaremEmptey = (TextView) findViewById(R.id.tv_harem_empty);
		haremList = (ReplyChildListView) findViewById(R.id.lv_harem);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_space_bottom);//底部四个按钮
		ll_container = (LinearLayout) findViewById(R.id.container);
		rl_dynamic_layout = (RelativeLayout) findViewById(R.id.rl_dynamic_layout);
		dynamic_iv = (ImageView) findViewById(R.id.dynamic_iv);
		dynamic_content_tv = (TextView) findViewById(R.id.dynamic_content_tv);
		tv_dynamic_time = (TextView) findViewById(R.id.tv_dynamic_time);
		title_iv_right = (ImageView) findViewById(R.id.title_iv_right);
		title_tv_right = (TextView) findViewById(R.id.title_tv_right);
		sv_scroll = (ObservableScrollView) findViewById(R.id.sv_scroll);
		ll_title = (LinearLayout) findViewById(R.id.ll_title);

		gv_skill = (UnScrollGridview) findViewById(R.id.gv_skill);
		tvSkillEmpty = (TextView) findViewById(R.id.tv_skill_empty_data);
		tv_add_skill = (TextView) findViewById(R.id.tv_add_skill);
		ll_skill_empty = (LinearLayout) findViewById(R.id.ll_skill_empty);
		ll_skill_title = (LinearLayout) findViewById(R.id.ll_skill_title);
		fl_skill_content = (FrameLayout) findViewById(R.id.fl_skill_content);
		tv_skill_title = (TextView) findViewById(R.id.tv_skill_title);
		tv_skill_second_title = (TextView) findViewById(R.id.tv_skill_second_title);

		initRightPopupWindow();
		setClickListener();
	}

	private void initRightPopupWindow() {
		View view = View.inflate(this, R.layout.activity_space_right_popupwindow, null);
		mTvFollow = (TextView) view.findViewById(R.id.space_custom_attention_tv);
		view.findViewById(R.id.space_custom_warn_tv).setOnClickListener(this);
		mTvFollow.setOnClickListener(this);
		mTvFollow.setClickable(false);
		rightPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightPopupWindow.setTouchable(true);
		rightPopupWindow.setOutsideTouchable(true);
		rightPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		// 设置动画效果
		rightPopupWindow.setAnimationStyle(R.style.anim_popwindow_alpha);
	}

	private void setClickListener() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		findViewById(R.id.ll_space_title_click).setOnClickListener(this);
		title_iv_right.setOnClickListener(this);
		title_tv_right.setOnClickListener(this);
		iv_onShow.setOnClickListener(this);
		iv_userlevel.setOnClickListener(this);
		findViewById(R.id.ll_space_glamour).setOnClickListener(this);
		findViewById(R.id.ll_space_vistor).setOnClickListener(this);
		findViewById(R.id.ll_space_fans).setOnClickListener(this);
		tv_space_group.setOnClickListener(this);
		findViewById(R.id.space_custom_talk_ll).setOnClickListener(this);
		findViewById(R.id.space_custom_gift_tv).setOnClickListener(this);
		rl_dynamic_layout.setOnClickListener(this);
		mIvHead.setOnClickListener(this);
		haremList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GroupInfo info = haremAdapter.getList().get(position);
				boolean isHost = false;
				if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == mFriendUid) {//说明进入的是自己个人主页，底部不可见
					isHost = true;
				}
				if (info != null && info.owner.intValue() == mFriendUid) {
					HaremUtils.intentManagerHaremActivity(SpaceActivity.this, info, true, isHost);
				} else {
					HaremUtils.intentManagerHaremActivity(SpaceActivity.this, info, false, isHost);
				}

			}
		});

		sv_scroll.setScrollListener(scrollListener);
		gv_skill.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GoGirlUserInfo userEntity = UserUtils.getUserEntity(SpaceActivity.this);
				if (userEntity != null && userEntity.uid.intValue() == mFriendUid) {//主人态
					if (position == (skillAdapter.getCount() - 1)) {
						GoddessSkillListActivity.openMineFaqActivity(SpaceActivity.this);
					}
				} else {
					SkillTextInfo info = (SkillTextInfo) skillAdapter.getItem(position);
					if (info != null) {
						BAApplication.getInstance().setSkillTextInfo(info);
						SkillInviteActivity.openMineFaqActivity(mFriendUid, mSex, mNick, SpaceActivity.this);
					}
				}
			}
		});
	}

	private ScrollListener scrollListener = new ScrollListener() {

		@Override
		public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
			if (y > 200) {
				ll_title.setBackgroundColor(getResources().getColor(R.color.space_title_bg));
			} else {
				ll_title.setBackgroundResource(R.drawable.activity_space_title_bg_shadow);
			}
		}
	};

	private void setTopViewHeight() {
		int screenWidth = BasePhone.getScreenWidth(this);
		float hegiht = (float) ((screenWidth / 640.0) * 400);
		rl_top_bg.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, (int) hegiht));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == PhotoSetDialog.GET_PHOTO_BY_GALLERY) {//获取相册回来
				Uri uri = data.getData(); // 读取相册图片
				if (uri != null) {
					String uriPath = BaseFile.getFilePathFromContentUri(uri, this.getContentResolver());
					spaceBiz.uploadHeadPic(imageLoader, rl_top_bg, uriPath);
				}
			}
			if (requestCode == PhotoSetDialog.GET_PHOTO_BY_CAMERA) {//拍照回来
				String path = BaseFile.getTempFile().getAbsolutePath();
				spaceBiz.uploadHeadPic(imageLoader, rl_top_bg, path);
			}
			if (requestCode == CREATE_HAREM_REQUEST_CODE) {
				CreateHarem.getInstance().getRelevantGroupList(this, mFriendUid, mHandler);//获取后宫列表
			}
		}
	}

	/**
	 * 客人状态头部用户信息
	 */
	private void setuserInfo() {
		if (!TextUtils.isEmpty(mPicHead)) {
			String key = mPicHead + "@true@210@210";
			DisplayImageOptions options_head = ImageOptionsUtils.GetHeadKeyBigRounded(this);
			//			imageLoader.displayImage("http://" + key, mIvHead, options_head);
			imageLoader.displayImage("http://" + key, mIvHead, options_head, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {

				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					mIvHead.setOnClickListener(null);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {

				}
			});
		}

		if (!TextUtils.isEmpty(mShowKey)) {
			String key = mShowKey + BAConstants.LOAD_640_APPENDSTR;
			DisplayImageOptions options = ImageOptionsUtils.getImageKeyOptionsPersonBg(this);
			imageLoader.loadImage("http://" + key, options, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {

				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (loadedImage != null) {
						rl_top_bg.setBackgroundDrawable(new BitmapDrawable(loadedImage));
					}
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {

				}
			});
		}

		String alias = SharedPreferencesTools.getInstance(SpaceActivity.this, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				mFriendUid);
		mTitle.setText((TextUtils.isEmpty(alias) ? mNick : alias));
		tv_id.setText("ID:" + mFriendUid);
		String time = "";
		if (mLatestOnlineTime > 0) {
			OperateViewUtils.setTextViewLeftDrawable(this, R.drawable.main_icon_online_yes, tv_onLine);
			time = getString(R.string.online);
		} else {
			OperateViewUtils.setNullTextViewLeftDrawable(tv_onLine);
			time = BaseTimes.getDiffTime(Math.abs(mLatestOnlineTime) * 1000);
		}
		tv_onLine.setText(time);

	}

	@Override
	protected int initView() {
		return R.layout.activity_custom_space;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (starFallFlakeView != null)
			starFallFlakeView.resume();
		if (cloudFlakeView != null)
			cloudFlakeView.resume();
		if (rose1FlakeView != null)
			rose1FlakeView.resume();
		if (flowerFlakeView != null) {
			flowerFlakeView.resume();
		}
		if (anim_pet != null && !anim_pet.isRunning()) {
			anim_pet.start();
		}
		if (anim_change != null && !anim_change.isRunning()) {
			anim_change.start();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (starFallFlakeView != null)
			starFallFlakeView.pause();
		if (cloudFlakeView != null)
			cloudFlakeView.pause();
		if (rose1FlakeView != null)
			rose1FlakeView.pause();
		if (flowerFlakeView != null) {
			flowerFlakeView.pause();
		}
		if (anim_pet != null) {
			anim_pet.stop();
		}
		if (anim_change != null) {
			anim_change.stop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		spaceBiz.setHandler(null);//防止数据继续返回回来
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onSuccess(int code, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.GET_DYNAMIC_SUCCESS;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onError(int code) {

	}

	@Override
	public void deleteFollowCallBack(int retCode) {
		sendHandlerMessage(mHandler, DELETEFOLLOW, retCode);
	}

	@Override
	public void getDistance(int retCode, int dis) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.USER_DISTANCE_VALUE, dis, dis);
	}

	@Override
	public void getPersonSkillInfoOnSuccess(int resCode, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_SUCCESS, resCode, obj);
	}

	@Override
	public void getPersonSkillInfoOnError(int resCode) {
		sendHandlerMessage(mHandler, HandlerValue.GET_PERSON_GODDESS_SKILL_INFO_ERROR, resCode);
	}
}
