package com.xqj.lovebabies.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.StringUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityInteractionNoticeEditor;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.adapters.adapter_fragment_interaction_viewpager;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.listeners.listener_fragment_interaction_on_click;
import com.xqj.lovebabies.listeners.listener_fragment_interaction_on_page_change;
import com.xqj.lovebabies.widgets.ContactsView;
import com.xqj.lovebabies.widgets.DialogueView;
import com.xqj.lovebabies.widgets.NewsView;
import com.xqj.lovebabies.widgets.NoticeView;

public class FragmentInteraction extends Fragment {
	private utils_common_tools tools = new utils_common_tools();
	private ViewPager cmd_viewpager_container = null;
	private Button cmd_button_dialogue = null;
	private Button cmd_button_notice = null;
	private Button cmd_button_news = null;
	private Button cmd_button_contacts = null;
	private List<Button> list_button = null;
//	private List<Fragment> list_fragment = null;
	private View main_view = null;
	
	//---------------------update by lxk-----------------
	private List<View> view_list = new ArrayList<View>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			main_view = inflater.inflate(R.layout.fragment_interaction, null);

			init_button_group();
			init_action_bar();
			init_view_pager();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return main_view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	private void init_action_bar() {
		try {
			f_action_click_dialogue();
			ActivityMain.main_action_bar.getCmd_textview_title_name().setText("家园互动");
			ActivityMain.main_action_bar.getCmd_imagebutton_more().setVisibility(View.GONE);
			ActivityMain.main_action_bar.getCmd_imageview_title_action().setVisibility(View.GONE);
			ActivityMain.main_action_bar.getCmd_imageview_title_icon().setVisibility(View.GONE);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

//	private void init_view_pager() {
//		try {
//			cmd_viewpager_container = (ViewPager) main_view.findViewById(R.id.fragment_interaction_viewpager_container);
//			list_fragment = new ArrayList<Fragment>();
//			list_fragment.add(new FragmentInteractionDialogue());
//			list_fragment.add(new FragmentInteractionNotice());
//			list_fragment.add(new FragmentInteractionSchoolNews());
//			list_fragment.add(new FragmentInteractionContacts());
//			cmd_viewpager_container.setAdapter(new adapter_fragment_interaction_viewpager(getFragmentManager(), list_fragment));
//			cmd_viewpager_container.setOnPageChangeListener(new listener_fragment_interaction_on_page_change(handler));
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
	
	private void init_button_group() {
		try {
			// 初始化底部按钮组
			list_button = new ArrayList<Button>();
			int button_icom_size = (int) getActivity().getResources().getDimension(R.dimen.fragment_interaction_bottom_button_icon_size);
			int button_text_size = (int) getActivity().getResources().getDimension(R.dimen.fragment_interaction_bottom_button_text_size);
			// ----------------------------

			cmd_button_dialogue = (Button) main_view.findViewById(R.id.fragment_interaction_button_dialogue);
			cmd_button_dialogue.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_1_1, button_icom_size, button_icom_size), null, null);
			cmd_button_dialogue.setOnClickListener(new listener_fragment_interaction_on_click(handler, 0));
			cmd_button_dialogue.setTextSize(button_text_size);
			cmd_button_dialogue.setText("会话");
			list_button.add(cmd_button_dialogue);
			// --
			cmd_button_notice = (Button) main_view.findViewById(R.id.fragment_interaction_button_notice);
			cmd_button_notice.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_2_1, button_icom_size, button_icom_size), null, null);
			cmd_button_notice.setOnClickListener(new listener_fragment_interaction_on_click(handler, 0));
			cmd_button_notice.setTextSize(button_text_size);
			cmd_button_notice.setText("公告");
			list_button.add(cmd_button_notice);
			// --
			cmd_button_news = (Button) main_view.findViewById(R.id.fragment_interaction_button_news);
			cmd_button_news.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_3_1, button_icom_size, button_icom_size), null, null);
			cmd_button_news.setOnClickListener(new listener_fragment_interaction_on_click(handler, 0));
			cmd_button_news.setTextSize(button_text_size);
			cmd_button_news.setText("动态");
			list_button.add(cmd_button_news);
			// --
			cmd_button_contacts = (Button) main_view.findViewById(R.id.fragment_interaction_button_contacts);
			cmd_button_contacts.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_4_1, button_icom_size, button_icom_size), null, null);
			cmd_button_contacts.setOnClickListener(new listener_fragment_interaction_on_click(handler, 0));
			cmd_button_contacts.setTextSize(button_text_size);
			cmd_button_contacts.setText("联系");
			list_button.add(cmd_button_contacts);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void set_button(Button btn, List<Button> btns) {
		try {
			if (btn == null) return;
			int button_icom_size = (int) getActivity().getResources().getDimension(R.dimen.fragment_interaction_bottom_button_icon_size);

			btns = btns == null ? new ArrayList<Button>() : btns;
			for (int i = 0; i < btns.size(); i++) {
				Button b = btns.get(i);
				// --
				b.setBackgroundColor(Color.BLACK);
				switch (b.getId()) {
				case R.id.fragment_interaction_button_dialogue:
					b.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_1_1, button_icom_size, button_icom_size), null, null);
					break;
				case R.id.fragment_interaction_button_notice:
					b.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_2_1, button_icom_size, button_icom_size), null, null);
					break;
				case R.id.fragment_interaction_button_news:
					b.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_3_1, button_icom_size, button_icom_size), null, null);
					break;
				case R.id.fragment_interaction_button_contacts:
					b.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_4_1, button_icom_size, button_icom_size), null, null);
					break;
				default:
					break;
				}
				// --
				if (b.getId() == btn.getId()) {
					// btn.setBackgroundColor(Color.WHITE);

					ActivityMain.main_action_bar.getCmd_imagebutton_more().setVisibility(View.GONE);
					switch (btn.getId()) {
					case R.id.fragment_interaction_button_dialogue:
						btn.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_1_2, button_icom_size, button_icom_size), null, null);
						break;
					case R.id.fragment_interaction_button_notice:
						btn.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_2_2, button_icom_size, button_icom_size), null, null);
						// --公告添加入口
						if (f_action_has_create_notice_permission()) {
							ActivityMain.main_action_bar.getCmd_imagebutton_more().setImageResource(R.drawable.add_baby_growth_selector);
							ActivityMain.main_action_bar.getCmd_imagebutton_more().setVisibility(View.VISIBLE);
							ActivityMain.main_action_bar.getCmd_imagebutton_more().setOnClickListener(new listener_fragment_interaction_on_click(handler, message_what_values.fragment_interaction_actionbar_button_more_create_notice_on_click));
						}
						break;
					case R.id.fragment_interaction_button_news:
						btn.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_3_2, button_icom_size, button_icom_size), null, null);
						break;
					case R.id.fragment_interaction_button_contacts:
						btn.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.fragment_interaction_menu_icon_4_2, button_icom_size, button_icom_size), null, null);
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == message_what_values.fragment_menu_button_dialogue_on_click) {
				f_action_click_dialogue();
			}
			if (msg.what == message_what_values.fragment_menu_button_notice_on_click) {
				f_action_click_notice();
			}
			if (msg.what == message_what_values.fragment_menu_button_news_on_click) {
				f_action_click_news();
			}
			if (msg.what == message_what_values.fragment_menu_button_contacts_on_click) {
				f_action_click_contacts();
			}
			if (msg.what == message_what_values.fragment_interaction_viewpager_on_change) {
				f_action_viewpager_change(msg);
			}
			if (msg.what == message_what_values.widget_top_action_bar_button_more_click) {
				f_action_click_actionbar_button_more(msg);
			}
		};
	};

	private void f_action_viewpager_change(Message message) {
		try {
			int index = message.arg1;
			Button btn = list_button.get(index);
			set_button(btn, list_button);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_click_actionbar_button_more(Message message) {
		try {
			int type = message.arg1;
			if (type == message_what_values.fragment_interaction_actionbar_button_more_create_notice_on_click) {
				// 跳转至公告发布activity
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityInteractionNoticeEditor.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_click_dialogue() {
		try {
			set_button(cmd_button_dialogue, list_button);
			cmd_viewpager_container.setCurrentItem(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_click_notice() {
		try {
			set_button(cmd_button_notice, list_button);
			cmd_viewpager_container.setCurrentItem(1);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_click_news() {
		try {
			set_button(cmd_button_news, list_button);
			cmd_viewpager_container.setCurrentItem(2);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_click_contacts() {
		try {
			set_button(cmd_button_contacts, list_button);
			cmd_viewpager_container.setCurrentItem(3);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 判断用户时候有公告添加权限
	private boolean f_action_has_create_notice_permission() {
		try {
			String user_type = PreferencesUtils.getString(getActivity(), "user_type", "3");
			if (StringUtils.isEquals(user_type, "1")) return true;
			if (StringUtils.isEquals(user_type, "2")) return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	//--------create by lxk----------------
	private void init_view_pager(){
		try {
			cmd_viewpager_container = (ViewPager) main_view.findViewById(R.id.fragment_interaction_viewpager_container);
			
			DialogueView dialogView = new DialogueView(getActivity());
			view_list.add(dialogView.getView());
			NoticeView noticeView = new NoticeView(getActivity());
			view_list.add(noticeView.getView());
			NewsView newsView = new NewsView(getActivity());
			view_list.add(newsView.getView());
			ContactsView contactsView = new ContactsView(getActivity());
			view_list.add(contactsView.getView());
			
			cmd_viewpager_container.setAdapter(new InteractionPaperAdapter());
			cmd_viewpager_container.setCurrentItem(0);
			cmd_viewpager_container.setOnPageChangeListener(new listener_fragment_interaction_on_page_change(handler));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	class InteractionPaperAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return view_list.size();
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			Log.i("INFO", "destroy item:"+position);  
            ((ViewPager) container).removeView(view_list.get(position));
		}
		@Override
		public Object instantiateItem(View container, int position) {
			Log.i("INFO", "instantiate item:"+position);  
            ((ViewPager) container).addView(view_list.get(position),0); 
            return view_list.get(position);  
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
	}

}
