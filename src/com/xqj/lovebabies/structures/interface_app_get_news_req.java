package com.xqj.lovebabies.structures;

public class interface_app_get_news_req extends interface_head_req {
	private String news_id = null;
	private String user_id = null;
	private int page_number = 0;

	public String getNews_id() {
		return news_id;
	}

	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getPage_number() {
		return page_number;
	}

	public void setPage_number(int page_number) {
		this.page_number = page_number;
	}
}
