package me.app.home;

import java.util.HashMap;
import java.util.Map;

import me.app.global.Constant;
import me.app.global.MyApplication;
import me.http.utils.MyHttpUtils;
import me.http.utils.MyThreadPool;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

import com.tencent.tencentmap.lbssdk.TencentMapLBSApi;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiListener;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;

public class MyLocationService extends Service {

	private LocListener mListener = null;
	private MyApplication mApplication = null;
	private TencentMapLBSApiResult mLocRes = null;
	private TencentMapLBSApiResult mOldLocRes = null;
	private SharedPreferences sp = null;
	private long lastUpdate;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.mListener = new LocListener(TencentMapLBSApi.GEO_TYPE_GCJ02,
				TencentMapLBSApi.LEVEL_ADMIN_AREA, 0);
		// TencentMapLBSApi.getInstance().setGPSUpdateInterval(6000);
		this.mApplication = (MyApplication) this.getApplication();
		int req = TencentMapLBSApi.getInstance().requestLocationUpdate(
				this.getApplicationContext(), mListener);
		this.sp = this.getSharedPreferences(Constant.sharePath,
				Context.MODE_PRIVATE);
		this.lastUpdate = sp.getLong(Constant.LAST_UPDATE_TIME, 0);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("service2", "我在服务中啊！");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		TencentMapLBSApi.getInstance().removeLocationUpdate();
		super.onDestroy();
	}

	private class LocListener extends TencentMapLBSApiListener {

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
				Log.i("service", "我变化了，来自于服务！" + locRes.toString());
				if (mApplication.mUser.getId() != -1) {
					updateMyLocation();
					Log.i("id", "id" + mApplication.mUser.getId());
				}
			}
		}

		@Override
		public void onStatusUpdate(int arg0) {
			// TODO Auto-generated method stub
			super.onStatusUpdate(arg0);
		}

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
							}
						}

					});
			Editor editor = sp.edit();
			editor.putLong(Constant.LAST_UPDATE_TIME,
					System.currentTimeMillis());
			editor.commit();
		}
	}

}
