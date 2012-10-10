package com.magellano.cordova_2_1_0;

import org.apache.cordova.DroidGap;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.magellano.cordova_2_1_0.ReloadHttpServer.OnReloadListener;

public class ReloadActivity extends DroidGap implements OnReloadListener {

	private ReloadHttpServer reloadServer = new ReloadHttpServer();

	static final private int ERROR_DIALOG = 0;

	private String server = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		server = getIntent().getStringExtra(Constant.EXTRA_RELOAD_SERVER);
		// Start reload server
		reloadServer.setListener(this);
		reloadServer.start();
		
		connect();	
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

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Unable to connect to " + server + " !!!");
		builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				ReloadActivity.this.endActivity();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
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
}
