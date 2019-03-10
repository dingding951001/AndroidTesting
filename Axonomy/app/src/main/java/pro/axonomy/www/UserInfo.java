package pro.axonomy.www;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class UserInfo {

    static final String AUTHORIZATION = "authorization";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setToken(Context ctx, String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(AUTHORIZATION, token);
        editor.commit();
        Log.i("UserInfo", "Store user info with token: " + token);
    }

    public static String getAuthorization(Context ctx)
    {
        return getSharedPreferences(ctx).getString(AUTHORIZATION, "");
    }
}
