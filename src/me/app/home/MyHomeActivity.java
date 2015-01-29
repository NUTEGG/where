package me.app.home;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.app.global.Constant;
import me.app.global.MyApplication;
import me.app.home.TaFragment.TaCheck;
import me.app.login.LoginActivity;
import me.app.parse.ParseJson;
import me.http.utils.MyHttpUtils;
import me.http.utils.MyThreadPool;
import me.where.home.R;
import me.where.more.AboutUsActivity;
import me.where.more.LeaveMsgActivity;
import me.where.more.MyLevelActivity;
import me.where.more.SignTodayActivity;
import me.where.more.SoftUseActivity;
import me.where.ta.TaMapActivity;
import net.youmi.android.diy.DiyManager;
import net.youmi.android.spot.SpotManager;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.ab.util.AbStrUtil;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApi;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiListener;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;

public class MyHomeActivity extends FragmentActivity implements
		OnClickListener, TaCheck {
	private static final boolean D = Constant.DEBUG;
	private static final String TAG = "MyHomeActivity";
	private long end_time = 0;
	private Handler myHandler = new MyHandler(this);
	private MyApplication mApplication = null;
	private TencentMapLBSApiResult mLocRes = null;
	private TencentMapLBSApiResult mOldLocRes = null;
	private MyOverlay myOverlay = null;
	private MyItemizedOverlay myItemOverlay = null;
	private MySlideTabFragment mySlideTabFragment;
	private MenuItem mLoginMenuItem;
	private LocListener mListener = null;
	private ProgressDialog mProgressDialog;
	private SharedPreferences sp = null;
	private long lastUpdate;

	private String taName, taPwd;
	private boolean taRember;

	private static final class MyHandler extends Handler {
		private final WeakReference<MyHomeActivity> myHomeActivity;

		private MyHandler(MyHomeActivity homeActivity) {
			this.myHomeActivity = new WeakReference<MyHomeActivity>(
					homeActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			MyHomeActivity homeActivity = myHomeActivity.get();
			if (homeActivity != null) {
				switch (msg.what) {
				case 0:
					Toast.makeText(homeActivity.getApplicationContext(),
							"您查找的用户不存在，请确认再查找，谢谢", Toast.LENGTH_SHORT).show();
					homeActivity.dialogDismiss();
					break;
				case 1:
					if (D) {
						Toast.makeText(homeActivity.getApplicationContext(),
								"查看成功啦", Toast.LENGTH_SHORT).show();
					}
					JSONObject ta = (JSONObject) msg.obj;
					homeActivity.dialogDismiss();
					homeActivity.saveTaData(ta);
					homeActivity.toTaMapActivity();
					break;
				case 2:
					JSONObject obj = (JSONObject) msg.obj;
					homeActivity.saveMyData(obj);
					homeActivity.loginAuto();
					break;
				case 3:
					Bundle data = msg.getData();
					String pwd = data.getString("pwd");
					homeActivity.setMyViewPwd(pwd);
					break;
				case 5:
					Toast.makeText(homeActivity.getApplicationContext(),
							"恭喜，设置查询密码成功！", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		}
	};

	private void setMyViewPwd(String pwd) {
		final Map<String, String> params = new HashMap<String, String>();
		int id = mApplication.mUser.getId();
		params.put("viewpwd", pwd);
		if (id != -1) {
			params.put("id", Integer.toString(id));
			MyThreadPool.getInstance().getExecutorService()
					.submit(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							JSONObject result = MyHttpUtils.postDataByUrl(
									Constant.WBSITE + "viewpwd", params);
							if (result != null) {
								myHandler.sendEmptyMessage(5);
							}
						}

					});
		} else {
			Toast.makeText(MyHomeActivity.this.getApplicationContext(),
					"请先登录，谢谢！", Toast.LENGTH_SHORT).show();
		}

	}

	private void dialogDismiss() {
		this.mProgressDialog.dismiss();
	}

	private void loginAuto() {
		mApplication.mUser.setLogin(true);
		this.mLoginMenuItem.setTitle("退出账号");
	}

	private void tryLoginAuto() {
		if (mApplication.mUser.isRemerberPwd()) {
			final String name = mApplication.mUser.getName();
			final String pwd = mApplication.mUser.getPwd();
			if (name != null && pwd != null) {
				MyThreadPool.getInstance().getExecutorService()
						.submit(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Map<String, String> mapParams = new HashMap<String, String>();
								mapParams.put("username", name);
								mapParams.put("pwd", pwd);
								JSONObject user = MyHttpUtils.getDataByUrl(
										Constant.WBSITE + "check", mapParams);
								if (user != null) {
									Log.i("get", user.toString());
									Message msg = myHandler.obtainMessage(2);
									msg.obj = user;
									myHandler.sendMessage(msg);
								}
							}

						});
			}
		}
	}

	private void saveMyData(JSONObject obj) {
		int id = ParseJson.parseMyData(obj, mApplication.mUser);
		Editor editor = sp.edit();
		editor.putInt(Constant.USER_ID_COOKIE, id);
		editor.commit();
		if (id != -1) {
			Log.i("autoLogin", "login auto sucessfully");
		}
	}

	private void saveTaData(JSONObject ta) {
		Editor editor = sp.edit();
		editor.putString(Constant.USER_TANAME_COOKIE, taName);
		editor.putString(Constant.USER_TAPASSWORD_COOKIE, taPwd);
		editor.putBoolean(Constant.USER_TAPASSWORD_REMEMBER_COOKIE, taRember);
		mApplication.ta.setName(taName);
		mApplication.ta.setPwd(taPwd);
		mApplication.ta.setRember(taRember);
		int id = ParseJson.parseTaId(ta);
		if (id != -1) {
			mApplication.ta.setId(id);
		}
		editor.commit();
	}

	private void toTaMapActivity() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("id", mApplication.ta.getId());
		if (this.mLocRes != null) {
			bundle.putBoolean("me", true);
			bundle.putDouble("lat", mLocRes.Latitude);
			bundle.putDouble("lng", mLocRes.Longitude);
			bundle.putString("location", MapUtil.resultToString(mLocRes));
		}
		intent.putExtras(bundle);
		intent.setClass(MyHomeActivity.this, TaMapActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_home);
		overridePendingTransition(R.anim.anim_into, R.anim.anim_back);
		this.mApplication = (MyApplication) this.getApplication();

		this.mProgressDialog = new ProgressDialog(this);
		this.mProgressDialog.setCancelable(true);
		this.mProgressDialog.setMessage(Constant.DIALOG_MESSAGE);
		this.mProgressDialog.setCanceledOnTouchOutside(true);

		this.sp = this.getSharedPreferences(Constant.sharePath,
				Context.MODE_PRIVATE);
		this.lastUpdate = sp.getLong(Constant.LAST_UPDATE_TIME, 0);
		myOverlay = new MyOverlay();
		myItemOverlay = new MyItemizedOverlay(this);
		this.mListener = new LocListener(TencentMapLBSApi.GEO_TYPE_GCJ02,
				TencentMapLBSApi.LEVEL_ADMIN_AREA, 0);
		int req = TencentMapLBSApi.getInstance().requestLocationUpdate(
				this.getApplicationContext(), mListener);
		if (savedInstanceState == null) {
			mySlideTabFragment = (MySlideTabFragment) MySlideTabFragment
					.instantiate(this, MySlideTabFragment.class.getName());
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, mySlideTabFragment).commit();
		} else {
			Log.i("saved", "我回来了");
			mySlideTabFragment = (MySlideTabFragment) this
					.getSupportFragmentManager().getFragment(
							savedInstanceState,
							MySlideTabFragment.class.getName());
		}
		StatConfig.setDebugEnable(true);
		StatService.trackCustomEvent(this, "onCreate", "where");
		this.getActionBar().setBackgroundDrawable(null);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		this.getSupportFragmentManager().putFragment(outState,
				MySlideTabFragment.class.getName(), mySlideTabFragment);
	}

	public class LocListener extends TencentMapLBSApiListener {

		public LocListener(int reqGeoType, int reqLevel, int reqDelay) {
			super(reqGeoType, reqLevel, reqDelay);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onLocationUpdate(TencentMapLBSApiResult locRes) {
			// TODO Auto-generated method stub
			mOldLocRes = mLocRes;
			mLocRes = locRes;
			if ((mOldLocRes == null && mLocRes != null)
					|| (mLocRes != null && MapUtil.cmpTwoRes(mLocRes,
							mOldLocRes))) {
				updateMapView();
				if (mApplication.mUser.isLogin()) {
					updateMyLocation();
				}
				if (D) {
					Log.i("meinfo", "I am in " + mLocRes.Latitude);
				}
			}
		}

		@Override
		public void onStatusUpdate(int arg0) {
			// TODO Auto-generated method stub
			super.onStatusUpdate(arg0);
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
		StatService.trackEndPage(this, "home");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
		StatService.trackBeginPage(this, "home");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_home, menu);
		this.mLoginMenuItem = menu.findItem(R.id.menu_login);
		tryLoginAuto();
		return true;
	}

	private void updateMyLocation() {
		if (System.currentTimeMillis() - this.lastUpdate > 300000) {
			MyThreadPool.getInstance().getExecutorService()
					.submit(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Map<String, String> params = new HashMap<String, String>();
							params.put("id", Integer
									.toString(mApplication.mUser.getId()));
							if (mLocRes.Latitude != 0 && mLocRes.Longitude != 0) {
								params.put("lat",
										Double.toString(mLocRes.Latitude));
								params.put("lng",
										Double.toString(mLocRes.Longitude));
								MyHttpUtils.postDataByUrl(Constant.WBSITE
										+ "uplocation", params);
								if (D) {
									Log.i("post", "I have posted");
								}
							}
						}

					});
			Editor editor = sp.edit();
			editor.putLong(Constant.LAST_UPDATE_TIME,
					System.currentTimeMillis());
			editor.commit();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (System.currentTimeMillis() - this.end_time >= 2000) {
				Toast.makeText(getApplicationContext(), "再点一次退出应用！",
						Toast.LENGTH_SHORT).show();
				this.end_time = System.currentTimeMillis();
			} else {
				this.startService(new Intent("me.app.home.MyLocationService"));
				this.finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		TencentMapLBSApi.getInstance().removeLocationUpdate();
//		this.startService(new Intent("me.app.home.MyLocationService"));
//		SpotManager.getInstance(this).unregisterSceenReceiver();
//		super.onDestroy();
//	}

	private void showViewPwdDialog() {
		final EditText etViewPwd = new EditText(this);
		etViewPwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		etViewPwd.setHint("限数字和字母");
		DialogFragment dialogFragment = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				// TODO Auto-generated method stub
				return new AlertDialog.Builder(getActivity())
						.setIcon(null)
						.setTitle("请设置您自己的查询密码")
						.setView(etViewPwd)
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										String pwd = etViewPwd.getText()
												.toString().trim();
										if (AbStrUtil.isEmpty(pwd)) {
											Toast.makeText(
													MyHomeActivity.this
															.getApplicationContext(),
													R.string.error_pwd,
													Toast.LENGTH_SHORT).show();
											return;
										}// 长度也可以限制一下
										if (AbStrUtil.strLength(pwd) <= 3) {
											Toast.makeText(
													MyHomeActivity.this
															.getApplicationContext(),
													R.string.error_pwd_len1,
													Toast.LENGTH_SHORT).show();
											return;
										}
										if (AbStrUtil.strLength(pwd) >= 15) {
											Toast.makeText(
													MyHomeActivity.this
															.getApplicationContext(),
													R.string.error_pwd_len2,
													Toast.LENGTH_SHORT).show();
											return;
										}
										if (!AbStrUtil.isNumberLetter(pwd)) {
											Toast.makeText(
													MyHomeActivity.this
															.getApplicationContext(),
													R.string.error_pwd_expr,
													Toast.LENGTH_SHORT).show();
											if (D) {
												Log.i("密码格式有误", pwd);
											}
											return;
										}
										if (D) {
											Log.i("myviewPwd", pwd);
										}
										Message msg = myHandler
												.obtainMessage(3);
										Bundle data = new Bundle();
										data.putString("pwd", pwd);
										msg.setData(data);
										myHandler.sendMessage(msg);
									}

								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}).create();
			}

		};
		dialogFragment.show(this.getFragmentManager().beginTransaction(),
				"dialog1");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.linear_view_pwd:
			showViewPwdDialog();
			break;
		case R.id.sign_in:
			if (mApplication.mUser.isLogin()) {
				Intent intent = new Intent(MyHomeActivity.this,
						SignTodayActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(MyHomeActivity.this.getApplicationContext(),
						"亲，请先登录，谢谢！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.linear_member:
			if (mApplication.mUser.isLogin()) {
				Intent intent = new Intent(MyHomeActivity.this,
						MyLevelActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(MyHomeActivity.this.getApplicationContext(),
						"亲，请先登录，谢谢！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.use_instruction:
			Intent intent = new Intent(MyHomeActivity.this,
					SoftUseActivity.class);
			startActivity(intent);
			break;
		case R.id.linear_us:
			Intent intent2 = new Intent(MyHomeActivity.this,
					AboutUsActivity.class);
			startActivity(intent2);
			break;
		case R.id.linear_leave_msg:
			Intent intent3 = new Intent(MyHomeActivity.this,
					LeaveMsgActivity.class);
			startActivity(intent3);
			break;
			/*
			 * 注释了应用推荐和游戏推荐的两个链接
			 */
//		case R.id.linear_app:
//			DiyManager.showRecommendAppWall(MyHomeActivity.this);
//			break;
//		case R.id.linear_game:
//			DiyManager.showRecommendGameWall(MyHomeActivity.this);
//			break;
		default:
			break;

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_login:
			if (!mApplication.mUser.isLogin()) {
				intent = new Intent(MyHomeActivity.this, LoginActivity.class);
				startActivityForResult(intent, 1);
			} else {
				mApplication.mUser.setLogin(false);
				mLoginMenuItem.setTitle("登录");
				Toast.makeText(MyHomeActivity.this.getApplicationContext(),
						"您已经退出当前帐号", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void updateMapView() {
		// TODO Auto-generated method stub
		if (null != mApplication.mMapView && null != mLocRes) {
			mApplication.mMapView.clearAllOverlays();
			mApplication.mMapView.getController().setZoom(15);
			mApplication.mMapView.getController().setCenter(
					new GeoPoint((int) (mLocRes.Latitude * 1E6),
							(int) (mLocRes.Longitude * 1E6)));
			myOverlay.setLat(mLocRes.Latitude);
			myOverlay.setLng(mLocRes.Longitude);
			mApplication.mMapView.addOverlay(myOverlay);
			List<TencentMapLBSApiResult> results = new ArrayList<TencentMapLBSApiResult>();
			results.add(mLocRes);
			myItemOverlay.setLbsRes(results);
			mApplication.mMapView.addOverlay(myItemOverlay);
			if (D) {
				Log.i("map", "I have changed!");
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (1 == resultCode) {
			if (this.mLoginMenuItem != null) {
				this.mLoginMenuItem.setTitle("退出帐号");
			}
		}
	}

//	@Override
//	public void onBackPressed() {
//		// 如果有需要，可以点击后退关闭插播广告。
//		if (!SpotManager.getInstance(this).disMiss()) {
//			super.onBackPressed();
//		}
//	}

	// @Override
	// protected void onStop() {
	// // 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
	// SpotManager.getInstance(this).disMiss();
	// super.onStop();
	// }

	@Override
	public void checkTaLogin(final String name, final String pwd,
			final boolean isRem) {
		// TODO Auto-generated method stub
		this.mProgressDialog.show();
		this.mProgressDialog.setTitle(Constant.DIALOG_MESSAGE);
		MyThreadPool.getInstance().getExecutorService().submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", name);
				params.put("pwd", pwd);
				JSONObject ta = MyHttpUtils.getDataByUrl(Constant.WBSITE
						+ "checkta", params);
				if (ta != null) {
					Message msg = myHandler.obtainMessage(1);
					msg.obj = ta;
					taName = name;
					taPwd = pwd;
					taRember = isRem;
					myHandler.sendMessage(msg);
				} else {
					myHandler.sendEmptyMessage(0);
				}
			}

		});
	}
}
