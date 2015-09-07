package net.softwarepage.facharbeit.normalgame.logic;

import org.junit.Assert;
import org.junit.Test;

public class NormalGameTest {

    private Player player1;
    private Player player2;
    private NormalGame game;

    @Test
    public void testZeroSumGame() {
        setupNashNullSum();
        Assert.assertTrue(game.isZeroSumGame());
    }

    @Test
    public void testRemoveStrategy() {
        setupGame();
        game.removeStrategy("unten", player1);
        Assert.assertNull(game.getStrategy("unten", player1));
        try {
            game.removeStrategy("oben", player1);
            Assert.fail("Could remove last strategy!");
        } catch (IllegalStateException e) {
        }
        setupGame2();
        game.removeStrategy("mitte", player2);
        Assert.assertNull(game.getStrategy("mitte", player2));
        Assert.assertNotNull(game.getStrategy("mitte", player1));
    }

    @Test
    public void testStrategyOperations() {
        setupGame();
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
        setupGame();
        game.renameStrategy("oben", "OBEN", player1);
        Assert.assertEquals(1f, game.getVector(game.getStrategy("OBEN", player1), game.getStrategy("links", player2)).getFirst(), .1f);
        Assert.assertNull(game.getStrategy("oben", player1));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testRenameStrategySame() {
        setupGame();
        game.renameStrategy("unten", "oben", player1);
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

    private void setupNashNullSum() {
        player1 = new Player("oben", "unten");
        player2 = new Player("links", "rechts");

        game = new NormalGame(player1, player2);
        game.addField(new Vector(0, 0));
        game.addField(new Vector(1, -1));
        game.addField(new Vector(-1, 1));
        game.addField(new Vector(0, 0));
    }
}
