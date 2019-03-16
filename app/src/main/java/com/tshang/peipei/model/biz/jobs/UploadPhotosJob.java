package com.tshang.peipei.model.biz.jobs;

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
import com.tshang.peipei.model.entity.PhotoEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.storage.database.entity.PhotoDatabaseEntity;
import com.tshang.peipei.storage.database.operate.PhotoOperate;

import de.greenrobot.event.EventBus;

/**
 * @Title: UploadPhotosJob.java 
 *
 * @Description: 上传相片任务线程.
 *
 * @author vactor
 *
 * @date 2014-4-3 下午3:22:44 
 *
 * @version V1.0   
 */
public class UploadPhotosJob extends Job implements BizCallBackUploadPhotos {

	private static final long serialVersionUID = 1L;
	private static final int PRIORITY = 1;
	//上传的总张数
	private int uploadTotal;
	private List<PhotoEntity> mList;

	private Context mContext;
	private int mAlbumId;
	private String mCurrentTime;
	private PhotoOperate photoOperate;

	protected UploadPhotosJob(Context context) {
		super(new Params(PRIORITY).setRequiresNetwork(true));
		this.mContext = context;
		if (BAApplication.mLocalUserInfo != null) {
			photoOperate = PhotoOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
		}
		EventBus.getDefault().register(this);
	}

	public UploadPhotosJob(Context context, int albumId, List<PhotoEntity> list) {
		this(context);
		this.mAlbumId = albumId;
		this.mList = list;
		uploadTotal = list.size();
	}

	@Override
	public void onAdded() {

		NoticeEvent notice = new NoticeEvent();
		notice.setFlag(NoticeEvent.NOTICE7);
		EventBus.getDefault().post(notice);

	}

	@Override
	protected void onCancel() {

	}

	public void onEventMainThread(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE4) {
			mList.clear();
		}
	}

	@Override
	public void onRun() throws Throwable {
		//如果正在上传
		if (photoOperate != null) {
			boolean isUploading = photoOperate.getPhotoStatus(BAConstants.UploadStatus.UPLOADING.getValue());
			if (isUploading) {
				NoticeEvent notice = new NoticeEvent();
				notice.setFlag(NoticeEvent.NOTICE6);
				EventBus.getDefault().post(notice);
				return;
			}
			PhotoDatabaseEntity photo = new PhotoDatabaseEntity();
			photo.setAlbumId(mAlbumId);
			photo.setUserId(BAApplication.mLocalUserInfo.uid.intValue());
			photo.setNickName(new String(BAApplication.mLocalUserInfo.nick));
			photo.setGender(BAApplication.mLocalUserInfo.sex.intValue());
			photo.setTitle("");
			photo.setDesc("");
			mCurrentTime = System.currentTimeMillis() + "";
			photo.setCreatetime(mCurrentTime);
			photo.setProvince("");
			photo.setCity(new String(BAApplication.mLocalUserInfo.city));
			photo.setDetailAddress("");
			String imageKeys = getImageKeys(mList);
			photo.setImageKeys(imageKeys);
			photo.setStatus(BAConstants.UploadStatus.UPLOADING.getValue());
			photo.setErrorCode(0);
			photo.setTotal(mList.size());
			//插入数据库
			photoOperate.insertPhoto(photo);
			UserAlbumBiz biz = new UserAlbumBiz();

			while (mList.size() > 0) {
				PhotoEntity info = mList.get(0);
				Bitmap bitmap = BaseFile.getImageFromFile(info.getPath());
				if (bitmap != null) {
					byte[] msg = BaseBitmap.compBitmap2Byte(bitmap, 640, 200);
					bitmap.recycle();
					bitmap = null;
//					int isEnd = mList.size() == 1 ? 1 : 0;
					int isEnd = 0;
					//启动上传
					biz.uploadPhotos(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
							mAlbumId, msg, info.getTitle(), info.getDesc(), isEnd, UploadPhotosJob.this);
					//等 待 返回
					synchronized (this) {
						this.wait();
					}
				} else {
					//　选择相片的时候有可能选择的相片数据是无效数据，所以这里删掉，继续下一张上传
					mList.remove(0);
				}

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
		BaseLog.i("vactor_log", "upload back:" + retCode);
		if (retCode == 0) {
			mList.remove(0);
			//上传完毕,删除数据库中对应的信息
			if (null != mList && mList.size() == 0 && photoOperate != null) {
				photoOperate.deletePhoto(mCurrentTime, mAlbumId);
			}
		}
		int status = retCode == 0 ? BAConstants.UploadStatus.UPLOADING.getValue() : BAConstants.UploadStatus.FAILURE.getValue();
		//更新数据库中的状态,并更新imagekeys 为剩下需要上传的图片 集合(多张情况下以";"为分隔符)
		String imagekyes = getImageKeys(mList);
		if (photoOperate != null)
			photoOperate.updatePhotoStatus(status, imagekyes, mAlbumId, mCurrentTime);
		//更新界面
		NoticeEvent event = new NoticeEvent();
		event.setFlag(NoticeEvent.NOTICE5);
		event.setRetcode(retCode);
		//总张数
		event.setNum(uploadTotal);
		event.setNum3(charmnum);//返回的魅力值

		//剩下的还未上传的张数
		int needUpload = mList.size();
		event.setNum2(needUpload);
		EventBus.getDefault().post(event);

		//上传失败,退出 循环
		if (retCode != 0) {
			mList.clear();
		}
		synchronized (this) {
			this.notify();
		}

	}

	public String getImageKeys(List<PhotoEntity> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			PhotoEntity photo = list.get(i);
			builder.append(photo.getPath());
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
