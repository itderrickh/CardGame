package com.itderrickh.cardgame;

import java.io.Serializable;

public class Deck implements Serializable {
    public static final int SUIT_COUNT = 4;
    public static final int CARD_IN_SUIT_COUNT = 14;
    public static final int DECK_SIZE = 52;
    private Card[] deck;
    private int topCard;

    public Deck() {
        this.deck = new Card[52];
        int cardCt = 0; // How many cards have been created so far.
        for(int suit = 0; suit < SUIT_COUNT; suit++) {
            for(int value = 1; value < CARD_IN_SUIT_COUNT; value++ ) {
                deck[cardCt] = new Card(suit, value);
                cardCt++;
            }
        }

        shuffle();
    }

    public void shuffle() {
        for(int i = deck.length - 1; i > 0; i--) {
            int rand = (int)(Math.random()*(i+1));
            Card temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        topCard = DECK_SIZE;
    }

    public Card pullCard() {
        if (topCard == 0) {
            throw new IllegalStateException("No cards are left in the deck.");
        }

        Card card = deck[topCard - 1];
        topCard--;
        return card;
    }

    public boolean hasCardsLeft() {
        return topCard > 0;
    }
}