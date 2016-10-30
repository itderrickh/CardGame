package com.itderrickh.cardgame;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.itderrickh.cardgame.fragments.BiddingFragment;
import com.itderrickh.cardgame.fragments.FieldFragment;
import com.itderrickh.cardgame.fragments.TableFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView messages;
    private Button sendMessage;
    private MessageAdapter messageAdapter;
    private EditText messageText;
    public String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getIntent().getStringExtra("email");

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape);
            getSupportActionBar().hide();

            this.messages = (ListView) findViewById(R.id.messagesList);
            this.sendMessage = (Button) findViewById(R.id.sendMessage);
            this.messageAdapter = new MessageAdapter(this, R.layout.messages_row, new ArrayList<Message>());
            this.messageText = (EditText) findViewById(R.id.editText);

            this.messages.setAdapter(this.messageAdapter);
            this.sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = messageText.getText().toString();

                    messageAdapter.add(new Message(0, email, text));
                    messageText.setText("");
                }
            });
        } else {
            setContentView(R.layout.activity_main);
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BiddingFragment bidding = BiddingFragment.newInstance(10);
        fragmentTransaction.replace(R.id.playingArea, bidding, "BIDDING");
        fragmentTransaction.commit();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

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
}
