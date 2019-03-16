package com.tshang.peipei.model.redpacket;

import android.app.Activity;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfoList;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.harem.CreateHarem;
import com.tshang.peipei.model.request.RequestCreateRedPacket;
import com.tshang.peipei.model.request.RequestCreateRedPacket.ICreateredpacket;
import com.tshang.peipei.model.request.RequestGetDeliverRedPacketList;
import com.tshang.peipei.model.request.RequestGetDeliverRedPacketList.IGetDeliverRedPacketList;
import com.tshang.peipei.model.request.RequestGetRedPacketDetail;
import com.tshang.peipei.model.request.RequestGetRedPacketDetail.IGetRedPacketDetail;
import com.tshang.peipei.model.request.RequestGetUnpackRedPacketList;
import com.tshang.peipei.model.request.RequestGetUnpackRedPacketList.IGetUnpackRedPacketList;
import com.tshang.peipei.model.request.RequestUnpackRedPacket;
import com.tshang.peipei.model.request.RequestUnpackRedPacket.IUnpackredpacket;

/**
 * @Title: CreateRedPacket.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 创建红包逻辑
 *
 * @author Jeff 
 *
 * @date 2014年10月16日 下午6:31:27 
 *
 * @version V1.4.0   
 */
public class RedPacketCreate implements ICreateredpacket, IUnpackredpacket, IGetRedPacketDetail, IGetDeliverRedPacketList, IGetUnpackRedPacketList {
	private BAHandler handler;
	private int loadDeliverPosition = -1;

	public int getLoadDeliverPosition() {
		return loadDeliverPosition;
	}

	public void setLoadDeliverPosition(int loadDeliverPosition) {
		this.loadDeliverPosition = loadDeliverPosition;
	}

	private static RedPacketCreate instance = null;

	public static RedPacketCreate getInstance() {
		if (instance == null) {
			synchronized (CreateHarem.class) {
				if (instance == null) {
					instance = new RedPacketCreate();
				}
			}
		}
		return instance;
	}

	public void reqCreateRedPacket(Activity activity, int groupid, int totalgoldcoin, int portionnum, String desc, BAHandler handle) {
		RequestCreateRedPacket req = new RequestCreateRedPacket();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.handler = handle;
		BaseUtils.showDialog(activity, "正在恩赐...");
		req.createRedPacket(info.auth, BAApplication.app_version_code, info.uid.intValue(), groupid, totalgoldcoin, desc, portionnum, this);
	}

	/**
	 *  领取红包
	 * @author Jeff
	 *
	 * @param activity 上下文
	 * @param redpacketid 红包id
	 * @param redpacketuid 发送红包人的uid
	 * @param handle
	 */
	public void reqUnpackRedPacket(Activity activity, int redpacketid, int redpacketuid, BAHandler handle) {
		RequestUnpackRedPacket req = new RequestUnpackRedPacket();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.handler = handle;
		BaseUtils.showDialog(activity, "正在领取...");
		req.unpackRedPacket(info.auth, BAApplication.app_version_code, info.uid.intValue(), redpacketid, redpacketuid, this);
	}

	public void reqGetDeliverRedPacketList(Activity activity, BAHandler handle) {
		RequestGetDeliverRedPacketList req = new RequestGetDeliverRedPacketList();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.handler = handle;
		req.getDeliverRedPacketList(info.auth, BAApplication.app_version_code, info.uid.intValue(), info.uid.intValue(), loadDeliverPosition, 10,
				this);
	}

	public void reqGetReceiverRedPacketList(Activity activity, BAHandler handle) {
		RequestGetUnpackRedPacketList req = new RequestGetUnpackRedPacketList();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.handler = handle;
		req.getUnpackRedPacketList(info.auth, BAApplication.app_version_code, info.uid.intValue(), info.uid.intValue(), loadDeliverPosition, 10, this);
	}

	/**
	 * 获取红包详情
	 * @author Administrator
	 *
	 * @param activity
	 * @param redpacketid
	 * @param redpacketuid
	 * @param handle
	 */
	public void reqGetRedPacketDetail(Activity activity, int redpacketid, int redpacketuid, BAHandler handle) {
		RequestGetRedPacketDetail req = new RequestGetRedPacketDetail();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		this.handler = handle;
		BaseUtils.showDialog(activity, activity.getString(R.string.loading));
		req.getRedPacketDetail(info.auth, BAApplication.app_version_code, info.uid.intValue(), redpacketid, redpacketuid, this);
	}

	@Override
	public void createredpacket(int retCode, RedPacketInfo info) {
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_CREATE_SUCCESS_VALUE, info);
		} else if (retCode == E_GG_RED_PACKET_PORTION) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_CREATE_OUT_OF_NUM_VALUE);
		} else if (retCode == E_GG_RED_PACKET_LIMIT) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_ABOVE_NORM);
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_CREATE_FAILED_VALUE, info);
		}

	}

	@Override
	public void unpacketRedpacket(int retCode, RedPacketInfo info) {
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_UNPAKCET_SUCCESS_VALUE, info);//领取成功
		} else if (retCode == E_GG_RED_PACKET_HAS_UNPACKED) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_UNPACKET_HAVE_RECEIVER_SUCCESS_VALUE, info);//已经领过了
		} else if (retCode == E_GG_RED_PACKET_TIMEOUT) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_UNPACKET_TIMEOUT_VALUE, info);//红包超时失效
		} else if (retCode == E_GG_RED_PACKET_DONE) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_UNPACKET_NO_MONEY_VALUE, info);//红包被领完
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_UNPACKET_FAILED_VALUE, info);
		}
	}

	public static final int E_GG_RED_PACKET_NO_EXIST = -28058; // 红包不存在
	public static final int E_GG_RED_PACKET_HAS_UNPACKED = -28059; // 已领红包
	public static final int E_GG_RED_PACKET_TIMEOUT = -28060; // 红包过期
	public static int E_GG_RED_PACKET_DONE = -28061; // 红包已领完
	public static int E_GG_RED_PACKET_PORTION = -28066; // 红包份数问题
	public static int E_GG_RED_PACKET_LIMIT = -28079; // 红包额度超限

	@Override
	public void getRedpacketDetail(int retCode, RedPacketInfo info) {
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_GET_REDPACKET_DETAIL_SUCCESS_VALUE, info);//获取详情成功
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_GET_REDPACKET_DETAIL_FAILED_VALUE, info);//获取详情失败
		}
	}

	@Override
	public void getDeliverRedPacketList(int retCode, int isend, RedPacketInfoList list) {//我发的红包回来
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_GET_DELIVER_REDPACKET_LIST_SUCCESS_VALUE, isend, isend, list);//获取详情成功
			loadDeliverPosition -= 10;
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_GET_DELIVER_REDPACKET_LIST_FAILED_VALUE, isend, isend, list);//获取详情成功
		}

	}

	@Override
	public void getUnpackRedPacketList(int retCode, int isend, RedPacketInfoList list) {//我收到的红包
		if (retCode == 0) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_GET_DELIVER_REDPACKET_LIST_SUCCESS_VALUE, isend, isend, list);//获取详情成功
			loadDeliverPosition -= 10;
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_GET_DELIVER_REDPACKET_LIST_FAILED_VALUE, isend, isend, list);//获取详情成功
		}

	}
}
