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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.util.AbStrUtil;

public class RegisterActivity extends AbActivity {

	private MyApplication mApplication = null;
	private static final boolean D = Constant.DEBUG;
	private EditText userName = null;
	private EditText userPwd = null;
	private EditText userPwd2 = null;
	private EditText email = null;
	private CheckBox checkBox = null;

	private ImageButton mClear1;
	private ImageButton mClear2;
	private ImageButton mClear3;
	private ImageButton mClear4;
	private Button agreementBtn = null;
	private Button registerBtn = null;
	private Handler mHandler = null;
	private String name = null;
	private String pwd = null;
	private String strEmail = null;
	private ProgressDialog mProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_register);
		overridePendingTransition(R.anim.anim_into, R.anim.anim_back);
		this.mApplication = (MyApplication) this.getApplication();
		this.mAbTitleBar.setTitleText(R.string.register_name);
		this.mAbTitleBar.setLogo(R.drawable.button_selector_back);
		this.mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		this.mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		this.mAbTitleBar.setLogoLine(R.drawable.line);

		userName = (EditText) this.findViewById(R.id.userName);
		userPwd = (EditText) this.findViewById(R.id.userPwd);
		userPwd2 = (EditText) this.findViewById(R.id.userPwd2);
		email = (EditText) this.findViewById(R.id.email);
		checkBox = (CheckBox) this.findViewById(R.id.register_check);
		mClear1 = (ImageButton) findViewById(R.id.clearName);
		mClear2 = (ImageButton) findViewById(R.id.clearPwd);
		mClear3 = (ImageButton) findViewById(R.id.clearPwd2);
		mClear4 = (ImageButton) findViewById(R.id.clearEmail);
		agreementBtn = (Button) findViewById(R.id.agreementBtn);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		this.mProgressDialog = new ProgressDialog(this);
		this.mHandler = new MyHandler(this);
		this.mAbTitleBar.getLogoView().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}

				});

		userName.addTextChangedListener(new TextWatcher() {

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
				String str = userName.getText().toString().trim();
				int len = str.length();
				if (len > 0) {
					mClear1.setVisibility(View.VISIBLE);
					mClear1.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mClear1.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					mClear1.setVisibility(View.INVISIBLE);
				}
			}

		});

		this.userPwd.addTextChangedListener(new TextWatcher() {

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
				String str = userPwd.getText().toString().trim();
				int len = str.length();
				if (len > 0) {
					mClear2.setVisibility(View.VISIBLE);
					mClear2.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mClear2.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					mClear2.setVisibility(View.INVISIBLE);
				}
			}

		});

		this.userPwd2.addTextChangedListener(new TextWatcher() {

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
				String pwd = userPwd2.getText().toString().trim();
				int len = pwd.length();
				if (len > 0) {
					mClear3.setVisibility(View.INVISIBLE);
					mClear3.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mClear3.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					mClear3.setVisibility(View.INVISIBLE);
				}
			}

		});

		this.email.addTextChangedListener(new TextWatcher() {

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
				String str = email.getText().toString().trim();
				int len = str.length();
				if (len > 0) {
					mClear4.setVisibility(View.VISIBLE);
					mClear4.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mClear4.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					mClear4.setVisibility(View.INVISIBLE);
				}
			}

		});

		this.mClear1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName.setText("");
			}

		});

		this.mClear2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userPwd.setText("");
			}

		});

		this.mClear3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userPwd2.setText("");
			}

		});

		this.mClear4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				email.setText("");
			}

		});

		this.registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = userName.getText().toString().trim();
				pwd = userPwd.getText().toString().trim();
				final String pwd2 = userPwd2.getText().toString().trim();
				strEmail = email.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					showToast(R.string.error_name);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(name) >= 10) {
					showToast(R.string.error_name_len2);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(pwd)) {
					showToast(R.string.error_pwd);
					userPwd.setFocusable(true);
					userName.requestFocus();
					return;
				}
				if (!AbStrUtil.isNumberLetter(pwd)) {
					showToast(R.string.error_pwd_expr);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(pwd) <= 3) {
					showToast(R.string.error_pwd_len1);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(pwd) >= 15) {
					showToast(R.string.error_pwd_len2);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}

				if (TextUtils.isEmpty(pwd2)) {
					showToast(R.string.error_pwd);
					userPwd.setFocusable(true);
					userName.requestFocus();
					return;
				}

				if (!pwd.equals(pwd2)) {
					showToast(R.string.error_pwd_match);
					userPwd2.setFocusable(true);
					userPwd2.requestFocus();
					return;
				}

				if (!AbStrUtil.isNumberLetter(pwd2)) {
					showToast(R.string.error_pwd_expr);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(pwd2) <= 3) {
					showToast(R.string.error_pwd_len1);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(pwd2) >= 15) {
					showToast(R.string.error_pwd_len2);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}

				if (TextUtils.isEmpty(strEmail)) {
					showToast(R.string.error_email);
					email.setFocusable(true);
					email.requestFocus();
					return;
				}
				if (!AbStrUtil.isEmail(strEmail)) {
					showToast(R.string.error_email_expr);
					email.setFocusable(true);
					email.requestFocus();
					return;
				}
				if (!checkBox.isChecked()) {
					showToast(R.string.error_agreement);
					return;
				}
				mProgressDialog.show();
				MyThreadPool.getInstance().getExecutorService()
						.submit(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Map<String, String> mapParams = new HashMap<String, String>();
								mapParams.put("name", name);
								mapParams.put("pwd", pwd);
								mapParams.put("email", strEmail);
								JSONObject result = MyHttpUtils.postDataByUrl(
										Constant.WBSITE + "new", mapParams);
								if (result != null) {
									Log.i("get", result.toString());
									Message msg = mHandler.obtainMessage(1);
									msg.obj = result;
									mHandler.sendMessage(msg);
								} else {
									Log.i("get", "对不起，创建用户失败！");
									Message msg = mHandler.obtainMessage(0);
									mHandler.sendMessage(msg);
								}
							}

						});
			}

		});
		this.agreementBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegisterActivity.this,
						AgreeActivity.class);
				startActivity(intent);
			}

		});
	}

	private static final class MyHandler extends Handler {
		private final WeakReference<RegisterActivity> mRegisterActivity;

		private MyHandler(RegisterActivity registerActivity) {
			this.mRegisterActivity = new WeakReference<RegisterActivity>(
					registerActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			RegisterActivity registerActivity = mRegisterActivity.get();
			if (registerActivity != null) {
				switch (msg.what) {
				case 0:
					Bundle data = msg.getData();
					if (data != null) {
						registerActivity.dialogDismissAndAlert(data
								.getString("msg"));
					}
					break;
				case 1:
					JSONObject result = (JSONObject) msg.obj;
					registerActivity.dialogDismissAndLogin(result);
					break;
				default:
					break;
				}
			}
		}
	};

	private void dialogDismissAndAlert(String msg) {
		this.mProgressDialog.dismiss();
		if (msg != null) {
			Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT)
					.show();
		}

	}

	private void dialogDismissAndLogin(JSONObject result) {
		this.mProgressDialog.dismiss();
		boolean isSuccess = ParseJson.parseResult(result);
		if (!isSuccess) {
			Toast.makeText(RegisterActivity.this.getApplicationContext(),
					"对不起，创建用户失败了！", Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(this.getApplicationContext(), "恭喜创建用户成功！",
				Toast.LENGTH_SHORT).show();
		mApplication.mUser.setName(name);
		mApplication.mUser.setPwd(pwd);
		mApplication.mUser.setEmail(strEmail);
		Intent data = new Intent();
		data.putExtra("name", name);
		data.putExtra("pwd", pwd);
		this.setResult(1, data);
		finish();
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
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
