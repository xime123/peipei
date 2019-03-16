package com.tshang.peipei.base.babase;

import java.util.List;

import com.tshang.peipei.protocol.asn.gogirl.CommentInfoList;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;

/**
 * @Title: BAMessageBean.java 
 *
 * @Description: handler 消息传递
 *
 * @author vactor
 *
 * @date 2014-4-14 上午10:58:13 
 *
 * @version V1.0   
 */
public class BAMessageBean {
	private int retCode;
	private int isEnd;
	private int total;
	private List<Object> list;
	private TopicInfo topicInfo;
	private CommentInfoList commentList;

	public TopicInfo getTopicInfo() {
		return topicInfo;
	}

	public void setTopicInfo(TopicInfo topicInfo) {
		this.topicInfo = topicInfo;
	}

	public CommentInfoList getCommentList() {
		return commentList;
	}

	public void setCommentList(CommentInfoList commentList) {
		this.commentList = commentList;
	}

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public int getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(int isEnd) {
		this.isEnd = isEnd;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

}
