package com.gdm.forma.inventory.consumableproducts.app.util.alert;

import com.gdm.forma.inventory.consumableproducts.app.util.service.ExceptionService;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.concurrent.atomic.AtomicReference;

public class AlertUtil {

    public static Alert alert(Alert.AlertType alertType, String message){

        Alert alert = new Alert(alertType);

        setUpContextText(alert, alertType, message);

        return alert;
    }

    private static void setUpContextText(Alert alert, Alert.AlertType alertType, String message){

        if(alertType.equals(Alert.AlertType.ERROR)){
            DialogPane alertContentPane = alert.getDialogPane();

            TextArea exceptionText = setTextAreaForErrorAlert(message);

            Button showExceptionMessage = setButtonForErrorAlert(exceptionText);

            VBox alertContentContainer = new VBox();
            alertContentContainer.getChildren().addAll(showExceptionMessage,
                    exceptionText);

            alertContentPane.setContent(alertContentContainer);
            return;
        }

        Label label = new Label(message);
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);
    }

    private static TextArea setTextAreaForErrorAlert(String message){

        TextArea textArea = new TextArea();

        textArea.setText(ExceptionService.formatLogMessageAsException(message));
        textArea.setEditable(false);
        textArea.setVisible(false);

        return textArea;
    }

    private static Button setButtonForErrorAlert(TextArea textArea){

        Button button = new Button("Arata mesajul de eroare");
        AtomicReference<String> buttonText = new AtomicReference<>();

        button.setOnMouseClicked(mouseEvent -> {
            textArea.setVisible(!textArea.isVisible());
            buttonText.set(textArea.isVisible()?
                    "Ascunde mesajul de eroare": "Arata mesajul de eroare");
            button.setText(buttonText.get());
        });

        return button;
    }
}