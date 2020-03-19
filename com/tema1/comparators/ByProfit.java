package com.tema1.comparators;

import java.util.Comparator;

import com.tema1.goods.Goods;

public final class ByProfit implements Comparator<Goods> {

    @Override
    public int compare(final Goods card1, final Goods card2) {
        if (card1.getProfit() != card2.getProfit()) {
            return card2.getProfit() - card1.getProfit();
        } else {
            return card2.getId() - card1.getId();
        }
    }

}
