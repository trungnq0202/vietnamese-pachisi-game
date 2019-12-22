import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/view_fxml/game_board.fxml"));
        primaryStage.setTitle("Co ca ngua");
        primaryStage.setScene(new Scene(root,1500,1100));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
