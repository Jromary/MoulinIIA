package moulin.modele;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Ordinateur implements Observer {
    private Board boardJeu;
    private int numeroJoueur;

    public Ordinateur(Board boardJeu, int numeroJoueur){
        this.boardJeu = boardJeu;
        boardJeu.addObserver(this);
        this.numeroJoueur = numeroJoueur;
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
            if (score > score_max){
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
                score_max = Double.max(score_max, eval_alpha_beta(new_Board, depth-1, alpha, beta));
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
                score_min = Double.min(score_min, eval_alpha_beta(new_Board, depth-1, alpha, beta));
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
            System.out.println("Mouvement choisi:" + move);
            boardJeu.makeMove(move);
        }
    }
}
