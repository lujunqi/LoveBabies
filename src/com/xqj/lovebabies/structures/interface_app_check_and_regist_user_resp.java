package com.xqj.lovebabies.structures;

public class interface_app_check_and_regist_user_resp extends interface_head_resp {	
	private String resultCode="";
	private String returnCode="";
//	����ֵ1��ע��ɹ�
//	jsonResult.put(1);
//	����ֵ2������ʧ��
//	jsonResult.put(-1); 
//	����ֵ2����֤�벻����
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
