package me.http.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.app.global.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MyHttpUtils {

	public static JSONObject getDataByUrl(String url,
			Map<String, String> mapParams) {
		if (url == null) {
			return null;
		}
		HttpClient httpClient = new DefaultHttpClient();
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(httpParams, Constant.TIME_OUT);
		HttpConnectionParams
				.setConnectionTimeout(httpParams, Constant.TIME_OUT);

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : mapParams.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		String mUrl = url + "?"
				+ URLEncodedUtils.format(params, "UTF-8").trim();
		Log.i("URL", mUrl);

		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.setParams(httpParams);

		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
				try {
					JSONObject resultObject = new JSONObject(result);
					return resultObject;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static JSONArray getDatasByUrl(String url,
			Map<String, String> mapParams) {
		if (url == null) {
			return null;
		}
		HttpClient httpClient = new DefaultHttpClient();
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(httpParams, Constant.TIME_OUT);
		HttpConnectionParams
				.setConnectionTimeout(httpParams, Constant.TIME_OUT);
		String mUrl = null;
		if (mapParams != null) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> entry : mapParams.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			mUrl = url + "?" + URLEncodedUtils.format(params, "UTF-8").trim();
			Log.i("URL", mUrl);
		} else {
			mUrl = url;
		}

		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.setParams(httpParams);

		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
				try {
					JSONArray resultObject = new JSONArray(result);
					return resultObject;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject postDataByUrl(String url,
			Map<String, String> mapParams) {
		if (url == null || mapParams == null) {
			return null;
		}
		HttpClient httpClient = new DefaultHttpClient();
		BasicHttpParams httpParams = new BasicHttpParams();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : mapParams.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		HttpConnectionParams.setSoTimeout(httpParams, Constant.TIME_OUT);
		HttpConnectionParams
				.setConnectionTimeout(httpParams, Constant.TIME_OUT);
		HttpPost httpPost = new HttpPost(url);
		Log.i("post", url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			httpPost.setParams(httpParams);
			try {
				HttpResponse response = httpClient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(response.getEntity());
					try {
						JSONObject resultObject = new JSONObject(result);
						return resultObject;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
}
