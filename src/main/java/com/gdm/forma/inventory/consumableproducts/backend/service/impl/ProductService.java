package com.gdm.forma.inventory.consumableproducts.backend.service.impl;

import com.gdm.forma.inventory.consumableproducts.app.util.alert.AlertUtil;
import com.gdm.forma.inventory.consumableproducts.app.util.logger.LogType;
import com.gdm.forma.inventory.consumableproducts.app.util.logger.Logger;
import com.gdm.forma.inventory.consumableproducts.app.util.service.ExceptionService;
import com.gdm.forma.inventory.consumableproducts.backend.model.entity.Product;
import com.gdm.forma.inventory.consumableproducts.backend.repository.ProductRepository;
import com.gdm.forma.inventory.consumableproducts.backend.service.intrf.IProductService;
import javafx.scene.control.Alert;

import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductService implements IProductService {

    private ProductRepository productRepository;

    public ProductService(){

        try{
            productRepository = new ProductRepository(Persistence.createEntityManagerFactory(
                    "product"
            ));
            Logger.log(LogType.INFO, "INFO: S-a creat o sesiune nou la baza de date");
        }catch (Exception exception) {
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
        }
    }

    public ProductService(String persistenceUnitName){

        try{
            productRepository = new ProductRepository(Persistence.createEntityManagerFactory(
                    persistenceUnitName
            ));
            Logger.log(LogType.INFO, "INFO: S-a creat o sesiune nou la baza de date");
        }catch (Exception exception) {
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
        }
    }

    public void createTable(){

        productRepository.createTable();
        Logger.log(LogType.INFO, "INFO: A fost creat un tabel custom definit pentru clasa product");
    }

    public List<Product> getAll(){

        Logger.log(LogType.INFO, "INFO: Userul a returnat toate randurile din baza de date");
        return productRepository.getAll();
    }

    public Product saveOrUpdate(Product product){

        Logger.log(LogType.INFO, String.format("INFO: Un nou produs: %s, urmeaza sa fie salvat", product.getName()));
        return productRepository.saveOrUpdate(product);
    }

    public List<Product> saveOrUpdateAllArray(Product... products){

        Logger.log(LogType.INFO, String.format("INFO: Mai multe produse noi: %s, urmeaza sa fie salvate",
                getProductsNames(List.of(products))));
        return productRepository.saveOrUpdateAllArray(products);
    }

    public List<Product> saveOrUpdateAllList(List<Product> products){

        Logger.log(LogType.INFO, String.format("INFO: O lista de produse noi: %s, urmeaza sa fie salvate",
                getProductsNames(products)));
        return productRepository.saveOrUpdateAllList(products);
    }

    public Product update(Product product){

        Logger.log(LogType.INFO, String.format("INFO: Un nou produs: %s, urmeaza sa fie actualizat",
                product.getName()));
        return productRepository.update(product);
    }

    public List<Product> updateAllArray(Product... products){

        Logger.log(LogType.INFO, String.format("INFO: Mai multe produse: %s, urmeaza sa fie actualizate",
                getProductsNames(List.of(products))));
        return productRepository.updateAllArray(products);
    }

    public List<Product> updateAllList(List<Product> products){

        Logger.log(LogType.INFO, String.format("INFO: O lista de produse: %s, urmeaza sa fie actualizare",
                getProductsNames(products)));
        return productRepository.updateAllList(products);
    }

    public void delete(Product product){

        Logger.log(LogType.INFO, String.format("INFO: Produsul: %s, urmeaza sa fie sters", product));
        productRepository.delete(product.getId());}

    public void truncate(){

        Logger.log(LogType.INFO, "INFO: Toate randurile din tabel urmeaza sa fie sterse");
        productRepository.truncate(Product.class);
    }

    public void dropTable(){

        Logger.log(LogType.INFO, "INFO: Tabelul de produse a fost sters");
        productRepository.dropTable();
    }

    public void close(){

        Logger.log(LogType.INFO, "INFO: Serviciul care face legatura cu baza de date a fost inchis");
        if(productRepository != null){
            productRepository.close();
        }
    }

    private static List<String> getProductsNames(List<Product> products){
        List<String> names = new ArrayList<>();

        products.forEach(product -> names.add(product.getName()));

        return names;
    }
}