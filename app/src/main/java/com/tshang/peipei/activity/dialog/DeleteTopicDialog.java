package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.os.Message;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.space.SpaceCustomAdapter;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.main.MainMySpaceBiz;
import com.tshang.peipei.model.request.RequestDeleteTopic.IDeleteTopic;
import com.tshang.peipei.storage.database.operate.PublishOperate;

/**
 * @Title: DeleteTopicDialog.java 
 *
 * @Description: 删除帖子对话框
 *
 * @author allen  
 *
 * @date 2014-7-17 上午10:45:46 
 *
 * @version V1.0   
 */
public class DeleteTopicDialog extends BaseNormalDialog implements IDeleteTopic {
	private SpaceCustomAdapter adapter;
	private TopicInfo info;
	private BAHandler handler;
	private int Flag;

	public DeleteTopicDialog(Activity context, int theme, int title, int sure, int cancel, SpaceCustomAdapter adapter, BAHandler handler,
			TopicInfo info, int DEELETE_TOPIC) {
		super(context, theme, title, sure, cancel);
		this.adapter = adapter;
		this.info = info;
		this.handler = handler;
		Flag = DEELETE_TOPIC;
	}

	@Override
	public void OnclickSure() {
		if (info.id.intValue() < 0) {
			BaseUtils.showDialog(context, R.string.str_deleting);
			if (BAApplication.mLocalUserInfo != null) {
				PublishOperate publisOperate = PublishOperate.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "");
				publisOperate.deleteCurrentTime(String.valueOf(info.createtime));
				adapter.getList().remove(info);
				adapter.notifyDataSetChanged();
			}
		} else {
			MainMySpaceBiz bizSpace = new MainMySpaceBiz();
			int topicId = info.topicid.intValue();
			int topicUid = info.uid.intValue();
			BaseUtils.showDialog(context, R.string.str_deleting);
			bizSpace.delteTopic((Activity) context, topicId, topicUid, this);
		}
	}

	@Override
	public void deleteTopicCallBack(int retCode) {
		if (null != handler) {
			Message msg = handler.obtainMessage();
			msg.what = Flag;
			msg.arg1 = retCode;
			handler.sendMessage(msg);
		}

	}

}
