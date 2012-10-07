package com.magellano.cordova_2_1_0;

import org.apache.cordova.DroidGap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;

public class ReloadActivity extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String server = getIntent().getStringExtra(
				Constant.EXTRA_RELOAD_SERVER);
		try {
			HttpClient hc = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://" + server + ":1337/connect");
			HttpResponse rp = hc.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.loadUrl("http://" + server + ":1337/index.html");
	}
}
