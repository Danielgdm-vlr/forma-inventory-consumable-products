package com.gdm.forma.inventory.consumableproducts.frontend.components.menu;

import com.gdm.forma.inventory.consumableproducts.app.util.alert.AlertUtil;
import com.gdm.forma.inventory.consumableproducts.app.util.logger.LogType;
import com.gdm.forma.inventory.consumableproducts.app.util.logger.Logger;
import com.gdm.forma.inventory.consumableproducts.app.util.service.ExceptionService;
import com.gdm.forma.inventory.consumableproducts.app.util.service.ResourcePath;
import com.gdm.forma.inventory.consumableproducts.backend.model.entity.Product;
import com.gdm.forma.inventory.consumableproducts.backend.service.impl.ProductService;
import com.gdm.forma.inventory.consumableproducts.frontend.model.TableProduct;
import com.gdm.forma.inventory.consumableproducts.frontend.util.menu.MenuControllerUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MenuController {

    @FXML
    private TextField searchProductInput;
    @FXML
    private Button buttonAddProduct;
    @FXML
    private TableView<TableProduct> tableViewProduct;
    @FXML
    private TableColumn<TableProduct, Label> tableColumnProductName;
    @FXML
    private TableColumn<TableProduct, Integer> columnProductStockInSalon,
            columnProductStockToBuy, columnProductStockPerWeek, columnProductActions;
    @FXML
    private TableColumn<TableProduct, Button> columnProductActionMinus, columnProductActionReset,
            columnProductActionEdit, columnProductActionDelete;

    private @Getter @Setter
    Stage menuStage;

    private @Getter @Setter
    Scene menuScene;

    private @Getter @Setter
    ProductService productService;

    private List<TableProduct> tableProducts = new ArrayList<>();

    public void init() {

        MenuControllerUtil.init(tableViewProduct, tableColumnProductName, columnProductStockInSalon,
                columnProductStockToBuy, columnProductStockPerWeek, columnProductActions,
                columnProductActionMinus, columnProductActionReset, columnProductActionEdit,
                columnProductActionDelete, tableProducts);

        menuStage.setOnCloseRequest(windowEvent -> {
            if (getButtonPressedFromAlert("Inchideti aplicatia?",
                    Alert.AlertType.CONFIRMATION).equals(ButtonType.OK)) {
                sleep(500);
            } else {
                windowEvent.consume();
            }
        });

        initTableList();
        initTableOptionsButtons();
    }

    private void sleep(int millis) {

        try {
            Thread.sleep(millis);
        } catch (Exception exception) {
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
        }
    }

    @FXML
    private void searchProduct() {

        List<TableProduct> tempProducts = new ArrayList<>();

        String inputSearchTextFromUser = searchProductInput.getText().toLowerCase();

        tableProducts.forEach(tableProduct -> {
            if (tableProduct.getName().getText().toLowerCase().contains(inputSearchTextFromUser)) {
                tempProducts.add(tableProduct);
            }
        });

        resetTableData(tempProducts);
    }

    private void initTableList() {
        List<Product> productList = getAllProducts();
        int size = productList.size();
        TableProduct tableProduct;
        Product product;

        for (int tableRow = 0; tableRow < size; tableRow++) {

            product = productList.get(tableRow);

            Button minusButton = initTableRowButton("minus", tableRow);
            Button resetButton = initTableRowButton("reset", tableRow);
            Button buttonEdit = initTableRowButton("edit", tableRow);
            Button buttonDelete = initTableRowButton("delete", tableRow);

            Label labelName = new Label();
            labelName.setText(product.getName());
            labelName.setWrapText(true);

            tableProduct = new TableProduct(
                    product.getId(),
                    labelName,
                    product.getStockInSalon(),
                    (product.getStockPerWeek() - product.getStockInSalon()),
                    product.getStockPerWeek(),
                    minusButton,
                    resetButton,
                    buttonEdit,
                    buttonDelete
            );

            tableProducts.add(tableProduct);
        }
    }

    private List<Product> getAllProducts() {

        return productService.getAll();
    }

    private Button initTableRowButton(String imageName, int tableRow) {

        Button button = new Button();

        setButtonImage(button, imageName);
        button.setOnAction(actionEvent -> setTableButtonAction(imageName, tableRow));

        return button;
    }

    private void initTableOptionsButtons() {
        setButtonImage(buttonAddProduct, "add");
        buttonAddProduct.setOnMouseClicked(mouseEvent -> addProductButtonAction());
    }

    private void setButtonImage(Button button, String imageName) {

        Image image = new Image(String.format(
                "%1$s/%2$s.png",
                ResourcePath.PATH_TO_IMAGES,
                imageName));
        ImageView imageView = new ImageView(image);

        button.setGraphic(imageView);
    }

    private void setTableButtonAction(String imageName, int tableRow) {

        switch (imageName) {
            case "minus":
                removeButtonAction(tableRow);
                break;
            case "reset":
                resetQuantityButtonAction(tableRow);
                break;
            case "edit":
                editProductButtonAction(tableRow);
                break;
            case "delete":
                deleteProductButtonAction(tableRow);
                break;
        }
    }

    private void removeButtonAction(int tableRow) {

        Product product = getProductFromTable(tableRow);

        if (product.getStockInSalon() == 0) {
            AlertUtil.alert(Alert.AlertType.WARNING,
                            String.format("Nu se poate scade stocul produsului %s, deoarece este 0!", product.getName()))
                    .showAndWait();
            return;
        }

        if (getButtonPressedFromAlert(String.format("Stergeti o unitate din produsul: %s?", product.getName()),
                Alert.AlertType.CONFIRMATION).equals(ButtonType.OK)) {
            removeUnits(1, product);
            refreshTableData(tableRow, product);
        }
    }

    private void resetQuantityButtonAction(int tableRow) {

        Product product = getProductFromTable(tableRow);

        if (product.getStockInSalon().equals(product.getStockPerWeek())) {
            AlertUtil.alert(Alert.AlertType.WARNING,
                    String.format("Produsul %s are deja stocul resetat!", product.getName())).showAndWait();
            return;
        }

        if (getButtonPressedFromAlert(String.format("Resetati stocul pentru produsul: %s?", product.getName()),
                Alert.AlertType.CONFIRMATION).equals(ButtonType.OK)) {
            resetQuantity(product);
            refreshTableData(tableRow, product);
        }
    }

    private void deleteProductButtonAction(int tableRow) {

        Product product = getProductFromTable(tableRow);

        if (getButtonPressedFromAlert(String.format("Stergeti produsul: %s?", product.getName()),
                Alert.AlertType.CONFIRMATION).equals(ButtonType.OK)) {
            deleteProduct(product);
            refreshTableData(tableRow, null);
        }
    }

    private void editProductButtonAction(int tableRow) {

        System.out.println("edit" + tableRow);
        openAddOrEditPage("edit", tableRow);
    }

    private void addProductButtonAction() {

        System.out.println("add");
        openAddOrEditPage("add", Integer.MIN_VALUE);
    }

    private void openAddOrEditPage(String page, int tableRow) {

        System.out.println("add-or-edit");
        switch (page){
            case "edit":
                System.out.println("edit page" + tableRow);
                break;
            case "add":
                System.out.println("add");
                break;
        }
    }

    private Product getProductFromTable(int tableRow) {

        TableProduct tableProduct = getTableProductFromTable(tableRow);

        Product product = new Product(
                tableProduct.getName().getText(),
                tableProduct.getStockInSalon(),
                tableProduct.getStockPerWeek()
        );
        product.setId(tableProduct.getId());

        return product;
    }

    private TableProduct getTableProductFromTable(int tableRow) {

        return tableViewProduct.getItems().get(tableRow);
    }

    private void removeUnits(int units, Product product) {

        product.setStockInSalon(product.getStockInSalon() - units);
        productService.update(product);
    }

    private void resetQuantity(Product product) {

        product.setStockInSalon(product.getStockPerWeek());
        productService.update(product);
    }

    private void deleteProduct(Product product) {

        productService.delete(product);
    }

    private ButtonType getButtonPressedFromAlert(String alertMessage, Alert.AlertType alertType) {
        Optional<ButtonType> optionButtonType =
                Objects.requireNonNull(AlertUtil.alert(alertType,
                        alertMessage)).showAndWait();
        AtomicReference<ButtonType> buttonType = new AtomicReference<>();
        optionButtonType.ifPresent(buttonType::set);
        return buttonType.get();
    }

    private void refreshTableData(int tableRow, Product product) {

        if (product == null) {
            tableViewProduct.getItems().remove(tableRow);
            return;
        }

        TableProduct tableProduct = getTableProductFromTable(tableRow);

        tableProduct.getName().setText(product.getName());
        tableProduct.setStockInSalon(product.getStockInSalon());
        tableProduct.setStockPerWeek(product.getStockPerWeek());
        tableProduct.setStockToBuy(tableProduct.getStockPerWeek() - tableProduct.getStockInSalon());

        tableViewProduct.getItems().set(tableRow, tableProduct);
    }

    private void resetTableData(List<TableProduct> tableProducts) {

        tableViewProduct.getItems().removeAll();
        tableViewProduct.setItems(FXCollections.observableList(tableProducts));
    }
}