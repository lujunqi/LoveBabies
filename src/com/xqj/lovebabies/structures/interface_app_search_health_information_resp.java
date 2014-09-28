package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.table_health_information;

public class interface_app_search_health_information_resp extends interface_head_resp{
	private List<table_health_information> list = null;

	public List<table_health_information> getList() {
		return list;
	}

	public void setList(List<table_health_information> list) {
		this.list = list;
	}
}
