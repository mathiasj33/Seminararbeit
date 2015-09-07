package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.softwarepage.facharbeit.normalgame.helpers.MathHelper;

public class MixedNashEquilibriumFinder implements Serializable {

    private final NormalGame game;
    private TreeMap<Strategy, Float> finalProbabilities;

    public MixedNashEquilibriumFinder(NormalGame game) {
        this.game = game;
    }

    public void clear() {
        if (finalProbabilities != null) {
            finalProbabilities.clear();
        }
    }

    public List<MixedNashEquilibrium> findMixedNashEquilibria() { //Hier auch eine Liste herausgeben - Alle NashGleichgewichte, bei Klick soll sich matrixdarstellung verändern (optimale strategie)
        List<MixedNashEquilibrium> equilibria = new ArrayList<>();
        if (finalProbabilities != null) {
            finalProbabilities.clear();
        }
        calculateProbabilities(game.getPlayer1());
        calculateProbabilities(game.getPlayer2());
        if (areProbabilitesValid(game.getPlayer1()) && areProbabilitesValid(game.getPlayer2())) {
            MixedNashEquilibrium mne = new MixedNashEquilibrium(copyFinalProbabilities());
            equilibria.add(mne);
        }
        if (getSubGames2x2().size() > 1) {
            for (NormalGame subGame : getSubGames2x2()) {
                System.out.println(subGame);
                if (isSubGameEquilibriumValid(subGame)) {
                    System.out.println("VALID");
                    MixedNashEquilibrium mne = new MixedNashEquilibrium(copyFinalProbabilities());
                    equilibria.add(mne);
                }
            }
        }
        if (equilibria.isEmpty()) {
            return null;
        }
        return equilibria; //testen etc.
    }

    private void calculateProbabilities(Player player) {
        HashMap<Strategy, Float> localProbabilities = calculateEvenProbabilities(player);
        if (finalProbabilities == null) {
            finalProbabilities = new TreeMap<>(new StrategyComparator(game));
        }
        for (int i = 0; i < 100000; i++) {  //100k because 100 = .001 * 100k
            for (Strategy strat : localProbabilities.keySet()) {
                List<Float> payoffs = getMixedPayoffsOfOtherPlayer(localProbabilities, player);
                float difference = getMaxDifference(payoffs);
                if (difference == 0.0f || difference > -.01f && difference < .01f) {
                    for (Strategy probStrat : localProbabilities.keySet()) {
                        finalProbabilities.put(probStrat, MathHelper.round(localProbabilities.get(probStrat), 1));
                    }
                    return;
                } else {
                    localProbabilities = increaseProbability(localProbabilities, strat, .001f);
                    payoffs = getMixedPayoffsOfOtherPlayer(localProbabilities, player);
                    if (getMaxDifference(payoffs) > difference) {
                        localProbabilities = increaseProbability(localProbabilities, strat, -.002f);
                    }
                }
            }
        }
    }

    private HashMap<Strategy, Float> increaseProbability(HashMap<Strategy, Float> localProbabilities, Strategy strat, float percentage) {
        localProbabilities.put(strat, localProbabilities.get(strat) + percentage);
        for (Strategy s : localProbabilities.keySet()) {
            if (strat.equals(s)) {
                continue;
            }
            localProbabilities.put(s, localProbabilities.get(s) - percentage / (float) (localProbabilities.keySet().size() - 1));
        }
        return localProbabilities;
    }

    private boolean areProbabilitesValid(Player player) {
        for (Strategy strategy : player.getStrategies()) {
            if (finalProbabilities.get(strategy) == null || finalProbabilities.get(strategy) < 0f || finalProbabilities.get(strategy) > 100f) {
                return false;
            }
        }
        return true;
    }

    private HashMap<Strategy, Float> calculateEvenProbabilities(Player player) {
        List<Strategy> ownStrats = player.getStrategies();
        HashMap<Strategy, Float> localProbabilities = new HashMap<>();
        for (Strategy ownStrat : ownStrats) {
            localProbabilities.put(ownStrat, (float) (1 / (float) ownStrats.size()) * 100);
        };
        return localProbabilities;
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
        final List<Strategy> firstStrategies = game.getPlayer1().getStrategies();
        final List<Strategy> secondStrategies = game.getPlayer2().getStrategies();
        int player1StrategiesIndex = 0;
        int player2StrategiesIndex = 0;
//        while (true) {
//            StrategyPair firstPlayerPair = new StrategyPair(firstStrategies.get(player1StrategiesIndex), firstStrategies.get(player1StrategiesIndex + 1));
//            StrategyPair secondPlayerPair = new StrategyPair(secondStrategies.get(player2StrategiesIndex), secondStrategies.get(player2StrategiesIndex + 1));
//            Player newFirstPlayer = createPlayer(firstPlayerPair);
//            Player newSecondPlayer = createPlayer(secondPlayerPair);
//
//            NormalGame newGame = new NormalGame(newFirstPlayer, newSecondPlayer);
//            add2x2Vectors(newGame, firstPlayerPair, secondPlayerPair);
//            games.add(newGame);
//
//            if (strategyIndexOutOfBounds(player1StrategiesIndex, firstStrategies.size())) {
//                if (strategyIndexOutOfBounds(player2StrategiesIndex, secondStrategies.size())) {
//                    break;
//                }
//                player1StrategiesIndex = 0;
//                player2StrategiesIndex++;
//            } else {
//                player1StrategiesIndex++;
//            }
//        }
        List<Strategy> player1StrategiesToRemove = new ArrayList<>();
        List<Strategy> player2StrategiesToRemove = new ArrayList<>();
        
        for(Strategy strat1 : game.getPlayer1().getStrategies()) {
            for(Strategy strat2 : game.getPlayer2().getStrategies()) {
                player2StrategiesToRemove.add(strat2);
                Player newPlayer1 = copyPlayerWithoutStrategies(game.getPlayer1(),player1StrategiesToRemove);
                Player newPlayer2 = copyPlayerWithoutStrategies(game.getPlayer2(), player2StrategiesToRemove);
                NormalGame subGame = new NormalGame(newPlayer1, newPlayer2);
                addVectorsToSubGame(subGame);
                player2StrategiesToRemove.clear();
            }
        }
        return games;
    }
    
    private void addVectorsToSubGame(NormalGame subGame) {
        for(Strategy s1 : subGame.getPlayer1().getStrategies()) {
            for(Strategy s2 : subGame.getPlayer2().getStrategies()) {
                StrategyPair pair = new StrategyPair(s1, s2);
                Strategy gameStrategy1 = game.getStrategy(s1.getName(), game.getPlayer1());
                Strategy gameStrategy2 = game.getStrategy(s2.getName(), game.getPlayer2());
                Vector payoff = new Vector(game.getPayoff(game.getPlayer1(), gameStrategy1, gameStrategy2), 
                        game.getPayoff(game.getPlayer2(), gameStrategy1, gameStrategy2));
                subGame.setVector(pair, payoff);
            }
        }
    }
    
    private Player copyPlayerWithoutStrategies(Player player, List<Strategy> strategiesToRemove) {
        List<Strategy> newStrategies = player.getStrategies().stream().
                filter(s -> !strategiesToRemove.contains(s)).collect(Collectors.toList());
        return new Player(newStrategies);
    }

    private Player createPlayer(StrategyPair pair) {
        Player player = new Player(pair.getStrategy1().getName(), pair.getStrategy2().getName());
        player.setName(game.getPlayer1().getName());;
        return player;
    }

    private void add2x2Vectors(NormalGame newGame, StrategyPair firstPlayerPair, StrategyPair secondPlayerPair) {
        newGame.addField(game.getVector(firstPlayerPair.getStrategy1(), secondPlayerPair.getStrategy1()));
        newGame.addField(game.getVector(firstPlayerPair.getStrategy1(), secondPlayerPair.getStrategy2()));
        newGame.addField(game.getVector(firstPlayerPair.getStrategy2(), secondPlayerPair.getStrategy1()));
        newGame.addField(game.getVector(firstPlayerPair.getStrategy2(), secondPlayerPair.getStrategy2()));
    }

    private boolean strategyIndexOutOfBounds(int index, int size) {
        return index + 1 == size - 1;
    }

    public boolean isSubGameEquilibriumValid(NormalGame subGame) {
        List<MixedNashEquilibrium> equilibria = subGame.findMixedNashEquilibria();
        if (equilibria == null) {
            return false;
        }
        MixedNashEquilibrium mne = equilibria.get(0);
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
            payoff = getMixedPayoff(firstStrategy.getName(), player).getFirst();
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

    public Vector getMixedPayoff(String strat, Player player) {
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

    public Vector getOptimalMixedPayoff() {
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

    private List<Float> getMixedPayoffsOfOtherPlayer(Map<Strategy, Float> localProbabilities, Player player) {
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
}
