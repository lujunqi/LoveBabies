package com.xqj.lovebabies.structures;

public class interface_app_check_and_regist_user_resp extends interface_head_resp {	
	private String resultCode="";
	private String returnCode="";
//	返回值1：注册成功
//	jsonResult.put(1);
//	返回值2：保存失败
//	jsonResult.put(-1); 
//	返回值2：验证码不存在
//	jsonResult.put(-2);
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
