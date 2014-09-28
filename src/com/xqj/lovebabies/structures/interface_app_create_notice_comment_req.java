package com.xqj.lovebabies.structures;

public class interface_app_create_notice_comment_req extends interface_head_req {
	private String comment_notice_id = null;
	private String comment_sender = null;
	private String comment_content = null;

	public String getComment_notice_id() {
		return comment_notice_id;
	}

	public void setComment_notice_id(String comment_notice_id) {
		this.comment_notice_id = comment_notice_id;
	}

	public String getComment_sender() {
		return comment_sender;
	}

	public void setComment_sender(String comment_sender) {
		this.comment_sender = comment_sender;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

}
