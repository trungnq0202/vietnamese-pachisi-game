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

package controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public final class I18NController {
    // fields
    public enum Language {ENGLISH, VIETNAMESE}
    private static final ObjectProperty<Locale> locale;

    static {
        locale = new SimpleObjectProperty<>(getLanguageLocale(Language.ENGLISH));
        locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
    }

    public static Locale getLocale() {
        return locale.get();
    }

    public static void setLocale(Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    public static ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    public static String get(final String key, final Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("lang", getLocale());
        return MessageFormat.format(bundle.getString(key), args);
    }

    // create string binding
    public static StringBinding createStringBinding(final String key, Object... args) {
        return Bindings.createStringBinding(() -> get(key, args), locale);
    }

    // setup label text
    public static void setUpLabelText(Label label, final String key, final Object... args) {
        label.textProperty().bind(createStringBinding(key));
    }

    // setup button text
    public static void setUpButtonText(Button button, final String key, final Object... args) {
        button.textProperty().bind(createStringBinding(key));
    }

    // Get the Locale object of a language
    public static Locale getLanguageLocale(Language language) {
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

    // switch language
    public static void switchLanguage(Locale locale) {
        setLocale(locale);
    }

    // check if current locale is US
    public static boolean isEnglish(){
        return locale.get().getDisplayCountry().equals("United States");
    }
}