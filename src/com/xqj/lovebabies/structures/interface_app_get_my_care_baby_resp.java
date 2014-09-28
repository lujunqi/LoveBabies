package com.xqj.lovebabies.structures;

import java.util.List;
import com.xqj.lovebabies.databases.table_my_center_my_care_baby;

public class interface_app_get_my_care_baby_resp extends interface_head_resp{
	private List<table_my_center_my_care_baby> list = null;

	public List<table_my_center_my_care_baby> getList() {
		return list;
	}

	public void setList(List<table_my_center_my_care_baby> list) {
		this.list = list;
	}
}
