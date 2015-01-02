package mw.ankara.punch.conversation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import mw.ankara.base.app.BluesActivity;
import mw.ankara.punch.R;

public class ConversationActivity extends BluesActivity {

    private EditText mEtContent;

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
        ListView history = (ListView) findViewById(R.id.conversation_lv_history);
        mHistory = new ConversationAdapter(this);
        history.setAdapter(mHistory);
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
                    mHistory.addConversations(conversations);
                    mHistory.notifyDataSetChanged();
                }
            }
        };
    }

    public void onSendClick(View view) {
        if (!TextUtils.isEmpty(mEtContent.getText())) {
            String content = mEtContent.getText().toString();

            mManager.talk(content, mCallback);

            Conversation myVoice = new Conversation(Conversation.ME, content);
            mHistory.addConversation(myVoice);
            mHistory.notifyDataSetChanged();

            mEtContent.setText("");
        }
    }
}
