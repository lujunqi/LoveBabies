package com.xqj.lovebabies.commons;

import java.io.File;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.trinea.android.common.util.JSONUtils;
import cn.trinea.android.common.util.StringUtils;

import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_album_baby_growth;
import com.xqj.lovebabies.databases.table_album_growth_comment;
import com.xqj.lovebabies.databases.table_album_growth_praise;
import com.xqj.lovebabies.databases.table_album_my_baby;
import com.xqj.lovebabies.databases.table_health_information;
import com.xqj.lovebabies.databases.table_health_information_top_pic_info;
import com.xqj.lovebabies.databases.table_interaction_contacts;
import com.xqj.lovebabies.databases.table_interaction_news;
import com.xqj.lovebabies.databases.table_interaction_notice;
import com.xqj.lovebabies.databases.table_interaction_notice_comment;
import com.xqj.lovebabies.databases.table_interaction_notice_praise;
import com.xqj.lovebabies.databases.table_my_center_baby_relations;
import com.xqj.lovebabies.databases.table_my_center_favorite;
import com.xqj.lovebabies.databases.table_my_center_my_care_baby;
import com.xqj.lovebabies.databases.table_my_center_my_points;
import com.xqj.lovebabies.databases.table_my_center_record_rules;
import com.xqj.lovebabies.structures.*;

public class utils_network_interfaces {
	private HttpParams http_params = null;
	private HttpPost http_post = null;
	private MultipartEntity http_entity = null;
	private HttpResponse response = null;
	private HttpClient client = null;
	private Map<String, String> params = null;
	private int result_code = 0;
	private String result_content = null;
	private JSONArray json_array = null;
	private JSONObject json_array_item = null;
	private JSONObject json_object = null;
	private int timeoutConnection = 5000;
	private int timeoutSocket = 10000;

	// 系统登录
	public interface_app_user_login_resp ios_interface_app_user_login(interface_app_user_login_req req) {
		interface_app_user_login_resp resp = null;
		try {
			resp = new interface_app_user_login_resp();
			// --
			System.out.println("-----------------系统登录------------");
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_phone", req.getUser_name());
			params.put("user_pwd", req.getUser_password());
			params.put("login_type", interface_app_user_login_req.LOGIN_TYPE_ANDROID);
			params.put("login_code", req.getLogin_code());
			// --
			http_post = new HttpPost(network_interface_paths.interface_app_user_login);
			System.out.println("path-->"+network_interface_paths.interface_app_user_login);
			System.out.println("user_phone-->"+req.getUser_name());
			System.out.println("user_pwd-->"+req.getUser_password());
			System.out.println("login_type-->"+interface_app_user_login_req.LOGIN_TYPE_ANDROID);
			System.out.println("login_code-->"+req.getLogin_code());
			
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				System.out.println(result_content);
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				json_array_item = json_array.getJSONObject(0);
				resp.setUser_id(json_array_item.getString("USER_ID"));
				resp.setUser_icon(json_array_item.getString("USER_PIC"));
				resp.setUser_type(json_array_item.getString("UTYPE_ID"));
				return resp;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	// 获取用户详情
	public interface_app_get_user_detail_resp ios_interface_app_get_user_detail(interface_app_get_user_detail_req req) {
		interface_app_get_user_detail_resp resp = null;
		try {
			resp = new interface_app_get_user_detail_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_user_detail);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				json_array_item = json_array.getJSONObject(0);
				resp.setUser_id(json_array_item.getString("USER_ID"));
				resp.setUser_email(json_array_item.getString("USER_EMAIL"));
				resp.setUser_icon(json_array_item.getString("USER_PIC"));
				resp.setUser_integral(json_array_item.getString("USER_INTEGRAL"));
				resp.setUser_nike_name(json_array_item.getString("USER_NICK_NAME"));
				resp.setUser_real_name(json_array_item.getString("REAL_NAME"));
				resp.setUser_password(json_array_item.getString("USER_PWD"));
				resp.setUser_phone(json_array_item.getString("USER_PHONE"));
				resp.setUser_sex(json_array_item.getString("SEX"));
				resp.setUser_signature(json_array_item.getString("USER_SIGNATURE"));
				resp.setVaccine_remind_status(json_array_item.getString("IS_RECEIVE"));
				resp.setVaccine_remind_time(json_array_item.getString("REMIND_TIME"));
				return resp;
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 分页获取公告列表
	public interface_app_get_notice_list_resp ios_interface_app_get_notice_list(interface_app_get_notice_list_req req) {
		interface_app_get_notice_list_resp resp = null;
		List<table_interaction_notice> notices = null;
		table_interaction_notice notice = null;
		try {
			resp = new interface_app_get_notice_list_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", String.valueOf(req.getUser_id()));
			params.put("page_index", String.valueOf(req.getPage_number()));
			// --
			System.out.println("----网络获取公告----");
			http_post = new HttpPost(network_interface_paths.interface_get_notice_list);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				notices = new ArrayList<table_interaction_notice>();
				for (int i = 0; i < json_array.length(); i++) {
					json_array_item = json_array.getJSONObject(i);
					notice = new table_interaction_notice();
					notice.setNotice_collect_count(json_array_item.getString("COLLECT_NUMS"));
					notice.setNotice_comment_count(json_array_item.getString("COMMENT_NUMS"));
					notice.setNotice_content(json_array_item.getString("NOTICE_CONTENT"));
					notice.setNotice_download_count(json_array_item.getString("DOWN_NUMS"));
					notice.setNotice_id(json_array_item.getString("NOTICE_ID"));
					notice.setNotice_org_name(json_array_item.getString("ORG_SHORTNAME"));
					notice.setNotice_picture_breviary(json_array_item.getString("NOTICE_SPIC"));
					notice.setNotice_picture_detailed(json_array_item.getString("NOTICE_PIC"));
					notice.setNotice_praise_count(json_array_item.getString("PRAISE_NUMS"));
					notice.setNotice_publish_time(json_array_item.getString("PUBLISH_TIME"));
					notice.setNotice_read_count(json_array_item.getString("READ_NUMS"));
					notice.setNotice_sender_icon(json_array_item.getString("CREATER_PIC"));
					notice.setNotice_sender_id(json_array_item.getString("CREATER"));
					notice.setNotice_sender_name(json_array_item.getString("CREATER_NAME"));
					notice.setNotice_share_count(json_array_item.getString("SHARE_NUMS"));
					notice.setNotice_title(json_array_item.getString("NOTICE_TITLE"));
					notice.setNotice_type_name(json_array_item.getString("NOTICE_NAME"));
					System.out.println(notice.getNotice_content());
					notices.add(notice);
				}
				resp.setNotices(notices);
			}
			return resp;
		} catch (Exception e) {
			return null;
		}
	}

	// 上传语音、图片、txt等文件。路径与文件名放入不同变量
	public interface_app_upload_file_resp ios_interface_app_upload_media_file(interface_app_upload_file_req req) {
		interface_app_upload_file_resp resp = null;
		try {
			resp = new interface_app_upload_file_resp();
			resp.setUpload_file_path(req.getUpload_file_path());
			resp.setUpload_file_name(req.getUpload_file_name());
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_upload_file);
			MultipartEntity entity = new MultipartEntity();
			System.out.println("------------上传文件-------------");
			System.out.println("path-->"+network_interface_paths.interface_upload_file);
			System.out.println("user_id-->"+req.getUpload_user_id());
			System.out.println("upload_file-->"+req.getUpload_file_path());
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("user_id", req.getUpload_user_id());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					entity.addPart(entry.getKey(), par);
				}
			}
			// 添加文件参数信息
			System.out.println("file-->"+req.getUpload_file_path()+req.getUpload_file_name());
			File file = null;
			file = new File(req.getUpload_file_path()+req.getUpload_file_name());
			ContentBody content = new FileBody(file);
			entity.addPart("upload_file", content);
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				json_object = new JSONObject(EntityUtils.toString(re.getEntity()));
				String return_string = json_object.getString("return");
				System.out.println("upload_file_return_string-->"+return_string);
				resp.setReturn_str(return_string);
			} else {
				resp.setReturn_str("error");
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	// 上传语音、图片、txt等文件。路径与文件名放入同一变量
	public interface_app_upload_file_resp ios_interface_app_upload_file(interface_app_upload_file_req req) {
		interface_app_upload_file_resp resp = null;
		try {
			resp = new interface_app_upload_file_resp();
			resp.setUpload_file_path(req.getUpload_file_path());
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_upload_file);
			MultipartEntity entity = new MultipartEntity();
			System.out.println("------------上传文件-------------");
			System.out.println("path-->"+network_interface_paths.interface_upload_file);
			System.out.println("user_id-->"+req.getUpload_user_id());
			System.out.println("upload_file-->"+req.getUpload_file_path());
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("user_id", req.getUpload_user_id());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					entity.addPart(entry.getKey(), par);
				}
			}
			// 添加文件参数信息
			File file = null;
			file = new File(req.getUpload_file_path());
			ContentBody content = new FileBody(file);
			entity.addPart("upload_file", content);
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				json_object = new JSONObject(EntityUtils.toString(re.getEntity()));
				String return_string = json_object.getString("return");
				System.out.println("upload_file_return_string-->"+return_string);
				resp.setReturn_str(return_string);
			} else {
				resp.setReturn_str("error");
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 获取公告详情
	public interface_app_get_notice_detail_resp ios_interface_app_get_notice_detail(interface_app_get_notice_detail_req req) {
		interface_app_get_notice_detail_resp resp = null;
		try {
			resp = new interface_app_get_notice_detail_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			params.put("notice_id", req.getNotice_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_notice_detail);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				System.out.println(result_content);

			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 获取公告点赞列表
	public interface_app_get_notice_praise_list_resp ios_interface_app_get_notice_praise(interface_app_get_notice_praise_list_req req) {
		interface_app_get_notice_praise_list_resp resp = null;
		try {
			resp = new interface_app_get_notice_praise_list_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			if(req.getUser_id()!=null && req.getUser_id().length()>0){
				params.put("user_id", req.getUser_id());
			}
			if(req.getNotice_id()!=null && req.getNotice_id().length()>0){
				params.put("notice_id", req.getNotice_id());
			}
			System.out.println("user_id-->"+req.getUser_id());
			System.out.println("notice_id-->"+req.getNotice_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_notice_praise);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			table_interaction_notice_praise praise = null;
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				System.out.println("result_content-->"+result_content);
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(json_object.getString("return"));
				json_object = json_array.getJSONObject(0);
				json_array = new JSONArray(json_object.getString("PRAISELIST"));
				List<table_interaction_notice_praise> list = new ArrayList<table_interaction_notice_praise>();
				for (int i = 0; i < json_array.length(); i++) {
					json_array_item = json_array.getJSONObject(i);
					praise = new table_interaction_notice_praise();
					praise.setNotice_id(req.getNotice_id());
					praise.setPraise_user_icon(json_array_item.getString("USER_PIC"));
					praise.setPraise_user_id(json_array_item.getString("USER_ID"));
					praise.setPraise_user_nike_name(json_array_item.getString("USER_NICK_NAME"));
					list.add(praise);
				}
				resp.setPraises(list);
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 获取公告评论列表
	public interface_app_get_notice_comment_list_resp ios_interface_app_get_notice_comment(interface_app_get_notice_comment_list_req req) {
		interface_app_get_notice_comment_list_resp resp = null;
		try {
			resp = new interface_app_get_notice_comment_list_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			if(req.getUser_id()!=null && req.getUser_id().length()>0){
				params.put("user_id", req.getUser_id());
			}
			if(req.getNotice_id()!=null && req.getNotice_id().length()>0){
				params.put("notice_id", req.getNotice_id());
			}
			System.out.println("user_id-->"+req.getUser_id());
			System.out.println("notice_id-->"+req.getNotice_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_notice_comment);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				System.out.println("result_content-->"+result_content);
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(json_object.getString("return"));
				json_object = json_array.getJSONObject(0);
				json_array = new JSONArray(json_object.getString("COMMLIST"));
				List<table_interaction_notice_comment> list = new ArrayList<table_interaction_notice_comment>();
				table_interaction_notice_comment comment = null;
				for (int i = 0; i < json_array.length(); i++) {
					comment = new table_interaction_notice_comment();
					json_array_item = json_array.getJSONObject(i);
					comment.setComment_content(json_array_item.getString("COMM_CONTENT"));
					comment.setComment_id(json_array_item.getString("COMM_ID"));
					comment.setComment_time(json_array_item.getString("COMM_TIME"));
					comment.setComment_user_icon(json_array_item.getString("USER_PIC"));
					comment.setComment_user_id(json_array_item.getString("USER_ID"));
					comment.setComment_user_nike_name(json_array_item.getString("USER_NICK_NAME"));
					comment.setNotice_id(req.getNotice_id());
					list.add(comment);
				}
				resp.setComments(list);
			}
			return resp;
		} catch (Exception e) {
			return null;
		}
	}

	// 公告点赞
	public interface_app_set_notice_praise_resp ios_interface_app_set_notice_praise_resp(interface_app_set_notice_praise_req req) {
		interface_app_set_notice_praise_resp resp = null;
		try {
			resp = new interface_app_set_notice_praise_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			params.put("notice_id", req.getNotice_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_set_notice_praise);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(json_object.getString("return"));
				resp.setResult_code(json_array.getString(0));
			}
			return resp;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	// 公告取消点赞
	public interface_app_unset_notice_praise_resp ios_interface_app_unset_notice_praise_resp(interface_app_unset_notice_praise_req req) {
		interface_app_unset_notice_praise_resp resp = null;
		try {
			resp = new interface_app_unset_notice_praise_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			params.put("notice_id", req.getNotice_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_unset_notice_praise);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(json_object.getString("return"));
				resp.setResult_code(json_array.getString(0));
			}
			return resp;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	// 获取公告接收者列表
	public interface_app_get_notice_receiver_list_resp ios_interface_app_get_notice_receiver_list_resp(interface_app_get_notice_receiver_list_req req) {
		interface_app_get_notice_receiver_list_resp resp = null;
		try {
			resp = new interface_app_get_notice_receiver_list_resp();
			// --
			http_params = new BasicHttpParams();
			http_params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("UTF-8"));
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_notice_receiver);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				json_object = new JSONObject(result_content);
				json_array = JSONUtils.getJSONArray(json_object, "return", null);
				for (int i = 0; i < json_array.length(); i++) {
					json_object = json_array.getJSONObject(i);

				}
			}
			return resp = null;
		} catch (Exception e) {
			return null;
		}
	}

	private interface_app_get_notice_receiver_list_resp analysis_interface_notice_receiver(JSONArray array) {
		interface_app_get_notice_receiver_list_resp resp = null;
		List<table_interaction_contacts> list_contact = null;
		table_interaction_contacts contact = null;
		JSONObject j_root = null;
		JSONArray j_array = null;
		JSONObject j_array_item = null;
		JSONArray j_array_temp = null;
		JSONObject j_array_temp_item = null;
		try {
			array = array == null ? new JSONArray() : array;
			if (array.length() <= 0) return null;
			// --
			resp = new interface_app_get_notice_receiver_list_resp();
			for (int i = 0; i < array.length(); i++) {
				j_root = array.getJSONObject(i);
				resp.setOrg_id(JSONUtils.getString(j_root, "ORG_ID", null));
				resp.setOrg_name(JSONUtils.getString(j_root, "ORG_SHORTNAME", null));
				// --
				j_array = JSONUtils.getJSONArray(j_root, "SAME", null);
				j_array = j_array == null ? new JSONArray() : j_array;
				list_contact = new ArrayList<table_interaction_contacts>();
//				for (int j = 0; j < j_array.length(); j++) {
//					j_array_item = j_array.getJSONObject(i);
//					contact = new table_interaction_contacts();
//					contact.setUser_id(user_id);
//					contact.setUser_real_name(user_real_name);
//				}
//				resp.setSame(same);
//				resp.setBaby(baby);
//				resp.setOrgs(orgs);
			}
			return resp;
		} catch (Exception e) {
			return null;
		}
	}

	// 发布公告
	public interface_app_create_notice_resp ios_interface_create_notice_resp(interface_app_create_notice_req req) {
		interface_app_create_notice_resp resp = null;
		try {
			resp = new interface_app_create_notice_resp();
			// --
			http_params = new BasicHttpParams();
			http_params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("UTF-8"));
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("notice_content", req.getNotice_content());
			params.put("creater", req.getNotice_sender());
			params.put("notice_title", req.getNotice_title());
			params.put("type_id", req.getNotice_type_id());
			List<String> list_receiver = req.getNotice_receiver();
			list_receiver = list_receiver == null ? new ArrayList<String>() : list_receiver;
			for (int i = 0; i < list_receiver.size(); i++) {
				params.put("user_id", list_receiver.get(i));
			}
			// --
			http_post = new HttpPost(network_interface_paths.interface_create_notice);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			List<File> list_picture = new ArrayList<File>();
			list_picture = req.getNotice_picture();
			list_picture = list_picture == null ? new ArrayList<File>() : list_picture;
			for (int i = 0; i < list_picture.size(); i++) {
				File file = list_picture.get(i);
				ContentBody content = new FileBody(file);
				http_entity.addPart("notice_pic", content);
			}
			// --
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(json_object.getString("return"));
				resp.setResult_code(json_array.getString(0));
			}
			return resp;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	// 发布公告评论
	public interface_app_create_notice_comment_resp ios_interface_app_create_notice_comment_resp(interface_app_create_notice_comment_req req) {
		interface_app_create_notice_comment_resp resp = null;
		try {
			resp = new interface_app_create_notice_comment_resp();
			// --
			http_params = new BasicHttpParams();
			http_params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("UTF-8"));
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);

			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			// --
			params = new HashMap<String, String>();
			params.put("comm_content", req.getComment_content());
			params.put("notice_id", req.getComment_notice_id());
			params.put("user_id", req.getComment_sender());
			// --
			http_post = new HttpPost(network_interface_paths.interface_create_notice_comment);
			http_post.addHeader("charset", HTTP.UTF_8);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {

					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					http_entity.addPart(entry.getKey(), par);
				}
			}
			System.out.println(http_entity.getContentEncoding());
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(json_object.getString("return"));
				resp.setResult_code(json_array.getString(0));
			}
			return resp;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	// 删除公告评论
	public interface_app_delete_notice_comment_resp ios_interface_app_delete_notice_comment_resp(interface_app_delete_notice_comment_req req) {
		interface_app_delete_notice_comment_resp resp = null;
		try {
			resp = new interface_app_delete_notice_comment_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("comm_id", req.getComment_id());
			params.put("user_id", req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_delete_notice_comment);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(json_object.getString("return"));
				resp.setResult_code(json_array.getString(0));
			}
			return resp;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	// 获取动态列表
	public interface_app_get_news_resp ios_interface_app_get_news(interface_app_get_news_req req) {

		table_interaction_news t_interaction_news = null;
		List<table_interaction_news> list = null;
		interface_app_get_news_resp resp = null;
		try {
			resp = new interface_app_get_news_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			if (!StringUtils.isBlank(req.getNews_id())) {
				params.put("info_id", req.getNews_id());
			}
			if (!StringUtils.isBlank(req.getUser_id())) {
				params.put("user_id", req.getUser_id());
			}
			if (req.getPage_number() > 0) {
				params.put("index", String.valueOf(req.getPage_number()));
			}
			System.out.println("info_id-->"+req.getNews_id());
			System.out.println("user_id-->"+req.getUser_id());
			System.out.println("index-->"+req.getPage_number());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_news_list);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				list = new ArrayList<table_interaction_news>();
				result_content = EntityUtils.toString(response.getEntity());
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(json_object.getString("return"));
				for (int i = 0; i < json_array.length(); i++) {
					json_array_item = json_array.getJSONObject(i);
					t_interaction_news = new table_interaction_news();
					t_interaction_news.setCollect_count(json_array_item.getString("COLLECT_NUMS"));
					t_interaction_news.setComment_count(json_array_item.getString("COMMENT_NUMS"));
					t_interaction_news.setDown_count(json_array_item.getString("DOWN_NUMS"));
					t_interaction_news.setNews_content(json_array_item.getString("INFO_CONTENT"));
					t_interaction_news.setNews_id(json_array_item.getString("OINFO_ID"));
					t_interaction_news.setNews_org_name(json_array_item.getString("ORG_NAME"));
					t_interaction_news.setNews_title(json_array_item.getString("INFO_TITLE"));
					t_interaction_news.setPicture_path(json_array_item.getString("INFO_PIC"));
					t_interaction_news.setPraise_count(json_array_item.getString("PRAISE_NUMS"));
					t_interaction_news.setPublish_time(json_array_item.getString("PUBLISH_TIME"));
					t_interaction_news.setRead_count(json_array_item.getString("READ_NUMS"));
					t_interaction_news.setRemarks(json_array_item.getString("REMARK"));
					t_interaction_news.setShare_count(json_array_item.getString("SHARE_NUMS"));
					t_interaction_news.setUser_id(json_array_item.getString("USER_ID"));
					t_interaction_news.setVideo_path(json_array_item.getString("INFO_VEDIO"));
					list.add(t_interaction_news);
				}
				resp.setNews(list);
			}
			return resp;
		} catch (Exception e) {
			return null;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------------------
	/****************************** by lxk **********************************/
	// 查询与我相关的宝宝
	public interface_app_get_all_my_baby_resp ios_interface_app_get_all_my_baby(interface_app_get_all_my_baby_req req) {
		interface_app_get_all_my_baby_resp resp = null;
		try {
			resp = new interface_app_get_all_my_baby_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_all_my_baby);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				List<table_album_my_baby> baby_list = new ArrayList<table_album_my_baby>();
				if (json_array.length() > 0) {
					for (int i = 0; i < json_array.length(); i++) {
						table_album_my_baby baby = new table_album_my_baby();
						json_array_item = json_array.getJSONObject(i);
						baby.setBaby_id(json_array_item.getString("BABY_ID"));
						baby.setUser_id(json_array_item.getString("USER_ID"));
						baby.setBaby_name(json_array_item.getString("BABY_NAME"));

						if (json_array_item.has("BABY_SEX")) {
							baby.setBaby_sex(json_array_item.getString("BABY_SEX"));
						}
						if (json_array_item.has("BIRTHDAY")) {
							baby.setBirthday(json_array_item.getString("BIRTHDAY"));
						}
						if (json_array_item.has("BABY_PIC")) {
							baby.setBaby_pic(json_array_item.getString("BABY_PIC"));
						}
						if (json_array_item.has("PERMISSIONS")) {
							baby.setPermissons(json_array_item.getString("PERMISSIONS"));
						}
						baby_list.add(baby);
					}
				}
				resp.setList(baby_list);
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// 获取我的宝贝
	public interface_app_get_my_baby_resp ios_interface_app_get_my_baby(interface_app_get_my_baby_req req) {
		interface_app_get_my_baby_resp resp = null;
		try {
			resp = new interface_app_get_my_baby_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_my_baby);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				List<table_album_my_baby> baby_list = new ArrayList<table_album_my_baby>();
				if (json_array.length() > 0) {
					for (int i = 0; i < json_array.length(); i++) {
						table_album_my_baby baby = new table_album_my_baby();
						json_array_item = json_array.getJSONObject(i);
						baby.setBaby_id(json_array_item.getString("BABY_ID"));
						baby.setUser_id(json_array_item.getString("USER_ID"));
						baby.setBaby_name(json_array_item.getString("BABY_NAME"));

						if (json_array_item.has("BABY_SEX")) {
							baby.setBaby_sex(json_array_item.getString("BABY_SEX"));
						}
						if (json_array_item.has("BIRTHDAY")) {
							baby.setBirthday(json_array_item.getString("BIRTHDAY"));
						}
						if (json_array_item.has("BABY_PIC")) {
							baby.setBaby_pic(json_array_item.getString("BABY_PIC"));
						}

						baby_list.add(baby);
					}
				}
				resp.setList(baby_list);
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 分页获取宝贝的成长记录
	public interface_app_get_baby_growth_by_page_resp ios_interface_app_get_baby_growth_by_page(interface_app_get_baby_growth_by_page_req req) {
		interface_app_get_baby_growth_by_page_resp resp = null;
		try {
			System.out.println("network---------------interface_get_baby_growth_by_page-------");
			resp = new interface_app_get_baby_growth_by_page_resp();
			resp.setBaby_id(req.getBaby_id());
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			params.put("baby_id", req.getBaby_id());
			params.put("page_index", req.getPage_index());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_baby_growth_by_page);
			System.out.println("network_interface_paths.interface_get_baby_growth_by_page--->" + network_interface_paths.interface_get_baby_growth_by_page);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					System.out.println("||" + entry.getKey() + ":" + entry.getValue() + "||");
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("network ------interface_get_baby_growth_by_page---------- result_code=" + result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				try {
					json_object = new JSONObject(result_content);
					List<table_album_baby_growth> baby_list = new ArrayList<table_album_baby_growth>();

					if (json_object.has("return")) {
						JSONObject select_json = new JSONObject(json_object.getString("return"));
						System.out.println("json-->" + select_json);

						if (select_json.has("RECORD_SELECT")) {
							json_array = select_json.getJSONArray("RECORD_SELECT");
							System.out.println("json——array--size-->" + json_array.length());

							if (json_array.length() > 0) {
								for (int i = 0; i < json_array.length(); i++) {
									table_album_baby_growth baby = new table_album_baby_growth();
									json_array_item = json_array.getJSONObject(i);
									baby.setBaby_id(json_array_item.getString("BABY_ID"));
									baby.setRecord_id(json_array_item.getString("RECORD_ID"));
									if (json_array_item.has("RECORD_TIME")) {
										baby.setRecord_time(json_array_item.getString("RECORD_TIME"));
									}
									if (json_array_item.has("LOCATIONS")) {
										baby.setLocations(json_array_item.getString("LOCATIONS"));
									}
									if (json_array_item.has("BABY_AGE_TRUE")) {
										baby.setAge_true(json_array_item.getString("BABY_AGE_TRUE"));
									}
									if (json_array_item.has("WEIGHT")) {
										baby.setWeight(json_array_item.getString("WEIGHT"));
									}
									if (json_array_item.has("HEIGHT")) {
										baby.setHeight(json_array_item.getString("HEIGHT"));
									}
									if (json_array_item.has("WORD_RECORD")) {
										baby.setWord_record(json_array_item.getString("WORD_RECORD"));
									}
									if (json_array_item.has("PIC_NAME")) {
										String pic_name = json_array_item.getString("PIC_NAME");
										if(pic_name!=null && pic_name.length()>0){
											baby.setPic_name(pic_name);
										}
									}
									baby_list.add(baby);
								}
							}
						}
					}
					resp.setResult_code(result_code);
					resp.setList(baby_list);
				} catch (Exception ex) {
					ex.printStackTrace();
					resp.setResult_code(result_code);
					resp.setReturn_code("json解析出错了");
				}
				return resp;
			} else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 查询宝贝成长记录图片——首页
	public interface_app_get_baby_growth_img_resp ios_interface_app_get_baby_growth_img(interface_app_get_baby_growth_img_req req) {
		interface_app_get_baby_growth_img_resp resp = null;
		try {
			System.out.println("network---------------interface_get_baby_growth_img-------");
			resp = new interface_app_get_baby_growth_img_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_baby_growth_img);
			System.out.println("network_interface_paths.interface_get_baby_growth_img--->" + network_interface_paths.interface_get_baby_growth_img);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					System.out.println("||" + entry.getKey() + ":" + entry.getValue() + "||");
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("network ------interface_get_baby_growth_img---------- result_code=" + result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
					System.out.println(result_content);
				try {
					json_object = new JSONObject(result_content);
					List<String> baby_img_list = new ArrayList<String>();

					if (json_object.has("return")) {
						json_array = new JSONArray(json_object.getString("return"));
						if (json_array.length() > 0) {
							for (int i = 0; i < json_array.length(); i++) {
								json_array_item = json_array.getJSONObject(i);
								if(json_array_item.has("SPIC_NAME")){
									System.out.println(json_array_item.getString("SPIC_NAME"));
									baby_img_list.add(json_array_item.getString("SPIC_NAME"));
								}
							}
						}
					}
					resp.setResult_code(result_code);
					resp.setList(baby_img_list);
				} catch (Exception ex) {
					ex.printStackTrace();
					resp.setResult_code(result_code);
					resp.setReturn_code("json解析出错了");
				}
				return resp;
			} else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	// 添加宝宝成长记录
	public interface_app_add_baby_growth_resp interface_app_add_baby_growth(interface_app_add_baby_growth_req req) {
		interface_app_add_baby_growth_resp resp = null;
		try {
			resp = new interface_app_add_baby_growth_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_add_baby_growth);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("baby_id", req.getBaby_id());
			params.put("user_id", req.getUser_id());
			params.put("locations", req.getLocations());
			params.put("word_record", req.getWord_record());
			if(req.getHeight()!=null && req.getHeight().length()>0){
				params.put("height", req.getHeight());
				System.out.println("height-->"+req.getHeight());
			}
			if(req.getWeight()!=null && req.getWeight().length()>0){
				params.put("weight", req.getWeight());
				System.out.println("weight-->"+req.getWeight());
			}
			System.out.println("baby_id-->"+req.getBaby_id());
			System.out.println("user_id-->"+req.getUser_id());
			System.out.println("locations-->"+req.getLocations());
			System.out.println("word_record-->"+req.getWord_record());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// 添加文件参数信息
			List<String> growth_pics = req.getGrowth_pic_list();
			if (growth_pics != null && growth_pics.size() > 0) {
				for (int i = 0; i < growth_pics.size(); i++) {
					File file = null;
					file = new File(growth_pics.get(i));
					ContentBody content = new FileBody(file);
					entity.addPart("growth_pic", content);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int statusCode = re.getStatusLine().getStatusCode();
			System.out.println("statusCode-->"+statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				json_object = new JSONObject(EntityUtils.toString(re.getEntity()));
				String returnString = json_object.getString("return");
				System.out.println("returnString-->"+returnString);
				resp.setReturnCode(returnString);
			} else {
				resp.setResultCode("error");
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除宝宝成长记录
	 * 
	 * @param req
	 * @return
	 */
	public interface_app_delete_baby_growth_resp interface_app_delete_baby_growth(interface_app_delete_baby_growth_req req) {
		interface_app_delete_baby_growth_resp resp = null;
		try {
			resp = new interface_app_delete_baby_growth_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_delete_baby_growth);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", req.getRecord_id());
			params.put("user_id", req.getUser_id());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str_result = EntityUtils.toString(re.getEntity());
				System.out.println("result:" + str_result);
				json_object = new JSONObject(str_result);
				String result_code = json_object.getString("return");
				System.out.println("result_code:" + result_code);
				resp.setResultCode(result_code);
			} else {
				resp.setResultCode("error");
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 添加宝宝成长记录评论
	 * 
	 * @param req
	 * @return
	 */
	public interface_app_add_baby_growth_comment_resp interface_app_add_baby_growth_comment(interface_app_add_baby_growth_comment_req req) {
		interface_app_add_baby_growth_comment_resp resp = null;
		try {
			resp = new interface_app_add_baby_growth_comment_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_add_baby_growth_comment);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("record_id", req.getRecord_id());
			params.put("user_id", req.getUser_id());
			params.put("comm_content", req.getComm_content());
			params.put("parent_comm_id", req.getParent_comm_id());
			params.put("comm_place", req.getComm_place());
			params.put("comm_machine", req.getComm_machion());
			params.put("remark", req.getRemark());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result_code = EntityUtils.toString(re.getEntity());
				System.out.println(result_code);
				json_object = new JSONObject(result_code);
				resp.setResultCode(json_object.getString("return"));
			} else {
				resp.setResultCode("error");
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除宝宝成长记录评论
	 * 
	 * @param req
	 * @return
	 */
	public interface_app_delete_baby_growth_comment_resp interface_app_delete_baby_growth_comment(interface_app_delete_baby_growth_comment_req req) {
		interface_app_delete_baby_growth_comment_resp resp = null;
		try {
			resp = new interface_app_delete_baby_growth_comment_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_delete_baby_growth_comment);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("comm_id", req.getComment_id());
			params.put("user_id", req.getUser_id());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result_code = EntityUtils.toString(re.getEntity());
				System.out.println(result_code);
				json_object = new JSONObject(result_code);
				resp.setResultCode(json_object.getString("return"));
			} else {
				resp.setResultCode("error");
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 给宝宝成长记录点赞
	 * 
	 * @param req
	 * @return interface_app_praise_baby_growth_req
	 */
	public interface_app_praise_baby_growth_resp interface_app_praise_baby_growth(interface_app_praise_baby_growth_req req) {
		interface_app_praise_baby_growth_resp resp = null;
		try {
			resp = new interface_app_praise_baby_growth_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			System.out.println("path-->\n" + network_interface_paths.interface_praise_baby_growth);
			HttpPost post = new HttpPost(network_interface_paths.interface_praise_baby_growth);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("record_id", req.getRecord_id());
			params.put("user_id", req.getUser_id());
			params.put("location", req.getLocation());
			params.put("oper_machine", req.getOper_mation());
			params.put("remark", req.getRemark());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result_code = EntityUtils.toString(re.getEntity());
				System.out.println(result_code);
				json_object = new JSONObject(result_code);
				resp.setResultCode(json_object.getString("return"));
			} else {
				resp.setResultCode("error");
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 取消给宝宝成长记录点赞
	 * 
	 * @param req
	 * @return interface_app_praise_baby_growth_req
	 */
	public interface_app_cancel_praise_baby_growth_resp interface_app_cancel_praise_baby_growth(interface_app_cancel_praise_baby_growth_req req) {
		interface_app_cancel_praise_baby_growth_resp resp = null;
		try {
			resp = new interface_app_cancel_praise_baby_growth_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			System.out.println("path-->\n" + network_interface_paths.interface_cancel_praise_baby_growth);
			HttpPost post = new HttpPost(network_interface_paths.interface_cancel_praise_baby_growth);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("record_id", req.getRecord_id());
			params.put("user_id", req.getUser_id());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result_code = EntityUtils.toString(re.getEntity());
				System.out.println(result_code);
				json_object = new JSONObject(result_code);
				resp.setResultCode(json_object.getString("return"));
			} else {
				resp.setResultCode("error");
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 获取成长记录的评论列表
	public interface_app_get_baby_growth_comment_resp ios_interface_app_get_baby_growth_comment(interface_app_get_baby_growth_comment_req req) {
		interface_app_get_baby_growth_comment_resp resp = null;
		try {
			resp = new interface_app_get_baby_growth_comment_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			params.put("record_id", req.getRecord_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_baby_growth_comment);
			System.out.println("path----\n" + network_interface_paths.interface_get_baby_growth_comment);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			System.out.println("----------   获取成长记录评论列表    ----------------");
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("result_code-->" + result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

				System.out.println(result_content);

				try {
					json_object = new JSONObject(result_content);
					List<table_album_growth_comment> comment_list = new ArrayList<table_album_growth_comment>();

					if (json_object.has("return")) {
						String j_str = json_object.getString("return");
						System.out.println(j_str);
						JSONArray select_json_array = new JSONArray(j_str);
						System.out.println("select_json_array-->" + select_json_array);
						if (select_json_array != null && select_json_array.length() > 0) {
							JSONObject comm_list_json = select_json_array.getJSONObject(0);
							if (comm_list_json.has("COMMLIST")) {
								json_array = comm_list_json.getJSONArray("COMMLIST");
								System.out.println("json——array--size-->" + json_array.length());

								if (json_array.length() > 0) {
									for (int i = 0; i < json_array.length(); i++) {
										table_album_growth_comment comment = new table_album_growth_comment();
										json_array_item = json_array.getJSONObject(i);
										comment.setUser_id(json_array_item.getString("USER_ID"));
										comment.setComm_id(json_array_item.getString("COMM_ID"));
										if (json_array_item.has("USER_NICK_NAME")) {
											comment.setComment_nick_name(json_array_item.getString("USER_NICK_NAME"));
										}
										if (json_array_item.has("COMM_CONTENT")) {
											comment.setComm_content(json_array_item.getString("COMM_CONTENT"));
										}
										if (json_array_item.has("COMM_TIME")) {
											comment.setComm_time(json_array_item.getString("COMM_TIME"));
										}
										comment_list.add(comment);
									}
								}
							}
						}
					}
					resp.setList(comment_list);
				} catch (Exception ex) {
					ex.printStackTrace();
					resp.setReturn_code("json解析出错了");
				}
				resp.setResult_code(String.valueOf(result_code));
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取成长记录的点赞列表
	 * 
	 * @param req
	 * @return
	 */
	public interface_app_get_baby_growth_praise_resp ios_interface_app_get_baby_growth_praise(interface_app_get_baby_growth_praise_req req) {
		interface_app_get_baby_growth_praise_resp resp = null;
		try {
			resp = new interface_app_get_baby_growth_praise_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			params.put("record_id", req.getRecord_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_baby_growth_praise);
			System.out.println("path----\n" + network_interface_paths.interface_get_baby_growth_praise);

			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			System.out.println("----------   获取成长记录点赞列表    ----------------");
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("result_code-->" + result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				try {
					json_object = new JSONObject(result_content);
					List<table_album_growth_praise> praise_list = new ArrayList<table_album_growth_praise>();
					String is_praise = "";

					if (json_object.has("return")) {
						JSONArray select_json_array = new JSONArray(json_object.getString("return"));
						System.out.println("select_json_array-->" + select_json_array);

						if (select_json_array != null && select_json_array.length() > 0) {
							System.out.println("select_json_array  size -->" + select_json_array.length());
							JSONObject select_json = select_json_array.getJSONObject(0);

							if (select_json.has("IS_PRAISED")) {// 是否已赞
								is_praise = select_json.getString("IS_PRAISED");
							}
							if (select_json.has("PRAISELIST")) {
								json_array = select_json.getJSONArray("PRAISELIST");
								System.out.println("json——array--size-->" + json_array.length());

								if (json_array.length() > 0) {
									for (int i = 0; i < json_array.length(); i++) {
										table_album_growth_praise praise_info = new table_album_growth_praise();
										json_array_item = json_array.getJSONObject(i);
										praise_info.setUser_id(json_array_item.getString("USER_ID"));
										if (json_array_item.has("USER_NICK_NAME")) {
											praise_info.setUser_nick_name(json_array_item.getString("USER_NICK_NAME"));
										}
										if (json_array_item.has("USER_PIC")) {
											praise_info.setUser_pick(json_array_item.getString("USER_PIC"));
										}
										praise_list.add(praise_info);
									}
								}
							}
						}
					}
					resp.setList(praise_list);
					resp.setIs_praised(is_praise);
				} catch (Exception ex) {
					ex.printStackTrace();
					resp.setReturn_code("json解析出错了");
				}
				resp.setResult_code(String.valueOf(result_code));
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/***
	 * ***************** 个人中心 *************************
	 */
	// 更新用户头像
	public interface_app_upload_user_head_icon_resp ios_interface_app_update_user_head_icon(interface_app_upload_user_head_icon_req req) {
		interface_app_upload_user_head_icon_resp resp = null;
		try {
			// --
			resp = new interface_app_upload_user_head_icon_resp();
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_upload_user_head_image);
			System.out.println("url-->"+network_interface_paths.interface_upload_user_head_image);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// 添加文件参数信息
			File file = null;
			file = new File(req.getImage_path());
			ContentBody content = new FileBody(file);
			entity.addPart("upload_file", content);
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int resultCode = re.getStatusLine().getStatusCode();
			System.out.println("resultCode-->"+resultCode);
			if (resultCode == HttpStatus.SC_OK) {
				json_object = new JSONObject(EntityUtils.toString(re.getEntity()));
				resp.setReturn_code(json_object.getString("return"));
			} else {
				resp.setReturn_code("error");
			}
			resp.setReturn_code(String.valueOf(resultCode));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 更新用户信息
	public interface_app_update_user_detail_resp ios_interface_app_update_user_detail(interface_app_update_user_detail_req req) {
		interface_app_update_user_detail_resp resp = null;
		try {
			// --
			resp = new interface_app_update_user_detail_resp();
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_update_user_info);
			System.out.println("url-->"+network_interface_paths.interface_update_user_info);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			params.put("user_phone", req.getUser_phone());
			params.put("auth_code", req.getAuth_code());
			params.put("nick_name", req.getNick_name());
			params.put("user_pwd", req.getUser_pwd());
			params.put("old_pwd", req.getOld_pwd());
			params.put("user_email", req.getUser_email());
			System.out.println("user_id-->"+req.getUser_id());
			System.out.println("user_phone-->"+req.getUser_phone());
			System.out.println("auth_code-->"+req.getAuth_code());
			System.out.println("nick_name-->"+req.getNick_name());
			System.out.println("user_pwd-->"+req.getUser_pwd());
			System.out.println("old_pwd-->"+req.getOld_pwd());
			System.out.println("user_email-->"+req.getUser_email());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if(entry.getValue()!=null && entry.getValue().length()>0){
						StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
						entity.addPart(entry.getKey(), par);
					}
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int resultCode = re.getStatusLine().getStatusCode();
			System.out.println("resultCode-->"+resultCode);
			if (resultCode == HttpStatus.SC_OK) {
				json_object = new JSONObject(EntityUtils.toString(re.getEntity()));
				resp.setReturn_code(json_object.getString("return"));
			} else {
				resp.setReturn_code("error");
			}
			resp.setReturn_code(String.valueOf(resultCode));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// 获取手机注册的验证码
	public interface_app_get_verify_code_resp ios_interface_app_get_verify_code(interface_app_get_verify_code_req req) {
		interface_app_get_verify_code_resp resp = null;
		try {
			resp = new interface_app_get_verify_code_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_get_verify_code);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("phone", req.getPhone());
			params.put("reason", req.getReason());
			System.out.println("phone-->"+req.getPhone());
			System.out.println("reason-->"+req.getReason());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				String return_string = EntityUtils.toString(re.getEntity());
				
				System.out.println("return_string-->"+return_string);
				
				json_object = new JSONObject(return_string);
				String invite_string = "";
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				if(json_array.length()>0){
					invite_string = json_array.get(0).toString();
					System.out.println("invite_string-->"+invite_string);
				}
				resp.setVerifyCode(invite_string);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// 更换手机号、账号
	public interface_app_change_phone_submit_resp ios_interface_app_update_phone(interface_app_change_phone_submit_req req) {
		interface_app_change_phone_submit_resp resp = null;
		try {
			resp = new interface_app_change_phone_submit_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_update_phone);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("v_code", req.getV_code());
			params.put("new_phone", req.getNew_phone());
			params.put("old_phone", req.getOld_phone());
			System.out.println("v_code-->"+req.getV_code());
			System.out.println("new_phone-->"+req.getNew_phone());
			System.out.println("old_phone-->"+req.getOld_phone());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				String return_string = EntityUtils.toString(re.getEntity());
				
				System.out.println("return_string-->"+return_string);
				
				json_object = new JSONObject(return_string);
				String invite_string = "";
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				if(json_array.length()>0){
					invite_string = json_array.get(0).toString();
					System.out.println("invite_string-->"+invite_string);
				}
				resp.setReturnCode(invite_string);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 添加宝宝
	public interface_app_add_and_set_my_baby_resp ios_interface_app_add_baby(interface_app_add_and_set_my_baby_req req) {
		interface_app_add_and_set_my_baby_resp resp = null;
		try {
			resp = new interface_app_add_and_set_my_baby_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_add_baby);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("baby_name", req.getBaby_name());
			params.put("user_id", req.getUser_id());
			params.put("sex", req.getSex());
			params.put("birthday", req.getBirthday());
			params.put("age", req.getAge());
			params.put("relation", req.getRelation());
			System.out.println("baby_name-->"+req.getBaby_name());
			System.out.println("user_id-->"+req.getUser_id());
			System.out.println("sex-->"+req.getSex());
			System.out.println("birthday-->"+req.getBirthday());
			System.out.println("age-->"+req.getAge());
			System.out.println("relation-->"+req.getRelation());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// 添加文件参数信息
			if(req.getUpload_file()!=null && req.getUpload_file().length()>0){
				File file = null;
				file = new File(req.getUpload_file());
				ContentBody content = new FileBody(file);
				entity.addPart("upload_file", content);
			}
			
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				json_object = new JSONObject(EntityUtils.toString(re.getEntity()));
				resp.setReturnCode(json_object.getString("return"));
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 修改宝宝
	public interface_app_add_and_set_my_baby_resp ios_interface_app_modify_baby(interface_app_add_and_set_my_baby_req req) {
		interface_app_add_and_set_my_baby_resp resp = null;
		try {
			resp = new interface_app_add_and_set_my_baby_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_modify_baby);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("baby_name", req.getBaby_name());
			params.put("user_id", req.getUser_id());
			params.put("baby_id", req.getBaby_id());
			params.put("sex", req.getSex());
			params.put("birthday", req.getBirthday());
			params.put("age", req.getAge());
			System.out.println("baby_name-->"+req.getBaby_name());
			System.out.println("user_id-->"+req.getUser_id());
			System.out.println("baby_id-->"+req.getBaby_id());
			System.out.println("sex-->"+req.getSex());
			System.out.println("birthday-->"+req.getBirthday());
			System.out.println("age-->"+req.getAge());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if(entry.getValue()!=null){
						StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
						entity.addPart(entry.getKey(), par);
					}
				}
			}
			if(req.getUpload_file()!=null && req.getUpload_file().length()>0){
				// 添加文件参数信息
				File file = null;
				file = new File(req.getUpload_file());
				ContentBody content = new FileBody(file);
				entity.addPart("upload_file", content);
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			System.out.println("result_code-->"+result_code);
			if (result_code == HttpStatus.SC_OK) {
				json_object = new JSONObject(EntityUtils.toString(re.getEntity()));
				String return_code = json_object.getString("return");
				System.out.println("return_code-->"+return_code);
				resp.setReturnCode(return_code);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 删除宝宝
	 * 
	 * @param req
	 * @return
	 */
	public interface_app_delete_baby_resp interface_app_delete_baby(interface_app_delete_baby_req req) {
		interface_app_delete_baby_resp resp = null;
		try {
			resp = new interface_app_delete_baby_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_del_baby);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("baby_id", req.getBaby_id());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				String str_result = EntityUtils.toString(re.getEntity());
				System.out.println("result:" + str_result);
				json_object = new JSONObject(str_result);
				String return_code = json_object.getString("return");
				System.out.println("result_code:" + result_code);
				resp.setReturnCode(return_code);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 获取我关注的宝贝
	public interface_app_get_my_care_baby_resp ios_interface_app_get_my_care_baby(interface_app_get_my_care_baby_req req) {
		interface_app_get_my_care_baby_resp resp = null;
		try {
			resp = new interface_app_get_my_care_baby_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_my_care_baby);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				List<table_my_center_my_care_baby> baby_list = new ArrayList<table_my_center_my_care_baby>();
				if (json_array.length() > 0) {
					for (int i = 0; i < json_array.length(); i++) {
						table_my_center_my_care_baby baby = new table_my_center_my_care_baby();
						json_array_item = json_array.getJSONObject(i);
						baby.setBaby_id(json_array_item.getString("BABY_ID"));
						baby.setUser_id(json_array_item.getString("USER_ID"));
						baby.setBaby_name(json_array_item.getString("BABY_NAME"));

						if (json_array_item.has("BABY_SEX")) {
							baby.setBaby_sex(json_array_item.getString("BABY_SEX"));
						}
						if (json_array_item.has("BIRTHDAY")) {
							baby.setBirthday(json_array_item.getString("BIRTHDAY"));
						}
						if (json_array_item.has("BABY_PIC")) {
							baby.setBaby_pic(json_array_item.getString("BABY_PIC"));
						}

						baby_list.add(baby);
					}
				}
				resp.setList(baby_list);
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 获取宝贝亲友列表
	public interface_app_get_baby_relations_resp ios_interface_app_get_baby_relations(interface_app_get_baby_relations_req req) {
		interface_app_get_baby_relations_resp resp = null;
		try {
			resp = new interface_app_get_baby_relations_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("baby_id", req.getBaby_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_baby_relations);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("result_code-->"+result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				List<table_my_center_baby_relations> baby_list = new ArrayList<table_my_center_baby_relations>();
				if (json_array.length() > 0) {
					for (int i = 0; i < json_array.length(); i++) {
						table_my_center_baby_relations baby = new table_my_center_baby_relations();
						json_array_item = json_array.getJSONObject(i);
						baby.setUser_id(json_array_item.getString("USER_ID"));
						baby.setUser_nick_name(json_array_item.getString("USER_NICK_NAME"));

						if (json_array_item.has("USER_PIC")) {
							baby.setUser_pic(json_array_item.getString("USER_PIC"));
						}
						if (json_array_item.has("RELATION")) {
							baby.setRelation(json_array_item.getString("RELATION"));
						}
						if (json_array_item.has("PERMISSIONS")) {
							baby.setPermission(json_array_item.getString("PERMISSIONS"));
						}
						baby_list.add(baby);
					}
				}
				resp.setList(baby_list);
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 生成邀请码
	public interface_app_get_invite_code_resp ios_interface_app_get_invite_code(interface_app_get_invite_code_req req) {
		interface_app_get_invite_code_resp resp = null;
		try {
			resp = new interface_app_get_invite_code_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_get_invite_code);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("baby_id", req.getBaby_id());
			params.put("permissions", req.getPermissions());
			params.put("relation", req.getRelation());
			System.out.println("baby_id-->"+req.getBaby_id());
			System.out.println("permissions-->"+req.getPermissions());
			System.out.println("relation-->"+req.getRelation());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				String return_string = EntityUtils.toString(re.getEntity());
				
				System.out.println("return_string-->"+return_string);
				
				json_object = new JSONObject(return_string);
				String invite_string = "";
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				if(json_array.length()>0){
					invite_string = json_array.get(0).toString();
					System.out.println("invite_string-->"+invite_string);
				}
				resp.setInviteCode(invite_string);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 输入邀请码添加宝宝
	public interface_app_invite_baby_resp ios_interface_app_invite_baby_code(interface_app_invite_baby_req req) {
		interface_app_invite_baby_resp resp = null;
		try {
			resp = new interface_app_invite_baby_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_invite_baby);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			params.put("invitation_code", req.getInvitation_code());
			System.out.println("user_id-->"+req.getUser_id());
			System.out.println("invitation_code-->"+req.getInvitation_code());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				String return_string = EntityUtils.toString(re.getEntity());
				
				System.out.println("return_string-->"+return_string);
				
				json_object = new JSONObject(return_string);
				String return_code = "";
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				if(json_array.length()>0){
					return_code = json_array.get(0).toString();
					System.out.println("return_code-->"+return_code);
				}
				resp.setReturnCode(return_code);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//查询我的积分
	public interface_app_get_my_points_resp ios_interface_app_get_my_points(interface_app_get_my_points_req req) {
		interface_app_get_my_points_resp resp = null;
		try {
			resp = new interface_app_get_my_points_resp();
			resp.setIndex(req.getIndex());
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			params.put("index", req.getIndex());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_integral_record);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("result_code-->"+result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				List<table_my_center_my_points> record_list = new ArrayList<table_my_center_my_points>();
				if (json_array.length() > 0) {
					for (int i = 0; i < json_array.length(); i++) {
						table_my_center_my_points my_record = new table_my_center_my_points();
						json_array_item = json_array.getJSONObject(i);
						my_record.setUser_id(json_array_item.getString("USER_ID"));
						if (json_array_item.has("CONSUME_STYLE")) {
							my_record.setConsume_style(json_array_item.getString("CONSUME_STYLE"));
						}
						if (json_array_item.has("INTEGRAL_COUNT")) {
							my_record.setIntegral_count(json_array_item.getString("INTEGRAL_COUNT"));
						}
						if (json_array_item.has("TIME")) {
							my_record.setTime(json_array_item.getString("TIME"));
						}
						record_list.add(my_record);
					}
				}
				resp.setList(record_list);
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//查询积分规则
	public interface_app_get_record_rules_resp ios_interface_app_get_record_rules(interface_app_get_record_rules_req req) {
		interface_app_get_record_rules_resp resp = null;
		try {
			resp = new interface_app_get_record_rules_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
//			params = new HashMap<String, String>();
//			params.put("user_id", req.getUser_id());
//			params.put("index", req.getIndex());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_integral_rules);
			http_entity = new MultipartEntity();
//			if (params != null && !params.isEmpty()) {
//				for (Map.Entry<String, String> entry : params.entrySet()) {
//					StringBody par = new StringBody(entry.getValue());
//					http_entity.addPart(entry.getKey(), par);
//				}
//			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("result_code-->"+result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				List<table_my_center_record_rules> rule_list = new ArrayList<table_my_center_record_rules>();
				if (json_array.length() > 0) {
					for (int i = 0; i < json_array.length(); i++) {
						table_my_center_record_rules my_rule = new table_my_center_record_rules();
						json_array_item = json_array.getJSONObject(i);
						my_rule.setRule_id(json_array_item.getString("RULE_ID"));
						my_rule.setRule_code(json_array_item.getString("RULE_CODE"));
						if (json_array_item.has("INTEGRAL_REASON")) {
							my_rule.setIntegral_reason(json_array_item.getString("INTEGRAL_REASON"));
						}
						if (json_array_item.has("INTEGRAL_COUNT")) {
							my_rule.setIntegral_count(json_array_item.getString("INTEGRAL_COUNT"));
						}
						if (json_array_item.has("COUNT_LIMIT")) {
							my_rule.setCount_limit(json_array_item.getString("COUNT_LIMIT"));
						}
						if (json_array_item.has("REMARK")) {
							my_rule.setRemark(json_array_item.getString("REMARK"));
						}
						rule_list.add(my_rule);
					}
				}
				resp.setList(rule_list);
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//查询积分规则
	public interface_app_get_total_point_resp ios_interface_app_get_total_points(interface_app_get_total_point_req req) {
		interface_app_get_total_point_resp resp = null;
		try {
			resp = new interface_app_get_total_point_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
				params = new HashMap<String, String>();
				params.put("user_id", req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_total_points);
			http_entity = new MultipartEntity();
				if (params != null && !params.isEmpty()) {
					for (Map.Entry<String, String> entry : params.entrySet()) {
						StringBody par = new StringBody(entry.getValue());
						http_entity.addPart(entry.getKey(), par);
					}
				}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("result_code-->"+result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				if (json_array.length() > 0) {
					for (int i = 0; i < json_array.length(); i++) {
						json_array_item = json_array.getJSONObject(i);
						if (json_array_item.has("ID")) {
							resp.setId(json_array_item.getString("ID"));
						}
						if (json_array_item.has("USER_GRADE")) {
							resp.setUser_grade(json_array_item.getString("USER_GRADE"));
						}
						if (json_array_item.has("USER_ID")) {
							resp.setUser_id(json_array_item.getString("USER_ID"));
						}
						if (json_array_item.has("TOTAL_INTEGRAL")) {
							resp.setTotal_integral(json_array_item.getString("TOTAL_INTEGRAL"));
						}
					}
				}
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//查询我的收藏
	public interface_app_get_my_favorite_resp ios_interface_app_get_my_favorite(interface_app_get_my_favorite_req req) {
		interface_app_get_my_favorite_resp resp = null;
		try {
			resp = new interface_app_get_my_favorite_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("user_id", req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_my_favorite);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("result_code-->"+result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
//				System.out.println(result_content);
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				List<table_my_center_favorite> record_list = new ArrayList<table_my_center_favorite>();
				if (json_array.length() > 0) {
					for (int i = 0; i < json_array.length(); i++) {
						table_my_center_favorite my_favorite = new table_my_center_favorite();
						json_array_item = json_array.getJSONObject(i);
						my_favorite.setId(json_array_item.getString("ID"));
						if (json_array_item.has("CONTENT_ID")) {
							my_favorite.setContent_id(json_array_item.getString("CONTENT_ID"));
						}
						if (json_array_item.has("PIC_NAME")) {
							my_favorite.setPic_name(json_array_item.getString("PIC_NAME"));
						}
						if (json_array_item.has("PUBLIST_TIME")) {
							my_favorite.setPublish_time(json_array_item.getString("PUBLIST_TIME"));
						}
						if (json_array_item.has("SOURCE")) {
							my_favorite.setSource(json_array_item.getString("SOURCE"));
						}
						if (json_array_item.has("TITLE")) {
							my_favorite.setTitle(json_array_item.getString("TITLE"));
						}
						record_list.add(my_favorite);
					}
				}
				resp.setList(record_list);
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 删除我的收藏
	 * 
	 * @param req
	 * @return
	 */
	public interface_app_delete_my_favorite_resp interface_app_delete_my_favorite(interface_app_delete_my_favorite_req req) {
		interface_app_delete_my_favorite_resp resp = null;
		try {
			resp = new interface_app_delete_my_favorite_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_del_my_favorite);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", req.getId());
			params.put("user_id", req.getUser_id());
			params.put("content_id", req.getContent_id());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				String str_result = EntityUtils.toString(re.getEntity());
				System.out.println("result:" + str_result);
				json_object = new JSONObject(str_result);
				String return_code = json_object.getString("return");
				System.out.println("result_code:" + result_code);
				resp.setReturnCode(return_code);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ******************健康育儿*******************************
	 */
	// 分页获取健康育儿资讯
	public interface_app_get_health_information_resp ios_interface_app_get_health_infomation_by_page(interface_app_get_health_information_req req) {
		interface_app_get_health_information_resp resp = null;
		try {
			System.out.println("network---------------interface_get_health_information_by_page-------");
			resp = new interface_app_get_health_information_resp();
			resp.setPage_number(req.getShow_page());
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("action", req.getAction());
			params.put("showPage", req.getShow_page());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_health_information_by_page);
			System.out.println("network_interface_paths.interface_get_health_information_by_page--->" + network_interface_paths.interface_get_health_information_by_page);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					System.out.println("||" + entry.getKey() + ":" + entry.getValue() + "||");
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("network ------interface_get_health_information_by_page---------- result_code=" + result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());

//				System.out.println(result_content);

				try {
					json_object = new JSONObject(result_content);
					
					if (json_object.has("return")) {
						String return_string = json_object.getString("return");
						System.out.println(req.getAction()+"--return_string-->"+return_string);
						json_array = new JSONArray(return_string);
						System.out.println("json——array--size-->" + json_array.length());
						
						if(json_array.length() > 0 && req.getAction()!=null && req.getAction().equals("info")){// 普通资讯
							System.out.println("---------------------------普通资讯-------------------------");
							List<table_health_information> info_list = new ArrayList<table_health_information>();
							for (int i = 0; i < json_array.length(); i++) {
								table_health_information info = new table_health_information();
								json_array_item = json_array.getJSONObject(i);
								info.setContent_id(json_array_item.getString("CONTENT_ID"));
								if (json_array_item.has("CONTENT")) {
									info.setContent(json_array_item.getString("CONTENT"));
								}
								if (json_array_item.has("COLLECT_COUNT")) {
									info.setCollect_count(json_array_item.getString("COLLECT_COUNT"));
								}
								if (json_array_item.has("SHARE_COUNT")) {
									info.setShare_count(json_array_item.getString("SHARE_COUNT"));
								}
								if (json_array_item.has("PUBLISH_TIME")) {
									info.setPublish_time(json_array_item.getString("PUBLISH_TIME"));
								}
								if (json_array_item.has("SOURCE")) {
									info.setSource(json_array_item.getString("SOURCE"));
								}
								if (json_array_item.has("TITLE")) {
									info.setTitle(json_array_item.getString("TITLE"));
								}
								if (json_array_item.has("SPIC_NAME")) {
									info.setS_pic_name(json_array_item.getString("SPIC_NAME"));
								}
								if (json_array_item.has("PIC_NAME")) {
									info.setPic_name(json_array_item.getString("PIC_NAME"));
								}
								info_list.add(info);
							}
							resp.setList(info_list);
						}else if(json_array.length() > 0 && req.getAction()!=null && req.getAction().equals("top")){// 头条
							System.out.println("---------------------------头条-------------------------");
							List<table_health_information_top_pic_info> top_list = new ArrayList<table_health_information_top_pic_info>();
							for (int i = 0; i < json_array.length(); i++) {
								table_health_information_top_pic_info top_info = new table_health_information_top_pic_info();
								json_array_item = json_array.getJSONObject(i);
								if (json_array_item.has("TOP_ID")) {
									top_info.setTop_id(json_array_item.getString("TOP_ID"));
								}
								if (json_array_item.has("SPIC_NAME")) {
									top_info.setS_pic_name(json_array_item.getString("SPIC_NAME"));
								}
								if (json_array_item.has("PIC_NAME")) {
									top_info.setPic_name(json_array_item.getString("PIC_NAME"));
								}
								if (json_array_item.has("TITLE")) {
									top_info.setTitle(json_array_item.getString("TITLE"));
								}
								top_list.add(top_info);
							}
							resp.setTop_list(top_list);
						}
					}
					resp.setAction(req.getAction());
					resp.setResult_code(result_code);
					 
				} catch (Exception ex) {
					ex.printStackTrace();
					resp.setResult_code(result_code);
					resp.setReturn_code("json解析出错了");
				}
				return resp;
			} else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 查询健康资讯详情
	public interface_app_get_health_information_detail_resp ios_interface_app_get_health_infomation_detail(interface_app_get_health_information_detail_req req) {
		interface_app_get_health_information_detail_resp resp = null;
		try {
			System.out.println("network---------------ios_interface_app_get_health_infomation_detail-------");
			resp = new interface_app_get_health_information_detail_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("id", req.getId());
			params.put("user_id", req.getUser_id());
			System.out.println("id-->"+req.getId());
			System.out.println("user_id-->"+req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_get_health_information_detail);
			System.out.println("network_interface_paths.interface_get_health_information_detail--->" + network_interface_paths.interface_get_health_information_detail);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					System.out.println("||" + entry.getKey() + ":" + entry.getValue() + "||");
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("network ------ios_interface_app_get_health_infomation_detail---------- result_code=" + result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
//				System.out.println(result_content);
				try {
					json_object = new JSONObject(result_content);
					if (json_object.has("return")) {
						json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
						json_array = json_array == null ? new JSONArray() : json_array;
						json_array_item = json_array.getJSONObject(0);
						resp.setContent_id(json_array_item.getString("CONTENT_ID"));
						if (json_array_item.has("CONTENT")) {
							resp.setContent(json_array_item.getString("CONTENT"));
						}
						if (json_array_item.has("COLLECT_COUNT")) {
							resp.setCollect_count(json_array_item.getString("COLLECT_COUNT"));
						}
						if (json_array_item.has("SHARE_COUNT")) {
							resp.setShare_count(json_array_item.getString("SHARE_COUNT"));
						}
						if (json_array_item.has("PUBLISH_TIME")) {
							resp.setPublish_time(json_array_item.getString("PUBLISH_TIME"));
						}
						if (json_array_item.has("SOURCE")) {
							resp.setSource(json_array_item.getString("SOURCE"));
						}
						if (json_array_item.has("TITLE")) {
							resp.setTitle(json_array_item.getString("TITLE"));
						}
						if (json_array_item.has("SPIC_NAME")) {
							resp.setS_pic_name(json_array_item.getString("SPIC_NAME"));
						}
						if (json_array_item.has("PIC_NAME")) {
							resp.setPic_name(json_array_item.getString("PIC_NAME"));
						}
						if (json_array_item.has("IS_COLLECT")) {
							resp.setIs_collect(json_array_item.getString("IS_COLLECT"));
						}
						resp.setResult_code(result_code);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					resp.setResult_code(result_code);
					resp.setReturn_code("json解析出错了");
				}
				return resp;
			} else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// 搜索健康资讯
	public interface_app_search_health_information_resp ios_interface_app_search_health_info(interface_app_search_health_information_req req) {
		interface_app_search_health_information_resp resp = null;
		try {
			resp = new interface_app_search_health_information_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("condition", req.getCondition());
			System.out.println("condition-->"+req.getCondition());
			// --
			http_post = new HttpPost(network_interface_paths.interface_search_health_information);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
//				System.out.println(result_content);
				json_object = new JSONObject(result_content);
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				json_array = json_array == null ? new JSONArray() : json_array;
				List<table_health_information> health_list = new ArrayList<table_health_information>();
				if (json_array.length() > 0) {
					for (int i = 0; i < json_array.length(); i++) {
						table_health_information health = new table_health_information();
						json_array_item = json_array.getJSONObject(i);
						health.setContent_id(json_array_item.getString("CONTENT_ID"));
						if(json_array_item.has("TITLE")){
							health.setTitle(json_array_item.getString("TITLE"));
						}
						if(json_array_item.has("PUBLISH_TIME")){
							health.setPublish_time(json_array_item.getString("PUBLISH_TIME"));
						}
						health_list.add(health);
					}
				}
				resp.setList(health_list);
				return resp;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 收藏健康资讯
	public interface_app_collect_health_information_resp ios_interface_app_collect_health_infomation(interface_app_collect_health_information_req req) {
		interface_app_collect_health_information_resp resp = null;
		try {
			System.out.println("network---------------interface_app_collect_health_information_resp-------");
			resp = new interface_app_collect_health_information_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			params = new HashMap<String, String>();
			params.put("content_id", req.getContent_id());
			params.put("user_id", req.getUser_id());
			System.out.println("content_id-->"+req.getContent_id());
			System.out.println("user_id-->"+req.getUser_id());
			// --
			http_post = new HttpPost(network_interface_paths.interface_collect_health_information);
			System.out.println("network_interface_paths.interface_collect_health_information--->" + network_interface_paths.interface_collect_health_information);
			http_entity = new MultipartEntity();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue());
					System.out.println("||" + entry.getKey() + ":" + entry.getValue() + "||");
					http_entity.addPart(entry.getKey(), par);
				}
			}
			http_post.setEntity(http_entity);
			// --
			response = client.execute(http_post);
			// --
			result_code = response.getStatusLine().getStatusCode();
			System.out.println("network ------ios_interface_app_collect_health_infomation---------- result_code=" + result_code);
			if (result_code == HttpStatus.SC_OK) {
				result_content = EntityUtils.toString(response.getEntity());
//				System.out.println(result_content);
				try {
					json_object = new JSONObject(result_content);
					if (json_object.has("return")) {
						json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
						json_array = json_array == null ? new JSONArray() : json_array;
						String return_code = "";
						if(json_array.length()>0){
							return_code = json_array.get(0).toString();
							System.out.println("return_code-->"+return_code);
						}
						resp.setReturn_code(return_code);
					}
					resp.setResult_code(result_code);
				} catch (Exception ex) {
					ex.printStackTrace();
					resp.setResult_code(result_code);
					resp.setReturn_code("json解析出错了");
				}
				return resp;
			} else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * *************  用户注册  *********************
	 */
	// 验证手机号和验证码
	public interface_app_check_verify_code_resp ios_interface_app_check_verify_code(interface_app_check_verify_code_req req) {
		interface_app_check_verify_code_resp resp = null;
		try {
			resp = new interface_app_check_verify_code_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_check_verify_code);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("phone", req.getPhone());
			params.put("v_code", req.getV_code());
			System.out.println("phone-->"+req.getPhone());
			System.out.println("v_code-->"+req.getV_code());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				String return_string = EntityUtils.toString(re.getEntity());
				
				System.out.println("return_string-->"+return_string);
				
				json_object = new JSONObject(return_string);
				String invite_string = "";
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				if(json_array.length()>0){
					invite_string = json_array.get(0).toString();
					System.out.println("invite_string-->"+invite_string);
				}
				resp.setReturnCode(invite_string);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// 验证通过后添加用户
	public interface_app_check_and_regist_user_resp ios_interface_app_check_and_regist_user(interface_app_check_and_regist_user_req req) {
		interface_app_check_and_regist_user_resp resp = null;
		try {
			resp = new interface_app_check_and_regist_user_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_check_and_register_user);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("phone", req.getPhone());
			params.put("v_code", req.getV_code());
			params.put("nick_name", req.getNick_name());
			params.put("password", req.getPassword());
			System.out.println("phone-->"+req.getPhone());
			System.out.println("v_code-->"+req.getV_code());
			System.out.println("nick_name-->"+req.getNick_name());
			System.out.println("password-->"+req.getPassword());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				String return_string = EntityUtils.toString(re.getEntity());
				
				System.out.println("return_string-->"+return_string);
				
				json_object = new JSONObject(return_string);
				String invite_string = "";
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				if(json_array.length()>0){
					invite_string = json_array.get(0).toString();
					System.out.println("invite_string-->"+invite_string);
				}
				resp.setReturnCode(invite_string);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// 验证通过后重置密码
	public interface_app_reset_password_resp ios_interface_app_reset_password(interface_app_reset_password_req req) {
		interface_app_reset_password_resp resp = null;
		try {
			resp = new interface_app_reset_password_resp();
			// --
			http_params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(http_params, timeoutSocket);
			HttpConnectionParams.setSoTimeout(http_params, timeoutConnection);
			client = new DefaultHttpClient(http_params);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// --
			HttpPost post = new HttpPost(network_interface_paths.interface_reset_user_password);
			MultipartEntity entity = new MultipartEntity();
			// 添加文字参数信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("phone", req.getPhone());
			params.put("v_code", req.getV_code());
			params.put("password", req.getPassword());
			System.out.println("phone-->"+req.getPhone());
			System.out.println("v_code-->"+req.getV_code());
			System.out.println("password-->"+req.getPassword());
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBody par = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
					entity.addPart(entry.getKey(), par);
				}
			}
			// --
			post.setEntity(entity);
			org.apache.http.HttpResponse re = client.execute(post);
			int result_code = re.getStatusLine().getStatusCode();
			if (result_code == HttpStatus.SC_OK) {
				String return_string = EntityUtils.toString(re.getEntity());
				System.out.println("return_string-->"+return_string);
				json_object = new JSONObject(return_string);
				String invite_string = "";
				json_array = new JSONArray(JSONUtils.getString(json_object, "return", null));
				if(json_array.length()>0){
					invite_string = json_array.get(0).toString();
					System.out.println("invite_string-->"+invite_string);
				}
				resp.setReturnCode(invite_string);
			} else {
				resp.setReturnCode("error");
			}
			resp.setResultCode(String.valueOf(result_code));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
