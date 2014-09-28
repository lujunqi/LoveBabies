package com.xqj.lovebabies.structures;

public class interface_app_delete_my_favorite_resp extends interface_head_resp {
	public final String SUCCESS = "[1]";// ³É¹¦ 
	public final String FAILED = "[0]";// Ê§°Ü 
	
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
