package com.xqj.lovebabies.widgets;

import com.xqj.lovebabies.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TopActionBar extends FrameLayout {
	private LayoutInflater inflater = null;
	private FrameLayout cmd_layout_view = null;
	private ImageButton cmd_imagebutton_menu = null;
	private ImageView cmd_imagebutton_more = null;
	private TextView cmd_textview_title_name = null;
	private CircleImageView cmd_imageview_title_icon = null;
	private ImageView cmd_imageview_title_action = null;

	public TopActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			// --
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			cmd_layout_view = (FrameLayout) inflater.inflate(R.layout.widget_top_action_bar, null);
			this.addView(cmd_layout_view);
			// --
			cmd_imagebutton_menu = (ImageButton) cmd_layout_view.findViewById(R.id.widget_activity_main_action_bar_imagebutton_menu);
			cmd_imagebutton_more = (ImageView) cmd_layout_view.findViewById(R.id.widget_activity_main_action_bar_imagebutton_more);
			cmd_imageview_title_action = (ImageView) cmd_layout_view.findViewById(R.id.widget_activity_main_action_bar_imageview_action);
			cmd_imageview_title_icon = (CircleImageView) cmd_layout_view.findViewById(R.id.widget_activity_main_action_bar_imageview_title_icon);
			cmd_textview_title_name = (TextView) cmd_layout_view.findViewById(R.id.widget_activity_main_action_bar_textview_title);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public FrameLayout getCmd_layout_view() {
		return cmd_layout_view;
	}

	public void setCmd_layout_view(FrameLayout cmd_layout_view) {
		this.cmd_layout_view = cmd_layout_view;
	}

	public ImageButton getCmd_imagebutton_menu() {
		return cmd_imagebutton_menu;
	}

	public void setCmd_imagebutton_menu(ImageButton cmd_imagebutton_menu) {
		this.cmd_imagebutton_menu = cmd_imagebutton_menu;
	}

	public ImageView getCmd_imagebutton_more() {
		return cmd_imagebutton_more;
	}

	public void setCmd_imagebutton_more(ImageButton cmd_imagebutton_more) {
		this.cmd_imagebutton_more = cmd_imagebutton_more;
	}

	public TextView getCmd_textview_title_name() {
		return cmd_textview_title_name;
	}

	public void setCmd_textview_title_name(TextView cmd_textview_title_name) {
		this.cmd_textview_title_name = cmd_textview_title_name;
	}

	public ImageView getCmd_imageview_title_icon() {
		return cmd_imageview_title_icon;
	}

	public void setCmd_imageview_title_icon(CircleImageView cmd_imageview_title_icon) {
		this.cmd_imageview_title_icon = cmd_imageview_title_icon;
	}

	public ImageView getCmd_imageview_title_action() {
		return cmd_imageview_title_action;
	}

	public void setCmd_imageview_title_action(ImageView cmd_imageview_title_action) {
		this.cmd_imageview_title_action = cmd_imageview_title_action;
	}

}
