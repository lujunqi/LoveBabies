package com.xqj.lovebabies.map;

import com.baidu.location.*;
import com.xqj.lovebabies.commons.utils_common_tools;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.TextView;

public class baidu_location{
	private final int STOP_LOCATION_CLIENT = 9001;
	private final String BAIDU_KEY = "697f50541f8d4779124896681cb6584d";
	private LocationClient mLocationClient = null;
	private String mData;  
	private MyLocationListenner myListener = new MyLocationListenner();
	private TextView mTv;
	private Handler handler;
	public static String TAG = "BaiDuLocation";
	
	private Application application = null;
	
	public baidu_location(Application application) {
		this.application = application;
		mLocationClient = new LocationClient( application );
		mLocationClient.setAK(BAIDU_KEY);
		mLocationClient.registerLocationListener( myListener );

		Log.d(TAG, "... Application onCreate... pid=" + Process.myPid());
	}
	
	public LocationClient getmLocationClient() {
		return mLocationClient;
	}

	public TextView getmTv() {
		return mTv;
	}

	public void setmTv(TextView mTv) {
		this.mTv = mTv;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public void logMsg(String str) {
		try {
			mData = str;
			
			if ( mTv != null ){
				if(mData==null){
					mTv.setText("无法获取所在位置");
				}
				mTv.setText(mData);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public class MyLocationListenner implements BDLocationListener {
		
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(1);
			
			   if (!new utils_common_tools().get_network_status(application.getApplicationContext())) {
				   sb.append("");
				}else{
					sb.append(location.getAddrStr());
				}
				
			logMsg(sb.toString());
			if(handler!=null){
				sendStopClient();
			}
			Log.i(TAG, sb.toString());
		}
		
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ; 
			}
			StringBuffer sb = new StringBuffer(1);
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append(poiLocation.getAddrStr());
			} 

			logMsg(sb.toString());
			if(handler!=null){
				sendStopClient();
			}
		}
	}
	
	/**
	 * 发送关闭搜索地址的请求
	 */
	public void sendStopClient(){
		Message msg = new Message();
		msg.what = STOP_LOCATION_CLIENT;
		handler.sendMessage(msg);
	}
	
}
