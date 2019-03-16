package com.tshang.peipei.activity.harem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.dialog.KickedHaremMemberDialog;
import com.tshang.peipei.activity.dialog.PhotoSetDialog;
import com.tshang.peipei.activity.dialog.ReqJoinHaremDialog;
import com.tshang.peipei.activity.harem.adapter.HaremMemberAdapter;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfoList;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatSessionManageBiz;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.harem.CreateHarem;
import com.tshang.peipei.model.harem.UpdateHarem;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ImageUtils;
import com.tshang.peipei.view.ReplyChildListView;

/**
 * 我的后宫管理界面
 * @author Jeff
 *
 */
public class ManagerHaremActivity extends BaseActivity implements OnItemClickListener {
	private ReplyChildListView haremMemberLisview;
	private HaremMemberAdapter haremMemberAdapter;
	private TextView tvHaremName;
	private TextView tvHaremNotice;
	private ImageView ivHarem;
	private ImageView guestImageView;
	private TextView tvGuestReqHarem;
	private TextView tvGuestHaremName;
	private TextView tvGuestNotice;
	private TextView tvHaremMember;
	private LinearLayout ll_guest;
	private LinearLayout ll_host;
	private TextView tv_host_harem_id;
	private TextView tv_guest_harem_id;
	private static final int CREATE_HAREM_REQUEST_CODE = 1;
	private int groupid;
	public static final int UPDATE_HAREM_NAME_REQUEST_CODE = 2;
	public static final int UPDATE_HAREM_NOTICE_REQUEST_CODE = 3;
	private String imagePath;
	private boolean isJoin = true;
	public static final int FROM_CHAT = 1;
	public static final int FROM_MYCREATE = 0;
	private int fromflag = 0;
	private static final String REQUIT_JOIN_TIME_SEP = "requit_join_time_sep";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case HandlerValue.HAREM_GET_GROUP_MEMBER_LIST_SUCCESS_VALUE:
			GroupMemberInfoList infoList = (GroupMemberInfoList) msg.obj;
			if (infoList != null) {
				tvGuestReqHarem.setVisibility(View.VISIBLE);
				GoGirlUserInfo userInfo = UserUtils.getUserEntity(this);
				if (userInfo != null) {
					for (Object object : infoList) {
						GroupMemberInfo info = (GroupMemberInfo) object;
						if (info.uid.intValue() == userInfo.uid.intValue()) {//说明自己已经存在这个群
							if (info.level.intValue() != 9000) {//不是群主//说明是要退出此宫了
								isJoin = false;
								tvGuestReqHarem.setText(R.string.str_request_exit);//申请按钮变成退出按钮
							}
						}
					}
				}
				haremMemberAdapter.setList(infoList);
			}
			break;
		case HandlerValue.HAREM_GET_GROUP_MEMBER_LIST_FAILED_VALUE:
			break;
		case HandlerValue.HAREM_UPDATE_GROUP_INFO_SUCCESS_VALUE:
			if (!TextUtils.isEmpty(imagePath)) {
				imageLoader.displayImage("file://" + imagePath, ivHarem);
			}
			break;
		case HandlerValue.HAREM_JOIN_GROUP_SUCCESS_VALUE:
			BaseUtils.showTost(this, R.string.str_apply_join_success);
			SharedPreferencesTools.getInstance(this).saveLongKeyValue(System.currentTimeMillis(), REQUIT_JOIN_TIME_SEP + groupid);
			break;
		case HandlerValue.HAREM_JOIN_GROUP_REPEAT_JOIN_VALUE:
			BaseUtils.showTost(this, "您已经加入过其他后宫");
			break;
		case HandlerValue.HAREM_JOIN_GROUP_FAILED_VALUE:
			BaseUtils.showTost(this, R.string.str_apply_join_failed);
			break;
		case HandlerValue.HAREM_GROUP_QUIT_SUCCESS_VALUE:
			BaseUtils.showTost(this, "操作成功");
			sendNoticeEvent(NoticeEvent.NOTICE65);//通知上个界面刷新

			CreateHarem.getInstance().getGroupMemberInfoList(this, groupid, mHandler);//获取后宫成员列表
			if (!isJoin) {//不是群主主动申请需要删除掉聊天记录
				ChatSessionManageBiz.removeChatSessionWithUserID(this, groupid, 1);//删除掉个人群的会话列表
				this.finish();
				if (fromflag == FROM_CHAT) {//说明从聊天过来的
					sendNoticeEvent(NoticeEvent.NOTICE66);//关闭聊天界面
				}
			}
			break;
		case HandlerValue.HAREM_GROUP_INFO_SUCCESS_VALUE:
			GroupInfo groupInfo = (GroupInfo) msg.obj;
			if (groupInfo != null && BAApplication.mLocalUserInfo != null) {
				String groupNames = new String(groupInfo.groupname);
				String groupbadgekey = new String(groupInfo.groupbadgekey);
				String groupNotice = new String(groupInfo.groupnotice);
				tvHaremMember.setText("后宫(" + groupInfo.membernum.intValue() + "/" + groupInfo.maxmembernum.intValue() + ")");
				if (groupInfo.owner.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {//说明是自己的后宫
					ll_guest.setVisibility(View.GONE);
					ll_host.setVisibility(View.VISIBLE);
					tvHaremName.setText(groupNames);
					tvHaremNotice.setText(groupNotice);
					imageLoader.displayImage("http://" + groupbadgekey + "@false@120@120", ivHarem);
					haremMemberAdapter.setHost(true);
				} else {//客人态
					ll_guest.setVisibility(View.VISIBLE);
					ll_host.setVisibility(View.GONE);
					tvGuestHaremName.setText(groupNames);
					tvGuestNotice.setText(groupNotice);
					imageLoader.displayImage("http://" + groupbadgekey + "@false@120@120", guestImageView);
					tvGuestReqHarem.setOnClickListener(this);
					haremMemberAdapter.setHost(false);
				}
			}
			break;
		case HandlerValue.HAREM_GROUP_INFO_FAILED_VALUE:
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_create_harem:
			BaseUtils.openResultActivity(this, CreateHaremActivity.class, null, CREATE_HAREM_REQUEST_CODE);
			break;
		case R.id.ll_show_harem_name:
			Bundle nameBundle = new Bundle();
			nameBundle.putInt("groupid", groupid);
			String name = tvHaremName.getText().toString();
			if (!TextUtils.isEmpty(name)) {
				nameBundle.putString("haremname", name);
			}
			BaseUtils.openResultActivity(this, UpdateGroupInfoNameActivity.class, nameBundle, UPDATE_HAREM_NAME_REQUEST_CODE);
			break;
		case R.id.ll_show_harem_notice:
			Bundle noticeBundle = new Bundle();
			noticeBundle.putInt("groupid", groupid);
			String notice = tvHaremNotice.getText().toString();
			if (!TextUtils.isEmpty(notice)) {
				noticeBundle.putString("haremnotice", notice);
			}
			BaseUtils.openResultActivity(this, UpdateGroupInfoNoticeActivity.class, noticeBundle, UPDATE_HAREM_NOTICE_REQUEST_CODE);
			break;
		case R.id.ll_show_image_harem:
			new PhotoSetDialog(this, android.R.style.Theme_Translucent_NoTitleBar).showDialog(0, 0);
			break;
		case R.id.tv_operate_harem:
			if (!isJoin) {
				new KickedHaremMemberDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mHandler, groupid, "", 0, true).showDialog();
			} else {
				long time = SharedPreferencesTools.getInstance(this).getLongKeyValue(REQUIT_JOIN_TIME_SEP + groupid);
				if (System.currentTimeMillis() - time < 60000) {
					BaseUtils.showTost(this, "您操作太快了，歇会吧~");
					return;
				}
				new ReqJoinHaremDialog(this, android.R.style.Theme_Translucent_NoTitleBar, mHandler, groupid).showDialog();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			fromflag = bundle.getInt("from");
			groupid = bundle.getInt("groupid");
			CreateHarem.getInstance().getGroupInfo(this, groupid, mHandler);
			CreateHarem.getInstance().getGroupMemberInfoList(this, groupid, mHandler);//获取后宫成员列表
			String showHaremId = this.getResources().getString(R.string.str_harem_id);
			tv_host_harem_id.setText(showHaremId + ":" + groupid);
			tv_guest_harem_id.setText(showHaremId + ":" + groupid);
		}
		haremMemberAdapter = new HaremMemberAdapter(this, groupid, mHandler);
		haremMemberLisview.setAdapter(haremMemberAdapter);
		haremMemberLisview.setOnItemClickListener(this);
	}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_harem);

		haremMemberLisview = (ReplyChildListView) findViewById(R.id.lv_my_harem_member);
		tvHaremName = (TextView) findViewById(R.id.tv_show_harem_name);
		tvHaremNotice = (TextView) findViewById(R.id.tv_show_harem_content);
		ivHarem = (ImageView) findViewById(R.id.iv_show_harem_image);

		findViewById(R.id.ll_show_harem_name).setOnClickListener(this);
		findViewById(R.id.ll_show_harem_notice).setOnClickListener(this);
		findViewById(R.id.ll_show_image_harem).setOnClickListener(this);

		guestImageView = (ImageView) findViewById(R.id.iv_show_guest_harem_image);
		tvGuestReqHarem = (TextView) findViewById(R.id.tv_operate_harem);
		tvGuestReqHarem.setOnClickListener(this);
		tvGuestNotice = (TextView) findViewById(R.id.tv_show_guest_harem_content);
		ll_guest = (LinearLayout) findViewById(R.id.ll_guest_harem_info);
		ll_host = (LinearLayout) findViewById(R.id.ll_host_harem_info);
		tvGuestHaremName = (TextView) findViewById(R.id.tv_show_guest_harem_name);
		tvHaremMember = (TextView) findViewById(R.id.tv_harem_member);

		tv_host_harem_id = (TextView) findViewById(R.id.tv_show_harem_id);
		tv_guest_harem_id = (TextView) findViewById(R.id.tv_show_guest_harem_id);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK) {
			if (arg0 == CREATE_HAREM_REQUEST_CODE) {

			} else if (arg0 == UPDATE_HAREM_NAME_REQUEST_CODE) {
				String name = arg2.getStringExtra("haremname");
				if (!TextUtils.isEmpty(name)) {
					tvHaremName.setText(name);
					sendNoticeEvent(NoticeEvent.NOTICE65);
				}
			} else if (arg0 == UPDATE_HAREM_NOTICE_REQUEST_CODE) {
				String notice = arg2.getStringExtra("haremnotice");
				if (TextUtils.isEmpty(notice)) {
					notice = "";
				}
				tvHaremNotice.setText(notice);
				sendNoticeEvent(NoticeEvent.NOTICE65);
			} else if (arg0 == PhotoSetDialog.GET_PHOTO_BY_CAMERA) {//file:///storage/sdcard0/DCIM/100MEDIA/50340temp.jpg
				imagePath = BaseFile.getTempFile().getAbsolutePath();
				byte[] images = getImageByte(imagePath);
				if (images != null) {
					UpdateHarem.getInstance().reqUpdateGroupInfo(this, images, groupid, "", "", mHandler);
					sendNoticeEvent(NoticeEvent.NOTICE65);
				}
			}

			else if (arg0 == PhotoSetDialog.GET_PHOTO_BY_GALLERY) {
				Uri uri = arg2.getData(); // 读取相册图片
				if (uri != null) {
					imagePath = BaseFile.getFilePathFromContentUri(uri, getContentResolver());
					byte[] images = getImageByte(imagePath);
					if (images != null) {
						UpdateHarem.getInstance().reqUpdateGroupInfo(this, images, groupid, "", "", mHandler);
					}
				}
			}
		}
	}

	private byte[] getImageByte(String path) {
		Bitmap bitmap = imageLoader.loadImageSync("file://" + path);
		if (bitmap == null) {
			return null;
		}
		int bitmapWidth = bitmap.getWidth();
		float scale = ((float) 120.0 / bitmapWidth);
		bitmap = ImageUtils.scaleImage(bitmap, scale, scale);
		byte[] badgepic = ImageUtils.bitmapToByte(bitmap);

		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		return badgepic;
	}

	@Override
	protected int initView() {
		return R.layout.activity_harem_manager;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		GroupMemberInfo info = (GroupMemberInfo) haremMemberLisview.getAdapter().getItem(position);
		if (info != null) {
			Bundle bundle = new Bundle();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, info.uid.intValue());
			int sex = info.sex.intValue();
			bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
			BaseUtils.openActivityByNew(this, SpaceActivity.class, bundle);
		}
	}
}
