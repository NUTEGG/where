package me.app.home;

import java.util.ArrayList;
import java.util.List;

import me.app.data.LocationInfo;
import me.app.global.Constant;
import android.app.AlertDialog;
import android.content.Context;

import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.ItemizedOverlay;
import com.tencent.tencentmap.mapsdk.map.OverlayItem;
import com.tencent.tencentmap.mapsdk.search.GeocoderSearch;
import com.tencent.tencentmap.mapsdk.search.ReGeocoderResult;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private static final boolean D = Constant.DEBUG;
	private List<OverlayItem> overlayItems = null;
	private List<TencentMapLBSApiResult> mLbsRes = null;
	private List<LocationInfo> mLocationInfos = null;

	private Context context = null;

	public MyItemizedOverlay(Context mContext) {
		super(mContext);
		this.context = mContext;
		this.overlayItems = new ArrayList<OverlayItem>();
		this.setOnFocusChangeListener(new com.tencent.tencentmap.mapsdk.map.ItemizedOverlay.OnFocusChangeListener() {

			@Override
			public void onFocusChanged(ItemizedOverlay<?> oldlay,
					OverlayItem newlay) {
				// TODO Auto-generated method stub
				if (newlay != null) {
					// Toast.makeText(context, newlay.getSnippet(),
					// Toast.LENGTH_LONG).show();
					new AlertDialog.Builder(context).setIcon(null)
							.setTitle("详细信息").setMessage(newlay.getSnippet())
							.setCancelable(true).setPositiveButton("知道啦", null)
							.show();
				}
			}

		});
		// TODO Auto-generated constructor stub
	}

	public List<TencentMapLBSApiResult> getLbsRes() {
		return mLbsRes;
	}

	public void setLbsRes(List<TencentMapLBSApiResult> lbsRes) {
		this.mLbsRes = lbsRes;
		this.overlayItems.clear();
		for (TencentMapLBSApiResult res : mLbsRes) {
			overlayItems.add(new OverlayItem(new GeoPoint(
					(int) (res.Latitude * 1E6), (int) (res.Longitude * 1E6)),
					"p", MapUtil.resultToString(res)));
		}
		populate();
	}

	public List<LocationInfo> getmLocationInfos() {
		return mLocationInfos;
	}

	public void setmLocationInfos(List<LocationInfo> mLocationInfos,
			double mLat, double mLng, String mLoc) {
		this.mLocationInfos = mLocationInfos;
		this.overlayItems.clear();
		for (LocationInfo info : mLocationInfos) {
			GeoPoint geoPoint = new GeoPoint((int) (info.getLat() * 1E6),
					(int) (info.getLng() * 1E6));
			ReGeocoderResult result = this.searchFromGeo(geoPoint);
			if (result != null) {
				overlayItems.add(new OverlayItem(geoPoint, "p", info.getTime()
						+ " Ta在这里:" + result.addresslist.get(0).name));
			} else {
				overlayItems.add(new OverlayItem(geoPoint, "p", info.getTime()
						+ " Ta在这里"));
			}
		}
		GeoPoint geoPoint = new GeoPoint((int) (mLat * 1E6), (int) (mLng * 1E6));
		overlayItems.add(new OverlayItem(geoPoint, "p", "我在：" + mLoc));
		populate();
	}

	public void setmLocationInfos(List<LocationInfo> mLocationInfos) {
		this.mLocationInfos = mLocationInfos;
		this.overlayItems.clear();
		for (LocationInfo info : mLocationInfos) {
			GeoPoint geoPoint = new GeoPoint((int) (info.getLat() * 1E6),
					(int) (info.getLng() * 1E6));
			ReGeocoderResult result = this.searchFromGeo(geoPoint);
			if (result != null) {
				overlayItems.add(new OverlayItem(geoPoint, "p", info.getTime()
						+ " Ta在这里:" + result.addresslist.get(0).name));
			} else {
				overlayItems.add(new OverlayItem(geoPoint, "p", info.getTime()
						+ " Ta在这里"));
			}
		}
		populate();
	}

	@Override
	protected OverlayItem createItem(int pos) {
		// TODO Auto-generated method stub
		if (overlayItems == null) {
			return null;
		}
		return overlayItems.get(pos);
	}

	private ReGeocoderResult searchFromGeo(GeoPoint geoPoint) {
		GeocoderSearch geocoderSearch = new GeocoderSearch(this.context);
		try {
			return geocoderSearch.searchFromLocation(geoPoint);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		if (overlayItems == null) {
			return 0;
		}
		return overlayItems.size();
	}

}
