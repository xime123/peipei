package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.mine.MineSettingUserInfoActivity;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ShowMemberType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.model.showrooms.RoomsGetBiz;
import com.tshang.peipei.protocol.Gogirl.GoGirlUserInfoP;
import com.tshang.peipei.protocol.Gogirl.ShowRoomMemberInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: RoomMembersAdapter.java 
 *
 * @Description: 成员列表适配器
 *
 * @author allen  
 *
 * @date 2015-1-23 下午4:51:15 
 *
 * @version V1.0   
 */
public class RoomMembersAdapter extends ArrayListAdapter<ShowRoomMemberInfo> {

	protected ImageLoader imageLoader;
	protected DisplayImageOptions options_uid_head;//通过UID加载
	protected int roomUid;
	protected int roomid;
	private RoomsGetBiz roomsGetBiz;
	private BAHandler mHandler;
	private DisplayImageOptions gradeInfoOptions;

	public RoomMembersAdapter(Activity context, int roomUid, int roomid, BAHandler mHandler) {
		super(context);
		this.roomid = roomid;
		this.roomUid = roomUid;
		this.mHandler = mHandler;
		imageLoader = ImageLoader.getInstance();
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(mContext);
		roomsGetBiz = new RoomsGetBiz(context, mHandler);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewholer;
		setMemberRoleListener roleListener = null;
		kickUserOutListener kickListener = null;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_room_member, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.item_room_member_head);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.item_room_member_nick);
			mViewholer.ivSex = (ImageView) convertView.findViewById(R.id.item_room_member_sex);
			mViewholer.btnVip = (Button) convertView.findViewById(R.id.item_room_member_vip);
			mViewholer.btnOut = (Button) convertView.findViewById(R.id.item_room_member_out);
			mViewholer.tvStauts = (TextView) convertView.findViewById(R.id.item_room_member_status);
			mViewholer.ivGradeinfo = (ImageView)convertView.findViewById(R.id.iv_room_member_gradeinfo);
			roleListener = new setMemberRoleListener();
			mViewholer.btnVip.setOnClickListener(roleListener);
			convertView.setTag(mViewholer.btnVip.getId(), roleListener);
			kickListener = new kickUserOutListener();
			mViewholer.btnOut.setOnClickListener(kickListener);
			convertView.setTag(mViewholer.btnOut.getId(), kickListener);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();
			roleListener = (setMemberRoleListener) convertView.getTag(mViewholer.btnVip.getId());
			kickListener = (kickUserOutListener) convertView.getTag(mViewholer.btnOut.getId());
		}
		ShowRoomMemberInfo roomInfo = mList.get(position);
		GoGirlUserInfoP info = roomInfo.getMemberuserinfo();

		roleListener.setMemberInfo(roomInfo);
		kickListener.setMemberInfo(roomInfo);

		if (info != null) {
			if (roomUid == BAApplication.mLocalUserInfo.uid.intValue()) {//主人态
				if (roomInfo.getType() == ShowMemberType.owner) {//房主
					mViewholer.btnVip.setVisibility(View.GONE);
					mViewholer.btnOut.setVisibility(View.GONE);
					mViewholer.tvNick.setTextColor(mContext.getResources().getColor(R.color.peach));
					mViewholer.tvStauts.setVisibility(View.VISIBLE);
					mViewholer.tvStauts.setText(R.string.str_show_room_owner);
					mViewholer.tvStauts.setTextColor(mContext.getResources().getColor(R.color.peach));
				} else {
					mViewholer.btnVip.setVisibility(View.VISIBLE);
					mViewholer.btnOut.setVisibility(View.VISIBLE);
					mViewholer.tvStauts.setVisibility(View.GONE);
					if (roomInfo.getType() == ShowMemberType.vip) {
						mViewholer.tvNick.setTextColor(mContext.getResources().getColor(R.color.peach));
						mViewholer.btnVip.setBackgroundResource(R.drawable.casino_home_honored_selector);
						mViewholer.btnVip.setText(R.string.str_show_delete_vip);
						mViewholer.btnVip.setTextColor(mContext.getResources().getColor(R.color.white));
					} else if (roomInfo.getType() == ShowMemberType.normal) {
						mViewholer.btnVip.setBackgroundDrawable(null);
						mViewholer.tvNick.setTextColor(mContext.getResources().getColor(R.color.black));
						mViewholer.btnVip.setText(R.string.str_show_room_set_vip);
						mViewholer.btnVip.setTextColor(mContext.getResources().getColor(R.color.green));
					}
				}
			} else {//客态
				mViewholer.btnVip.setVisibility(View.GONE);
				mViewholer.btnOut.setVisibility(View.GONE);
				if (roomInfo.getType() == ShowMemberType.owner) {//房主
					mViewholer.tvNick.setTextColor(mContext.getResources().getColor(R.color.peach));
					mViewholer.tvStauts.setVisibility(View.VISIBLE);
					mViewholer.tvStauts.setText(R.string.str_show_room_owner);
					mViewholer.tvStauts.setTextColor(mContext.getResources().getColor(R.color.peach));
				} else if (roomInfo.getType() == ShowMemberType.vip) {
					mViewholer.tvNick.setTextColor(mContext.getResources().getColor(R.color.peach));
					mViewholer.tvStauts.setVisibility(View.VISIBLE);
					mViewholer.tvStauts.setText(R.string.str_show_room_vip);
					mViewholer.tvStauts.setTextColor(mContext.getResources().getColor(R.color.green));
				} else if (roomInfo.getType() == ShowMemberType.normal) {
					mViewholer.tvNick.setTextColor(mContext.getResources().getColor(R.color.black));
					mViewholer.tvStauts.setVisibility(View.GONE);
				}
			}

			imageLoader.displayImage("http://" + info.getUid() + BAConstants.LOAD_HEAD_UID_APPENDSTR, mViewholer.ivHead, options_uid_head);
			mViewholer.tvNick.setText(new String(info.getNick().toByteArray()));

			if (info.getSex() == BAConstants.Gender.FEMALE.getValue()) {
				mViewholer.ivSex.setImageResource(R.drawable.broadcast_img_girl);
			} else {
				mViewholer.ivSex.setImageResource(R.drawable.broadcast_img_boy);
			}
			
			GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, new String(info.getGradeinfo().toByteArray()), mViewholer.ivGradeinfo, gradeInfoOptions);

			HeadClickListener headClickListener = new HeadClickListener(info.getUid(), info.getSex());
			mViewholer.ivHead.setOnClickListener(headClickListener);
		}

		return convertView;
	}

	private final class ViewHolder {
		ImageView ivHead;
		TextView tvNick;
		ImageView ivSex;
		Button btnVip;
		Button btnOut;
		TextView tvStauts;
		ImageView ivGradeinfo;
	}

	private class setMemberRoleListener implements OnClickListener {

		ShowRoomMemberInfo roomInfo;

		public void setMemberInfo(ShowRoomMemberInfo roomInfo) {
			this.roomInfo = roomInfo;
		}

		@Override
		public void onClick(View v) {
			if (roomInfo != null) {
				int act = 0;
				if (roomInfo.getType() == ShowMemberType.vip) {
					act = -1;
				} else if (roomInfo.getType() == ShowMemberType.normal) {
					act = 1;
				}

				roomsGetBiz.setMemberRole(act, roomInfo.getMemberuserinfo().getUid(), roomid);
			}
		}
	}

	public class kickUserOutListener implements OnClickListener {

		ShowRoomMemberInfo roomInfo;

		public void setMemberInfo(ShowRoomMemberInfo roomInfo) {
			this.roomInfo = roomInfo;
		}

		@Override
		public void onClick(View v) {
			if (roomInfo != null) {
				roomsGetBiz.InOutRooms(2, roomid, roomInfo.getMemberuserinfo().getUid());
			}
		}

	}

	protected class HeadClickListener implements OnClickListener {//头像点击事件
		private int sex;
		private int uid;

		public HeadClickListener(int uid, int sex) {
			this.sex = sex;
			this.uid = uid;
		}

		@Override
		public void onClick(View v) {
			if (uid != BAApplication.mLocalUserInfo.uid.intValue()) {
				Bundle bundle = new Bundle();
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, uid);
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
				BaseUtils.openActivityByNew(mContext, SpaceActivity.class, bundle);
			} else {
				if (BAApplication.mLocalUserInfo != null) {
					Bundle bundle = new Bundle();
					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, BAApplication.mLocalUserInfo.uid.intValue());
					bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, BAApplication.mLocalUserInfo.sex.intValue());
					BaseUtils.openActivity(mContext, MineSettingUserInfoActivity.class, bundle);
				}
			}
		}

	}
}
