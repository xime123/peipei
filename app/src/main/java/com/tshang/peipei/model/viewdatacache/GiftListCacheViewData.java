package com.tshang.peipei.model.viewdatacache;

import java.io.File;

import android.app.Activity;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.ibm.asn1.DEREncoder;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;

/**
 * @Title: GiftListCacheViewData.java 
 *
 * @Description: TODO缓存送礼界面数据
 *
 * @author Jeff  
 *
 * @date 2014年10月11日 下午1:40:14 
 *
 * @version V1.3.0   
 */
public class GiftListCacheViewData {
	public static final String FILE_NAME_START = "sendGiftInfoList";
	public static final String File_SHOW_NAME_START = "sendShowGiftInfoList";

	/**
	 * 
	 * @author Jeff 缓存送礼界面数据
	 *
	 * @param activity 上下文
	 * @param infoList 礼物列表
	 */
	public static void setSendGiftListCacheData(Activity activity, String fileName, GiftInfoList infoList) {
		if (infoList == null || infoList.isEmpty()) {
			return;
		}
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info != null) {
			DEREncoder edc = new DEREncoder();
			try {
				infoList.encode(edc);
				byte[] data = edc.toByteArray();
				ConfigCacheUtil.setUrlCache(activity, data, fileName + info.uid.intValue());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * 缓存的礼物数据
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static GiftInfoList getSendGiftListCacheData(Activity activity, String fileName) {
		File file = ConfigCacheUtil.getCacheFile(activity, fileName);
		if (file != null) {
			byte[] contents = BaseFile.getBytesByFilePath(file);
			if (contents != null && contents.length != 0) {
				GiftInfoList list = new GiftInfoList();
				BERDecoder dec = new BERDecoder(contents);
				try {
					list.decode(dec);
					return list;
				} catch (ASN1Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

}
