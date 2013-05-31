package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bill_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClickedSingup(View v) {

	}

	public void onClickedLogin(View v) {

	}

	public void onClickedLoginFB(View v) {

	}

	public void onClickedLoginTW(View v) {

	}

	public void onClickedSkipLogin(View v) {

	}
}
