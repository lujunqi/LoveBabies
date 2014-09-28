package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.table_interaction_notice;
import com.xqj.lovebabies.databases.table_interaction_notice_comment;
import com.xqj.lovebabies.databases.table_interaction_notice_praise;

public class interface_app_get_notice_detail_resp extends interface_head_resp {
	private table_interaction_notice notice = null;
	private List<table_interaction_notice_praise> praises = null;
	private List<table_interaction_notice_comment> comments = null;

	public table_interaction_notice getNotice() {
		return notice;
	}

	public void setNotice(table_interaction_notice notice) {
		this.notice = notice;
	}

	public List<table_interaction_notice_praise> getPraises() {
		return praises;
	}

	public void setPraises(List<table_interaction_notice_praise> praises) {
		this.praises = praises;
	}

	public List<table_interaction_notice_comment> getComments() {
		return comments;
	}

	public void setComments(List<table_interaction_notice_comment> comments) {
		this.comments = comments;
	}

}
