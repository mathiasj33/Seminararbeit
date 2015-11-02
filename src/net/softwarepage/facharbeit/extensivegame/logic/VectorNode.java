package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.Objects;
import net.softwarepage.facharbeit.normalgame.logic.Vector;

public class VectorNode extends Node {
    private Vector payoff;

    public VectorNode(float first, float second) {
        super();
        this.payoff = new Vector(first, second);
    }

    public Vector getPayoff() {
        return payoff;
    }

    public void setPayoff(Vector payoff) {
        this.payoff = payoff;
    }
}
