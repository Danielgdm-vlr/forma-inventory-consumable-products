package com.gdm.forma.inventory.consumableproducts.app.main;

import com.gdm.forma.inventory.consumableproducts.app.util.alert.AlertUtil;
import com.gdm.forma.inventory.consumableproducts.app.util.logger.LogType;
import com.gdm.forma.inventory.consumableproducts.app.util.logger.Logger;
import com.gdm.forma.inventory.consumableproducts.app.util.service.DatabaseService;
import com.gdm.forma.inventory.consumableproducts.app.util.service.ExceptionService;
import com.gdm.forma.inventory.consumableproducts.app.util.service.ResourcePath;
import com.gdm.forma.inventory.consumableproducts.backend.service.impl.ProductService;
import com.gdm.forma.inventory.consumableproducts.frontend.components.menu.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Arrays;

public class MainFX extends Application {

    private FXMLLoader fxmlLoader;
    private Scene menuScene;

    private ProductService productService;

    public static void main(String[] args){

        launch(args);
    }

    @Override
    public void start(Stage menuStage){

        try {
            setUp();
            loadPage(menuStage);
            Logger.log(LogType.INFO,
                    "Applicatia a pornit fara erori");
        }catch (Exception exception){
            String[] exceptionMessage = ExceptionService.getExceptionMessageAsStringArray(exception);
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    exceptionMessage));
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    exceptionMessage));
        }
    }

    private void setUp(){

        fxmlLoader = new FXMLLoader();
        productService = new ProductService();

        DatabaseService.initDataBaseData(productService);
    }

    private void loadPage(Stage menuStage) throws Exception {

        fxmlLoader.setLocation(
                MenuController.class.getResource("menu.fxml"));
        Parent root = fxmlLoader.load();
        menuScene = new Scene(root);
        setUpController(menuStage, menuScene);
        setupStageAndScene(menuStage);
    }

    private void setUpController(Stage menuStage, Scene menuScene){

        MenuController menuController = fxmlLoader.getController();

        menuController.setMenuStage(menuStage);
        menuController.setMenuScene(menuScene);
        menuController.setProductService(productService);

        menuController.init();
    }

    private void setupStageAndScene(Stage menuStage){

        menuStage.setScene(menuScene);

        menuScene.getStylesheets().add(
                String.valueOf(MenuController.class.getResource("menu.css")));
        System.out.println(ResourcePath.PATH_TO_IMAGES);
        menuStage.getIcons().add(new Image(String.format("%s/favicon.png", ResourcePath.PATH_TO_IMAGES)));

        menuStage.setWidth(1280.0);
        menuStage.setHeight(720.0);

        menuScene.getWindow().centerOnScreen();
        menuStage.show();
    }
}