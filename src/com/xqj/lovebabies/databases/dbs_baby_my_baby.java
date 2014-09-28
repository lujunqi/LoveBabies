package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbs_baby_my_baby extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_album_my_baby t_baby_my_baby = null;

	public dbs_baby_my_baby(Context context) {
		super(context, "db_baby_my_baby", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			t_baby_my_baby = new table_album_my_baby();
			db.execSQL(t_baby_my_baby.sql_create());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_baby_my_baby = new table_album_my_baby();
			db.execSQL(t_baby_my_baby.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<table_album_my_baby> do_select_data(String sql) {
		List<table_album_my_baby> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = this.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_album_my_baby>();
			while (cursor.moveToNext()) {
				t_baby_my_baby = new table_album_my_baby();
				t_baby_my_baby.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
				t_baby_my_baby.setBaby_id(cursor.getString(cursor.getColumnIndex("baby_id")));
				t_baby_my_baby.setBaby_age(cursor.getString(cursor.getColumnIndex("baby_age")));
				t_baby_my_baby.setBaby_name(cursor.getString(cursor.getColumnIndex("baby_name")));
				t_baby_my_baby.setBaby_pic(cursor.getString(cursor.getColumnIndex("baby_pic")));
				t_baby_my_baby.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
				t_baby_my_baby.setBaby_sex(cursor.getString(cursor.getColumnIndex("baby_sex")));
				t_baby_my_baby.setRelation(cursor.getString(cursor.getColumnIndex("relation")));
				t_baby_my_baby.setPermissons(cursor.getString(cursor.getColumnIndex("permissons")));
				list.add(t_baby_my_baby);
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

	public void do_insert_data(table_album_my_baby message) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("baby_id", message.getBaby_id());
			values.put("user_id", message.getUser_id());
			values.put("baby_age", message.getBaby_age());
			values.put("baby_name", message.getBaby_name());
			values.put("baby_pic", message.getBaby_pic());
			values.put("birthday", message.getBirthday());
			values.put("baby_sex", message.getBaby_sex());
			values.put("relation", message.getRelation());
			values.put("permissons", message.getPermissons());
			System.out.println("do_insert_data  permissons-->"+message.getPermissons());
			db.insert("t_baby_my_baby", null, values);
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
