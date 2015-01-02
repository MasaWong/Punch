package mw.ankara.punch.conversation;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URLEncoder;

import mw.ankara.base.network.WebManager;
import mw.ankara.punch.R;
import mw.ankara.punch.conversation.response.RobotResponse;

/**
 * 需要android.permission.READ_PHONE_STATE权限
 * @author MasaWong
 * @date 14/12/31.
 */
public class ConversationManager {

    public static final String URL = "http://www.tuling123.com/openapi/api";
    public static final String PREFERENCE_NAME = "conversation";
    public static final String PREFERENCE_USER = "user";

    private String mUrl;

    public ConversationManager(Context context) {
        mUrl = URL + "?key=" + getKey(context) + "&userid=" + getUserId(context);
    }

    private String getKey(Context context) {
        return context.getString(R.string.conversation_key);
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
                return "";
            } else {
                pref.edit().putString(PREFERENCE_USER, userId).apply();
                return userId;
            }
        }
    }

    public void talk(String content, final Callback callback) {
        String url = mUrl + "&info=" + URLEncoder.encode(content);
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        RobotResponse response = RobotResponse.getResponse(jsonObject);
                        callback.onCallback(response.getConversations());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onCallback(null);
                    }
                });
        WebManager.getInstance().sendRequest(request);
    }

    public static interface Callback {
        public void onCallback(Conversation[] conversations);
    }
}
