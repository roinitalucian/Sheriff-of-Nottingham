package com.tema1.engine;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

public final class Deck {
    private Queue<Goods> deck = new LinkedList<Goods>();
    private GoodsFactory gf = GoodsFactory.getInstance();

    public Deck(final List<Integer> cards) {
        for (Integer card : cards) {
            deck.add(gf.getGoodsById(card));
        }
    }

    public void removeCard() {
        deck.remove();
    }

    public Goods drawCard() {
        return deck.element();
    }

    public void addCard(final Goods card) {
        deck.add(card);
    }
}
