package moulin.modele.graph;

import java.util.ArrayList;

public class Graph {
    private int node;
    private ArrayList<Connection> edge;

    public Graph(int nbNode){
        edge = new ArrayList<>();
        node = nbNode;
    }

    public ArrayList<Connection> getConnection(int node){
        ArrayList<Connection> close = new ArrayList<>();
        for (Connection c : edge) {
            if (c.getFromNode() == node){
                close.add(c);
            }
        }
        return close;
    }

    public void addConnection(Connection connection) {
        edge.add(connection);
    }
}
