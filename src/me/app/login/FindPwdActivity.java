package me.app.login;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import me.app.global.Constant;
import me.app.global.MyApplication;
import me.app.parse.ParseJson;
import me.http.utils.MyHttpUtils;
import me.http.utils.MyThreadPool;
import me.where.home.R;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.util.AbStrUtil;

public class FindPwdActivity extends AbActivity {

	private Button btnFindPwd;
	private EditText etFindName, etFindEmail;
	private ImageButton btnClearName, btnClearEmail;
	private MyHandler mHandler;
	private MyApplication mApplication = null;
	private SharedPreferences sp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_find_pwd);
		overridePendingTransition(R.anim.anim_into, R.anim.anim_back);
		this.mAbTitleBar.setTitleText(R.string.login);
		this.mAbTitleBar.setLogo(R.drawable.button_selector_back);
		this.mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		this.mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		this.mAbTitleBar.setLogoLine(R.drawable.line);
		this.mApplication = (MyApplication) this.getApplication();
		this.sp = this.getSharedPreferences(Constant.sharePath,
				Context.MODE_PRIVATE);

		this.btnFindPwd = (Button) this.findViewById(R.id.findPwdBtn);
		this.etFindName = (EditText) this.findViewById(R.id.findName);
		this.etFindEmail = (EditText) this.findViewById(R.id.findemail);
		this.btnClearName = (ImageButton) this
				.findViewById(R.id.clear_find_name);
		this.btnClearEmail = (ImageButton) this
				.findViewById(R.id.clear_find_email);
		this.mHandler = new MyHandler(this);
		this.etFindEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String name = etFindName.getText().toString().trim();
				if (AbStrUtil.strLength(name) > 0) {
					btnClearName.setVisibility(View.VISIBLE);
					btnClearName.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							btnClearName.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					btnClearName.setVisibility(View.INVISIBLE);
				}
			}

		});
		this.btnClearName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etFindName.setText("");
			}

		});
		this.etFindEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String email = etFindEmail.getText().toString().trim();
				if (AbStrUtil.strLength(email) > 0) {
					btnClearEmail.setVisibility(View.VISIBLE);
					btnClearEmail.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							btnClearEmail.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					btnClearEmail.setVisibility(View.INVISIBLE);
				}
			}

		});
		this.btnClearEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etFindEmail.setText("");
			}

		});
		this.btnFindPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String name = etFindName.getText().toString().trim();
				final String email = etFindEmail.getText().toString().trim();
				if (AbStrUtil.isEmpty(name)) {
					Toast.makeText(
							FindPwdActivity.this.getApplicationContext(),
							R.string.error_name, Toast.LENGTH_SHORT).show();
					etFindName.setFocusable(true);
					etFindName.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(name) >= 10) {
					Toast.makeText(
							FindPwdActivity.this.getApplicationContext(),
							R.string.error_name_len2, Toast.LENGTH_SHORT)
							.show();
					etFindName.setFocusable(true);
					etFindName.requestFocus();
					return;
				}
				if (AbStrUtil.isEmpty(email)) {
					Toast.makeText(
							FindPwdActivity.this.getApplicationContext(),
							R.string.error_email, Toast.LENGTH_SHORT).show();
					etFindEmail.setFocusable(true);
					etFindEmail.requestFocus();
					return;
				}
				if (!AbStrUtil.isEmail(email)) {
					Toast.makeText(
							FindPwdActivity.this.getApplicationContext(),
							R.string.error_email_expr, Toast.LENGTH_SHORT)
							.show();
					etFindEmail.setFocusable(true);
					etFindEmail.requestFocus();
					return;
				}
				MyThreadPool.getInstance().getExecutorService()
						.submit(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Map<String, String> params = new HashMap<String, String>();
								params.put("name", name);
								params.put("email", email);
								JSONObject result = MyHttpUtils.getDataByUrl(
										Constant.WBSITE + "findpwd", params);
								if (result != null) {
									Message msg = mHandler.obtainMessage(1);
									msg.obj = result;
									mHandler.sendMessage(msg);
								} else {
									Log.i("findpwd failed", email + name);
								}
							}

						});
			}

		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.close_enter, R.anim.close_exit);
	}

	private static final class MyHandler extends Handler {
		private final WeakReference<FindPwdActivity> findActivity;

		private MyHandler(FindPwdActivity findActivity) {
			this.findActivity = new WeakReference<FindPwdActivity>(findActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			FindPwdActivity mFindActivity = findActivity.get();
			if (mFindActivity != null) {
				switch (msg.what) {
				case 0:
					Toast.makeText(mFindActivity.getApplicationContext(),
							"您的信息填写有误，请确认后再查询，谢谢！", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					JSONObject obj = (JSONObject) msg.obj;
					String pwd = mFindActivity.sp.getString(
							Constant.USER_PASSWORD_COOKIE, null);
					boolean ok = ParseJson.parseResult(obj);
					if (ok && pwd!=null) {
						Toast.makeText(mFindActivity.getApplicationContext(),
								"您的密码是："+pwd, Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(mFindActivity.getApplicationContext(),
								"对不起，暂时查不到结果！", Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
				}
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_pwd, menu);
		return true;
	}

}
