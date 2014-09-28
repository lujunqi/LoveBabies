package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbs_interaction_notice extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_interaction_notice t_interaction_notice = null;

	public dbs_interaction_notice(Context context) {
		super(context, "db_interaction_notice", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		try {
			t_interaction_notice = new table_interaction_notice();
			db.execSQL(t_interaction_notice.sql_create());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			t_interaction_notice = new table_interaction_notice();
			db.execSQL(t_interaction_notice.sql_drop());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public List<table_interaction_notice> do_select_result(String sql) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		List<table_interaction_notice> list = null;
		try {
			lock.lock();
			db = this.getReadableDatabase();
			list = new ArrayList<table_interaction_notice>();
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				t_interaction_notice = new table_interaction_notice();
				t_interaction_notice.setNotice_collect_count(cursor.getString(cursor.getColumnIndex("notice_collect_count")));
				t_interaction_notice.setNotice_comment_count(cursor.getString(cursor.getColumnIndex("notice_comment_count")));
				t_interaction_notice.setNotice_content(cursor.getString(cursor.getColumnIndex("notice_content")));
				t_interaction_notice.setNotice_download_count(cursor.getString(cursor.getColumnIndex("notice_download_count")));
				t_interaction_notice.setNotice_id(cursor.getString(cursor.getColumnIndex("notice_id")));
				t_interaction_notice.setNotice_org_name(cursor.getString(cursor.getColumnIndex("notice_org_name")));
				t_interaction_notice.setNotice_picture_breviary(cursor.getString(cursor.getColumnIndex("notice_picture_breviary")));
				t_interaction_notice.setNotice_picture_detailed(cursor.getString(cursor.getColumnIndex("notice_picture_detailed")));
				t_interaction_notice.setNotice_praise_count(cursor.getString(cursor.getColumnIndex("notice_praise_count")));
				t_interaction_notice.setNotice_publish_time(cursor.getString(cursor.getColumnIndex("notice_publish_time")));
				t_interaction_notice.setNotice_read_count(cursor.getString(cursor.getColumnIndex("notice_read_count")));
				t_interaction_notice.setNotice_sender_icon(cursor.getString(cursor.getColumnIndex("notice_sender_icon")));
				t_interaction_notice.setNotice_sender_id(cursor.getString(cursor.getColumnIndex("notice_sender_id")));
				t_interaction_notice.setNotice_sender_name(cursor.getString(cursor.getColumnIndex("notice_sender_name")));
				t_interaction_notice.setNotice_share_count(cursor.getString(cursor.getColumnIndex("notice_share_count")));
				t_interaction_notice.setNotice_title(cursor.getString(cursor.getColumnIndex("notice_title")));
				t_interaction_notice.setNotice_type_name(cursor.getString(cursor.getColumnIndex("notice_type_name")));
				list.add(t_interaction_notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
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
		return list;
	}

	public boolean do_insert_data(table_interaction_notice tab) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("Notice_collect_count", tab.getNotice_collect_count());
			values.put("Notice_comment_count", tab.getNotice_comment_count());
			values.put("Notice_content", tab.getNotice_content());
			values.put("Notice_download_count", tab.getNotice_download_count());
			values.put("Notice_id", tab.getNotice_id());
			values.put("Notice_org_name", tab.getNotice_org_name());
			values.put("Notice_picture_breviary", tab.getNotice_picture_breviary());
			values.put("Notice_picture_detailed", tab.getNotice_picture_detailed());
			values.put("Notice_praise_count", tab.getNotice_praise_count());
			values.put("Notice_publish_time", tab.getNotice_publish_time());
			values.put("Notice_read_count", tab.getNotice_read_count());
			values.put("Notice_sender_icon", tab.getNotice_sender_icon());
			values.put("Notice_sender_id", tab.getNotice_sender_id());
			values.put("Notice_sender_name", tab.getNotice_sender_name());
			values.put("Notice_share_count", tab.getNotice_share_count());
			values.put("Notice_title", tab.getNotice_title());
			values.put("Notice_type_name", tab.getNotice_type_name());
			db.insert("t_interaction_notice", null, values);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
