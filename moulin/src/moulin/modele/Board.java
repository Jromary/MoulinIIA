package moulin.modele;

import moulin.modele.connec.Client;
import moulin.modele.connec.Serveur;
import moulin.modele.graph.Connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

public class Board extends Observable{
    public boolean online = false;
    private int activePlayer; //0 = white ; 1 = black
    private Plateau plateau;
    public int nbTokenWhiteLeftOnBoard;
    public int nbTokenBlackLeftOnBoard;
    public int nbTokenWhiteLeftToPlay;
    public int nbTokenBlackLeftToPlay;
    private String[] position;
    public static int joueurOdinateur = 1;
    public Ordinateur ordinateur = null;

    /**
     * creation du jeu avec ip
     * @param ip ip du serveur sur lequel il faut ce connecter
     */
    public Board(String ip){
        this();
        this.online = true;
        if (joueurOdinateur == 1){
            Serveur.getInstance().init(this);
        }else {
            Client.getInstance().init(ip, this);
        }
    }

    /**
     * creation du jeu par default
     */
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
        ordinateur = new Ordinateur(this, 1);
        maj();
    }

    /**
     * creation de copie
     * @param activePlayer
     * @param plateau
     * @param nbTokenWhiteLeftOnBoard
     * @param nbTokenBlackLeftOnBoard
     * @param nbTokenWhiteLeftToPlay
     * @param nbTokenBlackLeftToPlay
     * @param position
     * @param joueurOdinateur
     */
    public Board(int activePlayer, Plateau plateau, int nbTokenWhiteLeftOnBoard, int nbTokenBlackLeftOnBoard, int nbTokenWhiteLeftToPlay, int nbTokenBlackLeftToPlay, String[] position, int joueurOdinateur) {
        this.activePlayer = activePlayer;
        this.plateau = plateau;
        this.nbTokenWhiteLeftOnBoard = nbTokenWhiteLeftOnBoard;
        this.nbTokenBlackLeftOnBoard = nbTokenBlackLeftOnBoard;
        this.nbTokenWhiteLeftToPlay = nbTokenWhiteLeftToPlay;
        this.nbTokenBlackLeftToPlay = nbTokenBlackLeftToPlay;
        this.position = Arrays.copyOf(position, position.length);
        this.joueurOdinateur = joueurOdinateur;
    }

    /**
     * recuperation de la liste de mouvement possible a partir d'une position actuel
     * @return ArrayList<Move>
     */
    public ArrayList<Move> getMoves(){
        ArrayList<Move> nextMoves = new ArrayList<>();

        if (currentPlayer() == 0){
            if (nbTokenWhiteLeftToPlay > 0){
                //il me reste de jeton a jouer
                for (int i = 0; i < position.length; i++) {
                    if (position[i].equals("E")){
                        if (isMoulinFromMove(i,-1)){
                            for (int j = 0; j < position.length; j++) {
                                if (position[j].equals("B")){
                                    nextMoves.add(new Move(i,-1,j));
                                }
                            }
                        }else {
                            nextMoves.add(new Move(i,-1,-1));
                        }
                    }
                }
            }else{
                if (nbTokenWhiteLeftOnBoard > 3){
                    //je n'ai plus de jeton a jouer
                    for (int i = 0; i < position.length; i++) {
                        if (position[i].equals("W")){
                            for (int adj : plateau.adj(i)) {
                                if (position[adj].equals("E")){
                                    if (isMoulinFromMove(adj, i)){
                                        for (int j = 0; j < position.length; j++) {
                                            if (position[j].equals("B")){
                                                nextMoves.add(new Move(adj, i, j));
                                            }
                                        }
                                    }else {
                                        nextMoves.add(new Move(adj, i, -1));
                                    }
                                }
                            }
                        }
                    }
                }else {
                    for (int i = 0; i < position.length; i++) {
                        if (position[i].equals("W")){
                            for (int j = 0; j < position.length; j++) {
                                if (position[j].equals("E")){
                                    if (isMoulinFromMove(j, i)){
                                        for (int k = 0; k < position.length; k++) {
                                            if (position[k].equals("B")){
                                                nextMoves.add(new Move(j,i,k));
                                            }
                                        }
                                    }else {
                                        nextMoves.add(new Move(j,i,-1));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else {//joueur Black
            if (nbTokenBlackLeftToPlay > 0){
                //il me reste de jeton a jouer
                for (int i = 0; i < position.length; i++) {
                    if (position[i].equals("E")){
                        if (isMoulinFromMove(i,-1)){
                            for (int j = 0; j < position.length; j++) {
                                if (position[j].equals("W")){
                                    nextMoves.add(new Move(i,-1,j));
                                }
                            }
                        }else {
                            nextMoves.add(new Move(i,-1,-1));
                        }
                    }
                }
            }else{
                if (nbTokenBlackLeftOnBoard > 3){
                    //je n'ai plus de jeton a jouer
                    for (int i = 0; i < position.length; i++) {
                        if (position[i].equals("B")){
                            for (int adj : plateau.adj(i)) {
                                if (position[adj].equals("E")){
                                    if (isMoulinFromMove(adj, i)){
                                        for (int j = 0; j < position.length; j++) {
                                            if (position[j].equals("W")){
                                                nextMoves.add(new Move(adj, i, j));
                                            }
                                        }
                                    }else {
                                        nextMoves.add(new Move(adj, i, -1));
                                    }
                                }
                            }
                        }
                    }
                }else {
                    for (int i = 0; i < position.length; i++) {
                        if (position[i].equals("B")){
                            for (int j = 0; j < position.length; j++) {
                                if (position[j].equals("E")){
                                    if (isMoulinFromMove(j, i)){
                                        for (int k = 0; k < position.length; k++) {
                                            if (position[k].equals("W")){
                                                nextMoves.add(new Move(j,i,k));
                                            }
                                        }
                                    }else {
                                        nextMoves.add(new Move(j,i,-1));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return nextMoves;
    }

    /**
     * methode qui applique un mouvement su le plateau actuel
     * @param move mouvement a effectuer
     */
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

    /**
     * Mise a jour des observer
     */
    private void maj() {
        setChanged();
        notifyObservers();
    }

    /**
     * premiere version de evaluate
     * @param joueur actif
     * @return
     */
    public double evaluate(int joueur){
        /*
        double alpha = 0.3;
        double eval = 0;
        int nbpairAlOrdi = nbPaireAlignee(joueurOdinateur);
        int nbpairAlNOrdi = nbPaireAlignee(Math.abs(joueurOdinateur - 1));
        eval = nbpairAlOrdi - nbpairAlNOrdi + alpha * (nbJetonNonAllignee(joueurOdinateur) - nbJetonNonAllignee(Math.abs(joueurOdinateur - 1)));
        return eval;
        */
        return evaluateBis(joueur);
    }

    /**
     * evaluate advenced
     * @param joueur actif
     * @return
     */
    private double evaluateBis(int joueur){
        double eval = 0;

        int[] coef = {2, -1, 4, 3};

        int nbpairAlLibreOrdi = nbPaireAligneeLibre(joueur);
        int nbpairAlLibreNOrdi = nbPaireAligneeLibre(Math.abs(joueur - 1));

        int nbpairAlMalOrdi = nbPaireAligneeMal(joueur);
        int nbpairAlMalNOrdi = nbPaireAligneeMal(Math.abs(joueur - 1));

        int nbMillOrdi = nbMoulin(joueur);
        int nbMillNOrdi = nbMoulin(Math.abs(joueur - 1));

        int nbMillBlockOrdi = nbMillBlock(joueur);
        int nbMillBlockNOrdi = nbMillBlock(Math.abs(joueur - 1));

        eval =  (coef[0] * (nbpairAlLibreOrdi - nbpairAlLibreNOrdi)) +
                (coef[1] * (nbpairAlMalOrdi - nbpairAlMalNOrdi)) +
                (coef[2] * (nbMillOrdi - nbMillNOrdi)) +
                (coef[3] * (nbMillBlockOrdi - nbMillBlockNOrdi));

        return eval;
    }

    /**
     * verifie nombre de moulin bloqué par joueur
     * @param joueur
     * @return
     */
    private int nbMillBlock(int joueur) {
        int nb = 0;
        String test;
        String testOP;
        if (joueur == 1){
            test = "B";
            testOP = "W";
        }else{
            test = "W";
            testOP = "B";
        }
        for (int[] moulin : plateau.lesMoulin) {
            if (position[moulin[0]].equals(test)){
                if (position[moulin[1]].equals(testOP) && position[moulin[2]].equals(testOP)){
                    nb++;
                }
            }else{
                if (position[moulin[1]].equals(test)){
                    if (position[moulin[0]].equals(testOP) && position[moulin[2]].equals(testOP)){
                        nb++;
                    }
                }else{
                    if (position[moulin[2]].equals(test)){
                        if (position[moulin[0]].equals(testOP) && position[moulin[1]].equals(testOP)){
                            nb++;
                        }
                    }
                }
            }
        }
        return nb;
    }

    /**
     * nombre de moulin fait par le joueur
     * @param joueur
     * @return
     */
    private int nbMoulin(int joueur){
        int nb = 0;
        String test;
        if (joueur == 1){
            test = "B";
        }else{
            test = "W";
        }
        for (int[] moulin : plateau.lesMoulin) {
            if (position[moulin[0]].equals(test) && position[moulin[1]].equals(test) && position[moulin[2]].equals(test)) {
                nb++;
            }
        }
        return nb;
    }

    /**
     * calcule le nombre de pair mal aligné
     * @param joueur
     * @return
     */
    private int nbPaireAligneeMal(int joueur){
        int nb = 0;
        String test;
        String testOP;
        if (joueur == 1){
            test = "B";
            testOP = "W";
        }else{
            test = "W";
            testOP = "B";
        }
        for (int[] moulin : plateau.lesMoulin) {
            if (position[moulin[0]].equals(test)){
                if (position[moulin[1]].equals(testOP) || position[moulin[2]].equals(testOP)){
                    nb++;
                }
            }else{
                if (position[moulin[1]].equals(test)){
                    if (position[moulin[0]].equals(testOP) || position[moulin[2]].equals(testOP)){
                        nb++;
                    }
                }else{
                    if (position[moulin[2]].equals(test)){
                        if (position[moulin[0]].equals(testOP) || position[moulin[1]].equals(testOP)){
                            nb++;
                        }
                    }
                }
            }
        }
        return nb;
    }

    /**
     * calcul le nombre de pair aligné libre
     * @param joueur
     * @return
     */
    private int nbPaireAligneeLibre(int joueur){
        int nb = 0;
        String test;
        if (joueur == 1){
            test = "B";
        }else{
            test = "W";
        }
        for (int[] moulin : plateau.lesMoulin) {
            if ((   position[moulin[0]].equals(test) && position[moulin[1]].equals(test) && position[moulin[2]].equals("E")) ||
                (   position[moulin[0]].equals(test) && position[moulin[1]].equals("E") && position[moulin[2]].equals(test)) ||
                (   position[moulin[0]].equals("E") && position[moulin[1]].equals(test) && position[moulin[2]].equals(test))){
                nb++;
            }
        }
        return nb;
    }

    /**
     * nombre de pair osef
     * @param joueur
     * @return
     */
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
                }
                if (position[moulin[2]].equals(test)){
                    nb++;
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

    /**
     * retourne le joueur actuel
     * @return
     */
    public int currentPlayer(){
        return activePlayer;
    }

    /**
     * verifie si le jeu est fini ou pas
     * @return
     */
    public boolean isGameOver(){
        if ((nbTokenWhiteLeftOnBoard + nbTokenWhiteLeftToPlay <= 2) || (nbTokenBlackLeftOnBoard + nbTokenBlackLeftToPlay <= 2)) {
            return true;
        }else{
            if (getMoves().size() <= 0 || getMoves().isEmpty()){
                return true;
            }
        }
        return false;
    }

    public boolean whiteWin(){
        return (nbTokenBlackLeftOnBoard + nbTokenBlackLeftToPlay <= 2) || (currentPlayer() == 1 && getMoves().size() <= 0);
    }

    /**
     * retourne le tableau des position
     * @return
     */
    public String[] getPosition() {
        return position;
    }

    /**
     * la case est elle jouable depuis la ou je vien
     * @param caseABouger
     * @param caseJouee
     * @return
     */
    public boolean caseJouable(int caseABouger, int caseJouee) {
        if(caseABouger == caseJouee || !position[caseJouee].equals("E") ){
            return false;
        }
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

    /**
     * je fait un moulin en jouaunt ?
     * @param caseJoue
     * @param caseFrom
     * @return
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.equals(position, board.position);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(position);
    }
}

