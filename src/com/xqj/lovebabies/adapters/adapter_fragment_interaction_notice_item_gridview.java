package com.xqj.lovebabies.adapters;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.structures.*;

import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class adapter_fragment_interaction_notice_item_gridview extends BaseAdapter {

	private fragment_interaction_notice_gridview_item item = null;
	private Context context = null;
	private String[] images = null;
	private LayoutInflater inflater;
	public adapter_fragment_interaction_notice_item_gridview(Context context, String[] images) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.images = images;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return images[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			if (view == null) {
				view = inflater.inflate(R.layout.fragment_interaction_notice_gridview_item, null, false);
				item = new fragment_interaction_notice_gridview_item();
				item.setCmd_notice_pictrue((ImageView) view.findViewById(R.id.fragment_interaction_notice_gridview_item_pictrue));
				view.setTag(item);
			} else {
				item = (fragment_interaction_notice_gridview_item) view.getTag();
			}
			// --
			try {
				// --
//				int item_size = (int) context.getResources().getDimension(R.dimen.fragment_interaction_notice_gridview_picture_size);
//				LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(item_size, item_size);
//				item.getCmd_notice_pictrue().setLayoutParams(lparams);
				// --
				String imageuri = "";
				imageuri += network_interface_paths.get_project_root;
				imageuri += images[position];
				System.out.println("¹«¸æÍ¼Æ¬Â·¾¶£º"+imageuri);
//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//				builder.showImageOnLoading(R.drawable.default_image_position);
//				builder.showImageForEmptyUri(R.drawable.default_image_position);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(imageuri, item.getCmd_notice_pictrue(), options);
				utils_common_tools.f_display_Image(context, 
						item.getCmd_notice_pictrue(), imageuri,R.drawable.default_image_position,
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
