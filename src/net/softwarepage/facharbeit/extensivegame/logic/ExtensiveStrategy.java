package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExtensiveStrategy {
    private final List<NodeConnection> decisions = new ArrayList<>();
    
    public ExtensiveStrategy(NodeConnection... decisions) {
        this.decisions.addAll(Arrays.asList(decisions));
    }

    public ExtensiveStrategy() {
    }
    
    public void addDecision(NodeConnection decision) {
        decisions.add(decision);
    }
    
    public void addDecisions(List<NodeConnection> decisions) {
        this.decisions.addAll(decisions);
    }

    public List<NodeConnection> getDecisions() {
        return decisions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.decisions);
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
        final ExtensiveStrategy other = (ExtensiveStrategy) obj;
        if (!Objects.equals(this.decisions, other.decisions)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return decisions.toString();
    }
}
