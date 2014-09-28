package com.xqj.lovebabies.structures;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class activity_my_center_favorite_item {
	private RelativeLayout left_layout;
	private RelativeLayout del_layout;
	
	private ImageView icon_imageview;
	private TextView title_textview;
	
	public RelativeLayout getLeft_layout() {
		return left_layout;
	}
	public void setLeft_layout(RelativeLayout left_layout) {
		this.left_layout = left_layout;
	}
	public RelativeLayout getDel_layout() {
		return del_layout;
	}
	public void setDel_layout(RelativeLayout del_layout) {
		this.del_layout = del_layout;
	}
	public ImageView getIcon_imageview() {
		return icon_imageview;
	}
	public void setIcon_imageview(ImageView icon_imageview) {
		this.icon_imageview = icon_imageview;
	}
	public TextView getTitle_textview() {
		return title_textview;
	}
	public void setTitle_textview(TextView title_textview) {
		this.title_textview = title_textview;
	}
}
