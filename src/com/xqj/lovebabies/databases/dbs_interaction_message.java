package com.xqj.lovebabies.databases;

import java.util.*;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.concurrent.locks.*;

public class dbs_interaction_message extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_interaction_message t_interaction_message = null;

	public dbs_interaction_message(Context context) {
		super(context, "db_interaction_message", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			t_interaction_message = new table_interaction_message();
			db.execSQL(t_interaction_message.sql_create());

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_interaction_message = new table_interaction_message();
			db.execSQL(t_interaction_message.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<table_interaction_message> do_select_data(String sql) {
		List<table_interaction_message> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = this.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_interaction_message>();
			while (cursor.moveToNext()) {
				t_interaction_message = new table_interaction_message();
				t_interaction_message.setMessage_direction_type(cursor.getString(cursor.getColumnIndex("message_direction_type")));
				t_interaction_message.setMessage_id(cursor.getInt(cursor.getColumnIndex("message_id")));
				t_interaction_message.setMessage_media_type(cursor.getString(cursor.getColumnIndex("message_media_type")));
				t_interaction_message.setMessage_read_status(cursor.getString(cursor.getColumnIndex("message_read_status")));
				t_interaction_message.setMessage_occurrence_time(cursor.getString(cursor.getColumnIndex("message_occurrence_time")));
				t_interaction_message.setMessage_receiver(cursor.getString(cursor.getColumnIndex("message_receiver")));
				t_interaction_message.setMessage_sender(cursor.getString(cursor.getColumnIndex("message_sender")));
				t_interaction_message.setMessage_content(cursor.getString(cursor.getColumnIndex("message_content")));
				t_interaction_message.setMessage_content_length(cursor.getString(cursor.getColumnIndex("message_content_length")));
				list.add(t_interaction_message);
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

	public boolean do_insert_data(table_interaction_message message) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("message_content", message.getMessage_content());
			values.put("message_content_length", message.getMessage_content_length());
			values.put("Message_direction_type", message.getMessage_direction_type());
			values.put("Message_media_type", message.getMessage_media_type());
			values.put("Message_read_status", message.getMessage_read_status());
			values.put("Message_occurrence_time", message.getMessage_occurrence_time());
			values.put("Message_receiver", message.getMessage_receiver());
			values.put("Message_sender", message.getMessage_sender());
			db.insert("t_interaction_message", null, values);
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

	public boolean do_execute_sql(String sql) {
		SQLiteDatabase db = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
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
