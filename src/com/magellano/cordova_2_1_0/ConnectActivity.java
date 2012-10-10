package com.magellano.cordova_2_1_0;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class ConnectActivity extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);

		findViewById(R.id.btConnect).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_connect, menu);
		return true;
	}

	public void onClick(View v) {
		final String server = ((EditText) findViewById(R.id.etReloadServer))
				.getText().toString();

		final Intent i = new Intent(this, ReloadActivity.class);
		i.putExtra(Constant.EXTRA_RELOAD_SERVER, server);

		startActivity(i);
	}

}
