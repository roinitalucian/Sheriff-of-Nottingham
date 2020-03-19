package com.tema1.comparators;

import java.util.Collections;
import java.util.Comparator;

import com.tema1.goods.Goods;
import com.tema1.players.Player;

public final class PlayersByItem implements Comparator<Player> {

    private Goods card;

    public PlayersByItem(final Goods card) {
        this.card = card;
    }

    @Override
    public int compare(final Player p1, final Player p2) {
        int cards1, cards2;
        cards1 = Collections.frequency(p1.getTable(), card);
        cards2 = Collections.frequency(p2.getTable(), card);
        if (cards1 != cards2) {
            return cards2 - cards1;
        } else {
            return p1.getId() - p2.getId();
        }
    }

}
