package com.xqj.lovebabies.structures;

import java.util.List;
import com.xqj.lovebabies.databases.*;

public class interface_app_get_notice_comment_list_resp extends interface_head_resp {
	private List<table_interaction_notice_comment> comments = null;

	public List<table_interaction_notice_comment> getComments() {
		return comments;
	}

	public void setComments(List<table_interaction_notice_comment> comments) {
		this.comments = comments;
	}
}
