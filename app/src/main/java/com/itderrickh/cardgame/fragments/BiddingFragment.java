package com.itderrickh.cardgame.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.itderrickh.cardgame.Card;
import com.itderrickh.cardgame.R;

public class BiddingFragment extends Fragment implements View.OnClickListener {

    private int biddingSlots;
    private Spinner spinner;
    public BiddingFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

        submitBid.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Load or initialize datas
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the state of the game here, in the future labs we will actually use this
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) spinner.getSelectedView();
        String result = textView.getText().toString();
        int bid = Integer.parseInt(result);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FieldFragment makeField = FieldFragment.newInstance(new Card[5]);
        fragmentTransaction.replace(R.id.playingArea, makeField, "BIDDING");
        fragmentTransaction.commit();

        TableFragment tableFragment = (TableFragment) fragmentManager.findFragmentById(R.id.tables_fragment);
        tableFragment.setupClickEvents();
        tableFragment.doneBidding = true;
        tableFragment.bid = bid;
        tableFragment.updateBid();
    }
}
