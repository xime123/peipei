package com.tshang.peipei.model.biz.jobs;
//package com.tshang.peipei.model.biz.jobs;
//
//import java.util.List;
//
//import android.content.Context;
//
//import com.path.android.jobqueue.Job;
//import com.path.android.jobqueue.Params;
//import com.tshang.peipei.base.babase.BAConstants;
//import com.tshang.peipei.base.babase.BATools;
//import com.tshang.peipei.model.event.NoticeEvent;
//import com.tshang.peipei.storage.database.entity.PhotoDatabaseEntity;
//import com.tshang.peipei.storage.database.operate.PhotoOperate;
//import com.tshang.peipei.storage.database.operate.PublishOperate;
//
//import de.greenrobot.event.EventBus;
//
///**
// * @Title: UploadPhotosJob.java 
// *
// * @Description: 从数据库中查询到未上传成功的图片(上传图片)
// *
// * @author vactor
// *
// * @date 2014-4-3 下午3:22:44 
// *
// * @version V1.0   
// */
//public class GetFailureOrUploadingPhotoFromDbJob extends Job {
//
//	private static final long serialVersionUID = 1L;
//	private static final int PRIORITY = 1;
//	private boolean flag;//标记是否查询正在发送的贴子
//	private List<PhotoDatabaseEntity> mList;
//	private Context mContext;
//
//	private PhotoOperate mPhotoOperate;
//	private boolean mFlag;
//
//	public GetFailureOrUploadingPhotoFromDbJob(Context context, boolean flag) {
//		super(new Params(PRIORITY).setRequiresNetwork(false));
//		this.mContext = context;
//		mPhotoOperate = PhotoOperate.getInstance(mContext, BATools.getDB(mContext));
//		this.mFlag = flag;
//	}
//
//	@Override
//	public void onAdded() {
//
//	}
//
//	@Override
//	protected void onCancel() {
//
//	}
//
//	@Override
//	public void onRun() throws Throwable {
//
//		//判断是否存在
//		if (mFlag) {
//			mPhotoOperate.isHaveUploading(BAConstants.UploadStatus.FAILURE.getValue(), BAConstants.UploadStatus.UPLOADING.getValue());
//			NoticeEvent noticeEvent = new NoticeEvent();
//			noticeEvent.setFlag(NoticeEvent.NOTICE6);
//			EventBus.getDefault().post(noticeEvent);
//		} else {
//			//查询 
//
//		}
//
//	}
//
//	@Override
//	protected boolean shouldReRunOnThrowable(Throwable arg0) {
//		return false;
//	}
//
//	private void getFailure() {
//
//	}
//
//}
