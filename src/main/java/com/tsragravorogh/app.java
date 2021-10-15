package com.tsragravorogh;

import com.tsragravorogh.elements.Game;
import com.tsragravorogh.services.GameService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class app {
    private final List<String> list = new ArrayList<>();
    public static void main(String[] args) {
//        Game game = new Game();
//        GameService svc = new GameService();
//        svc.play(game, 4);
        int i1 = Integer.MAX_VALUE;
        int i2 = Integer.MAX_VALUE;
        System.out.println(i1 + i2);

        int n = 0;
        for (int i = 0; i < 10; i++) {
           n = n++;
        }
     


//        class Sum {
//            void sum(int a, int b) {
//                System.out.println(a + b);
//            }
//
//            void sum(String a, String b) {
//                System.out.println(a + b);
//            }
//        }
//
//        var sum = new Sum();
//        sum.sum("5", "5");
//
//
//        Cat cat = null;
//        cat.say();
//
//        var a = new ArrayList<String>();
//        var b = new ArrayList<Integer>();
//        System.out.println(a.getClass() == b.getClass());



    }
}

class Sum {
    void sum(int a, int b) {
        System.out.println(a + b);
    }

    void sum(String a, String b) {
        System.out.println(a + b);
    }
}

class Cat {
    static void say() {
        System.out.println("Meow!");
    }
}
