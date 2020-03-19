package com.tema1.comparators;

import java.util.Comparator;

import com.tema1.players.Player;

public final class PlayersByCoins implements Comparator<Player> {

    @Override
    public int compare(final Player arg0, final Player arg1) {
        return arg1.getCoins() - arg0.getCoins();
    }

}
