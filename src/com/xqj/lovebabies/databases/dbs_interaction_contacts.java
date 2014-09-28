package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbs_interaction_contacts extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_interaction_contacts t_interaction_contacts = null;

	public dbs_interaction_contacts(Context context) {
		super(context, "db_intercation_contacts", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			t_interaction_contacts = new table_interaction_contacts();
			db.execSQL(t_interaction_contacts.sql_create());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_interaction_contacts = new table_interaction_contacts();
			db.execSQL(t_interaction_contacts.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<table_interaction_contacts> do_select_data(String sql) {
		List<table_interaction_contacts> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = this.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_interaction_contacts>();
			while (cursor.moveToNext()) {
				t_interaction_contacts = new table_interaction_contacts();
				t_interaction_contacts.setUser_icon_path(cursor.getString(cursor.getColumnIndex("user_icon_path")));
				t_interaction_contacts.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
				t_interaction_contacts.setUser_last_session_content(cursor.getString(cursor.getColumnIndex("user_last_session_content")));
				t_interaction_contacts.setUser_last_session_time(cursor.getString(cursor.getColumnIndex("user_last_session_time")));
				t_interaction_contacts.setUser_nike_name(cursor.getString(cursor.getColumnIndex("user_nike_name")));
				t_interaction_contacts.setUser_signature(cursor.getString(cursor.getColumnIndex("user_signature")));
				t_interaction_contacts.setUser_status(cursor.getString(cursor.getColumnIndex("user_status")));
				t_interaction_contacts.setUser_phone(cursor.getString(cursor.getColumnIndex("user_phone")));
				t_interaction_contacts.setUser_real_name(cursor.getString(cursor.getColumnIndex("user_real_name")));
				t_interaction_contacts.setUser_sex(cursor.getString(cursor.getColumnIndex("user_sex")));
				t_interaction_contacts.setFirst_letter(cursor.getString(cursor.getColumnIndex("first_letter")));
				list.add(t_interaction_contacts);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				cursor.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				db.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			lock.unlock();
		}
	}

	public void do_insert_data(table_interaction_contacts message) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("User_icon_path", message.getUser_icon_path());
			values.put("user_id", message.getUser_id());
			values.put("user_last_session_content", message.getUser_last_session_content());
			values.put("user_last_session_time", message.getUser_last_session_time());
			values.put("user_nike_name", message.getUser_nike_name());
			values.put("User_signature", message.getUser_signature());
			values.put("user_status", message.getUser_status());
			values.put("user_phone", message.getUser_phone());
			values.put("user_real_name", message.getUser_real_name());
			values.put("user_sex", message.getUser_sex());
			values.put("first_letter", message.getFirst_letter());
			db.insert("t_interaction_contacts", null, values);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				db.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			lock.unlock();
		}
	}

	public boolean do_execute_sql(String sql) {
		SQLiteDatabase db = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				db.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			lock.unlock();
		}
	}
}
