package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.ArrayList;
import java.util.List;

public class StrategyNode extends Node {

    private final List<NodeConnection> connections = new ArrayList<>();

    public StrategyNode(String name) {
        super();
        this.name = name;
    }

    public void addStrategy(String name, Node child) {
        child.setParent(this);
        connections.add(new NodeConnection(name, this, child));
    }

    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        connections.forEach(nc -> children.add(nc.getChild()));
        return children;
    }

    public List<Node> getDeepChildren() {
        final List<Node> deepChildren = new ArrayList<>();
        List<Node> directChildren = getChildren();
        deepChildren.addAll(directChildren);
        directChildren.forEach(c -> {
            if(c instanceof StrategyNode) {
                deepChildren.addAll(((StrategyNode) c).getDeepChildren());
            }
        });
        return deepChildren;
    }

    public NodeConnection getConnectionTo(Node toNode) {
        for(NodeConnection connection : connections) {
            if(connection.getChild().equals(toNode)) {
                return connection;
            }
        }
        return null;
    }
    
    public List<NodeConnection> getAllConnections() {
        return connections;
    }
}
