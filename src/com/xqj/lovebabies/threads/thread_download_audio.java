package com.xqj.lovebabies.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.os.Handler;
import android.os.Message;

public class thread_download_audio extends Thread {

	private String fileName;
	private Handler handler;
	private String localPath;
	private String uri;
	
	public thread_download_audio(Handler handler,String upload_file_path, String url){
		this.handler = handler;
		this.uri = url;
		this.localPath = upload_file_path;
	}
	
	@Override
	public void run() {
		String tag_string = "";
		int file_begin_index = uri.indexOf("a_");
		String voice_file_name = uri.substring(file_begin_index, uri.length());
		File voice_file = new File(localPath+voice_file_name);
		if(voice_file.exists() && voice_file.isFile()){//读取本地录音
			System.out.println("读取本地录音["+localPath+voice_file_name+"]...");
			tag_string = localPath+voice_file_name;
			Message message = new Message();
			message.obj = tag_string;
			message.arg1 = 1;
			handler.sendMessage(message);
		}else{//网络下载
			int begin_index = uri.indexOf("http://");
			String voice_uri = uri.substring(begin_index, uri.length());
			System.out.println("voice_uri-->"+voice_uri);
			int network_file_begin_index = voice_uri.indexOf("a_");
			String network_voice_file_name = uri.substring(network_file_begin_index, voice_uri.length());
			tag_string = localPath+network_voice_file_name;
			boolean result = loadUrlRecord(voice_uri);
			Message message = new Message();
			message.obj = localPath+fileName;
			if(result){
				message.arg1 = 1;
			}else{
				message.arg1 = -1;
			}
			handler.sendMessage(message);
		}
        
		super.run();
	}
	
	/**
	 * 通过url下载录音文件
	 * @param url
	 * @return
	 */
	public boolean loadUrlRecord(String url){
		URL request;
		InputStream input = null;
		try{
			request = new URL(url);
			input = (InputStream) request.getContent();
			saveAudioRecord(url, input);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(input!=null){
				try {
					input.close();
					input=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 保存录音文件
	 * @param fileName
	 * @param input
	 */
	public void saveAudioRecord(String fileName, InputStream input){
		FileOutputStream fos = null;
		File f = null; 
		try{
			f = createRecordFile(localPath,fileName);
		    fos = new FileOutputStream(f);
		    byte[] b = new byte[1];
		    while (input.read(b) != -1) {
		      fos.write(b);
		   }
		}catch(Exception ex){
			ex.printStackTrace();
		} finally{
			if(fos!=null){
				try {
					fos.close();
					fos = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(f!=null)
				f = null;
		}
	}
	
	/**
	 * 创建录音文件
	 * @param path
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	public File createRecordFile(String path, String name) throws IOException {
		File record = new File(path,name);
		if (!record.exists()) {
			record.createNewFile();
		} else {
			System.out.println("SDCard on exist !");
		}
		return record;
	}
}
