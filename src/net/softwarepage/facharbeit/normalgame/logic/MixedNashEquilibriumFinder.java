package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.softwarepage.facharbeit.normalgame.helpers.ListHelper;
import net.softwarepage.facharbeit.normalgame.helpers.MathHelper;

public class MixedNashEquilibriumFinder implements Serializable {

    private final NormalGame game;
    private Map<Strategy, Float> localProbabilities;
    private Map<Strategy, Float> finalProbabilities;
    private Map<Strategy, Float> toIncreaseMap;

    public MixedNashEquilibriumFinder(NormalGame game) {
        this.game = game;
    }

    public void clear() {
        if (finalProbabilities != null) {
            finalProbabilities.clear();
        }
    }

    public List<MixedNashEquilibrium> findMixedNashEquilibria() {
        List<MixedNashEquilibrium> equilibria = new ArrayList<>();
        MixedNashEquilibrium mne = findDirectMixedNashEquilibrium();
        if (mne != null) {
            equilibria.add(mne);
        }
        List<NormalGame> subGames = getSubGames();
        if (subGames.size() > 1) {
            for (NormalGame subGame : subGames) {
                if (isSubGameEquilibriumValid(subGame)) {
                    equilibria.add(new MixedNashEquilibrium(copyFinalProbabilities()));
                }
            }
        }
        if (equilibria.isEmpty()) {
            return null;
        }
        return equilibria;
    }

    public MixedNashEquilibrium findDirectMixedNashEquilibrium() {
        if (finalProbabilities != null) {
            finalProbabilities.clear();
        }
        calculateProbabilities(game.getPlayer1());
        calculateProbabilities(game.getPlayer2());
        if (areProbabilitesValid(game.getPlayer1()) && areProbabilitesValid(game.getPlayer2())) {
            MixedNashEquilibrium mne = new MixedNashEquilibrium(copyFinalProbabilities());
            return mne;
        }
        return null;
    }

    private void calculateProbabilities(Player player) {
        calculateEvenProbabilities(player);
        if (finalProbabilities == null) {
            finalProbabilities = new TreeMap<>(new StrategyComparator(game));
        }
        toIncreaseMap = new HashMap<>();
        for (Strategy strat : player.getStrategies()) {
            toIncreaseMap.put(strat, 1f);
        }
        float lastDifference = 0;
        float threshold = .01f;
        for (int i = 0; i < 100000; i++) {  //100k because 100 = .001 * 100k
            float difference = getMaxDifferenceOfOtherPlayer(player);
            if (Math.abs(difference - lastDifference) < 0.00001f) {  //difference == lastdifference
                if (Math.abs(threshold - .1f) < 0.00001f)  //threshold == .1f
                    return;
                threshold = .1f;
            }
            lastDifference = difference;
            for (Strategy strat : localProbabilities.keySet()) {
                difference = getMaxDifferenceOfOtherPlayer(player);
                if (difference > -threshold && difference < threshold) {
                    for (Strategy probStrat : localProbabilities.keySet()) {
                        finalProbabilities.put(probStrat, MathHelper.round(localProbabilities.get(probStrat), 1));
                    }
                    return;
                } else {
                    improveProbability(player, strat);
                }
            }
        }
    }

    private void improveProbability(Player player, Strategy strat) {
        float difference = getMaxDifferenceOfOtherPlayer(player);
        float toIncrease = toIncreaseMap.get(strat);
        increaseProbability(strat, toIncrease);
        if (getMaxDifferenceOfOtherPlayer(player) > difference) {
            increaseProbability(strat, -toIncrease * 2);
            if (getMaxDifferenceOfOtherPlayer(player) > difference) {
                increaseProbability(strat, toIncrease);
                if (Math.abs(toIncrease - 1) < 0.001f) {
                    toIncreaseMap.put(strat, .001f);
                    improveProbability(player, strat);
                }
            }
        }
    }

    private void increaseProbability(Strategy strat, float percentage) {
        localProbabilities.put(strat, localProbabilities.get(strat) + percentage);
        for (Strategy s : localProbabilities.keySet()) {
            if (strat.equals(s)) {
                continue;
            }
            localProbabilities.put(s, localProbabilities.get(s) - percentage / (float) (localProbabilities.keySet().size() - 1));
        }
    }

    private boolean areProbabilitesValid(Player player) {
        for (Strategy strategy : player.getStrategies()) {
            if (finalProbabilities.get(strategy) == null || finalProbabilities.get(strategy) < 0f || finalProbabilities.get(strategy) > 100f) {
                return false;
            }
        }
        return true;
    }

    private void calculateEvenProbabilities(Player player) {
        List<Strategy> ownStrats = player.getStrategies();
        localProbabilities = new HashMap<>();
        for (Strategy ownStrat : ownStrats) {
            localProbabilities.put(ownStrat, (float) (1 / (float) ownStrats.size()) * 100);
        };
    }

    private float getMaxDifference(List<Float> floats) {
        List<Float> differences = new ArrayList<>();
        for (int i = 0; i < floats.size(); i++) {
            for (int j = 0; j < floats.size(); j++) {
                differences.add(Math.abs(floats.get(i) - floats.get(j)));
            }
        }
        return Collections.max(differences);
    }

    private Map<Strategy, Float> copyFinalProbabilities() {
        Map<Strategy, Float> newMap = new TreeMap<>(new StrategyComparator(game));
        for (Strategy strat : finalProbabilities.keySet()) {
            newMap.put(strat, finalProbabilities.get(strat));
        }
        return newMap;
    }

    public List<NormalGame> getSubGames() {
        List<NormalGame> games = new ArrayList<>();
        for (List<Strategy> list : ListHelper.powerSet(game.getPlayer1().getStrategies())) {
            if (list.isEmpty() || list.size() < 2) {
                continue;
            }
            for (List<Strategy> list2 : ListHelper.powerSet(game.getPlayer2().getStrategies())) {
                if (list2.isEmpty() || list2.size() < 2) {
                    continue;
                }
                if (list.size() == game.getPlayer1().getStrategies().size() && list2.size() == game.getPlayer2().getStrategies().size()) {
                    continue;
                }
                Player newPlayer1 = copyPlayerWithStrategies(game.getPlayer1(), list);
                Player newPlayer2 = copyPlayerWithStrategies(game.getPlayer2(), list2);

                NormalGame newGame = new NormalGame(newPlayer1, newPlayer2);
                addSubGameVectors(newGame, newPlayer1, newPlayer2);
                games.add(newGame);
            }
        }
        return games;
    }

    private Player copyPlayerWithStrategies(Player player, List<Strategy> strategies) {
        Player newPlayer = new Player(strategies);
        newPlayer.setName(player.getName());
        return newPlayer;
    }

    private void addSubGameVectors(NormalGame newGame, Player player1, Player player2) {
        player1.getStrategies().forEach(s1 -> {
            player2.getStrategies().forEach(s2 -> {
                newGame.setVector(new StrategyPair(s1, s2), game.getVector(s1, s2));
            });
        });
    }

    public boolean isSubGameEquilibriumValid(NormalGame subGame) {
        MixedNashEquilibrium mne = subGame.findDirectMixedNashEquilibrium();
        if (mne == null) {
            return false;
        }
        setProbabilitiesToZero();
        copyProbabilities(subGame, mne);
        if (betterPayoffThanSubGamePayoffExists(subGame, game.getPlayer1()) || betterPayoffThanSubGamePayoffExists(subGame, game.getPlayer2())) {
            return false;
        }
        return true;
    }

    private void setProbabilitiesToZero() {
        game.getPlayer1().getStrategies().forEach(s -> {
            finalProbabilities.put(s, 0f);
        });
        game.getPlayer2().getStrategies().forEach(s -> {
            finalProbabilities.put(s, 0f);
        });
    }

    private void copyProbabilities(NormalGame subGame, MixedNashEquilibrium mne) {
        for (Strategy strat : mne.getProbabilities().keySet()) {
            Player player = subGame.getPlayer1().getStrategies().contains(strat) ? game.getPlayer1() : game.getPlayer2();
            finalProbabilities.put(game.getStrategy(strat.getName(), player), mne.getProbabilities().get(strat));
        }
    }

    private boolean betterPayoffThanSubGamePayoffExists(NormalGame subGame, Player player) {
        Player subGamePlayer = player.equals(game.getPlayer1()) ? subGame.getPlayer1() : subGame.getPlayer2();
        Strategy firstStrategy = subGamePlayer.getStrategies().get(0);
        float payoff;
        if (player.equals(game.getPlayer1())) {
            payoff = getMixedPayoff(firstStrategy.getName(), player).getFirst();  //In dem gem. NGG sind die Spieler zwischen ihren Strategien indifferent!! -> Auszahlung von irgendeiner NGG strategie muss immer größer sein als ASuzahlung von p=0
        } else {
            payoff = getMixedPayoff(firstStrategy.getName(), player).getSecond();
        }

        for (Strategy strat : player.getStrategies()) {
            if (subGame.getStrategy(strat.getName(), subGamePlayer) != null) {
                continue;
            }
            float stratPayoff;
            if (player.equals(game.getPlayer1())) {
                stratPayoff = getMixedPayoff(strat.getName(), player).getFirst();
            } else {
                stratPayoff = getMixedPayoff(strat.getName(), player).getSecond();
            }
            if (stratPayoff > payoff) {
                return true;
            }
        }
        return false;
    }

    public Vector getMixedPayoff(MixedNashEquilibrium mne, String strat, Player player) {
        finalProbabilities = mne.getProbabilities();
        return getMixedPayoff(strat, player);
    }

    private Vector getMixedPayoff(String strat, Player player) {
        Strategy strategy = game.getStrategy(strat, player);
        Player otherPlayer = player.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
        float aPayoff = 0;
        float bPayoff = 0;
        for (Strategy otherStrat : otherPlayer.getStrategies()) {
            aPayoff += game.getPayoff(game.getPlayer1(), strategy, otherStrat) * finalProbabilities.get(otherStrat) / 100;
            bPayoff += game.getPayoff(game.getPlayer2(), strategy, otherStrat) * finalProbabilities.get(otherStrat) / 100;
        }
        aPayoff = MathHelper.round(aPayoff, 2);
        bPayoff = MathHelper.round(bPayoff, 2);
        return new Vector(aPayoff, bPayoff);
    }

    public Vector getOptimalMixedPayoff(MixedNashEquilibrium mne) {
        finalProbabilities = mne.getProbabilities();
        return new Vector(getOptimalMixedPayoff(game.getPlayer1()), getOptimalMixedPayoff(game.getPlayer2()));
    }

    private float getOptimalMixedPayoff(Player player) {
        if (finalProbabilities == null) {
            return 0;
        }
        List<Strategy> ownStrats = player.getStrategies();
        List<Strategy> otherStrats = game.getOpponentStrategies(player);
        float payoff = 0;
        for (Strategy otherStrat : otherStrats) {
            for (Strategy ownStrat : ownStrats) {
                float otherProbability = (finalProbabilities.get(otherStrat) / 100);
                float ownProbability = (finalProbabilities.get(ownStrat) / 100);
                payoff += otherProbability * ownProbability * game.getPayoff(player, ownStrat, otherStrat);
            }
        }
        return MathHelper.round(payoff, 2);
    }

    private List<Float> getMixedPayoffsOfOtherPlayer(Player player) {
        List<Strategy> ownStrategies = player.getStrategies();
        List<Strategy> otherStrategies = game.getOpponentStrategies(player);
        List<Float> payoffs = new ArrayList<>();
        for (Strategy otherStrategy : otherStrategies) {
            float payoff = 0;
            for (Strategy ownStrategy : ownStrategies) {
                if (player.equals(game.getPlayer1())) {
                    payoff += (game.getVector(ownStrategy, otherStrategy).getSecond()) * localProbabilities.get(ownStrategy);
                } else {
                    payoff += (game.getVector(ownStrategy, otherStrategy).getFirst()) * localProbabilities.get(ownStrategy);
                }
            }
            payoffs.add(payoff);
        }
        return payoffs;
    }

    private float getMaxDifferenceOfOtherPlayer(Player player) {
        return getMaxDifference(getMixedPayoffsOfOtherPlayer(player));
    }
}
