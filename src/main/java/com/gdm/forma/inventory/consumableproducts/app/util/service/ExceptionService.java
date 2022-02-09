package com.gdm.forma.inventory.consumableproducts.app.util.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ExceptionService {

    public static String[] getExceptionMessageAsStringArray(Exception exception){

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);

        List<StackTraceElement> stackTraceElementList =
                new ArrayList<>(List.of(exception.getStackTrace()));

//                Arrays.stream(ioException.getStackTrace()).forEach(
//                        stackTraceElement -> {
//                            if(stackTraceElement.toString().contains("com.gdm.formainventoryapp")){
//                                stackTraceElementList.add(stackTraceElement);
//                            }
//                        }
//                );

        return new String[]{
                String.format("Exceptie detectata: %s", exception.getClass().getSimpleName()),
                String.format("Cauza: %s", exception.getMessage()),
                String.format("Stack trace: %s", stackTraceElementList)};
    }

    public static String formatLogMessageAsException(String message){

        message = message.replace("[", "");
        message = message.replace("]", "");
        String[] tempTokens = message.split(",");
        AtomicReference<String> newLogMessage = new AtomicReference<>("");

        for (String tempToken : tempTokens) {
            newLogMessage.set(String.format("%1$s%2$s\n", newLogMessage, tempToken));
        }
        message = newLogMessage.get();

        return message;
    }
}