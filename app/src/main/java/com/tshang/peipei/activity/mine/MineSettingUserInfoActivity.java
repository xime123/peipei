package com.tshang.peipei.activity.mine;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.NeedReIdentilyDialog;
import com.tshang.peipei.activity.dialog.NeedUploadHeadDialog;
import com.tshang.peipei.activity.dialog.PhotoSetDialog;
import com.tshang.peipei.activity.skillnew.GoddessSkillListActivity;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.SwitchStatus;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.OperateViewUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.user.UserInfoBiz;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackUpdateUserInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackUploadHeadPic;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestGetDistance.IGetDistance;
import com.tshang.peipei.model.space.SpaceBiz;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: 用户信息界面
 *
 * @Description: 展示用户信息
 *
 * @author jeff
 *
 * @version V1.1  
 */
public class MineSettingUserInfoActivity extends BaseActivity implements OnClickListener, BizCallBackUploadHeadPic, BizCallBackUpdateUserInfo,
		IGetDistance {

	public static final String RESULT_BITMAP_DATA = "result_bitmap_data";
	private static final int UPLOAD_HEAD = 1;
	private static final int UPDATE_BIRTHDAY = 2;

	private static final int RESULT_CLIP_IMAGE = 0x10;//图片剪切返回
	private static final int UPDATE_REQUEST_CODE = 0x11;//修改名称或者密码返回
	private LinearLayout ll_update_pwd;

	private ImageView mImageHead;
	private TextView mTextNick;
	private TextView mTextID;
	private TextView mTextGender;
	private TextView mTextBirthday;
	private DatePickerDialog mDatePicker;
	private String headKey;
	private SpaceBiz spaceBiz;

	private TextView tv_bind_phone;
	private View rl_phone;
	private ImageView iv_head_arrow;
	private ImageView iv_nick_arrow;
	private ImageView iv_id_arrow;
	private ImageView iv_sex_arrow;
	private ImageView iv_birth_arrow;
	private ImageView iv_updatepwd_arrow;
	private TextView tv_distance;
	private TextView tv_distance_value;
	private TextView tv_birth;
	private TextView tv_edit_sig;
	private TextView tv_edit_skill;
	private RelativeLayout rl_goddess_skill;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == RESULT_CLIP_IMAGE) {
				try {
					imageLoader.clearMemoryCache();//强制清除内存缓存
				} catch (Exception e) {
					e.printStackTrace();
				}
				byte[] resultBytes = data.getByteArrayExtra(RESULT_BITMAP_DATA);
				if (resultBytes != null) {
					uploadHeadPic(resultBytes);
				}
			}
			if (requestCode == PhotoSetDialog.GET_PHOTO_BY_CAMERA) {//file:///storage/sdcard0/DCIM/100MEDIA/50340temp.jpg
				startPhotoZoom(BaseFile.getTempFile().getAbsolutePath());
				return;
			}

			if (requestCode == PhotoSetDialog.GET_PHOTO_BY_GALLERY) {
				Uri uri = data.getData(); // 读取相册图片
				if (getContentResolver() != null) {
					startPhotoZoom(BaseFile.getFilePathFromContentUri(uri, getContentResolver()));
				}
				return;
			}
			if (requestCode == UPDATE_REQUEST_CODE) {
				spaceBiz.getUserInfo(mUid);
			}

		}
	}

	private void uploadHeadPic(byte[] pic) {
		new UserSettingBiz().uploadHeadPic(this, pic, BAConstants.UploadHeadType.HEAD, this);
	}

	private void setUserInfo(GoGirlUserInfo userInfo) {

		mTextID.setText(mUid + "，" + getString(R.string.str_no_change));
		String gender = mSex == 0 ? "女" : "男";
		mTextGender.setText(gender + "，" + getString(R.string.str_no_change));
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		boolean isHost = false;
		if (userEntity != null && mUid == userEntity.uid.intValue()) {//说明是主人态
			isHost = true;
			mTextNick.setText(new String(userEntity.nick));
			DisplayImageOptions options = ImageOptionsUtils.GetHeadKeySmallRounded(this);
			headKey = new String(userEntity.headpickey);
			imageLoader.displayImage("http://" + headKey + BAConstants.LOAD_HEAD_KEY_APPENDSTR, mImageHead, options);
//			Log.d("Aaron", "")
			String birthday = BaseTimes.getFormatTime(userEntity.birthday.longValue() * 1000);
			if (!TextUtils.isEmpty(birthday)) {
				mTextBirthday.setText(birthday);
			}
			this.findViewById(R.id.setting_userinfo_rlt_head).setOnClickListener(this);
			this.findViewById(R.id.setting_userinfo_nick_rlt).setOnClickListener(this);
			this.findViewById(R.id.setting_userinfo_birthday_rlt).setOnClickListener(this);
			ll_update_pwd.setOnClickListener(this);

			tv_bind_phone.setOnClickListener(this);

			if (BAApplication.isShowPwd) {//第三方登录就不显示的
				tv_bind_phone.setVisibility(View.VISIBLE);
				rl_phone.setVisibility(View.VISIBLE);
			}
			
			if(mSex == Gender.MALE.getValue()){
				rl_goddess_skill.setVisibility(View.GONE);
			}else{
				rl_goddess_skill.setVisibility(View.VISIBLE);
			}

		} else {//客人太
			if (userInfo == null) {//保证进来就请求
				new UserInfoBiz(this).getDistance(this, mUid, this);
			}
			isHost = false;
			//			ll_identify.setBackgroundResource(R.drawable.main_img_list0_selector);
			//			ll_identify.setPadding(BaseUtils.dip2px(this, 5), 0, 0, 0);
			//			ll_update_pwd.setVisibility(View.VISIBLE);
			iv_head_arrow.setVisibility(View.INVISIBLE);
			iv_nick_arrow.setVisibility(View.INVISIBLE);
			iv_id_arrow.setVisibility(View.INVISIBLE);
			iv_sex_arrow.setVisibility(View.INVISIBLE);
			iv_birth_arrow.setVisibility(View.INVISIBLE);
			iv_updatepwd_arrow.setVisibility(View.INVISIBLE);
			tv_distance.setText("距离");
			tv_birth.setText("年龄/星座");
			tv_distance_value.setVisibility(View.VISIBLE);
			if (userInfo != null) {
				mTextNick.setText(new String(userInfo.nick));
				DisplayImageOptions options = ImageOptionsUtils.GetHeadKeySmallRounded(this);
				headKey = new String(userInfo.headpickey);
				imageLoader.displayImage("http://" + headKey + BAConstants.LOAD_HEAD_KEY_APPENDSTR, mImageHead, options);
				String birthday = BaseTimes.getFormatTime(userInfo.birthday.longValue() * 1000);
				if (!TextUtils.isEmpty(birthday)) {
					mTextBirthday.setText(birthday);
				}

			}
		}
		if (userInfo != null) {
			if (!isHost) {
				String mBirthday = new String(BaseTimes.getFormatTime(userInfo.birthday.longValue() * 1000));
				mTextBirthday.setText(OperateViewUtils.calculateAge(mBirthday) + "/" + OperateViewUtils.getConstellation(mBirthday));
			}
		}

	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case UPLOAD_HEAD:
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, R.string.str_upload_head_image_success);
				spaceBiz.getUserInfo(mUid);
			} else {
				BaseUtils.showTost(this, R.string.str_update_error);
			}
			break;
		case UPDATE_BIRTHDAY:
			if (msg.arg1 == 0) {
				BaseUtils.showTost(this, R.string.str_update_birthday_success);
				spaceBiz.getUserInfo(mUid);
			} else {
				BaseUtils.showTost(this, R.string.str_update_error);
			}
			break;
		case HandlerValue.SPACE_USER_INFO_VALUE://获取到用户的基本信息，比如昵称等数据
			if (msg.arg1 == 0) {
				GoGirlUserInfo userInfo = (GoGirlUserInfo) msg.obj;
				if (userInfo != null && BAApplication.mLocalUserInfo != null) {
					if (userInfo.uid.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {
						UserSharePreference.getInstance(this).saveUserByKey(userInfo);
						//更新内存中的值
						BAApplication.mLocalUserInfo = userInfo;
						sendNoticeEvent(NoticeEvent.NOTICE18);
						sendNoticeEvent(NoticeEvent.NOTICE62);
					}
				}
				setUserInfo(userInfo);
			}
			break;
		case HandlerValue.USER_DISTANCE_VALUE:
			if (msg.arg1 < 0) {
				tv_distance_value.setText("未知");
			} else {
				tv_distance_value.setText(BaseTimes.getDistance(msg.arg1));
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		//选择头像
		case R.id.setting_userinfo_rlt_head:
			GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
			boolean needDialog = false;
			if (userEntity != null) {
				needDialog = SharedPreferencesTools.getInstance(this, userEntity.uid.intValue() + "").getBooleanKeyValue(BAConstants.PEIPEI_IDENTY);
			}

			if (needDialog) {
				new NeedReIdentilyDialog(this, R.string.str_need_reidentity, R.string.ok, R.string.cancel).showDialog();
			} else {
				new PhotoSetDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog(0, 0);
			}
			break;
		//修改昵称 
		case R.id.setting_userinfo_nick_rlt:
			intentUpdateActivity(true, false);
			break;
		//修改生日
		case R.id.setting_userinfo_birthday_rlt:
			initDatePicker();
			break;
		//修改密码	
		case R.id.ll_update_pwd:
			intentUpdateActivity(false, true);
			break;
		case R.id.setting_userinfo_phone_tv://绑定手机号
			if (TextUtils.isEmpty(new String(BAApplication.mLocalUserInfo.phone))) {
				BaseUtils.openActivity(this, MineSettingBindPhoneActivity.class);
			} else {
				BaseUtils.openActivity(this, MineSettingBindPhonedActivity.class);
			}
			break;
		case R.id.tv_edit_sig:
			MineEditSigActivity.openMineFaqActivity(this);
			break;
		case R.id.tv_edit_skill:
			GoddessSkillListActivity.openMineFaqActivity(this);
			break;
		}

	}

	private void intentUpdateActivity(boolean isupdateNick, boolean isupdatePwd) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(BAConstants.IntentType.SETTINGUSERINFOACTIVITY_UPDATENICK, isupdateNick);
		bundle.putBoolean(BAConstants.IntentType.SETTINGUSERINFOACTIVITY_UPDATEPASSWORD, isupdatePwd);
		BaseUtils.openResultActivity(this, MineSettingUserInfoUpdateActivity.class, bundle, UPDATE_REQUEST_CODE);

	}

	private void initDatePicker() {
		if (null == mDatePicker) {

			Calendar mCalendar = Calendar.getInstance();
			GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
			if (userEntity != null) {
				String birthday = BaseTimes.getFormatTime(userEntity.birthday.longValue() * 1000);
				if (!TextUtils.isEmpty(birthday)) {
					String[] birthdays = birthday.split("-");
					try {
						mCalendar.set(Calendar.YEAR, Integer.parseInt(birthdays[0]));
						mCalendar.set(Calendar.MONTH, Integer.parseInt(birthdays[1]) - 1);
						mCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(birthdays[2]));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				mDatePicker = new DatePickerDialog(this, dateListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
						mCalendar.get(Calendar.DAY_OF_MONTH));
			}
		}
		mDatePicker.show();
	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
			//Calendar月份是从0开始,所以month要加1 
			String strMonth = month + 1 + "";
			if (strMonth.length() < 2) {
				strMonth = 0 + strMonth;
			}
			String time = year + "" + strMonth + dayOfMonth;
			long mTime = BaseTimes.getlongTime(time);
			updateBirthday((int) mTime);
		}
	};

	private void updateBirthday(long birthday) {
		UserSettingBiz setBiz = new UserSettingBiz();
		if (birthday == -1) {
			birthday = 0;
		}
		String email = "";
		String phone = "";
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(this);
		if (userEntity != null) {
			setBiz.updateUserInfo(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, userEntity.uid.intValue(), new String(
					userEntity.nick), userEntity.sex.intValue(), birthday, email, phone, this);
		}
	}

	@Override
	public void uploadHeadPicCallBack(int retCode) {
		sendHandlerMessage(mHandler, UPLOAD_HEAD, retCode);
	}

	@Override
	public void updateUserInfoCallBack(int retCode) {
		sendHandlerMessage(mHandler, UPDATE_BIRTHDAY, retCode);
	}

	@Override
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mUid = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, 0);
			mSex = bundle.getInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, 0);
			spaceBiz = new SpaceBiz(this, mHandler);
			spaceBiz.getUserInfo(mUid);
			setUserInfo(null);
		}

	}

	private int mUid;
	private int mSex;

	@Override
	protected void initRecourse() {
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitle.setText(R.string.setting_userinfo);

		mImageHead = (ImageView) this.findViewById(R.id.setting_userinfo_iv_head);
		mTextNick = (TextView) this.findViewById(R.id.setting_userinfo_tv_nick);
		mTextID = (TextView) this.findViewById(R.id.setting_userinfo_tv_id);
		mTextGender = (TextView) this.findViewById(R.id.setting_userinfo_tv_gender);
		mTextBirthday = (TextView) this.findViewById(R.id.setting_userinfo_tv_birthday);
		tv_bind_phone = (TextView) findViewById(R.id.setting_userinfo_phone_tv);
		rl_phone = findViewById(R.id.rl_phone);

		iv_head_arrow = (ImageView) findViewById(R.id.iv_head_arrow);
		iv_nick_arrow = (ImageView) findViewById(R.id.iv_nick_arrow);
		iv_id_arrow = (ImageView) findViewById(R.id.iv_id_arrow);
		iv_sex_arrow = (ImageView) findViewById(R.id.iv_sex_arrow);
		iv_birth_arrow = (ImageView) findViewById(R.id.iv_birth_arrow);
		iv_updatepwd_arrow = (ImageView) findViewById(R.id.iv_updatepwd_arrow);
		tv_distance = (TextView) findViewById(R.id.setting_userinfo_modify_pwd_tv);
		tv_distance_value = (TextView) findViewById(R.id.tv_distance_value);
		tv_birth = (TextView) findViewById(R.id.tv_birth);

		ll_update_pwd = (LinearLayout) findViewById(R.id.ll_update_pwd);
		
		tv_edit_sig = (TextView) findViewById(R.id.tv_edit_sig);
		tv_edit_skill = (TextView) findViewById(R.id.tv_edit_skill);
		tv_edit_sig.setOnClickListener(this);
		tv_edit_skill.setOnClickListener(this);
		
		rl_goddess_skill = (RelativeLayout) findViewById(R.id.rl_goddess_skill);
	}

	@Override
	protected int initView() {
		return R.layout.activity_setting_userinfo;
	}

	public void startPhotoZoom(String path) {//进入到图片剪切页面
		Bundle bundle = new Bundle();
		bundle.putString(ClipViewActivity.GET_IMAGE_PATH, path);
		BaseUtils.openResultActivity(this, ClipViewActivity.class, bundle, RESULT_CLIP_IMAGE);
	}

	@Override
	public void getDistance(int retCode, int dis) {//获取用户之间的距离
		HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.USER_DISTANCE_VALUE, dis, dis);

	}

}
