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
    private int joueurOdinateur = 1;

    public Board(){
        nbTokenWhiteLeftToPlay = 9;
        nbTokenBlackLeftToPlay = 9;
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
        //premiere phase de jeu
        for (int i = 0; i < position.length; i++) {
            // si on a une case vide alors
            if (position[i].equals("E")){
                if ((currentPlayer() == 0 && nbTokenWhiteLeftToPlay > 0 ) ||
                        (currentPlayer() == 1 && nbTokenBlackLeftToPlay > 0)){
                    // si il nous reste des jeton a jouer en temps que joueur
                    // si on placant un jeton sur cette case on fait un moulin
                    if (isMoulinFromMove(i,-1)){
                        //on cherche toutes les case ou il y a une adversaire
                        for (int j = 0; j < position.length; j++) {
                            if (currentPlayer() == 1){
                                if (position[j].equals("W")){
                                    nextMoves.add(new Move(i,-1,j));
                                }
                            }
                            if (currentPlayer() == 0){
                                if (position[j].equals("B")){
                                    nextMoves.add(new Move(i,-1,j));
                                }
                            }
                        }
                    // si on ne fait pas de moulin on place alors juste le jeton au bon endroit
                    }else{
                        nextMoves.add(new Move(i,-1,-1));
                    }
                }
            }
        }
        /*
        if ((currentPlayer() == 0 && nbTokenWhiteLeftToPlay > 0 ) ||
                (currentPlayer() == 1 && nbTokenBlackLeftToPlay > 0)){
            //TODO: next move quand on a fini de placer les pions et qu'on commance a bouger les pins deja sur le jeu
            //TODO: verifier si on fait un moulin pour elever la piece enemie
        }
        */
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

    public double evaluate(){
        double alpha = 0.5;
        double eval = 0;
        int nbpairAlOrdi = nbPaireAlignee(joueurOdinateur);
        int nbpairAlNOrdi = nbPaireAlignee(Math.abs(joueurOdinateur - 1));
        System.out.println("nbpair ordi : " + nbpairAlOrdi);
        System.out.println("nbpair Nordi : " + nbpairAlNOrdi);
        System.out.println("nbRDM ordi : " + nbJetonNonAllignee(joueurOdinateur));
        System.out.println("nbRDM Nordi : " + nbJetonNonAllignee(Math.abs(joueurOdinateur - 1)));
        eval = nbpairAlOrdi - nbpairAlNOrdi + alpha * (nbJetonNonAllignee(joueurOdinateur) - nbJetonNonAllignee(Math.abs(joueurOdinateur - 1)));
        return eval;
    }

    private int nbPaireAlignee(int joueur){
        int nb = 0;
        String test;
        if (joueur == 1){
            test = "B";
        }else{
            test = "W";
        }
        for (int[] moulin : plateau.lesMoulin) {
            if (position[moulin[0]].equals(test)){
                if (position[moulin[1]].equals(test)){
                    nb++;
                }else{
                    if (position[moulin[2]].equals(test)){
                        nb++;
                    }
                }
            }else{
                if (position[moulin[1]].equals(test)){
                    if (position[moulin[2]].equals(test)){
                        nb++;
                    }
                }
            }
        }
        return nb;
    }

    private int nbJetonNonAllignee(int joueur){
        return Math.max( nbJetonPlacee(joueur) - nbPaireAlignee(joueur), 0);
    }

    private int nbJetonPlacee(int joueur){
        if (joueur == 1){
            return nbTokenBlackLeftOnBoard;
        }else {
            return nbTokenWhiteLeftOnBoard;
        }
    }

    public int currentPlayer(){
        return activePlayer;
    }

    public boolean isGameOver(){
        if ((nbTokenWhiteLeftOnBoard + nbTokenWhiteLeftToPlay <= 2) || (nbTokenBlackLeftOnBoard + nbTokenBlackLeftToPlay <= 2)) {
            return true;
        }else{
            //TODO: Faire la methode getMoves !
//            if (getMoves().size() <= 0){
//                return true;
//            }
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

    public boolean isMoulinFromMove(int caseJoue, int caseFrom){
        for (int[] moulin : plateau.lesMoulin) {
            if (moulin[0] == caseJoue){
                if (currentPlayer() == 0){
                    if(position[moulin[0]].equals("E") && position[moulin[1]].equals("W") && position[moulin[2]].equals("W")){
                        if (moulin[1] != caseFrom && moulin[2] != caseFrom){
                            return true;
                        }
                    }
                }else{
                    if (position[moulin[0]].equals("E") && position[moulin[1]].equals("B") && position[moulin[2]].equals("B")){
                        if (moulin[1] != caseFrom && moulin[2] != caseFrom){
                            return true;
                        }
                    }
                }
            }
            if (moulin[1] == caseJoue) {
                if (currentPlayer() == 0){
                    if(position[moulin[0]].equals("W") && position[moulin[1]].equals("E") && position[moulin[2]].equals("W")){
                        if (moulin[0] != caseFrom && moulin[2] != caseFrom){
                            return true;
                        }
                    }
                }else{
                    if (position[moulin[0]].equals("B") && position[moulin[1]].equals("E") && position[moulin[2]].equals("B")){
                        if (moulin[0] != caseFrom && moulin[2] != caseFrom){
                            return true;
                        }
                    }
                }
            }
            if(moulin[2] == caseJoue) {
                if (currentPlayer() == 0){
                    if(position[moulin[0]].equals("W") && position[moulin[1]].equals("W") && position[moulin[2]].equals("E")){
                        if (moulin[1] != caseFrom && moulin[0] != caseFrom){
                            return true;
                        }
                    }
                }else{
                    if (position[moulin[0]].equals("B") && position[moulin[1]].equals("B") && position[moulin[2]].equals("E")){
                        if (moulin[1] != caseFrom && moulin[0] != caseFrom){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}

