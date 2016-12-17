package com.itderrickh.cardgame.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itderrickh.cardgame.R;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, int resource, ArrayList<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.messages_row, parent, false);
        }

        TextView messageUser = (TextView) convertView.findViewById(R.id.messageUser);
        TextView messageText = (TextView) convertView.findViewById(R.id.messageText);

        messageUser.setText(message.getUsername());
        messageText.setText(message.getMessage());
        return convertView;
    }
}