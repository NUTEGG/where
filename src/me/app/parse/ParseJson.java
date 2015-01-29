package me.app.parse;

import java.util.ArrayList;
import java.util.List;

import me.app.data.LocationInfo;
import me.app.data.Member;
import me.app.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseArray;

public class ParseJson {

	public static boolean parseResult(JSONObject jsonObj) {
		if (jsonObj == null) {
			return false;
		}
		try {
			int result = jsonObj.getInt("result");
			return result == 1 ? true : false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean parseUser(JSONObject jsonObj, User user) {
		if (jsonObj == null || user == null) {
			return false;
		}
		int id, score;
		try {
			id = jsonObj.getInt("id");
			score = jsonObj.getInt("score");
			user.setId(id);
			user.setScore(score);
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void pasreMember(JSONArray jsonArray,
			SparseArray<Member> mapMember) {
		if (jsonArray == null || mapMember == null) {
			return;
		}
		for (int i = 0, len = jsonArray.length(); i < len; i++) {
			try {
				JSONObject obj = jsonArray.getJSONObject(i);
				int id = obj.getInt("id");
				String name = obj.getString("name");
				int score = obj.getInt("score");
				int viewNum = obj.getInt("view_num");
				mapMember.put(id, new Member(id, name, score, viewNum));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static List<LocationInfo> parseLocations(JSONArray locations) {
		if (locations == null) {
			return null;
		}
		List<LocationInfo> mLocations = new ArrayList<LocationInfo>();
		for (int i = 0, len = locations.length(); i < len; i++) {
			try {
				JSONObject location = locations.getJSONObject(i);
				double lat = location.getDouble("lat");
				double lng = location.getDouble("lng");
				String time = location.getString("local_time");
				mLocations.add(new LocationInfo(lat, lng, time));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return mLocations;
	}

	public static int parseTaId(JSONObject ta) {
		if (ta == null) {
			return -1;
		}
		int id = -1;
		try {
			id = ta.getInt("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return id;
	}

	public static int parseMyData(JSONObject obj, User user) {
		if (obj == null) {
			return -1;
		}
		int id = -1;
		try {
			id = obj.getInt("id");
			int score = obj.getInt("score");
			int member = obj.getInt("member_id");
			user.setId(id);
			user.setScore(score);
			user.setMember(member);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return id;
	}

}
