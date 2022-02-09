package com.gdm.forma.inventory.consumableproducts.app.util.service;

import com.gdm.forma.inventory.consumableproducts.app.util.logger.LogType;
import com.gdm.forma.inventory.consumableproducts.app.util.logger.Logger;
import com.gdm.forma.inventory.consumableproducts.backend.model.entity.Product;
import com.gdm.forma.inventory.consumableproducts.backend.service.impl.ProductService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

//class only for testing
public class DatabaseService {

    public static void initDataBaseData(ProductService productService){

        List<Product> productList = new ArrayList<>();
        List<String> productNames = getProductNamesFromFile();

        productNames
                .forEach(productName -> productList.add(new Product(
                        productName,
                        3,
                        7
                )));

        productService.saveOrUpdateAllList(productList);
    }

    private static List<String> getProductNamesFromFile(){

        Path path = null;
        try {
            path = Path.of("src/main/resources/database/products.txt");
        }catch (Exception exception){
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
        }

        List<String> productNames = new ArrayList<>();

        try {
            assert path != null;
            try(Stream<String> productsFileLines = Files.lines(path)) {
                productsFileLines.forEach(productNames::add);
            }
        } catch (Exception exception){
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
        }

        return productNames;
    }
}