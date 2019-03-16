package com.tshang.peipei.model.harem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GroupMemberInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestCreateGroup;
import com.tshang.peipei.model.request.RequestCreateGroup.ICreateGroup;
import com.tshang.peipei.model.request.RequestGetGroupInfo;
import com.tshang.peipei.model.request.RequestGetGroupInfo.IGetGroupInfo;
import com.tshang.peipei.model.request.RequestGetRelevantGroupList;
import com.tshang.peipei.model.request.RequestGetRelevantGroupList.IGetRelevantGroupList;
import com.tshang.peipei.model.request.RequestGroupMemberInfoList;
import com.tshang.peipei.model.request.RequestGroupMemberInfoList.IGetGroupMemberInfoList;
import com.tshang.peipei.vender.common.util.ImageUtils;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: CreateHarem.java 
 *
 * @Description: (用一句话描述该文件做什么) 
 *
 * @author Jeff 
 *
 * @date 2014年9月17日 下午2:15:48 
 *
 * @version V1.0   
 */
public class CreateHarem implements ICreateGroup, IGetRelevantGroupList, IGetGroupMemberInfoList, IGetGroupInfo {
	private BAHandler mHandler;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	private CreateHarem() {

	}

	private static CreateHarem instance = null;

	public static CreateHarem getInstance() {
		if (instance == null) {
			synchronized (CreateHarem.class) {
				if (instance == null) {
					instance = new CreateHarem();
				}
			}
		}
		return instance;
	}

	/**
	 * 请求创建后宫方法
	 * @author Jeff
	 *
	 * @param activity 上下文
	 * @param badgepic 后宫图标
	 * @param haremName 后宫名字
	 * @param haremNotice 后宫告示
	 * @param mHandler 
	 */
	public void reqCreateHarem(Activity activity, String path, String haremName, String haremNotice, BAHandler mHandler) {
		RequestCreateGroup req = new RequestCreateGroup();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		if (userInfo == null) {
			return;
		}
		if (TextUtils.isEmpty(haremName)) {
			BaseUtils.showTost(activity, R.string.str_harem_name_not_empty);
			return;
		}
		if (haremName.length() < 3) {
			BaseUtils.showTost(activity, "后宫名字不能够少于3个字");
			return;
		}
		if (TextUtils.isEmpty(haremNotice)) {
			haremNotice = "";
		}
		if (TextUtils.isEmpty(path)) {
			BaseUtils.showTost(activity, "请选择后宫图标");
			return;
		}
		Bitmap bitmap = imageLoader.loadImageSync("file://" + path);
		if (bitmap == null) {
			return;
		}
		this.mHandler = mHandler;
		int bitmapWidth = bitmap.getWidth();
		float scale = ((float) 120.0 / bitmapWidth);
		bitmap = ImageUtils.scaleImage(bitmap, scale, scale);
		byte[] badgepic = ImageUtils.bitmapToByte(bitmap);
		if (badgepic == null) {
			return;
		}
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		BaseUtils.showDialog(activity, "正在创建后宫");
		req.reqCreateGroup(userInfo.auth, BAApplication.app_version_code, badgepic, haremName, haremNotice, userInfo.uid.intValue(), this);
	}

	public void getRelevantGroupList(Activity activity, int friendUid, BAHandler mHandler) {//请求获取后宫列表
		RequestGetRelevantGroupList req = new RequestGetRelevantGroupList();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		int uid = 0;
		if (userInfo != null) {
			uid = userInfo.uid.intValue();
		}
		//		BaseUtils.showDialog(activity, R.string.loading);
		req.getRelevantGroupList("".getBytes(), BAApplication.app_version_code, friendUid, uid, this, mHandler);

	}

	public void getGroupMemberInfoList(Activity activity, int groupid, BAHandler mHandler) {//请求获取后宫成员
		RequestGroupMemberInfoList req = new RequestGroupMemberInfoList();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		int uid = 0;
		if (userInfo != null) {
			uid = userInfo.uid.intValue();
		}
		this.mHandler = mHandler;
		BaseUtils.showDialog(activity, R.string.loading);
		req.getGroupMemberInfoList("".getBytes(), BAApplication.app_version_code, groupid, uid, 0, 100, this);

	}

	/**
	 * 获取群信息
	 * @author Jeff
	 *
	 * @param activity
	 * @param groupid
	 * @param mHandler
	 */
	public void getGroupInfo(Activity activity, int groupid, BAHandler mHandler) {//请求获取后宫成员
		RequestGetGroupInfo req = new RequestGetGroupInfo();
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		if (userInfo == null) {
			return;
		}
		this.mHandler = mHandler;
		BaseUtils.showDialog(activity, R.string.loading);
		req.reqgetGroupInfo(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), groupid, this);
	}

	@Override
	public void createGroup(int retCode, int groupid) {
		if (mHandler == null) {
			return;
		}
		if (retCode == 0) {//创建成功
			mHandler.sendEmptyMessage(HandlerValue.HAREM_CREATE_SUCCESS_VALUE);
		} else if (retCode == -28048) {//等级不够
			mHandler.sendEmptyMessage(HandlerValue.HAREM_CREATE_LEVER_LOWER_FAILED_VALUE);
		} else if (retCode == -28049) {//群个数问题
			mHandler.sendEmptyMessage(HandlerValue.HAREM_CREATE_LEVER_GROUP_LIMIT_FAILED_VALUE);
		} else {
			mHandler.sendEmptyMessage(HandlerValue.HAREM_CREATE_FAILED_VALUE);
		}
	}

	@Override
	//夹在后宫群列表返回
	public void getRelevantGroupList(int retCode, int end, GroupInfoList info, BAHandler hander) {
		if (hander == null) {
			return;
		}
		if (retCode == 0) {
			hander.sendMessage(hander.obtainMessage(HandlerValue.HAREM_GET_GROUP_LIST_SUCCESS_VALUE, info));
		} else {//失败
			hander.sendEmptyMessage(HandlerValue.HAREM_GET_GROUP_LIST_FAILED_VALUE);
		}
	}

	@Override
	//加载后宫成员列表返回
	public void getgroupMemberInfoList(int retCode, int maxnum, GroupMemberInfoList infoList) {
		if (mHandler == null) {
			return;
		}
		if (retCode == 0) {
			mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HAREM_GET_GROUP_MEMBER_LIST_SUCCESS_VALUE, maxnum, 0, infoList));
		} else {//失败
			mHandler.sendEmptyMessage(HandlerValue.HAREM_GET_GROUP_MEMBER_LIST_FAILED_VALUE);
		}
	}

	@Override
	public void getGroupInfo(int retCode, int uid, GroupInfo info) {
		if (mHandler == null) {
			return;
		}
		if (retCode == 0) {
			if (info != null) {
				HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HAREM_GROUP_INFO_SUCCESS_VALUE, info);
				if (uid == info.owner.intValue()) {
					mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HAREM_GROUP_IS_OWNER_VALUE, 1, 1, info));
					return;
				}
			}
		} else {
			HandlerUtils.sendHandlerMessage(mHandler, HandlerValue.HAREM_GROUP_INFO_FAILED_VALUE, info);
		}
		mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HAREM_GROUP_IS_OWNER_VALUE, 0, 1, info));
	}

}
