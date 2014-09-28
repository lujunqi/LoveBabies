package com.xqj.lovebabies.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class dbs_interaction_news extends SQLiteOpenHelper {
	public static ReentrantLock lock = new ReentrantLock();
	private table_interaction_news t_interaction_news = null;

	public dbs_interaction_news(Context context) {
		super(context, "db_interaction_news", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		try {
			t_interaction_news = new table_interaction_news();
			arg0.execSQL(t_interaction_news.sql_create());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		try {
			t_interaction_news = new table_interaction_news();
			arg0.execSQL(t_interaction_news.sql_drop());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<table_interaction_news> do_select_data(String sql) {
		List<table_interaction_news> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			lock.lock();
			db = this.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			list = new ArrayList<table_interaction_news>();
			while (cursor.moveToNext()) {
				t_interaction_news = new table_interaction_news();
				t_interaction_news.setCollect_count(cursor.getString(cursor.getColumnIndex("collect_count")));
				t_interaction_news.setComment_count(cursor.getString(cursor.getColumnIndex("comment_count")));
				t_interaction_news.setDown_count(cursor.getString(cursor.getColumnIndex("down_count")));
				t_interaction_news.setNews_content(cursor.getString(cursor.getColumnIndex("news_content")));
				t_interaction_news.setNews_id(cursor.getString(cursor.getColumnIndex("news_id")));
				t_interaction_news.setNews_org_name(cursor.getString(cursor.getColumnIndex("news_org_name")));
				t_interaction_news.setNews_title(cursor.getString(cursor.getColumnIndex("news_title")));
				t_interaction_news.setPicture_path(cursor.getString(cursor.getColumnIndex("picture_path")));
				t_interaction_news.setPraise_count(cursor.getString(cursor.getColumnIndex("praise_count")));
				t_interaction_news.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time")));
				t_interaction_news.setRead_count(cursor.getString(cursor.getColumnIndex("read_count")));
				t_interaction_news.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
				t_interaction_news.setShare_count(cursor.getString(cursor.getColumnIndex("share_count")));
				t_interaction_news.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
				t_interaction_news.setVideo_path(cursor.getString(cursor.getColumnIndex("video_path")));
				list.add(t_interaction_news);
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

	public boolean do_insert_data(table_interaction_news message) {
		SQLiteDatabase db = null;
		ContentValues values = null;
		try {
			lock.lock();
			db = this.getWritableDatabase();
			values = new ContentValues();
			values.put("message_content", message.getCollect_count());
			values.put("message_content_length", message.getComment_count());
			values.put("Message_direction_type", message.getDown_count());
			values.put("Message_media_type", message.getNews_content());
			values.put("Message_read_status", message.getNews_id());
			values.put("Message_occurrence_time", message.getNews_org_name());
			values.put("Message_receiver", message.getNews_title());
			values.put("Message_sender", message.getPicture_path());
			values.put("message_content", message.getPraise_count());
			values.put("message_content_length", message.getPublish_time());
			values.put("Message_direction_type", message.getRead_count());
			values.put("Message_media_type", message.getRemarks());
			values.put("Message_read_status", message.getShare_count());
			values.put("Message_occurrence_time", message.getUser_id());
			values.put("Message_receiver", message.getVideo_path());
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
}
