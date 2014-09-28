package com.xqj.lovebabies.structures;

import java.io.File;
import java.util.List;

public class interface_app_create_notice_req extends interface_head_req {
	private String notice_type_id = null;
	private String notice_title = null;
	private String notice_content = null;
	private String notice_sender = null;
	private List<String> notice_receiver = null;
	private List<File> notice_picture = null;

	public String getNotice_type_id() {
		return notice_type_id;
	}

	public void setNotice_type_id(String notice_type_id) {
		this.notice_type_id = notice_type_id;
	}

	public String getNotice_title() {
		return notice_title;
	}

	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}

	public String getNotice_content() {
		return notice_content;
	}

	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}

	public String getNotice_sender() {
		return notice_sender;
	}

	public void setNotice_sender(String notice_sender) {
		this.notice_sender = notice_sender;
	}

	public List<String> getNotice_receiver() {
		return notice_receiver;
	}

	public void setNotice_receiver(List<String> notice_receiver) {
		this.notice_receiver = notice_receiver;
	}

	public List<File> getNotice_picture() {
		return notice_picture;
	}

	public void setNotice_picture(List<File> notice_picture) {
		this.notice_picture = notice_picture;
	}

}
