package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import models.Sound;

public class SoundController {
    private static final String SOUND_ON_IMG_URL = "file:src/resources/images/sound_on.jpg";
    private static final String SOUND_OFF_IMG_URL = "file:src/resources/images/sound_off.jpg";

    @FXML private Button soundEnabledBtn;              //Sound enabled Button in fxml
    private MainController mainController;            //Communicate with others controller through maincontroller

    private Sound btnSound;                           //Button sound object, for making sound when pressing the sound enabled button
    private boolean isEnabledSound;                   //Variable checking if the sound of the system is on
    private Image soundOnImg;                         //Image object for loading "sound on" image from resources
    private Image soundOffImg;                        //Image object for loading "sound off" image from resources
    private Background bgSoundOnImg;                  //Background object for setting "sound on" image as soundEnabledBtn background image
    private Background bgSoundOffImg;                 //Background object for setting "sound off" image as soundEnabledBtn background image
    private Sound backgroundMusic;                    //Sound object for playing background music

    //Injecting maincontroller
    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

    public SoundController(){
        isEnabledSound = true;                                                        //Sound is enabled at first
        btnSound = new Sound(Sound.SoundType.BUTTON_CLICK_SFX);                              //Create a "button sound" object
        backgroundMusic = new Sound(Sound.SoundType.BACKGROUND_MUSIC);                   //Create a "background music" sound object
        soundOnImg = new Image(SOUND_ON_IMG_URL);         //Load "sound on" image object for sound enabled button
        soundOffImg = new Image(SOUND_OFF_IMG_URL);       //Load "sound off" image object for sound enabled button
    }

    public void initialize(){
        setSoundEnableButtonImage();    //Create BackgroundImage object for setting background image of the sound button
        setSoundEnableBtnEventHandler();      //Set On Mouse Click event handler for the sound enabled button
    }

    public void setSoundEnableButtonImage(){
        bgSoundOnImg = new Background(new BackgroundImage(soundOnImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(soundEnabledBtn.getWidth(), soundEnabledBtn.getHeight(), true, true, true, false)));
        bgSoundOffImg = new Background(new BackgroundImage(soundOffImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(soundEnabledBtn.getWidth(), soundEnabledBtn.getHeight(), true, true, true, false)));
        soundEnabledBtn.setBackground(bgSoundOnImg);    //At first, the sound is already on and the background image is "sound on" image
    }

    public void setSoundEnableBtnEventHandler() {
        soundEnabledBtn.setOnMouseClicked((MouseEvent e) -> {
            btnSound.play();                                   //Make "button sound" when clicked
            //If the system's sound is enabled
            if (isEnabledSound){
                isEnabledSound = false;                         //Set the system's sound to mute state
                backgroundMusic.pause();                        //Stop playing background music
                soundEnabledBtn.setBackground(bgSoundOffImg);   //Set "sound off" background image for soundEnabledBtn
            }else{ //If the system's sound is being muted
                isEnabledSound = true;                          //Set the system's sound to on state
                backgroundMusic.play();                         //Resume playing background music
                soundEnabledBtn.setBackground(bgSoundOnImg);    //Set "sound on" background image for soundEnabledBtn
            }
        });
    }

    public boolean isSoundEnabled(){
        return isEnabledSound;
    }
}