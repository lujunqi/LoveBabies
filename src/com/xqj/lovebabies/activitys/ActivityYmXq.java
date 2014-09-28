package com.xqj.lovebabies.activitys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_Json;
import com.xqj.lovebabies.commons.utils_network_tools;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.fragments.FragmentYmtx;
import com.xqj.lovebabies.listeners.listener_activity_main_on_click;
import com.xqj.lovebabies.widgets.TopActionBar;

public class ActivityYmXq extends Activity {
	private TextView vaccine_knowledge = null;
	private TextView attention = null;
	private TextView is_vaccinated = null;
	private TextView vaccine_send_time = null;

	private TextView disease_introduction = null;

	private static String URLPATH = "http://"
			+ global_contants.application_server_ip
			+ ":8080/lovebaby/services/vaccine/getVaccineById";
	private static String SETVACCINATED = "http://"
			+ global_contants.application_server_ip
			+ ":8080/lovebaby/services/vaccine/setVaccinated";
	

	
	private Map<String, Object> infoMap = new HashMap<String, Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_ymxq);
		vaccine_knowledge = (TextView) this
				.findViewById(R.id.vaccine_knowledge);
		attention = (TextView) this.findViewById(R.id.attention);
		is_vaccinated = (TextView) this.findViewById(R.id.is_vaccinated);
		vaccine_send_time = (TextView) this
				.findViewById(R.id.vaccine_send_time);

		disease_introduction = (TextView) this
				.findViewById(R.id.disease_introduction);
		TopActionBar main_action_bar = (TopActionBar) findViewById(R.id.activity_main_actionbar_topbar);
		main_action_bar.getCmd_textview_title_name().setText("疫苗知识");
		main_action_bar.getCmd_imagebutton_menu().setOnClickListener(
				new listener_activity_main_on_click(handler));
		Intent intent = this.getIntent();
		String id = intent.getStringExtra("id");
		new GetVaccineTask().execute(id);
		is_vaccinated.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tmp = is_vaccinated.getText() + "";
				if ("已接种".equals(tmp)) {
					is_vaccinated.setText("未接种");
				} else {
					is_vaccinated.setText("已接种");
				}
			}

		});
		vaccine_send_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String time = (String) vaccine_send_time.getText();
				Date d = new Date();
				int year = d.getYear();
				int month = d.getMonth();
				int day = d.getDate();
				try {
					year = Integer.parseInt(time.substring(5, 9));
					month = Integer.parseInt(time.substring(10, 12)) - 1;
					day = Integer.parseInt(time.substring(13, 15));
				} catch (Exception e) {

				}
				Log.d("line68", year + "==" + month);
				DatePickerDialog dialog = new DatePickerDialog(
						ActivityYmXq.this, dateListener, year, month, day);
				dialog.show();
			}
		});
	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String m = (monthOfYear + 1) + "";
			if (monthOfYear < 9) {
				m = "0" + (monthOfYear + 1);
			}
			String d = dayOfMonth + "";
			if (dayOfMonth < 10) {
				d = "0" + dayOfMonth;
			}

			vaccine_send_time.setText("接种时间：" + year + "-" + m + "-" + d);
		}

	};
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == message_what_values.widget_top_action_bar_button_menu_click) {
				String time = vaccine_send_time.getText() + "";
				try {
					time = time.substring(5);
				} catch (Exception e) {
					time = "2014-09-15";
				}
				String vaccine_id = infoMap.get("VACCINE_ID") + "";
				SharedPreferences sp = ActivityYmXq.this.getSharedPreferences(
						"ym", Context.MODE_PRIVATE);
				Map<String, ?> m = sp.getAll();

				String baby_id = "" + m.get("BABY_ID");

				String vaccinated = "0";
				String tmp = is_vaccinated.getText() + "";
				if ("已接种".equals(tmp)) {
					vaccinated = "1";
				} else {
					vaccinated = "0";
				}

				new SaveTask().execute(time, vaccine_id, baby_id, vaccinated);

				FragmentYmtx frag = (FragmentYmtx) ActivityMain.fragment_manager
						.findFragmentById(R.id.activity_main_linearlayout_container);
				frag.init(baby_id);
				Log.d("line158", "==" + frag.getClass());
				ActivityYmXq.this.finish();
			}
		};
	};

	private class SaveTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			Map<String, String> param = new HashMap<String, String>();
			try {

				param.put("time", params[0]);
				param.put("vaccine_id", params[1]);
				param.put("baby_id", params[2]);
				param.put("vaccinated", params[3]);
				Log.d("line165", "===" + param);
				String json = utils_network_tools.doGet(SETVACCINATED, param);
				Pattern p_html = Pattern.compile("<[^>]+>",
						Pattern.CASE_INSENSITIVE);
				Matcher m_html = p_html.matcher(json);
				json = m_html.replaceAll("");
				return json;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String param) {
			try {
				Toast.makeText(ActivityYmXq.this, param, Toast.LENGTH_SHORT)
						.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private class GetVaccineTask extends
			AsyncTask<String, Void, List<Map<String, Object>>> {
		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			Map<String, String> param = new HashMap<String, String>();
			try {
				param.put("id", params[0]);
				String json = utils_network_tools.doGet(URLPATH, param);
				Pattern p_html = Pattern.compile("<[^>]+>",
						Pattern.CASE_INSENSITIVE);
				Matcher m_html = p_html.matcher(json);
				json = m_html.replaceAll("");
				List<Map<String, Object>> list = utils_Json.json2List(json);
				return list;
			} catch (Exception w) {
				Log.d("line63", "error", w);
				return new ArrayList<Map<String, Object>>();
			}
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> list) {
			try {
				if (!list.isEmpty()) {
					Map<String, Object> map = list.get(0);
					vaccine_knowledge
							.setText("" + map.get("VACCINE_KNOWLEDGE"));
					attention.setText("" + map.get("ATTENTION"));
					disease_introduction.setText(""
							+ map.get("DISEASE_INTRODUCTION"));
					vaccine_send_time.setText("接种时间："
							+ map.get("VACCINE_SEND_TIME"));
					if ("1".equals(map.get("IS_VACCINATED"))) {
						is_vaccinated.setText("" + "已接种");
					} else {
						is_vaccinated.setText("" + "未接种");
					}
					infoMap = map;
				}
			} catch (Exception w) {
				Log.d("line81", "error", w);
			}

		}
	}
}
