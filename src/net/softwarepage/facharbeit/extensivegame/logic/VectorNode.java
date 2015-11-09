package net.softwarepage.facharbeit.extensivegame.logic;

import net.softwarepage.facharbeit.normalgame.logic.Vector;

public class VectorNode extends Node {  //Ein Knotenpunkt, der ein Endpunkt ist
    private Vector payoff;

    public VectorNode(Vector payoff) {
        this.payoff = payoff;
    }
    
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
