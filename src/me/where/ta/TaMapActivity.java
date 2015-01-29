package me.where.ta;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.app.data.LocationInfo;
import me.app.global.Constant;
import me.app.home.MyItemizedOverlay;
import me.app.home.MyOverlay;
import me.app.parse.ParseJson;
import me.http.utils.MyHttpUtils;
import me.http.utils.MyThreadPool;
import me.where.home.R;
import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import org.json.JSONArray;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;

public class TaMapActivity extends MapActivity {

	private static final boolean D = Constant.DEBUG;
	private MapView mapView;
	private ProgressDialog mProgressDialog;
	private int taId;
	private MyHandler mHandler;
	private MyItemizedOverlay mItemizedOverlay;
	private double mLat, mLng;
	private String mLoc;
	private MyOverlay myOverlay;
	private boolean haveMe = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment);
		overridePendingTransition(R.anim.anim_into, R.anim.anim_back);
		this.mapView = (MapView) findViewById(R.id.mapview);
		this.mProgressDialog = new ProgressDialog(this);
		this.mProgressDialog.setMessage(Constant.DIALOG_MESSAGE);
		this.mProgressDialog.setCancelable(true);
		this.mProgressDialog.setCanceledOnTouchOutside(true);
		this.mProgressDialog.show();

		this.mHandler = new MyHandler(TaMapActivity.this);

		this.myOverlay = new MyOverlay();
		this.mItemizedOverlay = new MyItemizedOverlay(this);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		taId = bundle.getInt("id");
		if (bundle.getBoolean("me")) {
			this.haveMe = true;
			this.mLat = bundle.getDouble("lat");
			this.mLng = bundle.getDouble("lng");
			this.mLoc = bundle.getString("location");
		}

		if (taId != -1) {
			MyThreadPool.getInstance().getExecutorService()
					.submit(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Map<String, String> params = new HashMap<String, String>();
							params.put("id", Integer.toString(taId));
							JSONArray taDatas = MyHttpUtils.getDatasByUrl(
									Constant.WBSITE + "getta", params);
							if (taDatas != null) {
								Message msg = mHandler.obtainMessage(1);
								msg.obj = taDatas;
								mHandler.sendMessage(msg);
							} else {
							}
						}
					});
		}

		if (Constant.MI) {
			AdManager.getInstance(this.getApplicationContext()).init(
					"aaab3ee0a6e55113", "835c8257524ceeb5", false);
			SpotManager.getInstance(this).loadSpotAds();
			// 设置展示超时时间，加载超时则不展示广告，默认0，代表不设置超时时间
			SpotManager.getInstance(this).setSpotTimeout(5000);// 设置5秒
			//SpotManager.getInstance(this).setSpotTimeout(20000);// 设置20秒的显示时间间隔
			// 如需要使用自动关闭插屏功能，请取消注释下面方法
//			SpotManager.getInstance(this).setAutoCloseSpot(true);// 设置自动关闭插屏开关
//			SpotManager.getInstance(this).setCloseTime(2300); // 设置关闭插屏时间
//			SpotManager.getInstance(this).showSpotAds(this, null);
		}
	}

	private static final class MyHandler extends Handler {
		private final WeakReference<TaMapActivity> taMapActivity;

		private MyHandler(TaMapActivity taMapActivity) {
			this.taMapActivity = new WeakReference<TaMapActivity>(taMapActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			TaMapActivity taActivity = taMapActivity.get();
			if (taActivity != null) {
				switch (msg.what) {
				case 0:
					taActivity.dismissDialog();
					Toast.makeText(taActivity.getApplicationContext(),
							"对不起，暂时没有Ta的位置信息哦！！", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					JSONArray taDatas = (JSONArray) msg.obj;
					taActivity.showDatasOnMap(taDatas);
					taActivity.dismissDialog();
					if (D) {
						Toast.makeText(taActivity.getApplicationContext(),
								"获取Ta的数据成功！", Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
				}
			}
		}
	};

	private void dismissDialog() {
		this.mProgressDialog.dismiss();
	}

	private void showDatasOnMap(JSONArray taDatas) {
		List<LocationInfo> mLocationInfos = ParseJson.parseLocations(taDatas);
		if (null != mLocationInfos) {
			this.mapView.clearAllOverlays();
			this.mapView.getController().setZoom(16);
			if (haveMe) {
				this.mItemizedOverlay.setmLocationInfos(mLocationInfos, mLat,
						mLng, mLoc);
				// GeoPoint geoPoint = this.mItemizedOverlay.getCenter();
				// mapView.getController().setCenter(geoPoint);
				LocCenterPoint center = getCenterPoint(mLocationInfos);
				mapView.getController().setCenter(
						new GeoPoint((int) (center.lat * 1E6),
								(int) (center.lng * 1E6)));
				myOverlay.setLat(mLat);
				myOverlay.setLng(mLng);
				mapView.addOverlay(myOverlay);
			} else {
				this.mItemizedOverlay.setmLocationInfos(mLocationInfos);
				// GeoPoint geoPoint = this.mItemizedOverlay.getCenter();
				// mapView.getController().setCenter(geoPoint);
				LocCenterPoint center = getCenterPoint(mLocationInfos);
				mapView.getController().setCenter(
						new GeoPoint((int) (center.lat * 1E6),
								(int) (center.lng * 1E6)));
				LocCenterPoint centerPoint = getCenterPoint(mLocationInfos);
				myOverlay.setLat(centerPoint.lat);
				myOverlay.setLng(centerPoint.lng);
				mapView.addOverlay(myOverlay);
			}
			if (D) {
				Log.i("showdata", mLocationInfos.get(0).getTime()
						+ mLocationInfos.size());
			}
			this.mapView.addOverlay(mItemizedOverlay);
		}
	}

	private LocCenterPoint getCenterPoint(List<LocationInfo> mLocationInfos) {
		double latSum = 0.0, lngSum = 0.0;
		int len = mLocationInfos.size();
		for (int i = 0; i < len; i++) {
			latSum += mLocationInfos.get(i).getLat();
			lngSum += mLocationInfos.get(i).getLng();
		}
		return new LocCenterPoint(latSum / len, lngSum / len);
	}

	public class LocCenterPoint {
		double lat;
		double lng;

		public LocCenterPoint(double lat, double lng) {
			this.lat = lat;
			this.lng = lng;
		}
	}

//	@Override
//	public void onBackPressed() {
//		// 如果有需要，可以点击后退关闭插播广告。
//		if (!SpotManager.getInstance(this).disMiss()) {
//			super.onBackPressed();
//			overridePendingTransition(R.anim.close_enter, R.anim.close_exit);
//		}
//	}
//
//	@Override
//	protected void onStop() {
//		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
//		SpotManager.getInstance(this).disMiss();
//		super.onStop();
//	}
//
//	@Override
//	protected void onDestroy() {
//		SpotManager.getInstance(this).unregisterSceenReceiver();
//		super.onDestroy();
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ta_map, menu);
		return true;
	}

}
