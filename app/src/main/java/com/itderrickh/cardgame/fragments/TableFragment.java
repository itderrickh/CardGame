package com.itderrickh.cardgame.fragments;

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

import com.itderrickh.cardgame.helpers.Card;
import com.itderrickh.cardgame.activities.MainActivity;
import com.itderrickh.cardgame.R;
import com.itderrickh.cardgame.helpers.GameUser;

import java.io.Serializable;
import java.util.ArrayList;

public class TableFragment extends Fragment implements Serializable {
    //Playfield
    private Card[] playedCards;
    //Hand
    private ImageView[] handLocs;
    //Trump card
    private ImageView trumpCardImage;
    private Card trumpCard;

    private TextView[] usernameFields;

    private int lastClickedCard = -1;
    private int cardPlayed = 0;
    public int bid = 0;
    public String username;
    public ArrayList<GameUser> users;
    public ArrayList<Card> hand;

    public TableFragment() { }

    public static TableFragment newInstance(ArrayList<GameUser> gameUsers, ArrayList<Card> handCards, Card trumpCard) {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();

        fragment.users = gameUsers;
        fragment.hand = handCards;
        fragment.trumpCard = trumpCard;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        return view;
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

        for(int i = 0; i < usernameFields.length; i++) {
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

        for(int z = 0; z < handLocs.length; z++) {
            handLocs[z].setImageResource(hand.get(z).getResourceImage());
        }

        trumpCardImage = (ImageView) getView().findViewById(R.id.trumpCard);

        trumpCardImage.setImageResource(trumpCard.getResourceImage());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            this.playedCards = (Card[])savedInstanceState.getSerializable("playedCards");
            this.cardPlayed = savedInstanceState.getInt("cardPlayed");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the state of the game here, in the future labs we will actually use this
        outState.putSerializable("trumpCard", this.trumpCard);
        outState.putSerializable("hand", this.hand);
        outState.putSerializable("playedCards", this.playedCards);
        outState.putInt("cardPlayed", this.cardPlayed);
        outState.putString("username", this.username);
    }

    public void updateBid() {
        TextView bidArea = (TextView) getView().findViewById(R.id.bid1);
        bidArea.setText(bid + "");
    }

    public void setPlayedCards(ArrayList<Card> playedCards) {

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
        if(cardPlayed < 5 && this.hand.get(index) != null) {
            if(lastClickedCard == index) {
                cardView.setImageBitmap(null);
                this.playedCards[cardPlayed] = this.hand.get(index);
                this.hand.set(index, null);
                cardView.setBackgroundColor(Color.TRANSPARENT);
                cardPlayed++;

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FieldFragment makeField = FieldFragment.newInstance(playedCards);
                fragmentTransaction.replace(R.id.playingArea, makeField, "BIDDING");
                fragmentTransaction.commit();
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
