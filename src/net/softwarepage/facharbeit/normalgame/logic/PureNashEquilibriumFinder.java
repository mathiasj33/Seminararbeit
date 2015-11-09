package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PureNashEquilibriumFinder implements Serializable {  //Bietet die Methode zum Finden eines Nash-Gleichgewichts in reinen Strategien an
    
    private final NormalGame game;

    public PureNashEquilibriumFinder(NormalGame game) {
        this.game = game;
    }
    
    public List<PureNashEquilibrium> findPureNashEquilibria() {
        List<StrategyPair> firstPairs = getPlayedStrategyPairs(game.getPlayer1());  //Eine Liste von den besten Antworten
        List<StrategyPair> secondPairs = getPlayedStrategyPairs(game.getPlayer2());
        firstPairs.retainAll(secondPairs);  //Wenn ein Strategienpaar in beiden Listen vorkommt
        List<PureNashEquilibrium> nes = new ArrayList<>();
        firstPairs.forEach((pair) -> {
            nes.add(createNormalEquilibrium(pair));  //Dann wird daraus ein Nash-Gleichgewicht erstellt
        });
        return nes;
    } 
    
    private List<StrategyPair> getPlayedStrategyPairs(Player player) {  //Gibt die besten Antworten zurück
        List<Strategy> ownStrategies = player.getStrategies();
        List<Strategy> opponentStrategies = game.getOpponentStrategies(player);

        List<StrategyPair> playedStrategyPairs = new ArrayList<>();
        for (Strategy opponentStrategy : opponentStrategies) {
            float maximum = getMaximumPayoff(player, opponentStrategy);
            for (Strategy ownStrategy : ownStrategies) {
                float payoff = game.getPayoff(player, ownStrategy, opponentStrategy);
                if (Math.abs(payoff - maximum) < 0.0001f) {  //Wenn diese Strategie die höchste Auszahlung hat
                    if (player.equals(game.getPlayer1())) {
                        playedStrategyPairs.add(new StrategyPair(ownStrategy, opponentStrategy));   //Die Strategie mit der höchsten Auszahlung ist die beste Antwort und wird hinzugefügt
                    } else {
                        playedStrategyPairs.add(new StrategyPair(opponentStrategy, ownStrategy));
                    }
                }
            }
        }
        return playedStrategyPairs;
    }
    
    private float getMaximumPayoff(Player player, Strategy opponentStrategy) {  //Gibt die höchste Auszahlung für eine gegnerische Strategie zurück
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
        if (Collections.frequency(firstPayoffs, maxPayoff) > 1) {  //Wenn die höchste Auszahlung auch vorkommt, wenn das Nash-Gleichgewicht von einem Spieler nicht gespielt wird, so ist es nicht strikt
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
