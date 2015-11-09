package net.softwarepage.facharbeit.extensivegame.logic;

public class TreeHelper { //Verschiedene automatisierte Tests um die Algorithmen zu überprüfen
    public static Tree getTestTree() {
        StrategyNode sp1 = getPlayer1Node();

        StrategyNode a = getPlayer2Node();
        StrategyNode d = getPlayer1Node();
        StrategyNode e = getPlayer2Node();
        VectorNode g = new VectorNode(0, 0);
        VectorNode h = new VectorNode(6, 4);
        VectorNode i = new VectorNode(0, 0);
        VectorNode j = new VectorNode(9, 1);
        e.addStrategy("G", g);
        e.addStrategy("H", h);
        e.addStrategy("I", i);
        e.addStrategy("J", j);
        VectorNode f = new VectorNode(8, 2);
        d.addStrategy("E", e);
        d.addStrategy("F", f);
        a.addStrategy("D", d);
        
//        VectorNode y = new VectorNode("Y", 1, 1));
//        
//        a.addStrategy("Y", y);

        StrategyNode b = getPlayer2Node();
        VectorNode k = new VectorNode(0, 10);
        VectorNode l = new VectorNode(4, 6);
        VectorNode m = new VectorNode(0, 0);
        b.addStrategy("K", k);
        b.addStrategy("L", l);
        b.addStrategy("M", m);
        
        StrategyNode c = getPlayer2Node();
        VectorNode n = new VectorNode(0, 0);
        VectorNode o = new VectorNode(6, 4);
        VectorNode p = new VectorNode(0, 0);
        VectorNode q = new VectorNode(9, 1);
        c.addStrategy("N", n);
        c.addStrategy("O", o);
        c.addStrategy("P", p);
        c.addStrategy("Q", q);
        
        sp1.addStrategy("A", a);
        sp1.addStrategy("B", b);
        sp1.addStrategy("C", c);
        return new Tree(sp1);
    }
    
    public static Tree getSmallTestTree() {
        StrategyNode sp1 = getPlayer1Node();
        StrategyNode sp2 = getPlayer2Node();
        sp1.addStrategy("A", sp2);
        VectorNode aa = new VectorNode(1, 0);
        sp2.addStrategy("AA", aa);
        VectorNode ab = new VectorNode(2, 0);
        sp2.addStrategy("AB", ab);
        VectorNode b = new VectorNode(0, 3);
        sp1.addStrategy("B", b);
        return new Tree(sp1);
    }
    
    private static StrategyNode getPlayer1Node() {
        return new StrategyNode("Player1");
    }
    
    private static StrategyNode getPlayer2Node() {
        return new StrategyNode("Player2");
    }
}
