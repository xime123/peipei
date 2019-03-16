package com.tshang.peipei.model.showrooms;

import java.util.List;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.entity.ShowChatEntity;
import com.tshang.peipei.model.request.RequestAddHot;
import com.tshang.peipei.model.request.RequestAddHot.IAddHot;
import com.tshang.peipei.model.request.RequestCloseShow;
import com.tshang.peipei.model.request.RequestCloseShow.ICloseShow;
import com.tshang.peipei.model.request.RequestDelateShow;
import com.tshang.peipei.model.request.RequestDelateShow.IDelateShow;
import com.tshang.peipei.model.request.RequestEnterBroadcast;
import com.tshang.peipei.model.request.RequestEnterBroadcast.IEnterBroadcastCallBack;
import com.tshang.peipei.model.request.RequestGetDevoteRank;
import com.tshang.peipei.model.request.RequestGetDevoteRank.IGetDevoteRank;
import com.tshang.peipei.model.request.RequestGetGiftShowList;
import com.tshang.peipei.model.request.RequestGetGiftShowList.IGetGiftShowList;
import com.tshang.peipei.model.request.RequestGetMemberList;
import com.tshang.peipei.model.request.RequestGetMemberList.IGetMemberList;
import com.tshang.peipei.model.request.RequestGetShowGiftList;
import com.tshang.peipei.model.request.RequestGetShowGiftList.IGetShowGiftList;
import com.tshang.peipei.model.request.RequestGetShowHistoryData;
import com.tshang.peipei.model.request.RequestGetShowHistoryData.IGetShowHistoryData;
import com.tshang.peipei.model.request.RequestGetShowRooms;
import com.tshang.peipei.model.request.RequestGetShowRooms.IGetShowRoomCallBack;
import com.tshang.peipei.model.request.RequestGetSingleShowRoom;
import com.tshang.peipei.model.request.RequestGetSingleShowRoom.IGetSingleShowRoom;
import com.tshang.peipei.model.request.RequestInOutRoom;
import com.tshang.peipei.model.request.RequestInOutRoom.IInOutRoom;
import com.tshang.peipei.model.request.RequestOpenBox;
import com.tshang.peipei.model.request.RequestOpenBox.IOpenBox;
import com.tshang.peipei.model.request.RequestOpenShow;
import com.tshang.peipei.model.request.RequestOpenShow.IOpenShowCallBack;
import com.tshang.peipei.model.request.RequestSetMemberRole;
import com.tshang.peipei.model.request.RequestSetMemberRole.ISetMemberRole;
import com.tshang.peipei.model.request.RequestShowChat;
import com.tshang.peipei.model.request.RequestShowChat.IShowChat;
import com.tshang.peipei.model.viewdatacache.GiftListCacheViewData;
import com.tshang.peipei.protocol.Gogirl.GiftDealInfoP;
import com.tshang.peipei.protocol.Gogirl.GoGirlChatDataP;
import com.tshang.peipei.protocol.Gogirl.RspGetRoomMemberList;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;
import com.tshang.peipei.protocol.Gogirl.ShowRoomMemberInfo;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: RoomsGet.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) //获取秀场列表
 *
 * @author jeff  
 *
 * @date 2015年1月19日 下午2:14:10 
 *
 * @version V2.0   
 */
public class RoomsGetBiz implements IGetShowRoomCallBack, IOpenShowCallBack, IShowChat, IGetMemberList, IGetDevoteRank, IGetShowHistoryData,
		IInOutRoom, IEnterBroadcastCallBack, IGetGiftShowList, ISetMemberRole, IAddHot, ICloseShow, IDelateShow, IGetSingleShowRoom,
		IGetShowGiftList, IOpenBox {
	private Activity activity;
	private BAHandler handler;

	public RoomsGetBiz(Activity activity, BAHandler handler) {
		this.activity = activity;
		this.handler = handler;
	}

	public void getShowRoomLists() {
		RequestGetShowRooms request = new RequestGetShowRooms();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		request.getShowRooms(auth, BAApplication.app_version_code, 0, 0, uid, this);
	}

	public void getEnterBroadcast() {
		if (System.currentTimeMillis() - enter_broadCast_time < 1000 * 60) {
			return;
		}

		RequestEnterBroadcast request = new RequestEnterBroadcast();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		request.getEnterBroadcast(auth, BAApplication.app_version_code, uid, this);

	}

	public void openShow() {
		RequestOpenShow req = new RequestOpenShow();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		req.requestOpenShow(auth, BAApplication.app_version_code, uid, this);
	}

	@Override
	public void getShowRooms(int retCode, List<ShowRoomInfo> lists) {
		if (lists != null && !lists.isEmpty()) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_LISTS_VALUE, lists);
		}
	}

	public void sendShowChat(byte[] chatdata, int showRoomId, String dataid, int length, int type, int screenType, ShowChatEntity chatEntity) {
		RequestShowChat req2 = new RequestShowChat();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		String nick = "";
		int sex = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
			nick = new String(info.nick);
			sex = info.sex.intValue();
		}
		int roomuid = 0;
		if (BAApplication.showRoomInfo != null) {
			roomuid = BAApplication.showRoomInfo.getOwneruserinfo().getUid();
		}
		req2.showChat(auth, BAApplication.app_version_code, roomuid, uid, showRoomId, nick, sex, 2, chatdata, dataid, length, type, screenType,
				chatEntity, this);
	}

	@Override
	public void openShowCallback(int retCode, ShowRoomInfo info) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_OPEN_SHOW, retCode, 0, info);
	}

	private long enter_broadCast_time = 0;

	@Override
	public void getEnterBroadcast(int retCode, String message) {

		if (retCode == 0 || retCode == -28219) {//返回成功了
			enter_broadCast_time = System.currentTimeMillis();
		}

	}

	@Override
	public void showChatcallback(int retcode, int screenType, GoGirlChatDataP chatDataP) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_CHAT_BACK, retcode, screenType, chatDataP);
	}

	public void getRoomMemberList(int roomid, int uid, int start, int num) {
		RequestGetMemberList req = new RequestGetMemberList();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int Selfuid = 0;
		if (info != null) {
			auth = info.auth;
			Selfuid = info.uid.intValue();
		}
		req.getMemberList(auth, BAApplication.app_version_code, roomid, Selfuid, uid, start, num, this);
	}

	@Override
	public void getMemberResult(int retCode, RspGetRoomMemberList list) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_GET_MEMBER_LIST, retCode, 0, list);
	}

	public void getDevoteRank(int roomid, int uid, int start, int num, int type) {
		RequestGetDevoteRank req = new RequestGetDevoteRank();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int Selfuid = 0;
		if (info != null) {
			auth = info.auth;
			Selfuid = info.uid.intValue();
		}
		req.getDevoteRank(auth, BAApplication.app_version_code, roomid, Selfuid, uid, start, num, type, this);
	}

	@Override
	public void getDevoteRankResult(int retCode, int end, int type, List<ShowRoomMemberInfo> getMemberlistList) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_GET_DEVOTERANK, retCode, type, getMemberlistList);
	}

	public void getShowHistorData(int roomid, int hostuid, int start, int num, int type) {
		RequestGetShowHistoryData req = new RequestGetShowHistoryData();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		req.getShowHistoryData(auth, BAApplication.app_version_code, roomid, hostuid, uid, start, num, type, this);
	}

	@Override
	public void resultShowHistoryData(int retCode, int end, int type, List<GoGirlChatDataP> list) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_GET_HISTORDATA, retCode, type, list);
	}

	public void InOutRooms(int act, int showRoomId, int roomUid) {
		RequestInOutRoom req = new RequestInOutRoom();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		req.InOutRoom(auth, BAApplication.app_version_code, act, showRoomId, uid, roomUid, this);
	}

	@Override
	public void resultInOut(int retCode, int act, int role) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_IN_OUT, retCode, act, role);
	}

	public void getGiftShowList(int start, int num) {
		RequestGetGiftShowList req = new RequestGetGiftShowList();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		req.getGiftShow(auth, BAApplication.app_version_code, uid, start, num, this);
	}

	@Override
	public void resultGetGiftShowList(int retCode, GiftInfoList list) {
		GiftListCacheViewData.setSendGiftListCacheData(activity, GiftListCacheViewData.File_SHOW_NAME_START, list);
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_GIFT_LIST, retCode, 0, list);
	}

	public void setMemberRole(int act, int uid, int roomid) {
		RequestSetMemberRole req = new RequestSetMemberRole();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int roomuid = 0;
		if (info != null) {
			auth = info.auth;
			roomuid = info.uid.intValue();
		}
		req.setMemberRole(auth, BAApplication.app_version_code, act, uid, roomuid, roomid, this);
	}

	@Override
	public void resultMemberRole(int retCode, int act) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_MEMBER_ROLE, retCode, act);
	}

	public void addHot(int roomid, int roomuid) {
		RequestAddHot req = new RequestAddHot();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int Selfuid = 0;
		if (info != null) {
			auth = info.auth;
			Selfuid = info.uid.intValue();
		}
		req.clickAddHot(auth, BAApplication.app_version_code, roomid, Selfuid, roomuid, this);
	}

	@Override
	public void resultAddHot(int retCode) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_ADD_HOT, retCode, 0);
	}

	public void finishShow(int act) {
		RequestCloseShow req = new RequestCloseShow();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		if (BAApplication.showRoomInfo != null) {
			req.closeShow(auth, BAApplication.app_version_code, BAApplication.showRoomInfo.getRoomid(), uid, act, this);
		}
	}

	@Override
	public void resultCloseShow(int retCode) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_CLOSE, retCode, 0);
	}

	public void delateShow() {
		RequestDelateShow req = new RequestDelateShow();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		if (BAApplication.showRoomInfo != null) {
			req.getDelateShow(auth, BAApplication.app_version_code, BAApplication.showRoomInfo.getRoomid(), uid, BAApplication.showRoomInfo
					.getOwneruserinfo().getUid(), "", this);
		}
	}

	@Override
	public void getDelateShowResult(int retCode) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_DELATESHOW, retCode, 0);
	}

	public void getSingleRoomInfo(int roomid, int roomuid) {
		RequestGetSingleShowRoom req = new RequestGetSingleShowRoom();

		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int Selfuid = 0;
		if (info != null) {
			auth = info.auth;
			Selfuid = info.uid.intValue();
		}
		req.getRoomInfo(auth, BAApplication.app_version_code, roomid, Selfuid, roomuid, this);
	}

	@Override
	public void getIGetSingleShowRoomResult(int retCode, ShowRoomInfo roomInfo) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_GET_SINGLE_ROOM, retCode, 0, roomInfo);
	}

	public void getShowGiftHistory(int hostuid, int start, int num) {
		RequestGetShowGiftList req = new RequestGetShowGiftList();

		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int Selfuid = 0;
		if (info != null) {
			auth = info.auth;
			Selfuid = info.uid.intValue();
		}
		req.getShowGiftHistory(auth, BAApplication.app_version_code, hostuid, Selfuid, start, num, this);
	}

	@Override
	public void resultShowGiftList(int retCode, int end, List<GiftDealInfoP> list) {
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_GIFT_HISTORY, retCode, end, list);
	}

	public void openBox() {
		RequestOpenBox req = new RequestOpenBox();

		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int Selfuid = 0;
		if (info != null) {
			auth = info.auth;
			Selfuid = info.uid.intValue();
		}
		req.openBox(auth, BAApplication.app_version_code, Selfuid, this);
	}

	@Override
	public void resultOpenBox(int retCode, int num, String key, String name) {
		BAApplication.showBoxNum = num;
		BAApplication.showBoxName = name;
		BAApplication.showBoxPic = key;
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.SHOW_ROOM_OPEN_BOX_RESULT, retCode, 0);
	}
}
