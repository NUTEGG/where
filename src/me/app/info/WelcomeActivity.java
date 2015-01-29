package me.app.info;

import me.app.home.MyHomeActivity;
import me.where.home.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;

public class WelcomeActivity extends Activity {

	private final int SPLASH_TIME = 600;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WelcomeActivity.this,
						MyHomeActivity.class);
				startActivity(intent);
				finish();
			}

		}, this.SPLASH_TIME);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_view, menu);
		return true;
	}

}
