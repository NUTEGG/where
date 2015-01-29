package me.app.global;

import me.app.data.Member;
import me.app.data.Ta;
import me.app.data.User;
import me.app.parse.ParseJson;
import me.http.utils.MyHttpUtils;
import me.http.utils.MyThreadPool;

import org.json.JSONArray;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import com.tencent.tencentmap.mapsdk.map.MapView;

public class MyApplication extends Application {
	public MapView mMapView;
	public User mUser;
	public Ta ta;
	public SparseArray<Member> mapMember;
	public boolean isFirst = true;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				Log.i("application", "I am in application!");
				JSONArray array = (JSONArray) msg.obj;
				ParseJson.pasreMember(array, mapMember);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.initLoginParams();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	private void initLoginParams() {
		mapMember = new SparseArray<Member>();
		SharedPreferences sp = this.getSharedPreferences(Constant.sharePath,
				Context.MODE_PRIVATE);
		String name = sp.getString(Constant.USER_NAME_COOKIE, null);
		String taName = sp.getString(Constant.USER_TANAME_COOKIE, null);
		String pwd = sp.getString(Constant.USER_PASSWORD_COOKIE, null);
		String taPwd = sp.getString(Constant.USER_TAPASSWORD_COOKIE, null);
		int id = sp.getInt(Constant.USER_ID_COOKIE, -1);
		boolean remember = sp.getBoolean(
				Constant.USER_PASSWORD_REMEMBER_COOKIE, false);
		boolean taRember = sp.getBoolean(
				Constant.USER_TAPASSWORD_REMEMBER_COOKIE, false);
		this.mUser = new User();
		this.ta = new Ta();
		if (name != null && pwd != null) {
			mUser.setName(name);
			mUser.setPwd(pwd);
			mUser.setRemerberPwd(remember);
			mUser.setId(id);
			mUser.setLogin(false);
		}
		if (taName != null && taPwd != null) {
			ta.setName(taName);
			ta.setPwd(taPwd);
			ta.setRember(taRember);
		}
		MyThreadPool.getInstance().getExecutorService().submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String mUrl = Constant.WBSITE + "member";
				JSONArray array = MyHttpUtils.getDatasByUrl(mUrl, null);
				if (array != null) {
					Log.i("json member", array.toString());
					Message msg = mHandler.obtainMessage(0);
					msg.obj = array;
					mHandler.sendMessage(msg);
				}
			}

		});
	}

	private void clearLoginParams() {
		SharedPreferences sp = this.getSharedPreferences(Constant.sharePath,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

}
