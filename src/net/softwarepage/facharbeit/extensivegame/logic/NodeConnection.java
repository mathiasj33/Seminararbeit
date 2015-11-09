package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.Objects;

public class NodeConnection {  //Eine Verbindung zweier Knotenpunkte
    private final String name;
    private final StrategyNode parent;
    private final Node child;

    public NodeConnection(String name, StrategyNode parent, Node child) {
        this.name = name;
        this.parent = parent;
        this.child = child;
    }

    public String getName() {
        return name;
    }

    public StrategyNode getParent() {
        return parent;
    }

    public Node getChild() {
        return child;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.parent);
        hash = 97 * hash + Objects.hashCode(this.child);
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
        final NodeConnection other = (NodeConnection) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.parent, other.parent)) {
            return false;
        }
        if (!Objects.equals(this.child, other.child)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
