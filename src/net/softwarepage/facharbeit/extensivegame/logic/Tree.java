package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.softwarepage.facharbeit.normalgame.logic.Vector;

public class Tree {  //Ein Spielbaum

    private final StrategyNode rootNode;

    public Tree(StrategyNode rootNode) {
        this.rootNode = rootNode;
    }

    public int getNumberOfLayers() {  //Gibt die Anzahl an Ebenen an. Bei einer Ebene gibt es immer nur Entscheidungsmöglichkeiten für einen Spieler. So ist Ebene 1 von Spieler A, Ebene 2 von Spieler B etc.
        Node deepestChild = getDeepestChild();
        return countNumberOfAncestors(deepestChild) + 1;
    }

    public Node getDeepestChild() {  //Gibt den tiefsten Knotenpunkt in der Baumstruktur zurück
        List<Node> deepChildren = rootNode.getDeepChildren();  //Alle Knotenpunkte sind in dieser Liste
        deepChildren.sort((Node t, Node t1) -> {  //Die Liste wird nach den Knoten sortiert, die am meisten übergeordnete haben
            if (countNumberOfAncestors(t) > countNumberOfAncestors(t1)) {
                return -1;
            } else if (countNumberOfAncestors(t) == countNumberOfAncestors(t1)) {
                return 0;
            }
            return 1;
        });
        return deepChildren.get(0);  //Der erste von diesen Knoten ist der tiefste Knotenpunkt in dem Baum
    }

    public List<Branch> getBranches() {
        return getBranches(rootNode);
    }

    public List<Branch> getBranches(StrategyNode root) {  //Gibt alle Äste ausgehend von einem Knotenpunkt zurück (vom Knotenpunkt bis zum Ende)
        List<Branch> branches = new ArrayList<>();
        for (Node node : getEndingNodes(root)) {
            List<Node> branch = new ArrayList<>();
            branch.add(node);
            while (!node.equals(root)) {  //Zuerst wird ein Liste von den Knotenpunkten von hinten nach vorne erstellt, bis man bei dem Knotenpunkt root angekommen ist
                node = node.getParent();
                branch.add(node);
            }
            Collections.reverse(branch);  //Die Liste wird umgekehrt und ist richtig geordnet
            List<NodeConnection> connections = new ArrayList<>();
            for (Node n : branch) {
                if (n instanceof VectorNode)
                    continue;
                StrategyNode sn = (StrategyNode) n;
                connections.add(sn.getConnectionTo(branch.get(branch.indexOf(sn) + 1)));  //Die Verbindungen von den Knotenpunkten untereinander wird letztendlich für den Ast verwendet
            }
            branches.add(new Branch(connections));
        }
        return branches;
    }

    public List<NodeConnection> getFirstPlayerFirstStrategies() {  //Die ersten Entscheidungsmöglichkeiten von Spieler A
        return rootNode.getAllConnections();
    }

    public int getLayer(Node child) {  //Gibt die Ebene eines bestimmten Knotenpunktes an
        return countNumberOfAncestors(child);
    }

    private List<VectorNode> getEndingNodes(StrategyNode node) { //Gibt die VectorNodes zurück, die mit einem Knotenpunkt verbunden sind
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

    public Vector getVector(ExtensiveStrategy strat1, ExtensiveStrategy strat2) {  //gibt einen Vektor für zwei Extensivstrategien zurück
        Node firstChild = strat1.getDecisions().get(0).getChild();
        if (firstChild instanceof VectorNode)
            return ((VectorNode) firstChild).getPayoff();
        StrategyNode currentNode = (StrategyNode) firstChild;
        int player = 2;
        Vector vector = null;
        while (vector == null) {
            ExtensiveStrategy lookupStrat = player == 2 ? strat2 : strat1;
            for (NodeConnection conn : lookupStrat.getDecisions()) {
                if (currentNode.getAllConnections().contains(conn)) {  //Alle Entscheidungen werden durchlaufen, bis die Entscheidung, die in der Strategie ist, erreicht wird
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

    private int countNumberOfAncestors(Node child) {  //Gibt die Anzahl an übergeordneten Knotenpunkten zurück
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
