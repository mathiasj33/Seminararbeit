package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.softwarepage.facharbeit.normalgame.logic.Vector;

public class Tree {

    private final StrategyNode rootNode;

    public Tree(StrategyNode rootNode) {
        this.rootNode = rootNode;
    }

    public int getNumberOfLayers() {
        Node deepestChild = getDeepestChild();
        return countNumberOfAncestors(deepestChild) + 1;
    }

    public Node getDeepestChild() {
        List<Node> deepChildren = rootNode.getDeepChildren();
        deepChildren.sort((Node t, Node t1) -> {
            if (countNumberOfAncestors(t) > countNumberOfAncestors(t1)) {
                return -1;
            } else if (countNumberOfAncestors(t) == countNumberOfAncestors(t1)) {
                return 0;
            }
            return 1;
        });
        return deepChildren.get(0);
    }

    public List<Branch> getBranches() {
        return getBranches(rootNode);
    }

    public List<Branch> getBranches(StrategyNode root) {
        List<Branch> branches = new ArrayList<>();
        for (Node node : getEndingNodes(root)) {
            List<Node> branch = new ArrayList<>();
            branch.add(node);
            while (node.getParent() != null) {
                node = node.getParent();
                branch.add(node);
            }
            Collections.reverse(branch);
            List<NodeConnection> connections = new ArrayList<>();
            for (Node n : branch) {
                if (n instanceof VectorNode)
                    continue;
                StrategyNode sn = (StrategyNode) n;
                connections.add(sn.getConnectionTo(branch.get(branch.indexOf(sn) + 1)));
            }
            branches.add(new Branch(connections));
        }
        return branches;
    }

    public List<NodeConnection> getFirstPlayerFirstStrategies() {
        return rootNode.getAllConnections();
    }

    public int getLayer(Node child) {
        return countNumberOfAncestors(child);
    }

    private List<VectorNode> getEndingNodes(StrategyNode node) {
        List<VectorNode> nodes = new ArrayList<>();
        node.getDeepChildren().forEach(c -> {
            if (c instanceof VectorNode) {
                nodes.add((VectorNode) c);
            }
        });
        return nodes;
    }

    public boolean isPlayer1Layer(int layer) {
        return layer % 2 == 0;
    }

    public Vector getVector(ExtensiveStrategy strat1, ExtensiveStrategy strat2) {
        if (strat1.getDecisions().get(0).getChild() instanceof VectorNode)
            return ((VectorNode) strat1.getDecisions().get(0).getChild()).getPayoff();
        StrategyNode currentNode = (StrategyNode) strat1.getDecisions().get(0).getChild();
        int player = 2;
        Vector vector = null;
        while (vector == null) {
            ExtensiveStrategy lookupStrat = player == 2 ? strat2 : strat1;
            for (NodeConnection conn : lookupStrat.getDecisions()) {
                if (currentNode.getAllConnections().contains(conn)) {
                    Node child = conn.getChild();
                    if (child instanceof VectorNode)
                        return ((VectorNode) child).getPayoff();
                    else
                        currentNode = (StrategyNode) child;
                }
            }
            if (player == 1)
                player = 2;
            else
                player = 1;
        }
        return null;
    }

    private int countNumberOfAncestors(Node child) {
        int numberOfParents = 0;
        StrategyNode parent = child.getParent();
        while (parent != null) {
            numberOfParents++;
            parent = parent.getParent();
        }
        return numberOfParents;
    }

    public StrategyNode getRootNode() {
        return rootNode;
    }
}
