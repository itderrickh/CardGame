package com.itderrickh.cardgame.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.itderrickh.cardgame.helpers.Card;
import com.itderrickh.cardgame.R;

public class FieldFragment extends Fragment {

    private ImageView[] playField;
    private Card[] playedCards;
    public FieldFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FieldFragment newInstance(Card[] cards) {
        FieldFragment fragment = new FieldFragment();
        Bundle args = new Bundle();
        args.putSerializable("cards", cards);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.playedCards = (Card[]) getArguments().getSerializable("cards");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_field, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.playField = new ImageView[5];
        this.playField[0] = (ImageView) getView().findViewById(R.id.playCard1);
        this.playField[1] = (ImageView) getView().findViewById(R.id.playCard2);
        this.playField[2] = (ImageView) getView().findViewById(R.id.playCard3);
        this.playField[3] = (ImageView) getView().findViewById(R.id.playCard4);
        this.playField[4] = (ImageView) getView().findViewById(R.id.playCard5);

        for(int d = 0; d < this.playedCards.length; d++) {
            if(this.playedCards[d] != null) {
                this.playField[d].setImageResource(this.playedCards[d].getResourceImage());
            } else {
                //this.playField[d].setImageResource(R.drawable.cardback);
                this.playField[d].setImageBitmap(null);
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
