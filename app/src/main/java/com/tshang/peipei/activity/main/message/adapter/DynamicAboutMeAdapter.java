package com.tshang.peipei.activity.main.message.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.AboutMeReplyInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.NewDynamicReplyEntity;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: DynamicAboutMeAdapter.java 
 *
 * @Description: 关于我动态适配器 
 *
 * @author Aaron  
 *
 * @date 2015-8-18 下午2:58:19 
 *
 * @version V1.0   
 */
public class DynamicAboutMeAdapter extends ArrayListAdapter<NewDynamicReplyEntity> {

	private Context mContext;

	private DisplayImageOptions options_head;

	protected DisplayImageOptions options;

	public DynamicAboutMeAdapter(Context context) {
		super((Activity) context);
		this.mContext = context;
		options_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		options = ImageOptionsUtils.getImageKeyOptions((Activity) context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHoler viewholer;
		if (convertView == null) {
			viewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_dynamic_about_me_item_layout, parent, false);
			initViewHoler(convertView, viewholer);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHoler) convertView.getTag();
		}

		final NewDynamicReplyEntity entity = mList.get(position);
		//显示备注
		String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
				entity.getFromuid());
		viewholer.nameText.setText(TextUtils.isEmpty(alias) ? entity.getNick() : alias);

		viewholer.timeText.setText(BaseTimes.getTime(entity.getCreatetime() * 1000));
		viewholer.contentText.setText(entity.getReplyContent());
		String headKey = entity.getFromuid() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + headKey, viewholer.headerImage, options_head);
		//是否已读 1未读 0已读
		int readStatus = entity.getStatus();
		if (readStatus == 1) {
			viewholer.headerNew.setVisibility(View.VISIBLE);
		} else {
			viewholer.headerNew.setVisibility(View.INVISIBLE);
		}
		int status = entity.getAuditstatus();
		if (status == 2 || BAApplication.mLocalUserInfo.uid.intValue() == entity.getTopicuid()) {
			viewholer.imageView.setVisibility(View.VISIBLE);
			viewholer.dynamicContent.setVisibility(View.GONE);
			String imgKey = entity.getImageKey() + "@false@500@500";
			imageLoader.displayImage("http://" + imgKey, viewholer.imageView, options);
		} else {
			viewholer.imageView.setVisibility(View.GONE);
			viewholer.dynamicContent.setVisibility(View.VISIBLE);
			viewholer.dynamicContent.setText(entity.getDynamicContent());
			String bgColor = entity.getColor();
			if (!TextUtils.isEmpty(bgColor)) {
				viewholer.dynamicContent.setBackgroundColor(Color.parseColor("#" + bgColor));
			} else {
				viewholer.dynamicContent.setBackgroundColor(mContext.getResources().getColor(R.color.official_bg1));
			}
			int fontType = entity.getFonttype();
			if (fontType == 0) {
				viewholer.dynamicContent.setTextColor(mContext.getResources().getColor(R.color.black));
			} else {
				viewholer.dynamicContent.setTextColor(mContext.getResources().getColor(R.color.white));
			}
		}
		viewholer.headerImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SpaceUtils.toSpaceCustom((Activity) mContext, entity.getFromuid(), entity.getSex());
			}
		});
		return convertView;
	}

	private void initViewHoler(View convertView, ViewHoler viewholer) {
		viewholer.headerImage = (ImageView) convertView.findViewById(R.id.new_dynamic_list_header_image);
		viewholer.headerNew = (ImageView) convertView.findViewById(R.id.new_dynamic_list_header_text);
		viewholer.nameText = (TextView) convertView.findViewById(R.id.new_dynamic_list_chat_name);
		viewholer.timeText = (TextView) convertView.findViewById(R.id.new_dynamic_list_chat_time);
		viewholer.contentText = (TextView) convertView.findViewById(R.id.new_dynamic_list_content);
		viewholer.imageView = (ImageView) convertView.findViewById(R.id.new_dynamic_list_iv);
		viewholer.dynamicContent = (TextView) convertView.findViewById(R.id.new_dynamic_list_tv);
	}

	final class ViewHoler {
		ImageView headerImage;
		ImageView headerNew;
		ImageView imageView;
		TextView nameText;
		ImageView sexImage;
		TextView timeText;
		TextView contentText;
		TextView dynamicContent;
	}

	/**
	 * 获取是否已点赞
	 * @author Aaron
	 *
	 * @param info
	 * @return
	 */
	public boolean getIsParise(AboutMeReplyInfo info) {
		return SharedPreferencesTools.getInstance(mContext).getBooleanKeyValue(info.topicuid + "-" + info.topicid.intValue());
	}

	public void updateReadStatus(long createTime) {
		for (int i = 0; i < getList().size(); i++) {
			if (createTime == getList().get(i).getCreatetime()) {
				getList().get(i).setStatus(0);
				notifyDataSetChanged();
			}
		}
	}
}
