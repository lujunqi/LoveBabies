package com.xqj.lovebabies.structures;

import java.util.List;
import com.xqj.lovebabies.databases.table_album_growth_praise;

public class interface_app_get_baby_growth_praise_resp extends interface_head_resp{
	private List<table_album_growth_praise> list = null;
	private String is_praised;//  «∑Ò“—‘ﬁ
	private String result_code;
	private String return_code;

	public List<table_album_growth_praise> getList() {
		return list;
	}

	public void setList(List<table_album_growth_praise> list) {
		this.list = list;
	}

	public String getIs_praised() {
		return is_praised;
	}

	public void setIs_praised(String is_praised) {
		this.is_praised = is_praised;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
}
