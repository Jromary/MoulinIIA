package moulin.modele.graph;

public class Connection {
    private int nodeFrom;
    private int nodeTo;
    private int cost;

    public Connection(int nodeFrom, int nodeTo, int cost) {
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.cost = cost;
    }

    public int getFromNode() {
        return nodeFrom;
    }

    public int getToNode(){
        return nodeTo;
    }

    public int getCost() {
        return cost;
    }
}
