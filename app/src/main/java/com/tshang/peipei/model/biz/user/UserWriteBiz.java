package com.tshang.peipei.model.biz.user;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackAddTopic;
import com.tshang.peipei.model.entity.PhotoEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.request.RequestAddTopic;
import com.tshang.peipei.model.request.RequestAddTopic.IAddTopic;
import com.tshang.peipei.model.request.RequestUploadTopicContent;
import com.tshang.peipei.model.request.RequestUploadTopicContent.IUploadTopicContent;
import com.tshang.peipei.storage.database.operate.PublishOperate;

import de.greenrobot.event.EventBus;

/**
 * @Title: UserWriteBiz.java 
 *
 * @Description: 写贴,删除,更新等业务层
 *
 * @author vactor 
 *
 * @date 2014-4-10 下午8:09:55 
 *
 * @version V1.0   
 */
public class UserWriteBiz implements IAddTopic, IUploadTopicContent {

	private BizCallBackAddTopic bizCallBackAddTopic;
	private int topicId;
	private List<PhotoEntity> photoList;
	private Activity mContext;

	private String currentTime;
	private String content;
	private String bitmapPath;

	public UserWriteBiz(Activity context) {
		this.mContext = context;
		EventBus.getDefault().register(this);
	}

	public void onEventMainThread(NoticeEvent event) {
		if (event.getFlag() == NoticeEvent.NOTICE17) {
			photoList.clear();
		}
	}

	// 图片数量>1时,写贴有二个步骤,此处为第一步
	public void addTopicIfHasManyPhotos(String city, long time, int la, int lo, String province, String content, List<PhotoEntity> list,
			BizCallBackAddTopic callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(mContext);
		if (userEntity == null) {
			return;
		}
		this.photoList = list;
		RequestAddTopic requestAddTopic = new RequestAddTopic();
		this.bizCallBackAddTopic = callBack;
		this.content = content;
		BaseLog.i("vactor_log", "upload many photos size:" + list.size());
		requestAddTopic.addTopicIfHasManyPhotos(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), city, time, la, lo,
				province, content, list, this);
	}

	//图片数量==1时,写贴一步完成
	public void addTopicIfHasOnePhoto(String city, long time, int la, int lo, String province, String content, String bitmapPath,
			BizCallBackAddTopic callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(mContext);
		if (userEntity == null) {
			return;
		}

		RequestAddTopic requestAddTopic = new RequestAddTopic();
		this.bizCallBackAddTopic = callBack;
		PublishOperate publisOperate = null;
		if (BAApplication.mLocalUserInfo != null)
			publisOperate = PublishOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
		currentTime = System.currentTimeMillis() + "";
		//根据内容+时间戳确定唯一性,更新数据库,所以这里先临时存储一下内容
		this.content = content;
		this.bitmapPath = bitmapPath;
		int status = BAConstants.UploadStatus.UPLOADING.getValue();
		//一张时,因为topicId,不会从服务器获得,所以这里以当前时间作为topicId,更新时,以当前时间为主键,进行更新
		if (publisOperate != null)
			publisOperate.insertPublish(userEntity.uid.intValue(), currentTime, new String(userEntity.nick), userEntity.sex.intValue(), "", city,
					content, bitmapPath, BAConstants.MessageType.TEXT.getValue(), status, currentTime);
		byte[] msg = null;
		if (!TextUtils.isEmpty(bitmapPath)) {
			Bitmap bitmap = BaseFile.getImageFromFile(bitmapPath);
			msg = BaseBitmap.compBitmap2Byte(bitmap, 640, 200);
		}
		requestAddTopic.addTopicIfHasOnePhoto(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), city, time, la, lo,
				province, content, msg, this);
	}

	@Override
	public void addTopicCallBack(int retCode, int topicId, int charmnum, GoGirlDataInfoList list) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(mContext);
		if (userEntity == null) {
			return;
		}
		//如果list.size()>0 表示写贴时有多张图片需要上传,启动上传第二步
		if (null != photoList && photoList.size() > 0) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < photoList.size(); i++) {
				PhotoEntity photo = photoList.get(i);
				builder.append(photo.getPath());
				builder.append(";");
			}
			builder.deleteCharAt(builder.length() - 1);//去除最后一个";"
			String imageKeys = builder.toString();

			BaseLog.i("vactor_log", "imagekeys: " + imageKeys);
			int type = BAConstants.MessageType.IMAGE_KEY.getValue();
			int status = BAConstants.UploadStatus.UPLOADING.getValue();

			PublishOperate publisOperate = null;
			//插入数据库，这里可以考虑提前插入数据库
			if (BAApplication.mLocalUserInfo != null) {
				publisOperate = PublishOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
				publisOperate.insertPublish(BAApplication.mLocalUserInfo.uid.intValue(), topicId + "", new String(BAApplication.mLocalUserInfo.nick),
						BAApplication.mLocalUserInfo.sex.intValue(), "", new String(BAApplication.mLocalUserInfo.city), content,
						imageKeys.toString(), type, status, System.currentTimeMillis() + "");
			}

			this.topicId = topicId;
			//成功则启动上传
			if (retCode == 0) {
				//回包数据
				GoGirlDataInfo rspData = (GoGirlDataInfo) list.get(0);
				//取得图片数据
				PhotoEntity info = photoList.get(0);
				Bitmap bitmap = BaseFile.getImageFromFile(info.getPath());
				byte[] bitmapData = BaseBitmap.compBitmap2Byte(bitmap, 640, 200);
				//上传
				requestUploadTopicContent(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), topicId, bitmapData, rspData);
				//移除第1个上传的
				photoList.remove(0);
			} else {
				//失败,更新数据库.
				if (null != bizCallBackAddTopic) {
					bizCallBackAddTopic.addTopicCallBack(retCode, topicId,charmnum, list);
				}
				if (publisOperate != null)
					publisOperate.updateStatusByTopicId(BAConstants.UploadStatus.FAILURE.getValue(), imageKeys, topicId + "");
			}
			publisOperate = null;

			//上传一步完成的情况
		} else {
			if (null != bizCallBackAddTopic) {
				bizCallBackAddTopic.addTopicCallBack(retCode, topicId,charmnum, list);
			}

			int status = retCode == 0 ? BAConstants.UploadStatus.SUCCESS.getValue() : BAConstants.UploadStatus.FAILURE.getValue();
			//更新数据库
			PublishOperate publisOperate = null;
			if (BAApplication.mLocalUserInfo != null)
				publisOperate = PublishOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
			//写贴成功,直接删除数据库中对应的数据
			if (BAApplication.mLocalUserInfo != null)
				if (status == 0) {
					publisOperate.deleteCurrentTime(currentTime);
				} else {
					publisOperate.updateStatusByTopicId(status, bitmapPath, currentTime);
				}
			publisOperate = null;
			EventBus.getDefault().unregister(this);

		}

	}

	//当图片数量>1时,写贴最后一步
	public void requestUploadTopicContent(byte[] auth, int ver, int uid, int topicId, byte[] bitmapData, GoGirlDataInfo dataInfo) {
		RequestUploadTopicContent requestUploadContent = new RequestUploadTopicContent();
		requestUploadContent.requestUploadTopicContent(auth, ver, uid, topicId, bitmapData, dataInfo, this);
	}

	@Override
	public void uploadTopicContentCallBack(int retCode, GoGirlDataInfoList list) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(mContext);
		if (userEntity == null) {
			return;
		}
		PublishOperate publisOperate = null;
		if (BAApplication.mLocalUserInfo != null)
			publisOperate = PublishOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");

		if (retCode == 0) {
			//循环上传
			if (photoList.size() > 0) {
				BaseLog.i("vactor_log", "upload call back list.size>" + list.size());
				String imageKeys = getImageKeys(photoList);
				//更新数据库,imageKeys为剩下需要上传的图片路径
				if (publisOperate != null)
					publisOperate.updateStatusByTopicId(BAConstants.UploadStatus.UPLOADING.getValue(), imageKeys, topicId + "");
				//回包数据
				GoGirlDataInfo rspData = (GoGirlDataInfo) list.get(0);
				//再取第一个开始上传
				PhotoEntity info = photoList.get(0);
				Bitmap bitmap = BaseFile.getImageFromFile(info.getPath());
				byte[] bitmapData = BaseBitmap.compBitmap2Byte(bitmap, 640, 200);
				//上传
				requestUploadTopicContent(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), topicId, bitmapData, rspData);
				//移除头部相片信息
				photoList.remove(0);

			} else {
				BaseLog.i("vactor_log", "upload complete and this topicId is:" + topicId);
				//所有图片上传完成 ,删除数据库对应信息
				if (publisOperate != null)
					publisOperate.deleteTopicId(topicId + "");
				//所有图片上传完毕,写贴完成
				if (null != bizCallBackAddTopic) {
					bizCallBackAddTopic.addTopicCallBack(retCode, topicId,0, list);
				}
				EventBus.getDefault().unregister(this);
			}

		} else {
			BaseLog.i("vactor_log", "upload failure");
			//如果photolist中还有未上传成功的
			if (null != photoList && photoList.size() > 0) {
				String imageKeys = getImageKeys(photoList);
				if (publisOperate != null)
					publisOperate.updateStatusByTopicId(BAConstants.UploadStatus.FAILURE.getValue(), imageKeys, topicId + "");
			}
		}
	}

	public String getImageKeys(List<PhotoEntity> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (PhotoEntity photoEntity : list) {
			builder.append(photoEntity.getPath()).append(";");
			builder.append(";");
		}
		builder.deleteCharAt(builder.length() - 1).toString();
		String imageKeys = builder.deleteCharAt(builder.length() - 1).toString();
		return imageKeys;
	}

}
