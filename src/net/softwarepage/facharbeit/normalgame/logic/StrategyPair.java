package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.Objects;

public class StrategyPair implements Serializable {
    
    private Strategy strategy1;
    private Strategy strategy2;
    
    public StrategyPair(Strategy firstStrategy, Strategy secondStrategy) {
        this.strategy1 = firstStrategy;
        this.strategy2 = secondStrategy;
    }
    
    public boolean contains(Strategy strategy) {
        return getStrategy1().equals(strategy) || getStrategy2().equals(strategy);
    }
    
    @Override
    public String toString() {
        return strategy1 + " + " + strategy2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.strategy1.getUUID());
        hash += 89 * hash + Objects.hashCode(this.strategy2.getUUID());
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StrategyPair other = (StrategyPair) obj;
        if (!Objects.equals(this.strategy1.getUUID(), other.strategy1.getUUID())) {
            return false;
        }
        if (!Objects.equals(this.strategy2.getUUID(), other.strategy2.getUUID())) {
            return false;
        }
        return true;
    }
    

    public Strategy getStrategy1() {
        return strategy1;
    }

    public void setStrategy1(Strategy firstStrategy) {
        this.strategy1 = firstStrategy;
    }

    public Strategy getStrategy2() {
        return strategy2;
    }

    public void setStrategy2(Strategy secondStrategy) {
        this.strategy2 = secondStrategy;
    }
    
}
