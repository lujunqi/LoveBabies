package com.xqj.lovebabies.widgets;

import com.xqj.lovebabies.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class NoticeEditHistoryView {
	
	private Context context;
	private LayoutInflater inflater;
	
	private View main_view;
	
	public NoticeEditHistoryView(Context context){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}
	
	public View getView(){
		main_view = inflater.inflate(R.layout.activity_interaction_notice_editor_history, null);
		
		
		return main_view;
	}

}
