package com.magellano.cordova_2_1_0;

import java.lang.ref.PhantomReference;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuInflater;
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

		final String server = getPreferences(MODE_PRIVATE).getString(
				Constant.SHARED_PREF_RELOAD_SERVER, "");
		((EditText) findViewById(R.id.etReloadServer)).setText(server);

	}

	public void onClick(View v) {
		final String server = ((EditText) findViewById(R.id.etReloadServer))
				.getText().toString();

		final Intent i = new Intent(this, ReloadActivity.class);
		i.putExtra(Constant.EXTRA_RELOAD_SERVER, server);

		final Editor editor = getPreferences(MODE_PRIVATE).edit();
		editor.putString(Constant.SHARED_PREF_RELOAD_SERVER, server);
		editor.commit();
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_connect, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menuSettings:
			startActivity(new Intent(this, PreferenceActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
