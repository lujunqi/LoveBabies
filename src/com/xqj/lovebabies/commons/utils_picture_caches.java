package com.xqj.lovebabies.commons;

import java.io.File;

import android.content.Context;
import cn.trinea.android.common.util.PreferencesUtils;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class utils_picture_caches {
	private utils_common_tools tools = new utils_common_tools();

	private static class SingletonHolder {
		static final utils_picture_caches INSTANCE = new utils_picture_caches();
	}

	public static utils_picture_caches getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void init(Context context) {
		try {
			if (!ImageLoader.getInstance().isInited()) {// 未初始化则初始化
				// 默认缓存路径
				String cache_path = "";
				cache_path += PreferencesUtils.getString(context,
						"sys_path_sd_card");
				cache_path += PreferencesUtils.getString(context,
						"sys_path_app_folder");
				cache_path += PreferencesUtils.getString(context,
						"sys_path_cache");
				File cache = new File(cache_path);
				if (!cache.exists())
					cache.mkdirs();
				// --
				ImageLoaderConfiguration.Builder config_builder = new ImageLoaderConfiguration.Builder(
						context);
				config_builder.threadPoolSize(3);
				config_builder.diskCacheSize(700 * 1024 * 1024);
				config_builder.diskCache(new UnlimitedDiscCache(cache));
				config_builder
						.diskCacheFileNameGenerator(new Md5FileNameGenerator());
				config_builder.tasksProcessingOrder(QueueProcessingType.FIFO);
				config_builder.imageDownloader(new BaseImageDownloader(context,
						5 * 1000, 30 * 1000));
				// config_builder.writeDebugLogs();
				// --
				// ImageLoader.getInstance().destroy();
				ImageLoader.getInstance().init(config_builder.build());
			}
		} catch (Exception e) {
		}
	}
}
