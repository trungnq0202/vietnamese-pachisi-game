package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18NController {
    //FXML
    @FXML private Button englishLangBtn;
    @FXML private Button vietnameseLangBtn;

    // Attributes
    private MainController mainController;
    private ResourceBundle bundle;

    public enum Language { ENGLISH, VIETNAMESE }

    public I18NController() {
        // constructing this controller with English as the default language
        this.bundle = ResourceBundle.getBundle("lang", getLanguageLocale(Language.ENGLISH));
    }

    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void initialize() {
        setMenuButtonsEventHandler();
    }

    // Set event handler for all language menu buttons
    private void setMenuButtonsEventHandler(){
        setEnglishLangBtnEventHandler();

    }

    // Event handler for the englishLangBtn
    private void setEnglishLangBtnEventHandler(){
        englishLangBtn.setOnMouseClicked(event -> {
            changeLanguage(Language.ENGLISH);
        });
    }

    // Event handler for the vietnameseLangBtn
    private void setVietnameseLangBtnEventHandler(){
        vietnameseLangBtn.setOnMouseClicked(event -> {
            changeLanguage(Language.VIETNAMESE);
        });
    }

    // Get the Locale object of a language
    private Locale getLanguageLocale(Language language) {
        switch (language) {
            case ENGLISH: {
                return new Locale("en", "US");
            }
            case VIETNAMESE: {
                return new Locale("vi", "VN");
            }
            default:
                return new Locale("en", "US");
        }
    }

    // Change the language
    private void changeLanguage(Language language) {
        Locale.setDefault(getLanguageLocale(language));
        // reload language bundle
        this.bundle = ResourceBundle.getBundle("lang");
    }

    // Return text message from key value
    public String text(String key) {
        return this.bundle.getString(key);
    }

}
