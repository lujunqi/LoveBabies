package com.xqj.lovebabies.structures;

/**
 * ��ѯ����������Ѷ����
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
