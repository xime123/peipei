package com.tshang.peipei.activity.harem.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.KickedHaremMemberDialog;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.broadcast.GradeInfoImgUtils;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 *
 * @Description: 后宫对应的适配器
 *
 * @author Jeff 
 *
 * @version V1.3.0   
 */
public class HaremMemberAdapter extends ArrayListAdapter<GroupMemberInfo> {
	private DisplayImageOptions options;
	private DisplayImageOptions gradeInfoOptions;
	private BAHandler handler;
	private int groupid;
	private boolean isHost = false;

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
		notifyDataSetChanged();
	}

	public HaremMemberAdapter(Activity context, int groupid, BAHandler handler) {
		super(context);
		options = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		gradeInfoOptions = ImageOptionsUtils.getGradeInfoImageKeyOptions(context);
		this.handler = handler;
		this.groupid = groupid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			row = inflater.inflate(R.layout.adapter_harem_group_member_item, null);
			holder = new ViewHolder();
			holder.headerImage = (ImageView) row.findViewById(R.id.iv_harem_group_member_head_item);
			holder.nametxt = (TextView) row.findViewById(R.id.tv_harem_group_member_name_item);
			holder.leverImage = (ImageView) row.findViewById(R.id.iv_harem_group_member_level_item);
			holder.tvOperate = (TextView) row.findViewById(R.id.tv_harem_group_member_operate_item);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		final GroupMemberInfo info = mList.get(position);
		if (info != null) {
			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					info.uid.intValue());
			String sendUserName = TextUtils.isEmpty(alias) ? new String(info.nick) : alias;

			holder.nametxt.setText(sendUserName);
			String gradeinfo = BaseUtils.byteToString(info.gradeinfo);
			if (!TextUtils.isEmpty(gradeinfo)) {
				holder.leverImage.setVisibility(View.VISIBLE);
				GradeInfoImgUtils.loadGradeInfoImg(mContext, imageLoader, gradeinfo, holder.leverImage, gradeInfoOptions);
			} else {
				holder.leverImage.setVisibility(View.GONE);
			}
			imageLoader.displayImage("http://" + info.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR, holder.headerImage, options);
			int level = info.level.intValue();
			if (level == 9000) {//说明是宫主
				holder.tvOperate.setVisibility(View.VISIBLE);
				holder.tvOperate.setText(R.string.str_harem_owner);
				holder.tvOperate.setTextColor(mContext.getResources().getColor(R.color.peach));
				holder.tvOperate.setBackgroundResource(android.R.color.transparent);
			} else {//说明是成员
				if (isHost) {
					holder.tvOperate.setText(R.string.str_harem_kicked_out);
					holder.tvOperate.setTextColor(mContext.getResources().getColor(R.color.white));
					holder.tvOperate.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							new KickedHaremMemberDialog(mContext, android.R.style.Theme_Translucent_NoTitleBar, handler, groupid, new String(
									info.nick), info.uid.intValue(), false).showDialog();

						}
					});
				} else {
					holder.tvOperate.setVisibility(View.GONE);
				}
			}
		}

		return row;
	}

	final class ViewHolder {
		ImageView headerImage;
		TextView nametxt;
		ImageView leverImage;
		TextView tvOperate;

	}

}
