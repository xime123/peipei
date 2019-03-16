package com.tshang.peipei.model.biz.jobs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.user.UserAlbumBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackUploadPhotos;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.storage.database.entity.PhotoDatabaseEntity;
import com.tshang.peipei.storage.database.operate.PhotoOperate;

import de.greenrobot.event.EventBus;

/**
 * @Title: UploadPhotosJob.java 
 *
 * @Description: 重新上传相片任务线程.
 *
 * @author vactor
 *
 * @date 2014-4-3 下午3:22:44 
 *
 * @version V1.0   
 */
public class ReUploadPhotosJob extends Job implements BizCallBackUploadPhotos {

	private static final long serialVersionUID = 1L;
	private static final int PRIORITY = 1;
	private PhotoDatabaseEntity mPhotoEntity;
	private Context mContext;
	private PhotoOperate photoOperate;
	private List<String> mList = new ArrayList<String>();

	public ReUploadPhotosJob(Context context, int albumId) {
		super(new Params(PRIORITY).setRequiresNetwork(true));
		this.mContext = context;
		if (BAApplication.mLocalUserInfo != null) {
			photoOperate = PhotoOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");

			mPhotoEntity = photoOperate.getPhotoList(BAConstants.UploadStatus.FAILURE.getValue(), albumId);
			if (mPhotoEntity != null) {
				String imageKeys = mPhotoEntity.getImageKeys();
				String[] images = imageKeys.split(";");
				for (int i = 0; i < images.length; i++) {
					mList.add(images[i]);
				}
				EventBus.getDefault().registerSticky(this);
			}
		}
	}

	@Override
	public void onAdded() {

	}

	@Override
	protected void onCancel() {

	}

	//中止上传
	public void onEvent(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE4) {
			mList.clear();
			//删除数据库信息
			if (null != photoOperate) {
				photoOperate.deletePhoto(mPhotoEntity.getCreatetime(), mPhotoEntity.getAlbumId());
				NoticeEvent notice = new NoticeEvent();
				notice.setFlag(NoticeEvent.NOTICE8);
				EventBus.getDefault().post(notice);
			}
		}
	}

	@Override
	public void onRun() throws Throwable {
		UserAlbumBiz biz = new UserAlbumBiz();

		while (mList.size() > 0) {

			String imagPath = mList.get(0);
			Bitmap bitmap = BaseFile.getImageFromFile(imagPath);
			if (bitmap != null) {
				byte[] msg = BaseBitmap.compBitmap2Byte(bitmap, 640, 200);
				int isEnd = mList.size() == 0 ? 1 : 0;
				//启动上传
				if (BAApplication.mLocalUserInfo != null)
					biz.uploadPhotos(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
							mPhotoEntity.getAlbumId(), msg, mPhotoEntity.getTitle(), mPhotoEntity.getDesc(), isEnd, ReUploadPhotosJob.this);
				//等 待 返回
				synchronized (this) {
					this.wait();
				}
			} else {
				mList.remove(0);
			}

		}
		EventBus.getDefault().unregister(this);

	}

	@Override
	protected boolean shouldReRunOnThrowable(Throwable arg0) {
		return false;
	}

	@Override
	public void uploadPhotosCallBack(int retCode, int charmnum) {
		//成功的话,移除头部相片信息,并继续上传
		if (retCode == 0) {
			mList.remove(0);
			//上传完毕,删除数据库中对应的信息
			if (null != mList && mList.size() == 0 && photoOperate != null) {
				photoOperate.deletePhoto(mPhotoEntity.getCreatetime(), mPhotoEntity.getAlbumId());
			}
		}
		int status = retCode == 0 ? BAConstants.UploadStatus.UPLOADING.getValue() : BAConstants.UploadStatus.FAILURE.getValue();
		//更新数据库中的状态,并更新imagekeys 为剩下需要上传的图片 集合(多张情况下以";"为分隔符)
		String imagekyes = getImageKeys(mList);
		if (photoOperate != null)
			photoOperate.updatePhotoStatus(status, imagekyes, mPhotoEntity.getAlbumId(), mPhotoEntity.getCreatetime());
		//更新界面
		NoticeEvent event = new NoticeEvent();
		event.setFlag(NoticeEvent.NOTICE5);
		event.setRetcode(retCode);
		//总张数
		event.setNum(mPhotoEntity.getTotal());
		event.setNum3(charmnum);//返回的魅力值

		int needUpload = mList.size();
		//剩下的还未上传的张数
		event.setNum2(needUpload);
		EventBus.getDefault().post(event);

		//上传失败,退出 循环
		if (retCode != 0) {
			BaseLog.i("vactor_log", "upload error,exit....");
			mList.clear();
		}
		synchronized (this) {
			this.notify();
		}
	}

	public String getImageKeys(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			String imagePath = list.get(i);
			builder.append(imagePath);
			builder.append(";");
		}
		//找到最后一个";"
		int index = builder.lastIndexOf(";");
		//去除最后一个";"
		String imageKeys = builder.toString();
		if (index > 0) {
			imageKeys = builder.substring(0, index).toString();
		}
		return imageKeys;
	}

}
