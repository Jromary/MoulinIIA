package moulin.vue.board;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import moulin.modele.Board;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class BoardControler implements Observer {
    @FXML
    public AnchorPane boardPane;

    private Board board;
    private ArrayList<Button> cases;

    public BoardControler(Board board){
        super();
        this.board = board;
        this.cases = new ArrayList<>(24);
        for(Node n : boardPane.getChildren()){
            this.cases.add((Button) n);
        }
        board.addObserver(this);
    }

    @FXML
    public void initialize() {
        update(board, "");
    }

    public void test(ActionEvent actionEvent) {
        System.out.println(actionEvent.getSource().toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        for (int i = 0; i < 23; i++){
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
                    System.out.println(this.cases.get(i));
                    this.cases.get(i).setStyle("-fx-background-color: #2f2f2f");
                }
            }
        }
    }
}
