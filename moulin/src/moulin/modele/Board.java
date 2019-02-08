package moulin.modele;

import moulin.modele.graph.Connection;

import java.util.ArrayList;
import java.util.Observable;

public class Board extends Observable{
    private int activePlayer; //0 = white ; 1 = black
    private Plateau plateau;
    public int nbTokenWhiteLeftOnBoard;
    public int nbTokenBlackLeftOnBoard;
    public int nbTokenWhiteLeftToPlay;
    public int nbTokenBlackLeftToPlay;
    private String[] position;

    public Board(){
        nbTokenWhiteLeftToPlay = 3;
        nbTokenBlackLeftToPlay = 3;
        nbTokenBlackLeftOnBoard = 0;
        nbTokenWhiteLeftOnBoard = 0;
        plateau = Plateau.getInstance();
        activePlayer = 0;
        position = new String[24];
        for (int i = 0; i < 24; i++){
            position[i] = "E";
        }
    }

    public ArrayList<Move> getMoves(){
        ArrayList<Move> nextMoves = new ArrayList<>();

        for (int i = 0; i < position.length; i++) {
            if (position[i].equals("E")){
                if ((currentPlayer() == 0 && nbTokenWhiteLeftToPlay > 0 ) ||
                        (currentPlayer() == 1 && nbTokenBlackLeftToPlay > 0)){
                    nextMoves.add(new Move(i,-1,-1));
                }
            }
        }

        if ((currentPlayer() == 0 && nbTokenWhiteLeftToPlay > 0 ) ||
                (currentPlayer() == 1 && nbTokenBlackLeftToPlay > 0)){

        }
        return nextMoves;
    }

    public void makeMove(Move move){
        if (currentPlayer() == 0){
            position[move.getAddPiece()] = "W";
            if (move.getDelPiece() == -1){
                nbTokenWhiteLeftOnBoard++;
                if (nbTokenWhiteLeftToPlay > 0){
                    nbTokenWhiteLeftToPlay--;
                }
            }else{
                position[move.getDelPiece()] = "E";
            }
            if (move.getDelPieceE() != -1){
                nbTokenBlackLeftOnBoard--;
                position[move.getDelPieceE()] = "E";
            }
            activePlayer = 1;
        }else{
            position[move.getAddPiece()] = "B";
            if (move.getDelPiece() == -1){
                nbTokenBlackLeftOnBoard++;
                if (nbTokenBlackLeftToPlay > 0){
                    nbTokenBlackLeftToPlay--;
                }
            }else{
                position[move.getDelPiece()] = "E";
            }
            if (move.getDelPieceE() != -1){
                nbTokenWhiteLeftOnBoard--;
                position[move.getDelPieceE()] = "E";
            }
            activePlayer = 0;
        }
        maj();
    }

    private void maj() {
        setChanged();
        notifyObservers();
    }

    public void evaluate(){

    }

    public int currentPlayer(){
        return activePlayer;
    }

    public boolean isGameOver(){
        if ((nbTokenWhiteLeftOnBoard + nbTokenWhiteLeftToPlay <= 2) || (nbTokenBlackLeftOnBoard + nbTokenBlackLeftToPlay <= 2)) {
            return true;
        }
        return false;
    }

    public String[] getPosition() {
        return position;
    }

    public boolean caseJouable(int caseABouger, int caseJouee) {
        if (caseABouger == -1){
            return true;
        }
        for (Connection c :
                plateau.plateau.getConnection(caseABouger)) {
            if (c.getToNode() == caseJouee || c.getFromNode() == caseJouee) {
                return true;
            }
        }
        return false;
    }

    public boolean isMoulinFromMove(int caseJoue){
        //System.out.println(caseJoue);
        for (int[] moulin : plateau.lesMoulin) {
            if (moulin[0] == caseJoue){
                if (currentPlayer() == 0){
                    if(position[moulin[0]].equals("E") && position[moulin[1]].equals("W") && position[moulin[2]].equals("W")){
                        return true;
                    }
                }else{
                    if (position[moulin[0]].equals("E") && position[moulin[1]].equals("B") && position[moulin[2]].equals("B")){
                        return true;
                    }
                }
            }
            if (moulin[1] == caseJoue) {
                if (currentPlayer() == 0){
                    if(position[moulin[0]].equals("W") && position[moulin[1]].equals("E") && position[moulin[2]].equals("W")){
                        return true;
                    }
                }else{
                    if (position[moulin[0]].equals("B") && position[moulin[1]].equals("E") && position[moulin[2]].equals("B")){
                        return true;
                    }
                }
            }
            if(moulin[2] == caseJoue) {
                if (currentPlayer() == 0){
                    if(position[moulin[0]].equals("W") && position[moulin[1]].equals("W") && position[moulin[2]].equals("E")){
                        return true;
                    }
                }else{
                    if (position[moulin[0]].equals("B") && position[moulin[1]].equals("B") && position[moulin[2]].equals("E")){
                        return true;
                    }
                }
            }

        }

        return false;
    }
}

