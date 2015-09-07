package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Strategy implements Serializable {

    private final UUID uuid;
    private String name;
    private StrategyType type;

    public Strategy(String name) {
        this.name = name;
        type = StrategyType.Normal;
        uuid = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.uuid);
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
        final Strategy other = (Strategy) obj;
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }
    
    protected void setName(String name) {
        this.name = name;
    }

    public StrategyType getType() {
        return type;
    }

    public void setType(StrategyType type) {
        this.type = type;
    }
    
    public UUID getUUID() {
        return uuid;
    }
}
