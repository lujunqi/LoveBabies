package com.xqj.lovebabies.widgets;

import com.xqj.lovebabies.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class NoticeEditInterceptView {
	
	private Context context;
	private LayoutInflater inflater;
	
	private View main_view = null;
	
	public NoticeEditInterceptView(Context context){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}
	
	public View getView(){
		try{
			main_view = inflater.inflate(R.layout.activity_interaction_notice_editor_intercept,null);
			
			
		}catch(Exception ex){
			
		}
		return main_view;
	}

}
