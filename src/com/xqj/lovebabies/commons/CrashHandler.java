package com.xqj.lovebabies.commons;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.structures.interface_app_upload_file_req;
import com.xqj.lovebabies.structures.interface_app_upload_file_resp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * 
 * UncaughtExceptionHandler���߳�δ�����쳣����������������δ�����쳣�ġ� 
 *                           ������������δ�����쳣Ĭ�������������ǿ�йرնԻ���
 *                           ʵ�ָýӿڲ�ע��Ϊ�����е�Ĭ��δ�����쳣���� 
 *                           ������δ�����쳣����ʱ���Ϳ�����Щ�쳣�������
 *                           ���磺�ռ��쳣��Ϣ�����ʹ��󱨸� �ȡ�
 * 
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�ɸ������ӹܳ���,����¼���ʹ��󱨸�.
 */
public class CrashHandler implements UncaughtExceptionHandler {
	/** Debug Log Tag */
	public static final String TAG = "CrashHandler";
	/** �Ƿ�����־���, ��Debug״̬�¿���, ��Release״̬�¹ر��������������� */
	public static final boolean DEBUG = true;
	/** CrashHandlerʵ�� */
	private static CrashHandler INSTANCE;
	/** �����Context���� */
	private Context mContext;
	
	/** ϵͳĬ�ϵ�UncaughtException������ */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	/** ʹ��Properties�������豸����Ϣ�ʹ����ջ��Ϣ */
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";
	private String versionName = "";
	/** ���󱨸��ļ�����չ�� */
	private String crash_file_path = "";
	private static final String CRASH_REPORTER_EXTENSION = ".txt";
	
	/** ��ֻ֤��һ��CrashHandlerʵ�� */
	private CrashHandler() {
	}

	/** ��ȡCrashHandlerʵ�� ,����ģʽ */
	public static CrashHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}
	
	/**
	 * ��ʼ��,ע��Context����, ��ȡϵͳĬ�ϵ�UncaughtException������, ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
	 * 
	 * @param ctx
	 */
	public void init(Context cxt) {
		mContext = cxt;
		crash_file_path = PreferencesUtils.getString(mContext, global_contants.SYS_PATH_SD_CARD) 
				+ PreferencesUtils.getString(mContext, global_contants.SYS_PATH_APP_FOLDER);
		crash_file_path = crash_file_path + PreferencesUtils.getString(mContext, global_contants.SYS_PATH_CRASH);
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	/**
	 * ��UncaughtException����ʱ��ת��ú���������
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// ����û�û�д�������ϵͳĬ�ϵ��쳣������������
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// Sleepһ����������
			// �����߳�ֹͣһ����Ϊ����ʾToast��Ϣ���û���Ȼ��Kill����
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "Error : ", e);
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
		}
	}

	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����. �����߿��Ը����Լ���������Զ����쳣�����߼�
	 * 
	 * @param ex
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		final String msg = ex.getLocalizedMessage();
		// ʹ��Toast����ʾ�쳣��Ϣ
		new Thread() {
			@Override
			public void run() {
				// Toast ��ʾ��Ҫ������һ���̵߳���Ϣ������
				Looper.prepare();
				Toast.makeText(mContext, "���������:" + msg, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		// �ռ��豸��Ϣ
//		collectCrashDeviceInfo(mContext);
		// ������󱨸��ļ�
		String crashFileName = saveCrashInfoToFile(ex);
//		// ���ʹ��󱨸浽������
//		sendCrashReportsToServer(mContext);
//		String exCrashStr = getExCrashInfo(ex);
//		String deviceInfo = getDeviceInfo(mContext);
		postReport2Server(crash_file_path + "/" + crashFileName);
		System.out.println("----------------------------------CrashHander  file dir-----------------------------------");
		System.out.println(crash_file_path + "/" + crashFileName);
		return true;
	}

	/**
	 * �ռ�����������豸��Ϣ
	 * 
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			// Class for retrieving various kinds of information related to the
			// application packages that are currently installed on the device.
			// You can find this class through getPackageManager().
			PackageManager pm = ctx.getPackageManager();
			// getPackageInfo(String packageName, int flags)
			// Retrieve overall information about an application package that is installed on the system.
			// public static final int GET_ACTIVITIES
			// Since: API Level 1 PackageInfo flag: return information about activities in the package in activities.
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				// public String versionName The version name of this package,
				// as specified by the <manifest> tag's versionName attribute.
				mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
				this.versionName = pi.versionName == null ? "" : pi.versionName;
				// public int versionCode The version number of this package, 
				// as specified by the <manifest> tag's versionCode attribute.
				mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Error while collect package info", e);
		}
		// ʹ�÷������ռ��豸��Ϣ.��Build���а��������豸��Ϣ,
		// ����: ϵͳ�汾��,�豸������ �Ȱ������Գ����������Ϣ
		// ���� Field �����һ�����飬��Щ����ӳ�� Class ��������ʾ�����ӿ��������������ֶ�
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				// setAccessible(boolean flag)
				// ���˶���� accessible ��־����Ϊָʾ�Ĳ���ֵ��
				// ͨ������Accessible����Ϊtrue,���ܶ�˽�б������з��ʣ���Ȼ��õ�һ��IllegalAccessException���쳣
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get(null));
				if (DEBUG) {
					Log.d(TAG, field.getName() + " : " + field.get(null));
				}
			} catch (Exception e) {
				Log.e(TAG, "Error while collect crash info", e);
			}
		}
	}
	
	/**
	 * ��ȡ�쳣����
	 * @param ex
	 * @return
	 */
	private String getExCrashInfo(Throwable ex) {
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		// printStackTrace(PrintWriter s)
		// ���� throwable ����׷�������ָ���� PrintWriter
		ex.printStackTrace(printWriter);

		// getCause() ���ش� throwable �� cause����� cause �����ڻ�δ֪���򷵻� null��
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		// toString() ���ַ�������ʽ���ظû������ĵ�ǰֵ��
		String result = info.toString();
		printWriter.close();
		return result;
	}
	
	/**
	 * �ռ�����������豸��Ϣ
	 * 
	 * @param ctx
	 */
	public String getDeviceInfo(Context ctx) {
		String deviceInfo = "";
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				this.versionName = pi.versionName == null ? "" : pi.versionName;
				deviceInfo += " VERSION_NAME="+pi.versionName+"\n";
				deviceInfo += " VERSION_CODE="+pi.versionCode+"\n";
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Error while collect package info", e);
		}
		// ʹ�÷������ռ��豸��Ϣ.��Build���а��������豸��Ϣ,
		// ����: ϵͳ�汾��,�豸������ �Ȱ������Գ����������Ϣ
		// ���� Field �����һ�����飬��Щ����ӳ�� Class ��������ʾ�����ӿ��������������ֶ�
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				// setAccessible(boolean flag)
				// ���˶���� accessible ��־����Ϊָʾ�Ĳ���ֵ��
				// ͨ������Accessible����Ϊtrue,���ܶ�˽�б������з��ʣ���Ȼ��õ�һ��IllegalAccessException���쳣
				field.setAccessible(true);
				deviceInfo += field.getName()+"="+field.get(null)+"\n";

				if (DEBUG) {
					Log.d(TAG, field.getName() + " : " + field.get(null));
				}
			} catch (Exception e) {
				Log.e(TAG, "Error while collect crash info", e);
			}
		}
		return deviceInfo;
	}
	
	/**
	 * �����ֻ���Ϣ
	 * @param pw
	 * @throws NameNotFoundException
	 */
	private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
		PackageManager pm = mContext.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
				PackageManager.GET_ACTIVITIES);
		pw.print("App Version: ");
		pw.print(pi.versionName);
		pw.print('_');
		pw.println(pi.versionCode);
		pw.print("OS Version: ");
		pw.print(Build.VERSION.RELEASE);
		pw.print("_");
		pw.println(Build.VERSION.SDK_INT);
		pw.print("Vendor: ");
		pw.println(Build.MANUFACTURER);
		pw.print("Model: ");
		pw.println(Build.MODEL);
		pw.print("CPU ABI: ");
		pw.println(Build.CPU_ABI);
		pw.print("Max Memory: ");
		pw.println(utils_common_tools.getMaxMemory()/1024/1024);// getTotalMemory
		pw.print("Total Memory: ");
		pw.println(utils_common_tools.getTotalMemory()/1024/1024);
	}
	
	/**
	 * ���������Ϣ���ļ���
	 * 
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Throwable ex) {
//		Writer info = new StringWriter();
//		PrintWriter printWriter = new PrintWriter(info);
//		try{
//			dumpPhoneInfo(printWriter);
//			// printStackTrace(PrintWriter s)
//			// ���� throwable ����׷�������ָ���� PrintWriter
//			ex.printStackTrace(printWriter);
//			// getCause() ���ش� throwable �� cause����� cause �����ڻ�δ֪���򷵻� null��
//			Throwable cause = ex.getCause();
//			while (cause != null) {
//				cause.printStackTrace(printWriter);
//				cause = cause.getCause();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		// toString() ���ַ�������ʽ���ظû������ĵ�ǰֵ��
//		String result = info.toString();
//		System.out.println("CrashHandler-->"+result);
//		printWriter.close();
//		mDeviceCrashInfo.put(STACK_TRACE, result);

		try {
			long timestamp = System.currentTimeMillis();
			String time = new SimpleDateFormat("HH:mm:ss").format(new Date(timestamp));
			String fileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date(timestamp))
					+ CRASH_REPORTER_EXTENSION;
//			String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;
//			// �����ļ�
//			FileOutputStream trace = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
//			mDeviceCrashInfo.store(trace, "");
//			trace.flush();
//			trace.close();
			File diretory = new File(crash_file_path);
			System.out.println("crash_file_path is exist-->"+diretory.exists());
			String fileNameAllPath = crash_file_path + "/" + fileName;
			File file = new File(fileNameAllPath);
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter pw = new PrintWriter(bufferedWriter);
			pw.println(time);
			dumpPhoneInfo(pw);
			pw.println();
			ex.printStackTrace(pw);
			pw.close();
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing report file...", e);
		}
		return null;
	}
	
	/**
	 * �Ѵ��󱨸淢�͸�������,�����²����ĺ���ǰû���͵�.
	 * 
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = new File(ctx.getFilesDir(), fileName);
				postReport(cr);
				cr.delete();// ɾ���ѷ��͵ı���
			}
		}
	}

	/**
	 * ��ȡ���󱨸��ļ���
	 * 
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		File filesDir = ctx.getFilesDir();
		// ʵ��FilenameFilter�ӿڵ���ʵ�������ڹ������ļ���
		FilenameFilter filter = new FilenameFilter() {
			// accept(File dir, String name)
			// ����ָ���ļ��Ƿ�Ӧ�ð�����ĳһ�ļ��б��С�
			public boolean accept(File dir, String name) {
				return name.endsWith(CRASH_REPORTER_EXTENSION);
			}
		};
		// list(FilenameFilter filter)
		// ����һ���ַ������飬��Щ�ַ���ָ���˳���·������ʾ��Ŀ¼������ָ�����������ļ���Ŀ¼
		return filesDir.list(filter);
	}

	private void postReport(File file) {
		// TODO ʹ��HTTP Post ���ʹ��󱨸浽������
		// ���ﲻ������,�����߿��Ը���OPhoneSDN�ϵ������������
		// �̳����ύ���󱨸�
		try{
			if(file.exists() && file.isFile()){
				InputStream input = new FileInputStream(file);
//				String logContent = IOUtils.stream2String(input);
//				String deviceinfo = "";
//				ConnectionManager.getInstance().saveAppExLog(this.app,deviceinfo, this.versionName, logContent );
			}
		}catch(Exception ex){
			//
		}
	}
	
	private void postReport2Server(String fileName) {
		// TODO ʹ��HTTP Post ���ʹ��󱨸浽������
		System.out.println("-------------------���ʹ��󱨸浽������------------------");
		File file = new File(fileName);
		if(file!=null && file.isFile()){
			String user_id = "0";
//					PreferencesUtils.getString(mContext, "user_id");
			utils_network_interfaces network = new utils_network_interfaces();
			interface_app_upload_file_req req = new interface_app_upload_file_req();
			req.setUpload_user_id(user_id);
			req.setUpload_file_path(fileName);
			interface_app_upload_file_resp resp = network.ios_interface_app_upload_file(req);
			if(resp!=null && !resp.getReturn_str().equals("error")){
				//�ϴ��ɹ���ɾ���ļ�
//				file.delete();
			}
		}
	}

	/**
	 * �ڳ�������ʱ��, ���Ե��øú�����������ǰû�з��͵ı���
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}
	
}
