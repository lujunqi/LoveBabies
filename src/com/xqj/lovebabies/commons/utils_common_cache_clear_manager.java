package com.xqj.lovebabies.commons;

import java.io.File;

import com.xqj.lovebabies.databases.dbs_album_baby_growth;
import com.xqj.lovebabies.databases.dbs_baby_my_baby;
import com.xqj.lovebabies.databases.dbs_interaction_contacts;
import com.xqj.lovebabies.databases.dbs_interaction_message;
import com.xqj.lovebabies.databases.dbs_interaction_notice;

import android.content.Context;
import android.os.Environment;

/**
 * 缓存清除管理器
 * @author sunshine
 *
 */
public class utils_common_cache_clear_manager {
	/** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /** * 按名字清除本应用数据库 * * @param context * @param dbName */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /** * 清除本应用所有的数据 * * @param context * @param filepath */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
    
    /**
     * 删除目录下所有文件以及文件夹
     * @param filePath
     */
    public static void deleteAllFiles(File file){
    	if (file != null && file.exists()) {
    		if(file.isDirectory()){
    			for(File item : file.listFiles()) {
    				deleteAllFiles(item);
                }
    		}else{
    			file.delete();
    		}
        }
    }
    
    /**
     * 清除数据库数据
     */
    public static void deleteDBDatas(Context ctx){
    	dbs_interaction_contacts db_interaction_contacts = new dbs_interaction_contacts(ctx);
		db_interaction_contacts.do_execute_sql("delete from t_interaction_contacts");
		dbs_interaction_message db_interaction_message = new dbs_interaction_message(ctx);
		db_interaction_message.do_execute_sql("delete from t_interaction_message");
		dbs_interaction_notice db_interaction_notice = new dbs_interaction_notice(ctx);
		db_interaction_notice.do_execute_sql("delete from t_interaction_notice");
		dbs_album_baby_growth db_album_baby_growth = new dbs_album_baby_growth(ctx);
		db_album_baby_growth.do_execute_sql("delete from t_album_baby_growth");
		dbs_baby_my_baby db_baby_my_baby = new dbs_baby_my_baby(ctx);
		db_baby_my_baby.do_execute_sql("delete from t_baby_my_baby");
    }
    	
}
