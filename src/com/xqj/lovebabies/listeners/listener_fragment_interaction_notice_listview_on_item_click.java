package com.xqj.lovebabies.listeners;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_notice;

public class listener_fragment_interaction_notice_listview_on_item_click
		implements OnItemClickListener {
	private table_interaction_notice t_interaction_notice = null;
	private Handler handler = null;
	private Message message = null;

	public listener_fragment_interaction_notice_listview_on_item_click(
			Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
//			message = new Message();
//			t_interaction_notice = new table_interaction_notice();
//			TextView cmd_textview_notice_id = (TextView) view
//					.findViewById(R.id.fragment_interaction_notice_item_textview_notice_id);
//			t_interaction_notice.setNotice_id(cmd_textview_notice_id.getText()
//					.toString());
//			message.what = message_what_values.fragment_interaction_notice_on_item_click;
//			message.obj = t_interaction_notice;
//			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
