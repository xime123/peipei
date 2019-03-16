package com.tshang.peipei.model.biz.chat;

/**
 * @Title: Video.java 
 *
 * @Description: 视频文件
 *
 * @author Jeff  
 *
 * @date 2014年8月29日 下午2:28:08 
 *
 * @version V1.0   
 */
public class Video {
	private int id;
	private String title;
	private String album;
	private String artist;
	private String displayName;
	private String mimeType;
	private String path;
	private long size;
	private long duration;
	private String thumbImg;

	public Video(int id, String title, String album, String artist, String displayName, String mimeType, String path, long size, long duration,
			String thumbImg) {
		this.id = id;
		this.title = title;
		this.album = album;
		this.artist = artist;
		this.displayName = displayName;
		this.mimeType = mimeType;
		this.path = path;
		this.size = size;
		this.duration = duration;
		this.thumbImg = thumbImg;
	}

	public String getThumbImg() {
		return thumbImg;
	}

	public void setThumbImg(String thumbImg) {
		this.thumbImg = thumbImg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
