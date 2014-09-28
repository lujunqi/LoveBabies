package com.xqj.lovebabies.structures;

public class interface_app_delete_baby_growth_resp extends interface_head_resp {
	public final String SUCCESS = "[1]";// 成功 
	public final String FAILED = "[0]";//	 提交失败
	
	public String growth_id = "";
	private String resultCode;
	private String returnCode;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getGrowth_id() {
		return growth_id;
	}

	public void setGrowth_id(String growth_id) {
		this.growth_id = growth_id;
	}
	
}
