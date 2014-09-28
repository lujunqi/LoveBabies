package com.xqj.lovebabies.fragments;

/**
 * 晨检 扫描二维码
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.adapters.adapter_fragment_interaction_viewpager;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.databases.dbs_ewen_data;
import com.zxing.activity.CaptureActivity;

public class FragmentCheck extends Fragment {
	private View main_view = null;
	private ViewPager cmd_viewpager_container = null;
	private List<Fragment> list_fragment = null;
	private List<Button> list_button = null;
	private utils_common_tools tools = new utils_common_tools();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			ImageView blue = ActivityMain.main_action_bar
					.getCmd_imagebutton_more();
			blue = ActivityMain.main_action_bar.getCmd_imagebutton_more();
			blue.setVisibility(View.VISIBLE);
			if (!"ON".equals(blue.getTag())) {
				blue.setImageResource(R.drawable.bluetooth);
				blue.setTag("OFF");
			}
			// ActivityMain.fragmentEwen.initBlue()
			ActivityMain.main_action_bar.getCmd_textview_title_name().setText(
					"晨检日记");
			main_view = inflater.inflate(R.layout.fragment_check, null);

			init_view_pager();
			init_button_group();

			SharedPreferences sp = main_view.getContext().getSharedPreferences(
					"SP", Context.MODE_PRIVATE);
			String ewenqiang_address_name = sp.getString(
					"ewenqiang_address_name", "x");
			if ("x".equals(ewenqiang_address_name)) {
				cmd_viewpager_container.setCurrentItem(2);
			} else {
				// 扫一扫
				Intent openCameraIntent = new Intent(getActivity(),
						CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);

				// cmd_viewpager_container.setCurrentItem(0);
			}
		} catch (Exception e) {
		}
		return main_view;
	}

	private void init_button_group() {
		try {
			list_button = new ArrayList<Button>();
			int button_icom_size = (int) getActivity()
					.getResources()
					.getDimension(
							R.dimen.fragment_interaction_bottom_button_icon_size);
			int button_text_size = (int) getActivity()
					.getResources()
					.getDimension(
							R.dimen.fragment_interaction_bottom_button_text_size);

			Button fragment_check_code = (Button) main_view
					.findViewById(R.id.fragment_check_code);
			fragment_check_code.setCompoundDrawablesWithIntrinsicBounds(null,
					tools.get_drawable_from_res(getResources(),
							R.drawable.fragment_check_menu_code,
							button_icom_size, button_icom_size), null, null);
			fragment_check_code.setTextSize(button_text_size);
			fragment_check_code.setText("扫一扫");
			fragment_check_code.setOnClickListener(l_fragment_check_code);
			list_button.add(fragment_check_code);

			Button fragment_check_choose = (Button) main_view
					.findViewById(R.id.fragment_check_choose);
			fragment_check_choose.setCompoundDrawablesWithIntrinsicBounds(null,
					tools.get_drawable_from_res(getResources(),
							R.drawable.fragment_check_menu_choose,
							button_icom_size, button_icom_size), null, null);
			fragment_check_choose.setTextSize(button_text_size);
			fragment_check_choose.setText("手动选择");
			fragment_check_choose.setOnClickListener(l_fragment_check_choose);
			list_button.add(fragment_check_choose);

			Button fragment_check_upload = (Button) main_view
					.findViewById(R.id.fragment_check_upload);
			fragment_check_upload.setCompoundDrawablesWithIntrinsicBounds(null,
					tools.get_drawable_from_res(getResources(),
							R.drawable.fragment_check_menu_upload,
							button_icom_size, button_icom_size), null, null);
			fragment_check_upload.setTextSize(button_text_size);
			fragment_check_upload.setText("上传数据");
			fragment_check_upload.setOnClickListener(l_fragment_check_upload);
			list_button.add(fragment_check_upload);

			Button fragment_check_qiang = (Button) main_view
					.findViewById(R.id.fragment_check_qiang);
			fragment_check_qiang.setCompoundDrawablesWithIntrinsicBounds(null,
					tools.get_drawable_from_res(getResources(),
							R.drawable.fragment_check_menu_qiang,
							button_icom_size, button_icom_size), null, null);
			fragment_check_qiang.setTextSize(button_text_size);
			fragment_check_qiang.setText("选择体温枪");
			fragment_check_qiang.setOnClickListener(l_fragment_check_qiang);
			list_button.add(fragment_check_qiang);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	private OnClickListener l_fragment_check_qiang = new OnClickListener(){
		@Override
		public void onClick(View v) {
			init_button((Button)v,R.drawable.fragment_check_menu_qiang);
			cmd_viewpager_container.setCurrentItem(2);
		}
	};
	private OnClickListener l_fragment_check_upload = new OnClickListener(){
		@Override
		public void onClick(View v) {
			init_button((Button)v,R.drawable.fragment_check_menu_upload_green);
			cmd_viewpager_container.setCurrentItem(1);
		}
	};
	private OnClickListener l_fragment_check_choose = new OnClickListener(){
		@Override
		public void onClick(View v) {
			init_button((Button)v,R.drawable.fragment_check_menu_choose_green);
			cmd_viewpager_container.setCurrentItem(0);
		}
	};
	private OnClickListener l_fragment_check_code = new OnClickListener(){
		@Override
		public void onClick(View v) {
			init_button((Button)v,R.drawable.fragment_check_menu_code_green);
			Intent openCameraIntent = new Intent(getActivity(),
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
		}
	};


	private void init_button(Button vbtn,int drawableId) {
		List<Integer> v_draw = new ArrayList<Integer>();
		v_draw.add(R.drawable.fragment_check_menu_code);
		v_draw.add(R.drawable.fragment_check_menu_choose);
		v_draw.add(R.drawable.fragment_check_menu_upload);
		v_draw.add(R.drawable.fragment_check_menu_qiang);
		
		int button_icom_size = (int) getActivity().getResources().getDimension(
				R.dimen.fragment_interaction_bottom_button_icon_size);
		int i = 0;
		for (Button btn : list_button) {
			btn.setCompoundDrawablesWithIntrinsicBounds(null, tools
					.get_drawable_from_res(getResources(),
							v_draw.get(i),
							button_icom_size, button_icom_size), null, null);
			i++;
		}
		vbtn.setCompoundDrawablesWithIntrinsicBounds(null, tools
				.get_drawable_from_res(getResources(),
						drawableId,
						button_icom_size, button_icom_size), null, null);
	}


	private void init_view_pager() {
		try {
			FragmentEwen fragmentEwen = new FragmentEwen();
			cmd_viewpager_container = (ViewPager) main_view
					.findViewById(R.id.fragment_check_viewpager_container);
			list_fragment = new ArrayList<Fragment>();
			list_fragment.add(new FragmentCheckBabies(list_fragment,
					cmd_viewpager_container));
			list_fragment.add(new FragmentCheckUpload());
			list_fragment.add(new FragmentGunMacList());
			list_fragment.add(fragmentEwen);
			ActivityMain.fragmentEwen = fragmentEwen;
			cmd_viewpager_container
					.setAdapter(new adapter_fragment_interaction_viewpager(
							getFragmentManager(), list_fragment));
		} catch (Exception e) {
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == android.app.Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			Map<String, Object> m = new dbs_ewen_data().getItem(result);

			if (!m.isEmpty()) {
				FragmentEwen fe = (FragmentEwen) list_fragment.get(3);
				fe.userInfo = m;
				cmd_viewpager_container.setCurrentItem(3);
			} else {
				Toast.makeText(main_view.getContext(), "二维码错误",
						Toast.LENGTH_SHORT).show();

			}

		}
	}

}
