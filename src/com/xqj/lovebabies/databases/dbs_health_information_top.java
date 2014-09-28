package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbs_health_information_top extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_health_information_top_pic_info t_health_information_top = null;

	public dbs_health_information_top(Context context) {
		super(context, "db_health_information_top", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			t_health_information_top = new table_health_information_top_pic_info();
			db.execSQL(t_health_information_top.sql_create());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_health_information_top = new table_health_information_top_pic_info();
			db.execSQL(t_health_information_top.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<table_health_information_top_pic_info> do_select_data(String sql) {
		List<table_health_information_top_pic_info> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = this.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_health_information_top_pic_info>();
			while (cursor.moveToNext()) {
				t_health_information_top = new table_health_information_top_pic_info();
				t_health_information_top.setTop_id(cursor.getString(cursor.getColumnIndex("top_id")));
				t_health_information_top.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				t_health_information_top.setPic_name(cursor.getString(cursor.getColumnIndex("pic_name")));
				t_health_information_top.setS_pic_name(cursor.getString(cursor.getColumnIndex("s_pic_name")));
				list.add(t_health_information_top);
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

	public void do_insert_data(table_health_information_top_pic_info message) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("top_id", message.getTop_id());
			values.put("title", message.getTitle());
			values.put("pic_name", message.getPic_name());
			values.put("s_pic_name", message.getS_pic_name());
			db.insert("t_health_information_top", null, values);
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
