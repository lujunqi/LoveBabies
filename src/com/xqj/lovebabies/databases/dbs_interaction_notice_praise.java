package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbs_interaction_notice_praise extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_interaction_notice_praise t_interaction_notice_praise = null;

	public dbs_interaction_notice_praise(Context context) {
		super(context, "db_interaction_notice_praise", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			t_interaction_notice_praise = new table_interaction_notice_praise();
			db.execSQL(t_interaction_notice_praise.sql_create());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_interaction_notice_praise = new table_interaction_notice_praise();
			db.execSQL(t_interaction_notice_praise.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<table_interaction_notice_praise> do_select_result(String sql) {
		List<table_interaction_notice_praise> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_interaction_notice_praise>();
			while (cursor.moveToNext()) {
				t_interaction_notice_praise = new table_interaction_notice_praise();
				t_interaction_notice_praise.setNotice_id(cursor.getString(cursor.getColumnIndex("notice_id")));
				t_interaction_notice_praise.setPraise_user_icon(cursor.getString(cursor.getColumnIndex("praise_user_icon")));
				t_interaction_notice_praise.setPraise_user_id(cursor.getString(cursor.getColumnIndex("praise_user_id")));
				t_interaction_notice_praise.setPraise_user_nike_name(cursor.getString(cursor.getColumnIndex("praise_user_nike_name")));
				list.add(t_interaction_notice_praise);
			}
			return list;
		} catch (Exception e) {
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

	public boolean do_insert_data(table_interaction_notice_praise data) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("Notice_id", data.getNotice_id());
			values.put("Praise_user_icon", data.getPraise_user_icon());
			values.put("Praise_user_id", data.getPraise_user_id());
			values.put("Praise_user_nike_name", data.getPraise_user_nike_name());
			db.insert("t_interaction_notice_praise", null, values);
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
