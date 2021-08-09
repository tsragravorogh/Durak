package com.tsragravorogh.elements;

public class Fight {
    private Card down;
    private Card up;

    public Fight(Card down, Card up) {
        this.down = down;
        this.up = up;
    }

    public Fight(Card down) {
        this.down = down;
        this.up = null;
    }

    public void setDown(Card down) {
        this.down = down;
    }

    public void setUp(Card up) {
        this.up = up;
    }
}
