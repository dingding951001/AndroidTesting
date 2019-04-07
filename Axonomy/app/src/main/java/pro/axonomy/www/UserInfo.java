package pro.axonomy.www;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

public class UserInfo {

    static final String AUTHORIZATION = "authorization";
    static final String LANGUAGE = "language";
    static final String TRANSLATION = "translation";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setToken(Context ctx, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(AUTHORIZATION, token);
        editor.commit();
        Log.i("UserInfo", "Store user info with token: " + token);
    }

    public static void setLanguage(Context ctx, String lang) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(LANGUAGE, lang);
        editor.commit();
        Log.i("UserInfo", "Store user info with language: " + lang);
    }

    public static void setTranslation(Context ctx, String translation) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(TRANSLATION, translation);
        editor.commit();
        Log.i("UserInfo", "Store user info with translation: " + translation);
    }

    public static String getAuthorization(Context ctx) {
        return getSharedPreferences(ctx).getString(AUTHORIZATION, "");
    }

    public static String getLanguage(Context ctx) {
        return getSharedPreferences(ctx).getString(LANGUAGE, Locale.getDefault().getLanguage());
    }

    public static String getTranslation(Context ctx) {
        return getSharedPreferences(ctx).getString(TRANSLATION, "");
    }
}
