package net.softwarepage.facharbeit.normalgame.logic;

import java.util.List;
import java.util.Map;

public class MixedNashEquilibrium implements NashEquilibrium {  //Ein Nash-Gleichgewicht in gemischten Strategien

    private final Map<Strategy, Float> probabilities;

    public MixedNashEquilibrium(Map<Strategy, Float> probabilities) {
        this.probabilities = probabilities;
    }

    public Map<Strategy, Float> getProbabilities() {
        return probabilities;
    }
    
    public Float getProbability(String strategyName, Player player) {
        List<Strategy> ownStrategies = player.getStrategies();
        for (Strategy strat : ownStrategies) {
            if (strat.getName().equals(strategyName)) {
                return probabilities.get(strat);
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return probabilities.toString();
    }
}
