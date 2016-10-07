package com.itderrickh.cardgame.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itderrickh.cardgame.Card;
import com.itderrickh.cardgame.Deck;
import com.itderrickh.cardgame.R;

public class TableFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Deck deck;
    private ImageView[] handLocs;
    private ImageView trumpCardImage;
    private Card[] hand;
    private Card trumpCard;
    private ImageView[] playField;
    private int lastClickedCard = -1;
    private int cardPlayed = 0;

    public TableFragment() { }

    public static TableFragment newInstance() {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();
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

        trumpCardImage = (ImageView) getView().findViewById(R.id.trumpCard);
        this.playField = new ImageView[5];
        this.playField[0] = (ImageView) getView().findViewById(R.id.playCard1);
        this.playField[1] = (ImageView) getView().findViewById(R.id.playCard2);
        this.playField[2] = (ImageView) getView().findViewById(R.id.playCard3);
        this.playField[3] = (ImageView) getView().findViewById(R.id.playCard4);
        this.playField[4] = (ImageView) getView().findViewById(R.id.playCard5);

        trumpCardImage.setImageResource(trumpCard.getResourceImage());
        for(int c = 0; c < this.handLocs.length; c++) {
            this.handLocs[c].setImageResource(this.hand[c].getResourceImage());
        }

        setupClickEvents();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(false) {
        //This work below will restore the state of the game
        //NOTE: Will have to save a few more state things to make sure this works
        //if(savedInstanceState != null) {
            this.deck = (Deck)savedInstanceState.getSerializable("deck");
            this.trumpCard = (Card)savedInstanceState.getSerializable("trumpCard");
            this.hand = (Card[])savedInstanceState.getSerializable("hand");
        } else {
            this.deck = new Deck();

            this.trumpCard = this.deck.pullCard();
            this.hand = new Card[10];
            for(int i = 0; i < this.hand.length; i++) {
                this.hand[i] = this.deck.pullCard();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the state of the game here, in the future labs we will actually use this
        outState.putSerializable("deck", this.deck);
        outState.putSerializable("trumpCard", this.trumpCard);
        outState.putSerializable("hand", this.hand);
    }

    private void setupClickEvents() {
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
        if(cardPlayed < 5 && this.hand[index] != null) {
            if(lastClickedCard == index) {
                cardView.setImageBitmap(null);
                this.playField[cardPlayed].setImageResource(this.hand[index].getResourceImage());
                this.hand[index] = null;
                cardView.setBackgroundColor(Color.TRANSPARENT);
                cardPlayed++;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
