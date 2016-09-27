package com.itderrickh.cardgame;

import android.graphics.drawable.Drawable;

import java.lang.reflect.Field;

public class Card {
    public final static int SPADES = 0;
    public final static int HEARTS = 1;
    public final static int DIAMONDS = 2;
    public final static int CLUBS = 3;

    public final static int ACE = 1;
    public final static int JACK = 11;
    public final static int QUEEN = 12;
    public final static int KING = 13;

    private final int suit;
    private final int value;

    public Card() {
        this(SPADES, ACE);
    }

    public Card(int suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public int getSuit() {
        return this.suit;
    }

    public int getValue() {
        return this.value;
    }

    public String getSuitString() {
        switch (this.suit) {
            case SPADES:
                return "spades";
            case HEARTS:
                return "hearts";
            case DIAMONDS:
                return "diamonds";
            case CLUBS:
                return "clubs";
            default:
                return "spades";
        }
    }

    public String getValueString() {
        switch (this.value) {
            case 1:
                return "ace";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            case 10:
                return "ten";
            case 11:
                return "jack";
            case 12:
                return "queen";
            default:
                return "king";
        }
    }

    private int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getResourceImage() {
        String resourceString = this.getValueString() + "_of_" + this.getSuitString();
        System.out.println(resourceString);
        return getResId(resourceString, R.drawable.class);
    }

    @Override
    public String toString() {
        return this.getValueString() + " of " + this.getSuitString();
    }
}