package moulin.modele;

public class Position {
    private int node;
    private boolean used;
    private int player;

    public Position(int node) {
        this.node = node;
        this.used = false;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getNode() {
        return node;
    }

    public boolean isUsed() {
        return used;
    }

    public int getPlayer() {
        return player;
    }
}
