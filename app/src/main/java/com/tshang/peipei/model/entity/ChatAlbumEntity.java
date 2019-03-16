package com.tshang.peipei.model.entity;

import java.io.Serializable;

/**
 * @Title: ChatAlbumEntity.java 
 *
 * @Description: 发送私密相册
 *
 * @author allen  
 *
 * @date 2014-5-16 下午2:20:56 
 *
 * @version V1.0   
 */
@SuppressWarnings("serial")
public class ChatAlbumEntity implements Serializable {

	public java.math.BigInteger id;
	public byte[] albumname;
	public byte[] albumdesc;
	public java.math.BigInteger createtime;
	public java.math.BigInteger lastupdatetime;
	public byte[] coverpic;
	public java.math.BigInteger coverpicid;
	public byte[] coverpickey;
	public java.math.BigInteger accessloyalty;
	public java.math.BigInteger photototal;

}
