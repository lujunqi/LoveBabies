package com.xqj.lovebabies.structures;

import com.xqj.lovebabies.widgets.CircleImageView;

import android.widget.*;

public class activity_chat_message_item {
	private TextView cmd_textview_occurrence_time = null;
	private ImageView cmd_imageview_user_icon = null;
	private TextView cmd_textview_text_content = null;
	private ImageView cmd_imageview_image_content = null;
	private ImageView cmd_image_voice_content = null;
	private TextView cmd_text_voice_content = null;
	private LinearLayout cmd_layout_voice_content = null;
	private ImageView cmd_imageview_message_status = null;

	public TextView getCmd_textview_occurrence_time() {
		return cmd_textview_occurrence_time;
	}

	public void setCmd_textview_occurrence_time(TextView cmd_textview_occurrence_time) {
		this.cmd_textview_occurrence_time = cmd_textview_occurrence_time;
	}

	public ImageView getCmd_imageview_user_icon() {
		return cmd_imageview_user_icon;
	}

	public void setCmd_imageview_user_icon(ImageView cmd_imageview_user_icon) {
		this.cmd_imageview_user_icon = cmd_imageview_user_icon;
	}

	public TextView getCmd_textview_text_content() {
		return cmd_textview_text_content;
	}

	public void setCmd_textview_text_content(TextView cmd_textview_text_content) {
		this.cmd_textview_text_content = cmd_textview_text_content;
	}

	public ImageView getCmd_imageview_image_content() {
		return cmd_imageview_image_content;
	}

	public void setCmd_imageview_image_content(ImageView cmd_imageview_image_content) {
		this.cmd_imageview_image_content = cmd_imageview_image_content;
	}


	public ImageView getCmd_image_voice_content() {
		return cmd_image_voice_content;
	}

	public void setCmd_image_voice_content(ImageView cmd_image_voice_content) {
		this.cmd_image_voice_content = cmd_image_voice_content;
	}

	public TextView getCmd_text_voice_content() {
		return cmd_text_voice_content;
	}

	public void setCmd_text_voice_content(TextView cmd_text_voice_content) {
		this.cmd_text_voice_content = cmd_text_voice_content;
	}

	public ImageView getCmd_imageview_message_status() {
		return cmd_imageview_message_status;
	}

	public void setCmd_imageview_message_status(
			ImageView cmd_imageview_message_status) {
		this.cmd_imageview_message_status = cmd_imageview_message_status;
	}

	public LinearLayout getCmd_layout_voice_content() {
		return cmd_layout_voice_content;
	}

	public void setCmd_layout_voice_content(LinearLayout cmd_layout_voice_content) {
		this.cmd_layout_voice_content = cmd_layout_voice_content;
	}
}
