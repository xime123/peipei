package com.tshang.peipei.activity.main.message.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsCommentInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: DynamicReplyDetailsAdapter.java 
 *
 * @Description: 动态回复二级回复 
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午5:06:10 
 *
 * @version V1.0   
 */
@SuppressLint({ "ViewHolder", "InflateParams" })
public class DynamicReplyDetailsAdapter extends ArrayListAdapter<DynamicsCommentInfo> {

	private Context mContext;
	private BAParseRspData parser;

	public DynamicReplyDetailsAdapter(Activity context) {
		super(context);
		this.mContext = context;
		parser = new BAParseRspData();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler = null;
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dynamic_details_reply_child, null);
			viewHodler.nickTV = (TextView) convertView.findViewById(R.id.dynamic_detail_reply_child_nick);
			viewHodler.ContentTV = (TextView) convertView.findViewById(R.id.dynamic_detail_reply_child_content);
			viewHodler.timeTV = (TextView) convertView.findViewById(R.id.dynamic_detail_reply_child_time);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		final ContentData data = parser.parseTopicInfo(mContext, mList.get(position).commentcontentlist, 0);
		if (BAApplication.mLocalUserInfo.uid.intValue() == mList.get(position).uid.intValue()) {
			viewHodler.nickTV.setText(mContext.getResources().getString(R.string.me));
		} else {
			//显示备注
			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					mList.get(position).uid.intValue());
			viewHodler.nickTV.setText(TextUtils.isEmpty(alias) ? new String(mList.get(position).nick) : alias);
		}
		viewHodler.ContentTV.setText(data.getContent());
		viewHodler.timeTV.setText(BaseTimes.getTime(mList.get(position).createtime.longValue() * 1000));

		return convertView;
	}

	private class ViewHodler {
		private TextView nickTV, timeTV, ContentTV;
	}

}
