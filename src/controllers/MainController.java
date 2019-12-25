package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MainController {
    @FXML private StackPane root;
    @FXML private StackPane menu;
    @FXML private VBox gameboard;
    @FXML private MediaView backgroundVideo;
    @FXML private MenuController menuController;
    private static final String VIDEO_URL = "../resources/videos/loopbackgroundvideo.mp4";

    public MainController(){
    }

    @FXML private void initialize(){
        injectMainControllerInNestedControllers();
        setBackgroundVideo();
    }

    private void injectMainControllerInNestedControllers(){
        menuController.injectMainController(this);
    }


    private void setBackgroundVideo(){
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(getClass().getResource(VIDEO_URL).toExternalForm()));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);
        backgroundVideo.setMediaPlayer(mediaPlayer);
    }


}
