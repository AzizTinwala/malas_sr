package com.Amitlibs.net;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.Amitlibs.net.toolbox.HttpURLConnectionHelper;
import com.Amitlibs.utils.AppUtils;
import com.Amitlibs.utils.StreamUtils;
import com.Amitlibs.CustomMultipartEntity.AndroidMultiPartEntity;
import com.android.volley.Request.Method;

public class JSONParser {

	public enum Http {
		GET, POST
    }

	public String getResponse(String url, List<NameValuePair> params) {
		HttpURLConnection conn = null;
		try {
			conn = HttpURLConnectionHelper.makeHTTPConnection(url + "?" + AppUtils.getUrlEncodedString(params));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (conn != null) {
			try {
				conn.setRequestProperty("Connection", "keep-alive");
				conn.setRequestMethod("GET");
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					InputStreamReader isr = new InputStreamReader(conn.getInputStream());
					String msg = StreamUtils.readData(isr);
					isr.close();
					return msg;
				}
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.disconnect();
			}
		}
		return null;
	}

	public synchronized String getResponseFromHttpPost(String url, List<NameValuePair> params) {
		InputStream is = null;
		String response = "";

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			if (params != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			}
			HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), false);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			response = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result ");
			e.printStackTrace();
		}
		return response;
	}

	public synchronized String getResponseFromHttpGet(String url, List<NameValuePair> params) {
		InputStream is = null;
		String response = "";

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(url);

			if (params != null) {
				String format = URLEncodedUtils.format(params, "UTF=8");
				httpGet = new HttpGet(url + "?" + format);
			}
			HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), false);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			response = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result ");
			e.printStackTrace();
		}
		return response;
	}

	public synchronized JSONObject getJsonObjectFromHttp(String url, List<NameValuePair> params, Http method) {
		String resp;

		if (method == Http.GET) {
			resp = getResponseFromHttpGet(url, params);
		} else {
			resp = getResponseFromHttpPost(url, params);
		}

		JSONObject jObj = null;

		Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + params + "\nREPSONSE: " + resp);

		try {
			jObj = new JSONObject(resp);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing object ");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jObj;
	}

	public synchronized JSONArray getJsonArrayFromHttp(String url, List<NameValuePair> params) {

		String resp = getResponse(url, params);
		JSONArray jArray = null;

		Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + params + "\nREPSONSE: " + resp);

		try {
			jArray = new JSONArray(resp);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing array ");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jArray;
	}

	public synchronized JSONObject getJsonObjectFromUrl(String url, List<NameValuePair> params) {

		String resp = getResponse(url, params);
		JSONObject jObj = null;

		Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + params + "\nREPSONSE: " + resp);

		try {
			jObj = new JSONObject(resp);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing object ");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jObj;
	}

	public synchronized JSONArray getJsonArrayFromUrl(String url, List<NameValuePair> params) {

		String resp = getResponse(url, params);
		JSONArray jArray = null;

		Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + params + "\nREPSONSE: " + resp);

		try {
			jArray = new JSONArray(resp);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing array ");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jArray;
	}

	public synchronized String getResponseFromMultipart(String url, MultipartEntityBuilder builder) {
		InputStream is = null;
		String response = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(builder.build());
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			response = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result ");
			e.printStackTrace();
		}
		return response;
	}

	public synchronized String getResponseFromMultipart(String url, AndroidMultiPartEntity builder) {
		InputStream is = null;
		String response = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(builder);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			response = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result ");
			e.printStackTrace();
		}
		return response;
	}

	public synchronized JSONObject getJsonObjectFromMultipart(String url, MultipartEntityBuilder builder) {

		String resp = getResponseFromMultipart(url, builder);
		Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + builder.toString() + "\nREPSONSE: " + resp);

		JSONObject jObj = null;

		try {
			jObj = new JSONObject(resp);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing object ");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jObj;
	}
}