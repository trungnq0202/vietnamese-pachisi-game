/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2019C
  Assessment: Final Project
  Created date: 01/01/2020
  By: Group 10 (3426353,3791159,3742774,3748575,3695662)
  Last modified: 14/01/2020
  By: Group 10 (3426353,3791159,3742774,3748575,3695662)
  Acknowledgement: none.
*/

package models;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public class Sound {
    // resource urls
    private static final String BACKGROUND_MUSIC_URL = "../resources/sounds/background.mp3";
    private static final String DICE_ROLL_SFX_URL = "../resources/sounds/sound_dice.mp3";
    private static final String HORSE_JUMP_SFX_URL = "../resources/sounds/sound_jump.wav";
    private static final String HORSE_MOVE_SFX_URL = "../resources/sounds/sound_horse_move.wav";
    private static final String HORSE_APPEAR_SFX_URL = "../resources/sounds/sound_appear.wav";
    private static final String BUTTON_CLICK_SFX_URL = "../resources/sounds/sound_button_click.mp3";
    private static final String HORSE_KICKED_SFX_URL = "../resources/sounds/sound_horsekicked.mp3";
    private static final String GAME_LAUNCH_SFX_URL = "../resources/sounds/sound_launch.mp3";
    private static final String NULL_SOUND_URL = "../resources/sounds/backgroundsong.wav";

    // fields
    public enum Type {
        BACKGROUND_MUSIC,
        DICE_ROLL_SFX,
        HORSE_JUMP_SFX,
        HORSE_MOVE_SFX,
        HORSE_APPEAR_SFX,
        BUTTON_CLICK_SFX,
        HORSE_KICKED_SFX,
        GAME_LAUNCH_SFX
    }
    private MediaPlayer sound;

    // constructor
    public Sound(Type soundType) {
        try {
            this.sound = new MediaPlayer(new Media(getResourceURL(soundType)));
        } catch (Exception e) {
            System.out.println("Cannot load sound.");
        }
        if (soundType == Type.BACKGROUND_MUSIC || soundType == Type.DICE_ROLL_SFX) {
            this.sound.setCycleCount(MediaPlayer.INDEFINITE);
        }
    }

    // get resource url from sound type
    private String getResourceURL(Type soundType) {
        URL resource;
        switch (soundType) {
            case BACKGROUND_MUSIC: {
                resource = getClass().getResource(BACKGROUND_MUSIC_URL);
                break;
            }
            case DICE_ROLL_SFX: {
                resource = getClass().getResource(DICE_ROLL_SFX_URL);
                break;
            }
            case HORSE_APPEAR_SFX: {
                resource = getClass().getResource(HORSE_APPEAR_SFX_URL);
                break;
            }
            case HORSE_JUMP_SFX: {
                resource = getClass().getResource(HORSE_JUMP_SFX_URL);
                break;
            }
            case HORSE_MOVE_SFX: {
                resource = getClass().getResource(HORSE_MOVE_SFX_URL);
                break;
            }
            case BUTTON_CLICK_SFX: {
                resource = getClass().getResource(BUTTON_CLICK_SFX_URL);
                break;
            }
            case HORSE_KICKED_SFX:{
                resource = getClass().getResource(HORSE_KICKED_SFX_URL);
                break;
            }
            case GAME_LAUNCH_SFX:{
                resource = getClass().getResource(GAME_LAUNCH_SFX_URL);
                break;
            }
            default: {
                // just to make sure resource var is initiated
                resource = getClass().getResource(NULL_SOUND_URL);
                break;
            }
        }
        return resource.toString();
    }

    // play audio
    public void play() {
        this.sound.seek(Duration.ZERO);
        this.sound.play();
    }

    // pause audio
    public void pause() {
        this.sound.pause();
    }

    // stop audio
    public void stop() {
        this.sound.stop();
    }
}