package com.tshang.peipei.model.biz.main;

import java.math.BigInteger;

import android.app.Activity;
import android.content.Context;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackAddComment;
import com.tshang.peipei.model.bizcallback.BizCallBackAddReply;
import com.tshang.peipei.model.bizcallback.BizCallBackAppreciateTopic;
import com.tshang.peipei.model.bizcallback.BizCallBackDeleteTopic;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFollowInfoShowList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetTopicCounter;
import com.tshang.peipei.model.bizcallback.BizCallBackGetTopicDetail;
import com.tshang.peipei.model.bizcallback.BizCallBackSpaceGetUserTopicList;
import com.tshang.peipei.model.request.RequestAddComment;
import com.tshang.peipei.model.request.RequestAddComment.IAddComment;
import com.tshang.peipei.model.request.RequestAddReply;
import com.tshang.peipei.model.request.RequestAddReply.IAddReply;
import com.tshang.peipei.model.request.RequestAppreciate;
import com.tshang.peipei.model.request.RequestAppreciate.IAppreciate;
import com.tshang.peipei.model.request.RequestDeleteTopic;
import com.tshang.peipei.model.request.RequestDeleteTopic.IDeleteTopic;
import com.tshang.peipei.model.request.RequestGetFollowShowInfoList;
import com.tshang.peipei.model.request.RequestGetFollowShowInfoList.IGetFollowShow;
import com.tshang.peipei.model.request.RequestGetTopicCounter;
import com.tshang.peipei.model.request.RequestGetTopicCounter.IGetTopicCounter;
import com.tshang.peipei.model.request.RequestGetTopicDetail;
import com.tshang.peipei.model.request.RequestGetTopicDetail.IGetTopicDetail;
import com.tshang.peipei.model.request.RequestGetUserTopicList;
import com.tshang.peipei.model.request.RequestGetUserTopicList.IgetUserTopicList;
import com.tshang.peipei.protocol.asn.gogirl.CommentInfo;
import com.tshang.peipei.protocol.asn.gogirl.CommentInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowShowInfoList;
import com.tshang.peipei.protocol.asn.gogirl.TopicCounterInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfoList;
import com.tshang.peipei.storage.database.operate.AppreciateOperate;

/**
 * @Title: MainMySpaceBiz.java 
 *
 * @Description: 我的动态
 *
 * @author vactor
 *
 * @date 2014-4-12 下午5:39:44 
 *
 * @version V1.0   
 */
public class MainMySpaceBiz implements IgetUserTopicList, IGetTopicDetail, IGetFollowShow, IGetTopicCounter, IAppreciate, IDeleteTopic, IAddComment,
		IAddReply {

	private BizCallBackSpaceGetUserTopicList mGetUserTopicListCallBack;
	private BizCallBackGetTopicDetail mGetTopicDeatil;
	private BizCallBackGetTopicCounter mGetTopicCounter;
	private BizCallBackAppreciateTopic mAppreciateTopic;
	private BizCallBackDeleteTopic mDeleteTopic;
	private BizCallBackAddComment mAddComment;
	private BizCallBackAddReply mAddReply;
	private BizCallBackGetFollowInfoShowList mGetFollow;

	private int topicId;
	private int topicUid;
	private Context context;

	//拉取用户动态
	public void getUserTopicList(int uid, int start, int num, int code, BizCallBackSpaceGetUserTopicList callBack) {
		byte[] auth = "".getBytes();
		int selfUid = 0;
		int type = 0;
		GoGirlUserInfo userEntity = BAApplication.mLocalUserInfo;
		if (userEntity != null) {
			auth = userEntity.auth;
			selfUid = userEntity.uid.intValue();
		}
		RequestGetUserTopicList request = new RequestGetUserTopicList();
		this.mGetUserTopicListCallBack = callBack;
		request.getUserTopicList(auth, BAApplication.app_version_code, uid, selfUid, type, start, num, code, this);
	}

	@Override
	public void getUserTopicListCallBack(int retCode, int isEnd, int total, int code, TopicInfoList list) {
		if (null != mGetUserTopicListCallBack) {
			mGetUserTopicListCallBack.getUserTopicListCallBack(retCode, isEnd, total, code, list);
		}
	}

	//获取动态详情
	public void getTopicDetail(byte[] auth, int ver, int uid, int topicUid, int topicid, int isGetTopic, int start, int num,
			BizCallBackGetTopicDetail callBack) {
		RequestGetTopicDetail request = new RequestGetTopicDetail();
		this.mGetTopicDeatil = callBack;
		request.getTopicDetail(auth, ver, uid, topicUid, topicid, isGetTopic, start, num, this);

	}

	@Override
	public void getTopicDetailCallBack(int retCode, TopicInfo topicInfo, CommentInfoList commentList, int commentTotal) {
		if (null != mGetTopicDeatil) {
			mGetTopicDeatil.getTopicDetailCallBack(retCode, topicInfo, commentList, commentTotal);
		}
	}

	//计数
	public void getTopicCounter(int topicUid, int topicId, int isGetCommentNum, BizCallBackGetTopicCounter callBack) {
		RequestGetTopicCounter req = new RequestGetTopicCounter();
		this.mGetTopicCounter = callBack;
		req.getTopicCounter("".getBytes(), BAApplication.app_version_code, topicUid, topicId, isGetCommentNum, this);

	}

	@Override
	public void getTopicCounterCallBack(int retCode, TopicCounterInfo toicCountInfo) {
		if (null != mGetTopicCounter) {
			mGetTopicCounter.getTopicCounterCallBack(retCode, toicCountInfo);
		}
	}

	//点赞
	public void appreciate(Activity context, int topicId, int topicUid, int act, BizCallBackAppreciateTopic callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(context);
		if (userEntity == null) {
			return;
		}
//		MobclickAgent.onEvent(context, "dianzan");
		RequestAppreciate req = new RequestAppreciate();
		this.mAppreciateTopic = callBack;
		insertAppreciate(context, userEntity.uid.intValue(), topicId, topicUid);
		req.appreciate(userEntity.auth, BAApplication.app_version_code, topicId, topicUid, userEntity.uid.intValue(), act, this);

	}

	//插入点赞表
	public void insertAppreciate(Context context, int uid, int topicId, int topicUid) {
		if (BAApplication.mLocalUserInfo != null) {
			AppreciateOperate appreciateOperate = AppreciateOperate.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "");
			this.topicId = topicId;
			this.topicUid = topicUid;
			this.context = context;
			appreciateOperate.insertPublish(uid, topicId, topicUid);
			appreciateOperate = null;
		}
	}

	@Override
	public void appreciateCallBack(int retCode) {
		if (null != mAppreciateTopic) {
			mAppreciateTopic.appreciateCallBack(retCode);
			//如果失败,则删除数据库对应记录
			if (retCode != 0) {
				if (null != context) {
					if (BAApplication.mLocalUserInfo != null) {
						AppreciateOperate appreciateOperate = AppreciateOperate
								.getInstance(context, BAApplication.mLocalUserInfo.uid.intValue() + "");
						appreciateOperate.deleteTopicId(topicId, topicUid);
						appreciateOperate = null;
						context = null;
					}
				}
			}
		}
	}

	//删除贴子
	public void delteTopic(Activity activity, int topicId, int topicUid, IDeleteTopic callBack) {
		GoGirlUserInfo userEntity = BAApplication.mLocalUserInfo;
		if (userEntity == null) {
			return;
		}
		RequestDeleteTopic req = new RequestDeleteTopic();
		BaseUtils.showDialog(activity, R.string.str_deleting);
		req.delteTopic(userEntity.auth, BAApplication.app_version_code, topicId, topicUid, userEntity.uid.intValue(), callBack);
	}

	@Override
	public void deleteTopicCallBack(int retCode) {
		if (null != mDeleteTopic) {
			mDeleteTopic.deleteTopicCallBack(retCode);
		}
	}

	public void addComment(Activity activity, String city, String province, int topicid, int topicuid, String coment, BizCallBackAddComment callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestAddComment req = new RequestAddComment();
		this.mAddComment = callBack;
		req.addReply(userEntity.auth, BAApplication.app_version_code, city, province, topicid, topicuid, userEntity.uid.intValue(), coment, this);
	}

	@Override
	public void addCommentCallBack(int retCode) {
		if (null != mAddComment) {
			mAddComment.addCommentCallBack(retCode);
		}
	}

	public void addReply(Activity activity, String city, String province, int topicid, int topicuid, int commentId, String coment,
			BizCallBackAddReply callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestAddReply req = new RequestAddReply();
		this.mAddReply = callBack;
		req.addReply(userEntity.auth, BAApplication.app_version_code, city, province, topicid, topicuid, userEntity.uid.intValue(), commentId,
				coment, this);

	}

	@Override
	public void addReplyCallBack(int retCode) {
		if (null != mAddReply) {
			mAddReply.addReplyCallBack(retCode);
		}
	}

	//关注女神的动态
	public void getfollowList(int uid, int start, int num, BizCallBackGetFollowInfoShowList callBack) {
		RequestGetFollowShowInfoList req = new RequestGetFollowShowInfoList();
		this.mGetFollow = callBack;
		req.getFollowShowInfoList("".getBytes(), BAApplication.app_version_code, uid, start, num, this);
	}

	@Override
	public void getFollowShowListCallBack(int retCode, int isEnd, RetFollowShowInfoList list) {
		if (null != mGetFollow) {
			mGetFollow.getFollowListCallBack(retCode, isEnd, list);
		}
	}

	//新建一个commentinfo
	@SuppressWarnings("unchecked")
	public CommentInfo createCommentInfo(String city, String province, String coment, String nick, int sex, int uid) {
		CommentInfo commentInfo = new CommentInfo();
		commentInfo.city = city.getBytes();
		//文本数据
		GoGirlDataInfo info = new GoGirlDataInfo();
		info.data = coment.getBytes();
		info.dataid = "".getBytes();
		info.datainfo = BigInteger.valueOf(0);
		info.revint0 = BigInteger.valueOf(0);
		info.revint1 = BigInteger.valueOf(0);
		info.revstr0 = "".getBytes();
		info.revstr1 = "".getBytes();
		info.type = BigInteger.valueOf(BAConstants.MessageType.TEXT.getValue());
		commentInfo.commentstatus = BigInteger.valueOf(0);
		commentInfo.createtime = BigInteger.valueOf(System.currentTimeMillis() / 1000);
		commentInfo.detailaddr = "".getBytes();
		commentInfo.id = BigInteger.valueOf(0);
		commentInfo.imei = "".getBytes();
		commentInfo.nick = nick.getBytes();
		commentInfo.province = province.getBytes();
		commentInfo.commentcontentlist.add(info);
		commentInfo.revint0 = BigInteger.valueOf(0);
		commentInfo.revint1 = BigInteger.valueOf(0);
		commentInfo.revint2 = BigInteger.valueOf(0);
		commentInfo.revint3 = BigInteger.valueOf(0);
		commentInfo.revint4 = BigInteger.valueOf(0);
		commentInfo.revstr0 = "".getBytes();
		commentInfo.revstr1 = "".getBytes();
		commentInfo.revstr2 = "".getBytes();
		commentInfo.revstr3 = "".getBytes();
		commentInfo.revstr4 = "".getBytes();
		commentInfo.revstr5 = "".getBytes();
		commentInfo.revstr6 = "".getBytes();
		commentInfo.revstr7 = "".getBytes();
		commentInfo.revstr8 = "".getBytes();
		commentInfo.revstr9 = "".getBytes();
		commentInfo.sex = BigInteger.valueOf(sex);
		commentInfo.uid = BigInteger.valueOf(uid);
		commentInfo.updown = BigInteger.valueOf(0);
		return commentInfo;
	}

}
