package com.cabinet.client.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private static Locale currentLocale = Locale.ENGLISH;

    public static void setLanguage(String language) {
        switch (language) {
            case "RO":
                currentLocale = new Locale("ro");
                break;
            case "DE":
                currentLocale = Locale.GERMAN;
                break;
            default:
                currentLocale = Locale.ENGLISH;
        }
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle(
                "i18n.messages",
                currentLocale
        );
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}