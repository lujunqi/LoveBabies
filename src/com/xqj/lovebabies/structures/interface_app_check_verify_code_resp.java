package com.xqj.lovebabies.structures;

public class interface_app_check_verify_code_resp extends interface_head_resp {	
	private String resultCode="";
	private String returnCode="";

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
