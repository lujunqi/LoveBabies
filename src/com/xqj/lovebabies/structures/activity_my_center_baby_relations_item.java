package com.xqj.lovebabies.structures;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class activity_my_center_baby_relations_item {
	private RelativeLayout left_layout;
	private RelativeLayout right_layout;
	private RelativeLayout del_layout;
	private ImageView icon_imageview;
	private TextView relation_name_textview;
	private TextView relation_textview;
	
	public RelativeLayout getLeft_layout() {
		return left_layout;
	}
	public void setLeft_layout(RelativeLayout left_layout) {
		this.left_layout = left_layout;
	}
	public RelativeLayout getDel_layout() {
		return del_layout;
	}
	public RelativeLayout getRight_layout() {
		return right_layout;
	}
	public void setRight_layout(RelativeLayout right_layout) {
		this.right_layout = right_layout;
	}
	public ImageView getIcon_imageview() {
		return icon_imageview;
	}
	public void setIcon_imageview(ImageView icon_imageview) {
		this.icon_imageview = icon_imageview;
	}
	public TextView getRelation_name_textview() {
		return relation_name_textview;
	}
	public void setRelation_name_textview(TextView relation_name_textview) {
		this.relation_name_textview = relation_name_textview;
	}
	public void setDel_layout(RelativeLayout del_layout) {
		this.del_layout = del_layout;
	}
	public TextView getRelation_textview() {
		return relation_textview;
	}
	public void setRelation_textview(TextView relation_textview) {
		this.relation_textview = relation_textview;
	}
	
	
	
}
