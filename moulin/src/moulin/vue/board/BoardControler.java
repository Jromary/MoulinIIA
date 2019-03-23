package moulin.vue.board;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import moulin.modele.Board;
import moulin.modele.Move;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class BoardControler implements Observer {
    @FXML
    public AnchorPane boardPane;

    private Board board;
    private ArrayList<Button> cases;

    private Move move;
    private int phase;

    public BoardControler(Board board){
        super();
        this.board = board;
        this.cases = new ArrayList<>(24);
        board.addObserver(this);
    }

    @FXML
    public void initialize() {
        // recuperation de toutes les cases du plateau de jeu
        for(Node n : boardPane.getChildren()){
            this.cases.add((Button) n);
        }
//        boardPane.setStyle("-fx-background-color: #2F1F00");
//        boardPane.setStyle("-fx-background-image: 'ressources/moulin.png'");
        BackgroundImage myBI= new BackgroundImage(new Image("ressources/moulin.png",700,700,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        boardPane.setBackground(new Background(myBI));

        update(board, "");
    }

    private void deactivatePlayer(int joueur){
        if (joueur == 0){
            for (int i = 0; i < 24; i++){
                switch (board.getPosition()[i]){
                    case "B":{
                        this.cases.get(i).setMouseTransparent(true);
                        break;
                    }
                    default:{
                    }
                }
            }
        }else{
            if (joueur == 1){
                for (int i = 0; i < 24; i++){
                    switch (board.getPosition()[i]){
                        case "W":{
                            this.cases.get(i).setMouseTransparent(true);
                            break;
                        }
                        default:{
                        }
                    }
                }
            }
        }
    }

    private void deactivateEmpty(){
        for (int i = 0; i < 24; i++){
            switch (board.getPosition()[i]){
                case "E":{
                    this.cases.get(i).setMouseTransparent(true);
                    break;
                }
                default:{
                }
            }
        }
    }

    private void activateAll(){
        for (Button b : this.cases) {
            b.setMouseTransparent(false);
        }
    }



    @Override
    public void update(Observable o, Object arg) {
        //for (String s : board.getPosition()) {
        //    System.out.print(s+" ");
        //}
        //System.out.println();
        //mise a jour des case en fonction du modéle
        for (int i = 0; i < 24; i++){
            switch (board.getPosition()[i]){
                case "B":{
                    this.cases.get(i).setStyle("-fx-background-color: #000000");
                    break;
                }
                case "W":{
                    this.cases.get(i).setStyle("-fx-background-color: #ffffff");
                    break;
                }
                default:{
                    this.cases.get(i).setStyle("-fx-background-color: #7f7f7f");
                }
            }
        }
        move = new Move(-1, -1, -1);

        // activation des cases puis desactivation des case qui ne doivent pas etre jouer
        activateAll();
        // si c'est au joueur un de jouer
        if (board.currentPlayer() == 0){
            // si il lui reste des pieces a jouer on desactive tout sauf la ou il peut placer
            if (board.nbTokenWhiteLeftToPlay > 0){
                deactivatePlayer(0);
                deactivatePlayer(1);
                this.phase = 1;
            }else{
                // sinon on desactive tout sauf ses pieces qu'il peut deplacer
                deactivateEmpty();
                deactivatePlayer(0);
                this.phase = 2;
            }
        }else{

            if (board.currentPlayer() == 1){
                // si il lui reste des pieces a jouer on desactive tout sauf la ou il peut placer
                if (board.nbTokenBlackLeftToPlay > 0){
                    deactivatePlayer(0);
                    deactivatePlayer(1);
                    this.phase = 1;
                }else{
                    // sinon on desactive tout sauf ses pieces qu'il peut deplacer
                    deactivateEmpty();
                    deactivatePlayer(1);
                    this.phase = 2;
                }
            }
        }

        if (!(board.ordinateur == null) && board.currentPlayer() == board.joueurOdinateur){
            deactivatePlayer(1);
            deactivatePlayer(0);
            deactivateEmpty();
        }

        if (board.isGameOver()){
            System.out.println("Partie Finie");
            deactivateEmpty();
            deactivatePlayer(1);
            deactivatePlayer(0);
        }
    }

    public void play(ActionEvent actionEvent) {
        int caseJouee = cases.indexOf((Button) actionEvent.getSource());
        if (phase == 1){
            if (caseJouee == move.getDelPiece()){
                phase = 2;
                move = new Move(-1, -1, -1);
                update(board, "");
            }else{
                if (board.caseJouable(move.getDelPiece(), caseJouee) || ((board.currentPlayer() == 0)? board.nbTokenWhiteLeftOnBoard <= 3: board.nbTokenBlackLeftOnBoard <= 3)){
                    move.setAddPiece(caseJouee);
                    if (board.isMoulinFromMove(caseJouee, move.getDelPiece())){
                        for (int i = 0; i < 24; i++){
                            switch (board.getPosition()[i]){
                                case "B":{
                                    if (board.currentPlayer() == 0){
                                        this.cases.get(i).setStyle("-fx-background-color: #000000; -fx-border-color: orange; -fx-border-width: 5");
                                    }
                                    break;
                                }
                                case "W":{
                                    if (board.currentPlayer() == 1){
                                        this.cases.get(i).setStyle("-fx-background-color: #ffffff; -fx-border-color: orange; -fx-border-width: 5");
                                    }
                                    break;
                                }
                                default:{
                                }
                            }
                        }
                        activateAll();
                        if (board.currentPlayer() == 0){
                            deactivatePlayer(1);
                        }else{
                            deactivatePlayer(0);
                        }
                        deactivateEmpty();
                        phase = 3;
                    }else{
                        board.makeMove(move);
                    }
                }
            }
        }else{
            if (phase == 2){
                move.setDelPiece(caseJouee);
                activateAll();
                deactivatePlayer(board.currentPlayer());
                deactivatePlayer(Math.abs(board.currentPlayer() - 1));
                cases.get(caseJouee).setMouseTransparent(false);
                phase = 1;
                if (board.currentPlayer() == 0){
                    cases.get(caseJouee).setStyle("-fx-border-color: red; -fx-background-color: #FFFFFF; -fx-border-width: 5");
                }else{
                    cases.get(caseJouee).setStyle("-fx-border-color: red; -fx-background-color: #000000; -fx-border-width: 5");
                }
            }else {
                if (phase == 3){
                    move.setDelPieceE(caseJouee);
                    board.makeMove(move);
                }
            }
        }
    }
}
