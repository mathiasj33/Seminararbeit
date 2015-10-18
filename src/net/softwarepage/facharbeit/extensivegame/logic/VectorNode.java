package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.Objects;
import net.softwarepage.facharbeit.normalgame.logic.Vector;

public class VectorNode extends Node {
    private Vector payoff;

    public VectorNode(Vector payoff) {
        super();
        this.payoff = payoff;
    }

    public Vector getPayoff() {
        return payoff;
    }

    public void setPayoff(Vector payoff) {
        this.payoff = payoff;
    }
}
