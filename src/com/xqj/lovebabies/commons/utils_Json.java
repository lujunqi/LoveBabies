package com.xqj.lovebabies.commons;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class utils_Json {
	public static Map<String, Object> json2Map(String jsonString) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(jsonString);
			@SuppressWarnings("unchecked")
			Iterator<String> it = json.keys();
			while (it.hasNext()) {
				String key = it.next();
				result.put(key, json.get(key));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String map2Json(Map<String, Object> m) {
		String reuslt = "";
		JSONObject json = new JSONObject();
		try {
			for (Map.Entry<String, Object> en : m.entrySet()) {
				json.put(en.getKey(), en.getValue());
			}

			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return reuslt;
	}

	public static String list2Json(List<Map<String, Object>> list) {
		JSONArray json = new JSONArray();
		try {

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> m = list.get(i);
				JSONObject obj = new JSONObject();
				for (Map.Entry<String, Object> en : m.entrySet()) {
					obj.put(en.getKey(), en.getValue());
				}
				json.put(obj);
			}
			return new String(json.toString().getBytes(),"GBK");
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static List<Map<String, Object>> json2List(String jsonString) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			JSONArray json = new JSONArray(jsonString);
			for (int i = 0; i < json.length(); i++) {
				
				Map<String, Object> map = new HashMap<String, Object>();

				JSONObject jsonObj = json.getJSONObject(i);
				@SuppressWarnings("unchecked")
				Iterator<String> it = jsonObj.keys();
				while (it.hasNext()) {
					String key = it.next();
					map.put(key, jsonObj.get(key));
				}
				result.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static List<Map<String, String>> json2ListEx(String jsonString) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		try {
			JSONArray json = new JSONArray(jsonString);
			for (int i = 0; i < json.length(); i++) {
				
				Map<String, String> map = new HashMap<String, String>();

				JSONObject jsonObj = json.getJSONObject(i);
				@SuppressWarnings("unchecked")
				Iterator<String> it = jsonObj.keys();
				while (it.hasNext()) {
					String key = it.next();
					map.put(key, ""+jsonObj.get(key));
				}
				result.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
