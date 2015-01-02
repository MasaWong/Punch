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
                case 200000:
                    return new UrlResponse(jsonObject);
                default:
                    return new ErrorResponse(jsonObject);
            }
        } catch (JSONException e) {
            return new ErrorResponse(jsonObject);
        }
    }

    public int code;

    public long time;

    public RobotResponse() {
        this.code = getCode();
        this.time = System.currentTimeMillis();
    }

    public abstract int getCode();
    public abstract Conversation[] getConversations();
}
