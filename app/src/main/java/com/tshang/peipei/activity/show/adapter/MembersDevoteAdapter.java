package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.mine.MineSettingUserInfoActivity;
import com.tshang.peipei.activity.space.SpaceActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.protocol.Gogirl.GoGirlUserInfoP;
import com.tshang.peipei.protocol.Gogirl.ShowRoomMemberInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: MembersDevoteAdapter.java 
 *
 * @Description: 成员贡献适配器
 *
 * @author allen  
 *
 * @date 2015-1-23 下午4:51:15 
 *
 * @version V1.0   
 */
public class MembersDevoteAdapter extends ArrayListAdapter<ShowRoomMemberInfo> {

	protected ImageLoader imageLoader;
	protected DisplayImageOptions options_uid_head;//通过UID加载
	private DisplayImageOptions gradeInfoOptions;
	private int type;

	public MembersDevoteAdapter(Activity context) {
		super(context);
		imageLoader = ImageLoader.getInstance();
		options_uid_head = ImageOptionsUtils.GetHeadUidSmallRounded(mContext);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_devote_member, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.item_devote_member_head);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.item_devote_member_nick);
			mViewholer.ivSex = (ImageView) convertView.findViewById(R.id.item_devote_member_sex);
			mViewholer.ivVip = (ImageView) convertView.findViewById(R.id.item_devote_member_vip);
			mViewholer.tvType = (TextView) convertView.findViewById(R.id.item_devote_member_type);
			mViewholer.tvGold = (TextView) convertView.findViewById(R.id.item_devote_member_gold_money);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHolder) convertView.getTag();
		}
		ShowRoomMemberInfo roomInfo = mList.get(position);
		GoGirlUserInfoP info = roomInfo.getMemberuserinfo();
		if (info != null) {
			imageLoader.displayImage("http://" + info.getUid() + BAConstants.LOAD_HEAD_UID_APPENDSTR, mViewholer.ivHead, options_uid_head);
			mViewholer.tvNick.setText(new String(info.getNick().toByteArray()));

			GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, new String(info.getGradeinfo().toByteArray()), mViewholer.ivVip,
					gradeInfoOptions);

			mViewholer.tvGold.setText(roomInfo.getDevotenum() + "");
			if (type == 0) {
				mViewholer.tvType.setText(R.string.str_show_devote_day_1);
			} else if (type == 1) {
				mViewholer.tvType.setText(R.string.str_show_devote_all_1);
			}

			if (info.getSex() == BAConstants.Gender.FEMALE.getValue()) {
				mViewholer.ivSex.setImageResource(R.drawable.broadcast_img_girl);
				//				holder.typeNametxt.setTextColor(mContext.getResources().getColor(R.color.red));
			} else {
				mViewholer.ivSex.setImageResource(R.drawable.broadcast_img_boy);
				//				holder.typeNametxt.setTextColor(mContext.getResources().getColor(R.color.blue_color));
			}
			
			HeadClickListener headClickListener = new HeadClickListener(info.getUid(), info.getSex());
			mViewholer.ivHead.setOnClickListener(headClickListener);
		}

		return convertView;
	}

	private final class ViewHolder {
		ImageView ivHead;
		TextView tvNick;
		ImageView ivSex;
		ImageView ivVip;
		TextView tvType;
		TextView tvGold;
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
