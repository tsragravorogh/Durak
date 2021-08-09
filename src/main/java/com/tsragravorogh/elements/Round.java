package com.tsragravorogh.elements;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private Player source;
    private Player target;
    private ArrayList<Fight> fights;

    public Round(Player source, Player target, ArrayList<Fight> fights) {
        this.source = source;
        this.target = target;
        this.fights = fights;
    }

    public Player getSource() {
        return source;
    }

    public void setSource(Player source) {
        this.source = source;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public List<Fight> getFights() {
        return fights;
    }

    public void setFights(ArrayList<Fight> fights) {
        this.fights = fights;
    }
}
