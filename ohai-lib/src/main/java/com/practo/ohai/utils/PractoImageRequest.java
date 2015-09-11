package com.practo.ohai.utils;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.error.VolleyError;
import com.android.volley.request.ImageRequest;
import com.practo.ohai.utils.Utils;

public class PractoImageRequest extends ImageRequest {

	/**
	 * {@inheritDoc}
	 */
	public PractoImageRequest(String url, Resources resources, ContentResolver contentResolver,
	                          Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, ImageView.ScaleType
									  scaleType,
	                          Bitmap.Config decodeConfig, ErrorListener errorListener) {
		super(url, resources, contentResolver, listener, maxWidth, maxHeight, scaleType, decodeConfig,
				errorListener);
	}

	@Override
	public void deliverError(VolleyError error) {
		super.deliverError(error);
		try {
			Utils.sendFailureLog(error, getUrl(), getMethod(), getParams());
		} catch (Exception e) {
			Utils.sendFailureLog(error, getUrl(), getMethod(), null);
		}
	}
}
