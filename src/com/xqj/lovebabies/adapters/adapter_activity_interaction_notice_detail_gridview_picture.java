package com.xqj.lovebabies.adapters;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.structures.activity_interaction_notice_detail_picture_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class adapter_activity_interaction_notice_detail_gridview_picture extends BaseAdapter {
	private Context context = null;
	private String[] pictures = null;
	private activity_interaction_notice_detail_picture_item item_picture = null;

	public adapter_activity_interaction_notice_detail_gridview_picture(Context context, String[] pictures) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.pictures = pictures;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		pictures = pictures == null ? new String[0] : pictures;
		return pictures.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return pictures[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		try {
			LayoutInflater inflater = LayoutInflater.from(context);
			if (view == null) {
				view = inflater.inflate(R.layout.activity_interaction_notice_detail_gridview_picture_item, null);
				item_picture = new activity_interaction_notice_detail_picture_item();
				item_picture.setCmd_imageview_picture((ImageView) view.findViewById(R.id.activity_interaction_notice_detail_imageview_picture));
				view.setTag(item_picture);
			} else {
				item_picture = (activity_interaction_notice_detail_picture_item) view.getTag();
			}
			try {
				String imageuri = "";
				imageuri += network_interface_paths.get_project_root;
				imageuri += pictures[position];
//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//				builder.showImageOnLoading(R.drawable.default_image_position);
//				builder.showImageForEmptyUri(R.drawable.default_image_position);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(imageuri, item_picture.getCmd_imageview_picture(), options);
				utils_common_tools.f_display_Image(context, 
						item_picture.getCmd_imageview_picture(), imageuri,R.drawable.default_image_position,
						R.drawable.default_image_err, ImageScaleType.IN_SAMPLE_INT);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return view;
	}

}
