package com.tsragravorogh;

import com.tsragravorogh.elements.Game;
import com.tsragravorogh.services.GameService;

public class app {
    public static void main(String[] args) {
        Game game = new Game();
        GameService svc = new GameService();
        svc.play(game, 4);
    }
}
