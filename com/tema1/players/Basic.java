package com.tema1.players;

import java.util.Collections;

import com.tema1.common.Constants;
import com.tema1.common.Functions;
import com.tema1.comparators.ByProfit;
import com.tema1.engine.GameManager;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;

public class Basic extends Player {

    public Basic(final int id) {
        super(id);
        strategy = 0;
    }

    /**
     * nu stiu de ce da eroare de checkstyle aici.
     */

    void createSack() {
        sack.clear();
        int[] fr = new int[Constants.CARDS_COUNT];
        GoodsFactory gf = GoodsFactory.getInstance();
        for (Goods card : hand) {
            if (card.getType() == GoodsType.Legal) {
                fr[card.getId()] = Collections.frequency(hand, card);
            }
        }
        int maxFr = Functions.maxValue(fr);

        Collections.sort(hand, new ByProfit());
        if (maxFr > 0) {
            for (Goods card : hand) {
                if (fr[card.getId()] == maxFr) {
                    declaredCard = card;
                    break;
                }
            }
        } else {
            declaredCard = gf.getGoodsById(0);
        }
        if (maxFr > 0) {
            for (Goods card : hand) {
                if (card == declaredCard && sack.size()
                        <= Constants.MAX_SACK_SIZE) {
                    sack.add(card);
                }
            }
            for (int i = 0; i < maxFr;) {
                hand.remove(declaredCard);
                i++;
            }
        } else {
            sack.add(hand.get(0));
            hand.remove(0);
        }

        beGreedy();
    }

    /**
     * pt checkstyle :).
     */

    public void playSheriff() {
        isSheriff = true;
        hand.clear();
        if (coins < Constants.MIN_COINS) {
            return;
        }
        for (Player player : GameManager.getPlayerList()) {
            if (!player.isSheriff()) {
                inspect(player, GameManager.getDeck());
            }
        }
    }

    /**
     *
     */
    void beGreedy() { }

    /**
     * nici aici.
     */
    void setBribe() {
        bribe = 0;
    }
}
