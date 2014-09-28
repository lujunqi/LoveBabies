package com.xqj.lovebabies.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.structures.fragment_menu_item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class adapter_fragment_menu_listview extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> list = null;
	private fragment_menu_item item = null;
	private Context context = null;
	private LayoutInflater inflater = null;

	public adapter_fragment_menu_listview(ArrayList<HashMap<String, Object>> list, Context context) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int icon_size = 0;
		int text_size = 0;
		
		if(convertView==null){
			convertView = inflater.inflate(R.layout.fragment_menu_item, null, false);
			item = new fragment_menu_item();
			item.setMenu_icon((ImageView) convertView.findViewById(R.id.fragment_menu_item_imageview_icon));
			item.setMenu_title((TextView) convertView.findViewById(R.id.fragment_menu_item_textview_title));
			convertView.setTag(item);
		}else{
			item = (fragment_menu_item)convertView.getTag();
		}
		try {
			if (position == 0) {
				// 填充用户头像和用户昵称
				Bitmap user_icon = (Bitmap)list.get(position).get("user_icon");
				System.out.println(list.get(position));
				if(user_icon!=null){
					item.getMenu_icon().setImageBitmap(user_icon);
				}else{
					String imgurl = network_interface_paths.get_project_root + PreferencesUtils.getString(context, "user_icon");
					icon_size = (int) context.getResources().getDimension(R.dimen.sliding_menu_item_user_icon_size);
					text_size = (int) context.getResources().getDimension(R.dimen.sliding_menu_item_user_name_text_size);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(icon_size, icon_size);
					item.getMenu_icon().setLayoutParams(params);
					item.getMenu_icon().setScaleType(ScaleType.FIT_XY);
					utils_picture_caches.getInstance().init(context);// 初始化图片缓存
					ImageLoader.getInstance().displayImage(imgurl, item.getMenu_icon());
				}
				// --
				item.getMenu_title().setText(PreferencesUtils.getString(context, "user_nick_name"));
				item.getMenu_title().setTextColor(Color.WHITE);
				item.getMenu_title().setTextSize(text_size);
			} else {
				// 填充菜单
				icon_size = (int) context.getResources().getDimension(R.dimen.sliding_menu_item_common_icon_size);
				text_size = (int) context.getResources().getDimension(R.dimen.sliding_menu_item_common_title_text_size);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(icon_size, icon_size);
				item.getMenu_icon().setLayoutParams(params);
				item.getMenu_icon().setScaleType(ScaleType.CENTER_CROP);
				item.getMenu_icon().setImageResource((Integer) list.get(position).get("menu_logo"));
				item.getMenu_title().setText(list.get(position).get("menu_name").toString());
				item.getMenu_title().setTextColor(Color.WHITE);
				item.getMenu_title().setTextSize(text_size);

			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return convertView;
	}

}
