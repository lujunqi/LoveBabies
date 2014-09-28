package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.*;

public class interface_app_get_notice_list_resp {
	private List<table_interaction_notice> notices = null;

	public List<table_interaction_notice> getNotices() {
		return notices;
	}

	public void setNotices(List<table_interaction_notice> notices) {
		this.notices = notices;
	}

}
