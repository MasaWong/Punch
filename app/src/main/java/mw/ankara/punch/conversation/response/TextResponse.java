package mw.ankara.punch.conversation.response;

import org.json.JSONException;
import org.json.JSONObject;

import mw.ankara.punch.conversation.Conversation;

/**
 * @author MasaWong
 * @date 15/1/2.
 */
public class TextResponse extends RobotResponse {

    public static final String ERROR_MESSAGE = "Ankara听不懂";

    public String text;

    public TextResponse(JSONObject jsonObject) {
        super(100000);
        try {
            text = jsonObject == null ? ERROR_MESSAGE : jsonObject.getString("text");
        } catch (JSONException e) {
            text = ERROR_MESSAGE;
        }
    }

    @Override
    public Conversation[] getConversations() {
        Conversation conversation = new Conversation(Conversation.ROBOT, time, text);
        return new Conversation[]{conversation};
    }
}
