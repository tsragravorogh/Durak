package com.tsragravorogh.services;

import com.tsragravorogh.elements.*;
import com.tsragravorogh.utils.CyclicLinkedList;

import java.util.*;

public class GameService {
    public void play(Game g, int playersCount) {
        //TODO 'initPlayers'
        //TODO 'initCards'
        //TODO 'dealCardsToPlayers'
    }

    private void initPlayers(Game g, int count) {
        CyclicLinkedList<Player> players = new CyclicLinkedList<>();

        if(count > 1 && count < 7){
            for (int i = 0; i < count; i++) {
                players.addLast(new Player("Player [" + i + "]"));
            }
        }
        g.setPlayers(players);
    }

    private void initCards(Game g) {
        LinkedList<Card> deck = new LinkedList<>();
        for (Suit suit : Suit.items) {
            for (Face face : Face.items) {
                deck.addLast(new Card(suit, face));
            }
        }

        Card trump = deck.getFirst();
        g.setTrump(trump);

        deck = shuffle(deck);
        g.setDeck(deck);
    }

    private LinkedList<Card> shuffle(LinkedList<Card> cards) {
        List<Card> cardsTmp = new ArrayList<>(cards);
        Collections.shuffle(cardsTmp);
        return new LinkedList<>(cardsTmp);
    }

    private void dealCardsToPlayers(Game g){
        Map<Player, ArrayList<Card>> cardsPlayer = new HashMap<>();
        CyclicLinkedList<Player> players = g.getPlayers();
        LinkedList<Card> deck = g.getDeck();

        for(int i = 0; i < players.size(); i++) {
            ArrayList<Card> cardsToPlayer = new ArrayList<>(6);
            for(int j = 0; j < 6; j++) {
                cardsToPlayer.add(deck.removeLast());
            }
            cardsPlayer.put(players.get(i), cardsToPlayer);
        }

        g.setCardsPlayer(cardsPlayer);
        g.setDeck(deck);
    }

    private Player getSourcePlayer(Game g, boolean isPickedUp, Player playerBefore) {
        Player player;
        if (!isPickedUp) {
            player = g.getNext(playerBefore);
        } else {
            player = g.getNext(g.getNext(playerBefore));
        }
        return player;
    }
}
