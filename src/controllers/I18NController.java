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
    // Attributes
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

    public static StringBinding createStringBinding(final String key, Object... args) {
        return Bindings.createStringBinding(() -> get(key, args), locale);
    }

    public static StringBinding createStringBinding(Callable<String> func) {
        return Bindings.createStringBinding(func, locale);
    }

    public static void setUpLabelText(Label label, final String key, final Object... args) {
        label.textProperty().bind(createStringBinding(key));
    }

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

    public static void switchLanguage(Locale locale) {
        setLocale(locale);
    }

    public static boolean isVietnamese(){
        return locale.get().getDisplayCountry().equals("Vietnam");
    }

    public static boolean isEnglish(){
        return locale.get().getDisplayCountry().equals("United States");
    }
}