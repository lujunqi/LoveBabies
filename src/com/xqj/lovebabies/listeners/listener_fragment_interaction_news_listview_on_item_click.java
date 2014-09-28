package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_news;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.*;

public class listener_fragment_interaction_news_listview_on_item_click implements OnItemClickListener {

	private table_interaction_news t_interaction_news = null;
	private TextView cmd_textview_news_id = null;
	private Handler handler = null;
	private Message message = null;

	public listener_fragment_interaction_news_listview_on_item_click(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			t_interaction_news = new table_interaction_news();
			cmd_textview_news_id = (TextView) arg1.findViewById(R.id.fragment_interaction_news_textview_item_news_id);
			t_interaction_news.setNews_id(cmd_textview_news_id.getText().toString());
			message.what = message_what_values.fragment_interaction_news_listview_on_item_click;
			message.obj = t_interaction_news;
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
