package com.magellano.cordova_2_1_0;

import org.apache.cordova.DroidGap;

import android.os.Bundle;

public class ReloadActivity extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String server = getIntent().getStringExtra(Constant.EXTRA_RELOAD_SERVER);
		super.loadUrl("http://" + server + ":1337/index.html");
	}
}
