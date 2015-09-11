package com.practo.ohai.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.VolleyTickle;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {

    public static final Pattern EMAIL_ADDRESS = Pattern
            .compile(
                    "^[a-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+(\\.[a-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+)" +
                            "*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2,})$");

    public static boolean isEmptyList(ArrayList arrayList) {
        return (arrayList == null || arrayList.size() <= 0);
    }

    public static boolean isNetConnected(Context context) {
        if (context != null) {
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return !(networkInfo == null || !networkInfo.isConnectedOrConnecting());
        } else {
            return false;
        }
    }

    public static boolean isEmptyString(String value) {
        return !(value != null && !value.trim().isEmpty());
    }

    public static boolean isEmptyArrayString(String arrayString) {
        return !(arrayString != null && !arrayString.trim().isEmpty() && !arrayString.equals("[]") && !arrayString
                .equals("[ ]"));
    }

    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isEmptyMap(HashMap map) {
        return (map == null || map.size() <= 0);
    }

    public static String getFormattedTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            StringBuilder ret = new StringBuilder();
            if (str.contains("-")) {
                String[] words = str.trim().split("-");
                for (int i = 0; i < words.length; i++) {
                    if (!TextUtils.isEmpty(words[i])) {
                        ret.append(Character.toUpperCase(words[i].charAt(0)));
                        ret.append(words[i].substring(1));
                        if (i < words.length - 1) {
                            ret.append(' ');
                        }
                    }
                }
            } else {
                ret.append(Character.toUpperCase(str.charAt(0)));
                ret.append(str.substring(1));
            }
            return ret.toString();
        }
        return str;
    }

    /**
     * source http://stackoverflow.com/a/16108347
     * @param original Original String
     * @return
     */
    public static ArrayList<String> splitString (String original)
    {
        ArrayList<String> splitted = new ArrayList<String>();
        int skipCommas = 0;
        String s = "";
        for ( char c : original.toCharArray() )
        {
            if ( c==',' && skipCommas == 0)
            {
                splitted.add (s);
                s="";
            } else {
                if ( c=='(' )
                    skipCommas++;
                if ( c==')' )
                    skipCommas--;
                s+= c;
            }
        }
        splitted.add(s);
        return splitted;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void sendFailureLog(VolleyError error, String url, int method, Map<String, String> params) {
        try {
            if (null != error && null != error.networkResponse) {
                ArrayMap<String, String> map = new ArrayMap<String, String>();
                map.put("Request_url", url);
                if (null != params) {
                    Utils.maskRequestParamKeys(params);
                    map.put("Request_params", getEncodedUrlParams(params));
                    if (null != error.networkResponse.data) {
                        map.put("Response", VolleyTickle.parseResponse(error.networkResponse));
                    }
                }

                String message = getMethodName(method);
                Uri uri = Uri.parse(url);
                if (null != uri && uri.getPathSegments().size() >= 3) {
                    message += " " + uri.getPathSegments().get(2);
                }

                message += " : " + error.networkResponse.statusCode;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String[] MASK_PARAM_KEYS = new String[]{"password"};

    public static void maskRequestParamKeys(Map<String, String> params) {
        for (String maskKey : MASK_PARAM_KEYS) {
            if (params.containsKey(maskKey)) {
                params.put(maskKey, "******");
            }
        }
    }

    public static String getEncodedUrlParams(Map<String, String> params) {

        StringBuilder encodedParams = new StringBuilder();
        String paramsEncoding = "UTF-8";
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (null == entry.getValue()) {
                    continue;
                }
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString();
        } catch (Exception uee) {
            return "Encoding not supported: " + uee.getMessage();
        }
    }

    public static String getMethodName(int type) {
        String name = "";
        switch (type) {
            case Request.Method.DEPRECATED_GET_OR_POST:
                name = "";
                break;
            case Request.Method.GET:
                name = "GET";
                break;
            case Request.Method.DELETE:
                name = "DELETE";
                break;
            case Request.Method.POST:
                name = "POST";
                break;
            case Request.Method.PATCH:
                name = "PATCH";
                break;
            case Request.Method.PUT:
                name = "PUT";
                break;
        }
        return name;
    }


    public static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {

        long now = System.currentTimeMillis();
        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        final long cacheHitButRefreshed =
                now + 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
        final long cacheExpired = now + 30 * 60 * 1000; // in 24 hours this cache entry expires completely
        final long softExpire = cacheHitButRefreshed;
        final long ttl = cacheExpired;

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }
}
