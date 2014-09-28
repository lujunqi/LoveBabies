package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.*;

public class interface_app_get_notice_receiver_list_resp extends interface_head_resp {
	private String org_id = null;
	private String org_name = null;
	private List<table_interaction_contacts> same = null;
	private List<table_interaction_contacts> baby = null;
	private interface_app_get_notice_receiver_list_resp orgs = null;

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public List<table_interaction_contacts> getSame() {
		return same;
	}

	public void setSame(List<table_interaction_contacts> same) {
		this.same = same;
	}

	public List<table_interaction_contacts> getBaby() {
		return baby;
	}

	public void setBaby(List<table_interaction_contacts> baby) {
		this.baby = baby;
	}

	public interface_app_get_notice_receiver_list_resp getOrgs() {
		return orgs;
	}

	public void setOrgs(interface_app_get_notice_receiver_list_resp orgs) {
		this.orgs = orgs;
	}

}
