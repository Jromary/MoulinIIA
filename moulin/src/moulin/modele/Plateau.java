package moulin.modele;

import moulin.modele.graph.Connection;
import moulin.modele.graph.Graph;

public class Plateau {
    private static Plateau ourInstance = new Plateau();
    public Graph plateau;

    public static Plateau getInstance() {
        return ourInstance;
    }

    private Plateau() {
        plateau = new Graph(23);
        initGraph();
    }

    private void initGraph(){
        plateau.addConnection(new Connection(0,1,0));
        plateau.addConnection(new Connection(0,9,0));
        plateau.addConnection(new Connection(1,2,0));
        plateau.addConnection(new Connection(1,4,0));
        plateau.addConnection(new Connection(2,14,0));
        plateau.addConnection(new Connection(3,4,0));
        plateau.addConnection(new Connection(3,10,0));
        plateau.addConnection(new Connection(4,5,0));
        plateau.addConnection(new Connection(4,7,0));
        plateau.addConnection(new Connection(5,13,0));
        plateau.addConnection(new Connection(6,7,0));
        plateau.addConnection(new Connection(6,11,0));
        plateau.addConnection(new Connection(7,8,0));
        plateau.addConnection(new Connection(8,12,0));
        plateau.addConnection(new Connection(9,21,0));
        plateau.addConnection(new Connection(9,10,0));
        plateau.addConnection(new Connection(10,11,0));
        plateau.addConnection(new Connection(10,18,0));
        plateau.addConnection(new Connection(11,15,0));
        plateau.addConnection(new Connection(12,17,0));
        plateau.addConnection(new Connection(12,13,0));
        plateau.addConnection(new Connection(13,14,0));
        plateau.addConnection(new Connection(13,20,0));
        plateau.addConnection(new Connection(14,23,0));
        plateau.addConnection(new Connection(15,16,0));
        plateau.addConnection(new Connection(16,17,0));
        plateau.addConnection(new Connection(16,19,0));
        plateau.addConnection(new Connection(18,19,0));
        plateau.addConnection(new Connection(19,20,0));
        plateau.addConnection(new Connection(19,22,0));
        plateau.addConnection(new Connection(21,22,0));
        plateau.addConnection(new Connection(22,23,0));
    }
}
