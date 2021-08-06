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
        //initGame(g);
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

        Card trump = deck.getFirst();
        g.setTrump(trump);

        deck = shuffle(deck);
        g.setDeck(deck);
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
        while (isGameAlive(g)) {
            Player source = null;
            Player target;

            boolean isPickedUp = false;
            //TODO choice of the user who plays first
            if(isFirstRound(g)){
                source = getFirstSourcePlayer(g);
                target = g.getPlayers().getNext(source);
            }
            else{
                source = getSourcePlayer(g, isPickedUp, source);
                target = getTargetPlayer(g, source);
            }
            attack(g, source, target);


            //Round round = new Round(source, target, );
        }
    }

    private void attack(Game g, Player source, Player target) {
        ArrayList<Card> sourceCards = sortCards(source.getPlayerCards());
        //ArrayList<Card> targetCards = sortCards(target.getPlayerCards());

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
        // TODO delete the player if he doesn't have any cards
    }

    private void putSourceCards(Game g, Player source) {
        ArrayList<Card> cards = g.getCardsOnDesk();
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
        return g.getFightHistory() == null;
    }

    private boolean isGameAlive(Game g){
        return g.getPlayers().size() > 1;
    }

    private Player getSourcePlayer(Game g, boolean isPickedUp, Player previousSource) {
        Player player;
        if (!isPickedUp) {
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
