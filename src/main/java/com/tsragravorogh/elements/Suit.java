package com.tsragravorogh.elements;

public enum Suit {
    CLUBS("♣"), DIAMONDS("♦"), HEARTS("♥"), SPADES("♠");

    private String value;

    Suit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Suit[] items = new Suit[]{
            CLUBS, DIAMONDS, HEARTS, SPADES
    };
}
