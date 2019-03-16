package com.tshang.peipei.model.biz.user;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.bizcallback.BizCallBackAddTopic;
import com.tshang.peipei.model.request.RequestAddTopic;
import com.tshang.peipei.model.request.RequestAddTopic.IAddTopic;
import com.tshang.peipei.model.request.RequestUploadTopicContent;
import com.tshang.peipei.model.request.RequestUploadTopicContent.IUploadTopicContent;
import com.tshang.peipei.storage.database.operate.PublishOperate;

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
public class UserResendBiz implements IAddTopic, IUploadTopicContent {

	private BizCallBackAddTopic bizCallBackAddTopic;

	private byte[] auth;
	private int ver;
	private int uid;
	private int topicId;
	private long mTime;
	private List<String> mList;
	private Context mContext;
	private String bitmapPath;

	public UserResendBiz(Context context) {
		this.mContext = context;
	}

	// 图片数量>1时,重发有二个步骤,此处为第一步
	public void resendTopicIfHasManyPhotos(byte[] auth, int ver, int uid, String city, int topicId, long time, int la, int lo, String province,
			String content, List<String> list, BizCallBackAddTopic callBack) {
		this.auth = auth;
		this.ver = ver;
		this.uid = uid;
		this.topicId = topicId;
		this.mList = list;
		RequestAddTopic requestAddTopic = new RequestAddTopic();
		this.bizCallBackAddTopic = callBack;
		this.mTime = time;
		BaseLog.i("vactor_log", "upload many photos size:" + list.size());
		//更新数据库状态为正在上传状态
		if (BAApplication.mLocalUserInfo != null) {
			PublishOperate publisOperate = PublishOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
			String imageKeys = getImageKeys(list);
			publisOperate.updateStatusByTopicId(BAConstants.UploadStatus.UPLOADING.getValue(), imageKeys, topicId + "");
			publisOperate = null;
		}
		requestAddTopic.addTopicIfHasManyPhotos2(auth, ver, uid, city, 0, la, lo, province, content, list, this);
	}

	//图片数量==1时,写贴一步完成
	public void addTopicIfHasOnePhoto(byte[] auth, int ver, int uid, String city, long time, int la, int lo, String province, String content,
			String bitmapPath, BizCallBackAddTopic callBack) {
		RequestAddTopic requestAddTopic = new RequestAddTopic();
		this.bizCallBackAddTopic = callBack;
		this.bitmapPath = bitmapPath;
		byte[] msg = null;
		if (!TextUtils.isEmpty(bitmapPath)) {
			Bitmap bitmap = BaseFile.getImageFromFile(bitmapPath);
			msg = BaseBitmap.compBitmap2Byte(bitmap, 640, 200);
		}
		this.mTime = time;

		//更新数据库状态为正在上传状态
		if (BAApplication.mLocalUserInfo != null) {
			PublishOperate publisOperate = PublishOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
			publisOperate.updateStatusByTopicId(BAConstants.UploadStatus.UPLOADING.getValue(), bitmapPath, time + "");
		}
		requestAddTopic.addTopicIfHasOnePhoto(auth, ver, uid, city, 0, la, lo, province, content, msg, this);
	}

	@Override
	public void addTopicCallBack(int retCode, int topicId, int charmnum, GoGirlDataInfoList list) {
		//如果photoList.size()>0 表示写贴时有多张图片需要上传,启动上传第二步

		String imageKeys = getImageKeys(mList);
		PublishOperate publisOperate = null;
		if (BAApplication.mLocalUserInfo != null)
			publisOperate = PublishOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
		if (null != mList && mList.size() > 0) {
			this.topicId = topicId;
			//成功则启动上传
			if (retCode == 0) {
				if (publisOperate != null)
					publisOperate.updateStatusByTime(topicId + "", String.valueOf(mTime));
				publisOperate = null;

				//回包数据
				GoGirlDataInfo rspData = (GoGirlDataInfo) list.get(0);
				//取得图片数据
				String imagePath = mList.get(0);
				Bitmap bitmap = BaseFile.getImageFromFile(imagePath);
				byte[] bitmapData = BaseBitmap.compBitmap2Byte(bitmap, 640, 200);
				//上传
				requestUploadTopicContent(auth, ver, uid, topicId, bitmapData, rspData);
				//移除头部相片信息
				mList.remove(0);
			} else {
				//失败,更新数据库.
				if (null != bizCallBackAddTopic) {
					bizCallBackAddTopic.addTopicCallBack(retCode, topicId, charmnum, list);
				}

				if (publisOperate != null)
					publisOperate.updateStatusByTopicId(BAConstants.UploadStatus.FAILURE.getValue(), imageKeys, topicId + "");
				publisOperate = null;
			}

			//上传一步完成的情况
		} else {
			if (null != bizCallBackAddTopic) {
				bizCallBackAddTopic.addTopicCallBack(retCode, topicId, charmnum, list);
			}
			int status = retCode == 0 ? BAConstants.UploadStatus.SUCCESS.getValue() : BAConstants.UploadStatus.FAILURE.getValue();
			//写贴成功,直接删除数据库中对应的数据
			if (publisOperate != null)
				if (status == 0) {
					publisOperate.deleteTopicId(topicId + "");
				} else {
					publisOperate.updateStatusByTopicId(status, bitmapPath, mTime + "");
				}
		}

	}

	//当图片数量>1时,写贴最后一步
	public void requestUploadTopicContent(byte[] auth, int ver, int uid, int topicId, byte[] bitmapData, GoGirlDataInfo dataInfo) {
		RequestUploadTopicContent requestUploadContent = new RequestUploadTopicContent();
		requestUploadContent.requestUploadTopicContent(auth, ver, uid, topicId, bitmapData, dataInfo, this);
	}

	@Override
	public void uploadTopicContentCallBack(int retCode, GoGirlDataInfoList list) {
		PublishOperate publisOperate = null;
		if (BAApplication.mLocalUserInfo != null)
			publisOperate = PublishOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");

		if (retCode == 0) {
			//循环上传
			if (mList.size() > 0) {
				BaseLog.i("vactor_log", "upload call back list.size>" + mList.size());
				//更新数据库,imageKeys为剩下需要上传的图片路径
				String imageKeys = getImageKeys(mList);
				if (publisOperate != null)
					publisOperate.updateStatusByTopicId(BAConstants.UploadStatus.UPLOADING.getValue(), imageKeys, topicId + "");

				//回包数据
				GoGirlDataInfo rspData = (GoGirlDataInfo) list.get(0);
				//再取第一个开始上传
				String path = mList.get(0);
				Bitmap bitmap = BaseFile.getImageFromFile(path);
				byte[] bitmapData = BaseBitmap.compBitmap2Byte(bitmap, 640, 200);
				//上传
				requestUploadTopicContent(auth, ver, uid, topicId, bitmapData, rspData);
				//移除头部相片信息
				mList.remove(0);
			} else {
				BaseLog.i("vactor_log", "upload complete and this topicId is:" + topicId);
				//所有图片上传完成 ,删除数据库对应信息
				if (publisOperate != null)
					publisOperate.deleteTopicId(topicId + "");
				//所有图片上传完毕,写贴完成
				if (null != bizCallBackAddTopic) {
					bizCallBackAddTopic.addTopicCallBack(retCode, topicId, 0, list);
				}
			}

		} else {
			BaseLog.i("vactor_log", "upload failure");
			//如果photolist中还有未上传成功的
			if (null != mList && mList.size() > 0) {
				BaseLog.i("vactor_log", "upload failure then update status in table");
				String imageKeys = getImageKeys(mList);
				if (publisOperate != null)
					publisOperate.updateStatusByTopicId(BAConstants.UploadStatus.FAILURE.getValue(), imageKeys, topicId + "");
			}
		}
	}

	public String getImageKeys(List<String> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (String string : list) {
			builder.append(string).append(";");
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}

}
