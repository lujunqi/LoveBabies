package com.xqj.lovebabies.structures;

/**
 * ��ѯ����������Ѷ����
 * @author Administrator
 *
 */
public class interface_app_get_health_information_req extends interface_head_req {
	private String action;// info:��ͨ     top��ͷ��
	private String show_page;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getShow_page() {
		return show_page;
	}
	public void setShow_page(String show_page) {
		this.show_page = show_page;
	}
	
}
