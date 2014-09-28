package com.xqj.lovebabies.adapters;

import java.util.List;
import java.util.Vector;

import cn.trinea.android.common.util.StringUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.*;
import com.xqj.lovebabies.structures.fragment_interaction_dialogue_item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.*;
import android.widget.*;

public class adapter_fragment_interaction_dialogue_listview extends BaseAdapter {
	private Vector<table_interaction_contacts> list_contacts;
	private table_interaction_contacts t_interaction_contacts = null;
	private fragment_interaction_dialogue_item item = null;
	private LayoutInflater inflater = null;
	private Context context = null;

	public adapter_fragment_interaction_dialogue_listview(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list_contacts = new Vector<table_interaction_contacts>();
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_contacts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void addItem(table_interaction_contacts item){
		list_contacts.add(item);
		this.notifyDataSetChanged();
	}
	
	public void removeAll(){
		list_contacts.removeAllElements();
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		t_interaction_contacts = list_contacts.get(position);
		if(view == null){
			view = inflater.inflate(R.layout.fragment_interaction_dialogue_item, null, false);
			item = new fragment_interaction_dialogue_item();
			item.setDialogue_user_icon((ImageView) view.findViewById(R.id.fragment_interaction_dialogue_item_imageview_user_icon));
			item.setDialogue_user_last_session_content((TextView) view.findViewById(R.id.fragment_interaction_dialogue_item_textview_last_session_content));
			item.setDialogue_user_last_session_time((TextView) view.findViewById(R.id.fragment_interaction_dialogue_item_textview_last_session_time));
			item.setDialogue_user_name((TextView) view.findViewById(R.id.fragment_interaction_dialogue_item_textview_user_name));
			item.setDialogue_user_id((TextView) view.findViewById(R.id.fragment_interaction_dialogue_item_textview_user_id));
			item.setDialogue_user_icon_path((TextView) view.findViewById(R.id.fragment_interaction_dialogue_item_textview_user_icon_path));
			view.setTag(item);
		}else{
			item = (fragment_interaction_dialogue_item)view.getTag();
		}
		// 设置用户头像
		try {
			String imgurl = network_interface_paths.get_project_root + t_interaction_contacts.getUser_icon_path();
			// --
//			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//			builder.showImageOnLoading(R.drawable.default_head_icon);
//			builder.showImageForEmptyUri(R.drawable.default_head_icon);
//			builder.cacheInMemory(false);
//			builder.cacheOnDisk(true);
//			builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
////			builder.displayer(new RoundedBitmapDisplayer(20));
//			DisplayImageOptions options = builder.build();
//			ImageLoader.getInstance().displayImage(imgurl, item.getDialogue_user_icon(), options);
			utils_common_tools.f_display_Image(context, 
					item.getDialogue_user_icon(), imgurl,R.drawable.default_head_icon,
					R.drawable.default_head_icon, ImageScaleType.IN_SAMPLE_INT);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// ------------------------------------------------
		// 设置用户头像路径
		try {
			item.getDialogue_user_icon_path().setText(t_interaction_contacts.getUser_icon_path());
		} catch (Exception e) {
			// TODO: handle exception
		}
		// ------------------------------------------------
		// 设置用户ID
		try {
			item.getDialogue_user_id().setText(t_interaction_contacts.getUser_id());
		} catch (Exception e) {
			// TODO: handle exception
		}
		// ------------------------------------------------
		// 设置用户名
		try {
			item.getDialogue_user_name().setText(t_interaction_contacts.getUser_real_name());
		} catch (Exception e) {
			// TODO: handle exception
		}
		// ------------------------------------------------
		// 最后会话时间
		try {
			item.getDialogue_user_last_session_time().setText(t_interaction_contacts.getUser_last_session_time());
		} catch (Exception e) {
			// TODO: handle exception
		}
		// ------------------------------------------------
		// 最后会话内容
		try {
			item.getDialogue_user_last_session_content().setText(t_interaction_contacts.getUser_last_session_content());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return view;
	}
}
