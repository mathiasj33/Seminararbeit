package net.softwarepage.facharbeit.normalgame.test;

import java.util.List;
import net.softwarepage.facharbeit.normalgame.logic.NormalGame;
import net.softwarepage.facharbeit.normalgame.logic.Vector;
import net.softwarepage.facharbeit.normalgame.logic.Player;
import org.junit.Assert;
import org.junit.Test;

public class TestSpecialCase {

    private Player player1;
    private Player player2;
    private NormalGame game;

    private void setupStandard() {
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
    
    @Test
    public void testGetSubGames2x2() {
        setupStandard();

        List<NormalGame> games = game.getSubGames2x2();
        NormalGame game1 = games.get(0);
        NormalGame game2 = games.get(1);
        
        Assert.assertEquals(2, games.size());
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
    public void testIsSubGameEquilibriumValid() {
        setupStandard();
        game.calculateNashWithMixedStrategies();
        List<NormalGame> games = game.getSubGames2x2();
        NormalGame game1 = games.get(0);
        NormalGame game2 = games.get(1);
        
        Assert.assertFalse(game.isSubGameEquilibriumValid(game1));
        Assert.assertTrue(game.isSubGameEquilibriumValid(game2));
    }
    
    @Test
    public void testCalculateNash() {
        setupStandard();

        game.calculateNashWithMixedStrategies();
        
        Assert.assertEquals(0, game.getProbability("oben", player1), .1f);
        Assert.assertEquals(25f, game.getProbability("mittig", player1), .1f);
        Assert.assertTrue(player1.getStrategies().contains(game.getStrategy("oben", player1)));
        Assert.assertEquals(new Vector(2, 2), game.getVector(game.getStrategy("oben", player1), game.getStrategy("links", player2)));
        Assert.assertEquals(3.3f, game.getOptimalMixedPayoff().getFirst(), .1f);
    }
    
    @Test
    public void testCalculateNash2() {
        player1 = new Player("oben", "unten");
        player1.setName("Player1");
        player2 = new Player("links", "mittig", "rechts"); //tests schreiben, ausprobieren, text schreiben
        player2.setName("Player2");
        game = new NormalGame(player1, player2);
        
        game.addField(new Vector(5, 3));
        game.addField(new Vector(12, 0));
        game.addField(new Vector(2, 2));
        game.addField(new Vector(6, 0));
        game.addField(new Vector(6, 2));
        game.addField(new Vector(9, 1));
        
        game.calculateNashWithMixedStrategies();
        
        Assert.assertEquals(33.3f, game.getProbability("oben", player1), 0.1f);
        Assert.assertEquals(66.6f, game.getProbability("unten", player1), 0.1f);
        Assert.assertEquals(0f, game.getProbability("links", player2), 0.1f);
        Assert.assertEquals(53.8f, game.getProbability("mittig", player2), 0.1f);
        Assert.assertEquals(46.2f, game.getProbability("rechts", player2), 0.1f);
        Assert.assertEquals(7.4f, game.getOptimalMixedPayoff().getFirst(), 0.1f);
        Assert.assertEquals(1.33f, game.getOptimalMixedPayoff().getSecond(), 0.1f);
    }
}
