package com.gdm.forma.inventory.consumableproducts.backend.repository;

import com.gdm.forma.inventory.consumableproducts.app.util.alert.AlertUtil;
import com.gdm.forma.inventory.consumableproducts.app.util.logger.LogType;
import com.gdm.forma.inventory.consumableproducts.app.util.logger.Logger;
import com.gdm.forma.inventory.consumableproducts.app.util.service.ExceptionService;
import javafx.scene.control.Alert;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class GenericRepository<T>{

    private final Class<T> entityClass;

    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public abstract EntityManager getEntityManager();

    public abstract void createTable();

    public abstract void dropTable();

    public List<T> getAll(){

        EntityManager entityManager = getEntityManager();

        try{
            CriteriaQuery<Object> criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(entityClass));
            return (List<T>) entityManager.createQuery(criteriaQuery).getResultList();
        }catch (RuntimeException exception){
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }
        return null;
    }

    public T saveOrUpdate(T entity){

        EntityManager entityManager = getEntityManager();

        try{
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        }finally {
            entityManager.close();
        }

        return !getAll().isEmpty() ? getAll().get(getAll().size() - 1) : null;
    }

    @SafeVarargs
    public final List<T> saveOrUpdateAllArray(T... entities){

        List<T> entityList = new ArrayList<>();

        for(T entity: entities){
            entityList.add(saveOrUpdate(entity));
        }

        return entityList;
    }

    public final List<T> saveOrUpdateAllList(List<T> entities){

        List<T> entityList = new ArrayList<>();

        for(T entity: entities){
            entityList.add(saveOrUpdate(entity));
        }

        return entityList;
    }

    public T update(T entity){

        EntityManager entityManager = getEntityManager();

        try{
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        }catch (RuntimeException exception){
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }

        return !getAll().isEmpty() ? getAll().get(getAll().size() - 1) : null;
    }

    @SafeVarargs
    public final List<T> updateAllArray(T... entities){

        List<T> entityList = new ArrayList<>();

        for(T entity: entities){
            entityList.add(update(entity));
        }

        return entityList;
    }

    public final List<T> updateAllList(List<T> entities){

        List<T> entityList = new ArrayList<>();

        for(T entity: entities){
            entityList.add(update(entity));
        }

        return entityList;
    }

    public void delete(long entityId){

        EntityManager em = getEntityManager();

        try{
            em.getTransaction().begin();
            em.remove(em.find(this.entityClass, entityId));
            em.getTransaction().commit();
        }catch (RuntimeException exception){
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            em.getTransaction().rollback();
        }finally {
            em.close();
        }
    }

    public void truncate(Class<T> entityClass) {

        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();

            String query = String.format("TRUNCATE TABLE %s",
                    entityClass.getSimpleName().toLowerCase(Locale.ROOT));

            entityManager.createNativeQuery(query).executeUpdate();
            entityManager.getTransaction().commit();
        }catch (Exception exception){
            AlertUtil.alert(Alert.AlertType.ERROR, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            Logger.log(LogType.EXCEPTION, Arrays.toString(
                    ExceptionService.getExceptionMessageAsStringArray(exception)));
            entityManager.getTransaction().rollback();
        }
        finally {
            entityManager.close();
        }
    }

    public void close(){

        EntityManager entityManager = this.getEntityManager();

        if(entityManager != null){
            entityManager.close();
        }
    }
}