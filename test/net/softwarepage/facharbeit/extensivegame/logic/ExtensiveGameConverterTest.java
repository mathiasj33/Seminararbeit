/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.softwarepage.facharbeit.extensivegame.logic;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import net.softwarepage.facharbeit.normalgame.logic.NormalGame;
import net.softwarepage.facharbeit.normalgame.logic.Strategy;
import net.softwarepage.facharbeit.normalgame.logic.Vector;
import org.junit.Test;

/**
 *
 * @author Mathias
 */
public class ExtensiveGameConverterTest {  //Verschiedene automatisierte Tests um die Algorithmen zu überprüfen
    
    private Tree tree;

    @Test
    public void testConvertToNormalGame() {
        tree = TreeHelper.getSmallTestTree();
        NormalGame game = new ExtensiveGameConverter().convertToNormalGame(tree);
        List<String> player1Names = getNames(game.getPlayer1().getStrategies());
        List<String> player2Names = getNames(game.getPlayer2().getStrategies());
        Assert.assertTrue(player1Names.contains("[A]"));
        Assert.assertTrue(player1Names.contains("[B]"));
        Assert.assertTrue(player2Names.contains("[AA]"));
        Assert.assertTrue(player2Names.contains("[AB]"));
        
        tree = TreeHelper.getTestTree();
        game = new ExtensiveGameConverter().convertToNormalGame(tree);
        player1Names = getNames(game.getPlayer1().getStrategies());
        player2Names = getNames(game.getPlayer2().getStrategies());
        Assert.assertTrue(player2Names.contains("[D, G, K, N]"));
        Assert.assertTrue(player2Names.contains("[D, I, M, Q]"));
        Assert.assertEquals(48, player2Names.size());
        Assert.assertTrue(player1Names.contains("[A, E]"));
        Assert.assertTrue(player1Names.contains("[A, F]"));
        Assert.assertTrue(player1Names.contains("[B]"));
        Assert.assertTrue(player1Names.contains("[C]"));
        Assert.assertEquals(4, player1Names.size());
        
        Assert.assertEquals(new Vector(8, 2), game.getVector(game.getStrategy("[A, F]", game.getPlayer1()), game.getStrategy("[D, G, K, N]", game.getPlayer2())));
        Assert.assertEquals(new Vector(4, 6), game.getVector(game.getStrategy("[B]", game.getPlayer1()), game.getStrategy("[D, I, L, Q]", game.getPlayer2())));
        Assert.assertEquals(new Vector(9, 1), game.getVector(game.getStrategy("[A, E]", game.getPlayer1()), game.getStrategy("[D, J, K, N]", game.getPlayer2())));
    }
    
    private List<String> getNames(List<Strategy> strategies) {
        List<String> names = new ArrayList<>();
        strategies.forEach(s -> names.add(s.getName()));
        return names;
    }
    
}
