package com.xqj.lovebabies.databases;

import java.io.Serializable;

/**
 * 积分规则
 * @author sunshine
 *
 */
@SuppressWarnings("serial")
public class table_my_center_record_rules implements Serializable {
	private String rule_id;
	private String rule_code;
	private String integral_reason;
	private String integral_count;
	private String count_limit;
	private String remark;
	
	public String getRule_id() {
		return rule_id;
	}
	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}
	public String getRule_code() {
		return rule_code;
	}
	public void setRule_code(String rule_code) {
		this.rule_code = rule_code;
	}
	public String getIntegral_reason() {
		return integral_reason;
	}
	public void setIntegral_reason(String integral_reason) {
		this.integral_reason = integral_reason;
	}
	public String getIntegral_count() {
		return integral_count;
	}
	public void setIntegral_count(String integral_count) {
		this.integral_count = integral_count;
	}
	public String getCount_limit() {
		return count_limit;
	}
	public void setCount_limit(String count_limit) {
		this.count_limit = count_limit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
