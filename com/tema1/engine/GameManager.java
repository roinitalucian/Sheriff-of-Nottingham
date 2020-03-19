package com.tema1.engine;

import java.util.ArrayList;
import java.util.Collections;

import com.tema1.common.Functions;
import com.tema1.comparators.PlayersByItem;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.goods.LegalGoods;
import com.tema1.main.GameInput;
import com.tema1.players.Basic;
import com.tema1.players.Bribed;
import com.tema1.players.Greedy;
import com.tema1.players.Player;

/**
 *
 * @author roinitalucian
 * Clasa contine toata logica jocului. Ea creeaza jucatorii, le atribuie
 * rolurile si calculeaza creditele si bonusurile primite de acestia
 */

public final class GameManager {

    public static final int CARDS_COUNT = 25;

    private static int currentRound;
    private static int subRound;

    private static ArrayList<Player> playerList = new ArrayList<Player>();

    private GameInput gameInput;

    private static Deck deck;
    private int sheriffIndex;

    public GameManager(final GameInput gameInput) {
        this.gameInput = gameInput;
        deck = new Deck(gameInput.getAssetIds());
        currentRound = 0;
        subRound = 0;
    }

    /**
     * apeleaza metodele corespunzatoare fiecarei etape a jocului.
     */

    public void startGame() {
        createPlayers();
        for (int i = 1; i <= gameInput.getRounds(); i++) {
            currentRound = i;
            startRound(i);
        }
        for (Player player : playerList) {
            player.sellGoods();
        }
        computeLegalBonus();
        Functions.print(playerList);
    }

    private void createPlayers() {
        int i = 0;
        for (String name : gameInput.getPlayerNames()) {
            if (name.equals("basic")) {
                playerList.add(new Basic(i));
            } else if (name.equals("greedy")) {
                playerList.add(new Greedy(i));
            } else {
                playerList.add(new Bribed(i));
            }
            i++;
        }
    }

    void startRound(final int roundNumber) {
        for (int i = 1; i <= gameInput.getPlayerNames().size(); i++) {
            subRound = i;
            startSubRound(i);
        }
    }

    void startSubRound(final int subRoundNumber) {
        for (Player player : playerList) {
            if (player.getId() == subRoundNumber - 1) {
                sheriffIndex = player.getId();
            } else {
                player.playMerchant(deck);
            }
        }

        playerList.get(sheriffIndex).playSheriff();
        for (Player player : playerList) {
            if (!player.isSheriff()) {
                player.putGoodsOnTable();
            }
        }
    }

    /**
     * se sorteaza jucatorii dupa fiecare tip de bun si li se acorda primilor 2
     * bonusul corespunzator.
     */

    void computeLegalBonus() {
        GoodsFactory gf = GoodsFactory.getInstance();
        for (int i = 0; i < CARDS_COUNT; i++) {
            if (gf.getGoodsById(i) != null && gf.getGoodsById(i).getType()
                    == GoodsType.Legal) {
                Collections.sort(playerList,
                        new PlayersByItem(gf.getGoodsById(i)));
                if (playerList.get(0) == null || !playerList.get(0).getTable().
                        contains(gf.getGoodsById(i))) {
                    continue;
                }
                playerList.get(0).takePenalty(-((LegalGoods) gf.
                        getGoodsById(i)).getKingBonus());
                if (playerList.get(1) == null || !playerList.get(1).getTable().
                        contains(gf.getGoodsById(i))) {
                    continue;
                }
                playerList.get(1).takePenalty(-((LegalGoods) gf.getGoodsById(i))
                        .getQueenBonus());
            }

        }
    }

    public static int getSubRound() {
        return subRound;
    }

    public static int getRound() {
        return currentRound;
    }

    public static ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public static Deck getDeck() {
        return deck;
    }
}
