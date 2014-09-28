package com.xqj.lovebabies.adapters;

import java.util.List;

import cn.trinea.android.common.util.StringUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_interaction_contacts;
import com.xqj.lovebabies.structures.fragment_interaction_contacts_item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class adapter_fragment_interaction_contacts_listview extends BaseAdapter {
	private table_interaction_contacts contacts = null;
	private fragment_interaction_contacts_item item = null;
	private LayoutInflater inflater = null;
	private List<table_interaction_contacts> list_contacts = null;
	private Context context = null;

	public adapter_fragment_interaction_contacts_listview(List<table_interaction_contacts> list_contacts, Context context) {
		// TODO Auto-generated constructor stub
		this.list_contacts = list_contacts;
		this.context = context;
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

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		contacts = (table_interaction_contacts) list_contacts.get(position);
		if(view == null){
			view = inflater.inflate(R.layout.fragment_interaction_contacts_item, null,false);
			item = new fragment_interaction_contacts_item();
			item.setContacts_type_letter((TextView) view.findViewById(R.id.fragment_interaction_contacts_item_letters));
			item.setContacts_user_icon((ImageView) view.findViewById(R.id.fragment_interaction_contacts_item_user_icon));
			item.setContacts_user_real_name((TextView) view.findViewById(R.id.fragment_interaction_contacts_item_user_real_name));
			item.setContacts_user_signature((TextView) view.findViewById(R.id.fragment_interaction_contacts_item_user_signature));
			item.setContacts_user_id((TextView) view.findViewById(R.id.fragment_interaction_contacts_item_user_id));
			view.setTag(item);
		}else{
			item = (fragment_interaction_contacts_item)view.getTag();
		}
		// 设置字母标签
		try {
			int section = contacts.getFirst_letter().charAt(0);
			if (position == getPositionForSection(section)) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
				// item.getContacts_type_letter().setVisibility(View.GONE);
				item.getContacts_type_letter().setText(contacts.getFirst_letter());
				item.getContacts_type_letter().setTextColor(Color.RED);
				item.getContacts_type_letter().setTextSize(18);
				params.topMargin = params.leftMargin = params.rightMargin = params.bottomMargin = 10;
				item.getContacts_type_letter().setLayoutParams(params);
				item.getContacts_type_letter().setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 设置用户头像

		try {
			String imgurl = network_interface_paths.get_project_root + contacts.getUser_icon_path();
			int icon_size = (int) context.getResources().getDimension(R.dimen.fragment_interaction_contacts_item_user_icon_size);
			int icon_margin = (int) context.getResources().getDimension(R.dimen.fragment_interaction_contacts_item_user_icon_margin);
//			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//			builder.showImageOnLoading(R.drawable.ic_launcher);
//			builder.showImageForEmptyUri(R.drawable.ic_launcher);
//			builder.cacheInMemory(false);
//			builder.cacheOnDisk(true);
//			builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
////			builder.displayer(new RoundedBitmapDisplayer(20));
//			DisplayImageOptions options = builder.build();
//			ImageLoader.getInstance().displayImage(imgurl, item.getContacts_user_icon(), options);
			utils_common_tools.f_display_Image(context, 
					item.getContacts_user_icon(), imgurl,R.drawable.ic_launcher,
					R.drawable.ic_launcher, ImageScaleType.IN_SAMPLE_INT);
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(icon_size, icon_size);
			item.getContacts_user_icon().setScaleType(ScaleType.CENTER_CROP);
			params.topMargin = params.leftMargin = params.rightMargin = params.bottomMargin = icon_margin;
			item.getContacts_user_icon().setLayoutParams(params);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				// bitmap.recycle();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 设置USER_ID
		try {
			item.getContacts_user_id().setText(contacts.getUser_id());
			item.getContacts_user_id().setVisibility(View.GONE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 设置用户名称
		try {
			item.getContacts_user_real_name().setText(contacts.getUser_real_name());
			item.getContacts_user_real_name().setTextSize(18);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 设置个性签名
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return view;
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list_contacts.get(i).getFirst_letter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) { return i; }
		}
		return -1;
	}

	public void refesh(List<table_interaction_contacts> list) {
		try {
			this.list_contacts = list;
			notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
