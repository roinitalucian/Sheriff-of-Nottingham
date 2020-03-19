package com.tema1.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tema1.common.Constants;
import com.tema1.engine.Deck;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.goods.IllegalGoods;

/**
 *
 * @author roinitalucian
 *
 * Clasa de baza a unui jucator ce va fi extinsa in functie de
 * strategia aleasa.
 */

public abstract class Player {

    private int id;
    protected int coins;
    protected int strategy; // 0=basic 1=greedy 2=bribe
    protected ArrayList<Goods> hand = new ArrayList<Goods>();
    protected ArrayList<Goods> sack = new ArrayList<Goods>();
    protected ArrayList<Goods> table = new ArrayList<Goods>();
    protected Goods declaredCard;
    protected int bribe;
    protected boolean isSheriff;

    public Player() {

    }

    public Player(final int id) {
        this.id = id;
        coins = Constants.INITIAL_COINS;
    }

    public final void playMerchant(final Deck deck) {
        isSheriff = false;
        hand.clear();
        drawCards(deck);
        createSack();
        setBribe();
    }

    /**
     * metoda se supraincarca in functie de strategia aleasa.
     */

    public void playSheriff() {
        isSheriff = true;
        hand.clear();
    }


    public final void drawCards(final Deck deck) {
        hand.clear();
        bribe = 0;
        for (int i = 0; i < Constants.HAND_SIZE; i++) {
            hand.add(deck.drawCard());
            deck.removeCard();
        }
    }

    public final int takeBribe() {
        int credits = bribe;
        bribe = 0;
        coins -= credits;
        return credits;
    }

    public final int getBribe() {
        return bribe;
    }

    public final void inspect(final Player inspectedPlayer, final Deck deck) {
        int penalty = 0;
        ArrayList<Goods> toBeRemoved = new ArrayList<Goods>();
        for (Goods card : inspectedPlayer.getSack()) {
            if (card != inspectedPlayer.getDeclaredCard()) {
                penalty += card.getPenalty();
                deck.addCard(card);
                toBeRemoved.add(card);
            }
        }
        for (Goods card : toBeRemoved) {
            inspectedPlayer.removeFromSack(card);
        }

        if (penalty != 0) {
            inspectedPlayer.takePenalty(penalty);
            coins += penalty;
        } else {
            penalty = inspectedPlayer.getSack().size()
                    * inspectedPlayer.getDeclaredCard().getPenalty();
            coins -= penalty;
            inspectedPlayer.takePenalty(-penalty);
        }
    }

    public final void putGoodsOnTable() {
        while (sack.size() > 0) {
            table.add(sack.get(0));
            sack.remove(0);
        }
    }

    public final void sellGoods() {
        computeIllegalBonus();
        for (Goods card : table) {
            coins += card.getProfit();
        }

    }

    /**
     * pentru fiecare bun ilegal de pe taraba se adauga bonusul aferent acestuia.
     */

    final void computeIllegalBonus() {
        GoodsFactory gf = GoodsFactory.getInstance();
        Map<Goods, Integer> bonuses = new HashMap<Goods, Integer>(Constants.CARDS_COUNT);
        ArrayList<Goods> cardsToBeAdded = new ArrayList<Goods>();
        for (Goods card : table) {
            if (card.getType() == GoodsType.Illegal) {
                bonuses = ((IllegalGoods) gf.getGoodsById(card.getId())).
                        getIllegalBonus();
                for (int i = 0; i < Constants.HAND_SIZE; i++) {
                    if (bonuses.get(gf.getGoodsById(i)) != null) {
                        for (int j = 0; j < bonuses.get(gf.
                                getGoodsById(i)); j++) {
                            cardsToBeAdded.add(gf.getGoodsById(i));
                        }
                    }
                }
            }
        }

        for (Goods card : cardsToBeAdded) {
            table.add(card);
        }
    }

    abstract void createSack();
    abstract void setBribe();

    public final ArrayList<Goods> getSack() {
        return sack;
    }

    public final ArrayList<Goods> getTable() {
        return table;
    }

    public final void removeFromSack(final int index) {
        sack.remove(index);
    }

    public final void removeFromSack(final Goods card) {
        sack.remove(card);
    }

    public final Goods getDeclaredCard() {
        return declaredCard;
    }

    public final void takePenalty(final int penalty) {
        coins -= penalty;
    }

    public final int getId() {
        return id;
    }

    public final boolean isSheriff() {
        return isSheriff;
    }

    public final int getCoins() {
        return coins;
    }

    public final String getStrategy() {
        if (strategy == 0) {
            return "BASIC";
        } else if (strategy == 1) {
            return "GREEDY";
        } else {
            return "BRIBED";
        }
    }
}
