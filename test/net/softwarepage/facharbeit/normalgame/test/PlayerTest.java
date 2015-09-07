/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.softwarepage.facharbeit.normalgame.test;

import net.softwarepage.facharbeit.normalgame.logic.Player;
import org.junit.Test;

public class PlayerTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        new Player("oben", "oben");
    }
}
