package com.itderrickh.cardgame.fragments;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.itderrickh.cardgame.R;

public class BiddingFragment extends Fragment implements View.OnClickListener {
    public BiddingFragment() { }

    public static BiddingFragment newInstance() {
        BiddingFragment fragment = new BiddingFragment();
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
        View view = inflater.inflate(R.layout.fragment_bidding, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Integer[] values = {1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), R.layout.support_simple_spinner_dropdown_item, values);
        Spinner spinner = (Spinner) getView().findViewById(R.id.biddingSpinner);
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
}
