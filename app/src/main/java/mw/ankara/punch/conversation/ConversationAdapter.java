package mw.ankara.punch.conversation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;

import mw.ankara.punch.R;

/**
 * @author MasaWong
 * @date 15/1/2.
 */
public class ConversationAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<Conversation> mConversations;

    public ConversationAdapter(Context context) {
        mContext = context;
        mConversations = new ArrayList<Conversation>();
    }

    public void addConversation(Conversation conversation) {
        mConversations.add(conversation);
    }

    public void addConversations(Conversation[] conversations) {
        Collections.addAll(mConversations, conversations);
    }

    @Override
    public int getCount() {
        return mConversations.size();
    }

    @Override
    public Object getItem(int position) {
        return mConversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mConversations.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.view_conversation, null);
        }

        ConversationHolder holder;
        if (convertView.getTag() != null && convertView.getTag() instanceof ConversationHolder) {
            holder = (ConversationHolder) convertView.getTag();
        } else {
            holder = new ConversationHolder(convertView);
        }

        holder.refresh((Conversation) getItem(position));

        return convertView;
    }
}
