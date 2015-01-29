package me.app.home;

import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;

public class MapUtil {
	public static String resultToString(TencentMapLBSApiResult result) {
		StringBuilder sb = new StringBuilder();
		if (result != null) {
			// if (result.Province != null &&
			// !result.Province.equals("Unknown")) {
			// sb.append(result.Province);
			// }
			if (result.City != null && !result.City.equals("Unknown")) {
				sb.append(result.City);
			}
			if (result.District != null && !result.District.equals("Unknown")) {
				sb.append(result.District);
			}
			if (result.Town != null && !result.Town.equals("Unknown")) {
				sb.append(result.Town);
			}
			if (result.Village != null && !result.Village.equals("Unknown")) {
				sb.append(result.Village);
			}
			if (result.Street != null && !result.Street.equals("Unknown")) {
				sb.append(result.Street);
			}
			if (result.StreetNo != null && !result.StreetNo.equals("Unknown")) {
				sb.append(result.StreetNo);
			}
		}
		return sb.toString();
	}

	public static boolean cmpTwoRes(TencentMapLBSApiResult resA,
			TencentMapLBSApiResult resB) {
		if (resA != null && resB != null) {
			if (resA.Latitude - resB.Latitude >= 0.035
					|| resA.Longitude - resB.Longitude >= 0.035) {
				return true;
			}
		}
		return false;
	}
}
