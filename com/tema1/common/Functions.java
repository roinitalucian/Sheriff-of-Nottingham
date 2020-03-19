package com.tema1.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tema1.comparators.PlayersByCoins;
import com.tema1.players.Player;

public final class Functions {

    private Functions() {

    }

    public static int maxValue(final int[] array) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return Collections.max(list);
    }

    public static void print(final ArrayList<Player> list) {
        Collections.sort(list, new PlayersByCoins());
        StringBuilder sb = new StringBuilder("");
        for (Player player : list) {
            sb.append(player.getId() + " ");
            sb.append(player.getStrategy() + " ");
            sb.append(player.getCoins() + "\n");
        }
        System.out.println(sb);
    }
}
