package com.xqj.lovebabies.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.activitys.ActivityYmXq;
import com.xqj.lovebabies.commons.utils_Json;
import com.xqj.lovebabies.commons.utils_network_tools;
import com.xqj.lovebabies.commons.utils_picture_tools;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.databases.dbs_ewen_data;

public class FragmentYmtx extends Fragment {
	private View main_view = null;
	private ImageView myBabaies = null;
	private ListView mytx_list = null;
	private static final String URL_getMyBaby = "http://"
			+ global_contants.application_server_ip
			+ ":8080/lovebaby/services/baby/getMyBaby";
	private String URL_PIC = "http://" + global_contants.application_server_ip
			+ ":8080/lovebaby/";
	private String VACC_PIC = "http://" + global_contants.application_server_ip
			+ ":8080/lovebaby/services/vaccine/getVaccineByBaby";
	private static String GETVACCINEBYMONTH = "http://"
			+ global_contants.application_server_ip
			+ ":8080/lovebaby/services/vaccine/getVaccineByMonth";
	private static String SETVACCINEBYARRAY = "http://"
			+ global_contants.application_server_ip
			+ ":8080/lovebaby/services/vaccine/setVaccineByArray";

	private TextView baby_name = null;
	private TextView modify_time = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ActivityMain.main_action_bar.getCmd_textview_title_name().setText(
				"疫苗提醒");

		main_view = inflater.inflate(R.layout.fragment_ymtx, null);
		try {
			initView();
		} catch (Exception e) {

		}
		return main_view;
	}

	private void initView() {
		myBabaies = (ImageView) main_view.findViewById(R.id.myBabaies);
		baby_name = (TextView) main_view.findViewById(R.id.baby_name);
		modify_time = (TextView) main_view.findViewById(R.id.modify_time);
		mytx_list = (ListView) main_view.findViewById(R.id.mytx_list);
		myBabaies.setOnClickListener(myBabaies_click);

		SharedPreferences sp = main_view.getContext().getSharedPreferences(
				"ym", Context.MODE_PRIVATE);
		Map<String, ?> m = sp.getAll();
		if (m.containsKey("BABY_PIC_PATH")) {
			dbs_ewen_data db = new dbs_ewen_data();
			String filePath = "" + m.get("BABY_PIC_PATH");
			File f = new File(filePath);
			baby_name.setText("" + m.get("BABY_NAME"));
			modify_time.setText("" + m.get("MODIFY_TIME"));
			if (f.exists()) {
				byte[] data = (byte[]) db.getData(f);
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
				myBabaies.setImageBitmap(utils_picture_tools.toOvalBitmap(bm));

			} else {
				Bitmap bitmap = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.vaccine_46);
				myBabaies.setImageBitmap(utils_picture_tools
						.toOvalBitmap(bitmap));
			}
		} else {
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.vaccine_46);
			myBabaies.setImageBitmap(utils_picture_tools.toOvalBitmap(bitmap));
		}
		if (m.containsKey("BABY_YM_PATH")) {
			dbs_ewen_data db = new dbs_ewen_data();
			String filePath = "" + m.get("BABY_YM_PATH");
			File f = new File(filePath);
			if (f.exists()) {
				@SuppressWarnings("unchecked")
				final List<Map<String, Object>> list = (List<Map<String, Object>>) db
						.getData(f);
				adapter_mytx_list adapter = new adapter_mytx_list(
						baby_name.getContext(), list, m.get("BABY_ID") + "");
				mytx_list.setAdapter(adapter);
				setSelect(adapter,list);
			}
		}
	}
	private void setSelect(adapter_mytx_list adapter,List<Map<String,Object>> list){
		adapter.notifyDataSetChanged();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			try {
				String dstr = map.get("VACCINE_SEND_TIME") + "";
				Date date = sdf.parse(dstr);
				Log.d("line155", dstr+i);
				if (now.before(date)) {
					mytx_list.setSelection(i);
					break;
				}
			} catch (ParseException e) {
				Log.d("line152", "===", e);
				e.printStackTrace();
			}
		}

	
	}
	public void init(String baby_id) {
		new GetMyBabyVaccList().execute(baby_id);
	}

	private OnClickListener myBabaies_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new GetMyBabyTask().execute();
		}

	};

	private class GetMyBabyTask extends
			AsyncTask<Void, Void, List<Map<String, Object>>> {
		private List<Map<String, Object>> list;

		@Override
		protected List<Map<String, Object>> doInBackground(Void... params) {
			Map<String, String> param = new HashMap<String, String>();
			try {
				String user_id = PreferencesUtils.getString(
						main_view.getContext(), "user_id");
				param.put("user_id", user_id);
				String json = utils_network_tools.doGet(URL_getMyBaby, param);
				Pattern p_html = Pattern.compile("<[^>]+>",
						Pattern.CASE_INSENSITIVE);
				Matcher m_html = p_html.matcher(json);

				list = utils_Json.json2List(m_html.replaceAll(""));
			} catch (Exception w) {

			}
			return list;
		}

		@Override
		protected void onPostExecute(final List<Map<String, Object>> list) {
			if (list != null) {
				if (!list.isEmpty()) {
					String[] items = new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map = (Map<String, Object>) list
								.get(i);
						items[i] = (String) map.get("BABY_NAME");
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(
							main_view.getContext());
					builder.setTitle("宝宝选择");
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									Map<String, Object> m = list.get(which);
									String baby_pic = (String) m
											.get("BABY_PIC");
									baby_name.setText("" + m.get("BABY_NAME"));
									modify_time.setText(""
											+ m.get("MODIFY_TIME"));
									String url_pic = URL_PIC + baby_pic;
									new GetMyBabyPic(url_pic, m).execute();// 获取图片
									new GetMyBabyVaccList().execute(m
											.get("BABY_ID") + "");
								}

							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
			super.onPostExecute(list);
		}

	}

	private class GetMyBabyVaccList extends
			AsyncTask<String, Void, List<Map<String, Object>>> {
		private String baby_id = "";

		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			try {
				Map<String, String> param = new HashMap<String, String>();
				baby_id = params[0];
				param.put("baby_id", params[0]);
				String json = utils_network_tools.doGet(VACC_PIC, param);
				Pattern p_html = Pattern.compile("<[^>]+>",
						Pattern.CASE_INSENSITIVE);
				Matcher m_html = p_html.matcher(json);
				json = m_html.replaceAll("");
				List<Map<String, Object>> list = utils_Json.json2List(json);
				return list;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> list) {
			try {
				if (list != null) {
					String timepoint = "x";
					int month = 0;
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> m = list.get(i);
						Map<String, Object> m2 = new HashMap<String, Object>();
						m2.putAll(m);
						if (!timepoint.equals(m2.get("VACCINE_AGE"))) {
							m2.put("MONTH", (month + 1) + "");
							m.put("FX", "L");
							list.add(i, m2);
							i++;
						} else {
							Map<String, Object> m3 = list.get(i - 1);
							if ("L".equals(m3.get("FX"))) {
								m.put("FX", "R");
							} else {
								m.put("FX", "L");
							}
						}
						timepoint = "" + m2.get("VACCINE_AGE");

					}
					adapter_mytx_list adapter = new adapter_mytx_list(
							baby_name.getContext(), list, baby_id);
					mytx_list.setAdapter(adapter);
					setSelect(adapter,list);
					// 保存疫苗
					dbs_ewen_data db = new dbs_ewen_data();
					String filePath = db.FILE_BASE_PATH + "/" + baby_id
							+ ".list";
					db.saveData(list, filePath);
					SharedPreferences sp = main_view.getContext()
							.getSharedPreferences("ym", Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("BABY_ID", baby_id);
					editor.putString("BABY_YM_PATH", filePath);
					editor.commit();
				}
			} catch (Exception e) {

			}
		}
	}

	private class adapter_mytx_list extends BaseAdapter {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		private Context context;
		private String baby_id;

		public adapter_mytx_list(Context context,
				List<Map<String, Object>> list, String baby_id) {
			this.context = context;
			this.list = list;
			this.baby_id = baby_id;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.fragment_ymtx_item, parent,
					false);
			final Map<String, Object> m = list.get(position);
			TextView vacc_age = (TextView) convertView
					.findViewById(R.id.vacc_age);
			LinearLayout vacc_age_layout = (LinearLayout) convertView
					.findViewById(R.id.vacc_age_layout);
			TextView left_info = (TextView) convertView
					.findViewById(R.id.left_info);
			TextView right_info = (TextView) convertView
					.findViewById(R.id.right_info);
			TextView is_vacc = (TextView) convertView
					.findViewById(R.id.is_vacc);
			RelativeLayout left_layout = (RelativeLayout) convertView
					.findViewById(R.id.left_layout);
			RelativeLayout right_layout = (RelativeLayout) convertView
					.findViewById(R.id.right_layout);
			TextView left_time = (TextView) convertView
					.findViewById(R.id.left_time);
			TextView right_time = (TextView) convertView
					.findViewById(R.id.right_time);
			RelativeLayout ym_layout = (RelativeLayout) convertView
					.findViewById(R.id.ym_layout);

			if (m.containsKey("MONTH")) {
				final String month = m.get("MONTH") + "";
				vacc_age_layout.setVisibility(View.VISIBLE);
				vacc_age.setText(m.get("VACCINE_AGE") + "");
				vacc_age_layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new SetYmTask().execute(month, baby_id);
					}

				});
				ym_layout.setVisibility(View.GONE);
				return convertView;
			}
			// 疫苗 明细
			vacc_age.setVisibility(View.GONE);
			vacc_age_layout.setVisibility(View.GONE);

			if ("1".equals(m.get("IS_VACCINATED"))) {
				is_vacc.setText("已接种");
				left_time.setTextColor(Color.parseColor("#02C7FE"));
				right_time.setTextColor(Color.parseColor("#02C7FE"));
				left_info.setBackgroundResource(R.drawable.nvaccine_cloudb01);
				right_info.setBackgroundResource(R.drawable.nvaccine_cloudb02);
				is_vacc.setBackgroundResource(R.drawable.vaccine_b03);
			} else {
				is_vacc.setText("未接种");
				is_vacc.setTextColor(Color.parseColor("#ffffff"));
				is_vacc.setBackgroundResource(R.drawable.vaccine_b04);
				left_info.setBackgroundResource(R.drawable.nvaccine_cloudr01);
				right_info.setBackgroundResource(R.drawable.nvaccine_cloudr02);

				left_time.setTextColor(Color.parseColor("#FC8989"));
				right_time.setTextColor(Color.parseColor("#FC8989"));
			}

			String vacc_name = m.get("VACCINE_NAME") + "";
			if (vacc_name.length() > 11) {
				vacc_name = vacc_name.substring(0, 9) + "\n"
						+ vacc_name.substring(10);
			}
			// Log.d("line381", m + "====");

			if ("L".equals(m.get("FX"))) {
				left_layout.setVisibility(View.VISIBLE);
				left_info.setText(vacc_name);
				left_time.setText("" + m.get("VACCINE_SEND_TIME"));

				right_layout.setVisibility(View.GONE);

			} else {
				right_layout.setVisibility(View.VISIBLE);
				right_info.setText(vacc_name);
				right_time.setText("" + m.get("VACCINE_SEND_TIME"));
				left_layout.setVisibility(View.GONE);

			}
			right_info.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String id = "" + m.get("ID");
					Intent intent = new Intent(getActivity(),
							ActivityYmXq.class);
					intent.putExtra("id", id);
					startActivityForResult(intent, 0);
				}
			});
			left_info.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					String id = "" + m.get("ID");
					Intent intent = new Intent(getActivity(),
							ActivityYmXq.class);
					intent.putExtra("id", id);
					startActivityForResult(intent, 0);
				}
			});
			return convertView;
		}
	}

	private class SetYmTask extends
			AsyncTask<String, Void, List<Map<String, Object>>> {
		String baby_id = "";

		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			Map<String, String> param = new HashMap<String, String>();
			try {

				param.put("month", params[0]);
				param.put("baby_id", params[1]);
				baby_id = params[1];
				String json = utils_network_tools.doGet(GETVACCINEBYMONTH,
						param);
				Pattern p_html = Pattern.compile("<[^>]+>",
						Pattern.CASE_INSENSITIVE);
				Matcher m_html = p_html.matcher(json);
				json = m_html.replaceAll("");
				List<Map<String, Object>> list = utils_Json.json2List(json);
				Log.d("line426", json + "==");
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<Map<String, Object>>();
			}
		}

		@Override
		protected void onPostExecute(final List<Map<String, Object>> list) {
			try {
				String[] items = new String[list.size()];
				final boolean[] checkedItems = new boolean[list.size()];
				final boolean[] checkedItemsEx = new boolean[list.size()];

				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> m = list.get(i);
					items[i] = m.get("VACCINE_NAME") + "";
					if ("1".equals(m.get("IS_CHECKED"))) {
						checkedItems[i] = true;
						checkedItemsEx[i] = true;

					} else {
						checkedItems[i] = false;
						checkedItemsEx[i] = true;

					}
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(
						main_view.getContext());
				builder.setTitle("疫苗设置");

				builder.setMultiChoiceItems(items, checkedItems,
						new OnMultiChoiceClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int which,
									boolean isChecked) {
								checkedItemsEx[which] = isChecked;
							}

						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								String add_vaccine_id = "";
								String del_vaccine_id = "";

								for (int i = 0; i < checkedItems.length; i++) {

									if (checkedItems[i] != checkedItemsEx[i]) {
										Map<String, Object> m = list.get(i);
										if (checkedItems[i]) {
											if ("".equals(del_vaccine_id)) {
												del_vaccine_id += ""
														+ m.get("ID");
											} else {
												del_vaccine_id += ","
														+ m.get("ID");
											}
										} else {
											if ("".equals(add_vaccine_id)) {
												add_vaccine_id += ""
														+ m.get("ID");
											} else {
												add_vaccine_id += ","
														+ m.get("ID");
											}
										}
									}
								}
								// 提交
								new setVaccineByArrayTask().execute(baby_id,
										add_vaccine_id, del_vaccine_id);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});
				builder.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class setVaccineByArrayTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			Map<String, String> param = new HashMap<String, String>();
			try {

				param.put("baby_id", params[0]);
				param.put("del_vaccine_id", params[1]);
				param.put("del_vaccine_id", params[2]);

				HttpClient httpClient = new DefaultHttpClient();
				StringBuilder sBuilder = new StringBuilder(SETVACCINEBYARRAY);
				sBuilder.append("?");
				sBuilder.append("&baby_id=" + params[0]);
				try {
					String[] adds = params[1].split(",");
					for (int i = 0; i < adds.length; i++) {
						sBuilder.append("&add_vaccine_id=" + adds[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					String[] adds = params[2].split(",");
					for (int i = 0; i < adds.length; i++) {
						sBuilder.append("&del_vaccine_id=" + adds[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class GetMyBabyPic extends AsyncTask<Void, Void, InputStream> {
		private String url_path = "";
		private Map<String, Object> m;

		public GetMyBabyPic(String url_path, Map<String, Object> m) {
			this.url_path = url_path;
			this.m = m;
		}

		@Override
		protected InputStream doInBackground(Void... params) {
			try {
				URL url = new URL(url_path);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				// Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return input;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(InputStream input) {
			try {
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				myBabaies.setImageBitmap(utils_picture_tools
						.toOvalBitmap(myBitmap));
				// --保存文件----
				dbs_ewen_data db = new dbs_ewen_data();
				String filePath = db.FILE_BASE_PATH + "/" + m.get("BABY_ID")
						+ ".bm";
				ByteArrayOutputStream baops = new ByteArrayOutputStream();
				myBitmap.compress(CompressFormat.PNG, 0, baops);

				db.saveData(baops.toByteArray(), filePath);

				SharedPreferences sp = main_view.getContext()
						.getSharedPreferences("ym", Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString("MODIFY_TIME", "" + m.get("MODIFY_TIME"));
				editor.putString("BABY_NAME", "" + m.get("BABY_NAME"));
				editor.putString("BABY_PIC_PATH", filePath);
				editor.putString("BABY_ID", m.get("BABY_ID") + "");

				editor.commit();
			} catch (Exception w) {

			}
		}

	}

}
