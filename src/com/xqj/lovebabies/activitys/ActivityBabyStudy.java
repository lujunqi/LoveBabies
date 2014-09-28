package com.xqj.lovebabies.activitys;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.global_contants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ActivityBabyStudy extends Activity {
	private WebView mWebView;
	String study_web_url = "http://124.232.142.81:8080/LoveBabyDDXX/html5/index.jsp?user_id=12";
//			global_contants.study_web_url_index + "?user_id=";
	String user_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		setContentView(R.layout.activity_baby_study);
//		System.out.println("--------------------activity_baby_study-------------------------");
		user_id = PreferencesUtils.getString(this, "user_id");
//		System.out.println(study_web_url + user_id);
		mWebView = (WebView) findViewById(R.id.baby_study_webview);
		webview();
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void webview() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true); // 支持Javascript脚本语言
		webSettings.setAllowFileAccess(true); // 允许WebView访问文件数据
		
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
//		mWebView.setWebChromeClient(webChromeClient);

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				study_web_url = url;
				if (url.equals("device:returnMain()")) {
					System.out.println("device:returnMain");
					onBackPressed();
				}
				return true;
			}
		});
//		System.out.println("loadurl-->"+study_web_url + user_id);
//		mWebView.loadUrl(study_web_url + user_id);
		mWebView.loadUrl(study_web_url);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		mWebView = null;
		super.onDestroy();
		java.lang.Runtime.getRuntime().freeMemory();
		System.gc();
	}
}
