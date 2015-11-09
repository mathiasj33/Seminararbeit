package net.softwarepage.facharbeit.normalgame.logic;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class PlayerTest { //Verschiedene automatisierte Tests um die Algorithmen zu überprüfen
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        new Player("oben", "oben");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() {
        Strategy strat1 = new Strategy("oben");
        Strategy strat2 = new Strategy("oben");
        List<Strategy> strats = new ArrayList<>();
        strats.add(strat1);
        strats.add(strat2);
        new Player(strats);
    }
}
