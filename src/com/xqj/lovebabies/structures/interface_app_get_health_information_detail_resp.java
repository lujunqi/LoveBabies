package com.xqj.lovebabies.structures;


public class interface_app_get_health_information_detail_resp extends interface_head_resp {
	private int result_code = 0;
	private String return_code = "";
	
	private String content_id;//��¼ID
	private String title;
	private String content;
	private String publish_time;
	private String collect_count;
	private String share_count;
	private String source;
	private String pic_name;
	private String s_pic_name;
	private String is_collect;
	

	public int getResult_code() {
		return result_code;
	}

	public void setResult_code(int result_code) {
		this.result_code = result_code;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getContent_id() {
		return content_id;
	}

	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public String getCollect_count() {
		return collect_count;
	}

	public void setCollect_count(String collect_count) {
		this.collect_count = collect_count;
	}

	public String getShare_count() {
		return share_count;
	}

	public void setShare_count(String share_count) {
		this.share_count = share_count;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPic_name() {
		return pic_name;
	}

	public void setPic_name(String pic_name) {
		this.pic_name = pic_name;
	}

	public String getS_pic_name() {
		return s_pic_name;
	}

	public void setS_pic_name(String s_pic_name) {
		this.s_pic_name = s_pic_name;
	}

	public String getIs_collect() {
		return is_collect;
	}

	public void setIs_collect(String is_collect) {
		this.is_collect = is_collect;
	}
	
}
