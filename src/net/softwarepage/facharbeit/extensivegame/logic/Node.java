package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.Objects;
import java.util.UUID;

public abstract class Node {
    protected String name;
    protected StrategyNode parent;
    private UUID uuid;
    
    public Node() {
        uuid = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StrategyNode getParent() {
        return parent;
    }

    protected void setParent(StrategyNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Node{" + "name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.uuid);
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
        final Node other = (Node) obj;
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        return true;
    }
}
