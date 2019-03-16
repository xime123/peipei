package com.tshang.peipei.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.tshang.peipei.protocol.asn.gogirl.UserSimpleInfo;

/**
 * @Title: 聊天所用到的xml格式
 *
 * @Description: 用于保存聊天xml对象
 *
 * @author Administrator  
 *
 * @date 2014-4-8 下午4:26:33 
 *
 * @version V1.0   
 */
public class ChatMessageEntity {

	private String length, finger1, finger2, fingerId, fingerFrom, fingerWinId, fingerUid1, fingerUid2, fingerNick1, fingerNick2, bet, globid,
			playtime2, antetype = "0", globalid, memo, darenick1, dareuid1, darenick2, dareuid2;

	public String getGlobalid() {
		return globalid;
	}

	public void setGlobalid(String globalid) {
		this.globalid = globalid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDarenick1() {
		return darenick1;
	}

	public void setDarenick1(String darenick1) {
		this.darenick1 = darenick1;
	}

	public String getDareuid1() {
		return dareuid1;
	}

	public void setDareuid1(String dareuid1) {
		this.dareuid1 = dareuid1;
	}

	public String getDarenick2() {
		return darenick2;
	}

	public void setDarenick2(String darenick2) {
		this.darenick2 = darenick2;
	}

	public String getDareuid2() {
		return dareuid2;
	}

	public void setDareuid2(String dareuid2) {
		this.dareuid2 = dareuid2;
	}

	public String getAntetype() {
		return antetype;
	}

	public void setAntetype(String antetype) {
		this.antetype = antetype;
	}

	public String getPlaytime2() {
		return playtime2;
	}

	public void setPlaytime2(String playtime2) {
		this.playtime2 = playtime2;
	}

	public String getGlobid() {
		return globid;
	}

	public void setGlobid(String globid) {
		this.globid = globid;
	}

	public String getFingerNick1() {
		return fingerNick1;
	}

	public String getFingerNick2() {
		return fingerNick2;
	}

	public void setFingerNick1(String fingerNick1) {
		this.fingerNick1 = fingerNick1;
	}

	public void setFingerNick2(String fingerNick2) {
		this.fingerNick2 = fingerNick2;
	}

	public String getFinger1() {
		return finger1;
	}

	public String getBet() {
		return bet;
	}

	public void setBet(String bet) {
		this.bet = bet;
	}

	public String getFingerUid1() {
		return fingerUid1;
	}

	public void setFingerUid1(String fingerUid1) {
		this.fingerUid1 = fingerUid1;
	}

	public String getFingerUid2() {
		return fingerUid2;
	}

	public void setFingerUid2(String fingerUid2) {
		this.fingerUid2 = fingerUid2;
	}

	public void setFinger1(String finger1) {
		this.finger1 = finger1;
	}

	public String getFinger2() {
		return finger2;
	}

	public void setFinger2(String finger2) {
		this.finger2 = finger2;
	}

	private String bigimgurl, midimgurl, thumburl, thumblength, thumbwidth, thumbheight;
	private String voicelength, downcount, cancelflag, voiceformat, forwardflag;
	private String id, albumname, albumdesc, createtime, lastupdatetime, coverpic, coverpicid, coverpickey, accessloyalty, photototal;
	private String giftSize, giftKey, giftName, giftId;

	public String getFingerWinId() {
		return fingerWinId;
	}

	public void setFingerWinId(String fingerWinId) {
		this.fingerWinId = fingerWinId;
	}

	public String getFingerFrom() {
		return fingerFrom;
	}

	public void setFingerFrom(String fingerFrom) {
		this.fingerFrom = fingerFrom;
	}

	public String getFingerId() {
		return fingerId;
	}

	public void setFingerId(String fingerId) {
		this.fingerId = fingerId;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getGiftSize() {
		return giftSize;
	}

	public void setGiftSize(String giftSize) {
		this.giftSize = giftSize;
	}

	public String getGiftKey() {
		return giftKey;
	}

	public void setGiftKey(String giftKey) {
		this.giftKey = giftKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlbumname() {
		return albumname;
	}

	public void setAlbumname(String albumname) {
		this.albumname = albumname;
	}

	public String getAlbumdesc() {
		return albumdesc;
	}

	public void setAlbumdesc(String albumdesc) {
		this.albumdesc = albumdesc;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getLastupdatetime() {
		return lastupdatetime;
	}

	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}

	public String getCoverpic() {
		return coverpic;
	}

	public void setCoverpic(String coverpic) {
		this.coverpic = coverpic;
	}

	public String getCoverpicid() {
		return coverpicid;
	}

	public void setCoverpicid(String coverpicid) {
		this.coverpicid = coverpicid;
	}

	public String getCoverpickey() {
		return coverpickey;
	}

	public void setCoverpickey(String coverpickey) {
		this.coverpickey = coverpickey;
	}

	public String getAccessloyalty() {
		return accessloyalty;
	}

	public void setAccessloyalty(String accessloyalty) {
		this.accessloyalty = accessloyalty;
	}

	public String getPhotototal() {
		return photototal;
	}

	public void setPhotototal(String photototal) {
		this.photototal = photototal;
	}

	public String getVoicelength() {
		return voicelength;
	}

	public void setVoicelength(String voicelength) {
		this.voicelength = voicelength;
	}

	public String getDowncount() {
		return downcount;
	}

	public void setDowncount(String downcount) {
		this.downcount = downcount;
	}

	public String getCancelflag() {
		return cancelflag;
	}

	public void setCancelflag(String cancelflag) {
		this.cancelflag = cancelflag;
	}

	public String getVoiceformat() {
		return voiceformat;
	}

	public void setVoiceformat(String voiceformat) {
		this.voiceformat = voiceformat;
	}

	public String getForwardflag() {
		return forwardflag;
	}

	public void setForwardflag(String forwardflag) {
		this.forwardflag = forwardflag;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getBigimgurl() {
		return bigimgurl;
	}

	public void setBigimgurl(String bigimgurl) {
		this.bigimgurl = bigimgurl;
	}

	public String getMidimgurl() {
		return midimgurl;
	}

	public void setMidimgurl(String midimgurl) {
		this.midimgurl = midimgurl;
	}

	public String getThumburl() {
		return thumburl;
	}

	public void setThumburl(String thumburl) {
		this.thumburl = thumburl;
	}

	public String getThumblength() {
		return thumblength;
	}

	public void setThumblength(String thumblength) {
		this.thumblength = thumblength;
	}

	public String getThumbwidth() {
		return thumbwidth;
	}

	public void setThumbwidth(String thumbwidth) {
		this.thumbwidth = thumbwidth;
	}

	public String getThumbheight() {
		return thumbheight;
	}

	public void setThumbheight(String thumbheight) {
		this.thumbheight = thumbheight;
	}

	private String desc;
	private String createuid;
	private String totalgoldcoin;
	private String totalportionnum;
	private String leftgoldcoin;
	private String leftportionnum;
	private String haveGetRedPacketCoinUserName;//领取人的姓名
	private String intdata;//领取人领了多少钱

	public String getHaveGetRedPacketCoinUserName() {
		return haveGetRedPacketCoinUserName;
	}

	public void setHaveGetRedPacketCoinUserName(String haveGetRedPacketCoinUserName) {
		this.haveGetRedPacketCoinUserName = haveGetRedPacketCoinUserName;
	}

	public String getIntdata() {
		return intdata;
	}

	public void setIntdata(String intdata) {
		this.intdata = intdata;
	}

	public String getTotalgoldcoin() {
		return totalgoldcoin;
	}

	public void setTotalgoldcoin(String totalgoldcoin) {
		this.totalgoldcoin = totalgoldcoin;
	}

	public String getTotalportionnum() {
		return totalportionnum;
	}

	public void setTotalportionnum(String totalportionnum) {
		this.totalportionnum = totalportionnum;
	}

	public String getLeftgoldcoin() {
		return leftgoldcoin;
	}

	public void setLeftgoldcoin(String leftgoldcoin) {
		this.leftgoldcoin = leftgoldcoin;
	}

	public String getLeftportionnum() {
		return leftportionnum;
	}

	public void setLeftportionnum(String leftportionnum) {
		this.leftportionnum = leftportionnum;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCreateuid() {
		return createuid;
	}

	public void setCreateuid(String createuid) {
		this.createuid = createuid;
	}

	private String step;//技能下单
	private String type;
	private String title;
	private String nick;
	private String skilluid;

	public String getSkilluid() {
		return skilluid;
	}

	public void setSkilluid(String skilluid) {
		this.skilluid = skilluid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String detail;

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	//女神技
	private String content;
	private String gold;
	private String lefttime;
	private String additionalword;
	private String charmeffect;
	private String scoreeffect;
	private String skillDesc;
	private String skilllistid;
	private String sex;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGold() {
		return gold;
	}

	public void setGold(String gold) {
		this.gold = gold;
	}

	public String getLefttime() {
		return lefttime;
	}

	public void setLefttime(String lefttime) {
		this.lefttime = lefttime;
	}

	public String getAdditionalword() {
		return additionalword;
	}

	public void setAdditionalword(String additionalword) {
		this.additionalword = additionalword;
	}
	
	public String getCharmeffect() {
		return charmeffect;
	}
	
	public void setCharmeffect(String charmeffect) {
		this.charmeffect = charmeffect;
	}

	public String getScoreeffect() {
		return scoreeffect;
	}

	public void setScoreeffect(String scoreeffect) {
		this.scoreeffect = scoreeffect;
	}

	public String getSkillDesc() {
		return skillDesc;
	}

	public void setSkillDesc(String skillDesc) {
		this.skillDesc = skillDesc;
	}
	
	public String getSkilllistid() {
		return skilllistid;
	}
	
	public void setSkilllistid(String skilllistid) {
		this.skilllistid = skilllistid;
	}
	
	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	//后宫红包接龙
	private String togroupid;
	private String orggoldcoin;
	private String orgsilvercoin;
	private String comsume;
	private String comsumedesc;
	private String endtime;
	private String createnick;
	private String createAvatar;
	private String gradeinfo;
	private String winnick;
	private String winAvatar;
	private String redpacketstatus;
	private String orgtotalgoldcoin;
	private List<ChatMessageEntity> jionPersons = new ArrayList<ChatMessageEntity>();
	
	public String getTogroupid() {
		return togroupid;
	}
	
	public void setTogroupid(String togroupid) {
		this.togroupid = togroupid;
	}
	
	public String getOrggoldcoin() {
		return orggoldcoin;
	}
	
	public void setOrggoldcoin(String orggoldcoin) {
		this.orggoldcoin = orggoldcoin;
	}
	
	public String getOrgsilvercoin() {
		return orgsilvercoin;
	}
	public void setOrgsilvercoin(String orgsilvercoin) {
		this.orgsilvercoin = orgsilvercoin;
	}
	
	public String getComsume() {
		return comsume;
	}
	
	public void setComsume(String comsume) {
		this.comsume = comsume;
	}
	
	public String getComsumedesc() {
		return comsumedesc;
	}
	
	public void setComsumedesc(String comsumedesc) {
		this.comsumedesc = comsumedesc;
	}
	
	public String getEndtime() {
		return endtime;
	}
	
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	public String getCreatenick() {
		return createnick;
	}
	
	public void setCreatenick(String createnick) {
		this.createnick = createnick;
	}
	
	public String getCreateAvatar() {
		return createAvatar;
	}
	
	public void setCreateAvatar(String createAvatar) {
		this.createAvatar = createAvatar;
	}
	
	public List<ChatMessageEntity> getJionPersons() {
		return jionPersons;
	}
	
	public String getRedpacketstatus() {
		return redpacketstatus;
	}
	
	public void setRedpacketstatus(String redpacketstatus) {
		this.redpacketstatus = redpacketstatus;
	}
	
	public String getWinnick() {
		return winnick;
	}
	
	public void setWinnick(String winnick) {
		this.winnick = winnick;
	}
	
	public void setWinAvatar(String winAvatar) {
		this.winAvatar = winAvatar;
	}
	
	public String getWinAvatar() {
		return winAvatar;
	}
	
	public String getGradeinfo() {
		return gradeinfo;
	}
	
	public void setGradeinfo(String gradeinfo) {
		this.gradeinfo = gradeinfo;
	}
	
	public String getOrgtotalgoldcoin() {
		return orgtotalgoldcoin;
	}
	
	public void setOrgtotalgoldcoin(String orgtotalgoldcoin) {
		this.orgtotalgoldcoin = orgtotalgoldcoin;
	}
}
