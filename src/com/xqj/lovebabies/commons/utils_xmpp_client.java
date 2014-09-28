package com.xqj.lovebabies.commons;

import java.util.*;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.content.Context;
import android.util.Log;

import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.listeners.utils_xmpp_client_chat;
import com.xqj.lovebabies.listeners.utils_xmpp_client_connect;
import com.xqj.lovebabies.listeners.utils_xmpp_client_roster;

public class utils_xmpp_client {
//	private final static String server_host = "220.168.86.34";
//	private final static int server_port = 5222;
	private XMPPConnection connection = null;
	private ChatManager chat_manager = null;
	private Roster roster = null;

	private static class SingletonHolder {
		static final utils_xmpp_client INSTANCE = new utils_xmpp_client();
	}

	public static utils_xmpp_client getInstance() {
		return SingletonHolder.INSTANCE;
	}

	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取连接并接收离线消息
	public XMPPConnection get_connection() {
		try {
			if (connection == null || !connection.isConnected()) {
				create_connection();
			}
			return connection;
		} catch (Exception e) {
			return null;
		}
	}

	// 接收离线消息
	public void get_offline_message(XMPPConnection con) {
		try {
			OfflineMessageManager offline = new OfflineMessageManager(con);
			Iterator<org.jivesoftware.smack.packet.Message> it = offline.getMessages();
			while (it.hasNext()) {
				org.jivesoftware.smack.packet.Message msg = it.next();

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 创建连接
	public XMPPConnection create_connection() {
		try {
			if (connection == null || !connection.isAuthenticated()) {
				ConnectionConfiguration cfg = new ConnectionConfiguration(global_contants.im_server_ip, global_contants.im_server_port);
				cfg.setReconnectionAllowed(true);
				cfg.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
				cfg.setSendPresence(false);
				cfg.setSASLAuthenticationEnabled(false);
				cfg.setTruststorePassword("changeit");
				cfg.setTruststoreType("bks");
				connection = new XMPPConnection(cfg);
				connection.connect();
			}
			return connection;
		} catch (Exception e) {
			return null;
		}
	}

	// 关闭连接
	public void close_connection() {
		try {
			if (connection != null) {
				// 移除连接监听
				connection.getAccountManager().deleteAccount();

				// 关闭连接
				if (connection.isConnected()) {
					connection.disconnect();
				}
				connection = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 用户登录
	public boolean login_user(String uid, String pwd, Context context) {
		try {
			if (get_connection() == null) return false;
			get_connection().login(uid, pwd);
			// 更改在线状态
			Presence presence = new Presence(Presence.Type.available);
			get_connection().sendPacket(presence);
			// 添加连接监听
			get_connection().addConnectionListener(new utils_xmpp_client_connect());
			// 添加消息接收监听
			chat_manager = get_connection().getChatManager();
			chat_manager.addChatListener(new utils_xmpp_client_chat(context));
			// 添加用户状态监听
			roster = get_connection().getRoster();
			roster.addRosterListener(new utils_xmpp_client_roster(context));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 获取好友列表
	public List<RosterEntry> get_all_friends_entries() {
		List<RosterEntry> Entrieslist = null;
		try {
			if (get_connection() == null) return null;
			Entrieslist = new ArrayList<RosterEntry>();
			Collection<RosterEntry> rosterEntry = get_connection().getRoster().getEntries();
			Iterator<RosterEntry> i = rosterEntry.iterator();
			while (i.hasNext()) {
				Entrieslist.add(i.next());
			}
			return Entrieslist;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void logout_user() {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 发送消息
	public boolean send_message(String receiver, String msg) {
		if (connection == null) return false;
		try {
			if (!connection.isConnected()) {
				connection.connect();
			}
			ChatManager cm = connection.getChatManager();
			System.out.println("createChat:"+receiver + "@" + connection.getServiceName());
			Chat chat = cm.createChat(receiver + "@" + connection.getServiceName(), null);
			Message m = new Message();
			m.setBody(msg);
			chat.sendMessage(m);
			return true;
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
