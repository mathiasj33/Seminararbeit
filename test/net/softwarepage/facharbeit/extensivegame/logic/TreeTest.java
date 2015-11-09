/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.softwarepage.facharbeit.extensivegame.logic;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Mathias
 */
public class TreeTest { //Verschiedene automatisierte Tests um die Algorithmen zu überprüfen

    private final Tree tree;

    public TreeTest() {
        tree = TreeHelper.getTestTree();
    }
    
    @Test
    public void testGetDeepestChild() {
        Assert.assertEquals("G", tree.getDeepestChild().getParent().getConnectionTo(tree.getDeepestChild()).getName());
    }
    
    @Test
    public void testGetBranches() {
        Branch first = tree.getBranches().get(0);
        Assert.assertEquals("[A, D, F]", first.toString());
        Branch fifth = tree.getBranches().get(4);
        Assert.assertEquals("[A, D, E, J]", fifth.toString());
        Branch seventh = tree.getBranches().get(6);
        Assert.assertEquals("[B, L]", seventh.toString());
        Branch twelvth = tree.getBranches().get(11);
        Assert.assertEquals("[C, Q]", twelvth.toString());
        Assert.assertEquals(12, tree.getBranches().size());
    }
    
    @Test
    public void testGetNumberOfLayers() {
        Assert.assertEquals(5, tree.getNumberOfLayers());
    }
    
    @Test
    public void testGetLayer() {
        Assert.assertEquals(4, tree.getLayer(tree.getDeepestChild()));
    }
    
    @Test
    public void testIsPlayer1Layer() {
        Assert.assertTrue(tree.isPlayer1Layer(0));
        Assert.assertTrue(tree.isPlayer1Layer(2));
    }
}
