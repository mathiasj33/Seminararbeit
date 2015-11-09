package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.softwarepage.facharbeit.normalgame.helpers.ListHelper;

public class Player implements Serializable {

    private String name;
    private List<Strategy> strategies;

    public Player(String... strategies) {
        List<String> names = Arrays.asList(strategies);
        if (ListHelper.isElementTwice(names)) {
            throw new IllegalArgumentException("Same strategy twice!");
        }
        this.strategies = names.stream().map(n -> new Strategy(n)).collect(Collectors.toList());
    }
    
    public Player(List<Strategy> strategies) {
        List<String> names = strategies.stream().map(s -> s.getName()).collect(Collectors.toList());  //Keine Strategie darf den selben Namen wie eine andere Strategie desselben Spielers haben
        if(ListHelper.isElementTwice(names)) {
            throw new IllegalArgumentException("Same strategy twice!");
        }
        this.strategies = strategies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Strategy> getStrategies() {
        return Collections.unmodifiableList(strategies);
    }

    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }

    public void addStrategy(Strategy strategy) {
        strategies.add(strategy);
        testStrategyNames(strategy);
    }

    public void testStrategyNames(Strategy newStrat) {
        List<String> names = createStrategyNameList(strategies);
        if (ListHelper.isElementTwice(names)) {
            removeStrategy(newStrat);
            throw new IllegalArgumentException("Same strategy twice!");
        }
    }

    private static List<String> createStrategyNameList(List<Strategy> strategies) {
        List<String> names = new ArrayList<>();
        strategies.forEach(strat -> {
            names.add(strat.getName());
        });
        return names;
    }

    public void removeStrategy(Strategy strategy) {
        strategies.remove(strategy);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.strategies);
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
        final Player other = (Player) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.strategies, other.strategies)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}
