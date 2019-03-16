package com.tshang.peipei.model.biz.user;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.base.BaseBitmap;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestDeleteDynamic;
import com.tshang.peipei.model.request.RequestDeleteDynamic.DeleteDynamicCallBack;
import com.tshang.peipei.model.request.RequestDynamicAboutMe;
import com.tshang.peipei.model.request.RequestDynamicAboutMe.GetDynamicAboutMeCallBack;
import com.tshang.peipei.model.request.RequestDynamicAll;
import com.tshang.peipei.model.request.RequestDynamicAll.GetDynamicAllCallBack;
import com.tshang.peipei.model.request.RequestDynamicOfficial;
import com.tshang.peipei.model.request.RequestDynamicOfficial.GetDynamicOfficialCallBack;
import com.tshang.peipei.model.request.RequestDynamicPraiseNumber;
import com.tshang.peipei.model.request.RequestDynamicPraiseNumber.AppPariseCallBack;
import com.tshang.peipei.model.request.RequestDynamicReply;
import com.tshang.peipei.model.request.RequestDynamicReply.addDynamicReplyCallBack;
import com.tshang.peipei.model.request.RequestDynamicReply2;
import com.tshang.peipei.model.request.RequestDynamicReply2.addDynamicReplyCallBack2;
import com.tshang.peipei.model.request.RequestDynamicReplyDetails;
import com.tshang.peipei.model.request.RequestDynamicReplyDetails.GetDynamicReplyDetails;
import com.tshang.peipei.model.request.RequestWriteDynamic;
import com.tshang.peipei.model.request.RequestWriteDynamic.WriteDynamicCallBack;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: UserDynamicPublis.java 
 *
 * @Description: 动态处理控制器 
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午6:27:54 
 *
 * @version V1.0   
 */
public class DynamicRequseControl implements WriteDynamicCallBack, DeleteDynamicCallBack {

	private BAHandler mHandler;
	private Dialog dialog;

	public DynamicRequseControl() {

	};

	/**
	 * 
	 * @author Aaron
	 *
	 * @param province 省
	 * @param city 市
	 * @param isanonymous 是否匿名  0-不匿名，1-匿名
	 * @param fonttype 字体类型  0-白色，1-黑色
	 * @param content  内容
	 * @param bitmapPath 图片路径
	 */
	public void writeDynamicRequest(String province, String city, int isanonymous, int fonttype, int srcpic, String content, String bitmapPath,
			int relativetopic, BAHandler handler) {
		this.mHandler = handler;
		GoGirlUserInfo userEntity = BAApplication.mLocalUserInfo;
		if (userEntity == null) {
			return;
		}
		byte[] msg = null;
		if (!TextUtils.isEmpty(bitmapPath)) {
			Bitmap bitmap = BaseFile.getImageFromFile(bitmapPath);
			msg = BaseBitmap.compBitmap2Byte(bitmap, 640, 200);
		}
		RequestWriteDynamic request = new RequestWriteDynamic();
		request.writeDynamic(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), province, city, isanonymous, fonttype,
				srcpic, content, msg, relativetopic, this);
	}

	/**
	 * 
	 * @author Aaron
	 *
	 * @param start 开始位置
	 * @param num 条数
	 * @param type 类型  0-所有动态，1-个人动态
	 * @param callBack 接口回调
	 */
	public void requstDynamicAll(int start, int num, int type, GetDynamicAllCallBack callBack) {
		RequestDynamicAll req = new RequestDynamicAll();
		if (BAApplication.mLocalUserInfo != null) {
			req.requestDynamicAll(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
					type, start, num, callBack);
		}
	}

	/**
	 * 获取动态列表
	 * @author Aaron
	 *
	 * @param start
	 * @param num
	 * @param type
	 * @param uid
	 * @param callBack
	 */
	public void getDynamicList(int start, int num, int type, int uid, GetDynamicAllCallBack callBack) {
		RequestDynamicAll req = new RequestDynamicAll();
		if (BAApplication.mLocalUserInfo != null) {
			req.requestDynamicAll(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, uid, type, start, num, callBack);
		}
	}

	/**
	 * 请求点赞
	 * @author Aaron
	 *
	 * @param topicid
	 * @param topicuid
	 * @param number
	 * @param callBack
	 */
	public void appPriaseNum(int topicid, int topicuid, int type, int upvotenum, int systemid, AppPariseCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestDynamicPraiseNumber req = new RequestDynamicPraiseNumber();
		req.requestPriaseNumber(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				topicid, topicuid, type, upvotenum, systemid, callBack);
	}

	/**
	 * 删除动态
	 * @author Aaron
	 *
	 * @param topicuid
	 * @param topicid
	 * @param handler
	 */
	public void deleteDynamic(int topicuid, int topicid, int systemtopicid, int type, Dialog dialog, BAHandler handler) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		this.mHandler = handler;
		this.dialog = dialog;
		RequestDeleteDynamic req = new RequestDeleteDynamic();
		req.deleteDynamic(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), topicuid,
				topicid, systemtopicid, type, this);
	}

	/**
	 * 获取官方动态
	 * @author Aaron
	 *
	 * @param topicid
	 * @param start
	 * @param num
	 * @param callBack
	 */
	public void getDynamicOfficial(int topicid, int start, int num, int type, GetDynamicOfficialCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestDynamicOfficial req = new RequestDynamicOfficial();
		req.requestDynamicAll(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				topicid, start, num, type, callBack);
	}

	/**
	 * 获取动态回复详情
	 * @author Aaron
	 *
	 * @param topicuid
	 * @param topicid
	 * @param start
	 * @param num
	 */
	public void getDynamicDetails(int topicuid, int topicid, int relativeid, int start, int num, int type, GetDynamicReplyDetails callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestDynamicReplyDetails req = new RequestDynamicReplyDetails();
		req.requestDynamicReplyDetails(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), topicid, topicuid, relativeid, start, num, type, callBack);
	}

	/**
	 * 添加动态回复<一级>
	 * @author Aaron
	 *
	 * @param topicuid
	 * @param topicid
	 * @param type 0: 回复主贴；1：二级回复
	 * @param province
	 * @param city
	 * @param replyContent
	 * @param callBack
	 */
	public void addDynamicReply(int topicuid, int topicid, int type, int systemtopicid, String province, String city, String replyContent,
			addDynamicReplyCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestDynamicReply req = new RequestDynamicReply();
		req.addDynamicReply(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(), topicuid,
				topicid, type, systemtopicid, province, city, replyContent, callBack);
	}

	/**
	 * 动态二级回复
	 * @author Aaron
	 *
	 * @param topicuid
	 * @param topicid
	 * @param commentid
	 * @param province
	 * @param city
	 * @param replyContent
	 */
	public void addDynamicReply2(int topicuid, int topicid, int commentid, int type, int systemtopicid, String province, String city,
			String replyContent, addDynamicReplyCallBack2 callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestDynamicReply2 req = new RequestDynamicReply2();
		req.addDynamicReply2(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				topicuid, topicid, commentid, type, systemtopicid, province, city, replyContent, callBack);
	}

	/**
	 * 请求关于我回复列表
	 * @author Aaron
	 *
	 * @param start
	 * @param num
	 * @param type
	 * @param callBack
	 */
	public void getAboutMeDynamicReply(int start, int num, int type, GetDynamicAboutMeCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		RequestDynamicAboutMe req = new RequestDynamicAboutMe();
		req.requestDynamicAboutMe(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				type, start, num, callBack);
	}

	@Override
	public void onSuccess(int code) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.PUBLISH_DYNAMIC_SUCCESS;
		msg.arg1 = code;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onError(int code) {
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.PUBLISH_DYNAMIC_ERROR;
		msg.arg1 = code;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onSuccessDelete(int code) {
		DialogFactory.dimissDialog(dialog);
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.DYNAMIC_DELETE_SUCCESS;
		msg.arg1 = code;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onErrorDelete(int code) {
		DialogFactory.dimissDialog(dialog);
		Message msg = mHandler.obtainMessage();
		msg.what = HandlerValue.DYNAMIC_DELETE_ERROR;
		msg.arg1 = code;
		mHandler.sendMessage(msg);
	}
}
