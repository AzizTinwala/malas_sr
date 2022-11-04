package com.Amitlibs.net;


import android.annotation.SuppressLint;
import android.util.Log;

import com.Amitlibs.CustomMultipartEntity.AndroidMultiPartEntity;
import com.Amitlibs.net.toolbox.HttpURLConnectionHelper;
import com.Amitlibs.utils.AppUtils;
import com.Amitlibs.utils.StreamUtils;import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class HttpUrlConnectionJSONParser {

    public enum Http {
        GET, POST
    }

    public String getResponse(String url, List<NameValuePair> params) {
        HttpURLConnection conn = null;
        try {
            conn = HttpURLConnectionHelper.makeHTTPConnection(url + "?" + AppUtils.getUrlEncodedString(params));
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(15000);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (conn != null) {
            try {
                conn.setRequestProperty("Connection", "keep-alive");
                conn.setRequestMethod("GET");
//                conn.setRequestMethod("POST");
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


    private synchronized String getResponseFromHttpPostJsonRequestWithHeader(String url, JSONObject params, String header) {
        InputStream is = null;
        String response = "";

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("authkey", header);
            if (params != null) {
                StringEntity requestEntity = new StringEntity(params.toString(), HTTP.UTF_8);
                requestEntity.setContentType("application/json");
                httpPost.setEntity( requestEntity);
            }
            HttpProtocolParams.setUseExpectContinue((HttpParams) httpClient.getParams(), false);
            HttpResponse httpResponse = (HttpResponse) httpClient.execute(httpPost);
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
            @SuppressLint({"NewApi", "LocalSuppress"}) BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1), 8);
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

    private synchronized String getResponseFromHttpPost(String myurl, List<NameValuePair> params) {
       InputStream is = null;
        String response = "";

        try {
            URL url = new URL(myurl);
            StringBuilder postData = new StringBuilder();
            for (int i = 0; i < params.size(); i++) {
                if (postData.length() != 0)
                    postData.append('&');
                postData.append(URLEncoder.encode(params.get(i).getName(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(params.get(i).getValue()), "UTF-8"));
            }
            @SuppressLint({"NewApi", "LocalSuppress"}) byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

           InputStream in = new BufferedInputStream(conn.getInputStream());
            //    String result = extract(in);
            response = org.apache.commons.io.IOUtils.toString(in,"UTF_8");
            in.close();
            conn.disconnect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    private synchronized String getResponseFromHttpGet(String myurl, List<NameValuePair> params) {
        InputStream is = null;
        String response = "";
        int len = 500;
        try {
            if (params != null) {
                String format = URLEncodedUtils.format(params, "UTF-8");
                myurl = myurl + "?" + format;
            }
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int responsecode = conn.getResponseCode();
            is = conn.getInputStream();
            response = readIt(is, len);

            is.close();
            conn.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @SuppressLint("NewApi")
    private synchronized String getResponseFromHttpPostWithJsonObject(String myurl, List<NameValuePair> params, List<JSONObject> jsonParams) {
        InputStream is = null;
        String response = "";
        OutputStream os;
        try {
            URL url = new URL(myurl);
            StringBuilder postData = new StringBuilder();
            for (int i = 0; i < params.size(); i++) {
                if (postData.length() != 0)
                    postData.append('&');
                postData.append(URLEncoder.encode(params.get(i).getName(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(params.get(i).getValue()), "UTF-8"));
            }
            @SuppressLint({"NewApi", "LocalSuppress"})   byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
            @SuppressLint({"NewApi", "LocalSuppress"})
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

            /* urlConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConn.connect();
            os = urlConn.getOutputStream();
            os.write(params.toString().getBytes("UTF-8"));
            os.close();
            InputStream in = new BufferedInputStream(urlConn.getInputStream());
            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            response = result;*/

            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.connect();

            os = conn.getOutputStream();
            os.write(jsonParams.toString().getBytes(StandardCharsets.UTF_8));
            os.close();


            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = org.apache.commons.io.IOUtils.toString(in, StandardCharsets.UTF_8);
            response = result;
            in.close();
            conn.disconnect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private synchronized String getResponseFromMultipart(String myurl, AndroidMultiPartEntity reqEntity) {
        InputStream is = null;
        String response = "";
        try {

            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(25000);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Content-length", reqEntity.getContentLength() + "");
            conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());
            OutputStream os = conn.getOutputStream();
            reqEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = readStream(conn.getInputStream());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public synchronized JSONObject getJsonFromHttpJsonRequestWithHeader(String url, JSONObject mJsonObject, String header) {
        String resp = getResponseFromHttpPostJsonRequestWithHeader(url, mJsonObject, header);
        JSONObject jObj = null;
        Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + mJsonObject + "\nREPSONSE: InProcessing....");

        try {
            jObj = new JSONObject(resp);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing object ");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + mJsonObject + "\nREPSONSE: " + resp);
        return jObj;
    }

    @SuppressLint("NewApi")
    private synchronized String getResponseFromHttpUrlConnectionJsonRequest_Post(String myurl, JSONObject params) {
        URL url;
        HttpURLConnection urlConn;
        OutputStream os;
        DataInputStream is;
        String response = "";

        try {
            url = new URL(myurl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConn.connect();
            os = urlConn.getOutputStream();
            os.write(params.toString().getBytes(StandardCharsets.UTF_8));
            os.close();
            InputStream in = new BufferedInputStream(urlConn.getInputStream());
            response = org.apache.commons.io.IOUtils.toString(in, StandardCharsets.UTF_8);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public synchronized JSONObject getJsonFromHttpUrlConnectionJsonRequest_Post(String url, JSONObject mJsonObject) {
        String resp = "";
        Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + mJsonObject + "\nREPSONSE: under processing...");
        resp = getResponseFromHttpUrlConnectionJsonRequest_Post(url, mJsonObject);
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(resp);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing object ");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + mJsonObject + "\nREPSONSE: " + resp);
        return jObj;
    }


    public synchronized JSONObject getJsonObjectFromHttpUrlConnection(String url, List<NameValuePair> params, Http method) {
        String resp;
        Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + params + "\nREPSONSE: in processing...");
        if (method == Http.GET) {
            resp = getResponseFromHttpGet(url, params);

        } else {
            resp = getResponseFromHttpPost(url, params);
        }

        JSONObject jObj = null;

        Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + params + "\nREPSONSE: " + resp);
        resp = resp.substring(resp.indexOf("{") == -1 ? 0 : resp.indexOf("{"));
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

    public synchronized JSONObject getJsonObjectFromMultipart(String url, AndroidMultiPartEntity multipartEntity) {

        String resp = getResponseFromMultipart(url, multipartEntity);
        Log.d(getClass().getName(), "URL: " + url + "\nPARAMS: " + multipartEntity.toString() + "\nREPSONSE: " + resp);
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

    @SuppressLint({"NewApi", "LocalSuppress"})
    private String readIt(InputStream stream, int len) throws IOException {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();


    }
}