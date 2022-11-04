package com.Amitlibs;

import org.apache.http.conn.*;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;

import com.Amitlibs.image.ImageCacheManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AmitLibs {

	private static final int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
	private static final CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static final int DISK_IMAGECACHE_QUALITY = 100;

	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;
	private static DefaultHttpClient httpClient;

	private AmitLibs() {
	}

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		// int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		// int cacheSize = 1024 * 1024 * memClass / 8;

		ImageCacheManager cacheManager = ImageCacheManager.getInstance();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			cacheManager.init(context, context.getPackageCodePath(), DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT, DISK_IMAGECACHE_QUALITY);
		}
		mImageLoader = new ImageLoader(mRequestQueue, cacheManager);

		httpClient = new DefaultHttpClient();
		ClientConnectionManager manager = httpClient.getConnectionManager();
		HttpParams params = httpClient.getParams();
		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, manager.getSchemeRegistry()), params);
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache} which effectively means that no memory caching is used. This is useful for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}

	public static DefaultHttpClient getHttpClient() {
		return AmitLibs.httpClient;
	}

	public static void setHttpClient(DefaultHttpClient httpClient) {
		AmitLibs.httpClient = httpClient;
	}

}
