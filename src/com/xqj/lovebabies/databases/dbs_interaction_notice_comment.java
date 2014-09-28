package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.*;
import android.database.sqlite.SQLiteOpenHelper;

public class dbs_interaction_notice_comment extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_interaction_notice_comment t_interaction_notice_comment = null;

	public dbs_interaction_notice_comment(Context context, String name, CursorFactory factory, int version) {
		super(context, "db_interaction_notice_comment", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			t_interaction_notice_comment = new table_interaction_notice_comment();
			db.execSQL(t_interaction_notice_comment.sql_create());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_interaction_notice_comment = new table_interaction_notice_comment();
			db.execSQL(t_interaction_notice_comment.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<table_interaction_notice_comment> do_select_result(String sql) {
		List<table_interaction_notice_comment> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_interaction_notice_comment>();
			while (cursor.moveToNext()) {
				try {
					t_interaction_notice_comment = new table_interaction_notice_comment();
					t_interaction_notice_comment.setComment_content(cursor.getString(cursor.getColumnIndex("comment_content")));
					t_interaction_notice_comment.setComment_id(cursor.getString(cursor.getColumnIndex("comment_id")));
					t_interaction_notice_comment.setComment_time(cursor.getString(cursor.getColumnIndex("comment_time")));
					t_interaction_notice_comment.setComment_user_icon(cursor.getString(cursor.getColumnIndex("comment_user_icon")));
					t_interaction_notice_comment.setComment_user_id(cursor.getString(cursor.getColumnIndex("comment_user_id")));
					t_interaction_notice_comment.setComment_user_nike_name(cursor.getString(cursor.getColumnIndex("comment_user_nike_name")));
					t_interaction_notice_comment.setNotice_id(cursor.getString(cursor.getColumnIndex("notice_id")));
					list.add(t_interaction_notice_comment);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			lock.unlock();
		}
	}

	public boolean do_insert_data(table_interaction_notice_comment data) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("Comment_content", data.getComment_content());
			values.put("Comment_id", data.getComment_id());
			values.put("Comment_time", data.getComment_time());
			values.put("Comment_user_icon", data.getComment_user_icon());
			values.put("Comment_user_id", data.getComment_user_id());
			values.put("Comment_user_nike_name", data.getComment_user_nike_name());
			values.put("Notice_id", data.getNotice_id());
			db.insert("t_interaction_notice_comment", null, values);
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
