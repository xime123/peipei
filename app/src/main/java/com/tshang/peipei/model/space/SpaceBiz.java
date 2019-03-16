package com.tshang.peipei.model.space;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.main.MainMySpaceBiz;
import com.tshang.peipei.model.biz.space.GetSkillListBiz;
import com.tshang.peipei.model.biz.space.SpaceRelationshipBiz;
import com.tshang.peipei.model.biz.store.StoreUserBiz;
import com.tshang.peipei.model.biz.user.UserAlbumBiz;
import com.tshang.peipei.model.biz.user.UserInfoBiz;
import com.tshang.peipei.model.biz.user.UserSettingBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackAddFollow;
import com.tshang.peipei.model.bizcallback.BizCallBackGetAlbumPhotoList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFollowInfoShowList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetTopicCounter;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserInfo;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserProperty;
import com.tshang.peipei.model.bizcallback.BizCallBackSpaceGetUserTopicList;
import com.tshang.peipei.model.bizcallback.BizCallBackUploadHeadPic;
import com.tshang.peipei.model.hall.MainHallBiz;
import com.tshang.peipei.model.request.RequestGetRelasionship.IGetRelationship;
import com.tshang.peipei.model.request.RequestGetRelevantCounter;
import com.tshang.peipei.model.request.RequestGetRelevantCounter.IGetRelevantCounter;
import com.tshang.peipei.model.request.RequestGetSkillList.IGetSkillList;
import com.tshang.peipei.model.request.RequestGetSpecialEffect;
import com.tshang.peipei.model.request.RequestGetSpecialEffect.IGetSpecialEffect;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RelationshipInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowShowInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowShowInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.TopicCounterInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.storage.database.entity.PublishDatabaseEntity;
import com.tshang.peipei.vender.common.util.ImageUtils;
import com.tshang.peipei.vender.common.util.ListUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

/**
 * @Title: SpaceBiz.java 
 *
 * 个人空间的请求接口
 *
 * @author Jeff
 *
 * @date 2014年8月8日 上午10:58:49 
 *
 * @version V1.0   
 */
public class SpaceBiz implements BizCallBackGetUserInfo, BizCallBackGetAlbumPhotoList, BizCallBackGetUserProperty, BizCallBackGetFollowInfoShowList,
		BizCallBackGetTopicCounter, IGetRelationship, IGetSkillList, BizCallBackSpaceGetUserTopicList, BizCallBackAddFollow,
		BizCallBackUploadHeadPic, IGetRelevantCounter, IGetSpecialEffect {
	protected Activity activity;
	protected BAHandler handler;
	public static final int LOAD_COUNT = 10;
	public static final int REFRESH_CODE = 1;
	public static final int LOAD_MORE_CODE = 2;

	private int startPos = -1;

	public BAHandler getHandler() {
		return handler;
	}

	public void setHandler(BAHandler handler) {
		this.handler = handler;
	}

	public SpaceBiz(Activity activity, BAHandler handler) {
		this.activity = activity;
		this.handler = handler;
	}

	public void uploadHeadPic(ImageLoader imageLoader, final RelativeLayout imageview, String path) {//设置封面照 
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(false).considerExifParams(true)
				.imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.loadImage("file://" + path, options, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (loadedImage != null) {
					new UserSettingBiz().uploadHeadPic(activity, ImageUtils.bitmapToByte(loadedImage), BAConstants.UploadHeadType.HEAD_BACKGROUND,
							SpaceBiz.this);
					imageview.setBackgroundDrawable(new BitmapDrawable(loadedImage));
				}
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {}
		});
	}

	public void addFollow(int uid) {//加关注
		new SpaceRelationshipBiz(activity).addFollow(uid, this);
	}

	public void getMySkillList(int uid) {//获取用户的技能
		new GetSkillListBiz(activity).getSkillList(uid, this);
	}

	public void getRelationship(int uid) {//获取是否有关注过该用户
		new UserInfoBiz(activity).getRelationship(uid, 2, this);//传2加载的时候没有进度条
	}

	public void getAlbumPhotoListData(int uid, int albumId, int start, int num) {//获取公开相册的个数
		new UserAlbumBiz().getAlbumPhotoList(activity, uid, albumId, start, num, this);
	}

	public void getUserInfo(int uid) {//用户的基本信息
		MainHallBiz.getInstance().getUserInfo(activity, uid, this);
	}

	public void getUserProperty(int uid) {//用户的财富值，比如金币等待呢个
		StoreUserBiz.getInstance().getUserProperty(activity, uid, this);
	}

	public void getUserPropertyFanse(int uid) {//用户的访问量

		RequestGetRelevantCounter request = new RequestGetRelevantCounter();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		if (info != null) {
			auth = info.auth;
		}
		request.getRelevantCounter(auth, BAApplication.app_version_code, uid, this);
	}

	public void getFollowList(int uid, int start, int num) {//关注人的动态列表
		new MainMySpaceBiz().getfollowList(uid, start, num, this);
	}

	public void getUserTopicList(int uid, int code) {//获取用户的动态
		if (code == REFRESH_CODE) {
			startPos = -1;
		}
		new MainMySpaceBiz().getUserTopicList(uid, startPos, LOAD_COUNT, code, this);
	}

	public void getTopicCounter(int topicUid, int topicId) {//帖子计数
		new MainMySpaceBiz().getTopicCounter(topicUid, topicId, 1, this);
	};

	@Override
	public void getAlbumPhotoList(int retCode, int total, PhotoInfoList list) {//公开相册列表
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_ALBUM_PHOTO_LIST_VALUE, retCode, total, list);
	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {//用户基本信息回调
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_USER_INFO_VALUE, retCode, retCode, userinfo);
	}

	@Override
	public void getUserProperty(int retCode, UserPropertyInfo userPropertyInfo) {//用户财富值回调
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_USER_PROPERTY_VALUE, retCode, retCode, userPropertyInfo);

	}

	@Override
	public void getFollowListCallBack(int retCode, int isEnd, RetFollowShowInfoList list) {//用户关注列表回调
		ArrayList<TopicInfo> topicInfoLists = new ArrayList<TopicInfo>();
		if (retCode == 0) {
			if (list != null && !list.isEmpty()) {
				for (int i = list.size() - 1; i >= 0; i--) {
					RetFollowShowInfo followShowInfo = (RetFollowShowInfo) list.get(i);
					TopicInfo topicInfo = followShowInfo.topicinfo;
					getTopicCounter(topicInfo.uid.intValue(), topicInfo.topicid.intValue());//获取到了关注的数据，继续请求获取帖子的计数
					topicInfoLists.add(topicInfo);
				}
			}
		}
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_MY_FOLLOW_LIST_VALUE, retCode, isEnd, topicInfoLists);
	}

	@Override
	public void getTopicCounterCallBack(int retCode, TopicCounterInfo info) {//帖子计数回调
		if (info != null) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_GET_TOPIC_COUNT_VALUE, info);
		}

	}

	@Override
	public void getRelationshipCallBack(int retCode, int isAttention, RelationshipInfo relation) {//获取关注回调
		if (relation != null) {
			int type = relation.isfocus.intValue();
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_GET_RELATIONSHIP_VALUE, retCode, type);
		}
	}

	@Override
	public void getSkillListCallBack(int retCode, int tatal, int isSend, RetGGSkillInfoList lists) {//获取到的技能列表回调
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_GET_MYSKILL_LIST_VALUE, retCode, tatal, lists);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getUserTopicListCallBack(int retCode, int isEnd, int total, int code, TopicInfoList list) {//动态列表回调
		if (retCode == 0) {
			startPos -= LOAD_COUNT;
		}
		if (!ListUtils.isEmpty(list)) {
			//反转
			Collections.reverse(list);
			for (Object object : list) {//计数查询
				TopicInfo topicInfo = (TopicInfo) object;
				getTopicCounter(topicInfo.uid.intValue(), topicInfo.topicid.intValue());
			}
		}
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_TOPIC_LIST_VALUE, code, isEnd, list);

	}

	@Override
	public void addFollowCallBack(int retCode) {//加关注回来
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_ADD_FOLLOW_VALUE, retCode);
		}
	}

	@Override
	public void uploadHeadPicCallBack(int retCode) {//上传照片回来
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_UPLOAD_BG_VALUE, retCode, retCode);
	}

	@SuppressWarnings("unchecked")
	public void queryLocalTopicData(List<PublishDatabaseEntity> list, List<TopicInfo> lists) {
		if (list != null && !list.isEmpty()) {
			for (PublishDatabaseEntity publishDatabaseEntity : list) {
				TopicInfo topicInfo = new TopicInfo();
				topicInfo.id = BigInteger.valueOf(-100);//代表是本地数据
				topicInfo.nick = publishDatabaseEntity.getNickName().getBytes();
				topicInfo.uid = BigInteger.valueOf(publishDatabaseEntity.getUserId());
				topicInfo.topicid = BigInteger.valueOf(publishDatabaseEntity.getUserId());
				topicInfo.createtime = BigInteger.valueOf(Long.valueOf(publishDatabaseEntity.getCreatetime()));
				GoGirlDataInfoList dataInfoList = new GoGirlDataInfoList();
				GoGirlDataInfo dataInfo = new GoGirlDataInfo();
				if (!TextUtils.isEmpty(publishDatabaseEntity.getContent())) {
					dataInfo.data = publishDatabaseEntity.getContent().getBytes();
					dataInfo.type = BigInteger.valueOf(MessageType.TEXT.getValue());
					dataInfoList.add(dataInfo);
				}
				if (!TextUtils.isEmpty(publishDatabaseEntity.getImageKeys())) {
					String images[] = publishDatabaseEntity.getImageKeys().split(";");

					for (int i = 0, len = images.length; i < len; i++) {
						dataInfo = new GoGirlDataInfo();
						dataInfo.type = BigInteger.valueOf(MessageType.IMAGE_KEY.getValue());
						dataInfo.data = images[i].getBytes();
						dataInfoList.add(dataInfo);
					}
				}
				topicInfo.topiccontentlist = dataInfoList;
				boolean flag = true;
				if (!ListUtils.isEmpty(lists)) {
					for (TopicInfo topicInfo1 : lists) {
						if (topicInfo1.createtime.intValue() == topicInfo.createtime.intValue()) {
							flag = false;
						}
					}
				}
				if (flag) {//重复的就不加
					HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_QUERY_LOCAL_TOPIC_VALUE, topicInfo);
				}
			}
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_QUERY_LOCAL_TOPIC_VALUE, null);
		}
	}

	@Override
	public void getRelevantCounter(int retCode, int fanscounter, int visitecounter) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_USER_FANSE_VALUE, fanscounter, visitecounter);
	}

	public void getSpecialEffect(int uid) {
		RequestGetSpecialEffect req = new RequestGetSpecialEffect();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		if (info != null) {
			auth = info.auth;
		}
		req.getSpecialEffect(activity, auth, BAApplication.app_version_code, uid, this);
	}

	public static final int ROSE_SPECIAL_VALUE = 1;
	public static final int STAR_FALL_SPECIAL_VALUE = 2;
	public static final int CLOUD_SPECIAL_VALUE = 3;
	public static final int DEER_RIDING_VALUE = 4;
	public static final int BIRD_RIDING_VALUE = 5;
	public static final int PEACOCK_RIDING_VALUE = 6;
	public static final int CHANGE_VALUE = 7;//三十六变
	public static final int FLOWER_VALUE = 8;//百花惊艳
	public static final int CAR_MOTORING=14;//
	public static final int SNOW_VALUE=15;
	public static final int BEAST_VALUE = 16;//年兽
	public static final int BLEESSING_VALUE = 17;//福

	@Override
	public void getSpecialEffect(int retCode, int riding, int sceneid) {
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SPACE_SPECIAL_VALUE, riding, sceneid);
		}
	}

}
