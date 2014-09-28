package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbs_my_center_my_points extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_my_center_my_points t_my_center_my_points = null;

	public dbs_my_center_my_points(Context context) {
		super(context, "db_my_center_my_points", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			t_my_center_my_points = new table_my_center_my_points();
			db.execSQL(t_my_center_my_points.sql_create());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_my_center_my_points = new table_my_center_my_points();
			db.execSQL(t_my_center_my_points.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
							
	public List<table_my_center_my_points> do_select_data(String sql) {
		List<table_my_center_my_points> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = this.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_my_center_my_points>();
			while (cursor.moveToNext()) {
				t_my_center_my_points = new table_my_center_my_points();
				t_my_center_my_points.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
				t_my_center_my_points.setConsume_style(cursor.getString(cursor.getColumnIndex("consume_style")));
				t_my_center_my_points.setIntegral_count(cursor.getString(cursor.getColumnIndex("integral_count")));
				t_my_center_my_points.setTime(cursor.getString(cursor.getColumnIndex("time")));
				t_my_center_my_points.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
				list.add(t_my_center_my_points);
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
	
	public void do_insert_data(table_my_center_my_points message) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("user_id", message.getUser_id());
			values.put("consume_style", message.getConsume_style());
			values.put("integral_count", message.getIntegral_count());
			values.put("time", message.getTime());
			values.put("remark", message.getRemark());
			db.insert("t_my_center_my_points", null, values);
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
