package com.xqj.lovebabies.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.xqj.lovebabies.contants.global_contants;

import android.content.Intent;
import android.net.Uri;

public class utils_common_share {
	
	/**
	 * �û����� ΢�š�΢�� �ȵ�
	 * @param hm
	 * @return
	 */
	public static Intent shareMethod(HashMap<String, String> hm) {
		System.out.println("share content utils_common_share...............");
		Intent intent = new Intent();
//		intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		intent.setAction(Intent.ACTION_SEND);
//		intent.setType("image/text");
		intent.setType("text/plain");
//		intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
//		if (hm.get("imagePath") == null) {
//			ArrayList<Uri> us=new ArrayList<Uri>();
//			for (int i = 0; i < hm.size()-1; i++) {
//				File f = new File(hm.get("Uri"+i));
//				Uri u = Uri.fromFile(f);
//				us.add(u);
//			}
//			intent.putExtra(Intent.EXTRA_STREAM, us);// ͼƬ·��
//		} else {
//			File f = new File(hm.get("imagePath"));
//			Uri u = Uri.fromFile(f);
//			intent.putExtra(Intent.EXTRA_STREAM, u);// ͼƬ·��
//		}
		String shareContent = hm.get("content");
		System.out.println("shareContent:"+shareContent);
		if (shareContent == null) {
			shareContent = "";
		} else {
			if (shareContent.length() > 40) {
				shareContent = shareContent.substring(0, 40) + "...";
			}
		}
		
		intent.putExtra(Intent.EXTRA_TEXT,"��������@���ϰ�����@����ͻ��ˡ�,"+ shareContent+ global_contants.ibaby_apk_download_path);// ���������
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}
}
