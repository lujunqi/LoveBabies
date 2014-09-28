package com.xqj.lovebabies.fragments;

/*
 * 健康档案
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.adapters.adaper_fragment_health_doc_listview;
import com.xqj.lovebabies.commons.utils_Json;
import com.xqj.lovebabies.commons.utils_network_tools;
import com.xqj.lovebabies.contants.global_contants;
public class FragmentHealthDoc extends Fragment {
	private View main_view = null;
	private adaper_fragment_health_doc_listview adapter;
	private static final String URL_PATH = "http://"
			+ global_contants.application_server_ip
			+ ":8080/lovebaby/services/morning_check/getCheckRecordByUser";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			ImageView blue = ActivityMain.main_action_bar
					.getCmd_imagebutton_more();
			blue.setVisibility(View.GONE);

			ActivityMain.main_action_bar.getCmd_textview_title_name().setText(
					"健康档案");
			initView(inflater);
		} catch (Exception e) {
		}
		return main_view;
	}

	private void initView(LayoutInflater inflater) {
		main_view = inflater.inflate(R.layout.fragment_health_doc, null);

		new GetDataTask().execute();

	}

	private class GetDataTask extends
			AsyncTask<Void, Void, List<Map<String, Object>>> {
		private List<Map<String, Object>> list;

		public GetDataTask() {
		}

		@Override
		protected List<Map<String, Object>> doInBackground(Void... params) {
			Map<String, String> param = new HashMap<String, String>();
			String user_id = PreferencesUtils.getString(main_view.getContext(),
					"user_id");
			param.put("userId", user_id);
			String json = utils_network_tools.doGet(URL_PATH, param);
			json = json
					.replaceAll(
							"<ns:getCheckRecordByUserResponse xmlns:ns=\"http://control.xqj.com\"><ns:return>",
							"");
			json = json.replaceAll(
					"</ns:return></ns:getCheckRecordByUserResponse>", "");

			list = utils_Json.json2List(json);
			Log.d("lone78", list + "==");
			// babies = list;
			return list;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> list) {
			// babies.clear();
			// babies.addAll(list);
			Log.d("lone88", list + "==");
			if (list.isEmpty()) {
				RelativeLayout no_health_doc_layout = (RelativeLayout) main_view
						.findViewById(R.id.no_health_doc_layout);
				no_health_doc_layout.setVisibility(View.VISIBLE);
				ListView health_doc_layout = (ListView) main_view
						.findViewById(R.id.health_doc_layout);
				health_doc_layout.setVisibility(View.GONE);
				TextView no_health_doc = (TextView) main_view
						.findViewById(R.id.no_health_doc);
				String info = "<p>您的宝宝<font color=\"#247DB7\">当前没有健康档案的数据哦！</font></p>"
						+ "<p>可能的原因是：</p>"
						+ "<p>1、您的宝宝所在的幼儿园还没有使用我们的晨检系统</p>"
						+ "<p>2、您的宝宝所在的幼儿园还没有开始给您的宝宝录入晨检数据</p>";
				no_health_doc.setText(Html.fromHtml(info));
			} else {
				adapter = new adaper_fragment_health_doc_listview(list,
						main_view.getContext());
				RelativeLayout no_health_doc_layout = (RelativeLayout) main_view
						.findViewById(R.id.no_health_doc_layout);
				no_health_doc_layout.setVisibility(View.GONE);
				ListView health_doc_layout = (ListView) main_view
						.findViewById(R.id.health_doc_layout);
				health_doc_layout.setAdapter(adapter);
				health_doc_layout.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
			}

			super.onPostExecute(list);
		}
	}
}
