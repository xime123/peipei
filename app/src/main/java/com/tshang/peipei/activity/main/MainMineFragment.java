package com.tshang.peipei.activity.main;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.NeedUploadHeadDialog;
import com.tshang.peipei.activity.dialog.UpdateApkDialog;
import com.tshang.peipei.activity.dialog.UpdateApkForceDialog;
import com.tshang.peipei.activity.mine.MineFaqActivity;
import com.tshang.peipei.activity.mine.MineInviteFriendH5Activity;
import com.tshang.peipei.activity.mine.MineMissionsActivity;
import com.tshang.peipei.activity.mine.MineSettingActivity;
import com.tshang.peipei.activity.mine.MineShowAllGiftListActivity;
import com.tshang.peipei.activity.mine.UploadIdentifyActivity;
import com.tshang.peipei.activity.mine.UploadingIdentifyActivity;
import com.tshang.peipei.activity.skill.MineSkillsListActivity;
import com.tshang.peipei.activity.store.StoreH5RechargeActivity;
import com.tshang.peipei.activity.store.StoreIntegralActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.HandlerType;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;
import com.tshang.peipei.base.babase.BAConstants.UpdateType;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.PeiPeiAppStartBiz;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.store.StoreUserBiz;
import com.tshang.peipei.model.biz.user.ShowGiftBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetLastestAppInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserProperty;
import com.tshang.peipei.model.bizcallback.BizCallBackShowGiftToday;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.space.SpaceBiz;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetLatestAppInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

import de.greenrobot.event.EventBus;

public class MainMineFragment extends BaseFragment implements BizCallBackGetUserProperty, BizCallBackShowGiftToday, BizCallBackGetLastestAppInfo {
	private LinearLayout ll_glamour;//显示魅力值的layout
	private TextView tv_glamour;
	private ImageView iv_mine_head;
	private ImageView iv_level_head;
	private TextView tv_gifts_count;
	private TextView tv_point_count;
	private TextView tv_gold_money;
	private TextView tv_silver_money;
	private TextView tv_name;
	private TextView tv_giftSendPeopleNum;
	private TextView tv_nohead;
	private View viewHeight;
	private ImageView ivMissionNew;
	private TextView tv_identify;
	private ImageView img_version_new;
	private TextView tv_curversion;
	private Dialog dialog;
	private boolean isIdentify = false;
	private boolean isClickCheck = false; //是否点击了检查版本
	private static final int USER_PROPERTY_INFO = 0x501;
	private static final int REFRESH_USER_PROPERTY_INFO = 0x502;
	private static final int CHANGE_USER_COUNT = 0x503;
	private static final int BIZ_CALLBACK_GET_GIFT = 0x504;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		EventBus.getDefault().registerSticky(this);
		View view = inflater.inflate(R.layout.fragment_mine, null);
		initView(view);
		initViewValue();
		getUserMoney();
		return view;
	}

	private void initView(View view) {
		view.findViewById(R.id.ll_mine_user).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_gift).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_integral).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_invite_friends).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_account).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_get_reward).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_setting).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_skills).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_scene_shop).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_get_find_apps).setOnClickListener(this);
		view.findViewById(R.id.ll_setting_head_identify).setOnClickListener(this);
		view.findViewById(R.id.ll_mine_check_version).setOnClickListener(this);
		
		tv_identify = (TextView) view.findViewById(R.id.tv_idtentify);
		ivMissionNew = (ImageView) view.findViewById(R.id.img_missions_new);

		iv_level_head = (ImageView) view.findViewById(R.id.iv_mine_level_head);
		tv_nohead = (TextView) view.findViewById(R.id.mine_no_head_text);

		ll_glamour = (LinearLayout) view.findViewById(R.id.ll_glamour);
		tv_glamour = (TextView) view.findViewById(R.id.layout_tv_glamour);

		iv_mine_head = (ImageView) view.findViewById(R.id.iv_mine_head);
		tv_gifts_count = (TextView) view.findViewById(R.id.tv_mine_gift_count);
		tv_gold_money = (TextView) view.findViewById(R.id.tv_gold_money);
		tv_silver_money = (TextView) view.findViewById(R.id.tv_silver_money);
		tv_name = (TextView) view.findViewById(R.id.tv_mine_name);
		tv_point_count = (TextView) view.findViewById(R.id.tv_mine_integral_count);
		tv_giftSendPeopleNum = (TextView) view.findViewById(R.id.tv_gift_sendpeople_num);
		viewHeight = view.findViewById(R.id.view_hight);
		
		img_version_new = (ImageView) view.findViewById(R.id.img_version_new);
		tv_curversion = (TextView) view.findViewById(R.id.tv_curversion);

		setNum();
	}

	private DisplayImageOptions options;

	private void initViewValue() {//给一些初始化的控件赋值
		if (BAApplication.mLocalUserInfo != null) {
			if (!isMan(BAApplication.mLocalUserInfo)) {//女号
				ll_glamour.setVisibility(View.VISIBLE);
				viewHeight.setVisibility(View.VISIBLE);
			} else {//男号
				ll_glamour.setVisibility(View.GONE);
				viewHeight.setVisibility(View.GONE);
			}

			tv_name.setText(ParseMsgUtil.convertUnicode2(new String(BAApplication.mLocalUserInfo.nick)));
			int mGiftNum = SharedPreferencesTools.getInstance(getActivity(), BAApplication.mLocalUserInfo.uid.intValue() + "")
					.getIntValueByKeyToZero(BAConstants.GIFTS_NUM);
			OperateViewUtils.setTextViewShowCount(tv_gifts_count, mGiftNum, false);
			options = ImageOptionsUtils.GetHeadKeyBigRounded(getActivity());
			if (!TextUtils.isEmpty(new String(BAApplication.mLocalUserInfo.headpickey))) {
				imageLoader.displayImage("http://" + new String(BAApplication.mLocalUserInfo.headpickey) + "@true@210@210", iv_mine_head, options);
				tv_nohead.setVisibility(View.GONE);
			} else {
				iv_mine_head.setImageResource(R.drawable.main_img_defaulthead_un);
				tv_nohead.setVisibility(View.VISIBLE);
			}
			String str_level_grade = new String(BAApplication.mLocalUserInfo.gradeinfo);
			showLevelHead(str_level_grade);
			checkVersion();
			
			getUserInfo();
		}
	}

	private void showLevelHead(String strGrade) {
		if (TextUtils.isEmpty(strGrade)) {
			iv_level_head.setVisibility(View.GONE);
		} else {
			iv_level_head.setVisibility(View.VISIBLE);
			DisplayImageOptions options = ImageOptionsUtils.getGradeInfoImageKeyOptions(getActivity());
			GradeInfoImgUtils.loadGradeInfoImg(getActivity(), imageLoader, options, strGrade, iv_level_head);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_mine_user://点击用户昵称进入个人空间
			String strGlamour = tv_glamour.getText().toString();
			if (TextUtils.isEmpty(strGlamour)) {
				strGlamour = "0";
			}
			SpaceUtils.toSpaceCustom(getActivity(), Long.parseLong(strGlamour));
			break;
		case R.id.ll_mine_gift:
			GoGirlUserInfo userEntity = UserUtils.getUserEntity(getActivity());
			if (userEntity != null) {
				Bundle bundle = new Bundle();
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, userEntity.uid.intValue());
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, userEntity.sex.intValue());
				BaseUtils.openActivity(getActivity(), MineShowAllGiftListActivity.class, bundle);
				SharedPreferencesTools.getInstance(getActivity(), userEntity.uid.intValue() + "").saveIntKeyValue(0, BAConstants.GIFTS_NUM);
				OperateViewUtils.setTextViewShowCount(tv_gifts_count, 0, false);
				NoticeEvent noticeEvent = new NoticeEvent();
				noticeEvent.setFlag(NoticeEvent.NOTICE28);
				EventBus.getDefault().postSticky(noticeEvent);
			}
			break;
		case R.id.ll_mine_integral:
			BaseUtils.openActivity(getActivity(), StoreIntegralActivity.class);
			break;
		case R.id.ll_mine_invite_friends:
			BaseUtils.openActivity(getActivity(), MineInviteFriendH5Activity.class);
			break;
		case R.id.ll_mine_account:
			Bundle bundle1 = new Bundle();
			bundle1.putString("gold", tv_gold_money.getText().toString());
			bundle1.putString("silver", tv_silver_money.getText().toString());
			BaseUtils.openActivity(getActivity(), StoreH5RechargeActivity.class, bundle1);
			break;
		case R.id.ll_mine_get_reward:
			BaseUtils.openActivity(getActivity(), MineMissionsActivity.class);
			break;
		case R.id.ll_mine_setting://点击进入设置界面
			BaseUtils.openActivity(getActivity(), MineSettingActivity.class);
			break;
		case R.id.ll_mine_skills:
			BaseUtils.openActivity(getActivity(), MineSkillsListActivity.class);
			break;
		case R.id.ll_mine_scene_shop:
			MineFaqActivity.openMineFaqActivity(getActivity(), MineFaqActivity.SCENE_SHOP_VALUE);
			break;
		case R.id.ll_mine_get_find_apps:
			MineFaqActivity.openMineFaqActivity(getActivity(), MineFaqActivity.FIND_APPS);
			break;
		case R.id.ll_setting_head_identify:

			if (BAApplication.mLocalUserInfo == null) {
				return;
			}
			if (BAApplication.mLocalUserInfo.headpickey == null || TextUtils.isEmpty(new String(BAApplication.mLocalUserInfo.headpickey))) {
				new NeedUploadHeadDialog(getActivity(), R.string.indentify_need_head, R.string.upload, R.string.cancel).showDialog();
			} else {
				if(BAApplication.mLocalUserInfo.authpickey == null){
					BaseUtils.openActivity(getActivity(), UploadIdentifyActivity.class);
				}else{
					String authKey = new String(BAApplication.mLocalUserInfo.authpickey);
					if (TextUtils.isEmpty(authKey)) {//没有上传认证头像
						BaseUtils.openActivity(getActivity(), UploadIdentifyActivity.class);
					} else {
						Bundle b = new Bundle();
						b.putBoolean("isIdentify", isIdentify);//是否认证通过
						b.putString("authKey", authKey);
						BaseUtils.openActivity(getActivity(), UploadingIdentifyActivity.class, b);
					}
				}
			}

			break;
		case R.id.ll_mine_check_version:
			isClickCheck = true;
			checkVersion();
			break;
		default:
			break;
		}
	}
	
	private void checkVersion(){
		//百度渠道
		if (BAApplication.Channel.equals("and-baidu")) {
			BDAutoUpdateSDK.cpUpdateCheck(getActivity(), new CPCheckUpdateCallback() {

				@Override
				public void onCheckUpdateCallback(final AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
					if (infoForInstall != null && !TextUtils.isEmpty(infoForInstall.getInstallPath())) {//安装包已经下载,下载安装
						BDAutoUpdateSDK.cpUpdateInstall(getActivity().getApplicationContext(), infoForInstall.getInstallPath());
					} else if (info != null) {//有新版本
						dialog = DialogFactory.showMsgDialog(getActivity(), "版本信息",
								info.getAppChangeLog().replaceAll("<br>", "\n").replaceAll(" ", "").trim(), "更新", "取消", new OnClickListener() {

									@Override
									public void onClick(View v) {
										DialogFactory.dimissDialog(dialog);
										BDAutoUpdateSDK.cpUpdateDownload(getActivity(), info, new UpdateDownloadCallback());
									}
								}, null);
					} else {
						BaseUtils.showTost(getActivity(), R.string.updateinfo_content);
					}
				}
			});
		} else{
			new PeiPeiAppStartBiz().checkAppInfo(getActivity(), this);
		}
	}
	
	private class UpdateDownloadCallback implements CPUpdateDownloadCallback {
		private View view;
		private ProgressBar progressBar;

		boolean isCancel = false;

		public UpdateDownloadCallback() {
			view = LayoutInflater.from(getActivity()).inflate(R.layout.down_progress_bar_layout, null);
			progressBar = (ProgressBar) view.findViewById(R.id.down_progress_bar);
		}

		@Override
		public void onDownloadComplete(String apkPath) {
			DialogFactory.dimissDialog(dialog);
			if (isCancel)
				return;
			BDAutoUpdateSDK.cpUpdateInstall(getActivity().getApplicationContext(), apkPath);

		}

		@Override
		public void onStart() {
			dialog = DialogFactory.showMsgDialog(getActivity(), "", view, false, "取消", "", null, new OnClickListener() {

				@Override
				public void onClick(View v) {
					isCancel = true;
					DialogFactory.dimissDialog(dialog);
				}
			});
		}

		@Override
		public void onPercent(int percent, long rcvLen, long fileSize) {
			Log.d("Aaron", "p===" + percent + "%");
			progressBar.setProgress(percent);
		}

		@Override
		public void onFail(Throwable error, String content) {
			BaseUtils.showTost(getActivity(), "下载失败");
		}

		@Override
		public void onStop() {
		}

	}
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case USER_PROPERTY_INFO:
			UserPropertyInfo userPropertyInfo = (UserPropertyInfo) msg.obj;
			if (userPropertyInfo != null && BAApplication.mLocalUserInfo != null
					&& userPropertyInfo.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
				SharedPreferencesTools.getInstance(getActivity(), userPropertyInfo.uid.intValue() + "").saveIntKeyValue(
						userPropertyInfo.goldcoin.intValue(), "goldcoin");//私聊送礼使用
				SharedPreferencesTools.getInstance(getActivity(), userPropertyInfo.uid.intValue() + "").saveIntKeyValue(
						userPropertyInfo.silvercoin.intValue(), "silvercoin");
				tv_silver_money.setText(String.valueOf(userPropertyInfo.silvercoin.intValue()));
				tv_gold_money.setText(String.valueOf(userPropertyInfo.goldcoin.intValue()));
				tv_glamour.setText(String.valueOf(userPropertyInfo.charmnum.intValue()));
				tv_point_count.setText(String.valueOf(userPropertyInfo.score.intValue()));
			}
			break;
		case REFRESH_USER_PROPERTY_INFO:
		case CHANGE_USER_COUNT://切换了账户
			initViewValue();
			getUserMoney();
			break;
		case BIZ_CALLBACK_GET_GIFT:
			int total = msg.arg2;
			if (total > 0) {
				tv_giftSendPeopleNum.setText(total + "人赠送");
			} else {
				tv_giftSendPeopleNum.setText("");
			}
			break;
		case HandlerValue.SPACE_USER_INFO_VALUE://获取到用户的基本信息，比如昵称等数据
			if (msg.arg1 == 0) {
				GoGirlUserInfo userInfo1 = (GoGirlUserInfo) msg.obj;
				if (userInfo1.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
					if (userInfo1 != null) {
						showLevelHead(new String(userInfo1.gradeinfo));
						
						if (userInfo1.authpickey != null && !TextUtils.isEmpty(new String(userInfo1.authpickey)) && (userInfo1.userstatus.intValue() & SwitchStatus.GG_US_AUTH_FLAG) > 0) {
							tv_identify.setText(getActivity().getString(R.string.str_auth));
							isIdentify = true;
						} else {
							tv_identify.setText(getActivity().getString(R.string.str_unauth));
							isIdentify = false;
						}
					}
					BAApplication.mLocalUserInfo = userInfo1;
					if (!TextUtils.isEmpty(new String(BAApplication.mLocalUserInfo.headpickey))) {
						imageLoader.displayImage("http://" + new String(BAApplication.mLocalUserInfo.headpickey) + "@true@210@210", iv_mine_head,
								options);
						tv_nohead.setVisibility(View.GONE);
					} else {
						iv_mine_head.setImageResource(R.drawable.main_img_defaulthead_un);
						tv_nohead.setVisibility(View.VISIBLE);
					}
					
					

				}

			}
			break;
		case HandlerType.CREATE_GETDATA_BACK:
			if (msg.arg1 == 0) {
				if (msg.obj instanceof RspGetLatestAppInfo) {
					RspGetLatestAppInfo rsp = (RspGetLatestAppInfo) msg.obj;
					String url = new String(rsp.updateurl);

					SharedPreferencesTools.getInstance(getActivity()).saveIntKeyValue(rsp.latestappver.intValue(),
							BAConstants.UPDATE_VER);
					if(isClickCheck){
						String title = new String(rsp.updatedesc);
						title = title.replaceAll("#", "\n");
						if (rsp.updatelevel.intValue() == UpdateType.UPDATE_LEVEL_NO.getValue()) {
							BaseUtils.showTost(getActivity(), R.string.updateinfo_content);
						} else if (rsp.updatelevel.intValue() == UpdateType.UPDATE_LEVEL_OPTIONAL.getValue()) {
							new UpdateApkDialog(getActivity(), 0, R.string.ok, R.string.cancel, url, title).showDialog();
						} else if (rsp.updatelevel.intValue() == UpdateType.UPDATE_LEVEL_FORCE.getValue()) {
							new UpdateApkForceDialog(getActivity(), url, title).showDialog();
						}
						isClickCheck = false;
					}else{
						if (rsp.updatelevel.intValue() == UpdateType.UPDATE_LEVEL_NO.getValue()) {
							tv_curversion.setVisibility(View.VISIBLE);
							img_version_new.setVisibility(View.GONE);
						}else{
							tv_curversion.setVisibility(View.GONE);
							img_version_new.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {//友盟统计
		super.onHiddenChanged(hidden);
		if (!hidden) {
			getUserMoney();
			getUserInfo();
			setNum();
		} else {
			mHandler.removeCallbacksAndMessages(null);
		}
	}

	private void setNum() {
		//		if (!SharedPreferencesTools.getInstance(getActivity()).getBooleanKeyValue(BAConstants.PEIPEI_POINTS_WALL)) {
		//			ivMissionNew.setVisibility(View.VISIBLE);
		//		} else {
		ivMissionNew.setVisibility(View.GONE);
		//		}
	}

	private void getUserMoney() {//获取用户的金币，银币，魅力值,送礼物数
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(getActivity());
		if (userEntity != null) {
			new ShowGiftBiz().showGiftToday(getActivity(), userEntity.uid.intValue(), -1, 1, this, 1);
			StoreUserBiz.getInstance().getUserProperty(getActivity(), userEntity.uid.intValue(), this);
		}
	}

	public void onEvent(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE18 || event.getFlag() == NoticeEvent.NOTICE28) {
			mHandler.sendEmptyMessage(REFRESH_USER_PROPERTY_INFO);
		} else if (event.getFlag() == NoticeEvent.NOTICE58) {//切换了账户
			mHandler.sendEmptyMessage(CHANGE_USER_COUNT);
		}

	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void getUserProperty(int retCode, UserPropertyInfo userPropertyInfo) {
		HandlerUtils.sendHandlerMessage(mHandler, USER_PROPERTY_INFO, retCode, retCode, userPropertyInfo);
	}

	@Override
	public void showGiftToday(int retcode, int tatol, int currpage, GiftDealInfoList giftInfoList) {
		HandlerUtils.sendHandlerMessage(mHandler, BIZ_CALLBACK_GET_GIFT, retcode, tatol, giftInfoList);

	}

	protected void getUserInfo() {//获取用户的信息
		if (BAApplication.mLocalUserInfo != null) {
			new SpaceBiz(getActivity(), mHandler).getUserInfo(BAApplication.mLocalUserInfo.uid.intValue());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setNum();
	}

	@Override
	public void checkLatestAppInfo(int retCode, RspGetLatestAppInfo rsp) {
		HandlerUtils.sendHandlerMessage(mHandler, HandlerType.CREATE_GETDATA_BACK, retCode, 0, rsp);
	}
}
