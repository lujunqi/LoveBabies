package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.*;

public class interface_app_get_news_resp extends interface_head_resp {
	private List<table_interaction_news> news = null;
	private int page_number = 1;
	
	public List<table_interaction_news> getNews() {
		return news;
	}

	public void setNews(List<table_interaction_news> news) {
		this.news = news;
	}

	public int getPage_number() {
		return page_number;
	}

	public void setPage_number(int page_number) {
		this.page_number = page_number;
	}

}
