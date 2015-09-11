package com.practo.ohai.utils;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.error.VolleyError;
import com.practo.ohai.utils.Utils;

/**
 * Created by rachit on 17/03/15.
 */
public abstract class PractoBaseRequest<T> extends Request<T> {

	public PractoBaseRequest(int method, String url, ErrorListener listener) {
		super(method, url, listener);
		setShouldCache(false);
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
