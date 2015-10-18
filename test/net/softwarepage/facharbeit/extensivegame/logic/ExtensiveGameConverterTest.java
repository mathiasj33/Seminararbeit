/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.softwarepage.facharbeit.extensivegame.logic;

import net.softwarepage.facharbeit.normalgame.logic.NormalGame;
import org.junit.Test;

/**
 *
 * @author Mathias
 */
public class ExtensiveGameConverterTest {
    
    private Tree tree; //TODO Hier noch einfacheres spiel benutzen und strategien überprüfen + schauen ob NGG bei AA und AB

    @Test
    public void testConvertToNormalGame() {
        tree = TreeHelper.getSmallTestTree();
        NormalGame game = new ExtensiveGameConverter().convertToNormalGame(tree);
    }
    
}
