package mw.ankara.punch.conversation.response;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import mw.ankara.punch.conversation.Conversation;

/**
 * @author MasaWong
 * @date 15/1/2.
 */
public class UrlResponse extends TextResponse {

    public String url;

    public UrlResponse(JSONObject jsonObject) {
        super(jsonObject);

        try {
            url = jsonObject == null ? "" : jsonObject.getString("url");
        } catch (JSONException e) {
            url = "";
        }
    }

    @Override
    public int getCode() {
        return 200000;
    }

    @Override
    public Conversation[] getConversations() {
        String content = TextUtils.isEmpty(url) ? text : text + "\n" + url;
        Conversation conversation = new Conversation(Conversation.ROBOT, time, content);
        return new Conversation[]{conversation};
    }
}
