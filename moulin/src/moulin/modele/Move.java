package moulin.modele;

import java.util.ArrayList;

public class Move {
    private int addPiece;
    private int delPiece;
    private int delPieceE;

    public Move(int addPiece, int delPiece, int delPieceE){
        this.addPiece = addPiece;
        this.delPiece = delPiece;
        this.delPieceE = delPieceE;
    }

    public void setAddPiece(int addPiece) {
        this.addPiece = addPiece;
    }

    public void setDelPiece(int delPiece) {
        this.delPiece = delPiece;
    }

    public void setDelPieceE(int delPieceE) {
        this.delPieceE = delPieceE;
    }

    public int getAddPiece() {
        return addPiece;
    }

    public int getDelPiece() {
        return delPiece;
    }

    public int getDelPieceE() {
        return delPieceE;
    }

    @Override
    public String toString() {
        return "Move{" +
                "addPiece=" + addPiece +
                ", delPiece=" + delPiece +
                ", delPieceE=" + delPieceE +
                '}';
    }
}
