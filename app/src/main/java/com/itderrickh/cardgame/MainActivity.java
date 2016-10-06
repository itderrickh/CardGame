package com.itderrickh.cardgame;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itderrickh.cardgame.fragments.TableFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TableFragment.OnFragmentInteractionListener {
    private ListView messages;
    private Button sendMessage;
    private MessageAdapter messageAdapter;
    private TextView messageText;
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape);
            getSupportActionBar().hide();

            this.messages = (ListView) findViewById(R.id.messagesList);
            this.sendMessage = (Button) findViewById(R.id.sendMessage);
            this.messageAdapter = new MessageAdapter(this, R.layout.messages_row, new ArrayList<Message>());
            this.messageText = (TextView) findViewById(R.id.editText);

            this.messages.setAdapter(this.messageAdapter);
            this.sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = (String)messageText.getText();
                    messageAdapter.add(new Message(0, "heined50", text));
                }
            });
        } else {
            setContentView(R.layout.activity_main);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public class MessageAdapter extends ArrayAdapter<Message> {
        public MessageAdapter(Context context, int resource) {
            super(context, resource);
        }

        public MessageAdapter(Context context, int resource, ArrayList<Message> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Message message = getItem(position);
            TextView messageUser = (TextView) findViewById(R.id.messageUser);
            TextView messageText = (TextView) findViewById(R.id.messageText);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.messages_row, parent, false);
            }

            messageUser.setText(message.getUsername());
            messageText.setText(message.getMessage());
            return super.getView(position, convertView, parent);
        }
    }
}
