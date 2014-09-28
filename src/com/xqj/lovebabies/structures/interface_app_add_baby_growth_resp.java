package com.xqj.lovebabies.structures;

public class interface_app_add_baby_growth_resp extends interface_head_resp {
	public final int ADD_SUCCESS_WITH_POINTS = 2;// 成功并且增加积分
	public final int ADD_SUCCESS_NO_POINTS = 1;// 成功 ，没有增加积分
	public final int ADD_FAILED = 0;//	 提交失败
	
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
