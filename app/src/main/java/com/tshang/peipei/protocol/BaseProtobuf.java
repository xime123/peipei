package com.tshang.peipei.protocol;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg.PacketId;
import com.tshang.peipei.protocol.protobuf.ByteString;

/**
 * @Title: BaseProtobuf.java 
 *
 * @Description: protobuf
 *
 * @author allen  
 *
 * @date 2015-1-17 下午2:54:21 
 *
 * @version V1.0   
 */
public class BaseProtobuf {

	public static GoGirlBaseMsg.Builder setBaseMsg(byte[] auth, int ver, PacketId cid) {
		GoGirlBaseMsg.Builder msg = GoGirlBaseMsg.newBuilder();
		msg.setAuth(ByteString.copyFrom(auth));
		msg.setVer(ver);
		msg.setCid(cid);
		msg.setOs(BAConstants.RECHARGE_OS_ANDROID.intValue());
		msg.setSrc(0);
		return msg;
	}
}
