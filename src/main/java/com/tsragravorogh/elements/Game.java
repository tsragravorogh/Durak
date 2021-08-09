package com.tsragravorogh.elements;

import com.tsragravorogh.utils.CyclicLinkedList;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    private Card trump;
    private LinkedList<Card> deck;
    private CyclicLinkedList<Player> players;
    private ArrayList<Round> fightHistory;
    private ArrayList<Card> cardsOnDesk;
    private boolean isPickedUp;

    public Game() {
    }

    public Card getTrump() {
        return trump;
    }

    public void setTrump(Card trump) {
        this.trump = trump;
    }

    public LinkedList<Card> getDeck() {
        return deck;
    }

    public void setDeck(LinkedList<Card> deck) {
        this.deck = deck;
    }

    public CyclicLinkedList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(CyclicLinkedList<Player> players) {
        this.players = players;
    }

    public ArrayList<Round> getFightHistory() {
        return fightHistory;
    }

    public void setFightHistory(ArrayList<Round> fightHistory) {
        this.fightHistory = fightHistory;
    }

    public Player getNext(Player player) {
        return players.getNext(player);
    }

    public ArrayList<Card> getCardsOnDesk() {
        return cardsOnDesk;
    }

    public void setCardsOnDesk(ArrayList<Card> cardsOnDesk) {
        this.cardsOnDesk = cardsOnDesk;
    }

    public boolean isPickedUp() {
        return isPickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
        if (pickedUp) System.out.println("потянул");
    }
}
