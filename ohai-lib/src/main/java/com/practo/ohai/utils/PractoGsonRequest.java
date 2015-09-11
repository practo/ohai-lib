package com.practo.ohai.utils;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.practo.ohai.BuildConfig;
import com.practo.ohai.helper.BaseRequestHelper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
/**
 * Volley adapter for JSON requests that will be parsed into Java objects by Gson.
 */
public class PractoGsonRequest<T> extends PractoBaseRequest<T> {

	/** Charset for request. */
	private static final String PROTOCOL_CHARSET = "utf-8";

	/** Content type for request. */
	private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);
	private final Gson gson = new Gson();
	private final Class<T> clazz;
	private Map<String, String> headers;
	private final Map<String, String> params;
	private final Listener<T> listener;
	private final String auth;
	private final ArrayMap<String, String> authArray;
	private final String mRequestBody;

	public PractoGsonRequest(String url, Class<T> clazz, String auth,
	                         Listener<T> listener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.clazz = clazz;
		this.params = null;
		this.listener = listener;
		this.auth = auth;
		this.authArray = null;
		this.mRequestBody = null;
		init();
	}

	public PractoGsonRequest(int type, String url, Class<T> clazz, String auth,
	                         Map<String, String> params,
	                         Listener<T> listener, ErrorListener errorListener) {
		super(type, url, errorListener);
		this.clazz = clazz;
		this.params = params;
		this.listener = listener;
		this.auth = auth;
		this.authArray = null;
		this.mRequestBody = null;
		init();
	}

	public PractoGsonRequest(int type, String url, Class<T> clazz, ArrayMap<String, String> authArray,
	                         Map<String, String> params,
	                         Listener<T> listener, ErrorListener errorListener) {
		super(type, url, errorListener);
		this.clazz = clazz;
		this.params = params;
		this.listener = listener;
		this.auth = null;
		this.authArray = authArray;
		this.mRequestBody = null;
		init();
	}


	public PractoGsonRequest(int type, String url, Class<T> clazz, ArrayMap<String, String> authArray,
	                          JSONObject jsonRequest, Listener<T> listener, ErrorListener errorListener) {
		super(type, url, errorListener);
		this.clazz = clazz;
		this.params = null;
		this.mRequestBody = (jsonRequest == null) ? null : jsonRequest.toString();
		this.listener = listener;
		this.auth = null;
		this.authArray = authArray;
		init();
	}

	private void init() {
		if (headers == null) {
			headers = new ArrayMap<String, String>();
		}
		if (!TextUtils.isEmpty(auth)) {
			headers.put(BaseRequestHelper.X_AUTH_TOKEN_HEADER, auth);
		} else if (authArray != null) {
			for (Map.Entry<String, String> authToken : authArray.entrySet()) {
				headers.put(authToken.getKey(), authToken.getValue());
			}
		}
		headers.put(BaseRequestHelper.X_DROID_VERSION_HEADER, BuildConfig.VERSION_NAME);
		headers.put(BaseRequestHelper.ACCEPT, "application/json");
		headers.put(BaseRequestHelper.API_VERSION, "2.0");
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params != null ? params : super.getParams();
	}

	@Override
	protected void deliverResponse(T response) {
		if (null != listener) {
			listener.onResponse(response);
		}
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(
					response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(
					gson.fromJson(json, clazz), Utils.parseIgnoreCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	public String getBodyContentType() {
		if (TextUtils.isEmpty(mRequestBody)) {
			return super.getBodyContentType();
		}
		return PROTOCOL_CONTENT_TYPE;
	}

	@Override
	public byte[] getBody() {
		try {
			if (TextUtils.isEmpty(mRequestBody)) {
				return super.getBody();
			}
			return mRequestBody.getBytes(PROTOCOL_CHARSET);
		} catch (UnsupportedEncodingException uee) {
			VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody,
					PROTOCOL_CHARSET);
			return null;
		} catch (AuthFailureError e) {
			return null;
		}
	}

}
