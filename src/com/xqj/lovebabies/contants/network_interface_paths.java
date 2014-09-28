package com.xqj.lovebabies.contants;

public class network_interface_paths {
	public final static String get_project_root = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/";
	public final static String get_temp_file = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/tempfiles";
	// --家园互动相关接口
	public final static String interface_app_user_login = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/user/login?response=application/json";
	public final static String interface_get_user_detail = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/user/getUser?response=application/json";
	public final static String interface_get_notice_list = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/notice/getListByPage?response=application/json";
	public final static String interface_get_notice_detail = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/notice/getDetail?response=application/json";
	public final static String interface_get_notice_comment = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/notice/getCommList?response=application/json";
	public final static String interface_get_notice_praise = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/notice/getPraiseList?response=application/json";
	public final static String interface_upload_file = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/upload/uploadFile?response=application/json";
	public final static String interface_set_notice_praise = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/notice/praiseNotice?response=application/json";
	public final static String interface_unset_notice_praise = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/notice/rePraiseNotice?response=application/json";
	public final static String interface_delete_notice_comment = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/notice/delNoticeComm?response=application/json";
	public final static String interface_create_notice_comment = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/notice/commNotice?response=application/json";
	public final static String interface_create_notice = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/notice/addNotice?response=application/json";
	public final static String interface_get_news_list = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/information/getMessage?response=application/json";
	public final static String interface_get_notice_receiver = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/contact/getContact?response=application/json";
	
	// --  宝宝相册
	public final static String interface_get_my_baby = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/baby/getMyBaby?response=application/json";
	public final static String interface_get_all_my_baby = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/baby/getAllBaby?response=application/json";
	public final static String interface_get_baby_growth_by_page = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/getBabyGrowthByPage?response=application/json";
	public final static String interface_get_baby_growth_img = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/getBabyGrowthImg?response=application/json";
	public final static String interface_add_baby_growth = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/addBabyGrowth?response=application/json";
	public final static String interface_delete_baby_growth = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/delBabyGrowth?response=application/json";
	public final static String interface_add_baby_growth_comment = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/commGrowth?response=application/json";
	public final static String interface_delete_baby_growth_comment = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/delGrowthComm?response=application/json";
	public final static String interface_praise_baby_growth = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/praiseGrowth?response=application/json";
	public final static String interface_cancel_praise_baby_growth = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/rePraiseGrowth?response=application/json";
	public final static String interface_get_baby_growth_comment = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/getCommList?response=application/json";
	public final static String interface_get_baby_growth_praise = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/growth/getPraiseList?response=application/json";

	public final static String interface_some_read = "";
	
	
	// ----个人中心
	public final static String interface_upload_user_head_image = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/user/uploadImage?response=application/json";
	public final static String interface_update_user_info = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/user/setUser?response=application/json";
	public final static String interface_get_verify_code = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/user/getVerificationCode?response=application/json";
	public final static String interface_check_verify_code = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/user/verify?response=application/json";
	public final static String interface_check_and_register_user = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/user/addUser?response=application/json";
	public final static String interface_reset_user_password = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/user/resetPassword?response=application/json";
	public final static String interface_update_phone = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/user/changePhone?response=application/json";
	public final static String interface_add_baby = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/baby/addBaby?response=application/json";
	public final static String interface_modify_baby = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/baby/setBaby?response=application/json";
	public final static String interface_del_baby = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/baby/delBaby?response=application/json";
	public final static String interface_get_my_care_baby = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/baby/getConcernedBaby?response=application/json";
	public final static String interface_get_baby_relations = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/baby/getBabyRelatives?response=application/json";
	public final static String interface_get_invite_code = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/baby/getInviteCode?response=application/json";
	public final static String interface_invite_baby = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/baby/invite?response=application/json";
	//积分查询
	public final static String interface_get_integral_record = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/integral/getRecord?response=application/json";
	public final static String interface_add_integral_record = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/integral/addIntegral?response=application/json";
	public final static String interface_get_integral_rules = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/integral/getRules?response=application/json";
	public final static String interface_get_total_points = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/integral/getTotal?response=application/json";
	// 我的收藏
	public final static String interface_get_my_favorite = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/collect/getCollect?response=application/json";
	public final static String interface_collect_health_information = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/collect/addCollect?response=application/json";
	public final static String interface_del_my_favorite = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/collect/delCollect?response=application/json";
	// 健康育儿
	public final static String interface_get_health_information_by_page = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/content/getList?response=application/json";
	public final static String interface_get_health_information_detail = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/content/getDetail?response=application/json";
	public final static String interface_search_health_information = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/services/content/search?response=application/json";

}
