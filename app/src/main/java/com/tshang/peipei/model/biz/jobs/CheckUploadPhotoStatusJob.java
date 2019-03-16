package com.tshang.peipei.model.biz.jobs;

import android.content.Context;
import android.text.TextUtils;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.storage.database.operate.PhotoOperate;
import com.tshang.peipei.storage.database.operate.PublishOperate;

import de.greenrobot.event.EventBus;

/**
 * @Title: CheckUploadStatusPhotoJob.java 
 *
 * @Description: 退出程序时，检验是否有正在上传的图片,如果有正在上传的图片，则把上传的状态置为false
 *
 * @author vactor
 *
 * @date 2014-4-24 下午9:18:05 
 *
 * @version V1.0   
 */
public class CheckUploadPhotoStatusJob extends Job {

	private static final long serialVersionUID = 1L;
	private static final int PRIORITY = 1;
	private Context mContext;

	public CheckUploadPhotoStatusJob(Context context) {
		super(new Params(PRIORITY).setRequiresNetwork(false));
		this.mContext = context;
	}

	@Override
	public void onAdded() {
		//通知相片上传线程停止
		NoticeEvent event = new NoticeEvent();
		event.setFlag(NoticeEvent.NOTICE4);
		EventBus.getDefault().postSticky(event);

		//通知写贴中的上传线程停止
		NoticeEvent event2 = new NoticeEvent();
		event.setFlag(NoticeEvent.NOTICE17);
		EventBus.getDefault().postSticky(event2);
	}

	@Override
	protected void onCancel() {

	}

	@Override
	public void onRun() throws Throwable {
		try {
			BaseLog.i("vactor_log", "delete all uploading data in db");
			if (BAApplication.mLocalUserInfo != null) {
				String dataBaseName = BAApplication.mLocalUserInfo.uid.intValue() + "";
				if (!TextUtils.isEmpty(dataBaseName)) {
					PhotoOperate photoOperate = PhotoOperate.getInstance(mContext, dataBaseName);

					//TODO
					//删除所有正在上传的数据,(这里处理的逻辑不太正确，应该是更新数据库的状态为失败状态)
					photoOperate.deletePhoto();
					PublishOperate publishOperate = PublishOperate.getInstance(mContext, dataBaseName);
					//TODO
					//写贴，删除所有正在上传的数据(这里处理的逻辑不太正确，应该是更新数据库的状态为失败状态)
					publishOperate.deleteTopic();
					photoOperate = null;
					publishOperate = null;
				}
				mContext = null;
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
