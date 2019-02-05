package moulin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import moulin.modele.Board;
import moulin.modele.graph.Graph;
import moulin.vue.board.BoardControler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Board board = new Board();
        BoardControler boardControler = new BoardControler(board);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/moulin/vue/board/board.fxml"));
        loader.setControllerFactory(iC -> boardControler);
        Parent vuePlateau = loader.load();

        BorderPane bp = new BorderPane();
        bp.setCenter(vuePlateau);

        primaryStage.setTitle("Jeu du moulin");
        primaryStage.setScene(new Scene(bp, 700, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
