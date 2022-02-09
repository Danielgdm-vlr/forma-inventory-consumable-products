package com.gdm.forma.inventory.consumableproducts.app.util.service;

import java.nio.file.Path;
import java.util.Locale;

public class ResourcePath {

    public static final Path PATH_TO_LOGS;
    public static final Path PATH_TO_INFO_LOGS;
    public static final Path PATH_TO_EXCEPTION_LOGS;
    public static final String PATH_TO_IMAGES;
    public static final Locale APP_LANGUAGE;

    static {

        PATH_TO_LOGS = Path.of(String.format("%s/logs",
                System.getProperty("user.dir")));
        PATH_TO_INFO_LOGS = Path.of("logs/InfoLogger.txt");
        PATH_TO_EXCEPTION_LOGS = Path.of("logs/ExceptionLogger.txt");
        PATH_TO_IMAGES = "com/gdm/forma/inventory/consumableproducts/frontend/images";
        APP_LANGUAGE = Locale.forLanguageTag("ro");
    }
}