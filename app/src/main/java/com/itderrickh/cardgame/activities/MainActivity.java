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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.itderrickh.cardgame.R;
import com.itderrickh.cardgame.fragments.BiddingFragment;
import com.itderrickh.cardgame.fragments.FieldFragment;
import com.itderrickh.cardgame.fragments.TableFragment;
import com.itderrickh.cardgame.helpers.Bid;
import com.itderrickh.cardgame.helpers.Card;
import com.itderrickh.cardgame.helpers.GameUser;
import com.itderrickh.cardgame.helpers.Message;
import com.itderrickh.cardgame.helpers.Score;
import com.itderrickh.cardgame.helpers.VolleyCallback;
import com.itderrickh.cardgame.services.GameRunnerService;
import com.itderrickh.cardgame.services.GameService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BiddingFragment.OnBidInteractionManager, TableFragment.OnCardPlayedHandler {
    private Intent serviceIntent;
    private ListView messages;
    private Button sendMessage;
    private MessageAdapter messageAdapter;
    private EditText messageText;
    private TextView loadingText;
    private RelativeLayout loadingArea;

    public String email;
    public ArrayList<Message> messagesArray = new ArrayList<>();
    public BiddingFragment bidFrag;
    public TableFragment tableFrag;
    public FieldFragment fieldFrag;
    BroadcastReceiver receiver;
    private boolean receivedHandAndTable = false;
    private boolean holdBidsAndHand = false;

    private String token;

    private ArrayList<GameUser> gameUsers;
    private ArrayList<Card> handCards;
    private ArrayList<Bid> gameBids;
    private ArrayList<Card> fieldCards;
    private ArrayList<Score> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("CARDGAME_SETTINGS", Context.MODE_PRIVATE);
        token = preferences.getString("Auth_Token", "");

        email = getIntent().getStringExtra("email");

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape);
            getSupportActionBar().hide();

            this.messageAdapter = new MessageAdapter(this, R.layout.messages_row, messagesArray);
            this.messages.setAdapter(this.messageAdapter);
        } else {
            setContentView(R.layout.activity_main);
        }

        loadingArea = (RelativeLayout) findViewById(R.id.loadingView);
        loadingText = (TextView) findViewById(R.id.loadingText);

        gameUsers = new ArrayList<GameUser>();
        handCards = new ArrayList<Card>();
        gameBids = new ArrayList<Bid>();
        fieldCards = new ArrayList<Card>();
        scores = new ArrayList<Score>();

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
                    JSONObject jsonObj = new JSONObject(intent.getStringExtra("data"));
                    int currentStatus = jsonObj.getInt("status");

                    if (currentStatus == 1) {
                        loadingText.setText("Waiting for users");
                    } else if(currentStatus == 2) {
                        loadingText.setText("Setting up game");
                    } else if(currentStatus == 3) {
                        loadingArea.setVisibility(View.GONE);

                        JSONArray bids = jsonObj.getJSONArray("bids");
                        JSONArray hand = jsonObj.getJSONArray("hand");
                        JSONArray users = jsonObj.getJSONArray("users");
                        JSONObject trumpCard = jsonObj.getJSONObject("trump");
                        JSONArray scoresJson = jsonObj.getJSONArray("scores");

                        //Fill up the users from the JSON
                        if(!receivedHandAndTable) {
                            GameUser row;
                            for(int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                row = new GameUser(user.getString("email"), user.getInt("id"), user.getInt("gameid"), user.getInt("userid"));
                                gameUsers.add(row);
                            }

                            Card cardRow;
                            for(int j = 0; j < hand.length(); j++) {
                                JSONObject card = hand.getJSONObject(j);
                                cardRow = new Card(card.getString("suit"), card.getString("value"));
                                cardRow.setId(card.getInt("handcardid"));

                                handCards.add(cardRow);
                            }

                            Card trump = new Card(trumpCard.getString("suit"), trumpCard.getString("value"));

                            insertTable(gameUsers, handCards, trump);
                            insertBiddingFrag();

                            receivedHandAndTable = true;
                        }

                        if(bids.length() > 0) {
                            Bid bidRow;
                            for(int c = 0; c < bids.length(); c++) {
                                JSONObject bid = bids.getJSONObject(c);
                                bidRow = new Bid(bid.getInt("userid"), bid.getInt("value"));

                                gameBids.add(bidRow);
                            }

                            Score scoreRow;
                            for(int d = 0; d < scoresJson.length(); d++) {
                                JSONObject score = scoresJson.getJSONObject(d);
                                scoreRow = new Score(score.getInt("userid"), score.getInt("score"));

                                scores.add(scoreRow);
                            }

                            updateScoreBoard(gameBids, scores);
                        }
                    } else if(currentStatus == 4) {
                        loadingArea.setVisibility(View.GONE);

                        JSONArray field = jsonObj.getJSONArray("field");
                        JSONArray bids = jsonObj.getJSONArray("bids");
                        JSONArray hand = jsonObj.getJSONArray("hand");
                        JSONArray users = jsonObj.getJSONArray("users");
                        JSONObject trumpCard = jsonObj.getJSONObject("trump");
                        JSONArray scoresJson = jsonObj.getJSONArray("scores");

                        //Fill up the users from the JSON
                        if(!receivedHandAndTable) {
                            GameUser row;
                            for(int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                row = new GameUser(user.getString("email"), user.getInt("id"), user.getInt("gameid"), user.getInt("userid"));
                                gameUsers.add(row);
                            }

                            Card cardRow;
                            for(int j = 0; j < hand.length(); j++) {
                                JSONObject card = hand.getJSONObject(j);
                                cardRow = new Card(card.getString("suit"), card.getString("value"));
                                cardRow.setId(card.getInt("handcardid"));

                                handCards.add(cardRow);
                            }

                            Card trump = new Card(trumpCard.getString("suit"), trumpCard.getString("value"));

                            insertTable(gameUsers, handCards, trump);
                            insertField(fieldCards.toArray(new Card[5]));
                            receivedHandAndTable = true;
                        }

                        if(bids.length() > 0) {
                            Bid bidRow;
                            for(int c = 0; c < bids.length(); c++) {
                                JSONObject bid = bids.getJSONObject(c);
                                bidRow = new Bid(bid.getInt("userid"), bid.getInt("value"));

                                gameBids.add(bidRow);
                            }

                            Score scoreRow;
                            for(int d = 0; d < scoresJson.length(); d++) {
                                JSONObject score = scoresJson.getJSONObject(d);
                                scoreRow = new Score(score.getInt("userid"), score.getInt("score"));

                                scores.add(scoreRow);
                            }

                            updateScoreBoard(gameBids, scores);
                        }

                        if(!holdBidsAndHand) {
                            if(bids.length() > 0) {
                                gameBids.clear();
                                Bid bidRow;
                                for(int c = 0; c < bids.length(); c++) {
                                    JSONObject bid = bids.getJSONObject(c);
                                    bidRow = new Bid(bid.getInt("userid"), bid.getInt("value"));

                                    gameBids.add(bidRow);
                                }

                                updateScoreBoard(gameBids, scores);
                            }

                            holdBidsAndHand = true;
                            tableFrag.setupClickEvents();
                        }

                        if(jsonObj.getBoolean("yourturn")) {
                            tableFrag.resetTurn();
                        }

                        if(field.length() > 0 && receivedHandAndTable) {
                            fieldCards.clear();
                            Card c;
                            for(int q = 0; q < field.length(); q++) {
                                JSONObject row = field.getJSONObject(q);
                                c = new Card(row.getString("suit"), row.getString("value"));
                                c.setId(row.getInt("userid"));
                                fieldCards.add(c);
                            }

                            fieldFrag.setPlayField(fieldCards);
                        }
                    } else if(currentStatus == 5) {
                        //Wait until this step is over to see scores
                        System.out.println(currentStatus);
                    } else if(currentStatus == 6) {
                        final boolean winner = jsonObj.getBoolean("isWinner");

                        //Go to the end game page
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getApplicationContext(), GameOverActivity.class);
                                i.putExtra("isWinner", winner);
                                startActivity(i);
                            }
                        });
                    }
                } catch (Exception ex) {
                    //Handle exception here
                    ex.printStackTrace();
                }
            }
        };

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

    private void updateScoreBoard(ArrayList<Bid> bids, ArrayList<Score> scores) {
        tableFrag.updateScoreBoard(bids, scores);
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

    private void insertTable(ArrayList<GameUser> users, ArrayList<Card> cards, Card trumpCard) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(tableFrag == null) {
            tableFrag = TableFragment.newInstance(users, cards, trumpCard);
        }

        fragmentTransaction.replace(R.id.tableArea, tableFrag, "TABLE");
        fragmentTransaction.commit();
    }

    private void insertField(Card[] cards) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(fieldFrag == null) {
            fieldFrag = FieldFragment.newInstance(cards);
        }

        fragmentTransaction.replace(R.id.playingArea, fieldFrag, "TABLE");
        fragmentTransaction.commit();
    }

    @Override
    public void onBidInteraction(int bid) {
        //Do something with the bid
        GameService.getInstance().placeBid(getApplicationContext(), token, bid, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                insertField(new Card[5]);
            }

            @Override
            public void onError(VolleyError string) {
                string.printStackTrace();
            }
        });
    }

    @Override
    public void playedCard(Card played) {
        GameService.getInstance().playCard(getApplicationContext(), token, played.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                //Do nothing for now
            }

            @Override
            public void onError(VolleyError string) {
                string.printStackTrace();
            }
        });
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
