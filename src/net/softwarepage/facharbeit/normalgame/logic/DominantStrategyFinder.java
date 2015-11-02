package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.softwarepage.facharbeit.normalgame.helpers.ListHelper;

public class DominantStrategyFinder implements Serializable {
    
    private final NormalGame game;

    public DominantStrategyFinder(NormalGame game) {
        this.game = game;
    }
    
    public List<Strategy> findDominatedStrategy(Player player) {
        List<Strategy> ownStrategies = player.getStrategies();
        List<Strategy> opponentStrategies = game.getOpponentStrategies(player);
        List<Strategy> dominatedStrategies = new ArrayList<>();
        for (Strategy ownStrategy : ownStrategies) {
            for (Strategy ownStrategy2 : ownStrategies) {
                if (ownStrategy.equals(ownStrategy2)) {
                    continue;
                }
                boolean weaklyDominated = false;
                List<Strategy> possiblyDominated = new ArrayList<>();
                for (Strategy opponentStrategy : opponentStrategies) {
                    StrategyPair firstPair = new StrategyPair(ownStrategy, opponentStrategy);
                    StrategyPair secondPair = new StrategyPair(ownStrategy2, opponentStrategy);
                    if (payoffIsLess(player, firstPair, secondPair)) {
                        possiblyDominated.add(ownStrategy);
                    } else if (payoffIsLess(player, secondPair, firstPair)) {
                        possiblyDominated.add(ownStrategy2);
                    } else {
                        weaklyDominated = true;
                    }
                }
                if (ListHelper.isListOfSameElements(possiblyDominated) && !possiblyDominated.isEmpty()) {
                    Strategy dominatedStrat = possiblyDominated.get(0);
                    if (!dominatedStrategies.contains(dominatedStrat)) {
                        StrategyType type = weaklyDominated ? StrategyType.WeaklyDominated : StrategyType.Dominated;
                        dominatedStrat.setType(type);
                        dominatedStrategies.add(dominatedStrat);
                    }
                }
            }
        }
        return dominatedStrategies;
    }
    
    private boolean payoffIsLess(Player player, StrategyPair firstStrategyPair, StrategyPair secondStrategyPair) {
        return game.getPayoff(player, firstStrategyPair.getStrategy1(), firstStrategyPair.getStrategy2())
                < game.getPayoff(player, secondStrategyPair.getStrategy1(), secondStrategyPair.getStrategy2());
    }
}
