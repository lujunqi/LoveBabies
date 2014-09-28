package com.xqj.lovebabies.structures;

public class interface_app_upload_file_resp extends interface_head_resp {
	private String return_str = null;
	private String upload_file_path = "";
	private String upload_file_name = "";

	public String getReturn_str() {
		return return_str;
	}

	public void setReturn_str(String return_str) {
		this.return_str = return_str;
	}

	public String getUpload_file_path() {
		return upload_file_path;
	}

	public void setUpload_file_path(String upload_file_path) {
		this.upload_file_path = upload_file_path;
	}

	public String getUpload_file_name() {
		return upload_file_name;
	}

	public void setUpload_file_name(String upload_file_name) {
		this.upload_file_name = upload_file_name;
	}

}
