package com.tshang.peipei.activity.main.message.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsCommentInfo;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsCommentInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.view.ReplyChildListView;

/**
 * @Title: DynamicDetailsAdapter.java 
 *
 * @Description: 动态回复列表
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午4:10:02 
 *
 * @version V1.0   
 */
@SuppressLint("InflateParams")
public class DynamicDetailsAdapter extends ArrayListAdapter<DynamicsCommentInfo> {

	private Context mContext;
	private DisplayImageOptions options_head;

	private BAParseRspData parser;

	private ChildReplyCallBack callBack;

	private List<DynamicReplyDetailsAdapter> adapters;

	public DynamicDetailsAdapter(Activity context) {
		super(context);
		this.mContext = context;
		options_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		parser = new BAParseRspData();
		adapters = new ArrayList<DynamicReplyDetailsAdapter>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler = null;
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dynamic_details_reply, null);
			viewHodler.nick = (TextView) convertView.findViewById(R.id.dynamic_detail_reply_nick);
			viewHodler.content = (TextView) convertView.findViewById(R.id.dynamic_detail_reply_content);
			viewHodler.time = (TextView) convertView.findViewById(R.id.dynamic_detail_reply_time);
			viewHodler.headiv = (ImageView) convertView.findViewById(R.id.dynamic_detail_reply_head);
			viewHodler.replyiv = (ImageView) convertView.findViewById(R.id.dynamic_detail_reply_btn);
			viewHodler.mListView = (ReplyChildListView) convertView.findViewById(R.id.dynamic_detail_reply_listview);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}

		DynamicsCommentInfo info = mList.get(position);
		final ContentData data = parser.parseTopicInfo(mContext, info.commentcontentlist, info.sex.intValue());
		String headKey = info.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + headKey, viewHodler.headiv, options_head);
		if (BAApplication.mLocalUserInfo.uid.intValue() == mList.get(position).uid.intValue()) {
			viewHodler.nick.setText(mContext.getResources().getString(R.string.me));
		} else {
			//显示备注
			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					mList.get(position).uid.intValue());
			viewHodler.nick.setText(TextUtils.isEmpty(alias) ? new String(mList.get(position).nick) : alias);
		}

		viewHodler.content.setText(data.getContent());
		viewHodler.time.setText(BaseTimes.getTime(info.createtime.longValue() * 1000));
		//判断二级回复
		DynamicsCommentInfoList infoList = mList.get(position).replylist;
		if (infoList != null && infoList.size() > 0) {
			DynamicReplyDetailsAdapter mAdapter = new DynamicReplyDetailsAdapter((Activity) mContext);
			adapters.add(mAdapter);
			ArrayList<DynamicsCommentInfo> commentInfos = new ArrayList<DynamicsCommentInfo>();
			for (int i = 0; i < infoList.size(); i++) {
				commentInfos.add((DynamicsCommentInfo) infoList.get(i));
			}
			mAdapter.appendToList(commentInfos);
			viewHodler.mListView.setAdapter(mAdapter);
			viewHodler.mListView.setVisibility(View.VISIBLE);
		} else {
			viewHodler.mListView.setVisibility(View.GONE);
		}
		viewHodler.replyiv.setOnClickListener(new ClickListener(position, info));
		viewHodler.headiv.setOnClickListener(new ClickListener(position, info));
		return convertView;
	}

	private class ViewHodler {
		private TextView nick, content, time;
		private ImageView headiv, replyiv;
		private ReplyChildListView mListView;
	}

	class ClickListener implements OnClickListener {
		int position;
		DynamicsCommentInfo info;

		public ClickListener(int position, DynamicsCommentInfo info) {
			this.position = position;
			this.info = info;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dynamic_detail_reply_btn:
				if (callBack != null) {
					callBack.onClickReplyBtn(info);
				}
				break;
			case R.id.dynamic_detail_reply_head:
				SpaceUtils.toSpaceCustom((Activity) mContext, info.uid.intValue(), info.sex.intValue());
				break;

			default:
				break;
			}
		}
	}

	public void clearChildReplyList() {
		if (adapters != null && adapters.size() > 0) {
			for (int i = 0; i < adapters.size(); i++) {
				adapters.get(i).clearList();
			}
		}
	}

	public void setReplyCallBack(ChildReplyCallBack callBack) {
		this.callBack = callBack;
	}

	public interface ChildReplyCallBack {
		public void onClickReplyBtn(DynamicsCommentInfo info);
	}

}
