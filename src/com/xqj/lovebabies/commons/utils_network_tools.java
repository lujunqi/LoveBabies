package com.xqj.lovebabies.commons;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;
import cn.trinea.android.common.entity.*;
import cn.trinea.android.common.util.*;

import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_interaction_notice;
import com.xqj.lovebabies.structures.*;

public class utils_network_tools {
	public static String doGet(String uri, Map<String, String> map) {
		String result = null;
		HttpClient httpClient = new DefaultHttpClient();
		StringBuilder sBuilder = new StringBuilder(uri);
		sBuilder.append("?");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sBuilder.append(entry.getKey()).append("=")
					.append(Uri.encode(entry.getValue()));
			sBuilder.append("&");
		}
		sBuilder.deleteCharAt(sBuilder.length() - 1);
		String str = sBuilder.toString();
		HttpGet httpGet = new HttpGet(str);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				result = result.replace("\"[", "[").replace("]\"", "]")
						.replace("\\\"", "\"").replace("\\\\\"", "\\\"");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
}
