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

    public Move(String receive) {
        addPiece = (receive.charAt(0) == 'Z')? -1: (int)receive.charAt(0) - 95;
        delPiece = (receive.charAt(1) == 'Z')? -1: (int)receive.charAt(1) - 95;
        delPieceE = (receive.charAt(2) == 'Z')? -1: (int)receive.charAt(2) - 95;
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

    public String encode(){
        StringBuilder sb = new StringBuilder();
        if (addPiece != -1){
            sb.append(String.valueOf((char)(addPiece + 65)));
        }else {
            sb.append("Z");
        }
        if (delPiece != -1){
            sb.append(String.valueOf((char)(delPiece + 65)));
        }else {
            sb.append("Z");
        }
        if (delPieceE != -1){
            sb.append(String.valueOf((char)(delPieceE + 65)));
        }else {
            sb.append("Z");
        }
        return sb.toString();
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
