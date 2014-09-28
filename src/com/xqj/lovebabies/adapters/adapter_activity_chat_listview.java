package com.xqj.lovebabies.adapters;

import java.util.LinkedList;

import cn.trinea.android.common.util.StringUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.*;
import com.xqj.lovebabies.listeners.listener_activity_chat_item_button_click;
import com.xqj.lovebabies.structures.*;
import com.xqj.lovebabies.widgets.CircleImageView;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class adapter_activity_chat_listview extends BaseAdapter {

	private Context context = null;
	private LayoutInflater inflater = null;
	private LinkedList<table_interaction_message> list_message = null;
	private table_interaction_message message = null;
	private activity_chat_message_item item = null;
	private table_interaction_contacts receiver = null;
	private table_interaction_contacts sender = null;
	private Handler handler = null;

	public adapter_activity_chat_listview(Context context, Handler handler, LinkedList<table_interaction_message> list_message, table_interaction_contacts receiver, table_interaction_contacts sender) {
		// TODO Auto-generated constructor stub
		this.list_message = list_message;
		this.context = context;
		this.handler = handler;
		this.receiver = receiver;
		this.sender = sender;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_message.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
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
			message = list_message.get(position);
			inflater = LayoutInflater.from(context);
			// ------------------------------------------������Ϣ�����ж�ʹ�õ� layout
			if (StringUtils.isEquals(message.getMessage_direction_type(), table_interaction_message.message_direction_type_mt)) {// ���˷�����ȥ����Ϣ

				view = inflater.inflate(R.layout.activity_interaction_chat_item_send, null);
				item = new activity_chat_message_item();
				item.setCmd_imageview_user_icon((CircleImageView) view.findViewById(R.id.activity_chat_send_imageview_user_icon));
				item.setCmd_textview_occurrence_time((TextView) view.findViewById(R.id.activity_chat_send_textview_chat_time));
				item.setCmd_textview_text_content((TextView) view.findViewById(R.id.activity_chat_send_textview_text_message_content));
				item.setCmd_imageview_image_content((ImageView) view.findViewById(R.id.activity_chat_send_imageview_image_message_content));
				item.setCmd_image_voice_content((ImageView)view.findViewById(R.id.activity_chat_send_button_voice_content_imageview));
				item.setCmd_layout_voice_content((LinearLayout)view.findViewById(R.id.activity_chat_send_button_voice_content_layout));
				item.setCmd_text_voice_content((TextView)view.findViewById(R.id.activity_chat_send_button_voice_content_textview));
				
				item.setCmd_imageview_message_status((ImageView) view.findViewById(R.id.activity_chat_receive_imageview_message_unread));
				view.setTag(item);

				// ������Ϣ��������û�icon
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

			} else if (StringUtils.isEquals(message.getMessage_direction_type(), table_interaction_message.message_direction_type_mo)) {// ���˽��յ�����Ϣ
				view = inflater.inflate(R.layout.activity_interaction_chat_item_receive, null);
				item = new activity_chat_message_item();
				item.setCmd_textview_occurrence_time((TextView) view.findViewById(R.id.activity_chat_receive_textview_occurrence_time));
				item.setCmd_imageview_user_icon((CircleImageView) view.findViewById(R.id.activity_chat_receive_imageview_user_icon));
				item.setCmd_textview_text_content((TextView) view.findViewById(R.id.activity_chat_receive_textview_text_message_content));
				item.setCmd_imageview_image_content((ImageView) view.findViewById(R.id.activity_chat_receive_imageview_image_message_content));
				item.setCmd_image_voice_content((ImageView)view.findViewById(R.id.activity_chat_send_button_voice_content_imageview));
				item.setCmd_layout_voice_content((LinearLayout)view.findViewById(R.id.activity_chat_send_button_voice_content_layout));
				item.setCmd_text_voice_content((TextView)view.findViewById(R.id.activity_chat_send_button_voice_content_textview));
				item.setCmd_imageview_message_status((ImageView) view.findViewById(R.id.activity_chat_receive_imageview_message_unread));
				view.setTag(item);

				// ������Ϣ��������û�icon
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
			// -------------------------------------------�����Ϣ����ʱ��
			try {
				item.getCmd_textview_occurrence_time().setText(message.getMessage_occurrence_time());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// -------------------------------------------������Ϣ��ý���������
			if (StringUtils.isEquals(message.getMessage_media_type(), table_interaction_message.message_media_type_text)) {// �ı���Ϣ
				// �����Ϣ����
				try {
					item.getCmd_textview_text_content().setVisibility(View.VISIBLE);
					String content = message.getMessage_content();
					// [/img001]
					if (content.length() == 9 && content.startsWith("[/img")) {
						String str = content.substring(5, 8);
						int index = Integer.parseInt(str);
						int sourceid = global_contants.faceImgId[index];
						item.getCmd_textview_text_content().setCompoundDrawablesWithIntrinsicBounds(sourceid, 0, 0, 0);
					} else {
						item.getCmd_textview_text_content().setText(content);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (StringUtils.isEquals(message.getMessage_media_type(), table_interaction_message.message_media_type_image)) {// ͼƬ��Ϣ
				String image_uri = message.getMessage_content().replace("[pic]", "");
				item.getCmd_imageview_image_content().setVisibility(View.VISIBLE);
//				utils_picture_caches.getInstance().init(context);
//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//				builder.showImageOnLoading(R.drawable.default_image_position);
//				builder.showImageForEmptyUri(R.drawable.default_image_err);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(image_uri, item.getCmd_imageview_image_content(), options);
				utils_common_tools.f_display_Image(context, 
						item.getCmd_imageview_image_content(), image_uri,R.drawable.default_image_position,
						R.drawable.default_image_err, ImageScaleType.IN_SAMPLE_INT);
			} else if (StringUtils.isEquals(message.getMessage_media_type(), table_interaction_message.message_media_type_voice)) {// ��Ƶ��Ϣ
				String uri = message.getMessage_content();
				item.getCmd_layout_voice_content().setVisibility(View.VISIBLE);
				item.getCmd_layout_voice_content().setOnClickListener(new listener_activity_chat_item_button_click(uri, handler));
				item.getCmd_text_voice_content().setText(message.getMessage_content_length());
			}
			// �Ƿ��Ѷ�
			if(message.getMessage_read_status()!=null 
					&& message.getMessage_read_status().equals("1")){// �Ѷ�
				item.getCmd_imageview_message_status().setVisibility(View.GONE);
			}else{
				item.getCmd_imageview_message_status().setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return view;
	}

}
