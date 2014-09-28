package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbs_album_baby_growth extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_album_baby_growth t_album_baby_growth = null;

	public dbs_album_baby_growth(Context context) {
		super(context, "db_album_baby_growth", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			t_album_baby_growth = new table_album_baby_growth();
			db.execSQL(t_album_baby_growth.sql_create());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_album_baby_growth = new table_album_baby_growth();
			db.execSQL(t_album_baby_growth.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<table_album_baby_growth> do_select_data(String sql) {
		List<table_album_baby_growth> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = this.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_album_baby_growth>();
			while (cursor.moveToNext()) {
				t_album_baby_growth = new table_album_baby_growth();
				t_album_baby_growth.setBaby_id(cursor.getString(cursor.getColumnIndex("baby_id")));
				t_album_baby_growth.setRecord_id(cursor.getString(cursor.getColumnIndex("record_id")));
				t_album_baby_growth.setRecord_time(cursor.getString(cursor.getColumnIndex("record_time")));
				t_album_baby_growth.setLocations(cursor.getString(cursor.getColumnIndex("locations")));
				t_album_baby_growth.setAge_true(cursor.getString(cursor.getColumnIndex("age_true")));
				t_album_baby_growth.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
				t_album_baby_growth.setHeight(cursor.getString(cursor.getColumnIndex("height")));
				t_album_baby_growth.setPic_name(cursor.getString(cursor.getColumnIndex("pic_name")));
				t_album_baby_growth.setWord_record(cursor.getString(cursor.getColumnIndex("word_record")));
				list.add(t_album_baby_growth);
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

	public void do_insert_data(table_album_baby_growth message) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("baby_id", message.getBaby_id());
			values.put("record_id", message.getRecord_id());
			values.put("record_time", message.getRecord_time());
			values.put("locations", message.getLocations());
			values.put("age_true", message.getAge_true());
			values.put("weight", message.getWeight());
			values.put("height", message.getHeight());
			values.put("pic_name", message.getPic_name());
			values.put("word_record", message.getWord_record());
			db.insert("t_album_baby_growth", null, values);
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
