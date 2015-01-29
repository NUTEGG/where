package me.app.login;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import me.app.data.User;
import me.app.global.Constant;
import me.app.global.MyApplication;
import me.app.parse.ParseJson;
import me.http.utils.MyHttpUtils;
import me.http.utils.MyThreadPool;
import me.where.home.R;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.util.AbStrUtil;

public class LoginActivity extends AbActivity {

	private EditText etUserName = null;
	private EditText etUserPwd = null;
	private MyApplication mApplication = null;
	private static final boolean D = Constant.DEBUG;
	private static final String TAG = "LoginActivity";
	private SharedPreferences sp = null;
	private String mStrName = null;
	private String mStrPwd = null;
	private ImageButton mClearBtnN;
	private ImageButton mClearBtnP;
	private Button loginBtn = null;
	private Button registerBtn = null;
	private Button findPwdBtn = null;
	private CheckBox cbRemPwd = null;
	private ProgressDialog progressDialog = null;
	private JSONObject user = null;
	private MyHandler mHandler = null;
	private boolean remember = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_login);
		overridePendingTransition(R.anim.anim_into, R.anim.anim_back);
		this.mApplication = (MyApplication) this.getApplication();
		mHandler = new MyHandler(this);
		this.sp = this.getSharedPreferences(Constant.sharePath,
				Context.MODE_PRIVATE);

		this.mAbTitleBar.setTitleText(R.string.login);
		this.mAbTitleBar.setLogo(R.drawable.button_selector_back);
		this.mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		this.mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		this.mAbTitleBar.setLogoLine(R.drawable.line);

		this.etUserName = (EditText) findViewById(R.id.userName);
		this.etUserPwd = (EditText) findViewById(R.id.userPwd);
		this.cbRemPwd = (CheckBox) findViewById(R.id.login_check);
		this.mClearBtnN = (ImageButton) findViewById(R.id.clearName);
		this.mClearBtnP = (ImageButton) findViewById(R.id.clearPwd);
		this.loginBtn = (Button) findViewById(R.id.loginBtn);
		this.registerBtn = (Button) findViewById(R.id.registerBtn);
		this.findPwdBtn = (Button) findViewById(R.id.pwdBtn);

		this.progressDialog = new ProgressDialog(this);
		this.progressDialog.setCancelable(true);
		this.progressDialog.setMessage(Constant.DIALOG_MESSAGE);
		this.progressDialog.setCanceledOnTouchOutside(true);

		if (mApplication.mUser != null) {
			User mUser = mApplication.mUser;
			if (mUser.getName() != null) {
				this.etUserName.setText(mUser.getName());
			}
			if (mUser.getPwd() != null) {
				this.etUserPwd.setText(mUser.getPwd());
			}
			this.cbRemPwd.setChecked(mUser.isRemerberPwd());
		}

		this.loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mStrName = etUserName.getText().toString().trim();
				mStrPwd = etUserPwd.getText().toString().trim();
				if (TextUtils.isEmpty(mStrName)) {
					showToast(R.string.error_name);
					etUserName.setFocusable(true);
					etUserName.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(mStrName) >= 10) {
					showToast(R.string.error_name_len2);
					etUserName.setFocusable(true);
					etUserName.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(mStrPwd)) {
					showToast(R.string.error_pwd);
					etUserPwd.setFocusable(true);
					etUserPwd.requestFocus();
					return;
				}
				if (!AbStrUtil.isNumberLetter(mStrPwd)) {
					showToast(R.string.error_pwd_expr);
					etUserPwd.setFocusable(true);
					etUserPwd.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(mStrPwd) <= 3) {
					showToast(R.string.error_pwd_len1);
					etUserPwd.setFocusable(true);
					etUserPwd.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(mStrPwd) >= 15) {
					showToast(R.string.error_pwd_len2);
					etUserPwd.setFocusable(true);
					etUserPwd.requestFocus();
					return;
				}
				progressDialog.show();
				progressDialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface arg0) {
						// TODO Auto-generated method stub
						if (D) {
							Log.i(TAG, "cancel");
						}
					}

				});
				MyThreadPool.getInstance().getExecutorService()
						.submit(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Map<String, String> mapParams = new HashMap<String, String>();
								mapParams.put("username", mStrName);
								mapParams.put("pwd", mStrPwd);
								user = MyHttpUtils.getDataByUrl(Constant.WBSITE
										+ "check", mapParams);
								if (user != null) {
									Log.i("get", user.toString());
									Message msg = mHandler.obtainMessage(1);
									msg.obj = user;
									mHandler.sendMessage(msg);
								} else {
									Log.i("get", "没有该用户！");
									Message msg = mHandler.obtainMessage(0);
									Bundle bundle = new Bundle();
									bundle.putString("msg", "用户不存在，请确认，谢谢！");
									msg.setData(bundle);
									mHandler.sendMessage(msg);
								}
							}

						});

			}

		});
		this.findPwdBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						FindPwdActivity.class);
				startActivity(intent);
			}

		});

		this.registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivityForResult(intent, 1);
			}

		});
		this.etUserName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String str = etUserName.getText().toString().trim();
				if (str.length() > 0) {
					mClearBtnN.setVisibility(View.VISIBLE);
					mClearBtnN.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mClearBtnN.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					mClearBtnN.setVisibility(View.INVISIBLE);
				}
			}

		});
		this.etUserPwd.addTextChangedListener(new TextWatcher() {

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
				String str = etUserPwd.getText().toString().trim();
				if (str.length() > 0) {
					etUserPwd.setVisibility(View.VISIBLE);
					mClearBtnP.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mClearBtnP.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					mClearBtnP.setVisibility(View.INVISIBLE);
				}
			}

		});
		this.mClearBtnN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				etUserName.setText("");
			}

		});
		this.mClearBtnP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				etUserPwd.setText("");
			}

		});
		this.cbRemPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton btnView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				remember = isChecked;
			}

		});

		this.mAbTitleBar.getLogoView().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
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

	private void dialogDismissAndLogin(JSONObject obj) {
		progressDialog.cancel();
		Editor editor = sp.edit();
		editor.putBoolean(Constant.USER_PASSWORD_REMEMBER_COOKIE, remember);
		editor.putString(Constant.USER_NAME_COOKIE, mStrName);
		editor.putString(Constant.USER_PASSWORD_COOKIE, mStrPwd);
		editor.commit();
		mApplication.mUser.setRemerberPwd(remember);
		ParseJson.parseUser(user, mApplication.mUser);
		mApplication.mUser.setLogin(true);
		saveMyData(obj);
		Toast.makeText(this.getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT)
				.show();
		this.setResult(1);
		this.finish();
	}

	private void saveMyData(JSONObject obj) {
		int id = ParseJson.parseMyData(obj, mApplication.mUser);
		Editor editor = sp.edit();
		editor.putInt(Constant.USER_ID_COOKIE, id);
		editor.commit();
		if (id != -1) {
			if (D) {
				Log.i("login", "login sucessfully");
			}
		}
	}

	private void dialogDismissAndAlert(String msg) {
		progressDialog.cancel();
		Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	private static final class MyHandler extends Handler {
		private final WeakReference<LoginActivity> mLoginActivity;

		private MyHandler(LoginActivity loginActivity) {
			this.mLoginActivity = new WeakReference<LoginActivity>(
					loginActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			LoginActivity loginActivity = mLoginActivity.get();
			if (loginActivity != null) {
				switch (msg.what) {
				case 0:
					Bundle data = msg.getData();
					loginActivity.dialogDismissAndAlert(data.getString("msg"));
					break;
				case 1:
					JSONObject obj = (JSONObject) msg.obj;
					loginActivity.dialogDismissAndLogin(obj);
					break;
				default:
					break;
				}
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 1) {
			String name = data.getStringExtra("name");
			String pwd = data.getStringExtra("pwd");
			this.etUserName.setText(name);
			this.etUserPwd.setText(pwd);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
