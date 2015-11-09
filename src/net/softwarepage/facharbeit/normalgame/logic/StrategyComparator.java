package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.Comparator;

public class StrategyComparator implements Comparator<Strategy>, Serializable {  //Dient dazu, die Strategien zu sortieren

    private final NormalGame game;

    public StrategyComparator(NormalGame game) {
        this.game = game;
    }

    @Override
    public int compare(Strategy s1, Strategy s2) {
        if (game.getPlayer1().getStrategies().contains(s1) && game.getPlayer2().getStrategies().contains(s2)) {
            return -1;
        } else {
            Player player = game.getPlayer1().getStrategies().contains(s1) ? game.getPlayer1() : game.getPlayer2();
            if (player.getStrategies().indexOf(s1) < player.getStrategies().indexOf(s2)) {
                return -1;
            }
            if(player.getStrategies().indexOf(s1) > player.getStrategies().indexOf(s2)) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}
