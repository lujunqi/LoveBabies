package com.xqj.lovebabies.adapters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityViewNoticePhoto;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_density_transform;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.databases.*;
import com.xqj.lovebabies.structures.fragment_interaction_notice_item;

public class adapter_fragment_interaction_notice_listview extends BaseAdapter {
	private static final int SINGLE_PIC_HEIGHT = 90;
	private static final int PIC_MARGIN = 2;
	private Vector<table_interaction_notice> list_notices = new Vector<table_interaction_notice>();
	private Context context = null;
	private table_interaction_notice t_interaction_notice = null;
	private fragment_interaction_notice_item item = null;
	private LayoutInflater inflater;

	public adapter_fragment_interaction_notice_listview(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_notices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_notices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void addItem(table_interaction_notice item){
		list_notices.add(item);
		this.notifyDataSetChanged();
	}

	public void removeAll(){
		list_notices.removeAllElements();
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		try {
			t_interaction_notice = list_notices.get(position);
			t_interaction_notice = t_interaction_notice == null ? new table_interaction_notice() : t_interaction_notice;
			// --
			if (view == null) {
				item = new fragment_interaction_notice_item();
				view = inflater.inflate(R.layout.fragment_interaction_notice_item, parent, false);
				item.setCmd_imageview_sender_icon((ImageView) view.findViewById(R.id.fragment_interaction_notice_item_imageview_sender_icon));
				item.setCmd_textview_sender_name((TextView) view.findViewById(R.id.fragment_interaction_notice_item_textview_sender_name));
				item.setCmd_textview_org_name((TextView) view.findViewById(R.id.fragment_interaction_notice_item_textview_org_name));
				item.setCmd_textview_notice_type((TextView) view.findViewById(R.id.fragment_interaction_notice_item_textview_notice_type));
				item.setCmd_textview_notice_content((TextView) view.findViewById(R.id.fragment_interaction_notice_item_textview_notice_content));
				item.setCmd_textview_notice_publish_time((TextView) view.findViewById(R.id.fragment_interaction_notice_item_textview_notice_publish_time));
				item.setCmd_textview_notice_comment_counts((TextView) view.findViewById(R.id.fragment_interaction_notice_textview_comment));
				item.setCmd_textview_notice_zan_counts((TextView) view.findViewById(R.id.fragment_interaction_notice_textview_zan));
				item.setCmd_gridview_notice_picture((GridView) view.findViewById(R.id.fragment_interaction_notice_item_gridview_pictures));
//				item.setCmd_layout_notice_picture((LinearLayout) view.findViewById(R.id.fragment_interaction_notice_item_gridview_pictures_layout));
				item.setCmd_gridview_notice_picture_count((TextView) view.findViewById(R.id.activity_interaction_notice_detail_textview_notice_picture_count));
				item.setCmd_textview_notice_title((TextView) view.findViewById(R.id.fragment_interaction_notice_item_textview_notice_title));
				view.setTag(item);
			} else {
				item = (fragment_interaction_notice_item) view.getTag();
			}
			// --
			try {// 填充用户头像
				String uri = "http://" + global_contants.application_server_ip + ":" + global_contants.application_server_port + "/lovebaby/" + t_interaction_notice.getNotice_sender_icon();

//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//				builder.showImageOnLoading(R.drawable.default_head_icon);
//				builder.showImageForEmptyUri(R.drawable.default_head_icon);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(uri, item.getCmd_imageview_sender_icon(), options);
				utils_common_tools.f_display_Image(context, 
						item.getCmd_imageview_sender_icon(), uri,R.drawable.default_head_icon,
						R.drawable.default_head_icon, ImageScaleType.IN_SAMPLE_INT);
			} catch (Exception e) {
				// TODO: handle exception
			}
			// --
			try {// 填充sender_name
				item.getCmd_textview_sender_name().setText(t_interaction_notice.getNotice_sender_name());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// --
			try {// 填充机构名称
				item.getCmd_textview_org_name().setText("来自:" + t_interaction_notice.getNotice_org_name());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// --
			try {// 填充公告发布时间
				item.getCmd_textview_notice_publish_time().setText(t_interaction_notice.getNotice_publish_time());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// --
			try {// 填充公告类别
				item.getCmd_textview_notice_type().setText(t_interaction_notice.getNotice_type_name());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// --
			try {// 填充公告内容
				String content = "";
				if (t_interaction_notice.getNotice_content().length() > 100) {
					content = t_interaction_notice.getNotice_content().substring(0, 100) + "...";
				} else {
					content = t_interaction_notice.getNotice_content();
				}
				item.getCmd_textview_notice_content().setText(content);
			} catch (Exception e) {
				// TODO: handle exception
			}
			// 填充公告标题
			if(t_interaction_notice.getNotice_title()!=null 
					&&t_interaction_notice.getNotice_title().length()>0){
				item.getCmd_textview_notice_title().setVisibility(View.VISIBLE);
				item.getCmd_textview_notice_title().setText(t_interaction_notice.getNotice_title());
			}else{
				item.getCmd_textview_notice_title().setVisibility(View.GONE);
			}
			// --
			try {// 填充评论次数，点赞次数等
				if(t_interaction_notice.getNotice_comment_count()!=null 
						&&t_interaction_notice.getNotice_comment_count().length()>0
						&&!t_interaction_notice.getNotice_comment_count().equals("0")){
					item.getCmd_textview_notice_comment_counts().setText(t_interaction_notice.getNotice_comment_count());
				}
				if(t_interaction_notice.getNotice_praise_count()!=null 
						&&t_interaction_notice.getNotice_praise_count().length()>0
						&&!t_interaction_notice.getNotice_praise_count().equals("0")){
					item.getCmd_textview_notice_zan_counts().setText(t_interaction_notice.getNotice_praise_count());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			// --
			try {// 填充图片九宫格
				String image_path = null;
				image_path = t_interaction_notice.getNotice_picture_breviary();
				image_path = image_path == null ? "" : image_path;
				String[] image_path_array = image_path.split(",");
				image_path_array = image_path_array == null ? new String[0] : image_path_array;
				item.getCmd_gridview_notice_picture_count().setText("共"+image_path_array.length+"张  >");
				// --
				item.getCmd_gridview_notice_picture().setAdapter(new adapter_fragment_interaction_notice_item_gridview(context, image_path_array));
				ViewGroup.LayoutParams params = item.getCmd_gridview_notice_picture().getLayoutParams();
				int gridHeight = utils_density_transform.dip2px(context, getGridViewHeight(image_path_array.length));
				params.height = gridHeight;
				item.getCmd_gridview_notice_picture().setLayoutParams(params);
//				int item_size = (int) context.getResources().getDimension(R.dimen.fragment_interaction_notice_gridview_picture_size);
//				LinearLayout.LayoutParams lparams = null;
//				if (image_path_array.length <= 0) {
//					item.getCmd_gridview_notice_picture().setVisibility(View.GONE);
//				} else if (image_path_array.length > 0 && image_path_array.length <= 3) {
//					lparams = new LinearLayout.LayoutParams(3 * item_size+4, item_size+16);
//					item.getCmd_gridview_notice_picture().setVisibility(View.VISIBLE);
//					item.getCmd_gridview_notice_picture().setLayoutParams(lparams);
//				} else if (image_path_array.length > 3 && image_path_array.length <= 6) {
//					lparams = new LinearLayout.LayoutParams(3 * item_size+4, 2 * item_size+20);
//					item.getCmd_gridview_notice_picture().setVisibility(View.VISIBLE);
//					item.getCmd_gridview_notice_picture().setLayoutParams(lparams);
//				} else {
//					lparams = new LinearLayout.LayoutParams(3 * item_size+4, 3 * item_size+24);
//					item.getCmd_gridview_notice_picture().setVisibility(View.VISIBLE);
//					item.getCmd_gridview_notice_picture().setLayoutParams(lparams);
//				}
				item.getCmd_gridview_notice_picture().setTag(String.valueOf(position));
				item.getCmd_gridview_notice_picture().setOnItemClickListener(gridviewItemClickListener);
			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return view;
	}
	
	private OnItemClickListener gridviewItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parentView, View arg1, int photoIndex,
				long arg3) {
			// TODO Auto-generated method stub
			try{
				int parentIndex = Integer.parseInt(parentView.getTag().toString());
				
				table_interaction_notice notice_item = list_notices.get(parentIndex);
				
				List<String> pic_path_list = new ArrayList<String>();
				String image_path = null;
				image_path = notice_item.getNotice_picture_detailed();
				image_path = image_path == null ? "" : image_path;
				String[] image_path_array = image_path.split(",");
				image_path_array = image_path_array == null ? new String[0] : image_path_array;
				for(String image : image_path_array){
					pic_path_list.add(image);
				}
				
				Intent intent = new Intent();
				intent.setClass(context, ActivityViewNoticePhoto.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("pic_list", (Serializable)pic_path_list);
				intent.putExtras(bundle);
				intent.putExtra("photo_index", photoIndex);
				context.startActivity(intent);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	};
	
	/**
	 * 获取GridView的高度
	 * @param num
	 * @return
	 */
	public static int getGridViewHeight(int num){
		if(num>0 && num<=3){
			return SINGLE_PIC_HEIGHT+(PIC_MARGIN*2);
		}else if(num>3 && num<=6){
			return (SINGLE_PIC_HEIGHT*2)+(PIC_MARGIN*3);
		}else if(num>6){
			return (SINGLE_PIC_HEIGHT*3)+(PIC_MARGIN*4);
		}else{
			return 0;			
		}
	}
}
