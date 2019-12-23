package resources.models;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class BackgroundVideo {
    private static final String VIDEO_URL = "../videos/loopbackgroundvideo.mp4";
    private MediaView mediaView;
    private MediaPlayer mediaPlayer;

    public BackgroundVideo(){
        System.out.println(getClass().getResource(VIDEO_URL).toExternalForm());
        mediaPlayer = new MediaPlayer( new Media(getClass().getResource(VIDEO_URL).toExternalForm()));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);
        mediaView = new MediaView(mediaPlayer);
    }

    public MediaView getMediaView() {
        return mediaView;
    }


}
