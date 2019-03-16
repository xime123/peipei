package com.tshang.peipei.base;

import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.model.ReceiverChatData;
import com.tshang.peipei.model.broadcast.BroadCastUtils;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;

/**
 * @Title: BaseAnimUtil.java 
 *
 * @Description: 解码大厅动画 
 *
 * @author DYH  
 *
 * @date 2016-1-26 下午3:40:22 
 *
 * @version V1.0   
 */
public class BaseAnimUtil {

	/**
	 * 解码大厅的抢红包
	 * @author DYH
	 *
	 * @param grapbroadcastInfo
	 * @return
	 */
	public static GoGirlDataInfo decodeGoGirlDataInfo(BroadcastInfo grapbroadcastInfo){
		GoGirlDataInfo goGirlDataInfo = null;
		if (grapbroadcastInfo != null) {
			byte[] datalist = grapbroadcastInfo.datalist;
			GoGirlDataInfo datainfo = null;
			if (datalist != null && datalist.length > 0) {
				datainfo = BroadCastUtils.getGoGirlDataInfo(datalist);
				if (datainfo != null) {
					if (datainfo.type.intValue() == MessageType.GOGIRL_DATA_TYPE_BROADCAST_SPECIAL_EFFECT.getValue()) {
						goGirlDataInfo = datainfo;
					}
				}
			}
		}
		
		return goGirlDataInfo;
	}
}
