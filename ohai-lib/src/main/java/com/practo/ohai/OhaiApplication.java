package com.practo.ohai;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestTickle;
import com.android.volley.VolleyLog;
import com.android.volley.cache.DiskLruBasedCache.ImageCacheParams;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.VolleyTickle;
import com.practo.ohai.utils.SimpleImageLoader;

public class OhaiApplication extends Application {

	public static final String TAG = "SynapseVolley";
	public static final String USER_IMAGE_CACHE_DIR = "user_thumbs";
	private static OhaiApplication sInstance;
	private RequestTickle mRequestTickle;
	private SimpleImageLoader mImageUserFetcher;
	private RequestQueue mRequestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;

		if (!BuildConfig.DEBUG) {
			//AnalyticsManager.initializeAnalyticsTracker(getApplicationContext());
		}
	}

	public static synchronized OhaiApplication getInstance() {
		return sInstance;
	}

	public RequestTickle getRequestTickle() {
		if (mRequestTickle == null) {
			mRequestTickle = VolleyTickle.newRequestTickle(getApplicationContext());
		}

		return mRequestTickle;
	}


	public SimpleImageLoader getImageLoader() {
		if (null == mImageUserFetcher) {
			ImageCacheParams cacheParams = new ImageCacheParams(getApplicationContext(), USER_IMAGE_CACHE_DIR);
			cacheParams.setMemCacheSizePercent(0.5f);

			mImageUserFetcher = new SimpleImageLoader(getApplicationContext(), cacheParams);
			mImageUserFetcher.setFadeInImage(false);
		}

		return mImageUserFetcher;
	}

	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		VolleyLog.d("Adding request to queue: %s", req.getUrl());
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	@Override
	public void onLowMemory() {
		//Runtime.getRuntime().gc();
		super.onLowMemory();
	}

}
