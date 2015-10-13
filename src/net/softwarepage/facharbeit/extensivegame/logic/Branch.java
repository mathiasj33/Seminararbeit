package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.List;

public class Branch {
    private List<NodeConnection> connections;

    public Branch(List<NodeConnection> connections) {
        this.connections = connections;
    }
    
    public List<NodeConnection> getConnections() {
        return connections;
    }

    @Override
    public String toString() {
        return connections.toString();
    }
}
