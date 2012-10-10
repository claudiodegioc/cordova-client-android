package com.magellano.cordova_2_1_0;

import org.apache.cordova.DroidGap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.magellano.cordova_2_1_0.ReloadHttpServer.OnReloadListener;

import android.os.Bundle;
import android.widget.Toast;

public class ReloadActivity extends DroidGap implements OnReloadListener {

	private ReloadHttpServer reloadServer = new ReloadHttpServer();

	private String server = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		server = getIntent().getStringExtra(Constant.EXTRA_RELOAD_SERVER);
		try {
			HttpClient hc = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://" + server + ":1337/connect");
			HttpResponse rp = hc.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
		}

		loadUrl("http://" + server + ":1337/index.html");

		// Start reload server
		reloadServer.setListener(this);
		reloadServer.start();
	}

	@Override
	public void onDestroy() {
		reloadServer.stop();
		super.onDestroy();
	}

	public void onReload() {
		// TODO Auto-generated method stub

		runOnUiThread(new Runnable() {

			public void run() {

				loadUrl("http://" + server + ":1337/index.html");
				Toast.makeText(ReloadActivity.this, "Reload Done",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
