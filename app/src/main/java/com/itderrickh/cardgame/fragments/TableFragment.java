package com.itderrickh.cardgame.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.itderrickh.cardgame.Card;
import com.itderrickh.cardgame.Deck;
import com.itderrickh.cardgame.R;

public class TableFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Deck mDeck;
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

    public TableFragment() {
        // Required empty public constructor

        mDeck = new Deck();
    }

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

        card1 = (ImageView) view.findViewById(R.id.card1);
        card2 = (ImageView) view.findViewById(R.id.card2);
        card3 = (ImageView) view.findViewById(R.id.card3);
        card4 = (ImageView) view.findViewById(R.id.card4);
        card5 = (ImageView) view.findViewById(R.id.card5);
        card6 = (ImageView) view.findViewById(R.id.card6);
        card7 = (ImageView) view.findViewById(R.id.card7);
        card8 = (ImageView) view.findViewById(R.id.card8);
        card9 = (ImageView) view.findViewById(R.id.card9);
        card10 = (ImageView) view.findViewById(R.id.card10);

        updateCards();

        return view;
    }

    public void updateCards() {
        Card[] cards = new Card[10];
        for(int i = 0; i < cards.length; i++) {
            cards[i] = mDeck.pullCard();
        }

        card1.setImageResource(cards[0].getResourceImage());
        card2.setImageResource(cards[1].getResourceImage());
        card3.setImageResource(cards[2].getResourceImage());
        card4.setImageResource(cards[3].getResourceImage());
        card5.setImageResource(cards[4].getResourceImage());
        card6.setImageResource(cards[5].getResourceImage());
        card7.setImageResource(cards[6].getResourceImage());
        card8.setImageResource(cards[7].getResourceImage());
        card9.setImageResource(cards[8].getResourceImage());
        card10.setImageResource(cards[9].getResourceImage());
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
