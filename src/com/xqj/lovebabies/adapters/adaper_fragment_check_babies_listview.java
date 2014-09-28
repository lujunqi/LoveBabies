package com.xqj.lovebabies.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xqj.lovebabies.R;

public class adaper_fragment_check_babies_listview extends BaseAdapter {
	private List<Map<String, Object>> babies;
	private LayoutInflater inflater = null;
	private Context context = null;

	public adaper_fragment_check_babies_listview(
			List<Map<String, Object>> babies, Context context) {
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
		view = inflater.inflate(R.layout.fragment_check_babies_item, null);
		Map<String, Object> baby = babies.get(position);
		String baby_name = (String) baby.get("BABY_NAME");// 姓名
		String class_name = (String) baby.get("ORG_SHORTNAME");// 班级
		String baby_no = (String) baby.get("QR_CODE");// 二维码
		String first_letter = (String) baby.get("BABY_PY");// 首字母

		TextView fragment_check_babies_item_name = (TextView) view
				.findViewById(R.id.fragment_check_babies_item_name);
		fragment_check_babies_item_name.setText(baby_name);
		TextView fragment_check_babies_item_other = (TextView) view
				.findViewById(R.id.fragment_check_babies_item_other);
		fragment_check_babies_item_other.setText("(" + class_name + baby_no
				+ ")");
		// 首字母 设置
		if (isFirstLetter(position, first_letter)) {
			TextView tv_first_letter = (TextView) view
					.findViewById(R.id.fragment_check_babies_item_letters);
			tv_first_letter.setText(first_letter);
			tv_first_letter.setTextColor(0xFF7BC31D);
			tv_first_letter.setTextSize(18);

			tv_first_letter.setVisibility(View.VISIBLE);
			View line = view.findViewById(R.id.line);
			line.setVisibility(View.VISIBLE);
		}
		return view;
	}

	private boolean isFirstLetter(int position, String first_letter) {
		if (position == 0) {
			return true;
		} else {
			Map<String, Object> baby = babies.get(position - 1);
			if (!first_letter.equals(baby.get("BABY_PY"))) {// 与前一个比较
				return true;
			} else {
				return false;
			}
		}
	}

	public int getPositionForSection(String k) {
		for (int i = 0; i < babies.size(); i++) {
			Map<String, Object> m = babies.get(i);
			if (k.equalsIgnoreCase(m.get("BABY_PY") + "")) {
				return i;
			}
		}
		return -1;
	}
}
