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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.itderrickh.cardgame.R;
import com.itderrickh.cardgame.fragments.BiddingFragment;
import com.itderrickh.cardgame.fragments.TableFragment;
import com.itderrickh.cardgame.helpers.Message;
import com.itderrickh.cardgame.helpers.VolleyCallback;
import com.itderrickh.cardgame.services.GameReadyService;
import com.itderrickh.cardgame.services.GameService;

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

        GameService.getInstance().joinGame(getApplicationContext(), token, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result.getBoolean("success")) {
                        //Handle updates from the score service
                        receiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                try {
                                    boolean data = intent.getBooleanExtra("data", false);

                                    if(data) {
                                        pgBar.setVisibility(View.GONE);

                                        sendMessage.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String text = messageText.getText().toString();

                                                messageAdapter.add(new Message(0, email, text));
                                                messageText.setText("");
                                            }
                                        });

                                        insertTable();
                                        insertBiddingFrag();
                                    }
                                } catch (Exception ex) {
                                    //Handle exception here
                                    ex.printStackTrace();
                                }
                            }
                        };

                        serviceIntent = new Intent(getApplicationContext(), GameReadyService.class);
                        startService(serviceIntent);

                        registerReceiver(receiver, new IntentFilter("com.itderrickh.broadcast"));
                    } else {
                        Toast.makeText(MainActivity.this, "Game full", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(login);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError string) {
                string.printStackTrace();
            }
        });

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

        serviceIntent = new Intent(getApplicationContext(), GameReadyService.class);
        startService(serviceIntent);

        registerReceiver(receiver, new IntentFilter("com.itderrickh.broadcast"));
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
