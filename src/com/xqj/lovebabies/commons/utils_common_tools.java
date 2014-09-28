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
	 * ��ȡ����ڴ�ֵ
	 */
	public static int getMaxMemory() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		return maxMemory;
	}

	/**
	 * ��ȡ���ڴ�ֵ
	 */
	public static int getTotalMemory() {
		int maxMemory = (int) Runtime.getRuntime().totalMemory();
		return maxMemory;
	}

	// ��ȡ�ֻ��Ĵ���
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
	 * �жϳ����Ƿ���ǰ̨����
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
			// Ӧ�ó���λ�ڶ�ջ�Ķ���
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	// ��ȡ����״̬
	public static boolean get_network_status(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE); // ��ȡϵͳ�������ӹ�����
		if (connectivity == null) { // ������������Ϊnull
			return false; // ����false���������޷�����
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo(); // ��ȡ���е��������Ӷ���
			if (info != null) { // ������Ϣ��Ϊnullʱ
				for (int i = 0; i < info.length; i++) { // ������·���Ӷ���
					if (info[i].isConnected()) { // ����һ���������Ӷ�������������ʱ
						return true; // ����true����������������
					}
				}
			}
		}
		return false;
	}

	// ��ȡ����SD��·������
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

	// ��ȡ�ļ�Ŀ¼��С
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

	// ��ȡ���洢�ռ��SD��Ϊ���򻺴�Ŀ¼
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
	 * �õ��������ڼ���������
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

	// ��ȡ���������ļ��Ĳ���ʱ��
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

	// ��ȡ���ֵ�ƴ��
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

	// ��ȡԲ��bitmap
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
	 * ����bitmap������SD��
	 * 
	 * @param bitmap
	 * @param path
	 * @return
	 */
	public static int saveBimap(Bitmap bitmap, String path) {
		try {
			System.out.println("saveBitmap��" + path);
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
	 * �ӱ���SD����ȡͼƬBitmap
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
	 * ��ȡָ����ʽ������
	 */
	public static String getFormatDate(Date date, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(date);
	}

	/**
	 * ��ʾͼƬ
	 * 
	 * @param imageView
	 * @param pic_path
	 */
	public static void f_display_Image(Context context, ImageView imageView,
			String pic_path, int default_onloading_resource,
			int default_empty_resource, ImageScaleType scale_type) {
		utils_picture_caches.getInstance().init(context);// ��ʼ��ͼƬ����
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
