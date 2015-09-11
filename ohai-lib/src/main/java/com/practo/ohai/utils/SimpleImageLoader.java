package com.practo.ohai.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.cache.DiskLruBasedCache;
import com.android.volley.error.VolleyError;

public class SimpleImageLoader extends com.android.volley.cache.SimpleImageLoader {

	public SimpleImageLoader(Context context, DiskLruBasedCache.ImageCacheParams imageCacheParams) {
		super(context, imageCacheParams);
	}

    @Override
	protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight, ImageView.ScaleType
            scaleType, final String cacheKey) {
		return new PractoImageRequest(requestUrl, getResources(), getContentResolver(),
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						onGetImageSuccess(cacheKey, response);
					}
				}, maxWidth, maxHeight, scaleType,
				Bitmap.Config.RGB_565, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onGetImageError(cacheKey, error);
			}
		});
	}
}
