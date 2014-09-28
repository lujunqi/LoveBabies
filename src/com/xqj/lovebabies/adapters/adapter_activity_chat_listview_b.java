package com.xqj.lovebabies.adapters;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.StringUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityViewNoticePhoto;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.*;
import com.xqj.lovebabies.listeners.listener_activity_chat_item_button_click;
import com.xqj.lovebabies.structures.*;
import com.xqj.lovebabies.threads.thread_download_audio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class adapter_activity_chat_listview_b extends BaseAdapter {

	private Context context = null;
	private LayoutInflater inflater = null;
	private Vector<table_interaction_message> list_message = null;
	private table_interaction_message message = null;
	private activity_chat_message_item item = null;
	private table_interaction_contacts receiver = null;
	private table_interaction_contacts sender = null;
	private Handler play_audio_handler = null;
	private Handler set_readed_handler = null;
	
	private int faceIconHeight = 0;
	private int faceIconWidth = 0;
	private float scale;// dp转pix参数
	private String upload_file_path;

	public adapter_activity_chat_listview_b(Context context,Handler set_readed_handler, Handler play_audio_handler, table_interaction_contacts receiver, table_interaction_contacts sender) {
		// TODO Auto-generated constructor stub
		this.list_message = new Vector<table_interaction_message>();
		this.context = context;
		this.play_audio_handler = play_audio_handler;
		this.set_readed_handler = set_readed_handler;
		this.receiver = receiver;
		this.sender = sender;
		
		scale = context.getResources().getDisplayMetrics().density;// dp转pix参数
		faceIconHeight = (int) (20 * scale + 0.5f);
		faceIconWidth = (int) (20 * scale + 0.5f);
		
		upload_file_path = "";
		upload_file_path += PreferencesUtils.getString(context,
				global_contants.SYS_PATH_SD_CARD);
		upload_file_path += PreferencesUtils.getString(context,
				global_contants.SYS_PATH_APP_FOLDER);
		upload_file_path += PreferencesUtils.getString(context,
				global_contants.SYS_PATH_UP);
		upload_file_path += File.separator;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_message.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_message.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void addItem(table_interaction_message item){
		list_message.add(item);
		this.notifyDataSetChanged();
	}
	
	/**
	 * 把Item加到首位
	 * @param item
	 */
	public void addFirst(table_interaction_message item){
		Vector<table_interaction_message> temp_list = new Vector<table_interaction_message>();
		temp_list.add(item);
		if(list_message!=null){
			for(table_interaction_message obj : list_message){
				temp_list.add(obj);
			}
		}
		list_message = temp_list;
		this.notifyDataSetChanged();
	}
	
	public void removeAll(){
		list_message.removeAllElements();
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			message = list_message.get(position);
			inflater = LayoutInflater.from(context);
			// ------------------------------------------根据消息方向判断使用的 layout
			if (StringUtils.isEquals(message.getMessage_direction_type(), table_interaction_message.message_direction_type_mt)) {// 本人发送下去的消息
				view = inflater.inflate(R.layout.activity_interaction_chat_item_send, null);
				item = new activity_chat_message_item();
				item.setCmd_imageview_user_icon((ImageView) view.findViewById(R.id.activity_chat_send_imageview_user_icon));
				item.setCmd_textview_occurrence_time((TextView) view.findViewById(R.id.activity_chat_send_textview_chat_time));
				item.setCmd_textview_text_content((TextView) view.findViewById(R.id.activity_chat_send_textview_text_message_content));
				item.setCmd_imageview_image_content((ImageView) view.findViewById(R.id.activity_chat_send_imageview_image_message_content));
				item.setCmd_image_voice_content((ImageView)view.findViewById(R.id.activity_chat_send_button_voice_content_imageview));
				item.setCmd_layout_voice_content((LinearLayout)view.findViewById(R.id.activity_chat_send_button_voice_content_layout));
				item.setCmd_text_voice_content((TextView)view.findViewById(R.id.activity_chat_send_button_voice_content_textview));
				item.setCmd_imageview_message_status((ImageView) view.findViewById(R.id.activity_chat_receive_imageview_message_unread));
				view.setTag(item);

				// 根据消息方向填充用户icon
				try {
					String imgurl = "";
					imgurl += network_interface_paths.get_project_root;
					imgurl += sender.getUser_icon_path();
					// --
//					DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//					builder.showImageOnLoading(R.drawable.default_image_position);
//					builder.showImageForEmptyUri(R.drawable.default_image_err);
//					builder.cacheInMemory(false);
//					builder.cacheOnDisk(true);
//					builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//					DisplayImageOptions options = builder.build();
//					ImageLoader.getInstance().displayImage(imgurl, item.getCmd_imageview_user_icon(), options);
					utils_common_tools.f_display_Image(context, 
							item.getCmd_imageview_user_icon(), imgurl,R.drawable.default_image_position,
							R.drawable.default_image_err, ImageScaleType.IN_SAMPLE_INT);
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (StringUtils.isEquals(message.getMessage_direction_type(), table_interaction_message.message_direction_type_mo)) {// 本人接收到的消息
				view = inflater.inflate(R.layout.activity_interaction_chat_item_receive, null);
				item = new activity_chat_message_item();
				item.setCmd_textview_occurrence_time((TextView) view.findViewById(R.id.activity_chat_receive_textview_occurrence_time));
				item.setCmd_imageview_user_icon((ImageView) view.findViewById(R.id.activity_chat_receive_imageview_user_icon));
				item.setCmd_textview_text_content((TextView) view.findViewById(R.id.activity_chat_receive_textview_text_message_content));
				item.setCmd_imageview_image_content((ImageView) view.findViewById(R.id.activity_chat_receive_imageview_image_message_content));
				item.setCmd_image_voice_content((ImageView)view.findViewById(R.id.activity_chat_send_button_voice_content_imageview));
				item.setCmd_layout_voice_content((LinearLayout)view.findViewById(R.id.activity_chat_send_button_voice_content_layout));
				item.setCmd_text_voice_content((TextView)view.findViewById(R.id.activity_chat_send_button_voice_content_textview));
				item.setCmd_imageview_message_status((ImageView) view.findViewById(R.id.activity_chat_receive_imageview_message_unread));
				view.setTag(item);

				// 根据消息方向填充用户icon
				try {
					String imgurl = "";
					imgurl += network_interface_paths.get_project_root;
					imgurl += receiver.getUser_icon_path();
					// --
//					DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//					builder.showImageOnLoading(R.drawable.default_image_position);
//					builder.showImageForEmptyUri(R.drawable.default_image_err);
//					builder.cacheInMemory(false);
//					builder.cacheOnDisk(true);
//					builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//					DisplayImageOptions options = builder.build();
//					ImageLoader.getInstance().displayImage(imgurl, item.getCmd_imageview_user_icon(), options);
					utils_common_tools.f_display_Image(context, 
							item.getCmd_imageview_user_icon(), imgurl,R.drawable.default_image_position,
							R.drawable.default_image_err, ImageScaleType.IN_SAMPLE_INT);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
			// -------------------------------------------填充消息发生时间
			try {
				item.getCmd_textview_occurrence_time().setText(message.getMessage_occurrence_time());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// -------------------------------------------根据消息的媒介填充内容
			System.out.println("message_media_type_text["+message.getMessage_media_type()+"]message_content["+message.getMessage_content()+"]");
			if (StringUtils.isEquals(message.getMessage_media_type(), table_interaction_message.message_media_type_text)) {// 文本消息
				// 填充消息内容
				try {
					item.getCmd_textview_text_content().setVisibility(View.VISIBLE);
					String content = message.getMessage_content();
					// /偷笑 ---  显示表情
					replaceImage(item.getCmd_textview_text_content(), content);
//					if (content.length() == 9 && content.startsWith("[/img")) {
//						String str = content.substring(5, 8);
//						int index = Integer.parseInt(str);
//						int sourceid = global_contants.faceImgId[index];
//						item.getCmd_textview_text_content().setCompoundDrawablesWithIntrinsicBounds(sourceid, 0, 0, 0);
//					} else {
//						item.getCmd_textview_text_content().setText(content);
//					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (StringUtils.isEquals(message.getMessage_media_type(), table_interaction_message.message_media_type_image)) {// 图片消息
				System.out.println(message.getMessage_content());
				String image_uri = message.getMessage_content().replace("[pic]", "");
				System.out.println(image_uri);
				item.getCmd_imageview_image_content().setVisibility(View.VISIBLE);
				int image_name_index = image_uri.indexOf("p_");
				String file_name = image_uri.substring(image_name_index);
				File image_file = new File(upload_file_path+file_name);
				if(image_file.exists() && image_file.isFile()){//读取本地图片
					Bitmap bm = loadLocalImage(upload_file_path, file_name);
					item.getCmd_imageview_image_content().setImageBitmap(bm);
				}else{
//					utils_picture_caches.getInstance().init(context);
//					DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//					builder.showImageOnLoading(R.drawable.default_image_position);
//					builder.showImageForEmptyUri(R.drawable.default_image_err);
//					builder.cacheInMemory(false);
//					builder.cacheOnDisk(true);
//					builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//					DisplayImageOptions options = builder.build();
//					ImageLoader.getInstance().displayImage(image_uri, item.getCmd_imageview_image_content(), options);
					utils_common_tools.f_display_Image(context, 
							item.getCmd_imageview_image_content(), image_uri,R.drawable.default_image_position,
							R.drawable.default_image_err, ImageScaleType.IN_SAMPLE_INT);
				}
			} else if (StringUtils.isEquals(message.getMessage_media_type(), table_interaction_message.message_media_type_voice)) {// 音频消息
//				String uri = message.getMessage_content();
				item.getCmd_layout_voice_content().setVisibility(View.VISIBLE);
				item.getCmd_layout_voice_content().setTag(message);
				item.getCmd_layout_voice_content().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						table_interaction_message message_tag = (table_interaction_message)view.getTag();
						new thread_download_audio(play_audio_handler,upload_file_path,message_tag.getMessage_content()).start();
						set_readed_handler.sendMessage(set_readed_handler.obtainMessage(1, message_tag));
					}
				});
				item.getCmd_text_voice_content().setText(message.getMessage_content_length());
			}
			// 是否已读
			if(message.getMessage_read_status()!=null 
					&& message.getMessage_read_status().equals("1")){// 已读
				item.getCmd_imageview_message_status().setVisibility(View.GONE);
			}else{
				if(StringUtils.isEquals(message.getMessage_media_type(), 
						table_interaction_message.message_media_type_voice)){//只有音频显示未读
					item.getCmd_imageview_message_status().setVisibility(View.VISIBLE);
				}else{
					item.getCmd_imageview_message_status().setVisibility(View.GONE);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return view;
	}

	/**
	 * 显示；表情图像
	 * @param textView
	 * @param content
	 */
	public void replaceImage(TextView textView, String content)
	{
		try{
			List<String> iconExistNameList = new ArrayList<String>();
			List<Integer> exitNameIndexList = new ArrayList<Integer>();
			for(int i=0;i<global_contants.faceImgName.length;i++){
				if(content.contains(global_contants.faceImgName[i])){
					iconExistNameList.add(global_contants.faceImgName[i]);
					exitNameIndexList.add(i);
				}
			}
			if(iconExistNameList.size()==0){
				textView.setText(content);
				return ;
			}
			SpannableString spannableString = new SpannableString(content);
			for(int j=0;j<iconExistNameList.size();j++){
				int beginIdx = 0;
				while(content.indexOf(iconExistNameList.get(j),beginIdx)>-1){
					beginIdx = content.indexOf(iconExistNameList.get(j),beginIdx);
					int exitNameIndex = exitNameIndexList.get(j);
					Drawable bd= new utils_common_tools().get_drawable_from_res(context.getResources(), 
							global_contants.faceImgId[exitNameIndex], faceIconWidth, faceIconHeight);
					bd.setBounds(0, 0, faceIconWidth+10, faceIconHeight+10); 
					ImageSpan imageSpan = new ImageSpan(bd, ImageSpan.ALIGN_BASELINE);
			        spannableString.setSpan(imageSpan,  beginIdx ,  beginIdx + 
			        		iconExistNameList.get(j).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			        beginIdx = beginIdx + iconExistNameList.get(j).length();
				}
			}
			textView.setText(spannableString);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 加载本地图片
	 */
	public Bitmap loadLocalImage(String path, String fileName){
		InputStream input = null;
		Bitmap bmp = null ;
		File file = null;
		// 从本地查找
		try{
			file = new File(path,fileName);
			if(file.exists()&&!file.isDirectory()){
				input = new FileInputStream(file);
				bmp = BitmapFactory.decodeStream(input);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		return bmp;
	}
	
}
