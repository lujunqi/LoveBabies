package com.xqj.lovebabies.activitys;

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
import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.network_interface_paths;

public class ActivitySomeread extends Activity {
	private WebView mWebView;
	String mUrl = network_interface_paths.interface_some_read + "?user_id=";
	String user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setOrientation();
		setContentView(R.layout.activity_someread);
		mWebView = (WebView) findViewById(R.id.webview);
		setWebView();
	}
	
	public void setOrientation(){
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void setWebView(){
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setAllowFileAccess(true); 
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				mUrl = url;
				if (url.equals("device:returnMain()")) {
					onBackPressed();
				}
				return true;
			}
		});
		String user_id = PreferencesUtils.getString(this, "user_id");
		mWebView.loadUrl(mUrl + user_id);
	}
}
