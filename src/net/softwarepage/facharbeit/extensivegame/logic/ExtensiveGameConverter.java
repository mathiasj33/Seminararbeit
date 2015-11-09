package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.softwarepage.facharbeit.normalgame.helpers.ListHelper;
import net.softwarepage.facharbeit.normalgame.logic.NormalGame;
import net.softwarepage.facharbeit.normalgame.logic.Player;
import net.softwarepage.facharbeit.normalgame.logic.Strategy;
import net.softwarepage.facharbeit.normalgame.logic.StrategyPair;



public class ExtensiveGameConverter {  //Die eigentliche Klasse zum konvertieren

    private Tree tree;
    private List<List<Branch>> separatedBranches = new ArrayList<>();

    public NormalGame convertToNormalGame(Tree tree) {
        this.tree = tree;

        List<ExtensiveStrategy> player1Strategies = getPlayer1Strategies();  //Die Strategien von beiden Spieler erstellen
        List<ExtensiveStrategy> player2Strategies = getPlayer2Strategies();

        List<Strategy> player1NormalStrategies = convertExtensiveStrategiesToNormalForm(player1Strategies);  //Strategien in Normalform umwandeln
        List<Strategy> player2NormalStrategies = convertExtensiveStrategiesToNormalForm(player2Strategies);
        removeDoubledStrategies(player2NormalStrategies);

        Player player1 = new Player(player1NormalStrategies);  //Spieler mit Strategien erstellen
        player1.setName("Player1");
        Player player2 = new Player(player2NormalStrategies);
        player2.setName("Player2");

        NormalGame game = new NormalGame(player1, player2);  //Spiel erstellen
        for (ExtensiveStrategy player1Strat : player1Strategies) {
            for (ExtensiveStrategy player2Strat : player2Strategies) {
                game.setVector(new StrategyPair(player1NormalStrategies.get(player1Strategies.indexOf(player1Strat)),  //Auszahlungsvektoren mit den Strategien verknüpfen
                        player2NormalStrategies.get(player2Strategies.indexOf(player2Strat))), tree.getVector(player1Strat, player2Strat));
            }
        }
        return game;
    }

    private List<Strategy> convertExtensiveStrategiesToNormalForm(List<ExtensiveStrategy> extensiveStrategies) {  //Umwandlung von einer ExtensiveStrategy zu einer Strategie in der Normalform
        List<Strategy> normalStrats = new ArrayList<>();
        extensiveStrategies.forEach(es -> normalStrats.add(new Strategy(es.toString())));
        return normalStrats;
    }

    private List<ExtensiveStrategy> getPlayer1Strategies() {
        List<ExtensiveStrategy> strategies = new ArrayList<>();  //leere Liste
        for (Branch branch : tree.getBranches()) {
            if (!isBranchValid(branch, 1))  //Ungültige Äste ignorieren
                continue;
            ExtensiveStrategy strat = new ExtensiveStrategy();
            for (NodeConnection conn : branch.getConnections()) {
                if (tree.isPlayer1Layer(tree.getLayer(conn.getParent()))) {
                    strat.addDecision(conn);  //Jede Entscheidung von Spieler A zur Strategie hinzufügen
                }
            }
            if (!strategies.contains(strat)) {  //Wenn diese Strategie so noch nicht erstellt wurde, wird sie hinzugefügt
                strategies.add(strat);
            }
        }
        return strategies;
    }

    private List<ExtensiveStrategy> getPlayer2Strategies() {
        setupSeparatedBranches();  //seperatedBranches ist die Liste, in der für jede erste Entscheidung von Spieler A eine neue Liste enthalten ist
        int[] indices = new int[separatedBranches.size()];
        return getStrategiesFromSeparatedBranches(indices); //die indices werden gebraucht, um alle Kombinationen zu erstellen
    }

    private void setupSeparatedBranches() {
        addAllBranchesToSeparated();  //Zunächst werden alle Äste hinzugefügt
        removeNotValidBranchesFromSeparated();  //Die nicht gültigen werden entfernt
        removeOtherPlayerLayersFromSeparated(); //Die Entscheidungen von Spieler A entfernen
    }

    private void addAllBranchesToSeparated() {
        separatedBranches = new ArrayList<>();
        for (NodeConnection strat : tree.getFirstPlayerFirstStrategies()) {
            if (strat.getChild() instanceof VectorNode) {
                continue;
            }
            separatedBranches.add(tree.getBranches((StrategyNode) strat.getChild()));
        }
    }

    private void removeNotValidBranchesFromSeparated() {
        separatedBranches.forEach(seperatedBranchList -> {
            Iterator<Branch> it = seperatedBranchList.iterator();
            while (it.hasNext()) {
                Branch branch = it.next();
                if (!isBranchValid(branch, 2)) {
                    it.remove();
                }
            }
        });
    }

    private boolean isBranchValid(Branch branch, int player) {
        for (NodeConnection conn : branch.getConnections()) {
            if (!(conn.getChild() instanceof VectorNode)) {
                continue;
            }
            for (Node child : conn.getParent().getChildren()) {
                if (child instanceof VectorNode)
                    continue;
                if (tree.isPlayer1Layer(tree.getLayer(child)) && player == 1)
                    return false;
                if (!tree.isPlayer1Layer(tree.getLayer(child)) && player == 2)
                    return false;
            }
        }
        return true;
    }

    private void removeOtherPlayerLayersFromSeparated() {
        for (List<Branch> seperatedBranchList : separatedBranches) {
            for (Branch branch : seperatedBranchList) {
                Iterator<NodeConnection> it = branch.getConnections().iterator();
                while (it.hasNext()) {
                    NodeConnection conn = it.next();
                    if (tree.isPlayer1Layer(tree.getLayer(conn.getParent()))) {
                        it.remove();
                    }
                }
            }
        }
    }

    private List<ExtensiveStrategy> getStrategiesFromSeparatedBranches(int[] indices) {
        List<ExtensiveStrategy> strategies = new ArrayList<>();
        if (indices == null)
            return strategies;
        Branch[] branches = new Branch[indices.length];  //Das ist ein Array -> so ähnlich wie eine Liste
        for (int i = 0; i < separatedBranches.size(); i++) {
            branches[i] = separatedBranches.get(i).get(indices[i]); //In dem Array wird je eine Strategie aus separatedBranches gespeichert
        }
        ExtensiveStrategy strategy = new ExtensiveStrategy();
        for (int i = 0; i < branches.length; i++) {
            strategy.addDecisions(branches[i].getConnections());  //Die Strategien aus dem Array werden kombiniert
        }
        strategies.add(strategy);
        strategies.addAll(getStrategiesFromSeparatedBranches(updateIndices(indices)));  //Der selbe Prozess wird dann mit anderen Strategien aus separatedBranches wiederholt
        return strategies;                                                              //(das Array indices gibt an, welche Strategien miteinander kombiniert werden)
    }

    private int[] updateIndices(int[] indices) {
        if (isMax(indices)) {
            return null;
        }
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] < separatedBranches.get(i).size() - 1) {
                indices[i] += 1;
                break;
            } else {
                indices[i] = 0;
            }
        }
        return indices;
    }

    private boolean isMax(int[] indices) {
        boolean max = true;
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] != separatedBranches.get(i).size() - 1) {
                max = false;
            }
        }
        return max;
    }

    private void removeDoubledStrategies(List<Strategy> strategies) {
        Map<String, Strategy> namesMap = new HashMap<>();

        List<String> names = new ArrayList<>();
        strategies.forEach(s -> {
            names.add(s.getName());
            namesMap.put(s.getName(), s);
        });
        
        names.stream().filter(e -> ListHelper.isSpecificElementTwice(names, e)).forEach(e -> {
            strategies.remove(namesMap.get(e));
        });
    }
}
