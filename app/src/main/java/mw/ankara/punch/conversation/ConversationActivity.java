package mw.ankara.punch.conversation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import mw.ankara.base.app.BluesActivity;
import mw.ankara.punch.R;

public class ConversationActivity extends BluesActivity {

    private ConversationManager mManager;

    private EditText mEditText;

    private ConversationManager.Callback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mEditText = (EditText) findViewById(R.id.conversation_et_content);

        mManager = new ConversationManager(this);
        mCallback = new ConversationManager.Callback() {
            @Override
            public void onCallback(Conversation[] conversations) {
                for (Conversation conversation : conversations) {
                    showToast(conversation.content);
                }
            }
        };
    }

    public void onSendClick(View view) {
        if (!TextUtils.isEmpty(mEditText.getText())) {
            mManager.talk(mEditText.getText().toString(), mCallback);
        }
    }
}
