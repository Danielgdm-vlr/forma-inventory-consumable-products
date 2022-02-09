package com.gdm.forma.inventory.consumableproducts.app.util.logger;

import com.gdm.forma.inventory.consumableproducts.app.util.service.ExceptionService;
import com.gdm.forma.inventory.consumableproducts.app.util.service.ResourcePath;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Logger {

    public static void log(LogType logType, String logMessage){

        switch (logType) {
            case INFO:
                logAction(ResourcePath.PATH_TO_INFO_LOGS, logMessage);
                break;
            case EXCEPTION:
                logMessage = ExceptionService.formatLogMessageAsException(logMessage);
                logAction(ResourcePath.PATH_TO_EXCEPTION_LOGS, logMessage);
                break;
        }
    }

    private static void logAction(Path pathToFile, String logMessage){

        /*
            first check if the folder "logs" is present
            if it is, write to the file pointed to by the path
            else, create the folder then the method Files.writeString will create the txt file if it not found
         */

        if(!Files.exists(ResourcePath.PATH_TO_LOGS)){
            /*
                create the folder
             */
            try {
                Files.createDirectory(ResourcePath.PATH_TO_LOGS);
            } catch (Exception exception) {
                Logger.log(LogType.EXCEPTION, Arrays.toString(
                        ExceptionService.getExceptionMessageAsStringArray(exception)));
            }
        }

        try {
            writeNewMessage(pathToFile, logMessage);
        }catch (Exception exception){
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
        }
    }

    /*
        metoda aceasta contine scrierea in fisier,
        mereu la inceputul fisierului
     */
    private static void writeNewMessage(Path pathToFile, String logMessage) throws Exception{

        if(!Files.exists(pathToFile) || filesExistsButIsEmpty(pathToFile)){
            /*
                fisierul nu exista => putem scrie direct,
                deoarece va fi primul mesaj, deci mereu in partea de sus a fisierului
             */
            writeMessage(pathToFile, logMessage);
            return;
        }
        /*
            fisierul exista si exista si continut in el
            incerc sa scriu mereu la inceputul fisierului,
            adica preiau continutul fisierului vechi
            sterg fisierul vechi
            scriu iar continutul
        */
        StringBuilder oldFileContent = getOldFileContent(pathToFile);

        logMessage = String.format("%1$s\n%2$s",
                logMessage,
                oldFileContent);

        writeMessage(pathToFile, logMessage);
    }

    private static void writeMessage(Path pathToFile, String logMessage) throws Exception{

        Files.writeString(pathToFile,
                String.format("         Data: %1$s - Ora: %2$s\n%3$s",
                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy",
                                ResourcePath.APP_LANGUAGE)),
                        LocalTime.now()
                                .truncatedTo(ChronoUnit.SECONDS)
                                .format(DateTimeFormatter.ISO_LOCAL_TIME),
                        logMessage),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }



    private static StringBuilder getOldFileContent(Path pathToFile) throws Exception {

        Stream<String> lines = Files.lines(pathToFile);
        List<String> lineList = new ArrayList<>();
        lines.forEach(lineList::add);
        Files.delete(pathToFile);

        StringBuilder oldFileContent = new StringBuilder();
        int size = lineList.size();
        for(int i = 0; i < size; i++){
            if(i == size - 1){
                oldFileContent.append(String.format("%s", lineList.get(i)));
            }
            else {
                oldFileContent.append(String.format("%s\n", lineList.get(i)));
            }
        }

        return oldFileContent;
    }

    private static boolean filesExistsButIsEmpty(Path pathToFile){

        File file = new File(pathToFile.toFile().toURI());
        return file.length() == 0;
    }
}