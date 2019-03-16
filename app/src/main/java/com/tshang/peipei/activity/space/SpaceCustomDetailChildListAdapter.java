package com.tshang.peipei.activity.space;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.CommentInfo;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.storage.SharedPreferencesTools;

/*
 *类        名 : SpaceCustomDetailChildListAdapter.java
 *功能描述 : 动态详情二级回复
 *作　    者 : vactor
 *设计日期 : 2014-3-27 下午1:38:52
 *修改日期 : 
 *修  改   人: Jeff
 *修 改内容: 
 */
public class SpaceCustomDetailChildListAdapter extends ArrayListAdapter<CommentInfo> {

	private BAParseRspData parser;
	private int topicUid;//发帖人UID

	public SpaceCustomDetailChildListAdapter(Activity context, int topicUid) {
		super(context);
		parser = new BAParseRspData();
		this.topicUid = topicUid;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_detail_space_child_list, null);
			viewHolder.txtNick = (TextView) convertView.findViewById(R.id.item_detail_space_child_nick);
			viewHolder.txtCreateTime = (TextView) convertView.findViewById(R.id.item_detail_space_child_create_time);
			viewHolder.txtContent = (TextView) convertView.findViewById(R.id.item_detail_space_child_tv_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CommentInfo comment = mList.get(position);
		if (BAApplication.mLocalUserInfo != null) {
			if (comment.uid.intValue() == topicUid && BAApplication.mLocalUserInfo.uid.intValue() == comment.uid.intValue()) {
				viewHolder.txtNick.setText("我");
			} else if (comment.uid.intValue() == topicUid && BAApplication.mLocalUserInfo.uid.intValue() != comment.uid.intValue()) {
				viewHolder.txtNick.setText("主人");
			} else if (comment.uid.intValue() != topicUid && BAApplication.mLocalUserInfo.uid.intValue() == comment.uid.intValue()) {
				viewHolder.txtNick.setText("我");
			} else {
				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						comment.uid.intValue());
				String userName = TextUtils.isEmpty(alias) ? new String(comment.nick) : alias;

				viewHolder.txtNick.setText(userName);
			}
		} else {
			if (comment.uid.intValue() == topicUid) {
				viewHolder.txtNick.setText("主人");
			} else {
				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						comment.uid.intValue());
				String userName = TextUtils.isEmpty(alias) ? new String(comment.nick) : alias;

				viewHolder.txtNick.setText(new String(userName));
			}
		}

		viewHolder.txtCreateTime.setText(BaseTimes.getFormatTime((comment.createtime.longValue()) * 1000));

		ContentData contentData = parser.parseTopicInfo(mContext, comment.commentcontentlist, comment.sex.intValue());
		viewHolder.txtContent.setText(ParseMsgUtil.convetToHtml(contentData.getContent(), mContext, BaseUtils.dip2px(mContext, 24)));
		return convertView;
	}

	class ViewHolder {
		TextView txtNick;
		TextView txtCreateTime;
		TextView txtContent;
	}

}
