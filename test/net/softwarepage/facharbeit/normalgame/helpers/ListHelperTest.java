/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.softwarepage.facharbeit.normalgame.helpers;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Mathias
 */
public class ListHelperTest { //Verschiedene automatisierte Tests um die Algorithmen zu überprüfen
    
    public ListHelperTest() {
    }

    @Test
    public void testGetPowerset() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        List<List<Integer>> powerset = ListHelper.powerSet(list);
        System.out.println(powerset);
    }
    
}
