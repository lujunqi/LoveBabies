package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smackx.packet.VCard;

import cn.trinea.android.common.util.StringUtils;

import com.xqj.lovebabies.commons.*;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_interaction_contacts;
import com.xqj.lovebabies.databases.table_interaction_contacts;
import com.xqj.lovebabies.structures.interface_app_get_user_detail_req;
import com.xqj.lovebabies.structures.interface_app_get_user_detail_resp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

//获取好友列表
public class thread_interaction_get_contacts extends Thread {
	private Context context = null;
	private Handler handler = null;
	private Message message = null;
	private utils_common_tools tools = new utils_common_tools();
	private utils_network_interfaces network = new utils_network_interfaces();
	private List<RosterEntry> list_roster_entry = null;
	private RosterEntry roster_entry = null;
	private table_interaction_contacts t_interaction_contacts = null;
	private dbs_interaction_contacts dbs = null;
	private interface_app_get_user_detail_req get_user_detail_req = null;
	private interface_app_get_user_detail_resp get_user_detail_resp = null;
	public ReentrantLock lock = new ReentrantLock();

	public thread_interaction_get_contacts(Context context, Handler handler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
	}

	public void run() {
		try {
			lock.lock();
			System.out.println("线程启动了。。。。。。。。。。。。。。");
			list_roster_entry = utils_xmpp_client.getInstance().get_all_friends_entries();
			list_roster_entry = list_roster_entry == null ? new ArrayList<RosterEntry>() : list_roster_entry;
			System.out.println(list_roster_entry.size() + "--------------------------------");
			for (int i = 0; i < list_roster_entry.size(); i++) {
				try {
					t_interaction_contacts = new table_interaction_contacts();
					roster_entry = list_roster_entry.get(i);

					t_interaction_contacts.setUser_id(roster_entry.getName());
					// 根据用户ID获取用户详情
					get_user_detail_req = new interface_app_get_user_detail_req();
					get_user_detail_req.setUser_id(t_interaction_contacts.getUser_id());
					get_user_detail_resp = network.ios_interface_app_get_user_detail(get_user_detail_req);
					if (get_user_detail_resp == null) continue;
					//System.out.println(get_user_detail_resp.getUser_real_name());
					// 保存联系人信息
					t_interaction_contacts.setUser_icon_path(get_user_detail_resp.getUser_icon());
					t_interaction_contacts.setUser_id(get_user_detail_resp.getUser_id());
					t_interaction_contacts.setUser_nike_name(get_user_detail_resp.getUser_nike_name());
					t_interaction_contacts.setUser_phone(get_user_detail_resp.getUser_phone());
					t_interaction_contacts.setUser_real_name(get_user_detail_resp.getUser_real_name());
					t_interaction_contacts.setUser_sex(get_user_detail_resp.getUser_sex());
					t_interaction_contacts.setUser_signature(get_user_detail_resp.getUser_signature());
					// 设置用户名首字母
					String first_letter = t_interaction_contacts.getUser_real_name().substring(0, 1);
					first_letter = tools.get_Hanzi_spell(first_letter);
					first_letter = StringUtils.isBlank(first_letter) ? "0" : first_letter;
					Pattern pattern = Pattern.compile("[A-Z]");
					first_letter = first_letter.substring(0, 1);
					Matcher matcher = pattern.matcher(first_letter);
					if (!matcher.matches()) {
						// 非字母
						first_letter = "#";
					}
					//System.out.println(first_letter);
					t_interaction_contacts.setFirst_letter(first_letter);
					saveContacts(t_interaction_contacts);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Thread.sleep(50);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				message = new Message();
				message.what = message_what_values.fragment_interaction_contacts_refesh_complete;
				handler.sendMessage(message);
			} catch (Exception e2) {
				// TODO: handle exception
			}
			lock.unlock();
		}
	}

	private void saveContacts(table_interaction_contacts contacts) {
		List<table_interaction_contacts> list = null;
		String sql = "";
		try {
			dbs = new dbs_interaction_contacts(context);
			sql = "select * from t_interaction_contacts where user_id='" + contacts.getUser_id() + "'";
			list = dbs.do_select_data(sql);
			list = list == null ? new ArrayList<table_interaction_contacts>() : list;
			if (list.size() <= 0) {
				dbs.do_insert_data(contacts);
				System.out.println("已经保存了");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
