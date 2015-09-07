package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PureNashEquilibriumFinder implements Serializable {
    
    private final NormalGame game;

    public PureNashEquilibriumFinder(NormalGame game) {
        this.game = game;
    }
    
    public List<PureNashEquilibrium> findPureNashEquilibria() {
        List<StrategyPair> firstPairs = getPlayedStrategyPairs(game.getPlayer1());
        List<StrategyPair> secondPairs = getPlayedStrategyPairs(game.getPlayer2());
        firstPairs.retainAll(secondPairs);
        List<PureNashEquilibrium> nes = new ArrayList<>();
        firstPairs.forEach((pair) -> {
            nes.add(createNormalEquilibrium(pair));
        });
        return nes;
    } 
    
    private List<StrategyPair> getPlayedStrategyPairs(Player player) {
        List<Strategy> ownStrategies = player.getStrategies();
        List<Strategy> opponentStrategies = game.getOpponentStrategies(player);

        List<StrategyPair> playedStrategyPairs = new ArrayList<>();
        for (Strategy opponentStrategy : opponentStrategies) {
            float maximum = getMaximumPayoff(player, opponentStrategy);
            for (Strategy ownStrategy : ownStrategies) {
                float payoff = game.getPayoff(player, ownStrategy, opponentStrategy);
                if (payoff == maximum) {
                    if (player.equals(game.getPlayer1())) {
                        playedStrategyPairs.add(new StrategyPair(ownStrategy, opponentStrategy));
                    } else {
                        playedStrategyPairs.add(new StrategyPair(opponentStrategy, ownStrategy));
                    }
                }
            }
        }
        return playedStrategyPairs;
    }
    
    private float getMaximumPayoff(Player player, Strategy opponentStrategy) {
        List<Strategy> ownStrategies = player.getStrategies();
        List<Float> payoffs = new ArrayList<>();
        for (Strategy ownStrategy : ownStrategies) {
            payoffs.add(game.getPayoff(player, ownStrategy, opponentStrategy));
        }
        return Collections.max(payoffs);
    }
    
    private PureNashEquilibrium createNormalEquilibrium(StrategyPair strategyPair) {
        PureNashEquilibrium equilibrium = new PureNashEquilibrium(strategyPair.getStrategy1(), strategyPair.getStrategy2());
        NashEquilibriumType type = isStrict(equilibrium) ? NashEquilibriumType.Strict : NashEquilibriumType.NotStrict;
        equilibrium.setType(type);
        return equilibrium;
    }
    
    private boolean isStrict(PureNashEquilibrium equilibrium) {
        List<Float> firstPayoffs = game.getPayoffsForOpponentStrategy(game.getPlayer2(), equilibrium.getFirstStrat());
        float maxPayoff = Collections.max(firstPayoffs);
        if (Collections.frequency(firstPayoffs, maxPayoff) > 1) {
            return false;
        }
        List<Float> secondPayoffs = game.getPayoffsForOpponentStrategy(game.getPlayer1(), equilibrium.getSecondStrat());
        maxPayoff = Collections.max(secondPayoffs);
        if (Collections.frequency(secondPayoffs, maxPayoff) > 1) {
            return false;
        }
        return true;
    }
}
