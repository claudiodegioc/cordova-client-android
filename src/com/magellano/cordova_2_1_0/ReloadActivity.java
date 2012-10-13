package com.magellano.cordova_2_1_0;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.DroidGap;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.IPlugin;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.magellano.cordova_2_1_0.ReloadHttpServer.OnReloadListener;

public class ReloadActivity extends Activity implements CordovaInterface,
		OnReloadListener {

	final static private String TAG = "ReloadActivity";

	private ReloadHttpServer reloadServer = new ReloadHttpServer();

	static final private int ERROR_DIALOG = 0;

	private String server = null;

	private CordovaWebView cwv = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reload);
		cwv = (CordovaWebView) findViewById(R.id.wvCordova);
		cwv.setWebViewClient(new ReloadWebViewClient());

		// setContentView(R.layout.activity_reload);
		server = getIntent().getStringExtra(Constant.EXTRA_RELOAD_SERVER);
		// Start reload server
		reloadServer.setListener(this);
		reloadServer.start();

		connect();
		reload();
	}

	@Override
	public void onDestroy() {
		reloadServer.stop();
		super.onDestroy();
	}

	public void onReload() {
		runOnUiThread(new Runnable() {

			public void run() {
				reload();
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Unable to connect to " + server + " !!!");
		builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				ReloadActivity.this.finish();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_reload, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menuReload:
			reload();
			return true;
		case R.id.menuConnect:
			connect();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void reload() {
		cwv.loadUrl("http://" + server + ":1337/index.html");
		findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
	}

	private void connect() {
		HttpClient hc = null;
		try {
			hc = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://" + server + ":1337/connect");
			hc.execute(post);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			showDialog(ERROR_DIALOG);
		}
	}

	@Deprecated
	public void cancelLoadUrl() {
		// TODO Auto-generated method stub

	}

	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

	@Deprecated
	public Context getContext() {
		return this;
	}

	public Object onMessage(String arg0, Object arg1) {
		return null;
	}

	public void setActivityResultCallback(IPlugin arg0) {

	}

	public void startActivityForResult(IPlugin arg0, Intent arg1, int arg2) {

	}

	private class ReloadWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			findViewById(R.id.rlLoading).setVisibility(View.INVISIBLE);
			super.onPageFinished(view, url);
		}
	}
}
