package com.xqj.lovebabies.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_my_center_favorite;
import com.xqj.lovebabies.structures.activity_my_center_favorite_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class adapter_activity_my_center_favorite_listview extends BaseAdapter {
	private Vector<table_my_center_favorite> list;
	private LayoutInflater inflater;
	private int right_width;
	private Context context;
	
	public adapter_activity_my_center_favorite_listview(Context context, int right_width){
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.right_width = right_width;
		list = new Vector<table_my_center_favorite>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addItem(table_my_center_favorite item){
		list.add(item);
		this.notifyDataSetChanged();
	}
	
	public void removeItem(int position){
		list.remove(position);
		this.notifyDataSetChanged();
	}
	
	public void removeAll(){
		list.removeAllElements();
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		activity_my_center_favorite_item viewItem = null;
		table_my_center_favorite favoriteItem = (table_my_center_favorite)getItem(position);
		
		if(convertView==null){
			convertView = inflater.inflate(R.layout.activity_my_center_favorite_item, parent, false);
			viewItem = new activity_my_center_favorite_item();
			
			TextView favorite_title_textview = (TextView)convertView.findViewById(R.id.my_center_favorite_item_title_textview);
			ImageView favorite_icon_imageview = (ImageView)convertView.findViewById(R.id.my_center_favorite_item_icon_imageview);
			RelativeLayout favorite_del_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_favorite_item_del_layout);
			RelativeLayout favorite_left_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_favorite_item_left_layout);
			
			
			viewItem.setIcon_imageview(favorite_icon_imageview);
			viewItem.setTitle_textview(favorite_title_textview);
			viewItem.setDel_layout(favorite_del_layout);
			viewItem.setLeft_layout(favorite_left_layout);
			
			convertView.setTag(viewItem);
		}else{
			viewItem = (activity_my_center_favorite_item)convertView.getTag();
		}
		
		LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
		viewItem.getLeft_layout().setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(right_width, LayoutParams.MATCH_PARENT);
        viewItem.getDel_layout().setLayoutParams(lp2);
        
        if(favoriteItem.getResource_id()>0){
        	viewItem.getIcon_imageview().setImageResource(favoriteItem.getResource_id());
        }else{// ÍøÂç¼ÓÔØÍ¼Æ¬
			if(favoriteItem.getPic_name()!=null && favoriteItem.getPic_name().length()>0){
				String imgurl = network_interface_paths.get_project_root+ favoriteItem.getPic_name();
//				System.out.println("GridViewIcons-->"+imgurl);
//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//				builder.showImageOnLoading(R.drawable.ic_launcher);
//				builder.showImageForEmptyUri(R.drawable.ic_launcher);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(imgurl, viewItem.getIcon_imageview(), options);
				utils_common_tools.f_display_Image(context, 
    					viewItem.getIcon_imageview(), imgurl,R.drawable.ic_launcher,
						R.drawable.ic_launcher, ImageScaleType.IN_SAMPLE_INT);
			}else{
				viewItem.getIcon_imageview().setImageResource(R.drawable.ic_launcher);
			}
        }
        viewItem.getTitle_textview().setText(favoriteItem.getTitle());
        
        viewItem.getDel_layout().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null){
					mListener.onDelItemClick(v, position);
				}
			}
		});
        
		return convertView;
	}

	/**
     * É¾³ý°´Å¥ µ¥»÷ÊÂ¼þ¼àÌýÆ÷
     */
    private onDelItemClickListener mListener = null;
    
    public void setOnDelItemClickListener(onDelItemClickListener listener){
    	mListener = listener;
    }

    public interface onDelItemClickListener {
        void onDelItemClick(View v, int position);
    }
}
