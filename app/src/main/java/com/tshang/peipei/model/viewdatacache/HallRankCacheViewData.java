package com.tshang.peipei.model.viewdatacache;

import java.io.File;

import android.app.Activity;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.ibm.asn1.DEREncoder;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserInfoAndSkillInfoList;

/**
 * @Title: GiftListCacheViewData.java 
 *
 * @Description: TODO缓存送礼界面数据
 *
 * @author Jeff  
 *
 * @date 2014年10月11日 下午3:13:32 
 *
 * @version V1.3.0   
 */
public class HallRankCacheViewData {
	public static final String HALL_ONTIME_FEMALE_FILE_NAME_START = "HallOnTimeFemaleGoGirlUserInfoList";
	public static final String HALL_ONTIME_MALE_FILE_NAME_START = "HallOnTimeMaleGoGirlUserInfoList";
	public static final String HALL_NEW_FEMALE_FILE_NAME_START = "HallNewFemaleGoGirlUserInfoList";
	public static final String HALL_NEW_MALE_FILE_NAME_START = "HallNewMaleGoGirlUserInfoList";
	public static final String HALL_NEAR_FEMALE_FILE_NAME_START = "HallNearFemaleGoGirlUserInfoList";
	public static final String HALL_NEAR_MALE_FILE_NAME_START = "HallNearMaleGoGirlUserInfoList";
	public static final String HALL_SKILL_FEMALE_FILE_NAME_START = "HallSkillFemaleRetGGSkillInfoList";
	public static final String HALL_SKILL_MALE_FILE_NAME_START = "HallSkillMaleRetGGSkillInfoList";

	public static final String RANK_GAME_FILE_NAME_START = "RankGameGoGirlUserInfoList";
	public static final String Rank_GAME_HAREM_FILE_NAME_START = "RankGameHaremGroupInfoList";
	public static final String RANK_NEW_FEMALE_FILE_NAME_START = "RankNewFemaleGoGirlUserInfoList";
	public static final String RANK_NEW_MALE_FILE_NAME_START = "RankNewMaleGoGirlUserInfoList";

	public static final String RANK_GLAMOUR_DAY_FILE_NAME_START = "RankGlamourDayGoGirlUserInfoList";
	public static final String RANK_GLAMOUR_WEEK_FILE_NAME_START = "RankGlamourWeekGoGirlUserInfoList";
	public static final String RANK_GLAMOUR_TOTAL_FILE_NAME_START = "RankGlamourTotalGoGirlUserInfoList";

	public static final String RANK_WEALTH_DAY_FILE_NAME_START = "RankWealthDayGoGirlUserInfoList";
	public static final String RANK_WEALTH_WEEK_FILE_NAME_START = "RankWealthWeekGoGirlUserInfoList";
	public static final String RANK_WEALTH_TOTAL_FILE_NAME_START = "RankWealthTotalGoGirlUserInfoList";

	/**
	 * 
	 * @author Jeff 缓存送礼界面数据
	 *
	 * @param activity 上下文
	 * @param infoList 礼物列表
	 */
	public static void setGoGirlUserInfoListCacheData(Activity activity, String fileNameStart, GoGirlUserInfoList infoList) {
		if (infoList == null || infoList.isEmpty()) {
			return;
		}
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info != null) {
			DEREncoder edc = new DEREncoder();
			try {
				infoList.encode(edc);
				byte[] data = edc.toByteArray();
				ConfigCacheUtil.setUrlCache(activity, data, fileNameStart + info.uid.intValue());
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
	public static GoGirlUserInfoList getGoGirlUserInfoListCacheData(Activity activity, String fileNameStart) {
		File file = ConfigCacheUtil.getCacheFile(activity, fileNameStart);
		if (file != null) {
			byte[] contents = BaseFile.getBytesByFilePath(file);
			if (contents != null && contents.length != 0) {
				GoGirlUserInfoList list = new GoGirlUserInfoList();
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
	
	/**
	 * 
	 * @author DYH 缓存交友界面数据
	 *
	 * @param activity 上下文
	 * @param infoList 礼物列表
	 */
	public static void setGoGirlUserInfoAndSkillListCacheData(Activity activity, String fileNameStart, UserAndSkillInfoList infoList) {
		if (infoList == null || infoList.isEmpty()) {
			return;
		}
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info != null) {
			DEREncoder edc = new DEREncoder();
			try {
				infoList.encode(edc);
				byte[] data = edc.toByteArray();
				ConfigCacheUtil.setUrlCache(activity, data, fileNameStart + info.uid.intValue());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}
	
	/**
	 * 缓存的用户和技能数据
	 * @author DYH
	 *
	 * @param activity
	 * @return
	 */
	public static UserAndSkillInfoList getGoGirlUserInfoAndSkillListCacheData(Activity activity, String fileNameStart) {
		File file = ConfigCacheUtil.getCacheFile(activity, fileNameStart);
		if (file != null) {
			byte[] contents = BaseFile.getBytesByFilePath(file);
			if (contents != null && contents.length != 0) {
				UserAndSkillInfoList list = new UserAndSkillInfoList();
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

	public static GroupInfoList getGroupInfoListCacheData(Activity activity, String fileNameStart) {
		File file = ConfigCacheUtil.getCacheFile(activity, fileNameStart);
		if (file != null) {
			byte[] contents = BaseFile.getBytesByFilePath(file);
			if (contents != null && contents.length != 0) {
				GroupInfoList list = new GroupInfoList();
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

	/**
	 * 
	 * @author Jeff 缓存后宫排行列表
	 *
	 * @param activity 上下文
	 * @param infoList 礼物列表
	 */
	public static void setGroupInfoListCacheData(Activity activity, String fileNameStart, GroupInfoList infoList) {
		if (infoList == null || infoList.isEmpty()) {
			return;
		}
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info != null) {
			DEREncoder edc = new DEREncoder();
			try {
				infoList.encode(edc);
				byte[] data = edc.toByteArray();
				ConfigCacheUtil.setUrlCache(activity, data, fileNameStart + info.uid.intValue());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	public static void setRetGGSkillInfoListCacheData(Activity activity, String fileNameStart, RetGGSkillInfoList infoList) {
		if (infoList == null || infoList.isEmpty()) {
			return;
		}
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info != null) {
			DEREncoder edc = new DEREncoder();
			try {
				infoList.encode(edc);
				byte[] data = edc.toByteArray();
				ConfigCacheUtil.setUrlCache(activity, data, fileNameStart + info.uid.intValue());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	public static RetGGSkillInfoList getRetGGSkillInfoListCacheData(Activity activity, String fileNameStart) {
		File file = ConfigCacheUtil.getCacheFile(activity, fileNameStart);
		if (file != null) {
			byte[] contents = BaseFile.getBytesByFilePath(file);
			if (contents != null && contents.length != 0) {
				RetGGSkillInfoList list = new RetGGSkillInfoList();
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
