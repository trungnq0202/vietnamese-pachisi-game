import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        System.out.println(this.getClass().getResource("resources/videos/loopbackgroundvideo.mp4").toExternalForm());
//
//        StackPane root = new StackPane();
//
//        MediaPlayer player = new MediaPlayer( new Media(getClass().getResource("resources/videos/loopbackgroundvideo.mp4").toExternalForm()));
//        player.setCycleCount(MediaPlayer.INDEFINITE);
//        player.setAutoPlay(true);
//        MediaView mediaView = new MediaView(player);
//
//        root.getChildren().add( mediaView);
//
//        Scene scene = new Scene(root, 1024, 768);
//
//        primaryStage.setScene(scene);
//        primaryStage.show();

        Parent root = FXMLLoader.load(getClass().getResource("resources/view_fxml/main.fxml"));
        primaryStage.setTitle("Co Ca Ngua");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
