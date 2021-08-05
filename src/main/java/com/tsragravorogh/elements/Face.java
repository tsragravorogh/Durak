package com.tsragravorogh.elements;

public enum Face {
    SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9), TEN("10", 10), JACK("J", 11), QUEEN("Q", 12), KING("K", 13), ACE("A", 14);

    private String value;
    private int rank;

    Face(String value, int rank) {
        this.value = value;
        this.rank = rank;
    }

    public String getValue() {
        return value;
    }

    public int getRank() {
        return rank;
    }

    public static Face[] items = new Face[]{
            SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    };
}
