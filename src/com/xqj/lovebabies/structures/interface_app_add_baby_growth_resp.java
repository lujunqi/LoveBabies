package com.xqj.lovebabies.structures;

public class interface_app_add_baby_growth_resp extends interface_head_resp {
	public final int ADD_SUCCESS_WITH_POINTS = 2;// �ɹ��������ӻ���
	public final int ADD_SUCCESS_NO_POINTS = 1;// �ɹ� ��û�����ӻ���
	public final int ADD_FAILED = 0;//	 �ύʧ��
	
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
