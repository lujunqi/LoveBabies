package com.xqj.lovebabies.structures;

/**
 * 查询健康育儿资讯请求
 * @author Administrator
 *
 */
public class interface_app_search_health_information_req extends interface_head_req {
	private String condition;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
}
