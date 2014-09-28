package com.xqj.lovebabies.fragments;

/**
 * 晨检 上传资料
 */

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.commons.utils_Json;
import com.xqj.lovebabies.commons.utils_network_tools;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.databases.dbs_ewen_data;

public class FragmentGunMacList extends Fragment {
	private View main_view = null;
	private Context context = null;
	private DropDownListView cmd_listview = null;
	private adaper_fragment_check_gun_mac listview_adapter;
	private List<Map<String, String>> babies = new ArrayList<Map<String, String>>();
	private static final String URL_PATH = "http://"+ global_contants.application_server_ip+":8080/lovebaby/services/morning_check/getCheckGunMacList";

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			
			main_view = inflater.inflate(R.layout.fragment_gun_mac, null);
			context = main_view.getContext();
			
			
			cmd_listview = (DropDownListView) main_view
					.findViewById(R.id.fragment_check_gun_mac_listview);
			dbs_ewen_data db = new dbs_ewen_data();
			Object obj = db.getData(new File(db.FILE_BASE_PATH, "gun_mac.obj"));

			if (obj != null) {
				babies = (List<Map<String, String>>) obj;
			}

			listview_adapter = new adaper_fragment_check_gun_mac(babies,
					context);
			cmd_listview.setAdapter(listview_adapter);
			cmd_listview.setOnDropDownListener(cmd_listview_down_listener);
			cmd_listview.setOnItemClickListener(cmd_listview_click_listener);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return main_view;
	}

	private OnItemClickListener cmd_listview_click_listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d("line77", babies.get(position - 1) + "==");
			Map<String, String> m = babies.get(position - 1);
			String ewenqiang_address = m.get("MAC");
			SharedPreferences sp = main_view.getContext().getSharedPreferences(
					"SP", main_view.getContext().MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("ewenqiang_address", ewenqiang_address);
			editor.putString("ewenqiang_address_name",  m.get("CODE"));
			
			editor.commit();
			Toast.makeText(context, "已经选择了体温枪:"+m.get("CODE"), Toast.LENGTH_SHORT).show();
		}

	};
	private OnDropDownListener cmd_listview_down_listener = new OnDropDownListener() {
		@Override
		public void onDropDown() {
			// 数据提取
			new GetDataTask(true).execute();
		}

	};

	private class GetDataTask extends AsyncTask<Void, Void, List<Map<String, String>>> {
		private boolean isDropDown;
		private List<Map<String, String>> list;

		public GetDataTask(boolean isDropDown) {
			this.isDropDown = isDropDown;
		}

		@Override
		protected List<Map<String, String>> doInBackground(Void... params) {
			Map<String, String> param = new HashMap<String, String>();
			String user_id = PreferencesUtils.getString(context, "user_id");
			param.put("userId", user_id);
			String json = utils_network_tools.doGet(URL_PATH, param);
			if (json == null) {
				return new ArrayList<Map<String, String>>();
			}
			json = json
					.replaceAll(
							"<ns:getCheckGunMacListResponse xmlns:ns=\"http://control.xqj.com\"><ns:return>",
							"");
			json = json.replaceAll(
					"</ns:return></ns:getCheckGunMacListResponse>", "");
			list = utils_Json.json2ListEx(json);
			dbs_ewen_data db = new dbs_ewen_data();
			db.saveData(list, db.FILE_BASE_PATH + "/gun_mac.obj");
//			babies = list;
			return list;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(List<Map<String, String>> list) {
			if (isDropDown) {
				babies.clear();
				babies.addAll(list);
				listview_adapter.notifyDataSetChanged();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"MM-dd HH:mm:ss");
				cmd_listview.onDropDownComplete("更新时间"
						+ dateFormat.format(new Date()));
			} else {
				listview_adapter.notifyDataSetChanged();
				cmd_listview.onBottomComplete();
			}
			super.onPostExecute(list);
		}
	}

	private class adaper_fragment_check_gun_mac extends BaseAdapter {
		private List<Map<String, String>> babies;
		private LayoutInflater inflater = null;
		private Context context = null;

		public adaper_fragment_check_gun_mac(List<Map<String, String>> babies,
				Context context) {
			this.babies = babies;
			this.context = context;
		}

		@Override
		public int getCount() {
			return babies.size();
		}

		@Override
		public Object getItem(int position) {
			return babies.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.fragment_check_gun_mac_item, null);
			Map<String, String> baby = babies.get(position);
			String mac = (String) baby.get("MAC");// 姓名
			String code = (String) baby.get("CODE");// 班级
			TextView fragment_check_gun_mac_item_mac = (TextView) view
					.findViewById(R.id.fragment_check_gun_mac_item_mac);
			TextView fragment_check_gun_mac_item_code = (TextView) view
					.findViewById(R.id.fragment_check_gun_mac_item_code);
			fragment_check_gun_mac_item_mac.setText(mac);
			fragment_check_gun_mac_item_code.setText("["+code+"]");

			return view;

		}
	}
}