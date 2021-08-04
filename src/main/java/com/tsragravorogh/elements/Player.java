package com.tsragravorogh.elements;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> playerCards;

    public Player() {
    }

    public Player(String name, ArrayList<Card> playerCards) {
        this.name = name;
        this.playerCards = playerCards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
    }

    private String getAllCardsToString(ArrayList<Card> cards) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Card c : cards) {
            sb.append(c.toString());
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "\nPlayer{" +
                "name='" + name + '\'' +
                ", playerCards=" + getAllCardsToString(getPlayerCards()) +
                '}';
    }
}
