package com.tshang.peipei.model.entity;

/**
 * @Title: ShowChatEntity.java 
 *
 * @Description: 秀场聊天对象 
 *
 * @author allen  
 *
 * @date 2015-1-29 下午2:35:34 
 *
 * @version V1.0   
 */
public class ShowChatEntity {
	public int uid;//用户id
	public int sex;//用户性别
	public String nick;//用户昵称
	public String data;//内容
	public String giftKey;//礼物图片
	public int giftId;//礼物id
	public int giftNum;//礼物数量
	public String giftName;//礼物名称
	public String receiveName;//接受礼物人的名字
	//	public GoGirlChatDataP chatdatap;
	public int type;//类型
	public int series = 0;//如果大于0说明就是连刷礼物
	public String voiceFile = "";
	public int voiceLength = 0;
	public int ridingid;
	public String ridingName;
}
