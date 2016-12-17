package com.itderrickh.cardgame.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itderrickh.cardgame.helpers.Bid;
import com.itderrickh.cardgame.helpers.Card;
import com.itderrickh.cardgame.activities.MainActivity;
import com.itderrickh.cardgame.R;
import com.itderrickh.cardgame.helpers.GameUser;
import com.itderrickh.cardgame.helpers.Score;

import java.io.Serializable;
import java.util.ArrayList;

public class TableFragment extends Fragment implements Serializable {
    //Hand
    private ImageView[] handLocs;
    private ArrayList<Card> hand;

    //Trump card
    private ImageView trumpCardImage;
    private Card trumpCard;

    //Scoreboard
    private TextView[] bidFields;
    private TextView[] scoreFields;
    private TextView[] usernameFields;
    private ArrayList<GameUser> users;

    private int lastClickedCard = -1;
    private int cardPlayed = 0;
    public String username;
    private boolean turnOver = true;
    private OnCardPlayedHandler listener;

    public TableFragment() { }

    public static TableFragment newInstance(ArrayList<GameUser> gameUsers, ArrayList<Card> handCards, Card trumpCard) {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();

        args.putSerializable("trumpCard", trumpCard);
        args.putSerializable("users", gameUsers);
        args.putSerializable("hand", handCards);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            this.users = (ArrayList<GameUser>)getArguments().getSerializable("users");
            this.hand = (ArrayList<Card>)getArguments().getSerializable("hand");
            this.trumpCard = (Card)getArguments().getSerializable("trumpCard");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_table, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity parent = (MainActivity)getActivity();
        this.username = parent.email;

        usernameFields = new TextView[5];
        usernameFields[0] = (TextView) getView().findViewById(R.id.playerName1);
        usernameFields[1] = (TextView) getView().findViewById(R.id.playerName2);
        usernameFields[2] = (TextView) getView().findViewById(R.id.playerName3);
        usernameFields[3] = (TextView) getView().findViewById(R.id.playerName4);
        usernameFields[4] = (TextView) getView().findViewById(R.id.playerName5);

        bidFields = new TextView[5];
        bidFields[0] = (TextView) getView().findViewById(R.id.bid1);
        bidFields[1] = (TextView) getView().findViewById(R.id.bid2);
        bidFields[2] = (TextView) getView().findViewById(R.id.bid3);
        bidFields[3] = (TextView) getView().findViewById(R.id.bid4);
        bidFields[4] = (TextView) getView().findViewById(R.id.bid5);

        scoreFields = new TextView[5];
        scoreFields[0] = (TextView) getView().findViewById(R.id.total1);
        scoreFields[1] = (TextView) getView().findViewById(R.id.total2);
        scoreFields[2] = (TextView) getView().findViewById(R.id.total3);
        scoreFields[3] = (TextView) getView().findViewById(R.id.total4);
        scoreFields[4] = (TextView) getView().findViewById(R.id.total5);

        for(int i = 0; i < users.size(); i++) {
            String email = users.get(i).getEmail();
            usernameFields[i].setText(email.substring(0, email.indexOf("@")));
        }


        handLocs = new ImageView[10];
        handLocs[0] = (ImageView) getView().findViewById(R.id.card1);
        handLocs[1] = (ImageView) getView().findViewById(R.id.card2);
        handLocs[2] = (ImageView) getView().findViewById(R.id.card3);
        handLocs[3] = (ImageView) getView().findViewById(R.id.card4);
        handLocs[4] = (ImageView) getView().findViewById(R.id.card5);
        handLocs[5] = (ImageView) getView().findViewById(R.id.card6);
        handLocs[6] = (ImageView) getView().findViewById(R.id.card7);
        handLocs[7] = (ImageView) getView().findViewById(R.id.card8);
        handLocs[8] = (ImageView) getView().findViewById(R.id.card9);
        handLocs[9] = (ImageView) getView().findViewById(R.id.card10);

        for(int z = 0; z < hand.size(); z++) {
            handLocs[z].setImageResource(hand.get(z).getResourceImage());
        }

        for(int h = hand.size(); h < handLocs.length; h++) {
            handLocs[h].setBackgroundColor(Color.TRANSPARENT);
            hand.add(null);
        }

        trumpCardImage = (ImageView) getView().findViewById(R.id.trumpCard);

        trumpCardImage.setImageResource(trumpCard.getResourceImage());
    }

    public void updateScoreBoard(ArrayList<Bid> bids, ArrayList<Score> scores) {
        for(int y = 0; y < users.size(); y++) {
            GameUser u = users.get(y);
            String email = u.getEmail();

            if(email.contains("@")) {
                usernameFields[y].setText(email.substring(0, email.indexOf("@")));
            } else {
                usernameFields[y].setText(email);
            }

            if(bids.size() == 0) {
                for(int x = 0; x < 5; x++) {
                    bidFields[y].setText(0 + "");
                }
            }

            for(int x = 0; x < bids.size(); x++) {
                if(u.getUserid() == bids.get(x).getUserid()) {
                    bidFields[y].setText(bids.get(x).getValue() + "");
                }
            }

            for(int x = 0; x < scores.size(); x++) {
                if(u.getUserid() == scores.get(x).getUserid()) {
                    scoreFields[y].setText(scores.get(x).getScore() + "");
                }
            }
        }
    }

    public void resetTurn() {
        turnOver = false;
    }

    public void setupClickEvents() {
        for(int i = 0; i < this.handLocs.length; i++) {
            //Make a variable we can reference in the inner class
            final int loc = i;
            View.OnClickListener click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardClick(handLocs[loc], loc);
                }
            };

            this.handLocs[i].setOnClickListener(click);
        }
    }

    private void cardClick(ImageView cardView, int index) {
        if(!turnOver && cardPlayed < 5 && this.hand.get(index) != null) {
            if(lastClickedCard == index) {
                cardView.setImageBitmap(null);
                Card c = this.hand.get(index);
                this.hand.set(index, null);
                cardView.setBackgroundColor(Color.TRANSPARENT);
                cardPlayed++;
                turnOver = true;

                listener.playedCard(c);
            } else {
                cardView.setBackgroundColor(Color.BLUE);
                lastClickedCard = index;
                for(int i = 0; i < this.handLocs.length; i++) {
                    if(i != index) {
                        this.handLocs[i].setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnCardPlayedHandler) {
            listener = (OnCardPlayedHandler) context;
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if(context instanceof OnCardPlayedHandler) {
            listener = (OnCardPlayedHandler) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnCardPlayedHandler {
        void playedCard(Card played);
    }
}
