package com.xqj.lovebabies.structures;

/**
 * ��ȡ���������б�����
 * @author Administrator
 *
 */
public class interface_app_get_baby_relations_req extends interface_head_req {
	private String baby_id;

	public String getBaby_id() {
		return baby_id;
	}

	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
	}
	
}
