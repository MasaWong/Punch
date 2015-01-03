package mw.ankara.punch.conversation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import mw.ankara.base.app.BluesActivity;
import mw.ankara.base.database.SQLitable;
import mw.ankara.punch.R;
import mw.ankara.punch.database.PunchDb;

public class ConversationActivity extends BluesActivity {

    private EditText mEtContent;

    private ListView mLvHistory;
    private ConversationAdapter mHistory;

    private ConversationManager mManager;
    private ConversationManager.Callback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mEtContent = (EditText) findViewById(R.id.conversation_et_content);

        initHistory();
        initConversation();
    }

    private void initHistory() {
        mLvHistory = (ListView) findViewById(R.id.conversation_lv_history);
        mHistory = new ConversationAdapter(this);

        ArrayList<SQLitable> conversations = PunchDb.getInstance().query(Conversation.class);
        for (SQLitable conversation : conversations) {
            mHistory.addConversation((Conversation) conversation);
        }

        mLvHistory.setAdapter(mHistory);
    }

    private void initConversation() {
        mManager = new ConversationManager(this);
        mCallback = new ConversationManager.Callback() {
            @Override
            public void onCallback(Conversation[] conversations) {
                if (conversations == null) {
                    // TODO: do sth.
                    showToast("");
                } else {
                    mHistory.addAndSaveConversations(conversations);
                    mHistory.notifyDataSetChanged();
                    mLvHistory.smoothScrollToPosition(Integer.MAX_VALUE);
                }
            }
        };
    }

    public void onSendClick(View view) {
        if (!TextUtils.isEmpty(mEtContent.getText())) {
            String content = mEtContent.getText().toString();

            mManager.talk(content, mCallback);

            Conversation myVoice = new Conversation(Conversation.ME, content);
            mHistory.addAndSaveConversation(myVoice);
            mHistory.notifyDataSetChanged();
            mLvHistory.smoothScrollToPosition(Integer.MAX_VALUE);

            mEtContent.setText("");
        }
    }
}
