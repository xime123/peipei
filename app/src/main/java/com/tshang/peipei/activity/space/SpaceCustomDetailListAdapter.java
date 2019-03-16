package com.tshang.peipei.activity.space;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.DeleteReplyDialog;
import com.tshang.peipei.protocol.asn.gogirl.CommentInfo;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.view.ReplyChildListView;

/**
 * 
 * @author Jeff
 *
 */
public class SpaceCustomDetailListAdapter extends ArrayListAdapter<CommentInfo> {

	private BAParseRspData parser;
	private IAddReply mAddReply;
	private int topicUid;//发帖人UID
	private int topicid;//帖子id
	private IIntoSpace intoSpace;
	private DisplayImageOptions options;
	private BAHandler mHandler;

	public SpaceCustomDetailListAdapter(Activity context, int topicUid, int topicid, BAHandler mHandler) {
		super(context);
		parser = new BAParseRspData();
		this.topicUid = topicUid;
		this.topicid = topicid;
		this.mHandler = mHandler;
		options = ImageOptionsUtils.GetHeadUidSmallRounded(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_detail_space_list, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.item_detail_space_head);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.item_detail_space_nick);
			mViewholer.tvCreateTime = (TextView) convertView.findViewById(R.id.item_detail_space_create_time);
			mViewholer.tvContent = (TextView) convertView.findViewById(R.id.item_detail_space_tv_content);
			mViewholer.listView = (ReplyChildListView) convertView.findViewById(R.id.item_detail_space_listview);
			mViewholer.txtAddReply = (TextView) convertView.findViewById(R.id.item_detail_space_tv_oprate);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}
		final CommentInfo commentInfo = mList.get(position);
		mViewholer.txtAddReply.setOnClickListener(new ClickListener(position, commentInfo));
		SpaceCustomDetailChildListAdapter adpater = null;
		//listview 数据 adapter
		if (null == mViewholer.listView.getAdapter()) {
			adpater = new SpaceCustomDetailChildListAdapter(mContext, topicUid);
			mViewholer.listView.setAdapter(adpater);

		} else {
			adpater = (SpaceCustomDetailChildListAdapter) mViewholer.listView.getAdapter();
		}
		mViewholer.listView.setOnItemLongClickListener(new DelReplyListener(commentInfo.id.intValue()));
		adpater.setList(commentInfo.replylist);
		if (BAApplication.mLocalUserInfo != null) {
			if (commentInfo.uid.intValue() == topicUid && BAApplication.mLocalUserInfo.uid.intValue() == commentInfo.uid.intValue()) {
				mViewholer.tvNick.setText("我");
			} else if (commentInfo.uid.intValue() == topicUid && BAApplication.mLocalUserInfo.uid.intValue() != commentInfo.uid.intValue()) {
				mViewholer.tvNick.setText("主人");
			} else if (commentInfo.uid.intValue() != topicUid && BAApplication.mLocalUserInfo.uid.intValue() == commentInfo.uid.intValue()) {
				mViewholer.tvNick.setText("我");
			} else {
				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						commentInfo.uid.intValue());
				String userName = TextUtils.isEmpty(alias) ? new String(commentInfo.nick) : alias;

				mViewholer.tvNick.setText(userName);
			}
		} else {
			if (commentInfo.uid.intValue() == topicUid) {
				mViewholer.tvNick.setText("主人");
			} else {
				String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
						commentInfo.uid.intValue());
				String userName = TextUtils.isEmpty(alias) ? new String(commentInfo.nick) : alias;

				mViewholer.tvNick.setText(userName);
			}
		}

		mViewholer.tvCreateTime.setText(BaseTimes.getFormatTime((commentInfo.createtime.longValue()) * 1000));
		ContentData content = parser.parseTopicInfo(mContext, commentInfo.commentcontentlist, commentInfo.sex.intValue());
		mViewholer.tvContent.setText(ParseMsgUtil.convetToHtml(content.getContent(), mContext, BaseUtils.dip2px(mContext, 24)));
		String key = commentInfo.uid.intValue() + "@true@80@80@uid";
		imageLoader.displayImage("http://" + key, mViewholer.ivHead, options);
		mViewholer.ivHead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (intoSpace != null) {
					intoSpace.intoSpace(commentInfo.uid.intValue(), commentInfo.sex.intValue());
				}
			}
		});
		return convertView;
	}

	public interface IAddReply {
		public void addReply(int position, CommentInfo info);
	}

	public void setAddReplyListener(IAddReply listner) {
		this.mAddReply = listner;
	}

	class ClickListener implements View.OnClickListener {

		private int position;
		private CommentInfo info;

		public ClickListener(int position, CommentInfo info) {
			this.position = position;
			this.info = info;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.item_detail_space_tv_oprate:
				if (null != mAddReply) {
					mAddReply.addReply(position, info);
				}
				break;
			}
		}

	}

	final class ViewHoler {
		ImageView ivHead;
		TextView tvNick;
		TextView tvCreateTime;
		TextView tvContent;
		ReplyChildListView listView;
		TextView txtAddReply;

	}

	public void setIntoSpace(IIntoSpace space) {
		intoSpace = space;
	}

	public interface IIntoSpace {
		public void intoSpace(int uid, int sex);
	}

	private class DelReplyListener implements OnItemLongClickListener {
		private int replyid;

		public DelReplyListener(int replyid) {
			this.replyid = replyid;
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (BAApplication.mLocalUserInfo != null && BAApplication.mLocalUserInfo.uid.intValue() == topicUid) {//只有自己才能够删帖
				CommentInfo info = (CommentInfo) parent.getAdapter().getItem(position);
				new DeleteReplyDialog(mContext, android.R.style.Theme_Translucent_NoTitleBar, replyid, info.id.intValue(), topicid, topicUid,
						topicUid, mHandler).showDialog();
			}

			return true;
		}
	}
}
