package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.softwarepage.facharbeit.normalgame.logic.NormalGame;

public class ExtensiveGameConverter {

    private List<List<Branch>> seperatedBranches = new ArrayList<>();
    
    public NormalGame convertToNormalGame(Tree tree) {
        Node rootNode = tree.getRootNode();

        List<ExtensiveStrategy> player1Strategies = new ArrayList<>();
        List<ExtensiveStrategy> player2Strategies = new ArrayList<>();

        List<NodeConnection> player1Connections = new ArrayList<>(); //TODO Hier weiter machen, damit auch player 2 stimmt; Mail an Mösl.
        List<NodeConnection> player2Connections = new ArrayList<>();
        
        seperatedBranches = new ArrayList<>();
        for (NodeConnection strat : tree.getFirstPlayerFirstStrategies()) {
            if (strat.getChild() instanceof VectorNode) {
                continue;
            }
            seperatedBranches.add(tree.getBranches((StrategyNode) strat.getChild()));
        }
        for (List<Branch> seperatedBranchList : seperatedBranches) {
            Iterator<Branch> it = seperatedBranchList.iterator();
            while(it.hasNext()) {
                Branch branch = it.next();
                for(NodeConnection conn : branch.getConnections()) {
                    if(conn.getChild() instanceof VectorNode) {
                        for(Node node : conn.getParent().getChildren()) {
                            if(!(node instanceof VectorNode)) {
                                if(tree.isPlayer1Layer(tree.getLayer(node))) {
                                    //für spieler 1 entfernen
                                }
                                else {
                                    it.remove();
                                }
                            }
                        }
                    }
                }
            }
        }
        for (List<Branch> seperatedBranchList : seperatedBranches) {
            for (Branch branch : seperatedBranchList) {
                Iterator<NodeConnection> branchIt = branch.getConnections().iterator();
                while (branchIt.hasNext()) {
                    NodeConnection conn = branchIt.next();
                    if (tree.isPlayer1Layer(tree.getLayer(conn.getParent()))) {
                        branchIt.remove();
                    }
                }
            }
        }
        //TODO: DAnn Spieler1 (auch D eleminieren), VectorNodes und NormalGame. Testen
        int[] indices = new int[seperatedBranches.size()];

        System.out.println(getStrategies(indices));
        return null;
    }
    
    private List<ExtensiveStrategy> getStrategies(int[] indices) {
        List<ExtensiveStrategy> strategies = new ArrayList<>();
        if(indices == null) {
            return strategies;
        }
        Branch[] stratBranch = new Branch[indices.length];
        for(int i = 0; i < seperatedBranches.size(); i++) {
            stratBranch[i] = seperatedBranches.get(i).get(indices[i]);
        }
        ExtensiveStrategy strategy = new ExtensiveStrategy();
        for(int i = 0; i < stratBranch.length; i++) {
            strategy.addDecisions(stratBranch[i].getConnections());
        }
        strategies.add(strategy);
        strategies.addAll(getStrategies(updateIndices(indices)));
        return strategies;
    }
    
    private int[] updateIndices(int[] indices) {
        if(isMax(indices)) {
            return null;
        }
        for(int i = 0; i < indices.length; i++) {
            if(indices[i] < seperatedBranches.get(i).size() - 1) {
                indices[i] += 1;
                break;
            }
            else {
                indices[i] = 0;
            }
        }
        return indices;
    }
    
    private boolean isMax(int[] indices) {
        boolean max = true;
        for(int i = 0; i < indices.length; i++) {
            if(indices[i] != seperatedBranches.get(i).size() - 1) {
                max = false;
            }
        }
        return max;
    }
}
