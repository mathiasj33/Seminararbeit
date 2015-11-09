package net.softwarepage.facharbeit.normalgame.logic;

import org.junit.Assert;
import org.junit.Test;

public class PureNashEquilibriumFinderTest { //Verschiedene automatisierte Tests um die Algorithmen zu überprüfen
    
    private NormalGame game;
    private Player player1;
    private Player player2;

    @Test
    public void testCalculateNash() {
        setupGame();
        Assert.assertEquals(1, game.findPureNashEquilibria().size());
        PureNashEquilibrium ne = game.findPureNashEquilibria().get(0);
        Assert.assertEquals("oben", ne.getFirstStrat().getName());
        Assert.assertEquals("rechts", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.Strict, ne.getType());
        
        setupGame2();
        Assert.assertEquals(2, game.findPureNashEquilibria().size());
        ne = game.findPureNashEquilibria().get(1);
        Assert.assertEquals("mitte", ne.getFirstStrat().getName());
        Assert.assertEquals("rechts", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.Strict, ne.getType());
        ne = game.findPureNashEquilibria().get(0);
        Assert.assertEquals("unten", ne.getFirstStrat().getName());
        Assert.assertEquals("links", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.Strict, ne.getType());
        
        setupGame3();
        Assert.assertEquals(2, game.findPureNashEquilibria().size());
        ne = game.findPureNashEquilibria().get(0);
        Assert.assertEquals("unten", ne.getFirstStrat().getName());
        Assert.assertEquals("links", ne.getSecondStrat().getName());
        Assert.assertEquals(NashEquilibriumType.NotStrict, ne.getType());
    }
    
    private void setupGame() {
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
    
    private void setupGame2() {
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
    
    private void setupGame3() {
        player1 = new Player("oben", "unten");
        player2 = new Player("links", "rechts");
        game = new NormalGame(player1, player2);
        
        game.addField(new Vector(6, 6));
        game.addField(new Vector(2, 8));
        game.addField(new Vector(6, 2));
        game.addField(new Vector(0, 0));
    }
}
