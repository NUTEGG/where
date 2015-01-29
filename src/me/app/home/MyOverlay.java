package me.app.home;

import me.app.global.Constant;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.Overlay;

public class MyOverlay extends Overlay {

	private double lat = 0, lng = 0;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	protected void draw(Canvas canvas, MapView mapView) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView);
		GeoPoint geoCircle = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		Point ptCicle = mapView.getProjection().toPixels(geoCircle, null);
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setAlpha(120);
		float radius = mapView.getProjection().metersToEquatorPixels(
				Constant.RADIUS);
		canvas.drawCircle(ptCicle.x, ptCicle.y, radius, paint);
	}

}
