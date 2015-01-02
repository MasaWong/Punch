package mw.ankara.punch.conversation.response;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

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
            url = jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCode() {
        return 200000;
    }

    @Override
    public Conversation[] getConversations() {
        String content = text + "\n" + url;

        int urlStart = text.length() + 1;
        int urlEnd = content.length();
        SpannableString spannableContent = new SpannableString(content);
        spannableContent.setSpan(new URLSpan(url), urlStart, urlEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableContent.setSpan(new UnderlineSpan(), urlStart, urlEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Conversation conversation = new Conversation(Conversation.ROBOT, time, spannableContent);
        return new Conversation[]{conversation};
    }
}
