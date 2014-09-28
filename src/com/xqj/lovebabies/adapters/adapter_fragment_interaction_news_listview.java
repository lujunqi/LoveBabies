package com.xqj.lovebabies.adapters;

import cn.trinea.android.common.util.StringUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.*;
import com.xqj.lovebabies.structures.fragment_interaction_news_item;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapter_fragment_interaction_news_listview extends BaseAdapter {
	private table_interaction_news t_interaction_news = null;
	private fragment_interaction_news_item item = null;
	private Vector<table_interaction_news> list_news = new Vector<table_interaction_news>();
	private Context context = null;
	private LayoutInflater inflater;
	public adapter_fragment_interaction_news_listview(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_news.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_news.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void addItem(table_interaction_news item){
		list_news.add(item);
		this.notifyDataSetChanged();
	}
	
	public void removeAll(){
		list_news.removeAllElements();
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		try {
			if (null == view) {
				view = inflater.inflate(R.layout.fragment_interaction_news_listview_item, arg2, false);
				item = new fragment_interaction_news_item();
				item.setCmd_imageview_news_picture((ImageView) view.findViewById(R.id.fragment_interaction_news_imageview_item_news_picture));
				item.setCmd_textview_news_content((TextView) view.findViewById(R.id.fragment_interaction_news_textview_item_news_content));
				item.setCmd_textview_news_title((TextView) view.findViewById(R.id.fragment_interaction_news_textview_item_news_title));
				item.setCmd_textview_news_id((TextView) view.findViewById(R.id.fragment_interaction_news_textview_item_news_id));
				view.setTag(item);
			} else {
				item = (fragment_interaction_news_item) view.getTag();
			}
			// --
			t_interaction_news = list_news.get(arg0);
			// 填充ID
			try {
				item.getCmd_textview_news_id().setText(t_interaction_news.getNews_id());
				item.getCmd_textview_news_id().setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO: handle exception
			}
			// 填充图片
			try {
				if (!StringUtils.isBlank(t_interaction_news.getPicture_path())) {
					String imageuri = "";
					imageuri += network_interface_paths.get_project_root;
					imageuri += t_interaction_news.getPicture_path();
					// --
					item.getCmd_imageview_news_picture().setVisibility(View.VISIBLE);
//					DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//					builder.showImageOnLoading(R.drawable.default_image_position);
//					builder.showImageForEmptyUri(R.drawable.default_image_err);
//					builder.cacheInMemory(false);
//					builder.cacheOnDisk(true);
//					builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//					DisplayImageOptions options = builder.build();
//					ImageLoader.getInstance().displayImage(imageuri, item.getCmd_imageview_news_picture(), options);
					utils_common_tools.f_display_Image(context, 
							item.getCmd_imageview_news_picture(), imageuri,R.drawable.default_image_position,
							R.drawable.default_image_err, ImageScaleType.IN_SAMPLE_INT);
				} else {
					item.getCmd_imageview_news_picture().setVisibility(View.GONE);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			// 填充动态新闻标题
			try {
				item.getCmd_textview_news_title().setText(t_interaction_news.getNews_title());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// 填充动态新闻内容概要
			try {
				String content = "";
				if (t_interaction_news.getNews_content().length() > 50) {
					content += t_interaction_news.getNews_content().substring(0, 50);
					content += "...";
				} else {
					content += t_interaction_news.getNews_content();
				}
				item.getCmd_textview_news_content().setText(content);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return view;
	}

}
