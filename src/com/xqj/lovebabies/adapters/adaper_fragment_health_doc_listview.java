package com.xqj.lovebabies.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xqj.lovebabies.R;

public class adaper_fragment_health_doc_listview extends BaseAdapter {

	private List<Map<String, Object>> list;
	private LayoutInflater inflater = null;
	private Context context = null;

	public adaper_fragment_health_doc_listview(List<Map<String, Object>> list,
			Context context) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.fragment_health_doc_item, null);
		Map<String, Object> m = list.get(position);
		TextView date = (TextView) view.findViewById(R.id.date);
		date.setText(m.get("CHECK_TIME") + "");
		TextView name = (TextView) view.findViewById(R.id.name);
		if (position == 0) {
			name.setText(Html.fromHtml("<font color=\"#C24376\">"
					+ m.get("BABY_NAME") + "</font>"));
		} else {
			name.setText(Html.fromHtml("<font color=\"#247DB7\">"
					+ m.get("BABY_NAME") + "</font>"));
		}

		TextView ewen = (TextView) view.findViewById(R.id.ewen);
		ewen.setText(Html.fromHtml(m.get("BODY_TEMPERATURE") + ""));

		TextView info = (TextView) view.findViewById(R.id.info);
		info.setText(Html.fromHtml(m.get("CHECK_RESULT") + ""));

		TextView info_ex = (TextView) view.findViewById(R.id.info_ex);
		if(m.containsKey("REMARK")){
			info_ex.setText(Html.fromHtml(m.get("REMARK") + ""));
		}else{
			info_ex.setVisibility(View.GONE);
			
		}
		return view;
	}

}
