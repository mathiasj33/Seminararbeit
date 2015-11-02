/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.softwarepage.facharbeit.normalgame.logic;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Mathias
 */
public class MixedNashEquilibriumFinderTest {

    private NormalGame game;
    private Player player1;
    private Player player2;

    @Test
    public void testFindMixedNashEquilibrium() {
        setupGame();
        MixedNashEquilibrium mne = game.findDirectMixedNashEquilibrium();
        Assert.assertEquals(9.1f, (float) mne.getProbability("l", player2), .1f);
        Assert.assertEquals(77.2f, (float) mne.getProbability("m", player2), .1f);
        Assert.assertEquals(13.6f, (float) mne.getProbability("r", player2), .1f);
        Assert.assertEquals(67.6f, (float) mne.getProbability("o", player1), .1f);
        Assert.assertEquals(4.6f, (float) mne.getProbability("m", player1), .1f);
        Assert.assertEquals(27.7f, (float) mne.getProbability("u", player1), .1f);
        
        setupGame2();
        mne = game.findMixedNashEquilibria().get(0);
        Assert.assertEquals(80f, (float) mne.getProbability("oben", player1), .1f);
        Assert.assertEquals(20f, (float) mne.getProbability("unten", player1), .1f);
        Assert.assertEquals(75f, (float) mne.getProbability("links", player2), .1f);
        Assert.assertEquals(25f, (float) mne.getProbability("rechts", player2), .1f);

        Assert.assertEquals(new Vector(2.4f, 1.6f), game.getMixedPayoff(mne, "rechts", player2));
        Assert.assertEquals(new Vector(1.2f, 1.6f), game.getMixedPayoff(mne, "links", player2));
        Assert.assertEquals(new Vector(1.5f, 1.75f), game.getMixedPayoff(mne, "oben", player1));
        Assert.assertEquals(new Vector(1.5f, 1f), game.getMixedPayoff(mne, "unten", player1));
        Assert.assertEquals(new Vector(1.5f, 1.6f), game.getOptimalMixedPayoff(mne));
    }
    
    @Test
    public void testGetSubGames2x2() {
        setupSubGame();
        List<NormalGame> games = game.getSubGames();
        NormalGame game1 = games.get(2);
        NormalGame game2 = games.get(0);
        Assert.assertEquals(3, games.size());
        Assert.assertNotNull(game1.getStrategy("oben", game1.getPlayer1()));
        Assert.assertNotNull(game1.getStrategy("mittig", game1.getPlayer1()));
        Assert.assertNotNull(game1.getStrategy("links", game1.getPlayer2()));
        Assert.assertNotNull(game1.getStrategy("rechts", game1.getPlayer2()));
        Assert.assertNull(game1.getStrategy("unten", game1.getPlayer1()));
        
        Assert.assertNotNull(game2.getStrategy("mittig", game2.getPlayer1()));
        Assert.assertNotNull(game2.getStrategy("unten", game2.getPlayer1()));
        Assert.assertNotNull(game2.getStrategy("links", game2.getPlayer2()));
        Assert.assertNotNull(game2.getStrategy("rechts", game2.getPlayer2()));
        Assert.assertNull(game2.getStrategy("oben", game2.getPlayer1()));
    }
    
    @Test
    public void testAreSubGameEquilibriaValid() {
        setupSubGame();
        MixedNashEquilibrium mne = game.findMixedNashEquilibria().get(0);
        List<NormalGame> games = game.getSubGames();
        NormalGame game1 = games.get(2);
        NormalGame game2 = games.get(0);
        
        Assert.assertFalse(game.isSubGameEquilibriumValid(game1));
        Assert.assertTrue(game.isSubGameEquilibriumValid(game2));  //Hier weiter machen -> stimmt die Berechnung (in Seminararbeit per Hand anderes Ergebnis!?)
        
        setupSubGame3();
        game.findMixedNashEquilibria();
        for(NormalGame g : game.getSubGames()) {
            Assert.assertFalse(game.isSubGameEquilibriumValid(g));
        }
    }
    
    @Test
    public void testWithSubGames() {
        setupSubGame();
        MixedNashEquilibrium mne = game.findMixedNashEquilibria().get(0);
        Assert.assertEquals(0, mne.getProbability("oben", player1), .1f);
        Assert.assertEquals(25f, mne.getProbability("mittig", player1), .1f);
        Assert.assertTrue(player1.getStrategies().contains(game.getStrategy("oben", player1)));
        Assert.assertEquals(new Vector(2, 2), game.getVector(game.getStrategy("oben", player1), game.getStrategy("links", player2)));
        Assert.assertEquals(3.3f, game.getOptimalMixedPayoff(mne).getFirst(), .1f);
        
        setupSubGame2();
        mne = game.findMixedNashEquilibria().get(0); 
        Assert.assertEquals(33.3f, mne.getProbability("oben", player1), 0.1f);
        Assert.assertEquals(66.6f, mne.getProbability("unten", player1), 0.1f);
        Assert.assertEquals(0f, mne.getProbability("links", player2), 0.1f);
        Assert.assertEquals(53.8f, mne.getProbability("mittig", player2), 0.1f);
        Assert.assertEquals(46.2f, mne.getProbability("rechts", player2), 0.1f);
        Assert.assertEquals(7.4f, game.getOptimalMixedPayoff(mne).getFirst(), 0.1f);
        Assert.assertEquals(1.33f, game.getOptimalMixedPayoff(mne).getSecond(), 0.1f);
    }
    
    @Test
    public void testWithMultipleEquilibria() {
        setupSubGame4();
        List<MixedNashEquilibrium> mixedNashEquilibria = game.findMixedNashEquilibria();
        MixedNashEquilibrium mne1 = mixedNashEquilibria.get(0);
        MixedNashEquilibrium mne2 = mixedNashEquilibria.get(1);
        
        Assert.assertEquals(0f, mne1.getProbability("oben", player1), .1f);
        Assert.assertEquals(98.1f, mne1.getProbability("unten", player1), .1f);
        Assert.assertEquals(8.2f, mne1.getProbability("links", player2), .1f);
        
        Assert.assertEquals(15.3f, mne2.getProbability("oben", player1), .1f);
        Assert.assertEquals(0f, mne2.getProbability("mittig2", player1), .1f);
        Assert.assertEquals(3.9f, mne2.getProbability("rechts", player2), .1f);
    }
    
    @Test
    public void testNoMixed() {
        setupNoMixedGame();
        Assert.assertEquals(null, game.findMixedNashEquilibria());
    }
    
    private void setupGame() {
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
    
    private void setupGame2() {
        player1 = new Player("oben", "unten");
        player2 = new Player("links", "rechts");
        game = new NormalGame(player1, player2);

        game.addField(new Vector(1, 2));
        game.addField(new Vector(3, 1));
        game.addField(new Vector(2, 0));
        game.addField(new Vector(0, 4));
    }
    
    private void setupSubGame() {
        player1 = new Player("oben", "mittig", "unten");
        player1.setName("Player1");
        player2 = new Player("links", "rechts");
        player2.setName("Player2");
        game = new NormalGame(player1, player2);

        game.addField(new Vector(2, 2));
        game.addField(new Vector(5, 1));
        game.addField(new Vector(3, 4));
        game.addField(new Vector(4, 1));
        game.addField(new Vector(4, 3));
        game.addField(new Vector(2, 4));
    }
    
    private void setupSubGame2() {
        player1 = new Player("oben", "unten");
        player1.setName("Player1");
        player2 = new Player("links", "mittig", "rechts");
        player2.setName("Player2");
        game = new NormalGame(player1, player2);
        
        game.addField(new Vector(5, 3));
        game.addField(new Vector(12, 0));
        game.addField(new Vector(2, 2));
        game.addField(new Vector(6, 0));
        game.addField(new Vector(6, 2));
        game.addField(new Vector(9, 1));
    }
    
    private void setupSubGame3() {
        player1 = new Player("oben", "mittig", "unten");
        player1.setName("Player1");
        player2 = new Player("links", "mittig", "rechts");
        player2.setName("Player2");
        game = new NormalGame(player1, player2);

        game.addField(new Vector(1, 2));
        game.addField(new Vector(3, 1));
        game.addField(new Vector(4, 5));
        game.addField(new Vector(2, 0));
        game.addField(new Vector(0, 4));
        game.addField(new Vector(2, 2));
        game.addField(new Vector(-3, 8));
        game.addField(new Vector(1, 0));
        game.addField(new Vector(-2, 4));
    }
    
    private void setupSubGame4() {
        player1 = new Player("oben", "mittig", "mittig2", "unten");
        player1.setName("Player1");
        player2 = new Player("links", "mittig", "rechts");
        player2.setName("Player2");
        game = new NormalGame(player1, player2);

        game.addField(new Vector(2.34f, 1.2f));
        game.addField(new Vector(0.3f, 0.4f));
        game.addField(new Vector(98, 23.1f));
        game.addField(new Vector(9.2f, 4.1f));
        game.addField(new Vector(4, 93));
        game.addField(new Vector(2.1f, 2.2f));
        game.addField(new Vector(1, 1));
        game.addField(new Vector(0, 0));
        game.addField(new Vector(9, -3));
        game.addField(new Vector(-2, 2));
        game.addField(new Vector(5, 0.3f));
        game.addField(new Vector(1, -2));
    }
    
    private void setupNoMixedGame() {
        player1 = new Player("oben", "unten");
        player2 = new Player("links", "rechts");
        game = new NormalGame(player1, player2);
        
        game.addField(new Vector(2, 2));
        game.addField(new Vector(5, 1));
        game.addField(new Vector(3, 4));
        game.addField(new Vector(4, 1));
        
    }
}
