package com.xqj.lovebabies.databases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class dbs_ewen_data {
	@SuppressLint("SdCardPath")
	private static final String filePath = Environment
			.getExternalStorageDirectory() + "/LoveBabies/cj/babies.obj";
	public final String FILE_UPLOAD_PATH = Environment
			.getExternalStorageDirectory() + "/LoveBabies/cj/upload/";
	public final String FILE_BASE_PATH = Environment
	.getExternalStorageDirectory() + "/LoveBabies/cj";
	public dbs_ewen_data() {
		File f = new File(FILE_UPLOAD_PATH);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			File file = new File(filePath);
			if (!file.exists()) {
				return list;
			}
			FileInputStream fis = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			list = (List<Map<String, Object>>) ois.readObject();
			ois.close();
			fis.close();
			ois = null;
			fis = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Object getData(File file) {
		Object result = null;

		try {
			if (!file.exists()) {
				return result;
			}
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = ois.readObject();
			ois.close();
			fis.close();
			ois = null;
			fis = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	public void delete(String file_path){
		File f = new File(file_path);
		f.delete();
	}
	public void saveData(List<Map<String, Object>> list) {
		try {

			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			Log.d("line75", list + "=");
			oos.writeObject(list);
			oos.flush();
			oos.close();
			fos.flush();
			fos.close();
			oos = null;
			fos = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveData(Object obj, String path) {
		try {
			Log.d("line79", "=======================" + path);
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			fos.flush();
			fos.close();
			oos = null;
			fos = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, Object> getItem(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = getData();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);
			if (id.equals(m.get("QR_CODE"))) {
				return m;
			}
		}
		return map;
	}

}
