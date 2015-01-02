package mw.ankara.punch.conversation;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import mw.ankara.punch.R;

/**
 * @author MasaWong
 * @date 15/1/2.
 */
public class ConversationHolder {

    private TextView mTvRobot;

    private TextView mTvMe;

    public ConversationHolder(View view) {
        mTvRobot = (TextView) view.findViewById(R.id.conversation_tv_robot);
        mTvMe = (TextView) view.findViewById(R.id.conversation_tv_me);
    }

    public void refresh(Conversation conversation) {
        if (conversation.role == Conversation.ROBOT) {
            mTvRobot.setVisibility(View.VISIBLE);
            mTvMe.setVisibility(View.GONE);

            mTvRobot.setText(conversation.content);
            mTvRobot.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mTvMe.setVisibility(View.VISIBLE);
            mTvRobot.setVisibility(View.GONE);

            mTvMe.setText(conversation.content);
            mTvMe.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
