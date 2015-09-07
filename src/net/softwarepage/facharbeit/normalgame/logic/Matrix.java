package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.HashMap;

public class Matrix implements Serializable {

    private final Player player1;
    private final Player player2;
    private final HashMap<StrategyPair, Vector> vectors = new HashMap<>();
    private int horizontalPosition = -1;
    private int verticalPosition = 0;

    public Matrix(Player firstPlayer, Player secondPlayer) {
        this.player1 = firstPlayer;
        this.player2 = secondPlayer;
    }

    public void addField(Vector payoff) {
        incrementPositions();
        vectors.put(new StrategyPair(player1.getStrategies().get(verticalPosition), player2.getStrategies().get(horizontalPosition)), payoff);
    }

    public void setVector(StrategyPair pair, Vector vector) {
        vectors.put(pair, vector);
    }

    public void removeVector(StrategyPair pair) {
        vectors.remove(pair);
    }

    private void incrementPositions() {
        int firstSize = player1.getStrategies().size();
        int secondSize = player2.getStrategies().size();
        if (horizontalPosition + 1 < secondSize) {
            horizontalPosition += 1;
        } else if (verticalPosition + 1 < firstSize) {
            horizontalPosition = 0;
            verticalPosition += 1;
        }
    }

    public void clear() {
        resetPositions();
        vectors.clear();
    }

    private void resetPositions() {
        horizontalPosition = -1;
        verticalPosition = 0;
    }

    public Vector getVector(Strategy firstStrat, Strategy secondStrat) {
        if (player1.getStrategies().contains(firstStrat)) {
            return vectors.get(new StrategyPair(firstStrat, secondStrat));
        } else {
            return vectors.get(new StrategyPair(secondStrat, firstStrat));
        }
    }

    public Strategy getStrategy(String name, Player player) {
        for (Strategy strategy : player.getStrategies()) {
            if (strategy.getName().equals(name)) {
                return strategy;
            }
        }
        return null;
    }

    public HashMap<StrategyPair, Vector> getVectors() {
        return vectors;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
    
    @Override
    public String toString() {
        String s = "";
        for (Strategy fStrat : player1.getStrategies()) {
            for (Strategy sStrat : player2.getStrategies()) {
                s += player1.getName() + ": " + fStrat + "; " + player2.getName() + ": " + sStrat + " -> ";
                s += getVector(fStrat, sStrat) + " ";
            }
        }
        return s;
    }
}
