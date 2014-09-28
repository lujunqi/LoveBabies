package com.xqj.lovebabies.structures;

import android.widget.*;

public class fragment_interaction_dialogue_item {

	private TextView dialogue_user_id = null;
	private TextView dialogue_user_name = null;
	private ImageView dialogue_user_icon = null;
	private TextView dialogue_user_icon_path = null;
	private TextView dialogue_user_last_session_time = null;
	private TextView dialogue_user_last_session_content = null;

	public TextView getDialogue_user_icon_path() {
		return dialogue_user_icon_path;
	}

	public void setDialogue_user_icon_path(TextView dialogue_user_icon_path) {
		this.dialogue_user_icon_path = dialogue_user_icon_path;
	}

	public TextView getDialogue_user_id() {
		return dialogue_user_id;
	}

	public void setDialogue_user_id(TextView dialogue_user_id) {
		this.dialogue_user_id = dialogue_user_id;
	}

	public ImageView getDialogue_user_icon() {
		return dialogue_user_icon;
	}

	public void setDialogue_user_icon(ImageView dialogue_user_icon) {
		this.dialogue_user_icon = dialogue_user_icon;
	}

	public TextView getDialogue_user_name() {
		return dialogue_user_name;
	}

	public void setDialogue_user_name(TextView dialogue_user_name) {
		this.dialogue_user_name = dialogue_user_name;
	}

	public TextView getDialogue_user_last_session_time() {
		return dialogue_user_last_session_time;
	}

	public void setDialogue_user_last_session_time(TextView dialogue_user_last_session_time) {
		this.dialogue_user_last_session_time = dialogue_user_last_session_time;
	}

	public TextView getDialogue_user_last_session_content() {
		return dialogue_user_last_session_content;
	}

	public void setDialogue_user_last_session_content(TextView dialogue_user_last_session_content) {
		this.dialogue_user_last_session_content = dialogue_user_last_session_content;
	}
}
