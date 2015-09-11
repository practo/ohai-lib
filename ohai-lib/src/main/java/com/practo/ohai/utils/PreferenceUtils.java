package com.practo.ohai.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

	public static final String API_TOKEN = "api_token";

	public static final String GCM_REGISTRATION_TOKEN = "gcm_registration_token";

	public static final String IS_REGISTERED_TO_SERVER = "is_registered_to_server";

    public static Boolean getBooleanPrefs(Context ctx, String key) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(key, false);
	}

	public static String getStringPrefs(Context ctx, String key) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, "");
	}

	public static String getStringPrefs(Context ctx, String key, String defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, defaultValue);
	}

	public static int getIntegerPrefs(Context ctx, String key) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(key, 0);
	}

	public static int getIntegerPrefs(Context ctx, String key, int defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(key, defaultValue);
	}

	public static long getLongPrefs(Context ctx, String key) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getLong(key, 0);
	}

	public static long getLongPrefs(Context ctx, String key, int defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getLong(key, defaultValue);
	}

    public static float getFLoatPrefs(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getFloat(key, 0);
    }


    public static void set(final Context context, final String key, final Object value) {
		SharedPreferences.Editor sharedPreferenceEditor = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		if (value instanceof String) {
			sharedPreferenceEditor.putString(key, (String) value);
		} else if (value instanceof Long) {
			sharedPreferenceEditor.putLong(key, (Long) value);
		} else if (value instanceof Integer) {
			sharedPreferenceEditor.putInt(key, (Integer) value);
		} else if (value instanceof Boolean) {
			sharedPreferenceEditor.putBoolean(key, (Boolean) value);
		} else if (value instanceof Float) {
            sharedPreferenceEditor.putFloat(key, (Float) value);
        }
		sharedPreferenceEditor.apply();
	}

	public static void clear(final Context context) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
	}

	public static String getApiToken(Context context) {
		return getStringPrefs(context, PreferenceUtils.API_TOKEN);
	}

	public static String getGcmRegistrationToken(Context context) {
		return getStringPrefs(context, PreferenceUtils.GCM_REGISTRATION_TOKEN);
	}
}
