package com.tsragravorogh.elements;

import java.util.List;

public class Round {
    private Player source;
    private Player target;
    private List<Fight> fights;

    public Round(Player source, Player target, List<Fight> fights) {
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

    public void setFights(List<Fight> fights) {
        this.fights = fights;
    }
}
