package com.xqj.lovebabies.structures;

public class interface_app_delete_baby_growth_comment_resp extends interface_head_resp {
	public final String SUCCESS = "[1]";// �ɹ� 
	public final String FAILED = "[0]";//	 �ύʧ��
	public final String FAILED_NO_PERMISSION = "[-1]";//	 �ύʧ��,��Ȩ��
	
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
