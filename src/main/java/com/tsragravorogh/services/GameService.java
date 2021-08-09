package com.tsragravorogh.services;

import com.tsragravorogh.elements.*;
import com.tsragravorogh.utils.CyclicLinkedList;

import java.util.*;
import java.util.stream.Collectors;

public class GameService {
    public void play(Game g, int playersCount) {
        initPlayers(g, playersCount);
        initCards(g);
        dealCardsToPlayers(g);
        initHistory(g);
        initGame(g);

//        initPlayers(g, playersCount);
//        initCards(g);
//        dealCardsToPlayers(g);
//
//        Player pl1 = g.getPlayers().get(0);
//        Player pl2 = g.getPlayers().get(1);
//        Player pl3 = g.getPlayers().get(2);
//        Player pl4 = g.getPlayers().get(3);
//
//        Player source = getFirstSourcePlayer(g);
//        Player target = getTargetPlayer(g, source);
//        System.out.println(source + "   1");
//        System.out.println(target + "   1");
//        source = getSourcePlayer(g, source);
//        target = getTargetPlayer(g, source);
//        System.out.println(source + "   2");
//        System.out.println(target + "   2");
        // TODO init the game
    }

    private void initPlayers(Game g, int count) {
        CyclicLinkedList<Player> players = new CyclicLinkedList<>();

        if(count > 1 && count < 7){
            for (int i = 1; i <= count; i++) {
                players.addLast(new Player("Player [" + i + "]"));
            }
        }
        g.setPlayers(players);
    }

    private void initCards(Game g) {
        LinkedList<Card> deck = new LinkedList<>();
        for (Suit suit : Suit.items) {
            for (Face face : Face.items) {
                deck.addLast(new Card(suit, face));
            }
        }

        deck = shuffle(deck);

        Card trump = deck.getFirst();
        g.setTrump(trump);
        g.setDeck(deck);
    }

    private void initHistory(Game g) {
        ArrayList<Round> rounds = new ArrayList<>();
        g.setFightHistory(rounds);
    }

    private LinkedList<Card> shuffle(LinkedList<Card> cards) {
        List<Card> cardsTmp = new ArrayList<>(cards);
        Collections.shuffle(cardsTmp);
        return new LinkedList<>(cardsTmp);
    }

    private void dealCardsToPlayers(Game g){
        CyclicLinkedList<Player> players = g.getPlayers();
        LinkedList<Card> deck = g.getDeck();
        for(int i = 0; i < players.size(); i++) {
            ArrayList<Card> cardsToPlayer = new ArrayList<>(6);
            for(int j = 0; j < 6; j++) {
                cardsToPlayer.add(deck.removeLast());
            }
            players.get(i).setPlayerCards(cardsToPlayer);
        }
        g.setPlayers(players);
        g.setDeck(deck);
    }

    // TODO init the game

    private void initGame(Game g) {
        Player source = null;
        Player target;

        while (isGameAlive(g)) {

            //TODO choice of the user who plays first
            if(isFirstRound(g)){
                source = getFirstSourcePlayer(g);
                //System.out.println(source + " FIRST ROUND SOURCE");
                target = g.getPlayers().getNext(source);
                //System.out.println(target + " FIRST ROUND TARGET");
            }
            else{
                source = getSourcePlayer(g, source);
                //System.out.println(source + " SOURCE");
                target = getTargetPlayer(g, source);
                //System.out.println(target + " TARGET");
            }
            System.out.println(source.getName() + " напал на " + target.getName());
            attack(g, source, target);
            defense(g, source, target);
            if(g.isPickedUp()) {
                System.out.println(target.getName() + " потянул");
            } else {
                System.out.println(target.getName() + " отбился");
            }

            if (g.getDeck().size() != 0) {
                getNeedCard(g);
            }

            removeWinningPlayers(g);
        }
    }

    private void removeWinningPlayers(Game g) {
        ArrayList<Player> playersToRemove = new ArrayList<>();

        for(int i = 0; i < g.getPlayers().size(); i++) {
            if(g.getPlayers().get(i).getPlayerCards().size() == 0) {
                playersToRemove.add(g.getPlayers().get(i));
            }
        }

        for (Player player : playersToRemove) {
            g.getPlayers().removePlayer(player);
        }
    }

    private void getNeedCard(Game g) {
        LinkedList<Card> deck = g.getDeck();
        CyclicLinkedList<Player> players = g.getPlayers();

        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).getPlayerCards().size() < 6 && g.getDeck().size() != 0) {
                ArrayList<Card> playerCards = players.get(i).getPlayerCards();
                playerCards.add(deck.removeLast());
                players.get(i).setPlayerCards(playerCards);
            }
        }

        g.setDeck(deck);
        g.setPlayers(players);
    }

    private void defense(Game g, Player source, Player target) {
        ArrayList<Card> targetCards = sortCards(target.getPlayerCards());
        ArrayList<Card> targetCardsStart = sortCards(target.getPlayerCards());
        ArrayList<Card> cardsToDefine = sortCards(g.getCardsOnDesk());
        ArrayList<Fight> fights = new ArrayList<>();

        Iterator<Card> cardsToDefineIterator = cardsToDefine.iterator();

        while (cardsToDefineIterator.hasNext() && !g.isPickedUp()) {
            Card cardToDefine = cardsToDefineIterator.next();
            Card selectedCard = cardSelection(g, cardToDefine, targetCards);
            if(selectedCard != cardToDefine) { // если можем отбить
                targetCards.remove(selectedCard); // бьем картой отбивающего
                cardsToDefineIterator.remove(); // побили карту с стола
                fights.add(new Fight(cardToDefine, selectedCard));
            }else {
                System.out.println(target.getName() + " игрок потянул");
                g.setPickedUp(true);
                fights.add(new Fight(cardToDefine));
            }
        }
        if(g.isPickedUp()) {
            ArrayList<Card> cardsForTarget = new ArrayList<>(g.getCardsOnDesk());
            cardsForTarget.addAll(targetCardsStart);
            target.setPlayerCards(cardsForTarget);
            g.setCardsOnDesk(new ArrayList<Card>());
            Round round = new Round(source, target, fights);
            ArrayList<Round> allRound =  g.getFightHistory();
            allRound.add(round);
            g.setFightHistory(allRound);
        }else { // заполняем историю сражения
            Round round = new Round(source, target, fights);
            ArrayList<Round> allRound =  g.getFightHistory();
            allRound.add(round);
            g.setFightHistory(allRound);
            g.setCardsOnDesk(new ArrayList<Card>());
        }
    }

    private Card cardSelection(Game g, Card toDefine, ArrayList<Card> targetCards) {
        boolean isTrump = g.getTrump().getSuit() == toDefine.getSuit();
        if(!isTrump) {
            for (Card card: targetCards) {
                if(card.getSuit() == toDefine.getSuit() && card.getFace().getRank() > toDefine.getFace().getRank()) {
                    return card;
                }
            }
            for (Card card: targetCards) {
                if(card.getSuit() == g.getTrump().getSuit()) {
                    return card;
                }
            }
        }else{
            for (Card card: targetCards) {
                if(card.getSuit() == toDefine.getSuit() && card.getFace().getRank() > toDefine.getFace().getRank()) {
                    return card;
                }
            }
        }
        return toDefine;
    }

    private void attack(Game g, Player source, Player target) {
        putSourceCards(g, source);
        putSimilarCards(g, target);
    }

    private void putSimilarCards(Game g, Player target) {
        if (g.getPlayers().size() == 2) {
            return;
        }

        int count = Math.min(target.getPlayerCards().size(), 6);
        ArrayList<Card> cardsOnDesk = g.getCardsOnDesk();
        ArrayList<Card> cardsToAdd = new ArrayList<>(cardsOnDesk);

        for(int i = 0; i < g.getPlayers().size(); i++) {
            ArrayList<Card> playerCards = g.getPlayers().get(i).getPlayerCards();
            Iterator<Card> playerCardsIterator = playerCards.iterator();

            for (Card c : cardsOnDesk) {
                while (playerCardsIterator.hasNext()) {
                    Card playerCard = playerCardsIterator.next();
                    if (c.getFace().getRank() == playerCard.getFace().getRank() && count != 0) {
                        cardsToAdd.add(playerCard);
                        playerCardsIterator.remove();
                        count--;
                    }
                }
            }
        }
        int size = g.getPlayers().size();
        for(int i = 0; i < size; i++) {
            if(g.getPlayers().get(i).getPlayerCards().size() == 0) {
                g.getPlayers().removePlayer(i);
            }
        }
        g.setCardsOnDesk(cardsToAdd);
    }

    private void putSourceCards(Game g, Player source) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(source.getPlayerCards().remove(0));
        g.setCardsOnDesk(cards);
    }

    private ArrayList<Card> sortCards(ArrayList<Card> cards) {
        return cards.stream().sorted(Comparator.comparingInt(c -> c.getFace().getRank())).collect(Collectors.toCollection(ArrayList::new));
    }

    private Player getFirstSourcePlayer(Game g) {
        CyclicLinkedList<Player> players = g.getPlayers();
        Map<Player, Card> highCard = new HashMap<>();
        Optional<Map.Entry<Player, Card>> high;

        if(g.getTrump().getFace().getRank() < 11) {
            for (int i = 0; i < players.size(); i++) {
                ArrayList<Card> cards = players.get(i).getPlayerCards();

                cards.stream().filter(c -> c.getSuit() == g.getTrump().getSuit()).filter(c -> c.getFace().getRank() > g.getTrump().getFace().getRank()).collect(Collectors.toCollection(ArrayList::new));
                Optional<Card> c = cards.stream().max(Comparator.comparingInt(e -> e.getFace().getRank()));
                if(c.isPresent()) {
                    highCard.put(players.get(i), c.get());
                }
            }
            high = highCard.entrySet().stream().max(Comparator.comparingInt(e -> e.getValue().getFace().getRank()));
        }else{
            for (int i = 0; i < players.size(); i++) {
                ArrayList<Card> cards = players.get(i).getPlayerCards();

                cards.stream().filter(c -> c.getSuit() == g.getTrump().getSuit()).filter(c -> c.getFace().getRank() < g.getTrump().getFace().getRank()).collect(Collectors.toCollection(ArrayList::new));
                Optional<Card> c = cards.stream().max(Comparator.comparingInt(e -> e.getFace().getRank()));

                if(c.isPresent()){
                    highCard.put(players.get(i), c.get());
                }
            }
            high = highCard.entrySet().stream().max(Comparator.comparingInt(e -> e.getValue().getFace().getRank()));
        }
        return high.get().getKey();
    }

    private boolean isFirstRound(Game g) {
        return g.getFightHistory().size() == 0;
    }

    private boolean isGameAlive(Game g){
        return g.getPlayers().size() > 1;
    }

    private Player getSourcePlayer(Game g, Player previousSource) {
        Player player;
        if (!g.isPickedUp()) {
            player = g.getNext(previousSource);
        } else {
            player = g.getNext(g.getNext(previousSource));
        }
        return player;
    }

    private Player getTargetPlayer(Game g, Player sourcePlayer) {
        return g.getNext(sourcePlayer);
    }
}
