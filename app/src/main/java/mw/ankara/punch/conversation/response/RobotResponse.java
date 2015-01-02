package mw.ankara.punch.conversation.response;

import org.json.JSONException;
import org.json.JSONObject;

import mw.ankara.punch.conversation.Conversation;

/**
 * @author MasaWong
 * @date 15/1/2.
 */
public abstract class RobotResponse {

    public static RobotResponse getResponse(JSONObject jsonObject) {
        try {
            int code = jsonObject.getInt("code");
            switch (code) {
                case 100000:
                    return new TextResponse(jsonObject);
                default:
                    return new TextResponse(null);
            }
        } catch (JSONException e) {
            return new TextResponse(null);
        }
    }

    public int code;

    public long time;

    public RobotResponse(int code) {
        this.code = code;
        this.time = System.currentTimeMillis();
    }

    public abstract Conversation[] getConversations();
}
