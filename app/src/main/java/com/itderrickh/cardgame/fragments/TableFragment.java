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
    private ImageView card1;
    private ImageView card2;
    private ImageView card3;
    private ImageView card4;
    private ImageView card5;
    private ImageView card6;
    private ImageView card7;
    private ImageView card8;
    private ImageView card9;
    private ImageView card10;
    private ImageView trumpCardImage;
    private Card[] hand;
    private Card trumpCard;
    private ImageView playCard1;
    private ImageView playCard2;
    private ImageView playCard3;
    private ImageView playCard4;
    private ImageView playCard5;

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

        card1 = (ImageView) getView().findViewById(R.id.card1);
        card2 = (ImageView) getView().findViewById(R.id.card2);
        card3 = (ImageView) getView().findViewById(R.id.card3);
        card4 = (ImageView) getView().findViewById(R.id.card4);
        card5 = (ImageView) getView().findViewById(R.id.card5);
        card6 = (ImageView) getView().findViewById(R.id.card6);
        card7 = (ImageView) getView().findViewById(R.id.card7);
        card8 = (ImageView) getView().findViewById(R.id.card8);
        card9 = (ImageView) getView().findViewById(R.id.card9);
        card10 = (ImageView) getView().findViewById(R.id.card10);
        trumpCardImage = (ImageView) getView().findViewById(R.id.trumpCard);
        playCard1 = (ImageView) getView().findViewById(R.id.playCard1);
        playCard2 = (ImageView) getView().findViewById(R.id.playCard2);
        playCard3 = (ImageView) getView().findViewById(R.id.playCard3);
        playCard4 = (ImageView) getView().findViewById(R.id.playCard4);
        playCard5 = (ImageView) getView().findViewById(R.id.playCard5);

        trumpCardImage.setImageResource(trumpCard.getResourceImage());
        card1.setImageResource(this.hand[0].getResourceImage());
        card2.setImageResource(this.hand[1].getResourceImage());
        card3.setImageResource(this.hand[2].getResourceImage());
        card4.setImageResource(this.hand[3].getResourceImage());
        card5.setImageResource(this.hand[4].getResourceImage());
        card6.setImageResource(this.hand[5].getResourceImage());
        card7.setImageResource(this.hand[6].getResourceImage());
        card8.setImageResource(this.hand[7].getResourceImage());
        card9.setImageResource(this.hand[8].getResourceImage());
        card10.setImageResource(this.hand[9].getResourceImage());

        setupClickEvents();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
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
        outState.putSerializable("deck", this.deck);
        outState.putSerializable("trumpCard", this.trumpCard);
        outState.putSerializable("hand", this.hand);
    }

    private void setupClickEvents() {
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card1, 0);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card2, 1);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card3, 2);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card4, 3);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card5, 4);
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card6, 5);
            }
        });

        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card7, 6);
            }
        });

        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card8, 7);
            }
        });

        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card9, 8);
            }
        });

        card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick(card10, 9);
            }
        });
    }

    private void cardClick(ImageView cardView, int index) {
        cardView.setImageBitmap(null);
        playCard1.setImageResource(this.hand[index].getResourceImage());
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
