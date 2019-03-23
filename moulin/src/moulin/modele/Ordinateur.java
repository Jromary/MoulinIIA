package moulin.modele;

import moulin.modele.connec.Client;
import moulin.modele.connec.Serveur;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Ordinateur implements Observer {
    private Board boardJeu;
    private int numeroJoueur;
    //private HashMap<Board, Double> sauvegarde;
    //utilisation de concurenthashmap pour l'utilisation des thread et eviter les acces concurentiel
    private ConcurrentHashMap<Board, Double> sauvegardebis;

    private ArrayList<Thread> threads;

    /**
     * constructeur
     * @param boardJeu
     * @param numeroJoueur
     */
    public Ordinateur(Board boardJeu, int numeroJoueur){
        this.boardJeu = boardJeu;
        boardJeu.addObserver(this);
        this.numeroJoueur = numeroJoueur;
        //sauvegarde = new HashMap<>();
        sauvegardebis = new ConcurrentHashMap<>();
    }

    /**
     * retourne le meilleur mouvement
     * @param board
     * @param depth
     * @return
     */
    public Move getBestMove(Board board, int depth){
        //variables
        final Board[] new_Board = new Board[1];
        double score, score_max;
        ArrayList<Move> successeurs;
        Move next_move = null;
        ArrayList<Move> nextPotentiel = new ArrayList<>();
        //

        HashMap<Double, Move> lesEvals = new HashMap<>();

        threads = new ArrayList<>();


        successeurs = board.getMoves();
        score_max = -Double.MAX_VALUE;
        for (Move move : successeurs) {
            // lance un thread pour allez plus vite
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //
                    new_Board[0] = new Board(board.currentPlayer(), Plateau.getInstance(), board.nbTokenWhiteLeftOnBoard, board.nbTokenBlackLeftOnBoard, board.nbTokenWhiteLeftToPlay, board.nbTokenBlackLeftToPlay, board.getPosition(), numeroJoueur);
                    new_Board[0].makeMove(move);
                    lesEvals.put(eval_alpha_beta(new_Board[0], depth, -Double.MAX_VALUE, Double.MAX_VALUE), move);
                }
            });
            thread.start();
            threads.add(thread);
        }
        // on attend que tout les thread est finie, comme ça on a vue tt les etats
        for (Thread t :
                threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // on coupe les thread, ils ne servent plus a rien
        for (Thread t :
                threads) {
            t.interrupt();
        }
        threads = new ArrayList<>();
        //on reccup les moves
        for (Map.Entry eval : lesEvals.entrySet()) {
            if ((Double)eval.getKey() >= score_max){
                if ((Double)eval.getKey() > score_max){
                    nextPotentiel = new ArrayList<>();
                }
                nextPotentiel.add((Move)eval.getValue());
                //next_move = (Move)eval.getValue();
                score_max = (Double)eval.getKey();
            }
        }


        //return next_move;
        return nextPotentiel.get((int) (Math.random()*nextPotentiel.size()));// on sort un move aleatoire si on a le meme score pour plusieur
    }

    private double eval_alpha_beta(Board b, int depth, double alpha, double beta){
        //variables
        Board new_Board;
        double score, score_min, score_max;
        ArrayList<Move> successeurs;
        //
        if (b.isGameOver()){
            if (b.currentPlayer() == this.numeroJoueur){
                return -Double.MAX_VALUE;
            }else {
                return Double.MAX_VALUE;
            }
        }
        if (depth == 0) {
            //System.out.println("\tmax depth reached: " + b.evaluate());
            return b.evaluate(this.numeroJoueur);
        }
        successeurs = b.getMoves();
        if (b.currentPlayer() == this.numeroJoueur){
            score_max = -Double.MAX_VALUE;
            for (Move move : successeurs) {
                new_Board = new Board(b.currentPlayer(), Plateau.getInstance(), b.nbTokenWhiteLeftOnBoard, b.nbTokenBlackLeftOnBoard, b.nbTokenWhiteLeftToPlay, b.nbTokenBlackLeftToPlay, b.getPosition(), this.numeroJoueur);
                new_Board.makeMove(move);
                if (sauvegardebis.containsKey(new_Board)){
                    score = sauvegardebis.get(new_Board);
                }else {
                    score = eval_alpha_beta(new_Board, depth-1, alpha, beta);
                    sauvegardebis.put(new_Board, score);
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
                if (sauvegardebis.containsKey(new_Board)){
                    score = sauvegardebis.get(new_Board);
                }else {
                    score = eval_alpha_beta(new_Board, depth - 1, alpha, beta);
                    sauvegardebis.put(new_Board, score);
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
                move = getBestMove(boardJeu, 7);
            }else{
                move = getBestMove(boardJeu, 9);
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
