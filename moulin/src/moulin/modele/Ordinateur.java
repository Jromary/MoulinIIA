package moulin.modele;

import moulin.modele.connec.Client;
import moulin.modele.connec.Serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Ordinateur implements Observer {
    private Board boardJeu;
    private int numeroJoueur;
    private HashMap<Board, Double> sauvegarde;

    public Ordinateur(Board boardJeu, int numeroJoueur){
        this.boardJeu = boardJeu;
        boardJeu.addObserver(this);
        this.numeroJoueur = numeroJoueur;
        sauvegarde = new HashMap<>();
    }

    public Move getBestMove(Board board, int depth){
        //variables
        Board new_Board;
        double score, score_max;
        ArrayList<Move> successeurs;
        Move next_move = null;
        //
        successeurs = board.getMoves();
        score_max = -Double.MAX_VALUE;
        for (Move move : successeurs) {
            new_Board = new Board(board.currentPlayer(), Plateau.getInstance(), board.nbTokenWhiteLeftOnBoard, board.nbTokenBlackLeftOnBoard, board.nbTokenWhiteLeftToPlay, board.nbTokenBlackLeftToPlay, board.getPosition(), this.numeroJoueur);
            new_Board.makeMove(move);
            score = eval_alpha_beta(new_Board, depth, -Double.MAX_VALUE, Double.MAX_VALUE);
            System.out.println(score);
            if (score > score_max){
                System.out.println("\t" + score);
                //System.out.println("Best move pour le joueur ordinateur avec un score de " + score +" :\n" + move);
                next_move = move;
                score_max = score;
            }
        }
        return next_move;
    }

    private double eval_alpha_beta(Board b, int depth, double alpha, double beta){
        //variables
        Board new_Board;
        double score, score_min, score_max;
        ArrayList<Move> successeurs;
        //
        if (b.isGameOver()){
            if (b.currentPlayer() == this.numeroJoueur){
                return Double.MAX_VALUE;
            }else {
                return -Double.MAX_VALUE;
            }
        }
        if (depth == 0) {
            //System.out.println("\tmax depth reached: " + b.evaluate());
            return b.evaluate();//TODO: ajouter le numero du joueur ici
        }
        successeurs = b.getMoves();
        if (b.currentPlayer() == this.numeroJoueur){
            score_max = -Double.MAX_VALUE;
            for (Move move : successeurs) {
                new_Board = new Board(b.currentPlayer(), Plateau.getInstance(), b.nbTokenWhiteLeftOnBoard, b.nbTokenBlackLeftOnBoard, b.nbTokenWhiteLeftToPlay, b.nbTokenBlackLeftToPlay, b.getPosition(), this.numeroJoueur);
                new_Board.makeMove(move);
                if (sauvegarde.containsKey(new_Board)){
                    score = sauvegarde.get(new_Board);
                }else {
                    score = eval_alpha_beta(new_Board, depth-1, alpha, beta);
                    sauvegarde.put(new_Board, score);
                }
                score_max = Double.max(score_max, score);
                if (score_max >= beta){ //coupure beta
                    return score_max;
                }
                alpha = Double.max(alpha, score_max);
            }
            return score_max;
        }else{
            score_min = Double.MAX_VALUE;
            for (Move move : successeurs) {
                new_Board = new Board(b.currentPlayer(), Plateau.getInstance(), b.nbTokenWhiteLeftOnBoard, b.nbTokenBlackLeftOnBoard, b.nbTokenWhiteLeftToPlay, b.nbTokenBlackLeftToPlay, b.getPosition(), this.numeroJoueur);
                new_Board.makeMove(move);
                if (sauvegarde.containsKey(new_Board)){
                    score = sauvegarde.get(new_Board);
                }else {
                    score = eval_alpha_beta(new_Board, depth - 1, alpha, beta);
                    sauvegarde.put(new_Board, score);
                }
                score_min = Double.min(score_min, score);
                if (score_min <= alpha){ //coupure alpha
                    return score_min;
                }
                beta = Double.min(alpha, score_min);
            }
            return score_min;
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (boardJeu.currentPlayer() == this.numeroJoueur && !boardJeu.isGameOver()){
            Move move = null;
            if (boardJeu.nbTokenBlackLeftToPlay > 0){
                move = getBestMove(boardJeu, 3);
            }else{
                move = getBestMove(boardJeu, 5);
            }
            if (boardJeu.online){
                if (this.numeroJoueur == 1){
                    try {
                        Serveur.getInstance().send(move.encode());
                    } catch (Exception e) {
                        System.out.println("ERREUR LORS DE L'ENVOIE DU MOUVEMENT: " + move.encode());
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }else {
                    try {
                        Client.getInstance().send(move.encode());
                    } catch (Exception e) {
                        System.out.println("ERREUR LORS DE L'ENVOIE DU MOUVEMENT: " + move.encode());
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            boardJeu.makeMove(move);
        }
    }
}
