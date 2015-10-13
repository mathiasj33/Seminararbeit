/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.softwarepage.facharbeit.extensivegame.logic;

import org.junit.Test;

/**
 *
 * @author Mathias
 */
public class ExtensiveGameConverterTest {
    
    private final Tree tree;
    
    public ExtensiveGameConverterTest() {
        tree = TreeHelper.getTestTree();
    }

    @Test
    public void testConvertToNormalGame() {
        new ExtensiveGameConverter().convertToNormalGame(tree);
    }
    
}
