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

    public int getAddPiece() {
        return addPiece;
    }

    public int getDelPiece() {
        return delPiece;
    }

    public int getDelPieceE() {
        return delPieceE;
    }
}
