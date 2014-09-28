package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbs_health_information extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_health_information t_health_information = null;

	public dbs_health_information(Context context) {
		super(context, "db_health_information", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			t_health_information = new table_health_information();
			db.execSQL(t_health_information.sql_create());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_health_information = new table_health_information();
			db.execSQL(t_health_information.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<table_health_information> do_select_data(String sql) {
		List<table_health_information> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = this.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_health_information>();
			while (cursor.moveToNext()) {
				t_health_information = new table_health_information();
				t_health_information.setContent_id(cursor.getString(cursor.getColumnIndex("content_id")));
				t_health_information.setContent(cursor.getString(cursor.getColumnIndex("content")));
				t_health_information.setCollect_count(cursor.getString(cursor.getColumnIndex("collect_count")));
				t_health_information.setPic_name(cursor.getString(cursor.getColumnIndex("pic_name")));
				t_health_information.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time")));
				t_health_information.setS_pic_name(cursor.getString(cursor.getColumnIndex("s_pic_name")));
				t_health_information.setShare_count(cursor.getString(cursor.getColumnIndex("share_count")));
				t_health_information.setSource(cursor.getString(cursor.getColumnIndex("source")));
				t_health_information.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				list.add(t_health_information);
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

	public void do_insert_data(table_health_information message) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("content_id", message.getContent_id());
			values.put("title", message.getTitle());
			values.put("content", message.getContent());
			values.put("publish_time", message.getPublish_time());
			values.put("collect_count", message.getCollect_count());
			values.put("share_count", message.getShare_count());
			values.put("source", message.getSource());
			values.put("pic_name", message.getPic_name());
			values.put("s_pic_name", message.getS_pic_name());
			db.insert("t_health_information", null, values);
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
