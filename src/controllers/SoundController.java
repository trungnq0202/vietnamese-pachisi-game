package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import models.Sound;

public final class SoundController {
    private static final String SOUND_ON_IMG_URL = "file:src/resources/images/sound_on.jpg";
    private static final String SOUND_OFF_IMG_URL = "file:src/resources/images/sound_off.jpg";

    //Sound enabled Button in fxml
    @FXML private static Button soundEnabledBtn;
    //Variable checking if the sound of the system is on, sound is ON by default
    private static boolean isEnabledSound = true;
    //Image object for loading "sound on" image from resources
    private static Image soundOnImg = new Image(SOUND_ON_IMG_URL);
    //Image object for loading "sound off" image from resources
    private static Image soundOffImg = new Image(SOUND_OFF_IMG_URL);
    //Background object for setting "sound on" image as soundEnabledBtn background image
    private static Background bgSoundOnImg = generateSoundButtonBackground(soundOnImg);
    //Background object for setting "sound off" image as soundEnabledBtn background image
    private static Background bgSoundOffImg = generateSoundButtonBackground(soundOffImg);
    //Sound object for playing background music
    private static Sound backgroundMusic = new Sound(Sound.SoundType.BACKGROUND_MUSIC);
    //Button sound object, for making sound when pressing the sound enabled button
    private static Sound buttonClickedSound = new Sound(Sound.SoundType.BUTTON_CLICK_SFX);
    private static Sound diceRollSound = new Sound(Sound.SoundType.DICE_ROLL_SFX);
    private static Sound horseJumpSound = new Sound(Sound.SoundType.HORSE_JUMP_SFX);
    private static Sound horseMoveSound = new Sound(Sound.SoundType.HORSE_MOVE_SFX);
    private static Sound horseAppearSound = new Sound(Sound.SoundType.HORSE_APPEAR_SFX);
    private static Sound horseKickedSound = new Sound(Sound.SoundType.HORSE_KICKED_SFX);
    private static Sound gameLaunchSound = new Sound(Sound.SoundType.GAME_LAUNCH_SFX);

    public static void initialize() {
        //Set On Mouse Click event handler for the sound enabled button
        setSoundEnableBtnEventHandler();
    }

    private static Background generateSoundButtonBackground(Image img) {
        return new Background(new BackgroundImage(img,
                                                BackgroundRepeat.NO_REPEAT,
                                                BackgroundRepeat.NO_REPEAT,
                                                BackgroundPosition.CENTER,
                                                new BackgroundSize(soundEnabledBtn.getWidth(),
                                                soundEnabledBtn.getHeight(),
                                                true, true, true, false)));
    }

    private static void setSoundEnableBtnEventHandler() {
        soundEnabledBtn.setOnMouseClicked((MouseEvent e) -> {
            //If the system's sound is enabled
            if (isEnabledSound) {
                isEnabledSound = false;                         //Set the system's sound to mute state
                muteSound();                                    //Stop playing background music
                soundEnabledBtn.setBackground(bgSoundOffImg);   //Set "sound off" background image for soundEnabledBtn
            } else { //If the system's sound is being muted
                buttonClickedSound.play();                                //Make "button sound" when clicked
                isEnabledSound = true;                          //Set the system's sound to on state
                unmuteSound();                                  //Resume playing background music
                soundEnabledBtn.setBackground(bgSoundOnImg);    //Set "sound on" background image for soundEnabledBtn
            }
        });
    }

    private static boolean isSoundEnabled() {
        return isEnabledSound;
    }

    private static void muteSound() {
        backgroundMusic.pause();
        diceRollSound.stop();
        horseJumpSound.stop();
        horseMoveSound.stop();
        horseAppearSound.stop();
        buttonClickedSound.stop();
        horseKickedSound.stop();
        gameLaunchSound.stop();
    }
    private static void unmuteSound() {
        backgroundMusic.play();
    }

    private static void playSound(Sound sound) {
        if (isSoundEnabled()) sound.play();
    }

    public static void playBackgroundSound() {
        playSound(backgroundMusic);
    }

    public static void playDiceRollSound() {
        playSound(diceRollSound);
    }

    public static void stopDiceRollSound() {
        diceRollSound.stop();
    }

    public static void playHorseJumpSound() {
        playSound(horseJumpSound);
    }

    public static void playHorseMoveSound() {
        playSound(horseMoveSound);
    }

    public static void playHorseAppearSound() {
        playSound(horseAppearSound);
    }

    public static void playButtonClickSound() {
        playSound(buttonClickedSound);
    }

    public static void playHorseKickedSound() {
        playSound(horseKickedSound);
    }

    public static void playGameLaunchSound() {
        playSound(gameLaunchSound);
    }
}