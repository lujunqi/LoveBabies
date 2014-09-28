package com.xqj.lovebabies.commons;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.storage.StorageManager;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import cn.trinea.android.common.util.ImageUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class utils_common_tools {

	/**
	 * 获取最大内存值
	 */
	public static int getMaxMemory() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		return maxMemory;
	}

	/**
	 * 获取总内存值
	 */
	public static int getTotalMemory() {
		int maxMemory = (int) Runtime.getRuntime().totalMemory();
		return maxMemory;
	}

	// 获取手机的串号
	public String get_phone_imei(Context context) {
		TelephonyManager telephonemanage = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			return telephonemanage.getDeviceId();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断程序是否在前台运行
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean isTopActivity(Activity activity) {
		String packageName = "com.xqj.lovebabies";
		ActivityManager activityManager = (ActivityManager) activity
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	// 获取网络状态
	public static boolean get_network_status(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取系统网络连接管理器
		if (connectivity == null) { // 如果网络管理器为null
			return false; // 返回false表明网络无法连接
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo(); // 获取所有的网络连接对象
			if (info != null) { // 网络信息不为null时
				for (int i = 0; i < info.length; i++) { // 遍历网路连接对象
					if (info[i].isConnected()) { // 当有一个网络连接对象连接上网络时
						return true; // 返回true表明网络连接正常
					}
				}
			}
		}
		return false;
	}

	// 获取外置SD卡路径数组
	public String[] get_dirs_path(Context context) {
		try {
			StorageManager sm = (StorageManager) context
					.getSystemService(Context.STORAGE_SERVICE);
			return (String[]) sm.getClass().getMethod("getVolumePaths", null)
					.invoke(sm, null);
		} catch (Exception e) {
			return null;
		}
	}

	// 获取文件目录大小
	public long get_dir_size(String path) {
		try {
			android.os.StatFs statfs = new android.os.StatFs(path);
			long nAvailaBlock = statfs.getAvailableBlocks();
			long nBlocSize = statfs.getBlockSize();
			long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;
			return nSDFreeSize;
		} catch (Exception e) {
			return 0;
		}
	}

	// 获取最大存储空间的SD卡为程序缓存目录
	public String get_application_dir_path(Context context) {
		String current_path = null;
		long current_size = 0;
		String path = null;
		long size = 0;
		try {
			String[] dirs = get_dirs_path(context);
			dirs = dirs == null ? new String[0] : dirs;
			for (int i = 0; i < dirs.length; i++) {
				path = dirs[i];
				size = get_dir_size(path);
				if (size > current_size) {
					current_path = path;
					current_size = size;
				}
			}
			return current_path;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 得到两个日期间相差的秒数
	 */
	public long get_second_differ(Date sj1, Date sj2) {
		long in = sj1.getTime();
		long out = sj2.getTime();
		long differ = out - in;
		if (differ < 0) {
			differ = differ * (-1);
		}
		long seconds = differ / 1000;
		return seconds;
	}

	// 获取网络语音文件的播放时长
	public String get_voice_length(String url) {
		MediaPlayer mp = null;
		int voice_length = 0;
		try {
			mp = new MediaPlayer();
			mp.setDataSource(url);
			mp.prepare();
			voice_length = mp.getDuration();
			return "" + (voice_length / 1000) + "\"";
		} catch (Exception e) {
			return "0\"";
		} finally {
			mp.release();
		}
	}

	// 获取汉字的拼音
	public String get_Hanzi_spell(String hanzi) {
		String spell = "#";
		try {
			HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
			pyFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
			spell = PinyinHelper.toHanyuPinyinString(hanzi, pyFormat, "");
			return spell;
		} catch (Exception e) {
			return "#";
		}
	}

	// Drawable from Resource

	public Drawable get_drawable_from_res(Resources res, int rid, int width,
			int height) {
		Drawable drawable = null;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeResource(res, rid);
			bitmap = ImageUtils.scaleImageTo(bitmap, width, height);
			drawable = ImageUtils.bitmapToDrawable(bitmap);
			return drawable;
		} catch (Exception e) {
			return null;
		} finally {

		}
	}

	// 获取圆形bitmap
	public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	/**
	 * 保存bitmap到本地SD卡
	 * 
	 * @param bitmap
	 * @param path
	 * @return
	 */
	public static int saveBimap(Bitmap bitmap, String path) {
		try {
			System.out.println("saveBitmap：" + path);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(path, false));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	/**
	 * 从本地SD卡获取图片Bitmap
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getBitmapFromSDCard(String path) {
		Bitmap bitmap = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(path);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}

	/**
	 * 获取指定格式的日期
	 */
	public static String getFormatDate(Date date, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(date);
	}

	/**
	 * 显示图片
	 * 
	 * @param imageView
	 * @param pic_path
	 */
	public static void f_display_Image(Context context, ImageView imageView,
			String pic_path, int default_onloading_resource,
			int default_empty_resource, ImageScaleType scale_type) {
		utils_picture_caches.getInstance().init(context);// 初始化图片缓存
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.showImageOnLoading(default_onloading_resource);
		builder.showImageForEmptyUri(default_empty_resource);
		builder.cacheInMemory(false);
		builder.cacheOnDisk(true);
		builder.imageScaleType(scale_type);
		DisplayImageOptions options = builder.build();
		ImageLoader.getInstance().displayImage(pic_path, imageView, options);
	}

}
