package com.itderrickh.cardgame;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.itderrickh.cardgame.fragments.TableFragment;

public class MainActivity extends AppCompatActivity implements TableFragment.OnFragmentInteractionListener {
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape);
            getSupportActionBar().hide();
        } else {
            setContentView(R.layout.activity_main);
        }

        Deck deck = new Deck();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        card1 = (ImageView) findViewById(R.id.card1);
        card2 = (ImageView) findViewById(R.id.card2);
        card3 = (ImageView) findViewById(R.id.card3);
        card4 = (ImageView) findViewById(R.id.card4);
        card5 = (ImageView) findViewById(R.id.card5);
        card6 = (ImageView) findViewById(R.id.card6);
        card7 = (ImageView) findViewById(R.id.card7);
        card8 = (ImageView) findViewById(R.id.card8);
        card9 = (ImageView) findViewById(R.id.card9);
        card10 = (ImageView) findViewById(R.id.card10);

        Card[] cards = new Card[10];
        for(int i = 0; i < cards.length; i++) {
            cards[i] = deck.pullCard();
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
}
