package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NormalGame implements Serializable {

    private final Matrix matrix;
    private final DominantStrategyFinder dominantStrategyFinder = new DominantStrategyFinder(this);
    private final PureNashEquilibriumFinder pureNashFinder = new PureNashEquilibriumFinder(this);
    private final MixedNashEquilibriumFinder mixedNashFinder = new MixedNashEquilibriumFinder(this);

    public NormalGame(Player player1, Player player2) {
        matrix = new Matrix(player1, player2);
    }

    public NormalGame() {
        Player player1 = new Player("oben", "unten");
        Player player2 = new Player("links", "rechts");
        player1.setName("Spieler A");
        player2.setName("Spieler B");
        matrix = new Matrix(player1, player2);
        initDefaultVectors();
    }

    private void initDefaultVectors() {
        for (int i = 0; i < 4; i++) {
            matrix.addField(new Vector(0, 0));
        }
    }

    public void addStrategy(String name, Player player) {
        List<Strategy> opponentStrategies = getOpponentStrategies(player);
        Strategy newStrategy = new Strategy(name);
        player.addStrategy(newStrategy);
        for (Strategy opponentStrategy : opponentStrategies) {
            StrategyPair pair;
            if (player.equals(matrix.getPlayer1())) {
                pair = new StrategyPair(newStrategy, opponentStrategy);
            } else {
                pair = new StrategyPair(opponentStrategy, newStrategy);
            }
            matrix.setVector(pair, new Vector(0, 0));
        }
    }

    public void removeStrategy(String name, Player player) {
        if (player.getStrategies().size() == 1) {
            throw new IllegalStateException("Trying to remove last strategy from a player");
        }
        Strategy strategy = getStrategy(name, player);
        player.removeStrategy(strategy);
        List<StrategyPair> toRemove = new ArrayList<>();
        for (StrategyPair pair : matrix.getVectors().keySet()) {
            if (pair.contains(strategy)) {
                toRemove.add(pair);
            }
        }
        for (StrategyPair pair : toRemove) {
            matrix.removeVector(pair);
        }
    }

    public void renameStrategy(String oldName, String newName, Player player) {
        Strategy strategy = getStrategy(oldName, player);
        strategy.setName(newName);
        try {
            player.testStrategyNames(strategy);
        } catch (IllegalArgumentException e) {
            strategy.setName(oldName);
            throw new IllegalArgumentException("Same strategy twice!");
        }
    }

    public void addField(Vector payoff) {
        matrix.addField(payoff);
    }

    public void setVector(StrategyPair pair, Vector vector) {
        matrix.setVector(pair, vector);
    }

    public void clear() {
        matrix.clear();
        mixedNashFinder.clear();
    }

    public Vector getVector(Strategy firstStrategy, Strategy secondStrategy) {
        return matrix.getVector(firstStrategy, secondStrategy);
    }

    public Strategy getStrategy(String name, Player player) {
        return matrix.getStrategy(name, player);
    }

    public List<Strategy> getOpponentStrategies(Player player) {
        if (player.equals(matrix.getPlayer1())) {
            return matrix.getPlayer2().getStrategies();
        } else {
            return matrix.getPlayer1().getStrategies();
        }
    }

    public boolean isZeroSumGame() {
        for (Vector vector : matrix.getVectors().values()) {
            if (vector.getFirst() + vector.getSecond() != 0) {
                return false;
            }
        }
        return true;
    }

    public float getPayoff(Player player, Strategy strategy1, Strategy strategy2) {
        Vector payoff = matrix.getVector(strategy1, strategy2);
        if (player.equals(matrix.getPlayer1())) {
            return payoff.getFirst();
        } else {
            return payoff.getSecond();
        }
    }
    
    public List<Float> getPayoffsForOpponentStrategy(Player player, Strategy opponentStrategy) {
        List<Strategy> ownStrats = player.getStrategies();
        List<Float> values = new ArrayList<>();
        ownStrats.forEach((ownStrat) -> {
            values.add(getPayoff(player, ownStrat, opponentStrategy));
        });
        return values;
    }

    public Player getPlayer1() {
        return matrix.getPlayer1();
    }

    public Player getPlayer2() {
        return matrix.getPlayer2();
    }

    public Map<StrategyPair, Vector> getVectors() {
        return matrix.getVectors();
    }
    
    public List<Strategy> findDominatedStrategies(Player player) {
        return dominantStrategyFinder.findDominatedStrategy(player);
    }
    
    public List<PureNashEquilibrium> findPureNashEquilibria() {
        return pureNashFinder.findPureNashEquilibria();
    }
    
    public MixedNashEquilibrium findDirectMixedNashEquilibrium() {
        return mixedNashFinder.findDirectMixedNashEquilibrium();
    }

    public List<MixedNashEquilibrium> findMixedNashEquilibria() {
        return mixedNashFinder.findMixedNashEquilibria();
    }
    
    public List<NormalGame> getSubGames() {
        return mixedNashFinder.getSubGames();
    }
    
    public boolean isSubGameEquilibriumValid(NormalGame subGame) {
        return mixedNashFinder.isSubGameEquilibriumValid(subGame);
    }

    public Vector getMixedPayoff(String strat, Player player) {
        return mixedNashFinder.getMixedPayoff(strat, player);
    }

    public Vector getOptimalMixedPayoff(MixedNashEquilibrium mne) {
        return mixedNashFinder.getOptimalMixedPayoff(mne);
    }

    public void print() {
        System.out.println(matrix);
    }
    
    @Override
    public String toString() {
        return matrix.toString();
    }
}
