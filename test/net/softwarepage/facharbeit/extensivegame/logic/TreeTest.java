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
public class TreeTest {

    private final Tree tree;

    public TreeTest() {
        tree = TreeHelper.getTestTree();
    }
    
    @Test
    public void testGetDeepestChild() {
        Assert.assertEquals("G", tree.getDeepestChild().getName());
    }
    
    @Test
    public void testGetBranches() {
        System.out.println(tree.getBranches());  //TODO MACHEN
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
    
    @Test
    public void testGetVector() {
        
    }
}
