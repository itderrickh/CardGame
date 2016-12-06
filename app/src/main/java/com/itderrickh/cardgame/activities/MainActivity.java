package com.itderrickh.cardgame.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itderrickh.cardgame.R;
import com.itderrickh.cardgame.fragments.BiddingFragment;
import com.itderrickh.cardgame.fragments.TableFragment;
import com.itderrickh.cardgame.helpers.Message;
import com.itderrickh.cardgame.services.GameRunnerService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Intent serviceIntent;
    private ListView messages;
    private Button sendMessage;
    private MessageAdapter messageAdapter;
    private EditText messageText;
    public String email;
    public ArrayList<Message> messagesArray = new ArrayList<>();
    public BiddingFragment bidFrag;
    public TableFragment tableFrag;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("CARDGAME_SETTINGS", Context.MODE_PRIVATE);
        String token = preferences.getString("Auth_Token", "");

        email = getIntent().getStringExtra("email");
        final ProgressBar pgBar = (ProgressBar) findViewById(R.id.isJoined);
        this.sendMessage = (Button) findViewById(R.id.sendMessage);
        this.messages = (ListView) findViewById(R.id.messagesList);
        this.messageText = (EditText) findViewById(R.id.editText);

        if(savedInstanceState != null) {
            messagesArray = (ArrayList<Message>)savedInstanceState.getSerializable("messagesArray");
            bidFrag = (BiddingFragment)savedInstanceState.getSerializable("bidFrag");
            tableFrag = (TableFragment) savedInstanceState.getSerializable("tableFrag");
        }

        //Handle updates from the score service
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("data"));
                    int currentStatus = jsonObj.getInt("status");

                    if(currentStatus == 1 || currentStatus == 2) {
                        //Still loading up the game
                    } else if(currentStatus == 3) {
                        pgBar.setVisibility(View.GONE);

                        JSONArray bids = jsonObj.getJSONArray("bids");
                        JSONArray hand = jsonObj.getJSONArray("hand");
                        JSONArray users = jsonObj.getJSONArray("users");

                        insertBiddingFrag();
                        insertTable();
                    } else if(currentStatus == 4) {

                    } else if(currentStatus == 5) {

                    } else if(currentStatus == 6) {

                    }
                } catch (Exception ex) {
                    //Handle exception here
                    ex.printStackTrace();
                }
            }
        };

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape);
            getSupportActionBar().hide();

            this.messageAdapter = new MessageAdapter(this, R.layout.messages_row, messagesArray);
            this.messages.setAdapter(this.messageAdapter);
        } else {
            setContentView(R.layout.activity_main);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onResume() {
        super.onResume();

        serviceIntent = new Intent(getApplicationContext(), GameRunnerService.class);
        startService(serviceIntent);

        registerReceiver(receiver, new IntentFilter("com.itderrickh.cardgame.broadcast"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(serviceIntent);
        unregisterReceiver(receiver);
    }

    private void insertBiddingFrag() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(bidFrag == null) {
            bidFrag = BiddingFragment.newInstance(10);
        }

        fragmentTransaction.replace(R.id.playingArea, bidFrag, "BIDDING");
        fragmentTransaction.commit();
    }

    private void insertTable() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(tableFrag == null) {
            tableFrag = TableFragment.newInstance();
        }

        fragmentTransaction.replace(R.id.tableArea, tableFrag, "TABLE");
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("messagesArray", messagesArray);
        outState.putSerializable("bidFrag", bidFrag);
        outState.putSerializable("tableFrag", tableFrag);
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
