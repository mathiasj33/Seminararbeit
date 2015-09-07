/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.softwarepage.facharbeit.normalgame.logic;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Mathias
 */
public class DominantStrategyFinderTest {

    private NormalGame game;
    private Player player1;
    private Player player2;

    @Test
    public void testFindDominantStrategy() {
        setupGame();
        Assert.assertEquals(1, game.findDominatedStrategies(player1).size());
        Assert.assertEquals(new Strategy("oben").getName(), game.findDominatedStrategies(player1).get(0).getName());
        Assert.assertEquals(StrategyType.Dominated, game.findDominatedStrategies(player1).get(0).getType());
        Assert.assertEquals(new Strategy("links").getName(), game.findDominatedStrategies(player2).get(0).getName());
        Assert.assertEquals(StrategyType.WeaklyDominated, game.findDominatedStrategies(player2).get(0).getType());
        
        setupGame2();
        Assert.assertEquals(1, game.findDominatedStrategies(player1).size());
        Assert.assertEquals("oben", game.findDominatedStrategies(player1).get(0).getName());
        Assert.assertEquals(StrategyType.WeaklyDominated, game.findDominatedStrategies(player1).get(0).getType());
        Assert.assertEquals("links", game.findDominatedStrategies(player2).get(0).getName());
        Assert.assertEquals(StrategyType.Dominated, game.findDominatedStrategies(player2).get(0).getType());
        Assert.assertEquals("mitte", game.findDominatedStrategies(player2).get(1).getName());
        Assert.assertEquals(StrategyType.WeaklyDominated, game.findDominatedStrategies(player2).get(1).getType());
    }
    
    public void setupGame() {
        player1 = new Player("oben", "unten");
        player2 = new Player("links", "rechts");
        game = new NormalGame(player1, player2);

        game.addField(new Vector(1, 2));
        game.addField(new Vector(0, 2));
        game.addField(new Vector(2, 1));
        game.addField(new Vector(2, 2));
    }
    
     private void setupGame2() {
        player1 = new Player("oben", "mitte", "unten");
        player2 = new Player("links", "mitte", "rechts");

        game = new NormalGame(player1, player2);

        game.addField(new Vector(1, 2));
        game.addField(new Vector(0, 3));
        game.addField(new Vector(1, 3));
        game.addField(new Vector(2, 1));
        game.addField(new Vector(2, 2));
        game.addField(new Vector(1, 3));
        game.addField(new Vector(1, 1));
        game.addField(new Vector(3, 2));
        game.addField(new Vector(2, 4));
    }
}
