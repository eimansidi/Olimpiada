package com.eiman.olimpiada.config;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageConfig {

    private static ResourceBundle currentBundle;
    private static String language = "es"; // Idioma predeterminado

    public static ResourceBundle getLanguageBundle(String languageCode) {
        Locale locale;
        if (languageCode.equalsIgnoreCase("en")) {
            locale = new Locale("en", "US");
        } else if (languageCode.equalsIgnoreCase("es")) {
            locale = new Locale("es", "ES");
        } else {
            locale = Locale.getDefault();
        }

        // Cambia "i18n.messages" a "lang.messages" para reflejar el nombre de la carpeta correcta
        currentBundle = ResourceBundle.getBundle("lang.messages", locale);
        language = languageCode;  // Actualiza el idioma actual
        return currentBundle;
    }

    public static ResourceBundle getCurrentBundle() {
        if (currentBundle == null) {
            currentBundle = getLanguageBundle(language);
        }
        return currentBundle;
    }

    public static void setLanguage(String languageCode) {
        getLanguageBundle(languageCode);
    }

    public static String getLanguage() {
        return language;
    }
}
