package com.tshang.peipei.model.biz.jobs;

import java.util.List;

import android.content.Context;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.storage.database.entity.PublishDatabaseEntity;
import com.tshang.peipei.storage.database.operate.PublishOperate;

import de.greenrobot.event.EventBus;

/**
 * @Title: UploadPhotosJob.java 
 *
 * @Description: 从数据库中接到未上传成功的帖子
 *
 * @author vactor
 *
 * @date 2014-4-3 下午3:22:44 
 *
 * @version V1.0   
 */
public class GetFailureOrSendingTopicFromDbJob extends Job {

	private static final long serialVersionUID = 1L;
	private static final int PRIORITY = 1;
	private boolean flag;//标记是否查询正在发送的贴子
	//	private List<PhotoEntity> mList;
	private Context mContext;

	private PublishOperate mPublishOperate;

	public GetFailureOrSendingTopicFromDbJob(Context context, boolean flag) {
		super(new Params(PRIORITY).setRequiresNetwork(false));
		this.mContext = context;
		this.flag = flag;
		if (BAApplication.mLocalUserInfo != null) {
			mPublishOperate = PublishOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
		}
	}

	@Override
	public void onAdded() {

	}

	@Override
	protected void onCancel() {

	}

	@Override
	public void onRun() throws Throwable {
		try {
			if (mPublishOperate != null) {
				List<PublishDatabaseEntity> list = null;
				if (flag) {
					list = mPublishOperate.getPublishList(BAConstants.UploadStatus.FAILURE.getValue(), BAConstants.UploadStatus.UPLOADING.getValue());
				} else {
					list = mPublishOperate.getPublishList(BAConstants.UploadStatus.FAILURE.getValue());

				}
				NoticeEvent noticeEvent = new NoticeEvent();
				noticeEvent.setFlag(NoticeEvent.NOTICE16);
				noticeEvent.setObj(list);
				EventBus.getDefault().post(noticeEvent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean shouldReRunOnThrowable(Throwable arg0) {
		return false;
	}

}
