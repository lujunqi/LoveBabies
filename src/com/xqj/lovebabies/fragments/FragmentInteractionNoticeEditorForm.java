package com.xqj.lovebabies.fragments;

import java.io.File;
import java.util.List;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.databases.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.TextView;

public class FragmentInteractionNoticeEditorForm extends Fragment {

	private String notice_type = null;
	private String notice_content = null;
	private List<File> notice_picture_list = null;
	private List<table_interaction_contacts> notice_receiver_list = null;
	private AlertDialog alertdialog = null;

	private TextView cmd_textview_title_select_receiver = null;
	private TextView cmd_textview_title_notice_type = null;
	private TextView cmd_textview_title_notice_content = null;
	private TextView cmd_textview_title_notice_picture = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = null;
		try {
			view = inflater.inflate(R.layout.fragment_interaction_notice_editor_form, null);
			// --
			cmd_textview_title_select_receiver = (TextView) view.findViewById(R.id.fragment_interaction_notice_editor_form_textview_title_notice_receiver);
			// --
			cmd_textview_title_notice_type = (TextView) view.findViewById(R.id.fragment_interaction_notice_editor_form_textview_title_notice_type);
			// --
			cmd_textview_title_notice_content = (TextView) view.findViewById(R.id.fragment_interaction_notice_editor_form_textview_title_notice_content);
			// --
			cmd_textview_title_notice_picture = (TextView) view.findViewById(R.id.fragment_interaction_notice_editor_form_textview_title_notice_picture);
			init();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return view;
	}

	private void init() {
		try {
			// --
			cmd_textview_title_select_receiver.setText("请选择公告接收者【必填】");
			// --
			cmd_textview_title_notice_type.setText("请选择公告类别【必填】");
			// --
			cmd_textview_title_notice_content.setText("请输入公告内容【必填】");
			// --
			cmd_textview_title_notice_picture.setText("插入公告图片【选填】");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}
