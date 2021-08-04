package com.tsragravorogh.services;

import com.tsragravorogh.elements.Game;
import com.tsragravorogh.elements.Player;

public class GameService {
    public void play(Game g, int playersCount) {
        //TODO 'initPlayers'
        //TODO 'initCards'
        //TODO 'dealCardsToPlayers'
    }

    private Player getSourcePlayer(Game g, boolean isPickedUp, Player playerBefore) {
        System.out.println("когда выбирали source" + g.getPlayers().toString());
        Player player;
        if (!isPickedUp) {
            player = g.getNext(playerBefore);
        } else {
            player = g.getNext(g.getNext(playerBefore));
        }
        return player;
    }
}
