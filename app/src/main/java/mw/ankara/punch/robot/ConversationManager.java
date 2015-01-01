package mw.ankara.punch.robot;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @author MasaWong
 * @date 14/12/31.
 */
public class ConversationManager {

    public static final String URL = "http://www.tuling123.com/openapi/api?key=KEY";
    public static final String PREFERENCE_NAME = "conversation";
    public static final String PREFERENCE_USER = "user";

    private String mUserId;

    public ConversationManager(Context context) {
        mUserId = getUserId(context);
    }

    private String getUserId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, 0);

        if (pref.contains(PREFERENCE_USER)) {
            return pref.getString(PREFERENCE_USER, "");
        } else {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String userId = manager.getDeviceId();
            if (TextUtils.isEmpty(userId)) {
                return  "";
            } else {
                pref.edit().putString(PREFERENCE_USER, userId).apply();
                return userId;
            }
        }
    }

    public void talk(String content, Callback callback) {

    }

    public static interface Callback {
        public void onCallback(Conversation conversation);
    }
}
