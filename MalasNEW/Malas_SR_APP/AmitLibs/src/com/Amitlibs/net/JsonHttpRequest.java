package com.Amitlibs.net;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Amitlibs.AmitLibs;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

public class JsonHttpRequest {
	public interface JsonResponseListener<T> {
		void onResponse(T json);

		void onError(VolleyError error);
	}

	public static void getJsonObject(String urlWithParams, final JsonResponseListener<JSONObject> responseListener) {

		final Listener<JSONObject> success = new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				responseListener.onResponse(response);
			}
		};
		final ErrorListener error = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				responseListener.onError(error);
			}
		};

		RequestQueue requestQueue = AmitLibs.getRequestQueue();
		JsonObjectRequest myReq = new JsonObjectRequest(Method.GET, urlWithParams, null, success, error);
		requestQueue.add(myReq);
	}

	public static void getJsonArray(String urlWithParams, final JsonResponseListener<JSONArray> responseListener) {

		final Listener<JSONArray> success = new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				responseListener.onResponse(response);
			}
		};
		final ErrorListener error = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				responseListener.onError(error);
			}
		};

		RequestQueue requestQueue = AmitLibs.getRequestQueue();
		JsonArrayRequest myReq = new JsonArrayRequest(urlWithParams, success, error);
		requestQueue.add(myReq);
	}

}
