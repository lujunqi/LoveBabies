package com.xqj.lovebabies.listeners;

import java.util.Collection;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;

//用户添加、删除、修改、在离线状态监听
public class utils_xmpp_client_roster implements RosterListener {
	private Context context = null;

	public utils_xmpp_client_roster(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void entriesAdded(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void presenceChanged(Presence arg0) {
		// TODO Auto-generated method stub
	}
}
