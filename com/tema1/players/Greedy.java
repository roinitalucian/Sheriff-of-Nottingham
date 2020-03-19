package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.engine.GameManager;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

public final class Greedy extends Basic {

    public Greedy(final int id) {
        super(id);
        strategy = 1;
    }

    /**
     * jucatorul greedy aplica strategia basic la care mai adauga ceva.
     */

    protected void beGreedy() {
        if (GameManager.getRound() % 2 == 0) {
            if (sack.size() < Constants.MAX_SACK_SIZE) {
                for (Goods card : hand) {
                    if (card.getType() == GoodsType.Illegal) {
                        sack.add(card);
                        break;
                    }
                }
            }
        }
    }

    public void playSheriff() {
        isSheriff = true;
        hand.clear();
        if (coins < Constants.MIN_COINS) {
            return;
        }
        for (Player player : GameManager.getPlayerList()) {
            if (!player.isSheriff()) {
                if (player.getBribe() == 0) {
                    inspect(player, GameManager.getDeck());
                } else {
                    takePenalty(-player.takeBribe());
                }
            }
        }
    }

}
