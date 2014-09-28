package com.xqj.lovebabies.structures;

public class interface_app_upload_file_req extends interface_head_req {
	private String upload_user_id = null;// 上传者ID
	private String upload_file_path = null;// 上传文件的本地目录
	private String upload_file_type = null;
	private String upload_file_name = null;//文件名

	public String getUpload_user_id() {
		return upload_user_id;
	}

	public void setUpload_user_id(String upload_user_id) {
		this.upload_user_id = upload_user_id;
	}

	public String getUpload_file_path() {
		return upload_file_path;
	}

	public void setUpload_file_path(String upload_file_path) {
		this.upload_file_path = upload_file_path;
	}

	public String getUpload_file_type() {
		return upload_file_type;
	}

	public void setUpload_file_type(String upload_file_type) {
		this.upload_file_type = upload_file_type;
	}

	public String getUpload_file_name() {
		return upload_file_name;
	}

	public void setUpload_file_name(String upload_file_name) {
		this.upload_file_name = upload_file_name;
	}

}
