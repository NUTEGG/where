package me.where.more;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.app.data.Member;
import me.app.global.Constant;
import me.app.global.MyApplication;
import me.http.utils.MyHttpUtils;
import me.http.utils.MyThreadPool;
import me.where.home.R;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;

public class SignTodayActivity extends AbActivity {
	private static final boolean D = Constant.DEBUG;
	private Button btnSign;
	private TextView tvMemberScore;
	private String lastSignTime;
	private MyApplication mApplication;
	private MyHandler mHandler;
	private SharedPreferences sp;
	private SimpleDateFormat sdf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_sign_today);
		overridePendingTransition(R.anim.anim_into, R.anim.anim_back);
		this.mAbTitleBar.setTitleText(R.string.sign_today);
		this.mAbTitleBar.setLogo(R.drawable.button_selector_back);
		this.mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		this.mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		this.mAbTitleBar.setLogoLine(R.drawable.line);
		this.btnSign = (Button) this.findViewById(R.id.btn_sign_today);
		this.tvMemberScore = (TextView) this.findViewById(R.id.member_score);
		sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		sp = this
				.getSharedPreferences(Constant.sharePath, Context.MODE_PRIVATE);
		this.lastSignTime = sp.getString(Constant.USER_SIGN_TODAY, "");
		this.mApplication = (MyApplication) this.getApplication();
		this.mHandler = new MyHandler(this);

		Calendar c = Calendar.getInstance();
		String signTime = sdf.format(c.getTime());
		if (this.lastSignTime.equals(signTime)) {
			this.btnSign.setText(R.string.signed_today);
			this.btnSign.setEnabled(false);
		}
		StringBuilder sb = new StringBuilder();
		Member member = mApplication.mapMember.get(mApplication.mUser
				.getMember());
		if (member != null) {
			sb.append("亲,您当前的会员积分是：").append(mApplication.mUser.getScore())
					.append("\n您的会员等级是：").append(member.getName())
					.append("\n您可以查看好友位置坐标的数量为：").append(member.getViewNum());
		} else {
			sb.append("对不起，暂时没有查到您的结果哦！");
		}
		this.tvMemberScore.setText(sb.toString());
		this.mAbTitleBar.getLogoView().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}

				});
		this.btnSign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int id = mApplication.mUser.getId();
				Log.i("myid", "id" + id);
				MyThreadPool.getInstance().getExecutorService()
						.submit(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Map<String, String> params = new HashMap<String, String>();
								params.put("id", Integer.toString(id));
								JSONObject obj = MyHttpUtils.postDataByUrl(
										Constant.WBSITE + "sign", params);
								if (obj != null) {
									mHandler.sendEmptyMessage(1);
								}
							}

						});
			}

		});

	}

	private static final class MyHandler extends Handler {
		private final WeakReference<SignTodayActivity> mSignActivity;

		private MyHandler(SignTodayActivity signActivity) {
			this.mSignActivity = new WeakReference<SignTodayActivity>(
					signActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			SignTodayActivity signActivity = mSignActivity.get();
			if (signActivity != null) {
				switch (msg.what) {
				case 0:
					break;
				case 1:
					signActivity.signSucess();
					break;
				default:
					break;
				}
			}
		}
	};

	private void signSucess() {
		Editor editor = sp.edit();
		Calendar c = Calendar.getInstance();
		if (D) {
			Log.i("date", sdf.format(c.getTime()));
		}
		editor.putString(Constant.USER_SIGN_TODAY, sdf.format(c.getTime()));
		editor.commit();
		int score = this.mApplication.mUser.getScore();
		score += 5;
		this.mApplication.mUser.setScore(score);
		Member member = mApplication.mapMember.get(mApplication.mUser
				.getMember());
		StringBuilder sb = new StringBuilder();
		if (member != null) {
			sb.append("亲,您当前的会员积分是：").append(mApplication.mUser.getScore())
					.append("\n您的会员等级是：").append(member.getName())
					.append("\n您可以查看好友位置坐标的数量为：").append(member.getViewNum());
		} else {
			sb.append("对不起，暂时没有查到您的结果哦！");
		}
		this.tvMemberScore.setText(sb.toString());
		this.btnSign.setText(R.string.signed_today);
		this.btnSign.setEnabled(false);
		Toast.makeText(this.getApplicationContext(), "恭喜，签到成功！",
				Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.sign_today, menu);
		return true;
	}

}
