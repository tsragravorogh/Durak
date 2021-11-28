package com.tsragravorogh.services;

import com.tsragravorogh.elements.*;
import com.tsragravorogh.utils.CyclicLinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class GameService {
    Logger logger = LoggerFactory.getLogger(GameService.class);

    public void play(Game g, int playersCount) {
        initPlayers(g, playersCount);
        initCards(g);
        dealCardsToPlayers(g);
        initHistory(g);
        initGame(g);
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

        shuffle(deck);

        Card trump = deck.getFirst();
        g.setTrump(trump);
        g.setDeck(deck);
    }

    private void initHistory(Game g) {
        ArrayList<Round> rounds = new ArrayList<>();
        g.setFightHistory(rounds);
        g.setWinnerList(new ArrayList<>());
    }

    private void shuffle(LinkedList<Card> cards) {
        Collections.shuffle(cards);
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
            logger.info("Player " + (i + 1) + " got cards: {}", cardsToPlayer.toString());
        }
        g.setPlayers(players);
        g.setDeck(deck);
    }

    private void initGame(Game g) {
        logger.info("Game is started");
        Player source = null;
        Player target;
//        SerializeService ss = new SerializeService();
//        g = ss.deserialize();
        while (isGameAlive(g)) {

            if(isFirstRound(g)){
                source = getFirstSourcePlayer(g);
                logger.info("First source Player is {}", source.getName());
                target = g.getPlayers().getNext(source);
                logger.info("First target Player is {}", target.getName());
            }
            else{
                source = getSourcePlayer(g, source);
                logger.info("Source Player is {}", source.getName());
                target = getTargetPlayer(g, source);
                logger.info("Target Player is {}", target.getName());
            }
            setConfig(g);
//            if(g.getPlayers().size() == 3) {
//                ss.serialize(g);
//            }
            source = attack(g, source, target);
            defense(g, source, target);

            if (g.getDeck().size() != 0) {
                getNeedCard(g);
            }
            removeWinningPlayers(g);
        }
        logger.info("Game is ended");
        showRatingList(g);
    }

    private void showRatingList(Game g) {
        g.getWinnerList().add(g.getPlayers().get(0));
        for(int i = 0; i < g.getWinnerList().size(); i++) {
            System.out.println("Player " + g.getWinnerList().get(i) + " has got " + (i + 1) + " place");
        }
    }

    private void setConfig(Game g) {
        if(g.isPickedUp()) g.setPickedUp(false);
    }

    private void removeWinningPlayers(Game g) {
        ArrayList<Player> playersToRemove = new ArrayList<>();

        for(int i = 0; i < g.getPlayers().size(); i++) {
            if(g.getPlayers().get(i).getPlayerCards().size() == 0) {
                logger.info(g.getPlayers().get(i) + " won and out of the game");
                playersToRemove.add(g.getPlayers().get(i));
                g.getWinnerList().add(g.getPlayers().get(i));
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

        while (cardsToDefineIterator.hasNext() && !g.isPickedUp()) { // проходим по всем картам которые надо побить
            Card cardToDefine = cardsToDefineIterator.next();
            Card selectedCard = cardSelection(g, cardToDefine, targetCards); // отбираем карту у игрока которой будем бить
            if(selectedCard != cardToDefine) { // если можем отбить
                targetCards.remove(selectedCard); // бьем картой отбивающего
                cardsToDefineIterator.remove(); // побили карту с стола
                fights.add(new Fight(cardToDefine, selectedCard));
            }else {
                g.setPickedUp(true);
                fights.add(new Fight(cardToDefine));
            }
        }
        if(g.isPickedUp()) {
            ArrayList<Card> cardsForTarget = new ArrayList<>(g.getCardsOnDesk());
            cardsForTarget.addAll(targetCardsStart);
            target.setPlayerCards(cardsForTarget);
            g.setCardsOnDesk(new ArrayList<>());
            Round round = new Round(source, target, fights);
            ArrayList<Round> allRound =  g.getFightHistory();
            allRound.add(round);
            g.setFightHistory(allRound);
            logger.info("Target player was picked up cards: {}", target.getName());
        }else { // заполняем историю сражения
            Round round = new Round(source, target, fights);
            ArrayList<Round> allRound =  g.getFightHistory();
            allRound.add(round);
            g.setFightHistory(allRound);
            target.setPlayerCards(targetCards);
            g.setCardsOnDesk(new ArrayList<>());
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

    private Player attack(Game g, Player source, Player target) {
        putSourceCards(g, source);
        source = putSimilarCards(g, source, target);
        return source;
    }

    private Player putSimilarCards(Game g, Player source, Player target) {

        Player beforeSource = g.getPlayers().playerBeforePlayer(source);
        int count = Math.min(target.getPlayerCards().size(), 6);
        ArrayList<Card> cardsOnDesk = g.getCardsOnDesk();
        ArrayList<Card> cardsToAdd = new ArrayList<>(cardsOnDesk);

        for(int i = 0; i < g.getPlayers().size(); i++) {
            if(g.getPlayers().get(i) != target){
                ArrayList<Card> playerCards = g.getPlayers().get(i).getPlayerCards();
                Iterator<Card> playerCardsIterator = playerCards.iterator();

                for (Card c : cardsOnDesk) {
                    while (playerCardsIterator.hasNext()) {
                        Card playerCard = playerCardsIterator.next();
                        if (c.getFace().getRank() == playerCard.getFace().getRank() && count != 0 && c.getSuit() != playerCard.getSuit()) {
                            cardsToAdd.add(playerCard);
                            playerCardsIterator.remove();
                            count--;
                        }
                    }
                }
                g.getPlayers().get(i).setPlayerCards(playerCards);
            }
        }
        boolean didSourceWin = false;
        int size = g.getPlayers().size();
        for(int i = 0; i < size; i++) {
            if(g.getPlayers().get(i).getPlayerCards().size() == 0) {
                logger.info(g.getPlayers().get(i) + " won and out of the game");
                if(g.getPlayers().get(i) == source) {
                    didSourceWin = true;
                }
                g.getWinnerList().add(g.getPlayers().get(i));
                g.getPlayers().removePlayer(i);
            }
        }



        g.setCardsOnDesk(cardsToAdd);
        logger.info("Cards on the desk {}", cardsToAdd);
        return didSourceWin ? beforeSource : source;
    }

    private void putSourceCards(Game g, Player source) {
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<Card> sorted = sortCards(source.getPlayerCards());
        cards.add(sorted.remove(0));
        source.setPlayerCards(sorted);
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

                cards = cards.stream().filter(c -> c.getSuit() == g.getTrump().getSuit()).filter(c -> c.getFace().getRank() > g.getTrump().getFace().getRank()).collect(Collectors.toCollection(ArrayList::new));
                Optional<Card> c = cards.stream().max(Comparator.comparingInt(e -> e.getFace().getRank()));
                if(c.isPresent()) {
                    highCard.put(players.get(i), c.get());
                }
            }
            high = highCard.entrySet().stream().max(Comparator.comparingInt(e -> e.getValue().getFace().getRank()));
        }else{
            for (int i = 0; i < players.size(); i++) {
                ArrayList<Card> cards = players.get(i).getPlayerCards();

                cards = cards.stream().filter(c -> c.getSuit() == g.getTrump().getSuit()).filter(c -> c.getFace().getRank() < g.getTrump().getFace().getRank()).collect(Collectors.toCollection(ArrayList::new));
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
