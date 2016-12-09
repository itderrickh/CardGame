package com.itderrickh.cardgame.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.itderrickh.cardgame.helpers.Card;
import com.itderrickh.cardgame.R;

import java.io.Serializable;

public class BiddingFragment extends Fragment implements View.OnClickListener, Serializable {

    private int biddingSlots;
    private int bid = 0;
    private Spinner spinner;
    public OnBidInteractionManager listener;

    public BiddingFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnBidInteractionManager) {
            listener = (OnBidInteractionManager) context;
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if(context instanceof OnBidInteractionManager) {
            listener = (OnBidInteractionManager) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static BiddingFragment newInstance(int slots) {
        BiddingFragment fragment = new BiddingFragment();
        Bundle args = new Bundle();
        args.putInt("slots", slots);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.biddingSlots = getArguments().getInt("slots");

        if(savedInstanceState != null) {
            this.bid = savedInstanceState.getInt("bid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bidding, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Initialize array 1 to N
        final Integer[] values = new Integer[biddingSlots + 1];
        for(int i = 0; i <= biddingSlots; i++) { values[i] = i; }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), R.layout.support_simple_spinner_dropdown_item, values);
        spinner = (Spinner) getView().findViewById(R.id.biddingSpinner);
        Button submitBid = (Button) getView().findViewById(R.id.submitBid);
        spinner.setAdapter(adapter);
        spinner.setSelection(bid);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bid = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        submitBid.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("bid", bid);
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) spinner.getSelectedView();
        String result = textView.getText().toString();
        bid = Integer.parseInt(result);

        listener.onBidInteraction(this.bid);
    }

    public interface OnBidInteractionManager {
        void onBidInteraction(int bid);
    }
}
