package controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import models.Sound;

public class SoundController {
    private static final String SOUND_ON_IMG_URL = "file:src/resources/images/sound_on.jpg";
    private static final String SOUND_OFF_IMG_URL = "file:src/resources/images/sound_off.jpg";

    //Variable checking if the sound of the system is on, sound is ON by default
    private static boolean isEnabledSound = true;
    //Image object for loading "sound on" image from resources
    private static Image soundOnImg = new Image(SOUND_ON_IMG_URL);
    //Image object for loading "sound off" image from resources
    private static Image soundOffImg = new Image(SOUND_OFF_IMG_URL);
    //Background object for setting "sound on" image as soundEnabledBtn background image
    public static Background bgSoundOnImg = generateSoundButtonBackground(soundOnImg);
    //Background object for setting "sound off" image as soundEnabledBtn background image
    public static Background bgSoundOffImg = generateSoundButtonBackground(soundOffImg);
    //Sound object for playing background music
    private static Sound backgroundMusic = new Sound(Sound.Type.BACKGROUND_MUSIC);
    //Button sound object, for making sound when pressing the sound enabled button
    private static Sound buttonClickedSound = new Sound(Sound.Type.BUTTON_CLICK_SFX);
    private static Sound diceRollSound = new Sound(Sound.Type.DICE_ROLL_SFX);
    private static Sound horseJumpSound = new Sound(Sound.Type.HORSE_JUMP_SFX);
    private static Sound horseMoveSound = new Sound(Sound.Type.HORSE_MOVE_SFX);
    private static Sound horseAppearSound = new Sound(Sound.Type.HORSE_APPEAR_SFX);
    private static Sound horseKickedSound = new Sound(Sound.Type.HORSE_KICKED_SFX);
    private static Sound gameLaunchSound = new Sound(Sound.Type.GAME_LAUNCH_SFX);

    // generating sound button background
    private static Background generateSoundButtonBackground(Image img) {
        return new Background(new BackgroundImage(img,
                                                BackgroundRepeat.NO_REPEAT,
                                                BackgroundRepeat.NO_REPEAT,
                                                BackgroundPosition.CENTER,
                                                new BackgroundSize(100, 100,
                                                true, true, true, false)));
    }

    // check if sound is enabled
    public static boolean isSoundEnabled() {
        return isEnabledSound;
    }

    // toggle sound on and off
    public static void toggleSound() {
        if (isSoundEnabled()) {
            isEnabledSound = false;
            muteSound();
        } else {
            isEnabledSound = true;
            unmuteSound();
        }
    }

    // stop all sounds
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

    // resume the background music
    private static void unmuteSound() {
        backgroundMusic.play();
    }

    // play sound if sound is enabled
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