package net.softwarepage.facharbeit.normalgame.test;

import net.softwarepage.facharbeit.normalgame.logic.Matrix;
import net.softwarepage.facharbeit.normalgame.logic.NormalNashEquilibrium;
import net.softwarepage.facharbeit.normalgame.logic.StrategyType;
import net.softwarepage.facharbeit.normalgame.logic.NormalGame;
import net.softwarepage.facharbeit.normalgame.logic.Vector;
import net.softwarepage.facharbeit.normalgame.logic.Strategy;
import net.softwarepage.facharbeit.normalgame.logic.Player;
import net.softwarepage.facharbeit.normalgame.logic.NashEquilibriumType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NormalGameTest {

    private Player player1;
    private Player player2;
    private NormalGame game;

    @Test
    public void testFindDominantStrategy() {
        Assert.assertEquals(1, game.findDominatedStrategies(player1).size());
        Assert.assertEquals(new Strategy("oben").getName(), game.findDominatedStrategies(player1).get(0).getName());
        Assert.assertEquals(StrategyType.Dominated, game.findDominatedStrategies(player1).get(0).getType());
        Assert.assertEquals(new Strategy("links").getName(), game.findDominatedStrategies(player2).get(0).getName());
        Assert.assertEquals(StrategyType.WeaklyDominated, game.findDominatedStrategies(player2).get(0).getType());
        setUpNashDominantStrategy2();
        Assert.assertEquals(1, game.findDominatedStrategies(player1).size());
        Assert.assertEquals("oben", game.findDominatedStrategies(player1).get(0).getName());
        Assert.assertEquals(StrategyType.WeaklyDominated, game.findDominatedStrategies(player1).get(0).getType());
        Assert.assertEquals("links", game.findDominatedStrategies(player2).get(0).getName());
        Assert.assertEquals(StrategyType.Dominated, game.findDominatedStrategies(player2).get(0).getType());
        Assert.assertEquals("mitte", game.findDominatedStrategies(player2).get(1).getName());
        Assert.assertEquals(StrategyType.WeaklyDominated, game.findDominatedStrategies(player2).get(1).getType());
        setUpNashNullSum();
    }

    @Test
    public void testCalculateNash() {
        Assert.assertEquals(1, game.calculateNash().size());
        NormalNashEquilibrium ne = game.calculateNash().get(0);
        Assert.assertEquals("unten", ne.getFirstStrat().getName());
        Assert.assertEquals("rechts", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.Strict, ne.getType());
        setUpNashFour();
        Assert.assertEquals(2, game.calculateNash().size());
        ne = game.calculateNash().get(0);
        Assert.assertEquals("unten", ne.getFirstStrat().getName());
        Assert.assertEquals("links", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.NotStrict, ne.getType());
        setUpNashSeven();
        Assert.assertEquals(1, game.calculateNash().size());
        ne = game.calculateNash().get(0);
        Assert.assertEquals("oben", ne.getFirstStrat().getName());
        Assert.assertEquals("rechts", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.Strict, ne.getType());
        setUpNashOwn();
        Assert.assertEquals(2, game.calculateNash().size());
        ne = game.calculateNash().get(1);
        Assert.assertEquals("mitte", ne.getFirstStrat().getName());
        Assert.assertEquals("rechts", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.Strict, ne.getType());
        ne = game.calculateNash().get(0);
        Assert.assertEquals("unten", ne.getFirstStrat().getName());
        Assert.assertEquals("links", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.Strict, ne.getType());
        setUpNashNullSum();
        Assert.assertEquals(1, game.calculateNash().size());
        ne = game.calculateNash().get(0);
        Assert.assertEquals("oben", ne.getFirstStrat().getName());
        Assert.assertEquals("links", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.Strict, ne.getType());

        //GUITEST
        player1 = new Player("oben", "mitte", "unten");
        player2 = new Player("links", "rechts");
        game = new NormalGame(player1, player2);
        game.addField(new Vector(2,2));
        game.addField(new Vector(5,1));
        game.addField(new Vector(3,4));
        game.addField(new Vector(4,1));
        game.addField(new Vector(4,3));
        game.addField(new Vector(2,4));
        
        game.calculateNashWithMixedStrategies();
    }

    @Test
    public void testNashMixed() {
        setUpNashMixed();
        game.calculateNashWithMixedStrategies();
        Assert.assertEquals(9.1f, (float) game.getProbability("l", player2), .01f);

        setUpNashMixed2();
        game.calculateNashWithMixedStrategies();
        Assert.assertEquals(75f, (float) game.getProbability("links", player2), .01f);
        Assert.assertEquals(80f, (float) game.getProbability("oben", player1), .01f);

        Assert.assertEquals(new Vector(2.4f, 1.6f), game.getMixedPayoff("rechts", player2));
        Assert.assertEquals(new Vector(1.2f, 1.6f), game.getMixedPayoff("links", player2));
        Assert.assertEquals(new Vector(1.5f, 1.75f), game.getMixedPayoff("oben", player1));
        Assert.assertEquals(new Vector(1.5f, 1f), game.getMixedPayoff("unten", player1));
        Assert.assertEquals(new Vector(1.5f, 1.6f), game.getOptimalMixedPayoff());
    }
    
    @Test
    public void testGetSubMatrix2x2() {
        Assert.assertEquals(1, game.getSubGames2x2().size());
        setUpNashMixed();
        Assert.assertEquals(4, game.getSubGames2x2().size());
        setUpNashOwn();
        Assert.assertEquals(2, game.getSubGames2x2().size());
    }

    @Test
    public void testZeroSumGame() {
        setUpNashNullSum();
        Assert.assertTrue(game.isZeroSumGame());
    }

    @Test
    public void testRemoveStrategy() {
        game.removeStrategy("unten", player1);
        Assert.assertNull(game.getStrategy("unten", player1));
        try {
            game.removeStrategy("oben", player1);
            Assert.fail("Could remove last strategy!");
        } catch (IllegalStateException e) {
        }
        setUpNashDominantStrategy2();
        game.removeStrategy("mitte", player2);
        Assert.assertNull(game.getStrategy("mitte", player2));
        Assert.assertNotNull(game.getStrategy("mitte", player1));
    }

    @Test
    public void testStrategyOperations() {
        try {
            game.addStrategy("unten", player1);
            Assert.fail("could add same strategy twice");
        } catch (IllegalArgumentException e) {

        }
        Assert.assertNotNull(game.getStrategy("unten", player1));

        game.addStrategy("newStrat", player1);
        Assert.assertNotNull(game.getStrategy("newStrat", player1));
        game.addStrategy("newStrat", player2);
        Assert.assertNotNull(game.getStrategy("newStrat", player2));
        game.addStrategy("testStrat", player2);
        Assert.assertNotNull(game.getStrategy("testStrat", player2));
        game.removeStrategy("testStrat", player2);
        Assert.assertNull(game.getStrategy("testStrat", player2));
    }

    @Test
    public void testRenameStrategy() {
        game.renameStrategy("oben", "OBEN", player1);
        Assert.assertEquals(1f, game.getVector(game.getStrategy("OBEN", player1), game.getStrategy("links", player2)).getFirst(), .1f);
        Assert.assertNull(game.getStrategy("oben", player1));
    }

    @Before
    public void setUp() {
        player1 = new Player("oben", "unten");
        player2 = new Player("links", "rechts");
        game = new NormalGame(player1, player2);

        game.addField(new Vector(1, 2));
        game.addField(new Vector(0, 2));
        game.addField(new Vector(2, 1));
        game.addField(new Vector(2, 2));
    }

    private void setUpNashDominantStrategy2() {
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

    private void setUpNashMixed() {
        player1 = new Player("o", "m", "u");
        player2 = new Player("l", "m", "r");
        player1.setName("Player A");
        player2.setName("Player B");

        game = new NormalGame(player1, player2);

        game.addField(new Vector(7, 0));
        game.addField(new Vector(6, 3));
        game.addField(new Vector(7, 0));
        game.addField(new Vector(9, 2));
        game.addField(new Vector(7, 6));
        game.addField(new Vector(0, 8));
        game.addField(new Vector(6, 9));
        game.addField(new Vector(7, 1));
        game.addField(new Vector(2, 8));
    }

    private void setUpNashMixed2() {
        player1 = new Player("oben", "unten");
        player2 = new Player("links", "rechts");
        game = new NormalGame(player1, player2);

        game.addField(new Vector(1, 2));
        game.addField(new Vector(3, 1));
        game.addField(new Vector(2, 0));
        game.addField(new Vector(0, 4));
    }

    private void setUpNashFour() {
        game.clear();
        game.addField(new Vector(6, 6));
        game.addField(new Vector(2, 8));
        game.addField(new Vector(6, 2));
        game.addField(new Vector(0, 0));
    }

    private void setUpNashSeven() {
        player1 = new Player("oben", "mitte", "unten");
        player2 = new Player("links", "mitte", "rechts");

        game = new NormalGame(player1, player2);

        game.addField(new Vector(2, 0));
        game.addField(new Vector(1, 1));
        game.addField(new Vector(4, 2));
        game.addField(new Vector(1, 4));
        game.addField(new Vector(1, 1));
        game.addField(new Vector(2, 3));
        game.addField(new Vector(1, 3));
        game.addField(new Vector(0, 2));
        game.addField(new Vector(3, 0));
    }

    private void setUpNashOwn() {
        player1 = new Player("oben", "mitte", "unten");
        player2 = new Player("links", "rechts");

        game = new NormalGame(player1, player2);

        game.addField(new Vector(1, 2));
        game.addField(new Vector(0, 4));
        game.addField(new Vector(1, 1));
        game.addField(new Vector(2, 2));
        game.addField(new Vector(2, 5));
        game.addField(new Vector(-1, 2));
    }

    private void setUpNashNullSum() {
        player1 = new Player("oben", "unten");
        player2 = new Player("links", "rechts");

        game = new NormalGame(player1, player2);
        game.addField(new Vector(0, 0));
        game.addField(new Vector(1, -1));
        game.addField(new Vector(-1, 1));
        game.addField(new Vector(0, 0));
    }
}
