package com.xqj.lovebabies.fragments;

/**
 * 晨检 上传资料
 */

import java.io.File;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_network_tools;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.databases.dbs_ewen_data;

public class FragmentCheckUpload extends Fragment {
	private View main_view = null;
	private static final String URL_PATH = "http://"+ global_contants.application_server_ip+":8080/lovebaby/services/morning_check/submit";
	File[] f = null;
	private Context context = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {


			main_view = inflater.inflate(R.layout.fragment_check_upload, null);
			context = main_view.getContext();

			dbs_ewen_data db = new dbs_ewen_data();
			TextView info = (TextView) main_view.findViewById(R.id.info);
			File fileDir = new File(db.FILE_UPLOAD_PATH);
			f = fileDir.listFiles();
			if (f.length == 0) {
				info.setText("没有收集晨检记录");
			} else {
				info.setText("已经收集了" + f.length + "条数据");

				Button fragment_upload_btn = (Button) main_view
						.findViewById(R.id.fragment_upload_btn);
				fragment_upload_btn
						.setOnClickListener(fragment_upload_btn_click_listener);

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return main_view;
	}

	private OnClickListener fragment_upload_btn_click_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new GetDataTask().execute();
		}
	};

	private class GetDataTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			// Map<String, String> param = new HashMap<String, String>();
			if (f == null) {
				return null;
			}
//			try {
				dbs_ewen_data db = new dbs_ewen_data();
				for (int i = 0; i < f.length; i++) {
					File file = f[i];
					@SuppressWarnings("unchecked")
					Map<String, String> data = (Map<String, String>) db
							.getData(file);
					String user_id = PreferencesUtils.getString(context,
							"user_id");
					// String user_id = "1026";
					data.put("userId", user_id);
					publishProgress("" + data.get("baby_name"),
							"" + data.get("baby_id"));
					String json = utils_network_tools.doGet(URL_PATH, data);
					Log.d("line89", json + "==============" + data);
				}
//			} catch (Exception e) {
//
//			}
			return null;
		}

		public void onProgressUpdate(String... progress) {

			Toast.makeText(context, progress[0] + "晨检日记数据已上传",
					Toast.LENGTH_SHORT).show();
			dbs_ewen_data db = new dbs_ewen_data();
			db.delete(db.FILE_UPLOAD_PATH + progress[1] + ".obj");
			File fileDir = new File(db.FILE_UPLOAD_PATH);
			File[] f = fileDir.listFiles();
			TextView info = (TextView) main_view.findViewById(R.id.info);
			info.setText("已经收集了" + f.length + "条数据");
			if (f.length == 0) {
				info.setText("没有收集晨检记录");
			}
		}

	}
}
