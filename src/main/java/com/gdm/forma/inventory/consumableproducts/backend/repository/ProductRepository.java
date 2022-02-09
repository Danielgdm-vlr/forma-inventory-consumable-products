package com.gdm.forma.inventory.consumableproducts.backend.repository;

import com.gdm.forma.inventory.consumableproducts.app.util.alert.AlertUtil;
import com.gdm.forma.inventory.consumableproducts.app.util.service.ExceptionService;
import com.gdm.forma.inventory.consumableproducts.backend.model.entity.Product;
import javafx.scene.control.Alert;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;

public class ProductRepository extends GenericRepository<Product> {

    private final EntityManagerFactory entityManagerFactory;

    public ProductRepository(EntityManagerFactory entityManagerFactory){

        super(Product.class);
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public EntityManager getEntityManager(){

        try{
            return entityManagerFactory.createEntityManager();
        }catch (Exception exception){
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            return null;
        }
    }

    @Override
    public void createTable(){

        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();

            String query = "CREATE TABLE IF NOT EXISTS product(" +
                    "id bigint auto_increment NOT NULL PRIMARY KEY," +
                    "name varchar(255)," +
                    "quantity bigint," +
                    "quantityNeeded" +
                    "defaultQuantityPerWeek bigint);";
            System.out.println();

            entityManager.createNativeQuery(query).executeUpdate();
            entityManager.getTransaction().commit();
        }catch (Exception exception){
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            entityManager.getTransaction().rollback();
        }
        finally {
            entityManager.close();
        }
    }

    @Override
    public void dropTable() {

        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();

            String query = "DROP TABLE product CASCADE";
            System.out.println();

            entityManager.createNativeQuery(query).executeUpdate();
            entityManager.getTransaction().commit();
        }catch (Exception exception){
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            entityManager.getTransaction().rollback();
        }
        finally {
            entityManager.close();
        }
    }
}