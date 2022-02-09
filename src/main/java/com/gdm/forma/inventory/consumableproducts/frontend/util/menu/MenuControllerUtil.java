package com.gdm.forma.inventory.consumableproducts.frontend.util.menu;

import com.gdm.forma.inventory.consumableproducts.frontend.model.TableProduct;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MenuControllerUtil {

    public static final TableProduct MINIMUM_STOCK_0;
    public static final TableProduct MINIMUM_STOCK_1;
    public static final TableProduct MINIMUM_STOCK_2;

    public static final String rowStyleWhenProductStockIs0;
    public static final String rowStyleWhenProductStockIs1;
    public static final String rowStyleWhenProductStockIs2;

    static {
        MINIMUM_STOCK_0 = new TableProduct(0);
        MINIMUM_STOCK_1 = new TableProduct(1);
        MINIMUM_STOCK_2 = new TableProduct(2);

        rowStyleWhenProductStockIs0 =
                "-fx-background-color: #ff0400;\n" +
                        "-fx-font-size: 1.25em;\n" +
                        "-fx-font-weight: bold;\n" +
                        "-fx-text-fill: #131516;\n" +
                        "-fx-table-cell-border-color: #ff0400;\n" +
                        "-fx-border-color: #ff0400;\n" +
                        "-fx-cell-size: 5em;";
        rowStyleWhenProductStockIs1 =
                "-fx-background-color: #ff3633;\n" +
                        "-fx-font-size: 1.25em;\n" +
                        "-fx-font-weight: bold;\n" +"" +
                        "-fx-text-fill: #131516;\n" +
                        "-fx-table-cell-border-color: #ff3633;\n" +
                        "-fx-border-color: #ff3633;\n" +
                        "-fx-cell-size: 5em;";
        rowStyleWhenProductStockIs2 =
                "-fx-background-color: #ff6966;\n" +
                        "-fx-font-size: 1.25em;\n" +
                        "-fx-font-weight: bold;\n" +
                        "-fx-text-fill: #131516;\n" +
                        "-fx-table-cell-border-color: #ff6966;\n" +
                        "-fx-border-color: #ff6966;\n" +
                        "-fx-cell-size: 5em;";
    }

    public static void init(
            TableView<TableProduct> tableViewProduct,
            TableColumn<TableProduct, Label> columnProductName,
            TableColumn<TableProduct, Integer> columnProductStockInSalon,
            TableColumn<TableProduct, Integer> columnProductStockToBuy,
            TableColumn<TableProduct, Integer> columnProductStockPerWeek,
            TableColumn<TableProduct, Integer> columnProductActions,
            TableColumn<TableProduct, Button> columnProductActionMinus,
            TableColumn<TableProduct, Button> columnProductActionReset,
            TableColumn<TableProduct, Button> columnProductActionEdit,
            TableColumn<TableProduct, Button> columnProductActionDelete,
            List<TableProduct> tableProducts){

        setTableColumnsWidth(
                tableViewProduct,
                columnProductName,
                columnProductStockInSalon,
                columnProductStockToBuy,
                columnProductStockPerWeek,
                columnProductActions,
                columnProductActionMinus,
                columnProductActionReset,
                columnProductActionEdit,
                columnProductActionDelete);

        setTableDataFromObservableList(
                tableViewProduct,
                columnProductName,
                columnProductStockInSalon,
                columnProductStockToBuy,
                columnProductStockPerWeek,
                columnProductActionMinus,
                columnProductActionReset,
                columnProductActionEdit,
                columnProductActionDelete,
                tableProducts
        );
    }

    public static void setTableColumnsWidth(
            TableView<TableProduct> tableViewProduct,
            TableColumn<TableProduct, Label> columnProductName,
            TableColumn<TableProduct, Integer> columnProductStockInSalon,
            TableColumn<TableProduct, Integer> columnProductStockToBuy,
            TableColumn<TableProduct, Integer> columnProductStockPerWeek,
            TableColumn<TableProduct, Integer> columnProductActions,
            TableColumn<TableProduct, Button> columnProductActionMinus,
            TableColumn<TableProduct, Button> columnProductActionReset,
            TableColumn<TableProduct, Button> columnProductActionEdit,
            TableColumn<TableProduct, Button> columnProductActionDelete){

        tableViewProduct.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        columnProductName.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        columnProductStockInSalon.setMaxWidth(1f * Integer.MAX_VALUE * 6);
        columnProductStockToBuy.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        columnProductStockPerWeek.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        columnProductActions.setMaxWidth(1f * Integer.MAX_VALUE * 50);
        columnProductActionMinus.setMaxWidth(1f * Integer.MAX_VALUE * 13);
        columnProductActionReset.setMaxWidth(1f * Integer.MAX_VALUE * 13);
        columnProductActionEdit.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        columnProductActionDelete.setMaxWidth(1f * Integer.MAX_VALUE * 12);
    }

    public static void setTableDataFromObservableList(
            TableView<TableProduct> tableViewProduct,
            TableColumn<TableProduct, Label> columnProductName,
            TableColumn<TableProduct, Integer> columnProductStockInSalon,
            TableColumn<TableProduct, Integer> columnProductStockToBuy,
            TableColumn<TableProduct, Integer> columnProductStockPerWeek,
            TableColumn<TableProduct, Button> columnProductActionMinus,
            TableColumn<TableProduct, Button> columnProductActionReset,
            TableColumn<TableProduct, Button> columnProductActionEdit,
            TableColumn<TableProduct, Button> columnProductActionDelete,
            List<TableProduct> tableProducts){

        columnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnProductStockInSalon.setCellValueFactory(new PropertyValueFactory<>("stockInSalon"));
        columnProductStockToBuy.setCellValueFactory(new PropertyValueFactory<>("stockToBuy"));
        columnProductStockPerWeek.setCellValueFactory(new PropertyValueFactory<>("stockPerWeek"));
        columnProductActionMinus.setCellValueFactory(new PropertyValueFactory<>("minusStock"));
        columnProductActionReset.setCellValueFactory(new PropertyValueFactory<>("resetStock"));
        columnProductActionEdit.setCellValueFactory(new PropertyValueFactory<>("editProduct"));
        columnProductActionDelete.setCellValueFactory(new PropertyValueFactory<>("deleteProduct"));

        tableViewProduct.setItems(FXCollections.observableList(tableProducts));

        setRowStyleByItemStock(tableViewProduct);
    }

    private static void setRowStyleByItemStock(TableView<TableProduct> tableViewProduct){

        tableViewProduct.setRowFactory(tempTableRow -> {
                    TableRow<TableProduct> row = new TableRow<>();
                    row.styleProperty().bind(Bindings.when(
                                    row.itemProperty().isEqualTo(MINIMUM_STOCK_0).and(
                                            row.itemProperty().isNotNull()))
                            .then(rowStyleWhenProductStockIs0)
                            .otherwise(Bindings.when(
                                            row.itemProperty().isEqualTo(MINIMUM_STOCK_1).and(
                                                    row.itemProperty().isNotNull()))
                                    .then(rowStyleWhenProductStockIs1)
                                    .otherwise(Bindings.when(
                                                    row.itemProperty().isEqualTo(MINIMUM_STOCK_2).and(
                                                            row.itemProperty().isNotNull()))
                                            .then(rowStyleWhenProductStockIs2)
                                            .otherwise(""))));
                    return row;
                }
        );
    }
}