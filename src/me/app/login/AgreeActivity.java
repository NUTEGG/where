package me.app.login;

import me.where.home.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.ab.activity.AbActivity;

public class AgreeActivity extends AbActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_agree);
		overridePendingTransition(R.anim.anim_into, R.anim.anim_back);
		this.mAbTitleBar.setTitleText(R.string.register_name);
		this.mAbTitleBar.setLogo(R.drawable.button_selector_back);
		this.mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		this.mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		this.mAbTitleBar.setLogoLine(R.drawable.line);

		this.mAbTitleBar.getLogoView().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}

				});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.close_enter, R.anim.close_exit);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agree, menu);
		return true;
	}

}
