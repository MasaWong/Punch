package mw.ankara.punch.conversation.response;

import org.json.JSONObject;

import mw.ankara.punch.conversation.Conversation;

/**
 * @author MasaWong
 * @date 15/1/2.
 */
public class ErrorResponse extends TextResponse{

    public static final String ERROR_MESSAGE = "Ankara听不懂";

    public ErrorResponse(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public int getCode() {
        return -1;
    }

    @Override
    public Conversation[] getConversations() {
        Conversation conversation = new Conversation(Conversation.ROBOT, time, ERROR_MESSAGE);
        return new Conversation[]{conversation};
    }
}
