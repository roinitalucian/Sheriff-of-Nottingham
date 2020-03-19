package com.tema1.players;

import java.util.Collections;

import com.tema1.common.Constants;
import com.tema1.comparators.ByProfit;
import com.tema1.engine.GameManager;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;

public final class Bribed extends Basic {

    private int illegalGoodsNumber = 0;
    private int possiblePenalty = 0;

    public Bribed(final int id) {
        super(id);
        strategy = 2;
    }

    /**
     * se creeaza sacul ilegal doar in conditiile in care jucatorul isi permite.
     */

    void createSack() {
        for (Goods card : hand) {
            if (card.getType() == GoodsType.Illegal) {
                illegalGoodsNumber++;
            }
        }
        if (coins <= Constants.MIN_COINS_BRIBED || illegalGoodsNumber == 0) {
            illegalGoodsNumber = 0;
            super.createSack();
        } else {
            createBribedSack();
        }
    }

    void createBribedSack() {
        sack.clear();
        GoodsFactory gf = GoodsFactory.getInstance();
        declaredCard = gf.getGoodsById(0);
        Collections.sort(hand, new ByProfit());
        possiblePenalty = 0;
        for (Goods card : hand) {
            if (sack.size() == Constants.MAX_SACK_SIZE) {
                break;
            }
            if (card.getType() == GoodsType.Legal) {
                break;
            }
            if (sack.size() >= Constants.SACK_SIZE_LEGAL && coins <= Constants.COINS_LEGAL) {
                break;
            }
            if (possiblePenalty + card.getPenalty() >= coins) {
                break;
            }
            sack.add(card);
            possiblePenalty += card.getPenalty();
        }
        illegalGoodsNumber = sack.size();
        setBribe();

        for (int i = 0; i < sack.size(); i++) {
            hand.remove(0);
        }
        if (hand.size() > 0) {
            while (hand.get(0).getType() == GoodsType.Illegal) {
                hand.remove(0);
                if (hand.size() == 0) {
                    return;
                }
            }
        }
        while (sack.size() < Constants.MAX_SACK_SIZE && possiblePenalty
                + hand.get(0).getPenalty() < coins) {
            sack.add(hand.get(0));
            possiblePenalty += hand.get(0).getPenalty();
            hand.remove(0);
        }
    }

    public void playSheriff() {
        if (coins < Constants.MIN_COINS_SHERIFF) {
            return;
        }
        isSheriff = true;
        hand.clear();
        if (getId() == 0) {
            inspect(GameManager.getPlayerList().get(GameManager.
                    getPlayerList().size() - 1),
                    GameManager.getDeck());
            if (GameManager.getPlayerList().size() > 2) {
                inspect(GameManager.getPlayerList().get(1), GameManager.getDeck());
            }
        } else if (getId() == GameManager.getPlayerList().size() - 1) {
            inspect(GameManager.getPlayerList().get(getId() - 1),
                    GameManager.getDeck());
            if (GameManager.getPlayerList().size() > 2) {
                inspect(GameManager.getPlayerList().get(0), GameManager.getDeck());
            }
        } else {
            inspect(GameManager.getPlayerList().get(getId() - 1),
                    GameManager.getDeck());
            inspect(GameManager.getPlayerList().get(getId() + 1),
                    GameManager.getDeck());
        }
    }

    void setBribe() {
        if (illegalGoodsNumber == 0) {
            bribe = 0;
        } else if (illegalGoodsNumber < Constants.BRIBE_LEVEL_2) {
            bribe = Constants.BRIBE_2;
        } else {
            bribe = Constants.BRIBE_3;
        }
    }
}
