package com.xqj.lovebabies.structures;

public class interface_app_add_baby_growth_comment_resp extends interface_head_resp {

	public final int ADD_SUCCESS = 1;// ³É¹¦
	
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
	
}
