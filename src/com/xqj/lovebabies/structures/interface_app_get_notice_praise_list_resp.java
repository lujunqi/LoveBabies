package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.table_interaction_notice_praise;

public class interface_app_get_notice_praise_list_resp extends interface_head_resp {
	private List<table_interaction_notice_praise> praises = null;

	public List<table_interaction_notice_praise> getPraises() {
		return praises;
	}

	public void setPraises(List<table_interaction_notice_praise> praises) {
		this.praises = praises;
	}

}
