package net.softwarepage.facharbeit.normalgame.logic;

public class PureNashEquilibrium implements NashEquilibrium {
    
    private NashEquilibriumType type;
    private final Strategy firstStrat;
    private final Strategy secondStrat;

    public PureNashEquilibrium(Strategy firstStrat, Strategy secondStrat) {
        this.firstStrat = firstStrat;
        this.secondStrat = secondStrat;
    }
    
    @Override
    public String toString() {
        String typeString;
        if(type == NashEquilibriumType.NotStrict) {
            typeString = "Nicht striktes Nash-Gleichgwicht";
        } else {
            typeString =  "Striktes Nash-Gleichgewicht";
        }
        return "A: " + firstStrat + ", B: " + secondStrat + "; " + typeString;
    }

    public NashEquilibriumType getType() {
        return type;
    }

    public void setType(NashEquilibriumType type) {
        this.type = type;
    }

    public Strategy getFirstStrat() {
        return firstStrat;
    }

    public Strategy getSecondStrat() {
        return secondStrat;
    }
}
