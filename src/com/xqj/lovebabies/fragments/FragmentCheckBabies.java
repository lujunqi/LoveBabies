package com.xqj.lovebabies.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.adapters.adaper_fragment_check_babies_listview;
import com.xqj.lovebabies.commons.utils_Json;
import com.xqj.lovebabies.commons.utils_network_tools;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.databases.dbs_ewen_data;
import com.xqj.lovebabies.widgets.ContactsSideBar;
import com.xqj.lovebabies.widgets.ContactsSideBar.OnTouchingLetterChangedListener;

@SuppressLint("ValidFragment")
public class FragmentCheckBabies extends Fragment {
	private View main_view = null;
	private DropDownListView cmd_listview = null;
	private List<Map<String, Object>> babies = null;
	private ContactsSideBar cmd_sidebar_letters = null;
	private adaper_fragment_check_babies_listview listview_adapter = null;
	private static final int SIDEBAR_LETTERS = 140815;
	private static final String URL_PATH = "http://"
			+ global_contants.application_server_ip
			+ ":8080/lovebaby/services/morning_check/getBabyQRCodeList";
	private ViewPager cmd_viewpager_container;
	private List<Fragment> list_fragment;

	public FragmentCheckBabies(List<Fragment> list_fragment,
			ViewPager cmd_viewpager_container) {
		this.cmd_viewpager_container = cmd_viewpager_container;
		this.list_fragment = list_fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {

			main_view = inflater.inflate(R.layout.fragment_check_babies, null);
			cmd_listview = (DropDownListView) main_view
					.findViewById(R.id.fragment_check_babies_listview);
			babies = new ArrayList<Map<String, Object>>();
			getData();
			// ContactsSideBar
			listview_adapter = new adaper_fragment_check_babies_listview(
					babies, getActivity());
			cmd_listview.setAdapter(listview_adapter);
			cmd_listview.setOnItemClickListener(cmd_listview_click_listener);
			cmd_listview.setOnScrollListener(cmd_listview_scroll_listener);
			cmd_listview.setOnDropDownListener(cmd_listview_down_listener);
			// set on bottom listener
			cmd_listview.setOnBottomListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new GetDataTask(false).execute();
				}
			});
			setSideBar();
		} catch (Exception e) {
		}
		return main_view;
	}

	private OnDropDownListener cmd_listview_down_listener = new OnDropDownListener() {
		@Override
		public void onDropDown() {
			// 数据提取

			new GetDataTask(true).execute();
		}

	};

	private OnScrollListener cmd_listview_scroll_listener = new OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			if (babies.size() >= firstVisibleItem && babies.size() != 0) {
				babies.get(firstVisibleItem);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}

	};
	private OnItemClickListener cmd_listview_click_listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			FragmentEwen fe = (FragmentEwen) list_fragment.get(3);
			fe.userInfo = babies.get(position - 1);

			cmd_viewpager_container.setCurrentItem(3);
			ImageView blue = ActivityMain.main_action_bar
					.getCmd_imagebutton_more();
			blue.setVisibility(View.VISIBLE);

			// ActivityMain.switch_fragment(
			// new FragmentEwen(babies.get(position - 1)), false);

		}

	};

	private void setSideBar() {// 侧边字母
		cmd_sidebar_letters = (ContactsSideBar) main_view
				.findViewById(R.id.fragment_check_babies_sidebar_letters);
		cmd_sidebar_letters
				.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
					@Override
					public void onTouchingLetterChanged(String s) {
						Message message = new Message();
						message.what = SIDEBAR_LETTERS;
						message.obj = s;
						handler.sendMessage(message);
					}
				});
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				if (msg.what == SIDEBAR_LETTERS) {
					cmd_listview.setSelection(listview_adapter
							.getPositionForSection(msg.obj.toString()));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};

	private void getData() {// 数据源
		babies = new dbs_ewen_data().getData();
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		private boolean isDropDown;
		private List<Map<String, Object>> list;

		public GetDataTask(boolean isDropDown) {
			this.isDropDown = isDropDown;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			Map<String, String> param = new HashMap<String, String>();
			// param.put("user_id", "1026");
			String user_id = PreferencesUtils.getString(main_view.getContext(),
					"user_id");
			param.put("userId", user_id);
			Log.d("line187",user_id+"================"+URL_PATH);
			String json = utils_network_tools.doGet(URL_PATH, param);
			json = json
					.replaceAll(
							"<ns:getBabyQRCodeListResponse xmlns:ns=\"http://control.xqj.com\"><ns:return>",
							"");
			json = json.replaceAll(
					"</ns:return></ns:getBabyQRCodeListResponse>", "");

			list = utils_Json.json2List(json);
			dbs_ewen_data data = new dbs_ewen_data();
			data.saveData(list);
			// babies = list;
			return null;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(String[] result) {
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
			super.onPostExecute(result);
		}
	}
}
